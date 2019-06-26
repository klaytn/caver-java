package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class RolesMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506102d2806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80630a3b0a4f1461004657806321887c3d1461006e57806329092d0e146100a8575b600080fd5b61006c6004803603602081101561005c57600080fd5b50356001600160a01b03166100ce565b005b6100946004803603602081101561008457600080fd5b50356001600160a01b03166100e2565b604080519115158252519081900360200190f35b61006c600480360360208110156100be57600080fd5b50356001600160a01b03166100fa565b6100df60008263ffffffff61010b16565b50565b60006100f4818363ffffffff61018c16565b92915050565b6100df60008263ffffffff6101f316565b610115828261018c565b15610167576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b0382166101d35760405162461bcd60e51b815260040180806020018281038252602281526020018061027c6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6101fd828261018c565b6102385760405162461bcd60e51b815260040180806020018281038252602181526020018061025b6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373a265627a7a72305820b2130c33c88829e32bef7f6c16950f2f5cdb004254b81b24768594230496991a64736f6c63430005090032";

    public static final String FUNC_ADD = "add";

    public static final String FUNC_HAS = "has";

    public static final String FUNC_REMOVE = "remove";

    protected RolesMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected RolesMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> add(String account) {
        final Function function = new Function(
                FUNC_ADD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> has(String account) {
        final Function function = new Function(FUNC_HAS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> remove(String account) {
        final Function function = new Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RolesMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new RolesMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static RolesMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RolesMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RolesMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(RolesMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<RolesMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(RolesMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
