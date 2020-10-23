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

package com.klaytn.caver.contract;

import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EventFilterOptions {
    List<IndexedParameter> filterOptions;
    List topics;

    public static List convertsTopic(ContractEvent event, EventFilterOptions options) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<IndexedParameter> filterOption = options.getFilterOptions();

        int indexed = 0;
        for(ContractIOType ioType : event.getInputs()){
            if(ioType.indexed) {
                indexed++;
            }
        }

        Object[] topics = new Object[indexed+1];
        topics[0] = ABI.encodeEventSignature(event);
        for(IndexedParameter indexedParameter : filterOption) {
            int index = 0;
            ContractIOType contractIOType = null;
            for(index=0; index<event.getInputs().size(); index++) {
                ContractIOType ioType = event.getInputs().get(index);
                if(ioType.getName().equals(indexedParameter.getIndexedParamName())) {
                    if(!ioType.isIndexed()) {
                        throw new IllegalArgumentException("Non indexed event parameter : " + indexedParameter.getIndexedParamName());
                    }
                    contractIOType = event.getInputs().get(index);
                    break;
                }
            }

            if(contractIOType == null) {
                throw new IllegalArgumentException("Not exist event parameter : " + indexedParameter.getIndexedParamName());
            }

            List<String> topicValue = indexedParameter.makeTopic(contractIOType.getType());
            if(topicValue.size() == 1) {
                topics[index+1] = topicValue.get(0);
            } else {
                topics[index+1] = topicValue;
            }
        }

        return Arrays.asList(topics);
    }

    public EventFilterOptions(List<IndexedParameter> filterOptions, List<String> topics) {
        this.filterOptions = filterOptions;
        this.topics = topics;
    }

    public List<IndexedParameter> getFilterOptions() {
        return filterOptions;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setFilterOptions(List<IndexedParameter> filterOptions) {
        this.filterOptions = filterOptions;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public static class IndexedParameter {
        String indexedParamName;
        List<Object> filterValue;

        public List<String> makeTopic(String solType) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            List<String> topicValue = new ArrayList<>();
            for(Object value : this.getFilterValue()) {
                if(value instanceof List) {
                    List<Object> filter = (List<Object>)value;

                    for(int i=0; i<filter.size(); i++) {
                        String encoded = ABI.encodeParameter(solType, filter.get(i));
                        topicValue.add(Utils.addHexPrefix(encoded));
                    }
                } else {
                    String encoded = ABI.encodeParameter(solType, value);
                    topicValue.add(Utils.addHexPrefix(encoded));
                }
            }

            return topicValue;
        }

        public IndexedParameter(String indexedParamName, List<Object> filterValue) {
            this.indexedParamName = indexedParamName;
            this.filterValue = filterValue;
        }

        public String getIndexedParamName() {
            return indexedParamName;
        }

        public List<Object> getFilterValue() {
            return filterValue;
        }

        public void setIndexedParamName(String indexedParamName) {
            this.indexedParamName = indexedParamName;
        }

        public void setFilterValue(List<Object> filterValue) {
            this.filterValue = filterValue;
        }
    }
}
