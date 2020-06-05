package com.klaytn.caver.transaction;

import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransferMemo;
import org.web3j.utils.Numeric;

public class TransactionDecoder {

    public static AbstractTransaction decode(String rlpEncoded) {
        byte[] rlpBytes = Numeric.hexStringToByteArray(rlpEncoded);

        if(rlpBytes[0] == TransactionType.TxTypeValueTransfer.getType()) {
            return ValueTransfer.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeValueTransferMemo.getType()) {
            return ValueTransferMemo.decode(rlpBytes);
        } else {
            return LegacyTransaction.decode(rlpBytes);
        }
    }
}
