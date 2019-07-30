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
public class PauserRoleMock extends SmartContract {
    private static final String BINARY = "6080604052610016336001600160e01b0361001b16565b61018f565b61003381600061006a60201b6102b41790919060201c565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61007d82826001600160e01b0361010e16565b156100e957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661016f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806105e26022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6104448061019e6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063329daf901461005c57806346fbf68e146100665780636b2c0f55146100a05780636ef8d66d146100c657806382dc1ec4146100ce575b600080fd5b6100646100f4565b005b61008c6004803603602081101561007c57600080fd5b50356001600160a01b031661013a565b604080519115158252519081900360200190f35b610064600480360360208110156100b657600080fd5b50356001600160a01b0316610152565b61006461015e565b610064600480360360208110156100e457600080fd5b50356001600160a01b0316610167565b6100fd3361013a565b6101385760405162461bcd60e51b815260040180806020018281038252603081526020018061039d6030913960400191505060405180910390fd5b565b600061014c818363ffffffff6101b416565b92915050565b61015b8161021b565b50565b6101383361021b565b6101703361013a565b6101ab5760405162461bcd60e51b815260040180806020018281038252603081526020018061039d6030913960400191505060405180910390fd5b61015b81610224565b60006001600160a01b0382166101fb5760405162461bcd60e51b81526004018080602001828103825260228152602001806103ee6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61015b8161026c565b61023560008263ffffffff6102b416565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61027d60008263ffffffff61033516565b6040516001600160a01b038216907fcd265ebaf09df2871cc7bd4133404a235ba12eff2041bb89d9c714a2621c7c7e90600090a250565b6102be82826101b4565b15610310576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b61033f82826101b4565b61037a5760405162461bcd60e51b81526004018080602001828103825260218152602001806103cd6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe506175736572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652050617573657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373a265627a7a72305820f175b831e015508230ca095e7b7c0e6904cdc60421cd1c60014ecc6b340ba39964736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_ONLYPAUSERMOCK = "onlyPauserMock";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_REMOVEPAUSER = "removePauser";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final Event PAUSERADDED_EVENT = new Event("PauserAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSERREMOVED_EVENT = new Event("PauserRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected PauserRoleMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PauserRoleMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public void onlyPauserMock() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<Boolean> isPauser(String account) {
        final Function function = new Function(FUNC_ISPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removePauser(String account) {
        final Function function = new Function(
                FUNC_REMOVEPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static PauserRoleMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PauserRoleMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PauserRoleMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PauserRoleMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PauserRoleMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PauserRoleMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<PauserRoleMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PauserRoleMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
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
