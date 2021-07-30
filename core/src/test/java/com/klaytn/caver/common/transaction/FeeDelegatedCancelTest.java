package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedCancel;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
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
public class FeeDelegatedCancelTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String senderPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";

    static String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
    static String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

    static String nonce = "0x4d2";
    static String gasPrice = "0x19";
    static String gas = "0xf4240";
    static String chainID = "0x1";

    static SignatureData senderSignatureData = new SignatureData(
            "0x26",
            "0x8409f5441d4725f90905ad87f03793857d124de7a43169bc67320cd2f020efa9",
            "0x60af63e87bdc565d7f7de906916b2334336ee7b24d9a71c9521a67df02e7ec92"
    );

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x26",
            "0x44d5b25e8c649a1fdaa409dc3817be390ad90a17c25bc17c89b6d5d248495e0",
            "0x73938e690d27b5267c73108352cf12d01de7fd0077b388e94721aa1fa32f85ec"
    );

    static String expectedRLPEncoding = "0x39f8c08204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84326a08409f5441d4725f90905ad87f03793857d124de7a43169bc67320cd2f020efa9a060af63e87bdc565d7f7de906916b2334336ee7b24d9a71c9521a67df02e7ec92945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0044d5b25e8c649a1fdaa409dc3817be390ad90a17c25bc17c89b6d5d248495e0a073938e690d27b5267c73108352cf12d01de7fd0077b388e94721aa1fa32f85ec";
    static String expectedTransactionHash = "0x96b39d3ab849127d31a5f7b5c882ca9ba408cd9d875052640d51a64f8c4acbb2";
    static String expectedSenderTransactionHash = "0xcc6c2673398903b3d906a3023b41636fc08bd1bddd5aa1602116091638f48447";
    static String expectedRLPEncodingForFeePayerSigning = "0xf8389fde398204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b945a0043070275d9f6054307ee7348bd660849d90f018080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedCancel.toString(), txObj.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setFrom(from)
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
            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
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

            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
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

            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
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

            FeeDelegatedCancel txObj = new FeeDelegatedCancel.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
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

            FeeDelegatedSmartContractExecution txObj = new FeeDelegatedSmartContractExecution.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
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
            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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
            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            String nonce = null;

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

            String gasPrice = null;

            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

        FeeDelegatedCancel txObj;
        SingleKeyring keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();

        @Before
        public void before() {

            txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            String privateKey = caver.wallet.keyring.generateSingleKey();
            String feePayer = new PrivateKey(privateKey).getDerivedAddress();

            txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
            );

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

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(feePayer, Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
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

        FeeDelegatedCancel mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
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

        FeeDelegatedCancel mTxObj;

        @Before
        public void before() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
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

        String from = "0xdcad313f2bf2240dbdb243eaf5eee2f512e0bfd1";
        String gas = "0xdbba0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        FeeDelegatedCancel mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
            );

            String rlpEncoded = "0x39f883018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f847f845820fe9a04d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1a07123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f940000000000000000000000000000000000000000c4c3018080";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x4d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1",
                    "0x7123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_MultipleSignature() {
            SignatureData senderSignature = new SignatureData(
                    "0x0fe9",
                    "0x4d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1",
                    "0x7123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f"
            );

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
            );

            String[] rlpEncodedStrings = {
                    "0x39f86f018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f847f845820fe9a0205d4f6f758629da5eb25d1d572e82430243e00096ed64097b6d0031847bf792a0280ce8a79438c699fce0417403e8892e46e10da764b16876091ef0965c1ce1df80c4c3018080",
                    "0x39f86f018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f847f845820feaa02f3c7b7aebd6c9af7a5b4259f0ea77d96362efbdca397b9f17e3c6924296c53fa00e4197ba6e38cecf99715f523c1805a58559072f944443bad1152dee73bfb16780c4c3018080",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x39f90111018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f8d5f845820fe9a04d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1a07123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606ff845820fe9a0205d4f6f758629da5eb25d1d572e82430243e00096ed64097b6d0031847bf792a0280ce8a79438c699fce0417403e8892e46e10da764b16876091ef0965c1ce1dff845820feaa02f3c7b7aebd6c9af7a5b4259f0ea77d96362efbdca397b9f17e3c6924296c53fa00e4197ba6e38cecf99715f523c1805a58559072f944443bad1152dee73bfb167940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x4d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1",
                            "0x7123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x205d4f6f758629da5eb25d1d572e82430243e00096ed64097b6d0031847bf792",
                            "0x280ce8a79438c699fce0417403e8892e46e10da764b16876091ef0965c1ce1df"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x2f3c7b7aebd6c9af7a5b4259f0ea77d96362efbdca397b9f17e3c6924296c53f",
                            "0x0e4197ba6e38cecf99715f523c1805a58559072f944443bad1152dee73bfb167"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
            );

            String rlpEncoded = "0x39f883018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1c4c3018080946f89ec285c52a3e092cdb017e125a9b197e78dc7f847f845820fe9a0a4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43a029e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0xa4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43",
                    "0x29e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0x6f89ec285c52a3e092cdb017e125a9b197e78dc7";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fe9",
                    "0xa4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43",
                    "0x29e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028"
            );

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String[] rlpEncodedStrings = new String[]{
                    "0x39f883018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1c4c3018080946f89ec285c52a3e092cdb017e125a9b197e78dc7f847f845820feaa09c86edd1b5d75ac1050a5a7494dece5f186b8e9654f75cf4942f7dca57fc2de0a032f306028776389107c40f1765679b2630f093b1c4f4fda9415f0c909c7addef",
                    "0x39f883018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1c4c3018080946f89ec285c52a3e092cdb017e125a9b197e78dc7f847f845820fe9a0392e7a5d2efbc7da9d114ce79797eebbe2007ece065109f7f93baed1e23bb22ca022e161a9f20c14b5830154e819cdaf59e8d82690b318afb19e2903b52020bb3e",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x39f90111018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1c4c3018080946f89ec285c52a3e092cdb017e125a9b197e78dc7f8d5f845820fe9a0a4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43a029e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028f845820feaa09c86edd1b5d75ac1050a5a7494dece5f186b8e9654f75cf4942f7dca57fc2de0a032f306028776389107c40f1765679b2630f093b1c4f4fda9415f0c909c7addeff845820fe9a0392e7a5d2efbc7da9d114ce79797eebbe2007ece065109f7f93baed1e23bb22ca022e161a9f20c14b5830154e819cdaf59e8d82690b318afb19e2903b52020bb3e";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xa4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43",
                            "0x29e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x9c86edd1b5d75ac1050a5a7494dece5f186b8e9654f75cf4942f7dca57fc2de0",
                            "0x32f306028776389107c40f1765679b2630f093b1c4f4fda9415f0c909c7addef"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x392e7a5d2efbc7da9d114ce79797eebbe2007ece065109f7f93baed1e23bb22c",
                            "0x22e161a9f20c14b5830154e819cdaf59e8d82690b318afb19e2903b52020bb3e"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
            );

            String rlpEncodedString = "0x39f8fd018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f8d5f845820fe9a04d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1a07123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606ff845820fe9a0205d4f6f758629da5eb25d1d572e82430243e00096ed64097b6d0031847bf792a0280ce8a79438c699fce0417403e8892e46e10da764b16876091ef0965c1ce1dff845820feaa02f3c7b7aebd6c9af7a5b4259f0ea77d96362efbdca397b9f17e3c6924296c53fa00e4197ba6e38cecf99715f523c1805a58559072f944443bad1152dee73bfb16780c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x4d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1",
                            "0x7123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x205d4f6f758629da5eb25d1d572e82430243e00096ed64097b6d0031847bf792",
                            "0x280ce8a79438c699fce0417403e8892e46e10da764b16876091ef0965c1ce1df"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x2f3c7b7aebd6c9af7a5b4259f0ea77d96362efbdca397b9f17e3c6924296c53f",
                            "0x0e4197ba6e38cecf99715f523c1805a58559072f944443bad1152dee73bfb167"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x39f90111018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1c4c3018080946f89ec285c52a3e092cdb017e125a9b197e78dc7f8d5f845820fe9a0a4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43a029e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028f845820feaa09c86edd1b5d75ac1050a5a7494dece5f186b8e9654f75cf4942f7dca57fc2de0a032f306028776389107c40f1765679b2630f093b1c4f4fda9415f0c909c7addeff845820fe9a0392e7a5d2efbc7da9d114ce79797eebbe2007ece065109f7f93baed1e23bb22ca022e161a9f20c14b5830154e819cdaf59e8d82690b318afb19e2903b52020bb3e";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0xa4ca740e08115db092a79ce902bdac45347a3d34a74ea0fcc371ccf01269ca43",
                            "0x29e095bf3f9e0be7e2130fe6985419114958877412b46b5b4243cc39380c5028"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x9c86edd1b5d75ac1050a5a7494dece5f186b8e9654f75cf4942f7dca57fc2de0",
                            "0x32f306028776389107c40f1765679b2630f093b1c4f4fda9415f0c909c7addef"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x392e7a5d2efbc7da9d114ce79797eebbe2007ece065109f7f93baed1e23bb22c",
                            "0x22e161a9f20c14b5830154e819cdaf59e8d82690b318afb19e2903b52020bb3e"
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()

                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
            );

            String rlpEncoded = "0x39f86f018505d21dba00830dbba094dcad313f2bf2240dbdb243eaf5eee2f512e0bfd1f847f845820fe9a04d9bf7a8bd15a41143eeecd3c39691cdc151b50d641534f0c73055849f7abca1a07123185b4cc046eb6a78e1ee370c059dfe437012098ebe18379685acd907606f80c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedCancel txObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedCancel mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

        FeeDelegatedCancel mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

        FeeDelegatedCancel mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
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

            mTxObj = caver.transaction.feeDelegatedCancel.create(
                    TxPropertyBuilder.feeDelegatedCancel()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setFeePayer(feePayer)
                            .setSignatures(senderSignatureData)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }

    public static class recoverPublicKeyTest {
        List<String> expectedPublicKeyList = Arrays.asList(
                "0xfbda4ac2c04336609f7e5a363c71c1565b442d552b82cbd0e75bbabaf215fd28b69ce88a6b9f2a463f1420bd9a0992413254748a7ab46d5ba78d09b35cf0e912",
                "0xa234bd09ea829cb39dd2f5aced2318039f30ce5fe28f5eb28a256bac8617eb5db57ac7683fa21a01c8cbd2ca31c2cf93c97871c73896bf051f9bc0885c87ebe2",
                "0x6ed39def6b25fc001790d267922281483c372b5d2486ae955ece1f1b64b19aea85392c8555947a1c63577439afdb74c77ef07d50520435d31cf4afb3dfe0074f"
        );

        List<String> expectedFeePayerPublicKeyList = Arrays.asList(
                "0x2b557d80ddac3a0bbcc8a7861773ca7434c969e2721a574bb94a1e3aa5ceed3819f08a82b31682c038f9f691fb38ee4aaf7e016e2c973a1bd1e48a51f60a54ea",
                "0x1a1cfe1e2ec4b15520c57c20c2460981a2f16003c8db11a0afc282abf929fa1c1868f60f91b330c423aa660913d86acc2a0b1b15e7ba1fe571e5928a19825a7e",
                "0xdea23a89dbbde1a0c26466c49c1edd32785432389641797038c2b53815cb5c73d6cf5355986fd9a22a68bb57b831857fd1636362b383bd632966392714b60d72"
        );

        List<SignatureData> expectedSigData = Arrays.asList(
                new SignatureData(
                        "0x0fe9",
                        "0x28ea52ee79b54fa321551689d4be9b932de1171ef239345953803c14576e10a1",
                        "0x11cb13643552c8a73d566a2e5dea8ed95c0173afef2842e3835fc67ba0bea411"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x6b8597441a8e1fe98a9f76bd75bafea8d0f0af2fcaadf5a8f31244e923fc5bd3",
                        "0x7e865b1b7858866f748ac288bf5547d31d3e92ee27024c1c0c5050f03ae470f9"
                ),
                new SignatureData(
                        "0x0fe9",
                        "0x1180d0ed68f60a9af4c65264dd6dc9d91cb63fa967d6c7ec7b8e79e556ba16ba",
                        "0x7039807b648fd82799637ac0190bd8caa3945d856cbe73a34ca549d9e2ec6f99"
                )
        );

        List<SignatureData> expectedFeePayerSigData = Arrays.asList(
                new SignatureData(
                        "0x0fe9",
                        "0x9a82ffdca6d654b9e65de5bab227bd4dd72c0c9e0e56e4035f2ce500b0eef297",
                        "0x0d28d0727d497e26f6356ad943b773b14860a66b8a8b45659692254073916715"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x36f0a2ae5dd31fb23d9e3c6991a39bf926cf4eabb268253e9e42ae909ef60ca6",
                        "0x283c87a0bb5a38de39c6cba969ea487c7c0df3f3af1873b7058e1c7aa3a1d725"
                ),
                new SignatureData(
                        "0x0fe9",
                        "0x2a3e055bd00aa863007b165d70749639fe26e15810dc4359c1e71bd9fe6a6b86",
                        "0x4a7cc2f1ba8ec721ed055cbc5dd8ebf58c9aa457c93e328ccfbdf826c8e4f094"
                )
        );

        FeeDelegatedCancel tx = new FeeDelegatedCancel.Builder()
                .setFrom("0xf21460730845e3652aa3cc9bc13b345e4f53984a")
                .setFeePayer("0xb5db72925b1b6b79299a1a49ae226cd7861083ac")
                .setChainId("0x7e3")
                .setGasPrice("0x5d21dba00")
                .setNonce("0x0")
                .setGas("0x2faf080")
                .setSignatures(expectedSigData)
                .setFeePayerSignatures(expectedFeePayerSigData)
                .build();

        @Test
        public void recoverPublicKey() {
            List<String> publicKeys = tx.recoverPublicKeys();
            assertEquals(expectedPublicKeyList, publicKeys);
        }

        @Test
        public void recoverFeePayerPublicKey() {
            List<String> publicKeys = tx.recoverFeePayerPublicKeys();
            assertEquals(expectedFeePayerPublicKeyList, publicKeys);
        }
    }
}
