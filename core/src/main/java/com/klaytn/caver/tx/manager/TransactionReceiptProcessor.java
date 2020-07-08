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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/TransactionReceiptProcessor.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.tx.manager;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * @deprecated This class is deprecated since caver-java:1.5.0
 */
@Deprecated
public abstract class TransactionReceiptProcessor {

    private final Caver caver;

    public TransactionReceiptProcessor(Caver caver) {
        this.caver = caver;
    }

    public abstract KlayTransactionReceipt.TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException;

    Optional<KlayTransactionReceipt.TransactionReceipt> sendTransactionReceiptRequest(String transactionHash)
            throws IOException, TransactionException {
        KlayTransactionReceipt transactionReceipt = caver.klay().getTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
