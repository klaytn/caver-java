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

package com.klaytn.caver.wallet;

import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.wrapper.KeyringFactoryWrapper;
import com.klaytn.caver.wallet.keyring.MessageSigned;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a Keyring container which manages keyring.<p>
 * To access it from Caver instance, it can be accessed through `caver.wallet`.
 * @see KeyringFactory
 * @see AbstractKeyring
 * @see com.klaytn.caver.wallet.keyring.SingleKeyring
 * @see com.klaytn.caver.wallet.keyring.MultipleKeyring
 * @see com.klaytn.caver.wallet.keyring.RoleBasedKeyring
 */
public class KeyringContainer implements IWallet{
    /**
     * The map where address and keyring are mapped
     */
    Map<String, AbstractKeyring> addressKeyringMap = new HashMap<>();

    /**
     * The KeyringFactoryWrapper instance
     * This is added to improve "SDK User Experience"
     * which means giving developers similar development experience with caver-js
     */
    public KeyringFactoryWrapper keyring;

    /**
     * Creates KeyringContainer instance.
     */
    public KeyringContainer() {
        keyring = new KeyringFactoryWrapper();
    }


    /**
     * Creates KeyringContainer instance.
     * @param keyrings An list of keyring
     */
    public KeyringContainer(List<AbstractKeyring> keyrings) {
        keyring = new KeyringFactoryWrapper();
        keyrings.stream().forEach(this::add);
    }

    /**
     * Generates keyrings in the keyring container with randomly generated key pairs.<p>
     * <pre>Example :
     * {@code
     * List<String> addressList = caver.wallet.generate(3);
     * }
     * </pre>
     *
     * @param num The number of keyring to create.
     * @return List of address generated Keyring instances
     */
    @Override
    public List<String> generate(int num) {
        return this.generate(num, null);
    }

    /**
     * Generates keyrings in the keyring container with randomly generated key pairs.<p>
     * <pre>Example :
     * {@code
     * List<String> addressList = caver.wallet.generate(3, "entropy");
     * }
     * </pre>
     *
     * @param num The number of keyring to create.
     * @param entropy A random string to increase entropy.
     * @return List of address generated Keyring instances
     */
    public List<String> generate(int num, String entropy) {
        List<String> addressList = new ArrayList<>();
        for(int i=0; i<num; i++) {
            AbstractKeyring keyring = KeyringFactory.generate(entropy);
            addressList.add(keyring.getAddress());
            this.add(keyring);
        }

        return addressList;
    }

    /**
     * Returns the number of keyrings in the KeyringContainer.<p>
     * <pre>Example :
     * {@code
     * int size = caver.wallet.length();
     * }
     * </pre>
     *
     * @return int
     */
    public int length() {
        return this.addressKeyringMap.size();
    }

    /**
     * Creates a single type keyring instance with given parameters and adds it to the keyringContainer.<p>
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.<p>
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String key = caver.wallet.keyring.generateSingleKey();
     *
     * SingleKeyring keyring = (SingleKeyring)caver.wallet.newKeyring(address, key);
     * }
     * </pre>
     *
     * @param address The address of the keyring
     * @param key Private key string
     * @return AbstractKeyring(SingleKeyring)
     */
    public AbstractKeyring newKeyring(String address, String key) {
        AbstractKeyring keyring = KeyringFactory.createWithSingleKey(address, key);

        return this.add(keyring);
    }

