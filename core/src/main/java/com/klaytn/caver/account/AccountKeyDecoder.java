package com.klaytn.caver.account;

import org.web3j.utils.Numeric;

/**
 * Representing an AccountKeyDecoder which can decode RLP-encoded accountKey string.
 */
public class AccountKeyDecoder {

    /**
     * Decodes a RLP-encoded AccountKey implements IAccountKey interface
     * @param rlpEncodedKey RLP-encoded AccountKey string.
     * @return AccountKey implements IAccountKey interface
     */
    public static IAccountKey decode(String rlpEncodedKey) {
        String hexPrefixEncoded = Numeric.prependHexPrefix(rlpEncodedKey);

        if(hexPrefixEncoded.startsWith(AccountKeyFail.getType())) {
            return AccountKeyFail.decode(hexPrefixEncoded);
        } else if(hexPrefixEncoded.startsWith(AccountKeyLegacy.getType())) {
            return AccountKeyLegacy.decode(hexPrefixEncoded);
        } else if(hexPrefixEncoded.startsWith(AccountKeyPublic.getType())) {
            return AccountKeyPublic.decode(hexPrefixEncoded);
        } else if(hexPrefixEncoded.startsWith(AccountKeyWeightedMultiSig.getType())) {
            return AccountKeyWeightedMultiSig.decode(hexPrefixEncoded);
        } else if(hexPrefixEncoded.startsWith(AccountKeyRoleBased.getType())) {
            return AccountKeyRoleBased.decode(hexPrefixEncoded);
        } else if(hexPrefixEncoded.startsWith(AccountKeyNil.getType())){
            return AccountKeyNil.decode(hexPrefixEncoded);
        } else {
            throw new RuntimeException("Invalid RLP-encoded account key string");
        }
    }
}
