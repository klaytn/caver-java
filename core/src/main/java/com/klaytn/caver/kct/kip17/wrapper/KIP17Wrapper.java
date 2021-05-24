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

package com.klaytn.caver.kct.kip17.wrapper;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Representing a KIP17Wrapper
 * 1. This class wraps all static methods of KIP17
 * 2. This class should be accessed via `caver.kct.kip17`
 */
public class KIP17Wrapper {
    /**
     * A Caver instance
     * this should be injected from KCTWrapper(= `caver.kct`)
     */
    private Caver caver;

    /**
     * Creates a KIP17Wrapper instance.
     * @param caver A Caver instance.
     */
    public KIP17Wrapper(Caver caver) {
        this.caver = caver;
    }

    /**
     * Creates a KIP17 instance.
     * @return KIP17
     * @throws IOException
     */
    public KIP17 create() throws IOException {
        return new KIP17(this.caver);
    }

    /**
     * Creates a KIP17 instance.
     * @param contractAddress A contract address.
     * @return KIP17
     * @throws IOException
     */
    public KIP17 create(String contractAddress) throws IOException {
        return new KIP17(this.caver, contractAddress);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param deployer A deployer's address.
     * @param name A KIP-17 contract name.
     * @param symbol A KIP-17 contract symbol.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(String deployer, String name, String symbol) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, deployer, name, symbol);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * It must add deployer's keyring in caver.wallet.<p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in `SendOptions` like below code example.
     * <pre>
     * <code>
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("deployer address");
     *     sendOptions.setGas(BigInteger.valueOf(gas value));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("fee payer address");
     *
     *     KIP17 kip17 = caver.kct.kip17.deploy(sendOptions, name, symbol);
     * </code>
     * </pre>
     * @param sendOptions The send options to deploy a contract.
     * @param name A KIP-17 contract name.
     * @param symbol A KIP-17 contract symbol.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(SendOptions sendOptions, String name, String symbol) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, sendOptions, name, symbol);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param deployer A deployer's address.
     * @param name A KIP-17 contract name.
     * @param symbol A KIP-17 contract symbol.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(String deployer, String name, String symbol, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, deployer, name, symbol, wallet);
    }

    /**
     * Deploy KIP-17 contract. <p>
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
     *     KIP17 kip17 = caver.kct.kip17.deploy(sendOptions, name, symbol, caver.getWallet());
     * </code>
     * </pre>
     * @param sendOptions The send options to deploy a contract.
     * @param name A KIP-17 contract name.
     * @param symbol A KIP-17 contract symbol.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(SendOptions sendOptions, String name, String symbol, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, sendOptions, name, symbol, wallet);
    }

    /**
     * Deploy KIP17 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param tokenInfo The KIP-17 contract's deploy parameter values.
     * @param deployer A deployer's address.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(KIP17DeployParams tokenInfo, String deployer) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, tokenInfo, deployer);
    }

    /**
     * Deploy KIP-17 contract.<p>
     * It must add deployer's keyring in caver.wallet.<p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in `SendOptions` like below code example.
     * <pre>
     * <code>
     *     SendOptions sendOptions = new SendOptions();
     *     sendOptions.setFrom("deployer address");
     *     sendOptions.setGas(BigInteger.valueOf(gas value));
     *     sendOptions.setFeeDelegation(true);
     *     sendOptions.setFeePayer("fee payer address");
     *
     *     KIP17DeployParams tokenInfo = new KIP17DeployParams(name, symbol);
     *     KIP17 kip17 = caver.kct.kip17.deploy(tokenInfo, sendOptions);
     * </code>
     * </pre>
     * @param tokenInfo The KIP-17 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(KIP17DeployParams tokenInfo, SendOptions sendOptions) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, tokenInfo, sendOptions);
    }

    /**
     * Deploy KIP17 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param tokenInfo The KIP-17 contract's deploy parameter values.
     * @param deployer A deployer's address.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP17
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(KIP17DeployParams tokenInfo, String deployer, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, tokenInfo, deployer, wallet);
    }

    /**
     * Deploy KIP-17 contract.<p>
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
     *     KIP17DeployParams tokenInfo = new KIP17DeployParams(name, symbol);
     *     KIP17 kip17 = caver.kct.kip17.deploy(tokenInfo, sendOptions, caver.getWallet());
     * </code>
     * </pre>
     * @param tokenInfo The KIP-17 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP17 deploy(KIP17DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP17.deploy(this.caver, tokenInfo, sendOptions, wallet);
    }

    /**
     * Detects which interface the KIP-17 token contract supports.<p>
     * Example :
     * <pre>{@code
     * Map<String, Boolean> result = KIP17.detectInterface();
     * result.get(KIP17.INTERFACE_ID_IKIP17);
     * result.get(KIP17.INTERFACE_ID_IKIP17_METADATA);
     * result.get(KIP17.INTERFACE_ID_IKIP17_ENUMERABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_MINTABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_METADATA_MINTABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_BURNABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_PAUSABLE);
     * }</pre>
     *
     * @param contractAddress A contract instance
     * @return Map&lt;String, Boolean&gt;
     */
    public Map<String, Boolean> detectInterface(String contractAddress) {
        return KIP17.detectInterface(this.caver, contractAddress);
    }
}
