/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.Account;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.AccountUpdateTransaction;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.Convert;
import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static com.klaytn.caver.base.Accounts.*;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class ManagedTransactionTest {
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    private static final byte[] PAYLOAD = Numeric.hexStringToByteArray("0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029");

    private Caver caver;
    private TransactionManager transactionManager;

    private SmartContractDeployTransaction smartContractDeployTransaction = SmartContractDeployTransaction
            .create(LUMAN.getAddress(), BigInteger.ZERO, PAYLOAD, GAS_LIMIT, CodeFormat.EVM);

    @Before
    public void setUp() {
        caver = Caver.build(Caver.DEFAULT_URL);
        transactionManager = getTransactionManager(LUMAN);
    }

    private TransactionManager getTransactionManager(KlayCredentials credentials) {
        return transactionManager = new TransactionManager.Builder(caver, credentials)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();
    }

    private SmartContractExecutionTransaction getContractExecutionTransaction(String deployedContract) {
        return SmartContractExecutionTransaction
                .create(LUMAN.getAddress(), deployedContract, BigInteger.ZERO, getChangePayload(), GAS_LIMIT);
    }

    private AccountUpdateTransaction getAccountUpdateTransaction(KlayCredentials from, KlayCredentials to) {
        return AccountUpdateTransaction.create(
                from.getAddress(),
                AccountKeyPublic.create(to.getEcKeyPair().getPublicKey()),
                GAS_LIMIT
        );
    }

    private String getDeployedContract(SmartContract smartContract) throws Exception {
        return smartContract.sendDeployTransaction(smartContractDeployTransaction)
                .send()
                .getContractAddress();
    }

    private byte[] getChangePayload() {
        String setCommand = "setCount(uint256)";
        BigInteger replaceValue = BigInteger.valueOf(27);
        String payLoadNoCommand = Numeric.toHexStringNoPrefix(Numeric.toBytesPadded(replaceValue, 32));
        String payLoad = Hash.sha3String(setCommand).substring(2, 10) + payLoadNoCommand;
        return Numeric.hexStringToByteArray(payLoad);
    }

    @Test
    public void testValueTransfer() throws Exception {
        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).send();
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testValueTransferFlow() {
        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);
        valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).flowable()
                .test()
                .assertSubscribed()
                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
    }

    @Test
    public void testValueTransferFuture() throws ExecutionException, InterruptedException {
        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);
        KlayTransactionReceipt.TransactionReceipt receipt = valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).sendAsync().get();
        assertEquals("0x1", receipt.getStatus());
    }

    @Test
    public void testAccountUpdate() throws Exception {
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());

        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                credentials.getAddress(),
                BigDecimal.valueOf(0.2),
                Convert.Unit.KLAY, GAS_LIMIT
        ).send();

        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
        Account updateAccount = Account.create(caver, getTransactionManager(credentials));
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).send();
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testUpdateFlow() throws Exception {
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());

        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                credentials.getAddress(),
                BigDecimal.valueOf(0.2),
                Convert.Unit.KLAY, GAS_LIMIT
        ).send();

        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
        Account updateAccount = Account.create(caver, getTransactionManager(credentials));
        updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).flowable()
                .test()
                .assertSubscribed()
                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
    }

    @Test
    public void testUpdateFuture() throws Exception {
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());

        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                credentials.getAddress(),
                BigDecimal.valueOf(0.2),
                Convert.Unit.KLAY, GAS_LIMIT
        ).send();

        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
        Account updateAccount = Account.create(caver, getTransactionManager(credentials));
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).sendAsync().get();
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testSmartContractDeploy() throws Exception {
        SmartContract smartContractDeploy = SmartContract.create(caver, transactionManager);
        KlayTransactionReceipt.TransactionReceipt receipt = smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).send();
        assertEquals("0x1", receipt.getStatus());
    }

    @Test
    public void testSmartContractDeployFlow() {
        SmartContract smartContractDeploy = SmartContract.create(caver, transactionManager);
        smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).flowable()
                .test()
                .assertSubscribed()
                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
    }

    @Test
    public void testSmartContractDeployFuture() throws ExecutionException, InterruptedException {
        SmartContract smartContractDeploy = SmartContract.create(caver, transactionManager);
        KlayTransactionReceipt.TransactionReceipt receipt = smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).sendAsync().get();
        assertEquals("0x1", receipt.getStatus());
    }

    @Test
    public void testSmartContractExecution() throws Exception {
        SmartContract smartContractExecution = SmartContract.create(caver, transactionManager);
        String deployedContract = getDeployedContract(smartContractExecution);
        KlayTransactionReceipt.TransactionReceipt receipt
                = smartContractExecution.sendExecutionTransaction(getContractExecutionTransaction(deployedContract)).send();
        assertEquals("0x1", receipt.getStatus());
    }

    @Test
    public void testSmartContractExecutionFlow() throws Exception {
        SmartContract smartContractExecution = SmartContract.create(caver, transactionManager);
        String deployedContract = getDeployedContract(smartContractExecution);
        smartContractExecution.sendExecutionTransaction(getContractExecutionTransaction(deployedContract))
                .flowable()
                .test()
                .assertSubscribed()
                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
    }

    @Test
    public void testSmartContractExecutionFuture() throws Exception {
        SmartContract smartContractExecution = SmartContract.create(caver, transactionManager);
        String deployedContract = getDeployedContract(smartContractExecution);
        KlayTransactionReceipt.TransactionReceipt receipt = smartContractExecution.sendExecutionTransaction(getContractExecutionTransaction(deployedContract)).sendAsync().get();
        assertEquals("0x1", receipt.getStatus());
    }

    @Test
    public void testValueTransferChainId() throws Exception {
        TransactionManager transactionManager = new TransactionManager.Builder(caver, LUMAN)
                .setChaindId(LOCAL_CHAIN_ID)
                .build();

        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = valueTransfer.sendFunds(LUMAN.getAddress(), BRANDON.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).send();
        assertEquals("0x1", transactionReceipt.getStatus());
    }
}
