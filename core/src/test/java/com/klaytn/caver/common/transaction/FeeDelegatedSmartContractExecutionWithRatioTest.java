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

package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecutionWithRatio;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
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
public class FeeDelegatedSmartContractExecutionWithRatioTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";

    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

    static String nonce = "0x4d2";
    static String gasPrice = "0x19";
    static String gas = "0xf4240";
    static String chainID = "0x1";
    static String value = "0xa";
    static BigInteger feeRatio = BigInteger.valueOf(30);

    static String input = "0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2";

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0x74ccfee18dc28932396b85617c53784ee366303bce39a2401d8eb602cf73766f",
            "0x4c937a5ab9401d2cacb3f39ba8c29dbcd44588cc5c7d0b6b4113cfa7b7d9427b"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x25",
            "0x4a4997524694d535976d7343c1e3a260f99ba53fcb5477e2b96216ec96ebb565",
            "0xf8cb31a35399d2b0fbbfa39f259c819a15370706c0449952c7cfc682d200d7c"
    );

    static String expectedRLPEncoding = "0x32f8fc8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b21ef845f84326a074ccfee18dc28932396b85617c53784ee366303bce39a2401d8eb602cf73766fa04c937a5ab9401d2cacb3f39ba8c29dbcd44588cc5c7d0b6b4113cfa7b7d9427b945a0043070275d9f6054307ee7348bd660849d90ff845f84325a04a4997524694d535976d7343c1e3a260f99ba53fcb5477e2b96216ec96ebb565a00f8cb31a35399d2b0fbbfa39f259c819a15370706c0449952c7cfc682d200d7c";
    static String expectedTransactionHash = "0xb204e530f2a7f010d65b6f0f7639d1e9fc8add73e3a0ff1551b11585c36d3bdb";
    static String expectedSenderTransactionHash = "0xd5e22319cbf020d422d8ba3a07da9d99b9300826637af85b4e061805dcb2c1b0";
    static String expectedRLPEncodingForFeePayerSigning = "0xf876b85cf85a328204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b21e945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), txObj.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            String nonce = null;
            String gasPrice = null;
            String chainId = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainId)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setValue(Numeric.toBigInt(value))
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
            assertEquals(value, txObj.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            BigInteger feeRatio = BigInteger.valueOf(101);

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String feeRatio = "0x1E";

        @Test
        public void createInstance() {
            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), txObj.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            String feeRatio = "0xFF";

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio txObj;
        SingleKeyring keyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            klaytnWalletKey = keyring.getKlaytnWalletKey();

            txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String feePayerPrivateKey = caver.wallet.keyring.generateSingleKey();
            String feePayer = caver.wallet.keyring.createFromPrivateKey(feePayerPrivateKey).getAddress();

            txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

            txObj.signAsFeePayer(feePayerPrivateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_multipleKey() throws IOException {
            String[] keyArr = {
                    caver.wallet.keyring.generateSingleKey(),
                    feePayerPrivateKey,
                    caver.wallet.keyring.generateSingleKey()
            };

            MultipleKeyring keyring = caver.wallet.keyring.createWithMultipleKey(feePayer, keyArr);
            txObj.signAsFeePayer(keyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_roleBasedKey() throws IOException {
            String[][] keyArr = {
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey(),
                    },
                    {
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            feePayerPrivateKey
                    }
            };

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(feePayer, Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = caver.wallet.keyring.generate();
            txObj.signAsFeePayer(keyring, 0);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3, 3, 3}, feePayer);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

            singleKeyring = caver.wallet.keyring.createWithSingleKey(
                    feePayer,
                    caver.wallet.keyring.generateSingleKey()
            );
            multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                    feePayer,
                    caver.wallet.keyring.generateMultipleKeys(8)
            );
            roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                    feePayer,
                    caver.wallet.keyring.generateRoleBasedKeys(new int[]{3, 4, 5})
            );
        }

        @Test
        public void signWithKeys_singleKeyring() throws IOException {
            mTxObj.signAsFeePayer(singleKeyring, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, mTxObj.getSignatures().size());
        }

        @Test
        public void signWithKeys_singleKeyring_NoSigner() throws IOException {
            mTxObj.signAsFeePayer(singleKeyring);
            assertEquals(1, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void signWithKeys_multipleKeyring() throws IOException {
            mTxObj.signAsFeePayer(multipleKeyring);
            assertEquals(8, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            mTxObj.signAsFeePayer(roleBasedKeyring);
            assertEquals(5, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = caver.wallet.keyring.generate();
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();
        }

        @Test
        public void appendFeePayerSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendFeePayerSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignatureList() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(emptySignature)
                    .build();

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(signatureData)
                    .build();

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(2, mTxObj.getFeePayerSignatures().size());
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getFeePayerSignatures().get(1));
        }

        @Test
        public void appendFeePayerSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(signatureData)
                    .build();

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            SignatureData signatureData2 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x9a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0xa3ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);
            list.add(signatureData2);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(3, mTxObj.getFeePayerSignatures().size());
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getFeePayerSignatures().get(1));
            assertEquals(signatureData2, mTxObj.getFeePayerSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xe862a5ddac7f82f57eaea34f3f915121a6da1bb2";
        String to = "0xf14274fd5f22f436e3a2d3f3b167f9f241c33db5";
        String value = "0x0";
        String input = "0xa9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be400";
        String gas = "0x30d40";
        String nonce = "0x3";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        BigInteger feeRatio = BigInteger.valueOf(30);

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x32f8e0038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef847f845820fe9a0b95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5ba0105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f940000000000000000000000000000000000000000c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0xb95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5b",
                    "0x105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData senderSignature = new SignatureData(
                    "0x0fe9",
                    "0xb95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5b",
                    "0x105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f"
            );

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignature)
                    .build();

            String[] rlpEncodedStrings = {
                    "0x32f8cc038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef847f845820fe9a058cf881d440cd88e2a1d0999b4b0eec72b36f7c13a793fcba7d509c544c06505a025bdcc5b6f7619169397508d38da290faa54b01c83c582d1dfa0ba250b7a187180c4c3018080",
                    "0x32f8cc038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef847f845820fe9a0fa309605c494a338e4cd92c7bedeafa25387f57e0b5f6e18f9d8da90edea9e44a055d173d614d096f23eb9a01fd894f961d266985df6503d5176d047eb3b3ef5ed80c4c3018080",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x32f9016e038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef8d5f845820fe9a0b95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5ba0105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8ff845820fe9a058cf881d440cd88e2a1d0999b4b0eec72b36f7c13a793fcba7d509c544c06505a025bdcc5b6f7619169397508d38da290faa54b01c83c582d1dfa0ba250b7a1871f845820fe9a0fa309605c494a338e4cd92c7bedeafa25387f57e0b5f6e18f9d8da90edea9e44a055d173d614d096f23eb9a01fd894f961d266985df6503d5176d047eb3b3ef5ed940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xb95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5b",
                            "0x105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x58cf881d440cd88e2a1d0999b4b0eec72b36f7c13a793fcba7d509c544c06505",
                            "0x25bdcc5b6f7619169397508d38da290faa54b01c83c582d1dfa0ba250b7a1871"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xfa309605c494a338e4cd92c7bedeafa25387f57e0b5f6e18f9d8da90edea9e44",
                            "0x55d173d614d096f23eb9a01fd894f961d266985df6503d5176d047eb3b3ef5ed"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            String feePayer = "0xad3bd7a7df94367e8b0443dd10e86330750ebf0c";

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x32f8e0038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ec4c301808094ad3bd7a7df94367e8b0443dd10e86330750ebf0cf847f845820fe9a0c7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31a01bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebb";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0xc7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31",
                    "0x1bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebb"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0xad3bd7a7df94367e8b0443dd10e86330750ebf0c";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fe9",
                    "0xc7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31",
                    "0x1bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebb"
            );

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();


            String[] rlpEncodedStrings = new String[]{
                    "0x32f8e0038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ec4c301808094ad3bd7a7df94367e8b0443dd10e86330750ebf0cf847f845820feaa09d6fb034ed27fa0baf8ba2650b48e087d261ab7716eae4df9299236ddce7dd08a053b1c7ab56349cbb5515e27737846f97862e3f20409b183c3c6b4a918cd20920",
                    "0x32f8e0038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ec4c301808094ad3bd7a7df94367e8b0443dd10e86330750ebf0cf847f845820fe9a019315d03a16242c6d754bd006883376e211b6f8af486d1b41a0705878e3bb100a06d463477534b9c5e82196cb8c8982bc0e3c9120b14c2db3df0f4d1c9dc04c657",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x32f9016e038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ec4c301808094ad3bd7a7df94367e8b0443dd10e86330750ebf0cf8d5f845820fe9a0c7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31a01bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebbf845820feaa09d6fb034ed27fa0baf8ba2650b48e087d261ab7716eae4df9299236ddce7dd08a053b1c7ab56349cbb5515e27737846f97862e3f20409b183c3c6b4a918cd20920f845820fe9a019315d03a16242c6d754bd006883376e211b6f8af486d1b41a0705878e3bb100a06d463477534b9c5e82196cb8c8982bc0e3c9120b14c2db3df0f4d1c9dc04c657";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xc7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31",
                            "0x1bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebb"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x9d6fb034ed27fa0baf8ba2650b48e087d261ab7716eae4df9299236ddce7dd08",
                            "0x53b1c7ab56349cbb5515e27737846f97862e3f20409b183c3c6b4a918cd20920"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x19315d03a16242c6d754bd006883376e211b6f8af486d1b41a0705878e3bb100",
                            "0x6d463477534b9c5e82196cb8c8982bc0e3c9120b14c2db3df0f4d1c9dc04c657"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeeRatio(feeRatio)
                    .build();


            String rlpEncodedString = "0x32f9015a038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef8d5f845820fe9a0b95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5ba0105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8ff845820fe9a058cf881d440cd88e2a1d0999b4b0eec72b36f7c13a793fcba7d509c544c06505a025bdcc5b6f7619169397508d38da290faa54b01c83c582d1dfa0ba250b7a1871f845820fe9a0fa309605c494a338e4cd92c7bedeafa25387f57e0b5f6e18f9d8da90edea9e44a055d173d614d096f23eb9a01fd894f961d266985df6503d5176d047eb3b3ef5ed80c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xb95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5b",
                            "0x105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x58cf881d440cd88e2a1d0999b4b0eec72b36f7c13a793fcba7d509c544c06505",
                            "0x25bdcc5b6f7619169397508d38da290faa54b01c83c582d1dfa0ba250b7a1871"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xfa309605c494a338e4cd92c7bedeafa25387f57e0b5f6e18f9d8da90edea9e44",
                            "0x55d173d614d096f23eb9a01fd894f961d266985df6503d5176d047eb3b3ef5ed"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x32f9016e038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ec4c301808094ad3bd7a7df94367e8b0443dd10e86330750ebf0cf8d5f845820fe9a0c7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31a01bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebbf845820feaa09d6fb034ed27fa0baf8ba2650b48e087d261ab7716eae4df9299236ddce7dd08a053b1c7ab56349cbb5515e27737846f97862e3f20409b183c3c6b4a918cd20920f845820fe9a019315d03a16242c6d754bd006883376e211b6f8af486d1b41a0705878e3bb100a06d463477534b9c5e82196cb8c8982bc0e3c9120b14c2db3df0f4d1c9dc04c657";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xc7a060a2e28476e4567bc76964f826153149a07c061e389b51f34f3863f65a31",
                            "0x1bfd20aca5b410ca369113150c16af4d9f9c72907aaaf34896427ef1f1a51ebb"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x9d6fb034ed27fa0baf8ba2650b48e087d261ab7716eae4df9299236ddce7dd08",
                            "0x53b1c7ab56349cbb5515e27737846f97862e3f20409b183c3c6b4a918cd20920"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x19315d03a16242c6d754bd006883376e211b6f8af486d1b41a0705878e3bb100",
                            "0x6d463477534b9c5e82196cb8c8982bc0e3c9120b14c2db3df0f4d1c9dc04c657"
                    ),
            };

            combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStringsWithFeePayerSignatures));

            assertEquals(expectedSignatures[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatures[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatures[2], mTxObj.getSignatures().get(2));

            assertEquals(expectedFeePayerSignatures[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedFeePayerSignatures[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedFeePayerSignatures[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            String gas = "0x1000";

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x32f8cc038505d21dba0083030d4094f14274fd5f22f436e3a2d3f3b167f9f241c33db58094e862a5ddac7f82f57eaea34f3f915121a6da1bb2b844a9059cbb000000000000000000000000ad3bd7a7df94367e8b0443dd10e86330750ebf0c00000000000000000000000000000000000000000000000000000002540be4001ef847f845820fe9a0b95ed5ff6d9cd8d02e3031ea4ddf38d42803817b5ecc086828f497787699bf5ba0105105455d4af28cc943e43e375316b57205e6eb664407b3bc1a7eca9ecd6c8f80c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedSmartContractExecutionWithRatio txObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedTransactionHash, mTxObj.getTransactionHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedSenderTransactionHash, mTxObj.getSenderTxHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecutionWithRatio mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncodingForFeePayerSigning, mTxObj.getRLPEncodingForFeePayerSignature());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }

    public static class recoverPublicKeyTest {
        List<String> expectedPublicKeyList = Arrays.asList(
                "0xfbda4ac2c04336609f7e5a363c71c1565b442d552b82cbd0e75bbabaf215fd28b69ce88a6b9f2a463f1420bd9a0992413254748a7ab46d5ba78d09b35cf0e912",
                "0xa234bd09ea829cb39dd2f5aced2318039f30ce5fe28f5eb28a256bac8617eb5db57ac7683fa21a01c8cbd2ca31c2cf93c97871c73896bf051f9bc0885c87ebe2",
                "0x6ed39def6b25fc001790d267922281483c372b5d2486ae955ece1f1b64b19aea85392c8555947a1c63577439afdb74c77ef07d50520435d31cf4afb3dfe0074f"
        );

        List<String> expectedFeePayerPublicKeyList = Arrays.asList(
                "0x2b557d80ddac3a0bbcc8a7861773ca7434c969e2721a574bb94a1e3aa5ceed3819f08a82b31682c038f9f691fb38ee4aaf7e016e2c973a1bd1e48a51f60a54ea",
                "0x1a1cfe1e2ec4b15520c57c20c2460981a2f16003c8db11a0afc282abf929fa1c1868f60f91b330c423aa660913d86acc2a0b1b15e7ba1fe571e5928a19825a7e",
                "0xdea23a89dbbde1a0c26466c49c1edd32785432389641797038c2b53815cb5c73d6cf5355986fd9a22a68bb57b831857fd1636362b383bd632966392714b60d72"
        );

        List<SignatureData> expectedSigData = Arrays.asList(
                new SignatureData(
                        "0x0fea",
                        "0xec91b2f010d2a67c553924a5da051c22b1fde3271ef5ca0caaef88e84a687346",
                        "0x4bdc835e82717a34354b7cb445d679f9fcfa4bfd5cb3e201c74aa1cfa1a0c264"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x88bff49b9b12dc37f2a9665dc9bf88bf433e3e66b8772006f0b21a6acb619511",
                        "0x1e56201dc2e43d108e7d568b1596d762f68296a47067d6da59f55b88a4c341d1"
                ),
                new SignatureData(
                        "0x0fe9",
                        "0xda52e3241059667a2dcb50a541b88b46cc1a8d7a5c5cc1047453815b03d9d34c",
                        "0x17dc3367702f6e20092e8b1893cf0fdec60e8dde82891e9b278607c97ca25a4a"
                )
        );

        List<SignatureData> expectedFeePayerSigData = Arrays.asList(
                new SignatureData(
                        "0x0fe9",
                        "0xcb516e7ab54063b6096bc7299f24731f08a3589576efe2a0bb590a0f22439db2",
                        "0x1b930b0ae22db6c66f1584b083707198ab7736edafd59a80064a4e1d2f18823e"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x2b345d208f3087287f35204f6fc1a68e6086a3a360f99fc6cc869095ad7770ef",
                        "0x4407f06020e6f26978c0fb57dbf4d45f6535ee7218835fcc8a03003fb4309944"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x8baf44a961ca99e953f27cc5c865e492f8db46b0b9a58a0ad1418efeb01bb217",
                        "0x4d3c1f7a43017f33a70541ab4f7d962da1c3246a88280cf53d0cb1c74b3a7100"
                )
        );

        FeeDelegatedSmartContractExecutionWithRatio tx = new FeeDelegatedSmartContractExecutionWithRatio.Builder()
                .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                .setFeeRatio("0x63")
                .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                .setValue("0x1")
                .setChainId("0x7e3")
                .setGasPrice("0x5d21dba00")
                .setNonce("0x0")
                .setGas("0x2faf080")
                .setInput("0xd95aced7000000000000000000000000640a4c021cb5889fa1d37378f04a36ad452862240000000000000000000000000000000000000000000000000000000000000001")
                .setSignatures(expectedSigData)
                .setFeePayerSignatures(expectedFeePayerSigData)
                .build();

        @Test
        public void recoverPublicKey() {
            List<String> publicKeys = tx.recoverPublicKeys();
            assertEquals(expectedPublicKeyList, publicKeys);
        }

        @Test
        public void recoverFeePayerPublicKey() {
            List<String> publicKeys = tx.recoverFeePayerPublicKeys();
            assertEquals(expectedFeePayerPublicKeyList, publicKeys);
        }
    }
}
