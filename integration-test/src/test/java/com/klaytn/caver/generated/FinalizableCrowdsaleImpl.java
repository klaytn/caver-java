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
public class FinalizableCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610c87380380610c87833981810160405260a081101561003357600080fd5b5080516020820151604083015160608401516080909401516001600055929391929091908484848484826100c857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610127576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526025815260200180610c626025913960400191505060405180910390fd5b6001600160a01b038116610186576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610c3e6024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b0319918216179091556001805492909316911617905542821015610211576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526033815260200180610bd46033913960400191505060405180910390fd5b818111610269576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526037815260200180610c076037913960400191505060405180910390fd5b60059190915560065550506007805460ff19169055505050610944806102906000396000f3fe60806040526004361061009c5760003560e01c80634bb278f3116100645780634bb278f314610136578063521eb2731461014b578063b3f05b971461017c578063b7a8807c14610191578063ec8ac4d8146101a6578063fc0c546a146101cc5761009c565b80631515bc2b146100a75780632c4e722e146100d05780634042b66f146100f757806347535d7b1461010c5780634b6753bc14610121575b6100a5336101e1565b005b3480156100b357600080fd5b506100bc6102e5565b604080519115158252519081900360200190f35b3480156100dc57600080fd5b506100e56102ed565b60408051918252519081900360200190f35b34801561010357600080fd5b506100e56102f3565b34801561011857600080fd5b506100bc6102f9565b34801561012d57600080fd5b506100e5610314565b34801561014257600080fd5b506100a561031a565b34801561015757600080fd5b506101606103f5565b604080516001600160a01b039092168252519081900360200190f35b34801561018857600080fd5b506100bc610404565b34801561019d57600080fd5b506100e561040d565b6100a5600480360360208110156101bc57600080fd5b50356001600160a01b03166101e1565b3480156101d857600080fd5b50610160610413565b6000805460010190819055346101f78382610422565b600061020282610485565b600454909150610218908363ffffffff6104a216565b6004556102258482610503565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a361027784836102e1565b61027f61050d565b61028984836102e1565b505060005481146102e1576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b600654421190565b60035490565b60045490565b6000600554421015801561030f57506006544211155b905090565b60065490565b60075460ff161561035c5760405162461bcd60e51b81526004018080602001828103825260278152602001806108956027913960400191505060405180910390fd5b6103646102e5565b6103b5576040805162461bcd60e51b815260206004820181905260248201527f46696e616c697a61626c6543726f776473616c653a206e6f7420636c6f736564604482015290519081900360640190fd5b6007805460ff191660011790556103ca610549565b6040517f9270cc390c096600a1c17c44345a1ba689fafd99d97487b10cfccf86cf73183690600090a1565b6002546001600160a01b031690565b60075460ff1690565b60055490565b6001546001600160a01b031690565b61042a6102f9565b61047b576040805162461bcd60e51b815260206004820152601860248201527f54696d656443726f776473616c653a206e6f74206f70656e0000000000000000604482015290519081900360640190fd5b6102e1828261054b565b600061049c600354836105e290919063ffffffff16565b92915050565b6000828201838110156104fc576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6102e1828261063b565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610546573d6000803e3d6000fd5b50565b565b6001600160a01b0382166105905760405162461bcd60e51b815260040180806020018281038252602a8152602001806108bc602a913960400191505060405180910390fd5b806102e1576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b6000826105f15750600061049c565b828202828482816105fe57fe5b04146104fc5760405162461bcd60e51b81526004018080602001828103825260218152602001806108746021913960400191505060405180910390fd5b6001546102e1906001600160a01b0316838363ffffffff61065816565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b1790526106aa9084906106af565b505050565b6106c1826001600160a01b031661086d565b610712576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b602083106107505780518252601f199092019160209182019101610731565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146107b2576040519150601f19603f3d011682016040523d82523d6000602084013e6107b7565b606091505b50915091508161080e576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b8051156108675780806020019051602081101561082a57600080fd5b50516108675760405162461bcd60e51b815260040180806020018281038252602a8152602001806108e6602a913960400191505060405180910390fd5b50505050565b3b15159056fe536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f7746696e616c697a61626c6543726f776473616c653a20616c72656164792066696e616c697a656443726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a72305820d1090963af68a71c7e019dc76829354a2bf46bec9a33cc20a039869001bf02cb64736f6c6343000509003254696d656443726f776473616c653a206f70656e696e672074696d65206973206265666f72652063757272656e742074696d6554696d656443726f776473616c653a206f70656e696e672074696d65206973206e6f74206265666f726520636c6f73696e672074696d6543726f776473616c653a20746f6b656e20697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_HASCLOSED = "hasClosed";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISOPEN = "isOpen";

    public static final String FUNC_CLOSINGTIME = "closingTime";

    public static final String FUNC_FINALIZE = "finalize";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_FINALIZED = "finalized";

    public static final String FUNC_OPENINGTIME = "openingTime";

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

    protected FinalizableCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected FinalizableCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public static FinalizableCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new FinalizableCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static FinalizableCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new FinalizableCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<FinalizableCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(FinalizableCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<FinalizableCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(FinalizableCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
