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
import com.klaytn.caver.transaction.AbstractFeeDelegatedWithRatioTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.crypto.Hash;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a fee delegated chain data anchoring with ratio transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation#txtypefeedelegatedchaindataanchoringwithratio to see more detail.
 */
public class FeeDelegatedChainDataAnchoringWithRatio extends AbstractFeeDelegatedWithRatioTransaction {
    /**
     * Data of the service chain.
     */
    String input;

    /**
     * FeeDelegatedChainDataAnchoringWithRatio Builder class
     */
    public static class Builder extends AbstractFeeDelegatedWithRatioTransaction.Builder<FeeDelegatedChainDataAnchoringWithRatio.Builder> {
        String input;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.toString());
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public FeeDelegatedChainDataAnchoringWithRatio build() {
            return new FeeDelegatedChainDataAnchoringWithRatio(this);
        }
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance.
     * @param builder FeeDelegatedChainDataAnchoringWithRatio.Builder instance.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public static FeeDelegatedChainDataAnchoringWithRatio create(FeeDelegatedChainDataAnchoringWithRatio.Builder builder) {
        return new FeeDelegatedChainDataAnchoringWithRatio(builder);
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer The address of the fee payer.
     * @param feePayerSignatures The fee payers's signatures.
     * @param feeRatio A fee ratio of the fee payer.
     * @param input The data of the service chain.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public static FeeDelegatedChainDataAnchoringWithRatio create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String input) {
        return new FeeDelegatedChainDataAnchoringWithRatio(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, input);
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance.
     * @param builder FeeDelegatedChainDataAnchoringWithRatio.Builder instance.
     */
    public FeeDelegatedChainDataAnchoringWithRatio(Builder builder) {
        super(builder);
        setInput(builder.input);
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer The address of the fee payer.
     * @param feePayerSignatures The fee payers's signatures.
     * @param feeRatio A fee ratio of the fee payer.
     * @param input The data of the service chain.
     */
    public FeeDelegatedChainDataAnchoringWithRatio(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String input) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures,
                feePayer,
                feePayerSignatures,
                feeRatio
        );
        setInput(input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public static FeeDelegatedChainDataAnchoringWithRatio decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public static FeeDelegatedChainDataAnchoringWithRatio decode(byte[] rlpEncoded) {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, from, input, feeRatio, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.toString());
        }

        //remove Tag
        byte[] detachedType = Arrays.copyOfRange(rlpEncoded, 1, rlpEncoded.length);

        RlpList rlpList = RlpDecoder.decode(detachedType);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

        BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
        BigInteger gas = ((RlpString) values.get(2)).asPositiveBigInteger();
        String from = ((RlpString) values.get(3)).asString();
        String input = ((RlpString) values.get(4)).asString();
        BigInteger feeRatio = ((RlpString) values.get(5)).asPositiveBigInteger();
        List<RlpType> senderSignatures = ((RlpList) (values.get(6))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(7)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(8))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedChainDataAnchoringWithRatio feeDelegatedChainDataAnchoringWithRatio = new FeeDelegatedChainDataAnchoringWithRatio.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setInput(input)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeeRatio(feeRatio)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedChainDataAnchoringWithRatio;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, from, input, feeRatio, txSignatures, feePayer, feePayerSignatures])
        this.validateOptionalValues(false);

        List<RlpType> senderSignatureRLPList = new ArrayList<>();
        List<RlpType> feePayerSignatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            senderSignatureRLPList.add(signatureData.toRlpList());
        }

        for(SignatureData signatureData : this.getFeePayerSignatures()) {
            feePayerSignatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigFeePayerRLP = encode([encode([type, nonce, gasPrice, gas, from, input, feeRatio]), feePayer, chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, from, input, feeRatio])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.getType();
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }

    /**
     * Returns a senderTxHash of transaction
     * @return String
     */
    @Override
    public String getSenderTxHash() {
        // SenderTxHashRLP = type + encode([nonce, gasPrice, gas, from, input, feeRatio, txSignatures])
        this.validateOptionalValues(false);

        List<RlpType> senderSignatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            senderSignatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(Hash.sha3(rawTx));
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param txObj The AbstractFeeDelegatedWithRatioTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractFeeDelegatedWithRatioTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!(txObj instanceof FeeDelegatedChainDataAnchoringWithRatio)) return false;
        FeeDelegatedChainDataAnchoringWithRatio feeDelegatedChainDataAnchoringWithRatio = (FeeDelegatedChainDataAnchoringWithRatio)txObj;

        if(!this.getInput().equals(feeDelegatedChainDataAnchoringWithRatio.getInput())) return false;

        return true;
    }

    /**
     * Getter function for input
     * @return String
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter function for input
     * @param input Data of the service chain.
     */
    public void setInput(String input) {
        if(input == null) {
            throw new IllegalArgumentException("input is missing.");
        }

        if(!Utils.isHex(input)) {
            throw new IllegalArgumentException("Invalid input : " + input);
        }

        this.input = Numeric.prependHexPrefix(input);
    }
}
