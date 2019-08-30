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
public class IncreasingPriceCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610e9f380380610e9f833981810160405260c081101561003357600080fd5b508051602082015160408301516060840151608085015160a0909501516001600055939492939192909181818787838888826100d057604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b03821661012f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526025815260200180610e7a6025913960400191505060405180910390fd5b6001600160a01b03811661018e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610e566024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b0319918216179091556001805492909316911617905542821015610219576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526033815260200180610d7e6033913960400191505060405180910390fd5b818111610271576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526037815260200180610df66037913960400191505060405180910390fd5b600591909155600655806102d0576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180610e2d6029913960400191505060405180910390fd5b808211610328576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526045815260200180610db16045913960600191505060405180910390fd5b600791909155600855505050505050610a38806103466000396000f3fe6080604052600436106100a75760003560e01c8063521eb27311610064578063521eb273146101565780639e51051f14610187578063b7a8807c1461019c578063ec8ac4d8146101b1578063f7fb07b0146101d7578063fc0c546a146101ec576100a7565b80631515bc2b146100b257806321106109146100db5780632c4e722e146101025780634042b66f1461011757806347535d7b1461012c5780634b6753bc14610141575b6100b033610201565b005b3480156100be57600080fd5b506100c7610305565b604080519115158252519081900360200190f35b3480156100e757600080fd5b506100f061030e565b60408051918252519081900360200190f35b34801561010e57600080fd5b506100f0610314565b34801561012357600080fd5b506100f061034d565b34801561013857600080fd5b506100c7610353565b34801561014d57600080fd5b506100f061036e565b34801561016257600080fd5b5061016b610374565b604080516001600160a01b039092168252519081900360200190f35b34801561019357600080fd5b506100f0610383565b3480156101a857600080fd5b506100f0610389565b6100b0600480360360208110156101c757600080fd5b50356001600160a01b0316610201565b3480156101e357600080fd5b506100f061038f565b3480156101f857600080fd5b5061016b610439565b6000805460010190819055346102178382610448565b6000610222826104ab565b600454909150610238908363ffffffff6104cf16565b6004556102458482610532565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a36102978483610301565b61029f61053c565b6102a98483610301565b50506000548114610301576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b60065442115b90565b60085490565b600060405162461bcd60e51b81526004018080602001828103825260278152602001806109686027913960400191505060405180910390fd5b60045490565b6000600554421015801561036957506006544211155b905090565b60065490565b6002546001600160a01b031690565b60075490565b60055490565b6000610399610353565b6103a55750600061030b565b60006103bf6103b2610389565b429063ffffffff61057816565b905060006103e26103ce610389565b6103d661036e565b9063ffffffff61057816565b905060006103fd60085460075461057890919063ffffffff16565b905061043161042283610416868563ffffffff6105d516565b9063ffffffff61062e16565b6007549063ffffffff61057816565b935050505090565b6001546001600160a01b031690565b610450610353565b6104a1576040805162461bcd60e51b815260206004820152601860248201527f54696d656443726f776473616c653a206e6f74206f70656e0000000000000000604482015290519081900360640190fd5b6103018282610698565b6000806104b661038f565b90506104c8818463ffffffff6105d516565b9392505050565b600082820183811015610529576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b90505b92915050565b610301828261072f565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610575573d6000803e3d6000fd5b50565b6000828211156105cf576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6000826105e45750600061052c565b828202828482816105f157fe5b04146105295760405162461bcd60e51b815260040180806020018281038252602181526020018061098f6021913960400191505060405180910390fd5b6000808211610684576040805162461bcd60e51b815260206004820152601a60248201527f536166654d6174683a206469766973696f6e206279207a65726f000000000000604482015290519081900360640190fd5b600082848161068f57fe5b04949350505050565b6001600160a01b0382166106dd5760405162461bcd60e51b815260040180806020018281038252602a8152602001806109b0602a913960400191505060405180910390fd5b80610301576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b600154610301906001600160a01b0316838363ffffffff61074c16565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b17905261079e9084906107a3565b505050565b6107b5826001600160a01b0316610961565b610806576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b602083106108445780518252601f199092019160209182019101610825565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146108a6576040519150601f19603f3d011682016040523d82523d6000602084013e6108ab565b606091505b509150915081610902576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b80511561095b5780806020019051602081101561091e57600080fd5b505161095b5760405162461bcd60e51b815260040180806020018281038252602a8152602001806109da602a913960400191505060405180910390fd5b50505050565b3b15159056fe496e6372656173696e67507269636543726f776473616c653a207261746528292063616c6c6564536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f7743726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a72305820dbdbafb1e246621ea7c7c790456a9ad26217bdcbe24f3813126ce8eae149457864736f6c6343000509003254696d656443726f776473616c653a206f70656e696e672074696d65206973206265666f72652063757272656e742074696d65496e6372656173696e67507269636543726f776473616c653a20696e697469616c2072617465206973206e6f742067726561746572207468616e2066696e616c207261746554696d656443726f776473616c653a206f70656e696e672074696d65206973206e6f74206265666f726520636c6f73696e672074696d65496e6372656173696e67507269636543726f776473616c653a2066696e616c2072617465206973203043726f776473616c653a20746f6b656e20697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_HASCLOSED = "hasClosed";

    public static final String FUNC_FINALRATE = "finalRate";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISOPEN = "isOpen";

    public static final String FUNC_CLOSINGTIME = "closingTime";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_INITIALRATE = "initialRate";

    public static final String FUNC_OPENINGTIME = "openingTime";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_GETCURRENTRATE = "getCurrentRate";

    public static final String FUNC_TOKEN = "token";

    public static final Event TIMEDCROWDSALEEXTENDED_EVENT = new Event("TimedCrowdsaleExtended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected IncreasingPriceCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected IncreasingPriceCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> hasClosed() {
        final Function function = new Function(FUNC_HASCLOSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> finalRate() {
        final Function function = new Function(FUNC_FINALRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> initialRate() {
        final Function function = new Function(FUNC_INITIALRATE, 
                Arrays.<Type>asList(), 
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

    public RemoteCall<BigInteger> getCurrentRate() {
        final Function function = new Function(FUNC_GETCURRENTRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public static IncreasingPriceCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new IncreasingPriceCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static IncreasingPriceCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IncreasingPriceCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IncreasingPriceCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, String wallet, String token, BigInteger initialRate, BigInteger finalRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.generated.Uint256(initialRate), 
                new org.web3j.abi.datatypes.generated.Uint256(finalRate)));
        return deployRemoteCall(IncreasingPriceCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<IncreasingPriceCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, String wallet, String token, BigInteger initialRate, BigInteger finalRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.generated.Uint256(initialRate), 
                new org.web3j.abi.datatypes.generated.Uint256(finalRate)));
        return deployRemoteCall(IncreasingPriceCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
