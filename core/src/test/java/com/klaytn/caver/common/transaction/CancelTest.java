package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.Cancel;
import com.klaytn.caver.transaction.type.SmartContractExecution;
import com.klaytn.caver.transaction.type.ValueTransferMemo;
import com.klaytn.caver.wallet.keyring.Keyring;
import com.klaytn.caver.wallet.keyring.PrivateKey;
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
        CancelTest.createInstance.class,
        CancelTest.createInstanceBuilder.class,
        CancelTest.getRLPEncodingTest.class,
        CancelTest.signWithKeyTest.class,
        CancelTest.signWithKeysTest.class,
        CancelTest.appendSignaturesTest.class,
        CancelTest.combineSignatureTest.class,
        CancelTest.getRawTransactionTest.class,
        CancelTest.getTransactionHashTest.class,
        CancelTest.getSenderTxHashTest.class,
        CancelTest.getRLPEncodingForSignatureTest.class,
})
public class CancelTest {

    static Caver caver = Caver.build(Caver.DEFAULT_URL);

    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";

    static KlaySignatureData klaySignatureData = new KlaySignatureData(
            Numeric.hexStringToByteArray("0x25"),
            Numeric.hexStringToByteArray("0xfb2c3d53d2f6b7bb1deb5a09f80366a5a45429cc1e3956687b075a9dcad20434"),
            Numeric.hexStringToByteArray("0x5c6187822ee23b1001e9613d29a5d6002f990498d2902904f7f259ab3358216e")
    );

    static String expectedRLPEncoding = "0x38f8648204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0fb2c3d53d2f6b7bb1deb5a09f80366a5a45429cc1e3956687b075a9dcad20434a05c6187822ee23b1001e9613d29a5d6002f990498d2902904f7f259ab3358216e";
    static String expectedTransactionHash = "0x10d135d590cb587cc45c1f94f4a0e3b8c24d24a6e4243f09ca395fb4e2450413";
    static String expectedRLPEncodingForSigning = "0xe39fde388204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b018080";

    public static Keyring generateRoleBaseKeyring(int[] numArr, String address) {
        String[][] keyArr = new String[3][];

        for(int i=0; i<numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j=0; j<length; j++) {
                arr[j] = PrivateKey.generate("entropy").getPrivateKey();
            }
            keyArr[i] = arr;
        }

        List<String[]> arr = Arrays.asList(keyArr);

        return Keyring.createWithRoleBasedKey(address, arr);
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
                    .setSignList(klaySignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            Cancel txObj = new Cancel.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setFrom(from)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());        }

        @Test
        public void BuilderTestWithBigInteger() {
            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setSignList(klaySignatureData)
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
                    .setSignList(klaySignatureData)
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
                    .setSignList(klaySignatureData)
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
                    .setSignList(klaySignatureData)
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
                    .setSignList(klaySignatureData)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            Cancel txObj = new Cancel(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            Cancel txObj = new Cancel(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            Cancel txObj = new Cancel(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            Cancel txObj = new Cancel(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            Cancel txObj = new Cancel(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            Cancel txObj = new Cancel.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            Cancel txObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void signWithKey_Keyring() throws IOException{
            mTxObj.signWithKey(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            mTxObj.signWithKey(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            mTxObj.signWithKey(coupledKeyring, 0);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            mTxObj.signWithKey(coupledKeyring);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString() throws IOException {
            mTxObj.signWithKey(privateKey, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            mTxObj.signWithKey(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoSigner() throws IOException {
            mTxObj.signWithKey(privateKey, 0);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_Only() throws IOException {
            mTxObj.signWithKey(privateKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KlayWalletKey() throws IOException {
            mTxObj.signWithKey(klaytnWalletKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.signWithKey(deCoupledKeyring);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");

            Keyring role = generateRoleBaseKeyring(new int[]{3,3,3}, from);
            mTxObj.signWithKey(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            mTxObj.signWithKeys(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            mTxObj.signWithKeys(coupledKeyring);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            mTxObj.signWithKeys(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            mTxObj.signWithKeys(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.signWithKeys(deCoupledKeyring);
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            Keyring roleBased = generateRoleBaseKeyring(new int[]{3,3,3}, from);

            mTxObj.signWithKeys(roleBased);
            assertEquals(3, mTxObj.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = coupledKeyring.getKlaytnWalletKey();
        }

        @Test
        public void appendSignature() {
            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<KlaySignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            KlaySignatureData emptySignature = KlaySignatureData.getEmptySignature();

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(emptySignature)
                    .build();

            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<KlaySignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignature_ExistedSignature() {
            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(signatureData)
                    .build();

            KlaySignatureData signatureData1 = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<KlaySignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj.appendSignatures(list);
            assertEquals(2, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
        }

        @Test
        public void appendSignatureList_ExistedSignature() {
            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(signatureData)
                    .build();

            KlaySignatureData signatureData1 = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            KlaySignatureData signatureData2 = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x9a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0xa3ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<KlaySignatureData> list = new ArrayList<>();
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

            KlaySignatureData expectedSignature = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                    Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
            );

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .build();

            String rlpEncoded = "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa00382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8a059d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae";
            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncoded));

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            String expectedRLPEncoded = "0x38f8f7018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f8d5f845820feaa00382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8a059d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9aef845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700f845820feaa0dccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912a07572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd";

            KlaySignatureData[] expectedSignature = new KlaySignatureData[] {
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                            Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x5a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3c"),
                            Numeric.hexStringToByteArray("0x0934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0xdccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912"),
                            Numeric.hexStringToByteArray("0x7572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd")
                    )
            };

            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x0382dcd275a9657d8fc3c4dc1509ad975f083184e3d34779dc6bef10e0e973c8"),
                    Numeric.hexStringToByteArray("0x59d5deb0f4c06a35a8024506159864ffc46dd08d91d5ac16fa69e92fb2d6b9ae")
            );

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(signatureData)
                    .build();

            String[] rlpEncodedString = new String[] {
                    "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700",
                    "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa0dccd060bd76582d221f6fe7e02e70877a25b65d80fed13b69b5c79d7c4520912a07572c5c68daf7094a17105eb6e5fed1b102bfe4ca737d62b51f921f7663fb2bd"
            };

            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncodedString));
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

            mTxObj = new Cancel.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .build();

            String rlpEncoded = "0x38f869018505d21dba00830dbba094504a835246e030d70ded9027f9f5a0aefcd45143f847f845820feaa05a3a7910ce495e316da1394f197cdadd95dbb6954d803052b9f62ce993c0ec3ca00934f8dda9666d759e511a5658de1db36faefb35e76a5e237d87ba8c3b9bb700";

            mTxObj.combineSignatures(Arrays.asList(rlpEncoded));
        }
    }

    public static class getRawTransactionTest {
        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
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
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
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

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
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

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        Cancel mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
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

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = new Cancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
