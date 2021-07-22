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
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.util.List;

/**
 * Represents an AccountUpdateWrapper
 * 1. This class wraps all of static methods of AccountUpdate
 * 2. This class should be accessed via `caver.transaction.accountUpdate
 */
public class AccountUpdateWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Creates an AccountUpdateWrapper instance.
     * @param klaytnCall Klay RPC instance
     */
    public AccountUpdateWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates an AccountUpdate instance derived from a RLP-encoded AccountUpdate string.
     * @param rlpEncoded RLP-encoded AccountUpdate string
     * @return AccountUpdate
     */
    public AccountUpdate create(String rlpEncoded) {
        AccountUpdate accountUpdate = AccountUpdate.decode(rlpEncoded);
        accountUpdate.setKlaytnCall(this.klaytnCall);
        return accountUpdate;
    }

    /**
     * Creates an AccountUpdate instance derived from a RLP-encoded AccountUpdate byte array.
     * @param rlpEncoded RLP-encoded AccountUpdate byte array.
     * @return AccountUpdate
     */
    public AccountUpdate create(byte[] rlpEncoded) {
        AccountUpdate accountUpdate = AccountUpdate.decode(rlpEncoded);
        accountUpdate.setKlaytnCall(this.klaytnCall);
        return accountUpdate;
    }

    /**
     * Creates an AccountUpdate instance using AccountUpdate.Builder
     * @param builder AccountUpdate.Builder
     * @return AccountUpdate
     */
    public AccountUpdate create(AccountUpdate.Builder builder) {
        builder.setKlaytnCall(this.klaytnCall);
        return AccountUpdate.create(builder);
    }

    /**
     * Creates an AccountUpdate instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param account The Account instance contains AccountKey to be updated to the account.
     * @return AccountUpdate
     */
    public AccountUpdate create(String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, Account account) {
        return AccountUpdate.create(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, account);
    }

    /**
     * Decodes a RLP-encoded AccountUpdate string.
     * @param rlpEncoded RLP-encoded AccountUpdate string.
     * @return AccountUpdate
     */
    public AccountUpdate decode(String rlpEncoded) {
        return AccountUpdate.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded AccountUpdate byte array.
     * @param rlpEncoded RLP-encoded AccountUpdate byte array.
     * @return AccountUpdate
     */
    public AccountUpdate decode(byte[] rlpEncoded) {
        return AccountUpdate.decode(rlpEncoded);
    }
}