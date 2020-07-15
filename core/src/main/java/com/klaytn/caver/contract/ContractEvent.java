package com.klaytn.caver.contract;

import java.util.List;

public class ContractEvent {
    String type;
    String name;
    String signature;
    List<ContractIOType> inputs;

    public ContractEvent() {
    }

    public ContractEvent(String type, String name, String signature, List<ContractIOType> inputs) {
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.inputs = inputs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<ContractIOType> getInputs() {
        return inputs;
    }

    public void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }
}
