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

package com.klaytn.caver.transaction.type;

public enum TransactionType {
    TxTypeLegacyTransaction(0x00),

    TxTypeValueTransfer(0x08),
    TxTypeFeeDelegatedValueTransfer(0x09),
    TxTypeFeeDelegatedValueTransferWithRatio(0x0a),

    TxTypeValueTransferMemo(0x10),
    TxTypeFeeDelegatedValueTransferMemo(0x11),
    TxTypeFeeDelegatedValueTransferMemoWithRatio(0x12),

    TxTypeAccountUpdate(0x20),
    TxTypeFeeDelegatedAccountUpdate(0x21),
    TxTypeFeeDelegatedAccountUpdateWithRatio(0x22),

    TxTypeSmartContractDeploy(0x28),
    TxTypeFeeDelegatedSmartContractDeploy(0x29),
    TxTypeFeeDelegatedSmartContractDeployWithRatio(0x2a),

    TxTypeSmartContractExecution(0x30),
    TxTypeFeeDelegatedSmartContractExecution(0x31),
    TxTypeFeeDelegatedSmartContractExecutionWithRatio(0x32),

    TxTypeCancel(0x38),
    TxTypeFeeDelegatedCancel(0x39),
    TxTypeFeeDelegatedCancelWithRatio(0x3a),

    TxTypeChainDataAnchoring(0x48),
    TxTypeFeeDelegatedChainDataAnchoring(0x49),
    TxTypeFeeDelegatedChainDataAnchoringWithRatio(0x4a);

    int type;
    
    TransactionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
