/*
 * Copyright 2019 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.model.executor;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.model.TransactionGenerator;
import com.klaytn.caver.model.VariableStorage;
import com.klaytn.caver.model.dto.Expected;
import com.klaytn.caver.model.dto.TestComponent;
import com.klaytn.caver.model.dto.Transaction;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionReceiptProcessor;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.type.TxType;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class TransactionExecutor extends Executor {
    public static final String FEE_DELEGATED_PREFIX = "FEE_DELEGATED";

    private Caver caver;
    private Transaction transaction;
    private TransactionReceiptProcessor pollingTransactionReceiptProcessor;

    public TransactionExecutor(Transaction transaction, String URL) {
        this.transaction = transaction;
        this.caver = Caver.build(URL);
        this.pollingTransactionReceiptProcessor
                = new PollingTransactionReceiptProcessor(caver, 1000, 50);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Transaction.InnerTransaction getTx() {
        return transaction.getTx();
    }

    public Expected getExpected() {
        return transaction.getExpected();
    }

    public TxType generateTransaction() throws Exception {
        String type = getTx().getType();
        return TransactionGenerator.generate(type, this);
    }

    public KlayRawTransaction getSignatureData() throws Exception {
        TxType transaction = generateTransaction();
        KlayRawTransaction signatureData
                = transaction.sign(getTx().getPrivateKey(), getTx().getChainId());

        if (getTx().getType().startsWith(FEE_DELEGATED_PREFIX)) {
            signatureData = getFeeDelegatedSignatureData(signatureData);
        }

        return signatureData;
    }

    public KlayRawTransaction getFeeDelegatedSignatureData(KlayRawTransaction klayRawTransaction) {
        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, getTx().getFeePayerPrivateKey())
                .setChainId(Transaction.DEFAULT_CHAIN_ID).build();
        return feePayerManager.sign(klayRawTransaction.getValueAsString());
    }

    public Bytes32 sendSignedTransaction(String signedTransaction) throws IOException {
        return caver.klay().sendSignedTransaction(signedTransaction).send();
    }

    public KlayTransactionReceipt.TransactionReceipt getReceipt(Bytes32 response) throws IOException, TransactionException {
        return pollingTransactionReceiptProcessor.waitForTransactionReceipt(response.getResult());
    }

    public KlayTransactionReceipt.TransactionReceipt getReceipt() throws Exception {
        Bytes32 response = sendSignedTransaction(getSignatureData().getValueAsString());
        return pollingTransactionReceiptProcessor.waitForTransactionReceipt(response.getResult());
    }

    @Override
    public Response execute() throws Exception {
        KlayRawTransaction signatureData = getSignatureData();

        String signedTransaction = manipulateSignature(signatureData);

        return sendSignedTransaction(signedTransaction);
    }

    @Override
    public TestComponent.Type getType() {
        return transaction.getType();
    }

    private String manipulateSignature(KlayRawTransaction signatureData) {
        String signedTransaction = signatureData.getValueAsString();
        if (transaction.getTx().getV() != null) {
            String originV = Numeric.toHexStringNoPrefix(signatureData.getSignatureData().getV());
            signedTransaction = signedTransaction.replace(originV, Numeric.cleanHexPrefix(getTx().getV()));
        }
        if (getTx().getR() != null) {
            String originR = Numeric.toHexStringNoPrefix(signatureData.getSignatureData().getR());
            signedTransaction = signedTransaction.replace(originR, Numeric.cleanHexPrefix(getTx().getR()));
        }
        if (getTx().getS() != null) {
            String originS = Numeric.toHexStringNoPrefix(signatureData.getSignatureData().getS());
            signedTransaction = signedTransaction.replace(originS, Numeric.cleanHexPrefix(getTx().getS()));
        }
        return signedTransaction;
    }

    public BigInteger getNonce() throws IOException {
        String target = getTx().getSender().getAddress();
        if (getTx().getNonce() != null) return getTx().getNonce();
        if (getTx().getFrom() != null) target = getTx().getFrom();
        return caver.klay()
                .getTransactionCount(target, DefaultBlockParameterName.PENDING)
                .send().getValue();
    }

    public void fillGasPrice(Transaction.InnerTransaction tx) throws IOException {
        if (tx.getGasPrice() == null) {
            String baseFee = caver.rpc.klay.getHeader(DefaultBlockParameterName.LATEST).send().getResult().getBaseFeePerGas();
            tx.setGasPrice(Numeric.toBigInt(baseFee).multiply(Numeric.toBigInt("2")).toString(16));
        }
    }

    public Object getTo() throws Exception {
        String to = transaction.getTx().getTo();
        if (!to.isEmpty() && !VariableStorage.isHexValue(to)) {
            Object storedTo = variableStorage.createRandom(to);
            if (storedTo instanceof String) return storedTo;
            if (storedTo instanceof KlayCredentials) return ((KlayCredentials) storedTo).getAddress();
        }
        return to;
    }
}
