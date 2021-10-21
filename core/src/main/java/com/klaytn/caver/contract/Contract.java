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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.LogsNotification;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.IWallet;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Contract {

    /**
     * A caver instance.
     */
    Caver caver;

    /**
     * A contract ABI(Application Binary interface) json string.
     */
    String abi;

    /**
     * A contract address.
     */
    String contractAddress;

    /**
     * The map where method name string and ContractMethod mapped.
     */
    Map<String, ContractMethod> methods;

    /**
     * The map where event name string and ContractEvent mapped.
     */
    Map<String, ContractEvent> events;

    /**
     * The ContractMethod instance related Contract's constructor.
     */
    ContractMethod constructor;

    /**
     * The default send option. When you execute call() or send() without SendOptions, defaultSendOptions will be used.
     */
    SendOptions defaultSendOptions;

    /**
     * The class instance implemented IWallet to sign transaction.
     */
    IWallet wallet;

    private static final Logger LOGGER = LoggerFactory.getLogger(Contract.class);

    /**
     * Creates a Contract instance.
     * @param caver A Caver instance.
     * @param abi A contract's ABI(Application Binary interface) json string.
     * @return Contract
     * @throws IOException
     */
    public static Contract create(Caver caver, String abi) throws IOException {
        return new Contract(caver, abi);
    }

    /**
     * Creates a Contract instance.
     * @param caver A Caver instance.
     * @param abi A contract's ABI(Application Binary interface) json string.
     * @param contractAddress An address string of contract deployed on Klaytn.
     * @return Contract
     * @throws IOException
     */
    public static Contract create(Caver caver, String abi, String contractAddress) throws IOException {
        return new Contract(caver, abi, contractAddress);
    }

    /**
     * Creates a Contract instance.
     * @param caver A Caver instance.
     * @param abi A contract's ABI(Application Binary interface) json string.
     * @throws IOException
     */
    public Contract(Caver caver, String abi) throws IOException{
        this(caver, abi, null);
    }

    /**
     * Creates a Contract instance.
     * @param caver A Caver instance
     * @param abi A contract's ABI(Application Binary Interface) json string.
     * @param contractAddress An address string of contract deployed on Klaytn.
     * @throws IOException
     */
    public Contract(Caver caver, String abi, String contractAddress) throws IOException {
        setAbi(abi);
        setCaver(caver);
        setContractAddress(contractAddress);
        setDefaultSendOptions(new SendOptions());
        setWallet(caver.getWallet());
    }

    /**
     * Deploy a contract
     * @param sendOptions A SendOption instance.
     * @param contractBinaryData A smart contract binary data.
     * @param constructorParams The smart contract constructor parameters.
     * @return Contract
     * @throws TransactionException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Contract deploy(SendOptions sendOptions, String contractBinaryData, Object... constructorParams) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        ContractDeployParams deployParams = new ContractDeployParams(contractBinaryData, Arrays.asList(constructorParams));
        return deploy(deployParams, sendOptions);
    }

    /**
     * Deploy a contract
     * @param sendOptions A SendOption instance
     * @param receiptProcessor A TransactionReceiptProcessor instance.
     * @param contractBinaryData A smart contract binary data.
     * @param constructorParams The smart contract constructor parameters.
     * @return Contract
     * @throws TransactionException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Contract deploy(SendOptions sendOptions, TransactionReceiptProcessor receiptProcessor, String contractBinaryData, Object... constructorParams) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        ContractDeployParams deployParams = new ContractDeployParams(contractBinaryData, Arrays.asList(constructorParams));
        return deploy(deployParams, sendOptions, receiptProcessor);
    }

    /**
     * Deploy a contract.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor instance.
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @return Contract
     * @throws TransactionException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        return deploy(deployParam, sendOptions, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Deploy a contract
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @param processor A TransactionReceiptProcessor instance.
     * @return Contract
     * @throws TransactionException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions, TransactionReceiptProcessor processor) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        deployParam.getDeployParams().add(0, deployParam.getBytecode());
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod("constructor").send(deployParam.getDeployParams(), sendOptions, processor);

        String contractAddress = receiptData.getContractAddress();
        this.setContractAddress(contractAddress);

        return this;
    }

    /**
     * Subscribes to an event and unsubscribes immediately after the first event or error.<p>
     * You can filter event through setting filterOptions or topics field in EventFilterOptions.
     *
     * <pre>Example :
     * {@code
     * Contract contract = caver.contract.create(ABI, contractAddress);
     *
     * EventFilterOptions options = new EventFilterOptions();
     * options.setFilterOptions(Arrays.asList(new EventFilterOptions.IndexedParameter("from", "0x{address}")));
     * //or
     * options.setTopics(Arrays.asList("topic0", "topic1"));
     *
     * Disposable disposable = contract.once("Transfer", options, (data) -> {});
     * }
     * </pre>
     *
     * @param eventName The name of the event in the contract.
     * @param paramsOption The filter events by indexed parameters.
     * @param callback The callback function that handled to returned data.
     * @return Disposable instance that able to unsubscribe.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Disposable once(String eventName, EventFilterOptions paramsOption, Consumer<LogsNotification> callback) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        KlayFilter filter = new KlayFilter();
        filter.setAddress(getContractAddress());

        if(eventName.equals("allEvents")) {
            if(paramsOption != null) {
                LOGGER.warn("If eventName has 'allEvent', passed paramOption will be ignored.");
            }
        } else {
            ContractEvent event = this.getEvent(eventName);
            if(paramsOption != null) {
                /**
                 * If filterOptions field of EventFilterOptions which has event type name and value is exist,
                 * it encodes topics data according to ABI spec and set by topics field of KlayFilter directly.
                 */
                if(paramsOption.getTopics() == null || paramsOption.getTopics().isEmpty()) {
                    if(paramsOption.getFilterOptions() != null && !paramsOption.getFilterOptions().isEmpty()) {
                        paramsOption.setTopicWithFilterOptions(event);
                        filter.setTopics(paramsOption.toKlayFilterTopic());
                    }
                }
                // If topics field in paramOptions is existed, it is set by topics field of KlayFilter directly.
                else {
                    filter.setTopics(paramsOption.toKlayFilterTopic());
                }
            }
        }

        final Flowable<LogsNotification> events = this.caver.rpc.klay.subscribeFlowable("logs", filter);
        return events.take(1).subscribe(callback);
    }

    /**
     * Get past events for this contract.
     * @param eventName The name of the event in the contract.
     * @param filterOption The KlayLogFilter instance to filter event.
     * @return KlayLogs
     * @throws IOException
     */
    public KlayLogs getPastEvent(String eventName, KlayLogFilter filterOption) throws IOException {
        ContractEvent event = getEvent(eventName);
        filterOption.addSingleTopic(ABI.encodeEventSignature(event));

        KlayLogs logs = caver.rpc.klay.getLogs(filterOption).send();

        return logs;
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<Type> call(String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return call(CallObject.createCallObject(), methodName, methodArguments);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * When creating CallObject, it need not to fill 'data', 'to' fields.
     * The 'data', 'to' fields automatically filled in call() method.
     * @param callObject A CallObject instance to 'call' smart contract method.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<Type> call(CallObject callObject, String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).call(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityType(String methodName, Type... methodArguments) throws IOException, ClassNotFoundException {
        return callWithSolidityType(CallObject.createCallObject(), methodName, methodArguments);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * When creating CallObject, it need not to fill 'data', 'to' fields.
     * The 'data', 'to' fields automatically filled in call() method.
     * @param callObject A CallObject instance to 'call' smart contract method.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityType(CallObject callObject, String methodName, Type... methodArguments) throws IOException, ClassNotFoundException {
        return this.getMethod(methodName).callWithSolidityWrapper(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It is used defaultSendOption field to sendOptions.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws TransactionException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(String methodName, Object... methodArguments) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException   {
        return send(null, methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param options An option to execute smart contract method.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws TransactionException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(SendOptions options, String methodName, Object... methodArguments) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException   {
        return send(options, new PollingTransactionReceiptProcessor(caver, 1000, 15), methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * @param options An option to execute smart contract method.
     * @param receiptProcessor A TransactionReceiptProcessor to get receipt.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws TransactionException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public TransactionReceipt.TransactionReceiptData send(SendOptions options, TransactionReceiptProcessor receiptProcessor, String methodName, Object... methodArguments) throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        ContractMethod contractMethod = this.getMethod(methodName);

        return contractMethod.send(Arrays.asList(methodArguments), options, receiptProcessor);
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It is used defaultSendOption field to sendOptions
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(String methodName, Type... methodArguments) throws IOException, TransactionException {
        return sendWithSolidityType(null, methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param options An option to execute smart contract method.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(SendOptions options, String methodName, Type... methodArguments) throws IOException, TransactionException {
        return sendWithSolidityType(options, new PollingTransactionReceiptProcessor(caver, 1000, 15), methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param options An option to execute smart contract method.
     * @param receiptProcessor A TransactionReceiptProcessor to get receipt.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(SendOptions options, TransactionReceiptProcessor receiptProcessor, String methodName, Type... methodArguments) throws IOException, TransactionException {
        ContractMethod contractMethod = this.getMethod(methodName);

        return contractMethod.sendWithSolidityWrapper(Arrays.asList(methodArguments), options, receiptProcessor);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument.<p>
     * <pre>
     * If the method name is a "constructor", it creates a transaction related to SmartContractDeploy and sign it.
     * The arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     Contract contract = caver.contract.create(abi);
     *     contract.setDefaultSendOptions(sendOptions);
     *
     *     contract.sign("constructor", bytecode, constructor_param1, constructor_param2...);
     * </code>
     * It is used defaultSendOption field to sendOptions.
     * </pre>
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public AbstractTransaction sign(String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).sign(Arrays.asList(methodArguments));
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument.<p>
     * It creates a transaction related to SmartContractDeploy or SmartContractExecution to deploy or execute smart contract method.
     * <pre>
     * If the method name is a "constructor", it creates a transaction related to SmartContractDeploy and sign it.
     * The arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "abi";
     *     String bytecode = "Contract bytecode";
     *
     *     Contract contract = caver.contract.create(abi);
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *
     *     contract.sign(sendOptions, "constructor", bytecode, constructor_param1, constructor_param2...);
     * </code>
     * </pre>
     * @param sendOptions An option to deploy or execute smart contract method.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AbstractTransaction sign(SendOptions sendOptions, String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).sign(Arrays.asList(methodArguments), sendOptions);
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to SmartContractExecution to execute smart contract method.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.<p>
     * It is used defaultSendOption field to sendOptions.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction signWithSolidityType(String methodName, Type... methodArguments) throws IOException {
        return this.getMethod(methodName).signWithSolidityWrapper(Arrays.asList(methodArguments));
    }

    /**
     * Create and sign a transaction with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to SmartContractExecution to execute smart contract method.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.<p>
     * @param sendOptions An option to execute smart contract method.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractTransaction signWithSolidityType(SendOptions sendOptions, String methodName, Type... methodArguments) throws IOException {
        return this.getMethod(methodName).signWithSolidityWrapper(Arrays.asList(methodArguments), sendOptions);
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument.<p>
     * <pre>
     * If the method name is a "constructor", it creates a transaction related to FeeDelegatedSmartContractDeploy and sign it.
     * The arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "Contract ABI data";
     *     String bytecode = "Contract bytecode";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("0x{feePayer}");
     *
     *     Contract contract = caver.contract.create(abi);
     *     contract.setDefaultSendOptions(sendOptions);
     *
     *     contract.signAsFeePayer("constructor", bytecode, constructor_param1, constructor_param2...);
     * </code>
     * </pre>
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AbstractTransaction signAsFeePayer(String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).signAsFeePayer(Arrays.asList(methodArguments));
    }

    /**
     /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument.<p>
     * <pre>
     * If the method name is a "constructor", it creates a transaction related to FeeDelegatedSmartContractDeploy and sign it.
     * The arguments parsed as follow.
     *   - arguments[0] : Smart contract's bytecode.
     *   - others : The constructor arguments to deploy smart contract.
     * <code>
     *     Caver caver = new Caver(Caver.DEFAULT_URL);
     *     String abi = "Contract ABI data";
     *     String bytecode = "Contract bytecode";
     *
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("0x{from}");
     *     sendOptions.setGas(BigInteger.valueOf(100000000));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("0x{feePayer}");
     *
     *     Contract contract = caver.contract.create(abi);
     *     contract.signAsFeePayer(sendOptions, "constructor", bytecode, constructor_param1, constructor_param2...);
     * </code>
     * </pre>
     * @param sendOptions An option to execute smart contract method.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments The list of arguments to execute or deploy a smart contract.
     * @return AbstractTransaction
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(SendOptions sendOptions, String methodName, Object... methodArguments) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).signAsFeePayer(Arrays.asList(methodArguments), sendOptions);
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to FeeDelegatedSmartContractExecution to execute smart contract method.<p>
     * It is used defaultSendOption field to sendOptions.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments A List of parameter that wrapped by solidity type class.
     * @return AbstractTransaction
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayerWithSolidityType(String methodName, Type... methodArguments) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.getMethod(methodName).signAsFeePayerWithSolidityWrapper(Arrays.asList(methodArguments));
    }

    /**
     * Create and sign a transaction as a fee payer with the input data generated by the passed argument that wrapped by solidity type class.<p>
     * It creates a transaction related to FeeDelegatedSmartContractExecution to execute smart contract method.<p>
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param sendOptions An option to execute smart contract method.
     * @param methodName The smart contract's method name to create a transaction and sign it.
     * @param methodArguments A List of parameter that wrapped by solidity type class.
     * @return AbstractTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayerWithSolidityType(SendOptions sendOptions, String methodName, Type... methodArguments) throws IOException {
        return this.getMethod(methodName).signAsFeePayerWithSolidityWrapper(Arrays.asList(methodArguments), sendOptions);
    }

    /**
     * Encodes the ABI for the method in Contract. The resulting hex string is 32-bit function signature hash plus the passed parameters in Solidity tightly packed format.
     * @param methodName The smart contract method name to encode.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return String
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public String encodeABI(String methodName, Object... methodArguments) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ContractMethod method = this.getMethod(methodName);
        return method.encodeABI(Arrays.asList(methodArguments));
    }

    /**
     * Encodes the ABI for the method in Contract with Solidity type wrapper reference. The resulting hex string is 32-bit function signature hash plus the passed parameters in Solidity tightly packed format.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param methodName The smart contract method name to encode.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return String
     */
    public String encodeABIWithSolidityType(String methodName, Type... methodArguments) {
        ContractMethod method = this.getMethod(methodName);
        return method.encodeABIWithSolidityWrapper(Arrays.asList(methodArguments));
    }

    /**
     * Estimate the gas to execute the contract's method.
     * @param callObject An option to execute smart contract method.
     * @param methodName The smart contract method name.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String estimateGas(CallObject callObject, String methodName, Object... methodArguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        ContractMethod method = this.getMethod(methodName);
        return method.estimateGas(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Estimate the gas to execute the contract's method with Solidity type wrapper reference.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param callObject An option to execute smart contract method.
     * @param methodName The smart contract method name.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return String
     * @throws IOException
     */
    public String estimateGasWithSolidityType(CallObject callObject, String methodName, Type... methodArguments) throws IOException {
        ContractMethod method = this.getMethod(methodName);
        return method.estimateGasWithSolidityWrapper(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Decodes a function call data that composed of function selector and encoded input argument.
     * <pre>Example :
     * {@code
     * String contractABI = ".....";
     * Contract contract = caver.contract.create(contractABI);
     *
     * List<Type> decoded = contract.decodeFunctionCall("0x{encoded function call data}");
     * }
     * </pre>
     * @param encodedString The encoded function call data string.
     * @return List&lt;Type&gt;
     * @throws ClassNotFoundException
     */
    public List<Type> decodeFunctionCall(String encodedString) throws ClassNotFoundException {
        String encoded = Utils.stripHexPrefix(encodedString);
        String functionSignature = encoded.substring(0, 8);

        //Search all ContractMethods to find the ContractMethod corresponding to the function signature passed as a parameter.
        ContractMethod findMethod = this.findContractMethodBySignature(functionSignature);
        if(findMethod == null) {
            throw new IllegalArgumentException("Invalid function signature: Cannot find the contract method matched the function signatures extracted from the function call string.");
        }

        return ABI.decodeFunctionCall(findMethod, encodedString);
    }

    /**
     * Find a ContractMethod instance that has the function signature same as passed as a parameter.
     * @param functionSignature The function signature to find a ContractMethod instance.
     * @return ContractMethod
     */
    public ContractMethod findContractMethodBySignature(String functionSignature) {
        ContractMethod findMethod = null;

        for(String key : this.getMethods().keySet()) {
            ContractMethod method = this.getMethod(key);

            findMethod = method.findMethodBySignature(functionSignature);
            if(findMethod != null) {
                break;
            }
        }

        return findMethod;
    }

    /**
     * Returns the ContractMethod instance corresponding to the method name.
     * @param methodName The method name.
     * @return ContractMethod
     */
    public ContractMethod getMethod(String methodName) {
        ContractMethod contractMethod = this.getMethods().get(methodName);
        if(contractMethod == null) {
            throw new NullPointerException(methodName + " method is not exist.");
        }
        return this.getMethods().get(methodName);
    }

    /**
     * Returns the ContractEvent instance corresponding to the event name.
     * @param eventName The event name.
     * @return ContractEvent
     */
    public ContractEvent getEvent(String eventName) {
        ContractEvent contractEvent = this.getEvents().get(eventName);
        if(contractEvent == null) {
            throw new NullPointerException(eventName + " event is not exist.");
        }
        return this.getEvents().get(eventName);
    }

    /**
     * Getter function for caver.
     * @return Caver
     */
    public Caver getCaver() {
        return caver;
    }

    /**
     * Getter function for contract's abi.
     * @return String
     */
    public String getAbi() {
        return abi;
    }

    /**
     * Getter function for contract address.
     * @return String
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Getter function for methods.
     * @return Map
     */
    public Map<String, ContractMethod> getMethods() {
        return methods;
    }

    /**
     * Getter function for events.
     * @return Map
     */
    public Map<String, ContractEvent> getEvents() {
        return events;
    }

    /**
     * Getter function for Contract's constructor function info.
     * @return ContractMethod.
     */
    public ContractMethod getConstructor() {
        return constructor;
    }

    /**
     * Getter function for DefaultSendOptions
     * @return SendOptions
     */
    public SendOptions getDefaultSendOptions() {
        return defaultSendOptions;
    }

    /**
     * Getter function for wallet
     * @return IWallet
     */
    public IWallet getWallet() {
        return wallet;
    }

    /**
     * Setter function for Caver.
     * @param caver The Caver instance.
     */
    void setCaver(Caver caver) {
        this.caver = caver;

        //When Caver instance changes, the caver instance of each ContractMethod is also replaced.
        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setCaver(caver));
        }
    }

    /**
     * Setter function for contract's abi.
     * @param abi The abi json string.
     * @throws IOException
     */
    void setAbi(String abi) throws IOException {
        this.abi = abi;

        //When abi changes, It newly set a "methods" and "events".
        init(this.abi);
    }

    /**
     * Setter function for contract address.
     * @param contractAddress The contract address.
     */
    void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;

        //When contract address changes, the contract address of each ContractMethod is also replaced.
        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setContractAddress(this.contractAddress));
        }
    }

    /**
     * Setter function for methods.
     * @param methods The map where method name string and ContractMethod mapped.
     */
    void setMethods(Map<String, ContractMethod> methods) {
        this.methods = methods;
    }

    /**
     * Setter function for events.
     * @param events The map where event name string and ContractEvent mapped.
     */
    void setEvents(Map<String, ContractEvent> events) {
        this.events = events;
    }

    /**
     * Setter function for constructor.
     * @param constructor The ContractMethod instance related Contract's constructor.
     */
    void setConstructor(ContractMethod constructor) {
        this.constructor = constructor;
    }

    /**
     * Setter function for wallet
     * @param wallet The class instance implemented IWallet to sign transaction.
     */
    public void setWallet(IWallet wallet) {
        this.wallet = wallet;

        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setWallet(this.wallet));
        }
    }

    /**
     * Setter function for defaultSendOption
     * @param defaultSendOptions The sendOptions to set DefaultSendOptions field.
     */
    public void setDefaultSendOptions(SendOptions defaultSendOptions) {
        this.defaultSendOptions = defaultSendOptions;

        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setDefaultSendOptions(this.defaultSendOptions));
        }
    }

    /**
     * Parse ABI json string and generate the mapped data related to method and event.
     * @param abi The contract's ABI(Application Binary Interface) json string.
     * @throws IOException
     */
    private void init(String abi) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        methods = new HashMap<>();
        events = new HashMap<>();

        JsonNode root = objectMapper.readTree(abi);
        Iterator<JsonNode> iterator = root.iterator();

        while(iterator.hasNext()) {
            JsonNode element = iterator.next();
            if(element.get("type").asText().equals("function")) {
                ContractMethod newMethod = objectMapper.readValue(element.toString(), ContractMethod.class);
                newMethod.setSignature(ABI.encodeFunctionSignature(newMethod));

                ContractMethod existedMethod = this.methods.get(newMethod.getName());
                if(existedMethod != null) {
                    boolean isWarning = existedMethod.getNextContractMethods().stream().anyMatch(contractMethod -> {
                        return contractMethod.getInputs().size() == newMethod.getInputs().size();
                    });

                    if(existedMethod.getInputs().size() == newMethod.getInputs().size() || isWarning) {
                        LOGGER.warn("An overloaded function with the same number of parameters may not be executed normally. Please use *withSolidityWrapper methods in ContractMethod class.");
                    }

                    existedMethod.getNextContractMethods().add(newMethod);
                } else {
                    methods.put(newMethod.getName(), newMethod);
                }

            } else if(element.get("type").asText().equals("event")) {
                ContractEvent event = objectMapper.readValue(element.toString(), ContractEvent.class);
                event.setSignature(ABI.encodeEventSignature(event));
                events.put(event.getName(), event);
            } else if(element.get("type").asText().equals("constructor")) {
                ContractMethod method = objectMapper.readValue(element.toString(), ContractMethod.class);
                //add a constructor info in methods.
                methods.put("constructor", method);
                this.constructor = method;
            }
        }
        //if the constructor is not existed in ABI, creates a dummy instance and adds it.
        if(methods.get("constructor") == null) {
            ContractMethod method = new ContractMethod();
            method.setType(ContractMethod.TYPE_CONSTRUCTOR);
            method.setInputs(new ArrayList<ContractIOType>());

            methods.put("constructor", method);
            this.constructor = method;
        }
    }
}
