/*
 * Copyright 2022 The caver-java Authors
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

package com.klaytn.caver.transaction.utils.wrapper;

import com.klaytn.caver.transaction.utils.AccessTuple;
import org.web3j.rlp.RlpList;

import java.util.List;

/**
 * Represents a AccessTupleWrapper
 * 1. This class wraps all of static methods of AccessTuple
 * 2. This class should be accessed via `caver.transaction.utils.accessTuple`
 */
public class AccessTupleWrapper {
    /**
     * Creates a AccessTupleWrapper instance.
     */
    public AccessTupleWrapper() {
    }

    /**
     * Creates an AccessTuple instance.
     * @param address An address string.
     * @param storageKeys A list of storage keys.
     * @return
     */
    public AccessTuple create(String address, List<String> storageKeys) {
        return AccessTuple.create(address, storageKeys);
    }
}