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

package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.web3j.protocol.core.Response;

public class DecodeAnchoringTransaction extends Response<DecodeAnchoringTransaction.AnchoredChainData> {

    public static class AnchoredChainData {
        @JsonProperty("TxHash")
        private String txHash;

        @JsonProperty("TxCount")
        private int txCount;

        @JsonProperty("StateRootHash")
        private String stateRootHash;

        @JsonProperty("ReceiptHash")
        private String receiptHash;

        @JsonProperty("ParentHash")
        private String parentHash;

        @JsonProperty("BlockNumber")
        private int blockNumber;

        @JsonProperty("BlockHash")
        private String blockHash;

        @JsonProperty("BlockCount")
        private int blockCount;

        public AnchoredChainData(String txHash, int txCount, String stateRootHash, String receiptHash, String parentHash, int blockNumber, String blockHash, int blockCount) {
            this.txHash = txHash;
            this.txCount = txCount;
            this.stateRootHash = stateRootHash;
            this.receiptHash = receiptHash;
            this.parentHash = parentHash;
            this.blockNumber = blockNumber;
            this.blockHash = blockHash;
            this.blockCount = blockCount;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public int getTxCount() {
            return txCount;
        }

        public void setTxCount(int txCount) {
            this.txCount = txCount;
        }

        public String getStateRootHash() {
            return stateRootHash;
        }

        public void setStateRootHash(String stateRootHash) {
            this.stateRootHash = stateRootHash;
        }

        public String getReceiptHash() {
            return receiptHash;
        }

        public void setReceiptHash(String receiptHash) {
            this.receiptHash = receiptHash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public int getBlockCount() {
            return blockCount;
        }

        public void setBlockCount(int blockCount) {
            this.blockCount = blockCount;
        }
    }
}
