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

import java.util.*;
import java.util.function.Function;

/**
 * @deprecated Please use {@link com.klaytn.caver.account.AccountKeyDecoder} instead.
 */
@Deprecated
public class AccountKeyDecoder {
    private static HashMap<AccountKey.Type, Function<byte[], AccountKey>> typeMap = new HashMap<AccountKey.Type, Function<byte[], AccountKey>>(){
        {
            put(AccountKey.Type.PUBLIC, AccountKeyPublic::decodeFromRlp);
            put(AccountKey.Type.MULTISIG, AccountKeyWeightedMultiSig::decodeFromRlp);
            put(AccountKey.Type.FAIL, AccountKeyFail::decodeFromRlp);
            put(AccountKey.Type.LEGACY, AccountKeyLegacy::decodeFromRlp);
            put(AccountKey.Type.ROLEBASED, AccountKeyRoleBased::decodeFromRlp);
        }
    };

    public static AccountKey fromRlp(String raw) {
        if(Numeric.toHexString(AccountKeyNil.RLP).equals(raw))
            return AccountKeyNil.create();

        AccountKey.Type type = AccountKey.Type.findByValue(Numeric.hexStringToByteArray(raw)[0]);
        return typeMap.get(type).apply(Numeric.hexStringToByteArray(raw));
    }

    public static List<AccountKey> fromRlp(String... raws) {
        List<AccountKey> accountKeys = new ArrayList<>();
        for (String raw : raws) {
            accountKeys.add(fromRlp(raw));
        }
        return accountKeys;
    }

    public static byte[] getRawTransactionNoType(byte[] rawTransaction) {
        return Arrays.copyOfRange(rawTransaction, 1, rawTransaction.length);
    }
}
