/*
 * Copyright 2022 The caver-java Authors
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

import java.util.Arrays;
import java.util.Collections;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.NodeInfo;
import com.klaytn.caver.methods.response.Peers;

public class Admin {
    protected final Web3jService web3jService;

    public Admin(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    /**
     * Returns the node information.
     *
     * @return NodeInfo - The node info of the running Klaytn node.
     */
    public Request<?, NodeInfo> getNodeInfo() {
        return new Request<>(
                "admin_nodeInfo",
                Collections.<String>emptyList(),
                web3jService,
                NodeInfo.class);
    }

    /**
     * Returns the running Klaytn node's data directory.
     *
     * @return Bytes - The absolute path of the klaytn node.
     */
    public Request<?, Bytes> getDataDir() {
        return new Request<>(
                "admin_datadir",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns the peer info of the connected remote nodes.
     *
     * @return Peers - peer info of the connected nodes.
     */
    public Request<?, Peers> getPeers() {
        return new Request<>(
                "admin_peers",
                Collections.<String>emptyList(),
                web3jService,
                Peers.class);
    }

    /**
     * Adds the new remote node from given kni.
     *
     * @param kni The remote node kni value.
     * @return Boolean - true if the peer was accepted, otherwise false.
     */
    public Request<?, Boolean> addPeer(String kni) {
        return new Request<>(
                "admin_addPeer",
                Collections.singletonList(kni),
                web3jService,
                Boolean.class);
    }

    /**
     * Adds the new remote node from given kni.
     *
     * @return Boolean - true if the peer was accepted, otherwise false.
     */
    /**
     * Removes the remote node from given kni.
     *
     * @param kni The remote node kni value.
     * @return Boolean - true if success, otherwise false.
     */
    public Request<?, Boolean> removePeer(String kni) {
        return new Request<>(
                "admin_addPeer",
                Collections.singletonList(kni),
                web3jService,
                Boolean.class);
    }
}
