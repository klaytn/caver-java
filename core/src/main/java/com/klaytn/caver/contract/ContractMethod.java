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
import com.klaytn.caver.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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


    List<ContractMethod> nextContractMethods = new ArrayList<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(ContractMethod.class);

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
     * When creating CallObject, it need not to fill 'data', 'to' fields.
     * The 'data', 'to' fields automatically filled in call() method.
     * @param arguments A List of parameter to call smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<Type> call(List<Object> arguments) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return call(arguments, CallObject.createCallObject());
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * When creating CallObject, it need not to fill 'data', 'to' fields.
     * The 'data', 'to' fields automatically filled in call() method.
     * @param arguments A List of parameter to call smart contract method.
     * @param callObject A CallObject instance to 'call' smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<Type> call(List<Object> arguments, CallObject callObject) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        ContractMethod matchedMethod = findMatchedInstance(functionParams);
        String encodedFunction = ABI.encodeFunctionCall(matchedMethod, functionParams);

        return callFunction(matchedMethod, encodedFunction, callObject);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * The 'data', 'to' fields automatically filled in call() method.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param callObject A CallObject instance to 'call' smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityWrapper(List<Type> arguments) throws IOException, ClassNotFoundException {
        return callWithSolidityWrapper(arguments, CallObject.createCallObject());
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * When creating CallObject, it need not to fill 'data', 'to' fields.
     * The 'data', 'to' fields automatically filled in call() method.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param callObject A CallObject instance to 'call' smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityWrapper(List<Type> arguments, CallObject callObject) throws IOException, ClassNotFoundException {
        List<Type> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        ContractMethod matchedMethod = findMatchedInstanceWithSolidityWrapper(functionParams);
        String encodedFunction = ABI.encodeFunctionCallWithSolidityWrapper(matchedMethod, functionParams);

        return callFunction(matchedMethod, encodedFunction, callObject);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It is used defaultSendOption field to sendOptions
     * @param arguments A List of parameter to call smart contract method.
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
     * @param arguments A List of parameter to call smart contract method.
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
     * @param arguments A List of parameter to call smart contract method.
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

        ContractMethod matchedMethod = findMatchedInstance(functionParams);
        String encodedFunction = ABI.encodeFunctionCall(matchedMethod, functionParams);

        return sendTransaction(matchedMethod, options, encodedFunction, processor);
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It is used defaultSendOption field to sendOptions
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped solidity wrapper class.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityWrapper(List<Type> wrapperArguments) throws IOException, TransactionException {
        return sendWithSolidityWrapper(wrapperArguments, null, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped solidity wrapper class.
     * @param options An option to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityWrapper(List<Type> wrapperArguments, SendOptions options) throws IOException, TransactionException {
        return sendWithSolidityWrapper(wrapperArguments, options, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped solidity wrapper class.
     * @param options An option to execute smart contract method.
     * @param processor A TransactionReceiptProcessor to get receipt.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityWrapper(List<Type> wrapperArguments, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        List<Type> functionParams = new ArrayList<>();

        if(wrapperArguments != null) {
            functionParams.addAll(wrapperArguments);
        }
        ContractMethod matchedMethod = findMatchedInstanceWithSolidityWrapper(functionParams);
        String encodedFunction = ABI.encodeFunctionCallWithSolidityWrapper(matchedMethod, functionParams);

        return sendTransaction(matchedMethod, options, encodedFunction, processor);
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.
     * @param arguments A List of parameter to encode function signature and parameters
     * @return The encoded ABI byte code to send via a transaction or call.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String encodeABI(List<Object> arguments) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        ContractMethod matchedMethod = findMatchedInstance(functionParams);
        return ABI.encodeFunctionCall(matchedMethod, arguments);
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param arguments A List of parameter that solidity wrapper class
     * @return The encoded ABI byte code to send via a transaction or call.
     */
    public String encodeABIWithSolidityWrapper(List<Type> wrapperArguments) {
        List<Type> functionParams = new ArrayList<>();

        if(wrapperArguments != null) {
            functionParams.addAll(wrapperArguments);
        }

        ContractMethod matchedMethod = this.findMatchedInstanceWithSolidityWrapper(functionParams);
        return ABI.encodeFunctionCallWithSolidityWrapper(matchedMethod, functionParams);
    }

    /**
     * Estimate the gas to execute the contract's method.
     * @param arguments A List of parameter that solidity wrapper type
     * @param callObject An option to execute smart contract method.
     * @return String
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String estimateGas(List<Object> arguments, CallObject callObject) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> functionParams = new ArrayList<>();

        if(arguments != null) {
            functionParams.addAll(arguments);
        }

        String encodedFunction = ABI.encodeFunctionCall(this, functionParams);

        if(callObject.getData() != null || callObject.getTo() != null) {
            LOGGER.warn("The 'to' and 'data' fields of the CallObject will be overwritten.");
        }
        callObject.setData(encodedFunction);
        callObject.setTo(this.getContractAddress());

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

    public List<ContractMethod> getNextContractMethods() {
        return nextContractMethods;
    }

    /**
     * Setter function for Caver.
     * @param caver The Caver instance.
     */
    void setCaver(Caver caver) {
        this.caver = caver;
    }

    /**
     * Setter function for type.
     * @param type The input type. It always set "function".
     */
    void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for name.
     * @param name A function name.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for inputs
     * @param inputs The list of ContractIOType contains to function parameter information.
     */
    void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    /**
     * Setter function for outputs
     * @param outputs The list of ContractIOTYpe contains to function return value information.
     */
    void setOutputs(List<ContractIOType> outputs) {
        this.outputs = outputs;
    }

    /**
     * Setter function for function signature.
     * @param signature A function signature
     */
    void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Setter function for contract address
     * @param contractAddress A contract address.
     */
    void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        if(this.getNextContractMethods() != null && this.getNextContractMethods().size() != 0) {
            this.getNextContractMethods().stream().forEach(contractMethod -> {
                contractMethod.contractAddress = contractAddress;
            });
        }
    }

    /**
     * Setter function for defaultSendOption
     * @param defaultSendOptions The sendOptions to set DefaultSendOptions field.
     */
    void setDefaultSendOptions(SendOptions defaultSendOptions) {
        this.defaultSendOptions = defaultSendOptions;
    }

    void setNextContractMethods(List<ContractMethod> nextContractMethods) {
        this.nextContractMethods = nextContractMethods;
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

    /**
     * Before executing SmartContractExecution transaction, check SendOptions field is valid.
     * @param options SendOption instance.
     */
    private void checkSendOption(SendOptions options) {
        if(options.getFrom() == null || !Utils.isAddress(options.getFrom())) {
            throw new IllegalArgumentException("Invalid 'from' parameter : " + options.getFrom());
        }

        if(options.getGas() == null || !Utils.isNumber(options.getGas())) {
            throw new IllegalArgumentException("Invalid 'gas' parameter : " + options.getGas());
        }

        if(options.getValue() == null || !Utils.isNumber(options.getValue())) {
            throw new IllegalArgumentException("Invalid 'value' parameter : " + options.getValue());
        }
    }

    private ContractMethod findMatchedInstance(List arguments) {
        // Check the parameter type defined in function and the parameter type passed are the same.
        List<ContractMethod> matchedMethod = new ArrayList<>();

        if(this.getInputs().size() != arguments.size()) {
            for(ContractMethod method : this.getNextContractMethods()) {
                if(method.getInputs().size() == arguments.size()) {
                    matchedMethod.add(method);
                }
            }
        } else {
            matchedMethod.add(this);
        }

        if(matchedMethod.size() == 0) {
            throw new IllegalArgumentException("Cannot find method with passed parameters.");
        }

        if(matchedMethod.size() != 1) {
            LOGGER.warn("It found a two or more overloaded function that has same parameter counts. It may be abnormally executed. Please use *withSolidityWrapper().");
        }

        return matchedMethod.get(0);
    }

    private ContractMethod findMatchedInstanceWithSolidityWrapper(List<Type> arguments) {
        ContractMethod matchedMethod = null;

        if(this.checkParamsTypeMatched(arguments)) {
            matchedMethod = this;
        } else {
            for(ContractMethod method : this.getNextContractMethods()) {
                if(method.checkParamsTypeMatched(arguments)) {
                    matchedMethod = method;
                }
            }
        }

        if(matchedMethod == null) {
            throw new IllegalArgumentException("Cannot find method with passed parameters.");
        }

        return matchedMethod;
    }

    private boolean checkParamsTypeMatched(List<Type> arguments) {
        if(this.getInputs().size() != arguments.size()) {
            return false;
        }

        for(int i=0; i< this.getInputs().size(); i++) {
            ContractIOType ioType = this.getInputs().get(i);
            if(!ioType.getType().equals(arguments.get(i).getTypeAsString())) {
                return false;
            }
        }

        return true;
    }

    private TransactionReceipt.TransactionReceiptData sendTransaction(ContractMethod method, SendOptions options, String encodedInput, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        //Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        //Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions sendOptions = makeSendOption(options);
        checkSendOption(sendOptions);

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(sendOptions.getFrom())
                .setTo(method.getContractAddress())
                .setInput(encodedInput)
                .setGas(sendOptions.getGas())
                .setValue(sendOptions.getValue())
                .build();

        caver.wallet.sign(sendOptions.getFrom(), smartContractExecution);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractExecution).send();

        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());
        return receipt;
    }

    private List<Type> callFunction(ContractMethod method, String encodedInput, CallObject callObject) throws ClassNotFoundException, IOException {
        if(callObject.getData() != null || callObject.getTo() != null) {
            LOGGER.warn("'to' and 'data' field in CallObject will overwrite.");
        }
        callObject.setData(encodedInput);
        callObject.setTo(method.getContractAddress());
        Bytes response = caver.rpc.klay.call(callObject).send();

        String encodedResult = response.getResult();
        return ABI.decodeParameters(method, encodedResult);
    }
}
