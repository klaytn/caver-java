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

import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.KlayPeerCount;
import com.klaytn.caver.methods.response.Quantity;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import java.util.Collections;

/**
 * @deprecated Please use {@link com.klaytn.caver.rpc.Net} instead.
 * @see com.klaytn.caver.rpc.RPC
 */
@Deprecated
public class JsonRpc2_0Net implements Net {

    protected final Web3jService web3jService;

    public JsonRpc2_0Net(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @Override
    public Request<?, Bytes> getNetworkId() {
        return new Request<>(
                "net_networkID",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Boolean> isListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Quantity> getPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, KlayPeerCount> getPeerCountByType() {
        return new Request<>(
                "net_peerCountByType",
                Collections.<String>emptyList(),
                web3jService,
                KlayPeerCount.class);
    }

}
