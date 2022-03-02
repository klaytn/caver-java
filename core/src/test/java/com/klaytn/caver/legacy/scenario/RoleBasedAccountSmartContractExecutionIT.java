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

import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.tx.type.TxTypeFeeDelegate;
import com.klaytn.caver.utils.CodeFormat;
import org.junit.Test;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class RoleBasedAccountSmartContractExecutionIT extends RoleBasedAccountScenario {
    protected static final String SET_COMMAND = "setCount(uint256)";
    protected static final String COUNT_COMMAND = "count()";
    protected static final int CHANGE_VALUE = 27;

    private final SmartContractExecutionTransaction getSmartContractExecutionTransactionTransformer(String from, String recipient) {
        return SmartContractExecutionTransaction.create(
                from,
                recipient,
                BigInteger.ZERO,
                getPayLoad(),
                GAS_LIMIT
        );
    }

    private final TransactionGetter getSmartContractExecutionTransactionGetter = (String from) -> {
        String deployedContract = getDeployedContract();
        return getSmartContractExecutionTransactionTransformer(from, deployedContract);
    };

    private final TransactionGetter getFeeDelegatedSmartContractExecutionTransactionGetter = (String from) -> {
        String deployedContract = getDeployedContract();
        return getSmartContractExecutionTransactionTransformer(from, deployedContract).feeDelegate();
    };

    private final TransactionGetter getFeeRatioSmartContractExecutionTransactionGetter = (String from) -> {
        Random random = new Random();
        BigInteger feeRatio = BigInteger.valueOf(random.nextInt(99) + 1);
        String deployedContract = getDeployedContract();
        return getSmartContractExecutionTransactionTransformer(from, deployedContract).feeDelegate().feeRatio(feeRatio);
    };

    private final ReceiptChecker getSmartContractExecutionReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        String recipient = transactionReceipt.getTo();
        Bytes changedValue = getChangedValue(recipient);
        assertEquals(BigInteger.valueOf(CHANGE_VALUE), Numeric.toBigInt(changedValue.getResult()));
    };

    private final ReceiptChecker getFeeDelegatedSmartContractExecutionReceiptChecker = getSmartContractExecutionReceiptChecker;
    private final ReceiptChecker getFeeRatioSmartContractExecutionReceiptChecker = (TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) -> {
        assertEquals("0x1", transactionReceipt.getStatus());
        String recipient = transactionReceipt.getTo();
        Bytes changedValue = getChangedValue(recipient);
        assertEquals(BigInteger.valueOf(CHANGE_VALUE), Numeric.toBigInt(changedValue.getResult()));

        TxTypeFeeDelegate txTypeFeeDelegate = (TxTypeFeeDelegate) transactionTransformer.build();
        assertEquals(Numeric.toHexStringWithPrefix(txTypeFeeDelegate.getFeeRatio()), transactionReceipt.getFeeRatio());
    };

    //////////////////////////////// BasicTest ////////////////////////////////
    @Test
    public void smartContractExecutionTest() throws Exception {
        roleBasedTransactionTest(
                getSmartContractExecutionTransactionGetter,
                getSmartContractExecutionReceiptChecker, false);
    }

    //////////////////////////////// FeeDelegateTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractExecutionTest() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                getFeeDelegatedSmartContractExecutionTransactionGetter,
                getFeeDelegatedSmartContractExecutionReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractExecutionWithRatio() throws Exception {
        feeDelegatedRoleBasedTransactionTest(
                getFeeRatioSmartContractExecutionTransactionGetter,
                getFeeRatioSmartContractExecutionReceiptChecker, false);
    }

    //////////////////////////////// MultiTransactionSignerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                getFeeDelegatedSmartContractExecutionTransactionGetter,
                getFeeDelegatedSmartContractExecutionReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiTransactionSignerTest() throws Exception {
        feeDelegatedRoleBasedMultiTransactionSignerTest(
                getFeeRatioSmartContractExecutionTransactionGetter,
                getFeeRatioSmartContractExecutionReceiptChecker, false);
    }

    //////////////////////////////// MultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                getFeeDelegatedSmartContractExecutionTransactionGetter,
                getFeeDelegatedSmartContractExecutionReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiFeePayerTest(
                getFeeRatioSmartContractExecutionTransactionGetter,
                getFeeRatioSmartContractExecutionReceiptChecker, false);
    }

    //////////////////////////////// MultiSignerMultiFeePayerTest ////////////////////////////////
    @Test
    public void feeDelegatedSmartContractDeployMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                getFeeDelegatedSmartContractExecutionTransactionGetter,
                getFeeDelegatedSmartContractExecutionReceiptChecker, false);
    }

    @Test
    public void feeDelegatedSmartContractDeployWithRatioMultiTransactionSignerMultiFeePayerTest() throws Exception {
        feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(
                getFeeRatioSmartContractExecutionTransactionGetter,
                getFeeRatioSmartContractExecutionReceiptChecker, false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Bytes getChangedValue(String deployedContract) throws java.io.IOException {
        CallObject callObject = new CallObject(
                BRANDON.getAddress(),
                deployedContract,
                GAS_LIMIT,
                GAS_PRICE,
                BigInteger.ZERO,
                Numeric.prependHexPrefix(Hash.sha3String(COUNT_COMMAND).substring(2, 10)));
        return caver.klay().call(callObject, DefaultBlockParameterName.LATEST).send();
    }

    private String getDeployedContract() throws Exception {
        KlayTransactionReceipt.TransactionReceipt receipt = SmartContract.create(caver, BRANDON, LOCAL_CHAIN_ID)
                .sendDeployTransaction(SmartContractDeployTransaction.create(
                        BRANDON.getAddress(),
                        BigInteger.ZERO,
                        Numeric.hexStringToByteArray("0x60806040526000805560405160208061014b83398101806040528101908080519060200190929190505050806000819055505061010a806100416000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60c6565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260cc565b6040518082815260200191505060405180910390f35b60c46004803603810190808035906020019092919050505060d4565b005b60005481565b600043905090565b80600081905550505600a165627a7a72305820d9f890da4e30bac256db19aacc47a7025c902da590bd8ebab1fe5425f3670df000290000000000000000000000000000000000000000000000000000000000000001"),
                        GAS_LIMIT,
                        CodeFormat.EVM
                )).send();
        return receipt.getContractAddress();
    }

    private byte[] getPayLoad() {
        BigInteger replaceValue = BigInteger.valueOf(CHANGE_VALUE);
        String payLoadNoCommand = Numeric.toHexString(Numeric.toBytesPadded(replaceValue, 32)).substring(2);
        String payLoad = Hash.sha3String(SET_COMMAND)
                .substring(2, 10) +
                payLoadNoCommand;
        return Numeric.hexStringToByteArray(payLoad);
    }
}

