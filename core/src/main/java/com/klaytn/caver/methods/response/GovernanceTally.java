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

import java.util.List;

/**
 * The class represented to store the response data of the {@link Governance#showTally()} method.
 */
public class GovernanceTally extends Response<List<GovernanceTally.TallyData>> {

    public static class TallyData implements IVote {

        /**
         * The vote key field in TallyData.
         */
        @JsonProperty("Key")
        String key;

        /**
         * The value in TallyData.
         */
        @JsonProperty("Value")
        Object value;

        /**
         * The approval percentage of ApprovalPercentage field.
         */
        @JsonProperty("ApprovalPercentage")
        Float approvalPercentage;

        public TallyData() {
        }

        /**
         * Getter function for key
         * @return String
         */
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
         * Getter function for value.
         * @return Object
         */
        public Object getValue() {
            return value;
        }

        /**
         * Setter function for value
         * @param value The value of value field.
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Getter function for approvalPercentage.
         * @return Float
         */
        public Float getApprovalPercentage() {
            return approvalPercentage;
        }

        /**
         * Setter function for approvalPercentage
         * @param approvalPercentage The value of approvalPercentage field.
         */
        public void setApprovalPercentage(Float approvalPercentage) {
            this.approvalPercentage = approvalPercentage;
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
