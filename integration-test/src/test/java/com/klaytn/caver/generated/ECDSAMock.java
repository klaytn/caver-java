package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ECDSAMock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506102cd806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c806319045a251461003b578063918a15cf14610104575b600080fd5b6100e86004803603604081101561005157600080fd5b8135919081019060408101602082013564010000000081111561007357600080fd5b82018360208201111561008557600080fd5b803590602001918460018302840111640100000000831117156100a757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610133945050505050565b604080516001600160a01b039092168252519081900360200190f35b6101216004803603602081101561011a57600080fd5b503561014e565b60408051918252519081900360200190f35b6000610145838363ffffffff61015916565b90505b92915050565b600061014882610247565b6000815160411461016c57506000610148565b60208201516040830151606084015160001a7f7fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a08211156101b25760009350505050610148565b8060ff16601b141580156101ca57508060ff16601c14155b156101db5760009350505050610148565b6040805160008152602080820180845289905260ff8416828401526060820186905260808201859052915160019260a0808401939192601f1981019281900390910190855afa158015610232573d6000803e3d6000fd5b5050604051601f190151979650505050505050565b604080517f19457468657265756d205369676e6564204d6573736167653a0a333200000000602080830191909152603c8083019490945282518083039094018452605c90910190915281519101209056fea265627a7a72305820394c2dbd2a8c77ee31f5c5a76b239caccfc4805aad47532ac4859d4c3b4300bd64736f6c63430005090032";

    public static final String FUNC_RECOVER = "recover";

    public static final String FUNC_TOETHSIGNEDMESSAGEHASH = "toEthSignedMessageHash";

    protected ECDSAMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ECDSAMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> recover(byte[] hash, byte[] signature) {
        final Function function = new Function(FUNC_RECOVER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash), 
                new org.web3j.abi.datatypes.DynamicBytes(signature)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<byte[]> toEthSignedMessageHash(byte[] hash) {
        final Function function = new Function(FUNC_TOETHSIGNEDMESSAGEHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static ECDSAMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ECDSAMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ECDSAMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ECDSAMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ECDSAMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ECDSAMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ECDSAMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ECDSAMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
