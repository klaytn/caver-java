package com.klaytn.caver.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.klaytn.caver.Klay;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * Abstract class that implements common logic for each fee delegated with ratio transaction type.
 */
abstract public class AbstractFeeDelegatedWithRatioTransaction extends AbstractFeeDelegatedTransaction {

    /**
     * Fee ratio of the fee payer.
     * The valid range is between 1 and 99. Zero(0) is not allowed. 100 and above are not allowed as well.
     */
    String feeRatio;

    /**
     * AbstractFeeDelegatedWithRatioTransaction Builder class.
     * @param <B> An generic extends to AbstractFeeDelegatedWithRatioTransaction.Builder
     */
    public static class Builder<B extends AbstractFeeDelegatedWithRatioTransaction.Builder> extends AbstractFeeDelegatedTransaction.Builder<B> {
        String feeRatio;

        public Builder(String type) {
            super(type);
        }

        public B setFeeRatio(String feeRatio) {
            this.feeRatio = feeRatio;
            return (B)this;
        }

        public B setFeeRatio(BigInteger feeRatio) {
            return setFeeRatio(Numeric.toHexStringWithPrefix(feeRatio));
        }
    }

    /**
     * Create an AbstractFeeDelegatedWithRatioTransaction instance
     * @param builder AbstractFeeDelegatedWithRatioTransaction.Builder instance.
     */
    public AbstractFeeDelegatedWithRatioTransaction(Builder builder) {
        super(builder);
        setFeeRatio(builder.feeRatio);
    }

    /**
     * Create an AbstractFeeDelegatedWithRatioTransaction instance
     * @param klaytnCall Klay RPC instance
     * @param type Transaction's type string
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer The address of the fee payer.
     * @param feePayerSignatures The fee payers's signatures.
     * @param feeRatio A fee ratio of the fee payer.
     */
    public AbstractFeeDelegatedWithRatioTransaction(Klay klaytnCall, String type, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio) {
        super(klaytnCall, type, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures);
        setFeeRatio(feeRatio);
    }

    /**
     * Check equals txObj passed parameter and current instance.
     * @param txObj The AbstractFeeDelegatedWithRatioTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    public boolean compareTxField(AbstractFeeDelegatedWithRatioTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!Numeric.toBigInt(this.getFeeRatio()).equals(Numeric.toBigInt(txObj.getFeeRatio()))) return false;

        return true;
    }

    /**
     * Getter function for feeRatio.
     * @return String
     */
    @JsonIgnore
    public String getFeeRatio() {
        return feeRatio;
    }

    @JsonProperty("feeRatio")
    public BigInteger getFeeRatioInteger() {
        return Numeric.toBigInt(feeRatio);
    }

    /**
     * Setter function for feeRatio.
     * @param feeRatio A fee ratio of the fee payer.
     */
    public void setFeeRatio(String feeRatio) {
        if(feeRatio == null) {
            throw new IllegalArgumentException("feeRatio is missing.");
        }

        if(!Utils.isNumber(feeRatio) && !Utils.isHex(feeRatio)) {
            throw new IllegalArgumentException("Invalid type of feeRatio: feeRatio should be number type or hex number string");
        }
        int feeRatioVal = Numeric.toBigInt(feeRatio).intValue();
        if(feeRatioVal <= 0 || feeRatioVal >= 100) {
            throw new IllegalArgumentException("Invalid feeRatio: feeRatio is out of range. [1,99]");
        }

        this.feeRatio = feeRatio;
    }

    /**
     * Setter function for feeRatio.
     * @param feeRatio A fee ratio of the fee payer.
     */
    public void setFeeRatio(BigInteger feeRatio) {
        if(feeRatio == null) {
            throw new IllegalArgumentException("feeRatio is missing.");
        }
        
        setFeeRatio(Numeric.toHexStringWithPrefix(feeRatio));
    }
}
