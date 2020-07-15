package com.klaytn.caver.contract;


import com.klaytn.caver.utils.Utils;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

public class ContractDeployParam {
    String from;
    String gas;
    String gasPrice;
    String binaryData;
    List<Type> deployParams;

    public ContractDeployParam(String from, String gas, String binaryData, List<Type> deployParams) {
        this(from, gas,null, binaryData, deployParams);
    }

    public ContractDeployParam(String from, BigInteger gas, String binaryData, List<Type> deployParams) {
        this(from, Numeric.toHexStringWithPrefix(gas),null, binaryData, deployParams);
    }

    public ContractDeployParam(String from, BigInteger gas, BigInteger gasPrice, String binaryData, List<Type> deployParams) {
        this(from, Numeric.toHexStringWithPrefix(gas), Numeric.toHexStringWithPrefix(gasPrice), binaryData, deployParams);
    }

    public ContractDeployParam(String from, String gas, String gasPrice, String binaryData, List<Type> deployParams) {
        setFrom(from);
        setGas(gas);
        setGasPrice(gasPrice);
        setBinaryData(binaryData);
        setDeployParams(deployParams);
    }

    public String getFrom() {
        return from;
    }

    public String getGas() {
        return gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public String getBinaryData() {
        return binaryData;
    }

    public List<Type> getDeployParams() {
        return deployParams;
    }

    public void setFrom(String from) {
        if(from == null) {
            throw new IllegalArgumentException("from is missing.");
        }

        if(!Utils.isAddress(from)) {
            throw new IllegalArgumentException("Invalid address. : " + from);
        }

        this.from = from;
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(String gas) {
        //Gas value must be set.
        if(gas == null || gas.isEmpty() || gas.equals("0x")) {
            throw new IllegalArgumentException("gas is missing.");
        }

        if(!Utils.isNumber(gas)) {
            throw new IllegalArgumentException("Invalid gas. : "  + gas);
        }
        this.gas = gas;
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(BigInteger gas) {
        setGas(Numeric.toHexStringWithPrefix(gas));
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(String gasPrice) {
        if(gasPrice == null || gasPrice.isEmpty() || gasPrice.equals("0x")) {
            gasPrice = "0x";
        }

        if(!gasPrice.equals("0x") && !Utils.isNumber(gasPrice)) {
            throw new IllegalArgumentException("Invalid gasPrice. : " + gasPrice);
        }

        this.gasPrice = gasPrice;
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(BigInteger gasPrice) {
        setGasPrice(Numeric.toHexStringWithPrefix(gasPrice));
    }

    public void setBinaryData(String binaryData) {
        if(binaryData == null || binaryData.isEmpty()) {
            throw new RuntimeException("binary data is missing.");
        }

        this.binaryData = binaryData;
    }

    public void setDeployParams(List<Type> deployParams) {
        this.deployParams = deployParams;
    }
}
