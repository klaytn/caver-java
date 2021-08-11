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
 * Representing a KeyringFactoryWrapper class which wraps all of static methods of KeyringFactory
 */
public class KeyringFactoryWrapper {

    /**
     * Creates a KeyringFactoryWrapper instance
     */
    public KeyringFactoryWrapper() {
    }

    /**
     * Generates a single type of keyring instance.
     * @return SingleKeyring
     */
    public SingleKeyring generate() {
        return KeyringFactory.generate();
    }

    /**
     * Generates a single type of keyring instance with entropy.
     * @param entropy A random string to create keyring.
     * @return SingleKeyring
     */
    public SingleKeyring generate(String entropy) {
        return KeyringFactory.generate(entropy);
    }

    /**
     * Generates a single private key string.
     * @return String
     */
    public String generateSingleKey() {
        return KeyringFactory.generateSingleKey();
    }

    /**
     * Generates a single private key string with entropy.
     * @param entropy A random string to create private key.
     * @return String
     */
    public String generateSingleKey(String entropy) {
        return KeyringFactory.generateSingleKey(entropy);
    }

    /**
     * Generates an array of private key strings.
     * @param num A length of keys.
     * @return String array
     */
    public String[] generateMultipleKeys(int num) {
        return KeyringFactory.generateMultipleKeys(num);
    }

    /**
     * Generates an array of private key strings with entropy.
     * @param num A length of keys.
     * @param entropy A random string to create private key.
     * @return String array
     */
    public String[] generateMultipleKeys(int num, String entropy) {
        return KeyringFactory.generateMultipleKeys(num, entropy);
    }

    /**
     * Generates an list of private key strings.
     * @param numArr An array containing the number of keys for each role.
     * @return List
     */
    public List<String[]> generateRoleBasedKeys(int[] numArr) {
        return KeyringFactory.generateRoleBasedKeys(numArr);
    }

    /**
     * Generates an list of private key strings.
     * @param numArr An array containing the number of keys for each role.
     * @param entropy A random string to create private key.
     * @return List
     */
    public List<String[]> generateRoleBasedKeys(int[] numArr, String entropy) {
        return KeyringFactory.generateRoleBasedKeys(numArr, entropy);
    }

    /**
     * Creates a single type of keyring instance.
     * @param address The address of keyring.
     * @param key The key of keyring.
     * @return SingleKeyring
     */
    public SingleKeyring create(String address, String key) {
        return KeyringFactory.create(address, key);
    }

    /**
     * Creates a multiple type of keyring instance.
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return MultipleKeyring
     */
    public MultipleKeyring create(String address, String[] keys) {
        return KeyringFactory.create(address, keys);
    }

    /**
     * Creates a roleBased type of keyring instance.
     * @param address The address of keyring.
     * @param keys The key list of keyring.
     * @return RoleBasedKeyring
     */
    public RoleBasedKeyring create(String address, List<String[]> keys) {
        return KeyringFactory.create(address, keys);
    }

    /**
     * Creates a single type of keyring instance with private key.
     * @param key A private key string.
     * @return SingleKeyring
     */
    public SingleKeyring createFromPrivateKey(String key) {
        return KeyringFactory.createFromPrivateKey(key);
    }

    /**
     * Creates a single type of keyring instance from KlaytnWalletKey string.
     * @param klaytnWalletKey A key string in KlaytnWalletKey format.
     * @return SingleKeyring
     */
    public SingleKeyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        return KeyringFactory.createFromKlaytnWalletKey(klaytnWalletKey);
    }

    /**
     * Creates a single type of keyring instance from address and private key string.
     * @param address An address of keyring.
     * @param key A private key string.
     * @return SingleKeyring
     */
    public SingleKeyring createWithSingleKey(String address, String key) {
        return KeyringFactory.createWithSingleKey(address, key);
    }

    /**
     * Creates a multiple type of keyring instance from address and private key strings.
     * @param address An address of keyring.
     * @param multipleKey An array of private key strings.
     * @return MultipleKeyring
     */
    public MultipleKeyring createWithMultipleKey(String address, String[] multipleKey) {
        return KeyringFactory.createWithMultipleKey(address, multipleKey);
    }

    /**
     * Create a roleBased type of keyring instance from address and private key strings.
     * @param address An address of keyring.
     * @param roleBasedKey A List of private key strings.
     * @return RoleBasedKeyring
     */
    public RoleBasedKeyring createWithRoleBasedKey(String address, List<String[]> roleBasedKey) {
        return KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);
    }

    /**
     * Decrypts a KeyStore json string and returns a keyring instance.
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
     * @param keystore The encrypted keystore to decrypt.
     * @param password The password to use for decryption.
     * @return AbstractKeyring
     * @throws CipherException It throws when cipher operation has failed.
     */
    public AbstractKeyring decrypt(KeyStore keystore, String password) throws CipherException{
        return KeyringFactory.decrypt(keystore, password);
    }
}
