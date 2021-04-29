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
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransferMemoWithRatio;

/**
 * Represents a FeeDelegatedValueTransferMemoWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedValueTransferMemoWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedValueTransferMemoWithRatio`
 */
public class FeeDelegatedValueTransferMemoWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a FeeDelegatedValueTransferMemoWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedValueTransferMemoWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemoWithRatio instance derived from a RLP-encoded FeeDelegatedValueTransferMemoWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemoWithRatio string
     * @return FeeDelegatedValueTransferMemoWithRatio
     */
    public FeeDelegatedValueTransferMemoWithRatio create(String rlpEncoded) {
        FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = FeeDelegatedValueTransferMemoWithRatio.decode(rlpEncoded);
        feeDelegatedValueTransferMemoWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferMemoWithRatio;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemoWithRatio instance derived from a RLP-encoded FeeDelegatedValueTransferMemoWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemoWithRatio byte array.
     * @return FeeDelegatedValueTransferMemoWithRatio
     */
    public FeeDelegatedValueTransferMemoWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedValueTransferMemoWithRatio feeDelegatedValueTransferMemoWithRatio = FeeDelegatedValueTransferMemoWithRatio.decode(rlpEncoded);
        feeDelegatedValueTransferMemoWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedValueTransferMemoWithRatio;
    }

    /**
     * Creates a FeeDelegatedValueTransferMemoWithRatio instance using FeeDelegatedValueTransferMemoWithRatio.Builder
     * @param builder FeeDelegatedValueTransferMemoWithRatio.Builder
     * @return FeeDelegatedValueTransferMemoWithRatio
     */
    public FeeDelegatedValueTransferMemoWithRatio create(FeeDelegatedValueTransferMemoWithRatio.Builder builder) {
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemoWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemoWithRatio string.
     * @return FeeDelegatedValueTransferMemoWithRatio
     */
    public FeeDelegatedValueTransferMemoWithRatio decode(String rlpEncoded) {
        return FeeDelegatedValueTransferMemoWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedValueTransferMemoWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedValueTransferMemoWithRatio byte array.
     * @return FeeDelegatedValueTransferMemoWithRatio
     */
    public FeeDelegatedValueTransferMemoWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedValueTransferMemoWithRatio.decode(rlpEncoded);
    }
}