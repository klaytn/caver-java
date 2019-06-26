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

package com.klaytn.caver.methods.response;


public interface AccountType {

    AccountType.Key getType();

    enum Key {
        EOA(0x1),
        CONTRACT(0x2);

        int keyType;

        Key(int keyType) {
            this.keyType = keyType;
        }

        public static AccountType.Key getType(int keyType) {
            if (EOA.keyType == keyType) return EOA;
            return CONTRACT;
        }

        public int getKeyType() {
            return keyType;
        }
    }

}
