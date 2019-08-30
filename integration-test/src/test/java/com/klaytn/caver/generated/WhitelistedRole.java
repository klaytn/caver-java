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
public class WhitelistedRole extends SmartContract {
    private static final String BINARY = "6080604052610016336001600160e01b0361001b16565b61018f565b61003381600061006a60201b6104161790919060201c565b6040516001600160a01b038216907f22380c05984257a1cb900161c713dd71d39e74820f1aea43bd3f1bdd2096129990600090a250565b61007d82826001600160e01b0361010e16565b156100e957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661016f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806107546022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6105b68061019e6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80634c5a628c1161005b5780634c5a628c1461010a5780637362d9c814610112578063bb5f747b14610138578063d6cd94731461015e5761007d565b806310154bad14610082578063291d9549146100aa5780633af32abf146100d0575b600080fd5b6100a86004803603602081101561009857600080fd5b50356001600160a01b0316610166565b005b6100a8600480360360208110156100c057600080fd5b50356001600160a01b03166101b6565b6100f6600480360360208110156100e657600080fd5b50356001600160a01b0316610203565b604080519115158252519081900360200190f35b6100a861021c565b6100a86004803603602081101561012857600080fd5b50356001600160a01b0316610227565b6100f66004803603602081101561014e57600080fd5b50356001600160a01b0316610274565b6100a8610286565b61016f33610274565b6101aa5760405162461bcd60e51b81526004018080602001828103825260408152602001806105426040913960400191505060405180910390fd5b6101b38161028f565b50565b6101bf33610274565b6101fa5760405162461bcd60e51b81526004018080602001828103825260408152602001806105426040913960400191505060405180910390fd5b6101b3816102d7565b600061021660018363ffffffff61031f16565b92915050565b61022533610386565b565b61023033610274565b61026b5760405162461bcd60e51b81526004018080602001828103825260408152602001806105426040913960400191505060405180910390fd5b6101b3816103ce565b6000610216818363ffffffff61031f16565b610225336102d7565b6102a060018263ffffffff61041616565b6040516001600160a01b038216907fee1504a83b6d4a361f4c1dc78ab59bfa30d6a3b6612c403e86bb01ef2984295f90600090a250565b6102e860018263ffffffff61049716565b6040516001600160a01b038216907f270d9b30cf5b0793bbfd54c9d5b94aeb49462b8148399000265144a8722da6b690600090a250565b60006001600160a01b0382166103665760405162461bcd60e51b81526004018080602001828103825260228152602001806105206022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61039760008263ffffffff61049716565b6040516001600160a01b038216907f0a8eb35e5ca14b3d6f28e4abf2f128dbab231a58b56e89beb5d636115001e16590600090a250565b6103df60008263ffffffff61041616565b6040516001600160a01b038216907f22380c05984257a1cb900161c713dd71d39e74820f1aea43bd3f1bdd2096129990600090a250565b610420828261031f565b15610472576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b6104a1828261031f565b6104dc5760405162461bcd60e51b81526004018080602001828103825260218152602001806104ff6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f206164647265737357686974656c69737441646d696e526f6c653a2063616c6c657220646f6573206e6f742068617665207468652057686974656c69737441646d696e20726f6c65a265627a7a72305820f69912a8f338568b5898086afd904fde9a06b4e9608f29b1e3a937390fdb9b1164736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_ADDWHITELISTED = "addWhitelisted";

    public static final String FUNC_REMOVEWHITELISTED = "removeWhitelisted";

    public static final String FUNC_ISWHITELISTED = "isWhitelisted";

    public static final String FUNC_RENOUNCEWHITELISTADMIN = "renounceWhitelistAdmin";

    public static final String FUNC_ADDWHITELISTADMIN = "addWhitelistAdmin";

    public static final String FUNC_ISWHITELISTADMIN = "isWhitelistAdmin";

    public static final String FUNC_RENOUNCEWHITELISTED = "renounceWhitelisted";

    public static final Event WHITELISTEDADDED_EVENT = new Event("WhitelistedAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTEDREMOVED_EVENT = new Event("WhitelistedRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTADMINADDED_EVENT = new Event("WhitelistAdminAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTADMINREMOVED_EVENT = new Event("WhitelistAdminRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected WhitelistedRole(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected WhitelistedRole(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addWhitelisted(String account) {
        final Function function = new Function(
                FUNC_ADDWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeWhitelisted(String account) {
        final Function function = new Function(
                FUNC_REMOVEWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isWhitelisted(String account) {
        final Function function = new Function(FUNC_ISWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceWhitelistAdmin() {
        final Function function = new Function(
                FUNC_RENOUNCEWHITELISTADMIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addWhitelistAdmin(String account) {
        final Function function = new Function(
                FUNC_ADDWHITELISTADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isWhitelistAdmin(String account) {
        final Function function = new Function(FUNC_ISWHITELISTADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceWhitelisted() {
        final Function function = new Function(
                FUNC_RENOUNCEWHITELISTED, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<WhitelistedAddedEventResponse> getWhitelistedAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTEDADDED_EVENT, transactionReceipt);
        ArrayList<WhitelistedAddedEventResponse> responses = new ArrayList<WhitelistedAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistedAddedEventResponse typedResponse = new WhitelistedAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistedRemovedEventResponse> getWhitelistedRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTEDREMOVED_EVENT, transactionReceipt);
        ArrayList<WhitelistedRemovedEventResponse> responses = new ArrayList<WhitelistedRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistedRemovedEventResponse typedResponse = new WhitelistedRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistAdminAddedEventResponse> getWhitelistAdminAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTADMINADDED_EVENT, transactionReceipt);
        ArrayList<WhitelistAdminAddedEventResponse> responses = new ArrayList<WhitelistAdminAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistAdminAddedEventResponse typedResponse = new WhitelistAdminAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistAdminRemovedEventResponse> getWhitelistAdminRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTADMINREMOVED_EVENT, transactionReceipt);
        ArrayList<WhitelistAdminRemovedEventResponse> responses = new ArrayList<WhitelistAdminRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistAdminRemovedEventResponse typedResponse = new WhitelistAdminRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static WhitelistedRole load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new WhitelistedRole(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static WhitelistedRole load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WhitelistedRole(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WhitelistedRole> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WhitelistedRole.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<WhitelistedRole> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WhitelistedRole.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class WhitelistedAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistedRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistAdminAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistAdminRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
