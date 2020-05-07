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


    private static final byte sType = (byte)0x03;

    //0x{Public Key X point}||{Public Y point}
    private String publicKey;

    public AccountKeyPublic(String publicKey) {
        this.publicKey = publicKey;
    }

    protected AccountKeyPublic() {

    }

    public static AccountKeyPublic fromXYPoint(String x, String y) {

        if(!AccountKeyPublicUtils.checkPointValid(x, y)) {
            throw new IllegalArgumentException("Invalid Argument Public Key value.");
        }

        String publicKey = Numeric.prependHexPrefix(x) + Numeric.cleanHexPrefix(y);
        return new AccountKeyPublic(publicKey);
    }

    public static AccountKeyPublic fromPublicKey(String publicKey) {
        String key = null;

        if(!AccountKeyPublicUtils.isValidatePublicKeyFormat(publicKey)) {
            throw new IllegalArgumentException("Invalid Argument Public Key value.");
        }

        //check Format -> Compressed or Decompressed
        if(AccountKeyPublicUtils.isCompressedFormat(publicKey)) {
            key = AccountKeyPublicUtils.deCompressPublicKeyXY(publicKey);
        } else {
            key = publicKey;
        }

        return new AccountKeyPublic(key);
    }

    public static AccountKeyPublic decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    public static AccountKeyPublic decode(byte[] rlpEncodedKey) {
        if(rlpEncodedKey[0] != sType) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyPublic Tag");
        }

        //remove Tag
        byte[] encodedPublicKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);
        RlpList rlpList = RlpDecoder.decode(encodedPublicKey);
        String compressedPubKey = ((RlpString) rlpList.getValues().get(0)).asString();

        try {
            //Get decompressed Public Key and ECC Point validation Check in toDecompressPublicKeyXY()
            String publicKey = AccountKeyPublicUtils.deCompressPublicKeyXY(compressedPubKey);
            return new AccountKeyPublic(publicKey);
        } catch (IllegalArgumentException e) {
            throw e;
        }

    }

    @Override
    public String getRLPEncoding() {
        String compressedKey = AccountKeyPublicUtils.toCompressedPublicKey(Numeric.toBigInt(this.publicKey));
        byte[] encodedPubKey = RlpEncoder.encode(RlpString.create(Numeric.hexStringToByteArray(compressedKey)));
        byte[] type = new byte[]{AccountKeyPublic.sType};

        return Numeric.toHexString(BytesUtils.concat(type, encodedPubKey));
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String[] getXYPoint() {
        String noPrefixKeyStr = Numeric.cleanHexPrefix(this.publicKey);
        String[] arr = new String[2];

        arr[0] = noPrefixKeyStr.substring(0, 63);
        arr[1] = noPrefixKeyStr.substring(64, 127);

        return arr;
    }
}
