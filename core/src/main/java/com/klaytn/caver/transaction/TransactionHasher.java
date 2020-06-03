package com.klaytn.caver.transaction;

import org.web3j.crypto.Hash;

public class TransactionHasher {

    public static String getHashForSignature(AbstractTransaction transaction) {
        String rlpEncoded = transaction.getRLPEncodingForSignature();
        return Hash.sha3(rlpEncoded);
    }
}
