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

import com.klaytn.caver.crpyto.KlaySignatureData;
import org.web3j.utils.Numeric;

public class KlayRawTransaction {
    private byte[] value;
    private KlaySignatureData signatureData;

    public KlayRawTransaction(byte[] value, KlaySignatureData signatureData) {
        this.value = value;
        this.signatureData = signatureData;
    }

    public byte[] getValue() {
        return value;
    }

    public KlaySignatureData getSignatureData() {
        return signatureData;
    }

    public String getValueAsString() {
        return Numeric.toHexString(value);
    }
}
