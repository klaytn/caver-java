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
import com.klaytn.caver.transaction.type.SmartContractDeploy;

/**
 * Represents a SmartContractDeployWrapper
 * 1. This class wraps all of static methods of SmartContractDeploy
 * 2. This class should be accessed via `caver.transaction.smartContractDeploy`
 */
public class SmartContractDeployWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a SmartContractDeployWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public SmartContractDeployWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a SmartContractDeploy instance derived from a RLP-encoded SmartContractDeploy string.
     *
     * @param rlpEncoded RLP-encoded SmartContractDeploy string
     * @return SmartContractDeploy
     */
    public SmartContractDeploy create(String rlpEncoded) {
        SmartContractDeploy smartContractDeploy = SmartContractDeploy.decode(rlpEncoded);
        smartContractDeploy.setKlaytnCall(this.klaytnCall);
        return smartContractDeploy;
    }

    /**
     * Creates a SmartContractDeploy instance derived from a RLP-encoded SmartContractDeploy byte array.
     *
     * @param rlpEncoded RLP-encoded SmartContractDeploy byte array.
     * @return SmartContractDeploy
     */
    public SmartContractDeploy create(byte[] rlpEncoded) {
        SmartContractDeploy smartContractDeploy = SmartContractDeploy.decode(rlpEncoded);
        smartContractDeploy.setKlaytnCall(this.klaytnCall);
        return smartContractDeploy;
    }

    /**
     * Creates a SmartContractDeploy instance using SmartContractDeploy.Builder
     *
     * @param builder SmartContractDeploy.Builder
     * @return SmartContractDeploy
     */
    public SmartContractDeploy create(SmartContractDeploy.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy string.
     *
     * @param rlpEncoded RLP-encoded SmartContractDeploy string
     * @return SmartContractDeploy
     */
    public SmartContractDeploy decode(String rlpEncoded) {
        return SmartContractDeploy.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy byte array.
     *
     * @param rlpEncoded RLP-encoded SmartContractDeploy byte array.
     * @return SmartContractDeploy
     */
    public SmartContractDeploy decode(byte[] rlpEncoded) {
        return SmartContractDeploy.decode(rlpEncoded);
    }
}