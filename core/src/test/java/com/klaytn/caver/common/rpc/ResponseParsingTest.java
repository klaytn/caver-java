package com.klaytn.caver.common.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Transaction;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class ResponseParsingTest {
    static Caver caver = new Caver();

    public static class TransactionTest {
        static String expectedGasPrice = "0x5d21dba00";

        @Test
        public void ethereumAccessListTest() throws IOException {
            String ethereumAccessListJson = "{\n" +
                    "  \"accessList\": [\n" +
                    "    {\n" +
                    "      \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "      \"storageKeys\": [\n" +
                    "        \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"blockHash\": \"0xd4287f78b7a928295bf414c6755c0fda0adc2ce6f1f62dc9fbdb5e9206a961bd\",\n" +
                    "  \"blockNumber\": \"0x3f1e\",\n" +
                    "  \"chainId\": \"0x7e3\",\n" +
                    "  \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "  \"gas\": \"0xc350\",\n" +
                    "  \"gasPrice\": \"0x5d21dba00\",\n" +
                    "  \"hash\": \"0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6\",\n" +
                    "  \"input\": \"0x\",\n" +
                    "  \"nonce\": \"0x2\",\n" +
                    "  \"senderTxHash\": \"0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6\",\n" +
                    "  \"signatures\": [\n" +
                    "    {\n" +
                    "      \"V\": \"0x0\",\n" +
                    "      \"R\": \"0x15f22a7da8ee3a48574a5bec2f17d5f2490f6d79368ada258661c5c9532496c9\",\n" +
                    "      \"S\": \"0x27033978588f3e2d0699c6af67ec5d28eef7833408e2780c0091456235f38f72\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"to\": \"0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee\",\n" +
                    "  \"transactionIndex\": \"0x0\",\n" +
                    "  \"type\": \"TxTypeEthereumAccessList\",\n" +
                    "  \"typeInt\": 30721,\n" +
                    "  \"value\": \"0x1\"\n" +
                    "}";
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            Reader reader = new StringReader(ethereumAccessListJson);
            Transaction.TransactionData transactionData = objectMapper.readValue(reader, Transaction.TransactionData.class);
            AccessList expectedAccessList = caver.transaction.utils.accessList.create(
                    Arrays.asList(
                            caver.transaction.utils.accessTuple.create(
                                    "0xca7a99380131e6c76cfa622396347107aeedca2d",
                                    Arrays.asList("0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f")
                            )
                    )
            );
            Assert.assertEquals(expectedAccessList, transactionData.getAccessList());
            Assert.assertEquals("0xd4287f78b7a928295bf414c6755c0fda0adc2ce6f1f62dc9fbdb5e9206a961bd", transactionData.getBlockHash());
            Assert.assertEquals("0x3f1e", transactionData.getBlockNumber());
            Assert.assertEquals("0x7e3", transactionData.getChainID());
            Assert.assertEquals("0xca7a99380131e6c76cfa622396347107aeedca2d", transactionData.getFrom());
            Assert.assertEquals("0xc350", transactionData.getGas());
            Assert.assertEquals(expectedGasPrice, transactionData.getGasPrice());
            Assert.assertEquals("0x2", transactionData.getNonce());
            String expecedTxHash = "0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6";
            Assert.assertEquals(expecedTxHash, transactionData.getSenderTxHash());
            SignatureData expectedSignature = new SignatureData(
                    "0x00",
                    "0x15f22a7da8ee3a48574a5bec2f17d5f2490f6d79368ada258661c5c9532496c9",
                    "0x27033978588f3e2d0699c6af67ec5d28eef7833408e2780c0091456235f38f72"
            );
            Assert.assertEquals(expectedSignature, transactionData.getSignatures().get(0));
            Assert.assertEquals("0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee", transactionData.getTo());
            Assert.assertEquals("TxTypeEthereumAccessList", transactionData.getType());
            Assert.assertEquals("30721", transactionData.getTypeInt());
            Assert.assertEquals("0x1", transactionData.getValue());
        }

        @Test
        public void ethereumDynamicFeeTest() throws IOException {
            String ethereumDynamicFeeJson = "{\n" +
                    "    \"accessList\": [\n" +
                    "      {\n" +
                    "        \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "        \"storageKeys\": [\n" +
                    "          \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\",\n" +
                    "          \"0x3d228ec053cf862382d404e79c80391ade4af5aca19ace983a4c6bd698a4e9f1\"\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"blockHash\": \"0x2cb63fb3f9764280061a40fa6993b5281ae775f22ae71db045d05f2b90f1c9d0\",\n" +
                    "    \"blockNumber\": \"0xabc1\",\n" +
                    "    \"chainId\": \"0x7e3\",\n" +
                    "    \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "    \"gas\": \"0x7a120\",\n" +
                    "    \"hash\": \"0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19\",\n" +
                    "    \"input\": \"0x\",\n" +
                    "    \"maxFeePerGas\": \"0x5d21dba00\",\n" +
                    "    \"maxPriorityFeePerGas\": \"0x5d21dba00\",\n" +
                    "    \"nonce\": \"0x0\",\n" +
                    "    \"senderTxHash\": \"0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19\",\n" +
                    "    \"signatures\": [\n" +
                    "      {\n" +
                    "        \"V\": \"0x0\",\n" +
                    "        \"R\": \"0xef11e538b4ae74704c26d0d23da0d93fea4ca65a1d9a924819b43fa6aeee3923\",\n" +
                    "        \"S\": \"0x2456ffa6da778525e44634306a81ada9effbbc2ab63c272a8ae208baac4e3deb\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"to\": \"0x3e2ac308cd78ac2fe162f9522deb2b56d9da9499\",\n" +
                    "    \"transactionIndex\": \"0x0\",\n" +
                    "    \"type\": \"TxTypeEthereumDynamicFee\",\n" +
                    "    \"typeInt\": 30722,\n" +
                    "    \"value\": \"0x1\"\n" +
                    "  }";
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            Reader reader = new StringReader(ethereumDynamicFeeJson);
            Transaction.TransactionData transactionData = objectMapper.readValue(reader, Transaction.TransactionData.class);
            AccessList expectedAccessList = caver.transaction.utils.accessList.create(
                    Arrays.asList(
                            caver.transaction.utils.accessTuple.create(
                                    "0xca7a99380131e6c76cfa622396347107aeedca2d",
                                    Arrays.asList(
                                            "0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f",
                                            "0x3d228ec053cf862382d404e79c80391ade4af5aca19ace983a4c6bd698a4e9f1"
                                    )
                            )
                    )
            );
            Assert.assertEquals(expectedAccessList, transactionData.getAccessList());
            Assert.assertEquals("0x2cb63fb3f9764280061a40fa6993b5281ae775f22ae71db045d05f2b90f1c9d0", transactionData.getBlockHash());
            Assert.assertEquals("0xabc1", transactionData.getBlockNumber());
            Assert.assertEquals("0x7e3", transactionData.getChainID());
            Assert.assertEquals("0xca7a99380131e6c76cfa622396347107aeedca2d", transactionData.getFrom());
            Assert.assertEquals("0x7a120", transactionData.getGas());
            Assert.assertEquals(expectedGasPrice, transactionData.getMaxPriorityFeePerGas());
            Assert.assertEquals(expectedGasPrice, transactionData.getMaxFeePerGas());
            String expectedTxHash = "0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19";
            Assert.assertEquals(expectedTxHash, transactionData.getSenderTxHash());
            SignatureData expectedSignature = new SignatureData(
                    "0x00",
                    "0xef11e538b4ae74704c26d0d23da0d93fea4ca65a1d9a924819b43fa6aeee3923",
                    "0x2456ffa6da778525e44634306a81ada9effbbc2ab63c272a8ae208baac4e3deb"
            );
            Assert.assertEquals(expectedSignature, transactionData.getSignatures().get(0));
            Assert.assertEquals("0x3e2ac308cd78ac2fe162f9522deb2b56d9da9499", transactionData.getTo());
            Assert.assertEquals("TxTypeEthereumDynamicFee", transactionData.getType());
            Assert.assertEquals("30722", transactionData.getTypeInt());
            Assert.assertEquals("0x1", transactionData.getValue());
        }
    }

    public static class TransactionReceiptTest {
        static String expectedGasPrice = "0x5d21dba00";
        @Test
        public void ethereumAccessListTest() throws IOException {
            String ethereumAccessListReceiptJson = "{\n" +
                    "  \"accessList\": [\n" +
                    "    {\n" +
                    "      \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "      \"storageKeys\": [\n" +
                    "        \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"blockHash\": \"0xd4287f78b7a928295bf414c6755c0fda0adc2ce6f1f62dc9fbdb5e9206a961bd\",\n" +
                    "  \"blockNumber\": \"0x3f1e\",\n" +
                    "  \"chainId\": \"0x7e3\",\n" +
                    "  \"contractAddress\": null,\n" +
                    "  \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "  \"gas\": \"0xc350\",\n" +
                    "  \"gasPrice\": \"0x5d21dba00\",\n" +
                    "  \"gasUsed\": \"0x62d4\",\n" +
                    "  \"input\": \"0x\",\n" +
                    "  \"logs\": [],\n" +
                    "  \"logsBloom\": \"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n" +
                    "  \"nonce\": \"0x2\",\n" +
                    "  \"senderTxHash\": \"0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6\",\n" +
                    "  \"signatures\": [\n" +
                    "    {\n" +
                    "      \"V\": \"0x0\",\n" +
                    "      \"R\": \"0x15f22a7da8ee3a48574a5bec2f17d5f2490f6d79368ada258661c5c9532496c9\",\n" +
                    "      \"S\": \"0x27033978588f3e2d0699c6af67ec5d28eef7833408e2780c0091456235f38f72\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"status\": \"0x1\",\n" +
                    "  \"to\": \"0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee\",\n" +
                    "  \"transactionHash\": \"0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6\",\n" +
                    "  \"transactionIndex\": \"0x0\",\n" +
                    "  \"type\": \"TxTypeEthereumAccessList\",\n" +
                    "  \"typeInt\": 30721,\n" +
                    "  \"value\": \"0x1\"\n" +
                    "}";
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            Reader reader = new StringReader(ethereumAccessListReceiptJson);
            TransactionReceipt.TransactionReceiptData transactionReceiptData = objectMapper.readValue(reader, TransactionReceipt.TransactionReceiptData.class);
            AccessList expectedAccessList = caver.transaction.utils.accessList.create(
                    Arrays.asList(
                            caver.transaction.utils.accessTuple.create(
                                    "0xca7a99380131e6c76cfa622396347107aeedca2d",
                                    Arrays.asList("0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f")
                            )
                    )
            );
            Assert.assertEquals(expectedAccessList, transactionReceiptData.getAccessList());
            Assert.assertEquals("0xd4287f78b7a928295bf414c6755c0fda0adc2ce6f1f62dc9fbdb5e9206a961bd", transactionReceiptData.getBlockHash());
            Assert.assertEquals("0x3f1e", transactionReceiptData.getBlockNumber());
            Assert.assertEquals("0x7e3", transactionReceiptData.getChainID());
            Assert.assertEquals("0xca7a99380131e6c76cfa622396347107aeedca2d", transactionReceiptData.getFrom());
            Assert.assertEquals("0xc350", transactionReceiptData.getGas());
            Assert.assertEquals(expectedGasPrice, transactionReceiptData.getGasPrice());
            Assert.assertEquals("0x62d4", transactionReceiptData.getGasUsed());
            String expecedTxHash = "0xcda2cfcd8c92e0c8f48e4695ade2147fa6c47930d98a16098cd7c743b2354fc6";
            Assert.assertEquals(expecedTxHash, transactionReceiptData.getSenderTxHash());
            SignatureData expectedSignature = new SignatureData(
                    "0x00",
                    "0x15f22a7da8ee3a48574a5bec2f17d5f2490f6d79368ada258661c5c9532496c9",
                    "0x27033978588f3e2d0699c6af67ec5d28eef7833408e2780c0091456235f38f72"
            );
            Assert.assertEquals(expectedSignature, transactionReceiptData.getSignatures().get(0));
            Assert.assertEquals("0x1", transactionReceiptData.getStatus());
            Assert.assertEquals("0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee", transactionReceiptData.getTo());
            Assert.assertEquals(expecedTxHash, transactionReceiptData.getTransactionHash());
            Assert.assertEquals("TxTypeEthereumAccessList", transactionReceiptData.getType());
            Assert.assertEquals("30721", transactionReceiptData.getTypeInt());
            Assert.assertEquals("0x1", transactionReceiptData.getValue());
        }

        @Test
        public void ethereumDynamicFeeTest() throws IOException {
            String ethereumDynamicFeeReceiptJson = "{\n" +
                    "  \"accessList\": [\n" +
                    "    {\n" +
                    "      \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "      \"storageKeys\": [\n" +
                    "        \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\",\n" +
                    "        \"0x3d228ec053cf862382d404e79c80391ade4af5aca19ace983a4c6bd698a4e9f1\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"blockHash\": \"0x2cb63fb3f9764280061a40fa6993b5281ae775f22ae71db045d05f2b90f1c9d0\",\n" +
                    "  \"blockNumber\": \"0xabc1\",\n" +
                    "  \"chainId\": \"0x7e3\",\n" +
                    "  \"contractAddress\": null,\n" +
                    "  \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "  \"gas\": \"0x7a120\",\n" +
                    "  \"gasUsed\": \"0x6a40\",\n" +
                    "  \"input\": \"0x\",\n" +
                    "  \"logs\": [],\n" +
                    "  \"logsBloom\": \"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n" +
                    "  \"maxFeePerGas\": \"0x5d21dba00\",\n" +
                    "  \"maxPriorityFeePerGas\": \"0x5d21dba00\",\n" +
                    "  \"nonce\": \"0x0\",\n" +
                    "  \"senderTxHash\": \"0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19\",\n" +
                    "  \"signatures\": [\n" +
                    "    {\n" +
                    "      \"V\": \"0x0\",\n" +
                    "      \"R\": \"0xef11e538b4ae74704c26d0d23da0d93fea4ca65a1d9a924819b43fa6aeee3923\",\n" +
                    "      \"S\": \"0x2456ffa6da778525e44634306a81ada9effbbc2ab63c272a8ae208baac4e3deb\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"status\": \"0x1\",\n" +
                    "  \"to\": \"0x3e2ac308cd78ac2fe162f9522deb2b56d9da9499\",\n" +
                    "  \"transactionHash\": \"0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19\",\n" +
                    "  \"transactionIndex\": \"0x0\",\n" +
                    "  \"type\": \"TxTypeEthereumDynamicFee\",\n" +
                    "  \"typeInt\": 30722,\n" +
                    "  \"value\": \"0x1\"\n" +
                    "}";
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            Reader reader = new StringReader(ethereumDynamicFeeReceiptJson);
            TransactionReceipt.TransactionReceiptData transactionReceiptData = objectMapper.readValue(reader, TransactionReceipt.TransactionReceiptData.class);
            AccessList expectedAccessList = caver.transaction.utils.accessList.create(
                    Arrays.asList(
                            caver.transaction.utils.accessTuple.create(
                                    "0xca7a99380131e6c76cfa622396347107aeedca2d",
                                    Arrays.asList(
                                            "0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f",
                                            "0x3d228ec053cf862382d404e79c80391ade4af5aca19ace983a4c6bd698a4e9f1"
                                    )
                            )
                    )
            );
            Assert.assertEquals(expectedAccessList, transactionReceiptData.getAccessList());
            Assert.assertEquals("0x2cb63fb3f9764280061a40fa6993b5281ae775f22ae71db045d05f2b90f1c9d0", transactionReceiptData.getBlockHash());
            Assert.assertEquals("0xabc1", transactionReceiptData.getBlockNumber());
            Assert.assertEquals("0x7e3", transactionReceiptData.getChainID());
            Assert.assertEquals("0xca7a99380131e6c76cfa622396347107aeedca2d", transactionReceiptData.getFrom());
            Assert.assertEquals("0x7a120", transactionReceiptData.getGas());
            Assert.assertEquals(expectedGasPrice, transactionReceiptData.getMaxPriorityFeePerGas());
            Assert.assertEquals(expectedGasPrice, transactionReceiptData.getMaxFeePerGas());
            String expectedTxHash = "0x3c43345866a6769f66a1d1e1866674751b39ff7db5061a6e4c6c1916e6395b19";
            Assert.assertEquals(expectedTxHash, transactionReceiptData.getSenderTxHash());
            SignatureData expectedSignature = new SignatureData(
                    "0x00",
                    "0xef11e538b4ae74704c26d0d23da0d93fea4ca65a1d9a924819b43fa6aeee3923",
                    "0x2456ffa6da778525e44634306a81ada9effbbc2ab63c272a8ae208baac4e3deb"
            );
            Assert.assertEquals(expectedSignature, transactionReceiptData.getSignatures().get(0));
            Assert.assertEquals("0x1", transactionReceiptData.getStatus());
            Assert.assertEquals("0x3e2ac308cd78ac2fe162f9522deb2b56d9da9499", transactionReceiptData.getTo());
            Assert.assertEquals(expectedTxHash, transactionReceiptData.getTransactionHash());
            Assert.assertEquals("TxTypeEthereumDynamicFee", transactionReceiptData.getType());
            Assert.assertEquals("30722", transactionReceiptData.getTypeInt());
            Assert.assertEquals("0x1", transactionReceiptData.getValue());
        }
    }
}
