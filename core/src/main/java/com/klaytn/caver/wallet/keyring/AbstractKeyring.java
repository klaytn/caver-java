package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.CipherException;
import org.web3j.utils.Numeric;

import java.util.List;

abstract public class AbstractKeyring {
    /**
     * The keyring address.
     */
    String address;

    /**
     * Create a AbstractKeyring instance.
     * @param address
     */
    public AbstractKeyring(String address) {
        this.address = address;
    }

    /**
     * Signs a transaction hash with all keys in specific role group and return signature list.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @return List
     */
    abstract public List<SignatureData> sign(String txHash, int chainId, int role);

    /**
     * Signs a transaction hash with key in specific role group and return signature.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @param index The index of the key to be used in the specific role group.
     * @return SignatureData
     */
    abstract public SignatureData sign(String txHash, int chainId, int role, int index);

    /**
     * Signs a hashed data with all key in specific role group and return MessageSigned instance.
     * @param message The data string to sign
     * @param role A number indicating the role of the key
     * @return MessageSigned
     */
    abstract public MessageSigned signMessage(String message, int role);

    /**
     * Signs a hashed data with key in specific role group and return MessageSigned instance.
     * @param message
     * @param role
     * @param index
     * @return
     */
    abstract public MessageSigned signMessage(String message, int role, int index);

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V4)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options  The options to use when encrypt a keyring.
     * @return KeyStore
     */
    abstract public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException;

    /**
     * Returns a copied keyring instance.
     * @return Keyring
     */
    abstract public AbstractKeyring copy();

    /**
     * Signs a transaction hash with all keys in specific role group and return signature list.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @return List
     */
    public List<SignatureData> sign(String txHash, String chainId, int role) {
        if(Utils.isNumber(chainId)) throw new IllegalArgumentException("Invalid chainId : " + chainId);

        return sign(txHash, Numeric.toBigInt(chainId).intValue(), role);
    }

    /**
     * Signs a transaction hash with key in specific role group and return signature.
     * @param txHash The hash of transaction.
     * @param chainId The chainId specific to the network.
     * @param role A number indicating the role of the key.
     * @param index The index of the key to be used in the specific role group.
     * @return SignatureData
     */
    public SignatureData sign(String txHash, String chainId, int role, int index) {
        if(Utils.isNumber(chainId)) throw new IllegalArgumentException("Invalid chainId : " + chainId);

        return sign(txHash, Numeric.toBigInt(chainId).intValue(), role, index);
    }

    /**
     * Encrypts a keyring and returns a KeyStore.(according to KeyStore V3)
     * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
     * @param options The options to use when encrypt a keyring.
     * @return KeyStore
     */
    public KeyStore encryptV3(String password, KeyStoreOption options) throws CipherException {
        throw new RuntimeException("This keyring cannot be encrypted keystore v3. Use 'encrypt()' function");
    }

    /**
     * Returns a KlaytnWalletKey format
     * @return String
     */
    public String getKlaytnWalletKey() {
        throw new RuntimeException("The keyring cannot be exported in KlaytnWalletKey format. Use keyring.encrypt.");
    }

    /**
     * Returns true if keyring has decoupled key.
     * @return boolean
     */
    public boolean isDecoupled() {
        return true;
    }

    boolean validatedIndexWithKeys(int index, int keyLength) {
        if(index < 0) throw new IllegalArgumentException("Invalid index : index cannot be negative");
        if(index >= keyLength) throw new IllegalArgumentException("Invalid index : index must be less than the length of the key.");

        return true;
    }

    public String getAddress() {
        return address;
    }
}
