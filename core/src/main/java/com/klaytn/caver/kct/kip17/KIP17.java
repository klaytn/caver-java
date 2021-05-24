/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.kct.kip17;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip13.KIP13;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.IWallet;
import com.klaytn.caver.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class KIP17 extends Contract {

    public static final String FUNCTION_ADD_MINTER = "addMinter";
    public static final String FUNCTION_ADD_PAUSER = "addPauser";
    public static final String FUNCTION_APPROVE = "approve";
    public static final String FUNCTION_BALANCE_OF = "balanceOf";
    public static final String FUNCTION_BURN = "burn";
    public static final String FUNCTION_GET_APPROVED = "getApproved";
    public static final String FUNCTION_IS_APPROVED_FOR_ALL = "isApprovedForAll";
    public static final String FUNCTION_IS_MINTER = "isMinter";
    public static final String FUNCTION_IS_PAUSER = "isPauser";
    public static final String FUNCTION_MINT = "mint";
    public static final String FUNCTION_MINT_WITH_TOKEN_URI = "mintWithTokenURI";
    public static final String FUNCTION_NAME = "name";
    public static final String FUNCTION_OWNER_OF = "ownerOf";
    public static final String FUNCTION_PAUSE = "pause";
    public static final String FUNCTION_PAUSED = "paused";
    public static final String FUNCTION_RENOUNCE_MINTER = "renounceMinter";
    public static final String FUNCTION_RENOUNCE_PAUSER = "renouncePauser";
    public static final String FUNCTION_SAFE_TRANSFER_FROM = "safeTransferFrom";
    public static final String FUNCTION_SET_APPROVAL_FOR_ALL = "setApprovalForAll";
    public static final String FUNCTION_SUPPORTS_INTERFACE = "supportsInterface";
    public static final String FUNCTION_SYMBOL = "symbol";
    public static final String FUNCTION_TOKEN_BY_INDEX = "tokenByIndex";
    public static final String FUNCTION_TOKEN_OF_OWNER_BY_INDEX = "tokenOfOwnerByIndex";
    public static final String FUNCTION_TOKEN_URI = "tokenURI";
    public static final String FUNCTION_TOTAL_SUPPLY = "totalSupply";
    public static final String FUNCTION_TRANSFER_FROM = "transferFrom";
    public static final String FUNCTION_UNPAUSE = "unpause";

    public enum INTERFACE {
        IKIP17("IKIP17", "0x80ac58cd"),
        IKIP17_METADATA("IKIP17Metadata", "0x5b5e139f"),
        IKIP17_ENUMERABLE("IKIP17Enumerable", "0x780e9d63"),
        IKIP17_MINTABLE("IKIP17Mintable", "0xeab83e20"),
        IKIP17_METADATA_MINTABLE("IKIP17MetadataMintable", "0xfac27f46"),
        IKIP17_BURNABLE("IKIP17Burnable", "0x42966c68"),
        IKIP17_PAUSABLE("IKIP17Pausable", "0x4d5507ff");

        String name;
        String id;

        INTERFACE(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * Creates a KIP17 instance.
     * @param caver A Caver instance.
     * @throws IOException
     */
    public KIP17(Caver caver) throws IOException {
        super(caver, KIP17ConstantData.ABI);
    }

    /**
     * Creates a KIP17 instance
     * @param caver A Caver instance.
     * @param contractAddress A contract address
     * @throws IOException
     */
    public KIP17(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP17ConstantData.ABI, contractAddress);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * The deployer's keyring should be added in `caver.wallet`.
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, String deployer, String name, String symbol) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        KIP17DeployParams deployParams = new KIP17DeployParams(name, symbol);
        return deploy(caver, deployParams, deployer);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * The deployer's keyring should be added in `caver.wallet`. <p>
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
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, SendOptions sendOptions, String name, String symbol) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        KIP17DeployParams deployParams = new KIP17DeployParams(name, symbol);
        return deploy(caver, deployParams, sendOptions);
    }

    /**
     * Deploy a KIP-17 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, String deployer, String name, String symbol, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        KIP17DeployParams deployParams = new KIP17DeployParams(name, symbol);
        return deploy(caver, deployParams, deployer, wallet);
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
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, SendOptions sendOptions, String name, String symbol, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        KIP17DeployParams deployParams = new KIP17DeployParams(name, symbol);
        return deploy(caver, deployParams, sendOptions, wallet);
    }

    /**
     * Deploy KIP17 contract.<p>
     * The deployer's keyring should be added in `caver.wallet`.
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, KIP17DeployParams tokenInfo, String deployer) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, tokenInfo, deployer, caver.getWallet());
    }

    /**
     * Deploy KIP-17 contract.<p>
     * The deployer's keyring should be added in `caver.wallet`. <p>
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
     * @param caver A Caver instance.
     * @param tokenInfo The KIP-17 contract's deploy parameter values.
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
    public static KIP17 deploy(Caver caver, KIP17DeployParams tokenInfo, SendOptions sendOptions) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, tokenInfo, sendOptions, caver.getWallet());
    }

    /**
     * Deploy KIP17 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance.
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
    public static KIP17 deploy(Caver caver, KIP17DeployParams tokenInfo, String deployer, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = new SendOptions();
        sendOptions.setFrom(deployer);
        sendOptions.setGas(BigInteger.valueOf(6500000));

        return deploy(caver, tokenInfo, sendOptions, wallet);
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
     * @param caver A Caver instance.
     * @param tokenInfo The KIP-17 contract's deploy parameter values.
     * @param sendOptions The send options to deploy a contract.
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
    public static KIP17 deploy(Caver caver, KIP17DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        List deployArgument = Arrays.asList(tokenInfo.getName(), tokenInfo.getSymbol());
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP17ConstantData.BINARY, deployArgument);

        KIP17 kip17 = new KIP17(caver);
        kip17.setWallet(wallet);
        kip17.deploy(contractDeployParams, sendOptions);

        return kip17;
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
     * @param caver A Caver instance.
     * @param contractAddress A contract instance
     * @return Map&lt;String, Boolean&gt;
     */
    public static Map<String, Boolean> detectInterface(Caver caver, String contractAddress) {
        KIP13 kip13;
        try {
            kip13 = new KIP13(caver, contractAddress);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if(!kip13.isImplementedKIP13Interface()) {
            throw new RuntimeException("This contract does not support KIP-13.");
        }

        Map<String, Boolean> result = new HashMap<>();
        Arrays.stream(INTERFACE.values())
                .forEach(element -> result.put(element.getName(), kip13.sendQuery(element.getId())));
        return result;
    }

    /**
     * Copy instance
     * @return KIP17
     */
    public KIP17 clone() {
        try {
            KIP17 kip17 = new KIP17(this.getCaver(), this.getContractAddress());
            kip17.setWallet(this.getWallet());

            return kip17;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Copy instance with token address
     * @param tokenAddress A KIP-17 token address
     * @return KIP17
     */
    public KIP17 clone(String tokenAddress) {
        try {
            KIP17 kip17 = new KIP17(this.getCaver(), tokenAddress);
            kip17.setWallet(this.getWallet());

            return kip17;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Detects which interface the KIP-17 token contract supports.<p>
     * Example :
     * <pre>{@code
     * KIP17 kip17 = new KIP17("0x{contract_address}");
     * Map<String, Boolean> result = kip17.detectInterface();
     * result.get(KIP17.INTERFACE_ID_IKIP17);
     * result.get(KIP17.INTERFACE_ID_IKIP17_METADATA);
     * result.get(KIP17.INTERFACE_ID_IKIP17_ENUMERABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_MINTABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_METADATA_MINTABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_BURNABLE);
     * result.get(KIP17.INTERFACE_ID_IKIP17_PAUSABLE);
     * }</pre>
     * @return Map&lt;String, Boolean&gt;
     */
    public Map<String, Boolean> detectInterface() {
        return detectInterface(this.getCaver(), this.getContractAddress());
    }

    /**
     * Call method "supportsInterface" in KIP-13 standard contract.
     * @param interfaceId interface identifier
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean supportInterface(String interfaceId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SUPPORTS_INTERFACE).call(Arrays.asList(interfaceId), callObject);

        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "name" in KIP-17 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String name() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_NAME).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    /**
     * Call method "symbol" in KIP-17 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String symbol() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SYMBOL).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    /**
     * Call method "tokenURI" in KIP-17 standard contract.
     * @param tokenId
     * @return
     */
    public String tokenURI(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_TOKEN_URI).call(Arrays.asList(tokenId), callObject);

        return (String)result.get(0).getValue();
    }

    /**
     * Call method "totalSupply" in KIP-17 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger totalSupply() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_TOTAL_SUPPLY).call(null, callObject);

        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Call method "tokenOwnerByIndex" in KIP-17 standard contract.
     * @param owner An account where we are interested in NFTs owned by them
     * @param index A counter less than "balanceOf(owner)"
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger tokenOwnerByIndex(String owner, BigInteger index) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_TOKEN_OF_OWNER_BY_INDEX).call(Arrays.asList(owner, index), callObject);

        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Call method "tokenByIndex" in KIP-17 standard contract.
     * @param index A counter less than "totalSupply"
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger tokenByIndex(BigInteger index) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_TOKEN_BY_INDEX).call(Arrays.asList(index), callObject);
        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Call method "balanceOf" in KIP-17 standard contract.
     * @param account An account for whom to query the balance
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger balanceOf(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_BALANCE_OF).call(Arrays.asList(account), callObject);
        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Call method "ownerOf" in KIP-17 standard contract.
     * @param tokenId The identifier of NFT
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String ownerOf(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_OWNER_OF).call(Arrays.asList(tokenId), callObject);
        return (String)result.get(0).getValue();
    }

    /**
     * Call method "getApproved" in KIP-17 standard contract.
     * @param tokenId The identifier of NFT
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String getApproved(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_GET_APPROVED).call(Arrays.asList(tokenId), callObject);
        return (String)result.get(0).getValue();
    }

    /**
     * Call method "isApprovedForAll" in KIP-17 standard contract.
     * @param owner The account that owns the NFTs
     * @param operator The account that act on behalf of the owner
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isApprovedForAll(String owner, String operator) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_IS_APPROVED_FOR_ALL).call(Arrays.asList(owner, operator), callObject);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "isMinter" in KIP-17 standard contract.
     * @param account The account to check the minting permission
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_IS_MINTER).call(Arrays.asList(account), callObject);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "paused" in KIP-17 standard contract.
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean paused() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_PAUSED).call(null, callObject);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "isPauser" in KIP-17 standard contract
     * @param account The account to check the pausing permission
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNCTION_IS_PAUSER).call(Arrays.asList(account), callObject);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Execute method "approve" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The new account approved NFT controller
     * @param tokenId The NFT ID to approve
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData approve(String to, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return approve(to, tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "approve" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The new account approved NFT controller
     * @param tokenId The NFT ID to approve
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData approve(String to, BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_APPROVE, Arrays.asList(to, tokenId));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_APPROVE).send(Arrays.asList(to, tokenId), sendOption);
        return receiptData;
    }

    /**
     * Execute method "setApprovedForAll" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to Account to add the set of authorized operators.
     * @param approved True if the operator is approved, false to revoke approval
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData setApproveForAll(String to, boolean approved) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return setApproveForAll(to, approved, this.getDefaultSendOptions());
    }

    /**
     * Execute method "setApprovedForAll" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to Account to add the set of authorized operators.
     * @param approved True if the operator is approved, false to revoke approval
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData setApproveForAll(String to, boolean approved, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_SET_APPROVAL_FOR_ALL, Arrays.asList(to, approved));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SET_APPROVAL_FOR_ALL).send(Arrays.asList(to, approved), sendOption);
        return receiptData;
    }

    /**
     * Execute method "transferFrom" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transferFrom(String from, String to, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return transferFrom(from, to, tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "transferFrom" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transferFrom(String from, String to, BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_TRANSFER_FROM, Arrays.asList(from, to, tokenId));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER_FROM).send(Arrays.asList(from, to, tokenId), sendOption);
        return receiptData;
    }

    /**
     * Execute method "safeTransferFrom" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransferFrom(from, to, tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "safeTransferFrom" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(from, to, tokenId));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(from, to, tokenId), sendOption);
        return receiptData;
    }

    /**
     * Execute method "safeTransferFrom" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @param data Additional data with no specified format, sent in call to "to"
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransferFrom(from, to, tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "safeTransferFrom" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param from The current owner of the NFT
     * @param to The new owner
     * @param tokenId The NFT identifier to transfer
     * @param data Additional data with no specified format, sent in call to "to"
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(from, to, tokenId, data));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(from, to, tokenId, data), sendOption);
        return receiptData;
    }

    /**
     * Execute method "addMinter" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the minting permission
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return addMinter(account, this.getDefaultSendOptions());
    }

    /**
     * Execute method "addMinter" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the minting permission
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addMinter(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_ADD_MINTER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_MINTER).send(Arrays.asList(account), sendOption);
        return receiptData;
    }

    /**
     * Execute method "renounceMinter" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renounceMinter() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renounceMinter(this.getDefaultSendOptions());
    }

    /**
     * Execute method "renounceMinter" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renounceMinter(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_MINTER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_RENOUNCE_MINTER).send(null, sendOption);
        return receiptData;
    }

    /**
     * Execute method "mint" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The account that will receive the minted token
     * @param tokenId The NFT identifier to be minted
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return mint(to, tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "mint" in KIP-17 standard contract.
     * Caller must have "Minter" permission
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The account that will receive the minted token
     * @param tokenId The NFT identifier to be minted
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_MINT, Arrays.asList(to, tokenId));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_MINT).send(Arrays.asList(to, tokenId), sendOption);
        return receiptData;
    }

    /**
     * Execute method "mintWithTokenURI" in KIP-17 standard contract.
     * Caller must have Minter permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The account that will receive the minted token
     * @param tokenId The NFT identifier to be minted
     * @param tokenURI The NFT URI(Uniform Resource Identifier) to be minted
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintWithTokenURI(String to, BigInteger tokenId, String tokenURI) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return mintWithTokenURI(to, tokenId, tokenURI, this.getDefaultSendOptions());
    }

    /**
     * Execute method "mintWithTokenURI" in KIP-17 standard contract.
     * Caller must have Minter permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param to The account that will receive the minted token
     * @param tokenId The NFT identifier to be minted
     * @param tokenURI The NFT URI(Uniform Resource Identifier) to be minted
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintWithTokenURI(String to, BigInteger tokenId, String tokenURI, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_MINT_WITH_TOKEN_URI, Arrays.asList(to, tokenId, tokenURI));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_MINT_WITH_TOKEN_URI).send(Arrays.asList(to, tokenId, tokenURI), sendOption);
        return receiptData;
    }

    /**
     * Execute method "burn" in KIP-17 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param tokenId The NFT identifier to be minted
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burn(tokenId, this.getDefaultSendOptions());
    }

    /**
     * Execute method "burn" in KIP-17 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param tokenId The NFT identifier to be minted
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_BURN, Arrays.asList(tokenId));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_BURN).send(Arrays.asList(tokenId), sendOption);
        return receiptData;
    }

    /**
     * Execute method "pause" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return pause(this.getDefaultSendOptions());
    }

    /**
     * Execute method "pause" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_PAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_PAUSE).send(null, sendOption);
        return receiptData;
    }

    /**
     * Execute method "unpause" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return unpause(this.getDefaultSendOptions());
    }

    /**
     * Execute method "unpause" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_UNPAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_UNPAUSE).send(null, sendOption);
        return receiptData;
    }

    /**
     * Execute method "addPauser" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the pausing permission
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return addPauser(account, this.getDefaultSendOptions());
    }

    /**
     * Execute method "addPauser" in KIP-17 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the pausing permission
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addPauser(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_ADD_PAUSER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_PAUSER).send(Arrays.asList(account), sendOption);
        return receiptData;
    }

    /**
     * Execute method "renouncePauser" in KIP-17 standard contract.
     * Caller must have Pauser permission
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renouncePauser() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renouncePauser(this.getDefaultSendOptions());
    }

    /**
     * Execute method "renouncePauser" in KIP-17 standard contract.
     * Caller must have Pauser permission
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renouncePauser(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_PAUSER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_RENOUNCE_PAUSER).send(null, sendOption);
        return receiptData;
    }



    private static SendOptions determineSendOptions(KIP17 kip17, SendOptions sendOptions, String functionName, List<Object> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;

        String from = kip17.getDefaultSendOptions().getFrom();
        String gas = kip17.getDefaultSendOptions().getGas();
        String value = kip17.getDefaultSendOptions().getValue();
        Boolean feeDelegation = kip17.getDefaultSendOptions().getFeeDelegation();
        String feePayer = kip17.getDefaultSendOptions().getFeePayer();
        String feeRatio = kip17.getDefaultSendOptions().getFeeRatio();

        if(sendOptions.getFrom() != null) {
            from = sendOptions.getFrom();
        }

        if(sendOptions.getGas() == null) {
            //If passed gas fields in sendOptions and defaultSendOptions is null, it estimate gas.
            if(gas == null) {
                CallObject callObject = CallObject.createCallObject(sendOptions.getFrom());
                BigInteger estimateGas = estimateGas(kip17, functionName, callObject, argument);
                gas = Numeric.toHexStringWithPrefix(estimateGas);
            }
        } else {
            gas = sendOptions.getGas();
        }

        if(!sendOptions.getValue().equals("0x0")) {
            value = sendOptions.getValue();
        }

        if(sendOptions.getFeeDelegation() != null) {
            feeDelegation = sendOptions.getFeeDelegation();
        }

        if(sendOptions.getFeePayer() != null) {
            feePayer = sendOptions.getFeePayer();
        }

        if(sendOptions.getFeeRatio() != null) {
            feeRatio = sendOptions.getFeeRatio();
        }

        if((feeDelegation == null || !feeDelegation) && (feePayer != null || feeRatio != null)) {
            throw new IllegalArgumentException("To use fee delegation with KCT, please set 'feeDelegation' field to true.");
        }

        newSendOptions = new SendOptions();
        newSendOptions.setFrom(from);
        newSendOptions.setGas(gas);
        newSendOptions.setValue(value);
        newSendOptions.setFeeDelegation(feeDelegation);
        newSendOptions.setFeePayer(feePayer);
        newSendOptions.setFeeRatio(feeRatio);

        return newSendOptions;
    }

    /**
     * Estimates the gas to execute the contract's method.
     * @param kip17 A KIP-17 instance.
     * @param functionName A KIP-17 contract's method.
     * @param callObject A CallObject instance to execute estimateGas
     * @param arguments A arguments to execute contract's method.
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    private static BigInteger estimateGas(KIP17 kip17, String functionName, CallObject callObject, List<Object> arguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String gas = kip17.getMethod(functionName).estimateGas(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.5)).toBigInteger();

        return gasInteger;
    }
}
