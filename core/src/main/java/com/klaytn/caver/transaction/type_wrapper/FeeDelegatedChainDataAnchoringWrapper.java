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

package com.klaytn.caver.transaction.type_wrapper;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.type.FeeDelegatedChainDataAnchoring;

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
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
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