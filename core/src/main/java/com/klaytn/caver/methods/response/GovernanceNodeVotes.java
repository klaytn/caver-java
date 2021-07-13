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

import com.klaytn.caver.rpc.Governance;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * The class represented to store the response data of the {@link Governance#getVotes()} method.
 */
public class GovernanceNodeVotes extends Response<List<GovernanceNodeVotes.NodeVote>> {
    public static class NodeVote implements IVote{
        /**
         * The value of key field in NodeVote.
         */
        String key;

        /**
         * The value of validator field in NodeVote.
         */
        String validator;

        /**
         * The value of value field in NodeVote.
         */
        Object value;

        public NodeVote() {
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
         * Setter function for key
         * @param key The value of key field.
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Getter function for validator.
         * @return String
         */
        public String getValidator() {
            return validator;
        }

        /**
         * Setter function for validator
         * @param validator The value of validator field.
         */
        public void setValidator(String validator) {
            this.validator = validator;
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
         * @param value The value of value field.
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
