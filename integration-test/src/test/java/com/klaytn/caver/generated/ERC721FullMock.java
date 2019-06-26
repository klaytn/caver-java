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
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
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
public class ERC721FullMock extends SmartContract {
    private static final String BINARY = "60806040523480156200001157600080fd5b50604051620024f5380380620024f5833981810160405260408110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b820160208101848111156200006457600080fd5b81516401000000008111828201871017156200007f57600080fd5b505092919060200180516401000000008111156200009c57600080fd5b82016020810184811115620000b057600080fd5b8151640100000000811182820187101715620000cb57600080fd5b50909350849250839150829050816200010d7f01ffc9a7000000000000000000000000000000000000000000000000000000006001600160e01b03620001f216565b620001417f80ac58cd000000000000000000000000000000000000000000000000000000006001600160e01b03620001f216565b620001757f780e9d63000000000000000000000000000000000000000000000000000000006001600160e01b03620001f216565b81516200018a9060099060208501906200043d565b508051620001a090600a9060208401906200043d565b50620001d57f5b5e139f000000000000000000000000000000000000000000000000000000006001600160e01b03620001f216565b50505050620001ea33620002c160201b60201c565b5050620004e2565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156200028457604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f4552433136353a20696e76616c696420696e7465726661636520696400000000604482015290519081900360640190fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b620002dc81600c6200031360201b6200183d1790919060201c565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b6200032882826001600160e01b03620003ba16565b156200039557604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b60006001600160a01b0382166200041d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620024d36022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200048057805160ff1916838001178555620004b0565b82800160010185558215620004b0579182015b82811115620004b057825182559160200191906001019062000493565b50620004be929150620004c2565b5090565b620004df91905b80821115620004be5760008155600101620004c9565b90565b611fe180620004f26000396000f3fe608060405234801561001057600080fd5b50600436106101735760003560e01c80634f6ccce7116100de578063983b2d5611610097578063aa271e1a11610071578063aa271e1a14610651578063b88d4fde14610677578063c87b56dd1461073d578063e985e9c51461075a57610173565b8063983b2d56146105f5578063986502751461061b578063a22cb4651461062357610173565b80634f6ccce71461045c57806350bb4e7f146104795780636352211e1461053457806370a08231146105515780638462151c1461057757806395d89b41146105ed57610173565b806323b872dd1161013057806323b872dd1461035e5780632f745c591461039457806340c10f19146103c057806342842e0e146103ec57806342966c68146104225780634f558e791461043f57610173565b806301ffc9a71461017857806306fdde03146101b3578063081812fc14610230578063095ea7b314610269578063162094c41461029757806318160ddd14610344575b600080fd5b61019f6004803603602081101561018e57600080fd5b50356001600160e01b031916610788565b604080519115158252519081900360200190f35b6101bb6107a7565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101f55781810151838201526020016101dd565b50505050905090810190601f1680156102225780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61024d6004803603602081101561024657600080fd5b503561083e565b604080516001600160a01b039092168252519081900360200190f35b6102956004803603604081101561027f57600080fd5b506001600160a01b0381351690602001356108a0565b005b610295600480360360408110156102ad57600080fd5b813591908101906040810160208201356401000000008111156102cf57600080fd5b8201836020820111156102e157600080fd5b8035906020019184600183028401116401000000008311171561030357600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506109b1945050505050565b61034c6109bf565b60408051918252519081900360200190f35b6102956004803603606081101561037457600080fd5b506001600160a01b038135811691602081013590911690604001356109c5565b61034c600480360360408110156103aa57600080fd5b506001600160a01b038135169060200135610a1a565b61019f600480360360408110156103d657600080fd5b506001600160a01b038135169060200135610a99565b6102956004803603606081101561040257600080fd5b506001600160a01b03813581169160208101359091169060400135610af2565b6102956004803603602081101561043857600080fd5b5035610b0d565b61019f6004803603602081101561045557600080fd5b5035610b5e565b61034c6004803603602081101561047257600080fd5b5035610b6f565b61019f6004803603606081101561048f57600080fd5b6001600160a01b03823516916020810135918101906060810160408201356401000000008111156104bf57600080fd5b8201836020820111156104d157600080fd5b803590602001918460018302840111640100000000831117156104f357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610bd5945050505050565b61024d6004803603602081101561054a57600080fd5b5035610c39565b61034c6004803603602081101561056757600080fd5b50356001600160a01b0316610c8d565b61059d6004803603602081101561058d57600080fd5b50356001600160a01b0316610cf5565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156105d95781810151838201526020016105c1565b505050509050019250505060405180910390f35b6101bb610d56565b6102956004803603602081101561060b57600080fd5b50356001600160a01b0316610db7565b610295610e04565b6102956004803603604081101561063957600080fd5b506001600160a01b0381351690602001351515610e0f565b61019f6004803603602081101561066757600080fd5b50356001600160a01b0316610edb565b6102956004803603608081101561068d57600080fd5b6001600160a01b038235811692602081013590911691604082013591908101906080810160608201356401000000008111156106c857600080fd5b8201836020820111156106da57600080fd5b803590602001918460018302840111640100000000831117156106fc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610eee945050505050565b6101bb6004803603602081101561075357600080fd5b5035610f46565b61019f6004803603604081101561077057600080fd5b506001600160a01b0381358116916020013516611021565b6001600160e01b03191660009081526020819052604090205460ff1690565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156108335780601f1061080857610100808354040283529160200191610833565b820191906000526020600020905b81548152906001019060200180831161081657829003601f168201915b505050505090505b90565b60006108498261104f565b6108845760405162461bcd60e51b815260040180806020018281038252602c815260200180611e08602c913960400191505060405180910390fd5b506000908152600260205260409020546001600160a01b031690565b60006108ab82610c39565b9050806001600160a01b0316836001600160a01b031614156108fe5760405162461bcd60e51b8152600401808060200182810382526021815260200180611eda6021913960400191505060405180910390fd5b336001600160a01b038216148061091a575061091a8133611021565b6109555760405162461bcd60e51b8152600401808060200182810382526038815260200180611d2c6038913960400191505060405180910390fd5b60008281526002602052604080822080546001600160a01b0319166001600160a01b0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b6109bb828261106c565b5050565b60075490565b6109cf33826110cf565b610a0a5760405162461bcd60e51b8152600401808060200182810382526031815260200180611efb6031913960400191505060405180910390fd5b610a15838383611173565b505050565b6000610a2583610c8d565b8210610a625760405162461bcd60e51b815260040180806020018281038252602b815260200180611c7f602b913960400191505060405180910390fd5b6001600160a01b0383166000908152600560205260409020805483908110610a8657fe5b9060005260206000200154905092915050565b6000610aa433610edb565b610adf5760405162461bcd60e51b8152600401808060200182810382526030815260200180611db76030913960400191505060405180910390fd5b610ae98383611192565b50600192915050565b610a1583838360405180602001604052806000815250610eee565b610b1733826110cf565b610b525760405162461bcd60e51b8152600401808060200182810382526030815260200180611f7d6030913960400191505060405180910390fd5b610b5b816111af565b50565b6000610b698261104f565b92915050565b6000610b796109bf565b8210610bb65760405162461bcd60e51b815260040180806020018281038252602c815260200180611f2c602c913960400191505060405180910390fd5b60078281548110610bc357fe5b90600052602060002001549050919050565b6000610be033610edb565b610c1b5760405162461bcd60e51b8152600401808060200182810382526030815260200180611db76030913960400191505060405180910390fd5b610c258484611192565b610c2f838361106c565b5060019392505050565b6000818152600160205260408120546001600160a01b031680610b695760405162461bcd60e51b8152600401808060200182810382526029815260200180611d8e6029913960400191505060405180910390fd5b60006001600160a01b038216610cd45760405162461bcd60e51b815260040180806020018281038252602a815260200180611d64602a913960400191505060405180910390fd5b6001600160a01b0382166000908152600360205260409020610b69906111c1565b6060610d00826111c5565b805480602002602001604051908101604052809291908181526020018280548015610d4a57602002820191906000526020600020905b815481526020019060010190808311610d36575b50505050509050919050565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156108335780601f1061080857610100808354040283529160200191610833565b610dc033610edb565b610dfb5760405162461bcd60e51b8152600401808060200182810382526030815260200180611db76030913960400191505060405180910390fd5b610b5b816111df565b610e0d33611227565b565b6001600160a01b038216331415610e6d576040805162461bcd60e51b815260206004820152601960248201527f4552433732313a20617070726f766520746f2063616c6c657200000000000000604482015290519081900360640190fd5b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b6000610b69600c8363ffffffff61126f16565b610ef98484846109c5565b610f05848484846112d6565b610f405760405162461bcd60e51b8152600401808060200182810382526032815260200180611caa6032913960400191505060405180910390fd5b50505050565b6060610f518261104f565b610f8c5760405162461bcd60e51b815260040180806020018281038252602f815260200180611eab602f913960400191505060405180910390fd5b6000828152600b602090815260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610d4a5780601f10610ff457610100808354040283529160200191610d4a565b820191906000526020600020905b8154815290600101906020018083116110025750939695505050505050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b6000908152600160205260409020546001600160a01b0316151590565b6110758261104f565b6110b05760405162461bcd60e51b815260040180806020018281038252602c815260200180611e34602c913960400191505060405180910390fd5b6000828152600b602090815260409091208251610a1592840190611b82565b60006110da8261104f565b6111155760405162461bcd60e51b815260040180806020018281038252602c815260200180611d00602c913960400191505060405180910390fd5b600061112083610c39565b9050806001600160a01b0316846001600160a01b0316148061115b5750836001600160a01b03166111508461083e565b6001600160a01b0316145b8061116b575061116b8185611021565b949350505050565b61117e838383611409565b611188838261154d565b610a158282611642565b61119c8282611680565b6111a68282611642565b6109bb816117b1565b610b5b6111bb82610c39565b826117f5565b5490565b6001600160a01b0316600090815260056020526040902090565b6111f0600c8263ffffffff61183d16565b6040516001600160a01b038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b611238600c8263ffffffff6118be16565b6040516001600160a01b038216907fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669290600090a250565b60006001600160a01b0382166112b65760405162461bcd60e51b8152600401808060200182810382526022815260200180611e606022913960400191505060405180910390fd5b506001600160a01b03166000908152602091909152604090205460ff1690565b60006112ea846001600160a01b0316611925565b6112f65750600161116b565b604051630a85bd0160e11b815233600482018181526001600160a01b03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b83811015611370578181015183820152602001611358565b50505050905090810190601f16801561139d5780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b1580156113bf57600080fd5b505af11580156113d3573d6000803e3d6000fd5b505050506040513d60208110156113e957600080fd5b50516001600160e01b031916630a85bd0160e11b14915050949350505050565b826001600160a01b031661141c82610c39565b6001600160a01b0316146114615760405162461bcd60e51b8152600401808060200182810382526029815260200180611e826029913960400191505060405180910390fd5b6001600160a01b0382166114a65760405162461bcd60e51b8152600401808060200182810382526024815260200180611cdc6024913960400191505060405180910390fd5b6114af8161192b565b6001600160a01b03831660009081526003602052604090206114d090611966565b6001600160a01b03821660009081526003602052604090206114f19061197d565b60008181526001602052604080822080546001600160a01b0319166001600160a01b0386811691821790925591518493918716917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4505050565b6001600160a01b03821660009081526005602052604081205461157790600163ffffffff61198616565b600083815260066020526040902054909150808214611612576001600160a01b03841660009081526005602052604081208054849081106115b457fe5b906000526020600020015490508060056000876001600160a01b03166001600160a01b0316815260200190815260200160002083815481106115f257fe5b600091825260208083209091019290925591825260069052604090208190555b6001600160a01b038416600090815260056020526040902080549061163b906000198301611c00565b5050505050565b6001600160a01b0390911660009081526005602081815260408084208054868652600684529185208290559282526001810183559183529091200155565b6001600160a01b0382166116db576040805162461bcd60e51b815260206004820181905260248201527f4552433732313a206d696e7420746f20746865207a65726f2061646472657373604482015290519081900360640190fd5b6116e48161104f565b15611736576040805162461bcd60e51b815260206004820152601c60248201527f4552433732313a20746f6b656e20616c7265616479206d696e74656400000000604482015290519081900360640190fd5b600081815260016020908152604080832080546001600160a01b0319166001600160a01b0387169081179091558352600390915290206117759061197d565b60405181906001600160a01b038416906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b600780546000838152600860205260408120829055600182018355919091527fa66cc928b5edb82af9bd49922954155ab7b0942694bea4ce44661d9a8736c6880155565b6117ff82826119e3565b6000818152600b602052604090205460026000196101006001841615020190911604156109bb576000818152600b602052604081206109bb91611c24565b611847828261126f565b15611899576040805162461bcd60e51b815260206004820152601f60248201527f526f6c65733a206163636f756e7420616c72656164792068617320726f6c6500604482015290519081900360640190fd5b6001600160a01b0316600090815260209190915260409020805460ff19166001179055565b6118c8828261126f565b6119035760405162461bcd60e51b8152600401808060200182810382526021815260200180611de76021913960400191505060405180910390fd5b6001600160a01b0316600090815260209190915260409020805460ff19169055565b3b151590565b6000818152600260205260409020546001600160a01b031615610b5b57600090815260026020526040902080546001600160a01b0319169055565b805461197990600163ffffffff61198616565b9055565b80546001019055565b6000828211156119dd576040805162461bcd60e51b815260206004820152601e60248201527f536166654d6174683a207375627472616374696f6e206f766572666c6f770000604482015290519081900360640190fd5b50900390565b6119ed8282611a0f565b6119f7828261154d565b6000818152600660205260408120556109bb81611ae6565b816001600160a01b0316611a2282610c39565b6001600160a01b031614611a675760405162461bcd60e51b8152600401808060200182810382526025815260200180611f586025913960400191505060405180910390fd5b611a708161192b565b6001600160a01b0382166000908152600360205260409020611a9190611966565b60008181526001602052604080822080546001600160a01b0319169055518291906001600160a01b038516907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908390a45050565b600754600090611afd90600163ffffffff61198616565b60008381526008602052604081205460078054939450909284908110611b1f57fe5b906000526020600020015490508060078381548110611b3a57fe5b60009182526020808320909101929092558281526008909152604090208290556007805490611b6d906000198301611c00565b50505060009182525060086020526040812055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611bc357805160ff1916838001178555611bf0565b82800160010185558215611bf0579182015b82811115611bf0578251825591602001919060010190611bd5565b50611bfc929150611c64565b5090565b815481835581811115610a1557600083815260209020610a15918101908301611c64565b50805460018160011615610100020316600290046000825580601f10611c4a5750610b5b565b601f016020900490600052602060002090810190610b5b91905b61083b91905b80821115611bfc5760008155600101611c6a56fe455243373231456e756d657261626c653a206f776e657220696e646578206f7574206f6620626f756e64734552433732313a207472616e7366657220746f206e6f6e20455243373231526563656976657220696d706c656d656e7465724552433732313a207472616e7366657220746f20746865207a65726f20616464726573734552433732313a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4552433732313a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734552433732313a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654552433732313a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732314d657461646174613a2055524920736574206f66206e6f6e6578697374656e7420746f6b656e526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734552433732313a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4552433732314d657461646174613a2055524920717565727920666f72206e6f6e6578697374656e7420746f6b656e4552433732313a20617070726f76616c20746f2063757272656e74206f776e65724552433732313a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564455243373231456e756d657261626c653a20676c6f62616c20696e646578206f7574206f6620626f756e64734552433732313a206275726e206f6620746f6b656e2074686174206973206e6f74206f776e4552433732314275726e61626c653a2063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564a265627a7a72305820561fe6c919884711aea4da1c5deada22b548c9eb05a3229afe8996621fed38ce64736f6c63430005090032526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_SETTOKENURI = "setTokenURI";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_EXISTS = "exists";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_MINTWITHTOKENURI = "mintWithTokenURI";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_TOKENSOFOWNER = "tokensOfOwner";

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

    protected ERC721FullMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected ERC721FullMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> setTokenURI(BigInteger tokenId, String uri) {
        final Function function = new Function(
                FUNC_SETTOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.Utf8String(uri)), 
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

    public RemoteCall<Boolean> exists(BigInteger tokenId) {
        final Function function = new Function(FUNC_EXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteCall<List> tokensOfOwner(String owner) {
        final Function function = new Function(FUNC_TOKENSOFOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
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

    public static ERC721FullMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new ERC721FullMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static ERC721FullMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721FullMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721FullMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider, String name, String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol)));
        return deployRemoteCall(ERC721FullMock.class, caver, credentials, chainId, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ERC721FullMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name, String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(symbol)));
        return deployRemoteCall(ERC721FullMock.class, caver, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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
