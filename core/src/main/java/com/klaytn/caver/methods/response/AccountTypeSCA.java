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

package com.klaytn.caver.methods.response;

public class AccountTypeSCA implements IAccountType {
    private String balance;
    private String codeFormat;
    private String codeHash;
    private boolean humanReadable;
    private AccountKey.AccountKeyData key;
    private String nonce;
    private String storageRoot;

    public AccountTypeSCA() {
    }

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
