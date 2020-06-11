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

public class RoleBasedKeyring extends AbstractKeyring {

    List<PrivateKey[]> keys;

    public RoleBasedKeyring(String address, List<PrivateKey[]> keys) {
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
        PrivateKey[] keyArr = getKeyByRole(role);
        validatedIndexWithKeys(index, keyArr.length);

        PrivateKey key = keyArr[index];
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
        PrivateKey[] keyArr = getKeyByRole(role);
        validatedIndexWithKeys(index, keyArr.length);

        PrivateKey key = keyArr[index];
        String messageHash = Utils.hashMessage(message);

        SignatureData signatureData = key.signMessage(messageHash);
        MessageSigned signed = new MessageSigned(messageHash, Arrays.asList(signatureData), message);

        return signed;
    }

    @Override
    public KeyStore encrypt(String password, KeyStoreOption options) throws CipherException {
        List<List<KeyStore.Crypto>> cryptoList = new ArrayList<>();


        for(int i=0; i<AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT; i++) {
            PrivateKey[] privateKeys = this.keys.get(i);
            List<KeyStore.Crypto> list = KeyStore.Crypto.createCrypto(privateKeys, password, options);
            cryptoList.add(list);
        }

        KeyStore keyStore = new KeyStore();
        keyStore.setAddress(this.address);
        keyStore.setKeyring(cryptoList);
        keyStore.setVersion(KeyStore.KEY_STORE_VERSION_V4);
        keyStore.setId(UUID.randomUUID().toString());

        return keyStore;
    }

    @Override
    public AbstractKeyring copy() {
        return new RoleBasedKeyring(this.address, this.keys);
    }


    public List<String[]> getPublicKey() {
        return this.keys.stream()
                .map(array -> {
                    return Arrays.stream(array)
                            .map(privateKey -> privateKey.getPublicKey(false))
                            .toArray(String[]::new);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public PrivateKey[] getKeyByRole(int role) {
        if(role < 0 || role > AccountKeyRoleBased.MAX_ROLE_BASED_KEY_COUNT) {
            throw new IllegalArgumentException("Invalid role index : " + role);
        }

        return this.keys.get(role);
    }

    public Account toAccount() {
        List<WeightedMultiSigOptions> options = WeightedMultiSigOptions.getDefaultOptionsForRoleBased(this.getPublicKey());
        return toAccount(options);
    }

    public Account toAccount(List<WeightedMultiSigOptions> options) {
        return Account.createWithAccountKeyRoleBased(this.address, this.getPublicKey(), options);
    }

    public List<PrivateKey[]> getKeys() {
        return keys;
    }
}
