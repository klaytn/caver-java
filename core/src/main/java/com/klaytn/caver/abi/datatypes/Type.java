/*
 * Modifications copyright 2021 The caver-java Authors
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is derived from web3j/abi/src/main/java/org/web3j/abi/datatypes/Type.java (2021/04/05).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.abi.datatypes;

/** ABI Types. */
public interface Type<T> {
    int MAX_BIT_LENGTH = 256;
    int MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8;

    default int bytes32PaddedLength() {
        return MAX_BYTE_LENGTH;
    }

    T getValue();

    String getTypeAsString();
}
