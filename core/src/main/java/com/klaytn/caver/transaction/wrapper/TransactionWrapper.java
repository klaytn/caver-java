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

package com.klaytn.caver.transaction.wrapper;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.transaction.TransactionHelper;
import com.klaytn.caver.transaction.accessList.wrapper.AccessListWrapper;
import com.klaytn.caver.transaction.type.wrapper.*;

import java.util.List;

/**
 * Represents a TransactionWrapper
 * 1. This class contains all types of transaction wrapper classes as member variables
 * 2. This class should be accessed via `caver.transaction`
 */
public class TransactionWrapper {

    private Klay klay;

    /**
     * LegacyTransactionWrapper instance
     */
    public LegacyTransactionWrapper legacyTransaction;

    /**
     * EthereumAccessListWrapper instance
     */
    public EthereumAccessListWrapper ethereumAccessList;

    /**
     * ValueTransferWrapper instance
     */
    public ValueTransferWrapper valueTransfer;

    /**
     * FeeDelegatedValueTransferWrapper instance
     */
    public FeeDelegatedValueTransferWrapper feeDelegatedValueTransfer;

    /**
     * FeeDelegatedValueTransferWithRatioWrapper instance
     */
    public FeeDelegatedValueTransferWithRatioWrapper feeDelegatedValueTransferWithRatio;

    /**
     * ValueTransferMemoWrapper instance
     */
    public ValueTransferMemoWrapper valueTransferMemo;

    /**
     * FeeDelegatedValueTransferMemoWrapper instance
     */
    public FeeDelegatedValueTransferMemoWrapper feeDelegatedValueTransferMemo;

    /**
     * FeeDelegatedValueTransferMemoWithRatioWrapper instance
     */
    public FeeDelegatedValueTransferMemoWithRatioWrapper feeDelegatedValueTransferMemoWithRatio;

    /**
     * AccountUpdateWrapper instance
     */
    public AccountUpdateWrapper accountUpdate;

    /**
     * FeeDelegatedAccountUpdateWrapper instance
     */
    public FeeDelegatedAccountUpdateWrapper feeDelegatedAccountUpdate;

    /**
     * FeeDelegatedAccountUpdateWithRatioWrapper instance
     */
    public FeeDelegatedAccountUpdateWithRatioWrapper feeDelegatedAccountUpdateWithRatio;

    /**
     * SmartContractDeployWrapper instance
     */
    public SmartContractDeployWrapper smartContractDeploy;

    /**
     * FeeDelegatedSmartContractDeployWrapper instance
     */
    public FeeDelegatedSmartContractDeployWrapper feeDelegatedSmartContractDeploy;

    /**
     * FeeDelegatedSmartContractDeployWithRatioWrapper instance
     */
    public FeeDelegatedSmartContractDeployWithRatioWrapper feeDelegatedSmartContractDeployWithRatio;

    /**
     * SmartContractExecutionWrapper instance
     */
    public SmartContractExecutionWrapper smartContractExecution;

    /**
     * FeeDelegatedSmartContractExecutionWrapper instance
     */
    public FeeDelegatedSmartContractExecutionWrapper feeDelegatedSmartContractExecution;

    /**
     * FeeDelegatedSmartContractExecutionWithRatioWrapper instance
     */
    public FeeDelegatedSmartContractExecutionWithRatioWrapper feeDelegatedSmartContractExecutionWithRatio;

    /**
     * CancelWrapper instance
     */
    public CancelWrapper cancel;

    /**
     * FeeDelegatedCancelWrapper instance
     */
    public FeeDelegatedCancelWrapper feeDelegatedCancel;

    /**
     * FeeDelegatedCancelWithRatioWrapper instance
     */
    public FeeDelegatedCancelWithRatioWrapper feeDelegatedCancelWithRatio;

    /**
     * ChainDataAnchoringWrapper instance
     */
    public ChainDataAnchoringWrapper chainDataAnchoring;

    /**
     * FeeDelegatedChainDataAnchoringWrapper instance
     */
    public FeeDelegatedChainDataAnchoringWrapper feeDelegatedChainDataAnchoring;

    /**
     * FeeDelegatedChainDataAnchoringWithRatioWrapper instance
     */
    public FeeDelegatedChainDataAnchoringWithRatioWrapper feeDelegatedChainDataAnchoringWithRatio;

    /**
     * AccessListWrapper instance
     */
    public AccessListWrapper accessList;

