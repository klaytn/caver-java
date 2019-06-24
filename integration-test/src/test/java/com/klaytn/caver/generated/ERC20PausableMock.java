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
public class ERC20PausableMock extends SmartContract {
    private static final String BINARY = "60806040523480156200001157600080fd5b50604051620011d4380380620011d4833981810160405260408110156200003757600080fd5b50805160209091015162000054336001600160e01b036200007b16565b6004805460ff191690556200007382826001600160e01b03620000cd16565b50506200038e565b62000096816003620001e860201b62000c1b1790919060201c565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b6001600160a01b0382166200014357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f45524332303a206d696e7420746f20746865207a65726f206164647265737300604482015290519081900360640190fd5b6200015f816002546200028f60201b62000b791790919060201c565b6002556001600160a01b038216600090815260208181526040909120546200019291839062000b796200028f821b17901c565b6001600160a01b0383166000818152602081815260408083209490945583518581529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35050565b620001fd82826001600160e01b036200030b16565b156200026a57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b6000828201838110156200030457604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b60006001600160a01b0382166200036e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620011b26022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610e14806200039e6000396000f3fe608060405234801561001057600080fd5b50600436106101005760003560e01c80636b2c0f55116100975780638456cb59116100665780638456cb591461027b578063a457c2d714610283578063a9059cbb146102af578063dd62ed3e146102db57610100565b80636b2c0f55146102015780636ef8d66d1461022757806370a082311461022f57806382dc1ec41461025557610100565b806339509351116100d3578063395093511461019f5780633f4ba83a146101cb57806346fbf68e146101d35780635c975abb146101f957610100565b8063095ea7b31461010557806318160ddd1461014557806323b872dd1461015f578063329daf9014610195575b600080fd5b6101316004803603604081101561011b57600080fd5b506001600160a01b038135169060200135610309565b604080519115158252519081900360200190f35b61014d610368565b60408051918252519081900360200190f35b6101316004803603606081101561017557600080fd5b506001600160a01b0381358116916020810135909116906040013561036e565b61019d6103cf565b005b610131600480360360408110156101b557600080fd5b506001600160a01b038135169060200135610415565b61019d61046d565b610131600480360360208110156101e957600080fd5b50356001600160a01b031661053e565b610131610557565b61019d6004803603602081101561021757600080fd5b50356001600160a01b0316610560565b61019d61056c565b61014d6004803603602081101561024557600080fd5b50356001600160a01b0316610575565b61019d6004803603602081101561026b57600080fd5b50356001600160a01b0316610590565b61019d6105dd565b6101316004803603604081101561029957600080fd5b506001600160a01b0381351690602001356106ae565b610131600480360360408110156102c557600080fd5b506001600160a01b038135169060200135610706565b61014d600480360360408110156102f157600080fd5b506001600160a01b038135811691602001351661075e565b60045460009060ff1615610357576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6103618383610789565b9392505050565b60025490565b60045460009060ff16156103bc576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6103c784848461079f565b949350505050565b6103d83361053e565b6104135760405162461bcd60e51b8152600401808060200182810382526030815260200180610d276030913960400191505060405180910390fd5b565b60045460009060ff1615610463576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b61036183836107f6565b6104763361053e565b6104b15760405162461bcd60e51b8152600401808060200182810382526030815260200180610d276030913960400191505060405180910390fd5b60045460ff166104ff576040805162461bcd60e51b815260206004820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b604482015290519081900360640190fd5b6004805460ff191690556040805133815290517f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa9181900360200190a1565b600061055160038363ffffffff61083216565b92915050565b60045460ff1690565b61056981610899565b50565b61041333610899565b6001600160a01b031660009081526020819052604090205490565b6105993361053e565b6105d45760405162461bcd60e51b8152600401808060200182810382526030815260200180610d276030913960400191505060405180910390fd5b610569816108a2565b6105e63361053e565b6106215760405162461bcd60e51b8152600401808060200182810382526030815260200180610d276030913960400191505060405180910390fd5b60045460ff161561066c576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6004805460ff191660011790556040805133815290517f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a2589181900360200190a1565b60045460009060ff16156106fc576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b61036183836108ea565b60045460009060ff1615610754576040805162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b604482015290519081900360640190fd5b6103618383610926565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6000610796338484610933565b50600192915050565b60006107ac848484610a1f565b6001600160a01b0384166000908152600160209081526040808320338085529252909120546107ec9186916107e7908663ffffffff610b1c16565b610933565b5060019392505050565b3360008181526001602090815260408083206001600160a01b038716845290915281205490916107969185906107e7908663ffffffff610b7916565b60006001600160a01b0382166108795760405162461bcd60e51b8152600401808060200182810382526022815260200180610d9a6022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61056981610bd3565b6108b360038263ffffffff610c1b16565b6040516001600160a01b038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b3360008181526001602090815260408083206001600160a01b038716845290915281205490916107969185906107e7908663ffffffff610b1c16565b6000610796338484610a1f565b6001600160a01b0383166109785760405162461bcd60e51b8152600401808060200182810382526024815260200180610dbc6024913960400191505060405180910390fd5b6001600160a01b0382166109bd5760405162461bcd60e51b8152600401808060200182810382526022815260200180610d576022913960400191505060405180910390fd5b6001600160a01b03808416600081815260016020908152604080832094871680845294825291829020859055815185815291517f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259281900390910190a3505050565b6001600160a01b038216610a645760405162461bcd60e51b8152600401808060200182810382526023815260200180610d046023913960400191505060405180910390fd5b6001600160a01b038316600090815260208190526040902054610a8d908263ffffffff610b1c16565b6001600160a01b038085166000908152602081905260408082209390935590841681522054610ac2908263ffffffff610b7916565b6001600160a01b038084166000818152602081815260409182902094909455805185815290519193928716927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef92918290030190a3505050565b600082821115610b73576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b600082820183811015610361576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b610be460038263ffffffff610c9c16565b6040516001600160a01b038216907fcd265ebaf09df2871cc7bd4133404a235ba12eff2041bb89d9c714a2621c7c7e90600090a250565b610c258282610832565b15610c77576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b610ca68282610832565b610ce15760405162461bcd60e51b8152600401808060200182810382526021815260200180610d796021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff1916905556fe45524332303a207472616e7366657220746f20746865207a65726f2061646472657373506175736572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652050617573657220726f6c6545524332303a20617070726f766520746f20746865207a65726f2061646472657373526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f206164647265737345524332303a20617070726f76652066726f6d20746865207a65726f2061646472657373a265627a7a72305820ed00b2874c262f1e18ebb5c0413842df09426b2347393944a3f971b4b3b888d164736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_ONLYPAUSERMOCK = "onlyPauserMock";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_REMOVEPAUSER = "removePauser";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

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

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected ERC20PausableMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC20PausableMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> approve(String spender, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(spender), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onlyPauserMock() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> increaseAllowance(String spender, BigInteger addedValue) {
        final Function function = new Function(
                FUNC_INCREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(spender), 
                new org.web3j.abi.datatypes.generated.Uint256(addedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isPauser(String account) {
        final Function function = new Function(FUNC_ISPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removePauser(String account) {
        final Function function = new Function(
                FUNC_REMOVEPAUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renouncePauser() {
        final Function function = new Function(
                FUNC_RENOUNCEPAUSER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> decreaseAllowance(String spender, BigInteger subtractedValue) {
        final Function function = new Function(
                FUNC_DECREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(spender), 
                new org.web3j.abi.datatypes.generated.Uint256(subtractedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner), 
                new org.web3j.abi.datatypes.Address(spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public List<TransferEventResponse> getTransferEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<ApprovalEventResponse> getApprovalEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ERC20PausableMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC20PausableMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC20PausableMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC20PausableMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC20PausableMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String initialAccount, BigInteger initialBalance) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(initialAccount), 
                new org.web3j.abi.datatypes.generated.Uint256(initialBalance)));
        return deployRemoteCall(ERC20PausableMock.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC20PausableMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String initialAccount, BigInteger initialBalance) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(initialAccount), 
                new org.web3j.abi.datatypes.generated.Uint256(initialBalance)));
        return deployRemoteCall(ERC20PausableMock.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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

    public static class TransferEventResponse {
        public KlayLogs.Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApprovalEventResponse {
        public KlayLogs.Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }
}
