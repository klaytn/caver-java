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
     * String[] keyArray = caver.wallet.keyring.generateMultipleKeys(3);
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
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3});
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.create(address, privateKeys);
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
     * String[] keyArray = caver.wallet.keyring.generateMultipleKeys(3);
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
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3});
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.createWithRoleBasedKey(address, privateKeys);
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
     *                 "  \"version\":4,\n" +
     *                 "  \"id\":\"55da3f9c-6444-4fc1-abfa-f2eabfc57501\",\n" +
     *                 "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
     *                 "  \"keyring\":[\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"93dd2c777abd9b80a0be8e1eb9739cbf27c127621a5d3f81e7779e47d3bb22f6\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"84f90907f3f54f53d19cbd6ae1496b86\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"69bf176a136c67a39d131912fb1e0ada4be0ed9f882448e1557b5c4233006e10\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"8f6d1d234f4a87162cf3de0c7fb1d4a8421cd8f5a97b86b1a8e576ffc1eb52d2\"\n" +
     *                 "      },\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"53d50b4e86b550b26919d9b8cea762cd3c637dfe4f2a0f18995d3401ead839a6\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"d7a6f63558996a9f99e7daabd289aa2c\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"966116898d90c3e53ea09e4850a71e16df9533c1f9e1b2e1a9edec781e1ad44f\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"bca7125e17565c672a110ace9a25755847d42b81aa7df4bb8f5ce01ef7213295\"\n" +
     *                 "      }\n" +
     *                 "    ],\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"f16def98a70bb2dae053f791882f3254c66d63416633b8d91c2848893e7876ce\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"f5006128a4c53bc02cada64d095c15cf\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"0d8a2f71f79c4880e43ff0795f6841a24cb18838b3ca8ecaeb0cda72da9a72ce\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"38b79276c3805b9d2ff5fbabf1b9d4ead295151b95401c1e54aed782502fc90a\"\n" +
     *                 "      }\n" +
     *                 "    ],\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"544dbcc327942a6a52ad6a7d537e4459506afc700a6da4e8edebd62fb3dd55ee\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"05dd5d25ad6426e026818b6fa9b25818\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"3a9003c1527f65c772c54c6056a38b0048c2e2d58dc0e584a1d867f2039a25aa\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"19a698b51409cc9ac22d63d329b1201af3c89a04a1faea3111eec4ca97f2e00f\"\n" +
     *                 "      },\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"dd6b920f02cbcf5998ed205f8867ddbd9b6b088add8dfe1774a9fda29ff3920b\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"ac04c0f4559dad80dc86c975d1ef7067\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"22279c6dbcc706d7daa120022a236cfe149496dca8232b0f8159d1df999569d6\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"1c54f7378fa279a49a2f790a0adb683defad8535a21bdf2f3dadc48a7bddf517\"\n" +
     *                 "      }\n" +
     *                 "    ]\n" +
     *                 "  ]\n" +
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
     *                 "  \"version\":4,\n" +
     *                 "  \"id\":\"55da3f9c-6444-4fc1-abfa-f2eabfc57501\",\n" +
     *                 "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
     *                 "  \"keyring\":[\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"93dd2c777abd9b80a0be8e1eb9739cbf27c127621a5d3f81e7779e47d3bb22f6\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"84f90907f3f54f53d19cbd6ae1496b86\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"69bf176a136c67a39d131912fb1e0ada4be0ed9f882448e1557b5c4233006e10\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"8f6d1d234f4a87162cf3de0c7fb1d4a8421cd8f5a97b86b1a8e576ffc1eb52d2\"\n" +
     *                 "      },\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"53d50b4e86b550b26919d9b8cea762cd3c637dfe4f2a0f18995d3401ead839a6\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"d7a6f63558996a9f99e7daabd289aa2c\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"966116898d90c3e53ea09e4850a71e16df9533c1f9e1b2e1a9edec781e1ad44f\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"bca7125e17565c672a110ace9a25755847d42b81aa7df4bb8f5ce01ef7213295\"\n" +
     *                 "      }\n" +
     *                 "    ],\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"f16def98a70bb2dae053f791882f3254c66d63416633b8d91c2848893e7876ce\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"f5006128a4c53bc02cada64d095c15cf\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"0d8a2f71f79c4880e43ff0795f6841a24cb18838b3ca8ecaeb0cda72da9a72ce\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"38b79276c3805b9d2ff5fbabf1b9d4ead295151b95401c1e54aed782502fc90a\"\n" +
     *                 "      }\n" +
     *                 "    ],\n" +
     *                 "    [\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"544dbcc327942a6a52ad6a7d537e4459506afc700a6da4e8edebd62fb3dd55ee\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"05dd5d25ad6426e026818b6fa9b25818\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"3a9003c1527f65c772c54c6056a38b0048c2e2d58dc0e584a1d867f2039a25aa\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"19a698b51409cc9ac22d63d329b1201af3c89a04a1faea3111eec4ca97f2e00f\"\n" +
     *                 "      },\n" +
     *                 "      {\n" +
     *                 "        \"ciphertext\":\"dd6b920f02cbcf5998ed205f8867ddbd9b6b088add8dfe1774a9fda29ff3920b\",\n" +
     *                 "        \"cipherparams\":{\"iv\":\"ac04c0f4559dad80dc86c975d1ef7067\"},\n" +
     *                 "        \"cipher\":\"aes-128-ctr\",\n" +
     *                 "        \"kdf\":\"scrypt\",\n" +
     *                 "        \"kdfparams\":{\n" +
     *                 "          \"dklen\":32,\n" +
     *                 "          \"salt\":\"22279c6dbcc706d7daa120022a236cfe149496dca8232b0f8159d1df999569d6\",\n" +
     *                 "          \"n\":4096,\n" +
     *                 "          \"r\":8,\n" +
     *                 "          \"p\":1\n" +
     *                 "        },\n" +
     *                 "        \"mac\":\"1c54f7378fa279a49a2f790a0adb683defad8535a21bdf2f3dadc48a7bddf517\"\n" +
     *                 "      }\n" +
     *                 "    ]\n" +
     *                 "  ]\n" +
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
