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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

public class Expected {

    private boolean status;
    private Receipt receipt;
    private String errorString;
    private String errorStringJava;
    private String returns;
    private Events events;

    public Expected() {
    }

    public boolean isStatus() {
        return status;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public String getErrorString() {
        return errorString;
    }

    public String getErrorStringJava() {
        return errorStringJava;
    }

    public String getReturns() {
        return returns;
    }

    public Events getEvents() {
        return events;
    }

    public static class Receipt {
        private boolean checkContractAddress;
        private boolean checkSenderTxHash;
        private String status;
        private String txError;

        public boolean getCheckContractAddress() {
            return checkContractAddress;
        }

        public boolean getCheckSenderTxHash() {
            return checkSenderTxHash;
        }

        public String getStatus() {
            return status;
        }

        public String getTxError() {
            return txError;
        }
    }

    @JsonDeserialize(using = EventDeserializer.class)
    public static class Events {
        private JsonNode values;

        public Events(JsonNode values) {
            this.values = values;
        }

        public JsonNode getValues() {
            return values;
        }
    }

    public static class EventDeserializer extends JsonDeserializer<Events> {

        @Override
        public Events deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
            return new Events(jsonNode);
        }
    }
}
