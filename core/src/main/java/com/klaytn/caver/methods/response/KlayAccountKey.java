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
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.account.AccountKeyRoleBased;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @deprecated Please use {@link com.klaytn.caver.methods.response.AccountKey} instead.
 */
@Deprecated
public class KlayAccountKey extends Response<KlayAccountKey.AccountKeyValue> {

    @JsonDeserialize(using = KlayAccountKey.AccountKeyDeserializer.class)
    public static class AccountKeyValue {

        private AccountKey key;

        private int keyType;

        public AccountKeyValue() {
        }

        public AccountKeyValue(AccountKey key, int keyType) {
            this.key = key;
            this.keyType = keyType;
        }

        public AccountKey getKey() {
            return key;
        }

        public int getKeyType() {
            return keyType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AccountKeyValue)) {
                return false;
            }

            AccountKeyValue that = (AccountKeyValue) o;

            if (getKey() != null
                    ? !getKey().equals(that.getKey()) : that.getKey() != null) {
                return false;
            }
            return getKeyType() == that.getKeyType();
        }

        @Override
        public int hashCode() {
            int result = getKey() != null ? getKey().hashCode() : 0;
            result = 31 * result + Integer.hashCode(getKeyType());
            return result;
        }
    }

    public static class AccountKeyDecoder {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        public static AccountKey decode(AccountKey.Type keyType, JsonNode key) throws IOException {
            if (keyType == AccountKey.Type.ROLEBASED) {
                Iterator<JsonNode> iterator = key.iterator();
                List<AccountKey> accountKeys = new ArrayList<>();
                while (iterator.hasNext()) {
                    JsonNode node = iterator.next();
                    AccountKey.Type innerKeyType = AccountKey.Type.findByValue((byte) node.get("keyType").intValue());
                    JsonNode innerKey = node.get("key");
                    accountKeys.add(AccountKeyDecoder.decode(innerKeyType, innerKey));
                }
                return AccountKeyRoleBased.create(accountKeys);
            }
            return (AccountKey) objectMapper.readValue(key.toString(), keyType.getClazz());
        }
    }


    public static class AccountKeyDeserializer extends JsonDeserializer<AccountKeyValue> {

        @Override
        public AccountKeyValue deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            AccountKey.Type keyKey = AccountKey.Type.findByValue((byte) node.get("keyType").intValue());
            JsonNode key = node.get("key");
            return new AccountKeyValue(AccountKeyDecoder.decode(keyKey, key), keyKey.getValue());
        }
    }
}

