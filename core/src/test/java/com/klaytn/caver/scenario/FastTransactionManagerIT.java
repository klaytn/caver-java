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
 * This file is derived from web3j/integration-tests/src/test/java/org/web3j/protocol/scenarios/FastRawTransactionManagerIT.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.scenario;

import com.klaytn.caver.methods.response.Callback;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.manager.*;
import com.klaytn.caver.utils.Convert;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.slf4j.LoggerFactory.getLogger;

public class FastTransactionManagerIT extends Scenario {
    private static final Logger log = getLogger(FastTransactionManagerIT.class);

    private static final int COUNT = 20;
    private static final int POLLING_ATTEMPTS = 30;
    private static final int POLLING_FREQUENCY = 1500;

    @Test
    public void testTransactionPolling() throws Exception {

        List<Future<KlayTransactionReceipt.TransactionReceipt>> transactionReceipts = new LinkedList<>();

        PollingTransactionReceiptProcessor pollingTransactionReceiptProcessor
                = new PollingTransactionReceiptProcessor(caver, POLLING_FREQUENCY, POLLING_ATTEMPTS);

        TransactionManager transactionManager = new TransactionManager.Builder(caver, BRANDON)
                .setTransactionReceiptProcessor(pollingTransactionReceiptProcessor)
                .setGetNonceProcessor(new FastGetNonceProcessor(caver))
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public void exception(Exception exception) {
                        log.error("error : {}", exception.getLocalizedMessage());
                    }
                })
                .build();

        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);

        for (int i = 0; i < COUNT; i++) {
            CompletableFuture<KlayTransactionReceipt.TransactionReceipt> transactionReceiptFuture = createTransaction(valueTransfer).sendAsync();
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0; i < POLLING_ATTEMPTS
                && !transactionReceipts.isEmpty(); i++) {

            for (Iterator<Future<KlayTransactionReceipt.TransactionReceipt>> iterator = transactionReceipts.iterator();
                 iterator.hasNext(); ) {
                Future<KlayTransactionReceipt.TransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionReceiptFuture.get();
                    TestCase.assertFalse(transactionReceipt.getBlockHash().isEmpty());
                    iterator.remove();
                }
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    @Test
    public void testTransactionQueuing() throws Exception {

        Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<KlayTransactionReceipt.TransactionReceipt> transactionReceipts =
                new ConcurrentLinkedQueue<>();


        Callback callback = new Callback<KlayTransactionReceipt.TransactionReceipt>() {
            @Override
            public void accept(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
                transactionReceipts.add(transactionReceipt);
            }

            @Override
            public void exception(Exception exception) {
            }
        };

        QueuingTransactionReceiptProcessor queuingTransactionReceiptProcessor =
                new QueuingTransactionReceiptProcessor(caver, callback, POLLING_ATTEMPTS, POLLING_FREQUENCY);

        TransactionManager transactionManager = new TransactionManager.Builder(caver, BRANDON)
                .setGetNonceProcessor(new FastGetNonceProcessor(caver))
                .setTransactionReceiptProcessor(queuingTransactionReceiptProcessor)
                .build();

        ValueTransfer valueTransfer = ValueTransfer.create(caver, transactionManager);

        for (int i = 0; i < COUNT; i++) {
            KlayTransactionReceipt.TransactionReceipt transactionReceipt = createTransaction(valueTransfer).send();
            pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0; i < POLLING_ATTEMPTS && !pendingTransactions.isEmpty(); i++) {
            for (KlayTransactionReceipt.TransactionReceipt transactionReceipt : transactionReceipts) {
                assertFalse(transactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(transactionReceipt.getTransactionHash());
                transactionReceipts.remove(transactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    private RemoteCall<KlayTransactionReceipt.TransactionReceipt> createTransaction(
            ValueTransfer valueTransfer) {
        return valueTransfer.sendFunds(BRANDON.getAddress(), LUMAN.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT);
    }

}
