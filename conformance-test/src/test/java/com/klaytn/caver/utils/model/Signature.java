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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = Signature.SignatureDeserializer.class)
public class Signature implements IMethodInputParams {
    SignatureData signature;
    List<SignatureData> signatures;

    public Signature() {
    }

    public Signature(List<SignatureData> signatureDataList) {
        this.signatures = signatureDataList;
    }

    public Signature(SignatureData data) {
        this.signature = data;
    }

    public SignatureData getSignature() {
        return signature;
    }

    public void setSignature(SignatureData signature) {
        this.signature = signature;
    }

    public List<SignatureData> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignatureData> signatures) {
        this.signatures = signatures;
    }

    @Override
    public Object[] getInputArray() {
        if(this.signatures != null && this.signatures.size() != 0) {
            return new Object[]{this.signatures};
        }

        return new Object[]{this.signature};
    }

    @Override
    public Class[] getInputTypeArray() {
        if(this.signatures != null && this.signatures.size() != 0) {
            return new Class[]{List.class};
        }

        return new Class[]{this.signature.getClass()};
    }

    public static class SignatureDeserializer extends JsonDeserializer<Signature> {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        @Override
        public Signature deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode root = p.getCodec().readTree(p);

            if(root.has("signatures")) {
                JsonNode signatures = root.get("signatures");
                List<SignatureData> list = new ArrayList<>();

                for(JsonNode signature : signatures) {
                    String v = signature.get("v").asText();
                    String r = signature.get("r").asText();
                    String s = signature.get("s").asText();

                    list.add(new SignatureData(v, r, s));
                }

                return new Signature(list);
            } else {
                JsonNode signature = root.get("signature");

                String v = signature.get("v").asText();
                String r = signature.get("r").asText();
                String s = signature.get("s").asText();

                return new Signature(new SignatureData(v, r, s));
            }
        }
    }
}
