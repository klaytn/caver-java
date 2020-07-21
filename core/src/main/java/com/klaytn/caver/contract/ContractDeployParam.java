package com.klaytn.caver.contract;


import org.web3j.abi.datatypes.Type;

import java.util.List;

/**
 * Representing a parameters need to deploy smart contract.
 */
public class ContractDeployParam {

    /**
     * A smart contract binary data.
     */
    String bytecode;

    /**
     * A List of smart contract constructor parameter.
     */
    List<Type> deployParams;

    /**
     * Creates a ContractDeployParam instance.
     * @param bytecode A smart contract binary data.
     * @param deployParams A List of smart contract constructor parameter.
     */
    public ContractDeployParam(String bytecode, List<Type> deployParams) {
        this.bytecode = bytecode;
        this.deployParams = deployParams;
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
    public List<Type> getDeployParams() {
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
    public void setDeployParams(List<Type> deployParams) {
        this.deployParams = deployParams;
    }
}
