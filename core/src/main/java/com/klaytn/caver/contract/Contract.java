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
import com.klaytn.caver.utils.Utils;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.protocol.websocket.events.LogNotification;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Contract {
    Caver caver;

    String abi;
    String contractAddress;

    Map<String, ContractMethod> methods;
    Map<String, ContractEvent> events;
    ContractMethod constructor;

    public Contract(Caver caver, String abi) throws IOException{
        this(caver, abi, null);
    }

    public Contract(Caver caver, String abi, String contractAddress) throws IOException{
        setAbi(abi);
        setCaver(caver);
        setContractAddress(contractAddress);
    }

    public Contract deploy(ContractDeployParam deployParam, SendOptions sendOptions) throws IOException, TransactionException {
        return deploy(deployParam, sendOptions, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    public Contract deploy(ContractDeployParam params, SendOptions sendOptions, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        List<Type> deployParams = params.getDeployParams();
        this.constructor.checkTypeValid(deployParams);

        String encodedParams = ABI.encodeParameters(deployParams);
        String input = params.getBytecode() + encodedParams;

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

    public Disposable once(String eventName, List paramsOption, Consumer<LogNotification> callback) {
        Map options = new HashMap<>();
        List topics = new ArrayList();

        String eventSignature = ABI.encodeEventSignature(this.getEvent(eventName));
        topics.add(eventSignature);

        for(int i=0; i<paramsOption.size(); i++) {
            if(paramsOption.get(i) instanceof Type) {
                String encoded = ABI.encodeParameter((Type)paramsOption.get(i));
                topics.add(Utils.addHexPrefix(encoded));
            } else if(paramsOption.get(i) instanceof List) {
                List<Type> optionTopicList = (List<Type>) paramsOption.get(i);
                List<String> arr = optionTopicList.stream()
                                                .map(ABI::encodeParameter)
                                                .map(Utils::addHexPrefix)
                                                .collect(Collectors.toList());
                topics.add(arr);
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


    public KlayLogs getPastEvent(String eventName, KlayLogFilter filterOption) throws IOException {
        ContractEvent event = getEvent(eventName);
        filterOption.addSingleTopic(ABI.encodeEventSignature(event));

        KlayLogs logs = caver.rpc.klay.getLogs(filterOption).send();

        return logs;
    }

    public ContractMethod getMethod(String methodName) {
        return this.getMethods().get(methodName);
    }

    public ContractEvent getEvent(String eventName) {
        return this.getEvents().get(eventName);
    }

    public Caver getCaver() {
        return caver;
    }

    public String getAbi() {
        return abi;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public Map<String, ContractMethod> getMethods() {
        return methods;
    }

    public Map<String, ContractEvent> getEvents() {
        return events;
    }

    public ContractMethod getConstructor() {
        return constructor;
    }

    public void setCaver(Caver caver) {
        this.caver = caver;

        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setCaver(caver));
        }
    }

    public void setAbi(String abi) throws IOException {
        this.abi = abi;
        init(this.abi);
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;

        if(this.methods != null && this.methods.size() != 0) {
            this.getMethods().values().forEach(value -> value.setContractAddress(contractAddress));
        }
    }

    public void setMethods(Map<String, ContractMethod> methods) {
        this.methods = methods;
    }

    public void setEvents(Map<String, ContractEvent> events) {
        this.events = events;
    }

    public void setConstructor(ContractMethod constructor) {
        this.constructor = constructor;
    }

    private void init(String abi) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        methods = new HashMap<>();
        events = new HashMap<>();

        JsonNode root = objectMapper.readTree(abi);
        Iterator<JsonNode> iterator = root.iterator();

        while(iterator.hasNext()) {
            JsonNode element = iterator.next();
            if(element.get("type").asText().equals("function")) {
                ContractMethod method = objectMapper.readValue(element.toString(), ContractMethod.class);
                method.setSignature(ABI.buildFunctionSignature(method));
                methods.put(method.getName(), method);
            } else if(element.get("type").asText().equals("event")) {
                ContractEvent event = objectMapper.readValue(element.toString(), ContractEvent.class);
                event.setSignature(ABI.buildEventSignature(event));
                events.put(event.getName(), event);
            } else if(element.get("type").asText().equals("constructor")) {
                ContractMethod method = objectMapper.readValue(element.toString(), ContractMethod.class);
                this.constructor = method;
            }
        }
    }
}
