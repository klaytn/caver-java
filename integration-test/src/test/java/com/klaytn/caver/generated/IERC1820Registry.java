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
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class IERC1820Registry extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_SETINTERFACEIMPLEMENTER = "setInterfaceImplementer";

    public static final String FUNC_GETMANAGER = "getManager";

    public static final String FUNC_SETMANAGER = "setManager";

    public static final String FUNC_INTERFACEHASH = "interfaceHash";

    public static final String FUNC_UPDATEERC165CACHE = "updateERC165Cache";

    public static final String FUNC_GETINTERFACEIMPLEMENTER = "getInterfaceImplementer";

    public static final String FUNC_IMPLEMENTSERC165INTERFACENOCACHE = "implementsERC165InterfaceNoCache";

    public static final String FUNC_IMPLEMENTSERC165INTERFACE = "implementsERC165Interface";

    public static final Event INTERFACEIMPLEMENTERSET_EVENT = new Event("InterfaceImplementerSet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event MANAGERCHANGED_EVENT = new Event("ManagerChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected IERC1820Registry(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected IERC1820Registry(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setInterfaceImplementer(String account, byte[] interfaceHash, String implementer) {
        final Function function = new Function(
                FUNC_SETINTERFACEIMPLEMENTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash), 
                new org.web3j.abi.datatypes.Address(implementer)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getManager(String account) {
        final Function function = new Function(FUNC_GETMANAGER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setManager(String account, String newManager) {
        final Function function = new Function(
                FUNC_SETMANAGER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.Address(newManager)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> interfaceHash(String interfaceName) {
        final Function function = new Function(FUNC_INTERFACEHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(interfaceName)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> updateERC165Cache(String account, byte[] interfaceId) {
        final Function function = new Function(
                FUNC_UPDATEERC165CACHE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getInterfaceImplementer(String account, byte[] interfaceHash) {
        final Function function = new Function(FUNC_GETINTERFACEIMPLEMENTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> implementsERC165InterfaceNoCache(String account, byte[] interfaceId) {
        final Function function = new Function(FUNC_IMPLEMENTSERC165INTERFACENOCACHE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> implementsERC165Interface(String account, byte[] interfaceId) {
        final Function function = new Function(FUNC_IMPLEMENTSERC165INTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<InterfaceImplementerSetEventResponse> getInterfaceImplementerSetEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(INTERFACEIMPLEMENTERSET_EVENT, transactionReceipt);
        ArrayList<InterfaceImplementerSetEventResponse> responses = new ArrayList<InterfaceImplementerSetEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            InterfaceImplementerSetEventResponse typedResponse = new InterfaceImplementerSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.interfaceHash = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.implementer = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<ManagerChangedEventResponse> getManagerChangedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(MANAGERCHANGED_EVENT, transactionReceipt);
        ArrayList<ManagerChangedEventResponse> responses = new ArrayList<ManagerChangedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            ManagerChangedEventResponse typedResponse = new ManagerChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newManager = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static IERC1820Registry load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new IERC1820Registry(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static IERC1820Registry load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IERC1820Registry(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IERC1820Registry> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1820Registry.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<IERC1820Registry> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1820Registry.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class InterfaceImplementerSetEventResponse {
        public KlayLogs.Log log;

        public String account;

        public byte[] interfaceHash;

        public String implementer;
    }

    public static class ManagerChangedEventResponse {
        public KlayLogs.Log log;

        public String account;

        public String newManager;
    }
}
