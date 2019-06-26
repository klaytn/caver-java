package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
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
public class ReentrancyMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50600160008181559055610444806100296000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063083b27321461005c57806361bc221a146100665780638c5344fa1461008057806396ffa6901461009d578063b672ad8b146100ba575b600080fd5b6100646100e0565b005b61006e61014c565b60408051918252519081900360200190f35b6100646004803603602081101561009657600080fd5b5035610152565b610064600480360360208110156100b357600080fd5b50356102f2565b610064600480360360208110156100d057600080fd5b50356001600160a01b0316610317565b60008054600101908190556100f3610405565b6000548114610149576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b50565b60015481565b600080546001019081905581156102985761016b610405565b60408051600019840160248083019190915282518083039091018152604490910182526020810180516001600160e01b0316634629a27d60e11b17815291518151600093309392918291908083835b602083106101d95780518252601f1990920191602091820191016101ba565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d806000811461023b576040519150601f19603f3d011682016040523d82523d6000602084013e610240565b606091505b5050905080610296576040805162461bcd60e51b815260206004820152601b60248201527f5265656e7472616e63794d6f636b3a206661696c65642063616c6c0000000000604482015290519081900360640190fd5b505b60005481146102ee576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b600080546001019081905581156102985761030b610405565b610298600183036102f2565b600080546001019081905561032a610405565b604080516963616c6c6261636b282960b01b8152815190819003600a018120630a2df1ed60e01b82526001600160e01b03198116600483015291516001600160a01b03851691630a2df1ed91602480830192600092919082900301818387803b15801561039657600080fd5b505af11580156103aa573d6000803e3d6000fd5b505050505060005481146102ee576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b600180548101905556fea265627a7a723058203be411159f1ad81ba52daf5c7a1409b7db178d43927a284b6395cabdea64bd6f64736f6c63430005090032";

    public static final String FUNC_CALLBACK = "callback";

    public static final String FUNC_COUNTER = "counter";

    public static final String FUNC_COUNTTHISRECURSIVE = "countThisRecursive";

    public static final String FUNC_COUNTLOCALRECURSIVE = "countLocalRecursive";

    public static final String FUNC_COUNTANDCALL = "countAndCall";

    protected ReentrancyMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ReentrancyMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> callback() {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> counter() {
        final Function function = new Function(FUNC_COUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> countThisRecursive(BigInteger n) {
        final Function function = new Function(
                FUNC_COUNTTHISRECURSIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(n)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> countLocalRecursive(BigInteger n) {
        final Function function = new Function(
                FUNC_COUNTLOCALRECURSIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(n)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> countAndCall(String attacker) {
        final Function function = new Function(
                FUNC_COUNTANDCALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(attacker)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ReentrancyMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ReentrancyMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ReentrancyMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ReentrancyMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ReentrancyMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ReentrancyMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
