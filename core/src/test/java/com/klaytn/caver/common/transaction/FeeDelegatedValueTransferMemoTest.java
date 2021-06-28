package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferMemo;
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
public class FeeDelegatedValueTransferMemoTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";
    static String value = "0xa";
    static String input = "0x68656c6c6f";

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0x64e213aef0167fbd853f8f9989ef5d8b912a77457395ccf13d7f37009edd5c5b",
            "0x5d0c2e55e4d8734fe2516ed56ac628b74c0eb02aa3b6eda51e1e25a1396093e1"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0x87390ac14d3c34440b6ddb7b190d3ebde1a07d9a556e5a82ce7e501f24a060f9",
            "0x37badbcb12cda1ed67b12b1831683a08a3adadee2ea760a07a46bdbb856fea44"
    );

    static String expectedRLPEncoding = "0x11f8dc8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6ff845f84326a064e213aef0167fbd853f8f9989ef5d8b912a77457395ccf13d7f37009edd5c5ba05d0c2e55e4d8734fe2516ed56ac628b74c0eb02aa3b6eda51e1e25a1396093e1945a0043070275d9f6054307ee7348bd660849d90ff845f84326a087390ac14d3c34440b6ddb7b190d3ebde1a07d9a556e5a82ce7e501f24a060f9a037badbcb12cda1ed67b12b1831683a08a3adadee2ea760a07a46bdbb856fea44";
    static String expectedTransactionHash = "0x8f68882f6192a53ba470aeca1e83ed9b9e519906a91256724b284dee778b21c9";
    static String expectedSenderTransactionHash = "0xfffaa2b38d4e684ea70a89c78fc7b2659000d130c76ad721d68175cbfc77c550";
    static String expectedRLPEncodingForFeePayerSigning = "0xf856b83cf83a118204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6f945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedValueTransferMemo.toString(), txObj.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
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
            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            FeeDelegatedValueTransferMemo txObj = new FeeDelegatedValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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
            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedValueTransferMemo.toString(), txObj.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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
            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x";

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = "0x";

            FeeDelegatedValueTransferMemo txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemo txObj;
        SingleKeyring keyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

            keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            klaytnWalletKey = keyring.getKlaytnWalletKey();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String expectedRLPEncoding = "0x11f8dc8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6ff845f84326a064e213aef0167fbd853f8f9989ef5d8b912a77457395ccf13d7f37009edd5c5ba05d0c2e55e4d8734fe2516ed56ac628b74c0eb02aa3b6eda51e1e25a1396093e19433f524631e573329a550296f595c820d6c65213ff845f84325a00a59dd9f258c326e1bbaf1ebb0899a269a78afd70976ca73df257acdcb339faba01935d1df1c174c012a723c7a03b33fffd987499755306d2266f00e888f80bd2c";

            SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey(feePayerPrivateKey);
            String feePayer = keyring.getAddress();

            txObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

        FeeDelegatedValueTransferMemo mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            SingleKeyring keyring = caver.wallet.keyring.generate();
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemo mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayer(feePayer)
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

        String from = "0x1bc5339c6c55380d0da8aaa28e135164ecb86262";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String value = "0xa";
        String input = "0x68656c6c6f";
        String gas = "0xf4240";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";

        FeeDelegatedValueTransferMemo mTxObj;


        @Test
        public void combineSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainId)
            );


            SignatureData expectedSignature = new SignatureData(
                    "0x0fea",
                    "0x60a20eed201a2b28bc452b65c699083a6399aaeff2a7572c5c8cf54056254aea",
                    "0x01586e5321f51ed56da5241d6cc8365bdcade89c4b08d2615bc21231f5e2c26e"
            );

            String rlpEncoded = "0x11f89f018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6ff847f845820feaa060a20eed201a2b28bc452b65c699083a6399aaeff2a7572c5c8cf54056254aeaa001586e5321f51ed56da5241d6cc8365bdcade89c4b08d2615bc21231f5e2c26e940000000000000000000000000000000000000000c4c3018080";
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
                    "0x60a20eed201a2b28bc452b65c699083a6399aaeff2a7572c5c8cf54056254aea",
                    "0x01586e5321f51ed56da5241d6cc8365bdcade89c4b08d2615bc21231f5e2c26e"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainId)
                            .setSignatures(signature)
            );

            String expectedRLPEncoded = "0x11f9012d018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6ff8d5f845820feaa060a20eed201a2b28bc452b65c699083a6399aaeff2a7572c5c8cf54056254aeaa001586e5321f51ed56da5241d6cc8365bdcade89c4b08d2615bc21231f5e2c26ef845820fe9a0b8f3ba052cd0ef34b683a3e8ad6f68f71a82d9416bf9732def4b66802967a055a07c241fa9b7d32b72fc8310e886c5b70de262457fd07711cbb2e17217d8c39b26f845820feaa06c301c61b6b8746f63baf57c477bd269ecdeb07d6200a719988bfcd0b7767bc1a016da23b63b4e54ffa16ce8668987e48a76b8e64ba7863359462efd1e8d9838a6940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignature = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x60a20eed201a2b28bc452b65c699083a6399aaeff2a7572c5c8cf54056254aea",
                            "0x01586e5321f51ed56da5241d6cc8365bdcade89c4b08d2615bc21231f5e2c26e"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xb8f3ba052cd0ef34b683a3e8ad6f68f71a82d9416bf9732def4b66802967a055",
                            "0x7c241fa9b7d32b72fc8310e886c5b70de262457fd07711cbb2e17217d8c39b26"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x6c301c61b6b8746f63baf57c477bd269ecdeb07d6200a719988bfcd0b7767bc1",
                            "0x16da23b63b4e54ffa16ce8668987e48a76b8e64ba7863359462efd1e8d9838a6"
                    )
            };

            String[] rlpEncodedString = new String[]{
                    "0x11f88b018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6ff847f845820fe9a0b8f3ba052cd0ef34b683a3e8ad6f68f71a82d9416bf9732def4b66802967a055a07c241fa9b7d32b72fc8310e886c5b70de262457fd07711cbb2e17217d8c39b2680c4c3018080",
                    "0x11f88b018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6ff847f845820feaa06c301c61b6b8746f63baf57c477bd269ecdeb07d6200a719988bfcd0b7767bc1a016da23b63b4e54ffa16ce8668987e48a76b8e64ba7863359462efd1e8d9838a680c4c3018080"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_withFeePayerSignature() {
            String feePayer = "0x8d2f6e4986bc55e2d50611149e5725999a763d7c";
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
            );

            String rlpEncoded = "0x11f89f018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6fc4c3018080948d2f6e4986bc55e2d50611149e5725999a763d7cf847f845820feaa0779d20a7958d3131e5ef6a423abb2337e8f120bd0798c47227aee51c70d23c06a07d3c36d5a33cb18e8fec7d1e1f2cfd9a0ec932adee9ad9a090fcd28fafd44392";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fea",
                    "0x779d20a7958d3131e5ef6a423abb2337e8f120bd0798c47227aee51c70d23c06",
                    "0x7d3c36d5a33cb18e8fec7d1e1f2cfd9a0ec932adee9ad9a090fcd28fafd44392"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_withMultipleFeePayerSignature() {
            String feePayer = "0x8d2f6e4986bc55e2d50611149e5725999a763d7c";
            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0x779d20a7958d3131e5ef6a423abb2337e8f120bd0798c47227aee51c70d23c06",
                    "0x7d3c36d5a33cb18e8fec7d1e1f2cfd9a0ec932adee9ad9a090fcd28fafd44392"
            );

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeePayerSignatures(signatureData)
            );

            String[] rlpEncodedStrings = new String[]{
                    "0x11f89f018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6fc4c3018080948d2f6e4986bc55e2d50611149e5725999a763d7cf847f845820fe9a0de14998f4aba6474b55b84e9a236daf159252b460915cea204a4361cf99c9dc9a0743a40d63646defba13c70581d85000836155dddb30bc8024c62dad76981abec",
                    "0x11f89f018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6fc4c3018080948d2f6e4986bc55e2d50611149e5725999a763d7cf847f845820fe9a034fa68120ce57d201f0c859308d32d74835e7969555960c4041a466c9e2f8788a05a996a8c67347f0eba83cd6b38fe030aff2e8356e4b5ec2af85549f040014e3d"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));

            String expectedRLPEncoded = "0x11f9012d018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a941bc5339c6c55380d0da8aaa28e135164ecb862628568656c6c6fc4c3018080948d2f6e4986bc55e2d50611149e5725999a763d7cf8d5f845820feaa0779d20a7958d3131e5ef6a423abb2337e8f120bd0798c47227aee51c70d23c06a07d3c36d5a33cb18e8fec7d1e1f2cfd9a0ec932adee9ad9a090fcd28fafd44392f845820fe9a0de14998f4aba6474b55b84e9a236daf159252b460915cea204a4361cf99c9dc9a0743a40d63646defba13c70581d85000836155dddb30bc8024c62dad76981abecf845820fe9a034fa68120ce57d201f0c859308d32d74835e7969555960c4041a466c9e2f8788a05a996a8c67347f0eba83cd6b38fe030aff2e8356e4b5ec2af85549f040014e3d";

            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x779d20a7958d3131e5ef6a423abb2337e8f120bd0798c47227aee51c70d23c06",
                            "0x7d3c36d5a33cb18e8fec7d1e1f2cfd9a0ec932adee9ad9a090fcd28fafd44392"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xde14998f4aba6474b55b84e9a236daf159252b460915cea204a4361cf99c9dc9",
                            "0x743a40d63646defba13c70581d85000836155dddb30bc8024c62dad76981abec"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x34fa68120ce57d201f0c859308d32d74835e7969555960c4041a466c9e2f8788",
                            "0x5a996a8c67347f0eba83cd6b38fe030aff2e8356e4b5ec2af85549f040014e3d"
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
            );

            String rlpEncoded = "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820feaa0d432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629a04f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedValueTransferMemo mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedValueTransferMemo mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

        FeeDelegatedValueTransferMemo mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

        FeeDelegatedValueTransferMemo mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncodingForFeePayerSigning, mTxObj.getRLPEncodingForFeePayerSignature());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedValueTransferMemo.create(
                    TxPropertyBuilder.feeDelegatedValueTransferMemo()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setTo(to)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
