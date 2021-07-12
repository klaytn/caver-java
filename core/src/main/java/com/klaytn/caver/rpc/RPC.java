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

package com.klaytn.caver.rpc;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.BatchRequest;
import org.web3j.protocol.core.Batcher;

/**
 * This class represents JSON-RPC 2.0 Klaytn APIs
 */
public class RPC implements Batcher {

    /**
     * JSON-RPC service instance.
     */
    private Web3jService web3jService;

    /**
     * The API class related accounts, blocks, transactions, nodes.
     */
    public Klay klay;

    /**
     * The API class used to query network configuration.
     */
    public Net net;

    /**
     * The API class related governance.
     */
    public Governance gov;

    /**
     * Constructor for RPC
     * @param web3jService JSON-RPC service instance
     */
    public RPC(Web3jService web3jService) {
        this.web3jService = web3jService;
        this.klay = new Klay(web3jService);
        this.net = new Net(web3jService);
        this.gov = new Governance(web3jService);
    }

    /**
     * Getter for web3jService
     * @return Web3jService
     */
    public Web3jService getWeb3jService() {
        return web3jService;
    }

    /**
     * Setter for web3jService
     * @param web3jService Web3jService instance.
     */
    public void setWeb3jService(Web3jService web3jService) {
        this.web3jService = web3jService;
        this.klay = new Klay(web3jService);
        this.net = new Net(web3jService);
        this.gov = new Governance(web3jService);
    }

    /**
     * Getter for Klay API instance.
     * @return Klay
     */
    public Klay getKlay() {
        return klay;
    }

    /**
     * Getter for Net API instance
     * @return Net
     */
    public Net getNet() {
        return net;
    }

    /**
     * Returns a new {@link BatchRequest}
     * @return BatchRequest
     */
    @Override
    public BatchRequest newBatch() {
        return new BatchRequest(web3jService);
    }
}
