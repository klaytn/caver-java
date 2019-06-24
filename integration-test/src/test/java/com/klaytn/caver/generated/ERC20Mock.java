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
public class ERC20Mock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610bb5380380610bb58339818101604052604081101561003357600080fd5b50805160209091015161004f82826001600160e01b0361005616565b50506101e5565b6001600160a01b0382166100cb57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f45524332303a206d696e7420746f20746865207a65726f206164647265737300604482015290519081900360640190fd5b6100e48160025461016a60201b6106931790919060201c565b6002556001600160a01b0382166000908152602081815260409091205461011491839061069361016a821b17901c565b6001600160a01b0383166000818152602081815260408083209490945583518581529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35050565b6000828201838110156101de57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6109c1806101f46000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c806370a082311161007157806370a08231146101d957806379cc6790146101ff5780639dc29fac1461022b578063a457c2d714610257578063a9059cbb14610283578063dd62ed3e146102af576100b4565b8063095ea7b3146100b957806318160ddd146100f957806323b872dd14610113578063395093511461014957806340c10f191461017557806356189cb4146101a3575b600080fd5b6100e5600480360360408110156100cf57600080fd5b506001600160a01b0381351690602001356102dd565b604080519115158252519081900360200190f35b6101016102f3565b60408051918252519081900360200190f35b6100e56004803603606081101561012957600080fd5b506001600160a01b038135811691602081013590911690604001356102f9565b6100e56004803603604081101561015f57600080fd5b506001600160a01b038135169060200135610350565b6101a16004803603604081101561018b57600080fd5b506001600160a01b03813516906020013561038c565b005b6101a1600480360360608110156101b957600080fd5b506001600160a01b0381358116916020810135909116906040013561039a565b610101600480360360208110156101ef57600080fd5b50356001600160a01b03166103aa565b6101a16004803603604081101561021557600080fd5b506001600160a01b0381351690602001356103c5565b6101a16004803603604081101561024157600080fd5b506001600160a01b0381351690602001356103cf565b6100e56004803603604081101561026d57600080fd5b506001600160a01b0381351690602001356103d9565b6100e56004803603604081101561029957600080fd5b506001600160a01b038135169060200135610415565b610101600480360360408110156102c557600080fd5b506001600160a01b0381358116916020013516610422565b60006102ea33848461044d565b50600192915050565b60025490565b6000610306848484610539565b6001600160a01b038416600090815260016020908152604080832033808552925290912054610346918691610341908663ffffffff61063616565b61044d565b5060019392505050565b3360008181526001602090815260408083206001600160a01b038716845290915281205490916102ea918590610341908663ffffffff61069316565b61039682826106f4565b5050565b6103a583838361044d565b505050565b6001600160a01b031660009081526020819052604090205490565b61039682826107e4565b6103968282610829565b3360008181526001602090815260408083206001600160a01b038716845290915281205490916102ea918590610341908663ffffffff61063616565b60006102ea338484610539565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6001600160a01b0383166104925760405162461bcd60e51b81526004018080602001828103825260248152602001806109696024913960400191505060405180910390fd5b6001600160a01b0382166104d75760405162461bcd60e51b81526004018080602001828103825260228152602001806109266022913960400191505060405180910390fd5b6001600160a01b03808416600081815260016020908152604080832094871680845294825291829020859055815185815291517f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259281900390910190a3505050565b6001600160a01b03821661057e5760405162461bcd60e51b81526004018080602001828103825260238152602001806109036023913960400191505060405180910390fd5b6001600160a01b0383166000908152602081905260409020546105a7908263ffffffff61063616565b6001600160a01b0380851660009081526020819052604080822093909355908416815220546105dc908263ffffffff61069316565b6001600160a01b038084166000818152602081815260409182902094909455805185815290519193928716927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef92918290030190a3505050565b60008282111561068d576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6000828201838110156106ed576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b9392505050565b6001600160a01b03821661074f576040805162461bcd60e51b815260206004820152601f60248201527f45524332303a206d696e7420746f20746865207a65726f206164647265737300604482015290519081900360640190fd5b600254610762908263ffffffff61069316565b6002556001600160a01b03821660009081526020819052604090205461078e908263ffffffff61069316565b6001600160a01b0383166000818152602081815260408083209490945583518581529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35050565b6107ee8282610829565b6001600160a01b038216600090815260016020908152604080832033808552925290912054610396918491610341908563ffffffff61063616565b6001600160a01b03821661086e5760405162461bcd60e51b81526004018080602001828103825260218152602001806109486021913960400191505060405180910390fd5b600254610881908263ffffffff61063616565b6002556001600160a01b0382166000908152602081905260409020546108ad908263ffffffff61063616565b6001600160a01b038316600081815260208181526040808320949094558351858152935191937fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929081900390910190a3505056fe45524332303a207472616e7366657220746f20746865207a65726f206164647265737345524332303a20617070726f766520746f20746865207a65726f206164647265737345524332303a206275726e2066726f6d20746865207a65726f206164647265737345524332303a20617070726f76652066726f6d20746865207a65726f2061646472657373a265627a7a72305820fba877a2b5adf7f26e3f3c915d3bb41b093f7aabd4f7f17d90edf751a6f89ea664736f6c63430005090032";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_APPROVEINTERNAL = "approveInternal";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BURNFROM = "burnFrom";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected ERC20Mock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC20Mock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> increaseAllowance(String spender, BigInteger addedValue) {
        final Function function = new Function(
                FUNC_INCREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(spender), 
                new org.web3j.abi.datatypes.generated.Uint256(addedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> mint(String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> approveInternal(String owner, String spender, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVEINTERNAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner), 
                new org.web3j.abi.datatypes.Address(spender), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> burnFrom(String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_BURNFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> burn(String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
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

    public static ERC20Mock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC20Mock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC20Mock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC20Mock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC20Mock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String initialAccount, BigInteger initialBalance) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(initialAccount), 
                new org.web3j.abi.datatypes.generated.Uint256(initialBalance)));
        return deployRemoteCall(ERC20Mock.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC20Mock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String initialAccount, BigInteger initialBalance) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(initialAccount), 
                new org.web3j.abi.datatypes.generated.Uint256(initialBalance)));
        return deployRemoteCall(ERC20Mock.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
