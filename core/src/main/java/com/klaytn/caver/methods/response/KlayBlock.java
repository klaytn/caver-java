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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/EthBlock.java (2019/06/13).
 * Modified and improved for the caver-java development.
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
import java.util.List;

/**
 * A block object, or null when no block was found
 */
public class KlayBlock extends Response<KlayBlock.Block> {

    @Override
    @JsonDeserialize(using = KlayBlock.ResponseDeserialiser.class)
    public void setResult(KlayBlock.Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {

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
         * The number of transactions made by the sender prior to this one.
         */
        private String nonce;

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
         * The address of the beneficiary to whom the mining rewards were given
         */
        private String miner;

        /**
         * Integer of the difficulty for this block
         */
        private String difficulty;

        /**
         * Integer of the total difficulty of the chain until this block
         */
        private String totalDifficulty;

        /**
         * The "extra data" field of this block
         */
        private String extraData;

        /**
         * Integer the size of this block in bytes
         */
        private String size;

        /**
         * The maximum gas allowed in this block
         */
        private String gasLimit;

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
        private List<KlayTransaction.Transaction> transactions;

        /**
         * RLP encoded governance configuration
         */
        private String governanceData;

        /**
         * RLP encoded governance vote of the proposer
         */
        private String voteData;

        private String reward;

        public Block() {
        }

        public Block(String number, String hash, String parentHash, String nonce, String logsBloom,
                     String transactionsRoot, String stateRoot, String receiptsRoot, String miner,
                     String difficulty, String totalDifficulty, String extraData, String size,
                     String gasLimit, String gasUsed, String timestamp, String timestampFoS,
                     List<KlayTransaction.Transaction> transactions, String governanceData, String voteData, String reward) {
            this.number = number;
            this.hash = hash;
            this.parentHash = parentHash;
            this.nonce = nonce;
            this.logsBloom = logsBloom;
            this.transactionsRoot = transactionsRoot;
            this.stateRoot = stateRoot;
            this.receiptsRoot = receiptsRoot;
            this.miner = miner;
            this.difficulty = difficulty;
            this.totalDifficulty = totalDifficulty;
            this.extraData = extraData;
            this.size = size;
            this.gasLimit = gasLimit;
            this.gasUsed = gasUsed;
            this.timestamp = timestamp;
            this.timestampFoS = timestampFoS;
            this.transactions = transactions;
            this.governanceData = governanceData;
            this.voteData = voteData;
            this.reward = reward;
        }

        public String getNumber() {
            return number;
        }

        public String getHash() {
            return hash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public String getNonce() {
            return nonce;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public String getMiner() {
            return miner;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public String getTotalDifficulty() {
            return totalDifficulty;
        }

        public String getExtraData() {
            return extraData;
        }

        public String getSize() {
            return size;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getTimestampFoS() {
            return timestampFoS;
        }

        public List<KlayTransaction.Transaction> getTransactions() {
            return transactions;
        }

        public String getGovernanceData() {
            return governanceData;
        }

        public String getVoteData() {
            return voteData;
        }

        public String getReward() {
            return reward;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Block)) {
                return false;
            }

            Block that = (Block) o;

            if (getNumber() != null
                    ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) {
                return false;
            }
            if (getHash() != null
                    ? !getHash().equals(that.getHash()) : that.getHash() != null) {
                return false;
            }
            if (getParentHash() != null
                    ? !getParentHash().equals(that.getParentHash()) : that.getParentHash() != null) {
                return false;
            }
            if (getNonce() != null
                    ? !getNonce().equals(that.getNonce()) : that.getNonce() != null) {
                return false;
            }
            if (getLogsBloom() != null
                    ? !getLogsBloom().equals(that.getLogsBloom()) : that.getLogsBloom() != null) {
                return false;
            }
            if (getTransactionsRoot() != null
                    ? !getTransactionsRoot().equals(that.getTransactionsRoot()) : that.getTransactionsRoot() != null) {
                return false;
            }
            if (getStateRoot() != null ? !getStateRoot().equals(that.getStateRoot()) : that.getStateRoot() != null) {
                return false;
            }
            if (getReceiptsRoot() != null ? !getReceiptsRoot().equals(that.getReceiptsRoot()) : that.getReceiptsRoot() != null) {
                return false;
            }
            if (getMiner() != null ? !getMiner().equals(that.getMiner()) : that.getMiner() != null) {
                return false;
            }
            if (getDifficulty() != null ? !getDifficulty().equals(that.getDifficulty()) : that.getDifficulty() != null) {
                return false;
            }
            if (getTotalDifficulty() != null ? !getTotalDifficulty().equals(that.getTotalDifficulty()) : that.getTotalDifficulty() != null) {
                return false;
            }
            if (getExtraData() != null ? !getExtraData().equals(that.getExtraData()) : that.getExtraData() != null) {
                return false;
            }
            if (getSize() != null ? !getSize().equals(that.getSize()) : that.getSize() != null) {
                return false;
            }
            if (getGasLimit() != null ? !getGasLimit().equals(that.getGasLimit()) : that.getGasLimit() != null) {
                return false;
            }
            if (getGasUsed() != null ? !getGasUsed().equals(that.getGasUsed()) : that.getGasUsed() != null) {
                return false;
            }
            if (getTimestamp() != null ? !getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() != null) {
                return false;
            }
            if (getTimestampFoS() != null ? !getTimestampFoS().equals(that.getTimestampFoS()) : that.getTimestampFoS() != null) {
                return false;
            }
            if (getTransactions() != null ? !getTransactions().equals(that.getTransactions()) : that.getTransactions() != null) {
                return false;
            }
            if (getGovernanceData() != null ? !getGovernanceData().equals(that.getGovernanceData()) : that.getGovernanceData() != null) {
                return false;
            }
            if (getVoteData() != null ? !getVoteData().equals(that.getVoteData()) : that.getVoteData() != null) {
                return false;
            }
            return getReward() != null ? getReward().equals(that.getReward()) : that.getReward() == null;
        }

        @Override
        public int hashCode() {
            int result = getNumber() != null ? getNumber().hashCode() : 0;
            result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
            result = 31 * result + (getParentHash() != null ? getParentHash().hashCode() : 0);
            result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
            result = 31 * result + (getLogsBloom() != null ? getLogsBloom().hashCode() : 0);
            result = 31 * result + (getTransactionsRoot() != null ? getTransactionsRoot().hashCode() : 0);
            result = 31 * result + (getStateRoot() != null ? getStateRoot().hashCode() : 0);
            result = 31 * result + (getReceiptsRoot() != null ? getReceiptsRoot().hashCode() : 0);
            result = 31 * result + (getMiner() != null ? getMiner().hashCode() : 0);
            result = 31 * result + (getDifficulty() != null ? getDifficulty().hashCode() : 0);
            result = 31 * result + (getTotalDifficulty() != null ? getTotalDifficulty().hashCode() : 0);
            result = 31 * result + (getExtraData() != null ? getExtraData().hashCode() : 0);
            result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
            result = 31 * result + (getGasLimit() != null ? getGasLimit().hashCode() : 0);
            result = 31 * result + (getGasUsed() != null ? getGasUsed().hashCode() : 0);
            result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
            result = 31 * result + (getTimestampFoS() != null ? getTimestampFoS().hashCode() : 0);
            result = 31 * result + (getTransactions() != null ? getTransactions().hashCode() : 0);
            result = 31 * result + (getGovernanceData() != null ? getGovernanceData().hashCode() : 0);
            result = 31 * result + (getVoteData() != null ? getVoteData().hashCode() : 0);
            result = 31 * result + (getReward() != null ? getReward().hashCode() : 0);
            return result;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Block deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Block.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
