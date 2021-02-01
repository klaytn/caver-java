package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractDeployWithRatio;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class FeeDelegatedSmartContractDeployWithRatioTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
    static String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
    static String from = "0x294f5bc8fadbd1079b191d9c47e1f217d6c987b4";
    static String to = "0x";
    static String value = "0x00";
    static String input = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
    static String gas = "0x493e0";
    static String gasPrice = "0x5d21dba00";
    static String chainID = "0x7e3";
    static String nonce = "0xe";
    static boolean humanReadable = false;
    static String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);
    static String feeRatio = "0x1e";

    static SignatureData senderSignatureData = new SignatureData(
            "0x0fe9",
            "0x8a20b415ae7cd642f7682e59b63cb81068723a18eb0d8d3ba58fa7545c4fc8a5",
            "0x5ba8a86f4496f124f04293d4b0afec85ab3946b039d1f6a25424217508df5867"
    );

    static String feePayer = "0xc56a1fafa968d64d19b4b81c306ecbab6e489743";

    static SignatureData feePayerSignatureData = new SignatureData(
            "0x0fe9",
            "0xa525cba1b73cbe33b4df9be7165f8731b848ce3deba607690896eda8791a1a96",
            "0x5ea75b4da1b6744bb98bc2b9748d0eca5c47714ea1c09e26bebc5de386ff9958"
    );

    static String expectedRLPEncoding = "0x2af902cd0e8505d21dba00830493e0808094294f5bc8fadbd1079b191d9c47e1f217d6c987b4b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f847f845820fe9a08a20b415ae7cd642f7682e59b63cb81068723a18eb0d8d3ba58fa7545c4fc8a5a05ba8a86f4496f124f04293d4b0afec85ab3946b039d1f6a25424217508df586794c56a1fafa968d64d19b4b81c306ecbab6e489743f847f845820fe9a0a525cba1b73cbe33b4df9be7165f8731b848ce3deba607690896eda8791a1a96a05ea75b4da1b6744bb98bc2b9748d0eca5c47714ea1c09e26bebc5de386ff9958";
    static String expectedTransactionHash = "0x4f87bc437bc048f96f3a005fba82647a468bf1fde914fe60e3772192f929b58a";
    static String expectedSenderTransactionHash = "0xa5fabe514d238298f8ed8ee1431bad33cd5d1349ffcedaf488f28474dfe62be2";
    static String expectedRLPEncodingForFeePayerSigning = "0xf90247b9022af902272a0e8505d21dba00830493e0808094294f5bc8fadbd1079b191d9c47e1f217d6c987b4b901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e8094c56a1fafa968d64d19b4b81c306ecbab6e4897438207e38080";

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = KeyringFactory.generateRoleBasedKeys(numArr, "entropy");
        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
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
            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("'to' field must be nil('0x') : ");

            String to = "invalid Address";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_FeeRatio_invalid() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid type of feeRatio: feeRatio should be number type or hex number string");

            String feeRatio = "invalid fee ratio";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_FeeRatio_outOfRange() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid feeRatio: feeRatio is out of range. [1,99]");

            String feeRatio = "0xFF";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
                    to,
                    value,
                    input,
                    humanReadable,
                    codeFormat
            );
        }

        @Test
        public void throwException_missingFeeRatio() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feeRatio is missing.");

            String feeRatio = null;

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    Arrays.asList(senderSignatureData),
                    feePayer,
                    Arrays.asList(feePayerSignatureData),
                    feeRatio,
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

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            String nonce = "0x";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            String gasPrice = "0x";

            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        FeeDelegatedSmartContractDeployWithRatio txObj;
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey(feePayerPrivateKey);
        String klaytnWalletKey = keyring.getKlaytnWalletKey();
        String feePayer = keyring.getAddress();

        @Before
        public void before() {

            txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .build();
        }

        @Test
        public void signAsFeePayer_String() throws IOException {
            txObj.signAsFeePayer(feePayerPrivateKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
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

        FeeDelegatedSmartContractDeployWithRatio mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String feePayer = KeyringFactory.createFromPrivateKey(feePayerPrivateKey).getAddress();

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        FeeDelegatedSmartContractDeployWithRatio mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        String from = "0x2b2043ef30fd370997404397156ccc8d4fe6c04a";
        String to = "0x";
        String value = "0x0";
        String input = FeeDelegatedSmartContractDeployWithRatioTest.input;
        String gas = "0x493e0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainId = "0x7e3";
        String feeRatio = "0x1E";
        boolean humanReadable = false;
        String codeFormat = FeeDelegatedSmartContractDeployTest.codeFormat;


        FeeDelegatedSmartContractDeployWithRatio mTxObj;


        @Test
        public void combineSignature() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x2af90289018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f847f845820fe9a0f7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9a045226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2940000000000000000000000000000000000000000c4c3018080";

            SignatureData expectedSignature = new SignatureData(
                    "0x0fe9",
                    "0xf7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9",
                    "0x45226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2"
            );

            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mTxObj.combineSignedRawTransactions(list);

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signature = new SignatureData(
                    "0x0fe9",
                    "0xf7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9",
                    "0x45226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2"
            );

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeeRatio(feeRatio)
                    .setSignatures(signature)
                    .build();

            String[] rlpEncodedString = new String[] {
                    "0x2af90275018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f847f845820feaa04015d11ffebcc72ab8bb8b6a337e4121316d1f24cc421c958fcb5c49328603a4a00bb02ad934a105c0d9436f9a0d88b721f489d7e2b13cb7d5af4269bb3202b11480c4c3018080",
                    "0x2af90275018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f847f845820feaa06645f3dad39b1b9fbb533828cdc7100c67fccc8fec08d7867fe9667a65538cbba07ddbfc223f4377a78f0ee3d18263e31080faad8305132dcc5c17f1f093c9e9a280c4c3018080"
            };

            String expectedRLPEncoded = "0x2af90317018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f8d5f845820fe9a0f7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9a045226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2f845820feaa04015d11ffebcc72ab8bb8b6a337e4121316d1f24cc421c958fcb5c49328603a4a00bb02ad934a105c0d9436f9a0d88b721f489d7e2b13cb7d5af4269bb3202b114f845820feaa06645f3dad39b1b9fbb533828cdc7100c67fccc8fec08d7867fe9667a65538cbba07ddbfc223f4377a78f0ee3d18263e31080faad8305132dcc5c17f1f093c9e9a2940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignature = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0xf7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9",
                            "0x45226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x4015d11ffebcc72ab8bb8b6a337e4121316d1f24cc421c958fcb5c49328603a4",
                            "0x0bb02ad934a105c0d9436f9a0d88b721f489d7e2b13cb7d5af4269bb3202b114"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x6645f3dad39b1b9fbb533828cdc7100c67fccc8fec08d7867fe9667a65538cbb",
                            "0x7ddbfc223f4377a78f0ee3d18263e31080faad8305132dcc5c17f1f093c9e9a2"
                    )
            };


            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_withFeePayerSignature() {
            String feePayer = "0x1df7e797610fabf3b0aefb32b3df4f7cfff52b40";
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x2af90289018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80c4c3018080941df7e797610fabf3b0aefb32b3df4f7cfff52b40f847f845820fe9a0c2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597a01da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0xc2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597",
                    "0x1da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669"
            );

            assertEquals(rlpEncoded,combined);
            assertEquals(expectedSignatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_withMultipleFeePayerSignature() {
            String feePayer = "0x1df7e797610fabf3b0aefb32b3df4f7cfff52b40";
            SignatureData signatureData = new SignatureData(
                    "0x0fe9",
                    "0xc2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597",
                    "0x1da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669"
            );

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeePayer(feePayer)
                    .setFeePayerSignatures(signatureData)
                    .setFeeRatio(feeRatio)
                    .build();

            String[] rlpEncodedStrings = new String[] {
                    "0x2af90289018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80c4c3018080941df7e797610fabf3b0aefb32b3df4f7cfff52b40f847f845820feaa01a875f02c07dfd8f1729b23183b17ec1072dc5b1f132bd4497e1a5834e1abf6fa0453b67bd7cce843aec8bcc64df6d9eed52f0efcaeab45366c11bcdd555768ccb",
                    "0x2af90289018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80c4c3018080941df7e797610fabf3b0aefb32b3df4f7cfff52b40f847f845820fe9a01ebfb413857294515eaf49db2ee050fbdda8a92fd413fb90671bd4b2f6a29f63a04a18a7423ea5210bda2753c57dbc8487909f126a01fd7577d5d48288c797bac7"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));

            String expectedRLPEncoded = "0x2af90317018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80c4c3018080941df7e797610fabf3b0aefb32b3df4f7cfff52b40f8d5f845820fe9a0c2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597a01da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669f845820feaa01a875f02c07dfd8f1729b23183b17ec1072dc5b1f132bd4497e1a5834e1abf6fa0453b67bd7cce843aec8bcc64df6d9eed52f0efcaeab45366c11bcdd555768ccbf845820fe9a01ebfb413857294515eaf49db2ee050fbdda8a92fd413fb90671bd4b2f6a29f63a04a18a7423ea5210bda2753c57dbc8487909f126a01fd7577d5d48288c797bac7";

            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0xc2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597",
                            "0x1da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x1a875f02c07dfd8f1729b23183b17ec1072dc5b1f132bd4497e1a5834e1abf6f",
                            "0x453b67bd7cce843aec8bcc64df6d9eed52f0efcaeab45366c11bcdd555768ccb"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x1ebfb413857294515eaf49db2ee050fbdda8a92fd413fb90671bd4b2f6a29f63",
                            "0x4a18a7423ea5210bda2753c57dbc8487909f126a01fd7577d5d48288c797bac7"
                    ),
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatures[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatures[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatures[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_feePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeeRatio(feeRatio)
                    .build();


            String rlpEncodedString = "0x2af90303018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f8d5f845820fe9a0f7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9a045226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2f845820feaa04015d11ffebcc72ab8bb8b6a337e4121316d1f24cc421c958fcb5c49328603a4a00bb02ad934a105c0d9436f9a0d88b721f489d7e2b13cb7d5af4269bb3202b114f845820feaa06645f3dad39b1b9fbb533828cdc7100c67fccc8fec08d7867fe9667a65538cbba07ddbfc223f4377a78f0ee3d18263e31080faad8305132dcc5c17f1f093c9e9a280c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0xf7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9",
                            "0x45226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd2"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x4015d11ffebcc72ab8bb8b6a337e4121316d1f24cc421c958fcb5c49328603a4",
                            "0x0bb02ad934a105c0d9436f9a0d88b721f489d7e2b13cb7d5af4269bb3202b114"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x6645f3dad39b1b9fbb533828cdc7100c67fccc8fec08d7867fe9667a65538cbb",
                            "0x7ddbfc223f4377a78f0ee3d18263e31080faad8305132dcc5c17f1f093c9e9a2"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x2af90317018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80c4c3018080941df7e797610fabf3b0aefb32b3df4f7cfff52b40f8d5f845820fe9a0c2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597a01da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669f845820feaa01a875f02c07dfd8f1729b23183b17ec1072dc5b1f132bd4497e1a5834e1abf6fa0453b67bd7cce843aec8bcc64df6d9eed52f0efcaeab45366c11bcdd555768ccbf845820fe9a01ebfb413857294515eaf49db2ee050fbdda8a92fd413fb90671bd4b2f6a29f63a04a18a7423ea5210bda2753c57dbc8487909f126a01fd7577d5d48288c797bac7";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[] {
                    new SignatureData(
                            "0x0fe9",
                            "0xc2d6fe5745e3a3a805dee9d6969efc60c58e8bba9368eed456ddad0347fa2597",
                            "0x1da449694111b286f9006fd9994fbb0ad3ce7298b33ff6e579748e653818e669"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x1a875f02c07dfd8f1729b23183b17ec1072dc5b1f132bd4497e1a5834e1abf6f",
                            "0x453b67bd7cce843aec8bcc64df6d9eed52f0efcaeab45366c11bcdd555768ccb"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x1ebfb413857294515eaf49db2ee050fbdda8a92fd413fb90671bd4b2f6a29f63",
                            "0x4a18a7423ea5210bda2753c57dbc8487909f126a01fd7577d5d48288c797bac7"
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setValue(value)
                    .setTo(to)
                    .setInput(input)
                    .setChainId(chainId)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setFeeRatio(feeRatio)
                    .build();

            String rlpEncoded = "0x2af90275018505d21dba00830493e08080942b2043ef30fd370997404397156ccc8d4fe6c04ab901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029801e80f847f845820fe9a0f7c0b0d305325ae87e8d63aaa771312764931c7cf27bcd516218de5d48f63fc9a045226063f9a529afeefc10e2f0e5f5c1c551d8fb9ebb0e6cb88d6c62262e0cd280c4c3018080";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Test
        public void getRawTransaction() {
            FeeDelegatedSmartContractDeployWithRatio txObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        FeeDelegatedSmartContractDeployWithRatio mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        FeeDelegatedSmartContractDeployWithRatio mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

        FeeDelegatedSmartContractDeployWithRatio mTxObj;

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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

            mTxObj = new FeeDelegatedSmartContractDeployWithRatio.Builder()
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
                    .setFeeRatio(feeRatio)
                    .setSignatures(senderSignatureData)
                    .setFeePayerSignatures(feePayerSignatureData)
                    .build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }

}
