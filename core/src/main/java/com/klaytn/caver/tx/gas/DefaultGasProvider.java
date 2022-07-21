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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/gas/DefaultGasProvider.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.tx.gas;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.BlockHeader;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.tx.ManagedTransaction;
import com.klaytn.caver.tx.SmartContract;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.math.BigInteger;

public class DefaultGasProvider implements ContractGasProvider {
    public static final BigInteger GAS_LIMIT = SmartContract.GAS_LIMIT;
    public static final BigInteger GAS_PRICE_25_STON = ManagedTransaction.GAS_PRICE;
    private final Caver caver;

    public DefaultGasProvider() {
        this.caver = null;
    }

    public DefaultGasProvider(Caver caver) {
        this.caver = caver;
    }

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return this.getGasPrice();
    }

    @Override
    public BigInteger getGasPrice() {
        try {
            if (this.caver == null) {
                return GAS_PRICE_25_STON;
            }
            // Klaytn decided to apply dynamic gas price policy, so we need to fetch base fee per gas
            // which can be dynamic based on network status.
            // klay_gasPrice will return baseFee * 2 after the Magma hard fork,
            // otherwise return unit price.
            Quantity gasPrice = caver.rpc.klay.getGasPrice().send();
            return gasPrice.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return getGasLimit();
    }

    @Override
    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }
}
