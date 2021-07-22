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
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

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
     * Creates a SmartContractDeployWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public SmartContractDeployWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a SmartContractDeploy instance derived from a RLP-encoded SmartContractDeploy string.
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
     * @param builder SmartContractDeploy.Builder
     * @return SmartContractDeploy
     */
    public SmartContractDeploy create(SmartContractDeploy.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return SmartContractDeploy.create(builder);
    }

    /**
     * Creates a SmartContractDeploy instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The message data attached to the transaction.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     * @return SmartContractDeploy
     */
    public SmartContractDeploy create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String value, String input, boolean humanReadable, String codeFormat) {
        return SmartContractDeploy.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, to, value, input, humanReadable, codeFormat);
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy string.
     * @param rlpEncoded RLP-encoded SmartContractDeploy string
     * @return SmartContractDeploy
     */
    public SmartContractDeploy decode(String rlpEncoded) {
        return SmartContractDeploy.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded SmartContractDeploy byte array.
     * @param rlpEncoded RLP-encoded SmartContractDeploy byte array.
     * @return SmartContractDeploy
     */
    public SmartContractDeploy decode(byte[] rlpEncoded) {
        return SmartContractDeploy.decode(rlpEncoded);
    }
}