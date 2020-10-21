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


import org.web3j.abi.datatypes.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Representing a parameters need to deploy smart contract.
 */
public class ContractDeployParams {

    /**
     * A smart contract binary data.
     */
    String bytecode;

    /**
     * A List of smart contract constructor parameter.
     */
    List<Object> deployParams;

    /**
     * Creates a ContractDeployParam instance.
     * @param bytecode A smart contract binary data.
     * @param deployParams A List of smart contract constructor parameter.
     */
    public ContractDeployParams(String bytecode, List<Object> deployParams) {
        setBytecode(bytecode);
        setDeployParams(deployParams);
    }

    /**
     * Getter function for bytecode.
     * @return String
     */
    public String getBytecode() {
        return bytecode;
    }

    /**
     * Getter function for deployParams.
     * @return List
     */
    public List<Object> getDeployParams() {
        return deployParams;
    }

    /**
     * Setter function for bytecode.
     * @param binaryData A smart contract binary data.
     */
    public void setBytecode(String binaryData) {
        if(binaryData == null || binaryData.isEmpty()) {
            throw new RuntimeException("binary data is missing.");
        }

        this.bytecode = binaryData;
    }

    /**
     * Setter function for deployParams.
     * @param deployParams A List of smart contract constructor parameter.
     */
    public void setDeployParams(List<Object> deployParams) {
        this.deployParams = new ArrayList<>();
        if(deployParams != null) {
            this.deployParams.addAll(deployParams);
        }
    }
}
