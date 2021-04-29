package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SmartContractDeployTest {

    public static final String BYTECODE = "0x60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb0029";

    static Caver caver = new Caver(Caver.DEFAULT_URL);

    static String nonce = "0x1f";
    static String gas = "0xdbba0";
    static String gasPrice = "0x5d21dba00";
    static String from = "0xd91aec35bea25d379e49cfe2dff5f5775cdac1a3";
    static String chainID = "0x7e3";
    static String value = "0x0";
    static String input = BYTECODE;
    static boolean humanReadable = false;
    static String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);

    static String expectedRlpEncodingForSigning = "0xf90241b90239f90236281f8505d21dba00830dbba0808094d91aec35bea25d379e49cfe2dff5f5775cdac1a3b9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb002980808207e38080";
    static String expectedTransactionHash = "0x523417d946221c4d12b58519580edca43662577df7e107f5ff92f115d4b3d210";
    static String expectedRLPEncoding = "0x28f9027e1f8505d21dba00830dbba0808094d91aec35bea25d379e49cfe2dff5f5775cdac1a3b9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a0018a9f680a74e275f1f83a5c2c45e1313c52432df4595e944240b1511a4f4ba7a02d762c3417f91b81db4907db832cb28cc64df7dca3ea9be64899ab3f4812f016";

    static SignatureData signatureData = new SignatureData(
            Numeric.hexStringToByteArray("0x0fe9"),
            Numeric.hexStringToByteArray("0x018a9f680a74e275f1f83a5c2c45e1313c52432df4595e944240b1511a4f4ba7"),
            Numeric.hexStringToByteArray("0x2d762c3417f91b81db4907db832cb28cc64df7dca3ea9be64899ab3f4812f016")
    );

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        String[][] keyArr = new String[3][];

        for(int i = 0; i < numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j = 0; j < length; j++) {
                arr[j] = PrivateKey.generate("entropy").getPrivateKey();
            }
            keyArr[i] = arr;
        }

        List<String[]> arr = Arrays.asList(keyArr);

        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void BuilderTest() {
            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .setSignatures(signatureData)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setGas(gas)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
            assertEquals(value, txObj.getValue());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(null)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue(value)
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setValue((String) null)
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(null)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(null)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();

        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("'to' field must be nil('0x') : ");

            String to = "invalid";

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setTo(to)
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();
        }

        @Test
        public void throwException_invalidHumanReadable() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("HumanReadable attribute must set false");

            boolean humanReadable = true;

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat(codeFormat)
                    .build();

        }

        @Test
        public void throwException_invalidCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("CodeFormat attribute only support EVM(0)");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat("1")
                    .build();
        }

        @Test
        public void throwException_missingCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("codeFormat is missing");

            SmartContractDeploy txObj = new SmartContractDeploy.Builder()
                    .setNonce(nonce)
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setValue(Numeric.toBigInt(value))
                    .setFrom(from)
                    .setInput(input)
                    .setHumanReadable(humanReadable)
                    .setCodeFormat((String) null)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createInstance() {
            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid value");

            String value = "invalid value";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_missingValue() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("value is missing.");

            String value = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid input");

            String input = "invalid input";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_missingInput() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("input is missing.");

            String input = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidTo() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("'to' field must be nil('0x') : ");

            String to = "invalid address";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setTo(to)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidHumanReadable() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("HumanReadable attribute must set false");

            boolean humanReadable = true;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_invalidCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("CodeFormat attribute only support EVM(0)");

            String codeFormat = "1";

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }

        @Test
        public void throwException_missingCodeFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("codeFormat is missing");

            String codeFormat = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            SmartContractDeploy txObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            assertEquals(expectedRLPEncoding, txObj.getRLPEncoding());
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";

        String expectedRLPEncoding = "0x28f9027e1f8505d21dba00830dbba0808094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a03a6324d855fa9326e52fc09420eb3194334d81b68e4c94847547e1ee681506f8a04eacfc4c04497cdcd79a28c8165ef824686c399ea6453b7e878d69ab6fcece5b";

        SmartContractDeploy mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKey
            );
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void signWithKey_Keyring() throws IOException {
            mTxObj.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring, 0);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_Only() throws IOException {
            mTxObj.sign(privateKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KlayWalletKey() throws IOException {
            mTxObj.sign(klaytnWalletKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring role = generateRoleBaseKeyring(new int[]{3, 3, 3}, from);
            mTxObj.sign(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";

        String expectedRLPEncoding = "0x28f9027e1f8505d21dba00830dbba0808094a94f5374fce5edbc8e2a8697c15331677e6ebf0bb9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a03a6324d855fa9326e52fc09420eb3194334d81b68e4c94847547e1ee681506f8a04eacfc4c04497cdcd79a28c8165ef824686c399ea6453b7e878d69ab6fcece5b";

        SmartContractDeploy mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;

        @Before
        public void before() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
            );

            coupledKeyring = caver.wallet.keyring.createFromPrivateKey(privateKey);
            deCoupledKeyring = caver.wallet.keyring.createWithSingleKey(
                    caver.wallet.keyring.generate().getAddress(),
                    privateKey
            );
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            mTxObj.sign(privateKey);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            AbstractKeyring roleBased = generateRoleBaseKeyring(new int[]{3, 3, 3}, from);

            mTxObj.sign(roleBased);
            assertEquals(3, mTxObj.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractDeploy mTxObj;

        @Before
        public void before() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
            );
        }


        @Test
        public void appendSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
                            .setSignatures(emptySignature)
            );

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj.appendSignatures(list);
            assertEquals(2, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
        }

        @Test
        public void appendSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            SignatureData signatureData2 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x9a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0xa3ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);
            list.add(signatureData2);

            mTxObj.appendSignatures(list);
            assertEquals(3, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
            assertEquals(signatureData2, mTxObj.getSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x47a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfa";
        BigInteger value = BigInteger.ZERO;
        BigInteger gas = BigInteger.valueOf(900000);
        String input = BYTECODE;
        String gasPrice = "0x5d21dba00";
        BigInteger chainId = BigInteger.valueOf(2019);
        String nonce = "0x1";


        SmartContractDeploy mTxObj;

        @Test
        public void combineSignature() {
            SignatureData expectedSignature = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0x4fbefaf9da3be278403c0b69861747a4f2f530958c5f3da0b7fed8898aa02a9d"),
                    Numeric.hexStringToByteArray("0x10b441518b74b9cb15d86403a4c59a562a4669bc9c878d77276f0205304c3d85")
            );

            String rlpEncoded = "0x28f9027e018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a04fbefaf9da3be278403c0b69861747a4f2f530958c5f3da0b7fed8898aa02a9da010b441518b74b9cb15d86403a4c59a562a4669bc9c878d77276f0205304c3d85";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
            );
            String combined = mTxObj.combineSignedRawTransactions(list);

            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0x4fbefaf9da3be278403c0b69861747a4f2f530958c5f3da0b7fed8898aa02a9d"),
                    Numeric.hexStringToByteArray("0x10b441518b74b9cb15d86403a4c59a562a4669bc9c878d77276f0205304c3d85")
            );

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            String[] rlpEncodedString = new String[]{
                    "0x28f9027e018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a06f59d699a5dd22a653b0ed1e39cbfc52ee468607eec95b195f302680ed7f9815a03b2f3f2a7a9482edfbcc9ee8e003e284b6c4a7ecbc8d361cc486562d4bdda389",
                    "0x28f9027e018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820feaa04a76af831891d1050d1a0c8e7656b4ab1952ef6a1059bff994edb29a6936a909a06affc6457e9c553c5efb138a7a56dbcbed681a5bae2dceff02848341a61ab9c4"
            };

            SignatureData[] expectedSignature = new SignatureData[]{
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x4fbefaf9da3be278403c0b69861747a4f2f530958c5f3da0b7fed8898aa02a9d"),
                            Numeric.hexStringToByteArray("0x10b441518b74b9cb15d86403a4c59a562a4669bc9c878d77276f0205304c3d85")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x6f59d699a5dd22a653b0ed1e39cbfc52ee468607eec95b195f302680ed7f9815"),
                            Numeric.hexStringToByteArray("0x3b2f3f2a7a9482edfbcc9ee8e003e284b6c4a7ecbc8d361cc486562d4bdda389")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x4a76af831891d1050d1a0c8e7656b4ab1952ef6a1059bff994edb29a6936a909"),
                            Numeric.hexStringToByteArray("0x6affc6457e9c553c5efb138a7a56dbcbed681a5bae2dceff02848341a61ab9c4")
                    )
            };

            String expectedRLPEncoded = "0x28f9030c018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f8d5f845820fe9a04fbefaf9da3be278403c0b69861747a4f2f530958c5f3da0b7fed8898aa02a9da010b441518b74b9cb15d86403a4c59a562a4669bc9c878d77276f0205304c3d85f845820fe9a06f59d699a5dd22a653b0ed1e39cbfc52ee468607eec95b195f302680ed7f9815a03b2f3f2a7a9482edfbcc9ee8e003e284b6c4a7ecbc8d361cc486562d4bdda389f845820feaa04a76af831891d1050d1a0c8e7656b4ab1952ef6a1059bff994edb29a6936a909a06affc6457e9c553c5efb138a7a56dbcbed681a5bae2dceff02848341a61ab9c4";

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            String value = "0x1000";

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(false)
                            .setCodeFormat(codeFormat)
            );

            String rlpEncoded = "0x28f9027e018505d21dba00830dbba080809447a4caa81fe2ed8cc834aafe5b1d7ee3ddedecfab9020e60806040526000805534801561001457600080fd5b506101ea806100246000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd1461007257806342cbb15c1461009d578063767800de146100c8578063b22636271461011f578063d14e62b814610150575b600080fd5b34801561007e57600080fd5b5061008761017d565b6040518082815260200191505060405180910390f35b3480156100a957600080fd5b506100b2610183565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61018b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561012b57600080fd5b5061014e60048036038101908080356000191690602001909291905050506101b1565b005b34801561015c57600080fd5b5061017b600480360381019080803590602001909291905050506101b4565b005b60005481565b600043905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b80600081905550505600a165627a7a7230582053c65686a3571c517e2cf4f741d842e5ee6aa665c96ce70f46f9a594794f11eb00298080f847f845820fe9a06f59d699a5dd22a653b0ed1e39cbfc52ee468607eec95b195f302680ed7f9815a03b2f3f2a7a9482edfbcc9ee8e003e284b6c4a7ecbc8d361cc486562d4bdda389";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRawTransaction() {
            String rawTx = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            ).getRawTransaction();
            assertEquals(expectedRLPEncoding, rawTx);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined.");

            caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            ).getRawTransaction();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined.");

            caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            ).getRawTransaction();
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractDeploy mTxObj;

        @Test
        public void getTransactionHash() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            String txHash = mTxObj.getTransactionHash();
            assertEquals(expectedTransactionHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractDeploy mTxObj;

        @Test
        public void getSenderTransactionHash() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            String txHash = mTxObj.getSenderTxHash();
            assertEquals(expectedTransactionHash, txHash);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        SmartContractDeploy mTxObj;

        @Test
        public void getRLPEncodingForSignature() {
            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            String rlp = mTxObj.getRLPEncodingForSignature();
            assertEquals(expectedRlpEncodingForSigning, rlp);
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setChainId(chainID)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setValue(value)
                            .setFrom(from)
                            .setInput(input)
                            .setHumanReadable(humanReadable)
                            .setCodeFormat(codeFormat)
                            .setSignatures(signatureData)
            );

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
