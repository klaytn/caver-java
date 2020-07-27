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
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representing a Contract's method information.
 */
public class ContractMethod {

    /**
     * A caver instance.
     */
    Caver caver;

    /**
     * The input type. It always set "function".
     */
    String type;

    /**
     * The function name.
     */
    String name;

    /**
     * The list of ContractIOType contains to function parameter information.
     */
    List<ContractIOType> inputs;

    /**
     * The list of ContractIOType contains to function return value information.
     */
    List<ContractIOType> outputs;

    /**
     * The function signature string. ex) foo(uint256,uin256)
     */
    String signature;

    /**
     * The contract address.
     */
    String contractAddress;

    /**
     * The default send option. When you execute call() or send() without SendOptions, defaultSendOptions will be used.
     */
    SendOptions defaultSendOptions;

    /**
     * Creates a ContractMethod instance.
     */
    public ContractMethod() {
    }

    /**
     * Creates a ContractMethod instance.
     * @param caver A Caver instance.
     * @param type The input type. It always set "function"
     * @param name The function name
     * @param inputs The list of ContractIOType contains to function parameter information.
     * @param outputs The list of ContractIOType
     * @param signature The function signature string.
     * @param contractAddress The contract address
     */
    public ContractMethod(Caver caver, String type, String name, List<ContractIOType> inputs, List<ContractIOType> outputs, String signature, String contractAddress) {
        this.caver = caver;
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.signature = signature;
        this.contractAddress = contractAddress;
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * It is used defaultSendOption field to sendOptions
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @return List
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<Type> call(List<Object> arguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return call(arguments, null);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> call(List<Object> arguments, SendOptions options) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        //Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        //Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions sendOptions = makeSendOption(options);

        // Check the parameter type defined in function and the parameter type passed are the same.
        checkTypeValid(functionParams);

        String encodedFunction = ABI.encodeFunctionCall(this, arguments);
        Bytes response = caver.rpc.klay.call(
                new CallObject(
                        sendOptions.getFrom(),
                        this.getContractAddress(),
                        Numeric.toBigInt(sendOptions.getGas()),
                        null,
                        Numeric.toBigInt(sendOptions.getValue()),
                        encodedFunction
                )).send();

        String encodedResult = response.getResult();
        return ABI.decodeParameters(this, encodedResult);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It is used defaultSendOption field to sendOptions
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return send(arguments, null, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments, SendOptions options) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return send(arguments, options, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @param processor A TransactionReceiptProcessor to get receipt.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        //Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        //Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions sendOptions = makeSendOption(options);

        checkTypeValid(functionParams);

        String encodedFunction = ABI.encodeFunctionCall(this, arguments);

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(sendOptions.getFrom())
                .setTo(this.getContractAddress())
                .setInput(encodedFunction)
                .setGas(sendOptions.getGas())
                .setValue(sendOptions.getValue())
                .build();

        caver.wallet.sign(sendOptions.getFrom(), smartContractExecution);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractExecution.getRawTransaction()).send();

        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());
        return receipt;
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.
     * @param arguments A List of parameter that solidity wrapper type
     * @return The encoded ABI byte code to send via a transaction or call.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String encodeABI(List<Object> arguments) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ABI.encodeFunctionCall(this, arguments);
    }

    /**
     * Estimate the gas to execute the contract's method.
     * @param arguments A List of parameter that solidity wrapper type
     * @param options An option to execute smart contract method.
     * @return String
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String estimateGas(List<Object> arguments, SendOptions options) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String data = ABI.encodeFunctionCall(this, arguments);
        CallObject callObject = new CallObject(options.getFrom(), this.getContractAddress(), Numeric.toBigInt(options.getGas()), null, Numeric.toBigInt(options.getValue()), data);

        Quantity estimateGas = caver.rpc.klay.estimateGas(callObject).send();
        if(estimateGas.hasError()) {
            throw new IOException(estimateGas.getError().getMessage());
        }

        return estimateGas.getResult();
    }

    /**
     * Check that passed parameter is valid to execute smart contract method.
     *   - check parameter count.
     *   - check defined parameter solidity type and parameter solidity wrapper type.
     * @param types A List of parameter that solidity wrapper type
     */
    public void checkTypeValid(List<Object> types) {
        if(types.size() != inputs.size()) {
            throw new IllegalArgumentException("Not matched passed parameter count.");
        }
    }

    /**
     * Getter function for Caver.
     * @return Caver
     */
    public Caver getCaver() {
        return caver;
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
     * Getter function for input.
     * @return List
     */
    public List<ContractIOType> getInputs() {
        return inputs;
    }

    /**
     * Getter function for output.
     * @return List
     */
    public List<ContractIOType> getOutputs() {
        return outputs;
    }

    /**
     * Getter function for signature.
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Getter function for contract address
     * @return String
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Getter function for DefaultSendOptions
     * @return SendOptions
     */
    public SendOptions getDefaultSendOptions() {
        return defaultSendOptions;
    }

    /**
     * Setter function for Caver.
     * @param caver The Caver instance.
     */
    public void setCaver(Caver caver) {
        this.caver = caver;
    }

    /**
     * Setter function for type.
     * @param type The input type. It always set "function".
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for name.
     * @param name A function name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for inputs
     * @param inputs The list of ContractIOType contains to function parameter information.
     */
    public void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    /**
     * Setter function for outputs
     * @param outputs The list of ContractIOTYpe contains to function return value information.
     */
    public void setOutputs(List<ContractIOType> outputs) {
        this.outputs = outputs;
    }

    /**
     * Setter function for function signature.
     * @param signature A function signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Setter function for contract address
     * @param contractAddress A contract address.
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * Setter function for defaultSendOption
     * @param defaultSendOptions The sendOptions to set DefaultSendOptions field.
     */
    public void setDefaultSendOptions(SendOptions defaultSendOptions) {
        this.defaultSendOptions = defaultSendOptions;
    }

    /**
     * Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
     * Passed parameter "options" has higher priority than "defaultSendOption" field.
     * @param sendOption SendOptions instance
     * @return SendOption
     */
    public SendOptions makeSendOption(SendOptions sendOption) {
        SendOptions defaultSendOption = this.getDefaultSendOptions();
        String from = defaultSendOption.getFrom();
        String gas = defaultSendOption.getGas();
        String value = defaultSendOption.getValue();

        if(sendOption != null) {
            if(sendOption.getFrom() != null) {
                from = sendOption.getFrom();
            }
            if(sendOption.getGas() != null) {
                gas = sendOption.getGas();
            }
            if(!sendOption.getValue().equals("0x0")) {
                value = sendOption.getValue();
            }
        }

        return new SendOptions(from, gas, value);
    }
}
