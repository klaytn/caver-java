package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class OwnableInterfaceId extends SmartContract {
    private static final String BINARY = "6080604052348015600f57600080fd5b5060908061001e6000396000f3fe6080604052348015600f57600080fd5b506004361060285760003560e01c80636b9241fc14602d575b600080fd5b60336050565b604080516001600160e01b03199092168252519081900360200190f35b63813ae5ed60e01b9056fea265627a7a72305820e8317dc602c768da1c7aabed3a190d26b1ce1bc64216fdf2a47a9d3d90f8059764736f6c63430005090032";

    public static final String FUNC_GETINTERFACEID = "getInterfaceId";

    protected OwnableInterfaceId(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected OwnableInterfaceId(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<byte[]> getInterfaceId() {
        final Function function = new Function(FUNC_GETINTERFACEID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static OwnableInterfaceId load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new OwnableInterfaceId(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static OwnableInterfaceId load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new OwnableInterfaceId(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<OwnableInterfaceId> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OwnableInterfaceId.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<OwnableInterfaceId> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OwnableInterfaceId.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
