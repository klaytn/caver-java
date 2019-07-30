package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ReentrancyAttack extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506101ab806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c80630a2df1ed14610030575b600080fd5b6100576004803603602081101561004657600080fd5b50356001600160e01b031916610059565b005b60408051600481526024810182526020810180516001600160e01b03166001600160e01b0319851617815291518151600093339392918291908083835b602083106100b55780518252601f199092019160209182019101610096565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610117576040519150601f19603f3d011682016040523d82523d6000602084013e61011c565b606091505b5050905080610172576040805162461bcd60e51b815260206004820152601d60248201527f5265656e7472616e637941747461636b3a206661696c65642063616c6c000000604482015290519081900360640190fd5b505056fea265627a7a72305820f7010f8f51e2f23c4761e24bf2c9dc17b81046f62f63ca9d7f0e992423997e0c64736f6c63430005090032";

    public static final String FUNC_CALLSENDER = "callSender";

    protected ReentrancyAttack(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ReentrancyAttack(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> callSender(byte[] data) {
        final Function function = new Function(
                FUNC_CALLSENDER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ReentrancyAttack load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ReentrancyAttack(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ReentrancyAttack load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ReentrancyAttack(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ReentrancyAttack> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyAttack.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ReentrancyAttack> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyAttack.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
