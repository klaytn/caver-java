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

package com.klaytn.caver;

import org.web3j.protocol.Web3jService;

/**
 * Core Caverj JSON-RPC API.
 */
public interface Caver {

    String DEFAULT_URL = "http://localhost:8551";
    String MAINNET_URL = "https://api.cypress.klaytn.net:8651";
    String BAOBAB_URL = "https://api.baobab.klaytn.net:8651";

    /**
     * Construct a new Caverj instance.
     *
     * @param service Web3jService
     * @return new Caverj instance
     */
    static Caver build(Web3jService service) {
        return new CaverImpl(service);
    }

    /**
     * Construct a new Caverj instance which is connected to {@code url} node.
     *
     * @param url Klaytn url
     * @return new Caverj instance
     */
    static Caver build(String url) {
        return new CaverImpl(url);
    }

    /**
     * Construct a new Caverj instance which is connected to local node.
     *
     * @return new Caverj instance
     */
    static Caver build() {
        return Caver.build(DEFAULT_URL);
    }

    Klay klay();

    Net net();
}
