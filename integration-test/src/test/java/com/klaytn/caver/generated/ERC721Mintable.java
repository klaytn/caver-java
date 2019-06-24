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
public class ERC721Mintable extends SmartContract {
    private static final String BINARY = "6080604052620000387f01ffc9a7000000000000000000000000000000000000000000000000000000006001600160e01b036200008616565b6200006c7f80ac58cd000000000000000000000000000000000000000000000000000000006001600160e01b036200008616565b62000080336001600160e01b036200015516565b620002d1565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156200011857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b62000170816005620001a760201b62000e901790919060201c565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b620001bc82826001600160e01b036200024e16565b156200022957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b038216620002b1576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620015186022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b61123780620002e16000396000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c806370a082311161008c578063a22cb46511610066578063a22cb465146102ac578063aa271e1a146102da578063b88d4fde14610300578063e985e9c5146103c6576100ea565b806370a0823114610246578063983b2d561461027e57806398650275146102a4576100ea565b806323b872dd116100c857806323b872dd1461019157806340c10f19146101c757806342842e0e146101f35780636352211e14610229576100ea565b806301ffc9a7146100ef578063081812fc1461012a578063095ea7b314610163575b600080fd5b6101166004803603602081101561010557600080fd5b50356001600160e01b0319166103f4565b604080519115158252519081900360200190f35b6101476004803603602081101561014057600080fd5b5035610413565b604080516001600160a01b039092168252519081900360200190f35b61018f6004803603604081101561017957600080fd5b506001600160a01b038135169060200135610475565b005b61018f600480360360608110156101a757600080fd5b506001600160a01b03813581169160208101359091169060400135610586565b610116600480360360408110156101dd57600080fd5b506001600160a01b0381351690602001356105db565b61018f6004803603606081101561020957600080fd5b506001600160a01b03813581169160208101359091169060400135610634565b6101476004803603602081101561023f57600080fd5b503561064f565b61026c6004803603602081101561025c57600080fd5b50356001600160a01b03166106a9565b60408051918252519081900360200190f35b61018f6004803603602081101561029457600080fd5b50356001600160a01b0316610711565b61018f610761565b61018f600480360360408110156102c257600080fd5b506001600160a01b038135169060200135151561076c565b610116600480360360208110156102f057600080fd5b50356001600160a01b0316610838565b61018f6004803603608081101561031657600080fd5b6001600160a01b0382358116926020810135909116916040820135919081019060808101606082013564010000000081111561035157600080fd5b82018360208201111561036357600080fd5b8035906020019184600183028401116401000000008311171561038557600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061084b945050505050565b610116600480360360408110156103dc57600080fd5b506001600160a01b03813581169160200135166108a3565b6001600160e01b03191660009081526020819052604090205460ff1690565b600061041e826108d1565b6104595760405162461bcd60e51b815260040180806020018281038252602c81526020018061113a602c913960400191505060405180910390fd5b506000908152600260205260409020546001600160a01b031690565b60006104808261064f565b9050806001600160a01b0316836001600160a01b031614156104d35760405162461bcd60e51b81526004018080602001828103825260218152602001806111b16021913960400191505060405180910390fd5b336001600160a01b03821614806104ef57506104ef81336108a3565b61052a5760405162461bcd60e51b815260040180806020018281038252603881526020018061105e6038913960400191505060405180910390fd5b60008281526002602052604080822080546001600160a01b0319166001600160a01b0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b61059033826108ee565b6105cb5760405162461bcd60e51b81526004018080602001828103825260318152602001806111d26031913960400191505060405180910390fd5b6105d6838383610992565b505050565b60006105e633610838565b6106215760405162461bcd60e51b81526004018080602001828103825260308152602001806110e96030913960400191505060405180910390fd5b61062b8383610ad6565b50600192915050565b6105d68383836040518060200160405280600081525061084b565b6000818152600160205260408120546001600160a01b0316806106a35760405162461bcd60e51b81526004018080602001828103825260298152602001806110c06029913960400191505060405180910390fd5b92915050565b60006001600160a01b0382166106f05760405162461bcd60e51b815260040180806020018281038252602a815260200180611096602a913960400191505060405180910390fd5b6001600160a01b03821660009081526003602052604090206106a390610c07565b61071a33610838565b6107555760405162461bcd60e51b81526004018080602001828103825260308152602001806110e96030913960400191505060405180910390fd5b61075e81610c0b565b50565b61076a33610c53565b565b6001600160a01b0382163314156107ca576040805162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c657200000000000000604482015290519081900360640190fd5b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b60006106a360058363ffffffff610c9b16565b610856848484610586565b61086284848484610d02565b61089d5760405162461bcd60e51b8152600401808060200182810382526032815260200180610fdc6032913960400191505060405180910390fd5b50505050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b6000908152600160205260409020546001600160a01b0316151590565b60006108f9826108d1565b6109345760405162461bcd60e51b815260040180806020018281038252602c815260200180611032602c913960400191505060405180910390fd5b600061093f8361064f565b9050806001600160a01b0316846001600160a01b0316148061097a5750836001600160a01b031661096f84610413565b6001600160a01b0316145b8061098a575061098a81856108a3565b949350505050565b826001600160a01b03166109a58261064f565b6001600160a01b0316146109ea5760405162461bcd60e51b81526004018080602001828103825260298152602001806111886029913960400191505060405180910390fd5b6001600160a01b038216610a2f5760405162461bcd60e51b815260040180806020018281038252602481526020018061100e6024913960400191505060405180910390fd5b610a3881610e35565b6001600160a01b0383166000908152600360205260409020610a5990610e70565b6001600160a01b0382166000908152600360205260409020610a7a90610e87565b60008181526001602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b6001600160a01b038216610b31576040805162461bcd60e51b815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f2061646472657373604482015290519081900360640190fd5b610b3a816108d1565b15610b8c576040805162461bcd60e51b815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e74656400000000604482015290519081900360640190fd5b600081815260016020908152604080832080546001600160a01b0319166001600160a01b038716908117909155835260039091529020610bcb90610e87565b60405181906001600160a01b038416906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b5490565b610c1c60058263ffffffff610e9016565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b610c6460058263ffffffff610f1116565b6040516001600160a01b038216907fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669290600090a250565b60006001600160a01b038216610ce25760405162461bcd60e51b81526004018080602001828103825260228152602001806111666022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b6000610d16846001600160a01b0316610f78565b610d225750600161098a565b604051630a85bd0160e11b815233600482018181526001600160a01b03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b83811015610d9c578181015183820152602001610d84565b50505050905090810190601f168015610dc95780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610deb57600080fd5b505af1158015610dff573d6000803e3d6000fd5b505050506040513d6020811015610e1557600080fd5b50516001600160e01b031916630a85bd0160e11b14915050949350505050565b6000818152600260205260409020546001600160a01b03161561075e57600090815260026020526040902080546001600160a01b0319169055565b8054610e8390600163ffffffff610f7e16565b9055565b80546001019055565b610e9a8282610c9b565b15610eec576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b610f1b8282610c9b565b610f565760405162461bcd60e51b81526004018080602001828103825260218152602001806111196021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b3b151590565b600082821115610fd5576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b5090039056fe4552433732313a207472616e7366657220746f206e6f6e20455243373231526563656976657220696d706c656d656e7465724552433732313a207472616e7366657220746f20746865207a65726f20616464726573734552433732313a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4552433732313a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734552433732313a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654552433732313a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734552433732313a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4552433732313a20617070726f76616c20746f2063757272656e74206f776e65724552433732313a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564a265627a7a723058201cbf348efd3f06edaec07197d9c1a1849cdb7b90e357d454ca6f04c92a965e6a64736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_ADDMINTER = "addMinter";

