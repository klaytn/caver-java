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

package com.klaytn.caver.methods.request;

import org.web3j.protocol.core.DefaultBlockParameter;

import java.util.Arrays;
import java.util.List;

public class KlayLogFilter extends KlayFilter {

    /**
     * (optional) A filter option that restricts the logs returned to the single block with the 32-byte hash blockHash.
     * Using blockHash is equivalent to fromBlock = toBlock = the block number with hash blockHash. If blockHash is
     * present in in the filter criteria, then neither fromBlock nor toBlock are allowed.
     */
    private String blockHash;

    public KlayLogFilter() {
        super();
    }

    public KlayLogFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                         List<String> address, String blockHash) {
        super(fromBlock, toBlock, address);
        this.blockHash = blockHash;
    }

    public KlayLogFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                         String address, String blockHash) {
        this(fromBlock, toBlock, Arrays.asList(address), blockHash);
    }

    public String getBlockHash() {
        return blockHash;
    }

    @Override
    KlayLogFilter getThis() {
        return this;
    }
}
