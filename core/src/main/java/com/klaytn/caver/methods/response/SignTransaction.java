package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.List;

public class SignTransaction extends Response<SignTransaction.SignTransactionData> {


    public static class SignTransactionData {
        String raw;
        Transaction.TransactionData tx;

        public SignTransactionData() {
        }

        public SignTransactionData(String raw, Transaction.TransactionData tx) {
            this.raw = raw;
            this.tx = tx;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public Transaction.TransactionData getTx() {
            return tx;
        }

        public void setTx(Transaction.TransactionData tx) {
            this.tx = tx;
        }
    }

    public static class SignTransactionDataDeSerializer extends JsonDeserializer<SignTransactionData> {
        @Override
        public SignTransactionData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            JsonNode node = p.getCodec().readTree(p);

            JsonNode tx = node.get("tx");
            String rawTx = node.get("raw").asText();

            Transaction.TransactionData data = mapper.readValue(tx.toString(), Transaction.TransactionData.class);

            return new SignTransactionData(rawTx, data);
        }
    }
}
