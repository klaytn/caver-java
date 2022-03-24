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

package com.klaytn.caver.kct.kip37;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Address;
import com.klaytn.caver.abi.datatypes.DynamicArray;
import com.klaytn.caver.abi.datatypes.NumericType;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.abi.datatypes.generated.Uint256;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip13.KIP13;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The KIP37 class that helps you easily handle a smart contract that implements KIP-37 as a Java instance on the Klaytn blockchain platform.<p>
 * This KIP-37 contract source code based on <a href="https://github.com/klaytn/klaytn-contracts">Klaytn-contracts</a>.
 * Static methods and constructor is accessed via `caver.kct.kip37`.
 * @see com.klaytn.caver.kct.kip7.wrapper.KIP7Wrapper
 * @see com.klaytn.caver.kct.wrapper.KCTWrapper
 * @see Contract
 */
public class KIP37 extends Contract {
    public static final String FUNCTION_URI = "uri";
    public static final String FUNCTION_BALANCE_OF = "balanceOf";
    public static final String FUNCTION_BALANCE_OF_BATCH = "balanceOfBatch";
    public static final String FUNCTION_SET_APPROVED_FOR_ALL = "setApprovalForAll";
    public static final String FUNCTION_IS_APPROVED_FOR_ALL = "isApprovedForAll";
    public static final String FUNCTION_TOTAL_SUPPLY = "totalSupply";
    public static final String FUNCTION_SAFE_TRANSFER_FROM = "safeTransferFrom";
    public static final String FUNCTION_SAFE_BATCH_TRANSFER_FROM = "safeBatchTransferFrom";
    public static final String FUNCTION_BURN = "burn";
    public static final String FUNCTION_BURN_BATCH = "burnBatch";
    public static final String FUNCTION_CREATE = "create";
    public static final String FUNCTION_MINT = "mint";
    public static final String FUNCTION_MINT_BATCH = "mintBatch";
    public static final String FUNCTION_PAUSED = "paused";
    public static final String FUNCTION_PAUSE = "pause";
    public static final String FUNCTION_UNPAUSE = "unpause";
    public static final String FUNCTION_IS_PAUSER = "isPauser";
    public static final String FUNCTION_ADD_PAUSER = "addPauser";
    public static final String FUNCTION_RENOUNCE_PAUSER = "renouncePauser";
    public static final String FUNCTION_IS_MINTER = "isMinter";
    public static final String FUNCTION_ADD_MINTER = "addMinter";
    public static final String FUNCTION_RENOUNCE_MINTER = "renounceMinter";
    public static final String FUNCTION_SUPPORTS_INTERFACE = "supportsInterface";

