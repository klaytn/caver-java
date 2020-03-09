package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.type.*;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.TransactionDecoder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import static junit.framework.TestCase.assertEquals;

import java.math.BigInteger;

public class TransactionDecodingTest {
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(25000000000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000L);

    private static int CHAIN_ID;
    private static Caver caver;

    private static KlayCredentials sender;
    private static KlayCredentials feePayer;
    private static KlayCredentials to;

    private static BigInteger nonce;
    private static BigInteger value;

    private static byte[] memo;

    @BeforeClass
    public static void setup() throws Exception {
        CHAIN_ID = ChainId.BAOBAB_TESTNET;
        caver = Caver.build("https://api.baobab.klaytn.net:8651/");

        sender = KlayCredentials.create(Keys.createEcKeyPair());
        feePayer = KlayCredentials.create(Keys.createEcKeyPair());
        to = KlayCredentials.create(Keys.createEcKeyPair());
        nonce = caver.klay().getTransactionCount(sender.getAddress(), DefaultBlockParameterName.LATEST).send().getValue();
        value = BigInteger.valueOf(10);
        memo = Numeric.hexStringToByteArray("0x68656c6c6f");
    }

    @Test
    public void testDecodingLegacyTransaction() {
        TxTypeLegacyTransaction tx = TxTypeLegacyTransaction.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                ""
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeLegacyTransaction decodedTx = (TxTypeLegacyTransaction) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getData(), "");
        assertEquals(decodedTx.getSenderSignatureDataSet().size(), 1);
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingValueTransferTransaction() {
        TxTypeValueTransfer tx = TxTypeValueTransfer.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress()
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeValueTransfer decodedTx = (TxTypeValueTransfer) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedValueTransferTransaction() {
        TxTypeFeeDelegatedValueTransfer tx = TxTypeFeeDelegatedValueTransfer.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress()
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedValueTransfer decodedTx = (TxTypeFeeDelegatedValueTransfer) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedValueTransfer) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedValueTransferWithRatioTransaction() {
        BigInteger feeRatio = BigInteger.valueOf(20);
        TxTypeFeeDelegatedValueTransferWithRatio tx = TxTypeFeeDelegatedValueTransferWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress(),
                feeRatio
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedValueTransferWithRatio decodedTx = (TxTypeFeeDelegatedValueTransferWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedValueTransferWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingValueTransferMemoTransaction() {
        TxTypeValueTransferMemo tx = TxTypeValueTransferMemo.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress(),
                memo
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeValueTransferMemo decodedTx = (TxTypeValueTransferMemo) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), Numeric.toHexString(memo));
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedValueTransferMemoTransaction() {
        TxTypeFeeDelegatedValueTransferMemo tx = TxTypeFeeDelegatedValueTransferMemo.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress(),
                memo
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedValueTransferMemo decodedTx = (TxTypeFeeDelegatedValueTransferMemo) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), Numeric.toHexString(memo));
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedValueTransferMemo) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), Numeric.toHexString(memo));
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedValueTransferMemoWithRatioTransaction() {
        BigInteger feeRatio = BigInteger.valueOf(20);
        TxTypeFeeDelegatedValueTransferMemoWithRatio tx = TxTypeFeeDelegatedValueTransferMemoWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                value,
                sender.getAddress(),
                memo,
                feeRatio
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedValueTransferMemoWithRatio decodedTx = (TxTypeFeeDelegatedValueTransferMemoWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), Numeric.toHexString(memo));
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedValueTransferMemoWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getTo(), to.getAddress());
        assertEquals(decodedTx.getValue(), value);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), Numeric.toHexString(memo));
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingAccountUpdateTransaction() throws Exception {
        AccountKeyPublic accountKeyPublic = AccountKeyPublic.create(Keys.createEcKeyPair().getPublicKey());
        TxTypeAccountUpdate tx = TxTypeAccountUpdate.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress(),
                accountKeyPublic
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeAccountUpdate decodedTx = (TxTypeAccountUpdate) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getAccountKey(), accountKeyPublic);
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedAccountUpdateTransaction() throws Exception {
        AccountKeyPublic accountKeyPublic = AccountKeyPublic.create(Keys.createEcKeyPair().getPublicKey());
        TxTypeFeeDelegatedAccountUpdate tx = TxTypeFeeDelegatedAccountUpdate.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress(),
                accountKeyPublic
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedAccountUpdate decodedTx = (TxTypeFeeDelegatedAccountUpdate) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getAccountKey(), accountKeyPublic);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedAccountUpdate) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getAccountKey(), accountKeyPublic);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedAccountUpdateWithRatioTransaction() throws Exception {
        BigInteger feeRatio = BigInteger.valueOf(20);
        AccountKeyPublic accountKeyPublic = AccountKeyPublic.create(Keys.createEcKeyPair().getPublicKey());
        TxTypeFeeDelegatedAccountUpdateWithRatio tx = TxTypeFeeDelegatedAccountUpdateWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress(),
                accountKeyPublic,
                feeRatio
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedAccountUpdateWithRatio decodedTx = (TxTypeFeeDelegatedAccountUpdateWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getAccountKey(), accountKeyPublic);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedAccountUpdateWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getAccountKey(), accountKeyPublic);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingSmartContractDeployTransaction() {
        String hexStringForDeploy = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
        TxTypeSmartContractDeploy tx = TxTypeSmartContractDeploy.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForDeploy),
                CodeFormat.EVM
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeSmartContractDeploy decodedTx = (TxTypeSmartContractDeploy) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForDeploy);
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedSmartContractDeployTransaction() {
        String hexStringForDeploy = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
        TxTypeFeeDelegatedSmartContractDeploy tx = TxTypeFeeDelegatedSmartContractDeploy.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForDeploy),
                CodeFormat.EVM
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedSmartContractDeploy decodedTx = (TxTypeFeeDelegatedSmartContractDeploy) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForDeploy);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedSmartContractDeploy) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForDeploy);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedSmartContractDeployWithRatioTransaction() {
        BigInteger feeRatio = BigInteger.valueOf(20);
        String hexStringForDeploy = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
        TxTypeFeeDelegatedSmartContractDeployWithRatio tx = TxTypeFeeDelegatedSmartContractDeployWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForDeploy),
                feeRatio,
                CodeFormat.EVM
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedSmartContractDeployWithRatio decodedTx = (TxTypeFeeDelegatedSmartContractDeployWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForDeploy);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedSmartContractDeployWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForDeploy);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingSmartContractExecutionTransaction() {
        String hexStringForExecution = "0xd14e62b8000000000000000000000000000000000000000000000000dc67327b51f7c636";
        TxTypeSmartContractExecution tx = TxTypeSmartContractExecution.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForExecution)
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeSmartContractExecution decodedTx = (TxTypeSmartContractExecution) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForExecution);
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedSmartContractExecutionTransaction() {
        String hexStringForExecution = "0xd14e62b8000000000000000000000000000000000000000000000000dc67327b51f7c636";
        TxTypeFeeDelegatedSmartContractExecution tx = TxTypeFeeDelegatedSmartContractExecution.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForExecution)
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedSmartContractExecution decodedTx = (TxTypeFeeDelegatedSmartContractExecution) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForExecution);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedSmartContractExecution) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForExecution);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedSmartContractExecutionWithRatioTransaction() {
        BigInteger feeRatio = BigInteger.valueOf(20);
        String hexStringForExecution = "0xd14e62b8000000000000000000000000000000000000000000000000dc67327b51f7c636";
        TxTypeFeeDelegatedSmartContractExecutionWithRatio tx = TxTypeFeeDelegatedSmartContractExecutionWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                to.getAddress(),
                BigInteger.ZERO,
                sender.getAddress(),
                Numeric.hexStringToByteArray(hexStringForExecution),
                feeRatio
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedSmartContractExecutionWithRatio decodedTx = (TxTypeFeeDelegatedSmartContractExecutionWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForExecution);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedSmartContractExecutionWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(Numeric.toHexString(decodedTx.getPayload()), hexStringForExecution);
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingCancelTransaction() {
        TxTypeCancel tx = TxTypeCancel.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress()
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeCancel decodedTx = (TxTypeCancel) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignature(decodedTx, klayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedCancelTransaction() {
        TxTypeFeeDelegatedCancel tx = TxTypeFeeDelegatedCancel.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress()
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedCancel decodedTx = (TxTypeFeeDelegatedCancel) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedCancel) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    @Test
    public void testDecodingFeeDelegatedCancelWithRatioTransaction() {
        BigInteger feeRatio = BigInteger.valueOf(20);
        TxTypeFeeDelegatedCancelWithRatio tx = TxTypeFeeDelegatedCancelWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                sender.getAddress(),
                feeRatio
        );

        KlayRawTransaction klayRawTransaction = tx.sign(sender, CHAIN_ID);
        String rawTx = klayRawTransaction.getValueAsString();

        TxTypeFeeDelegatedCancelWithRatio decodedTx = (TxTypeFeeDelegatedCancelWithRatio) TransactionDecoder.decode(rawTx);

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignature(decodedTx, klayRawTransaction);

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(CHAIN_ID).build();
        KlayRawTransaction payerSignedKlayRawTransaction = feePayerManager.sign(rawTx);

        decodedTx = (TxTypeFeeDelegatedCancelWithRatio) TransactionDecoder.decode(payerSignedKlayRawTransaction.getValueAsString());

        assertCommonValue(decodedTx);
        assertEquals(decodedTx.getFrom(), sender.getAddress());
        assertEquals(decodedTx.getFeeRatio(), feeRatio);
        assertSignatures(decodedTx, klayRawTransaction, payerSignedKlayRawTransaction);
    }

    private void assertCommonValue(AbstractTxType decodedTx) {
        assertEquals(decodedTx.getNonce(), nonce);
        assertEquals(decodedTx.getGasPrice(), GAS_PRICE);
        assertEquals(decodedTx.getGasLimit(), GAS_LIMIT);
    }

    private void assertSignature(AbstractTxType decodedTx, KlayRawTransaction rawTransaction) {
        assertEquals(decodedTx.getSenderSignatureDataSet().toArray()[0].toString(), rawTransaction.getSignatureData().toString());
    }

    private void assertSignatures(TxTypeFeeDelegate decodedTx, KlayRawTransaction rawTransaction, KlayRawTransaction payerSignedRawTransaction) {
        assertEquals(decodedTx.getSenderSignatureDataSet().toArray()[0].toString(), rawTransaction.getSignatureData().toString());
        assertEquals(decodedTx.getFeePayerSignatureData().toArray()[0].toString(), payerSignedRawTransaction.getSignatureData().toString());
    }
}
