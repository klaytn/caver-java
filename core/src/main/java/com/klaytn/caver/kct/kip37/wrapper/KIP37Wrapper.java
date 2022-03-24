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
import com.klaytn.caver.kct.kip37.KIP37DeployParams;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
     * Creates a KIP37 instance.
     * <pre>Example
     * {@code
     * KIP37 kip37 = caver.kct.kip37.create();
     * }
     * </pre>
     *
     * @return KIP37
     * @throws IOException
     */
    public KIP37 create() throws IOException {
        return KIP37.create(this.caver);
    }

    /**
     * Creates a KIP37 instance.
     * <pre>Example
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * KIP37 kip37 = caver.kct.kip37.create(contractAddress);
     * }
     * </pre>
     *
     * @param contractAddress A contract address
     * @return KIP37
     * @throws IOException
     */
    public KIP37 create(String contractAddress) throws IOException {
        return KIP37.create(this.caver, contractAddress);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`. See {@link com.klaytn.caver.wallet.KeyringContainer} and {@link IWallet}.
     * <pre>Example :
     * {@code
     * String deployAddress = "0x{deployAddress}";
     * String uri = "uri";
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(uri, deployerAddress);
     * }
     * </pre>
     *
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
     * The deployer's keyring should be existed in `caver.wallet`. See {@link com.klaytn.caver.wallet.KeyringContainer} and {@link IWallet}. <p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in SendOptions.
     * <pre> Example :
     * {@code
     * String uri = "uri";
     *
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom("deployer address");
     * sendOptions.setGas(BigInteger.valueOf(gas value));
     * sendOptions.setFeeDelegation(true);
     * sendOptions.setFeePayer("fee payer address");
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(uri, sendOptions);
     * }
     * </pre>
     *
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
     * <pre>Example :
     * {@code
     * String deployAddress = "0x{deployAddress}";
     * String uri = "uri";
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(uri, deployerAddress, caver.getWallet());
     * }
     * </pre>
     *
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
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in SendOptions.
     * <pre> Example :
     * {@code
     * String uri = "uri";
     *
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom("deployer address");
     * sendOptions.setGas(BigInteger.valueOf(gas value));
     * sendOptions.setFeeDelegation(true);
     * sendOptions.setFeePayer("fee payer address");
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(uri, sendOptions, caver.getWallet());
     * }
     * </pre>
     *
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
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * <pre>Example :
     * {@code
     * String deployAddress = "0x{deployAddress}";
     * String uri = "uri";
     * KIP37DeployParams deployInfo = new KIP37DeployParams(uri);
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(deployInfo, deployAddress);
     * }
     * </pre>
     *
     * @param tokenInfo The KIP-37 contract's deploy parameter values.
     * @param deployer A deployer's address
     * @return KIP37
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public KIP37 deploy(KIP37DeployParams tokenInfo, String deployer) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return KIP37.deploy(this.caver, tokenInfo, deployer, this.caver.getWallet());
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * If you want to deploy a contract using fee delegation transaction, you can create and send a fee delegated transaction through setting a fee delegation field in SendOptions.
     * <pre>Example :
     * {@code
     * String uri = "uri";
     * KIP37DeployParams deployInfo = new KIP37DeployParams(uri);
     *
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom("deployer address");
     * sendOptions.setGas(BigInteger.valueOf(gas value));
     * sendOptions.setFeeDelegation(true);
     * sendOptions.setFeePayer("fee payer address");
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(deployInfo, sendOptions);
     * }
     * </pre>
     *
     * @param tokenInfo The KIP-37 contract's deploy parameter values.
     * @param sendOptions The send options to deploy a contract.
     * @return KIP37
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public KIP37 deploy(KIP37DeployParams tokenInfo, SendOptions sendOptions) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return KIP37.deploy(this.caver, tokenInfo, sendOptions, this.caver.getWallet());
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * <pre>Example :
     * {@code
     * String deployAddress = "0x{deployAddress}";
     * String uri = "uri";
     * KIP37DeployParams deployInfo = new KIP37DeployParams(uri);
     *
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(deployInfo, sendOptions, caver.getWallet);
     * }
     * </pre>
     *
     * @param tokenInfo The KIP-37 contract's deploy parameter values.
     * @param deployer A deployer's address
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
    public KIP37 deploy(KIP37DeployParams tokenInfo, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return KIP37.deploy(caver, tokenInfo, deployer, wallet);
    }

    /**
     * Deploy a KIP-37 contract.<p>
     * The deployer's keyring should be existed in `caver.wallet`.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * <pre>Example :
     * {@code
     * String uri = "uri";
     * KIP37DeployParams deployInfo = new KIP37DeployParams(uri);
     *
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom("deployer address");
     * sendOptions.setGas(BigInteger.valueOf(gas value));
     * sendOptions.setFeeDelegation(true);
     * sendOptions.setFeePayer("fee payer address");
     *
     * KIP37 kip37 = caver.kct.kip37.deploy(deployInfo, sendOptions, caver.getWallet());
     * }
     * </pre>
     *
     * @param tokenInfo The KIP-37 contract's deploy parameter values.
     * @param sendOptions The send options to deploy a contract.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP37
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public KIP37 deploy(KIP37DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return KIP37.deploy(this.caver, tokenInfo, sendOptions, wallet);
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
