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

package com.klaytn.caver.wallet.keyring.wrapper;

import com.klaytn.caver.wallet.keyring.*;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.List;

/**
 * Representing a KeyringFactoryWrapper class which wraps all of static methods of KeyringFactory.<p>
 * This class makes KeyringFactory available through Caver instance like `caver.wallet.keyring`.
 * @see com.klaytn.caver.wallet.KeyringContainer#keyring
 * @see KeyringFactory
 * @see SingleKeyring
 * @see MultipleKeyring
 * @see RoleBasedKeyring
 */
public class KeyringFactoryWrapper {

    /**
     * Creates a KeyringFactoryWrapper instance
     */
    public KeyringFactoryWrapper() {
    }

    /**
     * Generates a single type of keyring instance.<p>
     * <pre>Example :
     * {@code
     * SingleKeyring keyring = caver.wallet.keyring.generate();
     * }
     * </pre>
     *
     * @return SingleKeyring
     */
    public SingleKeyring generate() {
        return KeyringFactory.generate();
    }

    /**
     * Generates a single type of keyring instance with entropy.<p>
     * <pre>Example :
     * {@code
     * String entropy = "entropy";
     * SingleKeyring keyring = caver.wallet.keyring.generate(entropy);
     * }
     * </pre>
     *
     * @param entropy A random string to create keyring.
     * @return SingleKeyring
     */
    public SingleKeyring generate(String entropy) {
        return KeyringFactory.generate(entropy);
    }

    /**
     * Generates a single private key string.<p>
     * <pre>Example :
     * {@code
     * String privateKey = caver.wallet.keyring.generateSingleKey();
     * }
     * </pre>
     *
     * @return String
     */
    public String generateSingleKey() {
        return KeyringFactory.generateSingleKey();
    }

    /**
     * Generates a single private key string with entropy.<p>
     * <pre>
     * {@code
     * String entropy = "entropy";
     * String privateKey = caver.wallet.keyring.generateSingleKey(entropy);
     * }
     * </pre>
     *
     * @param entropy A random string to create private key.
     * @return String
     */
    public String generateSingleKey(String entropy) {
        return KeyringFactory.generateSingleKey(entropy);
    }

    /**
     * Generates an array of private key strings.<p>
     * <pre>
     * {@code
     * String[] privateKeys = caver.wallet.keyring.generateMultipleKeys(3);
     * }
     * </pre>
     *
     * @param num A length of keys.
     * @return {@code String[]}
     */
    public String[] generateMultipleKeys(int num) {
        return KeyringFactory.generateMultipleKeys(num);
    }

    /**
     * Generates an array of private key strings with entropy.<p>
     * <pre>
     * {@code
     * String entropy = "entropy";
     * String[] privateKeys = caver.wallet.keyring.generateMultipleKeys(3, entropy);
     * }
     * </pre>
     *
     * @param num A length of keys.
     * @param entropy A random string to create private key.
     * @return {@code String[]}
     */
    public String[] generateMultipleKeys(int num, String entropy) {
        return KeyringFactory.generateMultipleKeys(num, entropy);
    }

    /**
     * Generates an list of private key strings.<p>
     * <pre>Example :
     * {@code
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3});
     * }
     * </pre>
     *
     * @param numArr An array containing the number of keys for each role.
     * @return {@code List<String[]>}
     */
    public List<String[]> generateRoleBasedKeys(int[] numArr) {
        return KeyringFactory.generateRoleBasedKeys(numArr);
    }

    /**
     * Generates an list of private key strings.
     * <pre>Example :
     * {@code
     * String entropy = "entropy";
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3});
     * }
     * </pre>
     *
     * @param numArr An array containing the number of keys for each role.
     * @param entropy A random string to create private key.
     * @return List
     */
    public List<String[]> generateRoleBasedKeys(int[] numArr, String entropy) {
        return KeyringFactory.generateRoleBasedKeys(numArr, entropy);
    }

