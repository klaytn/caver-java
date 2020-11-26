package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ContractOverloadFunctionsTest {
    static final String BYTECODE = "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a3610905806100dc6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c8063145ef6e9146100675780633b710f8c146100ea57806356df3db1146101c5578063893d20e814610213578063a6f9dae11461025d578063a7db555b146102a1575b600080fd5b61006f6102bf565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100af578082015181840152602081019050610094565b50505050905090810190601f1680156100dc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101c36004803603604081101561010057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561013d57600080fd5b82018360208201111561014f57600080fd5b8035906020019184600183028401116401000000008311171561017157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610361565b005b610211600480360360408110156101db57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506104f9565b005b61021b610681565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61029f6004803603602081101561027357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506106aa565b005b6102a961082a565b6040518082815260200191505060405180910390f35b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103575780601f1061032c57610100808354040283529160200191610357565b820191906000526020600020905b81548152906001019060200180831161033a57829003601f168201915b5050505050905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610423576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f43616c6c6572206973206e6f74206f776e65720000000000000000000000000081525060200191505060405180910390fd5b8173ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a3816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600190805190602001906104f4929190610834565b505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146105bb576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f43616c6c6572206973206e6f74206f776e65720000000000000000000000000081525060200191505060405180910390fd5b8173ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a3816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806002819055505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461076c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f43616c6c6572206973206e6f74206f776e65720000000000000000000000000081525060200191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000600254905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061087557805160ff19168380011785556108a3565b828001600101855582156108a3579182015b828111156108a2578251825591602001919060010190610887565b5b5090506108b091906108b4565b5090565b6108d691905b808211156108d25760008160009055506001016108ba565b5090565b9056fea165627a7a723058201024bd6f9780fc8257377e0719756900b6734eff21bdb2715e228c67a864f7600029";
    static final String ABIJson = "[\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"getOwnerName\",\n" +
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
            "        \"name\": \"newOwner\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"name\",\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"changeOwner\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"newOwner\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"number\",\n" +
            "        \"type\": \"uint256\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"changeOwner\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"getOwner\",\n" +
            "    \"outputs\": [\n" +
            "      {\n" +
            "        \"name\": \"\",\n" +
            "        \"type\": \"address\"\n" +
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
            "        \"name\": \"newOwner\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"changeOwner\",\n" +
            "    \"outputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"getOwnerNumber\",\n" +
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
            "    \"inputs\": [],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"constructor\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"oldOwner\",\n" +
            "        \"type\": \"address\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"indexed\": true,\n" +
            "        \"name\": \"newOwner\",\n" +
            "        \"type\": \"address\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"name\": \"OwnerSet\",\n" +
            "    \"type\": \"event\"\n" +
            "  }\n" +
            "]";

    public static String contractAddress = "";
    public static String ownerPrivateKey = "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3";

    @BeforeClass
    public static void deploy() {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey(ownerPrivateKey));

        SendOptions options = new SendOptions(LUMAN.getAddress(), DefaultGasProvider.GAS_LIMIT);
        ContractDeployParams contractDeployParam = new ContractDeployParams(BYTECODE);

        try {
            Contract contract = new Contract(caver, ABIJson);
            contract.deploy(contractDeployParam, options);
            contractAddress = contract.getContractAddress();
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void overloadFunctionTest() {
        String methodName = "changeOwner";

        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey(ownerPrivateKey));

        SendOptions options = new SendOptions(LUMAN.getAddress(), DefaultGasProvider.GAS_LIMIT);

        try {
            Contract contract = new Contract(caver, ABIJson, contractAddress);
            contract.getMethod(methodName).sendWithSolidityWrapper(Arrays.asList(new Address(LUMAN.getAddress()), new Utf8String("LUMAN")), options);
            List<Type> result = contract.getMethod("getOwnerName").call(null, CallObject.createCallObject());
            assertEquals("LUMAN", result.get(0).getValue());

            contract.getMethod(methodName).sendWithSolidityWrapper(Arrays.asList(new Address(LUMAN.getAddress()), new Uint256(BigInteger.valueOf(1234))), options);
            result = contract.getMethod("getOwnerName").call(null, CallObject.createCallObject());
            System.out.println(result.get(0).getValue());

            result = contract.getMethod("getOwnerNumber").call(null, CallObject.createCallObject());
            assertEquals(1234, ((BigInteger)result.get(0).getValue()).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void callWithSolidityWrapperTest() {
        String methodName = "getOwner";

        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey(ownerPrivateKey));

        try {
            Contract contract = new Contract(caver, ABIJson, contractAddress);

            List<Type> result = contract.getMethod(methodName).callWithSolidityWrapper(null, CallObject.createCallObject());
            assertEquals(LUMAN.getAddress(), ((Address)result.get(0)).getValue());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void callWithSolidityWrapperNoCallObjectTest() {
        String methodName = "getOwner";

        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey(ownerPrivateKey));

        try {
            Contract contract = new Contract(caver, ABIJson, contractAddress);

            List<Type> result = contract.getMethod(methodName).callWithSolidityWrapper(null);
            assertEquals(LUMAN.getAddress(), ((Address)result.get(0)).getValue());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void encodeABIWithSolidityWrapperTest() {
        String methodName = "changeOwner";

        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey(ownerPrivateKey));

        SendOptions options = new SendOptions(LUMAN.getAddress(), DefaultGasProvider.GAS_LIMIT);

        try {
            Contract contract = new Contract(caver, ABIJson, contractAddress);
            String expected = contract.getMethod(methodName).encodeABI(Arrays.asList(LUMAN.getAddress()));
            String actual = contract.getMethod(methodName).encodeABIWithSolidityWrapper(Arrays.asList(new Address(LUMAN.getAddress())));

            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
