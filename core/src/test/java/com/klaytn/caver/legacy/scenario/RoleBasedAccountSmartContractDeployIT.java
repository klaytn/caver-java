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

package com.klaytn.caver.legacy.scenario;

import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.tx.type.TxTypeFeeDelegate;
import com.klaytn.caver.utils.CodeFormat;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class RoleBasedAccountSmartContractDeployIT extends RoleBasedAccountScenario {
    final String CONTRACT_INPUT = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";

    private final SmartContractDeployTransaction getValueTransferTransaction(String from) {
        return SmartContractDeployTransaction.create(
                from,
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(CONTRACT_INPUT),
                GAS_LIMIT,
                CodeFormat.EVM
        );
    }

    private final ReceiptChecker getFeeDelegatedSmartContractDeployReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        assertEquals(CONTRACT_INPUT, transactionReceipt.getInput());
    };

    private final ReceiptChecker getFeeDelegatedSmartContractDeployWithRatioReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        assertEquals(CONTRACT_INPUT, transactionReceipt.getInput());
        TxTypeFeeDelegate txTypeFeeDelegate = (TxTypeFeeDelegate) transactionTransformer.build();
        assertEquals(Numeric.toHexStringWithPrefix(txTypeFeeDelegate.getFeeRatio()), transactionReceipt.getFeeRatio());
    };

    //////////////////////////////// BasicTest ////////////////////////////////
    @Test
    public void smartContractDeployTest() throws Exception {
        roleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from),
                getFeeDelegatedSmartContractDeployReceiptChecker, false);
    }

    //////////////////////////////// FeeDelegateTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployTest() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getFeeDelegatedSmartContractDeployReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatio() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedSmartContractDeployWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiTransactionSignerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getFeeDelegatedSmartContractDeployReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiTransactionSignerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedSmartContractDeployWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getFeeDelegatedSmartContractDeployReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiFeePayerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedSmartContractDeployWithRatioReceiptChecker, false);
    }

    //////////////////////////////// MultiSignerMultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate(),
                getFeeDelegatedSmartContractDeployReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiTransactionSignerMultiFeePayerTest() throws Exception {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                (String from) -> getValueTransferTransaction(from).feeDelegate().feeRatio(feeRatio),
                getFeeDelegatedSmartContractDeployWithRatioReceiptChecker, false);
    }
}
