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

import com.klaytn.caver.abi.wrapper.ABIWrapper;
import com.klaytn.caver.account.wrapper.AccountWrapper;
import com.klaytn.caver.contract.wrapper.ContractWrapper;
import com.klaytn.caver.ipfs.wrapper.IPFSWrapper;
import com.klaytn.caver.kct.wrapper.KCTWrapper;
import com.klaytn.caver.rpc.RPC;
import com.klaytn.caver.transaction.wrapper.TransactionWrapper;
import com.klaytn.caver.utils.wrapper.UtilsWrapper;
import com.klaytn.caver.wallet.IWallet;
import com.klaytn.caver.wallet.KeyringContainer;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

/**
 * Core Caverj JSON-RPC API.
 */
public class Caver {

    public static String DEFAULT_URL = "http://localhost:8551";

    /**
     * @deprecated Please use <code>caver.rpc.klay</code> instead.
     * @see RPC#klay
     */
    @Deprecated
    Klay klay;

    /**
     * @deprecated Please use <code>caver.rpc.net</code> instead.
     * @see RPC#net
     */
    @Deprecated
    Net net;

    /**
     * The JSON-RPC API instance
     */
    public RPC rpc;

    /**
     * The KeyringContainer instance.
     */
    public KeyringContainer wallet;

    /**
     * The Transaction instance.
     */
    public TransactionWrapper transaction;

    /**
     * The IPFSWrapper instance.
     */
    public IPFSWrapper ipfs;

    /**
     * The AccountWrapper instance
     */
    public AccountWrapper account;

    /**
     * The ContractWrapper instance.
     */
    public ContractWrapper contract;

    /**
     * The ABIWrapper instance
     */
    public ABIWrapper abi;

    /**
     * The KCTWrapper instance
     */
    public KCTWrapper kct;

    /**
     * The UtilsWrapper instance
     */
    public UtilsWrapper utils;

    /**
     * Creates a Caver instance<p>
     * It sets a HttpProvider that using DEFAULT_URL("http://localhost:8551").
     */
    public Caver() {
        this(new HttpService(DEFAULT_URL));
    }

    /**
     * Creates a Caver instance
     * @param url JSON-RPC request URL
     */
    public Caver(String url) {
        this(new HttpService(url));
    }

    /**
     * Creates a Caver instance
     * @param service Web3jService
     */
    public Caver(Web3jService service) {
        ipfs = new IPFSWrapper();
        rpc = new RPC(service);
        wallet = new KeyringContainer();
        account = new AccountWrapper();
        transaction = new TransactionWrapper(rpc.getKlay());
        contract = new ContractWrapper(this);
        abi = new ABIWrapper();
        kct = new KCTWrapper(this);
        utils = new UtilsWrapper();
    }

    /**
     * @deprecated Please use {@link #Caver(Web3jService)} instead.
     */
    @Deprecated
    public static Caver build(Web3jService service) {
        return new CaverImpl(service);
    }

    /**
     * @deprecated Please use {@link #Caver(String)} instead.
     */
    @Deprecated
    public static Caver build(String url) {
        return new CaverImpl(url);
    }

    /**
     * @deprecated Please use {@link #Caver()} instead.
     */
    @Deprecated
    public static Caver build() {
        return Caver.build(DEFAULT_URL);
    }

    /**
     * Getter for RPC
     * @return RPC
     */
    public RPC getRpc() {
        return rpc;
    }

    /**
     * Getter for Wallet
     * @return IWallet
     */
    public IWallet getWallet() {
        return wallet;
    }

    /**
     * Getter for IPFSWrapper
     * @return IPFSWrapper
     */
    public IPFSWrapper getIpfs() {
        return ipfs;
    }

    /**
     * @deprecated Please use <code>caver.rpc.klay</code> instead.
     * @see RPC#klay
     */
    @Deprecated
    public Klay klay() {
        return klay;
    };

    /**
     * @deprecated Please use <code>caver.rpc.net</code> instead.
     * @see RPC#net
     */
    @Deprecated
    public Net net() {
        return net;
    };
}
