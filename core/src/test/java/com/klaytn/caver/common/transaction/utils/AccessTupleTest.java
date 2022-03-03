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

package com.klaytn.caver.common.transaction.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.transaction.utils.AccessTuple;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class AccessTupleTest {

    public static String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }

    public static class createInstanceTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void create() throws IOException {
            AccessTuple expectedAccessTuple = new AccessTuple(
                    "0x4173c51bd0e64a4c58656a5401373a476e8534ec",
                    Arrays.asList(
                            "0x4f42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                            "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717"
                    )
            );

            AccessTuple accessTuple = new AccessTuple(
                    "0x4173C51bd0e64A4c58656A5401373A476E8534Ec",
                    Arrays.asList(
                            "0X4F42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B98",
                            "0XC4A32ABDF1905059FDFC304AAE1E8924279A36B2A6428552237F590156ED7717"
                    )
            );

            // Checksum address should be accepted.
            // Uppercase storage keys will be converted as small case letter when creating AccessTuple.
            Assert.assertEquals(expectedAccessTuple, accessTuple);
            Assert.assertTrue(Arrays.equals(expectedAccessTuple.encodeToBytes(), accessTuple.encodeToBytes()));
        }

        @Test
        public void create_invalidAddress() {
            // 1 byte-short
            String invalidAddress = "0x4173c51bd0e64a4c58656a5401373a476e8534";
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid address. Address: " + invalidAddress);

            new AccessTuple(
                    invalidAddress,
                    Arrays.asList(
                            "0X4F42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B98",
                            "0XC4A32ABDF1905059FDFC304AAE1E8924279A36B2A6428552237F590156ED7717"
                    )
            );
        }

        @Test
        public void create_invalidStorageKey() {
            // 1 byte-short
            String invalidStorageKey = "0x4F42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B";
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid storageKey. Storage key should be a 32 bytes of hex string " + invalidStorageKey.toLowerCase());

            new AccessTuple(
                    "0x4173C51bd0e64A4c58656A5401373A476E8534Ec",
                    Arrays.asList(
                            invalidStorageKey,
                            "0XC4A32ABDF1905059FDFC304AAE1E8924279A36B2A6428552237F590156ED7717"
                    )
            );
        }

        @Test
        public void create_orderedKeys() {
            AccessTuple accessTuple = new AccessTuple(
                    "0x4173C51bd0e64A4c58656A5401373A476E8534Ec",
                    Arrays.asList(
                            "0XC4A32ABDF1905059FDFC304AAE1E8924279A36B2A6428552237F590156ED7717",
                            "0XaF42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B98",
                            "0XbF42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B98",
                            "0XcF42391603E79B2A90C3FBFC070C995EB1163E0AC00FB4E8F3DA2DC81C451B98"
                    )
            );
            Assert.assertEquals(
                    Arrays.asList(
                            "0xaf42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                            "0xbf42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                            "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717",
                            "0xcf42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98"
                    ),
                    accessTuple.getStorageKeys()
            );
        }

    }
}
