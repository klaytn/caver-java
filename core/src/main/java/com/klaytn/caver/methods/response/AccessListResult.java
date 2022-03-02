/*
 * Copyright 2022 The caver-java Authors
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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.transaction.type.transactionUtils.AccessTuple;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccessListResult extends Response<AccessListResult.AccessListResultData> {

    @Override
    public void setResult(AccessListResultData result) {
        super.setResult(result);
    }

    @JsonDeserialize(using = AccessListResultData.AccessListResultDeserializer.class)
    public static class AccessListResultData {
        List<AccessTuple> accessList;
        String error;
        String gasUsed;

        public AccessListResultData(List<AccessTuple> accessList, String error, String gasUsed) {
            this.accessList = accessList;
            this.error = error;
            this.gasUsed = gasUsed;
        }

        public List<AccessTuple> getAccessList() {
            return accessList;
        }

        public void setAccessList(List<AccessTuple> accessList) {
            this.accessList = accessList;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public static class AccessListResultDeserializer extends JsonDeserializer<AccessListResultData> {
            @Override
            public AccessListResultData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode node = p.getCodec().readTree(p);

                List<AccessTuple> accessList = new ArrayList<>();
                JsonNode accessListNode = node.get("accessList");
                for (JsonNode innerNode : accessListNode) {
                    AccessTuple accessTuple = objectMapper.treeToValue(innerNode, AccessTuple.class);
                    accessList.add(accessTuple);
                }

                AccessListResultData accessListResultData = new AccessListResultData(accessList, null, "0x0");
                JsonNode error = node.get("error");
                if (error != null) {
                    accessListResultData.setError(error.textValue());
                }
                JsonNode gasUsed = node.get("gasUsed");
                if (gasUsed != null) {
                    accessListResultData.setGasUsed(gasUsed.textValue());
                }
                return accessListResultData;
            }
        }
    }
}
