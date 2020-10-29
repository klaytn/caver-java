package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
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

public class ValueTransferTest {

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
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

        Caver caver = new Caver(Caver.DEFAULT_URL);
        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String chainID = "0x1";
        String value = "0xa";

        @Test
        public void BuilderTest() {
            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            assertNotNull(valueTransfer);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException{
            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            valueTransfer.fillTransaction();

            assertFalse(valueTransfer.getNonce().isEmpty());
            assertFalse(valueTransfer.getGasPrice().isEmpty());
            assertFalse(valueTransfer.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setTo(to)
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .build();

            assertEquals(gas, valueTransfer.getGas());
            assertEquals(gasPrice, valueTransfer.getGasPrice());
            assertEquals(chainID, valueTransfer.getChainId());
            assertEquals(value, valueTransfer.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(null)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(null)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue((String)null)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas((String)null)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String nonce = "0x4D2";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String chainID = "0x1";
        String value = "0xa";

        @Test
        public void createInstance() {
            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );

            assertNotNull(valueTransfer);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    null,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String to = "invalid Address";

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

        @Test
        public void throwException_missingTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("to is missing.");

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    null,
                    value
            );
        }



        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    null
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            ValueTransfer valueTransfer = new ValueTransfer(
                    null,
                    from,
                    nonce,
                    null,
                    gasPrice,
                    chainID,
                    null,
                    to,
                    value
            );
        }

    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        @Test
        public void getRLPEncoding() {
            String expectedEncoded = "0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc";

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainId)
                    .setNonce(BigInteger.valueOf(nonce))
                    .setSignatures(signatureData)
                    .build();

            assertEquals(expectedEncoded, valueTransfer.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setFrom(from)
                    .setTo(to)
                    .setValue(value)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainId)
                    .setSignatures(signatureData)
                    .build();

            valueTransfer.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setFrom(from)
                    .setNonce(BigInteger.valueOf(nonce))
                    .setTo(to)
                    .setValue(value)
                    .setGas(gas)
                    .setChainId(chainId)
                    .setSignatures(signatureData)
                    .build();

            valueTransfer.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        ValueTransfer mValueTransfer;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        String expectedRawTx = "0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc";

        @Before
        public void before() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();

        }

