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

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.IWallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representing a Contract's method information.
 */
public class ContractMethod {
    static final String TYPE_FUNCTION = "function";
    static final String TYPE_CONSTRUCTOR = "constructor";

    /**
     * A caver instance.
     */
    Caver caver;

    /**
     * The input type. It may set "function" or "constructor".
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
     * The class instance implemented IWallet interface to sign transaction.
     */
    IWallet wallet;


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
     * Execute smart contract method in the EVM without sending any transaction.<p>
     * When creating CallObject, it need not to fill 'data', 'to' fields.<p>
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
     * Send a transaction to deploy smart contract or execute smart contract's method.
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * It is used defaultSendOption field to sendOptions.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *
     *     String sender = "0x{sender address}";
     *     Contract contract = caver.contract.create(abi);
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *     contract.setDefaultSendOptions(sendOptions);
     *
     *     TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(bytecode, constructor_param1, constructor_param2...));
     * </code>
     * </pre>
     * @param arguments A List of parameter to call smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return send(arguments, null, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to deploy smart contract or execute smart contract's method.
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *
     *     String sender = "0x{sender address}";
     *     Contract contract = caver.contract.create(abi);
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(bytecode, constructor_param1, constructor_param2...), sendOptions);
     * </code>
     * </pre>
     * @param arguments A List of parameter to call smart contract method.
     * @param options An option to deploy or execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments, SendOptions options) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return send(arguments, options, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to deploy smart contract or execute smart contract's method.
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *
     *     String sender = "0x{sender address}";
     *     Contract contract = caver.contract.create(abi);
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
     *     TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(bytecode, constructor_param1, constructor_param2...), sendOptions, receiptProcessor);
     * </code>
     * </pre>
     * @param arguments A List of parameter to call smart contract method.
     * @param options An option to deploy or execute smart contract method.
     * @param processor A TransactionReceiptProcessor to get receipt.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Object> arguments, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        SendOptions determinedOption = makeSendOption(options);
        AbstractTransaction transaction = sign(arguments, determinedOption);

        if(determinedOption.isFeeDelegation()) {
            if(determinedOption.getFeePayer() == null || !Utils.isAddress(determinedOption.getFeePayer())) {
                throw new IllegalArgumentException("The fee payer value is not valid. feePayer address - " + determinedOption.getFeePayer());
            }
            transaction = this.wallet.signAsFeePayer(determinedOption.getFeePayer(), (AbstractFeeDelegatedTransaction)transaction);
        }

        return sendTransaction(transaction, processor);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument.<p>
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * It is used defaultSendOption field to sendOptions.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *     String sender = "0x{sender address}";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     Contract contract = caver.contract.create(abi);
     *     contract.setDefaultSendOptions(sendOptions);
     *
     *     TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").sign(Arrays.asList(bytecode, constructor_param1, constructor_param2...));
     * </code>
     * </pre>
     * @param arguments The list of arguments to deploy or execute a smart contract.
     * @return AbstractTransaction
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public AbstractTransaction sign(List<Object> arguments) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        return sign(arguments, null);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument.<p>
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *     String sender = "0x{sender address}";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     Contract contract = caver.contract.create(abi);
     *     TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").sign(Arrays.asList(bytecode, constructor_param1, constructor_param2...), sendOptions);
     * </code>
     * </pre>
     * @param arguments The list of arguments to deploy or execute a smart contract.
     * @param sendOptions An option to deploy or execute smart contract method.
     * @return AbstractTransaction
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public AbstractTransaction sign(List<Object> arguments, SendOptions sendOptions) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        String encoded = encodeABI(arguments);
        //Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        //Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions determinedOption = makeSendOption(sendOptions);

        AbstractTransaction transaction = createTransaction(determinedOption, encoded);
        return caver.wallet.sign(determinedOption.getFrom(), transaction);
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument.<p>
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * It is used defaultSendOption field to sendOptions.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *     String sender = "0x{sender address}";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("0x{feePayer}");
     *
     *     Contract contract = caver.contract.create(abi);
     *     contract.setDefaultSendOptions(sendOptions);
     *     AbstractTransaction transaction = contract.getMethod("constructor").signAsFeePayer(Arrays.asList(bytecode, constructor_param1, constructor_param2....));
     * </code>
     * </pre>
     * @param arguments The list of arguments to deploy or execute a smart contract.
     * @return AbstractTransaction
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(List<Object> arguments) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        return signAsFeePayer(arguments, null);
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument.<p>
     * <pre>
     * If the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *     String sender = "0x{sender address}";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("0x{feePayer}");
     *
     *     Contract contract = caver.contract.create(abi);
     *     AbstractTransaction transaction = contract.getMethod("constructor").signAsFeePayer(Arrays.asList(bytecode, constructor_param1, constructor_param2....), sendOptions);
     * </code>
     * </pre>
     * @param arguments The list of arguments to deploy or execute a smart contract.
     * @param sendOptions An option to deploy or execute smart contract method.
     * @return AbstractTransaction
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(List<Object> arguments, SendOptions sendOptions) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        // Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        // Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions determinedOption = makeSendOption(sendOptions);
        if(!determinedOption.isFeeDelegation()) {
            throw new RuntimeException("'feeDelegation' field in SendOptions must set a true.");
        }

        checkSendOption(determinedOption);

        String encoded = encodeABI(arguments);

        AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)createTransaction(determinedOption, encoded);
        return caver.wallet.signAsFeePayer(determinedOption.getFeePayer(), transaction);
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.<p>
     * If the 'type' field is a "constructor", it encodes a constructor params to deploy.<p>
     * <pre>
     * When the 'type' field is a "constructor", the arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * </pre>
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

        if(getType().equals(TYPE_CONSTRUCTOR)) {
            // If type is a "constructor",
            // - argument[0] is smart contract's byte code,
            // - and others are constructor parameter
            String byteCode = (String)functionParams.get(0);
            List<Object> constructParams = functionParams.subList(1, arguments.size());

            return ABI.encodeContractDeploy(this, byteCode, constructParams);
        } else {
            ContractMethod matchedMethod = findMatchedInstance(functionParams);
            return ABI.encodeFunctionCall(matchedMethod, arguments);
        }
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
        String encodedFunctionCall = encodeABI(arguments);

        return estimateGas(encodedFunctionCall, callObject);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityWrapper(List<Type> arguments) throws IOException, ClassNotFoundException {
        return callWithSolidityWrapper(arguments, CallObject.createCallObject());
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.<p>
     * When creating CallObject, it need not to fill 'data', 'to' fields.<p>
     * The 'data', 'to' fields automatically filled in call() method.<p>
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
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.<p>
     * It is used defaultSendOption field to sendOptions.<p>
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.<p>
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
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.<p>
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.<p>
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
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped solidity wrapper class.
     * @param options An option to execute smart contract method.
     * @param processor A TransactionReceiptProcessor to get receipt.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityWrapper(List<Type> wrapperArguments, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        if(!getType().equals(TYPE_FUNCTION)) {
            throw new RuntimeException("This method can be used only to encode function with passed argument.");
        }

        SendOptions determinedOption = makeSendOption(options);

        AbstractTransaction transaction = signWithSolidityWrapper(wrapperArguments, determinedOption);

        if(determinedOption.isFeeDelegation() && determinedOption.getFeePayer() != null) {
            transaction = this.wallet.signAsFeePayer(determinedOption.getFeePayer(), (AbstractFeeDelegatedTransaction)transaction);
        }

        return sendTransaction(transaction, processor);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to SmartContractExecution to execute smart contract method.<p>
     * It is used defaultSendOption field to sendOptions.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped by solidity type wrapper class.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction signWithSolidityWrapper(List<Type> wrapperArguments) throws IOException {
        return signWithSolidityWrapper(wrapperArguments, null);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to SmartContractExecution to execute smart contract method.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped by solidity type wrapper class.
     * @param sendOptions An option to execute smart contract method.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction signWithSolidityWrapper(List<Type> wrapperArguments, SendOptions sendOptions) throws IOException {
        if(!getType().equals(TYPE_FUNCTION)) {
            throw new RuntimeException("This method can be used only to encode function with passed argument.");
        }

        //Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        //Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions determinedOption = makeSendOption(sendOptions);
        checkSendOption(determinedOption);

        String encoded = encodeABIWithSolidityWrapper(wrapperArguments);

        AbstractTransaction transaction = createTransaction(determinedOption, encoded);
        return caver.wallet.sign(determinedOption.getFrom(), transaction);
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to FeeDelegatedSmartContractExecution to execute smart contract method.<p>
     * It is used defaultSendOption field to sendOptions.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped by solidity type wrapper class.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayerWithSolidityWrapper(List<Type> wrapperArguments) throws IOException {
        return signAsFeePayerWithSolidityWrapper(wrapperArguments, null);
    }


    /**
     * Create and sign a transaction with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to FeeDelegatedSmartContractExecution to execute smart contract method.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that wrapped by solidity type wrapper class.
     * @param sendOptions An option to execute smart contract method.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayerWithSolidityWrapper(List<Type> wrapperArguments, SendOptions sendOptions) throws IOException {
        if(!getType().equals(TYPE_FUNCTION)) {
            throw new RuntimeException("This method can be used only to encode function with passed argument.");
        }

        // Make SendOptions instance by comparing with defaultSendOption and passed parameter "options"
        // Passed parameter "options" has higher priority than "defaultSendOption" field.
        SendOptions determinedOption = makeSendOption(sendOptions);
        if(!determinedOption.isFeeDelegation()) {
            throw new RuntimeException("'feeDelegation' field in SendOptions must set a true.");
        }

        checkSendOption(determinedOption);

        String encoded = encodeABIWithSolidityWrapper(wrapperArguments);

        AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)createTransaction(determinedOption, encoded);
        return caver.wallet.signAsFeePayer(determinedOption.getFeePayer(), transaction);
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param wrapperArguments A List of parameter that solidity wrapper class
     * @return The encoded ABI byte code to send via a transaction or call.
     */
    public String encodeABIWithSolidityWrapper(List<Type> wrapperArguments) {
        if(!getType().equals(TYPE_FUNCTION)) {
            throw new RuntimeException("This method can be used only to encode function with passed argument.");
        }

        List<Type> functionParams = new ArrayList<>();

        if(wrapperArguments != null) {
            functionParams.addAll(wrapperArguments);
        }

        ContractMethod matchedMethod = this.findMatchedInstanceWithSolidityWrapper(functionParams);
        return ABI.encodeFunctionCallWithSolidityWrapper(matchedMethod, functionParams);
    }

    /**
     * Estimate the gas to execute the Contract's method using Solidity type wrapper class.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param arguments The arguments that need to execute smart contract method.
     * @param callObject An option to execute smart contract method.
     * @return String
     * @throws IOException
     */
    public String estimateGasWithSolidityWrapper(List<Type> arguments, CallObject callObject) throws IOException {
        String encodedFunctionCall = encodeABIWithSolidityWrapper(arguments);

        return estimateGas(encodedFunctionCall, callObject);
    }

    /**
     * Check that passed parameter is valid to execute smart contract method.
     * - check parameter count.
     * - check defined parameter solidity type and parameter solidity wrapper type.
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

    /**
     * Setter function for wallet
     * @param wallet The class instance implemented IWallet interface to sign transaction.
     */
    public void setWallet(IWallet wallet) {
        this.wallet = wallet;
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
        boolean isFeeDelegation = defaultSendOption.isFeeDelegation();
        String feePayer = defaultSendOption.getFeePayer();
        String feeRatio = defaultSendOption.getFeeRatio();

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

            if(sendOption.isFeeDelegation()) {
                isFeeDelegation = sendOption.isFeeDelegation();
            }

            if(sendOption.getFeePayer() != null) {
                feePayer = sendOption.getFeePayer();
            }

            if(sendOption.getFeeRatio() != null) {
                feeRatio = sendOption.getFeeRatio();
            }
        }

        SendOptions options = new SendOptions(from, gas, value);
        options.setFeeDelegation(isFeeDelegation);

        if(options.isFeeDelegation()) {
            options.setFeePayer(feePayer);
            options.setFeeRatio(feeRatio);
        }


        return options;
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

        if(options.isFeeDelegation()) {
            if(options.getFeePayer() == null || options.getFeePayer().equals("0x")) {
                options.setFeePayer(Utils.DEFAULT_ZERO_ADDRESS);
            }

            if(!Utils.isAddress(options.getFeePayer())) {
                throw new IllegalArgumentException("Invalid 'feePayer' parameter : " + options.getFeePayer());
            }

            if(options.getFeeRatio() != null) {
                if(!Utils.isNumber(options.getFeeRatio()) && !Utils.isHex(options.getFeeRatio())) {
                    throw new IllegalArgumentException("Invalid type of feeRatio: feeRatio should be number type or hex number string");
                }

                int feeRatioVal = Numeric.toBigInt(options.getFeeRatio()).intValue();
                if(feeRatioVal <= 0 || feeRatioVal >= 100) {
                    throw new IllegalArgumentException("Invalid feeRatio: feeRatio is out of range. [1,99]");
                }
            }
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

        for(int i = 0; i < this.getInputs().size(); i++) {
            ContractIOType ioType = this.getInputs().get(i);
            if(!ioType.getTypeAsString().equals(arguments.get(i).getTypeAsString())) {
                return false;
            }
        }

        return true;
    }

    private TransactionReceipt.TransactionReceiptData sendTransaction(AbstractTransaction transaction, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        Bytes32 response = caver.rpc.klay.sendRawTransaction(transaction).send();
        if(response.hasError()) {
            throw new IOException(response.getError().getMessage());
        }

        return processor.waitForTransactionReceipt(response.getResult());
    }

    private List<Type> callFunction(ContractMethod method, String encodedInput, CallObject callObject) throws IOException, ClassNotFoundException {
        if(callObject.getData() != null || callObject.getTo() != null) {
            LOGGER.warn("'to' and 'data' field in CallObject will overwrite.");
        }
        callObject.setData(encodedInput);
        callObject.setTo(method.getContractAddress());
        Bytes response = caver.rpc.klay.call(callObject).send();
        if(response.hasError()) {
            throw new IOException(response.getError().getMessage());
        }

        String encodedResult = response.getResult();
        return ABI.decodeParameters(method, encodedResult);
    }

    private String estimateGas(String encodedFunctionCall, CallObject callObject) throws IOException {
        if(callObject.getData() != null || callObject.getTo() != null) {
            LOGGER.warn("The 'to' and 'data' fields of the CallObject will be overwritten.");
        }
        callObject.setData(encodedFunctionCall);
        callObject.setTo(this.getContractAddress());

        Quantity estimateGas = caver.rpc.klay.estimateGas(callObject).send();
        if(estimateGas.hasError()) {
            throw new IOException(estimateGas.getError().getMessage());
        }

        return estimateGas.getResult();
    }

    private AbstractTransaction createTransaction(SendOptions sendOptions, String encoded) {
        checkSendOption(sendOptions);

        if(getType().equals("constructor")) { // contract deploy
            if(sendOptions.isFeeDelegation()) { // fee delegation transaction
                if(sendOptions.getFeeRatio() == null) {
                    return caver.transaction.feeDelegatedSmartContractDeploy.create(
                            TxPropertyBuilder.feeDelegatedSmartContractDeploy()
                                    .setFrom(sendOptions.getFrom())
                                    .setGas(sendOptions.getGas())
                                    .setValue(sendOptions.getValue())
                                    .setInput(encoded)
                                    .setFeePayer(sendOptions.getFeePayer())
                    );
                } else {
                    return caver.transaction.feeDelegatedSmartContractDeployWithRatio.create(
                            TxPropertyBuilder.feeDelegatedSmartContractDeployWithRatio()
                                    .setFrom(sendOptions.getFrom())
                                    .setGas(sendOptions.getGas())
                                    .setValue(sendOptions.getValue())
                                    .setInput(encoded)
                                    .setFeePayer(sendOptions.getFeePayer())
                                    .setFeeRatio(sendOptions.getFeeRatio())
                    );
                }
            } else { // basic transaction
                return caver.transaction.smartContractDeploy.create(
                        TxPropertyBuilder.smartContractDeploy()
                                .setFrom(sendOptions.getFrom())
                                .setGas(sendOptions.getGas())
                                .setValue(sendOptions.getValue())
                                .setInput(encoded)
                );
            }
        } else { // contract execution
            if(sendOptions.isFeeDelegation()) { // fee delegation transaction
                if(sendOptions.getFeeRatio() == null) {
                    return caver.transaction.feeDelegatedSmartContractExecution.create(
                            TxPropertyBuilder.feeDelegatedSmartContractExecution()
                                    .setFrom(sendOptions.getFrom())
                                    .setTo(contractAddress)
                                    .setGas(sendOptions.getGas())
                                    .setValue(sendOptions.getValue())
                                    .setInput(encoded)
                                    .setFeePayer(sendOptions.getFeePayer())
                    );
                } else {
                    return caver.transaction.feeDelegatedSmartContractExecutionWithRatio.create(
                            TxPropertyBuilder.feeDelegatedSmartContractExecutionWithRatio()
                                    .setFrom(sendOptions.getFrom())
                                    .setTo(contractAddress)
                                    .setGas(sendOptions.getGas())
                                    .setValue(sendOptions.getValue())
                                    .setInput(encoded)
                                    .setFeePayer(sendOptions.getFeePayer())
                                    .setFeeRatio(sendOptions.getFeeRatio())
                    );
                }
            } else { // basic transaction
                return caver.transaction.smartContractExecution.create(
                        TxPropertyBuilder.smartContractExecution()
                                .setFrom(sendOptions.getFrom())
                                .setTo(contractAddress)
                                .setGas(sendOptions.getGas())
                                .setValue(sendOptions.getValue())
                                .setInput(encoded)
                );
            }
        }
    }
}
