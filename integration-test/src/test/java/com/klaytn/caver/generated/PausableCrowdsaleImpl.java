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
public class PausableCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161104f38038061104f8339818101604052606081101561003357600080fd5b50805160208201516040909201516001600055909190828282826100b857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610117576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602581526020018061102a6025913960400191505060405180910390fd5b6001600160a01b038116610176576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610fe46024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b031991821617909155600180549290931691161790556101b1336101c3565b50506006805460ff1916905550610337565b6101db81600561021260201b6108e51790919060201c565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61022582826001600160e01b036102b616565b1561029157604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b038216610317576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610c9e806103466000396000f3fe60806040526004361061009c5760003560e01c80635c975abb116100645780635c975abb146101705780636ef8d66d1461018557806382dc1ec41461019a5780638456cb59146101cd578063ec8ac4d8146101e2578063fc0c546a146102085761009c565b80632c4e722e146100a75780633f4ba83a146100ce5780634042b66f146100e357806346fbf68e146100f8578063521eb2731461013f575b6100a53361021d565b005b3480156100b357600080fd5b506100bc610321565b60408051918252519081900360200190f35b3480156100da57600080fd5b506100a5610327565b3480156100ef57600080fd5b506100bc6103f8565b34801561010457600080fd5b5061012b6004803603602081101561011b57600080fd5b50356001600160a01b03166103fe565b604080519115158252519081900360200190f35b34801561014b57600080fd5b50610154610417565b604080516001600160a01b039092168252519081900360200190f35b34801561017c57600080fd5b5061012b610426565b34801561019157600080fd5b506100a561042f565b3480156101a657600080fd5b506100a5600480360360208110156101bd57600080fd5b50356001600160a01b031661043a565b3480156101d957600080fd5b506100a561048a565b6100a5600480360360208110156101f857600080fd5b50356001600160a01b031661021d565b34801561021457600080fd5b5061015461055b565b600080546001019081905534610233838261056a565b600061023e826105bf565b600454909150610254908363ffffffff6105d616565b6004556102618482610637565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a36102b3848361031d565b6102bb610641565b6102c5848361031d565b5050600054811461031d576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b60035490565b610330336103fe565b61036b5760405162461bcd60e51b8152600401808060200182810382526030815260200180610b826030913960400191505060405180910390fd5b60065460ff166103b9576040805162461bcd60e51b815260206004820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b604482015290519081900360640190fd5b6006805460ff191690556040805133815290517f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa9181900360200190a1565b60045490565b600061041160058363ffffffff61067a16565b92915050565b6002546001600160a01b031690565b60065460ff1690565b610438336106e1565b565b610443336103fe565b61047e5760405162461bcd60e51b8152600401808060200182810382526030815260200180610b826030913960400191505060405180910390fd5b61048781610729565b50565b610493336103fe565b6104ce5760405162461bcd60e51b8152600401808060200182810382526030815260200180610b826030913960400191505060405180910390fd5b60065460ff1615610519576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6006805460ff191660011790556040805133815290517f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a2589181900360200190a1565b6001546001600160a01b031690565b60065460ff16156105b5576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b61031d8282610771565b60006104116003548361080890919063ffffffff16565b600082820183811015610630576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b61031d8282610861565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610487573d6000803e3d6000fd5b60006001600160a01b0382166106c15760405162461bcd60e51b8152600401808060200182810382526022815260200180610bf46022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6106f260058263ffffffff61087e16565b6040516001600160a01b038216907fcd265ebaf09df2871cc7bd4133404a235ba12eff2041bb89d9c714a2621c7c7e90600090a250565b61073a60058263ffffffff6108e516565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b6001600160a01b0382166107b65760405162461bcd60e51b815260040180806020018281038252602a815260200180610c16602a913960400191505060405180910390fd5b8061031d576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b60008261081757506000610411565b8282028284828161082457fe5b04146106305760405162461bcd60e51b8152600401808060200182810382526021815260200180610bd36021913960400191505060405180910390fd5b60015461031d906001600160a01b0316838363ffffffff61096616565b610888828261067a565b6108c35760405162461bcd60e51b8152600401808060200182810382526021815260200180610bb26021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b6108ef828261067a565b15610941576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b1790526109b89084906109bd565b505050565b6109cf826001600160a01b0316610b7b565b610a20576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b60208310610a5e5780518252601f199092019160209182019101610a3f565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610ac0576040519150601f19603f3d011682016040523d82523d6000602084013e610ac5565b606091505b509150915081610b1c576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b805115610b7557808060200190516020811015610b3857600080fd5b5051610b755760405162461bcd60e51b815260040180806020018281038252602a815260200180610c40602a913960400191505060405180910390fd5b50505050565b3b15159056fe506175736572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652050617573657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f77526f6c65733a206163636f756e7420697320746865207a65726f206164647265737343726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a723058208e4bb7d5055b197f7e0e54c5308af0e3238a3aab12a39b4a3d83c175567876fc64736f6c6343000509003243726f776473616c653a20746f6b656e20697320746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event PAUSERADDED_EVENT = new Event("PauserAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSERREMOVED_EVENT = new Event("PauserRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected PausableCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PausableCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function(FUNC_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> weiRaised() {
        final Function function = new Function(FUNC_WEIRAISED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isPauser(String account) {
        final Function function = new Function(FUNC_ISPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> wallet() {
        final Function function = new Function(FUNC_WALLET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renouncePauser() {
        final Function function = new Function(
                FUNC_RENOUNCEPAUSER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addPauser(String account) {
        final Function function = new Function(
                FUNC_ADDPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
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

    public List<PausedEventResponse> getPausedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<UnpausedEventResponse> getUnpausedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PauserAddedEventResponse> getPauserAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERADDED_EVENT, transactionReceipt);
        ArrayList<PauserAddedEventResponse> responses = new ArrayList<PauserAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PauserAddedEventResponse typedResponse = new PauserAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PauserRemovedEventResponse> getPauserRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERREMOVED_EVENT, transactionReceipt);
        ArrayList<PauserRemovedEventResponse> responses = new ArrayList<PauserRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PauserRemovedEventResponse typedResponse = new PauserRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
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

    public static PausableCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PausableCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PausableCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PausableCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PausableCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger _rate, String _wallet, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_rate), 
                new org.web3j.abi.datatypes.Address(_wallet), 
                new org.web3j.abi.datatypes.Address(_token)));
        return deployRemoteCall(PausableCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PausableCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _rate, String _wallet, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_rate), 
                new org.web3j.abi.datatypes.Address(_wallet), 
                new org.web3j.abi.datatypes.Address(_token)));
        return deployRemoteCall(PausableCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class PausedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class UnpausedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class PauserAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class PauserRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class TokensPurchasedEventResponse {
        public KlayLogs.Log log;

        public String purchaser;

        public String beneficiary;

        public BigInteger value;

        public BigInteger amount;
    }
}
