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
public class CappedCrowdsaleImpl extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610a31380380610a318339818101604052608081101561003357600080fd5b5080516020820151604083015160609093015160016000559192909180848484826100bf57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601460248201527f43726f776473616c653a20726174652069732030000000000000000000000000604482015290519081900360640190fd5b6001600160a01b03821661011e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526025815260200180610a0c6025913960400191505060405180910390fd5b6001600160a01b03811661017d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806109e86024913960400191505060405180910390fd5b600392909255600280546001600160a01b039283166001600160a01b031991821617909155600180549290931691161790558061021b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601960248201527f43617070656443726f776473616c653a20636170206973203000000000000000604482015290519081900360640190fd5b600555505050506107b7806102316000396000f3fe6080604052600436106100705760003560e01c80634f9359451161004e5780634f935945146100cc578063521eb273146100f5578063ec8ac4d814610126578063fc0c546a1461014c57610070565b80632c4e722e1461007b578063355274ea146100a25780634042b66f146100b7575b61007933610161565b005b34801561008757600080fd5b50610090610265565b60408051918252519081900360200190f35b3480156100ae57600080fd5b5061009061026b565b3480156100c357600080fd5b50610090610271565b3480156100d857600080fd5b506100e1610277565b604080519115158252519081900360200190f35b34801561010157600080fd5b5061010a61028b565b604080516001600160a01b039092168252519081900360200190f35b6100796004803603602081101561013c57600080fd5b50356001600160a01b0316610161565b34801561015857600080fd5b5061010a61029a565b60008054600101908190553461017783826102a9565b600061018282610321565b600454909150610198908363ffffffff61033e16565b6004556101a5848261039f565b604080518381526020810183905281516001600160a01b0387169233927f6faf93231a456e552dbc9961f58d9713ee4f2e69d15f1975b050ef0911053a7b929081900390910190a36101f78483610261565b6101ff6103a9565b6102098483610261565b50506000548114610261576040805162461bcd60e51b815260206004820152601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c00604482015290519081900360640190fd5b5050565b60035490565b60055490565b60045490565b6000600554610284610271565b1015905090565b6002546001600160a01b031690565b6001546001600160a01b031690565b6102b382826103e5565b6005546102ce826102c2610271565b9063ffffffff61033e16565b1115610261576040805162461bcd60e51b815260206004820152601d60248201527f43617070656443726f776473616c653a20636170206578636565646564000000604482015290519081900360640190fd5b60006103386003548361047c90919063ffffffff16565b92915050565b600082820183811015610398576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b61026182826104d5565b6002546040516001600160a01b03909116903480156108fc02916000818181858888f193505050501580156103e2573d6000803e3d6000fd5b50565b6001600160a01b03821661042a5760405162461bcd60e51b815260040180806020018281038252602a81526020018061072f602a913960400191505060405180910390fd5b80610261576040805162461bcd60e51b815260206004820152601960248201527f43726f776473616c653a20776569416d6f756e74206973203000000000000000604482015290519081900360640190fd5b60008261048b57506000610338565b8282028284828161049857fe5b04146103985760405162461bcd60e51b815260040180806020018281038252602181526020018061070e6021913960400191505060405180910390fd5b600154610261906001600160a01b0316838363ffffffff6104f216565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b179052610544908490610549565b505050565b61055b826001600160a01b0316610707565b6105ac576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b602083106105ea5780518252601f1990920191602091820191016105cb565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d806000811461064c576040519150601f19603f3d011682016040523d82523d6000602084013e610651565b606091505b5091509150816106a8576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b805115610701578080602001905160208110156106c457600080fd5b50516107015760405162461bcd60e51b815260040180806020018281038252602a815260200180610759602a913960400191505060405180910390fd5b50505050565b3b15159056fe536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f7743726f776473616c653a2062656e656669636961727920697320746865207a65726f20616464726573735361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564a265627a7a72305820da9193e347cccbc590661622f36f3d150ffc0e68b37ad690e757f16fe9499f5664736f6c6343000509003243726f776473616c653a20746f6b656e20697320746865207a65726f206164647265737343726f776473616c653a2077616c6c657420697320746865207a65726f2061646472657373";

    public static final String FUNC_RATE = "rate";

    public static final String FUNC_CAP = "cap";

    public static final String FUNC_WEIRAISED = "weiRaised";

    public static final String FUNC_CAPREACHED = "capReached";

    public static final String FUNC_WALLET = "wallet";

    public static final String FUNC_BUYTOKENS = "buyTokens";

    public static final String FUNC_TOKEN = "token";

    public static final Event TOKENSPURCHASED_EVENT = new Event("TokensPurchased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected CappedCrowdsaleImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected CappedCrowdsaleImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function(FUNC_RATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> cap() {
        final Function function = new Function(FUNC_CAP, 
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

    public RemoteCall<Boolean> capReached() {
        final Function function = new Function(FUNC_CAPREACHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> wallet() {
        final Function function = new Function(FUNC_WALLET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public static CappedCrowdsaleImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new CappedCrowdsaleImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static CappedCrowdsaleImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CappedCrowdsaleImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CappedCrowdsaleImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger rate, String wallet, String token, BigInteger cap) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.generated.Uint256(cap)));
        return deployRemoteCall(CappedCrowdsaleImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<CappedCrowdsaleImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger rate, String wallet, String token, BigInteger cap) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(rate), 
                new org.web3j.abi.datatypes.Address(wallet), 
                new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.generated.Uint256(cap)));
        return deployRemoteCall(CappedCrowdsaleImpl.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class TokensPurchasedEventResponse {
        public KlayLogs.Log log;

        public String purchaser;

        public String beneficiary;

        public BigInteger value;

        public BigInteger amount;
    }
}
