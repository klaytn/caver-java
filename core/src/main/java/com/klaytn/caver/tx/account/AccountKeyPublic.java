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

package com.klaytn.caver.tx.account;

import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * AccountKeyPublic is used for accounts having one public key.
 * If an account has an AccountKeyPublic object, the tx validation process is done like below:
 * <ul>
 * <li> Get the public key derived from ecrecover(txhash, txsig) </li>
 * <li> Check that the derived public key is the same as the corresponding </li>
 * <li> account's public key </li>
 * </ul>
 */
public class AccountKeyPublic implements AccountKey {

    private String x;
    private String y;

    public AccountKeyPublic() {
    }

    protected AccountKeyPublic(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public static AccountKeyPublic create(BigInteger publicKey) {
        String hexStringPublicKey = Numeric.toHexStringNoPrefixZeroPadded(publicKey, 128);
        String publicKeyX = Numeric.prependHexPrefix(hexStringPublicKey.substring(0, 64));
        String publicKeyY = Numeric.prependHexPrefix(hexStringPublicKey.substring(64, 128));
        return new AccountKeyPublic(publicKeyX, publicKeyY);
    }

    public static AccountKeyPublic create(String publicKeyX, String publicKeyY) {
        return new AccountKeyPublic(
                Numeric.prependHexPrefix(publicKeyX), Numeric.prependHexPrefix(publicKeyY)
        );
    }

    @Override
    public byte[] toRlp() {
        byte[] encodedTransaction = RlpEncoder.encode(
                RlpString.create(Numeric.hexStringToByteArray(toCompressedPublicKey())));
        byte[] type = {getType().getValue()};
        return BytesUtils.concat(type, encodedTransaction);
    }

    public BigInteger getPublicKey() {
        return Numeric.toBigInt(getX() + getY().substring(2));
    }

    public String toCompressedPublicKey() {
        return AccountKeyPublicUtils.toCompressedPublicKey(getPublicKey());
    }

    public static AccountKeyPublic decodeFromRlp(byte[] rawTransaction) {
        byte[] transaction = AccountKeyDecoder.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(transaction);
        String compressedPublicKey = ((RlpString) rlpList.getValues().get(0)).asString();

        return AccountKeyPublicUtils.decompressKey(compressedPublicKey);
    }

    public static AccountKeyPublic decodeFromRlp(String hexString) {
        return decodeFromRlp(Numeric.hexStringToByteArray(hexString));
    }

    public String getX() {
        return Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(this.x), 64);
    }

    public String getY() {
        return Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(this.y), 64);
    }

    @Override
    public Type getType() {
        return Type.PUBLIC;
    }

    @Override
    public String toString() {
        return "AccountKeyPublic x : " + getX() + " / y : " + getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKeyPublic that = (AccountKeyPublic) o;
        return Arrays.equals(toRlp(), that.toRlp());
    }
}
