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

package com.klaytn.caver.transaction.utils.accessList;

import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.util.*;

/**
 * Represents an access list.
 * AccessList is an EIP-2930 access list.
 */
public class AccessList extends ArrayList<AccessTuple> {
    /**
     * Constructor.
     */
    public AccessList() {}

    /**
     * Creates an AccessList instance from given list of AccessTuple.
     * @param items A list of AccessTuple.
     */
    public AccessList(List<AccessTuple> items) {
        this.addAll(items);
    }

    /**
     * Creates an Access list.
     *
     * @param items
     * @return
     */
    public static AccessList create(List<AccessTuple> items) {
        AccessList accessList = new AccessList(items);
        return accessList;
    }

    /**
     * Returns a decoded access list.
     *
     * @param accessListRlpList List of RlpType to decode.
     * @return AccessList
     */
    public static AccessList decode(RlpList accessListRlpList) {
        List<RlpType> accessTupleRlps = accessListRlpList.getValues();
        AccessList accessList = new AccessList();
        for (int i =0 ; i < accessTupleRlps.size(); i++) {
            accessList.add(AccessTuple.decode((RlpList) accessTupleRlps.get(i)));
        }
        return accessList;
    }

    /**
     * Returns a decoded access list.
     *
     * @param rlpEncoded Rlp encoded string of access list.
     * @return
     */
    public static AccessList decode(String rlpEncoded) {
        return AccessList.decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Returns a decoded access list.
     *
     * @param rlpEncoded Rlp encoded byte array of access list.
     * @return
     */
    public static AccessList decode(byte[] rlpEncoded) {
        RlpList rlpList = RlpDecoder.decode(rlpEncoded);
        return AccessList.decode(((RlpList) rlpList.getValues().get(0)));
    }

    /**
     * Returns the RLP-encoded string of this accessList.
     *
     * @return RlpList
     */
    public RlpList toRlpList() {
        List<RlpType> rlpTypeList = new ArrayList<>();
        for (AccessTuple accessTuple : this) {
            rlpTypeList.add(accessTuple.toRlpList());
        }
        return new RlpList(rlpTypeList);
    }

    /**
     * Returns an encoded access tuple.
     *
     * @return byte[]
     */
    public byte[] encodeToBytes() {
        RlpList rlPList = this.toRlpList();
        return RlpEncoder.encode(rlPList);
    }
}
