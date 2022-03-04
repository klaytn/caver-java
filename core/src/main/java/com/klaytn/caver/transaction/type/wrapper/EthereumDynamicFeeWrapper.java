/*
 * Copyright 2022 The caver-java Authors
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
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.transaction.type.EthereumDynamicFee;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a EthereumDynamicFeeWrapper
 * 1. This class wraps all of static methods of EthereumDynamicFee
 * 2. This class should be accessed via `caver.transaction.ethereumAccessList`
 */
public class EthereumDynamicFeeWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a EthereumDynamicFeeWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public EthereumDynamicFeeWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a EthereumDynamicFee instance derived from a RLP-encoded EthereumDynamicFee string.
     * @param rlpEncoded RLP-encoded EthereumDynamicFee string
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee create(String rlpEncoded) {
        EthereumDynamicFee legacyTransaction = EthereumDynamicFee.decode(rlpEncoded);
        legacyTransaction.setKlaytnCall(this.klaytnCall);
        return legacyTransaction;
    }

    /**
     * Creates a EthereumDynamicFee instance derived from a RLP-encoded EthereumDynamicFee byte array.
     * @param rlpEncoded RLP-encoded EthereumDynamicFee byte array.
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee create(byte[] rlpEncoded) {
        EthereumDynamicFee legacyTransaction = EthereumDynamicFee.decode(rlpEncoded);
        legacyTransaction.setKlaytnCall(this.klaytnCall);
        return legacyTransaction;
    }

    /**
     * Creates a EthereumDynamicFee instance using EthereumDynamicFee.Builder
     * @param builder EthereumDynamicFee.Builder
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee create(EthereumDynamicFee.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return EthereumDynamicFee.create(builder);
    }

    /**
     * Create a EthereumDynamicFee instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param maxPriorityFeePerGas Max prirotiy fee per gas.
     * @param maxFeePerGas Max fee per gas.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param input Data attached to the transaction, used for transaction execution.
     * @param value The amount of KLAY in peb to be transferred.
     * @param accessList The EIP-2930 access list.
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee create(String from, String nonce, String gas, String maxPriorityFeePerGas, String maxFeePerGas, String chainId, List<SignatureData> signatures, String to, String input, String value, AccessList accessList) {
        return EthereumDynamicFee.create(this.klaytnCall, from, nonce, gas, maxPriorityFeePerGas, maxFeePerGas, chainId, signatures, to, input, value, accessList);
    }

    /**
     * Decodes a RLP-encoded EthereumDynamicFee string.
     * @param rlpEncoded RLP-encoded EthereumDynamicFee string
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee decode(String rlpEncoded) {
        return EthereumDynamicFee.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded EthereumDynamicFee byte array.
     * @param rlpEncoded RLP-encoded EthereumDynamicFee byte array.
     * @return EthereumDynamicFee
     */
    public EthereumDynamicFee decode(byte[] rlpEncoded) {
        return EthereumDynamicFee.decode(rlpEncoded);
    }
}