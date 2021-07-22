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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Transaction extends Response<Transaction.TransactionData> {

    public static class TransactionData {
        /**
         * Hash of the block where this transaction was in. null when its pending.
         */
        private String blockHash;

        /**
         * Block number where this transaction was in. null when its pending.
         */
        private String blockNumber;

        /**
         * The code format of smart contract code.
         */
        private String codeFormat;

        /**
         * Address of the fee payer.
         */
        private String feePayer;

        /**
         *  An array of fee payer's signature objects. A signature object contains three fields (V, R, and S).
         *  V contains ECDSA recovery id. R contains ECDSA signature r while S contains ECDSA signature s.
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
         * Hash of the transaction.
         */
        private String hash;

        /**
         * true if the address is humanReadable, false if the address is not humanReadable.
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
        private List<SignatureData> signatures;

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

        public TransactionData() {}

        public TransactionData(String blockHash, String blockNumber, String codeFormat, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String from, String gas, String gasPrice, String hash, boolean humanReadable, String key, String input, String nonce, String senderTxHash, List<SignatureData> signatures, String to, String transactionIndex, String type, String typeInt, String value) {
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.codeFormat = codeFormat;
            this.feePayer = feePayer;
            this.feePayerSignatures = feePayerSignatures;
            this.feeRatio = feeRatio;
            this.from = from;
            this.gas = gas;
            this.gasPrice = gasPrice;
            this.hash = hash;
            this.humanReadable = humanReadable;
            this.key = key;
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
            return feeRatio;
        }

        public void setFeeRatio(String feeRatio) {
            this.feeRatio = feeRatio;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getSenderTxHash() {
            return senderTxHash;
        }

        public void setSenderTxHash(String senderTxHash) {
            this.senderTxHash = senderTxHash;
        }

        public List<SignatureData> getSignatures() {
            return signatures;
        }

        @JsonDeserialize(using = SignatureDataListDeserializer.class)
        public void setSignatures(List<SignatureData> signatures) {
            this.signatures = signatures;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeInt() {
            return typeInt;
        }

        public void setTypeInt(String typeInt) {
            this.typeInt = typeInt;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isHumanReadable() {
            return humanReadable;
        }

        public void setHumanReadable(boolean humanReadable) {
            this.humanReadable = humanReadable;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Convert TransactionData to Caver's transaction instance.
         * @param klay The Klay instance to fill gasPrice, chainId and nonce fields when signing a transaction.
         * @return AbstractTransaction.
         */
        public AbstractTransaction convertToCaverTransaction(Klay klay) {
            switch (TransactionType.valueOf(this.getType())) {
                case TxTypeLegacyTransaction:
                    return LegacyTransaction.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getTo(), this.getInput(), this.getValue());
                case TxTypeValueTransfer:
                    return ValueTransfer.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getTo(), this.getValue());
                case TxTypeFeeDelegatedValueTransfer:
                    return FeeDelegatedValueTransfer.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getTo(), this.getValue());
                case TxTypeFeeDelegatedValueTransferWithRatio:
                    return FeeDelegatedValueTransferWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), this.getTo(), this.getValue());
                case TxTypeValueTransferMemo:
                    return ValueTransferMemo.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeFeeDelegatedValueTransferMemo:
                    return FeeDelegatedValueTransferMemo.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeFeeDelegatedValueTransferMemoWithRatio:
                    return FeeDelegatedValueTransferMemoWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeAccountUpdate:
                    return AccountUpdate.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), Account.createFromRLPEncoding(this.getFrom(), this.getKey()));
                case TxTypeFeeDelegatedAccountUpdate:
                    return FeeDelegatedAccountUpdate.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), Account.createFromRLPEncoding(this.getFrom(), this.getKey()));
                case TxTypeFeeDelegatedAccountUpdateWithRatio:
                    return FeeDelegatedAccountUpdateWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), Account.createFromRLPEncoding(this.getFrom(), this.getKey()));
                case TxTypeSmartContractDeploy:
                    return SmartContractDeploy.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getTo(), this.getValue(), this.getInput(), false, Numeric.toHexStringWithPrefix(CodeFormat.EVM));
                case TxTypeFeeDelegatedSmartContractDeploy:
                    return FeeDelegatedSmartContractDeploy.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getTo(), this.getValue(), this.getInput(), false, Numeric.toHexStringWithPrefix(CodeFormat.EVM));
                case TxTypeFeeDelegatedSmartContractDeployWithRatio:
                    return FeeDelegatedSmartContractDeployWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), this.getTo(), this.getValue(), this.getInput(), false, Numeric.toHexStringWithPrefix(CodeFormat.EVM));
                case TxTypeSmartContractExecution:
                    return SmartContractExecution.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeFeeDelegatedSmartContractExecution:
                    return FeeDelegatedSmartContractExecution.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeFeeDelegatedSmartContractExecutionWithRatio:
                    return FeeDelegatedSmartContractExecutionWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), this.getTo(), this.getValue(), this.getInput());
                case TxTypeCancel:
                    return Cancel.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures());
                case TxTypeFeeDelegatedCancel:
                    return FeeDelegatedCancel.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures());
                case TxTypeFeeDelegatedCancelWithRatio:
                    return FeeDelegatedCancelWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio());
                case TxTypeChainDataAnchoring:
                    return ChainDataAnchoring.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getInput());
                case TxTypeFeeDelegatedChainDataAnchoring:
                    return FeeDelegatedChainDataAnchoring.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getInput());
                case TxTypeFeeDelegatedChainDataAnchoringWithRatio:
                    return FeeDelegatedChainDataAnchoringWithRatio.create(klay, this.getFrom(), this.getNonce(), this.getGas(), this.getGasPrice(), "", this.getSignatures(), this.getFeePayer(), this.getFeePayerSignatures(), this.getFeeRatio(), this.getInput());
                default:
                    throw new RuntimeException("Invalid transaction type : Cannot create a transaction instance that has Tx type :" + this.getType());
            }
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
