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

import com.klaytn.caver.account.Account;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdateWithRatio;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents a FeeDelegatedAccountUpdateWithRatioWrapper
 * 1. This class wraps all of static methods of FeeDelegatedAccountUpdateWithRatio
 * 2. This class should be accessed via `caver.transaction.feeDelegatedAccountUpdateWithRatio`
 */
public class FeeDelegatedAccountUpdateWithRatioWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatioWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public FeeDelegatedAccountUpdateWithRatioWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance derived from a RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio string
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(String rlpEncoded) {
        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdateWithRatio = FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
        feeDelegatedAccountUpdateWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdateWithRatio;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance derived from a RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(byte[] rlpEncoded) {
        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdateWithRatio = FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
        feeDelegatedAccountUpdateWithRatio.setKlaytnCall(this.klaytnCall);
        return feeDelegatedAccountUpdateWithRatio;
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance using FeeDelegatedAccountUpdateWithRatio.Builder
     * @param builder FeeDelegatedAccountUpdateWithRatio.Builder
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(FeeDelegatedAccountUpdateWithRatio.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return FeeDelegatedAccountUpdateWithRatio.create(builder);
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param account An account instance includes account key to be updated to the account in the network.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, Account account) {
        return FeeDelegatedAccountUpdateWithRatio.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, account);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio decode(String rlpEncoded) {
        return FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public FeeDelegatedAccountUpdateWithRatio decode(byte[] rlpEncoded) {
        return FeeDelegatedAccountUpdateWithRatio.decode(rlpEncoded);
    }
}