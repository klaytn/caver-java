/*
 * Copyright 2021 The caver-java Authors
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

import com.klaytn.caver.rpc.Governance;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * The class represented to store the response data of the following methods.
 * <ul>
 *     <li>{@link Governance#getIdxCache()}</li>
 *     <li>{@link Governance#getIdxCacheFromDb()}</li>
 * </ul>
 */
public class GovernanceIdxCache extends Response<List<String>> {
    @Override
    public String toString() {
        return Utils.printString(this);
    }
}
