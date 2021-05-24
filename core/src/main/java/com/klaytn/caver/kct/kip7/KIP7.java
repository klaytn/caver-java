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

package com.klaytn.caver.kct.kip7;

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

public class KIP7 extends Contract {

    private static final String FUNCTION_ADD_MINTER = "addMinter";
    private static final String FUNCTION_ADD_PAUSER = "addPauser";
    private static final String FUNCTION_ALLOWANCE = "allowance";
    private static final String FUNCTION_APPROVE = "approve";
    private static final String FUNCTION_BALANCE_OF = "balanceOf";
    private static final String FUNCTION_BURN = "burn";
    private static final String FUNCTION_BURN_FROM = "burnFrom";
    private static final String FUNCTION_DECIMALS = "decimals";
    private static final String FUNCTION_IS_MINTER = "isMinter";
    private static final String FUNCTION_IS_PAUSER = "isPauser";
    private static final String FUNCTION_MINT = "mint";
    private static final String FUNCTION_NAME = "name";
    private static final String FUNCTION_PAUSE = "pause";
    private static final String FUNCTION_PAUSED = "paused";
    private static final String FUNCTION_RENOUNCE_MINTER = "renounceMinter";
    private static final String FUNCTION_RENOUNCE_PAUSER = "renouncePauser";
    private static final String FUNCTION_SAFE_TRANSFER = "safeTransfer";
    private static final String FUNCTION_SAFE_TRANSFER_FROM = "safeTransferFrom";
    private static final String FUNCTION_SUPPORTS_INTERFACE = "supportsInterface";
    private static final String FUNCTION_SYMBOL = "symbol";
    private static final String FUNCTION_TOTAL_SUPPLY = "totalSupply";
    private static final String FUNCTION_TRANSFER = "transfer";
    private static final String FUNCTION_TRANSFER_FROM = "transferFrom";
    private static final String FUNCTION_UNPAUSE = "unpause";

    public enum INTERFACE {
        IKIP7("IKIP7", "0x65787371"),
        IKIP7_METADATA("IKIP7Metatdata", "0xa219a025"),
        IKIP7_MINTABLE("IKIP7Mintable", "0xeab83e20"),
        IKIP7_BURNABLE("IKIP7Burnable", "0x3b5a0bf8"),
        IKIP7_PAUSABLE("IKIP7Pausable", "0x4d5507ff");

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
     * Creates a KIP7 instance.
     * @param caver A Caver instance.
     * @throws IOException
     */
    public KIP7(Caver caver) throws IOException {
        super(caver, KIP7ConstantData.ABI);
    }

    /**
     * Creates a KIP7 instance
     * @param caver A Caver instance
     * @param contractAddress A contract address
     * @throws IOException
     */
    public KIP7(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP7ConstantData.ABI, contractAddress);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * It must add deployer's keyring in caver.wallet.
     * @param caver A Caver instance.
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
    public static KIP7 deploy(Caver caver, String deployer, String name, String symbol, int decimals, BigInteger initialSupply) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        KIP7DeployParams params = new KIP7DeployParams(name, symbol, decimals, initialSupply);
        return deploy(caver, params, deployer);
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
     *     KIP7 kip7 = caver.kct.kip7.deploy(caver, sendOptions, name, symbol, decimals, initialSupply);
     * </code>
     * </pre>
     * @param caver A Caver instance.
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
    public static KIP7 deploy(Caver caver, SendOptions sendOptions, String name, String symbol, int decimals, BigInteger initialSupply) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        KIP7DeployParams params = new KIP7DeployParams(name, symbol, decimals, initialSupply);
        return deploy(caver, params, sendOptions);
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
    public static KIP7 deploy(Caver caver, String deployer, String name, String symbol, int decimals, BigInteger initialSupply, IWallet wallet) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        KIP7DeployParams params = new KIP7DeployParams(name, symbol, decimals, initialSupply);
        return deploy(caver, params, deployer, wallet);
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
     *     KIP7 kip7 = caver.kct.kip7.deploy(caver, sendOptions, name, symbol, decimals, initialSupply, caver.getWallet());
     * </code>
     * </pre>
     * @param caver A Caver instance.
     * @param sendOptions The send options to deploy a contract.
     * @param name A KIP-7 contract name.
     * @param symbol A KIP-7 contract symbol.
     * @param decimals A KIP-7 contract decimals.
     * @param initialSupply A KIP-7 contract initial supply.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws TransactionException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static KIP7 deploy(Caver caver, SendOptions sendOptions, String name, String symbol, int decimals, BigInteger initialSupply, IWallet wallet) throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        KIP7DeployParams params = new KIP7DeployParams(name, symbol, decimals, initialSupply);
        return deploy(caver, params, sendOptions, wallet);
    }

