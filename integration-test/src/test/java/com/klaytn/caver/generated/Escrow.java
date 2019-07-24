package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class Escrow extends SmartContract {
    private static final String BINARY = "60806040819052600080546001600160a01b0319163317908190556001600160a01b031681527f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d990602090a16104f28061005a6000396000f3fe60806040526004361061004a5760003560e01c80632348238c1461004f57806351cff8d914610084578063c6dbdf61146100b7578063e3a9db1a146100e8578063f340fa011461012d575b600080fd5b34801561005b57600080fd5b506100826004803603602081101561007257600080fd5b50356001600160a01b0316610153565b005b34801561009057600080fd5b50610082600480360360208110156100a757600080fd5b50356001600160a01b031661023b565b3480156100c357600080fd5b506100cc610312565b604080516001600160a01b039092168252519081900360200190f35b3480156100f457600080fd5b5061011b6004803603602081101561010b57600080fd5b50356001600160a01b0316610321565b60408051918252519081900360200190f35b6100826004803603602081101561014357600080fd5b50356001600160a01b031661033c565b6000546001600160a01b0316331461019c5760405162461bcd60e51b815260040180806020018281038252602c815260200180610492602c913960400191505060405180910390fd5b6001600160a01b0381166101e15760405162461bcd60e51b815260040180806020018281038252602a815260200180610468602a913960400191505060405180910390fd5b600080546001600160a01b0319166001600160a01b03838116919091179182905560408051929091168252517f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d9916020908290030190a150565b6000546001600160a01b031633146102845760405162461bcd60e51b815260040180806020018281038252602c815260200180610492602c913960400191505060405180910390fd5b6001600160a01b038116600081815260016020526040808220805490839055905190929183156108fc02918491818181858888f193505050501580156102ce573d6000803e3d6000fd5b506040805182815290516001600160a01b038416917f7084f5476618d8e60b11ef0d7d3f06914655adb8793e28ff7f018d4c76d505d5919081900360200190a25050565b6000546001600160a01b031690565b6001600160a01b031660009081526001602052604090205490565b6000546001600160a01b031633146103855760405162461bcd60e51b815260040180806020018281038252602c815260200180610492602c913960400191505060405180910390fd5b6001600160a01b03811660009081526001602052604090205434906103b0908263ffffffff61040616565b6001600160a01b038316600081815260016020908152604091829020939093558051848152905191927f2da466a7b24304f47e87fa2e1e5a81b9831ce54fec19055ce277ca2f39ba42c492918290030190a25050565b600082820183811015610460576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b939250505056fe5365636f6e646172793a206e6577207072696d61727920697320746865207a65726f20616464726573735365636f6e646172793a2063616c6c6572206973206e6f7420746865207072696d617279206163636f756e74a265627a7a72305820f08e33e959fa9ed7ca9f81055912e31f22cdb77348370f8eb506e48e382a3e9d64736f6c63430005090032";

    public static final String FUNC_TRANSFERPRIMARY = "transferPrimary";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_PRIMARY = "primary";

    public static final String FUNC_DEPOSITSOF = "depositsOf";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final Event DEPOSITED_EVENT = new Event("Deposited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event WITHDRAWN_EVENT = new Event("Withdrawn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PRIMARYTRANSFERRED_EVENT = new Event("PrimaryTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected Escrow(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected Escrow(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferPrimary(String recipient) {
        final Function function = new Function(
                FUNC_TRANSFERPRIMARY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> withdraw(String payee) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(payee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> primary() {
        final Function function = new Function(FUNC_PRIMARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> depositsOf(String payee) {
        final Function function = new Function(FUNC_DEPOSITSOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(payee)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> deposit(String payee, BigInteger pebValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(payee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, pebValue);
    }

    public List<DepositedEventResponse> getDepositedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEPOSITED_EVENT, transactionReceipt);
        ArrayList<DepositedEventResponse> responses = new ArrayList<DepositedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            DepositedEventResponse typedResponse = new DepositedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.payee = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.weiAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WithdrawnEventResponse> getWithdrawnEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WITHDRAWN_EVENT, transactionReceipt);
        ArrayList<WithdrawnEventResponse> responses = new ArrayList<WithdrawnEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WithdrawnEventResponse typedResponse = new WithdrawnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.payee = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.weiAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PrimaryTransferredEventResponse> getPrimaryTransferredEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PRIMARYTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<PrimaryTransferredEventResponse> responses = new ArrayList<PrimaryTransferredEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PrimaryTransferredEventResponse typedResponse = new PrimaryTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static Escrow load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new Escrow(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static Escrow load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Escrow(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Escrow> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Escrow.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Escrow> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Escrow.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class DepositedEventResponse {
        public KlayLogs.Log log;

        public String payee;

        public BigInteger weiAmount;
    }

    public static class WithdrawnEventResponse {
        public KlayLogs.Log log;

        public String payee;

        public BigInteger weiAmount;
    }

    public static class PrimaryTransferredEventResponse {
        public KlayLogs.Log log;

        public String recipient;
    }
}
