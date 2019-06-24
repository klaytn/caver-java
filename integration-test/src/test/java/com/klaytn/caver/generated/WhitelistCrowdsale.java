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
public class WhitelistCrowdsale extends SmartContract {
    private static final String BINARY = "";

    public static final String FUNC_ADDWHITELISTED = "addWhitelisted";

    public static final String FUNC_REMOVEWHITELISTED = "removeWhitelisted";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_ISWHITELISTED = "isWhitelisted";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_RENOUNCEWHITELISTADMIN = "renounceWhitelistAdmin";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_ADDWHITELISTADMIN = "addWhitelistAdmin";

    public static final String FUNC_ISWHITELISTADMIN = "isWhitelistAdmin";

    public static final String FUNC_RENOUNCEWHITELISTED = "renounceWhitelisted";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event WHITELISTEDADDED_EVENT = new Event("WhitelistedAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTEDREMOVED_EVENT = new Event("WhitelistedRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTADMINADDED_EVENT = new Event("WhitelistAdminAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event WHITELISTADMINREMOVED_EVENT = new Event("WhitelistAdminRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected WhitelistCrowdsale(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected WhitelistCrowdsale(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addWhitelisted(String account) {
        final Function function = new Function(
                FUNC_ADDWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeWhitelisted(String account) {
        final Function function = new Function(
                FUNC_REMOVEWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function(FUNC_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isWhitelisted(String account) {
        final Function function = new Function(FUNC_ISWHITELISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> weiRaised() {
        final Function function = new Function(FUNC_WEIRAISED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceWhitelistAdmin() {
        final Function function = new Function(
                FUNC_RENOUNCEWHITELISTADMIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> wallet() {
        final Function function = new Function(FUNC_WALLET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addWhitelistAdmin(String account) {
        final Function function = new Function(
                FUNC_ADDWHITELISTADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isWhitelistAdmin(String account) {
        final Function function = new Function(FUNC_ISWHITELISTADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceWhitelisted() {
        final Function function = new Function(
                FUNC_RENOUNCEWHITELISTED, 
                Arrays.<Type>asList(), 
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

    public List<WhitelistedAddedEventResponse> getWhitelistedAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTEDADDED_EVENT, transactionReceipt);
        ArrayList<WhitelistedAddedEventResponse> responses = new ArrayList<WhitelistedAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistedAddedEventResponse typedResponse = new WhitelistedAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistedRemovedEventResponse> getWhitelistedRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTEDREMOVED_EVENT, transactionReceipt);
        ArrayList<WhitelistedRemovedEventResponse> responses = new ArrayList<WhitelistedRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistedRemovedEventResponse typedResponse = new WhitelistedRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistAdminAddedEventResponse> getWhitelistAdminAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTADMINADDED_EVENT, transactionReceipt);
        ArrayList<WhitelistAdminAddedEventResponse> responses = new ArrayList<WhitelistAdminAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistAdminAddedEventResponse typedResponse = new WhitelistAdminAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WhitelistAdminRemovedEventResponse> getWhitelistAdminRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTADMINREMOVED_EVENT, transactionReceipt);
        ArrayList<WhitelistAdminRemovedEventResponse> responses = new ArrayList<WhitelistAdminRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            WhitelistAdminRemovedEventResponse typedResponse = new WhitelistAdminRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static WhitelistCrowdsale load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new WhitelistCrowdsale(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static WhitelistCrowdsale load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WhitelistCrowdsale(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WhitelistCrowdsale> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WhitelistCrowdsale.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<WhitelistCrowdsale> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WhitelistCrowdsale.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class TokensPurchasedEventResponse {
        public KlayLogs.Log log;

        public String purchaser;

        public String beneficiary;

        public BigInteger value;

        public BigInteger amount;
    }

    public static class WhitelistedAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistedRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistAdminAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class WhitelistAdminRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
