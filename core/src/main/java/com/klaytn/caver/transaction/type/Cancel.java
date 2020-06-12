package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a cancel transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypecancel to see more detail.
 */
public class Cancel extends AbstractTransaction {

    /**
     * Cancel Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<Cancel.Builder> {
        public Builder() {
            super(TransactionType.TxTypeCancel.toString());
        }

        public Cancel build() {
            return new Cancel(this);
        }
    }

    /**
     * Creates a Cancel instance
     * @param builder Cancel.builder instance
     */
    private Cancel(Cancel.Builder builder) {
        super(builder);
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
        super(klaytnCall, TransactionType.TxTypeCancel.toString(), from, nonce, gas, gasPrice, chainId, signatures);
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
                .setSignList(signatureDataList)
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

        return true;
    }
}
