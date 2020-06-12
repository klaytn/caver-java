package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.SmartContractExecution;
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
import static org.junit.Assert.assertEquals;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        SmartContractExecutionTest.createInstance.class,
        SmartContractExecutionTest.createInstanceBuilder.class,
        SmartContractExecutionTest.getRLPEncodingTest.class,
        SmartContractExecutionTest.signWithKeyTest.class,
        SmartContractExecutionTest.signWithKeysTest.class,
        SmartContractExecutionTest.appendSignaturesTest.class,
        SmartContractExecutionTest.combineSignatureTest.class,
        SmartContractExecutionTest.getRawTransactionTest.class,
        SmartContractExecutionTest.getTransactionHashTest.class,
        SmartContractExecutionTest.getSenderTxHashTest.class,
        SmartContractExecutionTest.getRLPEncodingForSignatureTest.class,
})
public class SmartContractExecutionTest {

    static Caver caver = Caver.build(Caver.DEFAULT_URL);

    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
    static String input = "0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2";
    static String gas = "0xf4240";
    static String gasPrice = "0x19";
    static String value = "0xa";
    static String nonce = "0x4d2";
    static String chainID = "0x1";

    static SignatureData signatureData = new SignatureData(
            Numeric.hexStringToByteArray("0x26"),
            Numeric.hexStringToByteArray("0xe4276df1a779274fbb04bc18a0184809eec1ce9770527cebb3d64f926dc1810b"),
            Numeric.hexStringToByteArray("0x4103b828a0671a48d64fe1a3879eae229699f05a684d9c5fd939015dcdd9709b")
    );

    static String expectedRlpEncodingForSigning = "0xf860b85bf859308204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2018080";
    static String expectedTransactionHash = "0x23bb192bd58d56527843eb63225c5213f3aded95e4c9776f1ff0bdd8ee0b6826";
    static String expectedRLPEncoding = "0x30f89f8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84326a0e4276df1a779274fbb04bc18a0184809eec1ce9770527cebb3d64f926dc1810ba04103b828a0671a48d64fe1a3879eae229699f05a684d9c5fd939015dcdd9709b";


