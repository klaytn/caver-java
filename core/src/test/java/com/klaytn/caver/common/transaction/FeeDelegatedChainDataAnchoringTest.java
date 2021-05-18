package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedChainDataAnchoring;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class FeeDelegatedChainDataAnchoringTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);

    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String feePayer = "0x33f524631e573329a550296F595c820D6c65213f";

    static String gas = "0x174876e800";
    static String gasPrice = "0x5d21dba00";
    static String nonce = "0x11";
    static String chainID = "0x1";

    static String input = "f8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006";

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0xafe41edc9cce1185ab9065ca7dbfb89ab5c7bde3602a659aa258324124644142",
            "0x317848698248ba7cc057b8f0dd19a27b52ef904d29cb72823100f1ed18ba2bb3"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x25",
            "0x309e46db21a1bf7bfdae24d9192aca69516d6a341ecce8971fc69cff481cee76",
            "0x4b939bf7384c4f919880307323a5e36d4d6e029bae1887a43332710cdd48f174"
    );

    static String expectedRLPEncoding = "0x49f90176118505d21dba0085174876e80094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f845f84326a0afe41edc9cce1185ab9065ca7dbfb89ab5c7bde3602a659aa258324124644142a0317848698248ba7cc057b8f0dd19a27b52ef904d29cb72823100f1ed18ba2bb39433f524631e573329a550296f595c820d6c65213ff845f84325a0309e46db21a1bf7bfdae24d9192aca69516d6a341ecce8971fc69cff481cee76a04b939bf7384c4f919880307323a5e36d4d6e029bae1887a43332710cdd48f174";
    static String expectedTransactionHash = "0xecf1ec12937065617f9b3cd07570452bfdb75dc36404c4f37f78995c6dc462af";
    static String expectedSenderTransactionHash = "0x4f5c00ea8f6346baa7d4400dfefd72efa5ec219561ebcebed7be8a2b79d52bcd";
    static String expectedRLPEncodingForFeePayerSigning = "0xf8f0b8d6f8d449118505d21dba0085174876e80094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a000000000000000000000000000000000000000000000000000000000000000040580069433f524631e573329a550296f595c820d6c65213f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(TransactionType.TxTypeFeeDelegatedChainDataAnchoring.toString(), txObj.getType());
        }

        @Test
        public void BuilderTestWithRPCTest() throws IOException {
            String gasPrice = null;
            String nonce = null;
            String chainID = null;


            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
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
        public void BuilderTestWithBigInteger() throws IOException {
            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setGas(Numeric.toBigInt(gas))
                    .setFrom(from)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setInput(input)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
            assertEquals(nonce, txObj.getNonce());
        }


        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
                    .setInput(input)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = new FeeDelegatedChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setFeePayer(feePayer)
                    .setInput(input)
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
            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedChainDataAnchoring.toString(), txObj.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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
            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedChainDataAnchoring txObj;
        SingleKeyring keyring;
        String feePayerPrivateKey;
        String klaytnWalletKey;
        String feePayerAddress;

        @Before
        public void before() {
            feePayerPrivateKey = caver.wallet.keyring.generateSingleKey();
            keyring = caver.wallet.keyring.createFromPrivateKey(feePayerPrivateKey);
            klaytnWalletKey = keyring.getKlaytnWalletKey();
            feePayerAddress = keyring.getAddress();

            txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayerAddress)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
            );
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
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

            MultipleKeyring keyring = caver.wallet.keyring.createWithMultipleKey(feePayerAddress, keyArr);
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

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(feePayerAddress, Arrays.asList(keyArr));
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

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3, 3, 3}, feePayerAddress);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedChainDataAnchoring mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
            );

            singleKeyring = caver.wallet.keyring.createWithSingleKey(feePayer, caver.wallet.keyring.generateSingleKey());
            multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                    feePayer,
                    caver.wallet.keyring.generateMultipleKeys(8)
            );
            roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                    feePayer,
                    caver.wallet.keyring.generateRolBasedKeys(new int[]{3, 4, 5})
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

        FeeDelegatedChainDataAnchoring mTxObj;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
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

        String from = "0xf1f766ded1aae1e06e2ed6c85127dd69891f7b28";
        String gas = "0x174876e800";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String input = FeeDelegatedChainDataAnchoringTest.input;

        FeeDelegatedChainDataAnchoring mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainID)
            );

            String rlpEncoded = "0x49f90136018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f847f845820feaa0042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9a03ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be940000000000000000000000000000000000000000c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fea",
                    "0x042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9",
                    "0x3ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData senderSignature = new SignatureData(
                    "0x0fea",
                    "0x042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9",
                    "0x3ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be"
            );

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainID)
                            .setSignatures(senderSignature)
            );

            String[] rlpEncodedStrings = {
                    "0x49f90122018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f847f845820feaa0a45210f00ff64784e0aac0597b7eb19ea0890144100fd8dc8bb0b2fe003cbe84a07ff706e9a3825be7767f389789927a5633cf4995790a8bfe26d9332300de5db080c4c3018080",
                    "0x49f90122018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f847f845820feaa076a91becce2c632980731d97a319216030de7b1b94b04c8a568236547d42d263a061c3c3456cda8eb7c440c700a168204b054d45d7bf2652c04171a0a1f76eff7380c4c3018080"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x49f901c4018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f8d5f845820feaa0042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9a03ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974bef845820feaa0a45210f00ff64784e0aac0597b7eb19ea0890144100fd8dc8bb0b2fe003cbe84a07ff706e9a3825be7767f389789927a5633cf4995790a8bfe26d9332300de5db0f845820feaa076a91becce2c632980731d97a319216030de7b1b94b04c8a568236547d42d263a061c3c3456cda8eb7c440c700a168204b054d45d7bf2652c04171a0a1f76eff73940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9",
                            "0x3ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa45210f00ff64784e0aac0597b7eb19ea0890144100fd8dc8bb0b2fe003cbe84",
                            "0x7ff706e9a3825be7767f389789927a5633cf4995790a8bfe26d9332300de5db0"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x76a91becce2c632980731d97a319216030de7b1b94b04c8a568236547d42d263",
                            "0x61c3c3456cda8eb7c440c700a168204b054d45d7bf2652c04171a0a1f76eff73"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainID)
            );

            String rlpEncoded = "0x49f90136018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006c4c301808094ee43ecbed54e4862ed98c11d2e71b8bd04c1667ef847f845820fe9a0f8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356fa0346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0xf8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356f",
                    "0x346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0xee43ecbed54e4862ed98c11d2e71b8bd04c1667e";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fe9",
                    "0xf8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356f",
                    "0x346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16"
            );

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setFeePayer(feePayer)
                            .setFeePayerSignatures(feePayerSignatureData)
                            .setChainId(chainID)
            );


            String[] rlpEncodedStrings = new String[]{
                    "0x49f90136018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006c4c301808094ee43ecbed54e4862ed98c11d2e71b8bd04c1667ef847f845820feaa0baa6a845e8c68ae8bf9acc7e018bceaab506e0818e0dc8db2afe3490a1927317a046bacf69af211302103f8c3841bc3cc6a79e2298ee4bc5d5e73b25f42ca98156",
                    "0x49f90136018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006c4c301808094ee43ecbed54e4862ed98c11d2e71b8bd04c1667ef847f845820fe9a05df342131bfdae8239829e16a4298d711c238d0d4ab679b864878be729362921a07e3a7f484d6eb139c6b652c96aaa8ac8df43a5dfb3adaff46bc552a2c6965cba",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x49f901c4018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006c4c301808094ee43ecbed54e4862ed98c11d2e71b8bd04c1667ef8d5f845820fe9a0f8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356fa0346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16f845820feaa0baa6a845e8c68ae8bf9acc7e018bceaab506e0818e0dc8db2afe3490a1927317a046bacf69af211302103f8c3841bc3cc6a79e2298ee4bc5d5e73b25f42ca98156f845820fe9a05df342131bfdae8239829e16a4298d711c238d0d4ab679b864878be729362921a07e3a7f484d6eb139c6b652c96aaa8ac8df43a5dfb3adaff46bc552a2c6965cba";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xf8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356f",
                            "0x346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xbaa6a845e8c68ae8bf9acc7e018bceaab506e0818e0dc8db2afe3490a1927317",
                            "0x46bacf69af211302103f8c3841bc3cc6a79e2298ee4bc5d5e73b25f42ca98156"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x5df342131bfdae8239829e16a4298d711c238d0d4ab679b864878be729362921",
                            "0x7e3a7f484d6eb139c6b652c96aaa8ac8df43a5dfb3adaff46bc552a2c6965cba"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainID)
            );


            String rlpEncodedString = "0x49f901b0018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f8d5f845820feaa0042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9a03ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974bef845820feaa0a45210f00ff64784e0aac0597b7eb19ea0890144100fd8dc8bb0b2fe003cbe84a07ff706e9a3825be7767f389789927a5633cf4995790a8bfe26d9332300de5db0f845820feaa076a91becce2c632980731d97a319216030de7b1b94b04c8a568236547d42d263a061c3c3456cda8eb7c440c700a168204b054d45d7bf2652c04171a0a1f76eff7380c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9",
                            "0x3ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa45210f00ff64784e0aac0597b7eb19ea0890144100fd8dc8bb0b2fe003cbe84",
                            "0x7ff706e9a3825be7767f389789927a5633cf4995790a8bfe26d9332300de5db0"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x76a91becce2c632980731d97a319216030de7b1b94b04c8a568236547d42d263",
                            "0x61c3c3456cda8eb7c440c700a168204b054d45d7bf2652c04171a0a1f76eff73"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x49f901c4018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006c4c301808094ee43ecbed54e4862ed98c11d2e71b8bd04c1667ef8d5f845820fe9a0f8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356fa0346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16f845820feaa0baa6a845e8c68ae8bf9acc7e018bceaab506e0818e0dc8db2afe3490a1927317a046bacf69af211302103f8c3841bc3cc6a79e2298ee4bc5d5e73b25f42ca98156f845820fe9a05df342131bfdae8239829e16a4298d711c238d0d4ab679b864878be729362921a07e3a7f484d6eb139c6b652c96aaa8ac8df43a5dfb3adaff46bc552a2c6965cba";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xf8f21b4d667b139e80818c2b8bfd6117ace4bc11157be3c3ee74c0360565356f",
                            "0x346828779330f21b7d06be682ec8289f3211c4018a20385cabd0d0ebc2569f16"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xbaa6a845e8c68ae8bf9acc7e018bceaab506e0818e0dc8db2afe3490a1927317",
                            "0x46bacf69af211302103f8c3841bc3cc6a79e2298ee4bc5d5e73b25f42ca98156"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x5df342131bfdae8239829e16a4298d711c238d0d4ab679b864878be729362921",
                            "0x7e3a7f484d6eb139c6b652c96aaa8ac8df43a5dfb3adaff46bc552a2c6965cba"
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setInput(input)
                            .setChainId(chainID)
            );

            String rlpEncoded = "0x49f90122018505d21dba0085174876e80094f1f766ded1aae1e06e2ed6c85127dd69891f7b28b8aff8ad80b8aaf8a8a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a00000000000000000000000000000000000000000000000000000000000000004058006f847f845820feaa0042800bfb7429b6c054fed37b86c473fdea9d4481d5d5b32cc92f34d744983b9a03ce733dfce2efd9f6ffaf70d50a0a211b94d84a8a18f1196e875053896a974be80c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedChainDataAnchoring txObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedChainDataAnchoring mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

        FeeDelegatedChainDataAnchoring mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

        FeeDelegatedChainDataAnchoring mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedChainDataAnchoring.create(
                    TxPropertyBuilder.feeDelegatedChainDataAnchoring()
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setInput(input)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
