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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/response/EthGetTransactionReceipt.java (2019/06/13).
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
 * @deprecated Please use {@link com.klaytn.caver.methods.response.TransactionReceipt} instead.
 */
@Deprecated
public class KlayTransactionReceipt extends Response<KlayTransactionReceipt.TransactionReceipt> {

    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(getResult());
    }

    enum TxError {
        RUNTIME_ERROR("0x2", "runtime error occurred in interpreter"),
        MAX_CALL_EXCEED("0x3", "max call depth exceeded"),
        CONTRACT_ADDRESS_COLLISION("0x4", "contract address collision"),
        CONTRACT_OUT_OF_GAS("0x5", "contract creation code storage out of gas"),
        MAX_CODE_SIZE_EXCEED("0x6", "evm: max code size exceeded"),
        OUT_OF_GAS("0x7", "out of gas"),
        WRITE_PROTECTION("0x8", "evm: write protection"),
        EXECUTION_REVERTED("0x9", "evm: execution reverted"),
        OPCODE_COUNT_LIMIT("0xa", "reached the opcode count limit"),
        ACCOUNT_EXIST("0xb", "account already exists"),
        NOT_PROGRAM_ACCOUNT("0xc", "not a program account (e.g., an account having code and storage)"),
        NOT_HUMAN_READABLE_ACCOUNT("0xd", "not a human readable address"),
        FEE_RATIO_OUT_OF_RANGE("0xe", "fee ratio is out of range [1, 99]"),
        ACCOUNTKEYFAIL_NOT_UPDATABLE("0xf", "AccountKeyFail is not updatable"),
        DIFFERENT_ACCOUNTKEY_TYPE("0x10", "different account key type"),
        ACCOUNTKEYNIL_CANNOT_INITIALIZED("0x11", "AccountKeyNil cannot be initialized to an account"),
        PUBLICKEY_NOT_ON_CURVE("0x12", "public key is not on curve"),
        KEY_WEIGHT_ZERO("0x13", "key weight is zero"),
        KEY_NOT_SERIALIZABLE("0x14", "key is not serializable"),
        KEY_DUPLICATED("0x15", "duplicated key"),
        WEIGHTED_SUM_OVERFLOW("0x16", "weighted sum overflow"),
        UNSATISFIABLE_THRESHOLD("0x17", "unsatisfiable threshold. Weighted sum of keys is less than the threshold"),
        ZERO_LENGTH("0x18", "length is zero"),
        TOO_LONG_LENGTH("0x19", "length too long"),
        NESTED_ROLE_BASE_KEY("0x1a", "nested role-based key");

        private String value;
        private String errorMessage;

        TxError(String value, String errorMessage) {
            this.value = value;
            this.errorMessage = errorMessage;
        }

        public static String getErrorMessage(String value) {
            for (KlayTransactionReceipt.TxError type : values()) {
                if (type.value.equals(value)) {
                    return type.errorMessage;
                }
            }
            return null;
        }
    }

    public static class TransactionReceipt {

        /**
         * Hash of the block where this transaction was in.
         */
        private String blockHash;

        /**
         * The block number where this transaction was in.
         */
        private String blockNumber;

        /**
         * The contract address created, if the transaction was a contract creation, otherwise null.
         */
        private String contractAddress;

        /**
         * (optional) Address of the fee payer.
         */
        private String feePayer;

        /**
         * (optional) An array of fee payer's signature objects. A signature object contains three fields
         * (V, R, and S). V contains ECDSA recovery id. R contains ECDSA signature r while S contains ECDSA
         * signatures.
         */
        private List<KlaySignatureData> feePayerSignatures;

        /**
         * (optional) Fee ratio of the fee payer. If it is 30, 30% of the fee will be paid by the fee payer.
         * 70% will be paid by the sender.
         */
        private String feeRatio;

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
         * The amount of gas used by this specific transaction alone.
         */
        private String gasUsed;

        /**
         * The data send along with the transaction.
         */
        private String input;

        /**
         * Array of log objects, which this transaction generated.
         */
        private List<KlayLogs.Log> logs;

        /**
         * Bloom filter for light clients to quickly retrieve related logs.
         */
        private String logsBloom;

        /**
         * The number of transactions made by the sender prior to this one.
         */
        private String nonce;

        /**
         * Hash of the tx without the fee payer's address and signature. This value is always the same as the value
         * of transactionHash for non fee-delegated transactions.
         */
        private String senderTxHash;

        /**
         * An array of signature objects. A signature object contains three fields (V, R, and S). V contains ECDSA
         * recovery id. R contains ECDSA signature r while S contains ECDSA signature s.
         */
        private List<KlaySignatureData> signatures;

        /**
         * Either 1 (success) or 0 (failure).
         */
        private String status;

        /**
         * Address of the receiver. null when its a contract creation transaction.
         */
        private String to;

        /**
         * Integer of the transactions index position in the block.
         */
        private String transactionIndex;

        /**
         * Hash of the transaction.
         */
        private String transactionHash;

        /**
         * transaction error code
         */
        private String txError;

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

        public TransactionReceipt() {
        }

        public TransactionReceipt(String blockHash, String blockNumber, String contractAddress, String feePayer,
                                  List<KlaySignatureData> feePayerSignatures, String feeRatio, String from, String gas,
                                  String gasPrice, String gasUsed, String input, List<KlayLogs.Log> logs, String logsBloom,
                                  String nonce, String senderTxHash, List<KlaySignatureData> signatures, String status,
                                  String to, String transactionIndex, String transactionHash, String txError, String type,
                                  String typeInt, String value) {
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.contractAddress = contractAddress;
            this.feePayer = feePayer;
            this.feePayerSignatures = feePayerSignatures;
            this.feeRatio = feeRatio;
            this.from = from;
            this.gas = gas;
            this.gasPrice = gasPrice;
            this.gasUsed = gasUsed;
            this.input = input;
            this.logs = logs;
            this.logsBloom = logsBloom;
            this.nonce = nonce;
            this.senderTxHash = senderTxHash;
            this.signatures = signatures;
            this.status = status;
            this.to = to;
            this.transactionIndex = transactionIndex;
            this.transactionHash = transactionHash;
            this.txError = txError;
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

        public String getContractAddress() {
            return contractAddress;
        }

        public String getFeePayer() {
            return feePayer;
        }

        @JsonDeserialize(using = KlayTransactionReceiptDeserializer.class)
        public void setFeePayerSignatures(List<KlaySignatureData> feePayerSignatures) {
            this.feePayerSignatures = feePayerSignatures;
        }

        public List<KlaySignatureData> getFeePayerSignatures() {
            return feePayerSignatures;
        }

        public String getFeeRatio() {
            return feeRatio;
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

        public String getGasUsed() {
            return gasUsed;
        }

        public String getInput() {
            return input;
        }

        public List<KlayLogs.Log> getLogs() {
            return logs;
        }

        public String getLogsBloom() {
            return logsBloom;
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

        @JsonDeserialize(using = KlayTransactionReceiptDeserializer.class)
        public void setSignatures(List<KlaySignatureData> signatures) {
            this.signatures = signatures;
        }

        public String getStatus() {
            return status;
        }

        public String getTo() {
            return to;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getTxError() {
            return txError;
        }

        public String getErrorMessage() {
            return TxError.getErrorMessage(txError);
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
            if (!(o instanceof TransactionReceipt)) {
                return false;
            }

            TransactionReceipt that = (TransactionReceipt) o;

            if (getBlockHash() != null
                    ? !getBlockHash().equals(that.getBlockHash())
                    : that.getBlockHash() != null) {
                return false;
            }
            if (getBlockNumber() != null
                    ? !getBlockNumber().equals(that.getBlockNumber()) : that.getBlockNumber() != null) {
                return false;
            }
            if (getContractAddress() != null
                    ? !getContractAddress().equals(that.getContractAddress())
                    : that.getContractAddress() != null) {
                return false;
            }
            if (getFeePayer() != null ? !getFeePayer().equals(that.getFeePayer()) : that.getFeePayer() != null) {
                return false;
            }
            if (getFeePayerSignatures() != null
                    ? !getFeePayerSignatures().equals(that.getFeePayerSignatures())
                    : that.getFeePayerSignatures() != null) {
                return false;
            }
            if (getFeeRatio() != null ? !getFeeRatio().equals(that.getFeeRatio()) : that.getFeeRatio() != null) {
                return false;
            }
            if (getFrom() != null ? !getFrom().equals(that.getFrom()) : that.getFrom() != null) {
                return false;
            }
            if (getGas() != null ? !getGas().equals(that.getGas()) : that.getGas() != null) {
                return false;
            }
            if (getGasPrice() != null ? !getGasPrice().equals(that.getGasPrice()) : that.getGasPrice() != null) {
                return false;
            }
            if (getGasUsed() != null ? !getGasUsed().equals(that.getGasUsed()) : that.getGasUsed() != null) {
                return false;
            }
            if (getInput() != null ? !getInput().equals(that.getInput()) : that.getInput() != null) {
                return false;
            }
            if (getLogs() != null ? !getLogs().equals(that.getLogs()) : that.getLogs() != null) {
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
            if (getStatus() != null
                    ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) {
                return false;
            }
            if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null) {
                return false;
            }
            if (getTransactionHash() != null
                    ? !getTransactionHash().equals(that.getTransactionHash())
                    : that.getTransactionHash() != null) {
                return false;
            }
            if (getTransactionIndex() != null
                    ? !getTransactionIndex().equals(that.getTransactionIndex())
                    : that.getTransactionIndex() != null) {
                return false;
            }
            if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
                return false;
            }
            if (getTypeInt() != null ? !getTypeInt().equals(that.getTypeInt()) : that.getTypeInt() != null) {
                return false;
            }
            if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) {
                return false;
            }
            return getLogsBloom() != null
                    ? getLogsBloom().equals(that.getLogsBloom()) : that.getLogsBloom() == null;
        }


        @Override
        public int hashCode() {
            int result = getTransactionHash() != null ? getTransactionHash().hashCode() : 0;
            result = 31 * result + (getBlockHash() != null ? getBlockHash().hashCode() : 0);
            result = 31 * result + (getBlockNumber() != null ? getBlockNumber().hashCode() : 0);
            result = 31 * result + (getContractAddress() != null ? getContractAddress().hashCode() : 0);
            result = 31 * result + (getFeePayer() != null ? getFeePayer().hashCode() : 0);
            result = 31 * result + (getFeePayerSignatures() != null ? getFeePayerSignatures().hashCode() : 0);
            result = 31 * result + (getFeeRatio() != null ? getFeeRatio().hashCode() : 0);
            result = 31 * result + (getFrom() != null ? getFrom().hashCode() : 0);
            result = 31 * result + (getGas() != null ? getGas().hashCode() : 0);
            result = 31 * result + (getGasPrice() != null ? getGasPrice().hashCode() : 0);
            result = 31 * result + (getGasUsed() != null ? getGasUsed().hashCode() : 0);
            result = 31 * result + (getInput() != null ? getInput().hashCode() : 0);
            result = 31 * result + (getLogs() != null ? getLogs().hashCode() : 0);
            result = 31 * result + (getLogsBloom() != null ? getLogsBloom().hashCode() : 0);
            result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
            result = 31 * result + (getSenderTxHash() != null ? getSenderTxHash().hashCode() : 0);
            result = 31 * result + (getSignatures() != null ? getSignatures().hashCode() : 0);
            result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
            result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
            result = 31 * result + (getTransactionIndex() != null ? getTransactionIndex().hashCode() : 0);
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            result = 31 * result + (getTypeInt() != null ? getTypeInt().hashCode() : 0);
            result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
            return result;
        }

    }

    public static class KlayTransactionReceiptDeserializer extends JsonDeserializer<List<KlaySignatureData>> {

        @Override
        public List<KlaySignatureData> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Iterator<JsonNode> iterator = node.iterator();
            List<KlaySignatureData> klaySignatureData = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode innerNode = iterator.next();
                byte[] v = Numeric.hexStringToByteArray(innerNode.get("V").asText());
                byte[] r = Numeric.hexStringToByteArray(innerNode.get("R").asText());
                byte[] s = Numeric.hexStringToByteArray(innerNode.get("S").asText());
                klaySignatureData.add(new KlaySignatureData(v, r, s));
            }
            return klaySignatureData;
        }
    }
}
