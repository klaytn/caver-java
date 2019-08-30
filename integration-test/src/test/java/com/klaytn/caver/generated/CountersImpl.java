package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class CountersImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061014f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80632baeceb7146100465780639fa6a6e314610050578063d09de08a1461006a575b600080fd5b61004e610072565b005b61005861007e565b60408051918252519081900360200190f35b61004e61008f565b61007c6000610099565b565b600061008a60006100b0565b905090565b61007c60006100b4565b80546100ac90600163ffffffff6100bd16565b9055565b5490565b80546001019055565b600082821115610114576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b5090039056fea265627a7a72305820346550e3216b9dcd5cd7cf9d5603dd86025ea39d308c30fff21f5c912da21b0964736f6c63430005090032";

    public static final String FUNC_DECREMENT = "decrement";

    public static final String FUNC_CURRENT = "current";

    public static final String FUNC_INCREMENT = "increment";

    protected CountersImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected CountersImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> decrement() {
        final Function function = new Function(
                FUNC_DECREMENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> current() {
        final Function function = new Function(FUNC_CURRENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> increment() {
        final Function function = new Function(
                FUNC_INCREMENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static CountersImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new CountersImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static CountersImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CountersImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CountersImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CountersImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<CountersImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CountersImpl.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
