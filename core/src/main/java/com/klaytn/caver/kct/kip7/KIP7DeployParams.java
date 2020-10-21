/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.kct.kip7;

import java.math.BigInteger;

public class KIP7DeployParams {
    String name;
    String symbol;
    int decimals;
    BigInteger initialSupply;

    public KIP7DeployParams(String name, String symbol, int decimals, BigInteger initialSupply) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.initialSupply = initialSupply;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public BigInteger getInitialSupply() {
        return initialSupply;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public void setInitialSupply(BigInteger initialSupply) {
        this.initialSupply = initialSupply;
    }
}
