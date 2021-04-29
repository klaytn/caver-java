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
import com.klaytn.caver.transaction.type.AccountUpdate;

/**
 * Represents a AccountUpdateWrapper
 * 1. This class wraps all of static methods of AccountUpdate
 * 2. This class should be accessed via `caver.transaction.accountUpdate
 */
public class AccountUpdateWrapper {
    /**
     * Klay RPC instance
     */
    private Klay klaytnCall;

    /**
     * Create a AccountUpdateWrapper instance.
     *
     * @param klaytnCall Klay RPC instance
     */
    public AccountUpdateWrapper(Klay klaytnCall) {
        this.klaytnCall = klaytnCall;
    }

    /**
     * Creates a AccountUpdate instance derived from a RLP-encoded AccountUpdate string.
     *
     * @param rlpEncoded RLP-encoded AccountUpdate string
     * @return AccountUpdate
     */
    public AccountUpdate create(String rlpEncoded) {
        AccountUpdate accountUpdate = AccountUpdate.decode(rlpEncoded);
        accountUpdate.setKlaytnCall(this.klaytnCall);
        return accountUpdate;
    }

    /**
     * Creates a AccountUpdate instance derived from a RLP-encoded AccountUpdate byte array.
     *
     * @param rlpEncoded RLP-encoded AccountUpdate byte array.
     * @return AccountUpdate
     */
    public AccountUpdate create(byte[] rlpEncoded) {
        AccountUpdate accountUpdate = AccountUpdate.decode(rlpEncoded);
        accountUpdate.setKlaytnCall(this.klaytnCall);
        return accountUpdate;
    }

    /**
     * Creates a AccountUpdate instance using AccountUpdate.Builder
     *
     * @param builder AccountUpdate.Builder
     * @return AccountUpdate
     */
    public AccountUpdate create(AccountUpdate.Builder builder) {
        return builder
            .setKlaytnCall(this.klaytnCall)
            .build();
    }

    /**
     * Decodes a RLP-encoded AccountUpdate string.
     *
     * @param rlpEncoded RLP-encoded AccountUpdate string.
     * @return AccountUpdate
     */
    public AccountUpdate decode(String rlpEncoded) {
        return AccountUpdate.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded AccountUpdate byte array.
     *
     * @param rlpEncoded RLP-encoded AccountUpdate byte array.
     * @return AccountUpdate
     */
    public AccountUpdate decode(byte[] rlpEncoded) {
        return AccountUpdate.decode(rlpEncoded);
    }
}