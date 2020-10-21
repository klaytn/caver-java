/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.transaction.response;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Callback;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Async;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Transaction receipt processor that uses a single thread to query for transaction receipts.
 *
 * When initially invoked, this processor returns a transaction receipt containing only the transaction hash of the submitted transaction.
 */
public class QueuingTransactionReceiptProcessor extends TransactionReceiptProcessor {
    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 15;
    public static final long DEFAULT_POLLING_FREQUENCY = 1000;

    private final int pollingAttemptsPerTxHash;

    private final ScheduledExecutorService scheduledExecutorService;
    private final Callback<TransactionReceipt.TransactionReceiptData> callback;
    private final BlockingQueue<RequestWrapper> pendingTransactions;

    public QueuingTransactionReceiptProcessor(
            Caver caver, Callback callback,
            int pollingAttemptsPerTxHash, long pollingFrequency) {
        super(caver);
        this.scheduledExecutorService = Async.defaultExecutorService();
        this.callback = callback;
        this.pendingTransactions = new LinkedBlockingQueue<>();
        this.pollingAttemptsPerTxHash = pollingAttemptsPerTxHash;

        scheduledExecutorService.scheduleAtFixedRate(
                this::sendTransactionReceiptRequests,
                pollingFrequency, pollingFrequency, TimeUnit.MILLISECONDS);
    }

    public QueuingTransactionReceiptProcessor(
            Caver caver, Callback callback) {
        this(caver, callback, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, DEFAULT_POLLING_FREQUENCY);
    }

    @Override
    public TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        pendingTransactions.add(new QueuingTransactionReceiptProcessor.RequestWrapper(transactionHash));

        TransactionReceipt.TransactionReceiptData transactionReceiptData = new TransactionReceipt.TransactionReceiptData();
        transactionReceiptData.setTransactionHash(transactionHash);

        return transactionReceiptData;
    }

    private void sendTransactionReceiptRequests() {
        for (RequestWrapper requestWrapper : pendingTransactions) {
            try {
                String transactionHash = requestWrapper.getTransactionHash();
                Optional<TransactionReceipt.TransactionReceiptData> transactionReceipt =
                        sendTransactionReceiptRequest(transactionHash);
                if (transactionReceipt.isPresent()) {
                    callback.accept(transactionReceipt.get());
                    pendingTransactions.remove(requestWrapper);
                } else {
                    if (requestWrapper.getCount() == pollingAttemptsPerTxHash) {
                        throw new TransactionException(
                                "No transaction receipt for txHash: " + transactionHash
                                        + "received after " + pollingAttemptsPerTxHash
                                        + " attempts", transactionHash);
                    } else {
                        requestWrapper.incrementCount();
                    }
                }
            } catch (IOException | TransactionException e) {
                pendingTransactions.remove(requestWrapper);
                callback.exception(e);
            }
        }
    }

    /**
     * Java doesn't provide a concurrent linked hash set, so we use a simple wrapper to store
     * details of the number of requests we've made against this specific transaction hash. This
     * is so we can preserve submission order as we interate over the outstanding transactions.
     *
     * <p>Note - the equals/hashcode methods only operate on the transactionHash field. This is
     * intentional.
     */
    private static class RequestWrapper {
        private final String transactionHash;
        private int count;

        RequestWrapper(String transactionHash) {
            this.transactionHash = transactionHash;
            this.count = 0;
        }

        String getTransactionHash() {
            return transactionHash;
        }

        int getCount() {
            return count;
        }

        void incrementCount() {
            this.count += 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RequestWrapper that = (RequestWrapper) o;

            return transactionHash.equals(that.transactionHash);
        }

        @Override
        public int hashCode() {
            return transactionHash.hashCode();
        }
    }
}
