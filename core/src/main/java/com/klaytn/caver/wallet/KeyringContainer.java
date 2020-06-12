package com.klaytn.caver.wallet;

import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.MessageSigned;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Keyring container which manages keyring
 */
public class KeyringContainer {
    /**
     * The map where address and keyring are mapped
     */
    Map<String, AbstractKeyring> addressKeyringMap = new HashMap<>();


    /**
     * Creates KeyringContainer instance
     */
    public KeyringContainer() {}


    /**
     * Creates KeyringContainer instance
     * @param keyrings An list of keyring
     */
    public KeyringContainer(List<AbstractKeyring> keyrings) {
        keyrings.stream().forEach(this::add);
    }

    /**
     * Generates keyrings in the keyring container with randomly generated key pairs.
     * @param numberOfKeyrings The number of keyring to create.
     * @return List of address generated Keyring instances
     */
    public List<String> generate(int numberOfKeyrings) {
        return this.generate(numberOfKeyrings, null);
    }

    /**
     * Generates keyrings in the keyring container with randomly generated key pairs.
     * @param numberOfKeyrings The number of keyring to create.
     * @param entropy A random string to increase entropy.
     * @return List of address generated Keyring instances
     */
    public List<String> generate(int numberOfKeyrings, String entropy) {
        List<String> addressList = new ArrayList<>();
        for(int i=0; i<numberOfKeyrings; i++) {
            AbstractKeyring keyring = KeyringFactory.generate(entropy);
            addressList.add(keyring.getAddress());
            this.add(keyring);
        }

        return addressList;
    }

    /**
     * Returns a Keyring instance count in KeyringContainer
     * @return int
     */
    public int length() {
        return this.addressKeyringMap.size();
    }

    /**
     * Creates a single type keyring instance with given parameters and adds it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.
     * @param address The address of the keyring
     * @param key Private key string
     * @return Keyring
     */
    public AbstractKeyring newKeyring(String address, String key) {
        AbstractKeyring keyring = KeyringFactory.createWithSingleKey(address, key);

        return this.add(keyring);
    }

    /**
     * Creates a multiple type keyring instance with given parameters and add it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.
     * @param address The address of the keyring
     * @param keys An array of private keys
     * @return Keyring
     */
    public AbstractKeyring newKeyring(String address, String[] keys) {
        AbstractKeyring keyring = KeyringFactory.createWithMultipleKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Creates a role-basd type keyring instance with given parameters and add it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map {string:Keyring} which has address as key value.
     * @param address The address of the keyring
     * @param keys A List of private key array
     * @return Keyring
     */
    public AbstractKeyring newKeyring(String address, List<String[]> keys) {
        AbstractKeyring keyring = KeyringFactory.createWithRoleBasedKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Updates the keyring inside the keyringContainer.
     * Query the keyring to be updated from keyringContainer with the keyring's address,
     * and an error occurs when the keyring is not found in the keyringContainer.
     * @param keyring The keyring with new key
     * @return Keyring
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
     * Get the keyring in container corresponding to the address.
     * @param address The address of keyring to query
     * @return Keyring
     */
    public AbstractKeyring getKeyring(String address) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("Invalid address. To get keyring from wallet, you need to pass a valid address string as a parameter.");
        }

        AbstractKeyring found = this.addressKeyringMap.get(address.toLowerCase());
        return found;
    }

    /**
     * Adds a keyring to the keyringContainer.
     * @param keyring Keyring instance to be added.
     * @return Keyring
     */
    public AbstractKeyring add(AbstractKeyring keyring) {
        if (this.getKeyring(keyring.getAddress()) != null) {
            throw new IllegalArgumentException("Duplicated Account. Please use updateKeyring() instead");
        }

        AbstractKeyring added = keyring.copy();
        this.addressKeyringMap.put(keyring.getAddress(), added);

        return added;
    }

    /**
     * Deletes the keyring that associates with the given address from keyringContainer.
     * @param address An address of the keyring to be deleted in keyringContainer
     * @return boolean
     */
    public boolean remove(String address) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("To remove keyring, the first parameter should be an address string");
        }

        //deallocate keyring object created for keyringContainer.
        AbstractKeyring removed = this.addressKeyringMap.remove(address);
        removed = null;

        return true;
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', 'messageHash'
     * It automatically set 'roleIndex' and 'keyIndex' to 0.
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sign
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data) {
        return this.signMessage(address, data, 0, 0);
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', 'messageHash'
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sign
     * @param role A number indication the role of the key.
     * @param index An index of key to use for signing.
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data, int role, int index) {
        AbstractKeyring keyring = this.getKeyring(address);
        if(keyring == null) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return keyring.signMessage(data, role, index);
    }


//    public AbstractTransaction sign(String address, AbstractTransaction transaction) { }
//
//    public AbstractTransaction sign(String address, AbstractTransaction transaction, TransactionHasher hasher) { }
//
//    public AbstractTransaction sign(String address, AbstractTransaction transaction, int index) {}
//
//    public AbstractTransaction sign(String address, AbstractTransaction transaction, int index, TransactionHasher hasher) { }
//
//    public AbstractTransaction signAsFeePayer(String address, AbstractTransaction transaction) {}
//
//    public AbstractTransaction signAsFeePayer(String address, AbstractTransaction transaction, TransactionHasher hasher) {}
//
//    public AbstractTransaction signAsFeePayer(String address, AbstractTransaction transaction, int index) {}
//
//    public AbstractTransaction signAsFeePayer(String address, AbstractTransaction transaction, int index, TransactionHasher hasher) {}
}
