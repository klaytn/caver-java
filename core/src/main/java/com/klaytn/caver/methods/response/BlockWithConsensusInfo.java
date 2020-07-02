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

public class BlockWithConsensusInfo extends Response<BlockWithConsensusInfo.Block> {

    @Override
    @JsonDeserialize(using = BlockWithConsensusInfo.ResponseDeserializer.class)
    public void setResult(Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {

        /**
         * Former difficulty. Always 1 in the BFT consensus engine
         */
        private String blockScore;

        /**
         * Integer of the total blockScore of the chain until this block.
         */
        private String totalBlockScore;

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
        private List<Transaction.TransactionData> transactions;

        /**
         * The root of the transaction trie of the block
         */
        private String transactionsRoot;

        public Block() {
        }

        public Block(String blockScore, String totalBlockScore, List<String> committee, String gasLimit, String gasUsed, String hash, String miner, String nonce, String number, String parentHash, String proposer, String receiptsRoot, String size, String stateRoot, String timestamp, String timestampFoS, List<Transaction.TransactionData> transactions, String transactionsRoot) {
            this.blockScore = blockScore;
            this.totalBlockScore = totalBlockScore;
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

        public List<String> getCommittee() {
            return committee;
        }

        public void setCommittee(List<String> committee) {
            this.committee = committee;
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

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getProposer() {
            return proposer;
        }

        public void setProposer(String proposer) {
            this.proposer = proposer;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
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

        public List<Transaction.TransactionData> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<Transaction.TransactionData> transactions) {
            this.transactions = transactions;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }
    }

    public static class ResponseDeserializer extends JsonDeserializer<BlockWithConsensusInfo.Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public BlockWithConsensusInfo.Block deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, BlockWithConsensusInfo.Block.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
