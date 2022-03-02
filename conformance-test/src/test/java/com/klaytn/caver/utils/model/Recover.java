/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.utils.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.IMethodInputParams;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;

@JsonDeserialize(using = Recover.RecoverDeserializer.class)
public class Recover implements IMethodInputParams {
    String message;
    SignatureData signature;
    Boolean isHashed;

    public Recover() {
    }

    public Recover(String message, SignatureData signature, Boolean isHashed) {
        this.message = message;
        this.signature = signature;
        this.isHashed = isHashed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignatureData getSignature() {
        return signature;
    }

    public void setSignature(SignatureData signature) {
        this.signature = signature;
    }

    public boolean isHashed() {
        return isHashed;
    }

    public void setHashed(boolean hashed) {
        isHashed = hashed;
    }

    @Override
    public Object[] getInputArray() {
        if(isHashed != null) {
            return new Object[] {message, signature, isHashed};
        }
        return new Object[] { message, signature };
    }

    @Override
    public Class[] getInputTypeArray() {
        if(isHashed != null) {
            return new Class[] {message.getClass(), signature.getClass(), boolean.class};
        }
        return new Class[] {message.getClass(), signature.getClass()};
    }

    public static class RecoverDeserializer extends JsonDeserializer<Recover> {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        @Override
        public Recover deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Recover recover = new Recover();

            JsonNode root = p.getCodec().readTree(p);

            JsonNode signature = root.get("signature");
            String v = signature.get("v").asText();
            String r = signature.get("r").asText();
            String s = signature.get("s").asText();
            SignatureData signatureData = new SignatureData(v, r, s);

            String message = root.get("message").asText();

            recover.setMessage(message);
            recover.setSignature(signatureData);

            if(root.has("isHashed")) {
                recover.setHashed(root.get("isHashed").asBoolean());
            }
            return recover;
        }
    }
}
