package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedCancelWithRatio;
import com.klaytn.caver.wallet.keyring.*;
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
        FeeDelegatedCancelWithRatioTest.createInstance.class,
        FeeDelegatedCancelWithRatioTest.createInstanceBuilder.class,
        FeeDelegatedCancelWithRatioTest.getRLPEncodingTest.class,
        FeeDelegatedCancelWithRatioTest.signAsFeePayer_OneKeyTest.class,
        FeeDelegatedCancelWithRatioTest.signAsFeePayer_AllKeyTest.class,
        FeeDelegatedCancelWithRatioTest.appendFeePayerSignaturesTest.class,
        FeeDelegatedCancelWithRatioTest.combineSignatureTest.class,
        FeeDelegatedCancelWithRatioTest.getRawTransactionTest.class,
        FeeDelegatedCancelWithRatioTest.getTransactionHashTest.class,
        FeeDelegatedCancelWithRatioTest.getSenderTxHashTest.class,
        FeeDelegatedCancelWithRatioTest.getRLPEncodingForFeePayerSignatureTest.class,
})
public class FeeDelegatedCancelWithRatioTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String senderPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";

    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
    static String feeRatio = "0x1e";

    static String nonce = "0x4d2";
    static String gasPrice = "0x19";
    static String gas = "0xf4240";
    static String chainID = "0x1";

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0x72efa47960bef40b536c72d7e03ceaf6ca5f6061eb8a3eda3545b1a78fe52ef5",
            "0x62006ddaf874da205f08b3789e2d014ae37794890fc2e575bf75201563a24ba9"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0x6ba5ef20c3049323fc94defe14ca162e28b86aa64f7cf497ac8a5520e9615614",
            "0x4a0a0fc61c10b416759af0ce4ce5c09ca1060141d56d958af77050c9564df6bf"
    );

    static String expectedRLPEncoding = "0x3af8c18204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b1ef845f84326a072efa47960bef40b536c72d7e03ceaf6ca5f6061eb8a3eda3545b1a78fe52ef5a062006ddaf874da205f08b3789e2d014ae37794890fc2e575bf75201563a24ba9945a0043070275d9f6054307ee7348bd660849d90ff845f84326a06ba5ef20c3049323fc94defe14ca162e28b86aa64f7cf497ac8a5520e9615614a04a0a0fc61c10b416759af0ce4ce5c09ca1060141d56d958af77050c9564df6bf";
    static String expectedTransactionHash = "0x63604ebf68bfee51b2e3f54ddb2f19f9ea72d32b3fc70877324531ecda25817a";
    static String expectedSenderTransactionHash = "0xc0818be4cffbacfe29be1134e0267e10fd1afb6571f4ccc95dcc67a788bab5e7";
    static String expectedRLPEncodingForFeePayerSigning = "0xf839a0df3a8204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b1e945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = KeyringFactory.generateRoleBasedKeys(numArr, "entropy");
        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
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
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(gasPrice)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(Numeric.toBigInt(feeRatio))
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
            assertEquals(feeRatio, txObj.getFeeRatio());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            BigInteger feeRatio = BigInteger.valueOf(101);

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
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
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            String feeRatio = "0xFF";

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio txObj;
        SingleKeyring keyring = KeyringFactory.createWithSingleKey(feePayer, feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();

        @Before
        public void before() {

            txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String feePayer = new PrivateKey(privateKey).getDerivedAddress();

            txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

            txObj.signAsFeePayer(privateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
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
                    PrivateKey.generate().getPrivateKey(),
                    feePayerPrivateKey,
                    PrivateKey.generate().getPrivateKey()
            };

            MultipleKeyring keyring = KeyringFactory.createWithMultipleKey(feePayer, keyArr);
            txObj.signAsFeePayer(keyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_roleBasedKey() throws IOException {
            String[][] keyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),

                    },
                    {
                            PrivateKey.generate().getPrivateKey()
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            feePayerPrivateKey
                    }
            };

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(feePayer, Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = KeyringFactory.createWithSingleKey(feePayerPrivateKey, PrivateKey.generate().getPrivateKey());

            txObj.signAsFeePayer(keyring, 0);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3,3,3}, feePayer);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();

            singleKeyring = KeyringFactory.createWithSingleKey(feePayer, feePayerPrivateKey);
            multipleKeyring = KeyringFactory.createWithMultipleKey(feePayer, KeyringFactory.generateMultipleKeys(8));
            roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(feePayer, KeyringFactory.generateRolBasedKeys(new int[]{3,4,5}));
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

            SingleKeyring keyring = KeyringFactory.createFromPrivateKey(PrivateKey.generate().getPrivateKey());
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio mTxObj;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();
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

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(emptySignature)
                    .build();

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

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(signatureData)
                    .build();

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

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(signatureData)
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

        String from = "0x158a98f884e6f5a2731049569cb895cc1c75b47b";
        String gas = "0x249f0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        BigInteger feeRatio = BigInteger.valueOf(30);

        FeeDelegatedCancelWithRatio mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x3af870018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef847f845820fe9a0879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1a04b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c6175759080c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1",
                    "0x4b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0x879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1",
                    "0x4b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590"
            );

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .setSignatures(signatureData)
                    .build();

            String[] rlpEncodedStrings = {
                    "0x3af870018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef847f845820feaa00a5a7ad842672b62c26be2fae2644e9219bdf4baa2f7ea7745c74bab89fa1ff5a054ea57f591aea4d240da909e338b8df7c13a640d731eaaf785ca647c259066c580c4c3018080",
                    "0x3af870018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef847f845820fe9a0b871f4760b53fcba095b10979ae8b950e2692c1a526cd6f13c91401dde45d228a01aad1aa4f8efdfb9ab22cee80e0071ee3c6f5e8ad9b54ea79287b9f5631f30f180c4c3018080",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x3af8fe018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef8d5f845820fe9a0879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1a04b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590f845820feaa00a5a7ad842672b62c26be2fae2644e9219bdf4baa2f7ea7745c74bab89fa1ff5a054ea57f591aea4d240da909e338b8df7c13a640d731eaaf785ca647c259066c5f845820fe9a0b871f4760b53fcba095b10979ae8b950e2692c1a526cd6f13c91401dde45d228a01aad1aa4f8efdfb9ab22cee80e0071ee3c6f5e8ad9b54ea79287b9f5631f30f180c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1",
                            "0x4b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x0a5a7ad842672b62c26be2fae2644e9219bdf4baa2f7ea7745c74bab89fa1ff5",
                            "0x54ea57f591aea4d240da909e338b8df7c13a640d731eaaf785ca647c259066c5"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xb871f4760b53fcba095b10979ae8b950e2692c1a526cd6f13c91401dde45d228",
                            "0x1aad1aa4f8efdfb9ab22cee80e0071ee3c6f5e8ad9b54ea79287b9f5631f30f1"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x3af884018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ec4c301808094c01f48a99539a743256dc02dcfa9d0f5f075a5e4f847f845820feaa061eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727a055819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746d";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0x61eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727",
                    "0x55819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746d"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0xc01f48a99539a743256dc02dcfa9d0f5f075a5e4";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fea",
                    "0x61eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727",
                    "0x55819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746d"
            );

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String[] rlpEncodedStrings = new String[] {
                    "0x3af884018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ec4c301808094c01f48a99539a743256dc02dcfa9d0f5f075a5e4f847f845820fe9a0615be8124c6af821b6aec61b2021ebf7d677a38188c74d6324f21cd8ed3243dea0235142496683c0ff1352fe7f20bc83af7229b30be73ce895f040395ef5dfca66",
                    "0x3af884018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ec4c301808094c01f48a99539a743256dc02dcfa9d0f5f075a5e4f847f845820feaa0499e8cb92c800fc1437d64697c8c6c96a8455f30c654656a7ebf1b69f7aa8679a07f56c052fd2a8701705846d7313872afe85195087c06da4e3ed6c546eeb30259",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x3af90112018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ec4c301808094c01f48a99539a743256dc02dcfa9d0f5f075a5e4f8d5f845820feaa061eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727a055819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746df845820fe9a0615be8124c6af821b6aec61b2021ebf7d677a38188c74d6324f21cd8ed3243dea0235142496683c0ff1352fe7f20bc83af7229b30be73ce895f040395ef5dfca66f845820feaa0499e8cb92c800fc1437d64697c8c6c96a8455f30c654656a7ebf1b69f7aa8679a07f56c052fd2a8701705846d7313872afe85195087c06da4e3ed6c546eeb30259";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x61eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727",
                            "0x55819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746d"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x615be8124c6af821b6aec61b2021ebf7d677a38188c74d6324f21cd8ed3243de",
                            "0x235142496683c0ff1352fe7f20bc83af7229b30be73ce895f040395ef5dfca66"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x499e8cb92c800fc1437d64697c8c6c96a8455f30c654656a7ebf1b69f7aa8679",
                            "0x7f56c052fd2a8701705846d7313872afe85195087c06da4e3ed6c546eeb30259"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncodedString = "0x3af8fe018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef8d5f845820fe9a0879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1a04b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590f845820feaa00a5a7ad842672b62c26be2fae2644e9219bdf4baa2f7ea7745c74bab89fa1ff5a054ea57f591aea4d240da909e338b8df7c13a640d731eaaf785ca647c259066c5f845820fe9a0b871f4760b53fcba095b10979ae8b950e2692c1a526cd6f13c91401dde45d228a01aad1aa4f8efdfb9ab22cee80e0071ee3c6f5e8ad9b54ea79287b9f5631f30f180c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1",
                            "0x4b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c61757590"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x0a5a7ad842672b62c26be2fae2644e9219bdf4baa2f7ea7745c74bab89fa1ff5",
                            "0x54ea57f591aea4d240da909e338b8df7c13a640d731eaaf785ca647c259066c5"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xb871f4760b53fcba095b10979ae8b950e2692c1a526cd6f13c91401dde45d228",
                            "0x1aad1aa4f8efdfb9ab22cee80e0071ee3c6f5e8ad9b54ea79287b9f5631f30f1"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x3af90112018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ec4c301808094c01f48a99539a743256dc02dcfa9d0f5f075a5e4f8d5f845820feaa061eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727a055819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746df845820fe9a0615be8124c6af821b6aec61b2021ebf7d677a38188c74d6324f21cd8ed3243dea0235142496683c0ff1352fe7f20bc83af7229b30be73ce895f040395ef5dfca66f845820feaa0499e8cb92c800fc1437d64697c8c6c96a8455f30c654656a7ebf1b69f7aa8679a07f56c052fd2a8701705846d7313872afe85195087c06da4e3ed6c546eeb30259";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fea",
                            "0x61eba44b0175713e33867e2dde40aa8f73c67ecd50cf682dd879653a3e773727",
                            "0x55819bfb65b0a74de90e345fb6a5055872a370bf3cbead2d7bd5e43837bf746d"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x615be8124c6af821b6aec61b2021ebf7d677a38188c74d6324f21cd8ed3243de",
                            "0x235142496683c0ff1352fe7f20bc83af7229b30be73ce895f040395ef5dfca66"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x499e8cb92c800fc1437d64697c8c6c96a8455f30c654656a7ebf1b69f7aa8679",
                            "0x7f56c052fd2a8701705846d7313872afe85195087c06da4e3ed6c546eeb30259"
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

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x3af870018505d21dba00830249f094158a98f884e6f5a2731049569cb895cc1c75b47b1ef847f845820fe9a0879126759f424c790069e47d443c44674f4c2154d1e6f4f02134dbc56a6629f1a04b714b50c900b0b099b3e3ba15743654e8c576aa4fe504da38015f4c6175759080c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedCancelWithRatio txObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedTransactionHash, mTxObj.getTransactionHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedSenderTransactionHash, mTxObj.getSenderTxHash());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancelWithRatio mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncodingForFeePayerSigning, mTxObj.getRLPEncodingForFeePayerSignature());
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = new FeeDelegatedCancelWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
