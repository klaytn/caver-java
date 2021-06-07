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

import com.klaytn.caver.account.AccountKeyPublic;

/**
 * Representing an AccountKeyPublicWrapper which wraps all of static methods of AccountKeyPublic
 */
public class AccountKeyPublicWrapper {
    /**
     * Creates an AccountKeyPublicWrapper instance.
     */
    public AccountKeyPublicWrapper() {
    }

    /**
     * Creates AccountKeyPublic instance from Elliptic curve x, y coordinates.
     *
     * @param x The point x
     * @param y The point y
     * @return AccountKeyPublic
     */
    public AccountKeyPublic fromXYPoint(String x, String y) {
        return AccountKeyPublic.fromXYPoint(x, y);
    }

    /**
     * Creates AccountKeyPublic instance from ECC Public Key.
     *
     * @param publicKey The public key string. This public key can be in format of compressed or uncompressed.
     * @return AccountKeyPublic
     */
    public AccountKeyPublic fromPublicKey(String publicKey) {
        return AccountKeyPublic.fromPublicKey(publicKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic string
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic string.
     * @return AccountKeyPublic
     */
    public AccountKeyPublic decode(String rlpEncodedKey) {
        return AccountKeyPublic.decode(rlpEncodedKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic byte array
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic byte array
     * @return AccountKeyPublic
     */
    public AccountKeyPublic decode(byte[] rlpEncodedKey) {
        return AccountKeyPublic.decode(rlpEncodedKey);
    }

    /**
     * Returns an AccountKeyPublic's type attribute
     *
     * @return AccountKeyPublic's type attribute
     */
    public String getType() {
        return AccountKeyPublic.getType();
    }
}
