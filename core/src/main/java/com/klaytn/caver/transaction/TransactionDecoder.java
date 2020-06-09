package com.klaytn.caver.transaction;

import com.klaytn.caver.transaction.type.*;
import org.web3j.utils.Numeric;

public class TransactionDecoder {

    public static AbstractTransaction decode(String rlpEncoded) {
        byte[] rlpBytes = Numeric.hexStringToByteArray(rlpEncoded);

        if(rlpBytes[0] == TransactionType.TxTypeValueTransfer.getType()) {
            return ValueTransfer.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeValueTransferMemo.getType()) {
            return ValueTransferMemo.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeSmartContractDeploy.getType()) {
            return SmartContractDeploy.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeSmartContractExecution.getType()) {
            return SmartContractExecution.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeAccountUpdate.getType()) {
            return AccountUpdate.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeCancel.getType()) {
            return Cancel.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeChainDataAnchoring.getType()) {
            return ChainDataAnchoring.decode(rlpBytes);
        }
        else {
            return LegacyTransaction.decode(rlpBytes);
        }
    }
}
