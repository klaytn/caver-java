/*
 * Copyright 2019 The caver-java Authors
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Response from request of Account type: Smart Contract Account
 */
public class AccountSmartContract implements AccountType {

    private String balance;
    private String codeFormat;
    private String codeHash;
    private boolean humanReadable;
    private KlayAccountKey.AccountKeyValue key;
    private String nonce;
    private String storageRoot;

    public String getBalance() {
        return balance;
    }

    public String getCodeFormat() {
        return codeFormat;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    @JsonDeserialize(using = KlayAccountKey.AccountKeyDeserializer.class)
    public KlayAccountKey.AccountKeyValue getKey() {
        return key;
    }

    public String getNonce() {
        return nonce;
    }

    public String getStorageRoot() {
        return storageRoot;
    }

    @Override
    public AccountType.Key getType() {
        return Key.CONTRACT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountSmartContract)) {
            return false;
        }

        AccountSmartContract that = (AccountSmartContract) o;

        if (getBalance() != null
                ? !getBalance().equals(that.getBalance()) : that.getBalance() != null) {
            return false;
        }
        if (getCodeFormat() != null
                ? !getCodeFormat().equals(that.getCodeFormat()) : that.getCodeFormat() != null) {
            return false;
        }
        if (getCodeHash() != null
                ? !getCodeHash().equals(that.getCodeHash()) : that.getCodeHash() != null) {
            return false;
        }
        if (isHumanReadable() != that.isHumanReadable()) {
            return false;
        }
        if (getKey() != null
                ? !getKey().equals(that.getKey()) : that.getKey() != null) {
            return false;
        }
        if (getNonce() != null
                ? !getNonce().equals(that.getNonce()) : that.getNonce() != null) {
            return false;
        }
        return (getStorageRoot() != null ? getStorageRoot().equals(that.getStorageRoot()) : that.getStorageRoot() == null);
    }

    @Override
    public int hashCode() {
        int result = getBalance() != null ? getBalance().hashCode() : 0;
        result = 31 * result + (getCodeFormat() != null ? getCodeFormat().hashCode() : 0);
        result = 31 * result + (getCodeHash() != null ? getCodeHash().hashCode() : 0);
        result = 31 * result + java.lang.Boolean.hashCode(isHumanReadable());
        result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
        result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
        result = 31 * result + (getStorageRoot() != null ? getStorageRoot().hashCode() : 0);
        return result;
    }
}
