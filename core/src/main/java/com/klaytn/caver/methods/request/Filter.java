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
    /**
     * The Filter instance.
     */
    private T thisObj;

    /**
     * The list of topic to filter event.
     */
    private List<Filter.FilterTopic> topics;

    /**
     * Creates an Filter instance.
     */
    Filter() {
        thisObj = getThis();
        topics = new ArrayList<>();
    }

    /**
     * Add a single topic in topics field.
     * @param topic The single topic string.
     * @return Filter
     */
    public T addSingleTopic(String topic) {
        topics.add(new Filter.SingleTopic(topic));
        return getThis();
    }

    /**
     * Add an empty topic in topics filed.
     * @return Filter
     */
    public T addNullTopic() {
        topics.add(new Filter.SingleTopic());
        return getThis();
    }

    /**
     * Adds list of topics that means optional topic.
     * @param optionalTopics The list of topic.
     * @return Filter.
     */
    public T addOptionalTopics(String... optionalTopics) {
        topics.add(new Filter.ListTopic(optionalTopics));
        return getThis();
    }

    /**
     * Getter method for topics.
     * @return List
     */
    public List<Filter.FilterTopic> getTopics() {
        return topics;
    }

    /**
     * Setter method for topics.
     * @param topics The list of topic to filter event.
     */
    public void setTopics(List<FilterTopic> topics) {
        this.topics = topics;
    }

    abstract T getThis();

    public interface FilterTopic<T> {
        @JsonValue
        T getValue();
    }

    public static class SingleTopic implements Filter.FilterTopic<String> {
        /**
         * The event topic to filter event.
         */
        private String topic;

        /**
         * Creates a SingleTopic instance.
         */
        public SingleTopic() {
            this.topic = null;  // null topic
        }

        /**
         * Creates a SingleTopic instance.
         * @param topic The event topic to filter event.
         */
        public SingleTopic(String topic) {
            this.topic = topic;
        }

        /**
         * Returns a single topic value.
         * @return String
         */
        @Override
        public String getValue() {
            return topic;
        }
    }

    public static class ListTopic implements Filter.FilterTopic<List<Filter.SingleTopic>> {
        /**
         * The List of topic that means optional topic.
         */
        private List<Filter.SingleTopic> topics;

        /**
         * Creates a ListTopic instance.
         * @param optionalTopics The array of optional topics.
         */
        public ListTopic(String... optionalTopics) {
            this(Arrays.asList(optionalTopics));
        }

        /**
         * Creates a ListTopic instance.
         * @param optionalTopics The list of optional topics.
         */
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

        /**
         * Returns optional topic value.
         * @return List
         */
        @Override
        public List<Filter.SingleTopic> getValue() {
            return topics;
        }
    }
}
