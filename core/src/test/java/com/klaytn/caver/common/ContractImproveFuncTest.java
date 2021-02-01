package com.klaytn.caver.common;


import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * pragma solidity ^0.5.6;
 *
 * contract KVStore {
 *    mapping(string => string) storeString;
 *    mapping(string => uint256) storeUint;
 *    string symbol;
 *
 *    constructor(string memory _symbol) public {
 *        symbol = _symbol;
 *    }
 *
 *    function getSymbol() public view returns (string memory) {
 *        return symbol;
 *    }
 *
 *    function getString(string memory key) public view returns (string memory) {
 *        return storeString[key];
 *    }
 *
 *    function setString(string memory key, string memory value) public {
 *        storeString[key] = value;
 *    }
 *
 *    function getUint(string memory key) public view returns (uint256) {
 *        return storeUint[key];
 *    }
 *
 *    function setUint(string memory key, uint256 value) public {
 *        storeUint[key] = value;
 *    }
 * }
 */

public class ContractImproveFuncTest {
    static final String FUNC_SET_STRING = "setString";
    static final String FUNC_SET_UINT = "setUint";
    static final String FUNC_GET_STRING = "getString";
    static final String FUNC_GET_UINT = "getUint";
    static final String FUNC_GET_SYMBOL = "getSymbol";

    static final String BINARY = "608060405234801561001057600080fd5b50604051610a35380380610a358339810180604052602081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8281019050602081018481111561006157600080fd5b815185600182028301116401000000008211171561007e57600080fd5b5050929190505050806002908051906020019061009c9291906100a3565b5050610148565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100e457805160ff1916838001178555610112565b82800160010185558215610112579182015b828111156101115782518255916020019190600101906100f6565b5b50905061011f9190610123565b5090565b61014591905b80821115610141576000816000905550600101610129565b5090565b90565b6108de806101576000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063150704011461005c578063498bff00146100df57806356523acd146101ae5780636e1a1336146102735780639c981fcb146103c5575b600080fd5b6100646104f9565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100a4578082015181840152602081019050610089565b50505050905090810190601f1680156100d15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610198600480360360208110156100f557600080fd5b810190808035906020019064010000000081111561011257600080fd5b82018360208201111561012457600080fd5b8035906020019184600183028401116401000000008311171561014657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061059b565b6040518082815260200191505060405180910390f35b610271600480360360408110156101c457600080fd5b81019080803590602001906401000000008111156101e157600080fd5b8201836020820111156101f357600080fd5b8035906020019184600183028401116401000000008311171561021557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019092919050505061060e565b005b6103c36004803603604081101561028957600080fd5b81019080803590602001906401000000008111156102a657600080fd5b8201836020820111156102b857600080fd5b803590602001918460018302840111640100000000831117156102da57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561033d57600080fd5b82018360208201111561034f57600080fd5b8035906020019184600183028401116401000000008311171561037157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610680565b005b61047e600480360360208110156103db57600080fd5b81019080803590602001906401000000008111156103f857600080fd5b82018360208201111561040a57600080fd5b8035906020019184600183028401116401000000008311171561042c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610702565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104be5780820151818401526020810190506104a3565b50505050905090810190601f1680156104eb5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b606060028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105915780601f1061056657610100808354040283529160200191610591565b820191906000526020600020905b81548152906001019060200180831161057457829003601f168201915b5050505050905090565b60006001826040518082805190602001908083835b602083106105d357805182526020820191506020810190506020830392506105b0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b806001836040518082805190602001908083835b602083106106455780518252602082019150602081019050602083039250610622565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055505050565b806000836040518082805190602001908083835b602083106106b75780518252602082019150602081019050602083039250610694565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090805190602001906106fd92919061080d565b505050565b60606000826040518082805190602001908083835b6020831061073a5780518252602082019150602081019050602083039250610717565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108015780601f106107d657610100808354040283529160200191610801565b820191906000526020600020905b8154815290600101906020018083116107e457829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061084e57805160ff191683800117855561087c565b8280016001018555821561087c579182015b8281111561087b578251825591602001919060010190610860565b5b509050610889919061088d565b5090565b6108af91905b808211156108ab576000816000905550600101610893565b5090565b9056fea165627a7a723058207bffe25f9ec017dbe9a4f191bafd2f13252aa3e30263de201ea5fe4cc11542ed0029";
    static final String ABI = "[\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"getSymbol\",\n" +
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
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"key\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"getUint\",\n" +
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
            "        \"name\": \"key\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setUint\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"key\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"value\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"setString\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"key\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"getString\",\n" +
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
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"_symbol\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"constructor\"\n" +
            "  }\n" +
            "]";

    static String ownerPrivateKey = "0x871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
    static SingleKeyring ownerKeyring;
    static Caver caver;
    static Contract contract;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void init() throws Exception {
        caver = new Caver(Caver.DEFAULT_URL);
        ownerKeyring = KeyringFactory.createFromPrivateKey(ownerPrivateKey);
        caver.wallet.add(ownerKeyring);
        contract = new Contract(caver, ABI);
        contract.setDefaultSendOptions(new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(8000000)));

