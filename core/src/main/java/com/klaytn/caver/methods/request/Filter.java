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
 * This file is derived from web3j/core/src/main/java/org/web3j/protocol/core/methods/request/Filter.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Filter<T extends Filter> {
    private T thisObj;
    private List<Filter.FilterTopic> topics;

    Filter() {
        thisObj = getThis();
        topics = new ArrayList<>();
    }

    public T addSingleTopic(String topic) {
        topics.add(new Filter.SingleTopic(topic));
        return getThis();
    }

    public T addNullTopic() {
        topics.add(new Filter.SingleTopic());
        return getThis();
    }

    public T addOptionalTopics(String... optionalTopics) {
        topics.add(new Filter.ListTopic(optionalTopics));
        return getThis();
    }

    public List<Filter.FilterTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<FilterTopic> topics) {
        this.topics = topics;
    }

    abstract T getThis();

    public interface FilterTopic<T> {
        @JsonValue
        T getValue();
    }

    public static class SingleTopic implements Filter.FilterTopic<String> {

        private String topic;

        public SingleTopic() {
            this.topic = null;  // null topic
        }

        public SingleTopic(String topic) {
            this.topic = topic;
        }

        @Override
        public String getValue() {
            return topic;
        }
    }

    public static class ListTopic implements Filter.FilterTopic<List<Filter.SingleTopic>> {
        private List<Filter.SingleTopic> topics;

        public ListTopic(String... optionalTopics) {
            this(Arrays.asList(optionalTopics));
        }

        public ListTopic(List<String> optionalTopics) {
            topics = new ArrayList<>();
            for (String topic : optionalTopics) {
                if (topic != null) {
                    topics.add(new Filter.SingleTopic(topic));
                } else {
                    topics.add(new Filter.SingleTopic());
                }
            }
        }

        @Override
        public List<Filter.SingleTopic> getValue() {
            return topics;
        }
    }
}
