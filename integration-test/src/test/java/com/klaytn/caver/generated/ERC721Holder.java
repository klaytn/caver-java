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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC721Holder extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610158806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c8063150b7a0214610030575b600080fd5b6100f66004803603608081101561004657600080fd5b6001600160a01b0382358116926020810135909116916040820135919081019060808101606082013564010000000081111561008157600080fd5b82018360208201111561009357600080fd5b803590602001918460018302840111640100000000831117156100b557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610113945050505050565b604080516001600160e01b03199092168252519081900360200190f35b630a85bd0160e11b94935050505056fea265627a7a723058203e2e8af3667743b34d98bf35bf54dcb1eba020554749efba0a06b9950504fa7064736f6c63430005090032";

    public static final String FUNC_ONERC721RECEIVED = "onERC721Received";

    protected ERC721Holder(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721Holder(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> onERC721Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONERC721RECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0), 
                new org.web3j.abi.datatypes.Address(param1), 
                new org.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static ERC721Holder load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721Holder(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721Holder load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Holder(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721Holder> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721Holder.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC721Holder> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721Holder.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
