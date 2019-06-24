package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import java.util.List;
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
public class ERC165CheckerMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061032e806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80634b9dd90414610046578063c398a9251461010d578063d905700714610133575b600080fd5b6100f96004803603604081101561005c57600080fd5b6001600160a01b03823516919081019060408101602082013564010000000081111561008757600080fd5b82018360208201111561009957600080fd5b803590602001918460208302840111640100000000831117156100bb57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610169945050505050565b604080519115158252519081900360200190f35b6100f96004803603602081101561012357600080fd5b50356001600160a01b031661018d565b6100f96004803603604081101561014957600080fd5b5080356001600160a01b031690602001356001600160e01b0319166101a1565b60006101846001600160a01b0384168363ffffffff6101bc16565b90505b92915050565b6000610187826001600160a01b031661021c565b60006101846001600160a01b0384168363ffffffff61024f16565b60006101c78361021c565b6101d357506000610187565b60005b8251811015610212576101fc848483815181106101ef57fe5b6020026020010151610267565b61020a576000915050610187565b6001016101d6565b5060019392505050565b600061022f826301ffc9a760e01b610267565b80156101875750610248826001600160e01b0319610267565b1592915050565b600061025a8361021c565b8015610184575061018483835b6000806000610276858561028d565b915091508180156102845750805b95945050505050565b604080516001600160e01b031983166024808301919091528251808303909101815260449091018252602081810180516001600160e01b03166301ffc9a760e01b178152825193516000808252948594939091908183858b617530fa905190989097509550505050505056fea265627a7a7230582013c6eac50e02b78429109b83097d1cf194119eba974235cebaec1d9805a6669064736f6c63430005090032";

    public static final String FUNC_SUPPORTSALLINTERFACES = "supportsAllInterfaces";

    public static final String FUNC_SUPPORTSERC165 = "supportsERC165";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    protected ERC165CheckerMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC165CheckerMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsAllInterfaces(String account, List<byte[]> interfaceIds) {
        final Function function = new Function(FUNC_SUPPORTSALLINTERFACES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.datatypes.generated.Bytes4.class,
                        org.web3j.abi.Utils.typeMap(interfaceIds, org.web3j.abi.datatypes.generated.Bytes4.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> supportsERC165(String account) {
        final Function function = new Function(FUNC_SUPPORTSERC165, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> supportsInterface(String account, byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static ERC165CheckerMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC165CheckerMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC165CheckerMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC165CheckerMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC165CheckerMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC165CheckerMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC165CheckerMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC165CheckerMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
