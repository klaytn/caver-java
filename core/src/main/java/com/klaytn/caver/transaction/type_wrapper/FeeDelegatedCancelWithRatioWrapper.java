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
import com.klaytn.caver.transaction.type.FeeDelegatedCancelWithRatio;

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
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
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