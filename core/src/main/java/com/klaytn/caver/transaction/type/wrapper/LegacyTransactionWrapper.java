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
import com.klaytn.caver.transaction.type.LegacyTransaction;

/**
 * Represents a LegacyTransactionWrapper
 * 1. This class wraps all of static methods of LegacyTransaction
 * 2. This class should be accessed via `caver.transaction.legacyTransaction`
 */
public class LegacyTransactionWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a LegacyTransactionWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public LegacyTransactionWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a LegacyTransaction instance derived from a RLP-encoded LegacyTransaction string.
     * @param rlpEncoded RLP-encoded LegacyTransaction string
     * @return LegacyTransaction
     */
    public LegacyTransaction create(String rlpEncoded) {
        LegacyTransaction legacyTransaction = LegacyTransaction.decode(rlpEncoded);
        legacyTransaction.setKlaytnCall(this.klaytnCall);
        return legacyTransaction;
    }

    /**
     * Creates a LegacyTransaction instance derived from a RLP-encoded LegacyTransaction byte array.
     * @param rlpEncoded RLP-encoded LegacyTransaction byte array.
     * @return LegacyTransaction
     */
    public LegacyTransaction create(byte[] rlpEncoded) {
        LegacyTransaction legacyTransaction = LegacyTransaction.decode(rlpEncoded);
        legacyTransaction.setKlaytnCall(this.klaytnCall);
        return legacyTransaction;
    }

    /**
     * Creates a LegacyTransaction instance using LegacyTransaction.Builder
     * @param builder LegacyTransaction.Builder
     * @return LegacyTransaction
     */
    public LegacyTransaction create(LegacyTransaction.Builder builder) {
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction string.
     * @param rlpEncoded RLP-encoded LegacyTransaction string
     * @return LegacyTransaction
     */
    public LegacyTransaction decode(String rlpEncoded) {
        return LegacyTransaction.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded LegacyTransaction byte array.
     * @param rlpEncoded RLP-encoded LegacyTransaction byte array.
     * @return LegacyTransaction
     */
    public LegacyTransaction decode(byte[] rlpEncoded) {
        return LegacyTransaction.decode(rlpEncoded);
    }
}