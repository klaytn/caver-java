package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.BeforeClass;
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
@Suite.SuiteClasses({
        LegacyTransactionTest.createInstance.class,
        LegacyTransactionTest.createInstanceBuilder.class,
        LegacyTransactionTest.signWithKeyTest.class,
        LegacyTransactionTest.signWithKeysTest.class,
        LegacyTransactionTest.getRLPEncodingTest.class,
        LegacyTransactionTest.combineSignatureTest.class,
        LegacyTransactionTest.getRawTransactionTest.class,
        LegacyTransactionTest.getTransactionHashTest.class,
        LegacyTransactionTest.getSenderTxHashTest.class,
        LegacyTransactionTest.getRLPEncodingForSignatureTest.class,
        LegacyTransactionTest.appendSignaturesTest.class
})
public class LegacyTransactionTest {

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String chainID = "0x1";
        String input = "0x31323334";
        String value = "0xa";

        @Test
        public void create() {
            AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(privateKey);

            LegacyTransaction legacyTransaction = new LegacyTransaction(
                    null,
                    keyring.getAddress(),
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    input,
                    value
            );
            assertNotNull(legacyTransaction);
        }

        @Test
        public void createWithRPC() throws IOException {
            String gas = "0x0f4240";
            String to = "7b65b75d204abed71587c9e519a89277766ee1d0";
            String input = "0x31323334";
            String value = "0x0a";

            Caver caver = Caver.build(Caver.DEFAULT_URL);
            AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(privateKey);

            LegacyTransaction legacyTransaction = new LegacyTransaction(
                    caver.klay(),
                    keyring.getAddress(),
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    input,
                    value
            );

            legacyTransaction.fillTransaction();
            assertNotNull(legacyTransaction.getNonce());
            assertNotNull(legacyTransaction.getGasPrice());
            assertNotNull(legacyTransaction.getChainId());
        }

        @Test
        public void throwException_missing_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing");

            LegacyTransaction legacyTransaction = new LegacyTransaction(
                    null,
                    "0x",
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    input,
                    null
            );
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid";

