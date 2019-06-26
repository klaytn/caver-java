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

package com.klaytn.caver.model.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.model.ApiHelper;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestData {

    private String tcID;
    private String tcName;
    private List<TestComponent> test;

    public String getTcID() {
        return tcID;
    }

    public String getTcName() {
        return tcName;
    }

    public List<TestComponent> getTest() {
        return test;
    }

    @JsonDeserialize(using = ResponseDeserialiser.class)
    public void setTest(List<TestComponent> test) {
        this.test = test;
    }

    public static class ResponseDeserialiser extends JsonDeserializer<List<TestComponent>> {

        private ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        @Override
        public List<TestComponent> deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Iterator<JsonNode> iterator = node.iterator();

            List<TestComponent> testComponents = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode nextNode = iterator.next();
                if (nextNode.has("tx")) {
                    testComponents.add(objectMapper.readValue(nextNode.toString(), Transaction.class));
                }
                if (nextNode.has("api")) {
                    API.InnerAPI api = objectMapper.readValue(nextNode.get("api").toString(), API.InnerAPI.class);
                    Object expected = null;
                    String errorString = null;
                    if (nextNode.has("expected")) {
                        expected = objectMapper.readValue(nextNode.get("expected").toString(), ApiHelper.getModelClass(api.getMethod()));
                    }
                    if (nextNode.has("errorString")) {
                        errorString = nextNode.get("errorString").toString();
                    }
                    testComponents.add(new API(api, expected, errorString));
                }
                if (nextNode.has("send")) {
                    Contract.Component component = objectMapper.readValue(nextNode.get("send").toString(), Contract.Component.class);
                    Expected expected = objectMapper.readValue(nextNode.get("expected").toString(), Expected.class);
                    Contract contract = new Contract(component, expected, Contract.Component.Type.SEND);
                    testComponents.add(contract);
                }
                if (nextNode.has("call")) {
                    Contract.Component component = objectMapper.readValue(nextNode.get("call").toString(), Contract.Component.class);
                    Expected expected = objectMapper.readValue(nextNode.get("expected").toString(), Expected.class);
                    Contract contract = new Contract(component, expected, Contract.Component.Type.CALL);
                    testComponents.add(contract);
                }
            }
            return testComponents;
        }
    }
}
