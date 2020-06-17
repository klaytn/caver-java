package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
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
 * Represents a fee delegated chain data anchoring transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/fee-delegation#txtypefeedelegatedchaindataanchoring to see more detail.
 */
public class FeeDelegatedChainDataAnchoring extends AbstractFeeDelegatedTransaction {
    /**
     * Data of the service chain.
     */
    String input;

    /**
     * FeeDelegatedChainDataAnchoring Builder class
     */
    public static class Builder extends AbstractFeeDelegatedTransaction.Builder<FeeDelegatedChainDataAnchoring.Builder> {
        String input;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedChainDataAnchoring.toString());
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public FeeDelegatedChainDataAnchoring build() {
            return new FeeDelegatedChainDataAnchoring(this);
        }
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance.
     * @param builder FeeDelegatedChainDataAnchoring.Builder instance.
     */
    public FeeDelegatedChainDataAnchoring(Builder builder) {
        super(builder);
        setInput(builder.input);
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param input The data of the service chain.
     */
    public FeeDelegatedChainDataAnchoring(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String input) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedChainDataAnchoring.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures,
                feePayer,
                feePayerSignatures
        );
        setInput(input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring string.
     * @return FeeDelegatedChainDataAnchoring
     */
    public static FeeDelegatedChainDataAnchoring decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @return FeeDelegatedChainDataAnchoring
     */
    public static FeeDelegatedChainDataAnchoring decode(byte[] rlpEncoded) {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, from, anchoredData, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoring.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedChainDataAnchoring.toString());
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

        List<RlpType> senderSignatures = ((RlpList) (values.get(5))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(6)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(7))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedChainDataAnchoring feeDelegatedChainDataAnchoring = new FeeDelegatedChainDataAnchoring.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setInput(input)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedChainDataAnchoring;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        //TxHashRLP = type + encode([nonce, gasPrice, gas, from, anchoredData, txSignatures, feePayer, feePayerSignatures])
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
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoring.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        //encode([encode([type, nonce, gasPrice, gas, from, anchoredData]), feePayer, chainid, 0, 0])
        //encode([type, nonce, gasPrice, gas, from, anchoredData])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoring.getType();
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));

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
        //SenderTxHashRLP = type + encode([nonce, gasPrice, gas, from, anchoredData, txSignatures])
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
        rlpTypeList.add(new RlpList(senderSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedChainDataAnchoring.getType() };
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
        if(!(txObj instanceof FeeDelegatedChainDataAnchoring)) return false;
        FeeDelegatedChainDataAnchoring feeDelegatedChainDataAnchoring = (FeeDelegatedChainDataAnchoring)txObj;

        if(!this.getInput().equals(feeDelegatedChainDataAnchoring.getInput())) return false;

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
