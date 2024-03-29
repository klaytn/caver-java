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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Representing a KeyringFactory which supports create function for Keyring(Single/Multiple/RoleBased) instance. <p>
 * To access it from Caver instance, it can be accessed through `caver.wallet.keyring` using {@link com.klaytn.caver.wallet.keyring.wrapper.KeyringFactoryWrapper}. <p>
 * @see com.klaytn.caver.wallet.keyring.wrapper.KeyringFactoryWrapper
 * @see AbstractKeyring
 * @see com.klaytn.caver.wallet.keyring.SingleKeyring
 * @see com.klaytn.caver.wallet.keyring.MultipleKeyring
 * @see com.klaytn.caver.wallet.keyring.RoleBasedKeyring
 */
public class KeyringFactory {

    /**
     * Returns a randomly generated single keyring instance.<p>
     * <pre>Example :
     * {@code
     * SingleKeyring keyring = caver.wallet.keyring.generate();
     * }
     * </pre>
     *
     * @return SingleKeyring
     */
    public static SingleKeyring generate() {
        return KeyringFactory.generate(null);
    }

    /**
     * Returns a randomly generated single keyring instance with entropy.<p>
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
    public static SingleKeyring generate(String entropy) {
        PrivateKey privateKey = PrivateKey.generate(entropy);
        String address = privateKey.getDerivedAddress();

        return createWithSingleKey(address, privateKey.getPrivateKey());
    }

    /**
     * Returns a single private key string.<p>
     * <pre>Example :
     * {@code
     * String privateKey = caver.wallet.keyring.generateSingleKey();
     * }
     * </pre>
     *
     * @return String
     */
    public static String generateSingleKey() {
        return generateSingleKey(null);
    }

    /**
     * Returns a single private key string with entropy.<p>
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
    public static String generateSingleKey(String entropy) {
        return PrivateKey.generate(entropy).getPrivateKey();
    }

    /**
     * Returns an array of private key strings.<p>
     * <pre>
     * {@code
     * String[] privateKeys = caver.wallet.keyring.generateMultipleKeys(3);
     * }
     * </pre>
     *
     * @param num A length of keys.
     * @return {@code String[]}
     */
    public static String[] generateMultipleKeys(int num) {
        return generateMultipleKeys(num, null);
    }

    /**
     * Returns an array of private key strings with entropy.<p>
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
    public static String[] generateMultipleKeys(int num, String entropy) {
        String[] keyArr = new String[num];

        for(int i=0; i<num; i++) {
            keyArr[i] = PrivateKey.generate(entropy).getPrivateKey();
        }

        return keyArr;
    }

    /**
     * Returns a list of private key strings array.<p>
     * <pre>Example :
     * {@code
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3});
     * }
     * </pre>
     *
     * @param numArr An array containing the number of keys for each role.
     * @return {@code List<String[]>}
     */
    public static List<String[]> generateRoleBasedKeys(int[] numArr) {
        return generateRoleBasedKeys(numArr, null);
    }

    /**
     * Returns a list of private key strings array.
     * <pre>Example :
     * {@code
     * String entropy = "entropy";
     * List<String[]> privateKeys = caver.wallet.keyring.generateRoleBasedKeys(new int[] {3,3,3}, entropy);
     * }
     * </pre>
     *
     * @param numArr An array containing the number of keys for each role.
     * @param entropy A random string to create private key.
     * @return List
     */
    public static List<String[]> generateRoleBasedKeys(int[] numArr, String entropy) {
        String[][] keyArr = new String[3][];

        for(int i=0; i<numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j=0; j<length; j++) {
                arr[j] = PrivateKey.generate(entropy).getPrivateKey();
            }
            keyArr[i] = arr;
        }

        return Arrays.asList(keyArr);
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
    public static SingleKeyring create(String address, String key) {
        return createWithSingleKey(address, key);
    }

    /**
     * Creates a multiple type of keyring instance.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String[] keyArray = caver.wallet.keyring.generateMultipleKey(3);
     * MultipleKeyring keyring = (MultipleKeyring)caver.wallet.keyring.create(address, keyArray);
     * }
     * </pre>
     *
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return MultipleKeyring
     */
    public static MultipleKeyring create(String address, String[] keys) {
        return createWithMultipleKey(address, keys);
    }

