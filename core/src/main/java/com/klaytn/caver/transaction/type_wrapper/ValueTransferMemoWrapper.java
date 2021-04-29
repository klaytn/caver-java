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
import com.klaytn.caver.transaction.type.ValueTransferMemo;

/**
 * Represents a ValueTransferMemoWrapper
 * 1. This class wraps all of static methods of ValueTransferMemo
 * 2. This class should be accessed via `caver.transaction.valueTransferMemo`
 */
public class ValueTransferMemoWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a ValueTransferMemoWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public ValueTransferMemoWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a ValueTransferMemo instance derived from a RLP-encoded ValueTransferMemo string.
     * @param rlpEncoded RLP-encoded ValueTransferMemo string
     * @return ValueTransferMemo
     */
    public ValueTransferMemo create(String rlpEncoded) {
        ValueTransferMemo valueTransferMemo = ValueTransferMemo.decode(rlpEncoded);
        valueTransferMemo.setKlaytnCall(this.klaytnCall);
        return valueTransferMemo;
    }

    /**
     * Creates a ValueTransferMemo instance derived from a RLP-encoded ValueTransferMemo byte array.
     * @param rlpEncoded RLP-encoded ValueTransferMemo byte array.
     * @return ValueTransferMemo
     */
    public ValueTransferMemo create(byte[] rlpEncoded) {
        ValueTransferMemo valueTransferMemo = ValueTransferMemo.decode(rlpEncoded);
        valueTransferMemo.setKlaytnCall(this.klaytnCall);
        return valueTransferMemo;
    }

    /**
     * Creates a ValueTransferMemo instance using ValueTransferMemo.Builder
     * @param builder ValueTransferMemo.Builder
     * @return ValueTransferMemo
     */
    public ValueTransferMemo create(ValueTransferMemo.Builder builder) {
        return builder
                .setKlaytnCall(this.klaytnCall)
                .build();
    }

    /**
     * Decodes a RLP-encoded ValueTransferMemo string.
     * @param rlpEncoded RLP-encoded ValueTransferMemo string
     * @return ValueTransferMemo
     */
    public ValueTransferMemo decode(String rlpEncoded) {
        return ValueTransferMemo.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded ValueTransferMemo byte array.
     * @param rlpEncoded RLP-encoded ValueTransferMemo byte array.
     * @return ValueTransferMemo
     */
    public ValueTransferMemo decode(byte[] rlpEncoded) {
        return ValueTransferMemo.decode(rlpEncoded);
    }
}