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
 * Representing a Contract's event information.
 */
public class ContractEvent {

    /**
     * The input type. It always set "event".
     */
    String type;

    /**
     * The event name.
     */
    String name;

    /**
     * The event signature string.
     */
    String signature;

    /**
     * The list of ContractIOType contains to event parameter information.
     */
    List<ContractIOType> inputs;

    /**
     * Creates a ContractEvent instance.
     */
    public ContractEvent() {
    }

    /**
     * Creates a ContractEvent instance.
     * @param type The input type. It always set "event"
     * @param name The event name
     * @param signature The event signature string.
     * @param inputs The list of ContractIOType contains to event parameter information.
     */
    public ContractEvent(String type, String name, String signature, List<ContractIOType> inputs) {
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.inputs = inputs;
    }

    /**
     * Getter function for type.
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Getter function for name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter function for signature.
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Getter function for input.
     * @return List
     */
    public List<ContractIOType> getInputs() {
        return inputs;
    }

    /**
     * Setter function for name.
     * @param name A function name.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for type.
     * @param type The input type. It always set "event".
     */
    void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for inputs
     * @param inputs The list of ContractIOType contains to function parameter information.
     */
    void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    /**
     * Setter function for event signature.
     * @param signature A function signature
     */
    void setSignature(String signature) {
        this.signature = signature;
    }
}
