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
import com.klaytn.caver.transaction.type.FeeDelegatedCancel;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedCancelWrapper
 * 1. This class wraps all of static methods of FeeDelegatedCancel
 * 2. This class should be accessed via `caver.transaction.feeDelegatedCancel`
 */
public class FeeDelegatedCancelWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedCancelWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedCancelWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedCancel instance derived from a RLP-encoded FeeDelegatedCancel string.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancel string
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel create(String rlpEncoded) {
        FeeDelegatedCancel feeDelegatedCancel = FeeDelegatedCancel.decode(rlpEncoded);
        feeDelegatedCancel.setKlaytnCall(this.klaytnCall);
        return feeDelegatedCancel;
    }

    /**
     * Creates a FeeDelegatedCancel instance derived from a RLP-encoded FeeDelegatedCancel byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancel byte array.
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel create(byte[] rlpEncoded) {
        FeeDelegatedCancel feeDelegatedCancel = FeeDelegatedCancel.decode(rlpEncoded);
        feeDelegatedCancel.setKlaytnCall(this.klaytnCall);
        return feeDelegatedCancel;
    }

    /**
     * Creates a FeeDelegatedCancel instance using FeeDelegatedCancel.Builder
     * @param builder FeeDelegatedCancel.Builder
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel create(FeeDelegatedCancel.Builder builder) {
       builder.setKlaytnCall(this.klaytnCall);
       return FeeDelegatedCancel.create(builder);
    }

    /**
     * Creates a FeeDelegatedCancel instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures) {
        return FeeDelegatedCancel.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedCancel string.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancel string.
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel decode(String rlpEncoded) {
        return FeeDelegatedCancel.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedCancel byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedCancel byte array.
     * @return FeeDelegatedCancel
     */
    public FeeDelegatedCancel decode(byte[] rlpEncoded) {
        return FeeDelegatedCancel.decode(rlpEncoded);
    }
}