    public static final String FUNC_RENOUNCEMINTER = "renounceMinter";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_ISMINTER = "isMinter";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final Event MINTERADDED_EVENT = new Event("MinterAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event MINTERREMOVED_EVENT = new Event("MinterRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    protected ERC721Mintable(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721Mintable(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> mint(String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addMinter(String account) {
        final Function function = new Function(
                FUNC_ADDMINTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceMinter() {
        final Function function = new Function(
                FUNC_RENOUNCEMINTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setApprovalForAll(String to, Boolean approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.Bool(approved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isMinter(String account) {
        final Function function = new Function(FUNC_ISMINTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public List<MinterAddedEventResponse> getMinterAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(MINTERADDED_EVENT, transactionReceipt);
        ArrayList<MinterAddedEventResponse> responses = new ArrayList<MinterAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            MinterAddedEventResponse typedResponse = new MinterAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<MinterRemovedEventResponse> getMinterRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(MINTERREMOVED_EVENT, transactionReceipt);
        ArrayList<MinterRemovedEventResponse> responses = new ArrayList<MinterRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            MinterRemovedEventResponse typedResponse = new MinterRemovedEventResponse();
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

    public static ERC721Mintable load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721Mintable(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721Mintable load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Mintable(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721Mintable> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721Mintable.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC721Mintable> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721Mintable.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class MinterAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class MinterRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
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
