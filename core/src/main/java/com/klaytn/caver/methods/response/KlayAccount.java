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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;

public class KlayAccount extends Response<KlayAccount.Account> {

    @JsonDeserialize(using = KlayAccount.AccountDeserializer.class)
    public static class Account {

        private AccountType account;
        private int accType;

        public Account() {
        }

        public Account(AccountType account, int accType) {
            this.account = account;
            this.accType = accType;
        }

        public AccountType getAccount() {
            return account;
        }

        public int getAccType() {
            return accType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Account)) {
                return false;
            }

            Account that = (Account) o;

            if (getAccount() != null
                    ? !getAccount().equals(that.getAccount()) : that.getAccount() != null) {
                return false;
            }
            return getAccType() == that.getAccType();
        }

        @Override
        public int hashCode() {
            int result = getAccount() != null ? getAccount().hashCode() : 0;
            result = 31 * result + Integer.hashCode(getAccType());
            return result;
        }
    }

    public static class AccountDecoder {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        public static AccountType decode(AccountType.AccType keyType, JsonNode key) throws IOException {
            if (keyType == AccountType.AccType.EOA) {
                return objectMapper.readValue(key.toString(), AccountEOA.class);
            }
            return objectMapper.readValue(key.toString(), AccountSmartContract.class);
        }
    }

    public static class AccountDeserializer extends JsonDeserializer<KlayAccount.Account> {

        @Override
        public KlayAccount.Account deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            JsonNode key = node.get("account");
            AccountType.AccType accType = AccountType.AccType.getType(node.get("accType").intValue());
            return new Account(AccountDecoder.decode(accType, key), accType.getAccType());
        }
    }
}
