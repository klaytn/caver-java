/*
 * Copyright 2020 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.kct.kip7;

/**
 * Representing a data class that has KIP7 constant data to deploy.<p>
 * This KIP-7 contract source code based on <a href="https://github.com/klaytn/klaytn-contracts">Klaytn-contracts</a>.
 */
public class KIP7ConstantData {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162002f8438038062002f84833981018060405260808110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b50509291906020018051640100000000811115620000a257600080fd5b82810190506020810184811115620000b957600080fd5b8151856001820283011164010000000082111715620000d757600080fd5b50509291906020018051906020019092919080519060200190929190505050838383620001116301ffc9a760e01b6200023260201b60201c565b62000129636578737160e01b6200023260201b60201c565b6200013a336200033b60201b60201c565b6200015263eab83e2060e01b6200023260201b60201c565b6200016a633b5a0bf860e01b6200023260201b60201c565b6200017b336200039c60201b60201c565b6000600660006101000a81548160ff021916908315150217905550620001ae634d5507ff60e01b6200023260201b60201c565b8260079080519060200190620001c692919062000816565b508160089080519060200190620001df92919062000816565b5080600960006101000a81548160ff021916908360ff1602179055506200021363a219a02560e01b6200023260201b60201c565b505050620002283382620003fd60201b60201c565b50505050620008c5565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415620002cf576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b62000356816004620005c960201b620024181790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b620003b7816005620005c960201b620024181790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f860405160405180910390a250565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415620004a1576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b620004bd81600354620006ad60201b620022d31790919060201c565b6003819055506200051c81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054620006ad60201b620022d31790919060201c565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b620005db82826200073660201b60201c565b156200064f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b6000808284019050838110156200072c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415620007bf576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602281526020018062002f626022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200085957805160ff19168380011785556200088a565b828001600101855582156200088a579182015b82811115620008895782518255916020019190600101906200086c565b5b5090506200089991906200089d565b5090565b620008c291905b80821115620008be576000816000905550600101620008a4565b5090565b90565b61268d80620008d56000396000f3fe608060405234801561001057600080fd5b506004361061018e5760003560e01c80636ef8d66d116100de578063983b2d5611610097578063aa271e1a11610071578063aa271e1a146107b6578063b88d4fde14610812578063dd62ed3e14610917578063eb7955491461098f5761018e565b8063983b2d56146107025780639865027514610746578063a9059cbb146107505761018e565b80636ef8d66d1461058157806370a082311461058b57806379cc6790146105e357806382dc1ec4146106315780638456cb591461067557806395d89b411461067f5761018e565b80633f4ba83a1161014b57806342842e0e1161012557806342842e0e1461046757806342966c68146104d557806346fbf68e146105035780635c975abb1461055f5761018e565b80633f4ba83a146103a957806340c10f19146103b3578063423f6cef146104195761018e565b806301ffc9a71461019357806306fdde03146101f8578063095ea7b31461027b57806318160ddd146102e157806323b872dd146102ff578063313ce56714610385575b600080fd5b6101de600480360360208110156101a957600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610a74565b604051808215151515815260200191505060405180910390f35b610200610adb565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610240578082015181840152602081019050610225565b50505050905090810190601f16801561026d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102c76004803603604081101561029157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b7d565b604051808215151515815260200191505060405180910390f35b6102e9610c14565b6040518082815260200191505060405180910390f35b61036b6004803603606081101561031557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610c1e565b604051808215151515815260200191505060405180910390f35b61038d610cb7565b604051808260ff1660ff16815260200191505060405180910390f35b6103b1610cce565b005b6103ff600480360360408110156103c957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610e2e565b604051808215151515815260200191505060405180910390f35b6104656004803603604081101561042f57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ea2565b005b6104d36004803603606081101561047d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ec0565b005b610501600480360360208110156104eb57600080fd5b8101908080359060200190929190505050610ee0565b005b6105456004803603602081101561051957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610eed565b604051808215151515815260200191505060405180910390f35b610567610f0a565b604051808215151515815260200191505060405180910390f35b610589610f21565b005b6105cd600480360360208110156105a157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f2c565b6040518082815260200191505060405180910390f35b61062f600480360360408110156105f957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610f75565b005b6106736004803603602081101561064757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f83565b005b61067d610fed565b005b61068761114e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156106c75780820151818401526020810190506106ac565b50505050905090810190601f1680156106f45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6107446004803603602081101561071857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506111f0565b005b61074e61125a565b005b61079c6004803603604081101561076657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611265565b604051808215151515815260200191505060405180910390f35b6107f8600480360360208110156107cc57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506112fc565b604051808215151515815260200191505060405180910390f35b6109156004803603608081101561082857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561088f57600080fd5b8201836020820111156108a157600080fd5b803590602001918460018302840111640100000000831117156108c357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611319565b005b6109796004803603604081101561092d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061138c565b6040518082815260200191505060405180910390f35b610a72600480360360608110156109a557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156109ec57600080fd5b8201836020820111156109fe57600080fd5b80359060200191846001830284011164010000000083111715610a2057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611413565b005b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060078054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b735780601f10610b4857610100808354040283529160200191610b73565b820191906000526020600020905b815481529060010190602001808311610b5657829003601f168201915b5050505050905090565b6000600660009054906101000a900460ff1615610c02576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f5061757361626c653a207061757365640000000000000000000000000000000081525060200191505060405180910390fd5b610c0c8383611484565b905092915050565b6000600354905090565b6000600660009054906101000a900460ff1615610ca3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f5061757361626c653a207061757365640000000000000000000000000000000081525060200191505060405180910390fd5b610cae84848461149b565b90509392505050565b6000600960009054906101000a900460ff16905090565b610cd733610eed565b610d2c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125566030913960400191505060405180910390fd5b600660009054906101000a900460ff16610dae576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f5061757361626c653a206e6f742070617573656400000000000000000000000081525060200191505060405180910390fd5b6000600660006101000a81548160ff0219169083151502179055507f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa33604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1565b6000610e39336112fc565b610e8e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125a86030913960400191505060405180910390fd5b610e98838361154c565b6001905092915050565b610ebc828260405180602001604052806000815250611413565b5050565b610edb83838360405180602001604052806000815250611319565b505050565b610eea3382611709565b50565b6000610f038260056118c690919063ffffffff16565b9050919050565b6000600660009054906101000a900460ff16905090565b610f2a336119a4565b565b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b610f7f82826119fe565b5050565b610f8c33610eed565b610fe1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125566030913960400191505060405180910390fd5b610fea81611aa5565b50565b610ff633610eed565b61104b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125566030913960400191505060405180910390fd5b600660009054906101000a900460ff16156110ce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f5061757361626c653a207061757365640000000000000000000000000000000081525060200191505060405180910390fd5b6001600660006101000a81548160ff0219169083151502179055507f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a25833604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1565b606060088054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156111e65780601f106111bb576101008083540402835291602001916111e6565b820191906000526020600020905b8154815290600101906020018083116111c957829003601f168201915b5050505050905090565b6111f9336112fc565b61124e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125a86030913960400191505060405180910390fd5b61125781611aff565b50565b61126333611b59565b565b6000600660009054906101000a900460ff16156112ea576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f5061757361626c653a207061757365640000000000000000000000000000000081525060200191505060405180910390fd5b6112f48383611bb3565b905092915050565b60006113128260046118c690919063ffffffff16565b9050919050565b611324848484610c1e565b5061133184848484611bca565b611386576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180612528602e913960400191505060405180910390fd5b50505050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b61141d8383611265565b5061142a33848484611bca565b61147f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180612528602e913960400191505060405180910390fd5b505050565b6000611491338484611db3565b6001905092915050565b60006114a8848484611faa565b611541843361153c85600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461224a90919063ffffffff16565b611db3565b600190509392505050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156115ef576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b611604816003546122d390919063ffffffff16565b60038190555061165c81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546122d390919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156117ac576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4b4950373a206275726e2066726f6d20746865207a65726f206164647265737381525060200191505060405180910390fd5b6117c18160035461224a90919063ffffffff16565b60038190555061181981600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461224a90919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561194d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602281526020018061261d6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6119b881600561235b90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fcd265ebaf09df2871cc7bd4133404a235ba12eff2041bb89d9c714a2621c7c7e60405160405180910390a250565b611a088282611709565b611aa18233611a9c84600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461224a90919063ffffffff16565b611db3565b5050565b611ab981600561241890919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f860405160405180910390a250565b611b1381600461241890919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b611b6d81600461235b90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b6000611bc0338484611faa565b6001905092915050565b6000611beb8473ffffffffffffffffffffffffffffffffffffffff166124f3565b611bf85760019050611dab565b60008473ffffffffffffffffffffffffffffffffffffffff16639d188c22338887876040518563ffffffff1660e01b8152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611cd3578082015181840152602081019050611cb8565b50505050905090810190601f168015611d005780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015611d2257600080fd5b505af1158015611d36573d6000803e3d6000fd5b505050506040513d6020811015611d4c57600080fd5b81019080805190602001909291905050509050639d188c2260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149150505b949350505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415611e39576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061263f6023913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611ebf576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001806125076021913960400191505060405180910390fd5b80600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925836040518082815260200191505060405180910390a3505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415612030576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806125f96024913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156120b6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806125866022913960400191505060405180910390fd5b61210881600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461224a90919063ffffffff16565b600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061219d81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546122d390919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050565b6000828211156122c2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b600080828401905083811015612351576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b61236582826118c6565b6123ba576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001806125d86021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b61242282826118c6565b15612495576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b600080823b90506000811191505091905056fe4b4950373a20617070726f766520746f20746865207a65726f20616464726573734b4950373a207472616e7366657220746f206e6f6e204b495037526563656976657220696d706c656d656e746572506175736572526f6c653a2063616c6c657220646f6573206e6f742068617665207468652050617573657220726f6c654b4950373a207472616e7366657220746f20746865207a65726f20616464726573734d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b4950373a207472616e736665722066726f6d20746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b4950373a20617070726f76652066726f6d20746865207a65726f2061646472657373a165627a7a72305820e51fab94e7709d70e4b7ff4aaaf6428d983cfa2bbfb4be772ec2de87ea0a2b9f0029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
    public static final String ABI = "[\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"interfaceId\",\n" +
            "        \"type\": \"bytes4\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"supportsInterface\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"name\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"spender\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"approve\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"totalSupply\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"from\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"to\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"transferFrom\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"decimals\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"uint8\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"unpause\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"mint\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"recipient\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"safeTransfer\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"sender\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"recipient\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"safeTransferFrom\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"burn\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"isPauser\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"paused\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"renouncePauser\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"balanceOf\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"burnFrom\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"addPauser\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"pause\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"symbol\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"addMinter\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"renounceMinter\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"to\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"transfer\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"isMinter\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"bool\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"sender\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"recipient\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"data\",\n" +
            "        \"type\": \"bytes\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"safeTransferFrom\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"owner\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"spender\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"allowance\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"recipient\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"amount\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"data\",\n" +
            "        \"type\": \"bytes\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"safeTransfer\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"name\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"symbol\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"decimals\",\n" +
            "        \"type\": \"uint8\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"initialSupply\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"constructor\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": false,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"Paused\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": false,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"Unpaused\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"PauserAdded\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"PauserRemoved\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"MinterAdded\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"account\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"MinterRemoved\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"from\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"to\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"indexed\": false,\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"Transfer\",\n" +
            "    \"type\": \"event\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"owner\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"spender\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"indexed\": false,\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"Approval\",\n" +
            "    \"type\": \"event\"\n" +
            "  }\n" +
            "]";
}
