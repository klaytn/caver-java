package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a smart contract deploy transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypesmartcontractdeploy to see more detail.
 */
public class SmartContractDeploy extends AbstractTransaction {

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
    String input = " 0x";

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
     * SmartContractDeploy Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<SmartContractDeploy.Builder> {
        String to = "0x";
        String value = "0x00";
        String input = " 0x";
        boolean humanReadable = false;
        String codeFormat = Numeric.toHexStringWithPrefix(CodeFormat.EVM);

        public Builder() {
            super(TransactionType.TxTypeSmartContractDeploy.toString());
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

        public SmartContractDeploy build() {
            return new SmartContractDeploy(this);
        }
    }

    /**
     * Creates a SmartContractDeploy instance.
     * @param builder SmartContractDeploy.Builder instance.
     */
    public SmartContractDeploy(SmartContractDeploy.Builder builder) {
        super(builder);
        setValue(builder.value);

        setTo(builder.to);
        setValue(builder.value);
        setInput(builder.input);
        setHumanReadable(builder.humanReadable);
        setCodeFormat(builder.codeFormat);
    }

    /**
     * Creates a SmartContractDeploy instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The message data attached to the transaction.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     */
    public SmartContractDeploy(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<KlaySignatureData> signatures, String to, String value, String input, boolean humanReadable, String codeFormat) {
        super(
                klaytnCall,
                TransactionType.TxTypeSmartContractDeploy.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures
        );
        setTo(to);
        setValue(value);
        setInput(input);
        setHumanReadable(humanReadable);
        setCodeFormat(codeFormat);
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy string.
     * @param rlpEncoded RLP-encoded SmartContractDeploy string
     * @return SmartContractDeploy
     */
    public static SmartContractDeploy decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy byte array.
     * @param rlpEncoded RLP-encoded SmartContractDeploy byte array.
     * @return SmartContractDeploy
     */
    public static SmartContractDeploy decode(byte[] rlpEncoded) {
        // TXHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat, txSignatures])

        if(rlpEncoded[0] != (byte)TransactionType.TxTypeSmartContractDeploy.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeSmartContractDeploy.toString());
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
        List<KlaySignatureData> signatureDataList = KlaySignatureData.decodeSignatures(senderSignatures);

        SmartContractDeploy smartContractDeploy = new SmartContractDeploy.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setTo(to)
                .setValue(value)
                .setFrom(from)
                .setInput(input)
                .setHumanReadable(humanReadable)
                .setCodeFormat(codeFormat)
                .setSignList(signatureDataList)
                .build();

        return smartContractDeploy;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // TXHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat, txSignatures])
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
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(this.isHumanReadable()? 1 : 0));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getCodeFormat())));
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeSmartContractDeploy.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat]), chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, to, value, from, input, humanReadable, codeFormat])
        byte type = (byte)TransactionType.TxTypeSmartContractDeploy.getType();
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(RlpString.create(this.isHumanReadable()? 1 : 0));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getCodeFormat())));

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
        if(!(obj instanceof SmartContractDeploy)) return false;
        SmartContractDeploy txObj = (SmartContractDeploy)obj;

        if(!this.getTo().toLowerCase().equals(txObj.getTo().toLowerCase())) return false;
        if(!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(txObj.getValue()))) return false;
        if(!this.getInput().equals(txObj.getInput())) return false;
        if(this.isHumanReadable() != txObj.isHumanReadable()) return false;
        if(!this.getCodeFormat().equals(txObj.getCodeFormat())) return false;

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
    public boolean isHumanReadable() {
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

        if(!to.equals("0x") && !Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address. : " + to);
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
            throw new IllegalArgumentException("Invalid value.");
        }
        this.value = value;
    }

    /**
     * Setter function for input
     * @param input Data attached to the transaction, used for transaction execution.
     */
    public void setInput(String input) {
        if(input == null) {
            throw new IllegalArgumentException("input is missing.");
        }

        if(!input.equals("0x") && !Utils.isHex(input)) {
            throw new IllegalArgumentException("Invalid input.");
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
