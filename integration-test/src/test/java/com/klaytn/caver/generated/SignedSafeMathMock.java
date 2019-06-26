package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class SignedSafeMathMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610415806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80634350913814610051578063a5f3c23b14610086578063adefc37b146100a9578063bbe93d91146100cc575b600080fd5b6100746004803603604081101561006757600080fd5b50803590602001356100ef565b60408051918252519081900360200190f35b6100746004803603604081101561009c57600080fd5b5080359060200135610104565b610074600480360360408110156100bf57600080fd5b5080359060200135610110565b610074600480360360408110156100e257600080fd5b508035906020013561011c565b60006100fb8383610128565b90505b92915050565b60006100fb83836101e0565b60006100fb8383610245565b60006100fb83836102aa565b60008161017c576040805162461bcd60e51b815260206004820181905260248201527f5369676e6564536166654d6174683a206469766973696f6e206279207a65726f604482015290519081900360640190fd5b816000191480156101905750600160ff1b83145b156101cc5760405162461bcd60e51b81526004018080602001828103825260218152602001806103756021913960400191505060405180910390fd5b60008284816101d757fe5b05949350505050565b60008282018183128015906101f55750838112155b8061020a575060008312801561020a57508381125b6100fb5760405162461bcd60e51b81526004018080602001828103825260218152602001806103546021913960400191505060405180910390fd5b600081830381831280159061025a5750838113155b8061026f575060008312801561026f57508381135b6100fb5760405162461bcd60e51b81526004018080602001828103825260248152602001806103bd6024913960400191505060405180910390fd5b6000826102b9575060006100fe565b826000191480156102cd5750600160ff1b82145b156103095760405162461bcd60e51b81526004018080602001828103825260278152602001806103966027913960400191505060405180910390fd5b8282028284828161031657fe5b05146100fb5760405162461bcd60e51b81526004018080602001828103825260278152602001806103966027913960400191505060405180910390fdfe5369676e6564536166654d6174683a206164646974696f6e206f766572666c6f775369676e6564536166654d6174683a206469766973696f6e206f766572666c6f775369676e6564536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f775369676e6564536166654d6174683a207375627472616374696f6e206f766572666c6f77a265627a7a723058203fee1d59484424cd4c52d55845bdc1029fbf97266062b278054c0614c8c4889a64736f6c63430005090032";

    public static final String FUNC_DIV = "div";

    public static final String FUNC_ADD = "add";

    public static final String FUNC_SUB = "sub";

    public static final String FUNC_MUL = "mul";

    protected SignedSafeMathMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SignedSafeMathMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> div(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_DIV, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(a), 
                new org.web3j.abi.datatypes.generated.Int256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> add(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ADD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(a), 
                new org.web3j.abi.datatypes.generated.Int256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> sub(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_SUB, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(a), 
                new org.web3j.abi.datatypes.generated.Int256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> mul(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_MUL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(a), 
                new org.web3j.abi.datatypes.generated.Int256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static SignedSafeMathMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SignedSafeMathMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SignedSafeMathMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignedSafeMathMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignedSafeMathMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignedSafeMathMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SignedSafeMathMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignedSafeMathMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
