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

import com.klaytn.caver.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.account.WeightedMultiSigOptions;

import java.util.List;

/**
 * Representing an AccountKeyWeightedMultiSigWrapper which wraps all of static methods of AccountKeyWeightedMultiSig
 */
public class AccountKeyWeightedMultiSigWrapper {
    /**
     * Creates an AccountKeyWeightedMultiSigWrapper instance.
     */
    public AccountKeyWeightedMultiSigWrapper() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyWeightedMultiSig string
     * @param rlpEncodedKey RLP-encoded AccountKeyWeightedMultiSig string.
     * @return AccountKeyWeightedMultiSig
     */
    public AccountKeyWeightedMultiSig decode(String rlpEncodedKey) {
        return AccountKeyWeightedMultiSig.decode(rlpEncodedKey);
    }


    /**
     * Decodes a RLP-encoded AccountKeyWeightedMultiSig byte array.
     * @param rlpEncodedKey RLP-encoded AccountKeyWeightedMultiSig byte array.
     * @return AccountKeyWeightedMultiSig
     */
    public AccountKeyWeightedMultiSig decode(byte[] rlpEncodedKey) {
        return AccountKeyWeightedMultiSig.decode(rlpEncodedKey);
    }

    /**
     * Create AccountKeyWeightedMultiSig instance form public Key array and WeightedMultiSigOption
     * @param publicKeyArr Array of public key string
     * @param options An options which defines threshold and weight.
     * @return AccountKeyWeightedMultiSig
     */
    public AccountKeyWeightedMultiSig fromPublicKeysAndOptions(String[] publicKeyArr, WeightedMultiSigOptions options) {
        return AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKeyArr, options);
    }

    /**
     * Returns an AccountKeyWeightedMultiSig's type attribute
     * @return AccountKeyWeightedMultiSig's type attribute
     */
    public String getType() {
        return AccountKeyWeightedMultiSig.getType();
    }
}
