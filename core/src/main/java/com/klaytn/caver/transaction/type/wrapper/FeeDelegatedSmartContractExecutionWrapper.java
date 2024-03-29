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
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedSmartContractExecutionWrapper
 * 1. This class wraps all of static methods of FeeDelegatedSmartContractExecution
 * 2. This class should be accessed via `caver.transaction.feeDelegatedSmartContractExecution`
 */
public class FeeDelegatedSmartContractExecutionWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedSmartContractExecutionWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedSmartContractExecutionWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecution instance derived from a RLP-encoded FeeDelegatedSmartContractExecution string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecution string
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution create(String rlpEncoded) {
        FeeDelegatedSmartContractExecution feeDelegatedSmartContractExecution = FeeDelegatedSmartContractExecution.decode(rlpEncoded);
        feeDelegatedSmartContractExecution.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecution;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecution instance derived from a RLP-encoded FeeDelegatedSmartContractExecution byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecution byte array.
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution create(byte[] rlpEncoded) {
        FeeDelegatedSmartContractExecution feeDelegatedSmartContractExecution = FeeDelegatedSmartContractExecution.decode(rlpEncoded);
        feeDelegatedSmartContractExecution.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecution;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecution instance using FeeDelegatedSmartContractExecution.Builder
     * @param builder FeeDelegatedSmartContractExecution.Builder
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution create(FeeDelegatedSmartContractExecution.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedSmartContractExecution.create(builder);
    }

    /**
     * Creates a FeeDelegatedSmartContractExecution instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID.
     * @param signatures A sender signature list.
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value, String input) {
        return FeeDelegatedSmartContractExecution.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, to, value, input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecution string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecution string.
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution decode(String rlpEncoded) {
        return FeeDelegatedSmartContractExecution.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecution byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecution byte array.
     * @return FeeDelegatedSmartContractExecution
     */
    public FeeDelegatedSmartContractExecution decode(byte[] rlpEncoded) {
        return FeeDelegatedSmartContractExecution.decode(rlpEncoded);
    }
}