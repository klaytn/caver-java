/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.tx.model;

import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.utils.Numeric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KlayRawTransaction {
    private byte[] value;
    private Set<KlaySignatureData> signatureData;

    public KlayRawTransaction(byte[] value, Set<KlaySignatureData> signatureData) {
        this.value = value;
        this.signatureData = signatureData;
    }

    public KlayRawTransaction(byte[] value, KlaySignatureData signatureData) {
        this.value = value;
        this.signatureData = new HashSet<>(Arrays.asList(signatureData));
    }

    public byte[] getValue() {
        return value;
    }

    public KlaySignatureData getSignatureData() {
        try {
            return signatureData.iterator().next();
        } catch (Exception e) {
            throw new RuntimeException("Called without signature data");
        }
    }

    public String getValueAsString() {
        return Numeric.toHexString(value);
    }
}
