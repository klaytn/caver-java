package com.klaytn.caver.contract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractExecution;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContractMethod {
    Caver caver;

    String type;
    String name;
    List<ContractIOType> inputs;
    List<ContractIOType> outputs;
    String signature;

    public ContractMethod() {
    }

    public ContractMethod(Caver caver, String type, String name, List<ContractIOType> inputs, List<ContractIOType> outputs, String signature) {
        this.caver = caver;
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.signature = signature;
    }

    public List<Type> call(String from, String contractAddress, Type... argument) throws IOException, ClassNotFoundException {
        List params = Arrays.asList(argument);
        List<TypeReference<Type>> resultParams = new ArrayList<>();

        for(ContractIOType ioType: this.outputs) {
            Class cls = Class.forName(ioType.getJavaType());
            resultParams.add(TypeReference.create(cls));
        }

        checkTypeValid(params);

        String encodedFunction = ABI.encodeFunctionCall(this, params);
        Bytes response = caver.rpc.klay.call(
                new CallObject(from, contractAddress, null, null, null, encodedFunction)
        ).send();

        String encodedResult = response.getResult();
        return ABI.decodeReturnParameters(encodedResult, resultParams);
    }

    public TransactionReceipt.TransactionReceiptData send(ContractExecuteParam param) throws IOException, TransactionException {
        return send(param, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    public TransactionReceipt.TransactionReceiptData send(ContractExecuteParam param, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        List params = param.getParams();
        checkTypeValid(params);

        String encodedFunction = ABI.encodeFunctionCall(this, params);

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(param.getFrom())
                .setTo(param.getContractAddress())
                .setInput(encodedFunction)
                .setGas(DefaultGasProvider.GAS_LIMIT)
                .setGasPrice(param.getGasPrice())
                .build();

        caver.wallet.sign(param.getFrom(), smartContractExecution);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractExecution.getRawTransaction()).send();

        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());
        return receipt;
    }

    public String encodeABI(Type... argument) {
        return ABI.encodeFunctionCall(this, Arrays.asList(argument));
    }

    public String estimateGas(String from, BigInteger gas, String contractAddress, Type... argument) throws IOException {
        List params = Arrays.asList(argument);
        String data = ABI.encodeFunctionCall(this, params);
        CallObject callObject = new CallObject(from, contractAddress, gas, null, null, data);

        Quantity estimateGas = caver.rpc.klay.estimateGas(callObject).send();
        if(estimateGas.hasError()) {
            throw new IOException(estimateGas.getError().getMessage());
        }
        return estimateGas.getResult();
    }

    public void checkTypeValid(List<Type> types) {
        if(types.size() != inputs.size()) {
            throw new IllegalArgumentException("Not matched passed parameter count.");
        }

        for(int i=0; i<types.size(); i++) {
            Type type = types.get(i);
            String solidityType = inputs.get(i).getType();

            if(!type.getTypeAsString().equals(solidityType)) {
                throw new IllegalArgumentException("Not match parameter type. You should use " + inputs.get(i).getJavaType());
            }
        }
    }

    public Caver getCaver() {
        return caver;
    }

    public void setCaver(Caver caver) {
        this.caver = caver;
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

    public List<ContractIOType> getInputs() {
        return inputs;
    }

    public void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    public List<ContractIOType> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ContractIOType> outputs) {
        this.outputs = outputs;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
