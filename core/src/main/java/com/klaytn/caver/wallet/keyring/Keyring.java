package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.Wallet;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import javax.management.relation.Role;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Keyring {
    String address;
    List<PrivateKey[]> keys;

    private Keyring(String address, List<PrivateKey[]> keys) {
        if(!Utils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid Address");
        }

        this.address = address;
        this.keys = keys;
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

//    public static KeyStore encrypt(String key, String password, Crypto options) {
//
//    }
//
//    public static KeyStore encrypt(Keyring keyring, String password, Crypto options) {
//
//    }
//
//    public static KeyStore encryptV3(String key, String password, Crypto options) {
//
//    }
//
//    public static KeyStore encryptV3(Keyring keyring, String password, Crypto options) {
//
//    }
//
//    public static Keyring decrypt(KeyStore keystore, String password) {
//
//    }
//
    public static String recover(MessageSigned messageSigned) throws SignatureException {
        KlaySignatureData klaySignatureData = messageSigned.getSignatureData();
        return recover(messageSigned.getMessage(), klaySignatureData);
    }

    public static String recover(String message, KlaySignatureData signatureData) throws SignatureException {
        return recover(message, signatureData, false);
    }

    public static String recover(String message, KlaySignatureData signatureData, boolean isPrefixed) throws SignatureException {
        Sign.SignatureData data = new Sign.SignatureData(signatureData.getV()[0], signatureData.getR(), signatureData.getS());
        String hashedMessage = message;
        if(!isPrefixed) {
            hashedMessage = Utils.hashMessage(message);
        }

        BigInteger publicKey = Sign.signedMessageToKey(Numeric.hexStringToByteArray(hashedMessage), data);
        return Keys.getAddress(publicKey);
    }

    public List<String[]> getPublicKey() {
        List<String[]> publicKeyList = this.keys.stream().map(element -> {
            return Arrays.stream(element)
                    .map(PrivateKey::getDerivedAddress)
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

        String messageHash = Hash.sha3(message);
        KlaySignatureData signatureData =  groupKeyArr[keyIndex].signMessage(messageHash);
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
        if(isEmptyKey(this)) {
            throw new RuntimeException("Failed to create Account instance: Empty key in keyring.");
        }

        PrivateKey[] txGroupKeyArr = getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        if(txGroupKeyArr.length != 1) {
            throw new RuntimeException("Failed to create Account instance: There are two or more keys in RoleTransaction Key array.");
        }

        boolean isExistsOtherGroupKeys = this.keys.stream()
                .skip(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())
                .anyMatch(groupKeyArr -> groupKeyArr.length != 0);

        if(isExistsOtherGroupKeys) {
            throw new RuntimeException("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");
        }

//        for(int i = AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(); i<AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT; i++) {
//            PrivateKey[] groupKeyArr = getKeyByRole(i);
//            if(groupKeyArr.length !=0) {
//                throw new RuntimeException("Failed to create Account instance: There are exists keys in Group(RoleAccountUpdate, RoleFeePayer)");
//            }
//        }
        String publicKey = this.getPublicKey().get(0)[0];
        return Account.createWithAccountKeyPublic(this.address, publicKey);
    }

    public Account toAccount(WeightedMultiSigOptions options) {
        if(isEmptyKey(this)) {
            throw new RuntimeException("Failed to create Account instance: Empty key in keyring.");
        }

        PrivateKey[] txGroupKeyArr = getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        if(txGroupKeyArr.length == 0) {
            throw new RuntimeException("Failed to create Account instance: There must be one or more keys in RoleTransaction Key array.");
        }

        if(txGroupKeyArr.length != options.getWeights().size()) {
            throw new RuntimeException("Failed to create Account instance: The number of keys and the number of elements in the Weights array should be the same.");
        }

        boolean isExistsOtherGroupKeys = this.keys.stream()
                .skip(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())
                .anyMatch(groupKeyArr -> groupKeyArr.length != 0);

        if(isExistsOtherGroupKeys) {
            throw new RuntimeException("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");
        }

        String address = this.address;
        String[] publicKeyArr = this.getPublicKey().get(0);
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeyArr, options);
    }

    public Account toAccount(List<WeightedMultiSigOptions> options) {
        if(isEmptyKey(this)) {
            throw new RuntimeException("Failed to create Account instance: Empty key in keyring.");
        }

        return Account.createWithAccountKeyRoleBased(this.address, this.getPublicKey(), options);
    }

//    public KeyStore encrypt(String password, Crypto options) {
//
//    }
//
//    public KeyStore encryptV3(String password, Crypto options) {
//
//    }

    public boolean isDecoupled() {
        boolean isMultiple = this.keys.stream().anyMatch(privateKeys -> privateKeys.length > 1);
        if(isMultiple) return true;

        PrivateKey privateKey = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())[0];
        String derivedKey = privateKey.getDerivedAddress();

        return !(derivedKey.toLowerCase().equals(this.address.toLowerCase()));
    }

    private static boolean isEmptyKey(Keyring keyring) {
        if(keyring.keys == null)  {
            return true;
        }

        return keyring.keys.stream().allMatch(element -> element.length == 0);
    }

    public String getAddress() {
        return address;
    }

    public List<PrivateKey[]> getKeys() {
        return keys;
    }


}
