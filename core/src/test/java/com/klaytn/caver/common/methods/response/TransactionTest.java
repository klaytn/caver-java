package com.klaytn.caver.common.methods.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.methods.response.Transaction;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@RunWith(Enclosed.class)
public class TransactionTest {
    public static String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }

    public static class ethereumAccessListTest {
        @Test
        public void deserializeTest() throws IOException {
            String ethereumAccessListJson = "{\n" +
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
                    "  \"from\": \"0xca7a99380131e6c76cfa622396347107aeedca2d\",\n" +
                    "  \"gas\": 999999,\n" +
                    "  \"gasPrice\": 25000000000,\n" +
                    "  \"hash\": \"0x658b0aa2b625a1a0c3c9fe54e7f5f51ba962aeb7e62ae2ed338597058a603d71\",\n" +
                    "  \"input\": \"0x\",\n" +
                    "  \"nonce\": 0,\n" +
                    "  \"senderTxHash\": \"0x658b0aa2b625a1a0c3c9fe54e7f5f51ba962aeb7e62ae2ed338597058a603d71\",\n" +
                    "  \"signatures\": [\n" +
                    "    {\n" +
                    "      \"R\": \"0x1c064e8434def2a74b129dfaa5bd98e8fb8402fc5db284ecdd6807351681ba75\",\n" +
                    "      \"S\": \"0x3515882ee8d0d3a59c3075eca9748695f0a3e9a7f52b7e4987950eef52bbb42\",\n" +
                    "      \"V\": \"0x1\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"to\": \"0x8c9f4468ae04fb3d79c80f6eacf0e4e1dd21deee\",\n" +
                    "  \"transactionIndex\": 0,\n" +
                    "  \"type\": \"TxTypeEthereumAccessList\",\n" +
                    "  \"typeInt\": 30721,\n" +
                    "  \"value\": 1\n" +
                    "}";

            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new StringReader(ethereumAccessListJson);
            Transaction.TransactionData transactionData = objectMapper.readValue(reader, Transaction.TransactionData.class);
            System.out.println(objectToString(transactionData));
        }
    }
}
