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
public class IndividuallyCappedCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516111053803806111058339818101604052606081101561003357600080fd5b50805160208201516040909201516001600055909190828282826100b857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610117576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806110e06025913960400191505060405180910390fd5b6001600160a01b038116610176576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602481526020018061109a6024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b031991821617909155600180549290931691161790556101b1336101b9565b50505061032d565b6101d181600561020860201b6109071790919060201c565b6040516001600160a01b038216907fa7555c95b69d4f5cc847881feb4ab2883a1921319e34fa2043747b793d65b36e90600090a250565b61021b82826001600160e01b036102ac16565b1561028757604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661030d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806110be6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610d5e8061033c6000396000f3fe6080604052600436106100c25760003560e01c80635d5576f81161007f578063b3aefb7511610059578063b3aefb7514610268578063d1e07bcb1461029b578063ec8ac4d8146102b0578063fc0c546a146102d6576100c2565b80635d5576f8146101e757806380ad2cf3146101fc5780638dfbcf3614610235576100c2565b806321eff7fc146100cd5780632c4e722e1461011257806339564561146101275780633f4a64841461016e5780634042b66f146101a1578063521eb273146101b6575b6100cb336102eb565b005b3480156100d957600080fd5b50610100600480360360208110156100f057600080fd5b50356001600160a01b03166103ef565b60408051918252519081900360200190f35b34801561011e57600080fd5b5061010061040a565b34801561013357600080fd5b5061015a6004803603602081101561014a57600080fd5b50356001600160a01b0316610410565b604080519115158252519081900360200190f35b34801561017a57600080fd5b506100cb6004803603602081101561019157600080fd5b50356001600160a01b0316610429565b3480156101ad57600080fd5b50610100610435565b3480156101c257600080fd5b506101cb61043b565b604080516001600160a01b039092168252519081900360200190f35b3480156101f357600080fd5b506100cb61044a565b34801561020857600080fd5b506100cb6004803603604081101561021f57600080fd5b506001600160a01b038135169060200135610455565b34801561024157600080fd5b506100cb6004803603602081101561025857600080fd5b50356001600160a01b03166104b5565b34801561027457600080fd5b506101006004803603602081101561028b57600080fd5b50356001600160a01b0316610502565b3480156102a757600080fd5b506100cb61051d565b6100cb600480360360208110156102c657600080fd5b50356001600160a01b03166102eb565b3480156102e257600080fd5b506101cb610561565b6000805460010190819055346103018382610570565b600061030c826105ec565b600454909150610322908363ffffffff61060316565b60045561032f8482610664565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a3610381848361066e565b6103896106c1565b61039384836103eb565b505060005481146103eb576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b6001600160a01b031660009081526006602052604090205490565b60035490565b600061042360058363ffffffff6106fa16565b92915050565b61043281610761565b50565b60045490565b6002546001600160a01b031690565b61045333610761565b565b61045e33610410565b6104995760405162461bcd60e51b8152600401808060200182810382526030815260200180610cfa6030913960400191505060405180910390fd5b6001600160a01b03909116600090815260076020526040902055565b6104be33610410565b6104f95760405162461bcd60e51b8152600401808060200182810382526030815260200180610cfa6030913960400191505060405180910390fd5b6104328161076a565b6001600160a01b031660009081526007602052604090205490565b61052633610410565b6104535760405162461bcd60e51b8152600401808060200182810382526030815260200180610cfa6030913960400191505060405180910390fd5b6001546001600160a01b031690565b61057a82826107b2565b6001600160a01b0382166000908152600760209081526040808320546006909252909120546105af908363ffffffff61060316565b11156103eb5760405162461bcd60e51b8152600401808060200182810382526037815260200180610c996037913960400191505060405180910390fd5b60006104236003548361084990919063ffffffff16565b60008282018381101561065d576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6103eb82826108a2565b61067882826103eb565b6001600160a01b0382166000908152600660205260409020546106a1908263ffffffff61060316565b6001600160a01b0390921660009081526006602052604090209190915550565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610432573d6000803e3d6000fd5b60006001600160a01b0382166107415760405162461bcd60e51b8152600401808060200182810382526022815260200180610c4d6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610432816108bf565b61077b60058263ffffffff61090716565b6040516001600160a01b038216907fa7555c95b69d4f5cc847881feb4ab2883a1921319e34fa2043747b793d65b36e90600090a250565b6001600160a01b0382166107f75760405162461bcd60e51b815260040180806020018281038252602a815260200180610c6f602a913960400191505060405180910390fd5b806103eb576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b60008261085857506000610423565b8282028284828161086557fe5b041461065d5760405162461bcd60e51b8152600401808060200182810382526021815260200180610c2c6021913960400191505060405180910390fd5b6001546103eb906001600160a01b0316838363ffffffff61098816565b6108d060058263ffffffff6109df16565b6040516001600160a01b038216907f427400d279c506df610224b22ecce89b693fc1865864113f21c8d19c1f0c2a3b90600090a250565b61091182826106fa565b15610963576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b1790526109da908490610a46565b505050565b6109e982826106fa565b610a245760405162461bcd60e51b8152600401808060200182810382526021815260200180610c0b6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b610a58826001600160a01b0316610c04565b610aa9576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b60208310610ae75780518252601f199092019160209182019101610ac8565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610b49576040519150601f19603f3d011682016040523d82523d6000602084013e610b4e565b606091505b509150915081610ba5576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b805115610bfe57808060200190516020811015610bc157600080fd5b5051610bfe5760405162461bcd60e51b815260040180806020018281038252602a815260200180610cd0602a913960400191505060405180910390fd5b50505050565b3b15159056fe526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f77526f6c65733a206163636f756e7420697320746865207a65726f206164647265737343726f776473616c653a2062656e656669636961727920697320746865207a65726f2061646472657373496e646976696475616c6c7943617070656443726f776473616c653a2062656e65666963696172792773206361702065786365656465645361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564436170706572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652043617070657220726f6c65a265627a7a723058204eed2b5d6e4dbd06a021d56967817dd79850f9973cb2ffe2a7a52ff06d78abae64736f6c6343000509003243726f776473616c653a20746f6b656e20697320746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_GETCONTRIBUTION = "getContribution";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_ISCAPPER = "isCapper";

    public static final String FUNC_REMOVECAPPER = "removeCapper";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_RENOUNCECAPPER = "renounceCapper";

    public static final String FUNC_SETCAP = "setCap";

    public static final String FUNC_ADDCAPPER = "addCapper";

    public static final String FUNC_GETCAP = "getCap";

    public static final String FUNC_ONLYCAPPERMOCK = "onlyCapperMock";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event CAPPERADDED_EVENT = new Event("CapperAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event CAPPERREMOVED_EVENT = new Event("CapperRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected IndividuallyCappedCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected IndividuallyCappedCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> getContribution(String beneficiary) {
        final Function function = new Function(FUNC_GETCONTRIBUTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function(FUNC_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isCapper(String account) {
        final Function function = new Function(FUNC_ISCAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeCapper(String account) {
        final Function function = new Function(
                FUNC_REMOVECAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> weiRaised() {
        final Function function = new Function(FUNC_WEIRAISED, 
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceCapper() {
        final Function function = new Function(
                FUNC_RENOUNCECAPPER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setCap(String beneficiary, BigInteger cap) {
        final Function function = new Function(
                FUNC_SETCAP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary), 
                new org.web3j.abi.datatypes.generated.Uint256(cap)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addCapper(String account) {
        final Function function = new Function(
                FUNC_ADDCAPPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getCap(String beneficiary) {
        final Function function = new Function(FUNC_GETCAP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public void onlyCapperMock() {
        throw new RuntimeException("cannot call constant function with void return type");
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

    public List<CapperAddedEventResponse> getCapperAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(CAPPERADDED_EVENT, transactionReceipt);
        ArrayList<CapperAddedEventResponse> responses = new ArrayList<CapperAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            CapperAddedEventResponse typedResponse = new CapperAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<CapperRemovedEventResponse> getCapperRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(CAPPERREMOVED_EVENT, transactionReceipt);
        ArrayList<CapperRemovedEventResponse> responses = new ArrayList<CapperRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            CapperRemovedEventResponse typedResponse = new CapperRemovedEventResponse();
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

    public static IndividuallyCappedCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new IndividuallyCappedCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static IndividuallyCappedCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IndividuallyCappedCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IndividuallyCappedCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(IndividuallyCappedCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<IndividuallyCappedCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(IndividuallyCappedCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class CapperAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class CapperRemovedEventResponse {
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
