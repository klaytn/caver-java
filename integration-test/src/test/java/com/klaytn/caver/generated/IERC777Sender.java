package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
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
public class IERC777Sender extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_TOKENSTOSEND = "tokensToSend";

    protected IERC777Sender(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected IERC777Sender(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> tokensToSend(String operator, String from, String to, BigInteger amount, byte[] userData, byte[] operatorData) {
        final Function function = new Function(
                FUNC_TOKENSTOSEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator), 
                new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(userData), 
                new org.web3j.abi.datatypes.DynamicBytes(operatorData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static IERC777Sender load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new IERC777Sender(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static IERC777Sender load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IERC777Sender(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IERC777Sender> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC777Sender.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<IERC777Sender> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IERC777Sender.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