    /**
     * Creates a roleBased type of keyring instance.
     * <pre>Example :
     * {@code
     * String address = caver.wallet.keyring.generate().getAddress();
     * List<String> privateKeyList = caver.wallet.keyring.generateRoleBasedKeys(new int[]{3,3,3});
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.create(address, privateKeyList);
     * }
     * </pre>
     *
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return RoleBasedKeyring
     */
    public static RoleBasedKeyring create(String address, List<String[]> keys) {
        return createWithRoleBasedKey(address, keys);
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
    public static SingleKeyring createFromPrivateKey(String key) {
        if(Utils.isKlaytnWalletKey(key)) {
            return KeyringFactory.createFromKlaytnWalletKey(key);
        }

        PrivateKey privateKey = new PrivateKey(key);
        String address = privateKey.getDerivedAddress();

        return createWithSingleKey(address, key);
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
    public static SingleKeyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        String[] parsedKey = Utils.parseKlaytnWalletKey(klaytnWalletKey);

        String privateKey = parsedKey[0];
        String address = parsedKey[2];

        return KeyringFactory.createWithSingleKey(address, privateKey);
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
     *
     * @param address An address of keyring.
     * @param key A private key string.
     * @return SingleKeyring
     */
    public static SingleKeyring createWithSingleKey(String address, String key) {
        if(Utils.isKlaytnWalletKey(key)) {
            throw new IllegalArgumentException("Invalid format of parameter. Use 'fromKlaytnWalletKey' to create Keyring from KlaytnWalletKey.");
        }

        PrivateKey privateKey = new PrivateKey(key);
        return new SingleKeyring(address, privateKey);
    }

    /**
     * Creates a multiple type of keyring instance from address and private key strings.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String[] privateKeys = caver.wallet.keyring.generateMultipleKeys(3);
     * MultipleKeyring keyring = (MultipleKeyring)caver.wallet.keyring.createWithMultipleKey(address, privateKeys);
     * }
     * </pre>
     *
     * @param address An address of keyring.
     * @param multipleKey An array of private key strings.
     * @return MultipleKeyring
     */
    public static MultipleKeyring createWithMultipleKey(String address, String[] multipleKey) {
        if(multipleKey.length > WeightedMultiSigOptions.MAX_COUNT_WEIGHTED_PUBLIC_KEY) {
            throw new IllegalArgumentException("MultipleKey has up to 10.");
        }

        PrivateKey[] keyArr = Arrays.stream(multipleKey)
                                    .map(PrivateKey::new)
                                    .toArray(PrivateKey[]::new);

        return new MultipleKeyring(address, keyArr);
    }

    /**
     * Create a roleBased type of keyring instance from address and a list of private key strings array.
     * <pre>Example :
     * {@code
     * String address = caver.wallet.keyring.generate().getAddress();
     * List<String> privateKeysList = caver.wallet.keyring.generateRoleBasedKeys(new int[]{3,3,3});
     *
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.keyring.createWithRoleBasedKey(address, privateKeysList);
     * }
     * </pre>
     *
     * @param address An address of keyring.
     * @param roleBasedKey A List of private key strings.
     * @return RoleBasedKeyring
     */
    public static RoleBasedKeyring createWithRoleBasedKey(String address, List<String[]> roleBasedKey) {
        if(roleBasedKey.size() > AccountKeyRoleBased.ROLE_GROUP_COUNT) {
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

        return new RoleBasedKeyring(address, privateKeys);
    }

    /**
     * Decrypts a KeyStore json string and returns a keyring instance.
     * <pre>Example :
     * {@code
     * String keyStoreStr = ""{\n" +
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
    public static AbstractKeyring decrypt(String keyStore, String password) throws CipherException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        KeyStore file = mapper.readValue(keyStore, KeyStore.class);

        return decrypt(file, password);
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
     * AbstractKeyring keyring = caver.wallet.keyring.decrypt(file, "password");
     * }
     * </pre>
     *
     * @param keystore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return AbstractKeyring
     * @throws CipherException It throws when cipher operation has failed.
     */
    public static AbstractKeyring decrypt(KeyStore keystore, String password) throws CipherException{
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

        String address = Utils.addHexPrefix(keystore.getAddress());
        if(keystore.getVersion() == KeyStore.KEY_STORE_VERSION_V3) {
            KeyStore.Crypto crypto = keystore.getCrypto();
            String privateKey = KeyStore.Crypto.decryptCrypto(crypto, password);
            return KeyringFactory.create(address, privateKey);
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

        boolean isRoleBased = privateKeyList.stream().skip(1).anyMatch(array -> array.length > 0);

        if(isRoleBased) {
            return KeyringFactory.createWithRoleBasedKey(address, privateKeyList);
        } else {
            if(privateKeyList.get(0).length > 1) {
                return KeyringFactory.createWithMultipleKey(address, privateKeyList.get(0));
            } else {
                return KeyringFactory.createWithSingleKey(address, privateKeyList.get(0)[0]);
            }
        }
    }




    
    //    /**
//     * Encrypts a key string (normal private key or KlaytnWalletKey format) and returns a keystore object. (according to KeyStoreV4)
//     * @param key A private key string.
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param option The options to use when encrypt a keyring.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encrypt(String key, String password, KeyStoreOption option) throws CipherException{
//        Keyring keyring = null;
//        if(option.getAddress() != null) {
//            if(Utils.isKlaytnWalletKey(key)) {
//                keyring = Keyring.createFromKlaytnWalletKey(key);
//                if (!keyring.address.equals(option.address)) {
//                    throw new RuntimeException("The address defined in options does not match the address of KlaytnWalletKey");
//                }
//
//                keyring = Keyring.createFromKlaytnWalletKey(key);
//            } else {
//                keyring = Keyring.createWithSingleKey(option.getAddress(), key);
//            }
//        } else {
//            keyring = Keyring.createFromPrivateKey(key);
//        }
//        return keyring.encrypt(password, option);
//    }
//
//    /**
//     * Encrypts an multiple private key strings and returns a keystore object. (according to KeyStoreV4)
//     * @param key A private key strings.
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param option The options to use when encrypt a keyring. To encrypt multiple private key strings, address should be defined in option.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encrypt(String[] key, String password, KeyStoreOption option) throws CipherException{
//        if(option.getAddress() == null) {
//            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt multiple keys.");
//        }
//
//        Keyring keyring = Keyring.createWithMultipleKey(option.address, key);
//
//        return keyring.encrypt(password, option);
//    }
//
//    /**
//     * Encrypts a role based private key strings and returns a keystore object. (according to KeyStoreV4)
//     * @param key A List of private key strings
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param option The options to use when encrypt a keyring. To encrypt multiple private key strings, address should be defined in option.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encrypt(List<String[]> key, String password, KeyStoreOption option) throws CipherException{
//        if(option.getAddress() == null) {
//            throw new IllegalArgumentException("The address must be defined inside the option object to encrypt roleBased keys.");
//        }
//
//        Keyring keyring = Keyring.createWithRoleBasedKey(option.address, key);
//
//        return keyring.encrypt(password, option);
//    }
//
//    /**
//     * Encrypts a private key string and returns a keystore object. (according to KeyStoreV4)
//     * @param keyring A Keyring instance
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param option The options to use when encrypt a keyring.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encrypt(Keyring keyring, String password, KeyStoreOption option) throws CipherException {
//        return keyring.encrypt(password, option);
//    }
//
//    /**
//     * Encrypts a keyring instance and returns a keystore object. (according to KeyStoreV3)
//     * @param key A private key string.
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param option The options to use when encrypt a keyring.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encryptV3(String key, String password, KeyStoreOption option) throws CipherException {
//        Keyring keyring = null;
//        if(option.getAddress() != null) {
//            if(Utils.isKlaytnWalletKey(key)) {
//                keyring = Keyring.createFromKlaytnWalletKey(key);
//                if (!keyring.address.equals(option.address)) {
//                    throw new RuntimeException("The address defined in options does not match the address of KlaytnWalletKey");
//                }
//
//                keyring = Keyring.createFromKlaytnWalletKey(key);
//            } else {
//                keyring = Keyring.createWithSingleKey(option.getAddress(), key);
//            }
//        } else {
//            keyring = Keyring.createFromPrivateKey(key);
//        }
//        return keyring.encryptV3(password, option);
//    }
//
//    /**
//     * Encrypts a keyring instance and returns a keystore object. (according to KeyStoreV3)
//     * @param keyring A Keyring instance.
//     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
//     * @param options The options to use when encrypt a keyring.
//     * @return KeyStore
//     * @throws CipherException It throws when cipher operation has failed.
//     */
//    public static KeyStore encryptV3(Keyring keyring, String password, KeyStoreOption options) throws CipherException {
//        return keyring.encryptV3(password, options);
//    }
}
