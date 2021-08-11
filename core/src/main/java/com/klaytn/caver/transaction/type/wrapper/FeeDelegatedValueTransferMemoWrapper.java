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
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferMemo;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedValueTransferMemoWrapper
 * 1. This class wraps all of static methods of FeeDelegatedValueTransferMemo
 * 2. This class should be accessed via `caver.transaction.feeDelegatedValueTransferMemo`
 */
public class FeeDelegatedValueTransferMemoWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedValueTransferMemoWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedValueTransferMemoWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemo instance derived from a RLP-encoded FeeDelegatedValueTransferMemo string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo string
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo create(String rlpEncoded) {
        FeeDelegatedValueTransferMemo feeDelegatedValueTransferMemo = FeeDelegatedValueTransferMemo.decode(rlpEncoded);
        feeDelegatedValueTransferMemo.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferMemo;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemo instance derived from a RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo create(byte[] rlpEncoded) {
        FeeDelegatedValueTransferMemo feeDelegatedValueTransferMemo = FeeDelegatedValueTransferMemo.decode(rlpEncoded);
        feeDelegatedValueTransferMemo.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferMemo;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemo instance using FeeDelegatedValueTransferMemo.Builder
     * @param builder FeeDelegatedValueTransferMemo.Builder
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo create(FeeDelegatedValueTransferMemo.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedValueTransferMemo.create(builder);
    }

    /**
     * Creates a FeeDelegatedValueTransferMemo instance.
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
     * @param input The message data attached to the transaction.
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value, String input) {
        return FeeDelegatedValueTransferMemo.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, to, value, input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemo string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo string.
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo decode(String rlpEncoded) {
        return FeeDelegatedValueTransferMemo.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemo byte array.
     * @return FeeDelegatedValueTransferMemo
     */
    public FeeDelegatedValueTransferMemo decode(byte[] rlpEncoded) {
        return FeeDelegatedValueTransferMemo.decode(rlpEncoded);
    }
}