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

import com.klaytn.caver.transaction.type.*;

/**
 * Represents a txPropertyBuilder which contains static methods returning builders for all types of transaction
 */
public class TxPropertyBuilder {
    /**
     * Creates a Builder of LegacyTransaction
     * @return LegacyTransaction.Builder
     */
    public static LegacyTransaction.Builder legacyTransaction() {
        return new LegacyTransaction.Builder();
    }

    /**
     * Creates a Builder of EthereumAccessList
     * @return EthereumAccessList.Builder
     */
    public static EthereumAccessList.Builder ethereumAccessList() {
        return new EthereumAccessList.Builder();
    }

    /**
     * Creates a Builder of ValueTransfer
     * @return ValueTransfer.Builder
     */
    public static ValueTransfer.Builder valueTransfer() {
        return new ValueTransfer.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedValueTransfer
     * @return FeeDelegatedValueTransfer.Builder
     */
    public static FeeDelegatedValueTransfer.Builder feeDelegatedValueTransfer() {
        return new FeeDelegatedValueTransfer.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedValueTransferWithRatio
     * @return FeeDelegatedValueTransferWithRatio.Builder
     */
    public static FeeDelegatedValueTransferWithRatio.Builder feeDelegatedValueTransferWithRatio() {
        return new FeeDelegatedValueTransferWithRatio.Builder();
    }

    /**
     * Creates a Builder of ValueTransferMemo
     * @return ValueTransferMemo.Builder
     */
    public static ValueTransferMemo.Builder valueTransferMemo() {
        return new ValueTransferMemo.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedValueTransferMemo
     * @return FeeDelegatedValueTransferMemo.Builder
     */
    public static FeeDelegatedValueTransferMemo.Builder feeDelegatedValueTransferMemo() {
        return new FeeDelegatedValueTransferMemo.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedValueTransferMemoWithRatio
     * @return FeeDelegatedValueTransferMemoWithRatio.Builder
     */
    public static FeeDelegatedValueTransferMemoWithRatio.Builder feeDelegatedValueTransferMemoWithRatio() {
        return new FeeDelegatedValueTransferMemoWithRatio.Builder();
    }

    /**
     * Creates a Builder of AccountUpdate
     * @return AccountUpdate.Builder
     */
    public static AccountUpdate.Builder accountUpdate() {
        return new AccountUpdate.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedAccountUpdate
     * @return FeeDelegatedAccountUpdate.Builder
     */
    public static FeeDelegatedAccountUpdate.Builder feeDelegatedAccountUpdate() {
        return new FeeDelegatedAccountUpdate.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedAccountUpdateWithRatio
     * @return FeeDelegatedAccountUpdateWithRatio.Builder
     */
    public static FeeDelegatedAccountUpdateWithRatio.Builder feeDelegatedAccountUpdateWithRatio() {
        return new FeeDelegatedAccountUpdateWithRatio.Builder();
    }

    /**
     * Creates a Builder of SmartContractDeploy
     * @return SmartContractDeploy.Builder
     */
    public static SmartContractDeploy.Builder smartContractDeploy() {
        return new SmartContractDeploy.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedSmartContractDeploy
     * @return FeeDelegatedSmartContractDeploy.Builder
     */
    public static FeeDelegatedSmartContractDeploy.Builder feeDelegatedSmartContractDeploy() {
        return new FeeDelegatedSmartContractDeploy.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedSmartContractDeployWithRatio
     * @return FeeDelegatedSmartContractDeployWithRatio.Builder
     */
    public static FeeDelegatedSmartContractDeployWithRatio.Builder feeDelegatedSmartContractDeployWithRatio() {
        return new FeeDelegatedSmartContractDeployWithRatio.Builder();
    }

    /**
     * Creates a Builder of SmartContractExecution
     * @return SmartContractExecution.Builder
     */
    public static SmartContractExecution.Builder smartContractExecution() {
        return new SmartContractExecution.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedSmartContractExecution
     * @return FeeDelegatedSmartContractExecution.Builder
     */
    public static FeeDelegatedSmartContractExecution.Builder feeDelegatedSmartContractExecution() {
        return new FeeDelegatedSmartContractExecution.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedSmartContractExecutionWithRatio
     * @return FeeDelegatedSmartContractExecutionWithRatio.Builder
     */
    public static FeeDelegatedSmartContractExecutionWithRatio.Builder feeDelegatedSmartContractExecutionWithRatio() {
        return new FeeDelegatedSmartContractExecutionWithRatio.Builder();
    }

    /**
     * Creates a Builder of Cancel
     * @return Cancel.Builder
     */
    public static Cancel.Builder cancel() {
        return new Cancel.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedCancel
     * @return FeeDelegatedCancel.Builder
     */
    public static FeeDelegatedCancel.Builder feeDelegatedCancel() {
        return new FeeDelegatedCancel.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedCancelWithRatio
     * @return FeeDelegatedCancelWithRatio.Builder
     */
    public static FeeDelegatedCancelWithRatio.Builder feeDelegatedCancelWithRatio() {
        return new FeeDelegatedCancelWithRatio.Builder();
    }

    /**
     * Creates a Builder of ChainDataAnchoring
     * @return ChainDataAnchoring.Builder
     */
    public static ChainDataAnchoring.Builder chainDataAnchoring() {
        return new ChainDataAnchoring.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedChainDataAnchoring
     * @return FeeDelegatedChainDataAnchoring.Builder
     */
    public static FeeDelegatedChainDataAnchoring.Builder feeDelegatedChainDataAnchoring() {
        return new FeeDelegatedChainDataAnchoring.Builder();
    }

    /**
     * Creates a Builder of FeeDelegatedChainDataAnchoringWithRatio
     * @return FeeDelegatedChainDataAnchoringWithRatio.Builder
     */
    public static FeeDelegatedChainDataAnchoringWithRatio.Builder feeDelegatedChainDataAnchoringWithRatio() {
        return new FeeDelegatedChainDataAnchoringWithRatio.Builder();
    }
}