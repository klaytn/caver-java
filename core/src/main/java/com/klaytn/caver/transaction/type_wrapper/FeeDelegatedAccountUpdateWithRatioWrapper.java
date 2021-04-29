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
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdateWithRatio;

/**
 * Represents a FeeDelegatedAccountUpdateWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedAccountUpdateWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedAccountUpdateWithRatio`
 */
public class FeeDelegatedAccountUpdateWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a FeeDelegatedAccountUpdateWithRatioWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedAccountUpdateWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance derived from a RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio string
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(String rlpEncoded) {
        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdateWithRatio = FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
        feeDelegatedAccountUpdateWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdateWithRatio;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance derived from a RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdateWithRatio = FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
        feeDelegatedAccountUpdateWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdateWithRatio;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance using FeeDelegatedAccountUpdateWithRatio.Builder
     *
     * @param builder FeeDelegatedAccountUpdateWithRatio.Builder
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(FeeDelegatedAccountUpdateWithRatio.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio decode(String rlpEncoded) {
        return FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
    }
}