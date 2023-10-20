/*
 * Copyright 2022 The caver-java Authors
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

package com.klaytn.caver.common.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.account.*;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Account;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.KlayRewards.BlockRewards;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.utils.AccessTuple;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static com.klaytn.caver.base.LocalValues.LOCAL_NETWORK_ID;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class RpcTest extends Accounts {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static Klay klay = caver.rpc.klay;
    static Web3jService web3jService = caver.getRpc().getWeb3jService();
    static KIP17 kip17;

    public static void importKey(String privateKey, String password) throws IOException {
        new Request<>(
                "personal_importRawKey",
                Arrays.asList(privateKey, password),
                web3jService,
                Bytes20.class
        ).send();
    }

    public static void unlockAccount(String address, String password) throws IOException {
        new Request<>(
                "personal_unlockAccount",
                Arrays.asList(address, password),
                web3jService,
                Boolean.class
        ).send();
    }

    public static void deployContract() throws Exception {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));

        KIP17DeployParams kip7DeployParam = new KIP17DeployParams("CONTRACT_NAME", "CONTRACT_SYMBOL");
        kip17 = KIP17.deploy(caver, kip7DeployParam, LUMAN.getAddress());
    }

    public static class encodeAccountKeyTest {
        @Test
        public void withAccountKeyNil() throws IOException {
            String expected = "0x80";

            Request request = klay.encodeAccountKey(new AccountKeyNil());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyFail() throws IOException {
            String expected = "0x03c0";

            Request request = klay.encodeAccountKey(new AccountKeyFail());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyLegacy() throws IOException {
            String expected = "0x01c0";

            Request request = klay.encodeAccountKey(new AccountKeyLegacy());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyPublic() throws IOException {
            String expected = "0x02a102dbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8";

            String x = "0xdbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8";
            String y = "0x906d7170ba349c86879fb8006134cbf57bda9db9214a90b607b6b4ab57fc026e";
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromXYPoint(x, y);

            Request request = klay.encodeAccountKey(accountKeyPublic);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyWeightedMultiSig() throws IOException {
            String expected = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(expected);

            Request request = klay.encodeAccountKey(multiSig);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyRoleBased() throws IOException {
            String expected = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.decode(expected);

            Request request = klay.encodeAccountKey(accountKeyRoleBased);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }
    }

    public static class decodeAccountKeyTest {
        @Test
        public void withAccountKeyNil() throws IOException {
            String encodedStr = "0x80";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyNil);
        }

        @Test
        public void withAccountKeyFail() throws IOException {
            String encodedStr = "0x03c0";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyFail);
        }

        @Test
        public void withAccountKeyLegacy() throws IOException {
            String encodedStr = "0x01c0";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyLegacy);
        }

        @Test
        public void withAccountKeyPublic() throws IOException {
            String encodedStr = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyPublic);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyWeightedMultiSig() throws IOException {
            String encodedStr = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyWeightedMultiSig);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyRoleBased() throws IOException {
            String encodedStr = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKey accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyRoleBased);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }
    }

    public static class signTransactionTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);

        @BeforeClass
        public static void unlockAccount() throws IOException {
            RpcTest.importKey(klayProvider, "mypassword");
            RpcTest.unlockAccount(klayProviderKeyring.getAddress(), "mypassword");
        }

        @Test
        public void valueTransferTest() throws IOException {
            String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String nonce;
            String value = "0xa";

            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(to)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setValue(value)
                    .build();

            valueTransfer.sign(klayProviderKeyring);

            SignTransaction signTransaction = klay.signTransaction(valueTransfer).send();
            assertEquals(signTransaction.getResult().getRaw(), valueTransfer.getRLPEncoding());
        }
    }

    public static class signEthereumTransactionTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);

        @BeforeClass
        public static void unlockAccount() throws IOException {
            RpcTest.importKey(klayProvider, "mypassword");
            RpcTest.unlockAccount(klayProviderKeyring.getAddress(), "mypassword");
        }

        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String nonce;
        String value = "0xa";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        @Test
        public void ethereumAccessListTest() throws IOException {
            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                    .setTo(to)
                    .setGas(gas)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setValue(value)
                    .setAccessList(accessList)
            );

            ethereumAccessList.sign(klayProviderKeyring);

            SignTransaction signTransaction = klay.signTransaction(ethereumAccessList).send();
            assertEquals(signTransaction.getResult().getRaw(), ethereumAccessList.getRLPEncoding());
        }

        @Test
        public void ethereumDynamicFeeTest() throws IOException {
            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                    .setTo(to)
                    .setGas(gas)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setValue(value)
                    .setAccessList(accessList)
            );

            ethereumDynamicFee.sign(klayProviderKeyring);

            SignTransaction signTransaction = klay.signTransaction(ethereumDynamicFee).send();
            assertEquals(signTransaction.getResult().getRaw(), ethereumDynamicFee.getRLPEncoding());
        }
    }

    public static class sendAndGetEthereumTransactionTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);

        @BeforeClass
        public static void unlockAccount() throws IOException {
            RpcTest.importKey(klayProvider, "mypassword");
            RpcTest.unlockAccount(klayProviderKeyring.getAddress(), "mypassword");
        }

        String to = "0x7b65b75d204abed71587c9e519a89277766ee1d0";
        String gas = "0xf4240";
        String value = "0xa";
        AccessList accessList = new AccessList(
                Arrays.asList(
                        new AccessTuple(
                                "0x67116062f1626f7b3019631f03d301b8f701f709",
                                Arrays.asList(
                                        "0x0000000000000000000000000000000000000000000000000000000000000003",
                                        "0x0000000000000000000000000000000000000000000000000000000000000007"
                                )
                        ))
        );

        private void checkTransactionFields(Transaction.TransactionData transactionData, boolean isDynamicFee) {
            Assert.assertEquals(to, transactionData.getTo());
            Assert.assertEquals(gas, transactionData.getGas());
            Assert.assertEquals(accessList, transactionData.getAccessList());
        }

        private void checkTransactionReceiptFields(TransactionReceipt.TransactionReceiptData transactionReceiptData, boolean isDynamicFee) {
            Assert.assertEquals(to, transactionReceiptData.getTo());
            Assert.assertEquals(gas, transactionReceiptData.getGas());
            Assert.assertEquals(accessList, transactionReceiptData.getAccessList());
        }

       @Test
        public void ethereumAccessListTest() throws IOException, InterruptedException {
            String nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            EthereumAccessList ethereumAccessList = caver.transaction.ethereumAccessList.create(
                    TxPropertyBuilder.ethereumAccessList()
                    .setTo(to)
                    .setGas(gas)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setValue(value)
                    .setAccessList(accessList)
            );

            ethereumAccessList.sign(klayProviderKeyring);

            Bytes32 result = klay.sendTransaction(ethereumAccessList).send();
            String transactionHash = result.getResult();
            Thread.sleep(2000);
            Transaction transaction = klay.getTransactionByHash(transactionHash).send();
            checkTransactionFields(transaction.getResult(), false);
            TransactionReceipt transactionReceipt = klay.getTransactionReceipt(transactionHash).send();
            checkTransactionReceiptFields(transactionReceipt.getResult(), false);
            Assert.assertEquals(transactionHash, transactionReceipt.getResult().getTransactionHash());
        }


        @Test
        public void ethereumDynamicFeeTest() throws IOException, InterruptedException {
            String nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            EthereumDynamicFee ethereumDynamicFee = caver.transaction.ethereumDynamicFee.create(
                    TxPropertyBuilder.ethereumDynamicFee()
                    .setTo(to)
                    .setGas(gas)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setValue(value)
                    .setAccessList(accessList)
            );

            ethereumDynamicFee.sign(klayProviderKeyring);

            Bytes32 result = klay.sendTransaction(ethereumDynamicFee).send();
            String transactionHash = result.getResult();
            Thread.sleep(2000);
            Transaction transaction = klay.getTransactionByHash(transactionHash).send();
            checkTransactionFields(transaction.getResult(), true);
            TransactionReceipt transactionReceipt = klay.getTransactionReceipt(transactionHash).send();
            checkTransactionReceiptFields(transactionReceipt.getResult(), true);
            Assert.assertEquals(transactionHash, transactionReceipt.getResult().getTransactionHash());
        }
    }

    public static class signTransactionAsFeePayerTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static String feePayer = "1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);
        static SingleKeyring feePayerKeyring = KeyringFactory.createFromPrivateKey(feePayer);

        @BeforeClass
        public static void unlockAccount() throws IOException {
            RpcTest.importKey(klayProvider, "mypassword");
            RpcTest.importKey(feePayer, "mypassword");
            RpcTest.unlockAccount(klayProviderKeyring.getAddress(), "mypassword");
            RpcTest.unlockAccount(feePayerKeyring.getAddress(), "mypassword");

            Caver caver = new Caver(Caver.DEFAULT_URL);

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(feePayerKeyring.getAddress())
                    .setValue(Convert.toPeb("100", Convert.Unit.KLAY).toBigInteger())
                    .setGas("0xf4240")
                    .build();

            valueTransfer.sign(klayProviderKeyring);
            klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
        }

        @Test
        public void feeDelegatedValueTransferTest() throws IOException {
            String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String nonce;
            String value = "0xa";

            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            FeeDelegatedValueTransfer valueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(to)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setValue(value)
                    .setChainId(chainId)
                    .setFeePayer(feePayerKeyring.getAddress())
                    .build();
            valueTransfer.signAsFeePayer(feePayerKeyring);

            SignTransaction signTransaction = klay.signTransactionAsFeePayer(valueTransfer).send();

            assertEquals(signTransaction.getResult().getRaw(), valueTransfer.getRLPEncoding());

        }

        @Test
        public void feeDelegatedValueTransferFeeRatioTest() throws IOException {
            String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
            String gas = "0xf4240";
            String gasPrice = "0x19";
            String nonce;
            String value = "0xa";
            BigInteger feeRatio = BigInteger.valueOf(30);

            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            String chainId = klay.getChainID().send().getResult();

            FeeDelegatedValueTransferWithRatio valueTransfer = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(to)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFeePayer(feePayerKeyring.getAddress())
                    .setFeeRatio(feeRatio)
                    .build();

            valueTransfer.signAsFeePayer(feePayerKeyring);
            SignTransaction signTransaction = klay.signTransactionAsFeePayer(valueTransfer).send();
            assertEquals(valueTransfer.getRLPEncoding(), signTransaction.getResult().getRaw());
        }
    }

    public static class sendTransactionAsFeePayerTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static String feePayer = "1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);
        static SingleKeyring feePayerKeyring = KeyringFactory.createFromPrivateKey(feePayer);

        @BeforeClass
        public static void unlockAccount() throws IOException, InterruptedException {
            RpcTest.importKey(klayProvider, "mypassword");
            RpcTest.importKey(feePayer, "mypassword");
            RpcTest.unlockAccount(klayProviderKeyring.getAddress(), "mypassword");
            RpcTest.unlockAccount(feePayerKeyring.getAddress(), "mypassword");

            Caver caver = new Caver(Caver.DEFAULT_URL);

            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(feePayerKeyring.getAddress())
                    .setValue(Convert.toPeb("100", Convert.Unit.KLAY).toBigInteger())
                    .setGas("0xf4240")
                    .build();

            valueTransfer.sign(klayProviderKeyring);
            String txHash = klay.sendRawTransaction(valueTransfer.getRawTransaction()).send().getResult();

            for(int i=0; i< 30; i++) {
                TransactionReceipt transactionReceipt = klay.getTransactionReceipt(txHash).send();
                if(transactionReceipt == null) {
                    Thread.sleep(1000);
                } else {
                    break;
                }
            }
        }

        @Test
        public void feeDelegatedValueTransferTest() throws IOException {
            String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
            String gas = Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(350000));
            String gasPrice = "0x19";
            String nonce;
            String value = "0xa";

            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            gasPrice = klay.getGasPrice().send().getResult();
            String chainId = klay.getChainID().send().getResult();

            FeeDelegatedValueTransfer valueTransfer = new FeeDelegatedValueTransfer.Builder()
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(to)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setValue(value)
                    .setChainId(chainId)
                    .setFeePayer(feePayerKeyring.getAddress())
                    .build();
            valueTransfer.sign(klayProviderKeyring);
            String transactionHash = klay.sendTransactionAsFeePayer(valueTransfer).send().getResult();

            valueTransfer.signAsFeePayer(feePayerKeyring);
            assertEquals(transactionHash, valueTransfer.getTransactionHash());
        }

        @Test
        public void feeDelegatedValueTransferFeeRatioTest() throws IOException {
            String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
            String gas = Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(550000));
            String gasPrice = "0x19"; // 25ston
            String nonce;
            String value = "0xa";
            BigInteger feeRatio = BigInteger.valueOf(30);

            nonce = klay.getTransactionCount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send().getResult();
            gasPrice = klay.getGasPrice().send().getResult();
            String chainId = klay.getChainID().send().getResult();

            FeeDelegatedValueTransferWithRatio valueTransfer = new FeeDelegatedValueTransferWithRatio.Builder()
                    .setFrom(klayProviderKeyring.getAddress())
                    .setTo(to)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setChainId(chainId)
                    .setValue(value)
                    .setFeePayer(feePayerKeyring.getAddress())
                    .setFeeRatio(feeRatio)
                    .build();

            valueTransfer.sign(klayProviderKeyring);
            String transactionHash = klay.sendTransactionAsFeePayer(valueTransfer).send().getResult();

            valueTransfer.signAsFeePayer(feePayerKeyring);

            assertEquals(transactionHash, valueTransfer.getTransactionHash());
        }
    }

    public static class getAccountKeyTest {
        @Test
        public void getAccountKeyResponseTest() throws IOException {
            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            String testData = "{\"jsonrpc\":\"2.0\",\"id\":8,\"result\":{\"keyType\":2,\"key\":{\"x\":\"0x125b54c0500b2090d9b7504b010d5ee83962f19ca36cf592d5a798d7bc6d94d0\",\"y\":\"0x74dc3e8e8e7def04087010717522f3f1bbebb56c3030fa55853d05c435227cf\"}}}";

            AccountKey account = mapper.readValue(testData, AccountKey.class);
            assertEquals("0x125b54c0500b2090d9b7504b010d5ee83962f19ca36cf592d5a798d7bc6d94d0074dc3e8e8e7def04087010717522f3f1bbebb56c3030fa55853d05c435227cf",
                    ((AccountKeyPublic)account.getResult().getAccountKey()).getPublicKey());
        }
    }

    public static class otherRPCTest {
        static TransactionReceipt.TransactionReceiptData sampleReceiptData;

        private static TransactionReceipt.TransactionReceiptData sendKlay() throws IOException, TransactionException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            AbstractKeyring keyring = caver.wallet.add(KeyringFactory.createFromPrivateKey("0x871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5"));

            BigInteger value = new BigInteger(Utils.convertToPeb(BigDecimal.ONE, "KLAY"));

            //Create a value transfer transaction
            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setFrom(keyring.getAddress())
                    .setTo("0x8084fed6b1847448c24692470fc3b2ed87f9eb47")
                    .setValue(value)
                    .setGas(BigInteger.valueOf(25000))
                    .build();

            //Sign to the transaction
            valueTransfer.sign(keyring);

            //Send a transaction to the klaytn blockchain platform (Klaytn)
            Bytes32 result = caver.rpc.klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
            if(result.hasError()) {
                throw new RuntimeException(result.getError().getMessage());
            }

            //Check transaction receipt.
            TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(result.getResult());

            return transactionReceipt;
        }

        @BeforeClass
        public static void init() throws Exception {
            if(kip17 == null) {
                deployContract();
            }
            sampleReceiptData = sendKlay();
        }

        @Test
        public void getRewards() throws IOException {
            KlayRewards responseWithNumber = klay.getRewards(BigInteger.valueOf(5)).send();
            assertFalse(responseWithNumber.hasError());
            assertNotNull(responseWithNumber.getResult().getBurntFee());
            assertNotNull(responseWithNumber.getResult().getKff());
            assertNotNull(responseWithNumber.getResult().getKcf());
            assertNotNull(responseWithNumber.getResult().getMinted());
            assertNotNull(responseWithNumber.getResult().getProposer());
            assertNotNull(responseWithNumber.getResult().getRewards());
            assertNotNull(responseWithNumber.getResult().getStakers());
            assertNotNull(responseWithNumber.getResult().getTotalFee());

            KlayRewards responseWithTag = klay.getRewards(DefaultBlockParameterName.LATEST).send();
            assertFalse(responseWithTag.hasError());
            assertNotNull(responseWithTag.getResult().getBurntFee());
            assertNotNull(responseWithTag.getResult().getKff());
            assertNotNull(responseWithTag.getResult().getKcf());
            assertNotNull(responseWithTag.getResult().getMinted());
            assertNotNull(responseWithTag.getResult().getProposer());
            assertNotNull(responseWithTag.getResult().getRewards());
            assertNotNull(responseWithTag.getResult().getStakers());
            assertNotNull(responseWithTag.getResult().getTotalFee());

            KlayRewards response = klay.getRewards().send();
            assertFalse(response.hasError());
            assertNotNull(response.getResult().getBurntFee());
            assertNotNull(response.getResult().getKff());
            assertNotNull(response.getResult().getKcf());
            assertNotNull(response.getResult().getMinted());
            assertNotNull(response.getResult().getProposer());
            assertNotNull(response.getResult().getRewards());
            assertNotNull(response.getResult().getStakers());
            assertNotNull(response.getResult().getTotalFee());
        }

        @Test
        public void testIsAccountCreated() throws Exception {
            Boolean response = caver.rpc.klay.accountCreated(
                    LUMAN.getAddress(),
                    DefaultBlockParameterName.LATEST
            ).send();
            assertTrue(response.getResult());
        }

        @Test
        public void getAccountTest() {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            try {
                Account EOA_account = caver.rpc.klay.getAccount(LUMAN.getAddress()).send();
                assertEquals(IAccountType.AccType.EOA.getAccType(), EOA_account.getResult().getAccType());

                Account SCA_account = caver.rpc.klay.getAccount(kip17.getContractAddress()).send();
                assertEquals(IAccountType.AccType.SCA.getAccType(), SCA_account.getResult().getAccType());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getAccountKeyTest() {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            try {
                AccountKey accountKey = caver.rpc.klay.getAccountKey(LUMAN.getAddress()).send();
                assertNotNull(accountKey);
            } catch (IOException e){
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBalanceTest() {
            try {
                Quantity quantity = caver.rpc.klay.getBalance(LUMAN.getAddress()).send();
                assertNotNull(quantity);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getCodeTest() {
            try {
                String code = caver.rpc.klay.getCode(kip17.getContractAddress()).send().getResult();
                assertNotNull(code);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getTransactionCountTest() throws IOException {
            Quantity response = caver.rpc.klay.getTransactionCount(
                    LUMAN.getAddress(),
                    DefaultBlockParameterName.LATEST).send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        @Test
        public void isContractAccountTest() {
            try {
                Boolean result = caver.rpc.klay.isContractAccount(kip17.getContractAddress()).send();
                assertTrue(result.getResult());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBlockNumberTest() {
            try {
                Quantity response = caver.rpc.klay.getBlockNumber().send();
                BigInteger result = response.getValue();
                assertNotNull(result);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getHeaderTest() {
            try {
                BlockHeader response = caver.rpc.klay.getHeader(DefaultBlockParameterName.LATEST).send();
                BlockHeader.BlockHeaderData blockHeader = response.getResult();
                String hash = blockHeader.getHash();
                assertNotNull(hash);

                response = caver.rpc.klay.getHeader(BigInteger.valueOf(0)).send();
                blockHeader = response.getResult();
                assertNotNull(blockHeader.getHash());

                response = caver.rpc.klay.getHeader(hash).send();
                blockHeader = response.getResult();
                assertNotNull(blockHeader.getHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getHeaderByNumberTest() {
            try {
                BlockHeader response = caver.rpc.klay.getHeaderByNumber(DefaultBlockParameterName.LATEST).send();
                BlockHeader.BlockHeaderData blockHeader = response.getResult();
                assertNotNull(blockHeader.getHash());

                response = caver.rpc.klay.getHeaderByNumber(new DefaultBlockParameterNumber(0)).send();
                blockHeader = response.getResult();
                assertNotNull(blockHeader.getHash());

                response = caver.rpc.klay.getHeaderByNumber(BigInteger.valueOf(0)).send();
                blockHeader = response.getResult();
                assertNotNull(blockHeader.getHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getHeaderByHashTest() {
            try {
                BlockHeader response = caver.rpc.klay.getHeaderByNumber(DefaultBlockParameterName.LATEST).send();
                BlockHeader.BlockHeaderData blockHeader = response.getResult();

                BlockHeader responseByHash = caver.rpc.klay.getHeaderByHash(blockHeader.getHash()).send();

                assertEquals(blockHeader.getHash(), responseByHash.getResult().getHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBlockByNumberTest() {
            try {
                Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
                Block.BlockData<Transaction.TransactionData> block = response.getResult();
                assertNotNull(block.getHash());

                response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
                block = response.getResult();
                assertNotNull(block.getHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBlockByHashTest() {
            try {
                Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
                Block.BlockData<Transaction.TransactionData> block = response.getResult();

                Block responseByHash = caver.rpc.klay.getBlockByHash(block.getHash(), true).send();

                assertEquals(block.getHash(), responseByHash.getResult().getHash());

                response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
                block = response.getResult();

                responseByHash = caver.rpc.klay.getBlockByHash(block.getHash(), false).send();

                assertEquals(block.getHash(), responseByHash.getResult().getHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBlockReceiptsTest() {
            try {
                BlockTransactionReceipts blockReceipts = caver.rpc.klay.getBlockReceipts(sampleReceiptData.getBlockHash()).send();
                assertEquals(sampleReceiptData.getBlockHash(), blockReceipts.getResult().get(0).getBlockHash());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getTransactionCountByNumberTest() {
            try {
                Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
                Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

                Quantity responseByNumber = caver.rpc.klay.getTransactionCountByNumber(
                        new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
                BigInteger result = responseByNumber.getValue();
                assertEquals(testBlock.getTransactions().size(), result.intValue());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getTransactionCountByHash() {
            try {
                Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
                Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

                Quantity responseByHash = caver.rpc.klay.getTransactionCountByHash(testBlock.getHash()).send();
                BigInteger result = responseByHash.getValue();
                assertEquals(testBlock.getTransactions().size(), result.intValue());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getBlockWithConsensusInfoByHashTest() throws Exception {
            Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            BlockWithConsensusInfo responseInfo = caver.rpc.klay.getBlockWithConsensusInfoByHash(testBlock.getHash()).send();
            BlockWithConsensusInfo.Block result = responseInfo.getResult();
            assertEquals(testBlock.getHash(), result.getHash());
        }

        @Test
        public void getBlockWithConsensusInfoByNumberTest() throws Exception {
            Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            BlockWithConsensusInfo responseInfo = caver.rpc.klay.getBlockWithConsensusInfoByNumber(
                    new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber()))).send();
            BlockWithConsensusInfo.Block result = responseInfo.getResult();
            assertEquals(testBlock.getNumber(), result.getNumber());
        }

        @Test
        public void getCommitteeTest() throws IOException {
            Addresses response = caver.rpc.klay.getCommittee(DefaultBlockParameterName.LATEST).send();
            assertNull(response.getError());
        }

        @Test
        public void getCommitteeSizeTest() throws IOException {
            Quantity response = caver.rpc.klay.getCommitteeSize(DefaultBlockParameterName.LATEST).send();
            assertNull(response.getError());
        }

        @Test
        public void getCouncilTest() throws IOException {
            Addresses response = caver.rpc.klay.getCouncil(DefaultBlockParameterName.LATEST).send();
            assertNull(response.getError());
        }

        @Test
        public void getCouncilSizeTest() throws IOException {
            Quantity response = caver.rpc.klay.getCouncilSize(DefaultBlockParameterName.LATEST).send();
            assertNull(response.getError());
        }

        @Test
        public void getStorageAtTest() throws Exception {
            Response<String> response = caver.rpc.klay.getStorageAt(
                    LUMAN.getAddress(),
                    new DefaultBlockParameterNumber(0),
                    DefaultBlockParameterName.LATEST).send();
            String result = response.getResult();
            assertNotNull(result);
        }

        @Test
        public void isSyncingTest() throws Exception {
            KlaySyncing response = caver.rpc.klay.isSyncing().send();
            KlaySyncing.Result result = response.getResult();
            assertFalse(result.isSyncing());
        }

        @Test
        public void estimateGasTest() throws Exception {
            String encoded = kip17.getMethod("setApprovalForAll").encodeABI(Arrays.asList(BRANDON.getAddress(), true));

            CallObject callObject = CallObject.createCallObject(
                    LUMAN.getAddress(),
                    kip17.getContractAddress(),
                    new BigInteger("100000", 16),
                    new BigInteger("5d21dba00", 16),
                    new BigInteger("0", 16),
                    encoded
            );
            Quantity response = caver.rpc.klay.estimateGas(callObject).send();
            String result = response.getResult();
            assertEquals("0xcce5", result);
        }

        @Test
        public void estimateComputationCostTest() throws Exception {
            String encoded = kip17.getMethod("setApprovalForAll").encodeABI(Arrays.asList(BRANDON.getAddress(), true));

            CallObject callObject = CallObject.createCallObject(
                    LUMAN.getAddress(),
                    kip17.getContractAddress(),
                    new BigInteger("100000", 16),
                    new BigInteger("5d21dba00", 16),
                    new BigInteger("0", 16),
                    encoded
            );
            Quantity response = caver.rpc.klay.estimateComputationCost(callObject, DefaultBlockParameterName.LATEST).send();
            String result = response.getResult();
            assertEquals("0xae53", result);
        }

        @Test
        public void getTransactionByBlockHashAndIndexTest() throws Exception {
            TransactionReceipt.TransactionReceiptData receiptData = sendKlay();

            Transaction res = caver.rpc.klay.getTransactionByBlockHashAndIndex(receiptData.getBlockHash(), 0).send();
            assertEquals(receiptData.getBlockHash(), res.getResult().getBlockHash());
        }

        @Test
        public void getTransactionByBlockNumberAndIndexTest() throws IOException, TransactionException {
            Block response = caver.rpc.klay.getBlockByHash(sampleReceiptData.getBlockHash()).send();
            Block.BlockData blockData = response.getResult();

            Transaction res = caver.rpc.klay.getTransactionByBlockNumberAndIndex(
                    new DefaultBlockParameterNumber(Numeric.toBigInt(blockData.getNumber())),
                    new DefaultBlockParameterNumber(0)).send();
            assertEquals(blockData.getHash(), res.getResult().getBlockHash());
        }

        @Test
        public void getTransactionByHashTest() throws Exception {
            Transaction response = caver.rpc.klay.getTransactionByHash(sampleReceiptData.getTransactionHash()).send();
            Transaction.TransactionData result = response.getResult();
            assertEquals(sampleReceiptData.getTransactionHash(), result.getHash());
        }

        @Test
        public void getTransactionBySenderTxHashTest() throws Exception {
            Transaction response = caver.rpc.klay.getTransactionBySenderTxHash(sampleReceiptData.getTransactionHash()).send();
            assertEquals(sampleReceiptData.getTransactionHash(), response.getResult().getHash());
        }

        @Test
        public void getTransactionReceiptTest() throws Exception {
            TransactionReceipt response = caver.rpc.klay.getTransactionReceipt(sampleReceiptData.getTransactionHash()).send();
            assertEquals(sampleReceiptData.getTransactionHash(), response.getResult().getTransactionHash());
        }

        @Test
        public void getTransactionReceiptBySenderTxHashTest() throws Exception {
            TransactionReceipt response = caver.rpc.klay.getTransactionReceiptBySenderTxHash(sampleReceiptData.getTransactionHash()).send();
            assertEquals(sampleReceiptData.getTransactionHash(), response.getResult().getTransactionHash());
        }

        @Test
        public void getChainIdTest() throws Exception {
            Quantity response = caver.rpc.klay.getChainID().send();
            BigInteger result = response.getValue();
            assertEquals(BigInteger.valueOf(LOCAL_CHAIN_ID), result);
        }

        @Test
        public void getClientVersionTest() throws IOException {
            Bytes response = caver.rpc.klay.getClientVersion().send();
            assertNull(response.getError());
        }

        @Test
        public void getGasPriceTest() throws Exception {
            Quantity response = caver.rpc.klay.getGasPrice().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        @Test
        public void getGasPriceAtTest() throws IOException {
            Quantity response = caver.rpc.klay.getGasPriceAt().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        @Test
        public void getMaxPriorityFeePerGasTest() throws Exception {
            Quantity response = caver.rpc.klay.getMaxPriorityFeePerGas().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        @Test
        public void getUpperBoundGasPriceTest() throws Exception {
            Quantity response = caver.rpc.klay.getUpperBoundGasPrice().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        @Test
        public void getLowerBoundGasPriceTest() throws Exception {
            Quantity response = caver.rpc.klay.getLowerBoundGasPrice().send();
            BigInteger result = response.getValue();
            assertNotNull(result);
        }

        // checkFeeHistoryResult checks response from getFeeHistory is ok or not.
        private void checkFeeHistoryResult(FeeHistoryResult feeHistoryResult, long blockCount, List<Float> rewardPercentiles) {
            FeeHistoryResult.FeeHistoryResultData feeHistoryResultData = feeHistoryResult.getResult();
            assertEquals(blockCount + 1, feeHistoryResultData.getBaseFeePerGas().size());

            List<List<String>> reward = feeHistoryResultData.getReward();
            if (reward != null) {
                assertEquals(blockCount, reward.size());
                Consumer<List<String>> consumer = rewardElement -> {
                    int expectedSize = 0 ;
                    if (rewardPercentiles != null) {
                        expectedSize = rewardPercentiles.size();
                    }
                    assertEquals(expectedSize, rewardElement.size());
                };
                reward.forEach(consumer);
            }

            assertEquals(blockCount, feeHistoryResultData.getGasUsedRatio().size());
        }

        @Test
        public void getFeeHistoryTest() throws IOException, InterruptedException {
            SingleKeyring sender = caver.wallet.keyring.createFromKlaytnWalletKey(LUMAN.getKlaytnWalletKey());
            caver.wallet.add(sender);
            Quantity transactionCount = caver.rpc.klay.getTransactionCount(sender.getAddress()).send();
            BigInteger nonce = transactionCount.getValue();
            String gasPrice = klay.getGasPrice().send().getResult();

            String chainId = klay.getChainID().send().getResult();
            final int txsCount = 30;
            TransactionReceipt.TransactionReceiptData receiptData = null;
            for (int i = 0; i < txsCount; i++) {
                ValueTransfer tx = caver.transaction.valueTransfer.create(
                        TxPropertyBuilder.valueTransfer()
                                .setFrom(sender.getAddress())
                                .setTo(caver.wallet.keyring.generate().getAddress())
                                .setGas("0x99999")
                                .setGasPrice(gasPrice)
                                .setValue("0x1")
                                .setNonce(nonce)
                                .setChainId(chainId)
                );
                caver.wallet.sign(sender.getAddress(), tx);
                nonce = nonce.add(BigInteger.valueOf(1));

                if (i != txsCount - 1) {
                    caver.rpc.klay.sendRawTransaction(tx).send();
                    continue;
                }
                Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(tx).send();
                Thread.sleep(5000);
                String txHash = sendResult.getResult();
                TransactionReceipt receipt = caver.rpc.klay.getTransactionReceipt(txHash).send();
                receiptData = receipt.getResult();
            }
            if (receiptData == null) {
                fail();
            }

            long blockCount = 5;
            long blockNumber = new BigInteger(caver.utils.stripHexPrefix(receiptData.getBlockNumber()), 16).longValue();
            List<Float> rewardPercentiles = new ArrayList<Float>(Arrays.asList(0.3f, 0.5f, 0.8f));
            FeeHistoryResult feeHistoryResult = caver.rpc.klay.getFeeHistory(blockCount, blockNumber, rewardPercentiles).send();
            checkFeeHistoryResult(feeHistoryResult, blockCount, rewardPercentiles);

            blockCount = 5;
            rewardPercentiles = null;
            feeHistoryResult = caver.rpc.klay.getFeeHistory(blockCount, DefaultBlockParameterName.LATEST, rewardPercentiles).send();
            checkFeeHistoryResult(feeHistoryResult, blockCount, rewardPercentiles);
        }

        @Test
        public void createAccessListTest() throws IOException {
            Block block = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST).send();
            Quantity gasPrice = caver.rpc.klay.getGasPrice().send();
            BigInteger blockNumber = new BigInteger(caver.utils.stripHexPrefix(block.getResult().getNumber()), 16);
            String blockHash = block.getResult().getHash();
            CallObject callObject = CallObject.createCallObject(
                    LUMAN.getAddress(),
                    WAYNE.getAddress(),
                    BigInteger.valueOf(100000),
                    gasPrice.getValue(),
                    BigInteger.valueOf(1)
            );
            AccessListResult accessListResult = caver.rpc.klay.createAccessList(callObject, DefaultBlockParameterName.LATEST).send();
            checkAccessListResult(accessListResult.getResult());

            accessListResult = caver.rpc.klay.createAccessList(callObject, new DefaultBlockParameterNumber(blockNumber)).send();
            checkAccessListResult(accessListResult.getResult());

            accessListResult = caver.rpc.klay.createAccessList(callObject, blockNumber).send();
            checkAccessListResult(accessListResult.getResult());

            accessListResult = caver.rpc.klay.createAccessList(callObject, blockHash).send();
            checkAccessListResult(accessListResult.getResult());
        }

        // checkAccessListResult checks whether given AcccessListResultData instance is right or not.
        private void checkAccessListResult(AccessListResult.AccessListResultData accessListResultData) {

            assertEquals(0, accessListResultData.getAccessList().size()); // For now Klaytn will return empty access list.
            assertEquals("0x0", accessListResultData.getGasUsed()); // For now Klaytn will return zero gasUsed.
            assertEquals(null, accessListResultData.getError());
        }

        @Test
        public void isParallelDbWriteTest() throws Exception {
            Boolean response = caver.rpc.klay.isParallelDBWrite().send();
            java.lang.Boolean result = response.getResult();
            assertTrue(result);  // It is enabled by default
        }

        @Test
        public void isSenderTxHashIndexingEnabledTest() throws Exception {
            Boolean response = caver.rpc.klay.isSenderTxHashIndexingEnabled().send();
            java.lang.Boolean result = response.getResult();
            assertFalse(result);  // It is disabled by default
        }

        @Test
        public void getProtocolVersionTest() throws Exception {
            String result = caver.rpc.klay.getProtocolVersion().send().getResult();
            assertNotNull(result);
        }

        @Ignore
        @Test
        public void getRewardbaseTest() throws Exception {
            Bytes20 response = caver.rpc.klay.getRewardbase().send();
            // Result - If requested from non-CN nodes
            assertEquals("rewardbase must be explicitly specified", response.getError().getMessage());
        }

        @Test
        @Ignore
        public void getFilterChangesTest() throws Exception {
            KlayLogs response = caver.rpc.klay.getFilterChanges(
                    "d5b93cf592b2050aee314767a02976c5").send();
            List<KlayLogs.LogResult> result = response.getResult();
            assertTrue("need test data", false);
        }

        @Test
        @Ignore
        public void getFilterLogsTest() throws Exception {
            KlayLogs response = caver.rpc.klay.getFilterLogs(
                    "d5b93cf592b2050aee314767a02976c5").send();
            List<KlayLogs.LogResult> result = response.getResult();
            assertTrue("need test data", false);
        }

        @Test
        @Ignore
        public void getLogsTest() throws Exception {
            KlayLogFilter filter = new KlayLogFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    LUMAN.getAddress(),
                    "0xe2649fe9fbaa75601408fc54200e3f9b2128e8fec7cea96c9a65b9caf905c9e3");
            KlayLogs response = caver.rpc.klay.getLogs(filter).send();
            List<KlayLogs.LogResult> result = response.getResult();
            assertTrue("need test data", false);
        }

        @Test
        public void newBlockFilterTest() throws Exception {
            Response<String> response = caver.rpc.klay.newBlockFilter().send();
            String result = response.getResult();
            assertNotNull(result);
        }

        @Test
        public void newFilterTest() throws Exception {
            KlayFilter filter = new KlayFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    LUMAN.getAddress());
            filter.addSingleTopic("0xd596fdad182d29130ce218f4c1590c4b5ede105bee36690727baa6592bd2bfc8");
            Quantity response = caver.rpc.klay.newFilter(filter).send();
            String result = response.getResult();
            assertNotNull(result);
        }

        @Test
        public void newPendingTransactionFilterTest() throws Exception {
            Response<String> response = caver.rpc.klay.newPendingTransactionFilter().send();
            String result = response.getResult();
            assertNotNull(result);
        }

        @Test
        @Ignore
        public void uninstallFilterTest() throws Exception {
            Boolean response = caver.rpc.klay.uninstallFilter("0x0").send();
            java.lang.Boolean result = response.getResult();
            assertTrue("need test data", false);
        }

        @Test
        public void getSha3Test() throws IOException {
            Bytes response = caver.rpc.klay.sha3("0x123f").send();
            String result = response.getResult();
            assertEquals("0x7fab6b214381d6479bf140c3c8967efb9babe535025500d5b1dc2d549984b90b", result);
        }

        @Test
        public void getIdTest() throws Exception {
            Bytes netVersion = caver.rpc.net.getNetworkID().send();
            assertEquals(netVersion.getResult(), String.valueOf(LOCAL_NETWORK_ID));
        }

        @Test
        public void isListeningTest() throws Exception {
            Boolean netListening = caver.rpc.net.isListening().send();
            assertTrue(netListening.getResult());
        }

        @Test
        public void getPeerCountTest() throws Exception {
            Quantity peerCount = caver.rpc.net.getPeerCount().send();
            assertTrue(peerCount.getValue().intValue() >= 0);
        }

        @Test
        public void getPeerCountByTypeTest() throws Exception {
            KlayPeerCount klayPeerCount = caver.rpc.net.getPeerCountByType().send();
            KlayPeerCount.PeerCount peerCount = klayPeerCount.getResult();
            assertTrue(peerCount.getTotal().intValue() >= 0);
        }

        @Test
        public void getVersion() throws Exception {
            Quantity version = caver.rpc.net.getVersion().send();
            assertTrue(version.getValue().intValue() >= 0);
        }
    }

    public static class GovernanceAPITest {
        static Caver caver;


        @BeforeClass
        public static void init() throws InterruptedException, IOException {
            caver = new Caver(Caver.DEFAULT_URL);

            String modeKey = "governance.governancemode";
            String modeValue = "single";

            Bytes response = caver.rpc.governance.vote(modeKey, modeValue).send();

            String epochKey = "istanbul.epoch";
            BigInteger epochValue = BigInteger.valueOf(86400);

            response = caver.rpc.governance.vote(epochKey, epochValue).send();

            Thread.sleep(5000);
        }

        @Test
        public void vote() throws IOException {
            String modeKey = "governance.governancemode";
            String modeValue = "single";

            Bytes response = caver.rpc.governance.vote(modeKey, modeValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String unitPriceKey = "governance.unitprice";
            BigInteger unitPriceValue = new BigInteger("25000000000");

            response = caver.rpc.governance.vote(unitPriceKey, unitPriceValue).send();
            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String epochKey = "istanbul.epoch";
            BigInteger epochValue = BigInteger.valueOf(86400);

            response = caver.rpc.governance.vote(epochKey, epochValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String sizeKey = "istanbul.committeesize";
            BigInteger sizeValue = BigInteger.valueOf(7);

            response = caver.rpc.governance.vote(epochKey, epochValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String mintKey = "reward.mintingamount";
            String mintValue = "9600000000000000000";

            response = caver.rpc.governance.vote(mintKey, mintValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String ratioKey = "reward.ratio";
            String ratioValue = "34/54/12";

            response = caver.rpc.governance.vote(ratioKey, ratioValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String coeffKey = "reward.useginicoeff";
            boolean coeffValue = true;

            response = caver.rpc.governance.vote(coeffKey, coeffValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String txFeeKey = "reward.deferredtxfee";
            boolean txFeeValue = true;

            response = caver.rpc.governance.vote(txFeeKey, txFeeValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String stakingKey = "reward.minimumstake";
            String stakingValue = "5000000";

            response = caver.rpc.governance.vote(stakingKey, stakingValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String lowerBoundKey = "kip71.lowerboundbasefee";
            BigInteger lowerBoundValue = new BigInteger("25000000000");

            response = caver.rpc.governance.vote(lowerBoundKey, lowerBoundValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String upperBoundKey = "kip71.upperboundbasefee";
            BigInteger upperBoundValue = new BigInteger("750000000000");

            response = caver.rpc.governance.vote(upperBoundKey, upperBoundValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String gasTargetKey = "kip71.gastarget";
            BigInteger gasTargetValue = new BigInteger("30000000");

            response = caver.rpc.governance.vote(gasTargetKey, gasTargetValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String maxBlockGasUsedForBaseFeeKey = "kip71.maxblockgasusedforbasefee";
            BigInteger maxBlockGasUsedForBaseFeeValue = new BigInteger("60000000");

            response = caver.rpc.governance.vote(maxBlockGasUsedForBaseFeeKey, maxBlockGasUsedForBaseFeeValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));

            String baseFeeDenominatorKey = "kip71.basefeedenominator";
            BigInteger baseFeeDenominatorValue = new BigInteger("20");

            response = caver.rpc.governance.vote(baseFeeDenominatorKey, baseFeeDenominatorValue).send();

            assertFalse(response.hasError());
            assertTrue(isSuccessfulVoteResponse(response.getResult()));
        }

        @Test
        public void showTally() throws IOException {
            GovernanceTally response = caver.rpc.governance.showTally().send();
            assertFalse(response.hasError());
            assertNotNull(response.getResult());
        }

        @Test
        public void getTotalVotingPower() throws IOException {
            GovernanceVotingPower response = caver.rpc.governance.getTotalVotingPower().send();
            assertNotNull(response);
        }

        @Test
        public void getMyVotingPower() throws IOException {
            GovernanceVotingPower response = caver.rpc.governance.getMyVotingPower().send();
            assertNotNull(response);
        }

        @Test
        public void getMyVotes() throws IOException {
            GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test public void getKlayChainConfig() throws IOException {
            GovernanceChainConfig response = caver.rpc.klay.getChainConfig().send();
            assertNotNull(response.getResult());
            assertFalse(response.hasError());
        }

        @Test
        public void getKlayChainConfigAtWithBlockNumber() throws IOException {
            GovernanceChainConfig response = caver.rpc.klay.getChainConfigAt(BigInteger.valueOf(0)).send();
            assertNotNull(response.getResult());
            assertFalse(response.hasError());
        }

        @Test
        public void getKlayChainConfigAtBlockTag() throws IOException {
            GovernanceChainConfig response = caver.rpc.klay.getChainConfigAt("latest").send();
            assertNotNull(response.getResult());
            assertFalse(response.hasError());
        }

        @Test public void getGovernanceChainConfig() throws IOException {
            GovernanceChainConfig response = caver.rpc.governance.getChainConfig().send();
            assertNotNull(response.getResult());
            assertFalse(response.hasError());
        }

        // @Test
        // public void getGovernanceChainConfigAtWithBlockNumber() throws IOException {
        //     GovernanceChainConfig response = caver.rpc.governance.getChainConfigAt(BigInteger.valueOf(0)).send();
        //     assertNotNull(response.getResult());
        //     assertFalse(response.hasError());
        // }

        // @Test
        // public void getGovernanceChainConfigAtBlockTag() throws IOException {
        //     GovernanceChainConfig response = caver.rpc.governance.getChainConfigAt("latest").send();
        //     assertNotNull(response.getResult());
        //     assertFalse(response.hasError());
        // }

        @Test
        public void getKlayNodeAddress() throws IOException {
            Bytes20 response = caver.rpc.klay.getNodeAddress().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getGovernanceNodeAddress() throws IOException {
            Bytes20 response = caver.rpc.governance.getNodeAddress().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getParams() throws IOException {
            GovernanceItems response = caver.rpc.klay.getParams().send();
            assertNotNull(response);
            assertFalse(response.hasError());

            Map<String, Object> gov_item = response.getResult();

            response = caver.rpc.klay.getParams(DefaultBlockParameterName.LATEST).send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.klay.getParams(BigInteger.ZERO).send();
            assertNotNull(response);
            assertFalse(response.hasError());

            String mode = IVote.VoteItem.getGovernanceMode(response.getResult());
            System.out.println(mode);

        }

        // @Test
        // public void getItemsAt() throws IOException {
        //     GovernanceItems response = caver.rpc.governance.getItemsAt().send();
        //     assertNotNull(response);
        //     assertFalse(response.hasError());

        //     Map<String, Object> gov_item = response.getResult();

        //     response = caver.rpc.governance.getItemsAt(DefaultBlockParameterName.LATEST).send();
        //     assertNotNull(response);
        //     assertFalse(response.hasError());

        //     response = caver.rpc.governance.getItemsAt(BigInteger.ZERO).send();
        //     assertNotNull(response);
        //     assertFalse(response.hasError());

        //     String mode = IVote.VoteItem.getGovernanceMode(response.getResult());
        //     System.out.println(mode);

        // }

        @Test
        public void getPendingChanges() throws IOException {
            GovernanceItems response = caver.rpc.governance.getPendingChanges().send();
            assertNotNull(response);
            assertFalse(response.hasError());

            String mode = (String)response.getResult().get("governance.governancemode");
            assertEquals("single", mode);
        }

        @Test
        public void getVotes() throws IOException {
            GovernanceNodeVotes response = caver.rpc.governance.getVotes().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getIdxCache() throws IOException {
            GovernanceIdxCache response = caver.rpc.governance.getIdxCache().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getIdxCacheFromDb() throws IOException {
            GovernanceIdxCache response = caver.rpc.governance.getIdxCacheFromDb().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getItemCacheFromDb() throws IOException {
            GovernanceItems response = caver.rpc.governance.getItemCacheFromDb(BigInteger.ZERO).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getKlayStakingInfo() throws IOException {
            GovernanceStakingInfo response = caver.rpc.klay.getStakingInfo().send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.klay.getStakingInfo("latest").send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.klay.getStakingInfo(DefaultBlockParameterName.LATEST).send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.klay.getStakingInfo(BigInteger.ZERO).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }
        @Test
        public void getGovernanceStakingInfo() throws IOException {
            GovernanceStakingInfo response = caver.rpc.governance.getStakingInfo().send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.governance.getStakingInfo("latest").send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.governance.getStakingInfo(DefaultBlockParameterName.LATEST).send();
            assertNotNull(response);
            assertFalse(response.hasError());

            response = caver.rpc.governance.getStakingInfo(BigInteger.ZERO).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getRewardsAccumulated () throws IOException {
            RewardsAccumulated response = caver.rpc.governance.getRewardsAccumulated("123400489", "123416489").send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void parseGovernanceItem() throws IOException {
            String json = "{\n" +
                    "  \"governance.governancemode\": \"single\",\n" +
                    "  \"governance.governingnode\": \"0x7bf29f69b3a120dae17bca6cf344cf23f2daf208\",\n" +
                    "  \"governance.unitprice\": 25000000000,\n" +
                    "  \"istanbul.committeesize\": 13,\n" +
                    "  \"istanbul.epoch\": 30,\n" +
                    "  \"istanbul.policy\": 2,\n" +
                    "  \"reward.deferredtxfee\": true,\n" +
                    "  \"reward.minimumstake\": \"5000000\",\n" +
                    "  \"reward.mintingamount\": \"9600000000000000000\",\n" +
                    "  \"reward.proposerupdateinterval\": 30,\n" +
                    "  \"reward.ratio\": \"34/54/12\",\n" +
                    "  \"reward.stakingupdateinterval\": 60,\n" +
                    "  \"reward.useginicoeff\": true\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            Map<String, Object> item = mapper.readValue(json, Map.class);

            assertEquals("single", IVote.VoteItem.getGovernanceMode(item));
            assertEquals("0x7bf29f69b3a120dae17bca6cf344cf23f2daf208", IVote.VoteItem.getGoverningNode(item));
            assertEquals(new BigInteger("25000000000"), IVote.VoteItem.getUnitPrice(item));
            assertEquals(BigInteger.valueOf(13), IVote.VoteItem.getCommitteeSize(item));
            assertEquals(BigInteger.valueOf(30), IVote.VoteItem.getEpoch(item));
            assertEquals(BigInteger.valueOf(2), IVote.VoteItem.getPolicy(item));
            assertTrue(IVote.VoteItem.getDeferredTxFee(item));
            assertEquals("5000000", IVote.VoteItem.getMinimumStake(item));
            assertEquals("9600000000000000000", IVote.VoteItem.getMintingAmount(item));
            assertEquals("34/54/12", IVote.VoteItem.getRatio(item));
            assertTrue(IVote.VoteItem.getUseGinicoeff(item));
            assertEquals(BigInteger.valueOf(30), IVote.VoteItem.getProposerUpdateInterval(item));
            assertEquals(BigInteger.valueOf(60), IVote.VoteItem.getStakingUpdateInterval(item));
        }

        @Test
        public void parseChainConfig() throws IOException {
            String json = "{\n" +
                    "  \"chainId\": 1001,\n" +
                    "  \"deriveShaImpl\": 2,\n" +
                    "  \"governance\": {\n" +
                    "    \"governanceMode\": \"ballot\",\n" +
                    "    \"governingNode\": \"0xe733cb4d279da696f30d470f8c04decb54fcb0d2\",\n" +
                    "    \"reward\": {\n" +
                    "      \"deferredTxFee\": true,\n" +
                    "      \"minimumStake\": 5000000,\n" +
                    "      \"mintingAmount\": 9600000000000000000,\n" +
                    "      \"proposerUpdateInterval\": 3600,\n" +
                    "      \"ratio\": \"34/54/12\",\n" +
                    "      \"stakingUpdateInterval\": 20,\n" +
                    "      \"useGiniCoeff\": false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"istanbul\": {\n" +
                    "    \"epoch\": 20,\n" +
                    "    \"policy\": 2,\n" +
                    "    \"sub\": 1\n" +
                    "  },\n" +
                    "  \"unitPrice\": 25000000000\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            GovernanceChainConfig.ChainConfigData item = mapper.readValue(json, GovernanceChainConfig.ChainConfigData.class);

            assertEquals(1001, item.getChainid());
            assertEquals(2, item.getDeriveshaimpl());
            assertEquals("ballot", item.getGovernance().getGovernanceMode());
            assertEquals("0xe733cb4d279da696f30d470f8c04decb54fcb0d2", item.getGovernance().getGoverningNode());
            assertEquals(true, item.getGovernance().getReward().getDeferredTxFee());
            assertEquals(new BigInteger("5000000"), item.getGovernance().getReward().getMinimumStake());
            assertEquals(new BigInteger("9600000000000000000"), item.getGovernance().getReward().getMintingAmount());
            assertEquals(new BigInteger("3600"), item.getGovernance().getReward().getProposerUpdateInterval());
            assertEquals("34/54/12", item.getGovernance().getReward().getRatio());
            assertEquals(new BigInteger("20"), item.getGovernance().getReward().getStakingUpdateInterval());
            assertEquals(false, item.getGovernance().getReward().getUseGiniCoeff());
            assertEquals(BigInteger.valueOf(20), item.getIstanbul().getEpoch());
            assertEquals(BigInteger.valueOf(2), item.getIstanbul().getPolicy());
            assertEquals(BigInteger.valueOf(1), item.getIstanbul().getSub());

            System.out.println(item);
        }

        @Test
        public void parseTally() throws IOException {
            String json = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"id\": 0,\n" +
                    "  \"result\": [\n" +
                    "    {\n" +
                    "      \"ApprovalPercentage\": 36.2,\n" +
                    "      \"Key\": \"governance.unitprice\",\n" +
                    "      \"Value\": 25000000000\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"ApprovalPercentage\": 72.5,\n" +
                    "      \"Key\": \"reward.mintingamount\",\n" +
                    "      \"Value\": \"9600000000000000000\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            GovernanceTally item = mapper.readValue(json, GovernanceTally.class);

            GovernanceTally.TallyData item1 = item.getResult().get(0);
            assertEquals(36.2f, item1.getApprovalPercentage());
            assertEquals(new BigInteger("25000000000"), IVote.VoteItem.getUnitPrice(item1));

            GovernanceTally.TallyData item2 = (GovernanceTally.TallyData)item.getResult().get(1);
            assertEquals(72.5f, item2.getApprovalPercentage());
            assertEquals("9600000000000000000", IVote.VoteItem.getMintingAmount(item2));
        }

        @Test
        public void paresNodeVotes() throws IOException {
            String json = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"id\": 0,\n" +
                    "  \"result\": [\n" +
                    "    {\n" +
                    "      \"key\": \"reward.minimumstake\",\n" +
                    "      \"validator\": \"0xe733cb4d279da696f30d470f8c04decb54fcb0d2\",\n" +
                    "      \"value\": \"5000000\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"key\": \"reward.useginicoeff\",\n" +
                    "      \"validator\": \"0xa5bccb4d279419abe2d470f8c04dec0789ac2d54\",\n" +
                    "      \"value\": false\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            GovernanceNodeVotes vote = mapper.readValue(json, GovernanceNodeVotes.class);

            List voteList = vote.getResult();
            GovernanceNodeVotes.NodeVote nodeVote = (GovernanceNodeVotes.NodeVote)voteList.get(0);
            assertEquals("5000000", IVote.VoteItem.getMinimumStake(voteList));
            assertEquals("5000000", IVote.VoteItem.getMinimumStake(nodeVote));

            GovernanceNodeVotes.NodeVote nodeVote1 = (GovernanceNodeVotes.NodeVote)voteList.get(1);
            assertFalse(IVote.VoteItem.getUseGinicoeff(voteList));
            assertFalse(IVote.VoteItem.getUseGinicoeff(nodeVote1));

            assertEquals("0xe733cb4d279da696f30d470f8c04decb54fcb0d2", nodeVote.getValidator());
            assertEquals("0xa5bccb4d279419abe2d470f8c04dec0789ac2d54", nodeVote1.getValidator());
        }

        @Test
        public void paresMyVotes() throws IOException {
            String json = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"id\": 0,\n" +
                    "  \"result\": [\n" +
                    "    {\n" +
                    "      \"Key\": \"reward.useginicoeff\",\n" +
                    "      \"Value\": true,\n" +
                    "      \"Casted\": true,\n" +
                    "      \"BlockNum\": 2014\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"Key\": \"governance.governancemode\",\n" +
                    "      \"Value\": \"single\",\n" +
                    "      \"Casted\": true,\n" +
                    "      \"BlockNum\": 2610\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            GovernanceMyVotes vote = mapper.readValue(json, GovernanceMyVotes.class);

            List voteList = vote.getResult();
            GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
            assertTrue(IVote.VoteItem.getUseGinicoeff(voteList));
            assertTrue(IVote.VoteItem.getUseGinicoeff(myVote));
            GovernanceMyVotes.MyVote myVote1 = (GovernanceMyVotes.MyVote)voteList.get(1);
            assertEquals("single", IVote.VoteItem.getGovernanceMode(voteList));
            assertEquals("single", IVote.VoteItem.getGovernanceMode(myVote1));

            System.out.println(vote);
            System.out.println(myVote1);
        }

        private boolean isSuccessfulVoteResponse(String message) {
            return message != null && !message.contains("don't hvae the right") && !message.contains("couldn't be placed");
        }
    }

    public static class AdminAPITest {
        static Caver caver;

        @BeforeClass
        public static void init() throws InterruptedException, IOException {
            caver = new Caver(Caver.DEFAULT_URL);
        }

        @Test
        public void getNodeInfo() throws IOException {
            NodeInfo response = caver.rpc.admin.getNodeInfo().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void getDataDir() throws IOException {
            Bytes response = caver.rpc.admin.getDataDir().send();
            assertNotNull(response);
            assertFalse(response.hasError());
            assertNotNull(response.getResult());
        }

        @Test
        public void getPeers() throws IOException {
            Peers response = caver.rpc.admin.getPeers().send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void addPeer() throws IOException {
            String kni = "kni://a979fb575495b8d6db44f750317d0f4622bf4c2aa3365d6af7c284339968eef29b69ad0dce72a4d8db5ebb4968de0e3bec910127f134779fbcb0cb6d3331163c@10.0.0.1:32323";
            Boolean response = caver.rpc.admin.addPeer(kni).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void removePeer() throws IOException {
            String kni = "kni://a979fb575495b8d6db44f750317d0f4622bf4c2aa3365d6af7c284339968eef29b69ad0dce72a4d8db5ebb4968de0e3bec910127f134779fbcb0cb6d3331163c@10.0.0.1:32323";
            Boolean response = caver.rpc.admin.removePeer(kni).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void forkStatus() throws IOException {
            ForkStatusResult response = caver.rpc.klay.forkStatus(20).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void recoverFromMessage() throws IOException {
            Bytes response = caver.rpc.klay.recoverFromMessage(
                "0xA2a8854b1802D8Cd5De631E690817c253d6a9153",
                "0xdeadbeef", 
                "0x1e6338d6e4a8d688a25de78cf2a92efec9a92e52eb8425acaaee8c3957e68cdb3f91bdc483f0ed05a0da26eca3be4c566d087d90dc2ca293be23b2a9de0bcafc1c", 
                "latest"
            ).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }

        @Test
        public void recoverFromTransaction() throws IOException {
            Bytes response = caver.rpc.klay.recoverFromTransaction(
                "0x08f88608850ba43b7400827b0c94c40b6909eb7085590e1c26cb3becc25368e249e9880de0b6b3a764000094e15cd70a41dfb05e7214004d7d054801b2a2f06bf847f845820fe9a090421871e8fd77e08b6a72760006a15184a96cfc39c7486ea948d11fd830ae8aa05876248aa8dc0783d782e584e6f8d9bf977c698210a0eab3e754192d0954de65",
                "latest"
            ).send();
            assertNotNull(response);
            assertFalse(response.hasError());
        }
    }
}
