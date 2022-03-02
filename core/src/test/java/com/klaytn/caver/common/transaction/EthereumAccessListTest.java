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
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import com.klaytn.caver.transaction.type.EthereumAccessList;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.Rule;
import org.junit.Test;
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
            String expectedRLP ="0x7801f88701040a8301e241808080f838f7940000000000000000000000000000000000000001e1a0000000000000000000000000000000000000000000000000000000000000000001a05d2368dff6ab943d3467558de70805d5b6a4367ab94b1ee8a7ae53e4e0f68293a01d68f38dce681c32cd001ca155b2b27e26ddd788b4bdaaf355181c626805ec7f";
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
            EthereumAccessList decodedEthereumAccessList = (EthereumAccessList) TransactionDecoder.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // Test-2: encoding EthereumAccessList which have many storageKeys
            expectedRLP = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc01a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";
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
            decodedEthereumAccessList = (EthereumAccessList) TransactionDecoder.decode(expectedRLP);
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
            decodedEthereumAccessList = (EthereumAccessList) TransactionDecoder.decode(expectedRLP);
            assertTrue(ethereumAccessList.compareTxField(decodedEthereumAccessList, true));

            // Test-4: encoding EthereumAccessList which have many accessList
            expectedRLP = "0x7801f9013d01040a8301e241808080f8eef7940000000000000000000000000000000000000001e1a00000000000000000000000000000000000000000000000000000000000000000f859940000000000000000000000000000000000000002f842a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fcf859940000000000000000000000000000000000000003f842a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc01a05d6d9bc7bb01b05db25f5f2e4a995a4970124387293694f0fd8bdda95bc6e7f4a00782feaf0460341b320710ef7a4f07167d551fd897775af5ec2f1dea095e99cb";
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
            decodedEthereumAccessList = (EthereumAccessList) TransactionDecoder.decode(expectedRLP);
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
                    Numeric.hexStringToByteArray("0x25"),
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
                    Numeric.hexStringToByteArray("0x25"),
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
                    Numeric.hexStringToByteArray("0x25"),
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

}
