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
public class PaymentSplitter extends SmartContract {
    private static final String BINARY = "6080604052604051610b0e380380610b0e8339818101604052604081101561002657600080fd5b81019080805164010000000081111561003e57600080fd5b8201602081018481111561005157600080fd5b815185602082028301116401000000008211171561006e57600080fd5b5050929190602001805164010000000081111561008a57600080fd5b8201602081018481111561009d57600080fd5b81518560208202830111640100000000821117156100ba57600080fd5b5050929190505050805182511461011c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526032815260200180610ab16032913960400191505060405180910390fd5b600082511161018c57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601a60248201527f5061796d656e7453706c69747465723a206e6f20706179656573000000000000604482015290519081900360640190fd5b60005b82518110156101d6576101ce8382815181106101a757fe5b60200260200101518383815181106101bb57fe5b60200260200101516101de60201b60201c565b60010161018f565b505050610452565b6001600160a01b03821661023d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602c815260200180610a85602c913960400191505060405180910390fd5b600081116102ac57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601d60248201527f5061796d656e7453706c69747465723a20736861726573206172652030000000604482015290519081900360640190fd5b6001600160a01b0382166000908152600260205260409020541561031b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180610ae3602b913960400191505060405180910390fd5b60048054600181019091557f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b0180546001600160a01b0319166001600160a01b03841690811790915560009081526002602090815260408220839055905461038c9183906103d7811b6103fa17901c565b600055604080516001600160a01b03841681526020810183905281517f40c340f65e17194d14ddddb073d3c9f888e3cb52b5aae0c6c7706b4fbc905fac929181900390910190a15050565b60008282018381101561044b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b610624806104616000396000f3fe6080604052600436106100555760003560e01c806319165587146100915780633a98ef39146100c65780638b83209b146100ed5780639852595c14610133578063ce7c2ac214610166578063e33b7de314610199575b6040805133815234602082015281517f6ef95f06320e7a25a04a175ca677b7052bdd97131872c2192525a629f51be770929181900390910190a1005b34801561009d57600080fd5b506100c4600480360360208110156100b457600080fd5b50356001600160a01b03166101ae565b005b3480156100d257600080fd5b506100db61038e565b60408051918252519081900360200190f35b3480156100f957600080fd5b506101176004803603602081101561011057600080fd5b5035610394565b604080516001600160a01b039092168252519081900360200190f35b34801561013f57600080fd5b506100db6004803603602081101561015657600080fd5b50356001600160a01b03166103be565b34801561017257600080fd5b506100db6004803603602081101561018957600080fd5b50356001600160a01b03166103d9565b3480156101a557600080fd5b506100db6103f4565b6001600160a01b0381166000908152600260205260409020546102025760405162461bcd60e51b815260040180806020018281038252602681526020018061057e6026913960400191505060405180910390fd5b60015460009061021a9030319063ffffffff6103fa16565b6001600160a01b03831660009081526003602090815260408083205483546002909352908320549394509192610278929161026c9161026090879063ffffffff61045d16565b9063ffffffff6104b616565b9063ffffffff61052016565b9050806102b65760405162461bcd60e51b815260040180806020018281038252602b8152602001806105a4602b913960400191505060405180910390fd5b6001600160a01b0383166000908152600360205260409020546102df908263ffffffff6103fa16565b6001600160a01b03841660009081526003602052604090205560015461030b908263ffffffff6103fa16565b6001556040516001600160a01b0384169082156108fc029083906000818181858888f19350505050158015610344573d6000803e3d6000fd5b50604080516001600160a01b03851681526020810183905281517fdf20fd1e76bc69d672e4814fafb2c449bba3a5369d8359adf9e05e6fde87b056929181900390910190a1505050565b60005490565b6000600482815481106103a357fe5b6000918252602090912001546001600160a01b031692915050565b6001600160a01b031660009081526003602052604090205490565b6001600160a01b031660009081526002602052604090205490565b60015490565b600082820183811015610454576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b90505b92915050565b60008261046c57506000610457565b8282028284828161047957fe5b04146104545760405162461bcd60e51b81526004018080602001828103825260218152602001806105cf6021913960400191505060405180910390fd5b600080821161050c576040805162461bcd60e51b815260206004820152601a60248201527f536166654d6174683a206469766973696f6e206279207a65726f000000000000604482015290519081900360640190fd5b600082848161051757fe5b04949350505050565b600082821115610577576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b5090039056fe5061796d656e7453706c69747465723a206163636f756e7420686173206e6f207368617265735061796d656e7453706c69747465723a206163636f756e74206973206e6f7420647565207061796d656e74536166654d6174683a206d756c7469706c69636174696f6e206f766572666c6f77a265627a7a723058209e075bbfe4f4e00679df27fe3297a299d13a9aeb98c2eb657a4930a848a6d10064736f6c634300050900325061796d656e7453706c69747465723a206163636f756e7420697320746865207a65726f20616464726573735061796d656e7453706c69747465723a2070617965657320616e6420736861726573206c656e677468206d69736d617463685061796d656e7453706c69747465723a206163636f756e7420616c72656164792068617320736861726573";

