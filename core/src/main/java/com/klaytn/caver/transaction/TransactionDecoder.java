package com.klaytn.caver.transaction;

import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.TransactionType;
import org.web3j.utils.Numeric;

public class TransactionDecoder {

    public static AbstractTransaction decode(String rlpEncoded) {
        byte[] rlpBytes = Numeric.hexStringToByteArray(rlpEncoded);

        if(rlpBytes[0] == TransactionType.TxTypeValueTransfer.getType()) {
            return null;
        } else {
            return LegacyTransaction.decode(rlpBytes);
        }
    }
}
