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

import java.io.IOException;
import java.math.BigInteger;

/**
 * Representing a class that has constructor data to deploy KIP-7.
 */
public class KIP7DeployParams {
    /**
     * The KIP-7 contract name.
     */
    String name;

    /**
     * The KIP-7 contract symbol.
     */
    String symbol;

    /**
     * The KIP-7 contract decimals that represented token unit.
     */
    int decimals;

    /**
     * The initial supply of token when deploying KIP-7 contract.
     */
    BigInteger initialSupply;


    /**
     * Creates a KIP7DeployParams instance.
     * @param name The KIP-7 contract name.
     * @param symbol The KIP-7 contract symbol.
     * @param decimals The KIP-7 contract decimals that represented token unit.
     * @param initialSupply The initial supply of token when deploying KIP-7 contract.
     */
    public KIP7DeployParams(String name, String symbol, int decimals, BigInteger initialSupply) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.initialSupply = initialSupply;
    }

    /**
     * Getter for name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for symbol.
     * @return String
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Getter for decimals.
     * @return String
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * Getter for initialSupply.
     * @return String
     */
    public BigInteger getInitialSupply() {
        return initialSupply;
    }

    /**
     * Setter for name.
     * @param name The KIP-7 contract name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for symbol.
     * @param symbol The KIP-7 contract symbol.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Setter for decimals.
     * @param decimals The KIP-7 contract decimals that represented token unit.
     */
    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    /**
     * Setter for initialSupply.
     * @param initialSupply The initial supply of token when deploying KIP-7 contract.
     */
    public void setInitialSupply(BigInteger initialSupply) {
        this.initialSupply = initialSupply;
    }
}
