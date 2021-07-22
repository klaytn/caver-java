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

package com.klaytn.caver.transaction.type;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LegacyTransaction extends AbstractTransaction {
    /**
     * The account address that will receive the transferred value.
     */
    String to = "0x";

    /**
     * Data attached to the transaction, used for transaction execution.
     */
    String input = "0x";

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value;

    /**
     * LegacyTransaction Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<LegacyTransaction.Builder> {
        private String to = "0x";
        private String value;
        private String input = "0x";

        public Builder() {
            super(TransactionType.TxTypeLegacyTransaction.toString());
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setValue(BigInteger value) {
            setValue(Numeric.toHexStringWithPrefix(value));
            return this;
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public LegacyTransaction build() {
            return new LegacyTransaction(this);
        }
    }

    /**
     * Creates a LegacyTransaction instance.
     * @param builder LegacyTransaction.Builder instance.
     * @return LegacyTransaction
     */
    public static LegacyTransaction create(LegacyTransaction.Builder builder) {
        return new LegacyTransaction(builder);
    }

    /**
     * Create a LegacyTransaction instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param input Data attached to the transaction, used for transaction execution.
     * @param value The amount of KLAY in peb to be transferred.
     * @return LegacyTransaction
     */
    public static LegacyTransaction create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String input, String value) {
        return new LegacyTransaction(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, to, input, value);
    }

    /**
     * Creates a LegacyTransaction instance.
     * @param builder LegacyTransaction.Builder instance.
     */
    public LegacyTransaction(LegacyTransaction.Builder builder) {
        super(builder);

        setTo(builder.to);
        setValue(builder.value);
        setInput(builder.input);
    }

    /**
     * Create a LegacyTransaction instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param input Data attached to the transaction, used for transaction execution.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public LegacyTransaction(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String input, String value) {
        super(
                klaytnCall,
                TransactionType.TxTypeLegacyTransaction.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures
        );
        setTo(to);
        setValue(value);
        setInput(input);
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction string.
     * @param rlpEncoded RLP-encoded LegacyTransaction string
     * @return LegacyTransaction
     */
    public static LegacyTransaction decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction byte array.
     * @param rlpEncoded RLP-encoded LegacyTransaction byte array.
     * @return LegacyTransaction
     */
    public static LegacyTransaction decode(byte[] rlpEncoded) {
        // TxHashRLP = encode([nonce, gasPrice, gas, to, value, input, v, r, s])
        try {
            RlpList rlpList = RlpDecoder.decode(rlpEncoded);
            List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

            String nonce = ((RlpString) values.get(0)).asString();
            String gasPrice = ((RlpString) values.get(1)).asString();
            String gas = ((RlpString) values.get(2)).asString();
            String to = ((RlpString) values.get(3)).asString();
            String value = ((RlpString) values.get(4)).asString();
            String input = ((RlpString) values.get(5)).asString();

            LegacyTransaction legacyTransaction = new LegacyTransaction.Builder()
                    .setInput(input)
                    .setValue(value)
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setTo(to)
                    .build();

            byte[] v = ((RlpString) values.get(6)).getBytes();
            byte[] r = ((RlpString) values.get(7)).getBytes();
            byte[] s = ((RlpString) values.get(8)).getBytes();
            SignatureData signatureData = new SignatureData(v, r, s);

            legacyTransaction.appendSignatures(signatureData);
            return legacyTransaction;
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Appends signatures array to transaction.
     * Legacy transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData SignatureData instance contains ECDSA signature data
     */
    @Override
    public void appendSignatures(SignatureData signatureData) {
        if(this.getSignatures().size() != 0 && !Utils.isEmptySig(this.getSignatures().get(0))) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        super.appendSignatures(signatureData);
    }

    /**
     * Appends signatures array to transaction.
     * Legacy transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData List of SignatureData contains ECDSA signature data
     */
    @Override
    public void appendSignatures(List<SignatureData> signatureData) {
        if(this.getSignatures().size() != 0 && !Utils.isEmptySig(this.getSignatures())) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        if(signatureData.size() != 1) {
            throw new RuntimeException("Signatures are too long " + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        super.appendSignatures(signatureData);
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        this.validateOptionalValues(false);
        //TxHashRLP = encode([nonce, gasPrice, gas, to, value, input, v, r, s])
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        SignatureData signatureData = this.getSignatures().get(0);
        rlpTypeList.addAll(signatureData.toRlpList().getValues());

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }


    @Override
    public String getCommonRLPEncodingForSignature() {
        return getRLPEncodingForSignature();
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getRLPEncodingForSignature() {
        this.validateOptionalValues(true);

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getChainId())));
        rlpTypeList.add(RlpString.create(0));
        rlpTypeList.add(RlpString.create(0));

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param obj The AbstractTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractTransaction obj, boolean checkSig) {
        if(!super.compareTxField(obj, checkSig)) return false;
        if(!(obj instanceof LegacyTransaction)) return false;
        LegacyTransaction txObj = (LegacyTransaction)obj;

        if(!this.getTo().toLowerCase().equals(txObj.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(txObj.getValue()))) return false;
        if(!this.getInput().equals(txObj.getInput())) return false;

        return true;
    }

    /**
     * Getter function for to
     * @return String
     */
    public String getTo() {
        return to;
    }

    /**
     * Getter function for input
     * @return String
     */
    public String getInput() {
        return input;
    }

    /**
     * Getter function for value
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter function for to
     * @param to The account address that will receive the transferred value.
     */
    public void setTo(String to) {
        // "to" field in LegacyTransaction allows null
        if(to == null || to.isEmpty()) {
            to = "0x";
        }

        if(!to.equals("0x") && !Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address. : " + to);
        }
        this.to = to;
    }

    /**
     * Setter function for input
     * @param input Data attached to the transaction.
     */
    public void setInput(String input) {
        if(input == null) {
            throw new IllegalArgumentException("input is missing");
        }

        if(!Utils.isHex(input)) {
            throw new IllegalArgumentException("Invalid input : " + input);
        }
        this.input = input;
    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is missing");
        }

        if(!Utils.isNumber(value)) {
            throw new IllegalArgumentException("Invalid value : " + value);
        }
        this.value = value;
    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        setValue(Numeric.toHexStringWithPrefix(value));
    }
}
