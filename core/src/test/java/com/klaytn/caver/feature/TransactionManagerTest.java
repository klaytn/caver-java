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
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.*;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.wallet.WalletManager;
import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.*;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class TransactionManagerTest {

    private final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    private Caver caver;
    private TransactionManager transactionManager;

    @Before
    public void setUp() {
        caver = Caver.build(Caver.DEFAULT_URL);
        WalletManager walletManager = new WalletManager();
        walletManager.add(LUMAN);
        transactionManager = new TransactionManager.Builder(caver, walletManager)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();
    }

    @Test
    public void testValueTransfer() {
        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction
                .create(LUMAN.getAddress(), WAYNE.getAddress(), BigInteger.ZERO, BigInteger.valueOf(1000000));

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(valueTransferTransaction);
        assertNull(transactionReceipt.getErrorMessage());
    }

    @Test
    public void testAccountUpdate() throws Exception {
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID)
                .sendFunds(BRANDON.getAddress(), credentials.getAddress(), BigDecimal.valueOf(0.2), Convert.Unit.KLAY, GAS_LIMIT).send();
        TransactionManager updateTransactionManager = new TransactionManager.Builder(caver, credentials)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();

        KlayCredentials newCredentials = KlayCredentials.create(Keys.createEcKeyPair());
        AccountUpdateTransaction accountUpdate = AccountUpdateTransaction.create(
                credentials.getAddress(),
                AccountKeyPublic.create(newCredentials.getEcKeyPair().getPublicKey()),
                GAS_LIMIT
        );
        KlayTransactionReceipt.TransactionReceipt accountUpdateReceipt = updateTransactionManager.executeTransaction(accountUpdate);
        assertEquals("0x1", accountUpdateReceipt.getStatus());
    }

    @Test
    public void testSmartContractDeploy() {
        String contractInput = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";

        SmartContractDeployTransaction smartContractDeployTransaction = SmartContractDeployTransaction.create(
                LUMAN.getAddress(),
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(contractInput),
                GAS_LIMIT,
                CodeFormat.EVM
        );

        KlayTransactionReceipt.TransactionReceipt contractDeployReceipt = transactionManager.executeTransaction(smartContractDeployTransaction);
        assertEquals("0x1", contractDeployReceipt.getStatus());
    }

    @Test
    public void testSmartContractExecution() throws Exception {
        String deployedContractAddress = getDeployedContract();

        SmartContractExecutionTransaction smartContractExecution = SmartContractExecutionTransaction.create(
                LUMAN.getAddress(),
                deployedContractAddress,
                BigInteger.ZERO,
                getChangePayload(),
                GAS_LIMIT
        );

        KlayTransactionReceipt.TransactionReceipt contractExecutionReceipt = transactionManager.executeTransaction(smartContractExecution);
        assertEquals("0x1", contractExecutionReceipt.getStatus());
    }

    @Test
    public void testCancel() {
        String deployedContractAddress = "0xdc7eb926958efa9e7991d16346446f5ab7bb0499";

        SmartContractExecutionTransaction smartContractExecution = SmartContractExecutionTransaction.create(
                LUMAN.getAddress(),
                deployedContractAddress,
                BigInteger.ZERO,
                getChangePayload(),
                GAS_LIMIT
        );

        String rawContractDeploy = transactionManager.sign(smartContractExecution).getValueAsString();
        caver.klay().sendSignedTransaction(rawContractDeploy).sendAsync();

        CancelTransaction cancelTransaction = CancelTransaction.create(
                LUMAN.getAddress(),
                GAS_LIMIT
        );

        KlayTransactionReceipt.TransactionReceipt cancelReceipt = transactionManager.executeTransaction(cancelTransaction);
        assertEquals("0x1", cancelReceipt.getStatus());
    }

    private String getDeployedContract() throws Exception {
        byte[] payload = Numeric.hexStringToByteArray("0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029");
        SmartContract smartContract = SmartContract.create(caver, new TransactionManager.Builder(caver, BRANDON).setChaindId(LOCAL_CHAIN_ID).build());
        KlayTransactionReceipt.TransactionReceipt receipt = smartContract.sendDeployTransaction(SmartContractDeployTransaction.create(
                BRANDON.getAddress(), BigInteger.ZERO, payload, GAS_LIMIT, CodeFormat.EVM
        )).send();
        return receipt.getContractAddress();
    }

    private byte[] getChangePayload() {
        String setCommand = "setCount(uint256)";
        int changeValue = 27;

        BigInteger replaceValue = BigInteger.valueOf(changeValue);
        String payLoadNoCommand = Numeric.toHexStringNoPrefix(Numeric.toBytesPadded(replaceValue, 32));
        String payLoad = Hash.sha3String(setCommand).substring(2, 10) + payLoadNoCommand;
        return Numeric.hexStringToByteArray(payLoad);
    }

}
