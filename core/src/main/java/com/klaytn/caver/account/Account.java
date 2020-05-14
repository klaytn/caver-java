package com.klaytn.caver.account;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representing an Account which includes information for account update
 */
public class Account {
    /**
     * The address of Account
     */
    private String address;

    /**
     * The AccountKey of Account
     */
    private IAccountKey accountKey;

    /**
     * Creates an Account instance
     * @param address The address of Account
     * @param accountKey The accountKey of Account
     */
    public Account(String address, IAccountKey accountKey) {
        this.address = address;
        this.accountKey = accountKey;
    }

    /**
     * Create an Account instance that contains the AccountKeyPublic instance
     * @param address The address of Account
     * @param publicKey public key
     * @return Account
     */
    public static Account create(String address, String publicKey) {
        return createWithAccountKeyPublic(address, publicKey);
    }
                                                 
    /**
     * Create an Account instance that contains the AccountKeyWeightedMultiSig instance
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     * @param address address of Account
     * @param publicKeys public key array
     * @return Account
     */
    public static Account create(String address, String[] publicKeys) {
        return createWithAccountKeyWeightedMultiSig(address, publicKeys);
    }

    /**
     * Create an Account instance that contains AccountKeyWeightedMultiSig instance
     * @param address address of Account
     * @param publicKeys public key array
     * @param options WeightedMultiSigOptions
     * @return Account
     */
    public static Account create(String address, String[] publicKeys, WeightedMultiSigOptions options) {
        return createWithAccountKeyWeightedMultiSig(address, publicKeys, options);
    }

    /**
     * Create an Account instance that contains AccountKeyRoleBased instance
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     * @param address address of Account
     * @param publicKeyList List of public key array
     * @return Account
     */
    public static Account create(String address, List<String[]> publicKeyList) {
        return createWithAccountKeyRoleBased(address, publicKeyList);
    }

    /**
     * Create an Account instance that contains AccountKeyRoleBased instance
     * @param address address of Account
     * @param publicKeyList List of public key array
     * @param optionsList List of WeightedMultiSigOptions
     * @return Account
     */
    public static Account create(String address, List<String[]> publicKeyList, List<WeightedMultiSigOptions> optionsList ) {
        return createWithAccountKeyRoleBased(address, publicKeyList, optionsList);
    }

    /**
     * Create an Account instance from RLP-encoded account key
     * @param address address of Account
     * @param rlpEncodedKey RLP-encoded account key string
     * @return Account
     */
    public static Account createFromRLPEncoding(String address, String rlpEncodedKey) {
        IAccountKey accountKey = AccountKeyDecoder.decode(rlpEncodedKey);
        return new Account(address, accountKey);
    }

    /**
     * Create an Account instance which has AccountKeyLegacy as an accountKey
     * @param address address of Account
     * @return Account
     */
    public static Account createWithAccountKeyLegacy(String address) {
        IAccountKey accountKey = new AccountKeyLegacy();
        return new Account(address, accountKey);
    }

    /**
     * Create an Account instance which has AccountKeyFail as an accountKey
     * @param address address of Account
     * @return Account
     */
    public static Account createWithAccountKeyFail(String address) {
        IAccountKey accountKey = new AccountKeyFail();
        return new Account(address, accountKey);
    }

    /**
     * Creates an Account instance which has AccountKeyPublic as an accountKey
     * @param address address of Account
     * @param publicKey public key
     * @return Account
     */
    public static Account createWithAccountKeyPublic(String address, String publicKey) {
        IAccountKey accountKey = AccountKeyPublic.fromPublicKey(publicKey);
        return new Account(address, accountKey);
    }

    /**
     * Create an Account instance which has AccountKeyWeightedMultiSig as an accountKey
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     * @param address address of Account
     * @param publicKeys array of public key
     * @return Account
     */
    public static Account createWithAccountKeyWeightedMultiSig(String address, String[] publicKeys) {
        WeightedMultiSigOptions options = WeightedMultiSigOptions.fillWeightedMultiSigOptionForMultiSig(publicKeys.length);
        return createWithAccountKeyWeightedMultiSig(address, publicKeys, options);
    }

    /**
     * Create an Account instance which has AccountKeyWeightedMultiSig as an accountKey
     * @param address address of Account
     * @param publicKeys List of public key array
     * @param options List of WeightedMultiSigOptions
     * @return Account
     */
    public static Account createWithAccountKeyWeightedMultiSig(String address, String[] publicKeys, WeightedMultiSigOptions options) {
        if(options == null) {
            throw new NullPointerException("The variable 'options' is undefined. To create an Account instance with AccountKeyWeightedMultiSig, 'options' should be defined.");
        }
        IAccountKey accountKey = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKeys, options);
        return new Account(address, accountKey);
    }

    /**
     * Create an Account instance which has AccountKeyRoleBased as an accountKey
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     * @param address address of Account
     * @param roleBasedPublicKey List of public key array
     * @return Account
     */
    public static Account createWithAccountKeyRoleBased(String address, List<String[]> roleBasedPublicKey) {
        List<WeightedMultiSigOptions> optionList = WeightedMultiSigOptions.fillWeightedMultiSigOptionForRoleBased(roleBasedPublicKey);
        return createWithAccountKeyRoleBased(address, roleBasedPublicKey, optionList);
    }

    /**
     * Create an Account instance which has AccountKeyRoleBased as an accountKey
     * @param address address of Account
     * @param roleBasedPublicKey List of public key array
     * @param optionsList List of WeightedMultiSigOptions
     * @return Account
     */
    public static Account createWithAccountKeyRoleBased(String address, List<String[]> roleBasedPublicKey, List<WeightedMultiSigOptions> optionsList) {
        if (optionsList == null) {
            throw new NullPointerException("The variable 'optionsList' is undefined. To create an Account instance with AccountKeyRoleBased, 'optionsList' should be defined");
        }
        IAccountKey accountKey = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(roleBasedPublicKey, optionsList);

        return new Account(address, accountKey);
    }

    /**
     * Returns RLP-encoded accountKey string
     * @return String
     */
    public String getRLPEncodingAccountKey() {
        return this.getAccountKey().getRLPEncoding();
    }

    /**
     * Getter function for address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter function for accountKey
     * @return accountKey
     */
    public IAccountKey getAccountKey() {
        return accountKey;
    }
}