        @Test
        public void signWithKey_Keyring() throws IOException{
            mValueTransfer.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            mValueTransfer.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            mValueTransfer.sign(coupledKeyring, 0);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            mValueTransfer.sign(coupledKeyring);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            mValueTransfer.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_Only() throws IOException {
            mValueTransfer.sign(privateKey);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKey_KlayWalletKey() throws IOException {
            mValueTransfer.sign(klaytnWalletKey);
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mValueTransfer.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring role = generateRoleBaseKeyring(new int[]{3,3,3}, from);
            mValueTransfer.sign(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;
        SignatureData signatureData = new SignatureData (
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        ValueTransfer mValueTransfer;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        String expectedRawTx = "0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc";

        @Before
        public void before() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();

        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            mValueTransfer.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(1, mValueTransfer.getSignatures().size());
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            mValueTransfer.sign(coupledKeyring);
            assertEquals(1, mValueTransfer.getSignatures().size());
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            mValueTransfer.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mValueTransfer.getSignatures().size());
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            mValueTransfer.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mValueTransfer.getSignatures().size());
            assertEquals(expectedRawTx, mValueTransfer.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mValueTransfer.sign(deCoupledKeyring);
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            AbstractKeyring roleBased = generateRoleBaseKeyring(new int[]{3,3,3}, from);

            mValueTransfer.sign(roleBased);
            assertEquals(3, mValueTransfer.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;

        ValueTransfer mValueTransfer;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        String expectedRawTx = "0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc";

        @Before
        public void before() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
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

            mValueTransfer.appendSignatures(signatureData);
            assertEquals(signatureData, mValueTransfer.getSignatures().get(0));
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

            mValueTransfer.appendSignatures(list);
            assertEquals(signatureData, mValueTransfer.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(emptySignature)
                    .build();

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mValueTransfer.appendSignatures(list);
            assertEquals(signatureData, mValueTransfer.getSignatures().get(0));
        }

        @Test
        public void appendSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mValueTransfer.appendSignatures(list);
            assertEquals(2, mValueTransfer.getSignatures().size());
            assertEquals(signatureData, mValueTransfer.getSignatures().get(0));
            assertEquals(signatureData1, mValueTransfer.getSignatures().get(1));
        }

        @Test
        public void appendSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
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

            mValueTransfer.appendSignatures(list);
            assertEquals(3, mValueTransfer.getSignatures().size());
            assertEquals(signatureData, mValueTransfer.getSignatures().get(0));
            assertEquals(signatureData1, mValueTransfer.getSignatures().get(1));
            assertEquals(signatureData2, mValueTransfer.getSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";

        String from = "0x7d0104ac150f749d36bb34999bcade9f2c0bd2e6";
        String to = "0x8723590d5D60e35f7cE0Db5C09D3938b26fF80Ae";
        String nonce = "0x3a";
        String gasPrice = "0x5d21dba00";
        BigInteger gas = BigInteger.valueOf(90000);
        BigInteger chainId = BigInteger.valueOf(2019);
        BigInteger value = BigInteger.ONE;

        ValueTransfer mValueTransfer;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void combineSignature() {
            SignatureData expectedSignature = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            String rlpEncoded = "0x08f87f3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f847f845820feaa03d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3a01f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mValueTransfer.combineSignedRawTransactions(list);

            assertEquals(expectedSignature, mValueTransfer.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            String expectedRLPEncoded = "0x08f9010d3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f8d5f845820feaa03d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3a01f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017f845820feaa0c24227c8128652d4ec039950d9cfa82c3f962c4f4dee61e54236bdf89cbff8e9a04522134ef899ba136a668afd4ae76bd00bb19c0dc5ff66d7492a6a2a506021c2f845820fe9a0c9845154419b26dcb7700b4856c38f6e272004654ac3f38e9663134863600c52a05671961420adee43ee4538cba0200e82ff3c939c81e7d6f977660546b06d6914";

            SignatureData[] expectedSignature = new SignatureData[] {
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                            Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0xc24227c8128652d4ec039950d9cfa82c3f962c4f4dee61e54236bdf89cbff8e9"),
                            Numeric.hexStringToByteArray("0x4522134ef899ba136a668afd4ae76bd00bb19c0dc5ff66d7492a6a2a506021c2")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0xc9845154419b26dcb7700b4856c38f6e272004654ac3f38e9663134863600c52"),
                            Numeric.hexStringToByteArray("0x5671961420adee43ee4538cba0200e82ff3c939c81e7d6f977660546b06d6914")
                    )
            };

            String[] rlpEncodedString = new String[] {
                    "0x08f87f3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f847f845820feaa0c24227c8128652d4ec039950d9cfa82c3f962c4f4dee61e54236bdf89cbff8e9a04522134ef899ba136a668afd4ae76bd00bb19c0dc5ff66d7492a6a2a506021c2",
                    "0x08f87f3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f847f845820fe9a0c9845154419b26dcb7700b4856c38f6e272004654ac3f38e9663134863600c52a05671961420adee43ee4538cba0200e82ff3c939c81e7d6f977660546b06d6914"
            };

            String combined = mValueTransfer.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mValueTransfer.getSignatures().get(0));
            assertEquals(expectedSignature[1], mValueTransfer.getSignatures().get(1));
            assertEquals(expectedSignature[2], mValueTransfer.getSignatures().get(2));
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

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            String rlpEncoded = "0x08f87f3a8505d21dba0083015f90948723590d5d60e35f7ce0db5c09d3938b26ff80ae01947d0104ac150f749d36bb34999bcade9f2c0bd2e6f847f845820feaa0c24227c8128652d4ec039950d9cfa82c3f962c4f4dee61e54236bdf89cbff8e9a04522134ef899ba136a668afd4ae76bd00bb19c0dc5ff66d7492a6a2a506021c2";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mValueTransfer.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;

        ValueTransfer mValueTransfer;

        SignatureData SignatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        @Before
        public void before() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(SignatureData)
                    .build();
        }

        @Test
        public void getRawTransaction() {
            String rawTx = mValueTransfer.getRawTransaction();
            String expected = "0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc";
            assertEquals(expected, rawTx);
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String expectedHash = "0x762f130342569e9669a4d8547f1248bd2554fbbf3062d63a97ce28bfa97aa9d7";

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;

        ValueTransfer mValueTransfer;

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        @Test
        public void getTransactionHash() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            String txHash = mValueTransfer.getTransactionHash();
            assertEquals(expectedHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String expectedHash = "0x762f130342569e9669a4d8547f1248bd2554fbbf3062d63a97ce28bfa97aa9d7";

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;

        ValueTransfer mValueTransfer;

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        @Test
        public void getTransactionHash() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            String txHash = mValueTransfer.getSenderTxHash();
            assertEquals(expectedHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String value = "0xa";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String chainId = "0x1";
        int nonce = 1234;

        ValueTransfer mValueTransfer;

        SignatureData signatureData = new  SignatureData(
                Numeric.hexStringToByteArray("0x25"),
                Numeric.hexStringToByteArray("0xf3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29a"),
                Numeric.hexStringToByteArray("0x6748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc")
        );

        String expectedRLP = "0xf839b5f4088204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b018080";

        @Test
        public void getRLPEncodingForSignature() {
            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            String rlp = mValueTransfer.getRLPEncodingForSignature();
            assertEquals(expectedRLP, rlp);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setTo(to)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFrom(from)
                    .setSignatures(signatureData)
                    .build();

            mValueTransfer.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            mValueTransfer = new ValueTransfer.Builder()
                    .setNonce(BigInteger.valueOf(nonce))
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setTo(to)
                    .setValue(value)
                    .setFrom(from)
                    .build();

            mValueTransfer.getRLPEncodingForSignature();
        }
    }
}
