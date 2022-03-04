/*
 * Copyright 2021 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Enclosed.class)
public class LegacyTransactionTest {

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String from = "0x";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String chainID = "0x1";
        String input = "0x31323334";
        String value = "0xa";

        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Test
        public void createInstance() throws IOException {
            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setFrom(keyring.getAddress())
                            .setNonce("0x")
                            .setGas(gas)
                            .setGasPrice("0x")
                            .setChainId("0x")
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
            );

            assertNotNull(legacyTransaction);

            legacyTransaction.fillTransaction();
            assertNotNull(legacyTransaction.getNonce());
            assertNotNull(legacyTransaction.getGasPrice());
            assertNotNull(legacyTransaction.getChainId());
            assertEquals(TransactionType.TxTypeLegacyTransaction.toString(), legacyTransaction.getType());
        }

        @Test
        public void throwException_invalid_value() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid";

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
            );
        }

        @Test
        public void throwException_invalid_To() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid";

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setInput(input)
                            .setValue(value)
            );
        }
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        static String nonce = "0x4D2";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String chainID = "0x1";
        static String input = "0x31323334";
        static String value = "0xa";

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
            assertEquals(TransactionType.TxTypeLegacyTransaction.toString(), legacyTransaction.getType());
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

            Caver caver = new Caver(Caver.DEFAULT_URL);
            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
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

        static Caver caver = new Caver(Caver.DEFAULT_URL);

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
            return caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setInput(input)
                            .setValue(value)
                            .setTo(to)
            );
        }

        @BeforeClass
        public static void preSetup() {
            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    caver.wallet.keyring.generateSingleKey()
            );
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
            expectedException.expectMessage("TxTypeLegacyTransaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_notEqualAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(Numeric.toBigInt(gas))
                            .setGasPrice(Numeric.toBigInt(gasPrice))
                            .setChainId(Numeric.toBigInt(chainID))
                            .setInput(input)
                            .setValue(Numeric.toBigInt(value))
                            .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                            .setTo(to)
            );

            legacyTransaction.sign(coupledKeyring);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

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
            return caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setInput(input)
                            .setValue(value)
                            .setTo(to)
            );
        }

        @BeforeClass
        public static void preSetup() {
            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    caver.wallet.keyring.generateSingleKey()
            );
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

            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.createFromPrivateKey(privateKey).getAddress();
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
            expectedException.expectMessage("TxTypeLegacyTransaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();
            legacyTransaction.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_KlaytnWalletKeyFormat_decoupledKey() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("TxTypeLegacyTransaction cannot be signed with a decoupled keyring.");

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            String klaytnKey = privateKey + "0x00" + caver.wallet.keyring.generate().getAddress();
            legacyTransaction.sign(klaytnKey);
        }

        @Test
        public void throwException_multipleKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("TxTypeLegacyTransaction cannot be signed with a decoupled keyring.");

            String[] privateKeyArr = {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey()
            };

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractKeyring keyring = caver.wallet.keyring.createWithMultipleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKeyArr
            );
            legacyTransaction.sign(keyring);
        }

        @Test
        public void throwException_roleBasedKeyring() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("TxTypeLegacyTransaction cannot be signed with a decoupled keyring.");

            String[][] privateKeyArr = {
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey()
                    }
            };

            LegacyTransaction legacyTransaction = createLegacyTransaction();

            AbstractKeyring keyring = caver.wallet.keyring.createWithRoleBasedKey(
                    caver.wallet.keyring.generate().getAddress(),
                    Arrays.asList(privateKeyArr)
            );
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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom("0x7b65b75d204abed71587c9e519a89277766aaaa1")
                            .setInput(input)
                            .setValue(value)
                            .setTo(to)
            );

            AbstractKeyring keyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            legacyTransaction.sign(keyring);
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
            );

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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
            );

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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setSignatures(list)
                            .setTo(to)
            );

            legacyTransaction.getRLPEncoding();
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

        static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        @Test
        public void combineSignature() {
            String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
            BigInteger value = BigInteger.ONE;
            BigInteger gas = BigInteger.valueOf(90000);
            String gasPrice = "0x5d21dba00";
            String nonce = "0x3a";
            BigInteger chainID = BigInteger.valueOf(2019);

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
            );

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = legacyTransaction.combineSignedRawTransactions(rlpList);

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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setSignatures(emptySig)
            );

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);
            String combined = legacyTransaction.combineSignedRawTransactions(rlpList);

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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setSignatures(list)
                            .setTo(to)
            );

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            legacyTransaction.combineSignedRawTransactions(rlpList);
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

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
            );

            String rlpEncoded = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";

            List<String> rlpList = new ArrayList<>();
            rlpList.add(rlpEncoded);

            legacyTransaction.combineSignedRawTransactions(rlpList);
        }
    }

    public static class getRawTransactionTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);

        static String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
        static BigInteger value = BigInteger.ONE;
        static BigInteger gas = BigInteger.valueOf(90000);
        static String gasPrice = "0x5d21dba00";
        static String nonce = "0x3a";
        static BigInteger chainID = BigInteger.valueOf(2019);

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fea"),
                Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
        );

        @Test
        public void getRawTransaction() {
            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setSignatures(signatureData)
                            .setTo(to)
            );

            String expected = "0xf8673a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae0180820feaa0ade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6ea038160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e";
            String raw = legacyTransaction.getRawTransaction();

            assertEquals(expected, raw);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined.");

            caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setSignatures(signatureData)
                            .setTo(to)
            ).getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined.");

            caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setSignatures(signatureData)
                            .setTo(to)
            ).getRawTransaction();
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String value = "0xa";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static BigInteger nonce = BigInteger.valueOf(1234);
        static String chainID = "0x1";
        static String input = "0x31323334";

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
        );

        @Test
        public void getTransactionHash() {
            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getTransactionHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            legacyTransaction.getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getTransactionHash();

            assertEquals(expected, txHash);
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String value = "0xa";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static BigInteger nonce = BigInteger.valueOf(1234);
        static String chainID = "0x1";
        static String input = "0x31323334";

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
        );

        @Test
        public void getSenderTxHash() {
            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String expected = "0xe434257753bf31a130c839fec0bd34fc6ea4aa256b825288ee82db31c2ed7524";
            String txHash = legacyTransaction.getSenderTxHash();

            assertEquals(expected, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            legacyTransaction.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );


            String txHash = legacyTransaction.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        static String value = "0xa";
        static String gas = "0xf4240";
        static String gasPrice = "0x19";
        static BigInteger nonce = BigInteger.valueOf(1234);
        static String chainID = "0x1";
        static String input = "0x31323334";

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9"),
                Numeric.hexStringToByteArray("0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567")
        );

        @Test
        public void getRLPEncodingForSignature() {
            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String expected = "0xe68204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a8431323334018080";
            String rlpEncodedSign = legacyTransaction.getRLPEncodingForSignature();

            assertEquals(expected, rlpEncodedSign);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String encoded = legacyTransaction.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String encoded = legacyTransaction.getRLPEncodingForSignature();

        }

        @Test
        public void throwException_NotDefined_gasChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setInput(input)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            String encoded = legacyTransaction.getRLPEncodingForSignature();
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
        static BigInteger value = BigInteger.ONE;
        static BigInteger gas = BigInteger.valueOf(90000);
        static String gasPrice = "0x19";
        static String nonce = "0x3a";
        static BigInteger chainID = BigInteger.valueOf(2019);

        static SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fea"),
                Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
        );

        @Test
        public void appendSignature() {
            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
            );

            legacyTransaction.appendSignatures(signatureData);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureWithEmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
                            .setSignatures(emptySignature)
            );

            legacyTransaction.appendSignatures(signatureData);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
            );

            legacyTransaction.appendSignatures(list);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            SignatureData emptySignature = SignatureData.getEmptySignature();

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setSignatures(emptySignature)
            );

            legacyTransaction.appendSignatures(list);

            assertEquals(signatureData, legacyTransaction.getSignatures().get(0));
        }

        @Test
        public void throwException_appendData_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setSignatures(signatureData)
            );

            legacyTransaction.appendSignatures(signatureData);
        }

        @Test
        public void throwException_appendList_existsSignatureInTransaction() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setTo(to)
                            .setSignatures(list)
            );

            legacyTransaction.appendSignatures(list);
        }

        @Test
        public void throwException_tooLongSignatures() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Signatures are too long " + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);
            list.add(signatureData);

            LegacyTransaction legacyTransaction = caver.transaction.legacyTransaction.create(
                    TxPropertyBuilder.legacyTransaction()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setTo(to)
                            .setValue(value)
            );

            legacyTransaction.appendSignatures(list);
        }
    }

    public static class decodeTest {
        @Test
        public void decodeTest() {
            String rawTx = "0xf868808505d21dba008402faf0809459177716c34ac6e49e295a0e78e33522f14d61ee0180820fe9a0ecdec357060dbbb4bd3790e98b1733ec3a0b02b7e4ec7a5622f93cd9bee229fea00a4a5e28753e7c1d999b286fb07933c5bf353079b8ed4d1ed509a838b48be02c";
            LegacyTransaction tx = LegacyTransaction.decode(rawTx);

            assertEquals(rawTx, tx.getRawTransaction());
        }
    }

    public static class recoverPublicKeyTest {
        @Test
        public void recoverPublicKey() {
            String expectedPublicKey = "0x8bb6aaeb2d96d024754d3b50babf116cece68977acbe8ba6a66f14d5217c60d96af020a0568661e7c72e753e80efe084a3aed9f9ac87bf44d09ce67aad3d4e01";
            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0xecdec357060dbbb4bd3790e98b1733ec3a0b02b7e4ec7a5622f93cd9bee229fe",
                    "0x0a4a5e28753e7c1d999b286fb07933c5bf353079b8ed4d1ed509a838b48be02c"
            );

            LegacyTransaction tx = new LegacyTransaction.Builder()
                    .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                    .setTo("0x59177716c34ac6e49e295a0e78e33522f14d61ee")
                    .setValue("0x1")
                    .setChainId("0x7e3")
                    .setGasPrice("0x5d21dba00")
                    .setNonce("0x0")
                    .setGas("0x2faf080")
                    .setSignatures(signatureData)
                    .build();

            List<String> publicKeys = tx.recoverPublicKeys();
            assertEquals(expectedPublicKey, publicKeys.get(0));
        }
    }
}
