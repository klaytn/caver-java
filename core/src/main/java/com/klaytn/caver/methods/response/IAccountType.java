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


public interface IAccountType {

    AccType getType();

    enum AccType {
        EOA(0x1),
        SCA(0x2);

        int accType;

        AccType(int accType) {
            this.accType = accType;
        }

        public static AccType getType(int keyType) {
            if (EOA.accType == keyType) return EOA;
            return SCA;
        }

        public int getAccType() {
            return accType;
        }
    }

}
