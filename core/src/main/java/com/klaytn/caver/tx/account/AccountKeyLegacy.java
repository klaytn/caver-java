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

import org.web3j.utils.Numeric;

import java.util.Arrays;

/**
 * AccountKeyLegacy represents a key of legacy account types. If an account has AccountKeyLegacy,
 * the tx validation process is done like below (as Ethereum did):
 * <ul>
 * <li> Get the public key from ecrecover(txhash, txsig) </li>
 * <li> Get the address of the public key </li>
 * <li> The address is the sender </li>
 * </ul>
 */
public class AccountKeyLegacy implements AccountKey {

    private static byte[] RLP = new byte[]{(byte) 0x01, (byte) 0xc0};

    protected AccountKeyLegacy() {
    }


    public static AccountKeyLegacy create() {
        return new AccountKeyLegacy();
    }

    public static AccountKeyLegacy decodeFromRlp(byte[] rawTransaction) {
        return new AccountKeyLegacy();
    }

    public static AccountKeyLegacy decodeFromRlp(String hexString) {
        return decodeFromRlp(Numeric.hexStringToByteArray(hexString));
    }

    @Override
    public Type getType() {
        return Type.LEGACY;
    }

    @Override
    public byte[] toRlp() {
        return RLP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKeyLegacy that = (AccountKeyLegacy) o;
        return Arrays.equals(toRlp(), that.toRlp());
    }

    @Override
    public String toString() {
        return "AccountKeyLegacy : " + toRlp().toString();
    }
}
