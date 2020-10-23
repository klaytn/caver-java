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
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * With each provided transaction hash, poll until we obtain a transaction object.
 */
public class PollingTransactionReceiptProcessor extends TransactionReceiptProcessor{
    private final long sleepDuration;
    private final int attempts;

    public PollingTransactionReceiptProcessor(Caver caver, long sleepDuration, int attempts) {
        super(caver);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private TransactionReceipt.TransactionReceiptData getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        Optional<TransactionReceipt.TransactionReceiptData> receiptOptional = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                return receiptOptional.get();
            }
        }

        throw new TransactionException("Transaction receipt was not generated after "
                + ((sleepDuration * attempts) / 1000
                + " seconds for transaction: " + transactionHash), transactionHash);
    }
}
