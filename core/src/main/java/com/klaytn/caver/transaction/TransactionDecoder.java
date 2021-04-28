/*
 * Copyright 2020 The caver-java Authors
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

import com.klaytn.caver.transaction.type.*;
import org.web3j.utils.Numeric;

public class TransactionDecoder {
    /**
     * Decodes a RLP-encoded transaction and return it with matching type of transaction
     *
     * @param rlpEncoded RLP-encoded transaction
     * @return AbstractTransaction
     */
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
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedValueTransfer.getType()) {
            return FeeDelegatedValueTransfer.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedValueTransferMemo.getType()) {
            return FeeDelegatedValueTransferMemo.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedSmartContractDeploy.getType()) {
            return FeeDelegatedSmartContractDeploy.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedSmartContractExecution.getType()){
            return FeeDelegatedSmartContractExecution.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedAccountUpdate.getType()) {
            return FeeDelegatedAccountUpdate.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedCancel.getType()) {
            return FeeDelegatedCancel.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedChainDataAnchoring.getType()) {
            return FeeDelegatedChainDataAnchoring.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedCancelWithRatio.getType()) {
            return FeeDelegatedCancelWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedChainDataAnchoringWithRatio.getType()) {
            return FeeDelegatedChainDataAnchoringWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.getType()) {
            return FeeDelegatedAccountUpdateWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedValueTransferWithRatio.getType()) {
            return FeeDelegatedValueTransferWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.getType()) {
            return FeeDelegatedSmartContractExecutionWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedValueTransferMemoWithRatio.getType()) {
            return FeeDelegatedValueTransferMemoWithRatio.decode(rlpBytes);
        } else if(rlpBytes[0] == TransactionType.TxTypeFeeDelegatedSmartContractDeployWithRatio.getType()) {
            return FeeDelegatedSmartContractDeployWithRatio.decode(rlpBytes);
        }
        else {
            return LegacyTransaction.decode(rlpBytes);
        }
    }
}
