package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class FeeDelegatedValueTransferTest {
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
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

    static SignatureData senderSignatureData = new SignatureData(
            "0x25",
            "0x9f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956",
            "0x6bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0xf45cf8d7f88c08e6b6ec0b3b562f34ca94283e4689021987abb6b0772ddfd80a",
            "0x298fe2c5aeabb6a518f4cbb5ff39631a5d88be505d3923374f65fdcf63c2955b"
    );

    static String expectedRLPEncoding = "0x09f8d68204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a09f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956a06bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0f45cf8d7f88c08e6b6ec0b3b562f34ca94283e4689021987abb6b0772ddfd80aa0298fe2c5aeabb6a518f4cbb5ff39631a5d88be505d3923374f65fdcf63c2955b";
    static String expectedTransactionHash = "0xe1e07f9971153499fc8c7bafcdaf7abc20b37aa4c18fb1e53a9bfcc259e3644c";
    static String expectedSenderTransactionHash = "0x40f8c94e01e07eb5353f6cd4cd3eabd5893215dd53a50ba4b8ff9a447ac51731";
    static String expectedRLPEncodingForSigning = "0xf84eb5f4098204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b945a0043070275d9f6054307ee7348bd660849d90f018080";


    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(feeDelegatedValueTransfer);
            assertEquals(TransactionType.TxTypeFeeDelegatedValueTransfer.toString(), feeDelegatedValueTransfer.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            feeDelegatedValueTransfer.fillTransaction();

            assertFalse(feeDelegatedValueTransfer.getNonce().isEmpty());
            assertFalse(feeDelegatedValueTransfer.getGasPrice().isEmpty());
            assertFalse(feeDelegatedValueTransfer.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, feeDelegatedValueTransfer.getGas());
            assertEquals(gasPrice, feeDelegatedValueTransfer.getGasPrice());
            assertEquals(chainID, feeDelegatedValueTransfer.getChainId());
            assertEquals(value, feeDelegatedValueTransfer.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertNotNull(feeDelegatedValueTransfer);
            assertEquals(TransactionType.TxTypeFeeDelegatedValueTransfer.toString(), feeDelegatedValueTransfer.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, feeDelegatedValueTransfer.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            feeDelegatedValueTransfer.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = "0x";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            feeDelegatedValueTransfer.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransfer txObj;
        SingleKeyring keyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            txObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

            keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            klaytnWalletKey = keyring.getKlaytnWalletKey();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String expectedRLPEncoding = "0x09f8d68204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a09f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956a06bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e79433f524631e573329a550296f595c820d6c65213ff845f84325a011f8278d8d465d816acd57c1dd7fe07a070b747cefd65ef8c6607fe35b886de8a074cbb48a5ad3f4d8593de69ee8fd7d67d23636710c16097ca1c9bbb1f6967e72";

            SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey(feePayerPrivateKey);
            String feePayer = keyring.getAddress();

            txObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

            txObj.signAsFeePayer(feePayerPrivateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
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
                    caver.wallet.keyring.generateSingleKey(),
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
                            caver.wallet.keyring.generateSingleKey(),
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            feePayerPrivateKey
                    }
            };

            RoleBasedKeyring keyring = caver.wallet.keyring.createWithRoleBasedKey(feePayer, Arrays.asList(keyArr));
            txObj.signAsFeePayer(keyring, 1);
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

        FeeDelegatedValueTransfer mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

            singleKeyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
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

            SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey(
                    caver.wallet.keyring.generateSingleKey()
            );
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransfer mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKey
            );
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

        String from = "0x04bb86a1b16113ebe8f57071f839b002cbcbf7d0";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";

        FeeDelegatedValueTransfer mTxObj;


        @Test
        public void decode() {
            FeeDelegatedValueTransfer txObj = FeeDelegatedValueTransfer.decode(expectedRLPEncoding);
        }

        @Test
        public void combineSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
            );

            SignatureData expectedSignature = new SignatureData(
                    "0x0fea",
                    "0x68e56f3da7fbe7a86543eb4b244ddbcb13b2d1cb9adb3ee8a4c8b046821bc492",
                    "0x68c29c057055f68a7860b54184bba7967bcf42b6aae12beaf9f30933e6e730c2"
            );

            String rlpEncoded = "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0f847f845820feaa068e56f3da7fbe7a86543eb4b244ddbcb13b2d1cb9adb3ee8a4c8b046821bc492a068c29c057055f68a7860b54184bba7967bcf42b6aae12beaf9f30933e6e730c2940000000000000000000000000000000000000000c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mTxObj.combineSignedRawTransactions(list);

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signature = new SignatureData(
                    "0x0fea",
                    "0x68e56f3da7fbe7a86543eb4b244ddbcb13b2d1cb9adb3ee8a4c8b046821bc492",
                    "0x68c29c057055f68a7860b54184bba7967bcf42b6aae12beaf9f30933e6e730c2"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setSignatures(signature)
            );

            String expectedRLPEncoded = "0x09f90127018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0f8d5f845820feaa068e56f3da7fbe7a86543eb4b244ddbcb13b2d1cb9adb3ee8a4c8b046821bc492a068c29c057055f68a7860b54184bba7967bcf42b6aae12beaf9f30933e6e730c2f845820feaa007337912a1855c1b3ca511eb44099350590e54aa611069058a9b739945ad97eaa037dfa221d29bc6d418ade23456de937993885b77cde5bc265739f278deebbc39f845820fe9a0799f833aa487296b11988650c9a63dc2a850715de4a29c8ab2b7c648718205a6a005a5fbad245cceccb4c08dd4a1cc6e26dc4fda06d0f49b248f83329623e3bee8940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignature = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x68e56f3da7fbe7a86543eb4b244ddbcb13b2d1cb9adb3ee8a4c8b046821bc492",
                            "0x68c29c057055f68a7860b54184bba7967bcf42b6aae12beaf9f30933e6e730c2"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x07337912a1855c1b3ca511eb44099350590e54aa611069058a9b739945ad97ea",
                            "0x37dfa221d29bc6d418ade23456de937993885b77cde5bc265739f278deebbc39"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x799f833aa487296b11988650c9a63dc2a850715de4a29c8ab2b7c648718205a6",
                            "0x05a5fbad245cceccb4c08dd4a1cc6e26dc4fda06d0f49b248f83329623e3bee8"
                    )
            };

            String[] rlpEncodedString = new String[]{
                    "0x09f885018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0f847f845820feaa007337912a1855c1b3ca511eb44099350590e54aa611069058a9b739945ad97eaa037dfa221d29bc6d418ade23456de937993885b77cde5bc265739f278deebbc3980c4c3018080",
                    "0x09f885018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0f847f845820fe9a0799f833aa487296b11988650c9a63dc2a850715de4a29c8ab2b7c648718205a6a005a5fbad245cceccb4c08dd4a1cc6e26dc4fda06d0f49b248f83329623e3bee880c4c3018080"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_withFeePayerSignature() {
            String feePayer = "0xb85f01a3b0b6aaa2e487c9ed541e27b75b3eba95";
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
            );

            String rlpEncoded = "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820fe9a0388a4beb8a27fe3c3631eb66278f0a756da13562af5fa1c33345eccf742555dda065b829314f8e91f2ee0266d4f4936d3f3bdc7ef1364a931a068742834c2519f2";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x388a4beb8a27fe3c3631eb66278f0a756da13562af5fa1c33345eccf742555dd",
                    "0x65b829314f8e91f2ee0266d4f4936d3f3bdc7ef1364a931a068742834c2519f2"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_withMultipleFeePayerSignature() {
            String feePayer = "0xb85f01a3b0b6aaa2e487c9ed541e27b75b3eba95";
            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0x388a4beb8a27fe3c3631eb66278f0a756da13562af5fa1c33345eccf742555dd",
                    "0x65b829314f8e91f2ee0266d4f4936d3f3bdc7ef1364a931a068742834c2519f2"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeePayerSignatures(signatureData)
            );

            String[] rlpEncodedStrings = new String[]{
                    "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820fe9a00585c73b60072ebb22bcc38b08e318dc88fc074435c3fa5d345219f1962098b7a06adcc5a1bc49d1c465412628bf8782aa8254af7fae8763d834a3f1711b22474a",
                    "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820feaa0d432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629a04f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));

            String expectedRLPEncoded = "0x09f90127018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f8d5f845820fe9a0388a4beb8a27fe3c3631eb66278f0a756da13562af5fa1c33345eccf742555dda065b829314f8e91f2ee0266d4f4936d3f3bdc7ef1364a931a068742834c2519f2f845820fe9a00585c73b60072ebb22bcc38b08e318dc88fc074435c3fa5d345219f1962098b7a06adcc5a1bc49d1c465412628bf8782aa8254af7fae8763d834a3f1711b22474af845820feaa0d432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629a04f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b";

            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x388a4beb8a27fe3c3631eb66278f0a756da13562af5fa1c33345eccf742555dd",
                            "0x65b829314f8e91f2ee0266d4f4936d3f3bdc7ef1364a931a068742834c2519f2"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x0585c73b60072ebb22bcc38b08e318dc88fc074435c3fa5d345219f1962098b7",
                            "0x6adcc5a1bc49d1c465412628bf8782aa8254af7fae8763d834a3f1711b22474a"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xd432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629",
                            "0x4f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b"
                    ),
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatures[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatures[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatures[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            String value = "0x1000";

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
            );

            String rlpEncoded = "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820feaa0d432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629a04f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRawTransaction() {
            FeeDelegatedValueTransfer mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined.");

            caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            ).getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined.");

            caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            ).getRawTransaction();
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransfer mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransfer mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransfer mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransfer.create(
                    TxPropertyBuilder.feeDelegatedValueTransfer()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

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
        );

        List<SignatureData> expectedFeePayerSigData = Arrays.asList(
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
        );

        FeeDelegatedValueTransfer tx = new FeeDelegatedValueTransfer.Builder()
                .setFrom("0x07a9a76ef778676c3bd2b334edcf581db31a85e5")
                .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                .setValue("0x1")
                .setChainId("0x7e3")
                .setGasPrice("0x5d21dba00")
                .setNonce("0x0")
                .setGas("0x2faf080")
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
