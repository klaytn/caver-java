/*
 * Copyright 2020 The caver-java Authors
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
 */

package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionReceipt extends Response<TransactionReceipt.TransactionReceiptData> {

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
            for (TransactionReceipt.TxError type : values()) {
                if (type.value.equals(value)) {
                    return type.errorMessage;
                }
            }
            return null;
        }
    }

    public static class TransactionReceiptData {
        /**
         * Hash of the block where this transaction was in.
         */
        private String blockHash;

        /**
         * The block number where this transaction was in.
         */
        private String blockNumber;

        /**
         * The code format of smart contract code.
         */
        private String codeFormat;

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
        private List<SignatureData> feePayerSignatures;

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
         * Gas price used when the transaction was processed.
         */
        private String effectiveGasPrice;

        /**
         * Max priority fee per gas in peb.
         */
        private String maxPriorityFeePerGas;

        /**
         * Max fee per gas in peb.
         */
        private String maxFeePerGas;

        /**
         * The amount of gas used by this specific transaction alone.
         */
        private String gasUsed;

        /**
         * true if the humanReadable, false if the address is not humanReadable.
         */
        private boolean humanReadable;

        /**
         * Key of the newly created account.
         */
        private String key;

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
        private List<SignatureData> signatures;

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

        /**
         * Chain ID.
         */
        @JsonAlias({"chainId", "chainID"})
        private String chainID;

        /**
         * An access list.
         */
        private AccessList accessList;

        public TransactionReceiptData() {
        }

        public TransactionReceiptData(String blockHash, String blockNumber, String codeFormat, String contractAddress, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String from, String gas, String gasPrice, String effectiveGasPrice, String maxPriorityFeePerGas, String maxFeePerGas, String gasUsed, boolean humanReadable, String key, String input, List<KlayLogs.Log> logs, String logsBloom, String nonce, String senderTxHash, List<SignatureData> signatures, String status, String to, String transactionIndex, String transactionHash, String txError, String type, String typeInt, String value, String chainID, AccessList accessList) {
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.codeFormat = codeFormat;
            this.contractAddress = contractAddress;
            this.feePayer = feePayer;
            this.feePayerSignatures = feePayerSignatures;
            this.feeRatio = feeRatio;
            this.from = from;
            this.gas = gas;
            this.gasPrice = gasPrice;
            this.effectiveGasPrice = effectiveGasPrice;
            this.maxPriorityFeePerGas = maxPriorityFeePerGas;
            this.maxFeePerGas = maxFeePerGas;
            this.gasUsed = gasUsed;
            this.humanReadable = humanReadable;
            this.key = key;
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
            this.chainID = chainID;
            this.accessList = accessList;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getCodeFormat() {
            return codeFormat;
        }

        public void setCodeFormat(String codeFormat) {
            this.codeFormat = codeFormat;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getFeePayer() {
            return feePayer;
        }

        public void setFeePayer(String feePayer) {
            this.feePayer = feePayer;
        }

        public List<SignatureData> getFeePayerSignatures() {
            return feePayerSignatures;
        }

        @JsonDeserialize(using = SignatureDataListDeserializer.class)
        public void setFeePayerSignatures(List<SignatureData> feePayerSignatures) {
            this.feePayerSignatures = feePayerSignatures;
        }

        public String getFeeRatio() {
            return this.feeRatio;
        }

        public void setFeeRatio(String feeRatio) {
            this.feeRatio = feeRatio;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getGas() {
            return this.gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return this.gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getEffectiveGasPrice() {
            return this.effectiveGasPrice;
        }

        public void setEffectiveGasPrice(String effectiveGasPrice) {
            this.effectiveGasPrice = effectiveGasPrice;
        }

        public String getMaxPriorityFeePerGas() {
            return this.maxPriorityFeePerGas;
        }

        public void setMaxPriorityFeePerGas(String maxPriorityFeePerGas) {
            this.maxPriorityFeePerGas = maxPriorityFeePerGas;
        }

        public String getMaxFeePerGas() {
            return this.maxFeePerGas;
        }

        public void setMaxFeePerGas(String maxFeePerGas) {
            this.maxFeePerGas = maxFeePerGas;
        }

        public String getGasUsed() {
            return this.gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public boolean isHumanReadable() {
            return this.humanReadable;
        }

        public void setHumanReadable(boolean humanReadable) {
            this.humanReadable = humanReadable;
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getInput() {
            return this.input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public List<KlayLogs.Log> getLogs() {
            return this.logs;
        }

        public void setLogs(List<KlayLogs.Log> logs) {
            this.logs = logs;
        }

        public String getLogsBloom() {
            return this.logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getNonce() {
            return this.nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getSenderTxHash() {
            return this.senderTxHash;
        }

        public void setSenderTxHash(String senderTxHash) {
            this.senderTxHash = senderTxHash;
        }

        public List<SignatureData> getSignatures() {
            return this.signatures;
        }

        @JsonDeserialize(using = SignatureDataListDeserializer.class)
        public void setSignatures(List<SignatureData> signatures) {
            this.signatures = signatures;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTo() {
            return this.to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTransactionIndex() {
            return this.transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getTransactionHash() {
            return this.transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getTxError() {
            return this.txError;
        }

        public void setTxError(String txError) {
            this.txError = txError;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeInt() {
            return this.typeInt;
        }

        public void setTypeInt(String typeInt) {
            this.typeInt = typeInt;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getChainID() {
            return this.chainID;
        }

        public void setChainID(String chainID) {
            this.chainID = chainID;
        }

        public AccessList getAccessList() {
            return this.accessList;
        }

        public void setAccessList(AccessList accessList) {
            this.accessList = accessList;
        }
    }

    public static class SignatureDataListDeserializer extends JsonDeserializer<List<SignatureData>> {

        @Override
        public List<SignatureData> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Iterator<JsonNode> iterator = node.iterator();
            List<SignatureData> signatureDataList = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonNode innerNode = iterator.next();
                byte[] v = Numeric.hexStringToByteArray(innerNode.get("V").asText());
                byte[] r = Numeric.hexStringToByteArray(innerNode.get("R").asText());
                byte[] s = Numeric.hexStringToByteArray(innerNode.get("S").asText());
                signatureDataList.add(new SignatureData(v, r, s));
            }
            return signatureDataList;
        }
    }
}