    public enum INTERFACE {
        IKIP37("IKIP37", "0x6433ca1f"),
        IKIP37_METADATA("IKIP37Metatdata", "0x0e89341c"),
        IKIP37_MINTABLE("IKIP37Mintable", "0xdfd9d9ec"),
        IKIP37_BURNABLE("IKIP37Burnable", "0x9e094e9e"),
        IKIP37_PAUSABLE("IKIP37Pausable", "0x0e8ffdb7");

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
     * Creates a KIP37 instance.
     * <pre>Example
     * {@code
     * KIP37 kip37 = caver.kct.kip37.create();
     * }
     * </pre>
     *
     * @param caver A Caver instance.
     * @return KIP37
     * @throws IOException
     */
    public static KIP37 create(Caver caver) throws IOException {
        return new KIP37(caver);
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
     * @param caver A Caver instance.
     * @param contractAddress A contract address
     * @return KIP37
     * @throws IOException
     */
    public static KIP37 create(Caver caver, String contractAddress) throws IOException {
        return new KIP37(caver, contractAddress);
    }

    /**
     * Creates a KIP37 instance.
     * @param caver A Caver instance.
     * @throws IOException
     */
    public KIP37(Caver caver) throws IOException {
        super(caver, KIP37ConstantData.ABI);
    }

    /**
     * Creates a KIP37 instance.
     * @param caver A Caver instance
     * @param contractAddress A contract address.
     * @throws IOException
     */
    public KIP37(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP37ConstantData.ABI, contractAddress);
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, String uri, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, uri, deployer, caver.getWallet());
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, String uri, SendOptions sendOptions) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, uri, sendOptions, caver.getWallet());
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, String uri, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = new SendOptions();
        sendOptions.setFrom(deployer);
        sendOptions.setGas(BigInteger.valueOf(8000000));

        return deploy(caver, uri, sendOptions, wallet);
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
     * @param caver A Caver instance.
     * @param uri The URI for token type.
     * @param sendOptions The send options to deploy a contract.
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
    public static KIP37 deploy(Caver caver, String uri, SendOptions sendOptions, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, new KIP37DeployParams(uri), sendOptions, wallet);
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, KIP37DeployParams tokenInfo, String deployer) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return deploy(caver, tokenInfo, deployer, caver.getWallet());
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, KIP37DeployParams tokenInfo, SendOptions sendOptions) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return deploy(caver, tokenInfo, sendOptions, caver.getWallet());
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
     * @param caver A Caver instance.
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
    public static KIP37 deploy(Caver caver, KIP37DeployParams tokenInfo, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = new SendOptions();
        sendOptions.setFrom(deployer);
        sendOptions.setGas(BigInteger.valueOf(8000000));

        return deploy(caver, tokenInfo, sendOptions, wallet);
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
     * @param caver A Caver instance
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
    public static KIP37 deploy(Caver caver, KIP37DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP37ConstantData.BINARY, tokenInfo.getUri());

        KIP37 kip37 = caver.kct.kip37.create();
        kip37.setWallet(wallet);
        kip37.deploy(contractDeployParams, sendOptions);

        return kip37;
    }

    /**
     * Copy instance.
     * <pre>Example :
     * {@code
     * KIP37 cloned = kip37.clone();
     * }
     * </pre>
     *
     * @return KIP37
     */
    public KIP37 clone() {
        try {
            KIP37 kip37 = new KIP37(this.getCaver(), this.getContractAddress());
            kip37.setWallet(this.getWallet());

            return kip37;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Copy instance with token address.
     * <pre>Example :
     * {@code
     * String contractAddress = "0x{contractAddress}";
     * KIP37 cloned = kip37.clone(contractAddress);
     * }
     * </pre>
     *
     * @param tokenAddress A KIP-37 token address
     * @return KIP37
     */
    public KIP37 clone(String tokenAddress) {
        try {
            KIP37 kip37 = new KIP37(this.getCaver(), tokenAddress);
            kip37.setWallet(this.getWallet());

            return kip37;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
     * Detects which interface the KIP-37 token contract supports.<p>
     * Example :
     * <pre>{@code
     * KIP37 kip37 = new KIP37("0x{contract_address}");
     * Map<String, Boolean> result = kip37.detectInterface();
     *
     * result.get(KIP37.INTERFACE_ID_IKIP37);
     * result.get(KIP37.INTERFACE_ID_IKIP37_BURNABLE);
     * result.get(KIP37.INTERFACE_ID_IKIP37_METADATA);
     * result.get(KIP37.INTERFACE_ID_IKIP37_MINTABLE);
     * result.get(KIP37.INTERFACE_ID_IKIP37_PAUSABLE);
     * }</pre>
     * @return Map&lt;String, Boolean&gt;
     */
    public Map<String, Boolean> detectInterface() {
        return detectInterface(this.getCaver(), this.getContractAddress());
    }

    /**
     * Get a URI(Uniform Resource Identifier) for a given token ID.
     * <pre>Example :
     * {@code
     * String tokenId = "0x1";
     * String uri = kip37.uri(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The token id(hex string)
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String uri(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return uri(Numeric.toBigInt(tokenId));
    }

    /**
     * Get a URI(Uniform Resource Identifier) for a given token ID.
     * <pre>Example :
     * {@code
     * BigInteger tokenId = BigInteger.ONE;
     * String uri = kip37.uri(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The token id(integer)
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String uri(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNCTION_URI, tokenId);

        String uri = (String)result.get(0).getValue();
        if(uri.contains("{id}")) {
            String hexTokenID = Numeric.toHexStringNoPrefixZeroPadded(tokenId, 64);
            uri = uri.replace("{id}", hexTokenID);
        }

        return uri;
    }

    /**
     * Get the balance of an account's tokens.
     * <pre>Example :
     * {@code
     * String account = "0x{accountAddress}";
     * String tokenId = "0x1";
     *
     * BigInteger balance = kip37.balanceOf(account, tokenId);
     * }
     * </pre>
     *
     * @param account The address of the token holder.
     * @param tokenId The ID of the token(hex string).
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger balanceOf(String account, String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return balanceOf(account, Numeric.toBigInt(tokenId));
    }

    /**
     * Get the balance of an account's tokens.
     * <pre>Example :
     * {@code
     * String account = "0x{accountAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     *
     * BigInteger balance = kip37.balanceOf(account, tokenId);
     * }
     * </pre>
     *
     * @param account The address of the token holder.
     * @param tokenId The ID of the token(integer).
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger balanceOf(String account, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNCTION_BALANCE_OF, account, tokenId);
        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Get the balance of multiple account / token pairs.
     * <pre>Example :
     * {@code
     * String[] accounts = new String[] {....};
     * String[] tokenIds = new String[] {....};
     *
     * List<BigInteger> balances = kip37.balanceOf(accounts, tokenIds);
     * }
     * </pre>
     *
     * @param accounts The addresses of the token holders.
     * @param tokenIds IDs of the tokens(hex string)
     * @return List
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<BigInteger> balanceOfBatch(String[] accounts, String[] tokenIds) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        BigInteger[] arr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return balanceOfBatch(accounts, arr);
    }

    /**
     * Get the balance of multiple account / token pairs.
     * <pre>Example :
     * {@code
     * String[] accounts = new String[] {....};
     * BigInteger[] tokenIds = new BigInteger[] {....};
     *
     * List<BigInteger> balances = kip37.balanceOf(accounts, tokenIds);
     * }
     * </pre>
     *
     * @param accounts The addresses of the token holders.
     * @param tokenIds IDs of the tokens(integer)
     * @return List
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public List<BigInteger> balanceOfBatch(String[] accounts, BigInteger[] tokenIds) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNCTION_BALANCE_OF_BATCH, accounts, tokenIds);
        DynamicArray<Uint256> batchList = (DynamicArray<Uint256>)result.get(0);
        return batchList.getValue()
                .stream()
                .map(NumericType::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(hex string).
     * @param value Transfer amount.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, "", this.getDefaultSendOptions());
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(integer)
     * @param value Transfer amount.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, "", this.getDefaultSendOptions());
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, data);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(hex string).
     * @param value Transfer amount.
     * @param data Additional data with no specified format.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value, String data) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, data, this.getDefaultSendOptions());
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, data);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(integer)
     * @param value Transfer amount.
     * @param data Additional data with no specified format.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value, String data) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, data, this.getDefaultSendOptions());
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, snedOptions);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(hex string)
     * @param value Transfer amount.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, Numeric.toBigInt(tokenId), value, "", sendParam);
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, sendOptions);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(integer)
     * @param value Transfer amount.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, "", sendParam);
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, data, sendOptions);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(hex string)
     * @param value Transfer amount.
     * @param data Additional data with no specified format.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, Numeric.toBigInt(tokenId), value, data, sendParam);
    }

    /**
     * Transfers value amount of an 'id' from the 'from' address to the 'to' address specified.<p>
     * Caller must be approved to manage the tokens being transferred out of the 'from' account.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, data, sendOptions);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenId ID of the token type(integer)
     * @param value Transfer amount.
     * @param data Additional data with no specified format.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(from, to, tokenId, value, data));
        return this.send(sendOptions, FUNCTION_SAFE_TRANSFER_FROM, from, to, tokenId, value, data);
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, String, BigInteger, String)}. <p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenIds, amounts, data);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(hex string)
     * @param amounts The amount of transfer corresponding to each token ID
     * @param data Additional data with no specified format.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, data, this.getDefaultSendOptions());
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, BigInteger, BigInteger, String)}<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new String[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenIds, amounts);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(integer)
     * @param amounts The amount of transfer corresponding to each token ID
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, "", this.getDefaultSendOptions());
    }


    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, String, BigInteger, String)}. <p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, amounts);
     * }
     * </pre>
     *
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(hex string)
     * @param amounts The amount of transfer corresponding to each token ID
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, "", this.getDefaultSendOptions());
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, BigInteger, BigInteger, String)}<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, value, data);
     * }
     * </pre>
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(integer)
     * @param amounts The amount of transfer corresponding to each token ID
     * @param data Additional data with no specified format.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, data, this.getDefaultSendOptions());
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, String, BigInteger, String, SendOptions)}.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, amounts, sendOptions);
     * }
     * </pre>
     *
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(hex string)
     * @param amounts The amount of transfer corresponding to each token ID
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
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, "", sendParam);
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, BigInteger, BigInteger, String, SendOptions)}.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, amounts, sendOptions);
     * }
     * </pre>
     *
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(integer)
     * @param amounts The amount of transfer corresponding to each token ID
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
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, "", sendParam);
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, String, BigInteger, String, SendOptions)}.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, amounts, data, sendOptions);
     * }
     * </pre>
     *
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(hex string)
     * @param amounts The amount of transfer corresponding to each token ID
     * @param data Additional data with no specified format.
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
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return safeBatchTransferFrom(from, to, tokenIdArr, amounts, data, sendParam);
    }

    /**
     * Batch-operation version of {@link #safeTransferFrom(String, String, BigInteger, BigInteger, String, SendOptions)}.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String from = "0x{fromAddress}";
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{....};
     * BigInteger[] amounts = new BigInteger[]{....};
     * String data = "data";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.safeTransferFrom(from, to, tokenId, amounts, data, sendOptions);
     * }
     * </pre>
     *
     *
     * @param from Source address.
     * @param to Target address.
     * @param tokenIds IDs of token type(integer)
     * @param amounts The amount of transfer corresponding to each token ID
     * @param data Additional data with no specified format.
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
    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_BATCH_TRANSFER_FROM, Arrays.asList(from, to, tokenIds, amounts, data));
        return this.send(sendOptions, FUNCTION_SAFE_BATCH_TRANSFER_FROM, from, to, tokenIds, amounts, data);
    }

    /**
     * Enable or disable approval for a third party ("operator") to manage all of the caller's tokens.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String operator = "0x{operatorAddress}";
     * boolean approved = true;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.setApprovalForAll(operator, approved);
     * }
     * </pre>
     *
     * @param operator The address to add to the set of authorized operators.
     * @param approved True if the operator is approved, false to revoke approval
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData setApprovalForAll(String operator, boolean approved) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return setApprovalForAll(operator, approved, this.getDefaultSendOptions());
    }

    /**
     * Enable or disable approval for a third party ("operator") to manage all of the caller's tokens.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String operator = "0x{operatorAddress}";
     * boolean approved = true;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.setApprovalForAll(operator, approved, sendOptions);
     * }
     * </pre>
     *
     * @param operator The address to add to the set of authorized operators.
     * @param approved True if the operator is approved, false to revoke approval
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData setApprovalForAll(String operator, boolean approved, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNCTION_SET_APPROVED_FOR_ALL, Arrays.asList(operator, approved));
        return this.send(sendOption, FUNCTION_SET_APPROVED_FOR_ALL, operator, approved);
    }

    /**
     * Get the approval status of an operator for a given owner.
     * <pre>Example :
     * {@code
     * String owner = "0x{ownerAddress}";
     * String operator = "0x{operatorAddress}";
     *
     * boolean isApproved = kip37.isApprovedForALl(owner, operator);
     * }
     * </pre>
     * @param owner The address of the token owner.
     * @param operator The address of the authorized operator.
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isApprovedForAll(String owner, String operator) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNCTION_IS_APPROVED_FOR_ALL, owner, operator);
        return (Boolean)result.get(0).getValue();
    }

    /**
     * Get the total supply of the token type requested.
     * <pre>Example :
     * {@code
     * String tokenId = "0x1";
     * BigInteger supply = kip37.totalSupply(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The ID of the token.(hex string)
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger totalSupply(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return totalSupply(Numeric.toBigInt(tokenId));
    }

    /**
     * Get the total supply of the token type requested.
     * <pre>Example :
     * {@code
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger supply = kip37.totalSupply(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The ID of the token.(integer)
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger totalSupply(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNCTION_TOTAL_SUPPLY, tokenId);
        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String tokenId = "0x1";
     * BigInteger initialSupply = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply);
     * }
     * </pre>
     * @param tokenId The token id to create.(hex string)
     * @param initialSupply The amount of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, "", this.getDefaultSendOptions());
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger initialSupply = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(integer)
     * @param initialSupply The amount of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, "", this.getDefaultSendOptions());
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String tokenId = "0x1";
     * BigInteger initialSupply = BigInteger.ONE;
     * String uri = "uri";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(hex string)
     * @param initialSupply The amount of tokens being minted.
     * @param uri The token URI of the created token.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply, String uri) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, uri, this.getDefaultSendOptions());
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger initialSupply = BigInteger.ONE;
     * String uri = "uri";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri);
     * }
     * </pre>
     *
     *
     * @param tokenId The token id to create.(integer)
     * @param initialSupply The amount of tokens being minted.
     * @param uri The token URI of the created token.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply, String uri) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, uri, this.getDefaultSendOptions());
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String tokenId = "0x1";
     * BigInteger initialSupply = BigInteger.ONE;
     * String uri = "uri";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri, sendOptions);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(hex string)
     * @param initialSupply The amount of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(Numeric.toBigInt(tokenId), initialSupply, "", sendParam);
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger initialSupply = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri, sendOptions);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(integer)
     * @param initialSupply The amount of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, "", sendParam);
    }


    /**
     * Creates a new token type and assigns initialSupply to the minter.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String tokenId = "0x1";
     * BigInteger initialSupply = BigInteger.ONE;
     * String uri = "uri";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri, sendOptions);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(hex string)
     * @param initialSupply The amount of tokens being minted.
     * @param uri The token URI of the created token.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply, String uri, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(Numeric.toBigInt(tokenId), initialSupply, uri, sendParam);
    }

    /**
     * Creates a new token type and assigns initialSupply to the minter.
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger initialSupply = BigInteger.ONE;
     * String uri = "uri";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.create(tokenId, initialSupply, uri, sendOptions);
     * }
     * </pre>
     *
     * @param tokenId The token id to create.(integer)
     * @param initialSupply The amount of tokens being minted.
     * @param uri The token URI of the created token.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply, String uri, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_CREATE, Arrays.asList(tokenId, initialSupply, uri));
        return this.send(sendOptions, FUNCTION_CREATE, tokenId, initialSupply, uri);
    }

    /**
     * Mints token of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenId The token id to mint.(hex string)
     * @param value The quantity of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, String tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, Numeric.toBigInt(tokenId), value, this.getDefaultSendOptions());
    }

    /**
     * Mints token of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenId The token id to mint.(integer)
     * @param value The quantity of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, tokenId, value, this.getDefaultSendOptions());
    }

    /**
     * Mints token of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String to = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value, sendOptions);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenId The token id to mint.(hex string)
     * @param value The quantity of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, String tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, Numeric.toBigInt(tokenId), value, sendParam);
    }

    /**
     * Mints token of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value, sendOptions);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenId The token id to mint.(integer)
     * @param value The quantity of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNCTION_MINT, Arrays.asList(new Uint256(tokenId), new Address(to), new Uint256(value)));
        return this.sendWithSolidityType(sendOptions, FUNCTION_MINT, new Uint256(tokenId), new Address(to), new Uint256(value));
    }

    /**
     * Mints tokens of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String[] toList = new String[]{...};
     * String tokenId = "0x1";
     * BigInteger value = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value);
     * }
     * </pre>
     *
     *
     * @param toList The list of addresses that will receive the minted tokens.
     * @param tokenId The token id to mint.(hex string)
     * @param values The list of quantities of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String[] toList, String tokenId, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, Numeric.toBigInt(tokenId), values, this.getDefaultSendOptions());
    }

    /**
     * Mints tokens of the specific token type.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String[] toList = new String[]{...};
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value);
     * }
     * </pre>
     *
     *
     * @param toList The list of addresses that will receive the minted tokens.
     * @param tokenId The token id to mint.(integer)
     * @param values The list of quantities of tokens being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String[] toList, BigInteger tokenId, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, tokenId, values, this.getDefaultSendOptions());
    }

    /**
     * Mints tokens of the specific token type.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value);
     * }
     * </pre>
     *
     *
     * @param toList The list of addresses that will receive the minted tokens.
     * @param tokenId The token id to mint.(hex string)
     * @param values The list of quantities of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String[] toList, String tokenId, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, Numeric.toBigInt(tokenId), values, sendParam);
    }

    /**
     * Mints tokens of the specific token type.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String to = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenId, value, sendOptions);
     * }
     * </pre>
     *
     * @param toList The list of addresses that will receive the minted tokens.
     * @param tokenId The token id to mint.(integer)
     * @param values The list of quantities of tokens being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String[] toList, BigInteger tokenId, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        List<Uint256> valueList = Arrays.stream(values).map(Uint256::new).collect(Collectors.toList());
        List<Address> addressList = Arrays.stream(toList).map(Address::new).collect(Collectors.toList());

        Uint256 tokenIdSol = new Uint256(tokenId);
        DynamicArray<Address> toListSol = new DynamicArray<Address>(Address.class, addressList);
        DynamicArray<Uint256> valuesSol = new DynamicArray<Uint256>(Uint256.class, valueList);

        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNCTION_MINT, Arrays.asList(tokenIdSol, toListSol, valuesSol));
        return this.sendWithSolidityType(sendOptions, FUNCTION_MINT, tokenIdSol, toListSol, valuesSol);
    }

    /**
     * Mints multiple KIP-37 tokens of the specific token types.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{...}
     * BigInteger[] values = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenIds, values);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenIds The list of the token ids to mint.(hex string)
     * @param values The list of quantities of token being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintBatch(String to, String[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return mintBatch(to, tokenIdArr, values, this.getDefaultSendOptions());
    }

    /**
     * Mints multiple KIP-37 tokens of the specific token types.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{...}
     * BigInteger[] values = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenIds, values);
     * }
     * </pre>
     *
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenIds The list of the token ids to mint.(integer)
     * @param values The list of quantities of token being minted.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintBatch(String to, BigInteger[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mintBatch(to, tokenIds, values, this.getDefaultSendOptions());
    }

    /**
     * Mints multiple KIP-37 tokens of the specific token types.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String to = "0x{toAddress}";
     * String[] tokenIds = new String[]{...}
     * BigInteger[] values = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenIds, values, sendOptions);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenIds The list of the token ids to mint.(hex string)
     * @param values The list of quantities of token being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintBatch(String to, String[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return mintBatch(to, tokenIdArr, values, sendParam);
    }

    /**
     * Mints multiple KIP-37 tokens of the specific token types.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String to = "0x{toAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{...}
     * BigInteger[] values = new BigInteger[]{...}
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.mint(to, tokenIds, values, sendOptions);
     * }
     * </pre>
     *
     * @param to The address that will receive the minted tokens.
     * @param tokenIds The list of the token ids to mint.(integer)
     * @param values The list of quantities of token being minted.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mintBatch(String to, BigInteger[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        List<Uint256> tokenIdList = Arrays.stream(tokenIds).map(Uint256::new).collect(Collectors.toList());
        List<Uint256> valueList = Arrays.stream(values).map(Uint256::new).collect(Collectors.toList());

        Address toSol = new Address(to);
        DynamicArray<Uint256> tokenIdsSol = new DynamicArray<Uint256>(Uint256.class, tokenIdList);
        DynamicArray<Uint256> valuesSol = new DynamicArray<Uint256>(Uint256.class, valueList);

        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNCTION_MINT_BATCH, Arrays.asList(toSol, tokenIdsSol, valuesSol));
        return this.sendWithSolidityType(sendOptions, FUNCTION_MINT_BATCH, toSol, tokenIdsSol, valuesSol);
    }

    /**
     * Checks if specific account has the Minter role.
     * <pre>Example :
     * {@code
     * String account = "0x{accountAddress}";
     *
     * boolean hasMinter = kip37.isMinter(account);
     * }
     * </pre>
     * @param account The address to check that has Minter role
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

        List<Type> result = this.call(callObject, FUNCTION_IS_MINTER, account);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Adds an account as a minter that has the permission of MinterRole and can mint.<p>
     * It will use default sendOptions in contract instance to passed sendOptions<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String account = "0x{accountAddress}";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.addMinter(account);
     * }
     * </pre>
     *
     * @param account The account address to add minter role.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException  {
        return addMinter(account, this.getDefaultSendOptions());
    }

    /**
     * Adds an account as a minter that has the permission of MinterRole and can mint.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String account = "0x{address}";
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.addMinter(account, sendOptions);
     * }
     * </pre>
     *
     * @param account The account address to add minter role.
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_ADD_MINTER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.send(sendOptions, FUNCTION_ADD_MINTER, account);
        return receiptData;
    }

    /**
     * Renounces privilege of MinterRole from an account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.renounceMinter();
     * }
     * </pre>
     *
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
     * Renounces privilege of MinterRole from an account.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.renounceMinter(sendOptions);
     * }
     * </pre>
     *
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_MINTER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.send(sendOptions, FUNCTION_RENOUNCE_MINTER);
        return receiptData;
    }

    /**
     * Burns a token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String owner = "0x{ownerAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burn(owner, tokenId, value);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenId The token id to burn.(hex string)
     * @param value The token amount to burn.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(String address, String tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burn(address, tokenId, value, this.getDefaultSendOptions());
    }

    /**
     * Burns a token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String owner = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burn(owner, tokenId, value);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenId The token id to burn.(integer)
     * @param value The token amount to burn.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(String address, BigInteger tokenId, BigInteger value) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burn(address, tokenId, value, this.getDefaultSendOptions());
    }

    /**
     * Burns a token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String owner = "0x{toAddress}";
     * String tokenId = "0x1";
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burn(owner, tokenId, value, sendOptions);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenId The token id to burn.(hex string)
     * @param value The token amount to burn.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws TransactionException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public TransactionReceipt.TransactionReceiptData burn(String address, String tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burn(address, Numeric.toBigInt(tokenId), value, sendParam);
    }

    /**
     * Burns a token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String owner = "0x{toAddress}";
     * BigInteger tokenId = BigInteger.ONE;
     * BigInteger value = BigInteger.ONE;
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burn(owner, tokenId, value, sendOptions);
     * }
     * </pre>
     * @param address The account that owns tokens.
     * @param tokenId The token id to burn.(integer)
     * @param value The token amount to burn.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws TransactionException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public TransactionReceipt.TransactionReceiptData burn(String address, BigInteger tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN, Arrays.asList(address, tokenId, value));
        return this.send(sendOptions, FUNCTION_BURN, address, tokenId, value);
    }

    /**
     * Burns multiple KIP37 tokens.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String owner = "0x{ownerAddress}";
     * String[] tokenIds = new String[]{...}
     * BigInteger[] values = new BigInteger[]{...};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burnBatch(owner, tokenIds, values);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenIds The list of the token ids to burn.(hex string)
     * @param values The list of the token amounts to burn.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burnBatch(String address, String[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burnBatch(address, tokenIds, values, this.getDefaultSendOptions());
    }

    /**
     * Burns multiple KIP37 tokens.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String owner = "0x{ownerAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{...}
     * BigInteger[] values = new BigInteger[]{...};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burnBatch(owner, tokenIds, values);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenIds The list of the token ids to burn.(integer)
     * @param values The list of the token amounts to burn.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burnBatch(String address, BigInteger[] tokenIds, BigInteger[] values) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burnBatch(address, tokenIds, values, this.getDefaultSendOptions());
    }

    /**
     * Burns multiple KIP37 tokens.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String owner = "0x{ownerAddress}";
     * String[] tokenIds = new String[]{...}
     * BigInteger[] values = new BigInteger[]{...};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burnBatch(owner, tokenIds, values, sendOptions);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenIds The list of the token ids to burn.(hex string)
     * @param values The list of the token amounts to burn.
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
    public TransactionReceipt.TransactionReceiptData burnBatch(String address, String[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        BigInteger[] tokenIdsArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return burnBatch(address, tokenIdsArr, values, sendParam);
    }

    /**
     * Burns multiple KIP37 tokens.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String owner = "0x{ownerAddress}";
     * BigInteger[] tokenIds = new BigInteger[]{...}
     * BigInteger[] values = new BigInteger[]{...};
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.burnBatch(owner, tokenIds, values, sendOptions);
     * }
     * </pre>
     *
     * @param address The account that owns tokens.
     * @param tokenIds The list of the token ids to burn.(integer)
     * @param values The list of the token amounts to burn.
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
    public TransactionReceipt.TransactionReceiptData burnBatch(String address, BigInteger[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN_BATCH, Arrays.asList(address, tokenIds, values));
        return this.send(sendOptions, FUNCTION_BURN_BATCH, address, tokenIds, values);
    }

    /**
     * Check if contract has been paused state.
     * <pre>Example :
     * {@code
     * boolean isPausedContract = kip37.paused();
     * }
     * </pre>
     *
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
     * Check if token has been paused state.
     * <pre>Example :
     * {@code
     * String tokenId = "0x1";
     * boolean isPausedContract = kip37.paused(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The token ID.(hex string)
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean paused(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return paused(Numeric.toBigInt(tokenId));
    }

    /**
     * Check if token has been paused state.
     * <pre>Example :
     * {@code
     * BigInteger tokenId = BigInteger.ONE;
     * boolean isPausedContract = kip37.paused(tokenId);
     * }
     * </pre>
     *
     * @param tokenId The token ID.(integer)
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean paused(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.call(FUNCTION_PAUSED, tokenId);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Pause a contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause();
     * }
     * </pre>
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws TransactionException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public TransactionReceipt.TransactionReceiptData pause() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return pause(this.getDefaultSendOptions());
    }

    /**
     * Pause a contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause(sendOptions);
     * }
     * </pre>
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_PAUSE, null);
        return this.send(sendOptions, FUNCTION_PAUSE);
    }

    /**
     * Unpauses a contract. <p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause();
     * }
     * </pre>
     *
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unpause(this.getDefaultSendOptions());
    }

    /**
     * Unpauses a contract. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause(sendOptions);
     * }
     * </pre>
     *
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_UNPAUSE, null);
        return this.send(sendOptions, FUNCTION_UNPAUSE);
    }

    /**
     * Pauses a specific token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String tokenId = "0x1";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause(tokenId);
     * }
     * </pre>
     * @param tokenId The token id to pause(hex string)
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(tokenId, this.getDefaultSendOptions());
    }

    /**
     * Pauses a specific token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause(tokenId);
     * }
     * </pre>
     * @param tokenId The token id to pause(integer)
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(tokenId, this.getDefaultSendOptions());
    }

    /**
     * Pauses a specific token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String tokenId = "0x1";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause(tokenId, sendOptions);
     * }
     * </pre>
     * @param tokenId The token id to pause(hex string)
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(String tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(Numeric.toBigInt(tokenId), sendParam);
    }

    /**
     * Pauses a specific token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * TransactionReceipt.TransactionReceiptData receipt = kip37.pause(tokenId, sendOptions);
     * }
     * </pre>
     * @param tokenId The token id to pause(integer)
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_PAUSE, Arrays.asList(tokenId));
        return this.send(sendOptions, FUNCTION_PAUSE, tokenId);
    }

    /**
     * Unpauses a specific token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String tokenId = "0x1";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause(tokenId);
     * }
     * </pre>
     * @param tokenId he token id to unpause(hex string)
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unpause(tokenId, this.getDefaultSendOptions());
    }

    /**
     * Unpauses a specific token.<p>
     * It will use default sendOptions in contract instance to passed sendOptions. <p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause(tokenId);
     * }
     * </pre>
     * @param tokenId he token id to unpause(integer)
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unpause(tokenId, this.getDefaultSendOptions());
    }

    /**
     * Unpauses a specific token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String tokenId = "0x1";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause(tokenId, sendOptions);
     * }
     * </pre>
     *
     * @param tokenId he token id to unpause(hex string)
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(String tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unpause(Numeric.toBigInt(tokenId), sendParam);
    }

    /**
     * Unpauses a specific token.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * BigInteger tokenId = BigInteger.ONE;
     * TransactionReceipt.TransactionReceiptData receipt = kip37.unpause(tokenId, sendOptions);
     * }
     * </pre>
     * @param tokenId he token id to unpause(integer)
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_UNPAUSE, Arrays.asList(tokenId));
        return this.send(sendOptions, FUNCTION_UNPAUSE, tokenId);
    }

    /**
     * Checks if specific account has Pauser role.
     * @param account The account address to check that has Pauser role.
     * <pre>Example :
     * {@code
     * String account = "0x{accountAddress}";
     * boolean isPauser = kip37.isPauser(account);
     * }
     * </pre>
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

        List<Type> result = this.call(callObject, FUNCTION_IS_PAUSER, account);
        return (boolean)result.get(0).getValue();
    }

    /**
     * Adds an account as a pauser that has the permission of PauserRole and can pause.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * String account = "0x{accountAddress}";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.addPauser(account);
     * }
     * </pre>
     *
     * @param account The account address to add Pauser role.
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
     * Adds an account as a pauser that has the permission of PauserRole and can pause.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * String account = "0x{accountAddress}";
     * TransactionReceipt.TransactionReceiptData receipt = kip37.addPauser(account, sendOptions);
     * }
     * </pre>
     *
     * @param account The account address to add Pauser role.
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
     * Renounces privilege of PauserRole from an account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     * kip37.setDefaultSendOptions(sendOptions);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.renouncePauser();
     * }
     * </pre>
     *
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
     * Renounces privilege of PauserRole from an account.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * <pre>Example :
     * {@code
     * String sender = "0x{senderAddress}";
     * SendOptions sendOptions = new SendOptions();
     * sendOptions.setFrom(sender);
     *
     * TransactionReceipt.TransactionReceiptData receipt = kip37.renouncePauser(sendOptions);
     * }
     * </pre>
     *
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

        TransactionReceipt.TransactionReceiptData receiptData = this.send(sendOption, FUNCTION_RENOUNCE_PAUSER);
        return receiptData;
    }

    /**
     * Call method "supportsInterface" in KIP-13 standard contract.
     * <pre>Example :
     * {@code
     * boolean isSupported = kip37.supportInterface(KIP37.INTERFACE.IKIP37.getId());
     * }
     * </pre>
     * @param interfaceId interface identifier. see {@link KIP37.INTERFACE}
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean supportsInterface(String interfaceId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SUPPORTS_INTERFACE).call(Arrays.asList(interfaceId), callObject);

        return (boolean)result.get(0).getValue();
    }


    private static SendOptions determineSendOptions(KIP37 kip37, SendOptions sendOptions, String functionName, List<Object> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;

        String from = kip37.getDefaultSendOptions().getFrom();
        String gas = kip37.getDefaultSendOptions().getGas();
        String value = kip37.getDefaultSendOptions().getValue();
        Boolean feeDelegation = kip37.getDefaultSendOptions().getFeeDelegation();
        String feePayer = kip37.getDefaultSendOptions().getFeePayer();
        String feeRatio = kip37.getDefaultSendOptions().getFeeRatio();

        if(sendOptions.getFrom() != null) {
            from = sendOptions.getFrom();
        }

        if(sendOptions.getGas() == null) {
            //If passed gas fields in sendOptions and defaultSendOptions is null, it estimate gas.
            if(gas == null) {
                CallObject callObject = CallObject.createCallObject(sendOptions.getFrom());
                BigInteger estimateGas = estimateGas(kip37, functionName, callObject, argument);
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

    private static SendOptions determineSendOptionsWithSolidityType(KIP37 kip37, SendOptions sendOptions, String functionName, List<Type> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;

        String from = kip37.getDefaultSendOptions().getFrom();
        String gas = kip37.getDefaultSendOptions().getGas();
        String value = kip37.getDefaultSendOptions().getValue();
        Boolean feeDelegation = kip37.getDefaultSendOptions().getFeeDelegation();
        String feePayer = kip37.getDefaultSendOptions().getFeePayer();
        String feeRatio = kip37.getDefaultSendOptions().getFeeRatio();

        if(sendOptions.getFrom() != null) {
            from = sendOptions.getFrom();
        }

        if(sendOptions.getGas() == null) {
            //If passed gas fields in sendOptions and defaultSendOptions is null, it estimate gas.
            if(gas == null) {
                CallObject callObject = CallObject.createCallObject(sendOptions.getFrom());
                BigInteger estimateGas = estimateGasWithSolidityType(kip37, functionName, callObject, argument);
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


    private static BigInteger estimateGas(KIP37 kip37, String functionName, CallObject callObject, List<Object> arguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String gas = kip37.getMethod(functionName).estimateGas(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.7)).toBigInteger();

        return gasInteger;
    }

    private static BigInteger estimateGasWithSolidityType(KIP37 kip37, String functionName, CallObject callObject, List<Type> arguments) throws IOException {
        String gas = kip37.getMethod(functionName).estimateGasWithSolidityWrapper(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.7)).toBigInteger();

        return gasInteger;
    }


}
