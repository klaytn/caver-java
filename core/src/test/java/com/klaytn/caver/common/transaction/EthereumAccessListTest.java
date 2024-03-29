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

package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.EthereumAccessList;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class EthereumAccessListTest {

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String from = "0x";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String chainID = "0x1";
        String input = "0x31323334";
        String value = "0xa";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                                Arrays.asList(
                                        "0x4f42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                                        "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717"
                                )
                        ))
        );

        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Test
        public void createInstance() throws IOException {
            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(keyring.getAddress())
                            .setNonce("0x")
                            .setGas(gas)
                            .setGasPrice("0x")
                            .setChainId("0x")
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
                            .setAccessList(accessList)
            );

            assertNotNull(ethereumAccessList);

            ethereumAccessList.fillTransaction();
            assertNotNull(ethereumAccessList.getNonce());
            assertNotNull(ethereumAccessList.getGasPrice());
            assertNotNull(ethereumAccessList.getChainId());
            assertEquals(accessList, ethereumAccessList.getAccessList());
            assertEquals(TransactionType.TxTypeEthereumAccessList.toString(), ethereumAccessList.getType());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid";

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
                            .setAccessList(accessList)
            );
        }

        @Test
        public void throwException_invalid_To() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid";

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
                            .setAccessList(accessList)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
                            .setAccessList(accessList)
            );
        }
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        static String nonce = "0x4D2";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String chainID = "0x1";
        static String input = "0x31323334";
        static String value = "0xa";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                                Arrays.asList(
                                        "0x4f42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                                        "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717"
                                )
                        ))
        );

        @Test
        public void BuilderTest() {
            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            assertNotNull(ethereumAccessList);
            assertEquals(TransactionType.TxTypeEthereumAccessList.toString(), ethereumAccessList.getType());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setInput(input)
                    .setValue(Numeric.toBigInt(value))
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            assertEquals(gas, ethereumAccessList.getGas());
            assertEquals(gasPrice, ethereumAccessList.getGasPrice());
            assertEquals(chainID, ethereumAccessList.getChainId());
            assertEquals(value, ethereumAccessList.getValue());
            assertEquals(accessList, ethereumAccessList.getAccessList());
        }

        @Test
        public void BuilderTestRPC() throws IOException {
            String gas = "0x0f4240";
            String to = "7b65b75d204abed71587c9e519a89277766ee1d0";
            String input = "0x31323334";
            String value = "0x0a";

            Caver caver = new Caver(Caver.DEFAULT_URL);
            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setInput(input)
                    .setValue(value)
                    .setFrom(keyring.getAddress()) // For Test. It automatically filled when executed EthereumAccessList.signWithKey or signWithKeysTest.
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            ethereumAccessList.fillTransaction();
            assertNotNull(ethereumAccessList.getNonce());
            assertNotNull(ethereumAccessList.getGasPrice());
            assertNotNull(ethereumAccessList.getChainId());
            assertNotNull(ethereumAccessList.getAccessList());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setTo(to)
                    .setValue("0x")
                    .setAccessList(accessList)
                    .build();
        }

        @Test
        public void throwException_invalid_value2() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setTo(to)
                    .setValue("invalid")
                    .build();
        }

        @Test
        public void throwException_invalid_To() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String gas = "0xf4240";
            String to = "invalid";
            String input = "0x31323334";
            String value = "0xa";

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing");

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();
        }
    }

    public static class getRLPEncodingAndDecodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

        @Test
        public void getRLPEncodingAndDecoding() {
            String gas = "0x1e241";
            String gasPrice = "0x0a";
            String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
            String chainID = "0x1";
            String input = "0x616263646566";
            String value = "0x0";
            AccessList accessList1 = new AccessList(Arrays.asList(
                    new AccessTuple("0x0000000000000000000000000000000000000001",
                            Arrays.asList("0x0000000000000000000000000000000000000000000000000000000000000000"))
            ));
            AccessList accessList2 = new AccessList(Arrays.asList(
                    new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000",
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));
            AccessList accessList3 = new AccessList(Arrays.asList(
                    new AccessTuple("0x0000000000000000000000000000000000000001",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000"
                            )),
                    new AccessTuple("0x0000000000000000000000000000000000000002",
                            Arrays.asList(
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            )),
                    new AccessTuple("0x0000000000000000000000000000000000000003",
                            Arrays.asList(
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x5d2368dff6ab943d3467558de70805d5b6a4367ab94b1ee8a7ae53e4e0f68293"),
                    Numeric.hexStringToByteArray("0x1d68f38dce681c32cd001ca155b2b27e26ddd788b4bdaaf355181c626805ec7f")
            );
            SignatureData signatureData2 = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x19676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99"),
                    Numeric.hexStringToByteArray("0x70feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235")
            );
            SignatureData signatureData3 = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x3966e6b3297cbcfdbb3e1e3ede1b80dea99d9d01983fc32e337d785fe4445973"),
                    Numeric.hexStringToByteArray("0x49e25fcda7c8d5517181e2afcf13d2dff77dc33884b512fd1f1cd0770bcd0c59")
            );
            SignatureData signatureData4 = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x5d6d9bc7bb01b05db25f5f2e4a995a4970124387293694f0fd8bdda95bc6e7f4"),
                    Numeric.hexStringToByteArray("0x782feaf0460341b320710ef7a4f07167d551fd897775af5ec2f1dea095e99cb")
            );

            // Test-1: encoding EthereumAccessList which have an address and a storage key.
            String expectedRLP = "0x7801f88701040a8301e241808080f838f7940000000000000000000000000000000000000001e1a0000000000000000000000000000000000000000000000000000000000000000001a05d2368dff6ab943d3467558de70805d5b6a4367ab94b1ee8a7ae53e4e0f68293a01d68f38dce681c32cd001ca155b2b27e26ddd788b4bdaaf355181c626805ec7f";
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setNonce("0x4")
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setAccessList(accessList1)
                            .setSignatures(signatureData1)
            );
            assertEquals(expectedRLP, ethereumAccessList.getRLPEncoding());
            // Test-1: decoding
            EthereumAccessList decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // Test-2: encoding EthereumAccessList which have many storageKeys
            expectedRLP = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";
            ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setTo(to)
                            .setNonce(BigInteger.valueOf(4))
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(accessList2)
                            .setSignatures(signatureData2)
            );
            assertEquals(expectedRLP, ethereumAccessList.getRLPEncoding());
            // Test-2: decoding
            decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // Test-3: encoding EthereumAccessList which have empty accessList
            expectedRLP = "0x7801f86801030a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878086616263646566c001a03966e6b3297cbcfdbb3e1e3ede1b80dea99d9d01983fc32e337d785fe4445973a049e25fcda7c8d5517181e2afcf13d2dff77dc33884b512fd1f1cd0770bcd0c59";
            ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setTo(to)
                            .setNonce(BigInteger.valueOf(3))
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(null)
                            .setInput(input)
                            .setSignatures(signatureData3)
            );
            assertEquals(expectedRLP, ethereumAccessList.getRLPEncoding());
            // Test-3: decoding
            decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // Test-4: encoding EthereumAccessList which have many accessList
            expectedRLP = "0x7801f9013d01040a8301e241808080f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f859940000000000000000000000000000000000000002f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681f859940000000000000000000000000000000000000003f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a05d6d9bc7bb01b05db25f5f2e4a995a4970124387293694f0fd8bdda95bc6e7f4a00782feaf0460341b320710ef7a4f07167d551fd897775af5ec2f1dea095e99cb";
            ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setNonce(BigInteger.valueOf(4))
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(accessList3)
                            .setSignatures(signatureData4)
            );
            assertEquals(expectedRLP, ethereumAccessList.getRLPEncoding());
            // Test-4: decoding
            decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));
        }

        @Test
        public void getRLPEncodingAndDecodingYParity() {
            // 0 y-parity
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setTo("0xc6779d72a88bec1a03bbb83cf028d95ff5f32f5b")
                            .setValue("0x1")
                            .setGas("0x9c40")
                            .setNonce("0x1a")
                            .setGasPrice("0x5d21dba00")
                            .setChainId("0x2710")
                            .setInput("0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039")
                            .setAccessList(caver.transaction.utils.accessList.create(Arrays.asList(
                                caver.transaction.utils.accessTuple.create(
                                        "0xac9ba2a7fb8572e971bcac01a5b58934b385a172",
                                        Arrays.asList(
                                            "0x0000000000000000000000000000000000000000000000000000000000000003",
                                            "0x0000000000000000000000000000000000000000000000000000000000000007"
                                        )
                                )
                            )))
                            .setSignatures(new SignatureData(
                                    "0x00",
                                    "0x43ff73938e019e13dcc48c9ff1a46d9f1f081512351cf7b0eca49dbf74047848",
                                    "0x17a9816ca1446f51e0d6eb8c406a52758feb83b234128e4cfcaeaa8419f706af"
                            ))
            );
            String rlpEncoded = ethereumAccessList.getRLPEncoding();
            Assert.assertEquals("0x7801f901098227101a8505d21dba00829c4094c6779d72a88bec1a03bbb83cf028d95ff5f32f5b01b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf85994ac9ba2a7fb8572e971bcac01a5b58934b385a172f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000780a043ff73938e019e13dcc48c9ff1a46d9f1f081512351cf7b0eca49dbf74047848a017a9816ca1446f51e0d6eb8c406a52758feb83b234128e4cfcaeaa8419f706af", rlpEncoded);
            EthereumAccessList decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(rlpEncoded);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // 1 y-parity
            ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setTo("0xc5fb1386b60160614a8151dcd4b0ae41325d1cb8")
                            .setValue("0x1")
                            .setGas("0x9c40")
                            .setNonce("0x23")
                            .setGasPrice("0x5d21dba00")
                            .setChainId("0x2710")
                            .setInput("0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039")
                            .setAccessList(caver.transaction.utils.accessList.create(Arrays.asList(
                                    caver.transaction.utils.accessTuple.create(
                                            "0x5430192ae264b3feff967fc08982b9c6f5694023",
                                            Arrays.asList(
                                                    "0x0000000000000000000000000000000000000000000000000000000000000003",
                                                    "0x0000000000000000000000000000000000000000000000000000000000000007"
                                            )
                                    )
                            )))
                            .setSignatures(new SignatureData(
                                    "0x01",
                                    "0x5ac25e47591243af2d6b8e7f54d608e9e0e0aeb5194d34c17852bd7e376f4857",
                                    "0x095a40394f33e95cce9695d5badf4270f4cc8aff0b5395cefc3a0fe213be1f30"
                            ))
            );

            rlpEncoded = ethereumAccessList.getRLPEncoding();
            Assert.assertEquals("0x7801f90109822710238505d21dba00829c4094c5fb1386b60160614a8151dcd4b0ae41325d1cb801b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf859945430192ae264b3feff967fc08982b9c6f5694023f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000701a05ac25e47591243af2d6b8e7f54d608e9e0e0aeb5194d34c17852bd7e376f4857a0095a40394f33e95cce9695d5badf4270f4cc8aff0b5395cefc3a0fe213be1f30", ethereumAccessList.getRLPEncoding());
            decodedEthereumAccessList = (EthereumAccessList) caver.transaction.decode(rlpEncoded);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String gas = "0xf4240";
            String gasPrice = "0x19";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x1"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumAccessList.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumAccessList.getRLPEncoding();
        }

        @Test
        public void throwException_NoChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String gasPrice = "0x10";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumAccessList.getRLPEncoding();
        }


    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String gas = "0x1e241";
        static String gasPrice = "0x0a";
        static String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
        static String chainID = "0x1";
        static String input = "0x616263646566";
        static String value = "0x0";
        static String nonce = "0x4";

        static AccessList accessList = new AccessList(Arrays.asList(
                new AccessTuple("0x0000000000000000000000000000000000000001",
                        Arrays.asList(
                                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        )),
                new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                        Arrays.asList(
                                "0xd2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761",
                                "0xd2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7c"
                        )),
                new AccessTuple("0x0000000000000000000000000000000000000003",
                        Arrays.asList(
                                "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                        ))
        ));

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x01"),
                Numeric.hexStringToByteArray("0x26e8a6755fbf9d8d32271753c332718825881fef855e7f9e4868e2d16f908caa"),
                Numeric.hexStringToByteArray("0x412c0e1dc1fe87fcc7b262be995701dc0606038844f8a0da1eaddef965d8fc2b")
        );

        @Test
        public void getTransactionHash() {
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            String expected = "0x5f4484fe404d9e5b3c100d1b67aa5633daab5d516267bf8af03550644e2d7378";
            String txHash = ethereumAccessList.getTransactionHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumAccessList.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumAccessList.getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_ChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumAccessList.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String gas = "0x1e241";
        static String gasPrice = "0x0a";
        static String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
        static String chainID = "0x1";
        static String input = "0x616263646566";
        static String value = "0x0";
        static String nonce = "0x4";

        static AccessList accessList = new AccessList(Arrays.asList(
                new AccessTuple("0x0000000000000000000000000000000000000001",
                        Arrays.asList(
                                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        )),
                new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                        Arrays.asList(
                                "0xd2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761",
                                "0xd2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7c"
                        )),
                new AccessTuple("0x0000000000000000000000000000000000000003",
                        Arrays.asList(
                                "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                        ))
        ));

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x01"),
                Numeric.hexStringToByteArray("0x26e8a6755fbf9d8d32271753c332718825881fef855e7f9e4868e2d16f908caa"),
                Numeric.hexStringToByteArray("0x412c0e1dc1fe87fcc7b262be995701dc0606038844f8a0da1eaddef965d8fc2b")
        );

        @Test
        public void getTransactionHash() {
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            String expected = "0x5f4484fe404d9e5b3c100d1b67aa5633daab5d516267bf8af03550644e2d7378";
            String txHash = ethereumAccessList.getSenderTxHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumAccessList.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumAccessList.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_ChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumAccessList.getSenderTxHash();
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
;
        @Test
        public void combineSignature() {
            String gas = "0x1e241";
            String gasPrice = "0x0a";
            String chainID = "0x1";
            String nonce = "0x4";
            AccessList accessList = new AccessList(Arrays.asList(
                    new AccessTuple("0x0000000000000000000000000000000000000001",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000"
                            )),
                    new AccessTuple("0x0000000000000000000000000000000000000002",
                            Arrays.asList(
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            )),
                    new AccessTuple("0x0000000000000000000000000000000000000003",
                            Arrays.asList(
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x5d6d9bc7bb01b05db25f5f2e4a995a4970124387293694f0fd8bdda95bc6e7f4"),
                    Numeric.hexStringToByteArray("0x782feaf0460341b320710ef7a4f07167d551fd897775af5ec2f1dea095e99cb")
            );

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(accessList)
            );

            // rlpEncoded is a rlp encoded EthereumAccessList transaction containing signatureData defined above.
            String rlpEncoded = "0x7801f9013d01040a8301e241808080f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f859940000000000000000000000000000000000000002f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681f859940000000000000000000000000000000000000003f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a05d6d9bc7bb01b05db25f5f2e4a995a4970124387293694f0fd8bdda95bc6e7f4a00782feaf0460341b320710ef7a4f07167d551fd897775af5ec2f1dea095e99cb";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = ethereumAccessList.combineSignedRawTransactions(rlpList);

            assertEquals(rlpEncoded, combined);
        }

        @Test
        public void combineSignature_EmptySig() {
            String gas = "0x1e241";
            String gasPrice = "0x0a";
            String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
            String chainID = "0x1";
            String input = "0x616263646566";
            String value = "0x0";
            String nonce = "0x4";
            AccessList accessList = new AccessList(Arrays.asList(
                    new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000",
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x19676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99"),
                    Numeric.hexStringToByteArray("0x70feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235")
            );

            SignatureData emptySig = SignatureData.getEmptySignature();

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setTo(to)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(accessList)
                            .setSignatures(emptySig)
            );

            // rlpEncoded is a rlp encoded EthereumAccessList transaction containing signatureData defined above.
            String rlpEncoded = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = ethereumAccessList.combineSignedRawTransactions(rlpList);

            assertEquals(rlpEncoded, combined);
        }

        @Test
        public void throwException_existSignature() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");

            String gas = "0x1e241";
            String gasPrice = "0x0a";
            String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
            String chainID = "0x1";
            String input = "0x616263646566";
            String value = "0x0";
            String nonce = "0x4";
            AccessList accessList = new AccessList(Arrays.asList(
                    new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000",
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x01"),
                    Numeric.hexStringToByteArray("0x19676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99"),
                    Numeric.hexStringToByteArray("0x70feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235")
            );

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setFrom(null)
                            .setTo(to)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccessList(accessList)
                            .setSignatures(new SignatureData(
                                    Numeric.hexStringToByteArray("0x1"),
                                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
                            ))
            );

            // rlpEncoded is a rlp encoded EthereumAccessList transaction containing signatureData defined above.
            String rlpEncoded = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            ethereumAccessList.combineSignedRawTransactions(rlpList);
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            String gas = "0x1e241";
            String gasPrice = "0x0a";
            String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
            String chainID = "0x1";
            String input = "0x616263646566";
            String value = "0x0";
            String nonce = "0x4";
            AccessList accessList = new AccessList(Arrays.asList(
                    new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                            Arrays.asList(
                                    "0x0000000000000000000000000000000000000000000000000000000000000000",
                                    "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                            ))
            ));
            // rlpEncoded is a rlp encoded string of EthereumAccessList transaction containing above fields.
            String rlpEncoded = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc01a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";

            // All fields are same with above rlpEncoded EthereumAccessList transaction except accessList field.
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setAccessList(new AccessList(Arrays.asList(
                                    new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                                            Arrays.asList(
                                                    "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                                            ))
                            )))
                            .setTo(to)
            );


            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            ethereumAccessList.combineSignedRawTransactions(rlpList);
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
        static BigInteger value = BigInteger.ONE;
        static BigInteger gas = BigInteger.valueOf(90000);
        static String gasPrice = "0x19";
        static String nonce = "0x3a";
        static BigInteger chainID = BigInteger.valueOf(2019);
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                                Arrays.asList(
                                        "0x4f42391603e79b2a90c3fbfc070c995eb1163e0ac00fb4e8f3da2dc81c451b98",
                                        "0xc4a32abdf1905059fdfc304aae1e8924279a36b2a6428552237f590156ed7717"
                                )
                        ))
        );

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x1"),
                Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
        );

        @Test
        public void appendSignature() {
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setAccessList(accessList)
            );

            ethereumAccessList.appendSignatures(signatureData);

            assertEquals(signatureData, ethereumAccessList.getSignatures().get(0));
        }

        @Test
        public void throwException_invalidParity() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid signature: The y-parity of the transaction should either be 0 or 1.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setAccessList(accessList)
            );

            ethereumAccessList.appendSignatures(new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            ));
        }


        @Test
        public void throwException_setSignature_invalidParity() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid signature: The y-parity of the transaction should either be 0 or 1.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setAccessList(accessList)
                            .setSignatures(new SignatureData(
                                    Numeric.hexStringToByteArray("0x25"),
                                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
                            ))
            );
        }


        @Test
        public void appendSignatureWithEmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setAccessList(accessList)
                            .setSignatures(emptySignature)
            );

            ethereumAccessList.appendSignatures(signatureData);

            assertEquals(signatureData, ethereumAccessList.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setAccessList(accessList)
                            .setTo(to)
            );

            ethereumAccessList.appendSignatures(list);

            assertEquals(signatureData, ethereumAccessList.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            SignatureData emptySignature = SignatureData.getEmptySignature();

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(emptySignature)
            );

            ethereumAccessList.appendSignatures(list);

            assertEquals(signatureData, ethereumAccessList.getSignatures().get(0));
        }

        @Test
        public void throwException_appendData_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumAccessList.appendSignatures(signatureData);
        }

        @Test
        public void throwException_appendList_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(list)
            );

            ethereumAccessList.appendSignatures(list);
        }

        @Test
        public void throwException_tooLongSignatures() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures are too long " + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);
            list.add(signatureData);

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setValue(value)
            );

            ethereumAccessList.appendSignatures(list);
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String gas = "0x1e241";
        static String gasPrice = "0x0a";
        static String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
        static String chainID = "0x1";
        static String input = "0x616263646566";
        static String value = "0x0";
        static String nonce = "0x4";

        static AccessList accessList = new AccessList(Arrays.asList(
                new AccessTuple("0x0000000000000000000000000000000000000001",
                        Arrays.asList(
                                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        )),
                new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                        Arrays.asList(
                                "0xd2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761",
                                "0xd2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7c"
                        )),
                new AccessTuple("0x0000000000000000000000000000000000000003",
                        Arrays.asList(
                                "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681",
                                "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc"
                        ))
        ));

        @Test
        public void getRLPEncodingForSignature() {
            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
            );

            String expected = "0x01f9011401040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878086616263646566f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f85994284e47e6130523b2507ba38cea17dd40a20a0cd0f842a0d2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7ca0d2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761f859940000000000000000000000000000000000000003f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681";
            String rlpEncodedSign = ethereumAccessList.getRLPEncodingForSignature();

            assertEquals(expected, rlpEncodedSign);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
            );

            String encoded = ethereumAccessList.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
            );
            String encoded = ethereumAccessList.getRLPEncodingForSignature();

        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
            );
            String encoded = ethereumAccessList.getRLPEncodingForSignature();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver();

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String gas = "0x1e241";
        static String gasPrice = "0x0a";
        static String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
        static String chainID = "0x1";
        static String input = "0x616263646566";
        static String value = "0x1";
        static String nonce = "0x4";

        static AccessList accessList = new AccessList(Arrays.asList(
                new AccessTuple("0x0000000000000000000000000000000000000001",
                        Arrays.asList(
                                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        )),
                new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                        Arrays.asList(
                                "0xd2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7c",
                                "0xd2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761"
                        )),
                new AccessTuple("0x0000000000000000000000000000000000000003",
                        Arrays.asList(
                                "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc",
                                "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681"
                        ))
        ));

        public static EthereumAccessList createEthereumAccessList() {
            return caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setTo(to)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setInput(input)
                            .setChainId(chainID)
                            .setValue(value)
                            .setAccessList(accessList)
            );
        }

        static SignatureData expectedSignature = new SignatureData(
                "0x01",
                "0xbf84d5909e08e2e2bb1d5fa975fc2886fa0306c3279f1ad44ade0b8c5c094e7f",
                "64bb96aea6a5b42fc0ef65365b7eb2b347de4e9a58167975307173b7bd52a4a8"
        );
        static String expectedRlpEncoded = "0x7801f9015701040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d870186616263646566f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f85994284e47e6130523b2507ba38cea17dd40a20a0cd0f842a0d2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7ca0d2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761f859940000000000000000000000000000000000000003f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a0bf84d5909e08e2e2bb1d5fa975fc2886fa0306c3279f1ad44ade0b8c5c094e7fa064bb96aea6a5b42fc0ef65365b7eb2b347de4e9a58167975307173b7bd52a4a8";


        @BeforeClass
        public static void preSetup() {
            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKey
            );
        }

        @Test
        public void signWithKey_Keyring() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(coupledKeyring, 0);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(coupledKeyring);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(privateKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void throwException_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("TxTypeEthereumAccessList cannot be signed with a decoupled keyring.");

            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            ethereumAccessList.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_notEqualAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setNonce(nonce)
                            .setGas(Numeric.toBigInt(gas))
                            .setGasPrice(Numeric.toBigInt(gasPrice))
                            .setChainId(Numeric.toBigInt(chainID))
                            .setInput(input)
                            .setValue(Numeric.toBigInt(value))
                            .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                            .setTo(to)
                            .setAccessList(accessList)
            );

            ethereumAccessList.sign(coupledKeyring);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver();

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String gas = "0x1e241";
        static String gasPrice = "0x0a";
        static String to = "0x095e7baea6a6c7c4c2dfeb977efac326af552d87";
        static String chainID = "0x1";
        static String input = "0x616263646566";
        static String value = "0x1";
        static String nonce = "0x4";

        static AccessList accessList = new AccessList(Arrays.asList(
                new AccessTuple("0x0000000000000000000000000000000000000001",
                        Arrays.asList(
                                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        )),
                new AccessTuple("0x284e47e6130523b2507ba38cea17dd40a20a0cd0",
                        Arrays.asList(
                                "0xd2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7c",
                                "0xd2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761"
                        )),
                new AccessTuple("0x0000000000000000000000000000000000000003",
                        Arrays.asList(
                                "0x46d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc",
                                "0x6eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681"
                        ))
        ));

        public static EthereumAccessList createEthereumAccessList() {
            return caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                            .setTo(to)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setInput(input)
                            .setChainId(chainID)
                            .setValue(value)
                            .setAccessList(accessList)
            );
        }

        static SignatureData expectedSignature = new SignatureData(
                "0x01",
                "0xbf84d5909e08e2e2bb1d5fa975fc2886fa0306c3279f1ad44ade0b8c5c094e7f",
                "64bb96aea6a5b42fc0ef65365b7eb2b347de4e9a58167975307173b7bd52a4a8"
        );
        static String expectedRlpEncoded = "0x7801f9015701040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d870186616263646566f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f85994284e47e6130523b2507ba38cea17dd40a20a0cd0f842a0d2b691e13d4c3754fbe5dc75439f25dd11a908d89f5bbc55cd5bc4978f078b7ca0d2db659067b2b322f7010149472b81f172b9e331e1831ebee11c7b73facb0761f859940000000000000000000000000000000000000003f842a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fca06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f098368101a0bf84d5909e08e2e2bb1d5fa975fc2886fa0306c3279f1ad44ade0b8c5c094e7fa064bb96aea6a5b42fc0ef65365b7eb2b347de4e9a58167975307173b7bd52a4a8";


        @BeforeClass
        public static void preSetup() {
            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKey
            );
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            AbstractTransaction tx = ethereumAccessList.sign(privateKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_KlaytnWalletKeyFormat() throws IOException {
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.createFromPrivateKey(privateKey).getAddress();
            AbstractTransaction tx = ethereumAccessList.sign(klaytnKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void throwException_KlaytnWalletKeyFormat_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumAccessList + " cannot be signed with a decoupled keyring.");

            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.generate().getAddress();
            ethereumAccessList.sign(klaytnKey);
        }

        @Test
        public void throwException_multipleKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumAccessList + " cannot be signed with a decoupled keyring.");

            String[] privateKeyArr = {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey()
            };

            AbstractKeyring keyring = caver.wallet.keyring.createWithMultipleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKeyArr
            );
            EthereumAccessList ethereumAccessList = createEthereumAccessList();
            ethereumAccessList.sign(keyring);
        }

        @Test
        public void throwException_roleBasedKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumAccessList + " cannot be signed with a decoupled keyring.");

            String[][] privateKeyArr = {
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    }
            };

            EthereumAccessList ethereumAccessList = createEthereumAccessList();

            AbstractKeyring keyring = caver.wallet.keyring.createWithRoleBasedKey(
                    caver.wallet.keyring.generate().getAddress(),
                    Arrays.asList(privateKeyArr)
            );
            ethereumAccessList.sign(keyring);
        }
    }

    public static class recoverPublicKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void recoverPublicKey() {
            String expectedPublicKey = "0x3a514176466fa815ed481ffad09110a2d344f6c9b78c1d14afc351c3a51be33d8072e77939dc03ba44790779b7a1025baf3003f6732430e20cd9b76d953391b3";
            SignatureData signatureData = new SignatureData(
                    "0x1",
                    "0xbfc80a874c43b71b67c68fa5927d1443407f31aef4ec6369bbecdb76fc39b0c0",
                    "0x193e62c1dd63905aee7073958675dcb45d78c716a9a286b54a496e82cb762f26"
            );

            AccessList accessList = new AccessList(
                    Arrays.asList(
                            new AccessTuple(
                                    "0x0000000000000000000000000000000000000001",
                                    Arrays.asList(
                                            "0x0000000000000000000000000000000000000000000000000000000000000000"
                                    )
                            ))
            );

            EthereumAccessList tx = new EthereumAccessList.Builder()
                    .setFrom("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b")
                    .setTo("0x7b65b75d204abed71587c9e519a89277766ee1d0")
                    .setValue("0xa")
                    .setChainId("0x2")
                    .setGasPrice("0x19")
                    .setNonce("0x4d2")
                    .setGas("0xf4240")
                    .setInput("0x31323334")
                    .setAccessList(accessList)
                    .setSignatures(signatureData)
                    .build();

            List<String> publicKeys = tx.recoverPublicKeys();
            assertEquals(expectedPublicKey, publicKeys.get(0));
        }
    }

}
