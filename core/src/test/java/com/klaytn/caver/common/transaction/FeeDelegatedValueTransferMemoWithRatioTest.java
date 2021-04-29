package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferMemoWithRatio;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FeeDelegatedValueTransferMemoWithRatioTest {

    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";
    static String value = "0xa";
    static String input = "0x68656c6c6f";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
    static BigInteger feeRatio = BigInteger.valueOf(30);

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0x769f0afdc310289f9b24decb5bb765c8d7a87a6a4ae28edffb8b7085bbd9bc78",
            "0x6a7b970eea026e60ac29bb52aee10661a4222e6bdcdfb3839a80586e584586b4"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x25",
            "0xc1c54bdc72ce7c08821329bf50542535fac74f4bba5de5b7881118a461d52834",
            "0x3a3a64878d784f9af91c2e3ab9c90f17144c47cfd9951e3588c75063c0649ecd"
    );

    static String expectedRLPEncoding = "0x12f8dd8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6f1ef845f84326a0769f0afdc310289f9b24decb5bb765c8d7a87a6a4ae28edffb8b7085bbd9bc78a06a7b970eea026e60ac29bb52aee10661a4222e6bdcdfb3839a80586e584586b4945a0043070275d9f6054307ee7348bd660849d90ff845f84325a0c1c54bdc72ce7c08821329bf50542535fac74f4bba5de5b7881118a461d52834a03a3a64878d784f9af91c2e3ab9c90f17144c47cfd9951e3588c75063c0649ecd";
    static String expectedTransactionHash = "0xabcb0fd8ebb8f62ac899e5211b9ba47fe948a8efd815229cc4ed9cd781464f15";
    static String expectedSenderTransactionHash = "0x2c4e8cd3c68a4aacae51c695e857cfc1a019037ca71d8cd1e8ca56ec4eaf55b1";
    static String expectedRLPEncodingForSigning = "0xf857b83df83b128204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6f1e945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(feeDelegatedValueTransferMemoWithRatio);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setInput(input)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            feeDelegatedValueTransferMemoWithRatio.fillTransaction();

            assertFalse(feeDelegatedValueTransferMemoWithRatio.getNonce().isEmpty());
            assertFalse(feeDelegatedValueTransferMemoWithRatio.getGasPrice().isEmpty());
            assertFalse(feeDelegatedValueTransferMemoWithRatio.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setInput(input)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, feeDelegatedValueTransferMemoWithRatio.getGas());
            assertEquals(gasPrice, feeDelegatedValueTransferMemoWithRatio.getGasPrice());
            assertEquals(chainID, feeDelegatedValueTransferMemoWithRatio.getChainId());
            assertEquals(value, feeDelegatedValueTransferMemoWithRatio.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input : ");

            String input = "invalid";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            BigInteger feeRatio = BigInteger.valueOf(101);

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setInput(input)
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

            FeeDelegatedValueTransferMemoWithRatio txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }


        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input : ");

            String input = "invalid";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            String feeRatio = "0xFF";

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            FeeDelegatedValueTransferMemoWithRatio txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            FeeDelegatedValueTransferMemoWithRatio txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            FeeDelegatedValueTransferMemoWithRatio txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemoWithRatio txObj;
        SingleKeyring keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();

        @Before
        public void before() {

            txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
            );

        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String privateKey = caver.wallet.keyring.generateSingleKey();
            String feePayer = new PrivateKey(privateKey).getDerivedAddress();

            txObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
            );

            txObj.signAsFeePayer(privateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
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
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
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
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
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

        FeeDelegatedValueTransferMemoWithRatio mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
            );

            singleKeyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            multipleKeyring = caver.wallet.keyring.createWithMultipleKey(feePayer, caver.wallet.keyring.generateMultipleKeys(8));
            roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(feePayer, caver.wallet.keyring.generateRolBasedKeys(new int[]{3, 4, 5}));
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

        FeeDelegatedValueTransferMemoWithRatio mTxObj;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
            );
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(emptySignature)
            );

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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(signatureData)
            );

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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(signatureData)
            );

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

        String from = "0xceca418cc3ed540c8d16675fe600d703154e379f";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String value = "0xa";
        String input = "0x68656c6c6f";
        String gas = "0xf4240";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";
        BigInteger feeRatio = BigInteger.valueOf(30);

        FeeDelegatedValueTransferMemoWithRatio mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeeRatio(feeRatio)
            );

            String rlpEncoded = "0x12f8a0018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef847f845820feaa050edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22a01fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d940000000000000000000000000000000000000000c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fea",
                    "0x50edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22",
                    "0x1fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0x50edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22",
                    "0x1fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeeRatio(feeRatio)
                            .setSignatures(signatureData)
            );

            String[] rlpEncodedStrings = {
                    "0x12f88c018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef847f845820fe9a03c5bdf4fba47ee89e3072d2c707efb241aef04cb2c7b9771bea2ffd62c2b3807a05d7be6df572fdb60f68a3250da5794a983f609991561d31a9189f0d7212de88c80c4c3018080",
                    "0x12f88c018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef847f845820feaa0f1e794e5f0a28afce80bd9a89883ed55f96a8d45b03ae8355524a0000eac8a2ea0202e179034aefcadcc7a25360c3bb88f1a572c5912e5031bac11d466ebb6727e80c4c3018080",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x12f9012e018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef8d5f845820feaa050edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22a01fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286df845820fe9a03c5bdf4fba47ee89e3072d2c707efb241aef04cb2c7b9771bea2ffd62c2b3807a05d7be6df572fdb60f68a3250da5794a983f609991561d31a9189f0d7212de88cf845820feaa0f1e794e5f0a28afce80bd9a89883ed55f96a8d45b03ae8355524a0000eac8a2ea0202e179034aefcadcc7a25360c3bb88f1a572c5912e5031bac11d466ebb6727e940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x50edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22",
                            "0x1fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x3c5bdf4fba47ee89e3072d2c707efb241aef04cb2c7b9771bea2ffd62c2b3807",
                            "0x5d7be6df572fdb60f68a3250da5794a983f609991561d31a9189f0d7212de88c"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xf1e794e5f0a28afce80bd9a89883ed55f96a8d45b03ae8355524a0000eac8a2e",
                            "0x202e179034aefcadcc7a25360c3bb88f1a572c5912e5031bac11d466ebb6727e"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeeRatio(feeRatio)
            );

            String rlpEncoded = "0x12f8a0018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ec4c301808094188375ff24b14775e1c13d382c2d1ef3a27ca614f847f845820fe9a05610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2a05fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0x5610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2",
                    "0x5fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0x188375ff24b14775e1c13d382c2d1ef3a27ca614";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x5610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2",
                    "0x5fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String[] rlpEncodedStrings = new String[]{
                    "0x12f8a0018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ec4c301808094188375ff24b14775e1c13d382c2d1ef3a27ca614f847f845820feaa0defc41992109af25e9956cbe7d593cd3f65dd2bf1e8f71d7ac1799451a90c062a03487aacf56a6f5f4719e51778ac5fac00e6994b0327ffa5edf99d879116e6e5a",
                    "0x12f8a0018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ec4c301808094188375ff24b14775e1c13d382c2d1ef3a27ca614f847f845820fe9a09913be30cc8b8c68fd4745f6b04ede43e272496c9245bc0784339cdff8b3c008a02e3b652fa111946ea868e29714370822220dec6c4bfabfcaf1f023df800217d2",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x12f9012e018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ec4c301808094188375ff24b14775e1c13d382c2d1ef3a27ca614f8d5f845820fe9a05610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2a05fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76f845820feaa0defc41992109af25e9956cbe7d593cd3f65dd2bf1e8f71d7ac1799451a90c062a03487aacf56a6f5f4719e51778ac5fac00e6994b0327ffa5edf99d879116e6e5af845820fe9a09913be30cc8b8c68fd4745f6b04ede43e272496c9245bc0784339cdff8b3c008a02e3b652fa111946ea868e29714370822220dec6c4bfabfcaf1f023df800217d2";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x5610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2",
                            "0x5fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xdefc41992109af25e9956cbe7d593cd3f65dd2bf1e8f71d7ac1799451a90c062",
                            "0x3487aacf56a6f5f4719e51778ac5fac00e6994b0327ffa5edf99d879116e6e5a"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x9913be30cc8b8c68fd4745f6b04ede43e272496c9245bc0784339cdff8b3c008",
                            "0x2e3b652fa111946ea868e29714370822220dec6c4bfabfcaf1f023df800217d2"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeeRatio(feeRatio)
            );

            String rlpEncodedString = "0x12f9011a018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef8d5f845820feaa050edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22a01fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286df845820fe9a03c5bdf4fba47ee89e3072d2c707efb241aef04cb2c7b9771bea2ffd62c2b3807a05d7be6df572fdb60f68a3250da5794a983f609991561d31a9189f0d7212de88cf845820feaa0f1e794e5f0a28afce80bd9a89883ed55f96a8d45b03ae8355524a0000eac8a2ea0202e179034aefcadcc7a25360c3bb88f1a572c5912e5031bac11d466ebb6727e80c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x50edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22",
                            "0x1fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x3c5bdf4fba47ee89e3072d2c707efb241aef04cb2c7b9771bea2ffd62c2b3807",
                            "0x5d7be6df572fdb60f68a3250da5794a983f609991561d31a9189f0d7212de88c"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xf1e794e5f0a28afce80bd9a89883ed55f96a8d45b03ae8355524a0000eac8a2e",
                            "0x202e179034aefcadcc7a25360c3bb88f1a572c5912e5031bac11d466ebb6727e"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x12f9012e018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ec4c301808094188375ff24b14775e1c13d382c2d1ef3a27ca614f8d5f845820fe9a05610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2a05fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76f845820feaa0defc41992109af25e9956cbe7d593cd3f65dd2bf1e8f71d7ac1799451a90c062a03487aacf56a6f5f4719e51778ac5fac00e6994b0327ffa5edf99d879116e6e5af845820fe9a09913be30cc8b8c68fd4745f6b04ede43e272496c9245bc0784339cdff8b3c008a02e3b652fa111946ea868e29714370822220dec6c4bfabfcaf1f023df800217d2";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x5610e0b35da77d24c009fd6040a43ee70248b60b91892611a0cf36ef185399a2",
                            "0x5fc451b5b9e90453e8fcdf797e1a0875746ddfe1fdcc6617a21eb8e35b328f76"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xdefc41992109af25e9956cbe7d593cd3f65dd2bf1e8f71d7ac1799451a90c062",
                            "0x3487aacf56a6f5f4719e51778ac5fac00e6994b0327ffa5edf99d879116e6e5a"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x9913be30cc8b8c68fd4745f6b04ede43e272496c9245bc0784339cdff8b3c008",
                            "0x2e3b652fa111946ea868e29714370822220dec6c4bfabfcaf1f023df800217d2"
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeeRatio(feeRatio)
            );

            String rlpEncoded = "0x12f88c018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94ceca418cc3ed540c8d16675fe600d703154e379f8568656c6c6f1ef847f845820feaa050edf44854ee83c3ea396614796a19b9ebe4714b6fde40f52ce02b8e7a32be22a01fbbd3dd81af0eadc375e390fd468d9574a76a826cc02abe55f1d1176da4286d80c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedValueTransferMemoWithRatio mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemoWithRatio mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedTransactionHash, mTxObj.getTransactionHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String txHash = mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemoWithRatio mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedSenderTransactionHash, mTxObj.getSenderTxHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemoWithRatio mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncodingForSigning, mTxObj.getRLPEncodingForFeePayerSignature());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemoWithRatio()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeeRatio(feeRatio)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
