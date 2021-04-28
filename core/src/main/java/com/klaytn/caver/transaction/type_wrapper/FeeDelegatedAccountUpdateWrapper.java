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
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdate;

/**
 * Represents a FeeDelegatedAccountUpdateWrapper
 * 1. This class wraps all of static methods of FeeDelegatedAccountUpdate
 * 2. This class should be accessed via `caver.transaction.feeDelegatedAccountUpdate`
 */
public class FeeDelegatedAccountUpdateWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a FeeDelegatedAccountUpdateWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedAccountUpdateWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedAccountUpdate instance derived from a RLP-encoded FeeDelegatedAccountUpdate string.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate string
     * @return FeeDelegatedAccountUpdate
     */
    public FeeDelegatedAccountUpdate create(String rlpEncoded) {
        FeeDelegatedAccountUpdate feeDelegatedAccountUpdate = FeeDelegatedAccountUpdate.decode(rlpEncoded);
        feeDelegatedAccountUpdate.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdate;
    }

    /**
     * Creates a FeeDelegatedAccountUpdate instance derived from a RLP-encoded FeeDelegatedAccountUpdate byte array.
     *
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate byte array.
     * @return FeeDelegatedAccountUpdate
     */
    public FeeDelegatedAccountUpdate create(byte[] rlpEncoded) {
        FeeDelegatedAccountUpdate feeDelegatedAccountUpdate = FeeDelegatedAccountUpdate.decode(rlpEncoded);
        feeDelegatedAccountUpdate.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdate;
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdate string.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate string.
     * @return FeeDelegatedAccountUpdate
     */
    public FeeDelegatedAccountUpdate decode(String rlpEncoded) {
        return FeeDelegatedAccountUpdate.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdate byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate byte array.
     * @return FeeDelegatedAccountUpdate
     */
    public FeeDelegatedAccountUpdate decode(byte[] rlpEncoded) {
        return FeeDelegatedAccountUpdate.decode(rlpEncoded);
    }

    /**
     * Creates a FeeDelegatedAccountUpdate instance using FeeDelegatedAccountUpdate.Builder
     *
     * @param builder FeeDelegatedAccountUpdate.Builder
     * @return FeeDelegatedAccountUpdate
     */
    public FeeDelegatedAccountUpdate create(FeeDelegatedAccountUpdate.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }
}
