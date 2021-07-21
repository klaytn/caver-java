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

package com.klaytn.caver.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Transaction;
import com.klaytn.caver.transaction.type.LegacyTransaction;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.transaction.type.ValueTransfer;

import java.io.IOException;

public class TransactionHelper {

    /**
     * Query transaction from Klaytn and converts to a caver transaction instance.
     * @param caver The Caver instance.
     * @param transactionHash The transaction hash string to query form Klaytn.
     * @return AbstractTransaction
     * @throws IOException
     */
    public static AbstractTransaction getTransactionByHash(Caver caver, String transactionHash) {
        try {
            Transaction response = caver.rpc.klay.getTransactionByHash(transactionHash).send();
            if(response.hasError()) {
                throw new RuntimeException("Failed to get transaction from Klaytn with " + transactionHash);
            }

            Transaction.TransactionData txObject = response.getResult();

            switch (TransactionType.valueOf(txObject.getType())) {
                case TxTypeLegacyTransaction:
                    return caver.transaction.legacyTransaction.create(txObject);
                case TxTypeValueTransfer:
                    return caver.transaction.valueTransfer.create(txObject);
                case TxTypeFeeDelegatedValueTransfer:
                    return caver.transaction.feeDelegatedValueTransfer.create(txObject);
                case TxTypeFeeDelegatedValueTransferWithRatio:
                    return caver.transaction.feeDelegatedValueTransferWithRatio.create(txObject);
                case TxTypeValueTransferMemo:
                    return caver.transaction.valueTransferMemo.create(txObject);
                case TxTypeFeeDelegatedValueTransferMemo:
                    return caver.transaction.feeDelegatedValueTransferMemo.create(txObject);
                case TxTypeFeeDelegatedValueTransferMemoWithRatio:
                    return caver.transaction.feeDelegatedValueTransferMemoWithRatio.create(txObject);
                case TxTypeAccountUpdate:
                    return caver.transaction.accountUpdate.create(txObject);
                case TxTypeFeeDelegatedAccountUpdate:
                    return caver.transaction.feeDelegatedAccountUpdate.create(txObject);
                case TxTypeFeeDelegatedAccountUpdateWithRatio:
                    return caver.transaction.feeDelegatedAccountUpdateWithRatio.create(txObject);
                case TxTypeSmartContractDeploy:
                    return caver.transaction.smartContractDeploy.create(txObject);
                case TxTypeFeeDelegatedSmartContractDeploy:
                    return caver.transaction.feeDelegatedSmartContractDeploy.create(txObject);
                case TxTypeFeeDelegatedSmartContractDeployWithRatio:
                    return caver.transaction.feeDelegatedSmartContractDeployWithRatio.create(txObject);
                case TxTypeSmartContractExecution:
                    return caver.transaction.smartContractExecution.create(txObject);
                case TxTypeFeeDelegatedSmartContractExecution:
                    return caver.transaction.feeDelegatedSmartContractExecution.create(txObject);
                case TxTypeFeeDelegatedSmartContractExecutionWithRatio:
                    return caver.transaction.feeDelegatedSmartContractExecutionWithRatio.create(txObject);
                case TxTypeCancel:
                    return caver.transaction.cancel.create(txObject);
                case TxTypeFeeDelegatedCancel:
                    return caver.transaction.feeDelegatedCancel.create(txObject);
                case TxTypeFeeDelegatedCancelWithRatio:
                    return caver.transaction.feeDelegatedCancelWithRatio.create(txObject);
                case TxTypeChainDataAnchoring:
                    return caver.transaction.chainDataAnchoring.create(txObject);
                case TxTypeFeeDelegatedChainDataAnchoring:
                    return caver.transaction.feeDelegatedChainDataAnchoring.create(txObject);
                case TxTypeFeeDelegatedChainDataAnchoringWithRatio:
                    return caver.transaction.feeDelegatedChainDataAnchoring.create(txObject);
                default:
                    throw new RuntimeException("Invalid transaction type : Cannot create a transaction instance that has Tx type :" + txObject.getType());
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed to get transaction from Klaytn with" + transactionHash, e);
        }
    }
}
