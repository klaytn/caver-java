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
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.rpc.RPC;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.wallet.IWallet;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.websocket.events.LogNotification;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Contract {

    /**
     * A RPC instance to call klaytn JSON/RPC API.
     */
    RPC rpc;

    /**
     * A Class instance implemented IWallet.
     */
    IWallet wallet;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(Contract.class);

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
        this(caver.getWallet(), caver.getRpc(), abi, contractAddress);
    }

    /**
     * Creates a Contract instance.
     * @param wallet A Class instance implemented IWallet.
     * @param rpc A RPC instance to call klaytn JSON/RPC API.
     * @param abi A contract's ABI(Application Binary Interface) json string.
     * @throws IOException
     */
    public Contract(IWallet wallet, RPC rpc, String abi) throws IOException{
        this(wallet, rpc, abi, null);
    }

    /**
     * Create a Contract instance.
     * @param wallet A Class instance implemented IWallet.
     * @param rpc A RPC instance to call klaytn JSON/RPC API.
     * @param abi A contract's ABI(Application Binary Interface) json string.
     * @param contractAddress An address string of contract deployed on Klaytn.
     * @throws IOException
     */
    public Contract(IWallet wallet, RPC rpc, String abi, String contractAddress) throws IOException {
        setAbi(abi);
        setWallet(wallet);
        setRpc(rpc);
        setContractAddress(contractAddress);
        setDefaultSendOptions(new SendOptions());
    }

    /**
     * Deploy a contract
     * @param sendOptions A SendOption instance.
     * @param contractBinaryData A smart contract binary data.
     * @param constructorParams The smart contract constructor parameters.
     * @return Contract
     * @throws Exception
     */
    public Contract deploy(SendOptions sendOptions, String contractBinaryData, Object... constructorParams) throws Exception {
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
     * @throws Exception
     */
    public Contract deploy(SendOptions sendOptions, TransactionReceiptProcessor receiptProcessor, String contractBinaryData, Object... constructorParams) throws Exception {
        ContractDeployParams deployParams = new ContractDeployParams(contractBinaryData, Arrays.asList(constructorParams));
        return deploy(deployParams, sendOptions, receiptProcessor);
    }

    /**
     * Deploy a contract.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor instance.
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @return Contract
     * @throws Exception
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions) throws Exception {
        return deploy(deployParam, sendOptions, new PollingTransactionReceiptProcessor(getRpc(), 1000, 15));
    }

    /**
     * Deploy a contract
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @param processor A TransactionReceiptProcessor instance.
     * @return Contract
     * @throws Exception
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions, TransactionReceiptProcessor processor) throws Exception {
        String input = ABI.encodeContractDeploy(this.getConstructor(), deployParam.getBytecode(), deployParam.getDeployParams());

        SmartContractDeploy smartContractDeploy = new SmartContractDeploy.Builder()
                .setKlaytnCall(rpc.klay)
                .setFrom(sendOptions.getFrom())
                .setInput(input)
                .setCodeFormat(CodeFormat.EVM)
                .setHumanReadable(false)
                .setGas(sendOptions.getGas())
                .build();

        getWallet().sign(sendOptions.getFrom(), smartContractDeploy);

        Bytes32 txHash = rpc.klay.sendRawTransaction(smartContractDeploy.getRawTransaction()).send();
        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());

        String contractAddress = receipt.getContractAddress();
        this.setContractAddress(contractAddress);
        return this;
    }

    /**
     * Subscribes to an event and unsubscribes immediately after the first event or error.
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
    public Disposable once(String eventName, EventFilterOptions paramsOption, Consumer<LogNotification> callback) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map options = new HashMap<>();

        List topics = null;

        if(eventName.equals("allEvents")) {
            if(paramsOption != null) {
                LOGGER.warn("If eventName has 'allEvent', passed paramOption will be ignored.");
            }
        } else {
            ContractEvent event = this.getEvent(eventName);
            if(paramsOption.getTopics() == null || paramsOption.getTopics().size() == 0) {
                topics = EventFilterOptions.convertsTopic(event, paramsOption);
            } else {
                topics = paramsOption.getTopics();
            }
        }

        options.put("address", getContractAddress());
        options.put("topics", topics);

        final Request<?, EthSubscribe> subscribeRequest = new Request<>(
                "klay_subscribe",
                Arrays.asList("logs", options),
                getRpc().getWeb3jService(),
                EthSubscribe.class
        );

        final Flowable<LogNotification> events = getRpc().getWeb3jService().subscribe(subscribeRequest, "klay_unsubscribe", LogNotification.class);
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

        KlayLogs logs = getRpc().klay.getLogs(filterOption).send();

        return logs;
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<Type> call(String methodName, Object... methodArguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
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
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<Type> call(CallObject callObject, String methodName, Object... methodArguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        ContractMethod contractMethod = this.getMethod(methodName);
        return contractMethod.call(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param methodName The smart contract method name to execute.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return List
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<Type> callWithSolidityType(String methodName, Type... methodArguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
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
        ContractMethod contractMethod = this.getMethod(methodName);

        return contractMethod.callWithSolidityWrapper(Arrays.asList(methodArguments), callObject);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It is used defaultSendOption field to sendOptions.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData send(String methodName, Object... methodArguments) throws Exception {
        return send(null, methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param options An option to execute smart contract method.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData send(SendOptions options, String methodName, Object... methodArguments) throws Exception {
        return send(options, new PollingTransactionReceiptProcessor(getRpc(), 1000, 15), methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * @param options An option to execute smart contract method.
     * @param receiptProcessor A TransactionReceiptProcessor to get receipt.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData send(SendOptions options, TransactionReceiptProcessor receiptProcessor, String methodName, Object... methodArguments) throws Exception {
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
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(String methodName, Type... methodArguments) throws Exception {
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
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(SendOptions options, String methodName, Type... methodArguments) throws Exception {
        return sendWithSolidityType(options, new PollingTransactionReceiptProcessor(getRpc(), 1000, 15), methodName, methodArguments);
    }

    /**
     * Send a transaction to smart contract and execute its method using solidity type wrapper class.
     * It is recommended to use this function when you want to execute one of the functions with the same number of parameters.
     * @param options An option to execute smart contract method.
     * @param receiptProcessor A TransactionReceiptProcessor to get receipt.
     * @param methodName The smart contract method name to execute
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return TransactionReceiptData
     * @throws Exception
     */
    public TransactionReceipt.TransactionReceiptData sendWithSolidityType(SendOptions options, TransactionReceiptProcessor receiptProcessor, String methodName, Type... methodArguments) throws Exception {
        ContractMethod contractMethod = this.getMethod(methodName);

        return contractMethod.sendWithSolidityWrapper(Arrays.asList(methodArguments), options, receiptProcessor);
    }

    /**
     * Encodes the ABI for the method in Contract. The resulting hex string is 32-bit function signature hash plus the passed parameters in Solidity tightly packed format.
     * @param methodName The smart contract method name to encode.
     * @param methodArguments The arguments that need to execute smart contract method.
     * @return String
     * @throws Exception
     */
    public String encodeABI(String methodName, Object... methodArguments) throws Exception {
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
     * Getter function for wallet.
     * @return IWallet
     */
    public IWallet getWallet() {
        return wallet;
    }

    /**
     * Getter function for RPC
     * @return RPC
     */
    public RPC getRpc() {
        return rpc;
    }

    /**
     * Setter function for wallet.
     * @param wallet The class instance implemented IWallet interface.
     */
    public void setWallet(IWallet wallet) {
        this.wallet = wallet;

        //When IWallet instance changes, the caver instance of each ContractMethod is also replaced.
        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setWallet(wallet));
        }
    }

    /**
     * Setter function for RPC
     * @param rpc A RPC instance to call klaytn JSON/RPC API.
     */
    public void setRpc(RPC rpc) {
        this.rpc = rpc;

        //When IWallet instance changes, the caver instance of each ContractMethod is also replaced.
        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setRpc(rpc));
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
                this.constructor = method;
            }
        }
    }
}
