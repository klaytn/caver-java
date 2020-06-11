package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.utils.Utils;
import org.web3j.crypto.CipherException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MultipleKeyring extends AbstractKeyring{
    PrivateKey[] keys;

    public MultipleKeyring(String address, PrivateKey[] keys) {
        super(address);
        this.keys = keys;
    }

    @Override
    public List<SignatureData> sign(String txHash, int chainId, int role) {
        PrivateKey[] keyArr = getKeyByRole(role);

        return Arrays.stream(keyArr)
                .map(key-> {
                    return key.sign(txHash, chainId);
                }).collect(Collectors.toList());
    }

    @Override
    public SignatureData sign(String txHash, int chainId, int role, int index) {
        validatedIndexWithKeys(index, this.keys.length);

        PrivateKey key = getKeyByRole(role)[index];
        SignatureData signatureData = key.sign(txHash, chainId);

        return signatureData;
    }

    @Override
    public MessageSigned signMessage(String message, int role) {
        PrivateKey[] keyArr = getKeyByRole(role);
        String messageHash = Utils.hashMessage(message);

        List<SignatureData> signatureDataList = Arrays.stream(keyArr)
                .map(key -> {
                    return key.signMessage(messageHash);
                }).collect(Collectors.toCollection(ArrayList::new));

        MessageSigned signed = new MessageSigned(messageHash, signatureDataList, message);

        return signed;
    }

    @Override
    public MessageSigned signMessage(String message, int role, int index) {
        validatedIndexWithKeys(index, this.keys.length);

        PrivateKey key = getKeyByRole(role)[index];
        String messageHash = Utils.hashMessage(message);

        SignatureData signatureData = key.signMessage(messageHash);
        MessageSigned signed = new MessageSigned(messageHash, Arrays.asList(signatureData), message);

        return signed;
    }

    @Override
    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<KeyStore.Crypto> cryptoList = KeyStore.Crypto.createCrypto(this.keys, password, options);

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());
        keyStore.setKeyring(cryptoList);

        return keyStore;
    }

    @Override
    public AbstractKeyring copy() {
        return new MultipleKeyring(address, keys);
    }

    public String[] getPublicKey() {
        return Arrays.stream(this.keys).map(key -> {
            return key.getPublicKey(false);
        }).toArray(String[]::new);
    }

    public PrivateKey[] getKeyByRole(int role) {
        if(role < 0 || role > AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.keys;
    }

    public Account toAccount() {
        WeightedMultiSigOptions options = WeightedMultiSigOptions.getDefaultOptionsForWeightedMultiSig(this.getPublicKey());
        return toAccount(options);
    }

    public Account toAccount(WeightedMultiSigOptions options) {
        return Account.createWithAccountKeyWeightedMultiSig(address, this.getPublicKey(), options);
    }

    public PrivateKey[] getKeys() {
        return keys;
    }
}
