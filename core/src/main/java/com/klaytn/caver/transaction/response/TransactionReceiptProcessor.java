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
import com.klaytn.caver.rpc.RPC;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {
    private final RPC rpc;

    public TransactionReceiptProcessor(Caver caver) {
        this(caver.getRpc());
    }

    public TransactionReceiptProcessor(RPC rpc) {
        this.rpc = rpc;
    }

    public abstract TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt.TransactionReceiptData> sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException{
        TransactionReceipt transactionReceipt = rpc.klay.getTransactionReceipt(transactionHash).send();
        if(transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return Optional.ofNullable(transactionReceipt.getResult());
    }
}
