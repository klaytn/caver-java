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

package com.klaytn.caver.tx.manager;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.tx.exception.EmptyNonceException;
import com.klaytn.caver.tx.exception.PlatformErrorException;
import com.klaytn.caver.tx.exception.UnsupportedTxTypeException;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.type.AbstractTxType;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.utils.TransactionDecoder;
import com.klaytn.caver.wallet.WalletManager;
import com.klaytn.caver.wallet.exception.CredentialNotFoundException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

public class TransactionManager {

    private Caver caver;
    private WalletManager walletManager;
    private int chainId;
    private TransactionReceiptProcessor transactionReceiptProcessor;
    private ErrorHandler errorHandler;
    private GetNonceProcessor getNonceProcessor;

    private TransactionManager(Builder builder) {
        this.caver = builder.caver;
        this.walletManager = builder.walletManager;
        this.chainId = builder.chainId;
        this.transactionReceiptProcessor = builder.transactionReceiptProcessor;
        this.errorHandler = builder.errorHandler;
        this.getNonceProcessor = builder.getNonceProcessor;
    }

    public KlayTransactionReceipt.TransactionReceipt executeTransaction(
            TransactionTransformer transactionTransformer) {
        KlayTransactionReceipt.TransactionReceipt receipt = null;
        KlayRawTransaction rawTx = sign(transactionTransformer);
        try {
            String transactionHash = send(rawTx);
            receipt = transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
        } catch (TransactionException | PlatformErrorException | IOException e) {
            exception(e);
        }
        return receipt;
    }

    public KlayTransactionReceipt.TransactionReceipt executeTransaction(
            AbstractTxType txType) {
        KlayTransactionReceipt.TransactionReceipt receipt = null;
        KlayRawTransaction rawTx = sign(txType);
        try {
            String transactionHash = send(rawTx);
            receipt = transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
        } catch (TransactionException | PlatformErrorException | IOException e) {
            exception(e);
        }
        return receipt;
    }

    public KlayTransactionReceipt.TransactionReceipt executeTransaction(
            String rawTransaction) {

        AbstractTxType txType = TransactionDecoder.decode(rawTransaction);
        return executeTransaction(txType);
    }

    public KlaySignatureData makeSignatureData(AbstractTxType txType) {
        KlaySignatureData result = null;

        try {
            KlayCredentials credentials = walletManager.findByAddress(txType.getFrom());

            result = txType.getSignatureData(credentials, this.chainId);
        } catch (CredentialNotFoundException | EmptyNonceException e) {
            exception(e);
        }
        return result;
    }

    public KlaySignatureData makeSignatureData(TransactionTransformer transactionTransformer) {
        KlaySignatureData result = null;

        try {
            KlayCredentials credentials = walletManager.findByAddress(transactionTransformer.getFrom());

            if (transactionTransformer.getNonce() == null) {
                transactionTransformer.nonce(getNonceProcessor.getNonce(credentials));
            }

            result = transactionTransformer.build().getSignatureData(credentials, this.chainId);
        } catch (UnsupportedTxTypeException | CredentialNotFoundException | IOException | EmptyNonceException e) {
            exception(e);
        }
        return result;
    }

    public KlayRawTransaction sign(AbstractTxType txType) {
        KlayRawTransaction result = null;
        try {
            KlayCredentials credentials = walletManager.findByAddress(txType.getFrom());
            result = txType.sign(credentials, this.chainId);
        } catch (CredentialNotFoundException | EmptyNonceException e) {
            exception(e);
        }
        return result;
    }

    public KlayRawTransaction sign(String klayRawTransaction) {
        AbstractTxType txType = TransactionDecoder.decode(klayRawTransaction);
        return sign(txType);
    }

    public KlayRawTransaction sign(TransactionTransformer transactionTransformer) {
        KlayRawTransaction result = null;
        try {
            KlayCredentials credentials = walletManager.findByAddress(transactionTransformer.getFrom());

            if (transactionTransformer.getNonce() == null) {
                transactionTransformer.nonce(getNonceProcessor.getNonce(credentials));
            }

            result = transactionTransformer.build().sign(credentials, this.chainId);
        } catch (UnsupportedTxTypeException | CredentialNotFoundException | IOException | EmptyNonceException e) {
            exception(e);
        }
        return result;
    }

    public String send(KlayRawTransaction klayRawTransaction) throws IOException, PlatformErrorException {
        Bytes32 transactionHash = caver.klay().sendSignedTransaction(klayRawTransaction.getValueAsString()).send();
        if (transactionHash.hasError()) {
            throw new PlatformErrorException(transactionHash.getError());
        }

        return transactionHash.getResult();
    }

    public String getDefaultAddress() {
        KlayCredentials credentials = null;
        try {
            credentials = walletManager.getDefault();
        } catch (CredentialNotFoundException e) {
            exception(e);
        }
        return credentials.getAddress();
    }

    private void exception(Exception e) {
        if (errorHandler != null)
            errorHandler.exception(e);
    }

    public static class Builder {
        private Caver caver;
        private WalletManager walletManager;
        private int chainId = -1;
        private GetNonceProcessor getNonceProcessor;
        private TransactionReceiptProcessor transactionReceiptProcessor;
        private ErrorHandler errorHandler;

        public Builder(Caver caver, WalletManager walletManager) {
            this.caver = caver;
            this.walletManager = walletManager;
        }

        public Builder(Caver caver, KlayCredentials credentials) {
            this.caver = caver;
            WalletManager walletManager = new WalletManager();
            walletManager.add(credentials);
            this.walletManager = walletManager;
        }

        public Builder setChaindId(int chaindId) {
            this.chainId = chaindId;
            return this;
        }

        public Builder setGetNonceProcessor(GetNonceProcessor getNonceProcessor) {
            this.getNonceProcessor = getNonceProcessor;
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

        public TransactionManager build() {
            if (this.chainId == -1)
                this.chainId = ChainId.BAOBAB_TESTNET;
            if (this.getNonceProcessor == null)
                this.getNonceProcessor = new GetNonceProcessor(this.caver);
            if (this.transactionReceiptProcessor == null)
                this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(this.caver, 1000, 15);

            return new TransactionManager(this);
        }

    }

}
