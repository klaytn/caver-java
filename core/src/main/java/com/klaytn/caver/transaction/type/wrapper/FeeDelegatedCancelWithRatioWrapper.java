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
import com.klaytn.caver.transaction.type.FeeDelegatedCancelWithRatio;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedCancelWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedCancelWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedCancelWithRatio`
 */
public class FeeDelegatedCancelWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedCancelWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedCancelWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedCancelWithRatio instance derived from a RLP-encoded FeeDelegatedCancelWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancelWithRatio string
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio create(String rlpEncoded) {
        FeeDelegatedCancelWithRatio feeDelegatedCancelWithRatio = FeeDelegatedCancelWithRatio.decode(rlpEncoded);
        feeDelegatedCancelWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedCancelWithRatio;
    }

    /**
     * Creates a FeeDelegatedCancelWithRatio instance derived from a RLP-encoded FeeDelegatedCancelWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancelWithRatio byte array.
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedCancelWithRatio feeDelegatedCancelWithRatio = FeeDelegatedCancelWithRatio.decode(rlpEncoded);
        feeDelegatedCancelWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedCancelWithRatio;
    }

    /**
     * Creates a FeeDelegatedCancelWithRatio instance using FeeDelegatedCancelWithRatio.Builder
     * @param builder FeeDelegatedCancelWithRatio.Builder
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio create(FeeDelegatedCancelWithRatio.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedCancelWithRatio.create(builder);
    }

    /**
     * Creates a FeeDelegatedCancelWithRatio instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer The address of the fee payer.
     * @param feePayerSignatures The fee payers's signatures.
     * @param feeRatio A fee ratio of the fee payer.
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio) {
        return FeeDelegatedCancelWithRatio.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedCancelWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancelWithRatio string.
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio decode(String rlpEncoded) {
        return FeeDelegatedCancelWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedCancelWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancelWithRatio byte array.
     * @return FeeDelegatedCancelWithRatio
     */
    public FeeDelegatedCancelWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedCancelWithRatio.decode(rlpEncoded);
    }
}