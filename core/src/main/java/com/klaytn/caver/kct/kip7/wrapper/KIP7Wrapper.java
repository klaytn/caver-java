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

package com.klaytn.caver.kct.kip7.wrapper;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Map;

/**
 * Representing a KIP7Wrapper
 * 1. This class wraps all static methods of KIP7
 * 2. This class should be accessed via `caver.kct.kip7`
 */
public class KIP7Wrapper {
    /**
     * A Caver instance
     * this should be injected from KCTWrapper(= `caver.kct`)
     */
    private Caver caver;

    /**
     * Creates a KIP7Wrapper instance
     * @param caver A Caver instance
     */
    public KIP7Wrapper(Caver caver) {
        this.caver = caver;
    }

    /**
     * Creates a KIP7 instance
     * @return KIP7
     * @throws IOException
     */
    public KIP7 create() throws IOException {
        return new KIP7(this.caver);
    }

    /**
     * Creates a KIP7 instance
     * @param contractAddress A contract address
     * @return KIP7
     * @throws IOException
     */
    public KIP7 create(String contractAddress) throws IOException {
        return new KIP7(this.caver, contractAddress);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param deployer A deployer's address.
     * @param name A KIP-7 contract name.
     * @param symbol A KIP-7 contract symbol.
     * @param decimals A KIP-7 contract decimals.
     * @param initialSupply A KIP-7 contract initial supply.
     * @return KIP7
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP7 deploy(String deployer, String name, String symbol, int decimals, BigInteger initialSupply) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP7.deploy(this.caver, deployer, name, symbol, decimals, initialSupply);
    }

    /**
     * Deploy KIP-7 contract.<p>
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
     *     KIP7DeployParams tokenInfo = new KIP7DeployParams(name, symbol, decimals, initialSupply);
     *     caver.kct.kip7.deploy(sendOptions, name, symbol, decimals, initialSupply);
     * </code>
     * </pre>
     * @param sendOptions The send options to deploy a contract.
     * @param name A KIP-7 contract name.
     * @param symbol A KIP-7 contract symbol.
     * @param decimals A KIP-7 contract decimals.
     * @param initialSupply A KIP-7 contract initial supply.
     * @return KIP7
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public KIP7 deploy(SendOptions sendOptions, String name, String symbol, int decimals, BigInteger initialSupply) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return KIP7.deploy(this.caver, sendOptions, name, symbol, decimals, initialSupply);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance.
     * @param deployer A deployer's address.
     * @param name A KIP-7 contract name.
     * @param symbol A KIP-7 contract symbol.
     * @param decimals A KIP-7 contract decimals.
     * @param initialSupply A KIP-7 contract initial supply.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP7 deploy(String deployer, String name, String symbol, int decimals, BigInteger initialSupply, IWallet wallet) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return KIP7.deploy(this.caver, deployer, name, symbol, decimals, initialSupply, wallet);
    }

    /**
     * Deploy KIP-7 contract. <p>
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
     *     KIP7DeployParams tokenInfo = new KIP7DeployParams(name, symbol, decimals, initialSupply);
     *     KIP7 kip7 = caver.kct.kip7.deploy(sendOptions, name, symbol, decimals, initialSupply, caver.getWallet());
     * </code>
     * </pre>
     * @param sendOptions The send options to deploy a contract.
     * @param name A KIP-7 contract name.
     * @param symbol A KIP-7 contract symbol.
     * @param decimals A KIP-7 contract decimals.
     * @param initialSupply A KIP-7 contract initial supply.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws NoSuchMethodException
     * @throws TransactionException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public KIP7 deploy(SendOptions sendOptions, String name, String symbol, int decimals, BigInteger initialSupply, IWallet wallet) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return KIP7.deploy(this.caver, sendOptions, name, symbol, decimals, initialSupply, wallet);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param deployer A deployer's address
     * @return KIP7
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP7 deploy(KIP7DeployParams tokenInfo, String deployer) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return KIP7.deploy(this.caver, tokenInfo, deployer);
    }

    /**
     * Deploy KIP-7 contract.<p>
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
     *     KIP7DeployParams tokenInfo = new KIP7DeployParams(name, symbol, decimals, initialSupply);
     *     KIP7 kip7 = caver.kct.kip7.deploy(tokenInfo, sendOptions);
     * </code>
     * </pre>
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @return KIP7
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public KIP7 deploy(KIP7DeployParams tokenInfo, SendOptions sendOptions) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return KIP7.deploy(this.caver, tokenInfo, sendOptions);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param deployer A deployer's address
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP7 deploy(KIP7DeployParams tokenInfo, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP7.deploy(this.caver, tokenInfo, deployer, wallet);
    }

    /**
     * Deploy KIP-7 contract.<p>
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
     *     KIP7DeployParams tokenInfo = new KIP7DeployParams(name, symbol, decimals, initialSupply);
     *     KIP7 kip7 = caver.kct.kip7.deploy(tokenInfo, sendOptions, caver.getWallet());
     * </code>
     * </pre>
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public KIP7 deploy(KIP7DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP7.deploy(this.caver, tokenInfo, sendOptions, wallet);
    }

    /**
     * Detects which interface the KIP-7 token contract supports.<p>
     * Example :
     * <pre>{@code
     * Map<String, Boolean> result = KIP7.detectInterface();
     * result.get(KIP7.INTERFACE_ID_IKIP7);
     * result.get(KIP7.INTERFACE_ID_IKIP7_BURNABLE);
     * result.get(KIP7.INTERFACE_ID_IKIP7_METADATA);
     * result.get(KIP7.INTERFACE_ID_IKIP7_MINTABLE);
     * result.get(KIP7.INTERFACE_ID_IKIP7_PAUSABLE);
     * }</pre>
     *
     * @param contractAddress A contract address.
     * @return Map&lt;String, Boolean&gt;
     */
    public Map<String, Boolean> detectInterface(String contractAddress) {
        return KIP7.detectInterface(this.caver, contractAddress) ;
    }
}
