package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
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
public class IERC1820Implementer extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_CANIMPLEMENTINTERFACEFORADDRESS = "canImplementInterfaceForAddress";

    protected IERC1820Implementer(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected IERC1820Implementer(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<byte[]> canImplementInterfaceForAddress(byte[] interfaceHash, String account) {
        final Function function = new Function(FUNC_CANIMPLEMENTINTERFACEFORADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash), 
                new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static IERC1820Implementer load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new IERC1820Implementer(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static IERC1820Implementer load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IERC1820Implementer(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IERC1820Implementer> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1820Implementer.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<IERC1820Implementer> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC1820Implementer.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
