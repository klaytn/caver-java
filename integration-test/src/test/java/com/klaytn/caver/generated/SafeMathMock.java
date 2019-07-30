package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
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
public class SafeMathMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610397806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063771602f71461005c578063a391c15b14610091578063b67d77c5146100b4578063c8a4ac9c146100d7578063f43f523a146100fa575b600080fd5b61007f6004803603604081101561007257600080fd5b508035906020013561011d565b60408051918252519081900360200190f35b61007f600480360360408110156100a757600080fd5b5080359060200135610132565b61007f600480360360408110156100ca57600080fd5b508035906020013561013e565b61007f600480360360408110156100ed57600080fd5b508035906020013561014a565b61007f6004803603604081101561011057600080fd5b5080359060200135610156565b60006101298383610162565b90505b92915050565b600061012983836101bc565b60006101298383610226565b60006101298383610283565b600061012983836102dc565b600082820183811015610129576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b6000808211610212576040805162461bcd60e51b815260206004820152601a60248201527f536166654d6174683a206469766973696f6e206279207a65726f000000000000604482015290519081900360640190fd5b600082848161021d57fe5b04949350505050565b60008282111561027d576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6000826102925750600061012c565b8282028284828161029f57fe5b04146101295760405162461bcd60e51b81526004018080602001828103825260218152602001806103426021913960400191505060405180910390fd5b600081610330576040805162461bcd60e51b815260206004820152601860248201527f536166654d6174683a206d6f64756c6f206279207a65726f0000000000000000604482015290519081900360640190fd5b81838161033957fe5b06939250505056fe536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f77a265627a7a723058207b0ba9ee7f9327b3360e79798cea1f34c9b34bafbf72130d3a7fcb755826f5e764736f6c63430005090032";

    public static final String FUNC_ADD = "add";

    public static final String FUNC_DIV = "div";

    public static final String FUNC_SUB = "sub";

    public static final String FUNC_MUL = "mul";

    public static final String FUNC_MOD = "mod";

    protected SafeMathMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SafeMathMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> add(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ADD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> div(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_DIV, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> sub(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_SUB, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> mul(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_MUL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> mod(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_MOD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static SafeMathMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SafeMathMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SafeMathMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeMathMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeMathMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeMathMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SafeMathMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeMathMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
