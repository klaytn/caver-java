/*
 * Copyright 2021 The caver-java Authors
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
import com.klaytn.caver.rpc.Governance;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.math.BigInteger;
import java.util.List;

/**
 * The class represented to store the response data of the {@link Governance#getMyVotes()} method.
 */
public class GovernanceMyVotes extends Response<List<GovernanceMyVotes.MyVote>> {

    public static class MyVote implements IVote {
        /**
         * The block number in MyVote
         */
        @JsonProperty("BlockNum")
        BigInteger blockNum;

        /**
         * The value of casted field in MyVote.
         */
        @JsonProperty("Casted")
        boolean casted;

        /**
         * The value of key field in MyVote.
         */
        @JsonProperty("Key")
        String key;

        /**
         * The value of Value field in MyVote.
         */
        @JsonProperty("Value")
        Object value;

        public MyVote() {
        }

        /**
         * Getter function for blockNum
         * @return BigInteger
         */
        public BigInteger getBlockNum() {
            return blockNum;
        }

        /**
         * Setter function for blockNum
         * @param blockNum  The block number in MyVote
         */
        public void setBlockNum(BigInteger blockNum) {
            this.blockNum = blockNum;
        }

        /**
         * Getter function for catsed.
         * @return boolean
         */
        public boolean isCasted() {
            return casted;
        }

        /**
         * Setter function for casted.
         * @param casted The value of casted in MyVote.
         */
        public void setCasted(boolean casted) {
            this.casted = casted;
        }

        /**
         * Getter function for key.
         * @return String
         */
        @Override
        public String getKey() {
            return key;
        }

        /**
         * Setter function for key.
         * @param key The value of key field in MyVote.
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Getter function for value.
         * @return Object
         */
        @Override
        public Object getValue() {
            return value;
        }

        /**
         * Setter function for value.
         * @param value The value of value field in MyVote.
         */
        public void setValue(Object value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return Utils.printString(this);
        }
    }
    @Override
    public String toString() {
        return Utils.printString(this);
    }
}
