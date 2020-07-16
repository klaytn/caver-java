package com.klaytn.caver.contract;


import org.web3j.abi.datatypes.Type;

import java.util.List;

public class ContractDeployParam {
    String bytecode;
    List<Type> deployParams;

    public ContractDeployParam(String bytecode, List<Type> deployParams) {
        this.bytecode = bytecode;
        this.deployParams = deployParams;
    }

    public String getBytecode() {
        return bytecode;
    }

    public List<Type> getDeployParams() {
        return deployParams;
    }

    public void setBytecode(String binaryData) {
        if(binaryData == null || binaryData.isEmpty()) {
            throw new RuntimeException("binary data is missing.");
        }

        this.bytecode = binaryData;
    }

    public void setDeployParams(List<Type> deployParams) {
        this.deployParams = deployParams;
    }
}
