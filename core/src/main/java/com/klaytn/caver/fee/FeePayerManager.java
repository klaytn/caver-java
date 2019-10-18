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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/TransactionManager.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.fee;

import com.klaytn.caver.tx.exception.PlatformErrorException;
import com.klaytn.caver.tx.manager.ErrorHandler;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionReceiptProcessor;
import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Callback;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.type.AbstractTxType;
import com.klaytn.caver.tx.type.TxTypeFeeDelegate;
import com.klaytn.caver.utils.ChainId;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

public class FeePayerManager {

    private Caver caver;
    private FeePayer signer;
    private TransactionReceiptProcessor transactionReceiptProcessor;
    private ErrorHandler errorHandler;

    private FeePayerManager(Builder builder) {
        this.caver = builder.caver;
        this.signer = new FeePayer(builder.credentials, builder.chainId);
        this.transactionReceiptProcessor = builder.transactionReceiptProcessor;
        this.errorHandler = builder.errorHandler;
    }

    private TxTypeFeeDelegate decode(String rawTransaction) {
        return FeePayerTransactionDecoder.decode(rawTransaction);
    }

    public KlayTransactionReceipt.TransactionReceipt executeTransaction(
            String rawTransaction) {

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = null;
        try {
            KlayRawTransaction rawTx = sign(rawTransaction);
            String transactionHash = send(rawTx);

            transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
        } catch (TransactionException | IOException | PlatformErrorException e) {
            exception(e);
        }

        return transactionReceipt;
    }

    public KlayRawTransaction sign(String rawTransaction) {
        TxTypeFeeDelegate txTypeFeeDelegate = decode(rawTransaction);
        return signer.sign(txTypeFeeDelegate);
    }

    public String send(KlayRawTransaction klayRawTransaction) throws IOException, PlatformErrorException {
        Bytes32 transactionHash = caver.klay().sendSignedTransaction(klayRawTransaction.getValueAsString()).send();
        if (transactionHash.hasError()) {
            throw new PlatformErrorException(transactionHash.getError());
        }
        return transactionHash.getResult();
    }

    public void sendAsync(KlayRawTransaction klayRawTransaction, Callback<String> callback) {
        caver.klay().sendSignedTransaction(klayRawTransaction.getValueAsString()).sendAsync()
                .thenAcceptAsync(response -> {
                    if (response.hasError()) {
                        exception(new PlatformErrorException(response.getError()));
                        callback.exception(new PlatformErrorException(response.getError()));
                    }
                    callback.accept(response.getResult());
                })
                .exceptionally(throwable -> {
                    exception(new Exception(throwable));
                    return null;
                });
    }

    private void exception(Exception e) {
        if (errorHandler != null)
            errorHandler.exception(e);
    }

    public static class Builder {
        private Caver caver;
        private KlayCredentials credentials;
        private int chainId = -1;
        private TransactionReceiptProcessor transactionReceiptProcessor;
        private ErrorHandler errorHandler;

        public Builder(Caver caver, KlayCredentials credentials) {
            this.credentials = credentials;
            this.caver = caver;
        }

        public Builder setChainId(int chainId) {
            this.chainId = chainId;
            return this;
        }

        public Builder setTransactionReceiptProcessor(TransactionReceiptProcessor transactionReceiptProcessor) {
            this.transactionReceiptProcessor = transactionReceiptProcessor;
            return this;
        }

        public Builder setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        public FeePayerManager build() {
            if (this.chainId == -1) this.chainId = ChainId.BAOBAB_TESTNET;
            if (this.transactionReceiptProcessor == null)
                this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(this.caver, 1000, 15);

            return new FeePayerManager(this);
        }
    }
}
