package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC165InterfacesSupported extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516102d93803806102d98339818101604052602081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8201602081018481111561005e57600080fd5b815185602082028301116401000000008211171561007b57600080fd5b509093506100b692507f01ffc9a7000000000000000000000000000000000000000000000000000000009150506001600160e01b036100f316565b60005b81518110156100ec576100e48282815181106100d157fe5b60200260200101516100f360201b60201c565b6001016100b9565b50506101ab565b7fffffffff00000000000000000000000000000000000000000000000000000000808216141561016e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f8152602001806102aa602f913960400191505060405180910390fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b60f1806101b96000396000f3fe6080604052348015600f57600080fd5b506004361060325760003560e01c806301ffc9a714603757806334d7006c14606f575b600080fd5b605b60048036036020811015604b57600080fd5b50356001600160e01b0319166092565b604080519115158252519081900360200190f35b607560b1565b604080516001600160e01b03199092168252519081900360200190f35b6001600160e01b03191660009081526020819052604090205460ff1690565b6301ffc9a760e01b8156fea265627a7a72305820b0823fc699965177927c04bfb4c377c888aaf3894c3862d8cc6e22722870d3c764736f6c63430005090032455243313635496e7465726661636573537570706f727465643a20696e76616c696420696e74657266616365206964";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_INTERFACE_ID_ERC165 = "INTERFACE_ID_ERC165";

    protected ERC165InterfacesSupported(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC165InterfacesSupported(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<byte[]> INTERFACE_ID_ERC165() {
        final Function function = new Function(FUNC_INTERFACE_ID_ERC165, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static ERC165InterfacesSupported load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC165InterfacesSupported(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC165InterfacesSupported load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC165InterfacesSupported(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC165InterfacesSupported> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, List<byte[]> interfaceIds) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.datatypes.generated.Bytes4.class,
                        org.web3j.abi.Utils.typeMap(interfaceIds, org.web3j.abi.datatypes.generated.Bytes4.class))));
        return deployRemoteCall(ERC165InterfacesSupported.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC165InterfacesSupported> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, List<byte[]> interfaceIds) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.datatypes.generated.Bytes4.class,
                        org.web3j.abi.Utils.typeMap(interfaceIds, org.web3j.abi.datatypes.generated.Bytes4.class))));
        return deployRemoteCall(ERC165InterfacesSupported.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }
}
