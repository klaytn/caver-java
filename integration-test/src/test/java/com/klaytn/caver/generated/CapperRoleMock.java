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
public class CapperRoleMock extends SmartContract {
    private static final String BINARY = "6080604052610016336001600160e01b0361001b16565b61018f565b61003381600061006a60201b6102b41790919060201c565b6040516001600160a01b038216907fa7555c95b69d4f5cc847881feb4ab2883a1921319e34fa2043747b793d65b36e90600090a250565b61007d82826001600160e01b0361010e16565b156100e957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661016f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806105e26022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6104448061019e6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063395645611461005c5780633f4a6484146100965780635d5576f8146100be5780638dfbcf36146100c6578063d1e07bcb146100ec575b600080fd5b6100826004803603602081101561007257600080fd5b50356001600160a01b03166100f4565b604080519115158252519081900360200190f35b6100bc600480360360208110156100ac57600080fd5b50356001600160a01b031661010c565b005b6100bc610118565b6100bc600480360360208110156100dc57600080fd5b50356001600160a01b0316610123565b6100bc610170565b6000610106818363ffffffff6101b416565b92915050565b6101158161021b565b50565b6101213361021b565b565b61012c336100f4565b6101675760405162461bcd60e51b81526004018080602001828103825260308152602001806103e06030913960400191505060405180910390fd5b61011581610224565b610179336100f4565b6101215760405162461bcd60e51b81526004018080602001828103825260308152602001806103e06030913960400191505060405180910390fd5b60006001600160a01b0382166101fb5760405162461bcd60e51b81526004018080602001828103825260228152602001806103be6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6101158161026c565b61023560008263ffffffff6102b416565b6040516001600160a01b038216907fa7555c95b69d4f5cc847881feb4ab2883a1921319e34fa2043747b793d65b36e90600090a250565b61027d60008263ffffffff61033516565b6040516001600160a01b038216907f427400d279c506df610224b22ecce89b693fc1865864113f21c8d19c1f0c2a3b90600090a250565b6102be82826101b4565b15610310576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b61033f82826101b4565b61037a5760405162461bcd60e51b815260040180806020018281038252602181526020018061039d6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373436170706572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652043617070657220726f6c65a265627a7a723058204e5b56541bf6d73cfcf7caa74aa44814a613be2a807eaf4c6b3c6f8c44f864ee64736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_ISCAPPER = "isCapper";

    public static final String FUNC_REMOVECAPPER = "removeCapper";

    public static final String FUNC_RENOUNCECAPPER = "renounceCapper";

    public static final String FUNC_ADDCAPPER = "addCapper";

    public static final String FUNC_ONLYCAPPERMOCK = "onlyCapperMock";

    public static final Event CAPPERADDED_EVENT = new Event("CapperAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event CAPPERREMOVED_EVENT = new Event("CapperRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected CapperRoleMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected CapperRoleMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> isCapper(String account) {
        final Function function = new Function(FUNC_ISCAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeCapper(String account) {
        final Function function = new Function(
                FUNC_REMOVECAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public void onlyCapperMock() {
        throw new RuntimeException("cannot call constant function with void return type");
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

    public static CapperRoleMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new CapperRoleMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static CapperRoleMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CapperRoleMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CapperRoleMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CapperRoleMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<CapperRoleMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CapperRoleMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
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
