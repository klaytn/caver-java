package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FeeDelegatedSmartContractExecutionTest.createInstance.class,
        FeeDelegatedSmartContractExecutionTest.createInstanceBuilder.class,
        FeeDelegatedSmartContractExecutionTest.getRLPEncodingTest.class,
        FeeDelegatedSmartContractExecutionTest.signAsFeePayer_OneKeyTest.class,
        FeeDelegatedSmartContractExecutionTest.signAsFeePayer_AllKeyTest.class,
        FeeDelegatedSmartContractExecutionTest.appendFeePayerSignaturesTest.class,
        FeeDelegatedSmartContractExecutionTest.combineSignatureTest.class,
        FeeDelegatedSmartContractExecutionTest.getRawTransactionTest.class,
        FeeDelegatedSmartContractExecutionTest.getTransactionHashTest.class,
        FeeDelegatedSmartContractExecutionTest.getSenderTxHashTest.class,
        FeeDelegatedSmartContractExecutionTest.getRLPEncodingForFeePayerSignatureTest.class,
})
public class FeeDelegatedSmartContractExecutionTest {
    static Caver caver = Caver.build(Caver.DEFAULT_URL);
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

    static String input = "0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2";

    static SignatureData senderSignatureData = new SignatureData(
            "0x25",
            "0x253aea7d2c37160da45e84afbb45f6b3341cf1e8fc2df4ecc78f14adb512dc4f",
            "0x22465b74015c2a8f8501186bb5e200e6ce44be52e9374615a7e7e21c41bc27b5"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0xe7c51db7b922c6fa2a941c9687884c593b1b13076bdf0c473538d826bf7b9d1a",
            "0x5b0de2aabb84b66db8bf52d62f3d3b71b592e3748455630f1504c20073624d80"
    );

    static String expectedRLPEncoding = "0x31f8fb8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84325a0253aea7d2c37160da45e84afbb45f6b3341cf1e8fc2df4ecc78f14adb512dc4fa022465b74015c2a8f8501186bb5e200e6ce44be52e9374615a7e7e21c41bc27b5945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0e7c51db7b922c6fa2a941c9687884c593b1b13076bdf0c473538d826bf7b9d1aa05b0de2aabb84b66db8bf52d62f3d3b71b592e3748455630f1504c20073624d80";
    static String expectedTransactionHash = "0xef46f28c54b3d90a183e26f406ca1d5cc2b6e9fbb6cfa7c85a10330ffadf54b0";
    static String expectedSenderTransactionHash = "0x3cd3380f4206943422d5d5b218dd66d03d60d19a109f9929ea12b52a230257cb";
    static String expectedRLPEncodingForFeePayerSigning = "0xf875b85bf859318204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = KeyringFactory.generateRoleBasedKeys(numArr, "entropy");
        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
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
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String expectedRawTx = "0x31f8fb8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84325a0253aea7d2c37160da45e84afbb45f6b3341cf1e8fc2df4ecc78f14adb512dc4fa022465b74015c2a8f8501186bb5e200e6ce44be52e9374615a7e7e21c41bc27b59433f524631e573329a550296f595c820d6c65213ff845f84325a003eb559128cf96555ef9df53a8d93f8d8d3924bb510aa4d546f13d89a6ebcdf0a0743788a7ddc975fce765f837726c601d7f065af70f94af5b671b3f445825ecb5";

