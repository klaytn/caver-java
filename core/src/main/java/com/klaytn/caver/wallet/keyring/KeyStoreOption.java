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

/**
 * Represent a Crypto Option class using when creating KeyStore instance.
 */
public class KeyStoreOption {
    /**
     * Key Encryption / Decryption option.
     */
    KeyStore.CipherParams cipherParams;

    /**
     * Key derivation algorithm option.
     */
    KeyStore.IKdfParams kdfParams;

    /**
     * An Address used to create a KeyStore with a decoupled Key.
     */
    String address;

    /**
     * Creates an KeyStoreOption instance.
     * @param cipherParams Cipher option.(Key encrypt / decrypt)
     * @param kdfParams Key derivation algorithm option.
     * @param address An address.
     */
    public KeyStoreOption(KeyStore.CipherParams cipherParams, KeyStore.IKdfParams kdfParams, String address) {
        this.cipherParams = cipherParams;
        this.kdfParams = kdfParams;
        this.address = address;
    }

    /**
     * Creates an KeyStoreOption of each option instance has default value.
     * Address is automatically set to null
     * @param kdfName Key derivation algorithm name.
     * @return KeyStoreOption
     */
    public static KeyStoreOption getDefaultOptionWithKDF(String kdfName) {
        return getDefaultOptionWithKDF(kdfName, null);
    }

    /**
     * Creates an KeyStoreOption of each option instance has default value.
     * @param kdfName Key derivation algorithm name.
     * @param address An address.
     * @return KeyStoreOption
     */
    public static KeyStoreOption getDefaultOptionWithKDF(String kdfName, String address) {
        KeyStore.IKdfParams kdfParams;
        if (kdfName.equals(KeyStore.ScryptKdfParams.getName())) {
            kdfParams = new KeyStore.ScryptKdfParams();
        } else if (kdfName.equals(KeyStore.Pbkdf2KdfParams.getName())) {
            kdfParams = new KeyStore.Pbkdf2KdfParams();
        } else {
            throw new IllegalArgumentException("Not supported kdf method.");
        }
        return new KeyStoreOption(new KeyStore.CipherParams(), kdfParams, address);
    }

    /**
     * Getter function of CipherParams.
     * @return KeyStore.CipherParams
     */
    public KeyStore.CipherParams getCipherParams() {
        return cipherParams;
    }

    /**
     * Getter function of KdfParams.
     * @return KeyStore.KdfParams
     */
    public KeyStore.IKdfParams getKdfParams() {
        return kdfParams;
    }

    /**
     * Getter function of address.
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter function of CipherParams.
     * @param cipherParams KeyStore.CipherParams.
     */
    public void setCipherParams(KeyStore.CipherParams cipherParams) {
        this.cipherParams = cipherParams;
    }

    /**
     * Setter function of KdfParams.
     * @param kdfParams KeyStore.KdfParams.
     */
    public void setKdfParams(KeyStore.IKdfParams kdfParams) {
        this.kdfParams = kdfParams;
    }

    /**
     * Setter function of address.
     * @param address An address.
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
