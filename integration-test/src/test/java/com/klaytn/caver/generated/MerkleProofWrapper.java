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
public class MerkleProofWrapper extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506101e0806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c80635a9a49c714610030575b600080fd5b6100d86004803603606081101561004657600080fd5b81019060208101813564010000000081111561006157600080fd5b82018360208201111561007357600080fd5b8035906020019184602083028401116401000000008311171561009557600080fd5b91908080602002602001604051908101604052809392919081815260200183836020028082843760009201919091525092955050823593505050602001356100ec565b604080519115158252519081900360200190f35b60006100f9848484610101565b949350505050565b600081815b85518110156101a057600086828151811061011d57fe5b60200260200101519050808310156101655782816040516020018083815260200182815260200192505050604051602081830303815290604052805190602001209250610197565b808360405160200180838152602001828152602001925050506040516020818303038152906040528051906020012092505b50600101610106565b50909214939250505056fea265627a7a72305820561f7c48f52636e5fe480eca554eee3a7cb77306142eb395a4bf605cb3e4b09864736f6c63430005090032";

    public static final String FUNC_VERIFY = "verify";

    protected MerkleProofWrapper(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected MerkleProofWrapper(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> verify(List<byte[]> proof, byte[] root, byte[] leaf) {
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(proof, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                new org.web3j.abi.datatypes.generated.Bytes32(root), 
                new org.web3j.abi.datatypes.generated.Bytes32(leaf)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static MerkleProofWrapper load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new MerkleProofWrapper(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static MerkleProofWrapper load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MerkleProofWrapper(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MerkleProofWrapper> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MerkleProofWrapper.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<MerkleProofWrapper> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MerkleProofWrapper.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
