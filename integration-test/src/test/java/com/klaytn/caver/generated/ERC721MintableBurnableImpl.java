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
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class ERC721MintableBurnableImpl extends SmartContract {
    private static final String BINARY = "60806040523480156200001157600080fd5b506040518060400160405280600481526020017f54657374000000000000000000000000000000000000000000000000000000008152506040518060400160405280600481526020017f54455354000000000000000000000000000000000000000000000000000000008152508181620000986301ffc9a760e01b6200017b60201b60201c565b620000cc7f80ac58cd000000000000000000000000000000000000000000000000000000006001600160e01b036200017b16565b620001007f780e9d63000000000000000000000000000000000000000000000000000000006001600160e01b036200017b16565b815162000115906009906020850190620003c6565b5080516200012b90600a906020840190620003c6565b50620001607f5b5e139f000000000000000000000000000000000000000000000000000000006001600160e01b036200017b16565b5050505062000175336200024a60201b60201c565b6200046b565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156200020d57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b6200026581600c6200029c60201b620016461790919060201c565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b620002b182826001600160e01b036200034316565b156200031e57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b038216620003a6576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620022656022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200040957805160ff191683800117855562000439565b8280016001018555821562000439579182015b82811115620004395782518255916020019190600101906200041c565b50620004479291506200044b565b5090565b6200046891905b8082111562000447576000815560010162000452565b90565b611dea806200047b6000396000f3fe608060405234801561001057600080fd5b50600436106101425760003560e01c806350bb4e7f116100b8578063986502751161007c57806398650275146104aa578063a22cb465146104b2578063aa271e1a146104e0578063b88d4fde14610506578063c87b56dd146105cc578063e985e9c5146105e957610142565b806350bb4e7f1461037e5780636352211e1461043957806370a082311461045657806395d89b411461047c578063983b2d561461048457610142565b806323b872dd1161010a57806323b872dd146102805780632f745c59146102b657806340c10f19146102e257806342842e0e1461030e57806342966c68146103445780634f6ccce71461036157610142565b806301ffc9a71461014757806306fdde0314610182578063081812fc146101ff578063095ea7b31461023857806318160ddd14610266575b600080fd5b61016e6004803603602081101561015d57600080fd5b50356001600160e01b031916610617565b604080519115158252519081900360200190f35b61018a610636565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101c45781810151838201526020016101ac565b50505050905090810190601f1680156101f15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61021c6004803603602081101561021557600080fd5b50356106cd565b604080516001600160a01b039092168252519081900360200190f35b6102646004803603604081101561024e57600080fd5b506001600160a01b03813516906020013561072f565b005b61026e610840565b60408051918252519081900360200190f35b6102646004803603606081101561029657600080fd5b506001600160a01b03813581169160208101359091169060400135610846565b61026e600480360360408110156102cc57600080fd5b506001600160a01b03813516906020013561089b565b61016e600480360360408110156102f857600080fd5b506001600160a01b03813516906020013561091a565b6102646004803603606081101561032457600080fd5b506001600160a01b03813581169160208101359091169060400135610973565b6102646004803603602081101561035a57600080fd5b503561098e565b61026e6004803603602081101561037757600080fd5b50356109df565b61016e6004803603606081101561039457600080fd5b6001600160a01b03823516916020810135918101906060810160408201356401000000008111156103c457600080fd5b8201836020820111156103d657600080fd5b803590602001918460018302840111640100000000831117156103f857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610a45945050505050565b61021c6004803603602081101561044f57600080fd5b5035610aa9565b61026e6004803603602081101561046c57600080fd5b50356001600160a01b0316610b03565b61018a610b6b565b6102646004803603602081101561049a57600080fd5b50356001600160a01b0316610bcc565b610264610c19565b610264600480360360408110156104c857600080fd5b506001600160a01b0381351690602001351515610c24565b61016e600480360360208110156104f657600080fd5b50356001600160a01b0316610cf0565b6102646004803603608081101561051c57600080fd5b6001600160a01b0382358116926020810135909116916040820135919081019060808101606082013564010000000081111561055757600080fd5b82018360208201111561056957600080fd5b8035906020019184600183028401116401000000008311171561058b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610d03945050505050565b61018a600480360360208110156105e257600080fd5b5035610d5b565b61016e600480360360408110156105ff57600080fd5b506001600160a01b0381358116916020013516610e40565b6001600160e01b03191660009081526020819052604090205460ff1690565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106c25780601f10610697576101008083540402835291602001916106c2565b820191906000526020600020905b8154815290600101906020018083116106a557829003601f168201915b505050505090505b90565b60006106d882610e6e565b6107135760405162461bcd60e51b815260040180806020018281038252602c815260200180611c11602c913960400191505060405180910390fd5b506000908152600260205260409020546001600160a01b031690565b600061073a82610aa9565b9050806001600160a01b0316836001600160a01b0316141561078d5760405162461bcd60e51b8152600401808060200182810382526021815260200180611ce36021913960400191505060405180910390fd5b336001600160a01b03821614806107a957506107a98133610e40565b6107e45760405162461bcd60e51b8152600401808060200182810382526038815260200180611b356038913960400191505060405180910390fd5b60008281526002602052604080822080546001600160a01b0319166001600160a01b0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b60075490565b6108503382610e8b565b61088b5760405162461bcd60e51b8152600401808060200182810382526031815260200180611d046031913960400191505060405180910390fd5b610896838383610f2f565b505050565b60006108a683610b03565b82106108e35760405162461bcd60e51b815260040180806020018281038252602b815260200180611a88602b913960400191505060405180910390fd5b6001600160a01b038316600090815260056020526040902080548390811061090757fe5b9060005260206000200154905092915050565b600061092533610cf0565b6109605760405162461bcd60e51b8152600401808060200182810382526030815260200180611bc06030913960400191505060405180910390fd5b61096a8383610f4e565b50600192915050565b61089683838360405180602001604052806000815250610d03565b6109983382610e8b565b6109d35760405162461bcd60e51b8152600401808060200182810382526030815260200180611d866030913960400191505060405180910390fd5b6109dc81610f6f565b50565b60006109e9610840565b8210610a265760405162461bcd60e51b815260040180806020018281038252602c815260200180611d35602c913960400191505060405180910390fd5b60078281548110610a3357fe5b90600052602060002001549050919050565b6000610a5033610cf0565b610a8b5760405162461bcd60e51b8152600401808060200182810382526030815260200180611bc06030913960400191505060405180910390fd5b610a958484610f4e565b610a9f8383610f81565b5060019392505050565b6000818152600160205260408120546001600160a01b031680610afd5760405162461bcd60e51b8152600401808060200182810382526029815260200180611b976029913960400191505060405180910390fd5b92915050565b60006001600160a01b038216610b4a5760405162461bcd60e51b815260040180806020018281038252602a815260200180611b6d602a913960400191505060405180910390fd5b6001600160a01b0382166000908152600360205260409020610afd90610fe4565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106c25780601f10610697576101008083540402835291602001916106c2565b610bd533610cf0565b610c105760405162461bcd60e51b8152600401808060200182810382526030815260200180611bc06030913960400191505060405180910390fd5b6109dc81610fe8565b610c2233611030565b565b6001600160a01b038216331415610c82576040805162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c657200000000000000604482015290519081900360640190fd5b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b6000610afd600c8363ffffffff61107816565b610d0e848484610846565b610d1a848484846110df565b610d555760405162461bcd60e51b8152600401808060200182810382526032815260200180611ab36032913960400191505060405180910390fd5b50505050565b6060610d6682610e6e565b610da15760405162461bcd60e51b815260040180806020018281038252602f815260200180611cb4602f913960400191505060405180910390fd5b6000828152600b602090815260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610e345780601f10610e0957610100808354040283529160200191610e34565b820191906000526020600020905b815481529060010190602001808311610e1757829003601f168201915b50505050509050919050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b6000908152600160205260409020546001600160a01b0316151590565b6000610e9682610e6e565b610ed15760405162461bcd60e51b815260040180806020018281038252602c815260200180611b09602c913960400191505060405180910390fd5b6000610edc83610aa9565b9050806001600160a01b0316846001600160a01b03161480610f175750836001600160a01b0316610f0c846106cd565b6001600160a01b0316145b80610f275750610f278185610e40565b949350505050565b610f3a838383611212565b610f448382611356565b610896828261144b565b610f588282611489565b610f62828261144b565b610f6b816115ba565b5050565b6109dc610f7b82610aa9565b826115fe565b610f8a82610e6e565b610fc55760405162461bcd60e51b815260040180806020018281038252602c815260200180611c3d602c913960400191505060405180910390fd5b6000828152600b6020908152604090912082516108969284019061198b565b5490565b610ff9600c8263ffffffff61164616565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b611041600c8263ffffffff6116c716565b6040516001600160a01b038216907fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669290600090a250565b60006001600160a01b0382166110bf5760405162461bcd60e51b8152600401808060200182810382526022815260200180611c696022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b60006110f3846001600160a01b031661172e565b6110ff57506001610f27565b604051630a85bd0160e11b815233600482018181526001600160a01b03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b83811015611179578181015183820152602001611161565b50505050905090810190601f1680156111a65780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b1580156111c857600080fd5b505af11580156111dc573d6000803e3d6000fd5b505050506040513d60208110156111f257600080fd5b50516001600160e01b031916630a85bd0160e11b14915050949350505050565b826001600160a01b031661122582610aa9565b6001600160a01b03161461126a5760405162461bcd60e51b8152600401808060200182810382526029815260200180611c8b6029913960400191505060405180910390fd5b6001600160a01b0382166112af5760405162461bcd60e51b8152600401808060200182810382526024815260200180611ae56024913960400191505060405180910390fd5b6112b881611734565b6001600160a01b03831660009081526003602052604090206112d99061176f565b6001600160a01b03821660009081526003602052604090206112fa90611786565b60008181526001602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b6001600160a01b03821660009081526005602052604081205461138090600163ffffffff61178f16565b60008381526006602052604090205490915080821461141b576001600160a01b03841660009081526005602052604081208054849081106113bd57fe5b906000526020600020015490508060056000876001600160a01b03166001600160a01b0316815260200190815260200160002083815481106113fb57fe5b600091825260208083209091019290925591825260069052604090208190555b6001600160a01b0384166000908152600560205260409020805490611444906000198301611a09565b5050505050565b6001600160a01b0390911660009081526005602081815260408084208054868652600684529185208290559282526001810183559183529091200155565b6001600160a01b0382166114e4576040805162461bcd60e51b815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f2061646472657373604482015290519081900360640190fd5b6114ed81610e6e565b1561153f576040805162461bcd60e51b815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e74656400000000604482015290519081900360640190fd5b600081815260016020908152604080832080546001600160a01b0319166001600160a01b03871690811790915583526003909152902061157e90611786565b60405181906001600160a01b038416906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b600780546000838152600860205260408120829055600182018355919091527fa66cc928b5edb82af9bd49922954155ab7b0942694bea4ce44661d9a8736c6880155565b61160882826117ec565b6000818152600b60205260409020546002600019610100600184161502019091160415610f6b576000818152600b60205260408120610f6b91611a2d565b6116508282611078565b156116a2576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b6116d18282611078565b61170c5760405162461bcd60e51b8152600401808060200182810382526021815260200180611bf06021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b3b151590565b6000818152600260205260409020546001600160a01b0316156109dc57600090815260026020526040902080546001600160a01b0319169055565b805461178290600163ffffffff61178f16565b9055565b80546001019055565b6000828211156117e6576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6117f68282611818565b6118008282611356565b600081815260066020526040812055610f6b816118ef565b816001600160a01b031661182b82610aa9565b6001600160a01b0316146118705760405162461bcd60e51b8152600401808060200182810382526025815260200180611d616025913960400191505060405180910390fd5b61187981611734565b6001600160a01b038216600090815260036020526040902061189a9061176f565b60008181526001602052604080822080546001600160a01b0319169055518291906001600160a01b038516907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908390a45050565b60075460009061190690600163ffffffff61178f16565b6000838152600860205260408120546007805493945090928490811061192857fe5b90600052602060002001549050806007838154811061194357fe5b60009182526020808320909101929092558281526008909152604090208290556007805490611976906000198301611a09565b50505060009182525060086020526040812055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106119cc57805160ff19168380011785556119f9565b828001600101855582156119f9579182015b828111156119f95782518255916020019190600101906119de565b50611a05929150611a6d565b5090565b81548183558181111561089657600083815260209020610896918101908301611a6d565b50805460018160011615610100020316600290046000825580601f10611a5357506109dc565b601f0160209004906000526020600020908101906109dc91905b6106ca91905b80821115611a055760008155600101611a7356fe455243373231456e756d657261626c653a206f776e657220696e646578206f7574206f6620626f756e64734552433732313a207472616e7366657220746f206e6f6e20455243373231526563656976657220696d706c656d656e7465724552433732313a207472616e7366657220746f20746865207a65726f20616464726573734552433732313a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4552433732313a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734552433732313a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654552433732313a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732314d657461646174613a2055524920736574206f66206e6f6e6578697374656e7420746f6b656e526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734552433732313a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4552433732314d657461646174613a2055524920717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76616c20746f2063757272656e74206f776e65724552433732313a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564455243373231456e756d657261626c653a20676c6f62616c20696e646578206f7574206f6620626f756e64734552433732313a206275726e206f6620746f6b656e2074686174206973206e6f74206f776e4552433732314275726e61626c653a2063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564a265627a7a7230582083c3cb74f018a9174e0c6a99f62e178bbec622e929bdf71a7f1d423b85cd84f464736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_MINTWITHTOKENURI = "mintWithTokenURI";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_ADDMINTER = "addMinter";

    public static final String FUNC_RENOUNCEMINTER = "renounceMinter";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_ISMINTER = "isMinter";

    public static final String FUNC_TOKENURI = "tokenURI";

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

    protected ERC721MintableBurnableImpl(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721MintableBurnableImpl(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> burn(BigInteger tokenId) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> tokenByIndex(BigInteger index) {
        final Function function = new Function(FUNC_TOKENBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> mintWithTokenURI(String to, BigInteger tokenId, String tokenURI) {
        final Function function = new Function(
                FUNC_MINTWITHTOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.Utf8String(tokenURI)), 
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

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public static ERC721MintableBurnableImpl load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721MintableBurnableImpl(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721MintableBurnableImpl load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721MintableBurnableImpl(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721MintableBurnableImpl> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721MintableBurnableImpl.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC721MintableBurnableImpl> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC721MintableBurnableImpl.class, caver, transactionManager, contractGasProvider, BINARY, "");
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