            LegacyTransaction legacyTransaction = new LegacyTransaction(
                    null,
                    "0x",
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    input,
                    value
            );
        }

        @Test
        public void throwException_invalid_To() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String gas = "0xf4240";
            String to = "invalid";
            String input = "0x31323334";
            String value = "0xa";

            LegacyTransaction legacyTransaction = new LegacyTransaction(
                    null,
                    "0x",
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    input,
                    value
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();
        }
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String chainID = "0x1";
        String input = "0x31323334";
        String value = "0xa";

        @Test
        public void BuilderTest() {
            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();

            assertNotNull(legacyTransaction);
        }

        @Test
        public void BuilderTestWithBigInteger() {

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setInput(input)
                    .setValue(Numeric.toBigInt(value))
                    .setTo(to)
                    .build();

            assertEquals(gas, legacyTransaction.getGas());
            assertEquals(gasPrice, legacyTransaction.getGasPrice());
            assertEquals(chainID, legacyTransaction.getChainId());
            assertEquals(value, legacyTransaction.getValue());
        }

        @Test
        public void BuilderTestRPC() throws IOException {
            String gas = "0x0f4240";
            String to = "7b65b75d204abed71587c9e519a89277766ee1d0";
            String input = "0x31323334";
            String value = "0x0a";

            Caver caver = Caver.build(Caver.DEFAULT_URL);
            AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(privateKey);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setInput(input)
                    .setValue(value)
                    .setFrom(keyring.getAddress()) // For Test. It automatically filled when executed LegacyTransaction.signWithKey or signWithKeysTest.
                    .setTo(to)
                    .build();

            legacyTransaction.fillTransaction();
            assertNotNull(legacyTransaction.getNonce());
            assertNotNull(legacyTransaction.getGasPrice());
            assertNotNull(legacyTransaction.getChainId());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setTo(to)
                    .setValue("0x")
                    .build();
        }

        @Test
        public void throwException_invalid_value2() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setTo(to)
                    .setValue("invalid")
                    .build();
        }

        @Test
        public void throwException_invalid_To() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String gas = "0xf4240";
            String to = "invalid";
            String input = "0x31323334";
            String value = "0xa";

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String nonce = "0x4D2";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String chainID = "0x1";
        static String input = "0x31323334";
        static String value = "0xa";

        static String expectedRawTransaction = "0xf8668204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a843132333425a0b2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9a029da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567";

        public static LegacyTransaction createLegacyTransaction() {
            return new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();
        }

        @BeforeClass
        public static void preSetup() {
            caver = Caver.build(Caver.DEFAULT_URL);
            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());
        }

        @Test
        public void signWithKey_Keyring() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractTransaction tx = legacyTransaction.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractTransaction tx = legacyTransaction.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractTransaction tx = legacyTransaction.sign(coupledKeyring, 0);
            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void singWithKey_Keyring_Only() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractTransaction tx = legacyTransaction.sign(coupledKeyring);
            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractTransaction tx = legacyTransaction.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void throwException_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("A legacy transaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_notEqualAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setInput(input)
                    .setValue(Numeric.toBigInt(value))
                    .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                    .setTo(to)
                    .build();

            legacyTransaction.sign(coupledKeyring);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;

        static AbstractKeyring coupledKeyring;
        static AbstractKeyring deCoupledKeyring;
        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        static String nonce = "0x4D2";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String chainID = "0x1";
        static String input = "0x31323334";
        static String value = "0xa";

        static String expectedRawTransaction = "0xf8668204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a843132333425a0b2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9a029da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567";

        public static LegacyTransaction createLegacyTransaction() {
            return new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();
        }

        @BeforeClass
        public static void preSetup() {
            caver = Caver.build(Caver.DEFAULT_URL);
            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());
        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(coupledKeyring, TransactionHasher::getHashForSignature);

            assertEquals(expectedRawTransaction, legacyTransaction.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(coupledKeyring);

            assertEquals(expectedRawTransaction, legacyTransaction.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(privateKey, TransactionHasher::getHashForSignature);

            assertEquals(expectedRawTransaction, legacyTransaction.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_KlaytnWalletKeyFormat() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            String klaytnKey = privateKey + "0x00" + KeyringFactory.createFromPrivateKey(privateKey).getAddress();
            AbstractTransaction tx = legacyTransaction.sign(klaytnKey, TransactionHasher::getHashForSignature);

            assertEquals(expectedRawTransaction, tx.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            LegacyTransaction legacyTransaction = createLegacyTransaction();

            legacyTransaction.sign(coupledKeyring);
            assertEquals(expectedRawTransaction, legacyTransaction.getRawTransaction());
        }

        @Test
        public void throwException_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("A legacy transaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_KlaytnWalletKeyFormat_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("A legacy transaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            String klaytnKey = privateKey + "0x00" + KeyringFactory.generate().getAddress();
            legacyTransaction.sign(klaytnKey);
        }

        @Test
        public void throwException_multipleKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("A legacy transaction cannot be signed with a decoupled keyring.");

            String[] privateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractKeyring keyring = KeyringFactory.createWithMultipleKey(PrivateKey.generate().getDerivedAddress(), privateKeyArr);
            legacyTransaction.sign(keyring);
        }

        @Test
        public void throwException_roleBasedKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("A legacy transaction cannot be signed with a decoupled keyring.");

            String[][] privateKeyArr = {
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    },
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    },
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    }
            };

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractKeyring keyring = KeyringFactory.createWithRoleBasedKey(PrivateKey.generate().getDerivedAddress(), Arrays.asList(privateKeyArr));
            legacyTransaction.sign(keyring);
        }

        @Test
        public void throwException_notEqualAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d1";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                    .setInput(input)
                    .setValue(value)
                    .setTo(to)
                    .build();

            AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(privateKey);
            legacyTransaction.sign(keyring);
        }
    }



    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            String nonce = "0x4D2";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setSignList(list)
                    .setTo(to)
                    .build();

            String expectedRLP = "0xf8668204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a843132333425a0b2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9a029da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567";


            assertEquals(expectedRLP, legacyTransaction.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String gas = "0xf4240";
            String gasPrice = "0x19";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setSignList(list)
                    .setTo(to)
                    .build();

            legacyTransaction.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x4D2";
            String gas = "0xf4240";
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String chainID = "0x1";
            String input = "0x31323334";
            String value = "0xa";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setSignList(list)
                    .setTo(to)
                    .build();

            legacyTransaction.getRLPEncoding();
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;


        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        @Test
        public void combineSignature() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .build();

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = legacyTransaction.combineSignatures(rlpList);

            assertEquals(rlpEncoded, combined);
        }

        @Test
        public void combineSignature_EmptySig() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData emptySig = SignatureData.getEmptySignature();

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .setSignList(emptySig)
                    .build();

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = legacyTransaction.combineSignatures(rlpList);

            assertEquals(rlpEncoded, combined);
        }

        @Test
        public void throwException_existSignature() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setSignList(list)
                    .setTo(to)
                    .build();


            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            legacyTransaction.combineSignatures(rlpList);
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3F";
            BigInteger chainID = BigInteger.valueOf(2019);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .build();

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            legacyTransaction.combineSignatures(rlpList);
        }
    }

    public static class getRawTransactionTest {

        @Test
        public void getRawTransaction() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setSignList(list)
                    .setTo(to)
                    .build();


            String expected = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            String raw = legacyTransaction.getRawTransaction();

            assertEquals(expected, raw);
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getTransactionHash() {
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getTransactionHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .build();

            legacyTransaction.getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getTransactionHash();

            assertEquals(expected, txHash);
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getSenderTxHash() {
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getSenderTxHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            legacyTransaction.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();


            String txHash = legacyTransaction.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncodingForSignature() {
            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String expected = "0xe68204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a8431323334018080";
            String rlpEncodedSign = legacyTransaction.getRLPEncodingForSignature();

            assertEquals(expected, rlpEncodedSign);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String encoded = legacyTransaction.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            BigInteger nonce = BigInteger.valueOf(1234);
            String chainID = "0x1";
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setChainId(chainID)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String encoded = legacyTransaction.getRLPEncodingForSignature();

        }

        @Test
        public void throwException_NotDefined_gasChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
            String value = "0xa";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            BigInteger nonce = BigInteger.valueOf(1234);
            String input = "0x31323334";

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x25"),
                    Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                    Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setValue(value)
                    .setInput(input)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            String encoded = legacyTransaction.getRLPEncodingForSignature();
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void appendSignature() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setTo(to)
                    .setValue(value)
                    .build();

            legacyTransaction.appendSignatures(signatureData);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureWithEmptySig() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData emptySignature = SignatureData.getEmptySignature();

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setTo(to)
                    .setValue(value)
                    .setSignList(emptySignature)
                    .build();

            legacyTransaction.appendSignatures(signatureData);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .build();

            legacyTransaction.appendSignatures(list);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData emptySignature = SignatureData.getEmptySignature();

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .setSignList(emptySignature)
                    .build();

            legacyTransaction.appendSignatures(list);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void throwException_appendData_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            legacyTransaction.appendSignatures(signatureData);
        }

        @Test
        public void throwException_appendList_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setTo(to)
                    .setSignList(list)
                    .build();

            legacyTransaction.appendSignatures(list);
        }

        @Test
        public void throwException_tooLongSignatures() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures are too long " + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);
            list.add(signatureData);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setTo(to)
                    .setValue(value)
                    .build();

            legacyTransaction.appendSignatures(list);
        }
    }
}
