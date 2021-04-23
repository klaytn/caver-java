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

package com.klaytn.caver.account;

/**
 * Representing an AccountKeyNilWrapper which wraps all of static methods of AccountKeyNil
 */
public class AccountKeyNilWrapper {
    /**
     * Creates an AccountKeyNilWrapper instance.
     */
    public AccountKeyNilWrapper() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyNil string
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyNil string
     * @return AccountKeyNil
     */
    public AccountKeyNil decode(String rlpEncodedKey) {
        return AccountKeyNil.decode(rlpEncodedKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyNil byte array
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyNil byte array
     * @return AccountKeyNil
     */
    public AccountKeyNil decode(byte[] rlpEncodedKey) {
        return AccountKeyNil.decode(rlpEncodedKey);
    }

    /**
     * Return an AccountKeyNil's type attribute.
     *
     * @return AccountKeyNil's type attribute.
     */
    public String getType() {
        return AccountKeyNil.getType();
    }
}