    /**
     * Deploy KIP-7 contract.<p>
     * It must add deployer's keyring in caver.wallet.
     * @param caver A Caver instance
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
    public static KIP7 deploy(Caver caver, KIP7DeployParams tokenInfo, String deployer) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return deploy(caver, tokenInfo, deployer, caver.getWallet());
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
     *     KIP7 kip7 = caver.kct.kip7.deploy(caver, tokenInfo, sendOptions);
     * </code>
     * </pre>
     * @param caver A Caver instance
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @return KIP7
     * @throws NoSuchMethodException
     * @throws TransactionException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public static KIP7 deploy(Caver caver, KIP7DeployParams tokenInfo, SendOptions sendOptions) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return deploy(caver, tokenInfo, sendOptions, caver.getWallet());
    }

    /**
     * Deploy KIP-7 contract.<p>
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param deployer A deployer's address
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
    public static KIP7 deploy(Caver caver, KIP7DeployParams tokenInfo, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = new SendOptions();
        sendOptions.setFrom(deployer);
        sendOptions.setGas(BigInteger.valueOf(4000000));

        return deploy(caver, tokenInfo, sendOptions, wallet);
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
     *     KIP7 kip7 = caver.kct.kip7.deploy(caver, tokenInfo, sendOptions, caver.getWallet());
     * </code>
     * </pre>
     * @param caver A Caver instance
     * @param tokenInfo The KIP-7 contract's deploy parameter values
     * @param sendOptions The send options to deploy a contract.
     * @param wallet The class instance implemented IWallet to sign transaction.
     * @return KIP7
     * @throws IOException
     * @throws TransactionException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static KIP7 deploy(Caver caver, KIP7DeployParams tokenInfo, SendOptions sendOptions, IWallet wallet) throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List deployArgument = Arrays.asList(tokenInfo.getName(), tokenInfo.getSymbol(), tokenInfo.getDecimals(), tokenInfo.getInitialSupply());
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP7ConstantData.BINARY, deployArgument);

        KIP7 kip7 = new KIP7(caver);
        kip7.setWallet(wallet);
        kip7.deploy(contractDeployParams, sendOptions);

        return kip7;
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
     * @param caver A Caver instance.
     * @param contractAddress A contract address.
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
     * @return KIP7
     */
    @Override
    public KIP7 clone() {
        try {
            KIP7 kip7 = new KIP7(this.getCaver(), this.getContractAddress());
            kip7.setWallet(this.getWallet());
            return kip7;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copy instance with token address
     * @param tokenAddress A KIP-7 token address
     * @return KIP7
     */
    public KIP7 clone(String tokenAddress) {
        try {
            KIP7 kip7 = new KIP7(this.getCaver(), tokenAddress);
            kip7.setWallet(this.getWallet());
            return kip7;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Detects which interface the KIP-7 token contract supports.<p>
     * Example :
     * <pre>{@code
     * KIP7 kip7 = new KIP7("0x{contract_address}");
     * Map<String, Boolean> result = kip7.detectInterface();
     *
     * result.get(KIP7.INTERFACE_ID_IKIP7);
     * result.get(KIP7.INTERFACE_ID_IKIP7_BURNABLE);
     * result.get(KIP7.INTERFACE_ID_IKIP7_METADATA);
     * result.get(KIP7.INTERFACE_ID_IKIP7_MINTABLE);
     * result.get(KIP7.INTERFACE_ID_IKIP7_PAUSABLE);
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
     * Call method "name" in KIP-7 standard contract.
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
     * Call method "symbol" in KIP-7 standard contract.
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
     * Call method "decimals" in KIP-7 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public int decimals() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_DECIMALS).call(null, callObject);

        return ((BigInteger)result.get(0).getValue()).intValue();
    }

    /**
     * Call method "totalSupply" in KIP-7 standard contract.
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
     * Call method "balanceOf" in KIP-7 standard contract.
     * @param account An address for whom to query the balance
     * @return String
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
     * Call method "allowance" in KIP-7 standard contract.
     * @param owner The account allowed `spender` to withdraw the tokens from the account.
     * @param spender The address is approved to withdraw the tokens.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public BigInteger allowance(String owner, String spender) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_ALLOWANCE).call(Arrays.asList(owner, spender), callObject);

        return (BigInteger)result.get(0).getValue();
    }

    /**
     * Call method "isMinter" in KIP-7 standard contract.
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
     * Call method "isPauser" in KIP-7 standard contract.
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
     * Call method "paused" in KIP-7 standard contract.
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
     * Execute a method "approve" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param spender The address is approved to withdraw the tokens.
     * @param amount Amount the token amount will be approved.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return approve(spender, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "approve" in KIP-7 standard contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param spender The address is approved to withdraw the tokens.
     * @param amount Amount the token amount will be approved.
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
    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_APPROVE, Arrays.asList(spender, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_APPROVE).send(Arrays.asList(spender, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "transfer" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return transfer(recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "transfer" in KIP-7 standard contract.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
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
    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_TRANSFER, Arrays.asList(recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "transferFrom" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException  {
        return transferFrom(sender, recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "transferFrom" in KIP-7 standard contract.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
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
    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_TRANSFER_FROM, Arrays.asList(sender, recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return safeTransfer(recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
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
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER, Arrays.asList(recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER).send(Arrays.asList(recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException  {
        return safeTransfer(recipient, amount, data, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
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
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER, Arrays.asList(recipient, amount, data));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER).send(Arrays.asList(recipient, amount, data), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return safeTransferFrom(sender, recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
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
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(sender, recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransferFrom" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransferFrom(sender, recipient, amount, data, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransferFrom" in KIP-7 standard contract.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
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
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(sender, recipient, amount, data));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount, data), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "mint" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account that will receive the minted token
     * @param amount The token amount to mint
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return mint(account, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "mint" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account that will receive the minted token
     * @param amount The token amount to mint
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
    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_MINT, Arrays.asList(account, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_MINT).send(Arrays.asList(account, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "addMinter" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
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
    public TransactionReceipt.TransactionReceiptData addMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException  {
        return addMinter(account, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "addMinter" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_ADD_MINTER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_MINTER).send(Arrays.asList(account), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "renounceMinter" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
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
     * Execute a method "renounceMinter" in KIP-7 standard contract.<p>
     * Caller must have "Minter" Permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_MINTER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_RENOUNCE_MINTER).send(null, sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "burn" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param amount The token amount to be burned
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burn(amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "burn" in KIP-7 standard contract.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param amount The token amount to be burned
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
    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN, Arrays.asList(amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_BURN).send(Arrays.asList(amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "burnFrom" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account will be deducted is the The token amount to be burned
     * @param amount The token amount to be burned
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burnFrom(account, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "burnFrom" in KIP-7 standard contract.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account will be deducted is the The token amount to be burned
     * @param amount The token amount to be burned
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
    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN_FROM, Arrays.asList(account, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_BURN_FROM).send(Arrays.asList(account, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "addPauser" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
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
    public TransactionReceipt.TransactionReceiptData addPauser(String account) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return addPauser(account, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "addPauser" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_ADD_PAUSER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_PAUSER).send(Arrays.asList(account), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "pause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return pause(this.getDefaultSendOptions());
    }

    /**
     * Execute a method "pause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_PAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_PAUSE).send(null, sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.<p>
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
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
     * Execute a method "unpause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_UNPAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_UNPAUSE).send(null, sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
     * It will use default sendOptions in contract instance to passed sendOptions.
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
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
     * Execute a method "unpause" in KIP-7 standard contract.<p>
     * Caller must have Pauser permission.<p>
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
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_PAUSER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_RENOUNCE_PAUSER).send(null, sendOptions);
        return receiptData;
    }

    private static SendOptions determineSendOptions(KIP7 kip7, SendOptions sendOptions, String functionName, List<Object> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;

        String from = kip7.getDefaultSendOptions().getFrom();
        String gas = kip7.getDefaultSendOptions().getGas();
        String value = kip7.getDefaultSendOptions().getValue();
        Boolean feeDelegation = kip7.getDefaultSendOptions().getFeeDelegation();
        String feePayer = kip7.getDefaultSendOptions().getFeePayer();
        String feeRatio = kip7.getDefaultSendOptions().getFeeRatio();

        if(sendOptions.getFrom() != null) {
            from = sendOptions.getFrom();
        }

        if(sendOptions.getGas() == null) {
            //If passed gas fields in sendOptions and defaultSendOptions is null, it estimate gas.
            if(gas == null) {
                CallObject callObject = CallObject.createCallObject(sendOptions.getFrom());
                BigInteger estimateGas = estimateGas(kip7, functionName, callObject, argument);
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
     * @param kip7 A KIP7 instance.
     * @param functionName A KIP-7 contract's method.
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
    private static BigInteger estimateGas(KIP7 kip7, String functionName, CallObject callObject, List<Object> arguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String gas = kip7.getMethod(functionName).estimateGas(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.5)).toBigInteger();

        return gasInteger;
    }
}
