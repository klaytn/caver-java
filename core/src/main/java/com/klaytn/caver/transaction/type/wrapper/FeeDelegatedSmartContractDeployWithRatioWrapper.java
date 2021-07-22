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
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractDeployWithRatio;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedSmartContractDeployWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedSmartContractDeployWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedSmartContractDeployWithRatio`
 */
public class FeeDelegatedSmartContractDeployWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedSmartContractDeployWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedSmartContractDeployWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeployWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractDeployWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeployWithRatio string
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio create(String rlpEncoded) {
        FeeDelegatedSmartContractDeployWithRatio feeDelegatedSmartContractDeployWithRatio = FeeDelegatedSmartContractDeployWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractDeployWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractDeployWithRatio;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeployWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractDeployWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeployWithRatio byte array.
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedSmartContractDeployWithRatio feeDelegatedSmartContractDeployWithRatio = FeeDelegatedSmartContractDeployWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractDeployWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractDeployWithRatio;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeployWithRatio instance using FeeDelegatedSmartContractDeployWithRatio.Builder
     * @param builder FeeDelegatedSmartContractDeployWithRatio.Builder
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio create(FeeDelegatedSmartContractDeployWithRatio.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedSmartContractDeployWithRatio.create(builder);
    }

    /**
     * Creates a FeeDelegatedSmartContractDeployWithRatio instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String to, String value, String input, boolean humanReadable, String codeFormat) {
        return new FeeDelegatedSmartContractDeployWithRatio(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, to, value, input, humanReadable, codeFormat);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeployWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeployWithRatio string.
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio decode(String rlpEncoded) {
        return FeeDelegatedSmartContractDeployWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeployWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeployWithRatio byte array.
     * @return FeeDelegatedSmartContractDeployWithRatio
     */
    public FeeDelegatedSmartContractDeployWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedSmartContractDeployWithRatio.decode(rlpEncoded);
    }
}