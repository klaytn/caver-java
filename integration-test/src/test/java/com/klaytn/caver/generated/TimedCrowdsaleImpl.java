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
public class TimedCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610c89380380610c89833981810160405260a081101561003357600080fd5b5080516020820151604083015160608401516080909401516001600055929391929091908484848484826100c857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b038216610127576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526025815260200180610c646025913960400191505060405180910390fd5b6001600160a01b038116610186576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610c406024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b0319918216179091556001805492909316911617905542821015610211576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526033815260200180610bd66033913960400191505060405180910390fd5b818111610269576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526037815260200180610c096037913960400191505060405180910390fd5b6005919091556006555050505050610950806102866000396000f3fe6080604052600436106100915760003560e01c8063521eb27311610059578063521eb2731461012b578063a27aebbc1461015c578063b7a8807c14610186578063ec8ac4d81461019b578063fc0c546a146101c157610091565b80631515bc2b1461009c5780632c4e722e146100c55780634042b66f146100ec57806347535d7b146101015780634b6753bc14610116575b61009a336101d6565b005b3480156100a857600080fd5b506100b16102da565b604080519115158252519081900360200190f35b3480156100d157600080fd5b506100da6102e2565b60408051918252519081900360200190f35b3480156100f857600080fd5b506100da6102e8565b34801561010d57600080fd5b506100b16102ee565b34801561012257600080fd5b506100da610309565b34801561013757600080fd5b5061014061030f565b604080516001600160a01b039092168252519081900360200190f35b34801561016857600080fd5b5061009a6004803603602081101561017f57600080fd5b503561031e565b34801561019257600080fd5b506100da61032a565b61009a600480360360208110156101b157600080fd5b50356001600160a01b03166101d6565b3480156101cd57600080fd5b50610140610330565b6000805460010190819055346101ec838261033f565b60006101f7826103a2565b60045490915061020d908363ffffffff6103bf16565b60045561021a8482610420565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a361026c84836102d6565b61027461042a565b61027e84836102d6565b505060005481146102d6576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b600654421190565b60035490565b60045490565b6000600554421015801561030457506006544211155b905090565b60065490565b6002546001600160a01b031690565b61032781610463565b50565b60055490565b6001546001600160a01b031690565b6103476102ee565b610398576040805162461bcd60e51b815260206004820152601860248201527f54696d656443726f776473616c653a206e6f74206f70656e0000000000000000604482015290519081900360640190fd5b6102d6828261053f565b60006103b9600354836105d690919063ffffffff16565b92915050565b600082820183811015610419576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6102d6828261062f565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f19350505050158015610327573d6000803e3d6000fd5b61046b6102da565b156104bd576040805162461bcd60e51b815260206004820152601e60248201527f54696d656443726f776473616c653a20616c726561647920636c6f7365640000604482015290519081900360640190fd5b60065481116104fd5760405162461bcd60e51b815260040180806020018281038252603f815260200180610868603f913960400191505060405180910390fd5b600654604080519182526020820183905280517f46711e222f558a07afd26e5e71b48ecb0a8b2cdcd40faeb1323e05e2c76a2f329281900390910190a1600655565b6001600160a01b0382166105845760405162461bcd60e51b815260040180806020018281038252602a8152602001806108c8602a913960400191505060405180910390fd5b806102d6576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b6000826105e5575060006103b9565b828202828482816105f257fe5b04146104195760405162461bcd60e51b81526004018080602001828103825260218152602001806108a76021913960400191505060405180910390fd5b6001546102d6906001600160a01b0316838363ffffffff61064c16565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b17905261069e9084906106a3565b505050565b6106b5826001600160a01b0316610861565b610706576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b602083106107445780518252601f199092019160209182019101610725565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146107a6576040519150601f19603f3d011682016040523d82523d6000602084013e6107ab565b606091505b509150915081610802576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b80511561085b5780806020019051602081101561081e57600080fd5b505161085b5760405162461bcd60e51b815260040180806020018281038252602a8152602001806108f2602a913960400191505060405180910390fd5b50505050565b3b15159056fe54696d656443726f776473616c653a206e657720636c6f73696e672074696d65206973206265666f72652063757272656e7420636c6f73696e672074696d65536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f7743726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a72305820b5e4f8ed52ac349b229f9c7998f195ead0a55251fb2ffa2131c4c4ecaa52adda64736f6c6343000509003254696d656443726f776473616c653a206f70656e696e672074696d65206973206265666f72652063757272656e742074696d6554696d656443726f776473616c653a206f70656e696e672074696d65206973206e6f74206265666f726520636c6f73696e672074696d6543726f776473616c653a20746f6b656e20697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_HASCLOSED = "hasClosed";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_ISOPEN = "isOpen";

    public static final String FUNC_CLOSINGTIME = "closingTime";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_EXTENDTIME = "extendTime";

    public static final String FUNC_OPENINGTIME = "openingTime";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event TIMEDCROWDSALEEXTENDED_EVENT = new Event("TimedCrowdsaleExtended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected TimedCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected TimedCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> extendTime(BigInteger closingTime) {
        final Function function = new Function(
                FUNC_EXTENDTIME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(closingTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static TimedCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new TimedCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static TimedCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TimedCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TimedCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(TimedCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TimedCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger openingTime, BigInteger closingTime, BigInteger rate, String wallet, String token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(openingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(closingTime), 
                new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token)));
        return deployRemoteCall(TimedCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
