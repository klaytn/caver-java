package com.klaytn.caver.account;

import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.util.Arrays;

public class AccountKeyPublic implements IAccountKey{

    private static final byte TYPE = (byte)0x03;

    //0x{Public Key X point}||{Public Y point}
    /**
     * ECC Public Key value with "SECP-256k1" curve.
     * This String has following format.
     * 0x{Public Key X point}||{Public Y point}
     */
    private String publicKey;

    public AccountKeyPublic(String publicKey) {
        this.publicKey = publicKey;
    }

    protected AccountKeyPublic() {

    }

    /**
     * Creates AccountKeyPublic instance from Elliptic curve x, y coordinates.
     * @param x The point x
     * @param y The point y
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromXYPoint(String x, String y) {

        if(!AccountKeyPublicUtils.checkPointValid(x, y)) {
            throw new IllegalArgumentException("Invalid Argument Public Key value.");
        }

        String publicKey = Numeric.prependHexPrefix(x) + Numeric.cleanHexPrefix(y);
        return new AccountKeyPublic(publicKey);
    }

    /**
     * Creates AccountKeyPublic instance from ECC Public Key.
     * @param publicKey The public key string. This public key can be in format of compressed or uncompressed.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromPublicKey(String publicKey) {
        String key = null;

        if(!AccountKeyPublicUtils.isValidatePublicKeyFormat(publicKey)) {
            throw new IllegalArgumentException("Invalid Argument Public Key value.");
        }

        //check Format -> Compressed or Decompressed
        if(AccountKeyPublicUtils.isCompressedFormat(publicKey)) {
            key = AccountKeyPublicUtils.decompressPublicKeyXY(publicKey);
        } else {
            key = publicKey;
        }

        return new AccountKeyPublic(key);
    }

    /**
     * Decodes an RLP-encoded AccountKeyPublic string
     * @param rlpEncodedKey An RLP-encoded AccountKeyPublic string.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes an RLP-encoded AccountKeyPublic byte array
     * @param rlpEncodedKey An RLP-encoded AccountKeyPublic byte array
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(byte[] rlpEncodedKey) {
        if(rlpEncodedKey[0] != AccountKeyPublic.TYPE) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyPublic Tag");
        }

        //remove Tag
        byte[] encodedPublicKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);
        RlpList rlpList = RlpDecoder.decode(encodedPublicKey);
        String compressedPubKey = ((RlpString) rlpList.getValues().get(0)).asString();

        try {
            //Get decompressed Public Key and ECC Point validation Check in toDecompressPublicKeyXY()
            String publicKey = AccountKeyPublicUtils.decompressPublicKeyXY(compressedPubKey);
            return new AccountKeyPublic(publicKey);
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Encodes a AccountKeyPublic Object by RLP-Encoding method.
     * @return a RLP-encoded AccountKeyPublic String
     */
    @Override
    public String getRLPEncoding() {
        String compressedKey = AccountKeyPublicUtils.toCompressedPublicKey(Numeric.toBigInt(this.publicKey));
        byte[] encodedPubKey = RlpEncoder.encode(RlpString.create(Numeric.hexStringToByteArray(compressedKey)));
        byte[] type = new byte[] { AccountKeyPublic.TYPE };

        return Numeric.toHexString(BytesUtils.concat(type, encodedPubKey));
    }

    /**
     * Returns the x and y coordinates of publicKey.
     * @return String array of X,Y coordinates.
     */
    public String[] getXYPoint() {
        String noPrefixKeyStr = Numeric.cleanHexPrefix(this.publicKey);
        String[] arr = new String[2];

        arr[0] = noPrefixKeyStr.substring(0, 63);
        arr[1] = noPrefixKeyStr.substring(64, 127);

        return arr;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
