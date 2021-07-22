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

package com.klaytn.caver.transaction.type.wrapper;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecutionWithRatio;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedSmartContractExecutionWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedSmartContractExecutionWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedSmartContractExecutionWithRatio`
 */
public class FeeDelegatedSmartContractExecutionWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedSmartContractExecutionWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(String rlpEncoded) {
        FeeDelegatedSmartContractExecutionWithRatio feeDelegatedSmartContractExecutionWithRatio = FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractExecutionWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecutionWithRatio;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedSmartContractExecutionWithRatio feeDelegatedSmartContractExecutionWithRatio = FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractExecutionWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecutionWithRatio;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance using FeeDelegatedSmartContractExecutionWithRatio.Builder
     * @param builder FeeDelegatedSmartContractExecutionWithRatio.Builder
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(FeeDelegatedSmartContractExecutionWithRatio.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedSmartContractExecutionWithRatio.create(builder);
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID.
     * @param signatures A sender signature list.
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String to, String value, String input) {
        return FeeDelegatedSmartContractExecutionWithRatio.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, to, value, input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio decode(String rlpEncoded) {
        return FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
    }
}