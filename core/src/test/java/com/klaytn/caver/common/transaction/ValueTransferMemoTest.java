package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.ValueTransfer;
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
        ValueTransferMemoTest.createInstance.class,
        ValueTransferMemoTest.createInstanceBuilder.class,
        ValueTransferMemoTest.getRLPEncodingTest.class,
        ValueTransferMemoTest.signWithKeyTest.class,
        ValueTransferMemoTest.signWithKeysTest.class,
        ValueTransferMemoTest.appendSignaturesTest.class,
        ValueTransferMemoTest.combineSignatureTest.class,
        ValueTransferMemoTest.getRawTransactionTest.class,
        ValueTransferMemoTest.getTransactionHashTest.class,
        ValueTransferMemoTest.getSenderTxHashTest.class,
        ValueTransferMemoTest.getRLPEncodingForSignatureTest.class,
})
public class ValueTransferMemoTest {

    static Caver caver = Caver.build(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

    static String nonce = "0x4D2";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String chainID = "0x1";
    static String value = "0xa";
    static String input = "0x68656c6c6f";

    static KlaySignatureData klaySignatureData = new KlaySignatureData(
            Numeric.hexStringToByteArray("0x25"),
            Numeric.hexStringToByteArray("0x7d2b0c89ee8afa502b3186413983bfe9a31c5776f4f820210cffe44a7d568d1c"),
            Numeric.hexStringToByteArray("0x2b1cbd587c73b0f54969f6b76ef2fd95cea0c1bb79256a75df9da696278509f3")
    );

    static String expectedRlpEncodingForSigning = "0xf841b83cf83a108204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6f018080";
    static String expectedSenderTxHash = "0x6c7ee543c24e5b928b638a9f4502c1eca69103f5467ed4b6a2ed0ea5aede2e6b";
    static String expectedTransactionHash = "0x6c7ee543c24e5b928b638a9f4502c1eca69103f5467ed4b6a2ed0ea5aede2e6b";
    static String expectedRLPEncoding = "0x10f8808204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6ff845f84325a07d2b0c89ee8afa502b3186413983bfe9a31c5776f4f820210cffe44a7d568d1ca02b1cbd587c73b0f54969f6b76ef2fd95cea0c1bb79256a75df9da696278509f3";

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
            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
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

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(null)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(null)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value.");

            String value = "invalid value";

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue((String)null)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas((String)null)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input.");

            String input = "invalid input";

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(null)
                    .build();

            assertNotNull(txObj);
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
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

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    null,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
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

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    null,
                    value,
                    input
            );
        }



        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value.");

            String value = "invalid value";

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    null,
                    input
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    null,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input.");

            String input = "invalid input";

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    input
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            ValueTransferMemo txObj = new ValueTransferMemo(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value,
                    null
            );
            assertNotNull(txObj);
        }

    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            ValueTransferMemo txObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
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

        String from = "0x7d0104ac150f749d36bb34999bcade9f2c0bd2e6";
        String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
        String nonce = "0x3a";
        String gasPrice = "0x5d21dba00";
        BigInteger gas = BigInteger.valueOf(90000);
        BigInteger chainId = BigInteger.valueOf(2019);
        BigInteger value = BigInteger.ONE;
        String input = "0x68656c6c6f";

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0x2aea3bb7c0632f1991b0b0b7a51cd6537a35554b74c198ebd79069c72a591832"),
                    Numeric.hexStringToByteArray("0x617d2942861f2c4280e793f2bdb107751e88c43048983823110eb044d7572254")
            );

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(signatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void combineSignature() {
            KlaySignatureData expectedSignature = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0x2aea3bb7c0632f1991b0b0b7a51cd6537a35554b74c198ebd79069c72a591832"),
                    Numeric.hexStringToByteArray("0x617d2942861f2c4280e793f2bdb107751e88c43048983823110eb044d7572254")
            );

            String rlpEncoded = "0x10f8853a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e68568656c6c6ff847f845820fe9a02aea3bb7c0632f1991b0b0b7a51cd6537a35554b74c198ebd79069c72a591832a0617d2942861f2c4280e793f2bdb107751e88c43048983823110eb044d7572254";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mTxObj.combineSignatures(list);

            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            String expectedRLPEncoded = "0x10f901133a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e68568656c6c6ff8d5f845820fe9a02aea3bb7c0632f1991b0b0b7a51cd6537a35554b74c198ebd79069c72a591832a0617d2942861f2c4280e793f2bdb107751e88c43048983823110eb044d7572254f845820feaa0eda88095a7e349facbb40cc68c8c082aab3c21fbdbb05dca7fce6ab6c0a92866a03420efb785a186cda7f5bf99473bff57c18f9c4384126bec6f9172d6dcce2565f845820fe9a08d80151db0b7195adfef41443ddacd5ca57a6a479eb31fb0fea9f1c98596d4c9a079f37b400123c6a8415d8a851e8519102a02345feff6e2b3fb3b28699712e7e4";

            KlaySignatureData[] expectedSignature = new KlaySignatureData[] {
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x2aea3bb7c0632f1991b0b0b7a51cd6537a35554b74c198ebd79069c72a591832"),
                            Numeric.hexStringToByteArray("0x617d2942861f2c4280e793f2bdb107751e88c43048983823110eb044d7572254")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0xeda88095a7e349facbb40cc68c8c082aab3c21fbdbb05dca7fce6ab6c0a92866"),
                            Numeric.hexStringToByteArray("0x3420efb785a186cda7f5bf99473bff57c18f9c4384126bec6f9172d6dcce2565")
                    ),
                    new KlaySignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x8d80151db0b7195adfef41443ddacd5ca57a6a479eb31fb0fea9f1c98596d4c9"),
                            Numeric.hexStringToByteArray("0x79f37b400123c6a8415d8a851e8519102a02345feff6e2b3fb3b28699712e7e4")
                    )
            };

            String[] rlpEncodedString = new String[] {
                    "0x10f8853a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e68568656c6c6ff847f845820feaa0eda88095a7e349facbb40cc68c8c082aab3c21fbdbb05dca7fce6ab6c0a92866a03420efb785a186cda7f5bf99473bff57c18f9c4384126bec6f9172d6dcce2565",
                    "0x10f8853a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e68568656c6c6ff847f845820fe9a08d80151db0b7195adfef41443ddacd5ca57a6a479eb31fb0fea9f1c98596d4c9a079f37b400123c6a8415d8a851e8519102a02345feff6e2b3fb3b28699712e7e4"
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

            KlaySignatureData signatureData = new KlaySignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            String value = "0x1000";

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(signatureData)
                    .build();

            String rlpEncoded = "0x08f87f3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f847f845820feaa0c24227c8128652d4ec039950d9cfa82c3f962c4f4dee61e54236bdf89cbff8e9a04522134ef899ba136a668afd4ae76bd00bb19c0dc5ff66d7492a6a2a506021c2";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignatures(list);
        }
    }

    public static class getRawTransactionTest {

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

            mTxObj = new ValueTransferMemo.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

            mTxObj = new ValueTransferMemo.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        ValueTransferMemo mTxObj;
        Keyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            coupledKeyring = Keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }
        @Test
        public void getRLPEncodingForSignature() {
            String rlp = mTxObj.getRLPEncodingForSignature();
            assertEquals(expectedRlpEncodingForSigning, rlp);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new ValueTransferMemo.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new ValueTransferMemo.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignList(klaySignatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }
    }

}
