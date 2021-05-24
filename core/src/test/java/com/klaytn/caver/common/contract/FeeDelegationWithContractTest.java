/*
 * Copyright 2021 The caver-java Authors
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
import com.klaytn.caver.abi.datatypes.Utf8String;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.Utils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.*;

public class FeeDelegationWithContractTest {
    public static String byteCodeWithoutConstructor = "608060405234801561001057600080fd5b5061051f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063693ec85e1461003b578063e942b5161461016f575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c1565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102bf6004803603604081101561018557600080fd5b81019080803590602001906401000000008111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111640100000000831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561023957600080fd5b82018360208201111561024b57600080fd5b8035906020019184600183028401116401000000008311171561026d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103cc565b005b60606000826040518082805190602001908083835b602083106102f957805182526020820191506020810190506020830392506102d6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c05780601f10610395576101008083540402835291602001916103c0565b820191906000526020600020905b8154815290600101906020018083116103a357829003601f168201915b50505050509050919050565b806000836040518082805190602001908083835b6020831061040357805182526020820191506020810190506020830392506103e0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061044992919061044e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061048f57805160ff19168380011785556104bd565b828001600101855582156104bd579182015b828111156104bc5782518255916020019190600101906104a1565b5b5090506104ca91906104ce565b5090565b6104f091905b808211156104ec5760008160009055506001016104d4565b5090565b9056fea165627a7a723058203ffebc792829e0434ecc495da1b53d24399cd7fff506a4fd03589861843e14990029";
    public static String abiWithoutConstructor = "[\n" +
            "  {\n" +
            "    \"constant\":true,\n" +
            "    \"inputs\":[\n" +
            "      {\n" +
            "        \"name\":\"key\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\":\"get\",\n" +
            "    \"outputs\":[\n" +
            "      {\n" +
            "        \"name\":\"\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\":false,\n" +
            "    \"stateMutability\":\"view\",\n" +
            "    \"type\":\"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\":false,\n" +
            "    \"inputs\":[\n" +
            "      {\n" +
            "        \"name\":\"key\",\n" +
            "        \"type\":\"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\":\"value\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\":\"set\",\n" +
            "    \"outputs\":[],\n" +
            "    \"payable\":false,\n" +
            "    \"stateMutability\":\"nonpayable\",\n" +
            "    \"type\":\"function\"\n" +
            "  }\n" +
            "]";

    static String byteCodeWithConstructor = "608060405234801561001057600080fd5b506040516107413803806107418339810180604052604081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8281019050602081018481111561006157600080fd5b815185600182028301116401000000008211171561007e57600080fd5b5050929190602001805164010000000081111561009a57600080fd5b828101905060208101848111156100b057600080fd5b81518560018202830111640100000000821117156100cd57600080fd5b50509291905050506100e582826100ec60201b60201c565b5050610213565b806000836040518082805190602001908083835b602083106101235780518252602082019150602081019050602083039250610100565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061016992919061016e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101af57805160ff19168380011785556101dd565b828001600101855582156101dd579182015b828111156101dc5782518255916020019190600101906101c1565b5b5090506101ea91906101ee565b5090565b61021091905b8082111561020c5760008160009055506001016101f4565b5090565b90565b61051f806102226000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063693ec85e1461003b578063e942b5161461016f575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c1565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102bf6004803603604081101561018557600080fd5b81019080803590602001906401000000008111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111640100000000831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561023957600080fd5b82018360208201111561024b57600080fd5b8035906020019184600183028401116401000000008311171561026d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103cc565b005b60606000826040518082805190602001908083835b602083106102f957805182526020820191506020810190506020830392506102d6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c05780601f10610395576101008083540402835291602001916103c0565b820191906000526020600020905b8154815290600101906020018083116103a357829003601f168201915b50505050509050919050565b806000836040518082805190602001908083835b6020831061040357805182526020820191506020810190506020830392506103e0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061044992919061044e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061048f57805160ff19168380011785556104bd565b828001600101855582156104bd579182015b828111156104bc5782518255916020019190600101906104a1565b5b5090506104ca91906104ce565b5090565b6104f091905b808211156104ec5760008160009055506001016104d4565b5090565b9056fea165627a7a7230582025d50863c1fea84c9ea588d75b7fdab2de3a9b9fb3bc0b58ec83b3259c04285d0029";
    static String abiWithConstructor = "[\n" +
            "  {\n" +
            "    \"constant\":true,\n" +
            "    \"inputs\":[\n" +
            "      {\n" +
            "        \"name\":\"key\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\":\"get\",\n" +
            "    \"outputs\":[\n" +
            "      {\n" +
            "        \"name\":\"\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\":false,\n" +
            "    \"stateMutability\":\"view\",\n" +
            "    \"type\":\"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\":false,\n" +
            "    \"inputs\":[\n" +
            "      {\n" +
            "        \"name\":\"key\",\n" +
            "        \"type\":\"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\":\"value\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\":\"set\",\n" +
            "    \"outputs\":[],\n" +
            "    \"payable\":false,\n" +
            "    \"stateMutability\":\"nonpayable\",\n" +
            "    \"type\":\"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"inputs\":[\n" +
            "      {\n" +
            "        \"name\":\"key\",\n" +
            "        \"type\":\"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\":\"value\",\n" +
            "        \"type\":\"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\":false,\n" +
            "    \"stateMutability\":\"nonpayable\",\n" +
            "    \"type\":\"constructor\"\n" +
            "  }\n" +
            "]";

    public static class deployAndSendBasicTransaction {
        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "key";
            String valueString = "valueString";

            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, keyString, valueString);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeSmartContractDeploy", deployed.getType());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "key";
            String valueString = "valueString";

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeSmartContractDeploy", deployed.getType());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions,"set", keyString, valueString);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }
    }

    public static class deployAndSendFeeDelegatedTransaction {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, keyString, valueString);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeploy", deployed.getType());
            assertEquals(feePayer, deployed.getFeePayer());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeploy", deployed.getType());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions,"set", keyString, valueString);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void throw_Exception_execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The fee payer value is not valid. feePayer address - " );

            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);
        }
    }

    public static class deployAndSendFeeDelegatedWithRatioTransaction {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            String keyString = "key";
            String valueString = "valueString";

            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, keyString, valueString);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeployWithRatio", deployed.getType());
            assertEquals(feePayer, deployed.getFeePayer());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
            assertEquals(deployed.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            String keyString = "key";
            String valueString = "valueString";

            TransactionReceipt.TransactionReceiptData deployed = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertNotNull(deployed.getContractAddress());
            assertEquals("0x1", deployed.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeployWithRatio", deployed.getType());
            assertEquals(deployed.getFrom(), sendOptions.getFrom());
            assertEquals(deployed.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions,"set", keyString, valueString);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void throwException_execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The fee payer value is not valid. feePayer address - " );

            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            String keyString = "contract";
            String valueString = "so convenient";

            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);
        }
    }

    public static class signBasicTransaction {
        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractDeploy.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithConstructor, keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractDeploy.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractDeploy.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractDeploy.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.sign(sendOptions,"set", keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractExecution.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractExecution.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.signWithSolidityType(sendOptions,"set", new Utf8String(keyString), new Utf8String(valueString));
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractExecution.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethodWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").signWithSolidityWrapper(Arrays.asList(new Utf8String(keyString), new Utf8String(valueString)), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeSmartContractExecution.toString(), transaction.getType());

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }
    }

    public static class signFeeDelegatedTransaction {
        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithConstructor, keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.sign(sendOptions,"set", keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.signWithSolidityType(sendOptions,"set", new Utf8String(keyString), new Utf8String(valueString));
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethodWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").signWithSolidityWrapper(Arrays.asList(new Utf8String(keyString), new Utf8String(valueString)), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }
    }

    public static class signFeeDelegatedTransactionWitRatio {
        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.sign(sendOptions, "constructor", byteCodeWithConstructor, keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "key";
            String valueString = "valueString";

            AbstractTransaction transaction = contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.sign(sendOptions,"set", keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.signWithSolidityType(sendOptions,"set", new Utf8String(keyString), new Utf8String(valueString));
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethodWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);
            sendOptions.setFeeRatio(BigInteger.TEN);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractTransaction transaction = contract.getMethod("set").signWithSolidityWrapper(Arrays.asList(new Utf8String(keyString), new Utf8String(valueString)), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());

            caver.wallet.signAsFeePayer(feePayer, (AbstractFeeDelegatedTransaction)transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(receiptData.getFeeRatio(), sendOptions.getFeeRatio());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }
    }

    public static class signAsFeePayerFeeDelegatedTransaction {
        static Caver caver;
        static String sender;
        static String feePayer;
        static String contractAddress;

        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            Contract contract = new Contract(caver, abiWithConstructor);
            Contract deployed = contract.deploy(new SendOptions(sender, BigInteger.valueOf(3000000)), byteCodeWithConstructor, "key", "value");

            contractAddress = deployed.getContractAddress();
        }

        @Test
        public void deploy_contract_withoutConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contract_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithConstructor, keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withoutConstructor() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithoutConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            AbstractFeeDelegatedTransaction transaction = contract.getMethod("constructor").signAsFeePayer(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void deploy_contractMethod_withConstructor() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "key";
            String valueString = "valueString";

            AbstractFeeDelegatedTransaction transaction = contract.getMethod("constructor").signAsFeePayer(Arrays.asList(byteCodeWithConstructor, keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
        }

        @Test
        public void execute_contract() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions,"set", keyString, valueString);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethod() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = contract.getMethod("set").signAsFeePayer(Arrays.asList(keyString, valueString), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayerWithSolidityType(sendOptions,"set", new Utf8String(keyString), new Utf8String(valueString));
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }

        @Test
        public void execute_contractMethodWithSolidityType() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract contract = new Contract(caver, abiWithConstructor, contractAddress);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = contract.getMethod("set").signAsFeePayerWithSolidityWrapper(Arrays.asList(new Utf8String(keyString), new Utf8String(valueString)), sendOptions);
            assertFalse(Utils.isEmptySig(transaction.getFeePayerSignatures()));
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());

            caver.wallet.sign(sender, transaction);
            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(transaction).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
            assertEquals(receiptData.getFrom(), sendOptions.getFrom());
            assertEquals(contractAddress, receiptData.getTo());

            String value = (String)contract.call("get", keyString).get(0).getValue();
            assertEquals(valueString, value);
        }
    }

    public static class FeeDelegationInDefaultSendOptions {
        static Caver caver;
        static Contract contract;
        static SendOptions sendOptions;
        static String sender;
        static String feePayer;
        static Contract deployedContract;


        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            contract = new Contract(caver, abiWithoutConstructor);
            contract.getDefaultSendOptions().setFeeDelegation(true);

            sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeePayer(feePayer);

            deployedContract = new Contract(caver, abiWithConstructor);
            deployedContract.getDefaultSendOptions().setFeeDelegation(true);
            deployedContract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");
        }

        @Test
        public void deploy_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeploy", receiptData.getType());
        }

        @Test
        public void execute_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.send(sendOptions, "set", keyString, valueString);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
        }

        @Test
        public void execute_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
        }

        @Test
        public void sign_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.sign(sendOptions, "set", keyString, valueString);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) deployedContract.sign(sendOptions, "set", keyString, valueString);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(sendOptions.getFeePayer(), transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }
    }

    public static class FeePayerInDefaultSendOptions {
        static Caver caver;
        static Contract contract;
        static SendOptions sendOptions;
        static String sender;
        static String feePayer;
        static Contract deployedContract;


        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            contract = new Contract(caver, abiWithoutConstructor);
            contract.getDefaultSendOptions().setFeePayer(feePayer);

            sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);

            deployedContract = new Contract(caver, abiWithConstructor);
            deployedContract.getDefaultSendOptions().setFeePayer(feePayer);
            deployedContract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");
        }

        @Test
        public void deploy_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeploy", receiptData.getType());
        }

        @Test
        public void execute_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.send(sendOptions, "set", keyString, valueString);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
        }

        @Test
        public void execute_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecution", receiptData.getType());
        }

        @Test
        public void sign_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.sign(sendOptions, "set", keyString, valueString);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) deployedContract.sign(sendOptions, "set", keyString, valueString);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }
    }

    public static class FeeRatioInDefaultSendOptions {
        static Caver caver;
        static Contract contract;
        static SendOptions sendOptions;
        static String sender;
        static String feePayer;
        static Contract deployedContract;


        @BeforeClass
        public static void init() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            contract = new Contract(caver, abiWithoutConstructor);
            contract.getDefaultSendOptions().setFeeRatio(BigInteger.TEN);

            sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(feePayer);

            deployedContract = new Contract(caver, abiWithConstructor);
            deployedContract.getDefaultSendOptions().setFeeRatio(BigInteger.TEN);
            deployedContract.deploy(sendOptions, byteCodeWithConstructor, "key", "value");
        }

        @Test
        public void deploy_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Contract deployed = contract.deploy(sendOptions, byteCodeWithoutConstructor);
            assertNotNull(deployed.getContractAddress());
        }

        @Test
        public void deploy_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            TransactionReceipt.TransactionReceiptData receiptData = contract.getMethod("constructor").send(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractDeployWithRatio", receiptData.getType());
        }

        @Test
        public void execute_contract() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.send(sendOptions, "set", keyString, valueString);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
        }

        @Test
        public void execute_contractMethod() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";
            TransactionReceipt.TransactionReceiptData receiptData = deployedContract.getMethod("set").send(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(deployedContract.getContractAddress(), receiptData.getTo());
            assertEquals(feePayer, receiptData.getFeePayer());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals("TxTypeFeeDelegatedSmartContractExecutionWithRatio", receiptData.getType());
        }

        @Test
        public void sign_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.sign(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) contract.getMethod("constructor").sign(Arrays.asList(byteCodeWithoutConstructor), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_deploy_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            AbstractFeeDelegatedTransaction transaction = contract.signAsFeePayer(sendOptions, "constructor", byteCodeWithoutConstructor);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(!Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.sign(sendOptions, "set", keyString, valueString);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void sign_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contract() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction) deployedContract.sign(sendOptions, "set", keyString, valueString);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }

        @Test
        public void signAsFeePayer_execute_contractMethod() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            String keyString = "contract";
            String valueString = "so convenient";

            AbstractFeeDelegatedTransaction transaction = (AbstractFeeDelegatedTransaction)deployedContract.getMethod("set").sign(Arrays.asList(keyString, valueString), sendOptions);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), transaction.getType());
            assertEquals(sendOptions.getFrom(), transaction.getFrom());
            assertEquals(feePayer, transaction.getFeePayer());
            assertTrue(!Utils.isEmptySig(transaction.getSignatures()));
            assertTrue(Utils.isEmptySig(transaction.getFeePayerSignatures()));
        }
    }
}
