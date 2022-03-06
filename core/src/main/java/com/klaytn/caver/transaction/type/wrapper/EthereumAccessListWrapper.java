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
import com.klaytn.caver.transaction.type.EthereumAccessList;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a EthereumAccessListWrapper
 * 1. This class wraps all of static methods of EthereumAccessList
 * 2. This class should be accessed via `caver.transaction.ethereumAccessList`
 */
public class EthereumAccessListWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a EthereumAccessListWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public EthereumAccessListWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a EthereumAccessList instance derived from a RLP-encoded EthereumAccessList string.
     * @param rlpEncoded RLP-encoded EthereumAccessList string
     * @return EthereumAccessList
     */
    public EthereumAccessList create(String rlpEncoded) {
        EthereumAccessList ethereumAccessList = EthereumAccessList.decode(rlpEncoded);
        ethereumAccessList.setKlaytnCall(this.klaytnCall);
        return ethereumAccessList;
    }

    /**
     * Creates a EthereumAccessList instance derived from a RLP-encoded EthereumAccessList byte array.
     * @param rlpEncoded RLP-encoded EthereumAccessList byte array.
     * @return EthereumAccessList
     */
    public EthereumAccessList create(byte[] rlpEncoded) {
        EthereumAccessList ethereumAccessList = EthereumAccessList.decode(rlpEncoded);
        ethereumAccessList.setKlaytnCall(this.klaytnCall);
        return ethereumAccessList;
    }

    /**
     * Creates a EthereumAccessList instance using EthereumAccessList.Builder
     * @param builder EthereumAccessList.Builder
     * @return EthereumAccessList
     */
    public EthereumAccessList create(EthereumAccessList.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return EthereumAccessList.create(builder);
    }

    /**
     * Create a EthereumAccessList instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param input Data attached to the transaction, used for transaction execution.
     * @param value The amount of KLAY in peb to be transferred.
     * @param accessList The EIP-2930 access list.
     * @return EthereumAccessList
     */
    public EthereumAccessList create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String input, String value, AccessList accessList) {
        return EthereumAccessList.create(this.klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, to, input, value, accessList);
    }

    /**
     * Decodes a RLP-encoded EthereumAccessList string.
     * @param rlpEncoded RLP-encoded EthereumAccessList string
     * @return EthereumAccessList
     */
    public EthereumAccessList decode(String rlpEncoded) {
        return EthereumAccessList.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded EthereumAccessList byte array.
     * @param rlpEncoded RLP-encoded EthereumAccessList byte array.
     * @return EthereumAccessList
     */
    public EthereumAccessList decode(byte[] rlpEncoded) {
        return EthereumAccessList.decode(rlpEncoded);
    }
}