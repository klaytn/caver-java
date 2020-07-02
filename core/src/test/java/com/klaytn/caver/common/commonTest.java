package com.klaytn.caver.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.account.*;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Account;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferWithRatio;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class commonTest {

    String abi = "[{\"constant\":false,\"inputs\":[{\"name\":\"recipient\",\"type\":\"address\"}],\"name\":\"transferPrimary\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payee\",\"type\":\"address\"},{\"name\":\"allowed\",\"type\":\"bool\"}],\"name\":\"setAllowed\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payee\",\"type\":\"address\"}],\"name\":\"withdraw\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"payee\",\"type\":\"address\"}],\"name\":\"withdrawalAllowed\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"primary\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"payee\",\"type\":\"address\"}],\"name\":\"depositsOf\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payee\",\"type\":\"address\"}],\"name\":\"deposit\",\"outputs\":[],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"payee\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"weiAmount\",\"type\":\"uint256\"}],\"name\":\"Deposited\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"payee\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"weiAmount\",\"type\":\"uint256\"}],\"name\":\"Withdrawn\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"recipient\",\"type\":\"address\"}],\"name\":\"PrimaryTransferred\",\"type\":\"event\"}]";

    @org.junit.Test
    public void abiParserTest() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(abi, AbiDefinition[].class);

        System.out.println(abi);
    }

    @org.junit.Test
    public void rlpTest() {
        String chainId = "0x7e2";
        BigInteger chain = Numeric.toBigInt(chainId);

        KlaySignatureData signatureData = KlaySignatureData.createKlaySignatureDataFromChainId(chain.intValue());
        System.out.println(signatureData.toRlpList().toString());

        RlpString rlpString = RlpString.create(Numeric.hexStringToByteArray(chainId));
        RlpString chainRlp = RlpString.create(chain);
        RlpString zeroRlp = RlpString.create(0);
        RlpString zeroRlp1 = RlpString.create(0);

        List<RlpType> list = signatureData.toRlpList().getValues();

        for(RlpType string : signatureData.toRlpList().getValues()) {
            System.out.println(((RlpString)string).asString());
        }


        System.out.println(chainRlp.asString());
        System.out.println(zeroRlp.asString());
    }

    @org.junit.Test
    public void serializeAccountKeyRoleBased() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        String serialized;

        AccountKeyRoleBased roleBased = AccountKeyRoleBased.decode("0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d");
        serialized = objectMapper.writeValueAsString(roleBased);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void serializeAccountKeyPublic() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(PrivateKey.generate().getPublicKey(false));

        String serialized = objectMapper.writeValueAsString(accountKeyPublic);
        System.out.println(serialized);

        AccountKeyFail accountKeyFail = new AccountKeyFail();
        serialized = objectMapper.writeValueAsString(accountKeyFail);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void serializeAccountKeyWeightedMultiSig() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String[] publicKey = new String[] {
                "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
        };

        BigInteger threshold = BigInteger.valueOf(2);

        BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
        List<BigInteger> weightList = Arrays.asList(weight);

        WeightedMultiSigOptions option = new WeightedMultiSigOptions(threshold, weightList);
        AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, option);

        String serialized = objectMapper.writeValueAsString(multiSig);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void deserializeAccountKeyPublic()throws IOException  {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(PrivateKey.generate().getPublicKey(false));

        String serialized = objectMapper.writeValueAsString(accountKeyPublic);
        System.out.println(serialized);

//        AccountKeyFail accountKeyFail = new AccountKeyFail();
//        serialized = objectMapper.writeValueAsString(accountKeyFail);
//        System.out.println(serialized);

        AccountKeyPublic keyPublic = objectMapper.readValue(serialized, AccountKeyPublic.class);
    }

    @org.junit.Test
    public void deserializeAccountKeyWeightedMultiSig() throws IOException  {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String[] publicKey = new String[] {
                "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
        };

        BigInteger threshold = BigInteger.valueOf(2);

        BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
        List<BigInteger> weightList = Arrays.asList(weight);

        WeightedMultiSigOptions option = new WeightedMultiSigOptions(threshold, weightList);
        AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, option);

        String serialized = objectMapper.writeValueAsString(multiSig);
        System.out.println(serialized);

        AccountKeyWeightedMultiSig keyPublic = objectMapper.readValue(serialized, AccountKeyWeightedMultiSig.class);
    }

    @org.junit.Test
    public void deserializeAccountKeyRoleBase() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        IAccountKey roleBased = AccountKeyRoleBased.decode("0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d");
        String serialized = objectMapper.writeValueAsString(roleBased);
        System.out.println(serialized);

        IAccountKey keyPublic = objectMapper.readValue(serialized, AccountKeyRoleBased.class);
    }

    @org.junit.Test
    public void deserializeAccountKeyNil() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AccountKeyNil accountKeyNil = new AccountKeyNil();
        String serialized = objectMapper.writeValueAsString(accountKeyNil);

        System.out.println(serialized);

        AccountKeyNil keyPublic = objectMapper.readValue(serialized, AccountKeyNil.class);
    }

    @org.junit.Test
    public void isHexTest() {
        assertTrue(Utils.isHex("0x"));
    }


    @Ignore
    public void isEmptySigTest() {
        SignatureData signatureData = new SignatureData(
                "0x0fe9",
                "0x018a9f680a74e275f1f83a5c2c45e1313c52432df4595e944240b1511a4f4ba7",
                "0x2d762c3417f91b81db4907db832cb28cc64df7dca3ea9be64899ab3f4812f016"
        );

        List<SignatureData> signatureDataList = new ArrayList<>();

        signatureDataList.add(signatureData);
        signatureDataList.add(signatureData);
        signatureDataList.add(SignatureData.getEmptySignature());


        assertTrue(Utils.isEmptySig(signatureDataList));
    }

    @org.junit.Test
    public void transactionType() {
        System.out.println(TransactionType.TxTypeAccountUpdate);

        String to = TransactionType.TxTypeAccountUpdate.toString();
        assertTrue(to.contains("AccountUpdate"));
    }

    @org.junit.Test
    public void txEncode() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        String value = "0xa";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

        SignatureData senderSignatureData = new SignatureData(
                "0x25",
                "0x9f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956",
                "0x6bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7"
        );

        SignatureData feePayerSignatureData = new SignatureData(
                "0x26",
                "0xf45cf8d7f88c08e6b6ec0b3b562f34ca94283e4689021987abb6b0772ddfd80a",
                "0x298fe2c5aeabb6a518f4cbb5ff39631a5d88be505d3923374f65fdcf63c2955b"
        );

        FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                .setNonce(nonce)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setTo(to)
                .setChainId(chainID)
                .setValue(value)
                .setFrom(from)
                .setFeePayer(feePayer)
                .setSignatures(senderSignatureData)
                .setFeePayerSignatures(feePayerSignatureData)
                .build();

        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setNonce(nonce)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setTo(to)
                .setChainId(chainID)
                .setValue(value)
                .setFrom(from)
                .setSignatures(senderSignatureData)
                .build();

