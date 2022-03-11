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
 * Representing a Keyring which includes "address" and "private keys array"
 * @see AbstractKeyring
 * @see SingleKeyring
 * @see RoleBasedKeyring
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
     * <pre>Example :
     * {@code
     * String txHash = "0x{txHash}";
     * int chainId = 0;
     * int role = RoleGroup.TRANSACTION;
     *
     * List<SignatureData> signature = keyring.sign(txHash, chainId, role);
     * }
     * </pre>
     *
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @return {@code List<SignatureData>}
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
     * <pre>Example :
     * {@code
     * String txHash = "0x{txHash}";
     * int chainId = 0;
     * int role = RoleGroup.TRANSACTION;
     * int index = 0;
     *
     * SignatureData signature = keyring.sign(txHash, chainId, role, index);
     * }
     * </pre>
     *
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
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
     * Signs a transaction hash with all private keys in specific role group and returns a signature list which V is 0 or 1(parity of the y-value of a secp256k1 signature). <p>
     * MultipleKeyring doesn't define the role group, so it signs a transaction using all keys existed in MultipleKeyring.<p>
     * The role used in caver-java can be checked through {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * <pre>Example :
     * {@code
     * MultipleKeyring keyring = new MultipleKeyring(.....);
     * List<SignatureData> signatureList = keyring.ecsign("0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550", AccountKeyRoleBased.RoleGroup.TRANSACTION);
     * }
     * </pre>
     *
     * @param txHash The hash of transaction.
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @return {@code List<SignatureData>}
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
     * Signs a transaction hash with key in specific role group and returns a signature.
     * MultipleKeyring doesn't define the role group, so it signs a transaction using specific key existed in MultipleKeyring.
     * The role used in caver-java can be checked through {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * <pre>Example :
     * {@code
     * MultipleKeyring keyring = new MultipleKeyring(.....);
     * SignatureData signature = keyring.ecsign("0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550", AccountKeyRoleBased.RoleGroup.TRANSACTION, 0);
     * }
     * </pre>
     *
     * @param txHash The hash transaction
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @param index The index of the key to be used in the specific role group.
     * @return SignatureData
     */
    @Override
    public SignatureData ecsign(String txHash, int role, int index) {
        validatedIndexWithKeys(index, this.keys.length);

        PrivateKey key = getKeyByRole(role)[index];
        SignatureData signatureData = key.ecsign(txHash);

        return signatureData;
    }

    /**
     * Signs a hashed data with all key in specific role group and return MessageSigned instance.
     * <pre>Example :
     * {@code
     * Sting message = "message";
     * int role = RoleGroup.TRANSACTION;
     *
     * MessageSigned signedInfo = keyring.signMessage(message, role);
     * }
     * </pre>
     *
     * @param message The data string to sign.
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
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
     * <pre>Example :
     * {@code
     * Sting message = "message";
     * int role = RoleGroup.TRANSACTION;
     * int index = 0;
     *
     * MessageSigned signedInfo = keyring.signMessage(message, role, index);
     * }
     * </pre>
     *
     * @param message The data string to sign.
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @param index The index of the key to be used in the specific role group.
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
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V4) <p>
     * For more information, please refer to <a href="https://kips.klaytn.com/KIPs/kip-3">KIP3</a>.<p>
     * <pre>Example :
     * {@code
     * KeyStoreOption options = KeyStoreOption.getDefaultOptionWithKDF("pbkdf2");
     * KeyStore encrypted = keyring.encrypt("password", options);
     * }
     * </pre>
     *
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
     * <pre>Example :
     * {@code
     * AbstractKeyring copied = keyring.copy();
     * }
     * </pre>
     *
     * @return Keyring
     */
    @Override
    public AbstractKeyring copy() {
        return new MultipleKeyring(address, keys);
    }

    /**
     * Returns a public key strings.<p>
     * It returns a public key as a uncompressed format.
     * <pre>Example :
     * {@code
     * String[] publicKey = keyring.getPublicKey();
     * }
     * </pre>
     *
     * @return String array
     */
    public String[] getPublicKey() {
        return getPublicKey(false);
    }

    /**
     * Returns a public key strings.
     * <pre>Example :
     * {@code
     * String[] publicKey = keyring.getPublicKey(false);
     * }
     * </pre>
     *
     * @param compressed Whether in compressed format or not.
     * @return String array
     */
    public String[] getPublicKey(boolean compressed) {
        return Arrays.stream(this.keys).map(key -> {
            return key.getPublicKey(compressed);
        }).toArray(String[]::new);
    }

    /**
     * Returns keys by role. If the key of the role passed as parameter is empty, the default key is returned.
     * <pre>Example :
     * {@code
     * PrivateKey[] privateKeyArr = keyring.getKeyByRole(RoleGroup.TRANSACTION);
     * }
     * </pre>
     *
     * @param role A number indicating the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @return {@code PrivateKey[]}
     */
    public PrivateKey[] getKeyByRole(int role) {
        if(role < 0 || role >= AccountKeyRoleBased.ROLE_GROUP_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.keys;
    }

    /**
     * Returns an instance of Account.
     * <pre>Example:
     * {@code
     * Account account = keyring.toAccount();
     * }
     * </pre>
     *
     * @return Account
     */
    public Account toAccount() {
        WeightedMultiSigOptions options = WeightedMultiSigOptions.getDefaultOptionsForWeightedMultiSig(this.getPublicKey());
        return toAccount(options);
    }

    /**
     * Returns an instance of Account
     * <pre>Example:
     * {@code
     *  BigInteger[] optionWeight = {
     *     BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
     *  };
     *
     * WeightedMultiSigOptions weightedOption = new WeightedMultiSigOptions(
     *     BigInteger.ONE, Arrays.asList(optionWeight)
     * );
     *
     * Account account = keyring.toAccount(weightedOption);
     * }
     * </pre>
     *
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
