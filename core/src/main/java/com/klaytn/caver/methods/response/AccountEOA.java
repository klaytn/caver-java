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
 * Response from request of Account type: Externally Owned Account
 */
public class AccountEOA implements IAccountType {

    private String balance;
    private boolean humanReadable;
    private KlayAccountKey.AccountKeyValue key;
    private String nonce;

    public String getBalance() {
        return balance;
    }

    public boolean getHumanReadable() {
        return humanReadable;
    }

    @JsonDeserialize(using = KlayAccountKey.AccountKeyDeserializer.class)
    public KlayAccountKey.AccountKeyValue getKey() {
        return key;
    }

    public String getNonce() {
        return nonce;
    }

    @Override
    public AccType getType() {
        return AccType.EOA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountEOA)) {
            return false;
        }

        AccountEOA that = (AccountEOA) o;

        if (getBalance() != null
                ? !getBalance().equals(that.getBalance()) : that.getBalance() != null) {
            return false;
        }
        if (getHumanReadable() != that.getHumanReadable()) {
            return false;
        }
        if (getKey() != null
                ? !getKey().equals(that.getKey()) : that.getKey() != null) {
            return false;
        }
        return (getNonce() != null ? getNonce().equals(that.getNonce()) : that.getNonce() == null);
    }

    @Override
    public int hashCode() {
        int result = getBalance() != null ? getBalance().hashCode() : 0;
        result = 31 * result + java.lang.Boolean.hashCode(getHumanReadable());
        result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
        result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
        return result;
    }
}
