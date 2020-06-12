package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.CipherException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Representing a Keyring which includes "address" and "private key array"
 */
public class MultipleKeyring extends AbstractKeyring{
    /**
     * The Keys to use in MultipleKeyring
     */
    PrivateKey[] keys;

    /**
     * Creates a MultipleKeyring instance.
     * @param address The address of keyring.
     * @param keys The keys to use in MultipleKeyring.
     */
    public MultipleKeyring(String address, PrivateKey[] keys) {
        super(address);
        this.keys = keys;
    }

    /**
     * Signs a transaction hash with all keys in specific role group and return signature list.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @return List
     */
    @Override
    public List<SignatureData> sign(String txHash, int chainId, int role) {
        PrivateKey[] keyArr = getKeyByRole(role);

        return Arrays.stream(keyArr)
                .map(key-> {
                    return key.sign(txHash, chainId);
                }).collect(Collectors.toList());
    }

    /**
     * Signs a transaction hash with key in specific role group and return signature.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @param index The index of the key to be used in the specific role group.
     * @return SignatureData
     */
    @Override
    public SignatureData sign(String txHash, int chainId, int role, int index) {
        validatedIndexWithKeys(index, this.keys.length);

        PrivateKey key = getKeyByRole(role)[index];
        SignatureData signatureData = key.sign(txHash, chainId);

        return signatureData;
    }

    /**
     * Signs a hashed data with all key in specific role group and return MessageSigned instance.
     * @param message The data string to sign
     * @param role A number indicating the role of the key
     * @return MessageSigned
     */
    @Override
    public MessageSigned signMessage(String message, int role) {
        PrivateKey[] keyArr = getKeyByRole(role);
        String messageHash = Utils.hashMessage(message);

        List<SignatureData> signatureDataList = Arrays.stream(keyArr)
                .map(key -> {
                    return key.signMessage(messageHash);
                }).collect(Collectors.toCollection(ArrayList::new));

        MessageSigned signed = new MessageSigned(messageHash, signatureDataList, message);

        return signed;
    }

    /**
     * Signs a hashed data with key in specific role group and return MessageSigned instance.
     * @param message The data string to sign
     * @param role A number indicating the role of the key
     * @param index The index of the key to be used in the specific role group
     * @return MessageSigned
     */
    @Override
    public MessageSigned signMessage(String message, int role, int index) {
        validatedIndexWithKeys(index, this.keys.length);

        PrivateKey key = getKeyByRole(role)[index];
        String messageHash = Utils.hashMessage(message);

        SignatureData signatureData = key.signMessage(messageHash);
        MessageSigned signed = new MessageSigned(messageHash, Arrays.asList(signatureData), message);

        return signed;
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V4)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options  The options to use when encrypt a keyring.
     * @return KeyStore
     */
    @Override
    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<KeyStore.Crypto> cryptoList = KeyStore.Crypto.createCrypto(this.keys, password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setKeyring(cryptoList);

        return keyStore;
    }

    /**
     * Returns a copied MultipleKeyring instance.
     * @return Keyring
     */
    @Override
    public AbstractKeyring copy() {
        return new MultipleKeyring(address, keys);
    }

    /**
     * Return a public key strings
     * @return String array
     */
    public String[] getPublicKey() {
        return Arrays.stream(this.keys).map(key -> {
            return key.getPublicKey(false);
        }).toArray(String[]::new);
    }

    /**
     * returns keys by role. If the key of the role passed as parameter is empty, the default key is returned.
     * @param role A number indicating the role of the key. You can use `caver.wallet.keyring.role`.
     * @return PrivateKey Array
     */
    public PrivateKey[] getKeyByRole(int role) {
        if(role < 0 || role > AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.keys;
    }

    /**
     * Returns an instance of Account
     * @return Account
     */
    public Account toAccount() {
        WeightedMultiSigOptions options = WeightedMultiSigOptions.getDefaultOptionsForWeightedMultiSig(this.getPublicKey());
        return toAccount(options);
    }

    /**
     * Returns an instance of Account
     * @param options The options that includes 'threshold' and 'weight'. This is only necessary when keyring use multiple private keys.
     * @return Account
     */
    public Account toAccount(WeightedMultiSigOptions options) {
        return Account.createWithAccountKeyWeightedMultiSig(address, this.getPublicKey(), options);
    }

    /**
     * Getter function of keys
     * @return PrivateKey Array
     */
    public PrivateKey[] getKeys() {
        return keys;
    }
}
