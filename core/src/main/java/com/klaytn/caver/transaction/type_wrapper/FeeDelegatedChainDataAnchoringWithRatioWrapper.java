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
import com.klaytn.caver.transaction.type.FeeDelegatedChainDataAnchoringWithRatio;

/**
 * Represents a FeeDelegatedChainDataAnchoringWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedChainDataAnchoringWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedChainDataAnchoringWithRatio`
 */
public class FeeDelegatedChainDataAnchoringWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a FeeDelegatedChainDataAnchoringWithRatioWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedChainDataAnchoringWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance derived from a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public FeeDelegatedChainDataAnchoringWithRatio create(String rlpEncoded) {
        FeeDelegatedChainDataAnchoringWithRatio feeDelegatedChainDataAnchoringWithRatio = FeeDelegatedChainDataAnchoringWithRatio.decode(rlpEncoded);
        feeDelegatedChainDataAnchoringWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedChainDataAnchoringWithRatio;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance derived from a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public FeeDelegatedChainDataAnchoringWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedChainDataAnchoringWithRatio feeDelegatedChainDataAnchoringWithRatio = FeeDelegatedChainDataAnchoringWithRatio.decode(rlpEncoded);
        feeDelegatedChainDataAnchoringWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedChainDataAnchoringWithRatio;
    }

    /**
     * Creates a FeeDelegatedChainDataAnchoringWithRatio instance using FeeDelegatedChainDataAnchoringWithRatio.Builder
     *
     * @param builder FeeDelegatedChainDataAnchoringWithRatio.Builder
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public FeeDelegatedChainDataAnchoringWithRatio create(FeeDelegatedChainDataAnchoringWithRatio.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio string.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public FeeDelegatedChainDataAnchoringWithRatio decode(String rlpEncoded) {
        return FeeDelegatedChainDataAnchoringWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedChainDataAnchoringWithRatio byte array.
     * @return FeeDelegatedChainDataAnchoringWithRatio
     */
    public FeeDelegatedChainDataAnchoringWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedChainDataAnchoringWithRatio.decode(rlpEncoded);
    }
}