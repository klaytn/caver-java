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
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Block extends Response<Block.BlockData> {

    @Override
    @JsonDeserialize(using = Block.ResponseDeserializer.class)
    public void setResult(BlockData result) {
        super.setResult(result);
    }

    public static class BlockData<T> {
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
         * Integer of the total blockScore of the chain until this block.
         */
        private String totalBlockScore;

        /**
         * The "extra data" field of this block
         */
        private String extraData;

        /**
         * Integer the size of this block in bytes
         */
        private String size;

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
         * Array of transaction objects, or 32-byte transaction hashes depending on the last given parameter
         */
        private List transactions;

        /**
         * RLP encoded governance configuration
         */
        private String governanceData;

        /**
         * RLP encoded governance vote of the proposer
         */
        private String voteData;

        public BlockData() {
        }

        public BlockData(String number, String hash, String parentHash, String logsBloom, String transactionsRoot, String stateRoot, String receiptsRoot, String reward, String blockScore, String totalBlockScore, String extraData, String size, String gasUsed, String timestamp, String timestampFoS, List transactions, String governanceData, String voteData) {
            this.number = number;
            this.hash = hash;
            this.parentHash = parentHash;
            this.logsBloom = logsBloom;
            this.transactionsRoot = transactionsRoot;
            this.stateRoot = stateRoot;
            this.receiptsRoot = receiptsRoot;
            this.reward = reward;
            this.blockScore = blockScore;
            this.totalBlockScore = totalBlockScore;
            this.extraData = extraData;
            this.size = size;
            this.gasUsed = gasUsed;
            this.timestamp = timestamp;
            this.timestampFoS = timestampFoS;
            this.transactions = transactions;
            this.governanceData = governanceData;
            this.voteData = voteData;
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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
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

        public List getTransactions() {
            return transactions;
        }

        @JsonSetter("transactions")
        @JsonDeserialize(using = Block.TransactionsDeserializer.class)
        public void setTransactions(List transactions) {
            this.transactions = transactions;
        }

        public String getGovernanceData() {
            return governanceData;
        }

        public void setGovernanceData(String governanceData) {
            this.governanceData = governanceData;
        }

        public String getVoteData() {
            return voteData;
        }

        public void setVoteData(String voteData) {
            this.voteData = voteData;
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

        public String getTotalBlockScore() {
            return totalBlockScore;
        }

        public void setTotalBlockScore(String totalBlockScore) {
            this.totalBlockScore = totalBlockScore;
        }
    }

    public static class ResponseDeserializer extends JsonDeserializer<Block.BlockData> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Block.BlockData deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Block.BlockData.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }

    static class TransactionsDeserializer extends JsonDeserializer<List> {

        @Override
        public List deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ArrayNode root = objectMapper.readTree(jsonParser);

            if (root.size() == 0) {
                return Collections.emptyList();
            } else if (root.get(0).isTextual()) {
                return objectMapper.convertValue(root, new TypeReference<List<String>>() {
                });
            } else {
                return objectMapper.convertValue(root, new TypeReference<List<Transaction.TransactionData>>() {
                });
            }
        }
    }
}
