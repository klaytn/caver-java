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
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class RefundEscrow extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610b63380380610b638339818101604052602081101561003357600080fd5b5051600080546001600160a01b031916331790819055604080516001600160a01b03929092168252517f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d99181900360200190a16001600160a01b0381166100e5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602d815260200180610b36602d913960400191505060405180910390fd5b6002805460ff196001600160a01b039390931661010002610100600160a81b031990911617919091169055610a178061011f6000396000f3fe60806040526004361061009c5760003560e01c80638c52dc41116100645780638c52dc41146101965780639af6549a146101ab578063c19d93fb146101c0578063c6dbdf61146101f9578063e3a9db1a1461020e578063f340fa01146102535761009c565b80632348238c146100a157806338af3eed146100d657806343d726d61461010757806351cff8d91461011c578063685ca1941461014f575b600080fd5b3480156100ad57600080fd5b506100d4600480360360208110156100c457600080fd5b50356001600160a01b0316610279565b005b3480156100e257600080fd5b506100eb610361565b604080516001600160a01b039092168252519081900360200190f35b34801561011357600080fd5b506100d4610375565b34801561012857600080fd5b506100d46004803603602081101561013f57600080fd5b50356001600160a01b0316610444565b34801561015b57600080fd5b506101826004803603602081101561017257600080fd5b50356001600160a01b0316610494565b604080519115158252519081900360200190f35b3480156101a257600080fd5b506100d46104b0565b3480156101b757600080fd5b506100d4610580565b3480156101cc57600080fd5b506101d561060e565b604051808260028111156101e557fe5b60ff16815260200191505060405180910390f35b34801561020557600080fd5b506100eb610617565b34801561021a57600080fd5b506102416004803603602081101561023157600080fd5b50356001600160a01b0316610626565b60408051918252519081900360200190f35b6100d46004803603602081101561026957600080fd5b50356001600160a01b0316610641565b6000546001600160a01b031633146102c25760405162461bcd60e51b815260040180806020018281038252602c815260200180610985602c913960400191505060405180910390fd5b6001600160a01b0381166103075760405162461bcd60e51b815260040180806020018281038252602a81526020018061095b602a913960400191505060405180910390fd5b600080546001600160a01b0319166001600160a01b03838116919091179182905560408051929091168252517f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d9916020908290030190a150565b60025461010090046001600160a01b031690565b6000546001600160a01b031633146103be5760405162461bcd60e51b815260040180806020018281038252602c815260200180610985602c913960400191505060405180910390fd5b60006002805460ff16908111156103d157fe5b1461040d5760405162461bcd60e51b81526004018080602001828103825260298152602001806109326029913960400191505060405180910390fd5b6002805460ff1916811790556040517f088672c3a6e342f7cd94a65ba63b79df24a8973927b4d05d803c44bbf787d12f90600090a1565b61044d81610494565b6104885760405162461bcd60e51b81526004018080602001828103825260338152602001806108ff6033913960400191505060405180910390fd5b61049181610699565b50565b600060016002805460ff16908111156104a957fe5b1492915050565b6000546001600160a01b031633146104f95760405162461bcd60e51b815260040180806020018281038252602c815260200180610985602c913960400191505060405180910390fd5b60006002805460ff169081111561050c57fe5b146105485760405162461bcd60e51b81526004018080602001828103825260328152602001806109b16032913960400191505060405180910390fd5b6002805460ff191660011790556040517f599d8e5a83cffb867d051598c4d70e805d59802d8081c1c7d6dffc5b6aca2b8990600090a1565b6002805460ff168181111561059157fe5b146105cd5760405162461bcd60e51b815260040180806020018281038252603881526020018061089c6038913960400191505060405180910390fd5b6002546040516001600160a01b036101009092049190911690303180156108fc02916000818181858888f19350505050158015610491573d6000803e3d6000fd5b60025460ff1690565b6000546001600160a01b031690565b6001600160a01b031660009081526001602052604090205490565b60006002805460ff169081111561065457fe5b146106905760405162461bcd60e51b815260040180806020018281038252602b8152602001806108d4602b913960400191505060405180910390fd5b61049181610770565b6000546001600160a01b031633146106e25760405162461bcd60e51b815260040180806020018281038252602c815260200180610985602c913960400191505060405180910390fd5b6001600160a01b038116600081815260016020526040808220805490839055905190929183156108fc02918491818181858888f1935050505015801561072c573d6000803e3d6000fd5b506040805182815290516001600160a01b038416917f7084f5476618d8e60b11ef0d7d3f06914655adb8793e28ff7f018d4c76d505d5919081900360200190a25050565b6000546001600160a01b031633146107b95760405162461bcd60e51b815260040180806020018281038252602c815260200180610985602c913960400191505060405180910390fd5b6001600160a01b03811660009081526001602052604090205434906107e4908263ffffffff61083a16565b6001600160a01b038316600081815260016020908152604091829020939093558051848152905191927f2da466a7b24304f47e87fa2e1e5a81b9831ce54fec19055ce277ca2f39ba42c492918290030190a25050565b600082820183811015610894576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b939250505056fe526566756e64457363726f773a2062656e65666963696172792063616e206f6e6c79207769746864726177207768696c6520636c6f736564526566756e64457363726f773a2063616e206f6e6c79206465706f736974207768696c6520616374697665436f6e646974696f6e616c457363726f773a207061796565206973206e6f7420616c6c6f77656420746f207769746864726177526566756e64457363726f773a2063616e206f6e6c7920636c6f7365207768696c65206163746976655365636f6e646172793a206e6577207072696d61727920697320746865207a65726f20616464726573735365636f6e646172793a2063616c6c6572206973206e6f7420746865207072696d617279206163636f756e74526566756e64457363726f773a2063616e206f6e6c7920656e61626c6520726566756e6473207768696c6520616374697665a265627a7a72305820075756c59a04ebd4f669f46882f54f1ab69f83047abc9695a6b9ef6c784ba16764736f6c63430005090032526566756e64457363726f773a2062656e656669636961727920697320746865207a65726f2061646472657373";

    public static final String FUNC_TRANSFERPRIMARY = "transferPrimary";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_CLOSE = "close";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_WITHDRAWALALLOWED = "withdrawalAllowed";

    public static final String FUNC_ENABLEREFUNDS = "enableRefunds";

    public static final String FUNC_BENEFICIARYWITHDRAW = "beneficiaryWithdraw";

    public static final String FUNC_STATE = "state";

    public static final String FUNC_PRIMARY = "primary";

    public static final String FUNC_DEPOSITSOF = "depositsOf";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final Event REFUNDSCLOSED_EVENT = new Event("RefundsClosed", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event REFUNDSENABLED_EVENT = new Event("RefundsEnabled", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event DEPOSITED_EVENT = new Event("Deposited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event WITHDRAWN_EVENT = new Event("Withdrawn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PRIMARYTRANSFERRED_EVENT = new Event("PrimaryTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected RefundEscrow(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected RefundEscrow(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferPrimary(String recipient) {
        final Function function = new Function(
                FUNC_TRANSFERPRIMARY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> close() {
        final Function function = new Function(
                FUNC_CLOSE, 
                Arrays.<Type>asList(), 
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

    public RemoteCall<Boolean> withdrawalAllowed(String param0) {
        final Function function = new Function(FUNC_WITHDRAWALALLOWED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> enableRefunds() {
        final Function function = new Function(
                FUNC_ENABLEREFUNDS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> beneficiaryWithdraw() {
        final Function function = new Function(
                FUNC_BENEFICIARYWITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> state() {
        final Function function = new Function(FUNC_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> deposit(String refundee, BigInteger pebValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(refundee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, pebValue);
    }

    public List<RefundsClosedEventResponse> getRefundsClosedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDSCLOSED_EVENT, transactionReceipt);
        ArrayList<RefundsClosedEventResponse> responses = new ArrayList<RefundsClosedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            RefundsClosedEventResponse typedResponse = new RefundsClosedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<RefundsEnabledEventResponse> getRefundsEnabledEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDSENABLED_EVENT, transactionReceipt);
        ArrayList<RefundsEnabledEventResponse> responses = new ArrayList<RefundsEnabledEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            RefundsEnabledEventResponse typedResponse = new RefundsEnabledEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
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

    public static RefundEscrow load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new RefundEscrow(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static RefundEscrow load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RefundEscrow(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RefundEscrow> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)));
        return deployRemoteCall(RefundEscrow.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<RefundEscrow> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)));
        return deployRemoteCall(RefundEscrow.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class RefundsClosedEventResponse {
        public KlayLogs.Log log;
    }

    public static class RefundsEnabledEventResponse {
        public KlayLogs.Log log;
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