    public static RoleBasedKeyring generateRoleBaseKeyring(int[] numArr, String address) {
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

        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());        }

        @Test
        public void BuilderTestWithBigInteger() {
            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            assertNotNull(txObj);

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

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            String to = null;

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }


        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            SmartContractExecution txObj = new SmartContractExecution(
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

            SmartContractExecution txObj = new SmartContractExecution(
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

            String from = null;

            SmartContractExecution txObj = new SmartContractExecution(
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
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            SmartContractExecution txObj = new SmartContractExecution(
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

            String to = null;

            SmartContractExecution txObj = new SmartContractExecution(
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
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            SmartContractExecution txObj = new SmartContractExecution(
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

            String value = null;

            SmartContractExecution txObj = new SmartContractExecution(
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
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            SmartContractExecution txObj = new SmartContractExecution(
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

            String gas = null;

            SmartContractExecution txObj = new SmartContractExecution(
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
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            SmartContractExecution txObj = new SmartContractExecution(
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
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            SmartContractExecution txObj = new SmartContractExecution(
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
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            SmartContractExecution txObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String input = "0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String value = "0xa";
        String nonce = "0x4d2";
        String chainID = "0x1";

        String expectedRLPEncoding = "0x30f89f8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84326a0e4276df1a779274fbb04bc18a0184809eec1ce9770527cebb3d64f926dc1810ba04103b828a0671a48d64fe1a3879eae229699f05a684d9c5fd939015dcdd9709b";

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;



        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setTo(to)
                    .setInput(input)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void signWithKey_Keyring() throws IOException{
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

            RoleBasedKeyring role = generateRoleBaseKeyring(new int[]{3,3,3}, from);
            mTxObj.sign(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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
            AbstractKeyring roleBased = generateRoleBaseKeyring(new int[]{3,3,3}, from);

            mTxObj.sign(roleBased);
            assertEquals(3, mTxObj.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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
            SignatureData emptySignature = signatureData.getEmptySignature();

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(emptySignature)
                    .build();

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

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

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

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
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

        final String input = "0xa9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039";
        String from = "0x2de587b91c76fb0b92fd607c4fbd5e9a17da799f";
        String to = "0xe3cd4e1cd287235cc0ea48c9fd02978533f5ec2b";
        String value = "0x0";
        String gas = "0xdbba0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";

        SmartContractExecution mTxObj;

        @Test
        public void combineSignature() {
            String expectedRLPEncoded = "0x30f90153018505d21dba00830dbba094e3cd4e1cd287235cc0ea48c9fd02978533f5ec2b80942de587b91c76fb0b92fd607c4fbd5e9a17da799fb844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f8d5f845820fe9a04a7d00c2680e5bca49a5880e23c5adb40b069af204a55e888f45746a20978e46a007b57a439201d182f4aec5db28d72192468f58f4fe7a1e717f96dd0d1def2d16f845820fe9a08494bfd86b0480e33700635d37ab0eb0ce3e6d93b5c51e6eda9fadd179569804a047f601d9fcb8682090165d8d048d6a5e3c5a48377ec9b212be6d7ee72b768bfdf845820fe9a0f642b38cf64cf70c89a0ccd74de13266ea98854078119a4619cad3bb2e6d4530a02307abe779333fe9da8eeebf40fbfeff9f1314ae8467a0119541339dfb65f10a";

            SignatureData[] expectedSignature = new SignatureData[] {
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x4a7d00c2680e5bca49a5880e23c5adb40b069af204a55e888f45746a20978e46"),
                            Numeric.hexStringToByteArray("0x07b57a439201d182f4aec5db28d72192468f58f4fe7a1e717f96dd0d1def2d16")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x8494bfd86b0480e33700635d37ab0eb0ce3e6d93b5c51e6eda9fadd179569804"),
                            Numeric.hexStringToByteArray("0x47f601d9fcb8682090165d8d048d6a5e3c5a48377ec9b212be6d7ee72b768bfd")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0xf642b38cf64cf70c89a0ccd74de13266ea98854078119a4619cad3bb2e6d4530"),
                            Numeric.hexStringToByteArray("0x2307abe779333fe9da8eeebf40fbfeff9f1314ae8467a0119541339dfb65f10a")
                    )
            };

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0x4a7d00c2680e5bca49a5880e23c5adb40b069af204a55e888f45746a20978e46"),
                    Numeric.hexStringToByteArray("0x07b57a439201d182f4aec5db28d72192468f58f4fe7a1e717f96dd0d1def2d16")
            );


            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            String[] rlpEncodedString = new String[] {
                    "0x30f8c5018505d21dba00830dbba094e3cd4e1cd287235cc0ea48c9fd02978533f5ec2b80942de587b91c76fb0b92fd607c4fbd5e9a17da799fb844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f847f845820fe9a08494bfd86b0480e33700635d37ab0eb0ce3e6d93b5c51e6eda9fadd179569804a047f601d9fcb8682090165d8d048d6a5e3c5a48377ec9b212be6d7ee72b768bfd",
                    "0x30f8c5018505d21dba00830dbba094e3cd4e1cd287235cc0ea48c9fd02978533f5ec2b80942de587b91c76fb0b92fd607c4fbd5e9a17da799fb844a9059cbb0000000000000000000000008a4c9c443bb0645df646a2d5bb55def0ed1e885a0000000000000000000000000000000000000000000000000000000000003039f847f845820fe9a0f642b38cf64cf70c89a0ccd74de13266ea98854078119a4619cad3bb2e6d4530a02307abe779333fe9da8eeebf40fbfeff9f1314ae8467a0119541339dfb65f10a"
            };
            List<String> list = Arrays.asList(rlpEncodedString);

            String combined = mTxObj.combineSignatures(list);
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            String value = "0x1000";

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            String rlpEncoded = "0x28f9027e018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a06f59d699a5dd22a653b0ed1e39cbfc52ee468607eec95b195f302680ed7f9815a03b2f3f2a7a9482edfbcc9ee8e003e284b6c4a7ecbc8d361cc486562d4bdda389";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignatures(list);
        }
    }

    public static class getRawTransactionTest {

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
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

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
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

            mTxObj = new SmartContractExecution.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
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

            mTxObj = new SmartContractExecution.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractExecution mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
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

            mTxObj = new SmartContractExecution.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();
            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new SmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setSignatures(signatureData)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
