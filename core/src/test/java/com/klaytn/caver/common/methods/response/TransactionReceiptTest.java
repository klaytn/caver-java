package com.klaytn.caver.common.methods.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class TransactionReceiptTest {
    public static String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }

    public static class ethereumAccessListTest {
        @Test
        public void deserializeTest() throws IOException {
            String ethereumAccessListReceiptJson = "{\n" +
                    "  \"accessList\": [\n" +
                    "    {\n" +
                    "      \"address\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "      \"storageKeys\": [\n" +
                    "        \"0x0709c257577296fac29c739dad24e55b70a260497283cf9885ab67b4daa9b67f\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"blockHash\": \"0x43459f308ba7e54976491922c062cad07c70798a24de403ee830f2c36f0eb59e\",\n" +
                    "  \"blockNumber\": 91,\n" +
                    "  \"chainID\": \"0x7e3\",\n" +
                    "  \"contractAddress\": null,\n" +
                    "  \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "  \"gas\": \"0xf423f\",\n" +
                    "  \"gasPrice\": \"0x5d21dba00\",\n" +
                    "  \"gasUsed\": 25300,\n" +
                    "  \"input\": \"0x\",\n" +
                    "  \"logs\": [],\n" +
                    "  \"logsBloom\": \"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n" +
                    "  \"nonce\": \"0x0\",\n" +
                    "  \"senderTxHash\": \"0x658b0aa2b625a1a0c3c9fe54e7f5f51ba962aeb7e62ae2ed338597058a603d71\",\n" +
                    "  \"signatures\": [\n" +
                    "    {\n" +
                    "      \"R\": \"0x1c064e8434def2a74b129dfaa5bd98e8fb8402fc5db284ecdd6807351681ba75\",\n" +
                    "      \"S\": \"0x3515882ee8d0d3a59c3075eca9748695f0a3e9a7f52b7e4987950eef52bbb42\",\n" +
                    "      \"V\": \"0x1\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"status\": \"0x1\",\n" +
                    "  \"to\": \"0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee\",\n" +
                    "  \"transactionHash\": \"0x658b0aa2b625a1a0c3c9fe54e7f5f51ba962aeb7e62ae2ed338597058a603d71\",\n" +
                    "  \"transactionIndex\": 0,\n" +
                    "  \"type\": \"TxTypeEthereumAccessList\",\n" +
                    "  \"typeInt\": 30721,\n" +
                    "  \"value\": \"0x1\"\n" +
                    "}";
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(ethereumAccessListReceiptJson);
            TransactionReceipt.TransactionReceiptData transactionReceiptData = objectMapper.readValue(reader, TransactionReceipt.TransactionReceiptData.class);
            System.out.println(objectToString(transactionReceiptData));
        }
    }

    public static class ethereumDynamicFeeTest {
        @Test
        public void deserializeTest() throws IOException {
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
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(ethereumDynamicFeeReceiptJson);
            TransactionReceipt.TransactionReceiptData transactionReceiptData = objectMapper.readValue(reader, TransactionReceipt.TransactionReceiptData.class);
            System.out.println(objectToString(transactionReceiptData));
        }
    }
}
