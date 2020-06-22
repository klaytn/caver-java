package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
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
 * Represents a fee delegated value transfer with ratio transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation#txtypefeedelegatedvaluetransferwithratio to see more detail.
 */
public class FeeDelegatedValueTransferWithRatio extends AbstractFeeDelegatedWithRatioTransaction {

    /**
     * The account address that will receive the transferred value.
     */
    String to;

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value;

    /**
     * FeeDelegatedValueTransferWithRatio Builder class
     */
    public static class Builder extends AbstractFeeDelegatedWithRatioTransaction.Builder<FeeDelegatedValueTransferWithRatio.Builder> {
        String to;
        String value;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.toString());
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

        public FeeDelegatedValueTransferWithRatio build() {
            return new FeeDelegatedValueTransferWithRatio(this);
        }
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance.
     * @param builder FeeDelegatedValueTransferWithRatio.Builder instance.
     */
    public FeeDelegatedValueTransferWithRatio(Builder builder) {
        super(builder);
        setTo(builder.to);
        setValue(builder.value);
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID.
     * @param signatures A sender signature list.
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public FeeDelegatedValueTransferWithRatio(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String to, String value) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.toString(),
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
        setTo(to);
        setValue(value);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @return FeeDelegatedValueTransferWithRatio
     */
    public static FeeDelegatedValueTransferWithRatio decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @return FeeDelegatedValueTransferWithRatio
     */
    public static FeeDelegatedValueTransferWithRatio decode(byte[] rlpEncoded) {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, feeRatio, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.toString());
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
        BigInteger feeRatio = ((RlpString) values.get(6)).asPositiveBigInteger();

        List<RlpType> senderSignatures = ((RlpList) (values.get(7))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(8)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(9))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatio.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setFeeRatio(feeRatio)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedValueTransferWithRatio;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, feeRatio, txSignatures, feePayer, feePayerSignatures])
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
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigFeePayerRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from, feeRatio]), feePayer, chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, to, value, from, feeRatio])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
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
        //SenderTxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, feeRatio, txSignatures])
        //SenderTxHash = keccak256(SenderTxHashRLP)
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
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.getType() };
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
    public boolean compareTxField(AbstractFeeDelegatedWithRatioTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!(txObj instanceof FeeDelegatedValueTransferWithRatio)) return false;
        FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = (FeeDelegatedValueTransferWithRatio)txObj;

        if(!this.getTo().toLowerCase().equals(feeDelegatedValueTransferWithRatio.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(feeDelegatedValueTransferWithRatio.getValue()))) return false;

        return true;
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

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        setValue(Numeric.toHexStringWithPrefix(value));
    }

    /**
     * Getter function for to
     * @return The account address that will receive the transferred value.
     */
    public String getTo() {
        return to;
    }

    /**
     * Getter function for value
     * @return The amount of KLAY in peb to be transferred.
     */
    public String getValue() {
        return value;
    }
}