    /**
     * Creates a Transaction instance
     * @param klaytnCall Klay RPC instance
     */
    public TransactionWrapper(Klay klaytnCall) {
        this.klay = klaytnCall;

        this.legacyTransaction = new LegacyTransactionWrapper(klaytnCall);
        this.ethereumAccessList = new EthereumAccessListWrapper(klaytnCall);

        this.valueTransfer = new ValueTransferWrapper(klaytnCall);
        this.feeDelegatedValueTransfer = new FeeDelegatedValueTransferWrapper(klaytnCall);
        this.feeDelegatedValueTransferWithRatio = new FeeDelegatedValueTransferWithRatioWrapper(klaytnCall);

        this.valueTransferMemo = new ValueTransferMemoWrapper(klaytnCall);
        this.feeDelegatedValueTransferMemo = new FeeDelegatedValueTransferMemoWrapper(klaytnCall);
        this.feeDelegatedValueTransferMemoWithRatio = new FeeDelegatedValueTransferMemoWithRatioWrapper(klaytnCall);

        this.accountUpdate = new AccountUpdateWrapper(klaytnCall);
        this.feeDelegatedAccountUpdate = new FeeDelegatedAccountUpdateWrapper(klaytnCall);
        this.feeDelegatedAccountUpdateWithRatio = new FeeDelegatedAccountUpdateWithRatioWrapper(klaytnCall);

        this.smartContractDeploy = new SmartContractDeployWrapper(klaytnCall);
        this.feeDelegatedSmartContractDeploy = new FeeDelegatedSmartContractDeployWrapper(klaytnCall);
        this.feeDelegatedSmartContractDeployWithRatio = new FeeDelegatedSmartContractDeployWithRatioWrapper(klaytnCall);

        this.smartContractExecution = new SmartContractExecutionWrapper(klaytnCall);
        this.feeDelegatedSmartContractExecution = new FeeDelegatedSmartContractExecutionWrapper(klaytnCall);
        this.feeDelegatedSmartContractExecutionWithRatio = new FeeDelegatedSmartContractExecutionWithRatioWrapper(klaytnCall);

        this.cancel = new CancelWrapper(klaytnCall);
        this.feeDelegatedCancel = new FeeDelegatedCancelWrapper(klaytnCall);
        this.feeDelegatedCancelWithRatio = new FeeDelegatedCancelWithRatioWrapper(klaytnCall);

        this.chainDataAnchoring = new ChainDataAnchoringWrapper(klaytnCall);
        this.feeDelegatedChainDataAnchoring = new FeeDelegatedChainDataAnchoringWrapper(klaytnCall);
        this.feeDelegatedChainDataAnchoringWithRatio = new FeeDelegatedChainDataAnchoringWithRatioWrapper(klaytnCall);

        this.accessList = new AccessListWrapper();
    }

    /**
     * Decodes a RLP-encoded transaction and returns it with matching type of transaction
     * @param rlpEncoded RLP-encoded transaction
     * @return AbstractTransaction
     */
    public AbstractTransaction decode(String rlpEncoded) {
        return TransactionDecoder.decode(rlpEncoded);
    }

    /**
     * Query transaction from Klaytn and converts to a caver transaction instance.
     * <pre>Example :
     * {@code
     * AbstractTransaction tx = caver.transaction.getTransactionByHash("0x{txHash}");
     * }
     * </pre>
     * @param transactionHash The transaction hash string to query from Klaytn.
     * @return AbstractTransaction
     */
    public AbstractTransaction getTransactionByHash(String transactionHash) {
        return TransactionHelper.getTransactionByHash(klay, transactionHash);
    }

    /**
     * Recovers the public keys from "signatures" filed in raw transaction string.<p>
     * If you want to derive an address from public key, please use {@link com.klaytn.caver.utils.Utils#publicKeyToAddress(String)}
     * <pre>Example :
     * {@code
     * List<String> publicKeys = caver.transaction.recoverPublicKeys("0x{RLP-encoded transaction string}");
     * }
     * </pre>
     * @param rawTx The RLP-encoded transaction string to recover public keys from "signatures".
     * @return List&lt;String&gt;
     */
    public List<String> recoverPublicKeys(String rawTx) {
        return TransactionHelper.recoverPublicKeys(rawTx);
    }

    /**
     * Recovers the public keys from "feePayerSignatures" filed in raw transaction string.<p>
     * If you want to derive an address from public key, please use {@link com.klaytn.caver.utils.Utils#publicKeyToAddress(String)}
     * <pre>Example :
     * {@code
     * List<String> publicKeys = caver.transaction.recoverFeePayerPublicKeys("0x{RLP-encoded transaction string}");
     * }
     * </pre>
     * @param rawTx The RLP-encoded transaction string to recover public keys from "signatures".
     * @return List&lt;String&gt;
     */
    public List<String> recoverFeePayerPublicKeys(String rawTx) {
        return TransactionHelper.recoverFeePayerPublicKeys(rawTx);
    }
}
