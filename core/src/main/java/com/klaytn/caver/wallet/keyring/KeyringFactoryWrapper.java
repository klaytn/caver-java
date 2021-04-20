package com.klaytn.caver.wallet.keyring;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.List;

public class KeyringFactoryWrapper {
    public KeyringFactoryWrapper() {
    }

    public SingleKeyring generate() {
        return KeyringFactory.generate();
    }
    public SingleKeyring generate(String entropy) {
        return KeyringFactory.generate(entropy);
    }

    public String generateSingleKey() {
        return KeyringFactory.generateSingleKey();
    }

    public String generateSingleKey(String entropy) {
        return KeyringFactory.generateSingleKey(entropy);
    }

    public String[] generateMultipleKeys(int num) {
        return KeyringFactory.generateMultipleKeys(num);
    }

    public String[] generateMultipleKeys(int num, String entropy) {
        return KeyringFactory.generateMultipleKeys(num, entropy);
    }

    public List<String[]> generateRolBasedKeys(int[] numArr) {
        return KeyringFactory.generateRolBasedKeys(numArr);
    }

    public List<String[]> generateRoleBasedKeys(int[] numArr, String entropy) {
        return KeyringFactory.generateRoleBasedKeys(numArr, entropy);
    }

    public SingleKeyring create(String address, String key) {
        return KeyringFactory.create(address, key);
    }

    public MultipleKeyring create(String address, String[] keys) {
        return KeyringFactory.create(address, keys);
    }

    public RoleBasedKeyring create(String address, List<String[]> keys) {
        return KeyringFactory.create(address, keys);
    }

    public SingleKeyring createFromPrivateKey(String key) {
        return KeyringFactory.createFromPrivateKey(key);
    }

    public SingleKeyring createFromKlaytnWalletKey(String klaytnWalletKey) {
        return KeyringFactory.createFromKlaytnWalletKey(klaytnWalletKey);
    }

    public SingleKeyring createWithSingleKey(String address, String key) {
        return KeyringFactory.createWithSingleKey(address, key);
    }

    public MultipleKeyring createWithMultipleKey(String address, String[] multipleKey) {
        return KeyringFactory.createWithMultipleKey(address, multipleKey);
    }

    public RoleBasedKeyring createWithRoleBasedKey(String address, List<String[]> roleBasedKey) {
        return KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);
    }

    public AbstractKeyring decrypt(String keyStore, String password) throws CipherException, IOException {
        return KeyringFactory.decrypt(keyStore, password);
    }

    public AbstractKeyring decrypt(KeyStore keystore, String password) throws CipherException{
        return KeyringFactory.decrypt(keystore, password);
    }
}
