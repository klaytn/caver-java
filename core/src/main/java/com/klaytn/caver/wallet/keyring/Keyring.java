package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.Wallet;
import com.klaytn.caver.wallet.WalletFile;
import jnr.posix.Crypt;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Keyring {
    String address;
    List<PrivateKey[]> keys;

    private Keyring(String address, List<PrivateKey[]> keys) {
        if(!Utils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid Address");
        }

        setAddress(address);
        this.keys = keys;
    }

    public void setAddress(String address) {
        this.address = Numeric.prependHexPrefix(address);
    }

    public static Keyring generate() {
        return Keyring.generate(null);
    }

    public static Keyring generate(String entropy) {
        PrivateKey privateKey = PrivateKey.generate(entropy);
        String address = privateKey.getDerivedAddress();

        return createWithSingleKey(address, privateKey.getPrivateKey());
    }

    public static Keyring create(String address, String key) {
        return createWithSingleKey(address, key);
    }

    public static Keyring create(String address, String[] keys) {
        return createWithMultipleKey(address, keys);
    }

    public static Keyring create(String address, List<String[]> keys) {
        return createWithRoleBasedKey(address, keys);
    }

    public static Keyring createFromPrivateKey(String key) {
        if(Utils.isKlaytnWalletKeyFormat(key)) {
            return Keyring.createFromKlaytnWalletKey(key);
        }

        PrivateKey privateKey = new PrivateKey(key);
        String address = privateKey.getDerivedAddress();

        PrivateKey[][] privateKeys = {{privateKey}, {}, {}};
        return new Keyring(address, Arrays.asList(privateKeys));
    }

    public static Keyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        if(!Utils.isKlaytnWalletKeyFormat(klaytnWalletKey)) {
            throw new IllegalArgumentException("Invalid Klaytn wallet key.");
        }

        String[] parsedKey = Utils.parseKlaytnWalletKey(klaytnWalletKey);

        PrivateKey privateKey = new PrivateKey(parsedKey[0]);
        String address = Numeric.prependHexPrefix(parsedKey[2]);

        PrivateKey[][] privateKeys = {{privateKey}, {}, {}};
        return new Keyring(address, Arrays.asList(privateKeys));
    }

    public static Keyring createWithSingleKey(String address, String key) {
        if(Utils.isKlaytnWalletKeyFormat(key)) {
            throw new IllegalArgumentException("Invalid format of parameter. Use 'fromKlaytnWalletKey' to create Keyring from KlaytnWalletKey.");
        }

        PrivateKey privateKey = new PrivateKey(key);
        PrivateKey[][] privateKeys = {{privateKey}, {}, {}};
        return new Keyring(address, Arrays.asList(privateKeys));
    }

    public static Keyring createWithMultipleKey(String address, String[] multipleKey) {
        if(multipleKey.length > WeightedMultiSigOptions.MAX_COUNT_WEIGHTED_PUBLIC_KEY) {
            throw new IllegalArgumentException("MultipleKey has up to 10.");
        }

        PrivateKey[][] privateKeys = {{}, {}, {}};
        privateKeys[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()] = Arrays.stream(multipleKey)
                                                                                .map(PrivateKey::new)
                                                                                .toArray(PrivateKey[]::new);

        return new Keyring(address, Arrays.asList(privateKeys));
    }

    public static Keyring createWithRoleBasedKey(String address, List<String[]> roleBasedKey) {
        if(roleBasedKey.size() > AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("RoleBasedKey component must have 3.");
        }

        boolean isExceedKeyCount = roleBasedKey.stream().anyMatch(element -> {
            return element.length > WeightedMultiSigOptions.MAX_COUNT_WEIGHTED_PUBLIC_KEY;
        });

        if(isExceedKeyCount) {
            throw new IllegalArgumentException("The keys in RoleBasedKey component has up to 10.");
        }

        List<PrivateKey[]> privateKeys = roleBasedKey.stream().map(element -> {
            return Arrays.stream(element)
                    .map(PrivateKey::new)
                    .toArray(PrivateKey[]::new);

        }).collect(Collectors.toCollection(ArrayList::new));

        return new Keyring(address, privateKeys);
    }

    public static KeyStore encrypt(String key, String password, KeyStoreOption option) throws CipherException{
        Keyring keyring = null;
        if(option.getAddress() != null) {
            if(Utils.isKlaytnWalletKeyFormat(key)) {
                keyring = Keyring.createFromKlaytnWalletKey(key);
                if (!keyring.address.equals(option.address)) {
                    throw new RuntimeException("The address defined in options does not match the address of KlaytnWalletKey");
                }

                keyring = Keyring.createFromKlaytnWalletKey(key);
            } else {
                keyring = Keyring.createWithSingleKey(option.getAddress(), key);
            }
        } else {
            keyring = Keyring.createFromPrivateKey(key);
        }
        return keyring.encrypt(password, option);
    }

    public static KeyStore encrypt(String[] key, String password, KeyStoreOption option) throws CipherException{
        if(option.getAddress() == null) {
            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt multiple keys.");
        }

        Keyring keyring = Keyring.createWithMultipleKey(option.address, key);

        return keyring.encrypt(password, option);
    }

    public static KeyStore encrypt(List<String[]> key, String password, KeyStoreOption option) throws CipherException{
        if(option.getAddress() == null) {
            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt roleBased keys.");
        }

        Keyring keyring = Keyring.createWithRoleBasedKey(option.address, key);

        return keyring.encrypt(password, option);
    }

    public static KeyStore encrypt(Keyring keyring, String password, KeyStoreOption option) throws CipherException {
        return keyring.encrypt(password, option);
    }

    public static KeyStore encryptV3(String key, String password, KeyStoreOption option) throws CipherException {
        Keyring keyring = null;
        if(option.getAddress() != null) {
            if(Utils.isKlaytnWalletKeyFormat(key)) {
                keyring = Keyring.createFromKlaytnWalletKey(key);
                if (!keyring.address.equals(option.address)) {
                    throw new RuntimeException("The address defined in options does not match the address of KlaytnWalletKey");
                }

                keyring = Keyring.createFromKlaytnWalletKey(key);
            } else {
                keyring = Keyring.createWithSingleKey(option.getAddress(), key);
            }
        } else {
            keyring = Keyring.createFromPrivateKey(key);
        }
        return keyring.encryptV3(password, option);
    }

    public static KeyStore encryptV3(Keyring keyring, String password, KeyStoreOption options) throws CipherException {
        return keyring.encryptV3(password, options);
    }

    public static Keyring decrypt(KeyStore keystore, String password) throws CipherException{
        if(keystore.getVersion() == KeyStore.KEY_VERSION_V3 && keystore.getCrypto() == null) {
            throw new IllegalArgumentException("Invalid keystore V3 format: 'crypto' is not defined.");
        } else if(keystore.getVersion() == KeyStore.KEY_VERSION_V4 && keystore.getKeyring() == null) {
            throw new IllegalArgumentException("Invalid keystore V4 format: 'keyring' is not defined.");
        }

        if(keystore.getCrypto() != null) {
            if(keystore.getKeyring() != null) {
                throw new IllegalArgumentException("Invalid key store format: 'crypto' and 'keyring' cannot be defined together.");
            }
        }

        if(keystore.getVersion() == KeyStore.KEY_VERSION_V3) {
            KeyStore.Crypto crypto = keystore.getCrypto();
            String privateKey = decryptKeyFromKeyStore(crypto, password);
            return Keyring.create(keystore.getAddress(), privateKey);
        }

        List keyring = keystore.getKeyring();
        List<String[]> privateKeyList = new ArrayList<>();
        if(keyring.get(0) instanceof KeyStore.Crypto) {
            String[] privateKeyArr = new String[keyring.size()];
            for(int i=0; i<keyring.size(); i++) {
                KeyStore.Crypto crypto = (KeyStore.Crypto)keyring.get(i);
                String privateKey = decryptKeyFromKeyStore(crypto, password);
                privateKeyArr[i] = privateKey;
            }
            privateKeyList.add(privateKeyArr);
        } else {
            for(List<KeyStore.Crypto> multiKeying : (List<List<KeyStore.Crypto>>)keyring) {
                String[] privateKeyArr = new String[multiKeying.size()];
                for(int i=0; i<multiKeying.size(); i++) {
                    KeyStore.Crypto crypto = multiKeying.get(i);
                    String privateKey = decryptKeyFromKeyStore(crypto, password);
                    privateKeyArr[i] = privateKey;
                }
                privateKeyList.add(privateKeyArr);
            }
        }

        return Keyring.createWithRoleBasedKey(keystore.getAddress(), privateKeyList);
    }

    public static String recover(MessageSigned messageSigned) throws SignatureException {
        KlaySignatureData klaySignatureData = messageSigned.getSignatureData();
        return recover(messageSigned.getMessage(), klaySignatureData);
    }

    public static String recover(String message, KlaySignatureData signatureData) throws SignatureException {
        return recover(message, signatureData, false);
    }

    public static String recover(String message, KlaySignatureData signatureData, boolean isPrefixed) throws SignatureException {
        Sign.SignatureData data = new Sign.SignatureData(signatureData.getV()[0], signatureData.getR(), signatureData.getS());
        if(!isPrefixed) {
            message = Utils.addPrefixSignMessage(message);
        }

        BigInteger publicKey = Sign.signedMessageToKey(Numeric.hexStringToByteArray(message), data);
        return Numeric.prependHexPrefix(Keys.getAddress(publicKey));
    }

    public List<String[]> getPublicKey() {
        List<String[]> publicKeyList = this.keys.stream().map(element -> {
            return Arrays.stream(element)
                    .map(privateKey -> privateKey.getPublicKey(false))
                    .toArray(String[]::new);
        }).collect(Collectors.toCollection(ArrayList::new));

        return publicKeyList;
    }

    public Keyring copy() {
        return new Keyring(this.address, this.keys);
    }

    public KlaySignatureData signWithKey(String sigHash, int chainId, int roleIndex, int keyIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);
        if(keyIndex < 0) throw new IllegalArgumentException("keyIndex cannot have negative value.");
        if(keyIndex >= groupKeyArr.length) throw new IllegalArgumentException("keyIndex value must be less than the length of key array");

        return groupKeyArr[keyIndex].sign(sigHash, chainId);
    }

    public List<KlaySignatureData> signWithKeys(String sigHash, int chainId, int roleIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);

        return Arrays.stream(groupKeyArr)
                .map(privateKey -> {
                    return privateKey.sign(sigHash, chainId);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public MessageSigned signMessage(String message, int roleIndex, int keyIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);
        if(keyIndex < 0) throw new IllegalArgumentException("keyIndex cannot have negative value.");
        if(keyIndex >= groupKeyArr.length) throw new IllegalArgumentException("keyIndex value must be less than the length of key array");

        String messageHash = Hash.sha3(Utils.addPrefixSignMessage(message));
        KlaySignatureData signatureData = groupKeyArr[keyIndex].signMessage(messageHash);
        return new MessageSigned(messageHash, signatureData, message);
    }

    public PrivateKey[] getKeyByRole(int roleIndex) {
        if(roleIndex >= AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("Invalid role index");
        }

        PrivateKey[] groupKeyArr = this.keys.get(roleIndex);

        if(groupKeyArr.length == 0 && roleIndex > AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()) {
            groupKeyArr = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            if(groupKeyArr.length == 0) {
                throw new RuntimeException("The key data with specified roleIndex does not exist. The default key in TransactionRole is also empty.");
            }
        }

        return groupKeyArr;
    }

    public String getKlaytnWalletKey() {
        String errorMessage = "The keyring cannot be exported in KlaytnWalletKey format. Use caver.wallet.keyring.encrypt or keyring.encrypt.";

        PrivateKey[] txRoleGroupKeyArr = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        if(txRoleGroupKeyArr.length != 1) {
            throw new RuntimeException(errorMessage);
        }

        for(int i = AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(); i<AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT; i++) {
            if(this.keys.get(i).length > 0) {
                throw new RuntimeException(errorMessage);
            }
        }

        String address = Numeric.prependHexPrefix(this.address);
        String privateKeyStr = Numeric.prependHexPrefix(txRoleGroupKeyArr[0].getPrivateKey());

        return privateKeyStr + "0x00" + address;
    }

    public Account toAccount() {
        boolean isExistsOtherGroupKeys = this.keys.stream()
                .skip(1)
                .anyMatch(groupKeyArr -> groupKeyArr.length != 0);

        if(isExistsOtherGroupKeys) {
            throw new RuntimeException("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");
        }

        PrivateKey[] txGroupKeyArr = getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        if(txGroupKeyArr.length != 1) {
            throw new RuntimeException("Failed to create Account instance: There are two or more keys in RoleTransaction Key array.");
        }

        String publicKey = this.getPublicKey().get(0)[0];
        return Account.createWithAccountKeyPublic(this.address, publicKey);
    }

    public Account toAccount(WeightedMultiSigOptions options) {
        boolean isExistsOtherGroupKeys = this.keys.stream()
                .skip(1)
                .anyMatch(groupKeyArr -> groupKeyArr.length != 0);

        if(isExistsOtherGroupKeys) {
            throw new RuntimeException("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");
        }

        PrivateKey[] txGroupKeyArr = getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        if(txGroupKeyArr.length == 0) {
            throw new RuntimeException("Failed to create Account instance: There must be one or more keys in RoleTransaction Key array.");
        }

        if(txGroupKeyArr.length != options.getWeights().size()) {
            throw new RuntimeException("Failed to create Account instance: The number of keys and the number of elements in the Weights array should be the same.");
        }

        String address = this.address;
        String[] publicKeyArr = this.getPublicKey().get(0);
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeyArr, options);
    }

    public Account toAccount(List<WeightedMultiSigOptions> options) {
        return Account.createWithAccountKeyRoleBased(this.address, this.getPublicKey(), options);
    }

    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<List<KeyStore.Crypto>> cryptosList = new ArrayList<>();

        boolean isRoleBased = false;
        for(int i=0; i<AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT; i++) {
            PrivateKey[] privateKeys = this.getKeys().get(i);
            List<KeyStore.Crypto> list = generateKeyStoreCrypto(privateKeys, password, options);
            cryptosList.add(list);

            if(i > AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex() && privateKeys.length > 0) {
                isRoleBased = true;
            }
        }

        List keyList = isRoleBased? cryptosList : cryptosList.get(0);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.getAddress());
        keyStore.setKeyring(keyList);
        keyStore.setVersion(KeyStore.KEY_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());

        return keyStore;
    }

    public KeyStore encryptV3(String password, KeyStoreOption options) throws CipherException {
        String notAvailableError = "This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)";
        if(getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()).length > 1) {
            throw new RuntimeException(notAvailableError);
        }

        boolean isRoleBasedKey = this.keys.stream().skip(1).anyMatch(privateKeys -> privateKeys.length > 0);
        if (isRoleBasedKey) {
            throw new RuntimeException(notAvailableError);
        }

        List<KeyStore.Crypto> crypto = generateKeyStoreCrypto(this.getKeys().get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()), password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.getAddress());
        keyStore.setVersion(KeyStore.KEY_VERSION_V3);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setCrypto(crypto.get(0));

        return keyStore;
    }

    public boolean isDecoupled() {
        boolean isMultiple = this.keys.stream().anyMatch(privateKeys -> privateKeys.length > 1);
        if(isMultiple) return true;

        PrivateKey privateKey = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())[0];
        String derivedKey = privateKey.getDerivedAddress();

        return !(derivedKey.toLowerCase().equals(this.address.toLowerCase()));
    }

    public String getAddress() {
        return address;
    }

    public List<PrivateKey[]> getKeys() {
        return keys;
    }

    private static List<KeyStore.Crypto> generateKeyStoreCrypto(PrivateKey[] privateKeys, String password, KeyStoreOption option) throws CipherException {
        final int PRIVATE_KEY_SIZE = 32;
        final String CIPHER_METHOD = "aes-128-ctr";

        List<KeyStore.Crypto> cryptoList = new ArrayList<>();

        String kdfName = "";

        byte[] salt = (option.kdfParams.getSalt() != null) ? Numeric.hexStringToByteArray(option.kdfParams.getSalt()) : Utils.generateRandomBytes(32);

        byte[] iv;
        if(option.cipherParams.getIv() == null) {
            iv = Utils.generateRandomBytes(16);
            option.getCipherParams().setIv(Numeric.toHexStringNoPrefix(iv));
        } else {
            iv = Numeric.hexStringToByteArray(option.cipherParams.getIv());
        }

        for(int i=0; i < privateKeys.length; i++) {
            byte[] derivedKey;

            if(option.kdfParams instanceof KeyStore.ScryptKdfParams) {
                kdfName = KeyStore.ScryptKdfParams.getName();
                derivedKey = generateDerivedScryptKey(
                        password.getBytes(UTF_8),
                        salt,
                        ((KeyStore.ScryptKdfParams) option.kdfParams).getN(),
                        ((KeyStore.ScryptKdfParams) option.kdfParams).getR(),
                        ((KeyStore.ScryptKdfParams) option.kdfParams).getP(),
                        option.kdfParams.getDklen());

                ((KeyStore.ScryptKdfParams) option.kdfParams).setSalt(Numeric.toHexStringNoPrefix(salt));
            } else if(option.kdfParams instanceof KeyStore.Pbkdf2KdfParams) {
                kdfName = KeyStore.Pbkdf2KdfParams.getName();

                derivedKey = generatePbkdf2DerivedKey(
                        password.getBytes(UTF_8),
                        salt,
                        ((KeyStore.Pbkdf2KdfParams) option.kdfParams).getC(),
                        ((KeyStore.Pbkdf2KdfParams) option.kdfParams).getPrf()
                );

                ((KeyStore.Pbkdf2KdfParams) option.kdfParams).setSalt(Numeric.toHexStringNoPrefix(salt));
            } else {
                throw new RuntimeException("Unsupported KDF");
            }

            byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
            byte[] privateKeyBytes =
                    Numeric.toBytesPadded(Numeric.toBigInt(privateKeys[i].getPrivateKey()), PRIVATE_KEY_SIZE);

            byte[] cipherText = performCipherOperation(
                    Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes);

            byte[] mac = generateMac(derivedKey, cipherText);

            KeyStore.Crypto crypto = new KeyStore.Crypto();
            crypto.setCipher(CIPHER_METHOD);
            crypto.setCiphertext(Numeric.toHexStringNoPrefix(cipherText));
            crypto.setCipherparams(option.getCipherParams());

            crypto.setKdf(kdfName);
            crypto.setKdfparams(option.kdfParams);
            crypto.setMac(Numeric.toHexStringNoPrefix(mac));

            cryptoList.add(crypto);
        }
        return cryptoList;
    }

    private static String decryptKeyFromKeyStore(KeyStore.Crypto crypto, String password) throws CipherException {
        byte[] mac = Numeric.hexStringToByteArray(crypto.getMac());
        byte[] iv = Numeric.hexStringToByteArray(crypto.getCipherparams().getIv());
        byte[] cipherText = Numeric.hexStringToByteArray(crypto.getCiphertext());

        byte[] derivedKey;

        KeyStore.KdfParams kdfParams = crypto.getKdfparams();
        if (kdfParams instanceof KeyStore.ScryptKdfParams) {
            KeyStore.ScryptKdfParams scryptKdfParams =
                    (KeyStore.ScryptKdfParams) crypto.getKdfparams();
            int dklen = scryptKdfParams.getDklen();
            int n = scryptKdfParams.getN();
            int p = scryptKdfParams.getP();
            int r = scryptKdfParams.getR();
            byte[] salt = Numeric.hexStringToByteArray(scryptKdfParams.getSalt());
            derivedKey = generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, r, p, dklen);
        } else if (kdfParams instanceof KeyStore.Pbkdf2KdfParams) {
            KeyStore.Pbkdf2KdfParams aes128CtrKdfParams =
                    (KeyStore.Pbkdf2KdfParams) crypto.getKdfparams();
            int c = aes128CtrKdfParams.getC();
            String prf = aes128CtrKdfParams.getPrf();
            byte[] salt = Numeric.hexStringToByteArray(aes128CtrKdfParams.getSalt());

            derivedKey = generatePbkdf2DerivedKey(password.getBytes(UTF_8), salt, c, prf);
        } else {
            throw new CipherException("Unable to deserialize params: " + crypto.getKdf());
        }

        byte[] derivedMac = generateMac(derivedKey, cipherText);

        if (!Arrays.equals(derivedMac, mac)) {
            throw new CipherException("Invalid password provided");
        }

        byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        byte[] privateKey = performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText);

        return Numeric.toHexString(privateKey);
    }



    private static byte[] generateDerivedScryptKey(
            byte[] password, byte[] salt, int n, int r, int p, int dkLen) throws CipherException {
        return SCrypt.generate(password, salt, n, r, p, dkLen);
    }

    private static byte[] generatePbkdf2DerivedKey(
            byte[] password, byte[] salt, int c, String prf) throws CipherException {

        if (!prf.equals("hmac-sha256")) {
            throw new CipherException("Unsupported prf:" + prf);
        }

        // Java 8 supports this, but you have to convert the password to a character array, see
        // http://stackoverflow.com/a/27928435/3211687

        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(password, salt, c);
        return ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
    }

    private static byte[] performCipherOperation(
            int mode, byte[] iv, byte[] encryptKey, byte[] text) throws CipherException {

        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(mode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(text);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new CipherException("Error performing cipher operation", e);
        }
    }

    private static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
        byte[] result = new byte[16 + cipherText.length];

        System.arraycopy(derivedKey, 16, result, 0, 16);
        System.arraycopy(cipherText, 0, result, 16, cipherText.length);

        return Hash.sha3(result);
    }


}
