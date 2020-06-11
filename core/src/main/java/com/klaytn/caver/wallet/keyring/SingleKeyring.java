package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.CipherException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SingleKeyring extends AbstractKeyring{
    PrivateKey key;

    public SingleKeyring(String address, PrivateKey key) {
        super(address);
        this.key = key;
    }

    @Override
    public List<SignatureData> sign(String txHash, int chainId, int role) {
        PrivateKey key = getKeyByRole(role);
        SignatureData data = key.sign(txHash, chainId);

        return Arrays.asList(data);
    }

    @Override
    public SignatureData sign(String txHash, int chainId, int role, int index) {
        validatedIndexWithKeys(index, 1);

        PrivateKey key = getKeyByRole(role);
        SignatureData data = key.sign(txHash, chainId);

        return data;
    }

    @Override
    public MessageSigned signMessage(String message, int role) {
        PrivateKey key = getKeyByRole(role);
        String messageHash = Utils.hashMessage(message);
        SignatureData signatureData = key.signMessage(messageHash);

        return new MessageSigned(messageHash, Arrays.asList(signatureData), message);

    }

    @Override
    public MessageSigned signMessage(String message, int role, int index) {
        validatedIndexWithKeys(index, 1);

        String messageHash = Utils.hashMessage(message);
        SignatureData signatureData = key.signMessage(messageHash);

        return new MessageSigned(messageHash, Arrays.asList(signatureData), message);
    }

    @Override
    public String getKlaytnWalletKey() {
        return key.getPrivateKey() + "0x00" + this.getAddress();
    }

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

    @Override
    public boolean isDecoupled() {
        return !(this.address.toLowerCase().equals(this.key.getDerivedAddress().toLowerCase()));
    }

    @Override
    public AbstractKeyring copy() {
        return (AbstractKeyring) new SingleKeyring(this.address, this.key);
    }

    public Account toAccount() {
        return Account.createWithAccountKeyPublic(this.address, this.key.getPublicKey(false));
    }

    public String getPublicKey() {
        return this.key.getPublicKey(false);
    }

    public PrivateKey getKeyByRole(int role) {
        if(role < 0 || role > AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.key;
    }

    public PrivateKey getKey() {
        return key;
    }
}
