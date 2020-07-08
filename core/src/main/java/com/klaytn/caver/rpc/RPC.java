package com.klaytn.caver.rpc;

import org.web3j.protocol.Web3jService;

/**
 * This class represents JSON-RPC 2.0 Klaytn APIs
 */
public class RPC {

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
     * Constructor for RPC
     * @param web3jService JSON-RPC service instance
     */
    public RPC(Web3jService web3jService) {
        this.web3jService = web3jService;
        klay = new Klay(web3jService);
        net = new Net(web3jService);
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

}