    /**
     * Creates a single type of keyring instance.<p>
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String key = caver.wallet.keyring.generateSingleKey();
     *
     * SingleKeyring keyring = caver.wallet.keyring.create(address, key);
     * }
     * </pre>
     *
     * @param address The address of keyring.
     * @param key The key of keyring.
     * @return SingleKeyring
     */
    public SingleKeyring create(String address, String key) {
        return KeyringFactory.create(address, key);
    }

    /**
     * Creates a multiple type of keyring instance.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String[] keyArray = new String[] {caver.wallet.keyring.generateSingleKey(), caver.wallet.keyring.generateSingleKey(), ....};
     * MultipleKeyring keyring = (MultipleKeyring)caver.wallet.keyring.create(address, keyArray);
     * }
     * </pre>
     *
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return MultipleKeyring
     */
    public MultipleKeyring create(String address, String[] keys) {
        return KeyringFactory.create(address, keys);
    }

    /**
     * Creates a roleBased type of keyring instance.
     * <pre>Example :
     * {@code
     * String address = caver.wallet.keyring.generate().getAddress();
     * String[][] privateKeyArr = {
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     },
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     },
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     }
     * };
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.create(address, Arrays.asList(privateKeyArr));
     * }
     * </pre>
     *
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return RoleBasedKeyring
     */
    public RoleBasedKeyring create(String address, List<String[]> keys) {
        return KeyringFactory.create(address, keys);
    }

    /**
     * Creates a single type of keyring instance with private key.
     * <pre>Example :
     * {@code
     * String privateKey = "0x{privateKey}";
     * SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
     * }
     * </pre>
     *
     * @param key A private key string.
     * @return SingleKeyring
     */
    public SingleKeyring createFromPrivateKey(String key) {
        return KeyringFactory.createFromPrivateKey(key);
    }

