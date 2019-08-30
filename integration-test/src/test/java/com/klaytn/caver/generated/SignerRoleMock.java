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
public class SignerRoleMock extends SmartContract {
    private static final String BINARY = "6080604052610016336001600160e01b0361001b16565b61018f565b61003381600061006a60201b6102b41790919060201c565b6040516001600160a01b038216907f47d1c22a25bb3a5d4e481b9b1e6944c2eade3181a0a20b495ed61d35b5323f2490600090a250565b61007d82826001600160e01b0361010e16565b156100e957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661016f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806105e26022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6104448061019e6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80630e316ab71461005c5780637df73e2714610084578063b44e2ab9146100be578063e5c8b03d146100c6578063eb12d61e146100ce575b600080fd5b6100826004803603602081101561007257600080fd5b50356001600160a01b03166100f4565b005b6100aa6004803603602081101561009a57600080fd5b50356001600160a01b0316610100565b604080519115158252519081900360200190f35b610082610118565b61008261015e565b610082600480360360208110156100e457600080fd5b50356001600160a01b0316610167565b6100fd816101b4565b50565b6000610112818363ffffffff6101bd16565b92915050565b61012133610100565b61015c5760405162461bcd60e51b81526004018080602001828103825260308152602001806103e06030913960400191505060405180910390fd5b565b61015c336101b4565b61017033610100565b6101ab5760405162461bcd60e51b81526004018080602001828103825260308152602001806103e06030913960400191505060405180910390fd5b6100fd81610224565b6100fd8161026c565b60006001600160a01b0382166102045760405162461bcd60e51b81526004018080602001828103825260228152602001806103be6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61023560008263ffffffff6102b416565b6040516001600160a01b038216907f47d1c22a25bb3a5d4e481b9b1e6944c2eade3181a0a20b495ed61d35b5323f2490600090a250565b61027d60008263ffffffff61033516565b6040516001600160a01b038216907f3525e22824a8a7df2c9a6029941c824cf95b6447f1e13d5128fd3826d35afe8b90600090a250565b6102be82826101bd565b15610310576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b61033f82826101bd565b61037a5760405162461bcd60e51b815260040180806020018281038252602181526020018061039d6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f20616464726573735369676e6572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865205369676e657220726f6c65a265627a7a723058208eeb7c54f52ba9a7e89f20e447e2715c48f79861770683f84fb78125b51e491564736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_REMOVESIGNER = "removeSigner";

    public static final String FUNC_ISSIGNER = "isSigner";

    public static final String FUNC_ONLYSIGNERMOCK = "onlySignerMock";

    public static final String FUNC_RENOUNCESIGNER = "renounceSigner";

    public static final String FUNC_ADDSIGNER = "addSigner";

    public static final Event SIGNERADDED_EVENT = new Event("SignerAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event SIGNERREMOVED_EVENT = new Event("SignerRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected SignerRoleMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SignerRoleMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeSigner(String account) {
        final Function function = new Function(
                FUNC_REMOVESIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isSigner(String account) {
        final Function function = new Function(FUNC_ISSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public void onlySignerMock() {
        throw new RuntimeException("cannot call constant function with void return type");
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

    public static SignerRoleMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SignerRoleMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SignerRoleMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignerRoleMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignerRoleMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignerRoleMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SignerRoleMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignerRoleMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
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
