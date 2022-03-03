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
}
