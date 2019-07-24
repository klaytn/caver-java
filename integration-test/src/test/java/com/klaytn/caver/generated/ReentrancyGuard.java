package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ReentrancyGuard extends SmartContract {
    private static final String BINARY = "";

    protected ReentrancyGuard(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ReentrancyGuard(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static ReentrancyGuard load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ReentrancyGuard(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ReentrancyGuard load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ReentrancyGuard(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ReentrancyGuard> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyGuard.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ReentrancyGuard> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ReentrancyGuard.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
