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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.AbiTypes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
