package com.klaytn.caver.methods.response;

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
        private List transactions;

        /**
         * RLP encoded governance configuration
         */
        private String governanceData;

        /**
         * RLP encoded governance vote of the proposer
         */
        private String voteData;

        /**
         * The address of the beneficiary to whom the block rewards were given.
         */
        private String reward;

        public BlockData() {
        }

        public BlockData(String number, String hash, String parentHash, String nonce, String logsBloom, String transactionsRoot, String stateRoot, String receiptsRoot, String miner, String difficulty, String totalDifficulty, String extraData, String size, String gasLimit, String gasUsed, String timestamp, String timestampFoS, List transactions, String governanceData, String voteData, String reward) {
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

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
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

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getTotalDifficulty() {
            return totalDifficulty;
        }

        public void setTotalDifficulty(String totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
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

        public String getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
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
