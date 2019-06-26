package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
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
public class AddressImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060ae8061001f6000396000f3fe6080604052348015600f57600080fd5b506004361060285760003560e01c80631627905514602d575b600080fd5b605060048036036020811015604157600080fd5b50356001600160a01b03166064565b604080519115158252519081900360200190f35b6000606d826073565b92915050565b3b15159056fea265627a7a72305820d1678e986a7e1bd0063f927fc972660bdbe9ee381c557dfa5f8caf91ffbf585964736f6c63430005090032";

    public static final String FUNC_ISCONTRACT = "isContract";

    protected AddressImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected AddressImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> isContract(String account) {
        final Function function = new Function(FUNC_ISCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static AddressImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new AddressImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static AddressImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AddressImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AddressImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AddressImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<AddressImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AddressImpl.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
