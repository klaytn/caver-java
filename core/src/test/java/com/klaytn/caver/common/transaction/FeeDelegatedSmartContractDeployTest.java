package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractDeploy;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferMemo;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.utils.CodeFormat;
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
        FeeDelegatedSmartContractDeployTest.createInstance.class,
        FeeDelegatedSmartContractDeployTest.createInstanceBuilder.class,
        FeeDelegatedSmartContractDeployTest.getRLPEncodingTest.class,
        FeeDelegatedSmartContractDeployTest.signAsFeePayer_OneKeyTest.class,
        FeeDelegatedSmartContractDeployTest.signAsFeePayer_AllKeyTest.class,
        FeeDelegatedSmartContractDeployTest.appendFeePayerSignaturesTest.class,
        FeeDelegatedSmartContractDeployTest.combineSignatureTest.class,
        FeeDelegatedSmartContractDeployTest.getRawTransactionTest.class,
        FeeDelegatedSmartContractDeployTest.getTransactionHashTest.class,
        FeeDelegatedSmartContractDeployTest.getSenderTxHashTest.class,
        FeeDelegatedSmartContractDeployTest.getRLPEncodingForFeePayerSignatureTest.class,
})
public class FeeDelegatedSmartContractDeployTest {
    static Caver caver = Caver.build(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
    static String from = "0x8061145252c8f2b4f110aed096435ae6ed7d5a95";
    static String to = "0x";
    static String value = "0x00";
    static String input = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
    static String gas = "0xdbba0";
    static String gasPrice = "0x5d21dba00";
    static String chainID = "0x7e3";
    static String nonce = "0x0";
    static boolean humanReadable = false;
    static String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);

    static SignatureData senderSignatureData = new SignatureData(
            "0x0fe9",
            "0x7abfd0f0cfb9a9c38c6e3e1a4eeb15f43aeb4b4f6dee7c3f37c07e417af89d9b",
            "0x3f1e54a512c906d2e57a611b25ce4739d12928e199c3e89792b82f577f0da9ad"
    );

