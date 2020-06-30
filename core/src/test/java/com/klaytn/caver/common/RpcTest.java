package com.klaytn.caver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.account.*;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.kct.KIP7;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferWithRatio;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                RpcTest.encodeAccountKeyTest.class,
                RpcTest.decodeAccountKeyTest.class,
                RpcTest.sendTransactionAsFeePayerTest.class,
                RpcTest.signTransactionTest.class,
                RpcTest.signTransactionAsFeePayerTest.class
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

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyNil);
        }

        @Test
        public void withAccountKeyFail() throws IOException {
            String encodedStr = "0x03c0";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyFail);
        }

        @Test
        public void withAccountKeyLegacy() throws IOException {
            String encodedStr = "0x01c0";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyLegacy);
        }

        @Test
        public void withAccountKeyPublic() throws IOException {
            String encodedStr = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyPublic);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyWeightedMultiSig() throws IOException {
            String encodedStr = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyWeightedMultiSig);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyRoleBased() throws IOException {
            String encodedStr = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
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

            KlaySignTransaction signTransaction = klay.signTransaction(valueTransfer).send();
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

            KlaySignTransaction signTransaction = klay.signTransactionAsFeePayer(valueTransfer).send();

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
            KlaySignTransaction signTransaction = klay.signTransactionAsFeePayer(valueTransfer).send();
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
                KlayTransactionReceipt transactionReceipt = klay.getTransactionReceipt(txHash).send();
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
}
