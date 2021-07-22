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
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.CodeFormat;
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
 * Represents a fee delegated smart contract deploy transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/fee-delegation#txtypefeedelegatedsmartcontractdeploy to see more detail.
 */
public class FeeDelegatedSmartContractDeploy extends AbstractFeeDelegatedTransaction {

    /**
     * The account address that will receive the transferred value.
     */
    String to = "0x";

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value = "0x00";

    /**
     * Data attached to the transaction, used for transaction execution.
     */
    String input;

    /**
     * This must be false since human-readable address is not supported yet.
     * If true, the transaction will be rejected.
     */
    boolean humanReadable = false;

    /**
     * The code format of smart contract code.
     * The supported value for now is EVM(0x00) only.
     */
    String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);

    /**
     * FeeDelegatedSmartContractDeploy Builder class
     */
    public static class Builder extends AbstractFeeDelegatedTransaction.Builder<FeeDelegatedSmartContractDeploy.Builder> {
        String to = "0x";
        String value = "0x00";
        String input;
        boolean humanReadable = false;
        String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString());
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setValue(BigInteger value) {
            this.value = Numeric.toHexStringWithPrefix(value);
            return this;
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public Builder setHumanReadable(boolean humanReadable) {
            this.humanReadable = humanReadable;
            return this;
        }

        public Builder setCodeFormat(String codeFormat) {
            this.codeFormat = codeFormat;
            return this;
        }

        public Builder setCodeFormat(BigInteger codeFormat) {
            this.codeFormat = Numeric.toHexStringWithPrefix(codeFormat);
            return this;
        }

        public FeeDelegatedSmartContractDeploy build() {
            return new FeeDelegatedSmartContractDeploy(this);
        }
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance.
     * @param builder FeeDelegatedSmartContractDeploy.Builder instance.
     * @return FeeDelegatedSmartContractDeploy
     */
    public static FeeDelegatedSmartContractDeploy create(FeeDelegatedSmartContractDeploy.Builder builder) {
        return new FeeDelegatedSmartContractDeploy(builder);
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     * @return FeeDelegatedSmartContractDeploy
     */
    public static FeeDelegatedSmartContractDeploy create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value, String input, boolean humanReadable, String codeFormat) {
        return new FeeDelegatedSmartContractDeploy(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, to, value, input, humanReadable, codeFormat);
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance.
     * @param builder FeeDelegatedSmartContractDeploy.Builder instance.
     */
    public FeeDelegatedSmartContractDeploy(FeeDelegatedSmartContractDeploy.Builder builder) {
        super(builder);

        setTo(builder.to);
        setValue(builder.value);
        setInput(builder.input);
        setHumanReadable(builder.humanReadable);
        setCodeFormat(builder.codeFormat);
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     */
    public FeeDelegatedSmartContractDeploy(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value, String input, boolean humanReadable, String codeFormat) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures,
                feePayer,
                feePayerSignatures
        );
        setTo(to);
        setValue(value);
        setInput(input);
        setHumanReadable(humanReadable);
        setCodeFormat(codeFormat);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeploy string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy string.
     * @return FeeDelegatedSmartContractDeploy
     */
    public static FeeDelegatedSmartContractDeploy decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @return FeeDelegatedSmartContractDeploy
     */
    public static FeeDelegatedSmartContractDeploy decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedSmartContractDeploy.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString());
        }

        //remove Tag
        byte[] detachedType = Arrays.copyOfRange(rlpEncoded, 1, rlpEncoded.length);

        RlpList rlpList = RlpDecoder.decode(detachedType);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

        BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
        BigInteger gas = ((RlpString) values.get(2)).asPositiveBigInteger();
        String to = ((RlpString) values.get(3)).asString();
        BigInteger value = ((RlpString) values.get(4)).asPositiveBigInteger();
        String from = ((RlpString) values.get(5)).asString();
        String input = ((RlpString) values.get(6)).asString();
        boolean humanReadable = ((RlpString) values.get(7)).asPositiveBigInteger().compareTo(BigInteger.ZERO) != 0;
        BigInteger codeFormat = ((RlpString) values.get(8)).asPositiveBigInteger();

        List<RlpType> senderSignatures = ((RlpList) (values.get(9))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(10)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(11))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedSmartContractDeploy feeDelegatedSmartContractDeploy = new FeeDelegatedSmartContractDeploy.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setInput(input)
                .setHumanReadable(humanReadable)
                .setCodeFormat(codeFormat)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedSmartContractDeploy;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat, txSignatures, feePayer, feePayerSignatures])
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
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(this.getHumanReadable()? 1 : 0));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getCodeFormat())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedSmartContractDeploy.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat]), feePayer, chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedSmartContractDeploy.getType();
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(this.getHumanReadable()? 1 : 0));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getCodeFormat())));

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
        // SenderTxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input,humanReadable, codeFormat, txSignatures])
        this.validateOptionalValues(false);

        List<RlpType> senderSignatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            senderSignatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(this.getHumanReadable()? 1 : 0));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getCodeFormat())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedSmartContractDeploy.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(Hash.sha3(rawTx));
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param txObj The AbstractFeeDelegatedTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractFeeDelegatedTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!(txObj instanceof FeeDelegatedSmartContractDeploy)) return false;
        FeeDelegatedSmartContractDeploy feeDelegatedSmartContractDeploy = (FeeDelegatedSmartContractDeploy)txObj;

        if(!this.getTo().toLowerCase().equals(feeDelegatedSmartContractDeploy.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(feeDelegatedSmartContractDeploy.getValue()))) return false;
        if(!this.getInput().equals(feeDelegatedSmartContractDeploy.getInput())) return false;
        if(this.getHumanReadable() != feeDelegatedSmartContractDeploy.getHumanReadable()) return false;
        if(!this.getCodeFormat().equals(feeDelegatedSmartContractDeploy.getCodeFormat())) return false;

        return true;
    }

    /**
     * Getter function for to.
     * @return String
     */
    public String getTo() {
        return to;
    }

    /**
     * Getter function for value.
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter function for input.
     * @return String
     */
    public String getInput() {
        return input;
    }

    /**
     * Getter function for humanReadable
     * @return boolean
     */
    public boolean getHumanReadable() {
        return humanReadable;
    }

    /**
     * Getter function for code format
     * @return String
     */
    public String getCodeFormat() {
        return codeFormat;
    }

    /**
     * Setter function for to
     * @param to The account address that will receive the transferred value.
     */
    public void setTo(String to) {
        if(to == null || to.isEmpty()) {
            to = "0x";
        }

        if(!to.equals("0x")) {
            throw new IllegalArgumentException("'to' field must be nil('0x') : " + to);
        }

        this.to = "0x"; // currently "to" field must be nil
    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is missing.");
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

    /**
     * Setter function for input
     * @param input Data attached to the transaction, used for transaction execution.
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

    /**
     * Setter function for humanReadable
     * @param humanReadable boolean
     */
    public void setHumanReadable(boolean humanReadable) {
        if(humanReadable) {
            throw new IllegalArgumentException("HumanReadable attribute must set false");
        }
        this.humanReadable = false;
    }

    /**
     * Setter function for codeFormat
     * @param codeFormat The code format of smart contract code.
     */
    public void setCodeFormat(String codeFormat) {
        if(codeFormat == null) {
            throw new IllegalArgumentException("codeFormat is missing");
        }

        if(Numeric.toBigInt(codeFormat).compareTo(CodeFormat.EVM) != 0 ) {
            throw new IllegalArgumentException("CodeFormat attribute only support EVM(0)");
        }
        this.codeFormat = codeFormat;
    }


}
