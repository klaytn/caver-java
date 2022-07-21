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

package com.klaytn.caver.kct.kip17;

/**
 * Representing a class that has constructor data to deploy KIP-7.
 */
public class KIP17DeployParams {

    /**
     * The KIP-17 contract name.
     */
    String name;

    /**
     * The KIP-17 contract symbol.
     */
    String symbol;

    /**
     * Creates a KIP17DeployParams instance.
     * @param name The KIP-17 contract name.
     * @param symbol The KIP-17 contract symbol.
     */
    public KIP17DeployParams(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
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
}
