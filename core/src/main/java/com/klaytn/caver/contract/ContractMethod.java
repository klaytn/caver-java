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
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

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
    String contractAddress;

    public ContractMethod() {
    }

    public ContractMethod(Caver caver, String type, String name, List<ContractIOType> inputs, List<ContractIOType> outputs, String signature, String contractAddress) {
        this.caver = caver;
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.signature = signature;
        this.contractAddress = contractAddress;
    }

    public List<Type> call(List<Type> arguments, SendOptions options) throws IOException, ClassNotFoundException {
        List<TypeReference<Type>> resultParams = new ArrayList<>();

        for(ContractIOType ioType: this.outputs) {
            Class cls = Class.forName(ioType.getJavaType());
            resultParams.add(TypeReference.create(cls));
        }

        checkTypeValid(arguments);

        String encodedFunction = ABI.encodeFunctionCall(this, arguments);
        Bytes response = caver.rpc.klay.call(
                new CallObject(
                        options.from,
                        this.getContractAddress(),
                        Numeric.toBigInt(options.getGas()),
                        null,
                        Numeric.toBigInt(options.getValue()),
                        encodedFunction
                )).send();

        String encodedResult = response.getResult();
        return ABI.decodeReturnParameters(encodedResult, resultParams);
    }

    public TransactionReceipt.TransactionReceiptData send(List<Type> argument, SendOptions options) throws IOException, TransactionException {
        return send(argument, options, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    public TransactionReceipt.TransactionReceiptData send(List<Type> argument, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        checkTypeValid(argument);

        String encodedFunction = ABI.encodeFunctionCall(this, argument);

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(options.getFrom())
                .setTo(this.getContractAddress())
                .setInput(encodedFunction)
                .setGas(options.getGas())
                .build();

        caver.wallet.sign(options.getFrom(), smartContractExecution);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractExecution.getRawTransaction()).send();

        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());
        return receipt;
    }

    public String encodeABI(Type... argument) {
        return ABI.encodeFunctionCall(this, Arrays.asList(argument));
    }

    public String estimateGas(List<Type> argument, SendOptions options) throws IOException {
        String data = ABI.encodeFunctionCall(this, argument);
        CallObject callObject = new CallObject(options.getFrom(), this.getContractAddress(), Numeric.toBigInt(options.getGas()), null, Numeric.toBigInt(options.getValue()), data);

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

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<ContractIOType> getInputs() {
        return inputs;
    }

    public List<ContractIOType> getOutputs() {
        return outputs;
    }

    public String getSignature() {
        return signature;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setCaver(Caver caver) {
        this.caver = caver;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(List<ContractIOType> outputs) {
        this.outputs = outputs;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
