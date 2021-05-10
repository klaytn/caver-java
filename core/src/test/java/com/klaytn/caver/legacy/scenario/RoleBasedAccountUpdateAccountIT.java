package com.klaytn.caver.legacy.scenario;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayAccountKey;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.model.AccountUpdateTransaction;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.tx.type.*;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class RoleBasedAccountUpdateAccountIT extends RoleBasedAccountScenario {

    private final ReceiptChecker getAccountUpdateReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());

        AccountKey newAccountKey = getNewAccountKey(transactionTransformer);

        KlayAccountKey klayAccountKey = caver.klay().getAccountKey(transactionReceipt.getFrom(), DefaultBlockParameterName.LATEST).send();
        AccountKey responseAccountKey = klayAccountKey.getResult().getKey();

        assertEquals("Response\n" + responseAccountKey.toString() + "\nExpected" + newAccountKey.toString(), responseAccountKey, newAccountKey);
    };

    private final ReceiptChecker getFeeRatioAccountUpdateReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());

        TxTypeFeeDelegate txTypeFeeDelegate = (TxTypeFeeDelegate) transactionTransformer.build();
        assertEquals(Numeric.toHexStringWithPrefix(txTypeFeeDelegate.getFeeRatio()), transactionReceipt.getFeeRatio());

        AccountKey newAccountKey = getNewAccountKey(transactionTransformer);

        KlayAccountKey klayAccountKey = caver.klay().getAccountKey(transactionReceipt.getFrom(), DefaultBlockParameterName.LATEST).send();
        AccountKey responseAccountKey = klayAccountKey.getResult().getKey();

        assertEquals("Response\n" + responseAccountKey.toString() + "\nExpected" + newAccountKey.toString(), responseAccountKey, newAccountKey);
    };

    private final AccountUpdateTransaction getAccountUpdateTransactionTransformer(String from) throws Exception {
        KlayCredentials oldAccount = createAccount();
        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.setRandomRoleBasedNewAccountKey(oldAccount, 10, 10, 10);

        return AccountUpdateTransaction.create(
                from,
                roleBasedAccountGenerator.getNewAccountKey(),
                GAS_LIMIT
        );
    }

    //////////////////////////////// BasicTest ////////////////////////////////
    @Test
    public void AccountUpdateTest() throws Exception {
        roleBasedTransactionTest(
                (String from) -> getAccountUpdateTransactionTransformer(from),
                getAccountUpdateReceiptChecker, true);
    }

    //////////////////////////////// FeeDelegateTest ////////////////////////////////
    @Test
    public void feeDelegatedAccountUpdateTest() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate(),
                getAccountUpdateReceiptChecker, true);
    }

    @Test
    public void feeDelegatedAccountUpdateWithRatio() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate().feeRatio(feeRatio),
                getFeeRatioAccountUpdateReceiptChecker, true);
    }

    //////////////////////////////// MultiTransactionSignerTest ////////////////////////////////
    @Test
    public void feeDelegatedAccountUpdateMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate(),
                getAccountUpdateReceiptChecker, true);
    }

    @Test
    public void feeDelegatedAccountUpdateWithRatioMultiTransactionSignerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate().feeRatio(feeRatio),
                getFeeRatioAccountUpdateReceiptChecker, true);
    }

    //////////////////////////////// MultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedAccountUpdateMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate(),
                getAccountUpdateReceiptChecker, true);
    }

    @Test
    public void feeDelegatedAccountUpdateWithRatioMultiFeePayerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate().feeRatio(feeRatio),
                getFeeRatioAccountUpdateReceiptChecker, true);
    }

    //////////////////////////////// MultiSignerMultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedAccountUpdateMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate(),
                getAccountUpdateReceiptChecker, true);
    }

    @Test
    public void feeDelegatedAccountUpdateWithRatioMultiTransactionSignerMultiFeePayerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getAccountUpdateTransactionTransformer(from).feeDelegate().feeRatio(feeRatio),
                getFeeRatioAccountUpdateReceiptChecker, true);
    }

    private AccountKey getNewAccountKey(TransactionTransformer transactionTransformer) throws Exception {
        AccountKey newAccountKey = null;
        TxType txType = transactionTransformer.build();

        switch (txType.getType()) {
            case ACCOUNT_UPDATE:
                newAccountKey = ((TxTypeAccountUpdate) txType).getAccountKey();
                break;
            case FEE_DELEGATED_ACCOUNT_UPDATE:
                newAccountKey = ((TxTypeFeeDelegatedAccountUpdate) txType).getAccountKey();
                break;
            case FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO:
                newAccountKey = ((TxTypeFeeDelegatedAccountUpdateWithRatio) txType).getAccountKey();
                break;
        }
        return newAccountKey;
    }
}
