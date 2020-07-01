package com.klaytn.caver.methods.response;

public class AccountTypeSCA implements IAccountType {
    private String balance;
    private String codeFormat;
    private String codeHash;
    private boolean humanReadable;
    private AccountKey.AccountKeyData key;
    private String nonce;
    private String storageRoot;

    public AccountTypeSCA(String balance, String codeFormat, String codeHash, boolean humanReadable, AccountKey.AccountKeyData key, String nonce, String storageRoot) {
        this.balance = balance;
        this.codeFormat = codeFormat;
        this.codeHash = codeHash;
        this.humanReadable = humanReadable;
        this.key = key;
        this.nonce = nonce;
        this.storageRoot = storageRoot;
    }

    @Override
    public AccType getType() {
        return AccType.SCA;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCodeFormat() {
        return codeFormat;
    }

    public void setCodeFormat(String codeFormat) {
        this.codeFormat = codeFormat;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }

    public AccountKey.AccountKeyData getKey() {
        return key;
    }

    public void setKey(AccountKey.AccountKeyData key) {
        this.key = key;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }
}
