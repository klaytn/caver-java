package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a value transfer transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypevaluetransfer to see more detail.
 */
public class ValueTransfer extends AbstractTransaction {
    /**
     * The account address that will receive the transferred value.
     */
    String to;

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value = "0x00";

    /**
     * ValueTransfer Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<ValueTransfer.Builder> {
        private String to;
        private String value = "0x00";

        public Builder() {
            super(TransactionType.TxTypeValueTransfer.toString());
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

        public ValueTransfer build() {
            return new ValueTransfer(this);
        }
    }

    /**
     * Creates a ValueTransfer instance.
     * @param builder ValueTransfer.Builder instance.
     */
    public ValueTransfer(Builder builder) {
        super(builder);
        setTo(builder.to);
        setValue(builder.value);
    }

    /**
     * Creates a ValueTransfer instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public ValueTransfer(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<KlaySignatureData> signatures, String to, String value) {
        super(
                klaytnCall,
                TransactionType.TxTypeValueTransfer.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures
        );
        setTo(to);
        setValue(value);
    }

    /**
     * Decodes a RLP-encoded ValueTransfer string.
     * @param rlpEncoded RLP-encoded ValueTransfer string
     * @return ValueTransfer
     */
    public static ValueTransfer decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded ValueTransfer byte array.
     * @param rlpEncoded RLP-encoded ValueTransfer byte array.
     * @return ValueTransfer
     */
    public static ValueTransfer decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, txSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeValueTransfer.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeValueTransfer.toString());
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
        List<KlaySignatureData> signatureDataList = KlaySignatureData.decodeSignatures(senderSignatures);

        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setSignList(signatureDataList)
                .build();

        return valueTransfer;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, txSignatures])
        this.validateOptionalValues(false);

        List<RlpType> signatureRLPList = new ArrayList<>();

        for(KlaySignatureData signatureData : this.getSignatures()) {
            signatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeValueTransfer.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        //SigRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from]), chainId, 0, 0]
        //encode([type, nonce, gasPrice, gas, to, value, from]
        byte type = (byte)TransactionType.TxTypeValueTransfer.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
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
        if(!(obj instanceof ValueTransfer)) return false;
        ValueTransfer txObj = (ValueTransfer)obj;

        if(!this.getTo().toLowerCase().equals(txObj.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(txObj.getValue()))) return false;

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
        if(to == null) {
            throw new IllegalArgumentException("to is missing.");
        }

        if(!Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address.");
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
