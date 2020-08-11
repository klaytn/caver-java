package com.klaytn.caver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.account.*;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Account;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferWithRatio;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static com.klaytn.caver.base.LocalValues.LOCAL_NETWORK_ID;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                RpcTest.encodeAccountKeyTest.class,
                RpcTest.decodeAccountKeyTest.class,
                RpcTest.sendTransactionAsFeePayerTest.class,
                RpcTest.signTransactionTest.class,
                RpcTest.signTransactionAsFeePayerTest.class,
                RpcTest.otherRPCTest.class
        }
)
public class RpcTest extends Accounts {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    static Klay klay = caver.rpc.klay;
    static Web3jService web3jService = caver.getRpc().getWeb3jService();

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
        public static KIP17 deployContract() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));

            KIP17DeployParams kip7DeployParam = new KIP17DeployParams("CONTRACT_NAME", "CONTRACT_SYMBOL");
            KIP17 kip17 = KIP17.deploy(caver, kip7DeployParam, LUMAN.getAddress());

            return kip17;
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
            Caver caver = new Caver(Caver.BAOBAB_URL);
            try {
                Account EOA_account = caver.rpc.klay.getAccount("0x3e3733b256c93f9d759e33c9939258068bd5957d").send();
                assertEquals(IAccountType.AccType.EOA.getAccType(), EOA_account.getResult().getAccType());

                Account SCA_account = caver.rpc.klay.getAccount("0x5d3fc50fb0bfe6ab1644a893034d3a246cef1b4a").send();
                assertEquals(IAccountType.AccType.SCA.getAccType(), SCA_account.getResult().getAccType());
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getAccountKeyTest() {
            Caver caver = new Caver(Caver.BAOBAB_URL);
            try {
                AccountKey accountKey = caver.rpc.klay.getAccountKey("0x3e3733b256c93f9d759e33c9939258068bd5957d").send();
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
                KIP17 kip17 = deployContract();

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
                KIP17 kip17 = deployContract();

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
                Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
                Block.BlockData<Transaction.TransactionData> block = response.getResult();

                BlockTransactionReceipts blockReceipts = caver.rpc.klay.getBlockReceipts(block.getHash()).send();
                assertEquals(block.getHash(), blockReceipts.getResult().get(0).getBlockHash());
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
            KIP17 kip17 = deployContract();

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
            assertEquals("0xb2d9", result);
        }

        @Test
        public void estimateComputationCostTest() throws Exception {
            KIP17 kip17 = deployContract();

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
            assertEquals("0xe036", result);
        }

        @Test
        public void getTransactionByBlockHashAndIndexTest() throws IOException {
            Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Transaction res = caver.rpc.klay.getTransactionByBlockHashAndIndex(testBlock.getHash(), 0).send();
            assertEquals(testBlock.getHash(), res.getResult().getBlockHash());
        }

        @Test
        public void getTransactionByBlockNumberAndIndexTest() throws IOException {
            Block response = caver.rpc.klay.getBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            Block.BlockData<Transaction.TransactionData> testBlock = response.getResult();

            Transaction res = caver.rpc.klay.getTransactionByBlockNumberAndIndex(
                    new DefaultBlockParameterNumber(Numeric.toBigInt(testBlock.getNumber())),
                    new DefaultBlockParameterNumber(0)).send();
            assertEquals(testBlock.getHash(), res.getResult().getBlockHash());
        }

        @Test
        public void getTransactionByHashTest() throws Exception {
            KIP17 kip17 = deployContract();
            TransactionReceipt.TransactionReceiptData receiptData = kip17.pause(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(30000)));

            Transaction response = caver.rpc.klay.getTransactionByHash(receiptData.getTransactionHash()).send();
            Transaction.TransactionData result = response.getResult();
            assertEquals(receiptData.getTransactionHash(), result.getHash());
        }

        @Test
        public void getTransactionBySenderTxHashTest() throws Exception {
            KIP17 kip17 = deployContract();
            TransactionReceipt.TransactionReceiptData receiptData = kip17.pause(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(30000)));

            Transaction response = caver.rpc.klay.getTransactionBySenderTxHash(receiptData.getTransactionHash()).send();
            assertEquals(receiptData.getTransactionHash(), response.getResult().getHash());
        }

        @Test
        public void getTransactionReceiptTest() throws Exception {
            KIP17 kip17 = deployContract();
            TransactionReceipt.TransactionReceiptData receiptData = kip17.pause(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(30000)));

            TransactionReceipt response = caver.rpc.klay.getTransactionReceipt(receiptData.getTransactionHash()).send();
            assertEquals(receiptData.getTransactionHash(), response.getResult().getTransactionHash());
        }

        @Test
        public void getTransactionReceiptBySenderTxHashTest() throws Exception {
            KIP17 kip17 = deployContract();
            TransactionReceipt.TransactionReceiptData receiptData = kip17.pause(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(30000)));

            TransactionReceipt response = caver.rpc.klay.getTransactionReceiptBySenderTxHash(receiptData.getTransactionHash()).send();
            assertEquals(receiptData.getTransactionHash(), response.getResult().getTransactionHash());
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
            assertEquals(new BigInteger("5d21dba00", 16), result); // 25,000,000,000 peb = 25 Gpeb
        }

        @Test
        public void getGasPriceAtTest() throws IOException {
            Quantity response = caver.rpc.klay.getGasPriceAt().send();
            BigInteger result = response.getValue();
            assertEquals(new BigInteger("5d21dba00", 16), result); // 25,000,000,000 peb = 25 Gpeb
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
            assertEquals("0x40", result);
        }

        @Ignore
        @Test
        public void getRewardbaseTest() throws Exception {
            Bytes20 response = caver.rpc.klay.getRewardbase().send();
            // Result - If requested from non-CN nodes
            assertEquals("rewardbase must be explicitly specified", response.getError().getMessage());
        }

        @Test
        public void isWriteThroughCachingTest() throws Exception {
            Boolean response = caver.rpc.klay.writeThroughCaching().send();
            java.lang.Boolean result = response.getResult();
            assertFalse(result);  // It is false by default.
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
    }
}
