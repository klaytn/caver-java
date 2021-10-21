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
import com.klaytn.caver.methods.request.Filter;
import com.klaytn.caver.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventFilterOptions {
    List<IndexedParameter> filterOptions;
    List topics;

    /**
     * Converts a filterOptions in options parameter to topic list according to the ABI spec.
     * @param event The contract event to filter.
     * @param options The filterOptions to filter contract event.
     * @return List
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static List convertsTopic(ContractEvent event, EventFilterOptions options) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<IndexedParameter> filterOptions = options.getFilterOptions();
        return convertsTopic(event, filterOptions);
    }

    /**
     * Converts a filterOptions to topic list according to the ABI spec.
     * @param event The contract event to filter.
     * @param filterOptions The list of IndexedParameter instance that consist of parameter name of indexed event parameter and its value.
     * @return List
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static List convertsTopic(ContractEvent event, List<IndexedParameter> filterOptions) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int indexed = 0;
        for(ContractIOType ioType : event.getInputs()){
            if(ioType.indexed) {
                indexed++;
            }
        }

        //topic[0] == Event signature
        //topic[1~3] == indexed parameter
        Object[] topics = new Object[indexed+1];
        topics[0] = ABI.encodeEventSignature(event);

        //Finds information corresponding to the type given by filterOptions and encodes it using this information.
        for(IndexedParameter indexedParameter : filterOptions) {
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

            List<String> topicValue = indexedParameter.makeTopic(contractIOType.getTypeAsString());
            if(topicValue.size() == 1) {
                topics[index+1] = topicValue.get(0);
            } else {
                topics[index+1] = topicValue;
            }
        }

        return Arrays.asList(topics);
    }

    public EventFilterOptions() {
    }

    public EventFilterOptions(List<IndexedParameter> filterOptions, List topics) {
        this.filterOptions = filterOptions;
        this.topics = topics;
    }

    public void convertIndexedParamToTopic(ContractEvent event) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        setTopics(convertsTopic(event, getFilterOptions()));
    }

    public List<Filter.FilterTopic> toKlayFilterTopic() {
        List<Filter.FilterTopic> topicList = new ArrayList<>();
        for(int i=0; i<getTopics().size(); i++) {
            Object topic = getTopics().get(i);
            if(topic instanceof String) {
                topicList.add(new Filter.SingleTopic((String)topic));
            } else if(topic instanceof List) {
                topicList.add(new Filter.ListTopic((List)topic));
            } else {
                topicList.add(new Filter.SingleTopic());
            }
        }

        return topicList;
    }

    public List<IndexedParameter> getFilterOptions() {
        return filterOptions;
    }

    public List getTopics() {
        return topics;
    }

    public void setFilterOptions(List<IndexedParameter> filterOptions) {
        this.filterOptions = filterOptions;
    }

    public void setTopics(List topics) {
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
