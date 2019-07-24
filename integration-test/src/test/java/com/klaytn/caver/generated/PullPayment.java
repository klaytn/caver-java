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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class PullPayment extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_WITHDRAWPAYMENTS = "withdrawPayments";

    public static final String FUNC_PAYMENTS = "payments";

    protected PullPayment(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PullPayment(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> withdrawPayments(String payee) {
        final Function function = new Function(
                FUNC_WITHDRAWPAYMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(payee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> payments(String dest) {
        final Function function = new Function(FUNC_PAYMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(dest)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static PullPayment load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PullPayment(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PullPayment load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PullPayment(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PullPayment> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PullPayment.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<PullPayment> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PullPayment.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
