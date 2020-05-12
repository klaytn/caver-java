package com.klaytn.caver.account;

import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.util.Arrays;


/**
 * AccountKeyPublic is used for accounts having one public key.
 * If an account has an AccountKeyPublic object, the tx validation process is done like below:
 *
 * Get the public key derived from ecrecover(txhash, txsig)
 * Check that the derived public key is the same as the corresponding
 * account's public key
 *
 */
public class AccountKeyPublic implements IAccountKey{

    /**
     * AccountKeyPublic's Type attribute.
     */
    private static final String TYPE = "0x02";

    /**
     * ECC Public Key value with "SECP-256k1" curve.
     * This String has following format.
     * 1. Uncompressed format : 0x{Public Key X point}||{Public Y point}
     * 2. Compressed format : 0x{02 or 03 || Public Key X}
     */
    private String publicKey;

    /**
     * Creates an AccountKeyPublic instance.
     * @param publicKey ECC public key. (Uncompress or compress format)
     */
    public AccountKeyPublic(String publicKey) {
        setPublicKey(publicKey);
    }

    /**
     * Creates AccountKeyPublic instance from Elliptic curve x, y coordinates.
     * @param x The point x
     * @param y The point y
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromXYPoint(String x, String y) {
        String publicKey = Numeric.prependHexPrefix(x) + Numeric.cleanHexPrefix(y);
        return new AccountKeyPublic(publicKey);
    }

    /**
     * Creates AccountKeyPublic instance from ECC Public Key.
     * @param publicKey The public key string. This public key can be in format of compressed or uncompressed.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromPublicKey(String publicKey) {
        return new AccountKeyPublic(publicKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic string
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic string.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic byte array
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(byte[] rlpEncodedKey) {
        byte type = Numeric.hexStringToByteArray(getType())[0];
        if(rlpEncodedKey[0] != type) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyPublic Tag");
        }

        //remove Tag
        byte[] encodedPublicKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);
        RlpList rlpList = RlpDecoder.decode(encodedPublicKey);
        String compressedPubKey = ((RlpString) rlpList.getValues().get(0)).asString();

        try {
            //Get decompressed Public Key and ECC Point validation Check in toDecompressPublicKeyXY()
            String publicKey = AccountKeyPublicUtils.decompressPublicKey(compressedPubKey);
            return new AccountKeyPublic(publicKey);
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Encodes a AccountKeyPublic Object by RLP-encoding method.
     * @return RLP-encoded AccountKeyPublic String
     */
    @Override
    public String getRLPEncoding() {
        String compressedKey = AccountKeyPublicUtils.compressPublicKey(this.publicKey);
        byte[] encodedPubKey = RlpEncoder.encode(RlpString.create(Numeric.hexStringToByteArray(compressedKey)));
        byte[] type = Numeric.hexStringToByteArray(AccountKeyPublic.getType());

        return Numeric.toHexString(BytesUtils.concat(type, encodedPubKey));
    }

    /**
     * Returns the x and y coordinates of publicKey.
     * @return String array of X,Y coordinates.
     */
    public String[] getXYPoint() {
        String key = this.getPublicKey();
        if (AccountKeyPublicUtils.isCompressedPublicKey(this.getPublicKey())) {
            key = AccountKeyPublicUtils.decompressPublicKey(this.getPublicKey());
        }

        String noPrefixKeyStr = Numeric.cleanHexPrefix(key);
        String[] arr = new String[2];

        arr[0] = noPrefixKeyStr.substring(0, 64); // x point
        arr[1] = noPrefixKeyStr.substring(64); // y point

        return arr;
    }

    /**
     * Getter function for publicKey
     * @return ECC PublicKey string
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Returns an AccountKeyPublic's type attribute
     * @return AccountKeyPublic's type attribute
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Setter function for publicKey
     * @param publicKey ECC public key(compressed or uncompressed format)
     */
    public void setPublicKey(String publicKey) {
        if(!AccountKeyPublicUtils.isValidPublicKey(publicKey)) {
            throw new RuntimeException("Invalid Public Key format");
        }

        this.publicKey = publicKey;
    }
}
