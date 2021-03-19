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

package com.klaytn.caver.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.klaytn.caver.utils.Utils;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

@JsonDeserialize(using = WeightedPublicKey.WeightedPublicKeyDeserializer.class)
@JsonSerialize(using = WeightedPublicKey.WeightedPublicKeySerializer.class)
public class WeightedPublicKey {

    /**
     * ECC Public Key value with "SECP-256k1" curve.
     * This String has following format.
     * 1. Uncompressed format : 0x{Public Key X point}||{Public Y point}
     * 2. Compressed format : 0x{02 or 03 || Public Key X}
     */
    private String publicKey;

    /**
     * The weight of key
     */
    BigInteger weight;

    public static final int OFFSET_WEIGHT = 0;
    public static final int OFFSET_PUBLIC_KEY = 1;

    /**
     * Creates WeightedPublicKey instance
     * @param publicKey The ECC Public key String.(Compressed or Uncompressed format)
     * @param weight THe weight of Key
     */
    public WeightedPublicKey(String publicKey, BigInteger weight) {
        setPublicKey(publicKey);
        this.weight = weight;
    }

    /**
     * Getter function for PublicKey
     * @return publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Setter function for PublicKey
     * It allows both compressed and uncompressed format.
     * @param publicKey ecc Public key
     */
    public void setPublicKey(String publicKey) {
        if(!Utils.isValidPublicKey(publicKey)) {
            throw new IllegalArgumentException("Invalid Public key format");
        }
        this.publicKey = publicKey;
    }

    /**
     * Getter function for weight.
     * @return weight
     */
    public BigInteger getWeight() {
        return weight;
    }

    /**
     * Setter function for weight
     * @param weight a weight
     */
    public void setWeight(BigInteger weight) {
        this.weight = weight;
    }

    /**
     * Returns an encoded weighted public key string.
     * @return array of string. [0] : weight, [1] compressed public key
     */
    public String[] encodeToBytes() {
        if (this.publicKey == null) {
            throw new RuntimeException("public key should be specified for a multisig account");
        }
        if(this.weight == null) {
            throw new RuntimeException("weight should be specified for a multisig account");
        }

        String compressedKey = Utils.compressPublicKey(this.publicKey);
        return new String[] {Numeric.toHexStringWithPrefix(this.weight), compressedKey};
    }

    public static class WeightedPublicKeySerializer extends JsonSerializer<WeightedPublicKey> {
        @Override
        public void serialize(WeightedPublicKey weightedPublicKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("weight");
            jsonGenerator.writeNumber(weightedPublicKey.weight);

            String decompressedKey = Utils.decompressPublicKey(weightedPublicKey.publicKey);
            String stripHexKey = Utils.stripHexPrefix(decompressedKey);
            jsonGenerator.writeFieldName("key");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("x", Utils.addHexPrefix(stripHexKey.substring(0, 64)));
            jsonGenerator.writeStringField("y", Utils.addHexPrefix(stripHexKey.substring(64)));
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
        }
    }

    public static class WeightedPublicKeyDeserializer extends JsonDeserializer<WeightedPublicKey> {
        @Override
        public WeightedPublicKey deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            BigInteger weight = node.get("weight").bigIntegerValue();

            JsonNode key = node.get("key");

            String xPoint_padded = Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(key.get("x").asText()), 64);
            String yPoint_padded = Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(key.get("y").asText()), 64);
            String publicKey = Numeric.prependHexPrefix(xPoint_padded) + Numeric.cleanHexPrefix(yPoint_padded);

            return new WeightedPublicKey(publicKey, weight);
        }
    }
}
