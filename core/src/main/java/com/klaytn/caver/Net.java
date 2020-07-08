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
import org.web3j.protocol.core.Request;

/**
 * @deprecated Please use {@link com.klaytn.caver.rpc.Net} instead.
 * @see com.klaytn.caver.rpc.RPC
 */
@Deprecated
public interface Net {

    /**
     * Returns the network identifier (network ID).
     *
     * @return Bytes - The integer of the network identifier.
     */
    Request<?, Bytes> getNetworkId();

    /**
     * Returns true if the client is actively listening for network connections.
     *
     * @return Boolean - true when listening, otherwise false
     */
    Request<?, Boolean> isListening();

    /**
     * Returns the number of peers currently connected to the client.
     *
     * @return Quantity - Integer of the number of connected peers.
     */
    Request<?, Quantity> getPeerCount();

    /**
     * Returns the number of connected nodes by type and the total number of connected nodes.
     *
     * @return KlayPeerCount - The number of connected peers by type as well as the total number of connected peers.
     */
    Request<?, KlayPeerCount> getPeerCountByType();

}
