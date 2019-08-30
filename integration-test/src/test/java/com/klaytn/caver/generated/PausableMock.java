package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class PausableMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610023336001600160e01b0361003816565b6001805461ffff1916905560006002556101ac565b61005081600061008760201b6105ce1790919060201c565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61009a82826001600160e01b0361012b16565b1561010657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661018c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806109196022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61075e806101bb6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c80636ef8d66d116100715780636ef8d66d1461014d57806376657b8e1461015557806382dc1ec41461015d5780638456cb59146101835780639958f0451461018b578063e7651d7a14610193576100b4565b806306661abd146100b9578063329daf90146100d35780633f4ba83a146100dd57806346fbf68e146100e55780635c975abb1461011f5780636b2c0f5514610127575b600080fd5b6100c161019b565b60408051918252519081900360200190f35b6100db6101a1565b005b6100db6101e7565b61010b600480360360208110156100fb57600080fd5b50356001600160a01b03166102b8565b604080519115158252519081900360200190f35b61010b6102d0565b6100db6004803603602081101561013d57600080fd5b50356001600160a01b03166102d9565b6100db6102e5565b61010b6102ee565b6100db6004803603602081101561017357600080fd5b50356001600160a01b03166102fc565b6100db610349565b6100db610419565b6100db610478565b60025481565b6101aa336102b8565b6101e55760405162461bcd60e51b81526004018080602001828103825260308152602001806106b76030913960400191505060405180910390fd5b565b6101f0336102b8565b61022b5760405162461bcd60e51b81526004018080602001828103825260308152602001806106b76030913960400191505060405180910390fd5b60015460ff16610279576040805162461bcd60e51b815260206004820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b604482015290519081900360640190fd5b6001805460ff191690556040805133815290517f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa9181900360200190a1565b60006102ca818363ffffffff6104ce16565b92915050565b60015460ff1690565b6102e281610535565b50565b6101e533610535565b600154610100900460ff1681565b610305336102b8565b6103405760405162461bcd60e51b81526004018080602001828103825260308152602001806106b76030913960400191505060405180910390fd5b6102e28161053e565b610352336102b8565b61038d5760405162461bcd60e51b81526004018080602001828103825260308152602001806106b76030913960400191505060405180910390fd5b60015460ff16156103d8576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6001805460ff1916811790556040805133815290517f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a2589181900360200190a1565b60015460ff16610467576040805162461bcd60e51b815260206004820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b604482015290519081900360640190fd5b6001805461ff001916610100179055565b60015460ff16156104c3576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b600280546001019055565b60006001600160a01b0382166105155760405162461bcd60e51b81526004018080602001828103825260228152602001806107086022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6102e281610586565b61054f60008263ffffffff6105ce16565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61059760008263ffffffff61064f16565b6040516001600160a01b038216907fcd265ebaf09df2871cc7bd4133404a235ba12eff2041bb89d9c714a2621c7c7e90600090a250565b6105d882826104ce565b1561062a576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b61065982826104ce565b6106945760405162461bcd60e51b81526004018080602001828103825260218152602001806106e76021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe506175736572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652050617573657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373a265627a7a723058203adf9db6282e74e657726f9821f5fa30d08e3b811f7f2b817fe76bc46d520d2e64736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_COUNT = "count";

    public static final String FUNC_ONLYPAUSERMOCK = "onlyPauserMock";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_REMOVEPAUSER = "removePauser";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_DRASTICMEASURETAKEN = "drasticMeasureTaken";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_DRASTICMEASURE = "drasticMeasure";

    public static final String FUNC_NORMALPROCESS = "normalProcess";

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event PAUSERADDED_EVENT = new Event("PauserAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSERREMOVED_EVENT = new Event("PauserRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected PausableMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PausableMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> count() {
        final Function function = new Function(FUNC_COUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public void onlyPauserMock() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isPauser(String account) {
        final Function function = new Function(FUNC_ISPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
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

    public RemoteCall<Boolean> drasticMeasureTaken() {
        final Function function = new Function(FUNC_DRASTICMEASURETAKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addPauser(String account) {
        final Function function = new Function(
                FUNC_ADDPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> drasticMeasure() {
        final Function function = new Function(
                FUNC_DRASTICMEASURE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> normalProcess() {
        final Function function = new Function(
                FUNC_NORMALPROCESS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<PausedEventResponse> getPausedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<UnpausedEventResponse> getUnpausedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
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

    public static PausableMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PausableMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PausableMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PausableMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PausableMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PausableMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<PausableMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PausableMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class PausedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class UnpausedEventResponse {
        public KlayLogs.Log log;

        public String account;
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
