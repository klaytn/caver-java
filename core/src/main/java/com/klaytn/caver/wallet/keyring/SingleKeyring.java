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
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.CipherException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Representing a Keyring which includes 'address' and a 'private key'
 */
public class SingleKeyring extends AbstractKeyring{

    /**
     * The key to use in SingleKeyring.
     */
    PrivateKey key;

    /**
     * Creates a SingleKeyring instance.
     * @param address The address of keyring.
     * @param key The key to use in SingleKeyring.
     */
    public SingleKeyring(String address, PrivateKey key) {
        super(address);
        this.key = key;
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
        PrivateKey key = getKeyByRole(role);
        SignatureData data = key.sign(txHash, chainId);

        return Arrays.asList(data);
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
        validatedIndexWithKeys(index, 1);

        PrivateKey key = getKeyByRole(role);
        SignatureData data = key.sign(txHash, chainId);

        return data;
    }

    /**
     * Signs a hashed data with all key in specific role group and return MessageSigned instance.
     * @param message The data string to sign
     * @param role A number indicating the role of the key
     * @return MessageSigned
     */
    @Override
    public MessageSigned signMessage(String message, int role) {
        PrivateKey key = getKeyByRole(role);
        String messageHash = Utils.hashMessage(message);
        SignatureData signatureData = key.signMessage(messageHash);

        return new MessageSigned(messageHash, Arrays.asList(signatureData), message);

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
        validatedIndexWithKeys(index, 1);

        String messageHash = Utils.hashMessage(message);
        SignatureData signatureData = key.signMessage(messageHash);

        return new MessageSigned(messageHash, Arrays.asList(signatureData), message);
    }

    /**
     * Returns a KlaytnWalletKey format
     * @return String
     */
    @Override
    public String getKlaytnWalletKey() {
        return key.getPrivateKey() + "0x00" + this.getAddress();
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V4)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options  The options to use when encrypt a keyring.
     * @return KeyStore
     */
    @Override
    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<KeyStore.Crypto> crypto = KeyStore.Crypto.createCrypto(new PrivateKey[]{this.key}, password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setKeyring(crypto);

        return keyStore;
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V3)
     * This function automatically operates SCRYPT KDF Function
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @return KeyStore
     */
    @Override
    public KeyStore encryptV3(String password) throws CipherException {
        KeyStoreOption options = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());
        return encryptV3(password, options);
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V3)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options The options to use when encrypt a keyring.
     * @return KeyStore
     */
    @Override
    public KeyStore encryptV3(String password, KeyStoreOption options) throws CipherException {
        List<KeyStore.Crypto> crypto = KeyStore.Crypto.createCrypto(new PrivateKey[]{this.key}, password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V3);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setCrypto(crypto.get(0));

        return keyStore;
    }

    /**
     * Returns true if keyring has decoupled key.
     * @return boolean
     */
    @Override
    public boolean isDecoupled() {
        return !(this.address.toLowerCase().equals(this.key.getDerivedAddress().toLowerCase()));
    }

    /**
     * Returns a copied SingleKeyring instance.
     * @return Keyring
     */
    @Override
    public AbstractKeyring copy() {
        return (AbstractKeyring) new SingleKeyring(this.address, this.key);
    }

    /**
     * Returns an instance of Account
     * @return Account
     */
    public Account toAccount() {
        return Account.createWithAccountKeyPublic(this.address, this.key.getPublicKey(false));
    }

    /**
     * Return a public key strings.<p>
     * It returns a public key as a uncompressed format.
     * @return String
     */
    public String getPublicKey() {
        return getPublicKey(false);
    }

    /**
     * Return a public key strings.<p>
     * @param compressed Whether in compressed format or not.
     * @return String
     */
    public String getPublicKey(boolean compressed) {
        return this.key.getPublicKey(compressed);
    }

    /**
     * returns keys by role. If the key of the role passed as parameter is empty, the default key is returned.
     * @param role A number indicating the role of the key. You can use `AccountRoleBased.RoleGroup`.
     * @return PrivateKey Array
     */
    public PrivateKey getKeyByRole(int role) {
        if(role < 0 || role >= AccountKeyRoleBased.ROLE_GROUP_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.key;
    }

    /**
     * Getter function for key
     * @return PrivateKey
     */
    public PrivateKey getKey() {
        return key;
    }
}
