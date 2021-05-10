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

import com.klaytn.caver.account.AccountKeyLegacy;

/**
 * Representing an AccountKeyLegacyWrapper which wraps all of static methods of AccountKeyLegacy
 */
public class AccountKeyLegacyWrapper {
    /**
     * Creates an AccountKeyLegacyWrapper instance.
     */
    public AccountKeyLegacyWrapper() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyLegacy string
     * @param rlpEncodedKey A RLP-encoded AccountKeyLegacy string
     * @return AccountKeyLegacy
     */
    public AccountKeyLegacy decode(String rlpEncodedKey) {
        return AccountKeyLegacy.decode(rlpEncodedKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyLegacy byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyLegacy byte array
     * @return AccountKeyLegacy
     */
    public AccountKeyLegacy decode(byte[] rlpEncodedKey) {
        return AccountKeyLegacy.decode(rlpEncodedKey);
    }

    /**
     * Returns an AccountKeyLegacy's type attribute
     * @return AccountKeyLegacy's type attribute
     */
    public String getType() {
        return AccountKeyLegacy.getType();
    }
}
