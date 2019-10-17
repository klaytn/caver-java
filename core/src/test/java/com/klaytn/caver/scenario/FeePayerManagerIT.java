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

package com.klaytn.caver.scenario;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.wallet.KlayWalletUtils;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class FeePayerManagerIT extends Scenario {

    static final String CONTRACT_INPUT_DATA = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";
    static final String PASSWORD = "password";

    @Test
    public void testFeePayerManager() throws Exception {
        String keystoreFilePath = KlayWalletUtils.generateNewWalletFile(
                PASSWORD,
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );
        KlayCredentials credentials = KlayWalletUtils.loadCredentials(PASSWORD, keystoreFilePath);
        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                credentials.getAddress(),
                BigDecimal.valueOf(0.1),
                Convert.Unit.KLAY, GAS_LIMIT
        );

        SmartContractDeployTransaction smartContractDeploy = SmartContractDeployTransaction.create(
                credentials.getAddress(),
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(CONTRACT_INPUT_DATA),
                GAS_LIMIT,
                CodeFormat.EVM
        ).feeDelegate();
        TransactionManager transactionManager = new TransactionManager
                .Builder(caver, credentials).setChaindId(LOCAL_CHAIN_ID).build();
        String senderTx = transactionManager.sign(smartContractDeploy).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager
                .Builder(caver, LUMAN).setChainId(LOCAL_CHAIN_ID).build();
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderTx);

        assertEquals(CONTRACT_INPUT_DATA, transactionReceipt.getInput());
        assertEquals("0x1", transactionReceipt.getStatus());
    }

}
