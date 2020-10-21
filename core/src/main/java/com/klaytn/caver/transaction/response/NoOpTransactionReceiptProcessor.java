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

/**
 * Return an empty receipt back to callers containing only the transaction hash.
 */
public class NoOpTransactionReceiptProcessor extends TransactionReceiptProcessor {
    public NoOpTransactionReceiptProcessor(Caver caver) {
        super(caver);
    }

    @Override
    public TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        TransactionReceipt.TransactionReceiptData transactionReceiptData = new TransactionReceipt.TransactionReceiptData();
        transactionReceiptData.setTransactionHash(transactionHash);

        return transactionReceiptData;
    }
}
