package com.klaytn.caver.transaction.response;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {
    private final Caver caver;

    public TransactionReceiptProcessor(Caver caver) {
        this.caver = caver;
    }

    public abstract TransactionReceipt.TransactionReceiptData waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt.TransactionReceiptData> sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException{
        TransactionReceipt transactionReceipt = caver.rpc.klay.getTransactionReceipt(transactionHash).send();
        if(transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return Optional.ofNullable(transactionReceipt.getResult());
    }
}
