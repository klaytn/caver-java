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

import java.util.List;

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
     * A tuple type components information.
     */
    List<ContractIOType> components;

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
     * Getter function for components.
     * @return List
     */
    public List<ContractIOType> getComponents() {
        return components;
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

    /**
     * Setter function for components
     * @param components The tuple's components list.
     */
    public void setComponents(List<ContractIOType> components) {
        this.components = components;
    }

    /**
     * Returns a type string.
     * If type string has a tuple, the components of the tuple are listed and returned.
     * @return String
     */
    public String getTypeAsString() {
        if(getType().contains("tuple")) {
            String typeString = getComponentAsString();
            return getType().replace("tuple", "tuple" + typeString);
        } else {
            return getType();
        }
    }

    private String getComponentAsString() {
        if(this.getComponents() == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder("(");

        for(int i=0; i<this.getComponents().size(); i++) {
            ContractIOType ioType = this.components.get(i);
            if(ioType.getType().contains("tuple")) {
                stringBuilder.append(ioType.getTypeAsString());
            } else {
                stringBuilder.append(ioType.getType());
            }

            if(i < this.getComponents().size() -1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.append(")").toString();
    }
}
