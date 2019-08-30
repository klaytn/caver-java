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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/EmptyTransactionReceipt.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.methods.response;

import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;

import java.util.List;

/**
 * An empty transaction receipt object containing only the transaction hash. This is to support the
 * {@link QueuingTransactionReceiptProcessor} and {@link NoOpProcessor}.
 */
public class EmptyTransactionReceipt extends KlayTransactionReceipt.TransactionReceipt {

    public EmptyTransactionReceipt(String transactionHash) {
        super();
        this.setTransactionHash(transactionHash);
    }

    @Override
    public String getTransactionHash() {
        return super.getTransactionHash();
    }

    @Override
    public void setTransactionHash(String transactionHash) {
        super.setTransactionHash(transactionHash);
    }

    private UnsupportedOperationException unsupportedOperation() {
        return new UnsupportedOperationException(
                "Empty transaction receipt, only transaction hash is available");
    }

    @Override
    public String getBlockHash() {
        throw unsupportedOperation();
    }

    @Override
    public String getBlockNumber() {
        throw unsupportedOperation();
    }

    @Override
    public String getContractAddress() {
        throw unsupportedOperation();
    }

    @Override
    public String getFrom() {
        throw unsupportedOperation();
    }

    @Override
    public String getGas() {
        throw unsupportedOperation();
    }

    @Override
    public String getGasPrice() {
        throw unsupportedOperation();
    }

    @Override
    public String getGasUsed() {
        throw unsupportedOperation();
    }

    @Override
    public String getInput() {
        throw unsupportedOperation();
    }

    @Override
    public List<KlayLogs.Log> getLogs() {
        throw unsupportedOperation();
    }

    @Override
    public String getLogsBloom() {
        throw unsupportedOperation();
    }

    @Override
    public String getNonce() {
        throw unsupportedOperation();
    }

    @Override
    public String getSenderTxHash() {
        throw unsupportedOperation();
    }

    @Override
    public List<KlaySignatureData> getSignatures() {
        throw unsupportedOperation();
    }

    @Override
    public void setSignatures(List<KlaySignatureData> signatures) {
        throw unsupportedOperation();
    }

    @Override
    public String getStatus() {
        throw unsupportedOperation();
    }

    @Override
    public String getTo() {
        throw unsupportedOperation();
    }

    @Override
    public String getTransactionIndex() {
        throw unsupportedOperation();
    }

    @Override
    public String getTxError() {
        throw unsupportedOperation();
    }

    @Override
    public String getErrorMessage() {
        throw unsupportedOperation();
    }

    @Override
    public String getType() {
        throw unsupportedOperation();
    }

    @Override
    public String getTypeInt() {
        throw unsupportedOperation();
    }

    @Override
    public String getValue() {
        throw unsupportedOperation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionReceipt)) {
            return false;
        }

        TransactionReceipt that = (TransactionReceipt) o;

        return getTransactionHash() != null
                ? getTransactionHash().equals(that.getTransactionHash())
                : that.getTransactionHash() == null;
    }

    @Override
    public int hashCode() {
        return getTransactionHash() != null ? getTransactionHash().hashCode() : 0;
    }
}

