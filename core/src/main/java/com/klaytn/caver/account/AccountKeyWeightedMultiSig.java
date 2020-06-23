package com.klaytn.caver.account;

import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * AccountKeyWeightedMultiSig is an account key type containing a threshold and WeightedPublicKeys.
 * WeightedPublicKeys contains a slice of {weight and key}. To be a valid tx for an account associated
 * with AccountKeyWeightedMultiSig, the weighted sum of signed public keys should be larger than the threshold.
 */
public class AccountKeyWeightedMultiSig implements IAccountKey {

    /**
     * AccountKeyWeightedMultiSig's Type attribute.
     */
    private static final String TYPE = "0x04";

    /**
     * Validation threshold. To be a valid transaction, the weight sum of signatures should be larger than
     * or equal to the threshold.
     */
    private BigInteger threshold;

    /**
     * A List of weighted public keys. A weighted public key contains a weight and a public key.
     */
    private List<WeightedPublicKey> weightedPublicKeys;


    /**
     * Creates AccountKeyWeightedMultiSig instance with threshold, weightedPublicKey List
     * @param threshold Threshold of AccountKeyWeightedMultiSig
     * @param weightedPublicKeys WeightedPublicKey List
     * @return AccountKeyWeightedMultiSig
     */
    private AccountKeyWeightedMultiSig(BigInteger threshold, List<WeightedPublicKey> weightedPublicKeys) {
        this.threshold = threshold;
        this.weightedPublicKeys = weightedPublicKeys;
    }

    /**
     * Decodes a RLP-encoded AccountKeyWeightedMultiSig string
     * @param rlpEncodedKey RLP-encoded AccountKeyWeightedMultiSig string.
     * @return AccountKeyWeightedMultiSig
     */
    public static AccountKeyWeightedMultiSig decode(String rlpEncodedKey) {
        return AccountKeyWeightedMultiSig.decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyWeightedMultiSig byte array.
     * @param rlpEncodedKey RLP-encoded AccountKeyWeightedMultiSig byte array.
     * @return AccountKeyWeightedMultiSig
     */
    public static AccountKeyWeightedMultiSig decode(byte[] rlpEncodedKey) {
        //check tag
        byte type = Numeric.hexStringToByteArray(TYPE)[0];
        if(rlpEncodedKey[0] != type) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyWeightedMultiSig Tag");
        }

        //remove Tag
        byte[] encodedAccountKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);
        RlpList rlpList = RlpDecoder.decode(encodedAccountKey);
        RlpList values = (RlpList)rlpList.getValues().get(0);

        //get threshold
        BigInteger threshold = ((RlpString)values.getValues().get(0)).asPositiveBigInteger();

        //get WeightedPublicKey
        List<WeightedPublicKey> weightedPublicKeys = new ArrayList<>();
        RlpList rlpWeightedPublicKeys = (RlpList) values.getValues().get(1);
        for (RlpType item : rlpWeightedPublicKeys.getValues()) {
            RlpList rlpWeightedPublicKey = (RlpList) item;
            BigInteger weight = ((RlpString) rlpWeightedPublicKey.getValues().get(0)).asPositiveBigInteger();
            String compressedPublicKey = ((RlpString) rlpWeightedPublicKey.getValues().get(1)).asString();
            weightedPublicKeys.add(new WeightedPublicKey(compressedPublicKey, weight));
        }

        return new AccountKeyWeightedMultiSig(threshold, weightedPublicKeys);
    }

    /**
     * Create AccountKeyWeightedMultiSig instance form public Key array and WeightedMultiSigOption
     * @param publicKeyArr Array of public key string
     * @param options An options which defines threshold and weight.
     * @return AccountKeyWeightedMultiSig
     */
    public static AccountKeyWeightedMultiSig fromPublicKeysAndOptions(String[] publicKeyArr, WeightedMultiSigOptions options) {
        if(publicKeyArr.length > WeightedMultiSigOptions.MAX_COUNT_WEIGHTED_PUBLIC_KEY) {
            throw new IllegalArgumentException("It exceeds maximum public key count.");
        }

        if(publicKeyArr.length != options.getWeights().size()) {
            throw new IllegalArgumentException("The count of public keys is not equal to the length of weight array.");
        }

        List<WeightedPublicKey> weightedPublicKeyList = new ArrayList<>();
        for(int i=0; i< publicKeyArr.length; i++) {
            weightedPublicKeyList.add(new WeightedPublicKey(publicKeyArr[i], options.getWeights().get(i)));
        }

        return new AccountKeyWeightedMultiSig(options.getThreshold(), weightedPublicKeyList);
    }


    /**
     * Encodes a AccountKeyWeightedMultiSig Object by RLP-encoding method.
     * @return RLP-encoded AccountKeyWeightedMultiSig String
     */
    @Override
    public String getRLPEncoding() {
        if (threshold == null || weightedPublicKeys == null) {
            throw new NullPointerException("threshold or weightedPublicKeys must be exists for multisig.");
        }
        if(weightedPublicKeys.size() == 0) {
            throw new RuntimeException("weightedPublicKeys must have items for multisig.");
        }

        List<RlpType> rlpTypeList = new ArrayList<>();

        rlpTypeList.add(RlpString.create(this.threshold));

        List<RlpType> rlpWeightedPublicKeyList = new ArrayList<>();
        for(WeightedPublicKey item : this.weightedPublicKeys) {
            List<RlpType> rlpWeightedPublicKey = new ArrayList<>();

            BigInteger weight = item.weight;
            String compressedKey = Utils.compressPublicKey(item.getPublicKey());
            rlpWeightedPublicKey.addAll(Arrays.asList(
                    RlpString.create(weight),
                    RlpString.create(Numeric.hexStringToByteArray(compressedKey))
            ));

            rlpWeightedPublicKeyList.add(new RlpList(rlpWeightedPublicKey));
        }
        rlpTypeList.add(new RlpList(rlpWeightedPublicKeyList));

        byte[] encodedWeightedKey = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = Numeric.hexStringToByteArray(AccountKeyWeightedMultiSig.getType());

        return Numeric.toHexString(BytesUtils.concat(type, encodedWeightedKey));
    }

    /**
     * Getter function for threshold
     * @return threshold
     */
    public BigInteger getThreshold() {
        return threshold;
    }

    /**
     * Getter function for List of WeightedOfPublicKey
     * @return List of WeightedOfPublicKey
     */
    public List<WeightedPublicKey> getWeightedPublicKeys() {
        return weightedPublicKeys;
    }

    /**
     * Returns an AccountKeyWeightedMultiSig's type attribute
     * @return AccountKeyWeightedMultiSig's type attribute
     */
    public static String getType() {
        return TYPE;
    }
}
