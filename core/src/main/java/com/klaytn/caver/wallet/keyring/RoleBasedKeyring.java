/*
 * Copyright 2020 The caver-java Authors
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
 * Representing a Keyring which includes "address" and "private keys" by roles.
 */
public class RoleBasedKeyring extends AbstractKeyring {

    /**
     * The Keys to use in RoleBasedKeyring.
     */
    List<PrivateKey[]> keys;

    /**
     * Creates a RoleBasedKeyring.
     * @param address The address of keyring.
     * @param keys The keys to use in RoleBasedKeyring.
     */
    public RoleBasedKeyring(String address, List<PrivateKey[]> keys) {
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
        PrivateKey[] keyArr = getKeyByRole(role);
        validatedIndexWithKeys(index, keyArr.length);

        PrivateKey key = keyArr[index];
        SignatureData signatureData = key.sign(txHash, chainId);

        return signatureData;
    }

    /**
     * Signs a transaction hash with all private keys in specific role group and return signature list which V is 0 or 1(parity of the y-value of a secp256k1 signature). <p>
     * The role used in caver-java can be checked through {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * <pre>Example :
     * {@code
     * RoleBasedKeyring keyring = new RoleBasedKeyring(.....);
     * List<SignatureData> signatureList = keyring.ecsign("0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550", AccountKeyRoleBased.RoleGroup.TRANSACTION);
     * }
     * </pre>
     *
     * @param txHash The hash of transaction.
     * @param role A number indicating the role of the key.
     * @return
     */
    @Override
    public List<SignatureData> ecsign(String txHash, int role) {
        PrivateKey[] keyArr = getKeyByRole(role);

        return Arrays.stream(keyArr)
                .map(key-> {
                    return key.ecsign(txHash);
                }).collect(Collectors.toList());
    }

    /**
     * Signs a transaction hash with key in specific role group and return signature.<p>
     * The role used in caver-java can be checked through {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * <pre>Example :
     * {@code
     * RoleBasedKeyring keyring = new RoleBasedKeyring(.....);
     * SignatureData signatureList = keyring.ecsign("0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550", AccountKeyRoleBased.RoleGroup.TRANSACTION, 0);
     * }
     * </pre>
     *
     * @param txHash The hash transaction
     * @param role A number indicating the role of the key.
     * @param index The index of the key to be used in the specific role group.
     * @return
     */
    @Override
    public SignatureData ecsign(String txHash, int role, int index) {
        PrivateKey[] keyArr = getKeyByRole(role);
        validatedIndexWithKeys(index, keyArr.length);

        PrivateKey key = keyArr[index];
        SignatureData signatureData = key.ecsign(txHash);

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
        PrivateKey[] keyArr = getKeyByRole(role);
        validatedIndexWithKeys(index, keyArr.length);

        PrivateKey key = keyArr[index];
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
        List<List<KeyStore.Crypto>> cryptoList = new ArrayList<>();


        for(int i = 0; i<AccountKeyRoleBased.ROLE_GROUP_COUNT; i++) {
            PrivateKey[] privateKeys = this.keys.get(i);
            List<KeyStore.Crypto> list = KeyStore.Crypto.createCrypto(privateKeys, password, options);
            cryptoList.add(list);
        }

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setKeyring(cryptoList);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());

        return keyStore;
    }

    /**
     * Returns a copied RoleBasedKeyring instance.
     * @return Keyring
     */
    @Override
    public AbstractKeyring copy() {
        return new RoleBasedKeyring(this.address, this.keys);
    }

    /**
     * Returns a public key strings.<p>
     * It returns a public key as a uncompressed format.
     * @return String array
     */
    public List<String[]> getPublicKey() {
        return getPublicKey(false);
    }

    /**
     * Returns a public key strings.
     * @return String array
     */
    public List<String[]> getPublicKey(boolean compressed) {
        return this.keys.stream()
                .map(array -> {
                    return Arrays.stream(array)
                            .map(privateKey -> privateKey.getPublicKey(compressed))
                            .toArray(String[]::new);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns keys by role. If the key of the role passed as parameter is empty, the default key is returned.
     * @param role A number indicating the role of the key. You can use `AccountRoleBased.RoleGroup`.
     * @return PrivateKey Array
     */
    public PrivateKey[] getKeyByRole(int role) {
        if(role < 0 || role >= AccountKeyRoleBased.ROLE_GROUP_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        PrivateKey[] keyArr = this.keys.get(role);
        if(keyArr.length == 0 && role > AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()) {
            if(this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()).length == 0) {
                throw new IllegalArgumentException("The Key with specified role group does not exists. The TRANSACTION role group is also empty");
            }

            keyArr = this.keys.get(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        }

        return keyArr;
    }

    /**
     * Returns an instance of Account
     * @return Account
     */
    public Account toAccount() {
        List<WeightedMultiSigOptions> options = WeightedMultiSigOptions.getDefaultOptionsForRoleBased(this.getPublicKey());
        return toAccount(options);
    }

    /**
     * Returns an instance of Account
     * @param options The option List that includes 'threshold' and 'weight'. This is only necessary when keyring use multiple private keys.
     * @return Account
     */
    public Account toAccount(List<WeightedMultiSigOptions> options) {
        return Account.createWithAccountKeyRoleBased(this.address, this.getPublicKey(), options);
    }

    /**
     * Getter function of keys
     * @return PrivateKey Array
     */
    public List<PrivateKey[]> getKeys() {
        return keys;
    }
}