    public static final String FUNC_RELEASE = "release";

    public static final String FUNC_TOTALSHARES = "totalShares";

    public static final String FUNC_PAYEE = "payee";

    public static final String FUNC_RELEASED = "released";

    public static final String FUNC_SHARES = "shares";

    public static final String FUNC_TOTALRELEASED = "totalReleased";

    public static final Event PAYEEADDED_EVENT = new Event("PayeeAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYMENTRELEASED_EVENT = new Event("PaymentReleased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYMENTRECEIVED_EVENT = new Event("PaymentReceived", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    protected PaymentSplitter(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected PaymentSplitter(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> release(String account) {
        final Function function = new Function(
                FUNC_RELEASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalShares() {
        final Function function = new Function(FUNC_TOTALSHARES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> payee(BigInteger index) {
        final Function function = new Function(FUNC_PAYEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> released(String account) {
        final Function function = new Function(FUNC_RELEASED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> shares(String account) {
        final Function function = new Function(FUNC_SHARES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalReleased() {
        final Function function = new Function(FUNC_TOTALRELEASED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<PayeeAddedEventResponse> getPayeeAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYEEADDED_EVENT, transactionReceipt);
        ArrayList<PayeeAddedEventResponse> responses = new ArrayList<PayeeAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PayeeAddedEventResponse typedResponse = new PayeeAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.shares = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PaymentReleasedEventResponse> getPaymentReleasedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYMENTRELEASED_EVENT, transactionReceipt);
        ArrayList<PaymentReleasedEventResponse> responses = new ArrayList<PaymentReleasedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PaymentReleasedEventResponse typedResponse = new PaymentReleasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<PaymentReceivedEventResponse> getPaymentReceivedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYMENTRECEIVED_EVENT, transactionReceipt);
        ArrayList<PaymentReceivedEventResponse> responses = new ArrayList<PaymentReceivedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PaymentReceivedEventResponse typedResponse = new PaymentReceivedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PaymentSplitter load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new PaymentSplitter(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static PaymentSplitter load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PaymentSplitter(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PaymentSplitter> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, BigInteger initialPebValue, List<String> payees, List<BigInteger> shares) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(payees, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(shares, org.web3j.abi.datatypes.generated.Uint256.class))));
        return deployRemoteCall(PaymentSplitter.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor, initialPebValue);
    }

    public static RemoteCall<PaymentSplitter> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialPebValue, List<String> payees, List<BigInteger> shares) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(payees, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(shares, org.web3j.abi.datatypes.generated.Uint256.class))));
        return deployRemoteCall(PaymentSplitter.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor, initialPebValue);
    }

    public static class PayeeAddedEventResponse {
        public KlayLogs.Log log;

        public String account;

        public BigInteger shares;
    }

    public static class PaymentReleasedEventResponse {
        public KlayLogs.Log log;

        public String to;

        public BigInteger amount;
    }

    public static class PaymentReceivedEventResponse {
        public KlayLogs.Log log;

        public String from;

        public BigInteger amount;
    }
}
