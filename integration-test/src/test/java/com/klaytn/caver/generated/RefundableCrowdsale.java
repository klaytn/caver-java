package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class RefundableCrowdsale extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_HASCLOSED = "hasClosed";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_GOAL = "goal";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISOPEN = "isOpen";

    public static final String FUNC_CLOSINGTIME = "closingTime";

    public static final String FUNC_FINALIZE = "finalize";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_GOALREACHED = "goalReached";

    public static final String FUNC_FINALIZED = "finalized";

    public static final String FUNC_OPENINGTIME = "openingTime";

    public static final String FUNC_CLAIMREFUND = "claimRefund";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event CROWDSALEFINALIZED_EVENT = new Event("CrowdsaleFinalized", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event TIMEDCROWDSALEEXTENDED_EVENT = new Event("TimedCrowdsaleExtended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected RefundableCrowdsale(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected RefundableCrowdsale(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> hasClosed() {
        final Function function = new Function(FUNC_HASCLOSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function(FUNC_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> goal() {
        final Function function = new Function(FUNC_GOAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> weiRaised() {
        final Function function = new Function(FUNC_WEIRAISED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isOpen() {
        final Function function = new Function(FUNC_ISOPEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> closingTime() {
        final Function function = new Function(FUNC_CLOSINGTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> wallet() {
        final Function function = new Function(FUNC_WALLET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> goalReached() {
        final Function function = new Function(FUNC_GOALREACHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> finalized() {
        final Function function = new Function(FUNC_FINALIZED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> openingTime() {
        final Function function = new Function(FUNC_OPENINGTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> claimRefund(String refundee) {
        final Function function = new Function(
                FUNC_CLAIMREFUND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(refundee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> buyTokens(String beneficiary, BigInteger pebValue) {
        final Function function = new Function(
                FUNC_BUYTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, pebValue);
    }

    public RemoteCall<String> token() {
        final Function function = new Function(FUNC_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<CrowdsaleFinalizedEventResponse> getCrowdsaleFinalizedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(CROWDSALEFINALIZED_EVENT, transactionReceipt);
        ArrayList<CrowdsaleFinalizedEventResponse> responses = new ArrayList<CrowdsaleFinalizedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            CrowdsaleFinalizedEventResponse typedResponse = new CrowdsaleFinalizedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<TimedCrowdsaleExtendedEventResponse> getTimedCrowdsaleExtendedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TIMEDCROWDSALEEXTENDED_EVENT, transactionReceipt);
        ArrayList<TimedCrowdsaleExtendedEventResponse> responses = new ArrayList<TimedCrowdsaleExtendedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TimedCrowdsaleExtendedEventResponse typedResponse = new TimedCrowdsaleExtendedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.prevClosingTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newClosingTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<TokensPurchasedEventResponse> getTokensPurchasedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENSPURCHASED_EVENT, transactionReceipt);
        ArrayList<TokensPurchasedEventResponse> responses = new ArrayList<TokensPurchasedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TokensPurchasedEventResponse typedResponse = new TokensPurchasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.purchaser = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.beneficiary = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RefundableCrowdsale load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new RefundableCrowdsale(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static RefundableCrowdsale load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RefundableCrowdsale(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RefundableCrowdsale> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger goal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(goal)));
        return deployRemoteCall(RefundableCrowdsale.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<RefundableCrowdsale> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger goal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(goal)));
        return deployRemoteCall(RefundableCrowdsale.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class CrowdsaleFinalizedEventResponse {
        public KlayLogs.Log log;
    }

    public static class TimedCrowdsaleExtendedEventResponse {
        public KlayLogs.Log log;

        public BigInteger prevClosingTime;

        public BigInteger newClosingTime;
    }

    public static class TokensPurchasedEventResponse {
        public KlayLogs.Log log;

        public String purchaser;

        public String beneficiary;

        public BigInteger value;

        public BigInteger amount;
    }
}
