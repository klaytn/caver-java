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
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecutionWithRatio;

/**
 * Represents a FeeDelegatedSmartContractExecutionWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedSmartContractExecutionWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedSmartContractExecutionWithRatio`
 */
public class FeeDelegatedSmartContractExecutionWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a FeeDelegatedSmartContractExecutionWithRatioWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedSmartContractExecutionWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(String rlpEncoded) {
        FeeDelegatedSmartContractExecutionWithRatio feeDelegatedSmartContractExecutionWithRatio = FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractExecutionWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecutionWithRatio;
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance derived from a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedSmartContractExecutionWithRatio feeDelegatedSmartContractExecutionWithRatio = FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
        feeDelegatedSmartContractExecutionWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractExecutionWithRatio;
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio string.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio decode(String rlpEncoded) {
        return FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractExecutionWithRatio byte array.
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedSmartContractExecutionWithRatio.decode(rlpEncoded);
    }

    /**
     * Creates a FeeDelegatedSmartContractExecutionWithRatio instance using FeeDelegatedSmartContractExecutionWithRatio.Builder
     *
     * @param builder FeeDelegatedSmartContractExecutionWithRatio.Builder
     * @return FeeDelegatedSmartContractExecutionWithRatio
     */
    public FeeDelegatedSmartContractExecutionWithRatio create(FeeDelegatedSmartContractExecutionWithRatio.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }
}