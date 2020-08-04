package com.klaytn.caver.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.utils.CodeFormat;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Contract(Caver caver, String abi, String contractAddress) throws IOException{
        setAbi(abi);
        setCaver(caver);
        setContractAddress(contractAddress);
        setDefaultSendOptions(new SendOptions());
    }

    /**
     * Deploy a contract.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor instance.
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @return Contract
     * @throws IOException
     * @throws TransactionException
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return deploy(deployParam, sendOptions, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Deploy a contract
     * @param deployParam A DeployParam instance.
     * @param sendOptions A SendOption instance.
     * @param processor A TransactionReceiptProcessor instance.
     * @return Contract
     * @throws IOException
     * @throws TransactionException
     */
    public Contract deploy(ContractDeployParams deployParam, SendOptions sendOptions, TransactionReceiptProcessor processor) throws IOException, TransactionException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String encodedParams = "";

        if(this.getConstructor() != null) {
            this.getConstructor().checkTypeValid(deployParam.getDeployParams());
            encodedParams = ABI.encodeParameters(this.getConstructor(), deployParam.getDeployParams());
        }

        String input = deployParam.getBytecode() + encodedParams;

        SmartContractDeploy smartContractDeploy = new SmartContractDeploy.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(sendOptions.getFrom())
                .setInput(input)
                .setCodeFormat(CodeFormat.EVM)
                .setHumanReadable(false)
                .setGas(sendOptions.getGas())
                .build();

        caver.wallet.sign(sendOptions.getFrom(), smartContractDeploy);

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractDeploy.getRawTransaction()).send();
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
                this.caver.rpc.getWeb3jService(),
                EthSubscribe.class
        );

        final Flowable<LogNotification> events = this.caver.rpc.getWeb3jService().subscribe(subscribeRequest, "klay_unsubscribe", LogNotification.class);
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
     * Setter function for Caver.
     * @param caver The Caver instance.
     */
    public void setCaver(Caver caver) {
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
    public void setAbi(String abi) throws IOException {
        this.abi = abi;

        //When abi changes, It newly set a "methods" and "events".
        init(this.abi);
    }

    /**
     * Setter function for contract address.
     * @param contractAddress The contract address.
     */
    public void setContractAddress(String contractAddress) {
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
    public void setMethods(Map<String, ContractMethod> methods) {
        this.methods = methods;
    }

    /**
     * Setter function for events.
     * @param events The map where event name string and ContractEvent mapped.
     */
    public void setEvents(Map<String, ContractEvent> events) {
        this.events = events;
    }

    /**
     * Setter function for constructor.
     * @param constructor The ContractMethod instance related Contract's constructor.
     */
    public void setConstructor(ContractMethod constructor) {
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
