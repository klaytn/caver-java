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

package com.klaytn.caver.utils;

import com.klaytn.caver.tx.type.TxType;
import org.web3j.utils.Numeric;

import java.util.Arrays;

/**
 * @deprecated This class is deprecated since caver-java:1.5.0
 */
@Deprecated
public class KlayTransactionUtils {
    
    public static TxType.Type getType(byte[] rawTransaction) {
        return TxType.Type.findByValue(rawTransaction[0]);
    }

    public static TxType.Type getType(String rawTransaction) {
        return getType(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }

    public static byte[] getRawTransactionNoType(byte[] rawTransaction) {
        return Arrays.copyOfRange(rawTransaction, 1, rawTransaction.length);
    }
}
