package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.Cancel;
import com.klaytn.caver.transaction.type.ChainDataAnchoring;
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
        ChainDataAnchoringTest.createInstance.class,
        ChainDataAnchoringTest.createInstanceBuilder.class,
        ChainDataAnchoringTest.getRLPEncodingTest.class,
        ChainDataAnchoringTest.signWithKeyTest.class,
        ChainDataAnchoringTest.signWithKeysTest.class,
        ChainDataAnchoringTest.appendSignaturesTest.class,
        ChainDataAnchoringTest.combineSignatureTest.class,
        ChainDataAnchoringTest.getRawTransactionTest.class,
        ChainDataAnchoringTest.getTransactionHashTest.class,
        ChainDataAnchoringTest.getSenderTxHashTest.class,
        ChainDataAnchoringTest.getRLPEncodingForSignatureTest.class,
})
public class ChainDataAnchoringTest {

    static Caver caver = Caver.build(Caver.DEFAULT_URL);

    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String nonce = "0x4d2";
    static String chainID = "0x1";
    static String input = "0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405";

    static KlaySignatureData klaySignatureData = new KlaySignatureData(
            Numeric.hexStringToByteArray("0x25"),
            Numeric.hexStringToByteArray("0xe58b9abf9f33a066b998fccaca711553fb4df425c9234bbb3577f9d9775bb124"),
            Numeric.hexStringToByteArray("0x2c409a6c5d92277c0a812dd0cc553d7fe1d652a807274c3786df3292cd473e09")
    );

    static String expectedRLPEncoding = "0x48f9010e8204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f845f84325a0e58b9abf9f33a066b998fccaca711553fb4df425c9234bbb3577f9d9775bb124a02c409a6c5d92277c0a812dd0cc553d7fe1d652a807274c3786df3292cd473e09";
    static String expectedTransactionHash = "0x4aad85735e777795d24aa3eab51be959d8ebdf9683083d85b66f70b7170f2ea3";
    static String expectedRLPEncodingForSigning = "0xf8cfb8caf8c8488204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405018080";

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
            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());        }

        @Test
        public void BuilderTestWithBigInteger() {
            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            assertNotNull(txObj);
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    input
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            ChainDataAnchoring txObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

        String from = "0xb605c7550ad5fb15ddd9291a2d31a889db808152";
        String gas = "0xf4240";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";

        ChainDataAnchoring mTxObj;

        @Test
        public void combineSignature() {
            KlaySignatureData expectedSignature = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x91e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00ed"),
                    Numeric.hexStringToByteArray("0x08c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10c")
            );

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .build();

            String rlpEncoded = "0x48f90113018505d21dba00830f424094b605c7550ad5fb15ddd9291a2d31a889db808152b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f847f845820feaa091e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00eda008c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10c";
            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncoded));

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            String expectedRLPEncoded = "0x48f901a1018505d21dba00830f424094b605c7550ad5fb15ddd9291a2d31a889db808152b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f8d5f845820feaa091e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00eda008c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10cf845820feaa0c17c5ad8820b984da2bc816f881e1e283a9d7806ed5e3c703f58a7ed1f40edf1a049c4aa23508715aba0891ddad59bab4ff6abde777adffc1f39c79e51a78b786af845820fe9a0d2779b46862d5d10cb31d08ad5907eccf6343148e4264c730e048bb859cf1456a052570001d11eee29ee96c9f530be948a5f270167895705454596f6e61680718c";

            KlaySignatureData[] expectedSignature = new KlaySignatureData[] {
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x91e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00ed"),
                            Numeric.hexStringToByteArray("0x08c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10c")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0xc17c5ad8820b984da2bc816f881e1e283a9d7806ed5e3c703f58a7ed1f40edf1"),
                            Numeric.hexStringToByteArray("0x49c4aa23508715aba0891ddad59bab4ff6abde777adffc1f39c79e51a78b786a")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0xd2779b46862d5d10cb31d08ad5907eccf6343148e4264c730e048bb859cf1456"),
                            Numeric.hexStringToByteArray("0x52570001d11eee29ee96c9f530be948a5f270167895705454596f6e61680718c")
                    )
            };


            KlaySignatureData signature = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x91e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00ed"),
                    Numeric.hexStringToByteArray("0x08c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10c")
            );

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(signature)
                    .build();

            String[] rlpEncodedString = new String[] {
                    "0x48f90113018505d21dba00830f424094b605c7550ad5fb15ddd9291a2d31a889db808152b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f847f845820feaa0c17c5ad8820b984da2bc816f881e1e283a9d7806ed5e3c703f58a7ed1f40edf1a049c4aa23508715aba0891ddad59bab4ff6abde777adffc1f39c79e51a78b786a",
                    "0x48f90113018505d21dba00830f424094b605c7550ad5fb15ddd9291a2d31a889db808152b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f847f845820fe9a0d2779b46862d5d10cb31d08ad5907eccf6343148e4264c730e048bb859cf1456a052570001d11eee29ee96c9f530be948a5f270167895705454596f6e61680718c"
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

            String nonce = "0x1234";

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .build();

            String rlpEncoded = "0x48f901a1018505d21dba00830f424094b605c7550ad5fb15ddd9291a2d31a889db808152b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f8d5f845820feaa091e77e86e76dc7f1edb1ef1c87fd4bcba1fd95cbc659db407e1f358ae0cc00eda008c2fc7ec8ee14e734701435d0ca2e001bc1e0742c0fe0d58bd131a582e4f10cf845820feaa0c17c5ad8820b984da2bc816f881e1e283a9d7806ed5e3c703f58a7ed1f40edf1a049c4aa23508715aba0891ddad59bab4ff6abde777adffc1f39c79e51a78b786af845820fe9a0d2779b46862d5d10cb31d08ad5907eccf6343148e4264c730e048bb859cf1456a052570001d11eee29ee96c9f530be948a5f270167895705454596f6e61680718c";

            mTxObj.combineSignatures(Arrays.asList(rlpEncoded));
        }
    }

    public static class getRawTransactionTest {
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        ChainDataAnchoring mTxObj;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ChainDataAnchoring mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
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

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = new ChainDataAnchoring.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setFrom(from)
                    .setChainId(chainID)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
