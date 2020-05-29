package com.klaytn.caver.wallet.keyring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Representing a Keyring which includes 'address' and 'private keys' by roles.
 */
public class Keyring {
    String address;
    List<PrivateKey[]> keys;

    /**
     * Creates a Keyring instance.
     * @param address The address of Keyring instance.
     * @param keys The key list of keyring instance.
     */
    private Keyring(String address, List<PrivateKey[]> keys) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("Invalid Address");
        }

        setAddress(address);
        this.keys = keys;
    }

    /**
     * Generates a keyring instance.
     * @return Keyring
     */
    public static Keyring generate() {
        return Keyring.generate(null);
    }

    /**
     * Generates an keyring instance with entropy.
     * @param entropy A random string to create keyring.
     * @return Keyring
     */
    public static Keyring generate(String entropy) {
        PrivateKey privateKey = PrivateKey.generate(entropy);
        String address = privateKey.getDerivedAddress();

        return createWithSingleKey(address, privateKey.getPrivateKey());
    }

    /**
     * Creates a single type of keyring instance.
     * @param address The address of keyring.
     * @param key The key of keyring.
     * @return Keyring
     */
    public static Keyring create(String address, String key) {
        return createWithSingleKey(address, key);
    }

    /**
     * Creates a multiple type of keyring instance.
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return Keyring
     */
    public static Keyring create(String address, String[] keys) {
        return createWithMultipleKey(address, keys);
    }

    /**
     * Creates a roleBased type of keyring instance.
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return Keyring
     */
    public static Keyring create(String address, List<String[]> keys) {
        return createWithRoleBasedKey(address, keys);
    }

    /**
     * Creates a keyring instance with private key.
     * @param key A private key string.
     * @return Keyring
     */
    public static Keyring createFromPrivateKey(String key) {
        if(Utils.isKlaytnWalletKey(key)) {
            return Keyring.createFromKlaytnWalletKey(key);
        }

        PrivateKey privateKey = new PrivateKey(key);
        String address = privateKey.getDerivedAddress();

        PrivateKey[][] privateKeys = {{privateKey}, {}, {}};
        return new Keyring(address, Arrays.asList(privateKeys));
    }

    /**
     * Creates a keyring instance from KlaytnWalletKey string.
     * @param klaytnWalletKey A key string in KlaytnWalletKey format.
     * @return Keyring
     */
    public static Keyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        String[] parsedKey = Utils.parseKlaytnWalletKey(klaytnWalletKey);

        String privateKey = parsedKey[0];
        String address = parsedKey[2];

        return Keyring.createWithSingleKey(address, privateKey);
    }

    /**
     * Creates a single type of keyring instance from address and private key string.
     * @param address An address of keyring.
     * @param key A private key string.
     * @return Keyring
     */
    public static Keyring createWithSingleKey(String address, String key) {
        if(Utils.isKlaytnWalletKey(key)) {
            throw new IllegalArgumentException("Invalid format of parameter. Use 'fromKlaytnWalletKey' to create Keyring from KlaytnWalletKey.");
        }

        PrivateKey privateKey = new PrivateKey(key);
        PrivateKey[][] privateKeys = {{privateKey}, {}, {}};
        return new Keyring(address, Arrays.asList(privateKeys));
    }

    /**
     * Creates a multiple type of keyring instance from address and private key strings.
     * @param address An address of keyring.
     * @param multipleKey An array of private key strings.
     * @return Keyring
     */
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

    /**
     * Create a roleBased type of keyring instance from address and private key strings.
     * @param address An address of keyring.
     * @param roleBasedKey A List of private key strings.
     * @return Keyring
     */
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

    /**
     * Encrypts a key string (normal private key or KlaytnWalletKey format) and returns a keystore object. (according to KeyStoreV4)
     * @param key A private key string.
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param option The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encrypt(String key, String password, KeyStoreOption option) throws CipherException{
        Keyring keyring = null;
        if(option.getAddress() != null) {
            if(Utils.isKlaytnWalletKey(key)) {
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

    /**
     * Encrypts an multiple private key strings and returns a keystore object. (according to KeyStoreV4)
     * @param key A private key strings.
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param option The options to use when encrypt a keyring. To encrypt multiple private key strings, address should be defined in option.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encrypt(String[] key, String password, KeyStoreOption option) throws CipherException{
        if(option.getAddress() == null) {
            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt multiple keys.");
        }

        Keyring keyring = Keyring.createWithMultipleKey(option.address, key);

        return keyring.encrypt(password, option);
    }

    /**
     * Encrypts a role based private key strings and returns a keystore object. (according to KeyStoreV4)
     * @param key A List of private key strings
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param option The options to use when encrypt a keyring. To encrypt multiple private key strings, address should be defined in option.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encrypt(List<String[]> key, String password, KeyStoreOption option) throws CipherException{
        if(option.getAddress() == null) {
            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt roleBased keys.");
        }

        Keyring keyring = Keyring.createWithRoleBasedKey(option.address, key);

        return keyring.encrypt(password, option);
    }

    /**
     * Encrypts a private key string and returns a keystore object. (according to KeyStoreV4)
     * @param keyring A Keyring instance
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param option The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encrypt(Keyring keyring, String password, KeyStoreOption option) throws CipherException {
        return keyring.encrypt(password, option);
    }

    /**
     * Encrypts a keyring instance and returns a keystore object. (according to KeyStoreV3)
     * @param key A private key string.
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param option The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encryptV3(String key, String password, KeyStoreOption option) throws CipherException {
        Keyring keyring = null;
        if(option.getAddress() != null) {
            if(Utils.isKlaytnWalletKey(key)) {
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

    /**
     * Encrypts a keyring instance and returns a keystore object. (according to KeyStoreV3)
     * @param keyring A Keyring instance.
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static KeyStore encryptV3(Keyring keyring, String password, KeyStoreOption options) throws CipherException {
        return keyring.encryptV3(password, options);
    }

    /**
     * Decrypts a KeyStore json string and returns a keyring instance.
     * @param keyStore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return Keyring
     * @throws CipherException
     * @throws IOException
     */
    public static Keyring decrypt(String keyStore, String password) throws CipherException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        KeyStore file = mapper.readValue(keyStore, KeyStore.class);

        return decrypt(file, password);
    }

    /**
     * Decrypts a keystore v3 or v4 and returns a keyring instance.
     * @param keystore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return Keyring
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static Keyring decrypt(KeyStore keystore, String password) throws CipherException{
        if(keystore.getVersion() == KeyStore.KEY_STORE_VERSION_V3 && keystore.getCrypto() == null) {
            throw new IllegalArgumentException("Invalid keystore V3 format: 'crypto' is not defined.");
        } else if(keystore.getVersion() == KeyStore.KEY_STORE_VERSION_V4 && keystore.getKeyring() == null) {
            throw new IllegalArgumentException("Invalid keystore V4 format: 'keyring' is not defined.");
        }

        if(keystore.getCrypto() != null) {
            if(keystore.getKeyring() != null) {
                throw new IllegalArgumentException("Invalid key store format: 'crypto' and 'keyring' cannot be defined together.");
            }
        }

        if(keystore.getVersion() == KeyStore.KEY_STORE_VERSION_V3) {
            KeyStore.Crypto crypto = keystore.getCrypto();
            String privateKey = KeyStore.Crypto.decryptCrypto(crypto, password);
            return Keyring.create(keystore.getAddress(), privateKey);
        }

        List keyring = keystore.getKeyring();
        List<String[]> privateKeyList = new ArrayList<>();
        if(keyring.get(0) instanceof KeyStore.Crypto) {
            String[] privateKeyArr = new String[keyring.size()];
            for(int i=0; i<keyring.size(); i++) {
                KeyStore.Crypto crypto = (KeyStore.Crypto)keyring.get(i);
                String privateKey = KeyStore.Crypto.decryptCrypto(crypto, password);
                privateKeyArr[i] = privateKey;
            }
            privateKeyList.add(privateKeyArr);
        } else {
            for(List<KeyStore.Crypto> multiKeying : (List<List<KeyStore.Crypto>>)keyring) {
                String[] privateKeyArr = new String[multiKeying.size()];
                for(int i=0; i<multiKeying.size(); i++) {
                    KeyStore.Crypto crypto = multiKeying.get(i);
                    String privateKey = KeyStore.Crypto.decryptCrypto(crypto, password);
                    privateKeyArr[i] = privateKey;
                }
                privateKeyList.add(privateKeyArr);
            }
        }

        return Keyring.createWithRoleBasedKey(keystore.getAddress(), privateKeyList);
    }

    /**
     * Recovers the address that was used to sign the given data.
     * @param messageSigned A MessageSigned object
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(MessageSigned messageSigned) throws SignatureException {
        KlaySignatureData klaySignatureData = messageSigned.getSignatureData();
        return recover(messageSigned.getMessageHash(), klaySignatureData, true);
    }

    /**
     * Recovers the address that was used to sign the given data.
     * This function automatically creates a message hash by appending a Klaytn sign prefix to the message.
     * @param message A plain message when using signed.
     * @param signatureData The signature values in KlaySignatureData
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, KlaySignatureData signatureData) throws SignatureException {
        return recover(message, signatureData, false);
    }


    /**
     * Recovers the address that was used to sign the given data.
     * @param message A plain message or hashed message.
     * @param signatureData The signature values in KlaySignatureData
     * @param isPrefixed If true, the message param already hashed by appending a Klaytn sign prefix to the message.
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, KlaySignatureData signatureData, boolean isPrefixed) throws SignatureException {
        Sign.SignatureData signData = new Sign.SignatureData(signatureData.getV()[0], signatureData.getR(), signatureData.getS());
        String messageHash = message;
        if(!isPrefixed) {
            messageHash = Utils.hashMessage(message);
        }

        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();

        if(r == null || r.length != 32) {
            throw new IllegalArgumentException("r must be 32 bytes");
        }
        if(s == null || s.length != 32) {
            throw new IllegalArgumentException("s must be 32 bytes");
        }

        int header = signData.getV() & 0xFF;
        // The header byte: 0x1B = first key with even y, 0x1C = first key with odd y,
        //                  0x1D = second key with even y, 0x1E = second key with odd y
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }

        ECDSASignature sig = new ECDSASignature(
                new BigInteger(1, signatureData.getR()),
                new BigInteger(1, signatureData.getS()));

        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(recId, sig, Numeric.hexStringToByteArray(messageHash));
        if (key == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return Numeric.prependHexPrefix(Keys.getAddress(key));
    }

    /**
     * Returns public key strings in format of role-based.
     * @return A list of public keys
     */
    public List<String[]> getPublicKey() {
        List<String[]> publicKeyList = this.keys.stream().map(element -> {
            return Arrays.stream(element)
                    .map(privateKey -> privateKey.getPublicKey(false))
                    .toArray(String[]::new);
        }).collect(Collectors.toCollection(ArrayList::new));

        return publicKeyList;
    }

    /**
     * Returns a copied keyring instance.
     * @return Keyring
     */
    public Keyring copy() {
        return new Keyring(this.address, this.keys);
    }

    /**
     * Signs with transactionHash with key and returns signature.
     * @param sigHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param roleIndex A number indicating the role of the key.
     * @param keyIndex The index of the key to be used.
     * @return KlaySignatureData
     */
    public KlaySignatureData signWithKey(String sigHash, int chainId, int roleIndex, int keyIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);
        if(keyIndex < 0) throw new IllegalArgumentException("keyIndex cannot have negative value.");
        if(keyIndex >= groupKeyArr.length) throw new IllegalArgumentException("keyIndex value must be less than the length of key array");

        return groupKeyArr[keyIndex].sign(sigHash, chainId);
    }

    /**
     * Signs with transactionHash with multiple keys and returns signature.
     * @param sigHash The hash of transaction.
     * @param chainId The chainId specific to the network
     * @param roleIndex A number indicating the role of the key.
     * @return KlaySignatureData
     */
    public List<KlaySignatureData> signWithKeys(String sigHash, int chainId, int roleIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);

        return Arrays.stream(groupKeyArr)
                .map(privateKey -> {
                    return privateKey.sign(sigHash, chainId);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Signs with hashed data and returns MessageSigned Object.
     * The role index and key index set 0.
     * @param message The data string to sign
     * @return MessageSigned
     */
    public MessageSigned signMessage(String message) {
        int roleIndex = AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex();
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);

        if(groupKeyArr.length == 0) {
            throw new RuntimeException("Default Key does not have enough keys to sign.");
        }

        return signMessage(message, 0, 0);
    }

    /**
     * Signs with hashed data and returns MessageSigned Object.
     * @param message The data string to sign.
     * @param roleIndex A number indicating the role of the key.
     * @param keyIndex The index of the key to be used.
     * @return MessageSigned
     */
    public MessageSigned signMessage(String message, int roleIndex, int keyIndex) {
        PrivateKey[] groupKeyArr = getKeyByRole(roleIndex);
        if(keyIndex < 0) throw new IllegalArgumentException("keyIndex cannot have negative value.");
        if(keyIndex >= groupKeyArr.length) throw new IllegalArgumentException("keyIndex value must be less than the length of key array");

        String messageHash = Utils.hashMessage(message);
        KlaySignatureData signatureData = groupKeyArr[keyIndex].signMessage(messageHash);
        return new MessageSigned(messageHash, signatureData, message);
    }

    /**
     * Returns keys corresponding to the role index.
     * If the keys corresponding to the role index is empty, the default key is returned.(Role index 0)
     * @param roleIndex A number indicating the role of the key.
     * @return An array of PrivateKey
     */
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

    /**
     * Returns a KlaytnWalletKey format
     * @return String
     */
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

    /**
     * Returns an instance of Account(AccountKeyPublic).
     * @return Account
     */
    public Account toAccount() {
        boolean isExistsOtherGroupKeys = this.keys.stream()
                .skip(1)
                .anyMatch(groupKeyArr -> groupKeyArr.length != 0);

        if(isExistsOtherGroupKeys) {
            return Account.createWithAccountKeyRoleBased(this.getAddress(), this.getPublicKey());
        }

        PrivateKey[] txGroupKeyArr = getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

        if(txGroupKeyArr.length != 1) {
            String[] publicKey = this.getPublicKey().get(0);
            return Account.createWithAccountKeyWeightedMultiSig(this.address, publicKey);
        }

        String publicKey = this.getPublicKey().get(0)[0];
        return Account.createWithAccountKeyPublic(this.address, publicKey);
    }

    /**
     * Returns an instance of Account.(AccountKeyWeightedMultiSig)
     * @param options WeightedMultiSigOption to make AccountKeyWeightedMultiSig.
     * @return Account
     */
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

    /**
     * Return an instance of Account.(AccountKeyRoleBased)
     * @param options A List of WeightedMultiSigOption to make AccountKeyRoleBased.
     * @return Account
     */
    public Account toAccount(List<WeightedMultiSigOptions> options) {
        return Account.createWithAccountKeyRoleBased(this.address, this.getPublicKey(), options);
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V4)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<List<KeyStore.Crypto>> cryptosList = new ArrayList<>();

        boolean isRoleBased = false;
        for(int i=0; i<AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT; i++) {
            PrivateKey[] privateKeys = this.getKeys().get(i);
            List<KeyStore.Crypto> list = KeyStore.Crypto.createCrypto(privateKeys, password, options);
            cryptosList.add(list);

            if(i > AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex() && privateKeys.length > 0) {
                isRoleBased = true;
            }
        }

        List keyList = isRoleBased? cryptosList : cryptosList.get(0);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.getAddress());
        keyStore.setKeyring(keyList);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());

        return keyStore;
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V3)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options The options to use when encrypt a keyring.
     * @return KeyStore
     * @throws CipherException It throws when cipher operation has failed.
     */
    public KeyStore encryptV3(String password, KeyStoreOption options) throws CipherException {
        String notAvailableError = "This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)";
        if(getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()).length > 1) {
            throw new RuntimeException(notAvailableError);
        }

        boolean isRoleBasedKey = this.keys.stream().skip(1).anyMatch(privateKeys -> privateKeys.length > 0);
        if (isRoleBasedKey) {
            throw new RuntimeException(notAvailableError);
        }

        List<KeyStore.Crypto> crypto = KeyStore.Crypto.createCrypto(this.getKeys().get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()), password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.getAddress());
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V3);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setCrypto(crypto.get(0));

        return keyStore;
    }

    /**
     * Returns true if keyring has decoupled key.
     * @return boolean
     */
    public boolean isDecoupled() {
        boolean isMultiple = this.keys.stream().anyMatch(privateKeys -> privateKeys.length > 1);
        if(isMultiple) return true;

        PrivateKey privateKey = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())[0];
        String derivedKey = privateKey.getDerivedAddress();

        return !(derivedKey.toLowerCase().equals(this.address.toLowerCase()));
    }

    /**
     * Getter function of address
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter function of keys
     * @return List
     */
    public List<PrivateKey[]> getKeys() {
        return keys;
    }

    /**
     * Setter function of address
     * @param address An address
     */
    public void setAddress(String address) {
        this.address = Numeric.prependHexPrefix(address);
    }






}
