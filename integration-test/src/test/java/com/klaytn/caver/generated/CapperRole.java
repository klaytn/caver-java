package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class CapperRole extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_ISCAPPER = "isCapper";

    public static final String FUNC_RENOUNCECAPPER = "renounceCapper";

    public static final String FUNC_ADDCAPPER = "addCapper";

    public static final Event CAPPERADDED_EVENT = new Event("CapperAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event CAPPERREMOVED_EVENT = new Event("CapperRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected CapperRole(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected CapperRole(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> isCapper(String account) {
        final Function function = new Function(FUNC_ISCAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceCapper() {
        final Function function = new Function(
                FUNC_RENOUNCECAPPER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addCapper(String account) {
        final Function function = new Function(
                FUNC_ADDCAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<CapperAddedEventResponse> getCapperAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(CAPPERADDED_EVENT, transactionReceipt);
        ArrayList<CapperAddedEventResponse> responses = new ArrayList<CapperAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            CapperAddedEventResponse typedResponse = new CapperAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<CapperRemovedEventResponse> getCapperRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(CAPPERREMOVED_EVENT, transactionReceipt);
        ArrayList<CapperRemovedEventResponse> responses = new ArrayList<CapperRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            CapperRemovedEventResponse typedResponse = new CapperRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CapperRole load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new CapperRole(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static CapperRole load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CapperRole(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CapperRole> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CapperRole.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<CapperRole> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CapperRole.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class CapperAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class CapperRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
