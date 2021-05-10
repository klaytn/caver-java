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
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
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
     * Deploy KIP-7 contract.
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
        return KIP7.deploy(this.caver, deployer, name, symbol, decimals, initialSupply, this.caver.getWallet());
    }

    /**
     * Deploy KIP-7 contract.
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
        return KIP7.deploy(this.caver, tokenInfo, deployer, this.caver.getWallet());
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
