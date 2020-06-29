package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AccountTypeEOA implements AccountType {
    private String balance;

    private boolean humanReadable;

    private AccountKeyResponse.AccountKey key;

    private String nonce;

    public AccountTypeEOA() {
    }

    public AccountTypeEOA(String balance, boolean humanReadable, AccountKeyResponse.AccountKey key, String nonce) {
        this.balance = balance;
        this.humanReadable = humanReadable;
        this.key = key;
        this.nonce = nonce;
    }

    @Override
    public Key getType() {
        return Key.EOA;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }

    public AccountKeyResponse.AccountKey getKey() {
        return key;
    }

    public void setKey(AccountKeyResponse.AccountKey key) {
        this.key = key;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
