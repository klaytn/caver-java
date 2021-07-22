/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.common.transaction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Transaction;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHelper;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.*;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class TransactionHelperTest {
    public static class ConvertToCaverTransaction {
        static Caver caver;
        static Map<String, Transaction.TransactionData> sampleTxData;

        public void checkTxObject(Transaction.TransactionData expected, AbstractTransaction actual) {
            assertEquals(expected.getType(), actual.getType());
            assertEquals(expected.getFrom(), actual.getFrom());
            assertEquals(expected.getGas(), actual.getGas());
            assertEquals(expected.getNonce(), actual.getNonce());
            assertEquals(expected.getGasPrice(), actual.getGasPrice());
            assertEquals(expected.getHash(), actual.getTransactionHash());
        }

        @BeforeClass
        public static void init() throws IOException {
            caver = new Caver(Caver.DEFAULT_URL);

            ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
            sampleTxData = mapper.readValue(new File("src/test/resources/TransactionSample.json"), new TypeReference<Map<String, Transaction.TransactionData>>(){});
        }

        @Test
        public void legacyTransaction() {
            Transaction.TransactionData txObject = sampleTxData.get("legacyTransaction");
            LegacyTransaction legacyTransaction = (LegacyTransaction)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, legacyTransaction);
        }

        @Test
        public void valueTransfer() {
            Transaction.TransactionData txObject = sampleTxData.get("valueTransfer");
            ValueTransfer valueTransfer = (ValueTransfer)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, valueTransfer);

            txObject = sampleTxData.get("feeDelegatedValueTransfer");
            FeeDelegatedValueTransfer fdValueTransfer = (FeeDelegatedValueTransfer)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdValueTransfer);

            txObject = sampleTxData.get("feeDelegatedValueTransferWithRatio");
            FeeDelegatedValueTransferWithRatio fdValueTransferWithRatio = (FeeDelegatedValueTransferWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdValueTransferWithRatio);
        }

        @Test
        public void valueTransferMemo() {
            Transaction.TransactionData txObject = sampleTxData.get("valueTransferMemo");
            ValueTransferMemo valueTransferMemo = (ValueTransferMemo)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, valueTransferMemo);

            txObject = sampleTxData.get("feeDelegatedValueTransferMemo");
            FeeDelegatedValueTransferMemo fdValueTransferMemo = (FeeDelegatedValueTransferMemo)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdValueTransferMemo);

            txObject = sampleTxData.get("feeDelegatedValueTransferMemoWithRatio");
            FeeDelegatedValueTransferMemoWithRatio fdValueTransferMemoWithRatio = (FeeDelegatedValueTransferMemoWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdValueTransferMemoWithRatio);
        }

        @Test
        public void smartContractDeploy() {
            Transaction.TransactionData txObject = sampleTxData.get("smartContractDeploy");
            SmartContractDeploy smartContractDeploy = (SmartContractDeploy)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, smartContractDeploy);

            txObject = sampleTxData.get("feeDelegatedSmartContractDeploy");
            FeeDelegatedSmartContractDeploy fdSmartContractDeploy = (FeeDelegatedSmartContractDeploy)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdSmartContractDeploy);

            txObject = sampleTxData.get("feeDelegatedSmartContractDeployWithRatio");
            FeeDelegatedSmartContractDeployWithRatio fdSmartContractDeployWithRatio = (FeeDelegatedSmartContractDeployWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdSmartContractDeployWithRatio);
        }

        @Test
        public void smartContractExecution() {
            Transaction.TransactionData txObject = sampleTxData.get("smartContractExecution");
            SmartContractExecution smartContractExecution = (SmartContractExecution)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, smartContractExecution);

            txObject = sampleTxData.get("feeDelegatedSmartContractExecution");
            FeeDelegatedSmartContractExecution fdSmartContractExecution = (FeeDelegatedSmartContractExecution)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdSmartContractExecution);

            txObject = sampleTxData.get("feeDelegatedSmartContractExecutionWithRatio");
            FeeDelegatedSmartContractExecutionWithRatio fdSmartContractExecutionWithRatio = (FeeDelegatedSmartContractExecutionWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdSmartContractExecutionWithRatio);
        }

        @Test
        public void accountUpdate() {
            Transaction.TransactionData txObject = sampleTxData.get("accountUpdate");
            AccountUpdate accountUpdate = (AccountUpdate)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, accountUpdate);

            txObject = sampleTxData.get("feeDelegatedAccountUpdate");
            FeeDelegatedAccountUpdate feeDelegatedAccountUpdate = (FeeDelegatedAccountUpdate)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, feeDelegatedAccountUpdate);

            txObject = sampleTxData.get("feeDelegatedAccountUpdateWithRatio");
            FeeDelegatedAccountUpdateWithRatio fdSmartContractExecutionWithRatio = (FeeDelegatedAccountUpdateWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdSmartContractExecutionWithRatio);
        }

        @Test
        public void chainDataAnchoring() {
            Transaction.TransactionData txObject = sampleTxData.get("chainDataAnchoring");
            ChainDataAnchoring chainDataAnchoring = (ChainDataAnchoring)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, chainDataAnchoring);

            txObject = sampleTxData.get("feeDelegatedChainDataAnchoring");
            FeeDelegatedChainDataAnchoring fdChainDataAnchoring = (FeeDelegatedChainDataAnchoring)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdChainDataAnchoring);

            txObject = sampleTxData.get("feeDelegatedChainDataAnchoringWithRatio");
            FeeDelegatedChainDataAnchoringWithRatio fdChainDataAnchoringWithRatio = (FeeDelegatedChainDataAnchoringWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdChainDataAnchoringWithRatio);
        }

        @Test
        public void cancel() {
            Transaction.TransactionData txObject = sampleTxData.get("cancel");
            Cancel cancel = (Cancel)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, cancel);

            txObject = sampleTxData.get("feeDelegatedCancel");
            FeeDelegatedCancel fdCancel = (FeeDelegatedCancel)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdCancel);

            txObject = sampleTxData.get("feeDelegatedCancelWithRatio");
            FeeDelegatedCancelWithRatio fdCancelWithRatio = (FeeDelegatedCancelWithRatio)txObject.convertToCaverTransaction(caver.rpc.klay);
            checkTxObject(txObject, fdCancelWithRatio);
        }
    }

    public static class getTransactionByHash {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver = new Caver(Caver.DEFAULT_URL);
        static TransactionReceipt.TransactionReceiptData vtReceipt;

        private static TransactionReceipt.TransactionReceiptData sendKlay() throws IOException, TransactionException {
            AbstractKeyring keyring = caver.wallet.add(KeyringFactory.createFromPrivateKey("0x871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5"));

            BigInteger value = new BigInteger(Utils.convertToPeb(BigDecimal.ONE, "KLAY"));

            //Create a value transfer transaction
            ValueTransfer valueTransfer = new ValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setFrom(keyring.getAddress())
                    .setTo("0x8084fed6b1847448c24692470fc3b2ed87f9eb47")
                    .setValue(value)
                    .setGas(BigInteger.valueOf(25000))
                    .build();

            //Sign to the transaction
            valueTransfer.sign(keyring);

            //Send a transaction to the klaytn blockchain platform (Klaytn)
            Bytes32 result = caver.rpc.klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
            if(result.hasError()) {
                throw new RuntimeException(result.getError().getMessage());
            }

            //Check transaction receipt.
            TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
            TransactionReceipt.TransactionReceiptData transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(result.getResult());

            return transactionReceipt;
        }

        @BeforeClass
        public static void init() throws TransactionException, IOException {
            vtReceipt = sendKlay();
        }

        @Test
        public void getTransactionByHash() {
            AbstractTransaction vt = caver.transaction.getTransactionByHash(vtReceipt.getTransactionHash());
            assertTrue(vt instanceof ValueTransfer);
            assertEquals(vtReceipt.getTransactionHash(), vt.getTransactionHash());
        }

        @Test
        public void getTransactionByHash_notExistedHash() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to get transaction from Klaytn with ");

            String hash = "0x1111111111111111111111111111111111111111111111111111111111111111";
             caver.transaction.getTransactionByHash(hash);
        }
    }
}
