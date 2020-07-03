/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/crypto/src/main/java/org/web3j/crypto/TransactionEncoder.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.utils;

import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.crypto.Sign;

import java.math.BigInteger;


@Deprecated
public class KlaySignatureDataUtils {
    /**
     * @deprecated This method replaced by {@link com.klaytn.caver.wallet.keyring.SignatureData#makeEIP155Signature(int)}
     */
    public static KlaySignatureData createEip155KlaySignatureData(
            Sign.SignatureData signatureData, int chainId) {
        int v = (signatureData.getV() + chainId * 2) + 8;
        return new KlaySignatureData(BigInteger.valueOf(v).toByteArray(), signatureData.getR(), signatureData.getS());
    }

}
