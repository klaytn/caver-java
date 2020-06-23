package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferWithRatio;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        FeeDelegatedValueTransferWithRatioTest.createInstance.class,
        FeeDelegatedValueTransferWithRatioTest.createInstanceBuilder.class,
        FeeDelegatedValueTransferWithRatioTest.getRLPEncodingTest.class,
        FeeDelegatedValueTransferWithRatioTest.signAsFeePayer_OneKeyTest.class,
        FeeDelegatedValueTransferWithRatioTest.signAsFeePayer_AllKeyTest.class,
        FeeDelegatedValueTransferWithRatioTest.appendFeePayerSignaturesTest.class,
        FeeDelegatedValueTransferWithRatioTest.combineSignatureTest.class,
        FeeDelegatedValueTransferWithRatioTest.getRawTransactionTest.class,
        FeeDelegatedValueTransferWithRatioTest.getTransactionHashTest.class,
        FeeDelegatedValueTransferWithRatioTest.getSenderTxHashTest.class,
        FeeDelegatedValueTransferWithRatioTest.getRLPEncodingForFeePayerSignatureTest.class,
})
public class FeeDelegatedValueTransferWithRatioTest {
    static Caver caver = Caver.build(Caver.DEFAULT_URL);

    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";
    static String value = "0xa";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
    static BigInteger feeRatio = BigInteger.valueOf(30);

    static SignatureData senderSignatureData = new SignatureData(
            "0x25",
            "0xdde32b8241f039a82b124fe94d3e556eb08f0d6f26d07dcc0f3fca621f1090ca",
            "0x1c8c336b358ab6d3a2bbf25de2adab4d01b754e2fb3b9b710069177d54c1e956"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0x91ecf53f91bb97bb694f2f2443f3563ac2b646d651497774524394aae396360",
            "0x44228b88f275aa1ec1bab43681d21dc7e3a676786ed1906f6841d0a1a188f88a"
    );

    static String expectedRLPEncoding = "0x0af8d78204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b1ef845f84325a0dde32b8241f039a82b124fe94d3e556eb08f0d6f26d07dcc0f3fca621f1090caa01c8c336b358ab6d3a2bbf25de2adab4d01b754e2fb3b9b710069177d54c1e956945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0091ecf53f91bb97bb694f2f2443f3563ac2b646d651497774524394aae396360a044228b88f275aa1ec1bab43681d21dc7e3a676786ed1906f6841d0a1a188f88a";
    static String expectedTransactionHash = "0x83a89f4debd8e9d6374b987e25132b3a4030c9cf9ace2fc6e7d1086fcea2ce40";
    static String expectedSenderTransactionHash = "0x4711ed4023e821425968342c1d50063b6bc3176b1792b7075cfeee3656d450f6";
    static String expectedRLPEncodingForSigning = "0xf84fb6f50a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b1e945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = KeyringFactory.generateRoleBasedKeys(numArr, "entropy");
        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(feeDelegatedValueTransferWithRatio);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            feeDelegatedValueTransferWithRatio.fillTransaction();

            assertFalse(feeDelegatedValueTransferWithRatio.getNonce().isEmpty());
            assertFalse(feeDelegatedValueTransferWithRatio.getGasPrice().isEmpty());
            assertFalse(feeDelegatedValueTransferWithRatio.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, feeDelegatedValueTransferWithRatio.getGas());
            assertEquals(gasPrice, feeDelegatedValueTransferWithRatio.getGasPrice());
            assertEquals(chainID, feeDelegatedValueTransferWithRatio.getChainId());
            assertEquals(value, feeDelegatedValueTransferWithRatio.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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
            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );

