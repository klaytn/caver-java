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

package com.klaytn.caver.account.wrapper;

import com.klaytn.caver.account.AccountKeyFail;

/**
 * Representing an AccountKeyFailWrapper which wraps all of static methods of AccountKeyFail
 */
public class AccountKeyFailWrapper {
    /**
     * Creates an AccountKeyFailWrapper instance.
     */
    public AccountKeyFailWrapper() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail string
     * @param rlpEncodedKey RLP-encoded AccountKeyFail string
     * @return AccountKeyFail
     */
    public AccountKeyFail decode(String rlpEncodedKey) {
        return AccountKeyFail.decode(rlpEncodedKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyFail byte array
     * @return AccountKeyFail
     */
    public AccountKeyFail decode(byte[] rlpEncodedKey) {
        return AccountKeyFail.decode(rlpEncodedKey);
    }

    /**
     * Returns an AccountKeyFail's type attribute
     * @return AccountKeyFail's type attribute
     */
    public String getType() {
        return AccountKeyFail.getType();
    }
}
