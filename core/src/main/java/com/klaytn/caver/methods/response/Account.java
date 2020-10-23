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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;


public class Account extends Response<Account.AccountData> {
    @JsonDeserialize(using = AccountData.AccountDeserializer.class)
    public static class AccountData {
        int accType;
        private IAccountType account;

        public AccountData(int accType, IAccountType account) {
            this.accType = accType;
            this.account = account;
        }

        public int getAccType() {
            return accType;
        }

        public void setAccType(int accType) {
            this.accType = accType;
        }

        public IAccountType getAccount() {
            return account;
        }

        public void setAccount(IAccountType account) {
            this.account = account;
        }


        public static class AccountDecoder {

            private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

            public static IAccountType decode(IAccountType.AccType keyType, JsonNode key) throws IOException {
                if (keyType == IAccountType.AccType.EOA) {
                    return objectMapper.readValue(key.toString(), AccountTypeEOA.class);
                }
                return objectMapper.readValue(key.toString(), AccountSmartContract.class);
            }
        }

        public static class AccountDeserializer extends JsonDeserializer<AccountData> {
            @Override
            public AccountData deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {
                JsonNode node = jsonParser.getCodec().readTree(jsonParser);
                JsonNode key = node.get("account");
                IAccountType.AccType accType = IAccountType.AccType.getType(node.get("accType").intValue());
                return new AccountData(accType.getAccType(), AccountDecoder.decode(accType, key));
            }
        }
    }
}
