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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/EthLog.java (2019/06/13).
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
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Array of log objects, or an empty array if nothing has changed since last poll.
 */
public class KlayLogs extends Response<List<KlayLogs.LogResult>> {

    @Override
    @JsonDeserialize(using = LogResultDeserialiser.class)
    public void setResult(List<LogResult> result) {
        super.setResult(result);
    }

    public List<LogResult> getLogs() {
        return getResult();
    }

    public interface LogResult<T> {
        T get();
    }

    public static class LogObject extends Log implements LogResult<Log> {

        public LogObject() {
        }

        public LogObject(String logIndex, String transactionIndex,
                         String transactionHash, String blockHash, String blockNumber,
                         String address, String data, List<String> topics) {
            super(logIndex, transactionIndex, transactionHash, blockHash, blockNumber,
                    address, data, topics);
        }

        @Override
        public Log get() {
            return this;
        }
    }


    public static class Hash implements LogResult<String> {
        private String value;

        public Hash() {
        }

        public Hash(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Hash)) {
                return false;
            }

            Hash hash = (Hash) o;

            return value != null ? value.equals(hash.value) : hash.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static class Log {

        /**
         * Integer of the log index position in the block. null when it is a pending log
         */
        private String logIndex;

        /**
         * Integer of the transactions index position log was created from. null when pending
         */
        private String transactionIndex;

        /**
         * Hash of the transactions this log was created from. null when pending
         */
        private String transactionHash;

        /**
         * Hash of the block where this log was in. null when pending
         */
        private String blockHash;

        /**
         * The block number where this log was in. null when pending
         */
        private String blockNumber;

        /**
         * Address from which this log originated
         */
        private String address;

        /**
         * Contains the non-indexed arguments of the log
         */
        private String data;

        /**
         * Array of 0 to 4 32-byte DATA of indexed log arguments. (In Solidity: The first topic is
         * the hash of the signature of the event (e.g., Deposit(address,bytes32,uint256)),
         * except you declared the event with the anonymous specifier.).
         */
        private List<String> topics;

        /**
         * true when the log was removed, due to a chain reorganization. false if it is a valid log.
         */
        private boolean removed;

        public Log() {
        }

        public Log(String logIndex, String transactionIndex, String transactionHash,
                   String blockHash, String blockNumber, String address, String data,
                   List<String> topics) {
            this.logIndex = logIndex;
            this.transactionIndex = transactionIndex;
            this.transactionHash = transactionHash;
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.address = address;
            this.data = data;
            this.topics = topics;
        }

        public BigInteger getLogIndex() {
            return convert(logIndex);
        }

        public String getLogIndexRaw() {
            return logIndex;
        }

        public void setLogIndex(String logIndex) {
            this.logIndex = logIndex;
        }

        public BigInteger getTransactionIndex() {
            return convert(transactionIndex);
        }

        public String getTransactionIndexRaw() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public BigInteger getBlockNumber() {
            return convert(blockNumber);
        }

        public String getBlockNumberRaw() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        private BigInteger convert(String value) {
            if (value != null) {
                return Numeric.decodeQuantity(value);
            } else {
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Log)) {
                return false;
            }

            Log that = (Log) o;

            if (getLogIndexRaw() != null
                    ? !getLogIndexRaw().equals(that.getLogIndexRaw()) : that.getLogIndexRaw() != null) {
                return false;
            }
            if (getTransactionIndexRaw() != null
                    ? !getTransactionIndexRaw().equals(that.getTransactionIndexRaw())
                    : that.getTransactionIndexRaw() != null) {
                return false;
            }
            if (getTransactionHash() != null
                    ? !getTransactionHash().equals(that.getTransactionHash())
                    : that.getTransactionHash() != null) {
                return false;
            }
            if (getBlockHash() != null
                    ? !getBlockHash().equals(that.getBlockHash()) : that.getBlockHash() != null) {
                return false;
            }
            if (getBlockNumberRaw() != null
                    ? !getBlockNumberRaw().equals(that.getBlockNumberRaw())
                    : that.getBlockNumberRaw() != null) {
                return false;
            }
            if (getAddress() != null
                    ? !getAddress().equals(that.getAddress()) : that.getAddress() != null) {
                return false;
            }
            if (getData() != null ? !getData().equals(that.getData()) : that.getData() != null) {
                return false;
            }
            return getTopics() != null ? getTopics().equals(that.getTopics()) : that.getTopics() == null;
        }

        @Override
        public int hashCode() {
            int result = getLogIndexRaw() != null ? getLogIndexRaw().hashCode() : 0;
            result = 31 * result + (getTransactionIndexRaw() != null ? getTransactionIndexRaw().hashCode() : 0);
            result = 31 * result + (getTransactionHash() != null ? getTransactionHash().hashCode() : 0);
            result = 31 * result + (getBlockHash() != null ? getBlockHash().hashCode() : 0);
            result = 31 * result + (getBlockNumberRaw() != null ? getBlockNumberRaw().hashCode() : 0);
            result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
            result = 31 * result + (getData() != null ? getData().hashCode() : 0);
            result = 31 * result + (getTopics() != null ? getTopics().hashCode() : 0);
            return result;
        }
    }

    public static class LogResultDeserialiser extends JsonDeserializer<List<LogResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<LogResult> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<LogResult> logResults = new ArrayList<>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<LogObject> logObjectIterator =
                        objectReader.readValues(jsonParser, LogObject.class);
                while (logObjectIterator.hasNext()) {
                    logResults.add(logObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<KlayLogs.Hash> transactionHashIterator =
                        objectReader.readValues(jsonParser, Hash.class);
                while (transactionHashIterator.hasNext()) {
                    logResults.add(transactionHashIterator.next());
                }
            }
            return logResults;
        }
    }
}
