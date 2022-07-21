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

import org.web3j.protocol.core.Response;

import com.klaytn.caver.methods.response.NodeInfo.NodeInfoData;

public class NodeInfo extends Response<NodeInfoData> {

    public static class NodeInfoData {
        private String kni;
        private String id;
        private String ip;
        private String listenAddr;
        private String name;
        private NodeInfoPorts ports;
        // FIXME: what is the proper class?
        // private Map<String, Object> protocols;

        public String getKni() {
            return kni;
        }

        public void setKni(String kni) {
            this.kni = kni;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getListenAddr() {
            return listenAddr;
        }

        public void setListenAddr(String listenAddr) {
            this.listenAddr = listenAddr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NodeInfoPorts getPorts() {
            return ports;
        }

        public void setPorts(NodeInfoPorts ports) {
            this.ports = ports;
        }
    }

    public static class NodeInfoPorts {
        private Integer discovery;
        private Integer listener;

        public Integer getDiscovery() {
            return discovery;
        }

        public void setDiscovery(Integer discovery) {
            this.discovery = discovery;
        }

        public Integer getListener() {
            return listener;
        }

        public void setListener(Integer listener) {
            this.listener = listener;
        }
    }
}
