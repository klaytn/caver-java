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
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC777 extends SmartContract {
    private static final String BINARY = "6080604052600080546001600160a01b031916731820a4b7618bde71dce8cdc73aab6c95905fad241790553480156200003757600080fd5b5060405162001e9e38038062001e9e833981810160405260608110156200005d57600080fd5b8101908080516401000000008111156200007657600080fd5b820160208101848111156200008a57600080fd5b8151640100000000811182820187101715620000a557600080fd5b50509291906020018051640100000000811115620000c257600080fd5b82016020810184811115620000d657600080fd5b8151640100000000811182820187101715620000f157600080fd5b505092919060200180516401000000008111156200010e57600080fd5b820160208101848111156200012257600080fd5b81518560208202830111640100000000821117156200014057600080fd5b505085519093506200015c925060039150602086019062000362565b5081516200017290600490602085019062000362565b50805162000188906005906020840190620003e7565b5060005b600554811015620001e85760016006600060058481548110620001ab57fe5b6000918252602080832091909101546001600160a01b031683528201929092526040019020805460ff19169115159190911790556001016200018c565b5060008054604080517f455243373737546f6b656e0000000000000000000000000000000000000000008152815190819003600b0181207f29965a1d00000000000000000000000000000000000000000000000000000000825230600483018190526024830191909152604482015290516001600160a01b03909216926329965a1d9260648084019382900301818387803b1580156200028757600080fd5b505af11580156200029c573d6000803e3d6000fd5b505060008054604080517f4552433230546f6b656e000000000000000000000000000000000000000000008152815190819003600a0181207f29965a1d00000000000000000000000000000000000000000000000000000000825230600483018190526024830191909152604482015290516001600160a01b0390921694506329965a1d9350606480820193929182900301818387803b1580156200034057600080fd5b505af115801562000355573d6000803e3d6000fd5b5050505050505062000494565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620003a557805160ff1916838001178555620003d5565b82800160010185558215620003d5579182015b82811115620003d5578251825591602001919060010190620003b8565b50620003e39291506200044d565b5090565b8280548282559060005260206000209081019282156200043f579160200282015b828111156200043f57825182546001600160a01b0319166001600160a01b0390911617825560209092019160019091019062000408565b50620003e39291506200046d565b6200046a91905b80821115620003e3576000815560010162000454565b90565b6200046a91905b80821115620003e35780546001600160a01b031916815560010162000474565b6119fa80620004a46000396000f3fe608060405234801561001057600080fd5b50600436106101165760003560e01c8063959b8c3f116100a2578063d95b637111610071578063d95b637114610489578063dd62ed3e146104b7578063fad8b32a146104e5578063fc673c4f1461050b578063fe9d9303146105de57610116565b8063959b8c3f146103ac57806395d89b41146103d25780639bd9bbc6146103da578063a9059cbb1461045d57610116565b806323b872dd116100e957806323b872dd1461024a578063313ce56714610280578063556f0dc71461029e57806362ad1b83146102a657806370a082311461038657610116565b806306e485381461011b57806306fdde0314610173578063095ea7b3146101f057806318160ddd14610230575b600080fd5b610123610653565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561015f578181015183820152602001610147565b505050509050019250505060405180910390f35b61017b6106b5565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101b557818101518382015260200161019d565b50505050905090810190601f1680156101e25780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61021c6004803603604081101561020657600080fd5b506001600160a01b038135169060200135610742565b604080519115158252519081900360200190f35b610238610758565b60408051918252519081900360200190f35b61021c6004803603606081101561026057600080fd5b506001600160a01b0381358116916020810135909116906040013561075e565b6102886107b6565b6040805160ff9092168252519081900360200190f35b6102386107bb565b610384600480360360a08110156102bc57600080fd5b6001600160a01b03823581169260208101359091169160408201359190810190608081016060820135600160201b8111156102f657600080fd5b82018360208201111561030857600080fd5b803590602001918460018302840111600160201b8311171561032957600080fd5b919390929091602081019035600160201b81111561034657600080fd5b82018360208201111561035857600080fd5b803590602001918460018302840111600160201b8311171561037957600080fd5b5090925090506107c0565b005b6102386004803603602081101561039c57600080fd5b50356001600160a01b0316610885565b610384600480360360208110156103c257600080fd5b50356001600160a01b03166108a0565b61017b6109a1565b610384600480360360608110156103f057600080fd5b6001600160a01b0382351691602081013591810190606081016040820135600160201b81111561041f57600080fd5b82018360208201111561043157600080fd5b803590602001918460018302840111600160201b8311171561045257600080fd5b509092509050610a02565b61021c6004803603604081101561047357600080fd5b506001600160a01b038135169060200135610a59565b61021c6004803603604081101561049f57600080fd5b506001600160a01b0381358116916020013516610a67565b610238600480360360408110156104cd57600080fd5b506001600160a01b0381358116916020013516610b09565b610384600480360360208110156104fb57600080fd5b50356001600160a01b0316610b34565b6103846004803603608081101561052157600080fd5b6001600160a01b0382351691602081013591810190606081016040820135600160201b81111561055057600080fd5b82018360208201111561056257600080fd5b803590602001918460018302840111600160201b8311171561058357600080fd5b919390929091602081019035600160201b8111156105a057600080fd5b8201836020820111156105b257600080fd5b803590602001918460018302840111600160201b831117156105d357600080fd5b509092509050610c35565b610384600480360360408110156105f457600080fd5b81359190810190604081016020820135600160201b81111561061557600080fd5b82018360208201111561062757600080fd5b803590602001918460018302840111600160201b8311171561064857600080fd5b509092509050610cf8565b606060058054806020026020016040519081016040528092919081815260200182805480156106ab57602002820191906000526020600020905b81546001600160a01b0316815260019091019060200180831161068d575b5050505050905090565b60038054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106ab5780601f10610716576101008083540402835291602001916106ab565b820191906000526020600020905b81548152906001019060200180831161072457509395945050505050565b600061074f338484610d4d565b50600192915050565b60025490565b600061076c33858585610df4565b6001600160a01b0384166000908152600960209081526040808320338085529252909120546107ac9186916107a7908663ffffffff610e2016565b610d4d565b5060019392505050565b601290565b600190565b6107ca3388610a67565b6108055760405162461bcd60e51b815260040180806020018281038252602c815260200180611951602c913960400191505060405180910390fd5b61087c3388888888888080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525050604080516020601f8c018190048102820181019092528a815292508a9150899081908401838280828437600092019190915250610e7d92505050565b50505050505050565b6001600160a01b031660009081526001602052604090205490565b336001600160a01b03821614156108e85760405162461bcd60e51b815260040180806020018281038252602481526020018061189b6024913960400191505060405180910390fd5b6001600160a01b03811660009081526006602052604090205460ff1615610939573360009081526008602090815260408083206001600160a01b03851684529091529020805460ff19169055610968565b3360009081526007602090815260408083206001600160a01b03851684529091529020805460ff191660011790555b60405133906001600160a01b038316907ff4caeb2d6ca8932a215a353d0703c326ec2d81fc68170f320eb2ab49e9df61f990600090a350565b60048054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106ab5780601f10610716576101008083540402835291602001916106ab565b610a533333868686868080601f016020809104026020016040519081016040528093929190818152602001838380828437600092018290525060408051602081019091529081529250610e7d915050565b50505050565b600061074f33338585610df4565b6000816001600160a01b0316836001600160a01b03161480610ad257506001600160a01b03831660009081526006602052604090205460ff168015610ad257506001600160a01b0380831660009081526008602090815260408083209387168352929052205460ff16155b80610b0257506001600160a01b0380831660009081526007602090815260408083209387168352929052205460ff165b9392505050565b6001600160a01b03918216600090815260096020908152604080832093909416825291909152205490565b6001600160a01b038116331415610b7c5760405162461bcd60e51b81526004018080602001828103825260218152602001806118bf6021913960400191505060405180910390fd5b6001600160a01b03811660009081526006602052604090205460ff1615610bd0573360009081526008602090815260408083206001600160a01b03851684529091529020805460ff19166001179055610bfc565b3360009081526007602090815260408083206001600160a01b03851684529091529020805460ff191690555b60405133906001600160a01b038316907f50546e66e5f44d728365dc3908c63bc5cfeeab470722c1677e3073a6ac294aa190600090a350565b610c3f3387610a67565b610c7a5760405162461bcd60e51b815260040180806020018281038252602c815260200180611951602c913960400191505060405180910390fd5b610cf033878787878080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525050604080516020601f8b018190048102820181019092528981529250899150889081908401838280828437600092019190915250610e8d92505050565b505050505050565b610d4833338585858080601f016020809104026020016040519081016040528093929190818152602001838380828437600092018290525060408051602081019091529081529250610e8d915050565b505050565b6001600160a01b038216610d925760405162461bcd60e51b81526004018080602001828103825260238152602001806119a36023913960400191505060405180910390fd5b6001600160a01b03808416600081815260096020908152604080832094871680845294825291829020859055815185815291517f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259281900390910190a3505050565b610a538484848460405180602001604052806000815250604051806020016040528060008152506110ad565b600082821115610e77576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b610cf086868686868660016110b9565b6001600160a01b038416610ed25760405162461bcd60e51b81526004018080602001828103825260228152602001806118796022913960400191505060405180910390fd5b610ee185856000868686611351565b600254610ef4908463ffffffff610e2016565b6002556001600160a01b038416600090815260016020526040902054610f20908463ffffffff610e2016565b60016000866001600160a01b03166001600160a01b0316815260200190815260200160002081905550836001600160a01b0316856001600160a01b03167fa78a9be3a7b862d26933ad85fb11d80ef66b8f972d7cbba06621d583943a4098858585604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610fc8578181015183820152602001610fb0565b50505050905090810190601f168015610ff55780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b83811015611028578181015183820152602001611010565b50505050905090810190601f1680156110555780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a36040805184815290516000916001600160a01b038716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9181900360200190a35050505050565b610cf086868686868660005b6001600160a01b0386166110fe5760405162461bcd60e51b815260040180806020018281038252602681526020018061197d6026913960400191505060405180910390fd5b6001600160a01b0385166111435760405162461bcd60e51b815260040180806020018281038252602481526020018061192d6024913960400191505060405180910390fd5b611151878787878787611351565b6001600160a01b03861660009081526001602052604090205461117a908563ffffffff610e2016565b6001600160a01b0380881660009081526001602052604080822093909355908716815220546111af908563ffffffff61158816565b6001600160a01b0386166000908152600160205260409020556111d7878787878787876115e2565b846001600160a01b0316866001600160a01b0316886001600160a01b03167f06b541ddaa720db2b10a4d0cdac39b8d360425fc073085fac19bc82614677987878787604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015611260578181015183820152602001611248565b50505050905090810190601f16801561128d5780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156112c05781810151838201526020016112a8565b50505050905090810190601f1680156112ed5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a4846001600160a01b0316866001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef866040518082815260200191505060405180910390a350505050505050565b600080546040805163555ddc6560e11b81526001600160a01b0389811660048301527f29ddb589b1fb5fc7cf394961c1adf5f8c6454761adf795e67fe149f658abe89560248301529151919092169163aabbb8ca916044808301926020929190829003018186803b1580156113c557600080fd5b505afa1580156113d9573d6000803e3d6000fd5b505050506040513d60208110156113ef57600080fd5b505190506001600160a01b0381161561087c57806001600160a01b03166375ab97828888888888886040518763ffffffff1660e01b815260040180876001600160a01b03166001600160a01b03168152602001866001600160a01b03166001600160a01b03168152602001856001600160a01b03166001600160a01b031681526020018481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156114b557818101518382015260200161149d565b50505050905090810190601f1680156114e25780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156115155781810151838201526020016114fd565b50505050905090810190601f1680156115425780820380516001836020036101000a031916815260200191505b5098505050505050505050600060405180830381600087803b15801561156757600080fd5b505af115801561157b573d6000803e3d6000fd5b5050505050505050505050565b600082820183811015610b02576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b600080546040805163555ddc6560e11b81526001600160a01b0389811660048301527fb281fc8c12954d22544db45de3159a39272895b169a852b314f9cc762e44c53b60248301529151919092169163aabbb8ca916044808301926020929190829003018186803b15801561165657600080fd5b505afa15801561166a573d6000803e3d6000fd5b505050506040513d602081101561168057600080fd5b505190506001600160a01b0381161561181457806001600160a01b03166223de298989898989896040518763ffffffff1660e01b815260040180876001600160a01b03166001600160a01b03168152602001866001600160a01b03166001600160a01b03168152602001856001600160a01b03166001600160a01b031681526020018481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561174557818101518382015260200161172d565b50505050905090810190601f1680156117725780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156117a557818101518382015260200161178d565b50505050905090810190601f1680156117d25780820380516001836020036101000a031916815260200191505b5098505050505050505050600060405180830381600087803b1580156117f757600080fd5b505af115801561180b573d6000803e3d6000fd5b50505050611868565b81156118685761182c866001600160a01b0316611872565b156118685760405162461bcd60e51b815260040180806020018281038252604d8152602001806118e0604d913960600191505060405180910390fd5b5050505050505050565b3b15159056fe4552433737373a206275726e2066726f6d20746865207a65726f20616464726573734552433737373a20617574686f72697a696e672073656c66206173206f70657261746f724552433737373a207265766f6b696e672073656c66206173206f70657261746f724552433737373a20746f6b656e20726563697069656e7420636f6e747261637420686173206e6f20696d706c656d656e74657220666f7220455243373737546f6b656e73526563697069656e744552433737373a207472616e7366657220746f20746865207a65726f20616464726573734552433737373a2063616c6c6572206973206e6f7420616e206f70657261746f7220666f7220686f6c6465724552433737373a207472616e736665722066726f6d20746865207a65726f20616464726573734552433737373a20617070726f766520746f20746865207a65726f2061646472657373a265627a7a723058202c6f009a2b61f12c17f90e88acbf388bb556171f294f445976cae478445b522764736f6c63430005090032";

    public static final String FUNC_DEFAULTOPERATORS = "defaultOperators";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_GRANULARITY = "granularity";

    public static final String FUNC_OPERATORSEND = "operatorSend";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_AUTHORIZEOPERATOR = "authorizeOperator";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_SEND = "send";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ISOPERATORFOR = "isOperatorFor";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_REVOKEOPERATOR = "revokeOperator";

    public static final String FUNC_OPERATORBURN = "operatorBurn";

    public static final String FUNC_BURN = "burn";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SENT_EVENT = new Event("Sent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event MINTED_EVENT = new Event("Minted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event BURNED_EVENT = new Event("Burned", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event AUTHORIZEDOPERATOR_EVENT = new Event("AuthorizedOperator", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event REVOKEDOPERATOR_EVENT = new Event("RevokedOperator", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected ERC777(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC777(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<List> defaultOperators() {
        final Function function = new Function(FUNC_DEFAULTOPERATORS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> granularity() {
        final Function function = new Function(FUNC_GRANULARITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> operatorSend(String from, String to, BigInteger amount, byte[] data, byte[] operatorData) {
        final Function function = new Function(
                FUNC_OPERATORSEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.DynamicBytes(operatorData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String tokenHolder) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(tokenHolder)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> authorizeOperator(String operator) {
        final Function function = new Function(
                FUNC_AUTHORIZEOPERATOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> send(String to, BigInteger amount, byte[] data) {
        final Function function = new Function(
                FUNC_SEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
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

    public RemoteCall<Boolean> isOperatorFor(String operator, String tokenHolder) {
        final Function function = new Function(FUNC_ISOPERATORFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator), 
                new org.web3j.abi.datatypes.Address(tokenHolder)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner), 
                new org.web3j.abi.datatypes.Address(spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> revokeOperator(String operator) {
        final Function function = new Function(
                FUNC_REVOKEOPERATOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(operator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> operatorBurn(String from, BigInteger amount, byte[] data, byte[] operatorData) {
        final Function function = new Function(
                FUNC_OPERATORBURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.DynamicBytes(operatorData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> burn(BigInteger amount, byte[] data) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public List<SentEventResponse> getSentEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(SENT_EVENT, transactionReceipt);
        ArrayList<SentEventResponse> responses = new ArrayList<SentEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            SentEventResponse typedResponse = new SentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.operatorData = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<MintedEventResponse> getMintedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(MINTED_EVENT, transactionReceipt);
        ArrayList<MintedEventResponse> responses = new ArrayList<MintedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            MintedEventResponse typedResponse = new MintedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.operatorData = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<BurnedEventResponse> getBurnedEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(BURNED_EVENT, transactionReceipt);
        ArrayList<BurnedEventResponse> responses = new ArrayList<BurnedEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            BurnedEventResponse typedResponse = new BurnedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.operatorData = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<AuthorizedOperatorEventResponse> getAuthorizedOperatorEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUTHORIZEDOPERATOR_EVENT, transactionReceipt);
        ArrayList<AuthorizedOperatorEventResponse> responses = new ArrayList<AuthorizedOperatorEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            AuthorizedOperatorEventResponse typedResponse = new AuthorizedOperatorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.tokenHolder = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<RevokedOperatorEventResponse> getRevokedOperatorEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(REVOKEDOPERATOR_EVENT, transactionReceipt);
        ArrayList<RevokedOperatorEventResponse> responses = new ArrayList<RevokedOperatorEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            RevokedOperatorEventResponse typedResponse = new RevokedOperatorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.tokenHolder = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ERC777 load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC777(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC777 load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC777(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC777> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String name, String symbol, List<String> defaultOperators) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(defaultOperators, org.web3j.abi.datatypes.Address.class))));
        return deployRemoteCall(ERC777.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC777> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name, String symbol, List<String> defaultOperators) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(defaultOperators, org.web3j.abi.datatypes.Address.class))));
        return deployRemoteCall(ERC777.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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

    public static class SentEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String from;

        public String to;

        public BigInteger amount;

        public byte[] data;

        public byte[] operatorData;
    }

    public static class MintedEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String to;

        public BigInteger amount;

        public byte[] data;

        public byte[] operatorData;
    }

    public static class BurnedEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String from;

        public BigInteger amount;

        public byte[] data;

        public byte[] operatorData;
    }

    public static class AuthorizedOperatorEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String tokenHolder;
    }

    public static class RevokedOperatorEventResponse {
        public KlayLogs.Log log;

        public String operator;

        public String tokenHolder;
    }
}
