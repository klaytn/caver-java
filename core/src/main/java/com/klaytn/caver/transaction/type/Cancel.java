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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a cancel transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypecancel to see more detail.
 */
@JsonIgnoreProperties(value = { "chainId" })
public class Cancel extends AbstractTransaction {
    /**
     * A unit price of gas in peb the sender will pay for a transaction fee.
     */
    String gasPrice = "0x";

    /**
     * Cancel Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<Cancel.Builder> {
        String gasPrice = "0x";

        public Builder() {
            super(TransactionType.TxTypeCancel.toString());
        }

        public Builder setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
            return this;
        }

        public Builder setGasPrice(BigInteger gasPrice) {
            setGasPrice(Numeric.toHexStringWithPrefix(gasPrice));
            return this;
        }

        public Cancel build() {
            return new Cancel(this);
        }
    }

    /**
     * Create a Cancel instance.
     * @param builder Cancel.builder instance
     * @return Cancel
     */
    public static Cancel create(Cancel.Builder builder) {
        return new Cancel(builder);
    }

    /**
     * Creates a Cancel instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @return Cancel
     */
    public static Cancel create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures) {
        return new Cancel(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures);
    }

    /**
     * Creates a Cancel instance
     * @param builder Cancel.builder instance
     */
    public Cancel(Cancel.Builder builder) {
        super(builder);
        setGasPrice(builder.gasPrice);
    }

    /**
     * Creates a Cancel instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     */
    public Cancel(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures) {
        super(
                klaytnCall,
                TransactionType.TxTypeCancel.toString(),
                from,
                nonce,
                gas,
                chainId,
                signatures
        );
        setGasPrice(gasPrice);
    }

    /**
     * Getter function for gas price
     * @return String
     */
    public String getGasPrice() {
        return gasPrice;
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(String gasPrice) {
        if(gasPrice == null || gasPrice.isEmpty() || gasPrice.equals("0x")) {
            gasPrice = "0x";
        }

        if(!gasPrice.equals("0x") && !Utils.isNumber(gasPrice)) {
            throw new IllegalArgumentException("Invalid gasPrice. : " + gasPrice);
        }

        this.gasPrice = gasPrice;
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(BigInteger gasPrice) {
        setGasPrice(Numeric.toHexStringWithPrefix(gasPrice));
    }


    /**
     * Decodes a RLP-encoded Cancel string.
     * @param rlpEncoded RLP-encoded Cancel string
     * @return Cancel
     */
    public static Cancel decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded Cancel byte array.
     * @param rlpEncoded RLP-encoded Cancel byte array.
     * @return Cancel
     */
    public static Cancel decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, txSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeCancel.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeCancel.toString());
        }

        //remove Tag
        byte[] detachedType = Arrays.copyOfRange(rlpEncoded, 1, rlpEncoded.length);

        RlpList rlpList = RlpDecoder.decode(detachedType);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

        BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
        BigInteger gas = ((RlpString) values.get(2)).asPositiveBigInteger();
        String from = ((RlpString) values.get(3)).asString();

        List<RlpType> senderSignatures = ((RlpList) (values.get(4))).getValues();
        List<SignatureData> signatureDataList = SignatureData.decodeSignatures(senderSignatures);

        Cancel cancel = new Cancel.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setSignatures(signatureDataList)
                .build();

        return cancel;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        this.validateOptionalValues(false);

        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, txSignatures])
        List<RlpType> signatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            signatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeCancel.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigRLP = encode([encode([type, nonce, gasPrice, gas, from]), chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, from])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeCancel.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));

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
        if(!(obj instanceof Cancel)) return false;
        Cancel txObj = (Cancel) obj;
        if(Numeric.toBigInt(this.getGasPrice()).compareTo(Numeric.toBigInt(txObj.getGasPrice())) != 0) return false;

        return true;
    }

    /**
     * Combines signatures to the transaction from RLP-encoded transaction strings and returns a single transaction with all signatures combined.
     * When combining the signatures into a transaction instance,
     * an error is thrown if the decoded transaction contains different value except signatures.
     * @param rlpEncoded A List of RLP-encoded transaction strings.
     * @return String
     */
    @Override
    public String combineSignedRawTransactions(List<String> rlpEncoded) {
        boolean fillVariable = false;

        // If the signatures are empty, there may be an undefined member variable.
        // In this case, the empty information is filled with the decoded result.
        if(Utils.isEmptySig(this.getSignatures())) fillVariable = true;

        for(String encodedStr : rlpEncoded) {
            AbstractTransaction decode = TransactionDecoder.decode(encodedStr);
            if (!decode.getType().equals(this.getType())) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }
            Cancel txObj = (Cancel) decode;

            if(fillVariable) {
                if(this.getNonce().equals("0x")) this.setNonce(txObj.getNonce());
                if(this.getGasPrice().equals("0x")) this.setGasPrice(txObj.getGasPrice());
                fillVariable = false;
            }

            // Signatures can only be combined for the same transaction.
            // Therefore, compare whether the decoded transaction is the same as this.
            if(!this.compareTxField(txObj, false)) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }

            this.appendSignatures(txObj.getSignatures());
        }

        return this.getRLPEncoding();
    }

    /**
     * Fills empty optional transaction field.(gasPrice)
     * @throws IOException
     */
    @Override
    public void fillTransaction() throws IOException {
        super.fillTransaction();
        if(this.gasPrice.equals("0x")) {
            this.setGasPrice(this.getSuggestedGasPrice());
        }
        if(this.getGasPrice().equals("0x")) {
            throw new RuntimeException("Cannot fill transaction data. (gasPrice). `klaytnCall` must be set in Transaction instance to automatically fill the nonce, chainId or gasPrice. Please call the `setKlaytnCall` to set `klaytnCall` in the Transaction instance.");
        }
    }

    /**
     * Checks that member variables that can be defined by the user are defined.
     * If there is an undefined variable, an error occurs.
     */
    @Override
    public void validateOptionalValues(boolean checkChainID) {
        super.validateOptionalValues(checkChainID);
        if(this.getGasPrice() == null || this.getGasPrice().isEmpty() || this.getGasPrice().equals("0x")) {
            throw new RuntimeException("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");
        }
    }
}
