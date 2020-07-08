/*
 * Copyright 2019 The caver-java Authors
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
import java.util.List;

/**
 * @deprecated Please use {@link BlockWithConsensusInfo} instead.
 */
@Deprecated
public class KlayBlockWithConsensusInfo extends Response<KlayBlockWithConsensusInfo.Block> {

    @Override
    @JsonDeserialize(using = KlayBlockWithConsensusInfo.ResponseDeserialiser.class)
    public void setResult(KlayBlockWithConsensusInfo.Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {

        /**
         * Array of addresses of committee members of this block. The committee is a subset of
         * validators participated in the consensus protocol for this block
         */
        private List<String> committee;

        /**
         * The maximum gas allowed in this block
         */
        private String gasLimit;

        /**
         * The total used gas by all transactions in this block
         */
        private String gasUsed;

        /**
         * Hash of the block. null when its pending block
         */
        private String hash;

        /**
         * The address of the beneficiary to whom the mining rewards were given
         */
        private String miner;

        /**
         * The number of transactions made by the sender prior to this one.
         */
        private String nonce;

        /**
         * The block number. null when its pending block
         */
        private String number;

        /**
         * Hash of the parent block
         */
        private String parentHash;

        /**
         * The address of the block proposer
         */
        private String proposer;

        /**
         * The root of the receipts trie of the block
         */
        private String receiptsRoot;

        /**
         * Integer the size of this block in bytes
         */
        private String size;

        /**
         * The root of the final state trie of the block
         */
        private String stateRoot;

        /**
         * The Unix timestamp for when the block was collated
         */
        private String timestamp;

        /**
         * The fraction of a second of the timestamp for when the block was collated
         */
        private String timestampFoS;

        /**
         * Array of transaction objects
         */
        private List<KlayTransaction.Transaction> transactions;

        /**
         * The root of the transaction trie of the block
         */
        private String transactionsRoot;

        public Block() {
        }

        public Block(List<String> committee, String gasLimit, String gasUsed, String hash, String miner,
                     String nonce, String number, String parentHash, String proposer, String receiptsRoot,
                     String size, String stateRoot, String timestamp, String timestampFoS,
                     List<KlayTransaction.Transaction> transactions, String transactionsRoot) {
            this.committee = committee;
            this.gasLimit = gasLimit;
            this.gasUsed = gasUsed;
            this.hash = hash;
            this.miner = miner;
            this.nonce = nonce;
            this.number = number;
            this.parentHash = parentHash;
            this.proposer = proposer;
            this.receiptsRoot = receiptsRoot;
            this.size = size;
            this.stateRoot = stateRoot;
            this.timestamp = timestamp;
            this.timestampFoS = timestampFoS;
            this.transactions = transactions;
            this.transactionsRoot = transactionsRoot;
        }

        public List<String> getCommittee() {
            return committee;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public String getHash() {
            return hash;
        }

        public String getMiner() {
            return miner;
        }

        public String getNonce() {
            return nonce;
        }

        public String getNumber() {
            return number;
        }

        public String getParentHash() {
            return parentHash;
        }

        public String getProposer() {
            return proposer;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public String getSize() {
            return size;
        }

        public String getStateRoot() {
            return stateRoot;
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

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof KlayBlockWithConsensusInfo.Block)) {
                return false;
            }

            KlayBlockWithConsensusInfo.Block that = (KlayBlockWithConsensusInfo.Block) o;

            if (getCommittee() != null
                    ? !getCommittee().equals(that.getCommittee()) : that.getCommittee() != null) {
                return false;
            }
            if (getGasLimit() != null
                    ? !getGasLimit().equals(that.getGasLimit()) : that.getGasLimit() != null) {
                return false;
            }
            if (getGasUsed() != null
                    ? !getGasUsed().equals(that.getGasUsed()) : that.getGasUsed() != null) {
                return false;
            }
            if (getHash() != null
                    ? !getHash().equals(that.getHash()) : that.getHash() != null) {
                return false;
            }
            if (getMiner() != null
                    ? !getMiner().equals(that.getMiner()) : that.getMiner() != null) {
                return false;
            }
            if (getNonce() != null
                    ? !getNonce().equals(that.getNonce()) : that.getNonce() != null) {
                return false;
            }
            if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) {
                return false;
            }
            if (getParentHash() != null ? !getParentHash().equals(that.getParentHash()) : that.getParentHash() != null) {
                return false;
            }
            if (getProposer() != null ? !getProposer().equals(that.getProposer()) : that.getProposer() != null) {
                return false;
            }
            if (getReceiptsRoot() != null ? !getReceiptsRoot().equals(that.getReceiptsRoot()) : that.getReceiptsRoot() != null) {
                return false;
            }
            if (getSize() != null ? !getSize().equals(that.getSize()) : that.getSize() != null) {
                return false;
            }
            if (getStateRoot() != null ? !getStateRoot().equals(that.getStateRoot()) : that.getStateRoot() != null) {
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
            return getTransactionsRoot() != null ? getTransactionsRoot().equals(that.getTransactionsRoot()) : that.getTransactionsRoot() == null;
        }

        @Override
        public int hashCode() {
            int result = getCommittee() != null ? getCommittee().hashCode() : 0;
            result = 31 * result + (getGasLimit() != null ? getGasLimit().hashCode() : 0);
            result = 31 * result + (getGasUsed() != null ? getGasUsed().hashCode() : 0);
            result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
            result = 31 * result + (getMiner() != null ? getMiner().hashCode() : 0);
            result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
            result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
            result = 31 * result + (getParentHash() != null ? getParentHash().hashCode() : 0);
            result = 31 * result + (getProposer() != null ? getProposer().hashCode() : 0);
            result = 31 * result + (getReceiptsRoot() != null ? getReceiptsRoot().hashCode() : 0);
            result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
            result = 31 * result + (getStateRoot() != null ? getStateRoot().hashCode() : 0);
            result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
            result = 31 * result + (getTimestampFoS() != null ? getTimestampFoS().hashCode() : 0);
            result = 31 * result + (getTransactions() != null ? getTransactions().hashCode() : 0);
            result = 31 * result + (getTransactionsRoot() != null ? getTransactionsRoot().hashCode() : 0);
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
