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
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractDeploy;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedSmartContractDeployWrapper
 * 1. This class wraps all of static methods of FeeDelegatedSmartContractDeploy
 * 2. This class should be accessed via `caver.transaction.feeDelegatedSmartContractDeploy`
 */
public class FeeDelegatedSmartContractDeployWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedSmartContractDeployWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedSmartContractDeployWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance derived from a RLP-encoded FeeDelegatedSmartContractDeploy string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy string
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy create(String rlpEncoded) {
        FeeDelegatedSmartContractDeploy feeDelegatedSmartContractDeploy = FeeDelegatedSmartContractDeploy.decode(rlpEncoded);
        feeDelegatedSmartContractDeploy.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractDeploy;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance derived from a RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy create(byte[] rlpEncoded) {
        FeeDelegatedSmartContractDeploy feeDelegatedSmartContractDeploy = FeeDelegatedSmartContractDeploy.decode(rlpEncoded);
        feeDelegatedSmartContractDeploy.setKlaytnCall(this.klaytnCall);
        return feeDelegatedSmartContractDeploy;
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance using FeeDelegatedSmartContractDeploy.Builder
     * @param builder FeeDelegatedSmartContractDeploy.Builder
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy create(FeeDelegatedSmartContractDeploy.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedSmartContractDeploy.create(builder);
    }

    /**
     * Creates a FeeDelegatedSmartContractDeploy instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The data attached to the transaction, used for transaction execution.
     * @param humanReadable Is human-readable address.
     * @param codeFormat The code format of smart contract code
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String to, String value, String input, boolean humanReadable, String codeFormat) {
        return FeeDelegatedSmartContractDeploy.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, to, value, input, humanReadable, codeFormat);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeploy string.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy string.
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy decode(String rlpEncoded) {
        return FeeDelegatedSmartContractDeploy.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedSmartContractDeploy byte array.
     * @return FeeDelegatedSmartContractDeploy
     */
    public FeeDelegatedSmartContractDeploy decode(byte[] rlpEncoded) {
        return FeeDelegatedSmartContractDeploy.decode(rlpEncoded);
    }
}