package com.klaytn.caver.account;

import com.klaytn.caver.utils.AccountKeyPublicUtils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class WeightedPublicKey {

    /**
     * ECC Public Key value with "SECP-256k1" curve.
     * This String has following format.
     * 1. Uncompressed format : 0x{Public Key X point}||{Public Y point}
     * 2. Compressed format : 0x{02 or 03 || Public Key X}
     */
    private String publicKey;

    /**
     * The weight of key
     */
    BigInteger weight;

    public static final int OFFSET_WEIGHT = 0;
    public static final int OFFSET_PUBLIC_KEY = 1;

    /**
     * Creates WeightedPublicKey instance
     * @param publicKey The ECC Public key String.(Compressed or Uncompressed format)
     * @param weight THe weight of Key
     */
    public WeightedPublicKey(String publicKey, BigInteger weight) {
        setPublicKey(publicKey);
        this.weight = weight;
    }

    /**
     * PublicKey getter function.
     * @return publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * PublicKey setter function.
     * It allows both compressed and uncompressed format.
     * @param publicKey ecc Public key
     */
    public void setPublicKey(String publicKey) {
        if(!AccountKeyPublicUtils.isValidatePublicKeyFormat(publicKey)) {
            throw new IllegalArgumentException("Invalid Public key format");
        }
        this.publicKey = publicKey;
    }

    /**
     * Weight getter method
     * @return weight
     */
    public BigInteger getWeight() {
        return weight;
    }

    /**
     * Weight setter method
     * @param weight a weigth
     */
    public void setWeight(BigInteger weight) {
        this.weight = weight;
    }

    /**
     * Returns an encoded weighted public key string.
     * @return array of string. [0] : weight, [1] compressed public key
     */
    public String[] encodeToBytes() {
        if (this.publicKey == null) {
            throw new RuntimeException("public key should be specified for a multisig account");
        }
        if(this.weight == null) {
            throw new RuntimeException("weight should be specified for a multisig account");
        }

        String compressedKey = AccountKeyPublicUtils.compressPublicKey(this.publicKey);
        return new String[] {Numeric.toHexStringWithPrefix(this.weight), compressedKey};
    }
}
