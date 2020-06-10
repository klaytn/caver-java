package com.klaytn.caver.transaction.type;

import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FeeDelegatedValueTransfer extends AbstractFeeDelegatedTransaction {

    String to;
    String value;

    public static class Builder extends AbstractFeeDelegatedTransaction.Builder<FeeDelegatedValueTransfer.Builder> {
        String to;
        String value;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedValueTransfer.toString());
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
            setValue(Numeric.toHexStringWithPrefix(value));
            return this;
        }

        public FeeDelegatedValueTransfer build() {
            return new FeeDelegatedValueTransfer(this);
        }
    }

    public FeeDelegatedValueTransfer(Builder builder) {
        super(builder);
        setTo(builder.to);
        setValue(builder.value);
    }

    public static FeeDelegatedValueTransfer decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    public static FeeDelegatedValueTransfer decode(byte[] rlpEncoded) {
        // type + encode([nonce, gasPrice, gas, to, value, from, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedValueTransfer.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedValueTransfer.toString());
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

        List<RlpType> senderSignatures = ((RlpList) (values.get(6))).getValues();
        List<KlaySignatureData> senderSignList = KlaySignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(7)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(8))).getValues();
        List<KlaySignatureData> feePayerSignList = KlaySignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setSignList(senderSignList)
                .setFeePayerSignList(feePayerSignList)
                .build();


        return feeDelegatedValueTransfer;
    }

    @Override
    public String getRLPEncoding() {
        return null;
    }

    @Override
    public String getCommonRLPEncodingForSignature() {
        return null;
    }

    /**
     * Setter function for to
     * @param to The account address that will receive the transferred value.
     */
    public void setTo(String to) {
        if(to == null) {
            throw new IllegalArgumentException("to is missing.");
        }

        if(!Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address. : " + to);
        }

        this.to = to;
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
            throw new IllegalArgumentException("Invalid value.");
        }
        this.value = value;
    }
}
