package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC1820ImplementerMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061018d806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063249cb3fa1461003b5780635536e45d14610079575b600080fd5b6100676004803603604081101561005157600080fd5b50803590602001356001600160a01b03166100a7565b60408051918252519081900360200190f35b6100a56004803603604081101561008f57600080fd5b50803590602001356001600160a01b031661011c565b005b6000828152602081815260408083206001600160a01b038516845290915281205460ff166100d6576000610115565b604051602001808073455243313832305f4143434550545f4d4147494360601b8152506014019050604051602081830303815290604052805190602001205b9392505050565b610126828261012a565b5050565b6000918252602082815260408084206001600160a01b0390931684529190529020805460ff1916600117905556fea265627a7a72305820f0dae1f2e4a4a391083be02d7103010daace583d64d3f117cc584db7fdea1ce764736f6c63430005090032";

    public static final String FUNC_CANIMPLEMENTINTERFACEFORADDRESS = "canImplementInterfaceForAddress";

    public static final String FUNC_REGISTERINTERFACEFORADDRESS = "registerInterfaceForAddress";

    protected ERC1820ImplementerMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC1820ImplementerMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<byte[]> canImplementInterfaceForAddress(byte[] interfaceHash, String account) {
        final Function function = new Function(FUNC_CANIMPLEMENTINTERFACEFORADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash), 
                new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> registerInterfaceForAddress(byte[] interfaceHash, String account) {
        final Function function = new Function(
                FUNC_REGISTERINTERFACEFORADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash), 
                new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ERC1820ImplementerMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC1820ImplementerMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC1820ImplementerMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC1820ImplementerMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC1820ImplementerMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC1820ImplementerMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC1820ImplementerMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC1820ImplementerMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
