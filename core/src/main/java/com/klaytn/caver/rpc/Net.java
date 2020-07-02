package com.klaytn.caver.rpc;

import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.KlayPeerCount;
import com.klaytn.caver.methods.response.Quantity;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import java.util.Collections;

public class Net {
    protected final Web3jService web3jService;

    public Net(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    /**
     * Returns the network identifier (network ID).
     *
     * @return Bytes - The integer of the network identifier.
     */
    public Request<?, Bytes> getNetworkID() {
        return new Request<>(
                "net_networkID",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns true if the client is actively listening for network connections.
     *
     * @return Boolean - true when listening, otherwise false
     */
    public Request<?, Boolean> isListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    /**
     * Returns the number of peers currently connected to the client.
     *
     * @return Quantity - Integer of the number of connected peers.
     */
    public Request<?, Quantity> getPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the number of connected nodes by type and the total number of connected nodes.
     *
     * @return KlayPeerCount - The number of connected peers by type as well as the total number of connected peers.
     */
    public Request<?, KlayPeerCount> getPeerCountByType() {
        return new Request<>(
                "net_peerCountByType",
                Collections.<String>emptyList(),
                web3jService,
                KlayPeerCount.class);
    }
}
