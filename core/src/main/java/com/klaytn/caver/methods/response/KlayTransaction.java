/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/EthTransaction.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @deprecated Please use {@link com.klaytn.caver.methods.response.Transaction} instead.
 */
@Deprecated
public class KlayTransaction extends Response<KlayTransaction.Transaction> {

    public Optional<Transaction> getTransaction() {
        return Optional.ofNullable(getResult());
    }

    public static class Transaction {

        /**
         * Hash of the block where this transaction was in. null when its pending.
         */
        private String blockHash;

        /**
         * Block number where this transaction was in. null when its pending.
         */
        private String blockNumber;

        /**
         * Address of the sender.
         */
        private String from;

        /**
         * Gas provided by the sender.
         */
        private String gas;

        /**
         * Gas price provided by the sender in peb.
         */
        private String gasPrice;

        /**
         * Hash of the transaction.
         */
        private String hash;

        /**
         * The data send along with the transaction.
         */
        private String input;

        /**
         * The number of transactions made by the sender prior to this one.
         */
        private String nonce;

        /**
         * Hash of the tx without the fee payer's address and signature. This value is always the same
         * as the value of transactionHash for non fee-delegated transactions.
         */
        private String senderTxHash;

        /**
         * An array of signature objects. A signature object contains three fields (V, R, and S). V contains ECDSA
         * recovery id. R contains ECDSA signature r while S contains ECDSA signature s.
         */
        private List<KlaySignatureData> signatures;

        /**
         * Address of the receiver. null when its a contract creation transaction.
         */
        private String to;

        /**
         * Integer of the transactions index position in the block. null when its pending.
         */
        private String transactionIndex;

        /**
         * A string representing the type of the transaction.
         */
        private String type;

        /**
         * An integer representing the type of the transaction.
         */
        private String typeInt;

        /**
         * Value transferred in peb.
         */
        private String value;

        public Transaction() {
        }

        public Transaction(String blockHash, String blockNumber, String from, String gas, String gasPrice, String hash,
                           String input, String nonce, String senderTxHash, List<KlaySignatureData> signatures,
                           String to, String transactionIndex, String type, String typeInt, String value) {
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.from = from;
            this.gas = gas;
            this.gasPrice = gasPrice;
            this.hash = hash;
            this.input = input;
            this.nonce = nonce;
            this.senderTxHash = senderTxHash;
            this.signatures = signatures;
            this.to = to;
            this.transactionIndex = transactionIndex;
            this.type = type;
            this.typeInt = typeInt;
            this.value = value;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public String getFrom() {
            return from;
        }

        public String getGas() {
            return gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public String getHash() {
            return hash;
        }

        public String getInput() {
            return input;
        }

        public String getNonce() {
            return nonce;
        }

        public String getSenderTxHash() {
            return senderTxHash;
        }

        public List<KlaySignatureData> getSignatures() {
            return signatures;
        }

        @JsonDeserialize(using = KlayTransaction.KlayTransactionDeserializer.class)
        public void setSignatures(List<KlaySignatureData> signatures) {
            this.signatures = signatures;
        }

        public String getTo() {
            return to;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public String getType() {
            return type;
        }

        public String getTypeInt() {
            return typeInt;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Transaction)) {
                return false;
            }

            Transaction that = (Transaction) o;

            if (getBlockHash() != null
                    ? !getBlockHash().equals(that.getBlockHash()) : that.getBlockHash() != null) {
                return false;
            }
            if (getBlockNumber() != null
                    ? !getBlockNumber().equals(that.getBlockNumber()) : that.getBlockNumber() != null) {
                return false;
            }
            if (getFrom() != null
                    ? !getFrom().equals(that.getFrom()) : that.getFrom() != null) {
                return false;
            }
            if (getGas() != null
                    ? !getGas().equals(that.getGas()) : that.getGas() != null) {
                return false;
            }
            if (getGasPrice() != null
                    ? !getGasPrice().equals(that.getGasPrice()) : that.getGasPrice() != null) {
                return false;
            }
            if (getHash() != null
                    ? !getHash().equals(that.getHash()) : that.getHash() != null) {
                return false;
            }
            if (getInput() != null ? !getInput().equals(that.getInput()) : that.getInput() != null) {
                return false;
            }
            if (getNonce() != null ? !getNonce().equals(that.getNonce()) : that.getNonce() != null) {
                return false;
            }
            if (getSenderTxHash() != null ? !getSenderTxHash().equals(that.getSenderTxHash()) : that.getSenderTxHash() != null) {
                return false;
            }
            if (getSignatures() != null ? !getSignatures().equals(that.getSignatures()) : that.getSignatures() != null) {
                return false;
            }
            if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null) {
                return false;
            }
            if (getTransactionIndex() != null ? !getTransactionIndex().equals(that.getTransactionIndex()) : that.getTransactionIndex() != null) {
                return false;
            }
            if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
                return false;
            }
            if (getTypeInt() != null ? !getTypeInt().equals(that.getTypeInt()) : that.getTypeInt() != null) {
                return false;
            }
            return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
        }

        @Override
        public int hashCode() {
            int result = getBlockHash() != null ? getBlockHash().hashCode() : 0;
            result = 31 * result + (getBlockNumber() != null ? getBlockNumber().hashCode() : 0);
            result = 31 * result + (getFrom() != null ? getFrom().hashCode() : 0);
            result = 31 * result + (getGas() != null ? getGas().hashCode() : 0);
            result = 31 * result + (getGasPrice() != null ? getGasPrice().hashCode() : 0);
            result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
            result = 31 * result + (getInput() != null ? getInput().hashCode() : 0);
            result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
            result = 31 * result + (getSenderTxHash() != null ? getSenderTxHash().hashCode() : 0);
            result = 31 * result + (getSignatures() != null ? getSignatures().hashCode() : 0);
            result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
            result = 31 * result + (getTransactionIndex() != null ? getTransactionIndex().hashCode() : 0);
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            result = 31 * result + (getTypeInt() != null ? getTypeInt().hashCode() : 0);
            result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
            return result;
        }
    }

    public static class KlayTransactionDeserializer extends JsonDeserializer<List<KlaySignatureData>> {

        @Override
        public List<KlaySignatureData> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Iterator<JsonNode> iterator = node.iterator();
            List<KlaySignatureData> klaySignatureData = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode innerNode = iterator.next();
                byte[] v = Numeric.hexStringToByteArray(innerNode.get("V").toString());
                byte[] r = Numeric.hexStringToByteArray(innerNode.get("R").toString());
                byte[] s = Numeric.hexStringToByteArray(innerNode.get("S").toString());
                klaySignatureData.add(new KlaySignatureData(v, r, s));
            }
            return klaySignatureData;
        }
    }

}
