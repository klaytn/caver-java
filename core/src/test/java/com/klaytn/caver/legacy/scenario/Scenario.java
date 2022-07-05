/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/integration-tests/src/test/java/org/web3j/protocol/scenarios/Scenario.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.legacy.scenario;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.tx.type.TxType;
import org.junit.Before;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Optional;

import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static junit.framework.TestCase.fail;

/**
 * Common methods and settings used across scenarios
 */
public class Scenario {

    static final BigInteger GAS_LIMIT = BigInteger.valueOf(7_300_000);
    static DefaultGasProvider gasProvider;

    private static final String WALLET_PASSWORD = "";

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 2000;
    private static final int ATTEMPTS = 20;

    Caver caver;

    public Scenario() {
    }

    @Before
    public void setUp() throws Exception {
        this.caver = Caver.build(Caver.DEFAULT_URL);
        gasProvider = new DefaultGasProvider(this.caver);
    }

    BigInteger getNonce(String address) throws Exception {
        return caver.klay().getTransactionCount(
                address,
                DefaultBlockParameterName.PENDING).sendAsync().get().getValue();
    }

    String signTransaction(TxType tx, KlayCredentials credentials) {
        return tx.sign(credentials, LOCAL_CHAIN_ID).getValueAsString();
    }

    String signTransactionFromFeePayer(String senderRawTx, KlayCredentials feePayer) {
        FeePayerManager feePayerManager = new FeePayerManager
                .Builder(this.caver, feePayer).setChainId(LOCAL_CHAIN_ID).build();
        return feePayerManager.sign(senderRawTx).getValueAsString();
    }

    KlayTransactionReceipt.TransactionReceipt sendTxAndGetReceipt(String rawTx) throws Exception {
        Bytes32 response = caver.klay().sendSignedTransaction(rawTx).send();
        return waitForTransactionReceipt(response.getResult());
    }

    KlayTransactionReceipt.TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws Exception {
        Optional<KlayTransactionReceipt.TransactionReceipt> transactionReceiptOptional =
                getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after " + ATTEMPTS + " attempts");
        }
        return transactionReceiptOptional.get();
    }

    private Optional<KlayTransactionReceipt.TransactionReceipt> getTransactionReceipt(
            String transactionHash, int sleepDuration, int attempts) throws Exception {
        Optional<KlayTransactionReceipt.TransactionReceipt> receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }
        return receiptOptional;
    }

    private Optional<KlayTransactionReceipt.TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws Exception {
        Response<KlayTransactionReceipt.TransactionReceipt> transactionReceipt =
                caver.klay().getTransactionReceipt(transactionHash).sendAsync().get();
        return Optional.ofNullable(transactionReceipt.getResult());
    }
}