            assertNotNull(feeDelegatedValueTransferWithRatio);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }



        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            String feeRatio = "0xFF";

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio(
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
                    value
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            FeeDelegatedValueTransferWithRatio txObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio txObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferWithRatio txObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

        FeeDelegatedValueTransferWithRatio txObj;
        SingleKeyring keyring = KeyringFactory.createWithSingleKey(feePayer, feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();

        @Before
        public void before() {

            txObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String feePayer = new PrivateKey(privateKey).getDerivedAddress();

            txObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

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
                    PrivateKey.generate().getPrivateKey(),
                    feePayerPrivateKey,
                    PrivateKey.generate().getPrivateKey()
            };

            MultipleKeyring keyring = KeyringFactory.createWithMultipleKey(feePayer, keyArr);
            txObj.signAsFeePayer(keyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_roleBasedKey() throws IOException {
            String[][] keyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),

                    },
                    {
                            PrivateKey.generate().getPrivateKey()
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            feePayerPrivateKey
                    }
            };

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(feePayer, Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = KeyringFactory.createWithSingleKey(feePayerPrivateKey, PrivateKey.generate().getPrivateKey());

            txObj.signAsFeePayer(keyring, 0);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3,3,3}, feePayer);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferWithRatio mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

            singleKeyring = KeyringFactory.createWithSingleKey(feePayer, feePayerPrivateKey);
            multipleKeyring = KeyringFactory.createWithMultipleKey(feePayer, KeyringFactory.generateMultipleKeys(8));
            roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(feePayer, KeyringFactory.generateRolBasedKeys(new int[]{3,4,5}));
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

            SingleKeyring keyring = KeyringFactory.createFromPrivateKey(PrivateKey.generate().getPrivateKey());
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferWithRatio mTxObj;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

        String from = "0x31e7c5218f810af8ad1e50bf207de0cfb5bd4526";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";
        BigInteger feeRatio = BigInteger.valueOf(30);

        FeeDelegatedValueTransferWithRatio mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x0af886018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef847f845820feaa0a832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084a044ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc14580c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fea",
                    "0xa832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084",
                    "0x44ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0xa832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084",
                    "0x44ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145"
            );

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .setSignatures(signatureData)
                    .build();

            String[] rlpEncodedStrings = {
                    "0x0af886018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef847f845820feaa0df06077618ef021174af72c807ac968ab6b8549ca997432df73a1fe3612ed226a052684b175ec8fb45506e6f809ec0c879afa62e5cc1e64bd91e16a58a6aa0966780c4c3018080",
                    "0x0af886018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef847f845820feaa02944c25a5dc2fe12d365743413ee4f4c133c3f0c142629cadff486679408b86ea05d633f11b2dde9cf51bc596565ca18a7f0f92b9d3447ce4f622188563299217e80c4c3018080",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x0af90114018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef8d5f845820feaa0a832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084a044ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145f845820feaa0df06077618ef021174af72c807ac968ab6b8549ca997432df73a1fe3612ed226a052684b175ec8fb45506e6f809ec0c879afa62e5cc1e64bd91e16a58a6aa09667f845820feaa02944c25a5dc2fe12d365743413ee4f4c133c3f0c142629cadff486679408b86ea05d633f11b2dde9cf51bc596565ca18a7f0f92b9d3447ce4f622188563299217e80c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0xa832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084",
                            "0x44ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xdf06077618ef021174af72c807ac968ab6b8549ca997432df73a1fe3612ed226",
                            "0x52684b175ec8fb45506e6f809ec0c879afa62e5cc1e64bd91e16a58a6aa09667"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x2944c25a5dc2fe12d365743413ee4f4c133c3f0c142629cadff486679408b86e",
                            "0x5d633f11b2dde9cf51bc596565ca18a7f0f92b9d3447ce4f622188563299217e"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x0af89a018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ec4c30180809412dbe69692cb021bc1f161dd5abc0507bd1493cef847f845820fe9a00c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaaa029354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0x0c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaa",
                    "0x29354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0x12dbe69692cb021bc1f161dd5abc0507bd1493ce";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x0c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaa",
                    "0x29354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390"
            );

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .setFeePayer(feePayer)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String[] rlpEncodedStrings = new String[] {
                    "0x0af89a018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ec4c30180809412dbe69692cb021bc1f161dd5abc0507bd1493cef847f845820feaa015a95b922f43aad929329b13a42c3b00760eb0cabebd19cc0c7935db7d46da9ca02e4d9aa62bc12e40405d8209abbd40b65ba90eb6ff63b4e9260180c3a17525e0",
                    "0x0af89a018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ec4c30180809412dbe69692cb021bc1f161dd5abc0507bd1493cef847f845820fe9a0b6a124f371122cb3ac5c98c1d0a94ce41d87387b05fce8cfc244c152ab580ffda0121e3c11516f67cda27ade771a7166b93cdbd2beeba39302bd240ee1d9432060",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x0af90128018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ec4c30180809412dbe69692cb021bc1f161dd5abc0507bd1493cef8d5f845820fe9a00c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaaa029354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390f845820feaa015a95b922f43aad929329b13a42c3b00760eb0cabebd19cc0c7935db7d46da9ca02e4d9aa62bc12e40405d8209abbd40b65ba90eb6ff63b4e9260180c3a17525e0f845820fe9a0b6a124f371122cb3ac5c98c1d0a94ce41d87387b05fce8cfc244c152ab580ffda0121e3c11516f67cda27ade771a7166b93cdbd2beeba39302bd240ee1d9432060";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x0c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaa",
                            "0x29354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x15a95b922f43aad929329b13a42c3b00760eb0cabebd19cc0c7935db7d46da9c",
                            "0x2e4d9aa62bc12e40405d8209abbd40b65ba90eb6ff63b4e9260180c3a17525e0"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xb6a124f371122cb3ac5c98c1d0a94ce41d87387b05fce8cfc244c152ab580ffd",
                            "0x121e3c11516f67cda27ade771a7166b93cdbd2beeba39302bd240ee1d9432060"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncodedString = "0x0af90114018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef8d5f845820feaa0a832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084a044ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145f845820feaa0df06077618ef021174af72c807ac968ab6b8549ca997432df73a1fe3612ed226a052684b175ec8fb45506e6f809ec0c879afa62e5cc1e64bd91e16a58a6aa09667f845820feaa02944c25a5dc2fe12d365743413ee4f4c133c3f0c142629cadff486679408b86ea05d633f11b2dde9cf51bc596565ca18a7f0f92b9d3447ce4f622188563299217e80c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fea",
                            "0xa832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084",
                            "0x44ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc145"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xdf06077618ef021174af72c807ac968ab6b8549ca997432df73a1fe3612ed226",
                            "0x52684b175ec8fb45506e6f809ec0c879afa62e5cc1e64bd91e16a58a6aa09667"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x2944c25a5dc2fe12d365743413ee4f4c133c3f0c142629cadff486679408b86e",
                            "0x5d633f11b2dde9cf51bc596565ca18a7f0f92b9d3447ce4f622188563299217e"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x0af90128018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ec4c30180809412dbe69692cb021bc1f161dd5abc0507bd1493cef8d5f845820fe9a00c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaaa029354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390f845820feaa015a95b922f43aad929329b13a42c3b00760eb0cabebd19cc0c7935db7d46da9ca02e4d9aa62bc12e40405d8209abbd40b65ba90eb6ff63b4e9260180c3a17525e0f845820fe9a0b6a124f371122cb3ac5c98c1d0a94ce41d87387b05fce8cfc244c152ab580ffda0121e3c11516f67cda27ade771a7166b93cdbd2beeba39302bd240ee1d9432060";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x0c438aba938ee678761ccde71696518d40ed0669c420aaedf66952af0f4eafaa",
                            "0x29354a82fe53b4971b745acd837e0182b7df7e03c6e77821e508669b6a0a6390"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x15a95b922f43aad929329b13a42c3b00760eb0cabebd19cc0c7935db7d46da9c",
                            "0x2e4d9aa62bc12e40405d8209abbd40b65ba90eb6ff63b4e9260180c3a17525e0"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xb6a124f371122cb3ac5c98c1d0a94ce41d87387b05fce8cfc244c152ab580ffd",
                            "0x121e3c11516f67cda27ade771a7166b93cdbd2beeba39302bd240ee1d9432060"
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x0af886018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9431e7c5218f810af8ad1e50bf207de0cfb5bd45261ef847f845820feaa0a832e241979ee7a3e08b49d7a7e8f8029982ee5502c7f970a8fe2676fe1b1084a044ded5739de93803b37790bb323f5020de50850b7b7cdc9a6a2e23a29a8cc14580c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedValueTransferWithRatio mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferWithRatio mTxObj;

        @Test
        public void getTransactionHash() {
           mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

        FeeDelegatedValueTransferWithRatio mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

        FeeDelegatedValueTransferWithRatio mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncodingForSigning, mTxObj.getRLPEncodingForFeePayerSignature());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
