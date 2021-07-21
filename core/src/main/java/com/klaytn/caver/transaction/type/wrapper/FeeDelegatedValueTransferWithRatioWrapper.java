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
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferWithRatio;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedValueTransferWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedValueTransferWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedValueTransferWithRatio`
 */
public class FeeDelegatedValueTransferWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedValueTransferWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedValueTransferWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance derived from a RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio string
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio create(String rlpEncoded) {
        FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = FeeDelegatedValueTransferWithRatio.decode(rlpEncoded);
        feeDelegatedValueTransferWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferWithRatio;
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance derived from a RLP-encoded FeeDelegatedValueTransferWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio byte array.
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedValueTransferWithRatio feeDelegatedValueTransferWithRatio = FeeDelegatedValueTransferWithRatio.decode(rlpEncoded);
        feeDelegatedValueTransferWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferWithRatio;
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance using FeeDelegatedValueTransferWithRatio.Builder
     * @param builder FeeDelegatedValueTransferWithRatio.Builder
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio create(FeeDelegatedValueTransferWithRatio.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedValueTransferWithRatio.create(builder);
    }

    /**
     * Creates a FeeDelegatedValueTransferWithRatio instance.
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
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, String to, String value) {
        return FeeDelegatedValueTransferWithRatio.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, to, value);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio string.
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio decode(String rlpEncoded) {
        return FeeDelegatedValueTransferWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferWithRatio byte array.
     * @return FeeDelegatedValueTransferWithRatio
     */
    public FeeDelegatedValueTransferWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedValueTransferWithRatio.decode(rlpEncoded);
    }
}