/*
 * Copyright 2022 The caver-java Authors
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

package com.klaytn.caver.transaction.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AccessTuple is a class representing an element of EIP-2930 access list.
 */
@JsonDeserialize(using = AccessTuple.AccessTupleDeserializer.class)
public class AccessTuple {
    private String address;
    private List<String> storageKeys;

    /**
     * Create an AccessTuple instance.
     * @param address An address string.
     * @param storageKeys A list of storage keys.
     */
    public AccessTuple(String address, List<String> storageKeys) {
        this.address = address;
        this.storageKeys = storageKeys;
    }

    /**
     * Getter function of address.
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter function of address.
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter function of storageKeys.
     * @return List&lt;String&gt;
     */
    public List<String> getStorageKeys() {
        return storageKeys;
    }

    /**
     * Setter function of storageKeys.
     * @param storageKeys A list of storage keys.
     */
    public void setStorageKeys(List<String> storageKeys) {
        this.storageKeys = storageKeys;
    }

    /**
     * Decodes given RlpList to AccessTuple.
     * @param rlpEncodedAccessTuple
     * @return
     */
    public static AccessTuple decode(RlpList rlpEncodedAccessTuple) {
        try {
            List<RlpType> accessTupleRlp = rlpEncodedAccessTuple.getValues();
            String address = ((RlpString) accessTupleRlp.get(0)).asString();
            List<String> storageKeys = new ArrayList<>();
            List<RlpType> storageKeysRlpType = ((RlpList) (accessTupleRlp.get(1))).getValues();
            for (RlpType storageKeyRlpType : storageKeysRlpType) {
                storageKeys.add(((RlpString) storageKeyRlpType).asString()); }
            return new AccessTuple(address, storageKeys);
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }


    /**
     * Returns the RLP-encoded string of this accessTuple.
     *
     * @return RlpList
     */
    public RlpList toRlpList() {
        List<RlpType> storageKeysRLPList = new ArrayList<>();
        for (String storageKey : getStorageKeys()) {
            storageKeysRLPList.add(RlpString.create(Numeric.hexStringToByteArray(storageKey)));
        }
        return new RlpList(
                RlpString.create(Numeric.hexStringToByteArray(getAddress())),
                new RlpList(storageKeysRLPList)
        );
    }

    /**
     * Returns an encoded access tuple.
     * 
     * @return byte[]
     */
    public byte[] encodeToBytes() {
        RlpList rlPList = this.toRlpList();
        return RlpEncoder.encode(rlPList);
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The reference object with which to compare.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessTuple that = (AccessTuple) o;

        if (!address.equals(that.address)) {
            return false;
        }
        if (!storageKeys.equals(that.storageKeys)) {
            return false;
        }
        return true;
    }

    public static class AccessTupleDeserializer extends JsonDeserializer<AccessTuple> {
        @Override
        public AccessTuple deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode root = p.getCodec().readTree(p);
            JsonNode addressNode = root.get("address");
            String address = (addressNode == null ? "" : addressNode.textValue());
            List<String> storageKeys = new ArrayList<>();
            Iterator<JsonNode> storageKeysNodeIterator = root.get("storageKeys").iterator();
            while (storageKeysNodeIterator.hasNext()) {
                storageKeys.add(storageKeysNodeIterator.next().textValue());
            }
            return new AccessTuple(address, storageKeys);
        }
    }
}
