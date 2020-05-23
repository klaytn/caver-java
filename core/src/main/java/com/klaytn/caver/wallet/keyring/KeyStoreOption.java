package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.wallet.Wallet;
import org.web3j.utils.Numeric;

public class KeyStoreOption {
    KeyStore.CipherParams cipherParams;
    KeyStore.KdfParams kdfParams;
    String address;

    public KeyStoreOption(KeyStore.CipherParams cipherParams, KeyStore.KdfParams kdfParams, String address) {
        this.cipherParams = cipherParams;
        this.kdfParams = kdfParams;
        this.address = address;
    }

    public static KeyStoreOption getDefaultOptionWithKDF(String kdfName) {
        return getDefaultOptionWithKDF(kdfName, null);
    }

    public static KeyStoreOption getDefaultOptionWithKDF(String kdfName, String address) {
        KeyStore.KdfParams kdfParams = null;
        if (kdfName.equals(KeyStore.ScryptKdfParams.getName())) {
            kdfParams = new KeyStore.ScryptKdfParams();
        } else if (kdfName.equals(KeyStore.Pbkdf2KdfParams.getName())) {
            kdfParams = new KeyStore.Pbkdf2KdfParams();
        } else {
            throw new IllegalArgumentException("Not supported kdf method.");
        }
        return new KeyStoreOption(new KeyStore.CipherParams(), kdfParams, address);
    }

    public KeyStore.CipherParams getCipherParams() {
        return cipherParams;
    }

    public KeyStore.KdfParams getKdfParams() {
        return kdfParams;
    }

    public String getAddress() {
        return address;
    }

    public void setCipherParams(KeyStore.CipherParams cipherParams) {
        this.cipherParams = cipherParams;
    }

    public void setKdfParams(KeyStore.KdfParams kdfParams) {
        this.kdfParams = kdfParams;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
