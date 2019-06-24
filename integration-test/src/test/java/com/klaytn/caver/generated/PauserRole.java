package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
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
public class PauserRole extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final Event PAUSERADDED_EVENT = new Event("PauserAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSERREMOVED_EVENT = new Event("PauserRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected PauserRole(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PauserRole(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> isPauser(String account) {
        final Function function = new Function(FUNC_ISPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renouncePauser() {
        final Function function = new Function(
                FUNC_RENOUNCEPAUSER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addPauser(String account) {
        final Function function = new Function(
                FUNC_ADDPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<PauserAddedEventResponse> getPauserAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERADDED_EVENT, transactionReceipt);
        ArrayList<PauserAddedEventResponse> responses = new ArrayList<PauserAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PauserAddedEventResponse typedResponse = new PauserAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PauserRemovedEventResponse> getPauserRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERREMOVED_EVENT, transactionReceipt);
        ArrayList<PauserRemovedEventResponse> responses = new ArrayList<PauserRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PauserRemovedEventResponse typedResponse = new PauserRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PauserRole load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PauserRole(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PauserRole load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PauserRole(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PauserRole> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PauserRole.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<PauserRole> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PauserRole.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class PauserAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class PauserRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
