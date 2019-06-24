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
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC721Full extends SmartContract {
    private static final String BINARY = "60806040523480156200001157600080fd5b50604051620016b6380380620016b6833981810160405260408110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b820160208101848111156200006457600080fd5b81516401000000008111828201871017156200007f57600080fd5b505092919060200180516401000000008111156200009c57600080fd5b82016020810184811115620000b057600080fd5b8151640100000000811182820187101715620000cb57600080fd5b509093508492508391506200010b90507f01ffc9a7000000000000000000000000000000000000000000000000000000006001600160e01b03620001dd16565b6200013f7f80ac58cd000000000000000000000000000000000000000000000000000000006001600160e01b03620001dd16565b620001737f780e9d63000000000000000000000000000000000000000000000000000000006001600160e01b03620001dd16565b815162000188906009906020850190620002ac565b5080516200019e90600a906020840190620002ac565b50620001d37f5b5e139f000000000000000000000000000000000000000000000000000000006001600160e01b03620001dd16565b5050505062000351565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156200026f57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002ef57805160ff19168380011785556200031f565b828001600101855582156200031f579182015b828111156200031f57825182559160200191906001019062000302565b506200032d92915062000331565b5090565b6200034e91905b808211156200032d576000815560010162000338565b90565b61135580620003616000396000f3fe608060405234801561001057600080fd5b50600436106101005760003560e01c80634f6ccce711610097578063a22cb46511610066578063a22cb4651461033e578063b88d4fde1461036c578063c87b56dd14610432578063e985e9c51461044f57610100565b80634f6ccce7146102d65780636352211e146102f357806370a082311461031057806395d89b411461033657610100565b806318160ddd116100d357806318160ddd1461022457806323b872dd1461023e5780632f745c591461027457806342842e0e146102a057610100565b806301ffc9a71461010557806306fdde0314610140578063081812fc146101bd578063095ea7b3146101f6575b600080fd5b61012c6004803603602081101561011b57600080fd5b50356001600160e01b03191661047d565b604080519115158252519081900360200190f35b61014861049c565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561018257818101518382015260200161016a565b50505050905090810190601f1680156101af5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101da600480360360208110156101d357600080fd5b5035610533565b604080516001600160a01b039092168252519081900360200190f35b6102226004803603604081101561020c57600080fd5b506001600160a01b038135169060200135610595565b005b61022c6106a6565b60408051918252519081900360200190f35b6102226004803603606081101561025457600080fd5b506001600160a01b038135811691602081013590911690604001356106ac565b61022c6004803603604081101561028a57600080fd5b506001600160a01b038135169060200135610701565b610222600480360360608110156102b657600080fd5b506001600160a01b03813581169160208101359091169060400135610780565b61022c600480360360208110156102ec57600080fd5b503561079b565b6101da6004803603602081101561030957600080fd5b5035610801565b61022c6004803603602081101561032657600080fd5b50356001600160a01b031661085b565b6101486108c3565b6102226004803603604081101561035457600080fd5b506001600160a01b0381351690602001351515610924565b6102226004803603608081101561038257600080fd5b6001600160a01b038235811692602081013590911691604082013591908101906080810160608201356401000000008111156103bd57600080fd5b8201836020820111156103cf57600080fd5b803590602001918460018302840111640100000000831117156103f157600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506109f0945050505050565b6101486004803603602081101561044857600080fd5b5035610a48565b61012c6004803603604081101561046557600080fd5b506001600160a01b0381358116916020013516610b2d565b6001600160e01b03191660009081526020819052604090205460ff1690565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156105285780601f106104fd57610100808354040283529160200191610528565b820191906000526020600020905b81548152906001019060200180831161050b57829003601f168201915b505050505090505b90565b600061053e82610b5b565b6105795760405162461bcd60e51b815260040180806020018281038252602c81526020018061121f602c913960400191505060405180910390fd5b506000908152600260205260409020546001600160a01b031690565b60006105a082610801565b9050806001600160a01b0316836001600160a01b031614156105f35760405162461bcd60e51b81526004018080602001828103825260218152602001806112a36021913960400191505060405180910390fd5b336001600160a01b038216148061060f575061060f8133610b2d565b61064a5760405162461bcd60e51b81526004018080602001828103825260388152602001806111946038913960400191505060405180910390fd5b60008281526002602052604080822080546001600160a01b0319166001600160a01b0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b60075490565b6106b63382610b78565b6106f15760405162461bcd60e51b81526004018080602001828103825260318152602001806112c46031913960400191505060405180910390fd5b6106fc838383610c1c565b505050565b600061070c8361085b565b82106107495760405162461bcd60e51b815260040180806020018281038252602b8152602001806110e7602b913960400191505060405180910390fd5b6001600160a01b038316600090815260056020526040902080548390811061076d57fe5b9060005260206000200154905092915050565b6106fc838383604051806020016040528060008152506109f0565b60006107a56106a6565b82106107e25760405162461bcd60e51b815260040180806020018281038252602c8152602001806112f5602c913960400191505060405180910390fd5b600782815481106107ef57fe5b90600052602060002001549050919050565b6000818152600160205260408120546001600160a01b0316806108555760405162461bcd60e51b81526004018080602001828103825260298152602001806111f66029913960400191505060405180910390fd5b92915050565b60006001600160a01b0382166108a25760405162461bcd60e51b815260040180806020018281038252602a8152602001806111cc602a913960400191505060405180910390fd5b6001600160a01b038216600090815260036020526040902061085590610c3b565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156105285780601f106104fd57610100808354040283529160200191610528565b6001600160a01b038216331415610982576040805162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c657200000000000000604482015290519081900360640190fd5b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b6109fb8484846106ac565b610a0784848484610c3f565b610a425760405162461bcd60e51b81526004018080602001828103825260328152602001806111126032913960400191505060405180910390fd5b50505050565b6060610a5382610b5b565b610a8e5760405162461bcd60e51b815260040180806020018281038252602f815260200180611274602f913960400191505060405180910390fd5b6000828152600b602090815260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610b215780601f10610af657610100808354040283529160200191610b21565b820191906000526020600020905b815481529060010190602001808311610b0457829003601f168201915b50505050509050919050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b6000908152600160205260409020546001600160a01b0316151590565b6000610b8382610b5b565b610bbe5760405162461bcd60e51b815260040180806020018281038252602c815260200180611168602c913960400191505060405180910390fd5b6000610bc983610801565b9050806001600160a01b0316846001600160a01b03161480610c045750836001600160a01b0316610bf984610533565b6001600160a01b0316145b80610c145750610c148185610b2d565b949350505050565b610c27838383610d72565b610c318382610eb6565b6106fc8282610fab565b5490565b6000610c53846001600160a01b0316610fe9565b610c5f57506001610c14565b604051630a85bd0160e11b815233600482018181526001600160a01b03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b83811015610cd9578181015183820152602001610cc1565b50505050905090810190601f168015610d065780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610d2857600080fd5b505af1158015610d3c573d6000803e3d6000fd5b505050506040513d6020811015610d5257600080fd5b50516001600160e01b031916630a85bd0160e11b14915050949350505050565b826001600160a01b0316610d8582610801565b6001600160a01b031614610dca5760405162461bcd60e51b815260040180806020018281038252602981526020018061124b6029913960400191505060405180910390fd5b6001600160a01b038216610e0f5760405162461bcd60e51b81526004018080602001828103825260248152602001806111446024913960400191505060405180910390fd5b610e1881610fef565b6001600160a01b0383166000908152600360205260409020610e399061102c565b6001600160a01b0382166000908152600360205260409020610e5a90611043565b60008181526001602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b6001600160a01b038216600090815260056020526040812054610ee090600163ffffffff61104c16565b600083815260066020526040902054909150808214610f7b576001600160a01b0384166000908152600560205260408120805484908110610f1d57fe5b906000526020600020015490508060056000876001600160a01b03166001600160a01b031681526020019081526020016000208381548110610f5b57fe5b600091825260208083209091019290925591825260069052604090208190555b6001600160a01b0384166000908152600560205260409020805490610fa49060001983016110a9565b5050505050565b6001600160a01b0390911660009081526005602081815260408084208054868652600684529185208290559282526001810183559183529091200155565b3b151590565b6000818152600260205260409020546001600160a01b03161561102957600081815260026020526040902080546001600160a01b03191690555b50565b805461103f90600163ffffffff61104c16565b9055565b80546001019055565b6000828211156110a3576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b8154818355818111156106fc576000838152602090206106fc91810190830161053091905b808211156110e257600081556001016110ce565b509056fe455243373231456e756d657261626c653a206f776e657220696e646578206f7574206f6620626f756e64734552433732313a207472616e7366657220746f206e6f6e20455243373231526563656976657220696d706c656d656e7465724552433732313a207472616e7366657220746f20746865207a65726f20616464726573734552433732313a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4552433732313a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734552433732313a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4552433732314d657461646174613a2055524920717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76616c20746f2063757272656e74206f776e65724552433732313a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564455243373231456e756d657261626c653a20676c6f62616c20696e646578206f7574206f6620626f756e6473a265627a7a72305820d15158f6d971325bfc1b9c27d1185e9712662caeee935f700a0ff138ea17c41d64736f6c63430005090032";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_TOKENURI = "tokenURI";

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

    protected ERC721Full(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721Full(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> tokenOfOwnerByIndex(String owner, BigInteger index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner), 
                new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> tokenByIndex(BigInteger index) {
        final Function function = new Function(FUNC_TOKENBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<String> tokenURI(BigInteger tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public static ERC721Full load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721Full(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721Full load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Full(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721Full> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String name, String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol)));
        return deployRemoteCall(ERC721Full.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC721Full> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name, String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol)));
        return deployRemoteCall(ERC721Full.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
