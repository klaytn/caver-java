package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class TokenTimelock extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516105c13803806105c18339818101604052606081101561003357600080fd5b508051602082015160409092015190919042811161009c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252603281526020018061058f6032913960400191505060405180910390fd5b600080546001600160a01b039485166001600160a01b03199182161790915560018054939094169216919091179091556002556104b1806100de6000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806338af3eed1461005157806386d1a69f14610075578063b91d40011461007f578063fc0c546a14610099575b600080fd5b6100596100a1565b604080516001600160a01b039092168252519081900360200190f35b61007d6100b0565b005b6100876101cd565b60408051918252519081900360200190f35b6100596101d3565b6001546001600160a01b031690565b6002544210156100f15760405162461bcd60e51b81526004018080602001828103825260328152602001806103fe6032913960400191505060405180910390fd5b60008054604080516370a0823160e01b815230600482015290516001600160a01b03909216916370a0823191602480820192602092909190829003018186803b15801561013d57600080fd5b505afa158015610151573d6000803e3d6000fd5b505050506040513d602081101561016757600080fd5b50519050806101a75760405162461bcd60e51b815260040180806020018281038252602381526020018061045a6023913960400191505060405180910390fd5b6001546000546101ca916001600160a01b0391821691168363ffffffff6101e216565b50565b60025490565b6000546001600160a01b031690565b604080516001600160a01b038416602482015260448082018490528251808303909101815260649091019091526020810180516001600160e01b031663a9059cbb60e01b179052610234908490610239565b505050565b61024b826001600160a01b03166103f7565b61029c576040805162461bcd60e51b815260206004820152601f60248201527f5361666545524332303a2063616c6c20746f206e6f6e2d636f6e747261637400604482015290519081900360640190fd5b60006060836001600160a01b0316836040518082805190602001908083835b602083106102da5780518252601f1990920191602091820191016102bb565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d806000811461033c576040519150601f19603f3d011682016040523d82523d6000602084013e610341565b606091505b509150915081610398576040805162461bcd60e51b815260206004820181905260248201527f5361666545524332303a206c6f772d6c6576656c2063616c6c206661696c6564604482015290519081900360640190fd5b8051156103f1578080602001905160208110156103b457600080fd5b50516103f15760405162461bcd60e51b815260040180806020018281038252602a815260200180610430602a913960400191505060405180910390fd5b50505050565b3b15159056fe546f6b656e54696d656c6f636b3a2063757272656e742074696d65206973206265666f72652072656c656173652074696d655361666545524332303a204552433230206f7065726174696f6e20646964206e6f742073756363656564546f6b656e54696d656c6f636b3a206e6f20746f6b656e7320746f2072656c65617365a265627a7a72305820d443b2e28cdf0d372cd7d0433eba00950ba069ab1817cd05e33897c7066009d564736f6c63430005090032546f6b656e54696d656c6f636b3a2072656c656173652074696d65206973206265666f72652063757272656e742074696d65";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_RELEASE = "release";

    public static final String FUNC_RELEASETIME = "releaseTime";

    public static final String FUNC_TOKEN = "token";

    protected TokenTimelock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected TokenTimelock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> release() {
        final Function function = new Function(
                FUNC_RELEASE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> releaseTime() {
        final Function function = new Function(FUNC_RELEASETIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> token() {
        final Function function = new Function(FUNC_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static TokenTimelock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new TokenTimelock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static TokenTimelock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TokenTimelock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TokenTimelock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String token, String beneficiary, BigInteger releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.Address(beneficiary), 
                new org.web3j.abi.datatypes.generated.Uint256(releaseTime)));
        return deployRemoteCall(TokenTimelock.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TokenTimelock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String token, String beneficiary, BigInteger releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token), 
                new org.web3j.abi.datatypes.Address(beneficiary), 
                new org.web3j.abi.datatypes.generated.Uint256(releaseTime)));
        return deployRemoteCall(TokenTimelock.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }
}