//        valueTransfer.getSignatures().remove(0);

        String serialized = objectMapper.writeValueAsString(valueTransfer);

        serialized = objectMapper.writeValueAsString(feeDelegatedValueTransfer);

        System.out.println(serialized);

    }

    @org.junit.Test
    public void commonTransactionSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        String value = "0xa";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

        SignatureData senderSignatureData = new SignatureData(
                "0x25",
                "0x9f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956",
                "0x6bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7"
        );

        AbstractTransaction valueTransfer = new ValueTransfer.Builder()
                .setNonce(nonce)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setTo(to)
                .setChainId(chainID)
                .setValue(value)
                .setFrom(from)
                .setSignatures(senderSignatureData)
                .build();

        String serialized = objectMapper.writeValueAsString(valueTransfer);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void feeDelegatedSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        String value = "0xa";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";

        SignatureData senderSignatureData = new SignatureData(
                "0x25",
                "0x9f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956",
                "0x6bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7"
        );

        SignatureData feePayerSignatureData = new SignatureData(
                "0x26",
                "0xf45cf8d7f88c08e6b6ec0b3b562f34ca94283e4689021987abb6b0772ddfd80a",
                "0x298fe2c5aeabb6a518f4cbb5ff39631a5d88be505d3923374f65fdcf63c2955b"
        );

        FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                .setNonce(nonce)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setTo(to)
                .setChainId(chainID)
                .setValue(value)
                .setFrom(from)
                .setFeePayer(feePayer)
                .setSignatures(senderSignatureData)
                .setFeePayerSignatures(feePayerSignatureData)
                .build();

        String serialized = objectMapper.writeValueAsString(feeDelegatedValueTransfer);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void feeRatioSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        String value = "0xa";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
        BigInteger feeRatio = BigInteger.valueOf(30);

        SignatureData senderSignatureData = new SignatureData(
                "0x25",
                "0xdde32b8241f039a82b124fe94d3e556eb08f0d6f26d07dcc0f3fca621f1090ca",
                "0x1c8c336b358ab6d3a2bbf25de2adab4d01b754e2fb3b9b710069177d54c1e956"
        );

        AbstractTransaction feeDelegatedWithRatioTransaction = new FeeDelegatedValueTransferWithRatio.Builder()
                .setNonce(nonce)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setTo(to)
                .setChainId(chainID)
                .setValue(value)
                .setFrom(from)
                .setSignatures(senderSignatureData)
                .setFeePayer(feePayer)
                .setFeeRatio(feeRatio)
                .build();

        String serialized = objectMapper.writeValueAsString(feeDelegatedWithRatioTransaction);
        System.out.println(serialized);
    }

    @org.junit.Test
    public void getRLPEncoding() {
        String rawTx = "0x08f87e018505d21dba008261a894584fd5100f6544e4963f85ce36d1998ee3afabbe0194e603af9c0a3541dd87d6b97932fc5635ad66fb83f847f845824e43a006e9afb5d3081b2af5d3cd95452246c25489e315fe8ffcc11d9d61a7a2b203b9a06cfd0aa79df7f86a1d88b49e7e131c98349960ddfbe7b08f4b159615ba449511";

        String from = "0xe603af9c0a3541dd87d6b97932fc5635ad66fb83";
        String to = "0x584fd5100f6544e4963f85ce36d1998ee3afabbe";
        String gas = "0x61a8";
        String gasPrice = "0x5d21dba00";
        String nonce = "0x1";
        String chainID = "0x2710";
        String value = "0x1";

        SignatureData senderSignatureData = new SignatureData(
                "0x4e43",
                "0x06e9afb5d3081b2af5d3cd95452246c25489e315fe8ffcc11d9d61a7a2b203b9",
                "0x6cfd0aa79df7f86a1d88b49e7e131c98349960ddfbe7b08f4b159615ba449511"
        );

        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setFrom(from)
                .setTo(to)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setChainId(chainID)
                .setValue(value)
                .setSignatures(senderSignatureData)
                .build();

        assertEquals(rawTx, valueTransfer.getRLPEncoding());
    }

    public static class getAccountTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static String feePayer = "1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);
        static SingleKeyring feePayerKeyring = KeyringFactory.createFromPrivateKey(feePayer);

        String EOAData  = "{\n" +
                "  \"accType\": 1,\n" +
                "  \"account\": {\n" +
                "    \"balance\": 4985316100000000000,\n" +
                "    \"humanReadable\": false,\n" +
                "    \"key\": {\n" +
                "      \"key\": {\n" +
                "        \"x\": \"0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7\",\n" +
                "        \"y\": \"0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6\"\n" +
                "      },\n" +
                "      \"keyType\": 2\n" +
                "    },\n" +
                "    \"nonce\": 11\n" +
                "  }\n" +
                "}";

        String SCAData = "{\n" +
                "  \"accType\": 2,\n" +
                "  \"account\": {\n" +
                "    \"balance\": \"0x0\",\n" +
                "    \"codeFormat\": 0,\n" +
                "    \"codeHash\": \"80NXvdOay02rYC/JgQ7RfF7yoxY1N7W8P7BiPvkIeF8=\",\n" +
                "    \"humanReadable\": false,\n" +
                "    \"key\": {\n" +
                "      \"key\": {},\n" +
                "      \"keyType\": 3\n" +
                "    },\n" +
                "    \"nonce\": 1,\n" +
                "    \"storageRoot\": \"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\"\n" +
                "  }\n" +
                "}";

        @org.junit.Test
        public void getAccountTest() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            Account.AccountData account = caver.rpc.klay.getAccount(klayProviderKeyring.getAddress()).send().getResult();

            assertEquals(IAccountType.AccType.EOA.getAccType(), account.getAccType());
        }

        @org.junit.Test
        public void getAccountWithBlockTag() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            Account.AccountData account = caver.rpc.klay.getAccount(klayProviderKeyring.getAddress(), DefaultBlockParameterName.LATEST).send().getResult();
            assertEquals(IAccountType.AccType.EOA.getAccType(), account.getAccType());
        }

        @org.junit.Test
        public void getAccountWithBlockNumber() throws IOException {
            Caver caver = new Caver(Caver.BAOBAB_URL);
            Web3jService web3jService = new HttpService(Caver.BAOBAB_URL);
            Klay klay = new Klay(web3jService);

            String sca_addr = "0x03d2ab781e6b4492ad6c8ff59f59d2af9e8ab8f7";

            Account.AccountData account = klay.getAccount(sca_addr, 31876852).send().getResult();
            assertEquals(IAccountType.AccType.SCA.getAccType(), account.getAccType());
        }

        @org.junit.Test
        public void getSCAAccountTest() throws IOException {
            Web3jService web3jService = new HttpService(Caver.BAOBAB_URL);
            Klay klay = new Klay(web3jService);
            String sca_addr = "0x03d2ab781e6b4492ad6c8ff59f59d2af9e8ab8f7";

            Account.AccountData account = klay.getAccount(sca_addr).send().getResult();
            assertEquals(IAccountType.AccType.SCA.getAccType(), account.getAccType());
        }
    }

    public static class getAccountKeyTest {
        static String klayProvider = "871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5";
        static String feePayer = "1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178";
        static SingleKeyring klayProviderKeyring = KeyringFactory.createFromPrivateKey(klayProvider);
        static SingleKeyring feePayerKeyring = KeyringFactory.createFromPrivateKey(feePayer);

        @org.junit.Test
        public void getAccountKey() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            AccountKey accountKeyResponse = caver.rpc.klay.getAccountKey(klayProviderKeyring.getAddress()).send();

            System.out.println(accountKeyResponse.getResult().getType());
        }

        @org.junit.Test
        public void getAccountKeyWithBlockTag() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            AccountKey accountKeyResponse = caver.rpc.klay.getAccountKey(klayProviderKeyring.getAddress(), DefaultBlockParameterName.PENDING).send();

            System.out.println(accountKeyResponse.getResult().getType());
        }

        @org.junit.Test
        public void getAccountKeyWithBlockNumber() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            AccountKey.AccountKeyData accountKey = caver.rpc.klay.getAccountKey(klayProviderKeyring.getAddress(), 128).send().getResult();
            System.out.println(accountKey.getType());
        }

        @Test
        public void getAccountKeyMain() throws IOException {
            Caver caver = new Caver(Caver.MAINNET_URL);
            AccountKey.AccountKeyData accountKey = caver.rpc.klay.getAccountKey("0x80f4009fb84eb6a151565657fb272bb9f016efb6").send().getResult();
        }
    }

    public static class getDecodedAnchoringTransactionTest {
        @org.junit.Test
        public void test() throws IOException {
            Caver caver = new Caver(Caver.BAOBAB_URL);

            String txHash = "0x95fc94f3fcf1e03e550d7fee5d3690b32bddd766a7a4725c72044be9ddf94e84";

            DecodeAnchoringTransaction chainDataAnchoringResponse = caver.rpc.klay.getDecodedAnchoringTransaction(txHash).send();
        }
    }

    @Test
    public void getTransactionReceipt() throws IOException {
        String hash = "0xcdf2bc727b1505dbd27bff89f55a2d9fe994846fc25d9403e45e74273681fafe";

        Caver caver = new Caver(Caver.MAINNET_URL);

        TransactionReceipt receipt = caver.rpc.klay.getTransactionReceipt(hash).send();
    }

    @Test
    public void getTransactionByHash() throws IOException {
        String hash = "0xcdf2bc727b1505dbd27bff89f55a2d9fe994846fc25d9403e45e74273681fafe";
        String contractExeHash = "0x7ea4f553a4bc22df206a2bbbf7439ac190d310e23c286a9e9dbc1e580fa76898";
        String feeRatioHash = "0xf619c0fa8a4b197ed95532410bcad7945fa30462980012794ffb6d01e54df35d";

        Caver caver = new Caver(Caver.MAINNET_URL);

        Transaction receipt = caver.rpc.klay.getTransactionByHash(hash).send();
        System.out.println(receipt.getResult().getBlockNumber());

        receipt = caver.rpc.klay.getTransactionByHash(contractExeHash).send();
        System.out.println(receipt.getResult().getBlockNumber());

        receipt = caver.rpc.klay.getTransactionByHash(feeRatioHash).send();
        System.out.println(receipt.getResult().getBlockNumber());
    }

    @Test
    public void getBlockByNumber() throws IOException {
        Caver caver = new Caver(Caver.BAOBAB_URL);

        Block block = caver.rpc.klay.getBlockByNumber(32039120).send();
    }

    @Test
    public void getBlockReceipts() throws IOException {
        Caver caver = new Caver(Caver.BAOBAB_URL);

        BlockTransactionReceipts receipts = caver.rpc.klay.getBlockReceipts("0x16bf5508420c6b8466ca0b4cfee0c1e462725ba3dbe0262ac2ba4cd6f2cd9814").send();
    }

    @Test
    public void getBlockWithConsensusInfo() throws IOException {
        Caver caver = new Caver(Caver.BAOBAB_URL);

        BlockWithConsensusInfo info = caver.rpc.klay.getBlockWithConsensusInfoByHash("0x16bf5508420c6b8466ca0b4cfee0c1e462725ba3dbe0262ac2ba4cd6f2cd9814").send();
    }

}
