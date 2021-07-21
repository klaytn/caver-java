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
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedValueTransferWrapper
 * 1. This class wraps all of static methods of FeeDelegatedValueTransfer
 * 2. This class should be accessed via `caver.transaction.feeDelegatedValueTransfer`
 */
public class FeeDelegatedValueTransferWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedValueTransferWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedValueTransferWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedValueTransfer instance derived from a RLP-encoded FeeDelegatedValueTransfer string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransfer string
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer create(String rlpEncoded) {
        FeeDelegatedValueTransfer feeDelegatedValueTransfer = FeeDelegatedValueTransfer.decode(rlpEncoded);
        feeDelegatedValueTransfer.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransfer;
    }

    /**
     * Creates a FeeDelegatedValueTransfer instance derived from a RLP-encoded FeeDelegatedValueTransfer byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransfer byte array.
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer create(byte[] rlpEncoded) {
        FeeDelegatedValueTransfer feeDelegatedValueTransfer = FeeDelegatedValueTransfer.decode(rlpEncoded);
        feeDelegatedValueTransfer.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransfer;
    }

    /**
     * Creates a FeeDelegatedValueTransfer instance using FeeDelegatedValueTransfer.Builder
     * @param builder FeeDelegatedValueTransfer.Builder
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer create(FeeDelegatedValueTransfer.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedValueTransfer.create(builder);
    }

    /**
     * Creates a FeeDelegatedValueTransfer instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A sender signature list.
     * @param feePayer A fee payer address
     * @param feePayerSignatures A fee payer signature list
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value) {
        return FeeDelegatedValueTransfer.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, to, value);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransfer string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransfer string
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer decode(String rlpEncoded) {
        return FeeDelegatedValueTransfer.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransfer byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransfer byte array.
     * @return FeeDelegatedValueTransfer
     */
    public FeeDelegatedValueTransfer decode(byte[] rlpEncoded) {
        return FeeDelegatedValueTransfer.decode(rlpEncoded);
    }
}