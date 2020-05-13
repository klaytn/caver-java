package com.klaytn.caver.account;

import com.klaytn.caver.tx.account.AccountKeyRoleBased;
import org.web3j.utils.Numeric;

/**
 * AccountKeyNil represents an empty key. If an account tries to having an AccountKeyNil object,
 * the transaction will be failed. AccountKeyNil is only used only for TxTypeAccountUpdate transactions
 * with role-based keys. For example, if an account tries to update RoleAccountUpdate key only, the key
 * field of the TxTypeAccountUpdate transaction would be: [AccountKeyNil, NewKey, AccountKeyNil]
 * Then, only the RoleAccountUpdate key is updated. Other roles are not updated.
 */
public class AccountKeyNil implements IAccountKey{

    /**
     * AccountKeyNil's RLP-Encoded data
     */
    private static final byte[] RLP = new byte[]{(byte) 0x80};

    /**
     * Creates an AccountKeyNil instance.
     */
    public AccountKeyNil() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyNil string
     * @param rlpEncodedKey RLP-encoded AccountKeyNil string
     * @return AccountKeyNil
     */
    public static AccountKeyNil decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyNil byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyNil byte array
     * @return AccountKeyNil
     */
    public static AccountKeyNil decode(byte[] rlpEncodedKey) {
        if (rlpEncodedKey.length != 1 || rlpEncodedKey[0] != RLP[0]) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyNil");
        }
        return new AccountKeyNil();
    }

    /**
     * Encodes a AccountKeyNil Object by an RLP-Encoding method.
     * @return RLP-encoded AccountKeyNil String
     */
    @Override
    public String getRLPEncoding() {
        return Numeric.toHexString(RLP);
    }

    /**
     * Return an AccountKeyNil's type attribute.
     * @return AccountKeyNil's type attribute.
     */
    public static String getType() {
        return Numeric.toHexString(RLP);
    }


}
