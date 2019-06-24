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
public class ERC165Mock extends SmartContract {
    private static final String BINARY = "60806040526100367f01ffc9a7000000000000000000000000000000000000000000000000000000006001600160e01b0361003b16565b610109565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156100cc57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b610183806101186000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c806301ffc9a71461003b578063214cdb8014610076575b600080fd5b6100626004803603602081101561005157600080fd5b50356001600160e01b03191661009f565b604080519115158252519081900360200190f35b61009d6004803603602081101561008c57600080fd5b50356001600160e01b0319166100be565b005b6001600160e01b03191660009081526020819052604090205460ff1690565b6100c7816100ca565b50565b6001600160e01b03198082161415610129576040805162461bcd60e51b815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b6001600160e01b0319166000908152602081905260409020805460ff1916600117905556fea265627a7a72305820bafe1fe24693d76016ff048110872128c94fd5e08303f7bf06a30bc36ecf2af164736f6c63430005090032";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_REGISTERINTERFACE = "registerInterface";

    protected ERC165Mock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC165Mock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> registerInterface(byte[] interfaceId) {
        final Function function = new Function(
                FUNC_REGISTERINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ERC165Mock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC165Mock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC165Mock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC165Mock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC165Mock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC165Mock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC165Mock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC165Mock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
