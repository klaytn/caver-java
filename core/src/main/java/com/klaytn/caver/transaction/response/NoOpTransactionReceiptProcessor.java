package com.klaytn.caver.transaction.response;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

/**
 * Return an empty receipt back to callers containing only the transaction hash.
 */
public class NoOpTransactionReceiptProcessor extends TransactionReceiptProcessor {
    public NoOpTransactionReceiptProcessor(Caver caver) {
        super(caver);
    }

    @Override
    public TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        TransactionReceipt.TransactionReceiptData transactionReceiptData = new TransactionReceipt.TransactionReceiptData();
        transactionReceiptData.setTransactionHash(transactionHash);

        return transactionReceiptData;
    }
}
