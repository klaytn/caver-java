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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class Secondary extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_TRANSFERPRIMARY = "transferPrimary";

    public static final String FUNC_PRIMARY = "primary";

    public static final Event PRIMARYTRANSFERRED_EVENT = new Event("PrimaryTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected Secondary(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected Secondary(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferPrimary(String recipient) {
        final Function function = new Function(
                FUNC_TRANSFERPRIMARY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> primary() {
        final Function function = new Function(FUNC_PRIMARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<PrimaryTransferredEventResponse> getPrimaryTransferredEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PRIMARYTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<PrimaryTransferredEventResponse> responses = new ArrayList<PrimaryTransferredEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PrimaryTransferredEventResponse typedResponse = new PrimaryTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static Secondary load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new Secondary(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static Secondary load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Secondary(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Secondary> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Secondary.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Secondary> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Secondary.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class PrimaryTransferredEventResponse {
        public KlayLogs.Log log;

        public String recipient;
    }
}
