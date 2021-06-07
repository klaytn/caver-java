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
package com.klaytn.caver.common.contract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.*;
import com.klaytn.caver.abi.datatypes.generated.StaticArray3;
import com.klaytn.caver.abi.datatypes.generated.Uint256;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

//// Klaytn IDE uses solidity 0.4.24, 0.5.6 versions.
//pragma solidity >=0.4.24 <=0.5.6;
//pragma experimental ABIEncoderV2;
//
//contract Count {
//    struct dynamicStruct {
//        string name;
//        uint256[3] array;
//    }
//
//    struct staticStruct {
//        uint256 count;
//        bool isValid;
//    }
//
//    struct combineStruct {
//        dynamicStruct ds;
//        staticStruct ss;
//        uint256 count;
//    }
//
//    // Storage variable `count` (type: uint256)
//    uint256 public count = 0;
//
//    function setDs(dynamicStruct memory ds) public pure returns (dynamicStruct memory) {
//        return ds;
//    }
//
//    function setSs(staticStruct memory ss) public pure returns (staticStruct memory) {
//        return ss;
//    }
//
//    function setSsArr(staticStruct[] memory ssArr) public pure returns (staticStruct[] memory) {
//        return ssArr;
//    }
//
//    function setCs(combineStruct memory cs) public pure returns (combineStruct memory) {
//        return cs
//    }
//
//    // Get current node's block number.
//    function getBlockNumber() public view returns (uint256) {
//      return block.number;
//    }
//
//    // Set value of storage variable `count`.
//    function setCount(uint256 _count) public {
//      count = _count;
//    }
//}

