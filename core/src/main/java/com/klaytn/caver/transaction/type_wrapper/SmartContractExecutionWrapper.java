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
import com.klaytn.caver.transaction.type.SmartContractExecution;

/**
 * Represents a SmartContractExecutionWrapper
 * 1. This class wraps all of static methods of SmartContractExecution
 * 2. This class should be accessed via `caver.transaction.smartContractExecution`
 */
public class SmartContractExecutionWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a SmartContractExecutionWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public SmartContractExecutionWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a SmartContractExecution instance derived from a RLP-encoded SmartContractExecution string.
     *
     * @param rlpEncoded RLP-encoded SmartContractExecution string
     * @return SmartContractExecution
     */
    public SmartContractExecution create(String rlpEncoded) {
        SmartContractExecution smartContractExecution = SmartContractExecution.decode(rlpEncoded);
        smartContractExecution.setKlaytnCall(this.klaytnCall);
        return smartContractExecution;
    }

    /**
     * Creates a SmartContractExecution instance derived from a RLP-encoded SmartContractExecution byte array.
     *
     * @param rlpEncoded RLP-encoded SmartContractExecution byte array.
     * @return SmartContractExecution
     */
    public SmartContractExecution create(byte[] rlpEncoded) {
        SmartContractExecution smartContractExecution = SmartContractExecution.decode(rlpEncoded);
        smartContractExecution.setKlaytnCall(this.klaytnCall);
        return smartContractExecution;
    }

    /**
     * Creates a SmartContractExecution instance using SmartContractExecution.Builder
     *
     * @param builder SmartContractExecution.Builder
     * @return SmartContractExecution
     */
    public SmartContractExecution create(SmartContractExecution.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }

    /**
     * Decodes a RLP-encoded SmartContractExecution string.
     * @param rlpEncoded RLP-encoded SmartContractExecution string
     * @return SmartContractExecution
     */
    public SmartContractExecution decode(String rlpEncoded) {
        return SmartContractExecution.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded SmartContractExecution byte array.
     * @param rlpEncoded RLP-encoded SmartContractExecution byte array.
     * @return SmartContractExecution
     */
    public SmartContractExecution decode(byte[] rlpEncoded) {
        return SmartContractExecution.decode(rlpEncoded);
    }
}