    /**
     * Creates a single type of keyring instance from KlaytnWalletKey string.
     * <pre>Example :
     * {@code
     * String klaytnWalletKey = "0x{private key}0x{type}0x{address}";
     * SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey(klaytnWalletKey);
     * }
     * </pre>
     *
     * @param klaytnWalletKey A key string in KlaytnWalletKey format.
     * @return SingleKeyring
     */
    public SingleKeyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        return KeyringFactory.createFromKlaytnWalletKey(klaytnWalletKey);
    }

    /**
     * Creates a single type of keyring instance from address and private key string.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String key = "0x{privateKey}";
     *
     * SingleKeyring keyring = caver.wallet.keyring.createWithSingleKey(address, key);
     * }
     * </pre>
     * @param address An address of keyring.
     * @param key A private key string.
     * @return SingleKeyring
     */
    public SingleKeyring createWithSingleKey(String address, String key) {
        return KeyringFactory.createWithSingleKey(address, key);
    }

    /**
     * Creates a multiple type of keyring instance from address and private key strings.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String[] keyArray = new String[] {caver.wallet.keyring.generateSingleKey(), caver.wallet.keyring.generateSingleKey(), ....};
     * MultipleKeyring keyring = (MultipleKeyring)caver.wallet.keyring.createWithMultipleKey(address, keyArray);
     * }
     * </pre>
     *
     * @param address An address of keyring.
     * @param multipleKey An array of private key strings.
     * @return MultipleKeyring
     */
    public MultipleKeyring createWithMultipleKey(String address, String[] multipleKey) {
        return KeyringFactory.createWithMultipleKey(address, multipleKey);
    }

    /**
     * Create a roleBased type of keyring instance from address and private key strings.
     * <pre>Example :
     * {@code
     * String address = caver.wallet.keyring.generate().getAddress();
     * String[][] privateKeyArr = {
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     },
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     },
     *     {
     *        caver.wallet.keyring.generateSingleKey(),
     *        caver.wallet.keyring.generateSingleKey(),
     *     }
     * };
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.createWithRoleBasedKey(address, Arrays.asList(privateKeyArr));
     * }
     * </pre>
     *
     * @param address An address of keyring.
     * @param roleBasedKey A List of private key strings.
     * @return RoleBasedKeyring
     */
    public RoleBasedKeyring createWithRoleBasedKey(String address, List<String[]> roleBasedKey) {
        return KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);
    }

    /**
     * Decrypts a KeyStore json string and returns a keyring instance.
     * <pre>Example :
     * {@code
     * String keyStoreStr = "{\n" +
     *                 "  \"version\":3,\n" +
     *                 "  \"id\":\"7a0a8557-22a5-4c90-b554-d6f3b13783ea\",\n" +
     *                 "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
     *                 "  \"crypto\":{\n" +
     *                 "    \"ciphertext\":\"696d0e8e8bd21ff1f82f7c87b6964f0f17f8bfbd52141069b59f084555f277b7\",\n" +
     *                 "    \"cipherparams\":{\"iv\":\"1fd13e0524fa1095c5f80627f1d24cbd\"},\n" +
     *                 "    \"cipher\":\"aes-128-ctr\",\n" +
     *                 "    \"kdf\":\"scrypt\",\n" +
     *                 "    \"kdfparams\":{\n" +
     *                 "      \"dklen\":32,\n" +
     *                 "      \"salt\":\"7ee980925cef6a60553cda3e91cb8e3c62733f64579f633d0f86ce050c151e26\",\n" +
     *                 "      \"n\":4096,\n" +
     *                 "      \"r\":8,\n" +
     *                 "      \"p\":1\n" +
     *                 "    },\n" +
     *                 "    \"mac\":\"8684d8dc4bf17318cd46c85dbd9a9ec5d9b290e04d78d4f6b5be9c413ff30ea4\"\n" +
     *                 "  }\n" +
     *                 "}";
     *
     * AbstractKeyring keyring = caver.wallet.keyring.decrypt(keyStoreStr, "password");
     * }
     * </pre>
     *
     * @param keyStore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return AbstractKeyring
     * @throws CipherException
     * @throws IOException
     */
    public AbstractKeyring decrypt(String keyStore, String password) throws CipherException, IOException {
        return KeyringFactory.decrypt(keyStore, password);
    }

    /**
     * Decrypts a keystore v3 or v4 and returns a keyring instance.
     * <pre>Example :
     * {@code
     * String keyStoreStr = "{\n" +
     *                 "  \"version\":3,\n" +
     *                 "  \"id\":\"7a0a8557-22a5-4c90-b554-d6f3b13783ea\",\n" +
     *                 "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
     *                 "  \"crypto\":{\n" +
     *                 "    \"ciphertext\":\"696d0e8e8bd21ff1f82f7c87b6964f0f17f8bfbd52141069b59f084555f277b7\",\n" +
     *                 "    \"cipherparams\":{\"iv\":\"1fd13e0524fa1095c5f80627f1d24cbd\"},\n" +
     *                 "    \"cipher\":\"aes-128-ctr\",\n" +
     *                 "    \"kdf\":\"scrypt\",\n" +
     *                 "    \"kdfparams\":{\n" +
     *                 "      \"dklen\":32,\n" +
     *                 "      \"salt\":\"7ee980925cef6a60553cda3e91cb8e3c62733f64579f633d0f86ce050c151e26\",\n" +
     *                 "      \"n\":4096,\n" +
     *                 "      \"r\":8,\n" +
     *                 "      \"p\":1\n" +
     *                 "    },\n" +
     *                 "    \"mac\":\"8684d8dc4bf17318cd46c85dbd9a9ec5d9b290e04d78d4f6b5be9c413ff30ea4\"\n" +
     *                 "  }\n" +
     *                 "}";
     *
     * ObjectMapper mapper = new ObjectMapper();
     * KeyStore file = mapper.readValue(keyStoreStr, KeyStore.class);
     *
     * AbstractKeyring keyring = caver.wallet.keyring(file, "password");
     * }
     * </pre>
     *
     * @param keystore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return AbstractKeyring
     * @throws CipherException It throws when cipher operation has failed.
     */
    public AbstractKeyring decrypt(KeyStore keystore, String password) throws CipherException{
        return KeyringFactory.decrypt(keystore, password);
    }
}