    /**
     * Creates a multiple type keyring instance with given parameters and add it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String[] keyArray = new String[] {caver.wallet.keyring.generateSingleKey(), caver.wallet.keyring.generateSingleKey(), ....};
     * MultipleKeyring keyring = (MultipleKeyring)caver.wallet.newKeyring(address, keyArray);
     * }
     * </pre>
     *
     * @param address The address of the keyring
     * @param keys An array of private keys
     * @return AbstractKeyring(MultipleKeyring)
     */
    public AbstractKeyring newKeyring(String address, String[] keys) {
        AbstractKeyring keyring = KeyringFactory.createWithMultipleKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Creates a role-basd type keyring instance with given parameters and add it to the keyringContainer.<p>
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.<p>
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
     * RoleBasedKeyring added = (RoleBasedKeyring)caver.wallet.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
     * }
     * </pre>
     *
     * @param address The address of the keyring
     * @param keys A List of private key array
     * @return AbstractKeyring(RoleBasedKeyring)
     */
    public AbstractKeyring newKeyring(String address, List<String[]> keys) {
        AbstractKeyring keyring = KeyringFactory.createWithRoleBasedKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Updates the keyring inside the keyringContainer.<p>
     * Query the keyring to be updated from keyringContainer with the keyring's address, <p>
     * and an error occurs when the keyring is not found in the keyringContainer.<p>
     * <pre>Example :
     * {@code
     * String updatedAddress = "0x{address}";
     * String newPrivateKey = caver.wallet.keyring.generateSingleKey();
     * SingleKeyring newKeyring = (SingleKeyring)caver.wallet.newKeyring(updatedAddress, newPrivateKey);
     *
     * caver.wallet.updateKeyring(newKeyring);
     * }
     * </pre>
     *
     * @param keyring The keyring with new key
     * @return AbstractKeyring
     */
    public AbstractKeyring updateKeyring(AbstractKeyring keyring) {
        AbstractKeyring founded = this.getKeyring(keyring.getAddress());
        if(founded == null) {
            throw new IllegalArgumentException("Failed to find keyring to update.");
        }

        this.remove(keyring.getAddress());
        return this.add(keyring);
    }

    /**
     * Returns the keyring in container corresponding to the address.<p>
     * <pre>Example :
     * String address = "0x{address}";
     * AbstractKeyring keyring = caver.wallet.getKeyring(address);
     * </pre>
     *
     * @param address The address of keyring to query
     * @return AbstractKeyring
     */
    public AbstractKeyring getKeyring(String address) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("Invalid address. To get keyring from wallet, you need to pass a valid address string as a parameter.");
        }

