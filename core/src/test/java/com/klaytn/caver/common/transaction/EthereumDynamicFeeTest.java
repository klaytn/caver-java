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
import com.klaytn.caver.abi.datatypes.Array;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.EthereumAccessList;
import com.klaytn.caver.transaction.type.EthereumDynamicFee;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class EthereumDynamicFeeTest {

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String nonce = "0x4D2";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        String chainID = "0x2710";
        String input = "0x31323334";
        String value = "0x1";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Test
        public void createInstance() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce("0x")
                            .setGas(gas)
                            .setMaxFeePerGas("0x")
                            .setMaxPriorityFeePerGas("0x")
                            .setChainId("0x")
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
                            .setAccessList(accessList)
            );

            assertNotNull(ethereumDynamicFee);

            ethereumDynamicFee.fillTransaction();
            assertNotNull(ethereumDynamicFee.getNonce());
            assertNotNull(ethereumDynamicFee.getMaxPriorityFeePerGas());
            assertNotNull(ethereumDynamicFee.getMaxFeePerGas());
            assertNotNull(ethereumDynamicFee.getChainId());
            assertEquals(accessList, ethereumDynamicFee.getAccessList());
            assertEquals(TransactionType.TxTypeEthereumDynamicFee.toString(), ethereumDynamicFee.getType());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid";

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
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

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        String nonce = "0x4D2";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        String chainID = "0x2710";
        String input = "0x31323334";
        String value = "0x1";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        @Test
        public void BuilderTest() {
            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                    .setMaxFeePerGas(maxFeePerGas)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            assertNotNull(ethereumDynamicFee);
            assertEquals(TransactionType.TxTypeEthereumDynamicFee.toString(), ethereumDynamicFee.getType());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setMaxPriorityFeePerGas(Numeric.toBigInt(maxPriorityFeePerGas))
                    .setMaxFeePerGas(Numeric.toBigInt(maxFeePerGas))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setInput(input)
                    .setValue(Numeric.toBigInt(value))
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            assertEquals(gas, ethereumDynamicFee.getGas());
            assertEquals(maxPriorityFeePerGas, ethereumDynamicFee.getMaxPriorityFeePerGas());
            assertEquals(maxFeePerGas, ethereumDynamicFee.getMaxFeePerGas());
            assertEquals(chainID, ethereumDynamicFee.getChainId());
            assertEquals(value, ethereumDynamicFee.getValue());
            assertEquals(accessList, ethereumDynamicFee.getAccessList());
        }

        @Test
        public void BuilderTestRPC() throws IOException {
            String gas = "0x0f4240";
            String to = "7b65b75d204abed71587c9e519a89277766ee1d0";
            String input = "0x31323334";
            String value = "0x0a";

            Caver caver = new Caver(Caver.DEFAULT_URL);
            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);

            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setInput(input)
                    .setValue(value)
                    .setFrom(keyring.getAddress()) // For Test. It automatically filled when executed EthereumDynamicFee.signWithKey or signWithKeysTest.
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            ethereumDynamicFee.fillTransaction();
            assertNotNull(ethereumDynamicFee.getNonce());
            assertNotNull(ethereumDynamicFee.getMaxPriorityFeePerGas());
            assertNotNull(ethereumDynamicFee.getMaxFeePerGas());
            assertNotNull(ethereumDynamicFee.getChainId());
            assertNotNull(ethereumDynamicFee.getAccessList());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                    .setMaxFeePerGas(maxFeePerGas)
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

            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                    .setMaxFeePerGas(maxFeePerGas)
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

            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                    .setMaxFeePerGas(maxFeePerGas)
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

            EthereumDynamicFee ethereumDynamicFee = new EthereumDynamicFee.Builder()
                    .setNonce(nonce)
                    .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                    .setMaxFeePerGas(maxFeePerGas)
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
            String gas = "0x9c40";
            String maxPriorityFeePerGas = "0x5d21dba00";
            String maxFeePerGas = "0x5d21dba00";
            String chainID = "0x2710";
            String value = "0x1";
            AccessList accessList = new AccessList(
                    Arrays.asList(
                            new AccessTuple(
                                    "0x67116062f1626f7b3019631f03d301b8f701f709",
                                    Arrays.asList(
                                            "0x0000000000000000000000000000000000000000000000000000000000000003",
                                            "0x0000000000000000000000000000000000000000000000000000000000000007"
                                    )
                            ))
            );
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0"),
                    Numeric.hexStringToByteArray("0x4fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040"),
                    Numeric.hexStringToByteArray("0x7d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc")
            );

            // 0 y-parity
            String expectedRLP = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000780a04fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040a07d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc";
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce("0x25")
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo("0x1fc92c23f71a7de4cdb4394a37fc636986a0f484")
                            .setValue(value)
                            .setInput("0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039")
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            assertEquals(expectedRLP, ethereumDynamicFee.getRLPEncoding());
            // decoding
            EthereumDynamicFee decodedEthereumDynamicFee = (EthereumDynamicFee) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumDynamicFee.compareTxField(decodedEthereumDynamicFee, true));

            // 1 y-parity
            expectedRLP = "0x7802f9010f822710288505d21dba008505d21dba00829c40947988508e9236a5b796ddbb6ac40864777a414f5f01b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf85994d9d6bd9e2186233d9441bde052504b926f2e0bb2f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000701a054d6ea6f359a7d3546199ac93dca216918b45647a45b6f32be58f33735a696b7a07179ffc15f5c6b4b08efc4f7306548c435529edc2e5b8243d1193f52085dbc65";
            ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce("0x28")
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo("0x7988508e9236a5b796ddbb6ac40864777a414f5f")
                            .setValue(value)
                            .setInput("0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039")
                            .setAccessList(caver.transaction.utils.accessList.create(
                                    Arrays.asList(
                                            caver.transaction.utils.accessTuple.create(
                                                    "0xd9d6bd9e2186233d9441bde052504b926f2e0bb2",
                                                    Arrays.asList(
                                                            "0x0000000000000000000000000000000000000000000000000000000000000003",
                                                            "0x0000000000000000000000000000000000000000000000000000000000000007"
                                                    )
                                            )
                                    )
                            ))
                            .setSignatures(new SignatureData(
                                    "0x01",
                                    "0x54d6ea6f359a7d3546199ac93dca216918b45647a45b6f32be58f33735a696b7",
                                    "0x7179ffc15f5c6b4b08efc4f7306548c435529edc2e5b8243d1193f52085dbc65")
                            )
            );
            assertEquals(expectedRLP, ethereumDynamicFee.getRLPEncoding());
            // decoding
            decodedEthereumDynamicFee = (EthereumDynamicFee) caver.transaction.decode(expectedRLP);
            assertTrue(ethereumDynamicFee.compareTxField(decodedEthereumDynamicFee, true));
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String gas = "0xf4240";
            String maxPriorityFeePerGas = "0x5d21dba00";
            String maxFeePerGas = "0x5d21dba00";
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumDynamicFee.getRLPEncoding();
        }

        @Test
        public void throwException_NoMaxPriorityFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxPriorityFeePerGas is undefined. Define maxPriorityFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String maxPriorityFeePerGas = "0x5d21dba00";
            String maxFeePerGas = "0x5d21dba00";
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumDynamicFee.getRLPEncoding();
        }

        @Test
        public void throwException_NoMaxFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxFeePerGas is undefined. Define maxFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String maxPriorityFeePerGas = "0x5d21dba00";
            String maxFeePerGas = "0x5d21dba00";
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumDynamicFee.getRLPEncoding();
        }

        @Test
        public void throwException_NoChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String maxPriorityFeePerGas = "0x5d21dba00";
            String maxFeePerGas = "0x5d21dba00";
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
                            .setAccessList(null)
            );

            ethereumDynamicFee.getRLPEncoding();
        }

    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

        String nonce = "0x25";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String chainID = "0x2710";
        String value = "0x1";
        String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0"),
                Numeric.hexStringToByteArray("0x4fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040"),
                Numeric.hexStringToByteArray("0x7d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc")
        );

        @Test
        public void getTransactionHash() {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            String expected = "0xf3a22f2ce973c9aa250d0dc64044e3dec9592b787f5abe3f557f3e81b99e3ca1";
            String txHash = ethereumDynamicFee.getTransactionHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumDynamicFee.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_MaxPriorityFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxPriorityFeePerGas is undefined. Define maxPriorityFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_MaxFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxFeePerGas is undefined. Define maxFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getRawTransaction();
        }


        @Test
        public void throwException_NotDefined_ChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getTransactionHash();
        }
    }


    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

        String nonce = "0x25";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String chainID = "0x2710";
        String value = "0x1";
        String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0"),
                Numeric.hexStringToByteArray("0x4fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040"),
                Numeric.hexStringToByteArray("0x7d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc")
        );

        @Test
        public void getSenderTxHash() {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            String expected = "0xf3a22f2ce973c9aa250d0dc64044e3dec9592b787f5abe3f557f3e81b99e3ca1";
            String txHash = ethereumDynamicFee.getSenderTxHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumDynamicFee.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_MaxPriorityFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxPriorityFeePerGas is undefined. Define maxPriorityFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_MaxFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxFeePerGas is undefined. Define maxFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setNonce(nonce)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getSenderTxHash();
        }


        @Test
        public void throwException_NotDefined_ChainId() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setNonce(nonce)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );
            ethereumDynamicFee.getSenderTxHash();
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        String nonce = "0x25";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String chainID = "0x2710";
        String value = "0x1";
        String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0"),
                Numeric.hexStringToByteArray("0x4fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040"),
                Numeric.hexStringToByteArray("0x7d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc")
        );
        @Test
        public void combineSignature() {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            // rlpEncoded is a rlp encoded EthereumDynamicFee transaction containing signatureData defined above.
            String rlpEncoded = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000780a04fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040a07d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = ethereumDynamicFee.combineSignedRawTransactions(rlpList);
            assertEquals(rlpEncoded, combined);
            EthereumDynamicFee decoded = (EthereumDynamicFee) caver.transaction.decode(combined);
            Assert.assertTrue(decoded.getSignatures().equals(Arrays.asList(signatureData)));
        }

        @Test
        public void combineSignature_EmptySig() {
            SignatureData emptySig = SignatureData.getEmptySignature();

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(emptySig)
            );

            // rlpEncoded is a rlp encoded EthereumDynamicFee transaction containing signatureData defined above.
            String rlpEncoded = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000780a04fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040a07d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = ethereumDynamicFee.combineSignedRawTransactions(rlpList);

            assertEquals(rlpEncoded, combined);
            EthereumDynamicFee decoded = (EthereumDynamicFee) caver.transaction.decode(combined);
            Assert.assertTrue(decoded.getSignatures().equals(Arrays.asList(signatureData)));
        }

        @Test
        public void throwException_existSignature() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumDynamicFee.toString() + " cannot include more than one signature.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(new SignatureData(
                                    Numeric.hexStringToByteArray("0x01"),
                                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
                            ))
            );

            // rlpEncoded is a rlp encoded EthereumDynamicFee transaction containing signatureData defined above.
            String rlpEncoded = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000780a04fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040a07d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            ethereumDynamicFee.combineSignedRawTransactions(rlpList);
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            // rlpEncoded is a rlp encoded string of EthereumDynamicFee transaction containing above fields.
            String rlpEncoded = "0x7801f8df01040a8301e24194095e7baea6a6c7c4c2dfeb977efac326af552d878080f87cf87a94284e47e6130523b2507ba38cea17dd40a20a0cd0f863a00000000000000000000000000000000000000000000000000000000000000000a06eab5ba2ea17e1ef4eac404d25f1fe9224421e3b639aec73d3b99c39f0983681a046d62a62fb985e2e7691a9044b8fae9149311c7f3dcf669265fe5c96072ba4fc01a019676433856a1bd3650e22c210e20c7efdedf1d4f555f1ab6eb7845024f52d99a070feddce085399eb085b55254fbc8bb5bf912464316b20c3be39bca9015da235";

            // All fields are same with above rlpEncoded EthereumDynamicFee transaction except maxFeePerGas field.
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas("0x1")
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );


            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            ethereumDynamicFee.combineSignedRawTransactions(rlpList);
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        String nonce = "0x25";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String chainID = "0x2710";
        String value = "0x1";
        String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0"),
                Numeric.hexStringToByteArray("0x4fc52da183020a27dc4b684a45404445630e946b0c1a37edeb538d4bdae63040"),
                Numeric.hexStringToByteArray("0x7d56dbcc61f42ffcbced105f838d20b8fe71e85a4d0344c7f60815fddfeae4cc")
        );

        @Test
        public void appendSignature() {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            ethereumDynamicFee.appendSignatures(signatureData);

            assertEquals(signatureData, ethereumDynamicFee.getSignatures().get(0));
        }

        @Test
        public void throwException_invalidParity() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid signature: The y-parity of the transaction should either be 0 or 1.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            ethereumDynamicFee.appendSignatures(new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            ));
        }


        @Test
        public void throwException_setSignature_invalidParity() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid signature: The y-parity of the transaction should either be 0 or 1.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
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

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(emptySignature)
            );

            ethereumDynamicFee.appendSignatures(signatureData);

            assertEquals(signatureData, ethereumDynamicFee.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            ethereumDynamicFee.appendSignatures(list);

            assertEquals(signatureData, ethereumDynamicFee.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            SignatureData emptySignature = SignatureData.getEmptySignature();

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(emptySignature)
            );

            ethereumDynamicFee.appendSignatures(list);

            assertEquals(signatureData, ethereumDynamicFee.getSignatures().get(0));
        }

        @Test
        public void throwException_appendData_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumDynamicFee.toString() + " cannot include more than one signature.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(signatureData)
            );

            ethereumDynamicFee.appendSignatures(signatureData);
        }

        @Test
        public void throwException_appendList_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeEthereumDynamicFee.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
                            .setSignatures(list)
            );

            ethereumDynamicFee.appendSignatures(list);
        }

        @Test
        public void throwException_tooLongSignatures() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures are too long " + TransactionType.TxTypeEthereumDynamicFee.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);
            list.add(signatureData);

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            ethereumDynamicFee.appendSignatures(list);
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        String nonce = "0x25";
        String gas = "0x9c40";
        String maxPriorityFeePerGas = "0x5d21dba00";
        String maxFeePerGas = "0x5d21dba00";
        String chainID = "0x2710";
        String value = "0x1";
        String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        @Test
        public void getRLPEncodingForSignature() {
            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            String expected = "0x02f8cc822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000007";
            String rlpEncodedSign = ethereumDynamicFee.getRLPEncodingForSignature();

            assertEquals(expected, rlpEncodedSign);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );

            String encoded = ethereumDynamicFee.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_MaxPriorityFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxPriorityFeePerGas is undefined. Define maxPriorityFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );
            String encoded = ethereumDynamicFee.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_MaxFeePerGas() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("maxFeePerGas is undefined. Define maxFeePerGas in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );
            String encoded = ethereumDynamicFee.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );
            String encoded = ethereumDynamicFee.getRLPEncodingForSignature();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver();

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String nonce = "0x25";
        static String gas = "0x9c40";
        static String maxPriorityFeePerGas = "0x5d21dba00";
        static String maxFeePerGas = "0x5d21dba00";
        static String chainID = "0x2710";
        static String value = "0x1";
        static String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        static String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        static AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        public static EthereumDynamicFee createEthereumDynamicFee() {
            return caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );
        }

        static SignatureData expectedSignature = new SignatureData(
                "0x01",
                "0x2e07db45b088d6a2cabce6c250b94252dd505789a3912e4fe08a2566973b208f",
                "0x76cbcc2f02063ee6c79ffea7f2078267405c3819e8ac4db0c504223d55892ba4"
        );
        static String expectedRlpEncoded = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000701a02e07db45b088d6a2cabce6c250b94252dd505789a3912e4fe08a2566973b208fa076cbcc2f02063ee6c79ffea7f2078267405c3819e8ac4db0c504223d55892ba4";


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
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(coupledKeyring, 0);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(coupledKeyring);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(privateKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void throwException_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("TxTypeEthereumDynamicFee cannot be signed with a decoupled keyring.");

            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            ethereumDynamicFee.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_notEqualAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(Numeric.toBigInt(gas))
                            .setMaxPriorityFeePerGas(Numeric.toBigInt(maxPriorityFeePerGas))
                            .setMaxFeePerGas(Numeric.toBigInt(maxFeePerGas))
                            .setChainId(Numeric.toBigInt(chainID))
                            .setInput(input)
                            .setValue(Numeric.toBigInt(value))
                            .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                            .setTo(to)
                            .setAccessList(accessList)
            );

            ethereumDynamicFee.sign(coupledKeyring);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();


        static Caver caver = new Caver();

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String nonce = "0x25";
        static String gas = "0x9c40";
        static String maxPriorityFeePerGas = "0x5d21dba00";
        static String maxFeePerGas = "0x5d21dba00";
        static String chainID = "0x2710";
        static String value = "0x1";
        static String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        static String to = "0x1fc92c23f71a7de4cdb4394a37fc636986a0f484";
        static AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        public static EthereumDynamicFee createEthereumDynamicFee() {
            return caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setMaxPriorityFeePerGas(maxPriorityFeePerGas)
                            .setMaxFeePerGas(maxFeePerGas)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setInput(input)
                            .setAccessList(accessList)
            );
        }

        static SignatureData expectedSignature = new SignatureData(
                "0x01",
                "0x2e07db45b088d6a2cabce6c250b94252dd505789a3912e4fe08a2566973b208f",
                "0x76cbcc2f02063ee6c79ffea7f2078267405c3819e8ac4db0c504223d55892ba4"
        );
        static String expectedRlpEncoded = "0x7802f9010f822710258505d21dba008505d21dba00829c40941fc92c23f71a7de4cdb4394a37fc636986a0f48401b844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f85bf8599467116062f1626f7b3019631f03d301b8f701f709f842a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000701a02e07db45b088d6a2cabce6c250b94252dd505789a3912e4fe08a2566973b208fa076cbcc2f02063ee6c79ffea7f2078267405c3819e8ac4db0c504223d55892ba4";

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
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            AbstractTransaction tx = ethereumDynamicFee.sign(privateKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_KlaytnWalletKeyFormat() throws IOException {
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.createFromPrivateKey(privateKey).getAddress();
            AbstractTransaction tx = ethereumDynamicFee.sign(klaytnKey, TransactionHasher::getHashForSignature);
            Assert.assertEquals(expectedSignature, tx.getSignatures().get(0));
            Assert.assertEquals(expectedRlpEncoded, tx.getRawTransaction());
        }

        @Test
        public void throwException_KlaytnWalletKeyFormat_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumDynamicFee + " cannot be signed with a decoupled keyring.");

            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.generate().getAddress();
            ethereumDynamicFee.sign(klaytnKey);
        }

        @Test
        public void throwException_multipleKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumDynamicFee + " cannot be signed with a decoupled keyring.");

            String[] privateKeyArr = {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey()
            };

            AbstractKeyring keyring = caver.wallet.keyring.createWithMultipleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKeyArr
            );
            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();
            ethereumDynamicFee.sign(keyring);
        }

        @Test
        public void throwException_roleBasedKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage(TransactionType.TxTypeEthereumDynamicFee + " cannot be signed with a decoupled keyring.");

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

            EthereumDynamicFee ethereumDynamicFee = createEthereumDynamicFee();

            AbstractKeyring keyring = caver.wallet.keyring.createWithRoleBasedKey(
                    caver.wallet.keyring.generate().getAddress(),
                    Arrays.asList(privateKeyArr)
            );
            ethereumDynamicFee.sign(keyring);
        }
    }

    public static class recoverPublicKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void recoverPublicKey() {

            String expectedPublicKey = "0x3a514176466fa815ed481ffad09110a2d344f6c9b78c1d14afc351c3a51be33d8072e77939dc03ba44790779b7a1025baf3003f6732430e20cd9b76d953391b3";
            SignatureData signatureData = new SignatureData(
                    "0x01",
                    "0x2e07db45b088d6a2cabce6c250b94252dd505789a3912e4fe08a2566973b208f",
                    "0x76cbcc2f02063ee6c79ffea7f2078267405c3819e8ac4db0c504223d55892ba4"
            );

            AccessList accessList = new AccessList(
                    Arrays.asList(
                            new AccessTuple(
                                    "0x67116062f1626f7b3019631f03d301b8f701f709",
                                    Arrays.asList(
                                            "0x0000000000000000000000000000000000000000000000000000000000000003",
                                            "0x0000000000000000000000000000000000000000000000000000000000000007"
                                    )
                            ))
            );

            EthereumDynamicFee tx = new EthereumDynamicFee.Builder()
                    .setFrom("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b")
                    .setTo("0x1fc92c23f71a7de4cdb4394a37fc636986a0f484")
                    .setValue("0x1")
                    .setChainId("0x2710")
                    .setMaxPriorityFeePerGas("0x5d21dba00")
                    .setMaxFeePerGas("0x5d21dba00")
                    .setNonce("0x25")
                    .setGas("0x9c40")
                    .setInput("0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039")
                    .setAccessList(accessList)
                    .setSignatures(signatureData)
                    .build();

            List<String> publicKeys = tx.recoverPublicKeys();
            assertEquals(expectedPublicKey, publicKeys.get(0));
        }
    }
}
