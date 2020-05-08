package com.klaytn.caver.account;

import org.web3j.utils.Numeric;

import java.util.Arrays;

/**
 * AccountKeyFail is used for smart contract accounts so that a transaction sent from
 * the smart contract account always fails.
 * If an account has the key AccountKeyFail, the tx validation process always fails.
 */
public class AccountKeyFail implements IAccountKey{
    private static final byte[] RLP = new byte[]{(byte) 0x03, (byte) 0xc0};
    private static final byte TYPE = (byte)0x03;

    public AccountKeyFail() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail string
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic string
     * @return AccountKeyFail
     */
    public static AccountKeyFail decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyFail byte array
     * @return AccountKeyFail
     */
    public static AccountKeyFail decode(byte[] rlpEncodedKey) {
        if(!Arrays.equals(rlpEncodedKey, RLP)) {
            throw new RuntimeException("Invalid RLP-encoded account key String");
        }

        return new AccountKeyFail();
    }

    /**
     * Encodes a AccountKeyPublic Object by RLP-Encoding method.
     * @return RLP-encoded AccountKeyPublic String
     */
    @Override
    public String getRLPEncoding() {
        return Numeric.toHexString(RLP);
    }

    public static byte getType() {
        return TYPE;
    }
}
