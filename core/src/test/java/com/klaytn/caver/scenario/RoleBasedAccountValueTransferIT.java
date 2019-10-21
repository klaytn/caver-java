package com.klaytn.caver.scenario;

import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.tx.model.ValueTransferTransaction;
import com.klaytn.caver.tx.type.TxTypeFeeDelegate;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static org.junit.Assert.assertEquals;

public class RoleBasedAccountValueTransferIT extends RoleBasedAccountScenario {

    private final ReceiptChecker getReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
    };

    private final ReceiptChecker getFeeDelegatedValueTransferMemoWithRatioReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        assertEquals(Numeric.toHexString(MEMO.getBytes()), transactionReceipt.getInput());
        TxTypeFeeDelegate txTypeFeeDelegate = (TxTypeFeeDelegate) transactionTransformer.build();
        assertEquals(Numeric.toHexStringWithPrefix(txTypeFeeDelegate.getFeeRatio()), transactionReceipt.getFeeRatio());
    };

    private final ReceiptChecker getFeeDelegatedValueTransferWithRatioReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        TxTypeFeeDelegate txTypeFeeDelegate = (TxTypeFeeDelegate) transactionTransformer.build();
        assertEquals(Numeric.toHexStringWithPrefix(txTypeFeeDelegate.getFeeRatio()), transactionReceipt.getFeeRatio());
    };

    private final ReceiptChecker getFeeDelegatedValueTransferMemoReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        assertEquals(Numeric.toHexString(MEMO.getBytes()), transactionReceipt.getInput());
    };

    private final ValueTransferTransaction getValueTransferTransaction(String from) {
        return ValueTransferTransaction.create(from, BRANDON.getAddress(), BigInteger.ONE, GAS_LIMIT);
    }

    //////////////////////////////// BasicTest - value Transfer ////////////////////////////////
    @Test
    public void valueTransferTest() throws Exception {
        roleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from),
                getReceiptChecker, false);
    }

    @Test
    public void valueTransferMemoTest() throws Exception {
        roleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).memo(MEMO),
                getFeeDelegatedValueTransferMemoReceiptChecker, false);
    }

    //////////////////////////////// FeeDelegateTest - value Transfer ////////////////////////////////
    @Test
    public void feeDelegatedValueTransferTest() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoTest() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO),
                getFeeDelegatedValueTransferMemoReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoWithRatioTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO).feeRatio(feeRatio),
                getFeeDelegatedValueTransferMemoWithRatioReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferWithRatio() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedValueTransferWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiTransactionSignerTest - value Transfer ////////////////////////////////
    @Test
    public void feeDelegatedValueTransferMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO),
                getFeeDelegatedValueTransferMemoReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoWithRatioMultiTransactionSignerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO).feeRatio(feeRatio),
                getFeeDelegatedValueTransferMemoWithRatioReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferWithRatioMultiTransactionSignerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedValueTransferWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiFeePayerTest - value Transfer ////////////////////////////////
    @Test
    public void feeDelegatedValueTransferMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO),
                getFeeDelegatedValueTransferMemoReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoWithRatioMultiFeePayerTest() throws Exception {
        BigInteger feeRatio = BigInteger.valueOf(new Random().nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO).feeRatio(feeRatio),
                getFeeDelegatedValueTransferMemoWithRatioReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferWithRatioMultiFeePayerTest() throws Exception {
        BigInteger feeRatio = BigInteger.valueOf(new Random().nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedValueTransferWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiSignerMultiFeePayerTest - value Transfer ////////////////////////////////
    @Test
    public void feeDelegatedValueTransferMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO),
                getFeeDelegatedValueTransferMemoReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferMemoWithRatioMultiTransactionSignerMultiFeePayerTest() throws Exception {
        BigInteger feeRatio = BigInteger.valueOf(new Random().nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().memo(MEMO).feeRatio(feeRatio),
                getFeeDelegatedValueTransferMemoWithRatioReceiptChecker, false);
    }

    @Test
    public void feeDelegatedValueTransferWithRatioMultiTransactionSignerMultiFeePayerTest() throws Exception {
        BigInteger feeRatio = BigInteger.valueOf(new Random().nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedValueTransferWithRatioReceiptChecker, false);
    }
}
