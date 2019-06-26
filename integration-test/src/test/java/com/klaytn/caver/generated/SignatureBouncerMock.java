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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class SignatureBouncerMock extends SmartContract {
    private static final String BINARY = "6080604052610016336001600160e01b0361001b16565b61018f565b61003381600061006a60201b610c641790919060201c565b6040516001600160a01b038216907f47d1c22a25bb3a5d4e481b9b1e6944c2eade3181a0a20b495ed61d35b5323f2490600090a250565b61007d82826001600160e01b0361010e16565b156100e957604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b03821661016f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806111926022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b610ff48061019e6000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80638263ac0c1161008c578063b44e2ab911610066578063b44e2ab914610676578063ca422c431461067e578063e5c8b03d14610686578063eb12d61e1461068e576100cf565b80638263ac0c146103e6578063997027e81461048a578063b20bdb68146105cb576100cf565b80630e316ab7146100d45780636123f7ba146100fc57806368b51ac7146101a05780636b802108146102685780636d5b94271461031c5780637df73e27146103c0575b600080fd5b6100fa600480360360208110156100ea57600080fd5b50356001600160a01b03166106b4565b005b6100fa6004803603602081101561011257600080fd5b810190602081018135600160201b81111561012c57600080fd5b82018360208201111561013e57600080fd5b803590602001918460018302840111600160201b8311171561015f57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506106c0945050505050565b610254600480360360408110156101b657600080fd5b6001600160a01b038235169190810190604081016020820135600160201b8111156101e057600080fd5b8201836020820111156101f257600080fd5b803590602001918460018302840111600160201b8311171561021357600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061070a945050505050565b604080519115158252519081900360200190f35b6102546004803603604081101561027e57600080fd5b6001600160a01b038235169190810190604081016020820135600160201b8111156102a857600080fd5b8201836020820111156102ba57600080fd5b803590602001918460018302840111600160201b831117156102db57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061071f945050505050565b6100fa6004803603602081101561033257600080fd5b810190602081018135600160201b81111561034c57600080fd5b82018360208201111561035e57600080fd5b803590602001918460018302840111600160201b8311171561037f57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061072b945050505050565b610254600480360360208110156103d657600080fd5b50356001600160a01b0316610771565b6100fa600480360360208110156103fc57600080fd5b810190602081018135600160201b81111561041657600080fd5b82018360208201111561042857600080fd5b803590602001918460018302840111600160201b8311171561044957600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506106bd945050505050565b610254600480360360808110156104a057600080fd5b6001600160a01b038235169190810190604081016020820135600160201b8111156104ca57600080fd5b8201836020820111156104dc57600080fd5b803590602001918460018302840111600160201b831117156104fd57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092958435959094909350604081019250602001359050600160201b81111561055757600080fd5b82018360208201111561056957600080fd5b803590602001918460018302840111600160201b8311171561058a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610783945050505050565b6100fa600480360360408110156105e157600080fd5b81359190810190604081016020820135600160201b81111561060257600080fd5b82018360208201111561061457600080fd5b803590602001918460018302840111600160201b8311171561063557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610798945050505050565b6100fa6107e3565b6100fa610829565b6100fa61087e565b6100fa600480360360208110156106a457600080fd5b50356001600160a01b0316610887565b6106bd816108d4565b50565b806106cb33826108dd565b6107065760405162461bcd60e51b8152600401808060200182810382526039815260200180610f506039913960400191505060405180910390fd5b5050565b60006107168383610a0a565b90505b92915050565b600061071683836108dd565b806107363382610a0a565b6107065760405162461bcd60e51b815260040180806020018281038252602e815260200180610f22602e913960400191505060405180910390fd5b6000610719818363ffffffff610a6e16565b600061078f8583610ad5565b95945050505050565b806107a33382610ad5565b6107de5760405162461bcd60e51b8152600401808060200182810382526037815260200180610f896037913960400191505060405180910390fd5b505050565b6107ec33610771565b6108275760405162461bcd60e51b8152600401808060200182810382526030815260200180610ef26030913960400191505060405180910390fd5b565b604051806020016040528060008152506108433382610ad5565b6106bd5760405162461bcd60e51b8152600401808060200182810382526037815260200180610f896037913960400191505060405180910390fd5b610827336108d4565b61089033610771565b6108cb5760405162461bcd60e51b8152600401808060200182810382526030815260200180610ef26030913960400191505060405180910390fd5b6106bd81610b9a565b6106bd81610be2565b6040805160048082528183019092526000916060919060208201818038833901905050905060005b8151811015610950576000368281811061091b57fe5b9050013560f81c60f81b82828151811061093157fe5b60200101906001600160f81b031916908160001a905350600101610905565b50610a0230858360405160200180846001600160a01b03166001600160a01b031660601b8152601401836001600160a01b03166001600160a01b031660601b815260140182805190602001908083835b602083106109bf5780518252601f1990920191602091820191016109a0565b6001836020036101000a03801982511681845116808217855250505050505090500193505050506040516020818303038152906040528051906020012084610c2a565b949350505050565b6000610716308460405160200180836001600160a01b03166001600160a01b031660601b8152601401826001600160a01b03166001600160a01b031660601b8152601401925050506040516020818303038152906040528051906020012083610c2a565b60006001600160a01b038216610ab55760405162461bcd60e51b8152600401808060200182810382526022815260200180610ed06022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b600060603611610b165760405162461bcd60e51b8152600401808060200182810382526023815260200180610e8c6023913960400191505060405180910390fd5b6060806000369050036040519080825280601f01601f191660200182016040528015610b49576020820181803883390190505b50905060005b81518110156109505760003682818110610b6557fe5b9050013560f81c60f81b828281518110610b7b57fe5b60200101906001600160f81b031916908160001a905350600101610b4f565b610bab60008263ffffffff610c6416565b6040516001600160a01b038216907f47d1c22a25bb3a5d4e481b9b1e6944c2eade3181a0a20b495ed61d35b5323f2490600090a250565b610bf360008263ffffffff610ce516565b6040516001600160a01b038216907f3525e22824a8a7df2c9a6029941c824cf95b6447f1e13d5128fd3826d35afe8b90600090a250565b600080610c4683610c3a86610d4c565b9063ffffffff610d9d16565b90506001600160a01b03811615801590610a025750610a0281610771565b610c6e8282610a6e565b15610cc0576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b610cef8282610a6e565b610d2a5760405162461bcd60e51b8152600401808060200182810382526021815260200180610eaf6021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b604080517f19457468657265756d205369676e6564204d6573736167653a0a333200000000602080830191909152603c8083019490945282518083039094018452605c909101909152815191012090565b60008151604114610db057506000610719565b60208201516040830151606084015160001a7f7fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a0821115610df65760009350505050610719565b8060ff16601b14158015610e0e57508060ff16601c14155b15610e1f5760009350505050610719565b6040805160008152602080820180845289905260ff8416828401526060820186905260808201859052915160019260a0808401939192601f1981019281900390910190855afa158015610e76573d6000803e3d6000fd5b5050604051601f19015197965050505050505056fe5369676e6174757265426f756e6365723a206461746120697320746f6f2073686f7274526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c65526f6c65733a206163636f756e7420697320746865207a65726f20616464726573735369676e6572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865205369676e657220726f6c655369676e6174757265426f756e6365723a20696e76616c6964207369676e617475726520666f722063616c6c65725369676e6174757265426f756e6365723a20696e76616c6964207369676e617475726520666f722063616c6c657220616e64206d6574686f645369676e6174757265426f756e6365723a20696e76616c6964207369676e617475726520666f722063616c6c657220616e642064617461a265627a7a723058206233e8996b9a381248accf74620fc7573e5ba9528e9461d887c0948691755f6864736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_REMOVESIGNER = "removeSigner";

    public static final String FUNC_ONLYWITHVALIDSIGNATUREANDMETHOD = "onlyWithValidSignatureAndMethod";

    public static final String FUNC_CHECKVALIDSIGNATURE = "checkValidSignature";

    public static final String FUNC_CHECKVALIDSIGNATUREANDMETHOD = "checkValidSignatureAndMethod";

    public static final String FUNC_ONLYWITHVALIDSIGNATURE = "onlyWithValidSignature";

    public static final String FUNC_ISSIGNER = "isSigner";

    public static final String FUNC_THEWRONGMETHOD = "theWrongMethod";

    public static final String FUNC_CHECKVALIDSIGNATUREANDDATA = "checkValidSignatureAndData";

    public static final String FUNC_ONLYWITHVALIDSIGNATUREANDDATA = "onlyWithValidSignatureAndData";

    public static final String FUNC_ONLYSIGNERMOCK = "onlySignerMock";

    public static final String FUNC_TOOSHORTMSGDATA = "tooShortMsgData";

    public static final String FUNC_RENOUNCESIGNER = "renounceSigner";

    public static final String FUNC_ADDSIGNER = "addSigner";

    public static final Event SIGNERADDED_EVENT = new Event("SignerAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event SIGNERREMOVED_EVENT = new Event("SignerRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected SignatureBouncerMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SignatureBouncerMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> removeSigner(String account) {
        final Function function = new Function(
                FUNC_REMOVESIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onlyWithValidSignatureAndMethod(byte[] signature) {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<Boolean> checkValidSignature(String account, byte[] signature) {
        final Function function = new Function(FUNC_CHECKVALIDSIGNATURE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.DynamicBytes(signature)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> checkValidSignatureAndMethod(String account, byte[] signature) {
        final Function function = new Function(FUNC_CHECKVALIDSIGNATUREANDMETHOD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.DynamicBytes(signature)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public void onlyWithValidSignature(byte[] signature) {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<Boolean> isSigner(String account) {
        final Function function = new Function(FUNC_ISSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public void theWrongMethod(byte[] param0) {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<Boolean> checkValidSignatureAndData(String account, byte[] param1, BigInteger param2, byte[] signature) {
        final Function function = new Function(FUNC_CHECKVALIDSIGNATUREANDDATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account), 
                new org.web3j.abi.datatypes.DynamicBytes(param1), 
                new org.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.web3j.abi.datatypes.DynamicBytes(signature)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public void onlyWithValidSignatureAndData(BigInteger param0, byte[] signature) {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public void onlySignerMock() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public void tooShortMsgData() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> renounceSigner() {
        final Function function = new Function(
                FUNC_RENOUNCESIGNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addSigner(String account) {
        final Function function = new Function(
                FUNC_ADDSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<SignerAddedEventResponse> getSignerAddedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNERADDED_EVENT, transactionReceipt);
        ArrayList<SignerAddedEventResponse> responses = new ArrayList<SignerAddedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            SignerAddedEventResponse typedResponse = new SignerAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<SignerRemovedEventResponse> getSignerRemovedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNERREMOVED_EVENT, transactionReceipt);
        ArrayList<SignerRemovedEventResponse> responses = new ArrayList<SignerRemovedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            SignerRemovedEventResponse typedResponse = new SignerRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SignatureBouncerMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SignatureBouncerMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SignatureBouncerMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignatureBouncerMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignatureBouncerMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignatureBouncerMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SignatureBouncerMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignatureBouncerMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class SignerAddedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }

    public static class SignerRemovedEventResponse {
        public KlayLogs.Log log;

        public String account;
    }
}
