/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.kct.kip37.wrapper;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip37.KIP37;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Map;

/**
 * Representing a KIP37Wrapper
 * 1. This class wraps all static methods of KIP37
 * 2. This class should be accessed via `caver.kct.kip37`
 */
public class KIP37Wrapper {
    /**
     * A Caver instance
     * this should be injected from KCTWrapper(= `caver.kct`)
     */
    private Caver caver;

    /**
     * Creates a KIP37Wrapper instance
     * @param caver A Caver instance
     */
    public KIP37Wrapper(Caver caver) {
        this.caver = caver;
    }

    /**
     * Creates a KIP37 instance
     * @return KIP37
     * @throws IOException
     */
    public KIP37 create() throws IOException {
        return new KIP37(this.caver);
    }

    /**
     * Creates a KIP37 instance
     * @param contractAddress A contract address
     * @return KIP37
     * @throws IOException
     */
    public KIP37 create(String contractAddress) throws IOException {
        return new KIP37(this.caver, contractAddress);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param uri The URI for token type.
     * @param deployer A deployer's address.
     * @return KIP37
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP37 deploy(String uri, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP37.deploy(this.caver, uri, deployer);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in `SendOptions` like below code example.
     * <pre>
     * <code>
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("deployer address");
     *     sendOptions.setGas(BigInteger.valueOf(gas value));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("fee payer address");
     *
     *     KIP37 kip37 = caver.kct.kip37.deploy(uri, sendOptions);
     * </code>
     * </pre>
     * @param uri The URI for token type.
     * @param sendOptions The send options to deploy a contract.
     * @return KIP37
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP37 deploy(String uri, SendOptions sendOptions) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP37.deploy(this.caver, uri, sendOptions);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param uri The URI for token type.
     * @param deployer A deployer's address.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP37
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP37 deploy(String uri, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP37.deploy(this.caver, uri, deployer, wallet);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.<p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in `SendOptions` like below code example.
     * <pre>
     * <code>
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("deployer address");
     *     sendOptions.setGas(BigInteger.valueOf(gas value));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("fee payer address");
     *
     *     KIP37 kip37 = caver.kct.kip37.deploy(caver, uri, sendOptions, caver.getWallet());
     * </code>
     * </pre>
     * @param uri The URI for token type.
     * @param sendOptions The send options to deploy a contract.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP37 deploy(String uri, SendOptions sendOptions, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP37.deploy(this.caver, uri, sendOptions, wallet);
    }

    /**
     * Detects which interface the KIP-37 token contract supports.<p>
     * Example :
     * <pre>{@code
     * Map<String, Boolean> result = KIP37.detectInterface();
     * result.get(KIP37.INTERFACE_ID_IKIP37);
     * result.get(KIP37.INTERFACE_ID_IKIP37_BURNABLE);
     * result.get(KIP37.INTERFACE_ID_IKIP37_METADATA);
     * result.get(KIP37.INTERFACE_ID_IKIP37_MINTABLE);
     * result.get(KIP37.INTERFACE_ID_IKIP37_PAUSABLE);
     * }</pre>
     *
     * @param contractAddress A contract instance
     * @return Map&lt;String, Boolean&gt;
     */
    public Map<String, Boolean> detectInterface(String contractAddress) {
        return KIP37.detectInterface(this.caver, contractAddress);
    }
}
