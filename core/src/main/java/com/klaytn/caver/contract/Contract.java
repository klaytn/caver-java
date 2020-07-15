package com.klaytn.caver.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.utils.CodeFormat;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Contract {
    Caver caver;

    String abi;
    String binaryData;
    String contractAddress;

    Map<String, ContractMethod> methods;
    Map<String, ContractEvent> events;
    ContractMethod constructor;

    public Contract(Caver caver, String abi, String contractAddress) throws IOException{
        this(caver,abi, null, contractAddress);
    }

    public Contract(Caver caver, String abi, String binaryData, String contractAddress) throws IOException {
        this.caver = caver;
        this.abi = abi;
        this.binaryData = binaryData;
        this.contractAddress = contractAddress;
        init(abi);
    }

    public Contract deploy(ContractDeployParam params) throws IOException, TransactionException {
        return deploy(params, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    public Contract deploy(ContractDeployParam params, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        List<Type> deployParams = params.getDeployParams();
        this.constructor.checkTypeValid(deployParams);

        String encodedParams = ABI.encodeParameter(deployParams);
        String input = params.getBinaryData() + encodedParams;

        SmartContractDeploy smartContractDeploy = new SmartContractDeploy.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(params.getFrom())
                .setInput(input)
                .setCodeFormat(CodeFormat.EVM)
                .setHumanReadable(false)
                .setGas(params.getGas())
                .setGasPrice(params.getGasPrice())
                .build();

        caver.wallet.sign(params.getFrom(), smartContractDeploy);

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractDeploy.getRawTransaction()).send();
        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());

        String contractAddress = receipt.getContractAddress();
        this.setContractAddress(contractAddress);
        return this;
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
                method.setCaver(caver);
                methods.put(method.getName(), method);
            } else if(element.get("type").asText().equals("event")) {
                ContractEvent event = objectMapper.readValue(element.toString(), ContractEvent.class);
                events.put(event.getName(), event);
            } else if(element.get("type").asText().equals("constructor")) {
                ContractMethod method = objectMapper.readValue(element.toString(), ContractMethod.class);
                this.constructor = method;
            }
        }
    }

    public Caver getCaver() {
        return caver;
    }

    public String getAbi() {
        return abi;
    }

    public String getBinaryData() {
        return binaryData;
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
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public void setBinaryData(String binaryData) {
        this.binaryData = binaryData;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
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
}
