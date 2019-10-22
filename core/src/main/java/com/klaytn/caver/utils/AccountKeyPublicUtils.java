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
 * This file is derived from web3j/crypto/src/main/java/org/web3j/crypto/Sign.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.utils;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECPoint;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class AccountKeyPublicUtils {

    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    static final ECDomainParameters CURVE = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
    
    public static String toCompressedPublicKey(BigInteger publicKey) {
        String hexStringPublicKey = Numeric.toHexStringNoPrefixZeroPadded(publicKey, 128);
        String publicKeyX = hexStringPublicKey.substring(0, 64);
        String pubKeyYPrefix = publicKey.testBit(0) ? "03" : "02";
        return pubKeyYPrefix + publicKeyX;
    }

    public static AccountKeyPublic decompressKey(String compressedPublicKey) {
        boolean yBit = Numeric.cleanHexPrefix(compressedPublicKey).substring(0, 2).equals("03");
        BigInteger xBN = Numeric.toBigInt(compressedPublicKey.substring(2));
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 0x03 : 0x02);
        ECPoint ecPoint = CURVE.getCurve().decodePoint(compEnc);
        return AccountKeyPublic.create(
                Numeric.toHexStringWithPrefixZeroPadded(ecPoint.getAffineXCoord().toBigInteger(), 64),
                Numeric.toHexStringWithPrefixZeroPadded(ecPoint.getAffineYCoord().toBigInteger(), 64)
        );
    }
}
