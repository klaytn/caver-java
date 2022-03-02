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

package com.klaytn.caver.common.transaction.accessList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class AccessListTest {

    public static String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }

    static Caver caver = new Caver();

    static AccessList accessList = new AccessList(Arrays.asList(
            new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                    Arrays.asList(
                            "0x0000000000000000000000000000000000000000000000000000000000000000",
                            "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                            "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                    ))
    ));
    static String encodedAccessList = "0xf87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc";

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void create() throws IOException {
            AccessList accessList = caver.transaction.utils.accessList.create(
                    Arrays.asList(
                            new AccessTuple(
                                    "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                                    Arrays.asList(
                                            "0x4f42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                                            "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717"
                                    )
                            )
                    )
            );
        }
    }

    public static class deserializeTest {

        @Test
        public void oneAddressAndOneStorageKey() throws IOException {
            String accessListJson = "[\n" +
                    "  {\n" +
                    "    \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "    \"storageKeys\": [\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]";
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(accessListJson);
            AccessList accessList = objectMapper.readValue(reader, AccessList.class);
            System.out.println(objectToString(accessList));
        }

        @Test
        public void oneAddressAndManyStorageKeys() throws IOException {
            String accessListJson = "[\n" +
                    "  {\n" +
                    "    \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "    \"storageKeys\": [\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\",\n" +
                    "      \"0x496cb27939a5289c1664367767cdead9748c2dbce7f1a886fd72a2e65f233a48\",\n" +
                    "      \"0x112645f3af43346fcb71a5d46c30de31408b7a1458cf4bf5f87b587226dec6f1\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]";
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(accessListJson);
            AccessList accessList = objectMapper.readValue(reader, AccessList.class);
            System.out.println(objectToString(accessList));
        }

        @Test
        public void manyAccessList() throws IOException {
            String accessListJson = "[\n" +
                    "  {\n" +
                    "    \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "    \"storageKeys\": [\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\",\n" +
                    "      \"0x496cb27939a5289c1664367767cdead9748c2dbce7f1a886fd72a2e65f233a48\",\n" +
                    "      \"0x112645f3af43346fcb71a5d46c30de31408b7a1458cf4bf5f87b587226dec6f1\",\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "    \"storageKeys\": [\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\",\n" +
                    "      \"0x496cb27939a5289c1664367767cdead9748c2dbce7f1a886fd72a2e65f233a48\",\n" +
                    "      \"0x112645f3af43346fcb71a5d46c30de31408b7a1458cf4bf5f87b587226dec6f1\",\n" +
                    "      \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]";
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(accessListJson);
            AccessList accessList = objectMapper.readValue(reader, AccessList.class);
            System.out.println(objectToString(accessList));
        }

    }

    public static class decodeTest {
        @Test
        public void decodeString() throws IOException {
            AccessList decodedAccessList = caver.transaction.utils.accessList.decode(encodedAccessList);
            System.out.println(objectToString(decodedAccessList));
            Assert.assertEquals(accessList, decodedAccessList);
        }
    }

    public static class encodeTest {
        @Test
        public void encodeToBytes() {
            byte[] encoded = accessList.encodeToBytes();
            String encodedString = Numeric.toHexString(encoded);

            AccessList.decode(encodedString);
            Assert.assertEquals(encodedAccessList, encodedString);
        }
    }
}