        ContractDeployParams deployParams = new ContractDeployParams(BINARY, Arrays.asList("CONTRACT_TEST"));
        contract.deploy(deployParams, new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(6500000)));

        storeStringData("Contract", "Call");
        storeUintData("Age", 2);

    }

    public static TransactionReceipt.TransactionReceiptData storeStringData(String key, String value) {
        try {
            SendOptions sendOptions = new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(500000));
            TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions, FUNC_SET_STRING, key, value);

            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            return receiptData;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return null;
    }

    public static TransactionReceipt.TransactionReceiptData storeUintData(String key, int value) {
        try {
            SendOptions sendOptions = new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(500000));
            TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions, FUNC_SET_UINT, key, value);

            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            return receiptData;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return null;
    }

    @Test
    public void deployTest() throws Exception {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(ownerKeyring);

        Contract contract = new Contract(caver, ABI);
        SendOptions sendOptions = new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(6500000));
        contract.deploy(sendOptions, BINARY, "TEST");

        assertNotNull(contract.getContractAddress());
    }

    @Test
    public void send_setString() {
        TransactionReceipt.TransactionReceiptData receiptData = storeStringData("CAVER", "TEST");
        assertEquals("0x1", receiptData.getStatus());
    }

    @Test
    public void send_setUint() {
        TransactionReceipt.TransactionReceiptData receiptData = storeUintData("CAVER", 2020);
        assertEquals("0x1", receiptData.getStatus());
    }

    @Test
    public void sendWithDefaultOption_setString() {
        try {
            TransactionReceipt.TransactionReceiptData receiptData = contract.send(FUNC_SET_STRING, "KLAYTN", "Block chain");
            assertEquals("0x1", receiptData.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void sendWithSolidityWrapper_setString() {
        try {
            TransactionReceipt.TransactionReceiptData receiptData = contract.sendWithSolidityType(FUNC_SET_STRING, new Utf8String("KLAY"), new Utf8String("GX"));
            assertEquals("0x1", receiptData.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void sendWithSolidityWrapper_setUint() {
        try {
            TransactionReceipt.TransactionReceiptData receiptData = contract.sendWithSolidityType(FUNC_SET_UINT, new Utf8String("DEV"), new Uint256(10));
            assertEquals("0x1", receiptData.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void call_getSymbol() {
        try {
            List<Type> result = contract.call(FUNC_GET_SYMBOL);
            assertEquals("CONTRACT_TEST", ((Utf8String) result.get(0)).getValue());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void callWithSolidityWrapper_getSymbol() {
        try {
            List<Type> result = contract.callWithSolidityType(FUNC_GET_SYMBOL);
            assertEquals("CONTRACT_TEST", ((Utf8String) result.get(0)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void call_getString() {
        try {
            List<Type> result = contract.call(FUNC_GET_STRING, "Contract");
            assertEquals("Call", ((Utf8String) result.get(0)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void callWithSolidityWrapper_getString() {
        try {
            List<Type> result = contract.callWithSolidityType(FUNC_GET_STRING, new Utf8String("Contract"));
            assertEquals("Call", ((Utf8String) result.get(0)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void call_getUint() {
        try {
            List<Type> result = contract.call(FUNC_GET_UINT, "Age");
            assertEquals(2, ((Uint256) result.get(0)).getValue().intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void callWithSolidityWrapper_getUint() {
        try {
            List<Type> result = contract.callWithSolidityType(FUNC_GET_UINT, new Utf8String("Age"));
            assertEquals(2, ((Uint256) result.get(0)).getValue().intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void estimateGas() {
        try {
            CallObject callObject = CallObject.createCallObject(ownerKeyring.getAddress());
            String gas = contract.estimateGas(callObject, FUNC_SET_STRING, "ESTIMATE", "GAS");
            assertNotNull(gas);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void estimateGasWithSolidityType() {
        try {
            CallObject callObject = CallObject.createCallObject(ownerKeyring.getAddress());
            String gas = contract.estimateGasWithSolidityType(callObject, FUNC_SET_STRING, new Utf8String("ESTIMATE"), new Utf8String("GAS"));
            assertNotNull(gas);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void encodeABI() {
        try {
            String expected = contract.getMethod(FUNC_SET_STRING).encodeABI(Arrays.asList("ENCODE", "FUNCTION"));
            String actual = contract.encodeABI(FUNC_SET_STRING, "ENCODE", "FUNCTION");

            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void encodeABIWithSolidityType() {
        try {
            String expected = contract.getMethod(FUNC_SET_STRING).encodeABIWithSolidityWrapper(Arrays.asList(new Utf8String("ENCODE"), new Utf8String("FUNCTION")));
            String actual = contract.encodeABIWithSolidityType(FUNC_SET_STRING, new Utf8String("ENCODE"), new Utf8String("FUNCTION"));

            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void sendRawTx_throwException_send() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("intrinsic gas too low");

        SendOptions sendOptions = new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(500));
        TransactionReceipt.TransactionReceiptData receiptData = contract.send(sendOptions, FUNC_SET_STRING, "GAS", "ENOUGH");

        if(!receiptData.getStatus().equals("0x1")) {
            fail();
        }
    }

    @Test
    public void sendRawTx_throwException_deploy() throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("intrinsic gas too low");

        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(ownerKeyring);

        Contract contract = new Contract(caver, ABI);
        SendOptions sendOptions = new SendOptions(ownerKeyring.getAddress(), BigInteger.valueOf(6500));
        contract.deploy(sendOptions, BINARY, "TEST");

        assertNotNull(contract.getContractAddress());
    }
}
