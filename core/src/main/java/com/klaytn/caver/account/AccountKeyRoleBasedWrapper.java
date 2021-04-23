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

import java.util.List;

/**
 * Representing an AccountKeyRoleBasedWrapper which wraps all of static methods of AccountKeyRoleBased
 */
public class AccountKeyRoleBasedWrapper {
    /**
     * Creates an AccountKeyRoleBasedWrapper instance.
     */
    public AccountKeyRoleBasedWrapper() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyRoleBased string
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyRoleBased string.
     * @return AccountKeyRoleBased
     */
    public AccountKeyRoleBased decode(String rlpEncodedKey) {
        return AccountKeyRoleBased.decode(rlpEncodedKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyRoleBased byte array
     *
     * @param rlpEncodedKey RLP-encoded AccountKeyRoleBased byte array
     * @return AccountKeyRoleBased
     */
    public AccountKeyRoleBased decode(byte[] rlpEncodedKey) {
        return AccountKeyRoleBased.decode(rlpEncodedKey);
    }


    public AccountKeyRoleBased fromRoleBasedPublicKeysAndOptions(List<String[]> pubArray, List<WeightedMultiSigOptions> options) {
        return AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(pubArray, options);
    }

    /**
     * Returns a AccountKeyRoleBased's type attribute
     *
     * @return AccountKeyRoleBased's type attribute
     */
    public String getType() {
        return AccountKeyRoleBased.getType();
    }
}
