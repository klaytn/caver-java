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
import com.klaytn.caver.transaction.type.Cancel;

/**
 * Represents a CancelWrapper
 * 1. This class wraps all of static methods of Cancel
 * 2. This class should be accessed via `caver.transaction.cancel`
 */
public class CancelWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a CancelWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public CancelWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a Cancel instance derived from a RLP-encoded Cancel string.
     * @param rlpEncoded RLP-encoded Cancel string
     * @return Cancel
     */
    public Cancel create(String rlpEncoded) {
        Cancel cancel = Cancel.decode(rlpEncoded);
        cancel.setKlaytnCall(this.klaytnCall);
        return cancel;
    }

    /**
     * Creates a Cancel instance derived from a RLP-encoded Cancel byte array.
     * @param rlpEncoded RLP-encoded Cancel byte array.
     * @return Cancel
     */
    public Cancel create(byte[] rlpEncoded) {
        Cancel cancel = Cancel.decode(rlpEncoded);
        cancel.setKlaytnCall(this.klaytnCall);
        return cancel;
    }

    /**
     * Creates a Cancel instance using Cancel.Builder
     * @param builder Cancel.Builder
     * @return Cancel
     */
    public Cancel create(Cancel.Builder builder) {
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
    }

    /**
     * Decodes a RLP-encoded Cancel string.
     * @param rlpEncoded RLP-encoded Cancel string
     * @return Cancel
     */
    public Cancel decode(String rlpEncoded) {
        return Cancel.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded Cancel byte array.
     * @param rlpEncoded RLP-encoded Cancel byte array.
     * @return Cancel
     */
    public Cancel decode(byte[] rlpEncoded) {
        return Cancel.decode(rlpEncoded);
    }
}