    static String feePayer = "0x2c8eb96e7060ab864d94e91ab16f214dc6647628";

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x0fe9",
            "0x192e3b6457f13c6ef557bd11074702d5062dd463473c483278c57f651d5b712b",
            "0x3ff8638b7cc7ed86c793cb5ffe0e8a064fc94946c3aab624bb7704c62e81ec2d"
    );

    static String expectedRLPEncoding = "0x29f902cc808505d21dba00830dbba08080948061145252c8f2b4f110aed096435ae6ed7d5a95b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f847f845820fe9a07abfd0f0cfb9a9c38c6e3e1a4eeb15f43aeb4b4f6dee7c3f37c07e417af89d9ba03f1e54a512c906d2e57a611b25ce4739d12928e199c3e89792b82f577f0da9ad942c8eb96e7060ab864d94e91ab16f214dc6647628f847f845820fe9a0192e3b6457f13c6ef557bd11074702d5062dd463473c483278c57f651d5b712ba03ff8638b7cc7ed86c793cb5ffe0e8a064fc94946c3aab624bb7704c62e81ec2d";
    static String expectedTransactionHash = "0x8dc83759a9c9b226493cb9f7b81a33e0b6b4643f2e82a02fbac784fbe53f9cd9";
    static String expectedSenderTransactionHash = "0xe189c8ead022fcddb97a8489bb1ee6362368579c65da7404db6b4e704b037ed7";
    static String expectedRLPEncodingForFeePayerSigning = "0xf90246b90229f9022629808505d21dba00830dbba08080948061145252c8f2b4f110aed096435ae6ed7d5a95b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080942c8eb96e7060ab864d94e91ab16f214dc66476288207e38080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = KeyringFactory.generateRoleBasedKeys(numArr, "entropy");
        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setKlaytnCall(caver.klay())
                    .setGas(gas)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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
            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
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

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("'to' field must be nil('0x') : ");

            String to = "invalid Address";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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
            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("'to' field must be nil('0x') : ");

            String to = "invalid Address";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }


        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_invalidHumanReadable() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("HumanReadable attribute must set false");

            boolean humanReadable = true;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );

        }

        @Test
        public void throwException_invalidCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("CodeFormat attribute only support EVM(0)");

            String codeFormat = "1";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_missingCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("codeFormat is missing");

            String codeFormat = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = "0x";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = "0x";

            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String expectedRawTx = "0x29f902c78204d219830f4240808094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f847f845820fe9a07abfd0f0cfb9a9c38c6e3e1a4eeb15f43aeb4b4f6dee7c3f37c07e417af89d9ba03f1e54a512c906d2e57a611b25ce4739d12928e199c3e89792b82f577f0da9ad9433f524631e573329a550296f595c820d6c65213ff845f84326a0e21385082f88713e8548e17a9b30e8627af749e064b0539890f65dbb8c7868caa0375316897322483d2c4946a761a4e8b22007fd2109f82f9d44255e56256905dc";
        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        String value = "0x00";

        FeeDelegatedSmartContractDeploy txObj;
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey(feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();
        String feePayer = keyring.getAddress();

        @Before
        public void before() {

            txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .build();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            txObj.signAsFeePayer(feePayerPrivateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
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
            assertEquals(expectedRawTx, txObj.getRawTransaction());
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

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(keyring.getAddress(), Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
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

        FeeDelegatedSmartContractDeploy mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String feePayer = KeyringFactory.createFromPrivateKey(feePayerPrivateKey).getAddress();

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

        FeeDelegatedSmartContractDeploy mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

        String from = "0x54dc8905caf698250cebfcbde49f037b52d55f61";
        String to = "0x";
        String value = "0x0";
        String input = FeeDelegatedSmartContractDeployTest.input;
        String gas = "0xdbba0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";
        boolean humanReadable = false;
        String codeFormat = FeeDelegatedSmartContractDeployTest.codeFormat;


        FeeDelegatedSmartContractDeploy mTxObj;


        @Test
        public void combineSignature() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();


            SignatureData expectedSignature = new SignatureData(
                    "0x0fe9",
                    "0x627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780",
                    "0x10632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bda"
            );

            String rlpEncoded = "0x29f90274018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f847f845820fe9a0627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780a010632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bda80c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mTxObj.combineSignatures(list);

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signature = new SignatureData(
                    "0x0fe9",
                    "0x627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780",
                    "0x10632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bda"
            );

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setSignatures(signature)
                    .build();

            String expectedRLPEncoded = "0x29f90302018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f8d5f845820fe9a0627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780a010632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bdaf845820fe9a0c941f8f173e5a5c22216f3f0fdfa4da602356398e24ceee99beb2a2a9c2bfafca00f231de0075bd109708513416a3896fa076130cf6fd891cb1a7abd1835a352bef845820fe9a073bdd7375228ab9598ab5be10a4ffe1c44211de675295cf07bfce726eef2b764a018923a5455d601c52532280259d820ec2ae5cda3ec57095d8df2a872192a5ae980c4c3018080";

            SignatureData[] expectedSignature = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780",
                            "0x10632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bda"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xc941f8f173e5a5c22216f3f0fdfa4da602356398e24ceee99beb2a2a9c2bfafc",
                            "0x0f231de0075bd109708513416a3896fa076130cf6fd891cb1a7abd1835a352be"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x73bdd7375228ab9598ab5be10a4ffe1c44211de675295cf07bfce726eef2b764",
                            "0x18923a5455d601c52532280259d820ec2ae5cda3ec57095d8df2a872192a5ae9"
                    )
            };

            String[] rlpEncodedString = new String[] {
                    "0x29f90274018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f847f845820fe9a0c941f8f173e5a5c22216f3f0fdfa4da602356398e24ceee99beb2a2a9c2bfafca00f231de0075bd109708513416a3896fa076130cf6fd891cb1a7abd1835a352be80c4c3018080",
                    "0x29f90274018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f847f845820fe9a073bdd7375228ab9598ab5be10a4ffe1c44211de675295cf07bfce726eef2b764a018923a5455d601c52532280259d820ec2ae5cda3ec57095d8df2a872192a5ae980c4c3018080"
            };

            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_withFeePayerSignature() {
            String feePayer = "0xb1d7bfd3587cd8bfb1cc6ae980fef3735a3601ab";
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .build();

            String rlpEncoded = "0x29f90288018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080c4c301808094b1d7bfd3587cd8bfb1cc6ae980fef3735a3601abf847f845820fe9a0142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121a07efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72";
            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121",
                    "0x7efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72"
            );

            assertEquals(rlpEncoded,combined);
            assertEquals(expectedSignatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_withMultipleFeePayerSignature() {
            String feePayer = "0xb1d7bfd3587cd8bfb1cc6ae980fef3735a3601ab";
            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0x142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121",
                    "0x7efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72"
            );

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setFeePayerSignatures(signatureData)
                    .build();

            String[] rlpEncodedStrings = new String[] {
                    "0x29f90288018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080c4c301808094b1d7bfd3587cd8bfb1cc6ae980fef3735a3601abf847f845820feaa06e6acf405e08848854e469e8e38edad783ebf0e24cdefbd5d4a2d8f75c37b662a06f39343ba613683a6b0c85ed2f421acdee010027d5803c0da9658b21602919c6",
                    "0x29f90288018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080c4c301808094b1d7bfd3587cd8bfb1cc6ae980fef3735a3601abf847f845820feaa0a85dfa3531c970ad63298dc803176e5c3edb98fdaa406fc32be853bedba01e51a03c6b2458349fc33f5acef72252e62e1c59e38dcb00a1c38426b00c2f0d8051be"
            };

            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncodedStrings));

            String expectedRLPEncoded = "0x29f90316018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080c4c301808094b1d7bfd3587cd8bfb1cc6ae980fef3735a3601abf8d5f845820fe9a0142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121a07efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72f845820feaa06e6acf405e08848854e469e8e38edad783ebf0e24cdefbd5d4a2d8f75c37b662a06f39343ba613683a6b0c85ed2f421acdee010027d5803c0da9658b21602919c6f845820feaa0a85dfa3531c970ad63298dc803176e5c3edb98fdaa406fc32be853bedba01e51a03c6b2458349fc33f5acef72252e62e1c59e38dcb00a1c38426b00c2f0d8051be";

            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121",
                            "0x7efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x6e6acf405e08848854e469e8e38edad783ebf0e24cdefbd5d4a2d8f75c37b662",
                            "0x6f39343ba613683a6b0c85ed2f421acdee010027d5803c0da9658b21602919c6"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa85dfa3531c970ad63298dc803176e5c3edb98fdaa406fc32be853bedba01e51",
                            "0x3c6b2458349fc33f5acef72252e62e1c59e38dcb00a1c38426b00c2f0d8051be"
                    ),
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatures[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatures[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatures[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();


            String rlpEncodedString = "0x29f90302018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080f8d5f845820fe9a0627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780a010632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bdaf845820fe9a0c941f8f173e5a5c22216f3f0fdfa4da602356398e24ceee99beb2a2a9c2bfafca00f231de0075bd109708513416a3896fa076130cf6fd891cb1a7abd1835a352bef845820fe9a073bdd7375228ab9598ab5be10a4ffe1c44211de675295cf07bfce726eef2b764a018923a5455d601c52532280259d820ec2ae5cda3ec57095d8df2a872192a5ae980c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x627b73b8636a2d98f5f51bc30381631055127c3aa13f6f5d470c94ace4d10780",
                            "0x10632196cf8e128de3f99f5e13d41c254dfe3edcc17eea84a49e287cf5b28bda"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xc941f8f173e5a5c22216f3f0fdfa4da602356398e24ceee99beb2a2a9c2bfafc",
                            "0x0f231de0075bd109708513416a3896fa076130cf6fd891cb1a7abd1835a352be"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x73bdd7375228ab9598ab5be10a4ffe1c44211de675295cf07bfce726eef2b764",
                            "0x18923a5455d601c52532280259d820ec2ae5cda3ec57095d8df2a872192a5ae9"
                    ),
            };

            String combined = mTxObj.combineSignatures(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x29f90316018505d21dba00830dbba080809454dc8905caf698250cebfcbde49f037b52d55f61b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00298080c4c301808094b1d7bfd3587cd8bfb1cc6ae980fef3735a3601abf8d5f845820fe9a0142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121a07efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72f845820feaa06e6acf405e08848854e469e8e38edad783ebf0e24cdefbd5d4a2d8f75c37b662a06f39343ba613683a6b0c85ed2f421acdee010027d5803c0da9658b21602919c6f845820feaa0a85dfa3531c970ad63298dc803176e5c3edb98fdaa406fc32be853bedba01e51a03c6b2458349fc33f5acef72252e62e1c59e38dcb00a1c38426b00c2f0d8051be";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0x142775d7fd0e65c21ddf3a83f32a9b3043638b0fd0f75301b436616b00261121",
                            "0x7efb4167f4cfd4c6497bf812014f80a4e66612f860e1ff5a4f5fdc282b131a72"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x6e6acf405e08848854e469e8e38edad783ebf0e24cdefbd5d4a2d8f75c37b662",
                            "0x6f39343ba613683a6b0c85ed2f421acdee010027d5803c0da9658b21602919c6"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0xa85dfa3531c970ad63298dc803176e5c3edb98fdaa406fc32be853bedba01e51",
                            "0x3c6b2458349fc33f5acef72252e62e1c59e38dcb00a1c38426b00c2f0d8051be"
                    ),
            };

            combined = mTxObj.combineSignatures(Arrays.asList(rlpEncodedStringsWithFeePayerSignatures));

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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();

            String rlpEncoded = "0x09f899018505d21dba00830f4240947b65b75d204abed71587c9e519a89277766ee1d00a9404bb86a1b16113ebe8f57071f839b002cbcbf7d0c4c301808094b85f01a3b0b6aaa2e487c9ed541e27b75b3eba95f847f845820feaa0d432bdce799828530d89d14b4406ccb0446852a51f13e365123eac9375d7e629a04f73deb5343ff7d587a5affb14196a79c522b9a67c7d895762c6758258ac247b";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignatures(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedSmartContractDeploy txObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertEquals(expectedRLPEncoding, txObj.getRawTransaction());
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractDeploy mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractDeploy mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedSmartContractDeploy mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
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

            mTxObj = new FeeDelegatedSmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
