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

package com.klaytn.caver.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyLegacy;
import com.klaytn.caver.methods.response.AccountKey;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SignatureData;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class ValidatorTest {
    public static class validatedSignedMessage_AccountKeyLegacy {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static String privateKey = "0x7db91b4606aa4421eeb85d03601562f966693e38957d5e79a29edda0e85b2225";

        static AccountKeyLegacy legacy = new AccountKeyLegacy();
        static String message = "Some Message";
        static String hashedMessage = "0xa4b1069c1000981f4fdca0d62302dfff77c2d0bc17f283d961e2dc5961105b18";
        static SignatureData signatureData = new SignatureData("0x1b", "0x8213e560e7bbe1f2e28fd69cbbb41c9108b84c98cd7c2c88d3c8e3549fd6ab10", "0x3ca40c9e20c1525348d734a6724db152b9244bff6e0ff0c2b811d61d8f874f00");

        @Test
        public void withMessage() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, signatureData, keyring.getAddress()));
        }

        @Test
        public void withMessageHash() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(hashedMessage, signatureData, keyring.getAddress(), true));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);

            SignatureData invalid = new SignatureData(
                    "0x1c",
                    "0xa5c9ff1df09258a6f9262f1fae43a306ec77592287787cbd3ee0419dd8d2bfeb",
                    "0x4c903d3dda703554cf7b65aa2c0dc819c86d36cf2dbf0ff5071667fb5551a706"
            );

            assertFalse(validator.validateSignedMessage(message, invalid, address));
        }
    }

    public static class validatedSignedMessage_AccountKeyNull {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static String privateKey = "0x7db91b4606aa4421eeb85d03601562f966693e38957d5e79a29edda0e85b2225";
        static String message = "Some Message";
        static String hashedMessage = "0xa4b1069c1000981f4fdca0d62302dfff77c2d0bc17f283d961e2dc5961105b18";
        static SignatureData signatureData = new SignatureData("0x1b", "0x8213e560e7bbe1f2e28fd69cbbb41c9108b84c98cd7c2c88d3c8e3549fd6ab10", "0x3ca40c9e20c1525348d734a6724db152b9244bff6e0ff0c2b811d61d8f874f00");

        @Test
        public void withMessage() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), null));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, signatureData, keyring.getAddress()));
        }

        @Test
        public void withMessageHash() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), null));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(hashedMessage, signatureData, keyring.getAddress(), true));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            AccountKey accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), null));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            Validator validator = new Validator(klay);

            SignatureData invalid = new SignatureData(
                    "0x1c",
                    "0xa5c9ff1df09258a6f9262f1fae43a306ec77592287787cbd3ee0419dd8d2bfeb",
                    "0x4c903d3dda703554cf7b65aa2c0dc819c86d36cf2dbf0ff5071667fb5551a706"
            );

            assertFalse(validator.validateSignedMessage(message, invalid, address));
        }
    }

    public static class validatedSignedMessage_AccountKeyPublic {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static String privateKey = "0xf95c224b63f5658281ad853b3f582051eb9bca9b3e5475a8d3e4315abf42cb02";
        static String message = "Some Message";
        static String hashedMessage = "0xa4b1069c1000981f4fdca0d62302dfff77c2d0bc17f283d961e2dc5961105b18";

        static SignatureData signatureData = new SignatureData(
                "0x1c",
                "0xa5c9ff1df09258a6f9262f1fae43a306ec77592287787cbd3ee0419dd8d2bfeb",
                "0x4c903d3dda703554cf7b65aa2c0dc819c86d36cf2dbf0ff5071667fb5551a706"
        );

        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String publicKeyJson = "{\n" +
                    "  \"keyType\":2,\n" +
                    "  \"key\":{\n" +
                    "    \"x\":\"0x89632f9a5aa49b30ddea62574c4b0e23cf05b934f667dd94eb3a4f394ca0bba3\",\n" +
                    "    \"y\":\"0x6726b313ef93fbfc1295b87f62f1c1c9fdb0dbc6f583eae6db0cbd5c715f19cd\"\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(publicKeyJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withMessage() throws IOException{
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, signatureData, keyring.getAddress()));
        }

        @Test
        public void withMessageHash() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(hashedMessage, signatureData, keyring.getAddress(), true));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            SignatureData invalid = new SignatureData(
                    "0x1b",
                    "0x883c4174fada447e95b09e3f27b2e27f419179366230bbcd5046ff946d1e4a90",
                    "0x052f9e19394e593547370ec5216703c6b698377d4f5fa422bf0e1cb26698dad2"
            );

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, invalid, keyring.getAddress()));
        }
    }

    public static class validatedSignedMessage_AccountKeyFail {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static String message = "Some Message";

        SignatureData signatureData = new SignatureData(
                "0x1c",
                "0xa5c9ff1df09258a6f9262f1fae43a306ec77592287787cbd3ee0419dd8d2bfeb",
                "0x4c903d3dda703554cf7b65aa2c0dc819c86d36cf2dbf0ff5071667fb5551a706"
        );

        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String accountKeyDataJson = "{\n" +
                    "  \"keyType\":3,\n" +
                    "  \"key\":{}\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(accountKeyDataJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withMessage() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, signatureData, address));
        }
    }

    public static class validatedSignedMessage_AccountKeyWeightedMultiSig {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static List<String> privateKeys = Arrays.asList(
                "0xc707902dba449d0ce3b47675db728ad795b63a4ae748dc4f7265e05305ae7424",
                "0x9611e74d616659598c56707b47779f99cae268c70a53163348cea01f8f872447",
                "0x24777022e1b8c993451c855b1f74561dff057caa825a01f4d87ab2f2b46aaaf7"
        );

        static String message = "Some Message";
        static String hashedMessage = "0xa4b1069c1000981f4fdca0d62302dfff77c2d0bc17f283d961e2dc5961105b18";

        static List<SignatureData> signatureData = Arrays.asList(
                new SignatureData(
                        "0x1b",
                        "0x883c4174fada447e95b09e3f27b2e27f419179366230bbcd5046ff946d1e4a90",
                        "0x052f9e19394e593547370ec5216703c6b698377d4f5fa422bf0e1cb26698dad2"
                ),
                new SignatureData(
                        "0x1c",
                        "0x8119b63a28a9c20ef8266b4d99e1f05c1bfa773e1376f19802f898b117311556",
                        "0x23d265b2cb102a2d81bf829aae1e9d579d7bf32110e3e62728070ebae466c131"
                ),
                new SignatureData(
                        "0x1c",
                        "0xa9770147c523ef699959b804c2dd9ba1b61b3c6bdd8eac57c0a72efd5c5c566c",
                        "0x2904d93e0510d76120de0e0cd8eeb4842867edde58c29efe389c8a82705ce00e"
                )
        );

        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":4,\n" +
                    "  \"key\":{\n" +
                    "    \"threshold\":3,\n" +
                    "    \"keys\":[\n" +
                    "      {\n" +
                    "        \"weight\":2,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x2459bce1b37d5c517854842574f7fc78d7e76238c1c8b1619f0189a397fbf9c2\",\n" +
                    "          \"y\":\"0xeeacaae1117b6994df2b5bc2990157f9c7a74c9ef211d6cb49b1a624911ec87b\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x334aa344adf4e5758d4d75a8cc89495d0cb809433960ea6459e31de4f1ac7ac1\",\n" +
                    "          \"y\":\"0x10a3971d6c139a38d16d83fe87024127b8326c8d16de7187a5adf0b1f7c28ada\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0xd9fbf7476758d2f4db78379bf0b9d2207207f638b39c831b8034cc35d0f87aba\",\n" +
                    "          \"y\":\"0xa6fcac353e85bde5e7a11173aa8ef1f546df4276816daa7b8b5e4985285f1ab0\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withMessage() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, signatureData, address));
        }

        @Test
        public void withMessageHash() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(hashedMessage, signatureData, address));
        }

        @Test
        public void lessThanThreshold() throws IOException {
            List<SignatureData> sigList = Arrays.asList(signatureData.get(1), signatureData.get(2));

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, sigList, address));
        }

        @Test
        public void satisfiedThresholdWithInvalidSig() throws IOException {
            SignatureData invalid = new SignatureData(
                    "0x1b",
                    "0x8213e560e7bbe1f2e28fd69cbbb41c9108b84c98cd7c2c88d3c8e3549fd6ab10",
                    "0x3ca40c9e20c1525348d734a6724db152b9244bff6e0ff0c2b811d61d8f874f00"
            );

            List<SignatureData> sigList = new ArrayList<>();
            sigList.add(invalid);
            sigList.addAll(signatureData);

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, sigList, address));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            List<SignatureData> signatureData = Arrays.asList(
                    new SignatureData(
                            "0x1b",
                            "0xa7373f18960e9f9085781b35c539aa82fe02f076d36b8f56fe199c1262a26691",
                            "0x0d003bc30f79b510a189f98a776b2419995bc3fc88a2e63094e2fb022323ff3c"
                    ),
                    new SignatureData(
                            "0x1c",
                            "0xdc92f3a0e46c4522a635ddf07a3f970bae4c6e546b3afc5f2fac8b60054ffcec",
                            "0x02b30df9d85a3a1cf2e03c01f5a32544ef68a65ff48ad25428dabe95405aef25"
                    )
            );

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, signatureData, address));
        }
    }

    public static class validatedSignedMessage_AccountKeyRoleBased {
        static String address = "0xa84a1ce657e9d5b383cece6f4ba365e23fa234dd";
        static String[][] privateKeyArr = {
                {
                        "0x91be7ec09a17222740fe356b5b2721722abc38b7b40334cda94a478993e51668",
                        "0x4f4b3517caae10ae4571eb1f30e649f1fa10c6a3c690e555db473623cad8a171"
                },
                {
                        "0xd9d77f73890681e7dfe26fba7bb19479112bc902129be2d19ad514b0d0f4cc54",
                        "0xf38971e17d4ee376498adba83ef951e93ee7aedeea350d1820c7b8436a5a6286"
                },
                {
                        "0x609afc089f497c8752664a34e03e305d0c9550f0efaa41c3636bc40e92fa6e83",
                        "0xf2263c847c9989660cc7319c16611cf5ddb8d3b7d15a45b2f93ef79cc48c582a"
                }
        };

        static String message = "Some Message";
        static String hashedMessage = "0xa4b1069c1000981f4fdca0d62302dfff77c2d0bc17f283d961e2dc5961105b18";

        static List<SignatureData> roleTxSignatures = Arrays.asList(
                new SignatureData(
                        "0x1b",
                        "0xa7373f18960e9f9085781b35c539aa82fe02f076d36b8f56fe199c1262a26691",
                        "0x0d003bc30f79b510a189f98a776b2419995bc3fc88a2e63094e2fb022323ff3c"
                ),
                new SignatureData(
                        "0x1c",
                        "0xdc92f3a0e46c4522a635ddf07a3f970bae4c6e546b3afc5f2fac8b60054ffcec",
                        "0x02b30df9d85a3a1cf2e03c01f5a32544ef68a65ff48ad25428dabe95405aef25"
                )
        );

        static List<SignatureData> roleAccountUpdateSignatures = Arrays.asList(
                new SignatureData(
                        "0x1b",
                        "0x2a163fa0cb6b191c4773b11bf177b1e51175db340b4d6c74046119d5d96b2946",
                        "0x1282daa48dd3cc500f5aa145bb87cd1f68bc7b9bf92cdd158c2075041002fc60"
                ),
                new SignatureData(
                        "0x1c",
                        "0x671fd9e070a42dc5e5ffd97b39fdceba4f670cad06f455513f021436a95f1ea1",
                        "0x0d01a612c20481f92c51be655a9603afd93a2a9134e12e2b45bf463cb7c02f26"
                )
        );

        static List<SignatureData> roleFeePayerSignatures = Arrays.asList(
                new SignatureData(
                        "0x1b",
                        "0x4e0fd15eb17809eed3f3b92de7a5d785fa462d4a26c671355c78f2e0ec643cf4",
                        "0x520fac71728145f92018466b724da7ab447b4ef07037d48efe3c3db6b6c5680d"
                ),
                new SignatureData(
                        "0x1c",
                        "0x84bb6abdafa54f456ade2de682a86ca8937f9bb5495966b856e9b6dd5b74c051",
                        "0x256e505d8e406b7ea7f37834b46b343fdc0658808762f94884921b8e9f3c5275"
                )
        );

        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":5,\n" +
                    "  \"key\":[\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x1bd81d8db362bb71ada110b6ce3c5c2cb83a09408b737514dbdd00aec664cb15\",\n" +
                    "              \"y\":\"0xc32180d23b23e0adff616ad7e47c47055c429f49729a564cfdef3a20d30c6484\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x37f5234c9f99ebf5cf4e54038d1fcd6c18f3dc80217d41581c22190310ab4546\",\n" +
                    "              \"y\":\"0x542eaa9a24a5aa5d4a6c5729c24c7b82006747408218dffdc3712635fc04bf2a\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x2524a74b1cdaca02dcba217dfc4c12e1ed211f0625a5625d3d46eba97e2b6f3e\",\n" +
                    "              \"y\":\"0x7dfb71b9444a455a88a2c65d5bcc17a4fbd78be0f0c638570d2e380273210ee0\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x0784d5e42363fb1828981e83506bcb3d0ee99bf5817513a0e828087b61ecbe78\",\n" +
                    "              \"y\":\"0xb9fd0686c949e722a201d568e83029cd5abaf2cd59b140ef7e6790a936576f20\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x953ba821d928da92be9455db80413b7f777ac1fccd2fc08c975e7d70631278fa\",\n" +
                    "              \"y\":\"0x35288de9c9b87588209c7aab345bd13244e68221b9fbeed7f60ba3769ba7efd8\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8b3f3ee33166683a0f6084f3d6030c184bb8901cf948447705b51399b752a766\",\n" +
                    "              \"y\":\"0x10135ab0b296efbdd68477f56541deaadd63cc253bcea61caac08596c2338fbc\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withMessage() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, roleTxSignatures, address));
        }

        @Test
        public void withMessageHash() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(hashedMessage, roleTxSignatures, address));
        }

        @Test
        public void lessThanThreshold() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            List<SignatureData> sigList = Arrays.asList(roleTxSignatures.get(1));

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(hashedMessage, sigList, address));
        }

        @Test
        public void satisfiedThresholdWithInvalidSig() throws IOException {
            SignatureData invalid = new SignatureData(
                    "0x1b",
                    "0x8213e560e7bbe1f2e28fd69cbbb41c9108b84c98cd7c2c88d3c8e3549fd6ab10",
                    "0x3ca40c9e20c1525348d734a6724db152b9244bff6e0ff0c2b811d61d8f874f00"
            );

            List<SignatureData> sigList = new ArrayList<>();
            sigList.add(invalid);
            sigList.addAll(roleTxSignatures);

            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSignedMessage(message, sigList, address));
        }

        @Test
        public void withAccountUpdateRoleSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, roleAccountUpdateSignatures, address));
        }

        @Test
        public void withFeePayerRoleSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSignedMessage(message, roleFeePayerSignatures, address));
        }
    }

    public static class validateSender_AccountKeyLegacy {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() {
            accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));
        }

        @Test
        public void withValidateSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            LegacyTransaction tx = new LegacyTransaction.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fe9",
                            "0xecdec357060dbbb4bd3790e98b1733ec3a0b02b7e4ec7a5622f93cd9bee229fe",
                            "0x0a4a5e28753e7c1d999b286fb07933c5bf353079b8ed4d1ed509a838b48be02c"
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSender(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            LegacyTransaction tx = new LegacyTransaction.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fe9",
                            "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                            "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }
    }

    public static class validateSender_AccountKeyPublic {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String publicKeyJson = "{\n" +
                    "  \"keyType\":2,\n" +
                    "  \"key\":{\n" +
                    "    \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "    \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(publicKeyJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidateSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fea",
                            "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                            "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSender(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fe9",
                            "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                            "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }
    }

    public static class validateSender_AccountKeyFail {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String accountKeyDataJson = "{\n" +
                    "  \"keyType\":3,\n" +
                    "  \"key\":{}\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(accountKeyDataJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fea",
                            "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                            "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }
    }

    public static class validateSender_AccountKeyWeightedMultiSig {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":4,\n" +
                    "  \"key\":{\n" +
                    "    \"threshold\":3,\n" +
                    "    \"keys\":[\n" +
                    "      {\n" +
                    "        \"weight\":2,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "          \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "          \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x3919091ba17c106dd034af508cfe00b963d173dffab2c7702890e25a96d107ca\",\n" +
                    "          \"y\":\"0x1bb4f148ee1984751e57d2435468558193ce84ab9a7731b842e9672e40dc0f22\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidateSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                                    "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x63177648732ef855f800eb9f80f68501abb507f84c0d660286a6e0801334a1d2",
                                    "0x620a996623c114f2df35b11ec8ac4f3758d3ad89cf81ba13614e51908cfe9218"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                                    "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSender(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fe9",
                                    "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                                    "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }

        @Test
        public void lessThanThreshold() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                                    "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }
    }

    public static class validateSender_AccountKeyRoleBased {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":5,\n" +
                    "  \"key\":[\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "              \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "              \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x3919091ba17c106dd034af508cfe00b963d173dffab2c7702890e25a96d107ca\",\n" +
                    "              \"y\":\"0x1bb4f148ee1984751e57d2435468558193ce84ab9a7731b842e9672e40dc0f22\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "              \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "              \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "              \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "              \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                                    "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x63177648732ef855f800eb9f80f68501abb507f84c0d660286a6e0801334a1d2",
                                    "0x620a996623c114f2df35b11ec8ac4f3758d3ad89cf81ba13614e51908cfe9218"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                                    "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSender(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            ValueTransfer tx = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fe9",
                                    "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                                    "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateSender(tx));
        }

        @Test
        public void validSignatureWithAccountUpdateRole() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            AccountUpdate tx = new AccountUpdate.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setAccount(Account.createWithAccountKeyLegacy("0xf21460730845e3652aa3cc9bc13b345e4f53984a"))
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x84299d74e8b491d7272d86b5ff4f4f4605830406befd360c90adaae56af99359",
                                    "0x196240cda43810ba4c19dd865435b991a9c16a91859357777594fb9e77d02d01"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0xaf27d2163b85e3de5f8b7fee56df509be231d3935890515bfe783e2f38c1c092",
                                    "0x1b5d6ff80bd3964ce311c658cdeac0e43a2171a87bb287695c9be2b3517651e9"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0xf17ec890c3eeae90702f811b4bb880c6631913bb307207bf0bccbcdc229f571a",
                                    "0x6f2f203218cc8ddbab785cd59dec47105c7919ab4192295c8307c9a0701605ed"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateSender(tx));
        }
    }

    public static class validateFeePayer_AccountKeyLegacy {
        static AccountKey accountKey;
        static FeeDelegatedValueTransfer.Builder txBuilder;

        @BeforeClass
        public static void init() {
            accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            txBuilder = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0xcb2bbf04a12ec3a06163c30ce8782739ec4745a53e265aa9443f1c0d678bb871",
                                    "0x7dd348c7d8fce6be36b661f116973d1c36cc92a389ad4a1a4053bd486060a083"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x6d5dfca992d6833c0da272578bc6ea941be45f44fb2fa114310ebe18d673ed52",
                                    "0x4dc5cd7985c9ce7d44d46d65e65c995a4a8c97159a1eed8b2efb0510b981ab7c"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x945151edf556fbcebf832092d4534b9a3b1f3d46f85bce09e7d7211070cb57be",
                                    "0x1617c8f918f96970baddd12f240a9824eca6b29d91eb7333adacb987f2dcd8dd"
                            )
                    ))
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ));
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = txBuilder.build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateFeePayer(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x945151edf556fbcebf832092d4534b9a3b1f3d46f85bce09e7d7211070cb57be",
                                    "0x1617c8f918f96970baddd12f240a9824eca6b29d91eb7333adacb987f2dcd8dd"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }
    }

    public static class validateFeePayer_AccountKeyPublic {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String publicKeyJson = "{\n" +
                    "  \"keyType\":2,\n" +
                    "  \"key\":{\n" +
                    "    \"x\":\"0x2b557d80ddac3a0bbcc8a7861773ca7434c969e2721a574bb94a1e3aa5ceed38\",\n" +
                    "    \"y\":\"0x19f08a82b31682c038f9f691fb38ee4aaf7e016e2c973a1bd1e48a51f60a54ea\"\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(publicKeyJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateFeePayer(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x945151edf556fbcebf832092d4534b9a3b1f3d46f85bce09e7d7211070cb57be",
                                    "0x1617c8f918f96970baddd12f240a9824eca6b29d91eb7333adacb987f2dcd8dd"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }
    }

    public static class validateFeePayer_AccountKeyFail {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String accountKeyDataJson = "{\n" +
                    "  \"keyType\":3,\n" +
                    "  \"key\":{}\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(accountKeyDataJson, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }
    }

    public static class validatedFeePayer_AccountKeyWeightedMultiSig {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":4,\n" +
                    "  \"key\":{\n" +
                    "    \"threshold\":3,\n" +
                    "    \"keys\":[\n" +
                    "      {\n" +
                    "        \"weight\":2,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x2b557d80ddac3a0bbcc8a7861773ca7434c969e2721a574bb94a1e3aa5ceed38\",\n" +
                    "          \"y\":\"0x19f08a82b31682c038f9f691fb38ee4aaf7e016e2c973a1bd1e48a51f60a54ea\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0x1a1cfe1e2ec4b15520c57c20c2460981a2f16003c8db11a0afc282abf929fa1c\",\n" +
                    "          \"y\":\"0x1868f60f91b330c423aa660913d86acc2a0b1b15e7ba1fe571e5928a19825a7e\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"weight\":1,\n" +
                    "        \"key\":{\n" +
                    "          \"x\":\"0xdea23a89dbbde1a0c26466c49c1edd32785432389641797038c2b53815cb5c73\",\n" +
                    "          \"y\":\"0xd6cf5355986fd9a22a68bb57b831857fd1636362b383bd632966392714b60d72\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidateSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateFeePayer(tx));
        }

        @Test
        public void withInvalidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x945151edf556fbcebf832092d4534b9a3b1f3d46f85bce09e7d7211070cb57be",
                                    "0x1617c8f918f96970baddd12f240a9824eca6b29d91eb7333adacb987f2dcd8dd"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }

        @Test
        public void lessThanThreshold() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }
    }

    public static class validateFeePayer_AccountKeyRoleBased {
        static AccountKey accountKey;

        @BeforeClass
        public static void init() throws IOException {
            String json = "{\n" +
                    "  \"keyType\":5,\n" +
                    "  \"key\":[\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "              \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "              \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x3919091ba17c106dd034af508cfe00b963d173dffab2c7702890e25a96d107ca\",\n" +
                    "              \"y\":\"0x1bb4f148ee1984751e57d2435468558193ce84ab9a7731b842e9672e40dc0f22\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d9\",\n" +
                    "              \"y\":\"0x6af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xc7751c794337a93e4db041fb5401c2c816cf0a099d8fd4b1f3f555aab5dfead2\",\n" +
                    "              \"y\":\"0x417521bb0c03d8637f350df15ef6a6cb3cdb806bd9d10bc71982dd03ff5d9ddd\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"keyType\":4,\n" +
                    "      \"key\":{\n" +
                    "        \"threshold\":2,\n" +
                    "        \"keys\":[\n" +
                    "          {\n" +
                    "            \"weight\":2,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x2b557d80ddac3a0bbcc8a7861773ca7434c969e2721a574bb94a1e3aa5ceed38\",\n" +
                    "              \"y\":\"0x19f08a82b31682c038f9f691fb38ee4aaf7e016e2c973a1bd1e48a51f60a54ea\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0x1a1cfe1e2ec4b15520c57c20c2460981a2f16003c8db11a0afc282abf929fa1c\",\n" +
                    "              \"y\":\"0x1868f60f91b330c423aa660913d86acc2a0b1b15e7ba1fe571e5928a19825a7e\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"weight\":1,\n" +
                    "            \"key\":{\n" +
                    "              \"x\":\"0xdea23a89dbbde1a0c26466c49c1edd32785432389641797038c2b53815cb5c73\",\n" +
                    "              \"y\":\"0xd6cf5355986fd9a22a68bb57b831857fd1636362b383bd632966392714b60d72\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            AccountKey.AccountKeyData accountKeyData = mapper.readValue(json, AccountKey.AccountKeyData.class);
            accountKey = new AccountKey();
            accountKey.setResult(accountKeyData);
        }

        @Test
        public void withValidSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertTrue(validator.validateFeePayer(tx));
        }

        @Test
        public void withInvalidFeePayerSignature() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fe9",
                                    "0x86c8ecbfd892be41d48443a2243274beb6daed3f72895045965a3baede4c350e",
                                    "0x69ea748aff6e4c106d3a8ba597d8f134745b76f12dacb581318f9da07351511a"
                            )
                    ))
                    .build();

            Validator validator = new Validator(klay);
            assertFalse(validator.validateFeePayer(tx));
        }
    }

    public static class validateTransaction {
        static ValueTransfer.Builder txBuilder;
        static FeeDelegatedValueTransfer.Builder fdTxBuilder;
        static EthereumAccessList.Builder accessTxBuilder;
        static AccountKey accountKey;

        @BeforeClass
        public static void init() {
            accountKey = new AccountKey();
            accountKey.setResult(new AccountKey.AccountKeyData(AccountKeyLegacy.getType(), new AccountKeyLegacy()));

            txBuilder = new ValueTransfer.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(new SignatureData(
                            "0x0fea",
                            "0x2b5934c6d26bb3e65edf099d79c57c743d2f70744ca09d3ba9a1099edff9f173",
                            "0x0797886edff4b449c1a599943e3a6003ae9e46b3f3f34862ced327e43fba3a6a"
                    ));

            fdTxBuilder = new FeeDelegatedValueTransfer.Builder()
                    .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                    .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0xcb2bbf04a12ec3a06163c30ce8782739ec4745a53e265aa9443f1c0d678bb871",
                                    "0x7dd348c7d8fce6be36b661f116973d1c36cc92a389ad4a1a4053bd486060a083"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0x6d5dfca992d6833c0da272578bc6ea941be45f44fb2fa114310ebe18d673ed52",
                                    "0x4dc5cd7985c9ce7d44d46d65e65c995a4a8c97159a1eed8b2efb0510b981ab7c"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x945151edf556fbcebf832092d4534b9a3b1f3d46f85bce09e7d7211070cb57be",
                                    "0x1617c8f918f96970baddd12f240a9824eca6b29d91eb7333adacb987f2dcd8dd"
                            )
                    ))
                    .setFeePayerSignatures(Arrays.asList(
                            new SignatureData(
                                    "0x0fea",
                                    "0x86fd17d788e89a6e0639395b3c0a04f916103debd6cbe639d6f4ff5034dde3e8",
                                    "0x0795551c551d9096234c290689767f34f2d409c95166ab18d216dbc93845ba16"
                            ),
                            new SignatureData(
                                    "0x0fea",
                                    "0x0653b6d1cdb90462094b089ce8e2fed0e3b8ec2c44125965e1a5af286644c758",
                                    "0x259b10e3bf594d48535fd0d95e15d095897c8d075c01dd56e7417d5943b0d53a"
                            ),
                            new SignatureData(
                                    "0x0fe9",
                                    "0xce8d051427adab10d1dc93de49123aeab18ba8aadedce0d57ef5b7fa451b1f4f",
                                    "0x4fe2a845d92ff48abca3e1d59637fab5f4a4e3172d91772d9bfce60760edc506"
                            )
                    ));

            AccessList accessList = new AccessList(
                    Arrays.asList(
                            new AccessTuple(
                                    "0x0000000000000000000000000000000000000001",
                                    Arrays.asList(
                                            "0x0000000000000000000000000000000000000000000000000000000000000000"
                                    )
                            ))
            );

            accessTxBuilder = new EthereumAccessList.Builder()
                    .setFrom("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b")
                    .setTo("0x7b65b75d204abed71587c9e519a89277766ee1d0")
                    .setValue("0xa")
                    .setChainId("0x2")
                    .setGasPrice("0x19")
                    .setNonce("0x4d2")
                    .setGas("0xf4240")
                    .setInput("0x31323334")
                    .setAccessList(accessList)
                    .setSignatures(new SignatureData(
                            "0x1",
                            "0xbfc80a874c43b71b67c68fa5927d1443407f31aef4ec6369bbecdb76fc39b0c0",
                            "0x193e62c1dd63905aee7073958675dcb45d78c716a9a286b54a496e82cb762f26"
                    ));

        }

        @Test
        public void isCallValidateSender() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);

            Validator spyValidator = Mockito.spy(validator);
            spyValidator.validateTransaction(txBuilder.build());
            Mockito.verify(spyValidator).validateSender(any());

            Validator spyValidator1 = Mockito.spy(validator);
            spyValidator1.validateTransaction(accessTxBuilder.build());
            Mockito.verify(spyValidator1).validateSender(any());
        }

        @Test
        public void isCallValidateFeePayer() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);

            Validator spyValidator = Mockito.spy(validator);
            spyValidator.validateTransaction(fdTxBuilder.build());

            Mockito.verify(spyValidator).validateSender(any());
            Mockito.verify(spyValidator).validateFeePayer(any());
        }

        @Test
        public void validateTransaction() throws IOException {
            Klay klay = mock(Klay.class, RETURNS_DEEP_STUBS);
            when(klay.getAccountKey(anyString()).send()).thenReturn(accountKey);

            Validator validator = new Validator(klay);
            assertTrue(validator.validateTransaction(txBuilder.build()));
            assertTrue(validator.validateTransaction(fdTxBuilder.build()));
            assertTrue(validator.validateTransaction(accessTxBuilder.build()));
        }
    }
}