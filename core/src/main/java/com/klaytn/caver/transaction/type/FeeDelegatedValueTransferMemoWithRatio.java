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
 * Represents a fee delegated value transfer memo with ratio transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation#txtypefeedelegatedvaluetransfermemowithratio to see more detail.
 */
public class FeeDelegatedValueTransferMemoWithRatio extends AbstractFeeDelegatedWithRatioTransaction {

    /**
     * The account address that will receive the transferred value.
     */
    String to;

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value;

    /**
     * Data attached to the transaction. The message should be passed to this attribute.
     */
    String input;

    /**
     * FeeDelegatedValueTransferMemo Builder class
     */
    public static class Builder extends AbstractFeeDelegatedWithRatioTransaction.Builder<FeeDelegatedValueTransferMemoWithRatio.Builder> {
        String to;
        String value;
        String input;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedValueTransferMemo.toString());
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
            return setValue(Numeric.toHexStringWithPrefix(value));
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public FeeDelegatedValueTransferMemoWithRatio build() {
            return new FeeDelegatedValueTransferMemoWithRatio(this);
        }
    }

    /**
     * Creates a FeeDelegatedValueTransferMemoWithRatio instance.
     * @param builder FeeDelegatedValueTransferMemoWithRatio.Builder instance.
     */
    public FeeDelegatedValueTransferMemoWithRatio(Builder builder) {
        super(builder);
        setTo(builder.to);
        setValue(builder.value);
        setInput(builder.input);
    }

    /**
     * Creates a FeeDelegatedValueTransferMemoWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID.
     * @param signatures A sender signature list.
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio  A fee ratio of the fee payer.
     * @param to The account adderss that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The message data attached to the transaction.
     */
    public FeeDelegatedValueTransferMemoWithRatio(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String to, String value, String input) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedValueTransferMemo.toString(),
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
        setInput(input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemo string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo string.
     * @return FeeDelegatedValueTransferMemo
     */
    public static FeeDelegatedValueTransferMemoWithRatio decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @return FeeDelegatedValueTransferMemo
     */
    public static FeeDelegatedValueTransferMemoWithRatio decode(byte[] rlpEncoded) {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, feeRatio, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.toString());
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
        BigInteger feeRatio = ((RlpString) values.get(7)).asPositiveBigInteger();

        List<RlpType> senderSignatures = ((RlpList) (values.get(8))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(9)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(10))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatio.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setInput(input)
                .setSignatures(senderSignList)
                .setFeeRatio(feeRatio)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedValueTransferMemoWithRatio;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, feeRatio, txSignatures, feePayer, feePayerSignatures])
        this .validateOptionalValues(false);

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
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        //SigFeePayerRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from, input, feeRatio]), feePayer, chainid, 0, 0])
        //encode([type, nonce, gasPrice, gas, to, value, from, input, feeRatio])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
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
        //SenderTxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, feeRatio, txSignatures])
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
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.getType() };
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
        if(!(txObj instanceof FeeDelegatedValueTransferMemoWithRatio)) return false;
        FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = (FeeDelegatedValueTransferMemoWithRatio)txObj;

        if(!this.getTo().toLowerCase().equals(feeDelegatedValueTransferMemoWithRatio.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(feeDelegatedValueTransferMemoWithRatio.getValue()))) return false;
        if(!this.getInput().equals(feeDelegatedValueTransferMemoWithRatio.getInput())) return false;

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
     * Getter function for input
     * @return String
     */
    public String getInput() {
        return input;
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
     * @param input Data attached to the transaction. The message should be passed to this attribute.
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
