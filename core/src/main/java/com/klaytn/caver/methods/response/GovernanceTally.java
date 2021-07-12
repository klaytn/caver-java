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
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.util.List;

public class GovernanceTally extends Response<List<GovernanceTally.TallyData>> {

    public static class TallyData implements IVote {
        @JsonProperty("Key")
        String key;
        @JsonProperty("Value")
        Object value;
        @JsonProperty("ApprovalPercentage")
        Float approvalPercentage;

        public TallyData() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Float getApprovalPercentage() {
            return approvalPercentage;
        }

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
