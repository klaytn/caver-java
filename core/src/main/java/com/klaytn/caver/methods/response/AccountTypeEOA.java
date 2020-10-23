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

public class AccountTypeEOA implements IAccountType {
    private String balance;

    private boolean humanReadable;

    private AccountKey.AccountKeyData key;

    private String nonce;

    public AccountTypeEOA() {
    }

    public AccountTypeEOA(String balance, boolean humanReadable, AccountKey.AccountKeyData key, String nonce) {
        this.balance = balance;
        this.humanReadable = humanReadable;
        this.key = key;
        this.nonce = nonce;
    }

    @Override
    public AccType getType() {
        return AccType.EOA;
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
}
