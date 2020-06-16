package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a chain data anchoring transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypechaindataanchoring to see more detail.
 */
public class ChainDataAnchoring extends AbstractTransaction {
    /**
     * Data of the service chain.
     */
    String input;

    /**
     * ChainDataAnchoring Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<ChainDataAnchoring.Builder> {
        String input;

        public Builder() {
            super(TransactionType.TxTypeChainDataAnchoring.toString());
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public ChainDataAnchoring build() {
            return new ChainDataAnchoring(this);
        }
    }

    /**
     * Creates a ChainDataAnchoring instance.
     * @param builder ChainDataAnchoring.Builder instance.
     */
    public ChainDataAnchoring(ChainDataAnchoring.Builder builder) {
        super(builder);
        setInput(builder.input);
    }

    /**
     * Creates a ChainDataAnchoring instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param input Data of the service chain.
     */
    public ChainDataAnchoring(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String input) {
        super(klaytnCall, TransactionType.TxTypeChainDataAnchoring.toString(), from, nonce, gas, gasPrice, chainId, signatures);
        setInput(input);
    }

    /**
     * Decodes a RLP-encoded ChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring string
     * @return ChainDataAnchoring
     */
    public static ChainDataAnchoring decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded ChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring string
     * @return ChainDataAnchoring
     */
    public static ChainDataAnchoring decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, anchoredData, txSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeChainDataAnchoring.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeChainDataAnchoring.toString());
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
        List<SignatureData> signatureDataList = SignatureData.decodeSignatures(senderSignatures);

        ChainDataAnchoring chainDataAnchoring = new ChainDataAnchoring.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setInput(input)
                .setSignatures(signatureDataList)
                .build();

        return chainDataAnchoring;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, anchoredData, txSignatures])
        this.validateOptionalValues(false);

        List<RlpType> signatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            signatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));

        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeChainDataAnchoring.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigRLP = encode([encode([type, nonce, gasPrice, gas, from, anchoredData]), chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, from, anchoredData])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeChainDataAnchoring.getType();

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
     * Check equals txObj passed parameter and Current instance.
     * @param obj The AbstractTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractTransaction obj, boolean checkSig) {
        if(!super.compareTxField(obj, checkSig)) return false;
        if(!(obj instanceof ChainDataAnchoring)) return false;
        ChainDataAnchoring txObj = (ChainDataAnchoring)obj;

        if(!this.getInput().equals(txObj.getInput())) return false;

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
