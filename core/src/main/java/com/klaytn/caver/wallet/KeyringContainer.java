package com.klaytn.caver.wallet;

import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.Keyring;
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
    Map<String, Keyring> addressKeyringMap = new HashMap<>();


    /**
     * Creates KeyringContainer instance
     * @param keyrings An list of keyring
     */
    public KeyringContainer() {}


    /**
     * Creates KeyringContainer instance
     * @param keyrings An list of keyring
     */
    public KeyringContainer(List<Keyring> keyrings) {
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
            Keyring keyring = Keyring.generate(entropy);
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
     * KeyringContainer manages Keyring instance using Map <string:Keyring> which has address as key value.
     * @param address The address of the keyring
     * @param key Private key string
     * @return Keyring
     */
    public Keyring newKeyring(String address, String key) {
        Keyring keyring = Keyring.createWithSingleKey(address, key);

        return this.add(keyring);
    }

    /**
     * Creates a multiple type keyring instance with given parameters and add it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map <string:Keyring> which has address as key value.
     * @param address The address of the keyring
     * @param keys An array of private keys
     * @return Keyring
     */
    public Keyring newKeyring(String address, String[] keys) {
        Keyring keyring = Keyring.createWithMultipleKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Creates a role-basd type keyring instance with given parameters and add it to the keyringContainer.
     * KeyringContainer manages Keyring instance using Map <string:Keyring> which has address as key value.
     * @param address The address of the keyring
     * @param keys A List of private key array
     * @return Keyring
     */
    public Keyring newKeyring(String address, List<String[]> keys) {
        Keyring keyring = Keyring.createWithRoleBasedKey(address, keys);

        return this.add(keyring);
    }

    /**
     * Updates the keyring inside the keyringContainer.
     * Query the keyring to be updated from keyringContainer with the keyring's address,
     * and an error occurs when the keyring is not found in the keyringContainer.
     * @param keyring The keyring with new key
     * @return Keyring
     */
    public Keyring updateKeyring(Keyring keyring) {
        Keyring founded = this.getKeyring(keyring.getAddress());
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
    public Keyring getKeyring(String address) {
        if(!Utils.isAddress(address)) {
            throw new IllegalArgumentException("Invalid address. To get keyring from wallet, you need to pass a valid address string as a parameter.");
        }

        Keyring found = this.addressKeyringMap.get(address.toLowerCase());
        return found;
    }

    /**
     * Adds a keyring to the keyringContainer.
     */
    public Keyring add(Keyring keyring) {
        if (this.getKeyring(keyring.getAddress()) != null) {
            throw new IllegalArgumentException("Duplicated Account. Please use updateKeyring() instead");
        }

        Keyring added = keyring.copy();
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
        Keyring removed = this.addressKeyringMap.remove(address);
        removed = null;

        return true;
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', 'messageHash'
     * It automatically set 'roleIndex' and 'keyIndex' to 0.
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sing
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data) {
        return this.signMessage(address, data, 0, 0);
    }

    /**
     * Signs with data and returns MessageSigned instance that includes 'signature', 'message', 'messageHash'
     * @param address An address of keyring in keyringContainer
     * @param data The data string to sing
     * @param role A number indication the role of the key.
     * @param index An index of key to use for signing.
     * @return MessageSigned
     */
    public MessageSigned signMessage(String address, String data, int role, int index) {
        Keyring keyring = this.getKeyring(address);
        if(keyring == null) {
            throw new NullPointerException("Failed to find keyring from wallet with address");
        }

        return keyring.signMessage(data, role, index);
    }


//    public AbstractTransaction signWithKey(String address, AbstractTransaction transaction) { }

//    public AbstractTransaction signWithKey(String address, AbstractTransaction transaction, TransactionHasher hasher) { }

//    public AbstractTransaction signWithKey(String address, AbstractTransaction transaction, int index) {}

//    public AbstractTransaction signWithKey(String address, AbstractTransaction transaction, int index, TransactionHasher hasher) { }

//    public AbstractTransaction signWithKeys(String address, AbstractTransaction transaction) {}

//    public AbstractTransaction signWithKeys(String address, AbstractTransaction transaction, TransactionHasher hasher) {}

//    public AbstractTransaction signFeePayerWithKey(String address, AbstractTransaction transaction) {}

//    public AbstractTransaction signFeePayerWithKey(String address, AbstractTransaction transaction, TransactionHasher hasher) {}

//    public AbstractTransaction signFeePayerWithKey(String address, AbstractTransaction transaction, int index) {}

//    public AbstractTransaction signFeePayerWithKey(String address, AbstractTransaction transaction, int index, TransactionHasher hasher) {}

//    public AbstractTransaction signFeePayerWithKeys(String address, AbstractTransaction transaction) {}

//    public AbstractTransaction signFeePayerWithKeys(String address, AbstractTransaction transaction, TransactionHasher hasher) {}
}
