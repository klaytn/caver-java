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
package com.klaytn.caver.methods.response;

import java.util.List;

import org.web3j.protocol.core.Response;

import com.klaytn.caver.methods.response.Peers.Peer;

public class Peers extends Response<List<Peer>> {

    public static class Peer {
        private String id;
        private List<String> caps;
        private String name;
        private List<PeerNetwork> networks;
        // FIXME: what is the proper class for protocols?
        // private Map<String, Object> protocols;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getCaps() {
            return caps;
        }

        public void setCaps(List<String> caps) {
            this.caps = caps;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<PeerNetwork> getNetworks() {
            return networks;
        }

        public void setNetworks(List<PeerNetwork> networks) {
            this.networks = networks;
        }
    }

    public static class PeerNetwork {
        private boolean inbound;
        private String localAddress;
        private String nodeType;
        private String remoteAddress;
        private boolean isStatic;
        private boolean trusted;

        public boolean isInbound() {
            return inbound;
        }

        public void setInbound(boolean inbound) {
            this.inbound = inbound;
        }

        public String getLocalAddress() {
            return localAddress;
        }

        public void setLocalAddress(String localAddress) {
            this.localAddress = localAddress;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getRemoteAddress() {
            return remoteAddress;
        }

        public void setRemoteAddress(String remoteAddress) {
            this.remoteAddress = remoteAddress;
        }

        public boolean isStatic() {
            return isStatic;
        }

        public void setStatic(boolean aStatic) {
            isStatic = aStatic;
        }

        public boolean isTrusted() {
            return trusted;
        }

        public void setTrusted(boolean trusted) {
            this.trusted = trusted;
        }
    }
}
