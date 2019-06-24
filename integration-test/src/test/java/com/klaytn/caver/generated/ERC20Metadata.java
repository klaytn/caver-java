package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.Arrays;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC20Metadata extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516102d23803806102d28339818101604052602081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8201602081018481111561005e57600080fd5b815164010000000081118282018710171561007857600080fd5b509093506100939250839150506001600160e01b0361009916565b5061014b565b80516100ac9060009060208401906100b0565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100f157805160ff191683800117855561011e565b8280016001018555821561011e579182015b8281111561011e578251825591602001919060010190610103565b5061012a92915061012e565b5090565b61014891905b8082111561012a5760008155600101610134565b90565b6101788061015a6000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c80633c130d9014610030575b600080fd5b6100386100ad565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561007257818101518382015260200161005a565b50505050905090810190601f16801561009f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156101395780601f1061010e57610100808354040283529160200191610139565b820191906000526020600020905b81548152906001019060200180831161011c57829003601f168201915b505050505090509056fea265627a7a72305820bd321398b57acc70e458c2f4147f94ebc899238c16e504f1e5cb5d62ccdc5fe864736f6c63430005090032";

    public static final String FUNC_TOKENURI = "tokenURI";

    protected ERC20Metadata(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC20Metadata(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> tokenURI() {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static ERC20Metadata load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC20Metadata(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC20Metadata load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC20Metadata(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC20Metadata> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String tokenURI_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(tokenURI_)));
        return deployRemoteCall(ERC20Metadata.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC20Metadata> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String tokenURI_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(tokenURI_)));
        return deployRemoteCall(ERC20Metadata.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }
}
