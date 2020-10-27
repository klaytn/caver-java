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

/**
 * Representing a Contract's method or event parameter information.
 */
public class ContractIOType {
    /**
     * A parameter name.
     */
    String name;

    /**
     * A solidity type of parameter.
     */
    String type;

    /**
     * True if the field is part of the log’s topics, false if it one of the log’s data segment.
     */
    boolean indexed;

    /**
     * Creates a ContractIOType instance.
     */
    public ContractIOType() {
    }

    /**
     * Creates a ContractIOType instance.
     * @param name The name of the parameter.
     * @param type A solidity type of parameter.
     * @param indexed The location of parameter. True if the field is part of the log’s topics, false if it one of the log’s data segment.
     */
    public ContractIOType(String name, String type, boolean indexed) {
        this.name = name;
        this.type = type;
        this.indexed = indexed;
    }

    /**
     * Getter function for name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter function for type
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Getter function for indexed
     * @return indexed.
     */
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Setter function for name
     * @param name parameter name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for type.
     * @param type solidity type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for indexed
     * @param indexed The location of parameter. True if the field is part of the log’s topics, false if it one of the log’s data segment.
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
}
