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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class WhitelistCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516110ad3803806110ad8339818101604052606081101561003357600080fd5b5080516020820151604090920151909190828282610059336001600160e01b036101c316565b6001600255826100ca57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610129576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806110886025913960400191505060405180910390fd5b6001600160a01b038116610188576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806110426024913960400191505060405180910390fd5b600592909255600480546001600160a01b039283166001600160a01b0319918216179091556003805492909316911617905550610337915050565b6101db81600061021260201b61088b1790919060201c565b6040516001600160a01b038216907f22380c05984257a1cb900161c713dd71d39e74820f1aea43bd3f1bdd2096129990600090a250565b61022582826001600160e01b036102b616565b1561029157604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b038216610317576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806110666022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610cfc806103466000396000f3fe6080604052600436106100a75760003560e01c8063521eb27311610064578063521eb273146101b05780637362d9c8146101e1578063bb5f747b14610214578063d6cd947314610247578063ec8ac4d81461025c578063fc0c546a14610282576100a7565b806310154bad146100b2578063291d9549146100e55780632c4e722e146101185780633af32abf1461013f5780634042b66f146101865780634c5a628c1461019b575b6100b033610297565b005b3480156100be57600080fd5b506100b0600480360360208110156100d557600080fd5b50356001600160a01b031661039b565b3480156100f157600080fd5b506100b06004803603602081101561010857600080fd5b50356001600160a01b03166103eb565b34801561012457600080fd5b5061012d610438565b60408051918252519081900360200190f35b34801561014b57600080fd5b506101726004803603602081101561016257600080fd5b50356001600160a01b031661043e565b604080519115158252519081900360200190f35b34801561019257600080fd5b5061012d610457565b3480156101a757600080fd5b506100b061045d565b3480156101bc57600080fd5b506101c5610468565b604080516001600160a01b039092168252519081900360200190f35b3480156101ed57600080fd5b506100b06004803603602081101561020457600080fd5b50356001600160a01b0316610477565b34801561022057600080fd5b506101726004803603602081101561023757600080fd5b50356001600160a01b03166104c4565b34801561025357600080fd5b506100b06104d6565b6100b06004803603602081101561027257600080fd5b50356001600160a01b0316610297565b34801561028e57600080fd5b506101c56104df565b6002805460010190819055346102ad83826104ee565b60006102b88261053c565b6006549091506102ce908363ffffffff61055316565b6006556102db84826105b4565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a361032d8483610397565b6103356105be565b61033f8483610397565b50506002548114610397576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b6103a4336104c4565b6103df5760405162461bcd60e51b8152600401808060200182810382526040815260200180610c346040913960400191505060405180910390fd5b6103e8816105f7565b50565b6103f4336104c4565b61042f5760405162461bcd60e51b8152600401808060200182810382526040815260200180610c346040913960400191505060405180910390fd5b6103e88161063f565b60055490565b600061045160018363ffffffff61068716565b92915050565b60065490565b610466336106ee565b565b6004546001600160a01b031690565b610480336104c4565b6104bb5760405162461bcd60e51b8152600401808060200182810382526040815260200180610c346040913960400191505060405180910390fd5b6103e881610736565b6000610451818363ffffffff61068716565b6104663361063f565b6003546001600160a01b031690565b6104f78261043e565b6105325760405162461bcd60e51b8152600401808060200182810382526041815260200180610b8f6041913960600191505060405180910390fd5b610397828261077e565b60006104516005548361081590919063ffffffff16565b6000828201838110156105ad576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b610397828261086e565b6004546040516001600160a01b03909116903480156108fc02916000818181858888f193505050501580156103e8573d6000803e3d6000fd5b61060860018263ffffffff61088b16565b6040516001600160a01b038216907fee1504a83b6d4a361f4c1dc78ab59bfa30d6a3b6612c403e86bb01ef2984295f90600090a250565b61065060018263ffffffff61090c16565b6040516001600160a01b038216907f270d9b30cf5b0793bbfd54c9d5b94aeb49462b8148399000265144a8722da6b690600090a250565b60006001600160a01b0382166106ce5760405162461bcd60e51b8152600401808060200182810382526022815260200180610c126022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6106ff60008263ffffffff61090c16565b6040516001600160a01b038216907f0a8eb35e5ca14b3d6f28e4abf2f128dbab231a58b56e89beb5d636115001e16590600090a250565b61074760008263ffffffff61088b16565b6040516001600160a01b038216907f22380c05984257a1cb900161c713dd71d39e74820f1aea43bd3f1bdd2096129990600090a250565b6001600160a01b0382166107c35760405162461bcd60e51b815260040180806020018281038252602a815260200180610c74602a913960400191505060405180910390fd5b80610397576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b60008261082457506000610451565b8282028284828161083157fe5b04146105ad5760405162461bcd60e51b8152600401808060200182810382526021815260200180610bf16021913960400191505060405180910390fd5b600354610397906001600160a01b0316838363ffffffff61097316565b6108958282610687565b156108e7576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b6109168282610687565b6109515760405162461bcd60e51b8152600401808060200182810382526021815260200180610bd06021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b1790526109c59084906109ca565b505050565b6109dc826001600160a01b0316610b88565b610a2d576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b60208310610a6b5780518252601f199092019160209182019101610a4c565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610acd576040519150601f19603f3d011682016040523d82523d6000602084013e610ad2565b606091505b509150915081610b29576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b805115610b8257808060200190516020811015610b4557600080fd5b5051610b825760405162461bcd60e51b815260040180806020018281038252602a815260200180610c9e602a913960400191505060405180910390fd5b50505050565b3b15159056fe57686974656c69737443726f776473616c653a2062656e656669636961727920646f65736e27742068617665207468652057686974656c697374656420726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f77526f6c65733a206163636f756e7420697320746865207a65726f206164647265737357686974656c69737441646d696e526f6c653a2063616c6c657220646f6573206e6f742068617665207468652057686974656c69737441646d696e20726f6c6543726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a72305820f939e7c17e23736f98218b6cd6ffa64f095c446fd9b52e3fa48112f4ed8e23d864736f6c6343000509003243726f776473616c653a20746f6b656e20697320746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

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

    protected WhitelistCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected WhitelistCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public static WhitelistCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new WhitelistCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static WhitelistCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WhitelistCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WhitelistCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger _rate, String _wallet, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_rate), 
                new org.web3j.abi.datatypes.Address(_wallet), 
                new org.web3j.abi.datatypes.Address(_token)));
        return deployRemoteCall(WhitelistCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<WhitelistCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _rate, String _wallet, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_rate), 
                new org.web3j.abi.datatypes.Address(_wallet), 
                new org.web3j.abi.datatypes.Address(_token)));
        return deployRemoteCall(WhitelistCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
