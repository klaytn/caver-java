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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;

public class BlockHeader extends Response<BlockHeader.BlockHeaderData> {

    @Override
    public void setResult(BlockHeaderData result) {
        super.setResult(result);
    }

    public static class BlockHeaderData<T> {
        /**
         * The block number. null when its pending block
         */
        private String number;

        /**
         * Hash of the block. null when its pending block
         */
        private String hash;

        /**
         * Hash of the parent block
         */
        private String parentHash;

        /**
         * The bloom filter for the logs of the block. null when its pending block
         */
        private String logsBloom;

        /**
         * The root of the transaction trie of the block
         */
        private String transactionsRoot;

        /**
         * The root of the final state trie of the block
         */
        private String stateRoot;

        /**
         * The root of the receipts trie of the block
         */
        private String receiptsRoot;

        /**
         * The address of the beneficiary to whom the block rewards were given.
         */
        private String reward;

        /**
         * Former difficulty. Always 1 in the BFT consensus engine
         */
        private String blockScore;

        /**
         * The "extra data" field of this block
         */
        private String extraData;

        /**
         * The total used gas by all transactions in this block
         */
        private String gasUsed;

        /**
         * The Unix timestamp for when the block was collated
         */
        private String timestamp;

        /**
         * The fraction of a second of the timestamp for when the block was collated
         */
        private String timestampFoS;

        /**
         * RLP encoded governance configuration
         */
        private String governanceData;

        /**
         * Base fee per gas
         */
        private String baseFeePerGas;

        public BlockHeaderData() {
        }

        public BlockHeaderData(String number, String hash, String parentHash, String logsBloom, String transactionsRoot, String stateRoot, String receiptsRoot, String reward, String blockScore, String extraData, String gasUsed, String timestamp, String timestampFoS, String governanceData, String baseFeePerGas) {
            this.number = number;
            this.hash = hash;
            this.parentHash = parentHash;
            this.logsBloom = logsBloom;
            this.transactionsRoot = transactionsRoot;
            this.stateRoot = stateRoot;
            this.receiptsRoot = receiptsRoot;
            this.reward = reward;
            this.blockScore = blockScore;
            this.extraData = extraData;
            this.gasUsed = gasUsed;
            this.timestamp = timestamp;
            this.timestampFoS = timestampFoS;
            this.governanceData = governanceData;
            this.baseFeePerGas = baseFeePerGas;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestampFoS() {
            return timestampFoS;
        }

        public void setTimestampFoS(String timestampFoS) {
            this.timestampFoS = timestampFoS;
        }

        public String getGovernanceData() {
            return governanceData;
        }

        public void setGovernanceData(String governanceData) {
            this.governanceData = governanceData;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getBlockScore() {
            return blockScore;
        }

        public void setBlockScore(String blockScore) {
            this.blockScore = blockScore;
        }

        public String getBaseFeePerGas() {
            return baseFeePerGas;
        }

        public void setBaseFeePerGas(String baseFeePerGas) {
            this.baseFeePerGas = baseFeePerGas;
        }
    }
}
