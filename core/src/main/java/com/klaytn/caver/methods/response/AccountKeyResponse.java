package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.account.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;

public class AccountKeyResponse extends Response<AccountKeyResponse.AccountKey> {
    @JsonDeserialize(using = AccountKeyResponse.AccountKeyDeserializer.class)
    public static class AccountKey {
        private String type;
        private IAccountKey accountKey;

        public AccountKey(String type, IAccountKey accountKey) {
            this.type = type;
            this.accountKey = accountKey;
        }

        public AccountKey() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public IAccountKey getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(IAccountKey accountKey) {
            this.accountKey = accountKey;
        }
    }

    public static class AccountKeyDeserializer extends JsonDeserializer<AccountKeyResponse.AccountKey> {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        public static IAccountKey decode(JsonNode keyJson, String type) throws IOException {
            if(type.equals(AccountKeyLegacy.getType())) {
                return new AccountKeyLegacy();
            } else if(type.equals(AccountKeyPublic.getType())) {
                return objectMapper.readValue(keyJson.toString(), AccountKeyPublic.class);
            } else if(type.equals(AccountKeyWeightedMultiSig.getType())) {
                return objectMapper.readValue(keyJson.toString(), AccountKeyWeightedMultiSig.class);
            } else if(type.equals(AccountKeyRoleBased.getType())) {
                return objectMapper.readValue(keyJson.toString(), AccountKeyRoleBased.class);
            }  else if(type.equals(AccountKeyFail.getType())) {
                return new AccountKeyFail();
            } else {
                return new AccountKeyNil();
            }
        }

        @Override
        public AccountKey deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode root = p.getCodec().readTree(p);
            String type = Numeric.toHexStringWithPrefixZeroPadded(root.get("keyType").bigIntegerValue(), 2);

            IAccountKey accountKey = decode(root, type);
            return new AccountKey(type, accountKey);
        }
    }
}
