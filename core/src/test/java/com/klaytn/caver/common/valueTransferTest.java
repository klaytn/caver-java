package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.exception.PlatformErrorException;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.manager.TransactionReceiptProcessor;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.model.ValueTransferTransaction;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.utils.Convert;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Ignore
public class valueTransferTest {

    @Test
    public void transferTest() throws IOException, TransactionException, PlatformErrorException {
//        String privateKey = "0x8b0bfc810606cb3ba9f7687ee3bded1e780911e73641f31e9d1732ebb2369d32";
        String privateKey = "private Key";
        Caver caver = Caver.build(Caver.BAOBAB_URL);

        KlayCredentials credentials = KlayCredentials.create(privateKey);
        TransactionManager transactionManager = new TransactionManager.Builder(caver, credentials)
                .setChaindId(ChainId.BAOBAB_TESTNET)
                .build();

        ValueTransferTransaction transferTransaction = ValueTransferTransaction.create(
                credentials.getAddress(),
                "0x1136f9e20a01fce90812a13078f8f7ca8fc3b6b7",
                Convert.toPeb(BigDecimal.ONE, Convert.Unit.PEB).toBigInteger(),
                BigInteger.valueOf(100_000));

        KlayRawTransaction klayRawTransaction = transactionManager.sign(transferTransaction);

        String rawTransaction = klayRawTransaction.getValueAsString();
        System.out.println(rawTransaction);

        Bytes32 transactionHash = caver.klay().sendSignedTransaction(rawTransaction).send();
        if (transactionHash.hasError()) {
            Response.Error error = transactionHash.getError();
            System.out.println(error.getCode());
            System.out.println(error.getMessage());
            System.out.println(error.getData());
        } else {
            System.out.println("TransactionHash : " + transactionHash.getResult());
        }

        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = receiptProcessor.waitForTransactionReceipt(transactionHash.getResult());

        System.out.println("TransactionReceipt : " + transactionReceipt);
    }

    @Test
    public void transferTest1() throws Exception {
        String privateKey = "0x8b0bfc810606cb3ba9f7687ee3bded1e780911e73641f31e9d1732ebb2369d32";
        Caver caver = Caver.build(Caver.BAOBAB_URL);

        KlayCredentials credentials = KlayCredentials.create(privateKey);

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer
                .create(caver, credentials, ChainId.BAOBAB_TESTNET)
                .sendFunds(ValueTransferTransaction.create(
                        credentials.getAddress(),  // fromAddress
                        "0x1136f9e20a01fce90812a13078f8f7ca8fc3b6b7",  // toAddress
                        Convert.toPeb(BigDecimal.ONE, Convert.Unit.PEB).toBigInteger(),  // value
                        BigInteger.valueOf(100_000)  // gasLimit
                )).send();
    }

    @Test
    public void sendRawTransaction() throws IOException, TransactionException {
        Caver caver = Caver.build(Caver.MAINNET_URL);

        String rawTx = "0x08f87f1a8505d21dba00830186a094e0711069b41397827fdfc3f31c901018e4301500019480f4009fb84eb6a151565657fb272bb9f016efb6f847f845824055a0ab0871aced6f1a73b55bf0491cb0039b703eac64dd57745e5404218e64cd6ce3a0588f3150f4096ab697fc59878a084d29599faf94bc72658dd5a15332a83cdb5d";

        String txHash = caver.klay().sendSignedTransaction(rawTx).send().getResult();

        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
    }
}