        AbstractKeyring found = this.addressKeyringMap.get(address.toLowerCase());
        return found;
    }

    /**
     * Adds a keyring to the keyringContainer.<p>
     * <pre>Example :
     * {@code
     * SingleKeyring keyring = caver.wallet.keyring.generate();
     * caver.wallet.add(keyring);
     * }
     * </pre>
     * @param keyring Keyring instance to be added.
     * @return AbstractKeyring
     */
    public AbstractKeyring add(AbstractKeyring keyring) {
        if (this.getKeyring(keyring.getAddress()) != null) {
            throw new IllegalArgumentException("Duplicated Account. Please use updateKeyring() instead");
        }

        AbstractKeyring added = keyring.copy();
        this.addressKeyringMap.put(keyring.getAddress().toLowerCase(), added);

        return added;
    }

    /**
     * Deletes the keyring that associates with the given address from keyringContainer.<p>
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * caver.wallet.remove(address);
     * }
     * </pre>
     *
     * @param address An address of the keyring to be deleted in keyringContainer
     * @return boolean
     */
    @Override
    public boolean remove(String address) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("To remove keyring, the first parameter should be an address string");
        }

        if(!isExisted(address)) {
            return false;
        }
        //deallocate keyring object created for keyringContainer.
        AbstractKeyring removed = this.addressKeyringMap.remove(address.toLowerCase());
        removed = null;

        return true;
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', and 'messageHash'.<p>
     * It automatically set both 'roleIndex' and 'keyIndex' to 0.<p>
     * <pre>Example :
     * {@code
     * String address = "0x{address in KeyringContainer}";
     * String data = "hello";
     *
     * MessageSigned signed = caver.wallet.signMessage(address, data);
     * }
     * </pre>
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sign
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data) {
        return this.signMessage(address, data, 0, 0);
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', and 'messageHash'.<p>
     * <pre>Example :
     * {@code
     * String address = "0x{address in KeyringContainer}";
     * String data = "hello";
     * int role = RoleGroup.TRANSACTION;
     * int index = 0;
     *
     * MessageSigned signed = caver.wallet.signMessage(address, data, role, index);
     * }
     * </pre>
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sign.
     * @param role A number indication the role of the key. see {@link com.klaytn.caver.account.AccountKeyRoleBased.RoleGroup}.
     * @param index An index of key to use for signing.
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data, int role, int index) {
        if(!isExisted(address)) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return this.getKeyring(address).signMessage(data, role, index);
    }

    /**
     * Signs the transaction as a sender of the transaction and appends signatures in the transaction instance using all keys in the Keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractTransaction signedTx = caver.wallet.sign("0x{address}", transaction);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractTransaction instance to sign.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(String address, AbstractTransaction transaction) throws IOException {
        return sign(address, transaction, TransactionHasher::getHashForSignature);
    }

    /**
     * Signs the transaction as a sender of the transaction and appends signatures in the transaction instance using all keys in the Keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractTransaction signedTx = caver.wallet.sign("0x{address}", transaction, TransactionHasher::getHashForSignature);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractTransaction instance to sign.
     * @param hasher A function to return hash of transaction.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction sign(String address, AbstractTransaction transaction, Function<AbstractTransaction, String> hasher) throws  IOException{
        if(!isExisted(address)) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return transaction.sign(this.getKeyring(address), hasher);
    }

    /**
     * Signs the transaction as a sender of the transaction and appends signatures in the transaction instance using one key in the keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractTransaction signedTx = caver.wallet.sign("0x{address}", transaction, 0);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractTransaction instance to sign
     * @param index An index of key to use for signing.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction sign(String address, AbstractTransaction transaction, int index) throws IOException {
        return sign(address, transaction, index, TransactionHasher::getHashForSignature);
    }

    /**
     * Signs the transaction as a sender of the transaction and appends signatures in the transaction instance using one key in the keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractTransaction signedTx = caver.wallet.sign("0x{address}", transaction, 0, TransactionHasher::getHashForSignature);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractTransaction instance to sign.
     * @param index An index of key to use for signing.
     * @param hasher A function to return hash of transaction.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction sign(String address, AbstractTransaction transaction, int index, Function<AbstractTransaction, String> hasher) throws IOException {
        if(!isExisted(address)) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return transaction.sign(this.getKeyring(address), index, hasher);
    }

    /**
     * Signs the FeeDelegatedTransaction as a fee payer of the transaction and appends feePayerSignatures in the transaction instance using all keys in the keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractFeeDelegatedTransaction signedTx = caver.wallet.signAsFeePayer("0x{address}", transaction);
     * }
     * </pre>
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    @Override
    public AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction) throws IOException {
        return signAsFeePayer(address, transaction, TransactionHasher::getHashForFeePayerSignature);
    }

    /**
     * Signs the FeeDelegatedTransaction as a fee payer of the transaction and appends feePayerSignatures in the transaction instance using all keys in the keyring instance corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractFeeDelegatedTransaction signedTx = caver.wallet.signAsFeePayer("0x{address}", transaction, TransactionHasher::getHashForSignature);
     * }
     * </pre>
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @param hasher A function to return hash of transaction.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(!isExisted(address)) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return transaction.signAsFeePayer(this.getKeyring(address), hasher);
    }

    /**
     * Signs the FeeDelegatedTransaction as a fee payer of the transaction and appends feePayerSignatures in the transaction instance  using one key in the keyring corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractFeeDelegatedTransaction signedTx = caver.wallet.signAsFeePayer("0x{address}", transaction, 0);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @param index An index of key to use for signing.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction, int index) throws IOException {
        return signAsFeePayer(address, transaction, index, TransactionHasher::getHashForFeePayerSignature);
    }

    /**
     * Signs the FeeDelegatedTransaction as a fee payer of the transaction and appends feePayerSignatures in the transaction instance using one key in the keyring corresponding to the address.<p>
     * <pre>Example :
     * {@code
     * AbstractFeeDelegatedTransaction signedTx = caver.wallet.signAsFeePayer("0x{address}", transaction, 0, TransactionHasher::getHashForSignature);
     * }
     * </pre>
     *
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @param index An index of key to user for signing
     * @param hasher A function to return hash of transaction.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction, int index, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(!isExisted(address)) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return transaction.signAsFeePayer(this.getKeyring(address), index, hasher);
    }

    /**
     * Returns true if there is a keyring matching the given address in the wallet.<p>
     * <pre>Exampe :
     * {@code
     * boolean isExist = caver.wallet.isExisted("0x{address}");
     * }
     * </pre>
     * @param address An address to find keyring in wallet.
     * @return boolean
     */
    @Override
    public boolean isExisted(String address) {
        return this.getKeyring(address) != null;
    }
}