public class StructContractTest {
    static String BYTECODE = "60806040526000805534801561001457600080fd5b50610bf5806100246000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c806388d325e91161005b57806388d325e9146100ee578063ad8db94e1461011e578063d14e62b81461014e578063dd1568331461016a5761007d565b806306661abd1461008257806342cbb15c146100a057806378c1f341146100be575b600080fd5b61008a61019a565b60405161009791906109ff565b60405180910390f35b6100a86101a0565b6040516100b591906109ff565b60405180910390f35b6100d860048036036100d39190810190610680565b6101a8565b6040516100e591906109c2565b60405180910390f35b610108600480360361010391908101906106c1565b6101b8565b60405161011591906109e4565b60405180910390f35b6101386004803603610133919081019061063f565b6101c8565b60405161014591906109a0565b60405180910390f35b610168600480360361016391908101906106ea565b6101d8565b005b610184600480360361017f91908101906105fe565b6101e2565b604051610191919061097e565b60405180910390f35b60005481565b600043905090565b6101b06101ec565b819050919050565b6101c061020c565b819050919050565b6101d0610228565b819050919050565b8060008190555050565b6060819050919050565b604051806080016040528060608152602001610206610255565b81525090565b6040518060400160405280600081526020016000151581525090565b6040518060e0016040528061023b610277565b8152602001610248610297565b8152602001600081525090565b6040518060600160405280600390602082028038833980820191505090505090565b604051806080016040528060608152602001610291610255565b81525090565b6040518060400160405280600081526020016000151581525090565b600082601f8301126102c457600080fd5b81356102d76102d282610a47565b610a1a565b915081818352602084019350602081019050838560408402820111156102fc57600080fd5b60005b8381101561032c57816103128882610552565b8452602084019350604083019250506001810190506102ff565b5050505092915050565b600082601f83011261034757600080fd5b600361035a61035582610a6f565b610a1a565b9150818385602084028201111561037057600080fd5b60005b838110156103a0578161038688826105ea565b845260208401935060208301925050600181019050610373565b5050505092915050565b60006103b68235610b52565b905092915050565b600082601f8301126103cf57600080fd5b81356103e26103dd82610a91565b610a1a565b915080825260208301602083018583830111156103fe57600080fd5b610409838284610b68565b50505092915050565b60006080828403121561042457600080fd5b61042e6060610a1a565b9050600082013567ffffffffffffffff81111561044a57600080fd5b6104568482850161048a565b600083015250602061046a84828501610552565b602083015250606061047e848285016105ea565b60408301525092915050565b60006080828403121561049c57600080fd5b6104a66040610a1a565b9050600082013567ffffffffffffffff8111156104c257600080fd5b6104ce848285016103be565b60008301525060206104e284828501610336565b60208301525092915050565b60006080828403121561050057600080fd5b61050a6040610a1a565b9050600082013567ffffffffffffffff81111561052657600080fd5b610532848285016103be565b600083015250602061054684828501610336565b60208301525092915050565b60006040828403121561056457600080fd5b61056e6040610a1a565b9050600061057e848285016105ea565b6000830152506020610592848285016103aa565b60208301525092915050565b6000604082840312156105b057600080fd5b6105ba6040610a1a565b905060006105ca848285016105ea565b60008301525060206105de848285016103aa565b60208301525092915050565b60006105f68235610b5e565b905092915050565b60006020828403121561061057600080fd5b600082013567ffffffffffffffff81111561062a57600080fd5b610636848285016102b3565b91505092915050565b60006020828403121561065157600080fd5b600082013567ffffffffffffffff81111561066b57600080fd5b61067784828501610412565b91505092915050565b60006020828403121561069257600080fd5b600082013567ffffffffffffffff8111156106ac57600080fd5b6106b8848285016104ee565b91505092915050565b6000604082840312156106d357600080fd5b60006106e18482850161059e565b91505092915050565b6000602082840312156106fc57600080fd5b600061070a848285016105ea565b91505092915050565b600061071f8383610931565b60408301905092915050565b60006107378383610960565b60208301905092915050565b600061074e82610ad4565b6107588185610b0f565b935061076383610abd565b60005b8281101561079157610779868351610713565b955061078482610af5565b9150600181019050610766565b50849250505092915050565b6107a681610adf565b6107b08184610b20565b92506107bb82610aca565b60005b828110156107e9576107d185835161072b565b94506107dc82610b02565b91506001810190506107be565b5050505050565b6107f981610b3c565b82525050565b600061080a82610aea565b6108148185610b2b565b9350610824818560208601610b77565b61082d81610baa565b840191505092915050565b6000608083016000830151848203600086015261085582826108c5565b915050602083015161086a6020860182610931565b50604083015161087d6060860182610960565b508091505092915050565b600060808301600083015184820360008601526108a582826107ff565b91505060208301516108ba602086018261079d565b508091505092915050565b600060808301600083015184820360008601526108e282826107ff565b91505060208301516108f7602086018261079d565b508091505092915050565b6040820160008201516109186000850182610960565b50602082015161092b60208501826107f0565b50505050565b6040820160008201516109476000850182610960565b50602082015161095a60208501826107f0565b50505050565b61096981610b48565b82525050565b61097881610b48565b82525050565b600060208201905081810360008301526109988184610743565b905092915050565b600060208201905081810360008301526109ba8184610838565b905092915050565b600060208201905081810360008301526109dc8184610888565b905092915050565b60006040820190506109f96000830184610902565b92915050565b6000602082019050610a14600083018461096f565b92915050565b6000604051905081810181811067ffffffffffffffff82111715610a3d57600080fd5b8060405250919050565b600067ffffffffffffffff821115610a5e57600080fd5b602082029050602081019050919050565b600067ffffffffffffffff821115610a8657600080fd5b602082029050919050565b600067ffffffffffffffff821115610aa857600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000819050919050565b600081519050919050565b600060039050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b600082825260208201905092915050565b600081905092915050565b600082825260208201905092915050565b60008115159050919050565b6000819050919050565b60008115159050919050565b6000819050919050565b82818337600083830152505050565b60005b83811015610b95578082015181840152602081019050610b7a565b83811115610ba4576000848401525b50505050565b6000601f19601f830116905091905056fea265627a7a72305820cfd3d9cae529927e688c0271e0d974fe739d3b69fc80d272f5b7085ba5106c696c6578706572696d656e74616cf50037";
    static String CONTRACT_ABI = "[\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"count\",\n" +
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
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"getBlockNumber\",\n" +
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
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"name\",\n" +
            "            \"type\": \"string\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"array\",\n" +
            "            \"type\": \"uint256[3]\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"ds\",\n" +
            "        \"type\": \"tuple\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setDs\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"name\",\n" +
            "            \"type\": \"string\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"array\",\n" +
            "            \"type\": \"uint256[3]\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"tuple\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"isValid\",\n" +
            "            \"type\": \"bool\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"ss\",\n" +
            "        \"type\": \"tuple\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setSs\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"isValid\",\n" +
            "            \"type\": \"bool\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"tuple\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"components\": [\n" +
            "              {\n" +
            "                \"name\": \"name\",\n" +
            "                \"type\": \"string\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"array\",\n" +
            "                \"type\": \"uint256[3]\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"name\": \"ds\",\n" +
            "            \"type\": \"tuple\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"components\": [\n" +
            "              {\n" +
            "                \"name\": \"count\",\n" +
            "                \"type\": \"uint256\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"isValid\",\n" +
            "                \"type\": \"bool\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"name\": \"ss\",\n" +
            "            \"type\": \"tuple\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"cs\",\n" +
            "        \"type\": \"tuple\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setCs\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"components\": [\n" +
            "              {\n" +
            "                \"name\": \"name\",\n" +
            "                \"type\": \"string\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"array\",\n" +
            "                \"type\": \"uint256[3]\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"name\": \"ds\",\n" +
            "            \"type\": \"tuple\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"components\": [\n" +
            "              {\n" +
            "                \"name\": \"count\",\n" +
            "                \"type\": \"uint256\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"isValid\",\n" +
            "                \"type\": \"bool\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"name\": \"ss\",\n" +
            "            \"type\": \"tuple\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"tuple\"\n" +
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
            "        \"name\": \"_count\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setCount\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"isValid\",\n" +
            "            \"type\": \"bool\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"ssArr\",\n" +
            "        \"type\": \"tuple[]\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setSsArr\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"components\": [\n" +
            "          {\n" +
            "            \"name\": \"count\",\n" +
            "            \"type\": \"uint256\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"isValid\",\n" +
            "            \"type\": \"bool\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"tuple[]\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  }\n" +
            "]";

    static Caver caver;
    static Contract testContract;
    static SingleKeyring deployerKeyring;

    @BeforeClass
    public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        caver = new Caver(Caver.DEFAULT_URL);
        deployerKeyring = (SingleKeyring)caver.wallet.add(KeyringFactory.createFromPrivateKey("871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5"));
        testContract = new Contract(caver, CONTRACT_ABI);

        testContract.deploy(new SendOptions(deployerKeyring.getAddress(), BigInteger.valueOf(30000000)), BYTECODE);
        System.out.println(testContract.getContractAddress());
    }

    @Test
    public void testDs() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Type> result = testContract.getMethod("setDs").call(Collections.singletonList(Arrays.asList("name", Arrays.asList(1,2,3))));
        StructType dynamicStruct = (DynamicStruct)result.get(0);
        assertEquals(dynamicStruct.getComponents().get(0).getValue(), "name");
        assertEquals(dynamicStruct.getComponents().get(1), new StaticArray3(Uint256.class, Arrays.asList(new Uint256(1), new Uint256(2), new Uint256(3))));
    }

    @Test
    public void testSs() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Type> result = testContract.getMethod("setSs").call(Collections.singletonList(Arrays.asList(1, false)));
        StaticStruct staticStruct = (StaticStruct)result.get(0);

        StructType expected = new StaticStruct(Arrays.asList(new Uint256(1), new Bool(false)));
        assertEquals(expected, staticStruct);
    }

    @Test
    public void testCs() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DynamicStruct expected = new DynamicStruct(
                Arrays.asList(
                        new DynamicStruct(
                                Arrays.asList(
                                        new Utf8String("name"),
                                        new StaticArray3(Uint256.class, Arrays.asList(new Uint256(1), new Uint256(2), new Uint256(3)))
                                )
                        ),
                        new StaticStruct(Arrays.asList(new Uint256(1), new Bool(false))),
                        new Uint256(123)
                )
        );

        List<Type> result = testContract.getMethod("setCs").call(
                Collections.singletonList(
                        Arrays.asList(
                                Arrays.asList("name", Arrays.asList(1,2,3)),
                                Arrays.asList(1, false),
                                123
                        )
                )
        );

        StructType dynamicStruct = (DynamicStruct)result.get(0);
        assertEquals(expected, dynamicStruct);
    }

    @Test
    public void testSsArr() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DynamicArray expected = new DynamicArray(
                StaticStruct.class,
                Arrays.asList(
                        new StaticStruct(Arrays.asList(new Uint256(1), new Bool(false))),
                        new StaticStruct(Arrays.asList(new Uint256(2), new Bool(false))),
                        new StaticStruct(Arrays.asList(new Uint256(3), new Bool(true)))
                )
        );

        List<Type> result = testContract.getMethod("setSsArr").call(
                Collections.singletonList(
                        Arrays.asList(
                                Arrays.asList(1, false),
                                Arrays.asList(2, false),
                                Arrays.asList(3, true)
                        )
                )
        );

        assertEquals(expected, result.get(0));
    }
}
