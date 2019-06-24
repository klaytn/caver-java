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
public class ERC721 extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506100437f01ffc9a7000000000000000000000000000000000000000000000000000000006001600160e01b0361007a16565b6100757f80ac58cd000000000000000000000000000000000000000000000000000000006001600160e01b0361007a16565b610148565b7fffffffff00000000000000000000000000000000000000000000000000000000808216141561010b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b610d23806101576000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80636352211e116100665780636352211e146101b157806370a08231146101ce578063a22cb46514610206578063b88d4fde14610234578063e985e9c5146102fa5761009e565b806301ffc9a7146100a3578063081812fc146100de578063095ea7b31461011757806323b872dd1461014557806342842e0e1461017b575b600080fd5b6100ca600480360360208110156100b957600080fd5b50356001600160e01b031916610328565b604080519115158252519081900360200190f35b6100fb600480360360208110156100f457600080fd5b5035610347565b604080516001600160a01b039092168252519081900360200190f35b6101436004803603604081101561012d57600080fd5b506001600160a01b0381351690602001356103a9565b005b6101436004803603606081101561015b57600080fd5b506001600160a01b038135811691602081013590911690604001356104ba565b6101436004803603606081101561019157600080fd5b506001600160a01b0381358116916020810135909116906040013561050f565b6100fb600480360360208110156101c757600080fd5b503561052a565b6101f4600480360360208110156101e457600080fd5b50356001600160a01b0316610584565b60408051918252519081900360200190f35b6101436004803603604081101561021c57600080fd5b506001600160a01b03813516906020013515156105ec565b6101436004803603608081101561024a57600080fd5b6001600160a01b0382358116926020810135909116916040820135919081019060808101606082013564010000000081111561028557600080fd5b82018360208201111561029757600080fd5b803590602001918460018302840111640100000000831117156102b957600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506106b8945050505050565b6100ca6004803603604081101561031057600080fd5b506001600160a01b0381358116916020013516610710565b6001600160e01b03191660009081526020819052604090205460ff1690565b60006103528261073e565b61038d5760405162461bcd60e51b815260040180806020018281038252602c815260200180610c48602c913960400191505060405180910390fd5b506000908152600260205260409020546001600160a01b031690565b60006103b48261052a565b9050806001600160a01b0316836001600160a01b031614156104075760405162461bcd60e51b8152600401808060200182810382526021815260200180610c9d6021913960400191505060405180910390fd5b336001600160a01b038216148061042357506104238133610710565b61045e5760405162461bcd60e51b8152600401808060200182810382526038815260200180610bbd6038913960400191505060405180910390fd5b60008281526002602052604080822080546001600160a01b0319166001600160a01b0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b6104c4338261075b565b6104ff5760405162461bcd60e51b8152600401808060200182810382526031815260200180610cbe6031913960400191505060405180910390fd5b61050a8383836107ff565b505050565b61050a838383604051806020016040528060008152506106b8565b6000818152600160205260408120546001600160a01b03168061057e5760405162461bcd60e51b8152600401808060200182810382526029815260200180610c1f6029913960400191505060405180910390fd5b92915050565b60006001600160a01b0382166105cb5760405162461bcd60e51b815260040180806020018281038252602a815260200180610bf5602a913960400191505060405180910390fd5b6001600160a01b038216600090815260036020526040902061057e90610943565b6001600160a01b03821633141561064a576040805162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c657200000000000000604482015290519081900360640190fd5b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b6106c38484846104ba565b6106cf84848484610947565b61070a5760405162461bcd60e51b8152600401808060200182810382526032815260200180610b3b6032913960400191505060405180910390fd5b50505050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b6000908152600160205260409020546001600160a01b0316151590565b60006107668261073e565b6107a15760405162461bcd60e51b815260040180806020018281038252602c815260200180610b91602c913960400191505060405180910390fd5b60006107ac8361052a565b9050806001600160a01b0316846001600160a01b031614806107e75750836001600160a01b03166107dc84610347565b6001600160a01b0316145b806107f757506107f78185610710565b949350505050565b826001600160a01b03166108128261052a565b6001600160a01b0316146108575760405162461bcd60e51b8152600401808060200182810382526029815260200180610c746029913960400191505060405180910390fd5b6001600160a01b03821661089c5760405162461bcd60e51b8152600401808060200182810382526024815260200180610b6d6024913960400191505060405180910390fd5b6108a581610a7a565b6001600160a01b03831660009081526003602052604090206108c690610ab7565b6001600160a01b03821660009081526003602052604090206108e790610ace565b60008181526001602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b5490565b600061095b846001600160a01b0316610ad7565b610967575060016107f7565b604051630a85bd0160e11b815233600482018181526001600160a01b03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b838110156109e15781810151838201526020016109c9565b50505050905090810190601f168015610a0e5780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610a3057600080fd5b505af1158015610a44573d6000803e3d6000fd5b505050506040513d6020811015610a5a57600080fd5b50516001600160e01b031916630a85bd0160e11b14915050949350505050565b6000818152600260205260409020546001600160a01b031615610ab457600081815260026020526040902080546001600160a01b03191690555b50565b8054610aca90600163ffffffff610add16565b9055565b80546001019055565b3b151590565b600082821115610b34576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b5090039056fe4552433732313a207472616e7366657220746f206e6f6e20455243373231526563656976657220696d706c656d656e7465724552433732313a207472616e7366657220746f20746865207a65726f20616464726573734552433732313a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4552433732313a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734552433732313a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4552433732313a20617070726f76616c20746f2063757272656e74206f776e65724552433732313a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564a265627a7a72305820e147bfd0bdc28150952307642019557ccdc0ebcfc45cceac3cd16a17fc5fdfaa64736f6c63430005090032";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    protected ERC721(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getApproved(BigInteger tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> approve(String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> ownerOf(BigInteger tokenId) {
        final Function function = new Function(FUNC_OWNEROF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setApprovalForAll(String to, Boolean approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.Bool(approved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId, byte[] _data) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isApprovedForAll(String owner, String operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner), 
                new org.web3j.abi.datatypes.Address(operator)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<TransferEventResponse> getTransferEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
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
            typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ERC721 load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721 load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC721> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class TransferEventResponse {
        public KlayLogs.Log log;

        public String from;

        public String to;

        public BigInteger tokenId;
    }

    public static class ApprovalEventResponse {
        public KlayLogs.Log log;

        public String owner;

        public String approved;

        public BigInteger tokenId;
    }

    public static class ApprovalForAllEventResponse {
        public KlayLogs.Log log;

        public String owner;

        public String operator;

        public Boolean approved;
    }
}
