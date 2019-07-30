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
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC777SenderRecipientMock extends SmartContract {
    private static final String BINARY = "60806040526001805462010000600160b01b031916751820a4b7618bde71dce8cdc73aab6c95905fad24000017905534801561003a57600080fd5b50610d858061004a6000396000f3fe608060405234801561001057600080fd5b50600436106100a85760003560e01c806375ab97821161007157806375ab97821461036f578063a8badaa514610455578063c97e18fc1461047b578063d2de64741461049a578063e0eb2180146104c0578063e1ecbd30146104e6576100a8565b806223de29146100ad578063249cb3fa146101955780633836ef89146101d357806344d17187146102975780634e4ae5a514610350575b600080fd5b610193600480360360c08110156100c357600080fd5b6001600160a01b03823581169260208101358216926040820135909216916060820135919081019060a081016080820135600160201b81111561010557600080fd5b82018360208201111561011757600080fd5b803590602001918460018302840111600160201b8311171561013857600080fd5b919390929091602081019035600160201b81111561015557600080fd5b82018360208201111561016757600080fd5b803590602001918460018302840111600160201b8311171561018857600080fd5b50909250905061050c565b005b6101c1600480360360408110156101ab57600080fd5b50803590602001356001600160a01b031661070b565b60408051918252519081900360200190f35b610193600480360360808110156101e957600080fd5b6001600160a01b03823581169260208101359091169160408201359190810190608081016060820135600160201b81111561022357600080fd5b82018360208201111561023557600080fd5b803590602001918460018302840111600160201b8311171561025657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610780945050505050565b610193600480360360608110156102ad57600080fd5b6001600160a01b0382351691602081013591810190606081016040820135600160201b8111156102dc57600080fd5b8201836020820111156102ee57600080fd5b803590602001918460018302840111600160201b8311171561030f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610868945050505050565b6101936004803603602081101561036657600080fd5b50351515610936565b610193600480360360c081101561038557600080fd5b6001600160a01b03823581169260208101358216926040820135909216916060820135919081019060a081016080820135600160201b8111156103c757600080fd5b8201836020820111156103d957600080fd5b803590602001918460018302840111600160201b831117156103fa57600080fd5b919390929091602081019035600160201b81111561041757600080fd5b82018360208201111561042957600080fd5b803590602001918460018302840111600160201b8311171561044a57600080fd5b509092509050610949565b6101936004803603602081101561046b57600080fd5b50356001600160a01b0316610b43565b6101936004803603602081101561049157600080fd5b50351515610be5565b610193600480360360208110156104b057600080fd5b50356001600160a01b0316610bff565b610193600480360360208110156104d657600080fd5b50356001600160a01b0316610c4f565b610193600480360360208110156104fc57600080fd5b50356001600160a01b0316610c9e565b600154610100900460ff161561052157600080fd5b604080516370a0823160e01b81526001600160a01b03891660048201529051339160009183916370a08231916024808301926020929190829003018186803b15801561056c57600080fd5b505afa158015610580573d6000803e3d6000fd5b505050506040513d602081101561059657600080fd5b5051604080516370a0823160e01b81526001600160a01b038b811660048301529151929350600092918516916370a0823191602480820192602092909190829003018186803b1580156105e857600080fd5b505afa1580156105fc573d6000803e3d6000fd5b505050506040513d602081101561061257600080fd5b5051604080516001600160a01b03808f168252808e166020830152808d1692820192909252606081018b905290851660c082015260e081018490526101008101829052610120608082018181529082018990529192507f47e915878c47f3ec4d7ff646a2becb229f64fd2abe4d2b5e2bb4275b0cf50d4e918d918d918d918d918d918d918d918d918d918d918d919060a0820161014083018a8a80828437600083820152601f01601f191690910184810383528881526020019050888880828437600083820152604051601f909101601f19169092018290039f50909d5050505050505050505050505050a15050505050505050505050565b6000828152602081815260408083206001600160a01b038516845290915281205460ff1661073a576000610779565b604051602001808073455243313832305f4143434550545f4d4147494360601b8152506014019050604051602081830303815290604052805190602001205b9392505050565b836001600160a01b0316639bd9bbc68484846040518463ffffffff1660e01b815260040180846001600160a01b03166001600160a01b0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156107fc5781810151838201526020016107e4565b50505050905090810190601f1680156108295780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b15801561084a57600080fd5b505af115801561085e573d6000803e3d6000fd5b5050505050505050565b6040805163fe9d930360e01b815260048101848152602482019283528351604483015283516001600160a01b0387169363fe9d9303938793879390929160640190602085019080838360005b838110156108cc5781810151838201526020016108b4565b50505050905090810190601f1680156108f95780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561091957600080fd5b505af115801561092d573d6000803e3d6000fd5b50505050505050565b6001805460ff1916911515919091179055565b60015460ff161561095957600080fd5b604080516370a0823160e01b81526001600160a01b03891660048201529051339160009183916370a08231916024808301926020929190829003018186803b1580156109a457600080fd5b505afa1580156109b8573d6000803e3d6000fd5b505050506040513d60208110156109ce57600080fd5b5051604080516370a0823160e01b81526001600160a01b038b811660048301529151929350600092918516916370a0823191602480820192602092909190829003018186803b158015610a2057600080fd5b505afa158015610a34573d6000803e3d6000fd5b505050506040513d6020811015610a4a57600080fd5b5051604080516001600160a01b03808f168252808e166020830152808d1692820192909252606081018b905290851660c082015260e081018490526101008101829052610120608082018181529082018990529192507faa3e88aca472e90221daf7d3d601abafb62b120319089d7a2c2f63588da85529918d918d918d918d918d918d918d918d918d918d918d919060a0820161014083018a8a80828437600083820152601f01601f191690910184810383528881526020019050888880828437600083820152604051601f909101601f19169092018290039f50909d5050505050505050505050505050a15050505050505050505050565b6001546040805174115490cdcdcdd51bdad95b9cd49958da5c1a595b9d605a1b815281519081900360150181206329965a1d60e01b825230600483015260248201526001600160a01b03848116604483015291516201000090930491909116916329965a1d9160648082019260009290919082900301818387803b158015610bca57600080fd5b505af1158015610bde573d6000803e3d6000fd5b5050505050565b600180549115156101000261ff0019909216919091179055565b604080517122a9219b9b9baa37b5b2b739a9b2b73232b960711b81529051908190036012019020610c309082610d22565b306001600160a01b038216811415610c4b57610c4b81610c9e565b5050565b6040805174115490cdcdcdd51bdad95b9cd49958da5c1a595b9d605a1b81529051908190036015019020610c839082610d22565b306001600160a01b038216811415610c4b57610c4b81610b43565b600154604080517122a9219b9b9baa37b5b2b739a9b2b73232b960711b815281519081900360120181206329965a1d60e01b825230600483015260248201526001600160a01b03848116604483015291516201000090930491909116916329965a1d9160648082019260009290919082900301818387803b158015610bca57600080fd5b6000918252602082815260408084206001600160a01b0390931684529190529020805460ff1916600117905556fea265627a7a7230582019843353e01538f03b4f4e41eb4732b6fcbf833f3065646d0ac559b0d3432bbb64736f6c63430005090032";

    public static final String FUNC_TOKENSRECEIVED = "tokensReceived";

    public static final String FUNC_CANIMPLEMENTINTERFACEFORADDRESS = "canImplementInterfaceForAddress";

    public static final String FUNC_SEND = "send";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_SETSHOULDREVERTSEND = "setShouldRevertSend";

    public static final String FUNC_TOKENSTOSEND = "tokensToSend";

    public static final String FUNC_REGISTERRECIPIENT = "registerRecipient";

    public static final String FUNC_SETSHOULDREVERTRECEIVE = "setShouldRevertReceive";

    public static final String FUNC_SENDERFOR = "senderFor";

    public static final String FUNC_RECIPIENTFOR = "recipientFor";

    public static final String FUNC_REGISTERSENDER = "registerSender";

    public static final Event TOKENSTOSENDCALLED_EVENT = new Event("TokensToSendCalled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENSRECEIVEDCALLED_EVENT = new Event("TokensReceivedCalled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected ERC777SenderRecipientMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC777SenderRecipientMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> tokensReceived(String operator, String from, String to, BigInteger amount, byte[] userData, byte[] operatorData) {
        final Function function = new Function(
                FUNC_TOKENSRECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator), 
                new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(userData), 
                new org.web3j.abi.datatypes.DynamicBytes(operatorData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> canImplementInterfaceForAddress(byte[] interfaceHash, String account) {
        final Function function = new Function(FUNC_CANIMPLEMENTINTERFACEFORADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(interfaceHash), 
                new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> send(String token, String to, BigInteger amount, byte[] data) {
        final Function function = new Function(
                FUNC_SEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> burn(String token, BigInteger amount, byte[] data) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setShouldRevertSend(Boolean shouldRevert) {
        final Function function = new Function(
                FUNC_SETSHOULDREVERTSEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(shouldRevert)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> tokensToSend(String operator, String from, String to, BigInteger amount, byte[] userData, byte[] operatorData) {
        final Function function = new Function(
                FUNC_TOKENSTOSEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator), 
                new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(userData), 
                new org.web3j.abi.datatypes.DynamicBytes(operatorData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> registerRecipient(String recipient) {
        final Function function = new Function(
                FUNC_REGISTERRECIPIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setShouldRevertReceive(Boolean shouldRevert) {
        final Function function = new Function(
                FUNC_SETSHOULDREVERTRECEIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(shouldRevert)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> senderFor(String account) {
        final Function function = new Function(
                FUNC_SENDERFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> recipientFor(String account) {
        final Function function = new Function(
                FUNC_RECIPIENTFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> registerSender(String sender) {
        final Function function = new Function(
                FUNC_REGISTERSENDER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(sender)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<TokensToSendCalledEventResponse> getTokensToSendCalledEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENSTOSENDCALLED_EVENT, transactionReceipt);
        ArrayList<TokensToSendCalledEventResponse> responses = new ArrayList<TokensToSendCalledEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TokensToSendCalledEventResponse typedResponse = new TokensToSendCalledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.operatorData = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.fromBalance = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            typedResponse.toBalance = (BigInteger) eventValues.getNonIndexedValues().get(8).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<TokensReceivedCalledEventResponse> getTokensReceivedCalledEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENSRECEIVEDCALLED_EVENT, transactionReceipt);
        ArrayList<TokensReceivedCalledEventResponse> responses = new ArrayList<TokensReceivedCalledEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            TokensReceivedCalledEventResponse typedResponse = new TokensReceivedCalledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.operatorData = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.fromBalance = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            typedResponse.toBalance = (BigInteger) eventValues.getNonIndexedValues().get(8).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ERC777SenderRecipientMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC777SenderRecipientMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC777SenderRecipientMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC777SenderRecipientMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC777SenderRecipientMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC777SenderRecipientMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC777SenderRecipientMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC777SenderRecipientMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class TokensToSendCalledEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String from;

        public String to;

        public BigInteger amount;

        public byte[] data;

        public byte[] operatorData;

        public String token;

        public BigInteger fromBalance;

        public BigInteger toBalance;
    }

    public static class TokensReceivedCalledEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String from;

        public String to;

        public BigInteger amount;

        public byte[] data;

        public byte[] operatorData;

        public String token;

        public BigInteger fromBalance;

        public BigInteger toBalance;
    }
}
