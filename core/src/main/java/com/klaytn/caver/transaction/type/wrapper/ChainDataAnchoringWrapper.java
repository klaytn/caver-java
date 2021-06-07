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
import com.klaytn.caver.transaction.type.ChainDataAnchoring;

/**
 * Represents a ChainDataAnchoringWrapper
 * 1. This class wraps all of static methods of ChainDataAnchoring
 * 2. This class should be accessed via `caver.transaction.chainDataAnchoring`
 */
public class ChainDataAnchoringWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a ChainDataAnchoringWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public ChainDataAnchoringWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a ChainDataAnchoring instance derived from a RLP-encoded ChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring string
     * @return ChainDataAnchoring
     */
    public ChainDataAnchoring create(String rlpEncoded) {
        ChainDataAnchoring chainDataAnchoring = ChainDataAnchoring.decode(rlpEncoded);
        chainDataAnchoring.setKlaytnCall(this.klaytnCall);
        return chainDataAnchoring;
    }

    /**
     * Creates a ChainDataAnchoring instance derived from a RLP-encoded ChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring byte array.
     * @return ChainDataAnchoring
     */
    public ChainDataAnchoring create(byte[] rlpEncoded) {
        ChainDataAnchoring chainDataAnchoring = ChainDataAnchoring.decode(rlpEncoded);
        chainDataAnchoring.setKlaytnCall(this.klaytnCall);
        return chainDataAnchoring;
    }

    /**
     * Creates a ChainDataAnchoring instance using ChainDataAnchoring.Builder
     * @param builder ChainDataAnchoring.Builder
     * @return ChainDataAnchoring
     */
    public ChainDataAnchoring create(ChainDataAnchoring.Builder builder) {
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
    }

    /**
     * Decodes a RLP-encoded ChainDataAnchoring string.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring string
     * @return ChainDataAnchoring
     */
    public ChainDataAnchoring decode(String rlpEncoded) {
        return ChainDataAnchoring.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded ChainDataAnchoring byte array.
     * @param rlpEncoded RLP-encoded ChainDataAnchoring byte array.
     * @return ChainDataAnchoring
     */
    public ChainDataAnchoring decode(byte[] rlpEncoded) {
        return ChainDataAnchoring.decode(rlpEncoded);
    }
}