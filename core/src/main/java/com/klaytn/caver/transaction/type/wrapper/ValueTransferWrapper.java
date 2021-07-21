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
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransferMemo;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a ValueTransferWrapper
 * 1. This class wraps all of static methods of ValueTransfer
 * 2. This class should be accessed via `caver.transaction.valueTransfer`
 */
public class ValueTransferWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a ValueTransferWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public ValueTransferWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a ValueTransfer instance derived from a RLP-encoded ValueTransfer string.
     * @param rlpEncoded RLP-encoded ValueTransfer string
     * @return ValueTransfer
     */
    public ValueTransfer create(String rlpEncoded) {
        ValueTransfer valueTransfer = ValueTransfer.decode(rlpEncoded);
        valueTransfer.setKlaytnCall(this.klaytnCall);
        return valueTransfer;
    }

    /**
     * Creates a ValueTransfer instance derived from a RLP-encoded ValueTransfer byte array.
     * @param rlpEncoded RLP-encoded ValueTransfer byte array.
     * @return ValueTransfer
     */
    public ValueTransfer create(byte[] rlpEncoded) {
        ValueTransfer valueTransfer = ValueTransfer.decode(rlpEncoded);
        valueTransfer.setKlaytnCall(this.klaytnCall);
        return valueTransfer;
    }

    /**
     * Creates a ValueTransfer instance using ValueTransfer.Builder
     * @param builder ValueTransfer.Builder
     * @return ValueTransfer
     */
    public ValueTransfer create(ValueTransfer.Builder builder) {
        builder.setKlaytnCall(klaytnCall);
        return ValueTransfer.create(builder);
    }

    /**
     * Creates a ValueTransferMemo instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param to The account address that will receive the transferred value.
     * @param value The amount of KLAY in peb to be transferred.
     * @param input The message data attached to the transaction.
     * @return ValueTransferMemo
     */
    public ValueTransferMemo create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String value, String input) {
        return ValueTransferMemo.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, to, value, input);
    }

    /**
     * Decodes a RLP-encoded ValueTransfer string.
     * @param rlpEncoded RLP-encoded ValueTransfer string
     * @return ValueTransfer
     */
    public ValueTransfer decode(String rlpEncoded) {
        return ValueTransfer.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded ValueTransfer byte array.
     * @param rlpEncoded RLP-encoded ValueTransfer byte array.
     * @return ValueTransfer
     */
    public ValueTransfer decode(byte[] rlpEncoded) {
        return ValueTransfer.decode(rlpEncoded);
    }
}