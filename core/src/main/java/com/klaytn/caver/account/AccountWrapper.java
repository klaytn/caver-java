/*
 * Copyright 2021 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.account;

import java.util.List;

/**
 * Representing an AccountWrapper which wraps all of static methods of Account
 */
public class AccountWrapper {
    public AccountKeyWrapper accountKey;

    /**
     * Creates an AccountWrapper instance
     */
    public AccountWrapper() {
        accountKey = new AccountKeyWrapper();
    }

    /**
     * Create an Account instance that contains the AccountKeyPublic instance
     *
     * @param address   The address of Account
     * @param publicKey public key
     * @return Account
     */
    public Account create(String address, String publicKey) {
        return Account.createWithAccountKeyPublic(address, publicKey);
    }

    /**
     * Create an Account instance that contains the AccountKeyWeightedMultiSig instance
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     *
     * @param address    address of Account
     * @param publicKeys public key array
     * @return Account
     */
    public Account create(String address, String[] publicKeys) {
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeys);
    }

    /**
     * Create an Account instance that contains AccountKeyWeightedMultiSig instance
     *
     * @param address    address of Account
     * @param publicKeys public key array
     * @param options    WeightedMultiSigOptions
     * @return Account
     */
    public Account create(String address, String[] publicKeys, WeightedMultiSigOptions options) {
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeys, options);
    }

    /**
     * Create an Account instance that contains AccountKeyRoleBased instance
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     *
     * @param address       address of Account
     * @param publicKeyList List of public key array
     * @return Account
     */
    public Account create(String address, List<String[]> publicKeyList) {
        return Account.createWithAccountKeyRoleBased(address, publicKeyList);
    }

    /**
     * Create an Account instance that contains AccountKeyRoleBased instance
     *
     * @param address       address of Account
     * @param publicKeyList List of public key array
     * @param optionsList   List of WeightedMultiSigOptions
     * @return Account
     */
    public Account create(String address, List<String[]> publicKeyList, List<WeightedMultiSigOptions> optionsList) {
        return Account.createWithAccountKeyRoleBased(address, publicKeyList, optionsList);
    }

    /**
     * Create an Account instance from RLP-encoded account key
     *
     * @param address       address of Account
     * @param rlpEncodedKey RLP-encoded account key string
     * @return Account
     */
    public Account createFromRLPEncoding(String address, String rlpEncodedKey) {
        return Account.createFromRLPEncoding(address, rlpEncodedKey);
    }

    /**
     * Create an Account instance which has AccountKeyLegacy as an accountKey
     *
     * @param address address of Account
     * @return Account
     */
    public Account createWithAccountKeyLegacy(String address) {
        return Account.createWithAccountKeyLegacy(address);
    }

    /**
     * Create an Account instance which has AccountKeyFail as an accountKey
     *
     * @param address address of Account
     * @return Account
     */
    public Account createWithAccountKeyFail(String address) {
        return Account.createWithAccountKeyFail(address);
    }

    /**
     * Creates an Account instance which has AccountKeyPublic as an accountKey
     *
     * @param address   address of Account
     * @param publicKey public key
     * @return Account
     */
    public Account createWithAccountKeyPublic(String address, String publicKey) {
        return Account.createWithAccountKeyPublic(address, publicKey);
    }

    /**
     * Create an Account instance which has AccountKeyWeightedMultiSig as an accountKey
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     *
     * @param address    address of Account
     * @param publicKeys array of public key
     * @return Account
     */
    public Account createWithAccountKeyWeightedMultiSig(String address, String[] publicKeys) {
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeys);
    }

    /**
     * Create an Account instance which has AccountKeyWeightedMultiSig as an accountKey
     *
     * @param address    address of Account
     * @param publicKeys List of public key array
     * @param options    List of WeightedMultiSigOptions
     * @return Account
     */
    public Account createWithAccountKeyWeightedMultiSig(String address, String[] publicKeys, WeightedMultiSigOptions options) {
        return Account.createWithAccountKeyWeightedMultiSig(address, publicKeys, options);
    }

    /**
     * Create an Account instance which has AccountKeyRoleBased as an accountKey
     * This method set 1 to WeightedMultiSigOptions values(threshold, weights)
     *
     * @param address            address of Account
     * @param roleBasedPublicKey List of public key array
     * @return Account
     */
    public Account createWithAccountKeyRoleBased(String address, List<String[]> roleBasedPublicKey) {
        return Account.createWithAccountKeyRoleBased(address, roleBasedPublicKey);
    }

    /**
     * Create an Account instance which has AccountKeyRoleBased as an accountKey
     *
     * @param address            address of Account
     * @param roleBasedPublicKey List of public key array
     * @param optionsList        List of WeightedMultiSigOptions
     * @return Account
     */
    public Account createWithAccountKeyRoleBased(String address, List<String[]> roleBasedPublicKey, List<WeightedMultiSigOptions> optionsList) {
        return Account.createWithAccountKeyRoleBased(address, roleBasedPublicKey, optionsList);
    }
}
