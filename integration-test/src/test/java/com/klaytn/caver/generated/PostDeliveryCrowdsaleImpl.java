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
public class PostDeliveryCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051611178380380611178833981810160405260a081101561003357600080fd5b5080516020820151604083015160608401516080909401516001600055929391929091908484848484826100c857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610127576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806111536025913960400191505060405180910390fd5b6001600160a01b038116610186576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602481526020018061112f6024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b0319918216179091556001805492909316911617905542821015610211576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001806110c56033913960400191505060405180910390fd5b818111610269576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260378152602001806110f86037913960400191505060405180910390fd5b60059190915560065560405161027e906102c6565b604051809103906000f08015801561029a573d6000803e3d6000fd5b50600880546001600160a01b0319166001600160a01b0392909216919091179055506102d39350505050565b61037e80610d4783390190565b610a65806102e26000396000f3fe60806040526004361061009c5760003560e01c80634b6753bc116100645780634b6753bc14610154578063521eb2731461016957806370a082311461019a578063b7a8807c146101cd578063ec8ac4d8146101e2578063fc0c546a146102085761009c565b80631515bc2b146100a75780632c4e722e146100d05780634042b66f146100f757806347535d7b1461010c57806349df728c14610121575b6100a53361021d565b005b3480156100b357600080fd5b506100bc610321565b604080519115158252519081900360200190f35b3480156100dc57600080fd5b506100e5610329565b60408051918252519081900360200190f35b34801561010357600080fd5b506100e561032f565b34801561011857600080fd5b506100bc610335565b34801561012d57600080fd5b506100a56004803603602081101561014457600080fd5b50356001600160a01b0316610350565b34801561016057600080fd5b506100e5610484565b34801561017557600080fd5b5061017e61048a565b604080516001600160a01b039092168252519081900360200190f35b3480156101a657600080fd5b506100e5600480360360208110156101bd57600080fd5b50356001600160a01b0316610499565b3480156101d957600080fd5b506100e56104b4565b6100a5600480360360208110156101f857600080fd5b50356001600160a01b031661021d565b34801561021457600080fd5b5061017e6104ba565b60008054600101908190553461023383826104c9565b600061023e8261052c565b600454909150610254908363ffffffff61054916565b60045561026184826105aa565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a36102b3848361031d565b6102bb6105fe565b6102c5848361031d565b5050600054811461031d576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b600654421190565b60035490565b60045490565b6000600554421015801561034b57506006544211155b905090565b610358610321565b6103935760405162461bcd60e51b81526004018080602001828103825260218152602001806109e66021913960400191505060405180910390fd5b6001600160a01b038116600090815260076020526040902054806103e85760405162461bcd60e51b81526004018080602001828103825260388152602001806109636038913960400191505060405180910390fd5b6001600160a01b038083166000908152600760205260408120556008541663beabacc86104136104ba565b604080516001600160e01b031960e085901b1681526001600160a01b03928316600482015291861660248301526044820185905251606480830192600092919082900301818387803b15801561046857600080fd5b505af115801561047c573d6000803e3d6000fd5b505050505050565b60065490565b6002546001600160a01b031690565b6001600160a01b031660009081526007602052604090205490565b60055490565b6001546001600160a01b031690565b6104d1610335565b610522576040805162461bcd60e51b815260206004820152601860248201527f54696d656443726f776473616c653a206e6f74206f70656e0000000000000000604482015290519081900360640190fd5b61031d828261063a565b6000610543600354836106d190919063ffffffff16565b92915050565b6000828201838110156105a3576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6001600160a01b0382166000908152600760205260409020546105d3908263ffffffff61054916565b6001600160a01b0380841660009081526007602052604090209190915560085461031d91168261072a565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610637573d6000803e3d6000fd5b50565b6001600160a01b03821661067f5760405162461bcd60e51b815260040180806020018281038252602a8152602001806109bc602a913960400191505060405180910390fd5b8061031d576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b6000826106e057506000610543565b828202828482816106ed57fe5b04146105a35760405162461bcd60e51b815260040180806020018281038252602181526020018061099b6021913960400191505060405180910390fd5b60015461031d906001600160a01b0316838363ffffffff61074716565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b17905261079990849061079e565b505050565b6107b0826001600160a01b031661095c565b610801576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b6020831061083f5780518252601f199092019160209182019101610820565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146108a1576040519150601f19603f3d011682016040523d82523d6000602084013e6108a6565b606091505b5091509150816108fd576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b8051156109565780806020019051602081101561091957600080fd5b50516109565760405162461bcd60e51b815260040180806020018281038252602a815260200180610a07602a913960400191505060405180910390fd5b50505050565b3b15159056fe506f737444656c697665727943726f776473616c653a2062656e6566696369617279206973206e6f742064756520616e7920746f6b656e73536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f7743726f776473616c653a2062656e656669636961727920697320746865207a65726f2061646472657373506f737444656c697665727943726f776473616c653a206e6f7420636c6f7365645361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a723058202a59048af0951d3d4cab19b1bdbe8970baf0e54ce65d440fe61238aafa68ec0564736f6c6343000509003260806040819052600080546001600160a01b0319163317908190556001600160a01b031681527f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d990602090a16103248061005a6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80632348238c14610046578063beabacc81461006e578063c6dbdf61146100a4575b600080fd5b61006c6004803603602081101561005c57600080fd5b50356001600160a01b03166100c8565b005b61006c6004803603606081101561008457600080fd5b506001600160a01b038135811691602081013590911690604001356101b0565b6100ac61028a565b604080516001600160a01b039092168252519081900360200190f35b6000546001600160a01b031633146101115760405162461bcd60e51b815260040180806020018281038252602c8152602001806102c4602c913960400191505060405180910390fd5b6001600160a01b0381166101565760405162461bcd60e51b815260040180806020018281038252602a81526020018061029a602a913960400191505060405180910390fd5b600080546001600160a01b0319166001600160a01b03838116919091179182905560408051929091168252517f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d9916020908290030190a150565b6000546001600160a01b031633146101f95760405162461bcd60e51b815260040180806020018281038252602c8152602001806102c4602c913960400191505060405180910390fd5b826001600160a01b031663a9059cbb83836040518363ffffffff1660e01b815260040180836001600160a01b03166001600160a01b0316815260200182815260200192505050602060405180830381600087803b15801561025957600080fd5b505af115801561026d573d6000803e3d6000fd5b505050506040513d602081101561028357600080fd5b5050505050565b6000546001600160a01b03169056fe5365636f6e646172793a206e6577207072696d61727920697320746865207a65726f20616464726573735365636f6e646172793a2063616c6c6572206973206e6f7420746865207072696d617279206163636f756e74a265627a7a72305820bf8ebc210a20fdcb2a8abc73b3079230db26017cf65fbe3e4fec012997aaf38d64736f6c6343000509003254696d656443726f776473616c653a206f70656e696e672074696d65206973206265666f72652063757272656e742074696d6554696d656443726f776473616c653a206f70656e696e672074696d65206973206e6f74206265666f726520636c6f73696e672074696d6543726f776473616c653a20746f6b656e20697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_HASCLOSED = "hasClosed";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISOPEN = "isOpen";

    public static final String FUNC_WITHDRAWTOKENS = "withdrawTokens";

    public static final String FUNC_CLOSINGTIME = "closingTime";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_OPENINGTIME = "openingTime";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event TIMEDCROWDSALEEXTENDED_EVENT = new Event("TimedCrowdsaleExtended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected PostDeliveryCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PostDeliveryCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> withdrawTokens(String beneficiary) {
        final Function function = new Function(
                FUNC_WITHDRAWTOKENS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public static PostDeliveryCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PostDeliveryCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PostDeliveryCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PostDeliveryCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PostDeliveryCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(PostDeliveryCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PostDeliveryCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(PostDeliveryCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
