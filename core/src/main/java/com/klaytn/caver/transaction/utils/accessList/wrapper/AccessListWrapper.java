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

package com.klaytn.caver.transaction.utils.accessList.wrapper;

import com.klaytn.caver.transaction.utils.accessList.AccessList;
import com.klaytn.caver.transaction.utils.accessList.AccessTuple;
import org.web3j.rlp.RlpList;

import java.util.List;

/**
 * Represents a AccessListWrapper
 * 1. This class wraps all of static methods of AccessList
 * 2. This class should be accessed via `caver.transaction.ethereumAccessList`
 */
public class AccessListWrapper {
    /**
     * Creates a AccessListWrapper instance.
     */
    public AccessListWrapper() {
    }

    /**
     * Creates a AccessList instance derived from a RLP-encoded AccessList string.
     *
     * @param items A list of AccessTuple
     * @return AccessList
     */
    public AccessList create(List<AccessTuple> items) {
        return AccessList.create(items);
    }

    /**
     * Decodes a RLP-encoded AccessList byte array.
     *
     * @param rlpEncoded RLP-encoded AccessList byte array.
     * @return AccessList
     */
    public AccessList decode(String rlpEncoded) {
        return AccessList.decode(rlpEncoded);
    }

    /**
     * Decodes a RLP-encoded AccessList byte array.
     *
     * @param rlpEncoded RLP-encoded AccessList byte array.
     * @return AccessList
     */
    public AccessList decode(byte[] rlpEncoded) {
        return AccessList.decode(rlpEncoded);
    }


    /**
     * Decodes a RLP-encoded AccessList.
     *
     * @param rlpTypeList List of RlpType to decode.
     * @return AccessList
     */
    public AccessList decode(RlpList rlpTypeList) {
        return AccessList.decode(rlpTypeList);
    }
}