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
public class SignatureBouncer extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_ISSIGNER = "isSigner";

    public static final String FUNC_RENOUNCESIGNER = "renounceSigner";

    public static final String FUNC_ADDSIGNER = "addSigner";

    public static final Event SIGNERADDED_EVENT = new Event("SignerAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event SIGNERREMOVED_EVENT = new Event("SignerRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected SignatureBouncer(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SignatureBouncer(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> isSigner(String account) {
        final Function function = new Function(FUNC_ISSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceSigner() {
        final Function function = new Function(
                FUNC_RENOUNCESIGNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addSigner(String account) {
        final Function function = new Function(
                FUNC_ADDSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<SignerAddedEventResponse> getSignerAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNERADDED_EVENT, transactionReceipt);
        ArrayList<SignerAddedEventResponse> responses = new ArrayList<SignerAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            SignerAddedEventResponse typedResponse = new SignerAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<SignerRemovedEventResponse> getSignerRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNERREMOVED_EVENT, transactionReceipt);
        ArrayList<SignerRemovedEventResponse> responses = new ArrayList<SignerRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            SignerRemovedEventResponse typedResponse = new SignerRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SignatureBouncer load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SignatureBouncer(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SignatureBouncer load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignatureBouncer(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignatureBouncer> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignatureBouncer.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SignatureBouncer> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignatureBouncer.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class SignerAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class SignerRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
