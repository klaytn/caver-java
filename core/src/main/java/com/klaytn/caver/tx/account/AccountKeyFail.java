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
 * AccountKeyFail is used for smart contract accounts so that a transaction sent from
 * the smart contract account always fails.
 * If an account has the key AccountKeyFail, the tx validation process always fails.
 */
public class AccountKeyFail implements AccountKey {

    private static byte[] RLP = new byte[]{(byte) 0x03, (byte) 0xc0};

    protected AccountKeyFail() {
    }

    public static AccountKeyFail create() {
        return new AccountKeyFail();
    }

    public static AccountKeyFail decodeFromRlp(byte[] rawTransaction) {
        return new AccountKeyFail();
    }

    public static AccountKeyFail decodeFromRlp(String hexString) {
        return decodeFromRlp(Numeric.hexStringToByteArray(hexString));
    }

    @Override
    public Type getType() {
        return Type.FAIL;
    }

    @Override
    public byte[] toRlp() {
        return RLP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKeyFail that = (AccountKeyFail) o;
        return Arrays.equals(toRlp(), that.toRlp());
    }

    @Override
    public String toString() {
        return "AccountKeyFail";
    }
}
