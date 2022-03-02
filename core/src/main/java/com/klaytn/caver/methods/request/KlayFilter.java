/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/core/src/main/java/org/web3j/protocol/core/methods/request/EthFilter.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.methods.request;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KlayFilter extends Filter<KlayFilter> {

    /**
     * (optional, default: "latest") Integer block number, or "latest" for the last mined block or
     * "earliest" for not yet mined transactions.
     */
    private DefaultBlockParameter fromBlock;

    /**
     * (optional, default: "latest") Integer block number, or "latest" for the last mined block or
     * "earliest" for not yet mined transactions.
     */
    private DefaultBlockParameter toBlock;

    /**
     * (optional) Contract address or a list of addresses from which logs should originate.
     * This can be single address as string or list
     */
    private List<String> address;

    public KlayFilter() {
        super();
    }

    public KlayFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public KlayFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     String address) {
        this(fromBlock, toBlock, Arrays.asList(address));
    }

    @Override
    KlayFilter getThis() {
        return this;
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }


    public void setFromBlock(BigInteger blockNum) {
        setFromBlock(toBlockParameter(blockNum));
    }

    public void setFromBlock(DefaultBlockParameterName blockTag) {
        setFromBlock(toBlockParameter(blockTag));
    }

    public void setFromBlock(DefaultBlockParameter fromBlock) {
        this.fromBlock = fromBlock;
    }

    public void setToBlock(BigInteger blockNum) {
        setToBlock(toBlockParameter(blockNum));
    }

    public void setToBlock(DefaultBlockParameterName toBlock) {
        setToBlock(toBlockParameter(toBlock));
    }

    public void setToBlock(DefaultBlockParameter toBlock) {
        this.toBlock = toBlock;
    }

    public void setAddress(String address) {
        setAddress(Collections.singletonList(address));
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    private DefaultBlockParameter toBlockParameter(Object blockNumOrTag) {
        if(blockNumOrTag instanceof BigInteger) {
            return DefaultBlockParameter.valueOf((BigInteger)blockNumOrTag);
        } else if(blockNumOrTag instanceof String) {
            return DefaultBlockParameter.valueOf((String)blockNumOrTag);
        } else if(blockNumOrTag instanceof DefaultBlockParameterNumber) {
            return (DefaultBlockParameter)blockNumOrTag;
        } else if(blockNumOrTag instanceof DefaultBlockParameterName) {
            return (DefaultBlockParameter) blockNumOrTag;
        } else {
            throw new RuntimeException("It cannot cast a block type.");
        }
    }

}
