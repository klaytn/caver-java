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
public class TokenVesting extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516110e83803806110e8833981810160405260a081101561003357600080fd5b50805160208201516040808401516060850151608090950151600080546001600160a01b031916331780825593519596949592949391926001600160a01b0392909216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908290a36001600160a01b0385166100fb576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602d815260200180611090602d913960400191505060405180910390fd5b81831115610154576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b8152602001806110bd602b913960400191505060405180910390fd5b600082116101c357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601b60248201527f546f6b656e56657374696e673a206475726174696f6e20697320300000000000604482015290519081900360640190fd5b426101db838661028360201b61070a1790919060201c565b11610231576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f815260200180611061602f913960400191505060405180910390fd5b600180546001600160a01b0319166001600160a01b0387161790556005805460ff191682151517905560048290556102748484610283602090811b61070a17901c565b600255505050600355506102fe565b6000828201838110156102f757604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b610d548061030d6000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063872a78101161008c5780639852595c116100665780639852595c1461019c578063be9a6555146101c2578063f2fde38b146101ca578063fa01dc06146101f0576100cf565b8063872a7810146101705780638da5cb5b1461018c5780638f32d59b14610194576100cf565b80630fb5a6b4146100d457806313d033c0146100ee57806319165587146100f657806338af3eed1461011e578063715018a61461014257806374a8f1031461014a575b600080fd5b6100dc610216565b60408051918252519081900360200190f35b6100dc61021c565b61011c6004803603602081101561010c57600080fd5b50356001600160a01b0316610222565b005b610126610327565b604080516001600160a01b039092168252519081900360200190f35b61011c610336565b61011c6004803603602081101561016057600080fd5b50356001600160a01b03166103d9565b610178610601565b604080519115158252519081900360200190f35b61012661060a565b610178610619565b6100dc600480360360208110156101b257600080fd5b50356001600160a01b031661062a565b6100dc610649565b61011c600480360360208110156101e057600080fd5b50356001600160a01b031661064f565b6101786004803603602081101561020657600080fd5b50356001600160a01b03166106b4565b60045490565b60025490565b600061022d826106d2565b905060008111610284576040805162461bcd60e51b815260206004820152601f60248201527f546f6b656e56657374696e673a206e6f20746f6b656e73206172652064756500604482015290519081900360640190fd5b6001600160a01b0382166000908152600660205260409020546102ad908263ffffffff61070a16565b6001600160a01b038084166000818152600660205260409020929092556001546102df9291168363ffffffff61076b16565b604080516001600160a01b03841681526020810183905281517fc7798891864187665ac6dd119286e44ec13f014527aeeb2b8eb3fd413df93179929181900390910190a15050565b6001546001600160a01b031690565b61033e610619565b61038f576040805162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572604482015290519081900360640190fd5b600080546040516001600160a01b03909116907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908390a3600080546001600160a01b0319169055565b6103e1610619565b610432576040805162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572604482015290519081900360640190fd5b60055460ff16610489576040805162461bcd60e51b815260206004820152601b60248201527f546f6b656e56657374696e673a2063616e6e6f74207265766f6b650000000000604482015290519081900360640190fd5b6001600160a01b03811660009081526007602052604090205460ff16156104e15760405162461bcd60e51b8152600401808060200182810382526023815260200180610cfd6023913960400191505060405180910390fd5b604080516370a0823160e01b815230600482015290516000916001600160a01b038416916370a0823191602480820192602092909190829003018186803b15801561052b57600080fd5b505afa15801561053f573d6000803e3d6000fd5b505050506040513d602081101561055557600080fd5b505190506000610564836106d2565b90506000610578838363ffffffff6107c216565b6001600160a01b0385166000908152600760205260409020805460ff1916600117905590506105bf6105a861060a565b6001600160a01b038616908363ffffffff61076b16565b604080516001600160a01b038616815290517f39983c6d4d174a7aee564f449d4a5c3c7ac9649d72b7793c56901183996f8af69181900360200190a150505050565b60055460ff1690565b6000546001600160a01b031690565b6000546001600160a01b0316331490565b6001600160a01b0381166000908152600660205260409020545b919050565b60035490565b610657610619565b6106a8576040805162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572604482015290519081900360640190fd5b6106b18161081f565b50565b6001600160a01b031660009081526007602052604090205460ff1690565b6001600160a01b038116600090815260066020526040812054610704906106f8846108bf565b9063ffffffff6107c216565b92915050565b600082820183811015610764576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b1790526107bd908490610a04565b505050565b600082821115610819576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6001600160a01b0381166108645760405162461bcd60e51b8152600401808060200182810382526026815260200180610c8c6026913960400191505060405180910390fd5b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b604080516370a0823160e01b8152306004820152905160009182916001600160a01b038516916370a08231916024808301926020929190829003018186803b15801561090a57600080fd5b505afa15801561091e573d6000803e3d6000fd5b505050506040513d602081101561093457600080fd5b50516001600160a01b0384166000908152600660205260408120549192509061096490839063ffffffff61070a16565b905060025442101561097b57600092505050610644565b6004546003546109909163ffffffff61070a16565b421015806109b657506001600160a01b03841660009081526007602052604090205460ff165b156109c45791506106449050565b6109fb6004546109ef6109e2600354426107c290919063ffffffff16565b849063ffffffff610bc216565b9063ffffffff610c1b16565b92505050610644565b610a16826001600160a01b0316610c85565b610a67576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b60208310610aa55780518252601f199092019160209182019101610a86565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610b07576040519150601f19603f3d011682016040523d82523d6000602084013e610b0c565b606091505b509150915081610b63576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b805115610bbc57808060200190516020811015610b7f57600080fd5b5051610bbc5760405162461bcd60e51b815260040180806020018281038252602a815260200180610cd3602a913960400191505060405180910390fd5b50505050565b600082610bd157506000610704565b82820282848281610bde57fe5b04146107645760405162461bcd60e51b8152600401808060200182810382526021815260200180610cb26021913960400191505060405180910390fd5b6000808211610c71576040805162461bcd60e51b815260206004820152601a60248201527f536166654d6174683a206469766973696f6e206279207a65726f000000000000604482015290519081900360640190fd5b6000828481610c7c57fe5b04949350505050565b3b15159056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f775361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564546f6b656e56657374696e673a20746f6b656e20616c7265616479207265766f6b6564a265627a7a72305820cbb82e0b3d443c81f5f3011f40750f6d27cece8bd0c23aca5bade62de3aa004764736f6c63430005090032546f6b656e56657374696e673a2066696e616c2074696d65206973206265666f72652063757272656e742074696d65546f6b656e56657374696e673a2062656e656669636961727920697320746865207a65726f2061646472657373546f6b656e56657374696e673a20636c696666206973206c6f6e676572207468616e206475726174696f6e";

    public static final String FUNC_DURATION = "duration";

    public static final String FUNC_CLIFF = "cliff";

    public static final String FUNC_RELEASE = "release";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REVOKE = "revoke";

    public static final String FUNC_REVOCABLE = "revocable";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_RELEASED = "released";

    public static final String FUNC_START = "start";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_REVOKED = "revoked";

    public static final Event TOKENSRELEASED_EVENT = new Event("TokensReleased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENVESTINGREVOKED_EVENT = new Event("TokenVestingRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected TokenVesting(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected TokenVesting(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> duration() {
        final Function function = new Function(FUNC_DURATION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> cliff() {
        final Function function = new Function(FUNC_CLIFF, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> release(String token) {
        final Function function = new Function(
                FUNC_RELEASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> revoke(String token) {
        final Function function = new Function(
                FUNC_REVOKE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> revocable() {
        final Function function = new Function(FUNC_REVOCABLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isOwner() {
        final Function function = new Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> released(String token) {
        final Function function = new Function(FUNC_RELEASED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> start() {
        final Function function = new Function(FUNC_START, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> revoked(String token) {
        final Function function = new Function(FUNC_REVOKED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<TokensReleasedEventResponse> getTokensReleasedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENSRELEASED_EVENT, transactionReceipt);
        ArrayList<TokensReleasedEventResponse> responses = new ArrayList<TokensReleasedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TokensReleasedEventResponse typedResponse = new TokensReleasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<TokenVestingRevokedEventResponse> getTokenVestingRevokedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENVESTINGREVOKED_EVENT, transactionReceipt);
        ArrayList<TokenVestingRevokedEventResponse> responses = new ArrayList<TokenVestingRevokedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TokenVestingRevokedEventResponse typedResponse = new TokenVestingRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TokenVesting load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new TokenVesting(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static TokenVesting load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TokenVesting(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TokenVesting> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String beneficiary, BigInteger start, BigInteger cliffDuration, BigInteger duration, Boolean revocable) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary), 
                new org.web3j.abi.datatypes.generated.Uint256(start), 
                new org.web3j.abi.datatypes.generated.Uint256(cliffDuration), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.Bool(revocable)));
        return deployRemoteCall(TokenVesting.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TokenVesting> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String beneficiary, BigInteger start, BigInteger cliffDuration, BigInteger duration, Boolean revocable) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(beneficiary), 
                new org.web3j.abi.datatypes.generated.Uint256(start), 
                new org.web3j.abi.datatypes.generated.Uint256(cliffDuration), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.Bool(revocable)));
        return deployRemoteCall(TokenVesting.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public static class TokensReleasedEventResponse {
        public KlayLogs.Log log;

        public String token;

        public BigInteger amount;
    }

    public static class TokenVestingRevokedEventResponse {
        public KlayLogs.Log log;

        public String token;
    }

    public static class OwnershipTransferredEventResponse {
        public KlayLogs.Log log;

        public String previousOwner;

        public String newOwner;
    }
}
