package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.Cancel;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
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

@RunWith(Suite.class)
@Suite.SuiteClasses({CancelTest.createInstanceBuilder.class, CancelTest.createInstance.class, CancelTest.getRLPEncodingTest.class, CancelTest.signWithKeyTest.class, CancelTest.signWithKeysTest.class, CancelTest.appendSignaturesTest.class, CancelTest.combineSignatureTest.class, CancelTest.getRawTransactionTest.class, CancelTest.getTransactionHashTest.class, CancelTest.getSenderTxHashTest.class, CancelTest.getRLPEncodingForSignatureTest.class})
public class CancelTest {

    static Caver caver = new Caver(Caver.DEFAULT_URL);

    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";

    static SignatureData signatureData = new SignatureData(
            Numeric.hexStringToByteArray("0x25"),
            Numeric.hexStringToByteArray("0xfb2c3d53d2f6b7bb1deb5a09f80366a5a45429cc1e3956687b075a9dcad20434"),
            Numeric.hexStringToByteArray("0x5c6187822ee23b1001e9613d29a5d6002f990498d2902904f7f259ab3358216e")
    );

    static String expectedRLPEncoding = "0x38f8648204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0fb2c3d53d2f6b7bb1deb5a09f80366a5a45429cc1e3956687b075a9dcad20434a05c6187822ee23b1001e9613d29a5d6002f990498d2902904f7f259ab3358216e";
    static String expectedTransactionHash = "0x10d135d590cb587cc45c1f94f4a0e3b8c24d24a6e4243f09ca395fb4e2450413";
    static String expectedRLPEncodingForSigning = "0xe39fde388204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        String[][] keyArr = new String[3][];

        for(int i = 0; i < numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j = 0; j < length; j++) {
                arr[j] = PrivateKey.generate("entropy").getPrivateKey();
            }
            keyArr[i] = arr;
        }

        List<String[]> arr = Arrays.asList(keyArr);

        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignatures(signatureData)
                    .build();

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeCancel.toString(), txObj.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            Cancel txObj = new Cancel.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            assertNotNull(txObj);

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignatures(signatureData)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeCancel.toString(), txObj.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            Cancel txObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void signWithKey_Keyring() throws IOException {
            mTxObj.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring, 0);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_Only() throws IOException {
            mTxObj.sign(privateKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KlayWalletKey() throws IOException {
            mTxObj.sign(klaytnWalletKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring role = generateRoleBaseKeyring(new int[]{3, 3, 3}, from);
            mTxObj.sign(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            AbstractKeyring roleBased = generateRoleBaseKeyring(new int[]{3, 3, 3}, from);

            mTxObj.sign(roleBased);
            assertEquals(3, mTxObj.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void appendSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(emptySignature)
            );

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj.appendSignatures(list);
            assertEquals(2, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
        }

        @Test
        public void appendSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
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

            mTxObj.appendSignatures(list);
            assertEquals(3, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
            assertEquals(signatureData2, mTxObj.getSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x504a835246e030d70ded9027f9f5a0aefcd45143";
        String gas = "0xdbba0";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";
        String nonce = "0x1";

        Cancel mTxObj;

        @Test
        public void combineSignature() {
            String expectedRLPEncoded = "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa00382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8a059d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae";

            SignatureData expectedSignature = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                    Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
            );

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
            );

            String rlpEncoded = "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa00382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8a059d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            String expectedRLPEncoded = "0x38f8f7018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f8d5f845820feaa00382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8a059d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9aef845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700f845820feaa0dccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912a07572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd";

            SignatureData[] expectedSignature = new SignatureData[]{
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                            Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x5a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3c"),
                            Numeric.hexStringToByteArray("0x0934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0xdccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912"),
                            Numeric.hexStringToByteArray("0x7572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd")
                    )
            };

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                    Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
            );

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            String[] rlpEncodedString = new String[]{
                    "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700",
                    "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa0dccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912a07572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            int nonce = 1234;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(BigInteger.valueOf(nonce))
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
            );

            String rlpEncoded = "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700";

            mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));
        }
    }

    public static class getRawTransactionTest {
        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void getRawTransaction() {
            String rawTx = mTxObj.getRawTransaction();
            assertEquals(expectedRLPEncoding, rawTx);
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void getTransactionHash() {
            String txHash = mTxObj.getTransactionHash();
            assertEquals(expectedTransactionHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void getSenderTransactionHash() {
            String txHash = mTxObj.getSenderTxHash();
            assertEquals(expectedTransactionHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(caver.wallet.keyring.generate().getAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void getRLPEncodingForSignature() {
            String rlp = mTxObj.getRLPEncodingForSignature();
            assertEquals(expectedRLPEncodingForSigning, rlp);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = caver.transaction.cancel.create(
                    TxPropertyBuilder.cancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFrom(from)
                            .setChainId(chainID)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
