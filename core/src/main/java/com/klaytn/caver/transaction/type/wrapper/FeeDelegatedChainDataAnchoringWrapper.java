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
import com.klaytn.caver.transaction.type.FeeDelegatedChainDataAnchoring;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedChainDataAnchoringWrapper
 * 1. This class wraps all of static methods of FeeDelegatedChainDataAnchoring
 * 2. This class should be accessed via `caver.transaction.feeDelegatedChainDataAnchoring`
 */
public class FeeDelegatedChainDataAnchoringWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedChainDataAnchoringWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedChainDataAnchoringWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance derived from a RLP-encoded FeeDelegatedChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring string
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring create(String rlpEncoded) {
        FeeDelegatedChainDataAnchoring feeDelegatedChainDataAnchoring = FeeDelegatedChainDataAnchoring.decode(rlpEncoded);
        feeDelegatedChainDataAnchoring.setKlaytnCall(this.klaytnCall);
        return feeDelegatedChainDataAnchoring;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance derived from a RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring create(byte[] rlpEncoded) {
        FeeDelegatedChainDataAnchoring feeDelegatedChainDataAnchoring = FeeDelegatedChainDataAnchoring.decode(rlpEncoded);
        feeDelegatedChainDataAnchoring.setKlaytnCall(this.klaytnCall);
        return feeDelegatedChainDataAnchoring;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance using FeeDelegatedChainDataAnchoring.Builder
     * @param builder FeeDelegatedChainDataAnchoring.Builder
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring create(FeeDelegatedChainDataAnchoring.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedChainDataAnchoring.create(builder);
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoring instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param input The data of the service chain.
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String input) {
        return FeeDelegatedChainDataAnchoring.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, input);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring string.
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring decode(String rlpEncoded) {
        return FeeDelegatedChainDataAnchoring.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoring byte array.
     * @return FeeDelegatedChainDataAnchoring
     */
    public FeeDelegatedChainDataAnchoring decode(byte[] rlpEncoded) {
        return FeeDelegatedChainDataAnchoring.decode(rlpEncoded);
    }
}