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
import com.klaytn.caver.tx.ManagedTransaction;
import com.klaytn.caver.tx.SmartContract;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

/**
 * @deprecated This class is deprecated since caver-java:1.5.0
 */
@Deprecated
public class DefaultGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_LIMIT = SmartContract.GAS_LIMIT;
    public static final BigInteger GAS_PRICE = ManagedTransaction.GAS_PRICE;

    public DefaultGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }

    public DefaultGasProvider(Caver caver) throws Exception{
        super(caver.klay().getGasPrice().send().getValue(), GAS_LIMIT);
    }
}
