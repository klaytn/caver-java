package com.klaytn.caver.transaction;

import com.klaytn.caver.Klay;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LegacyTransaction extends AbstractTransaction {
    String to;
    String input = "0x";
    String value;


    public LegacyTransaction(String nonce, String to, String value, String gas, String gasPrice, String input, String chainId) {
        super(
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                nonce,
                gas,
                gasPrice,
                chainId
        );

        this.input = input;
        this.value = value;
        setTo(to);
    }

    public LegacyTransaction(String nonce, String to, String value, BigInteger gas, BigInteger gasPrice, String input, BigInteger chainId) {
        super(
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                nonce,
                Numeric.toHexStringWithPrefix(gas),
                Numeric.toHexStringWithPrefix(gasPrice),
                Numeric.toHexStringWithPrefix(chainId)
        );

        this.input = input;
        this.value = value;
        setTo(to);
    }

    public LegacyTransaction(String nonce, String to, String value, String gas, String gasPrice, String chainId) {
        super(
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                nonce,
                gas,
                gasPrice,
                chainId
        );

        this.value = value;
        setTo(to);
    }

    public LegacyTransaction(String nonce, String to, String value, BigInteger gas, BigInteger gasPrice, BigInteger chainId) {
        super(
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                nonce,
                Numeric.toHexStringWithPrefix(gas),
                Numeric.toHexStringWithPrefix(gasPrice),
                Numeric.toHexStringWithPrefix(chainId)
        );

        this.value = value;
        setTo(to);
    }


    public LegacyTransaction(Klay klay, String gas, String to, String input, String value) {
        super(
                klay,
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                gas
        );

        this.input = input;
        this.value = value;
        setTo(to);
    }

    public LegacyTransaction(Klay klay, BigInteger gas, String to, String input, BigInteger value) {
        super(
                klay,
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                Numeric.toHexStringWithPrefix(gas)
        );

        this.input = input;
        this.value = Numeric.toHexStringWithPrefix(value);
        setTo(to);
    }

    public LegacyTransaction(Klay klay, String gas, String to, String value) {
        super(
                klay,
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                gas
        );

        this.value = value;
        setTo(to);
    }

    public LegacyTransaction(Klay klay, BigInteger gas, String to, BigInteger value) {
        super(
                klay,
                TransactionType.TxTypeLegacyTransaction.toString(),
                TransactionType.TxTypeLegacyTransaction.getType(),
                "0x",
                Numeric.toHexStringWithPrefix(gas)
        );

        this.value = Numeric.toHexStringWithPrefix(value);
        setTo(to);
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction string.
     * @param rlpEncoded RLP-encoded LegacyTransaction string
     * @return LegacyTransaction
     */
    public static LegacyTransaction decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction byte array.
     * @param rlpEncoded RLP-encoded LegacyTransaction byte array.
     * @return LegacyTransaction
     */
    public static LegacyTransaction decode(byte[] rlpEncoded) {
        // TxHashRLP = encode([nonce, gasPrice, gas, to, value, input, v, r, s])
        try {
            RlpList rlpList = RlpDecoder.decode(rlpEncoded);
            List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

            String nonce = ((RlpString) values.get(0)).asString();
            String gasPrice = ((RlpString) values.get(1)).asString();
            String gas = ((RlpString) values.get(2)).asString();
            String to = ((RlpString) values.get(3)).asString();
            String value = ((RlpString) values.get(4)).asString();
            String input = ((RlpString) values.get(5)).asString();

            LegacyTransaction legacyTransaction = new LegacyTransaction(nonce, gas, gasPrice, to, input, value, "");

            byte[] v = ((RlpString) values.get(6)).getBytes();
            byte[] r = ((RlpString) values.get(7)).getBytes();
            byte[] s = ((RlpString) values.get(8)).getBytes();
            KlaySignatureData signatureData = new KlaySignatureData(v, r, s);

            legacyTransaction.appendSignatures(signatureData);
            return legacyTransaction;
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Appends signatures array to transaction.
     * Legacy transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData KlaySignatureData instance contains ECDSA signature data
     */
    @Override
    public void appendSignatures(KlaySignatureData signatureData) {
        if(super.signatures.size() != 0) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        super.appendSignatures(signatureData);
    }

    /**
     * Appends signatures array to transaction.
     * Legacy transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData List of KlaySignatureData contains ECDSA signature data
     */
    @Override
    public void appendSignatures(List<KlaySignatureData> signatureData) {
        if(super.signatures.size() != 0) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        if(signatureData.size() != 1) {
            throw new RuntimeException("Signatures are too long" + TransactionType.TxTypeLegacyTransaction.toString() + " cannot include more than one signature.");
        }

        super.appendSignatures(signatureData);
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        super.validateOptionalValues();
        //TxHashRLP = encode([nonce, gasPrice, gas, to, value, input, v, r, s])
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.to)));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.value)));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.input)));
        KlaySignatureData signatureData = this.getSignatures().get(0);
        rlpTypeList.addAll(signatureData.toRlpList().getValues());

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }


    @Override
    public String getCommonRLPEncodingForSignature() {
        return getRLPEncodingForSignature();
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getRLPEncodingForSignature() {
        super.validateOptionalValues();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.to)));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.value)));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.input)));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(chainId)));
        rlpTypeList.add(RlpString.create(0));
        rlpTypeList.add(RlpString.create(0));

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }

    @Override
    public boolean checkTxField(AbstractTransaction obj, boolean checkSig) {
        super.checkTxField(obj, checkSig);
        if(!(obj instanceof LegacyTransaction)) return false;
        LegacyTransaction txObj = (LegacyTransaction)obj;

        if(!this.to.toLowerCase().equals(txObj.to.toLowerCase())) return false;
        if(!this.value.equals(txObj.value)) return false;
        if(!this.input.equals(txObj.input)) return false;

        return true;
    }

    public void setTo(String to) {
        if(!to.equals("0x") && !Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address.");
        }
        this.to = to;
    }
}