        FeeDelegatedSmartContractExecution txObj;
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey(feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();
        String feePayerAddress = keyring.getAddress();

        @Before
        public void before() {

            txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayerAddress)
                    .setSignatures(senderSignatureData)
                    .build();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            txObj.signAsFeePayer(feePayerPrivateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_multipleKey() throws IOException {
            String[] keyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    feePayerPrivateKey,
                    PrivateKey.generate().getPrivateKey()
            };

            MultipleKeyring keyring = KeyringFactory.createWithMultipleKey(feePayerAddress, keyArr);
            txObj.signAsFeePayer(keyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
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

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(feePayerAddress, Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
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

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3,3,3}, feePayerAddress);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecution mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

        FeeDelegatedSmartContractExecution mTxObj;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

        String from = "0x553b11f36cd1ebcbf74c920dc51cd8a1648cb98a";
        String to = "0xd3e7cbbba40c98e05d972438b11ff9374d71654a";
        String value = "0x0";
        String input = "0xa9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400";
        String gas = "0xdbba0";
        String nonce = "0x3";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        FeeDelegatedSmartContractExecution mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setInput(input)
                    .setChainId(chainID)
                    .build();

            String rlpEncoded = "0x31f8cb038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400f847f845820fe9a07531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acfa03ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd7180c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x7531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acf",
                    "0x3ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData senderSignature = new SignatureData(
                    "0x0fe9",
                    "0x7531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acf",
                    "0x3ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71"
            );

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setInput(input)
                    .setChainId(chainID)
                    .setSignatures(senderSignature)
                    .build();

            String[] rlpEncodedStrings = {
                    "0x31f8cb038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400f847f845820feaa0390e818cdd138b4c698082c52c330589489af0ba169f5c8685247c53abd08831a0784755dd4bc6c0a4b8e7f32f84c9d22e4b5ed04ddb43e9dfc35ee9083db474a380c4c3018080",
                    "0x31f8cb038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400f847f845820feaa0a94395fb25f06759e101fb159f7a2989b08c8912564b74d0c64078d964747f18a003c4574aa95af372c69accb57faa8e713ca180d3af85fa690201d33bf204639080c4c3018080"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x31f90159038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400f8d5f845820fe9a07531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acfa03ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71f845820feaa0390e818cdd138b4c698082c52c330589489af0ba169f5c8685247c53abd08831a0784755dd4bc6c0a4b8e7f32f84c9d22e4b5ed04ddb43e9dfc35ee9083db474a3f845820feaa0a94395fb25f06759e101fb159f7a2989b08c8912564b74d0c64078d964747f18a003c4574aa95af372c69accb57faa8e713ca180d3af85fa690201d33bf204639080c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x7531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acf",
                            "0x3ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x390e818cdd138b4c698082c52c330589489af0ba169f5c8685247c53abd08831",
                            "0x784755dd4bc6c0a4b8e7f32f84c9d22e4b5ed04ddb43e9dfc35ee9083db474a3"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa94395fb25f06759e101fb159f7a2989b08c8912564b74d0c64078d964747f18",
                            "0x03c4574aa95af372c69accb57faa8e713ca180d3af85fa690201d33bf2046390"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setInput(input)
                    .setChainId(chainID)
                    .build();

            String rlpEncoded = "0x31f8df038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf847f845820feaa08d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573a07e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5b";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0x8d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573",
                    "0x7e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5b"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0xfc9fb77a8407e4ac10e6d5f96574debc844d0d5b";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fea",
                    "0x8d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573",
                    "0x7e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5b"
            );

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setInput(input)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String[] rlpEncodedStrings = new String[] {
                    "0x31f8df038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf847f845820fe9a08ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69a03910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638",
                    "0x31f8df038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf847f845820feaa025f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1a020d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x31f9016d038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf8d5f845820feaa08d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573a07e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5bf845820fe9a08ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69a03910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638f845820feaa025f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1a020d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x8d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573",
                            "0x7e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5b"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x8ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69",
                            "0x3910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x25f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1",
                            "0x20d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .build();


            String rlpEncodedString = "0x31f90159038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400f8d5f845820fe9a07531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acfa03ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71f845820feaa0390e818cdd138b4c698082c52c330589489af0ba169f5c8685247c53abd08831a0784755dd4bc6c0a4b8e7f32f84c9d22e4b5ed04ddb43e9dfc35ee9083db474a3f845820feaa0a94395fb25f06759e101fb159f7a2989b08c8912564b74d0c64078d964747f18a003c4574aa95af372c69accb57faa8e713ca180d3af85fa690201d33bf204639080c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x7531ee2314700f1d4f45983cfd9f865cd7c7d90341c745f7371073f407d48acf",
                            "0x3ea07fc14ccd89da897dbfbe10ad04fe8c74ee3a3f3cadf1c5697a8f669bbd71"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x390e818cdd138b4c698082c52c330589489af0ba169f5c8685247c53abd08831",
                            "0x784755dd4bc6c0a4b8e7f32f84c9d22e4b5ed04ddb43e9dfc35ee9083db474a3"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa94395fb25f06759e101fb159f7a2989b08c8912564b74d0c64078d964747f18",
                            "0x03c4574aa95af372c69accb57faa8e713ca180d3af85fa690201d33bf2046390"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x31f9016d038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf8d5f845820feaa08d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573a07e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5bf845820fe9a08ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69a03910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638f845820feaa025f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1a020d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fea",
                            "0x8d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573",
                            "0x7e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5b"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x8ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69",
                            "0x3910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x25f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1",
                            "0x20d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5"
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .build();

            String rlpEncoded = "0x31f9016d038505d21dba00830dbba094d3e7cbbba40c98e05d972438b11ff9374d71654a8094553b11f36cd1ebcbf74c920dc51cd8a1648cb98ab844a9059cbb000000000000000000000000fc9fb77a8407e4ac10e6d5f96574debc844d0d5b00000000000000000000000000000000000000000000000000000002540be400c4c301808094fc9fb77a8407e4ac10e6d5f96574debc844d0d5bf8d5f845820feaa08d9d977567a1903deb82d67525beaa23842ebfe8ae7dad04c0d161a9a2451573a07e280f122aaf89e6379e95d1499c2d536d7ac37b77fa8980b5f083d153f2fb5bf845820fe9a08ffc31dc605d1d93b62e5dc5d72d62efe6994235e370feffc2f4366cf5f68a69a03910e05d112c137482ddb5740062dfcc6ce1556f081f22efb9b5f343adf45638f845820feaa025f9886ca65ae770ac69e155978600c6dfe9f2f3f06c692bbae5175f5eb4d7e1a020d0b91badffe5074dd66bdd558ddd2be0ec629e83e6616cf381bb692d41bbe5";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecution mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecution mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractExecution mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }

}
