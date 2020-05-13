package com.klaytn.caver.account;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Representing an options for AccountKeyWeightedMultiSig.
 * This class will define threshold and weights.
 */
public class WeightedMultiSigOptions {

    public static final int MAX_COUNT_WEIGHTED_PUBLIC_KEY = 10;

    /**
     * Validation threshold. To be a valid transaction, the weight sum of signatures should be larger than
     * or equal to the threshold.
     */
    private BigInteger threshold;

    /**
     * A List of weight values.
     */
    private List<BigInteger> weights;

    /**
     * Creates a empty WeightedMultiSIgOptions instance
     * This instance used when setting AccountKeyPublic to AccountKeyRoleBased component.
     */
    public WeightedMultiSigOptions() {
    }

    /**
     * Creates a WeightedMultiSigOptions instance.
     * @param threshold a threshold
     * @param weights a List contains weight value of key
     */
    public WeightedMultiSigOptions(BigInteger threshold, List<BigInteger> weights) {
        if (!isValidateOptions(threshold, weights)) {
             throw new IllegalArgumentException("Invalid argument in passing params.");
        }
        this.threshold = threshold;
        this.weights = weights;
    }

    /**
     * Before creating an instance, check whether the passed option is valid.
     *   - check threshold value bigger than zero
     *   - weights has weight up to 10
     *   - threshold must have value smaller than sum of weights item
     * @param threshold a threshold
     * @param weights
     * @return
     */
    public boolean isValidateOptions(BigInteger threshold, List<BigInteger> weights) {
        BigInteger sumOfWeights = BigInteger.ZERO;

        //threshold value has bigger than zero.
        if(threshold.compareTo(BigInteger.ZERO) <= 0) {
            return false;
        }

        //Weighted Public Key has up to 10.
        if(weights.size() > MAX_COUNT_WEIGHTED_PUBLIC_KEY) {
            return false;
        }

        for(BigInteger weight : weights) {
            sumOfWeights = sumOfWeights.add(weight);
        }

        if(threshold.compareTo(sumOfWeights) > 0) {
            return false;
        }

        return true;
    }

    public boolean isEmpty() {
        if(this.getWeights() == null && this.threshold == null) {
            return true;
        }
        return false;
    }

    /**
     * Getter function for threshold
     * @return threshold
     */
    public BigInteger getThreshold() {
        return threshold;
    }

    /**
     * Getter function for list of weight
     * @return weights
     */
    public List<BigInteger> getWeights() {
        return weights;
    }
}
