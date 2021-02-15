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
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip13.KIP13;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.IWallet;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static final String INTERFACE_ID_IKIP37 = "0x6433ca1f";
    public static final String INTERFACE_ID_IKIP37_METADATA = "0x0e89341c";
    public static final String INTERFACE_ID_IKIP37_MINTABLE = "0xdfd9d9ec";
    public static final String INTERFACE_ID_IKIP37_BURNABLE = "0x9e094e9e";
    public static final String INTERFACE_ID_IKIP37_PAUSABLE = "0x0e8ffdb7";

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
     * Deploy a KIP-37 contract.
     * The deployer's keyring should be existed in `caver.wallet`.
     * @param caver A Caver instance
     * @param uri The URI for token type
     * @param deployer A deployer's address
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
     * Deploy a KIP-37 contract.
     * The wallet used in the contract is set to the wallet type passed as a parameter of the method.
     * @param caver A Caver instance.
     * @param uri The URI for token type
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
    public static KIP37 deploy(Caver caver, String uri, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP37ConstantData.BINARY, uri);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(8000000));

        KIP37 kip37 = new KIP37(caver);
        kip37.setWallet(wallet);
        kip37.deploy(contractDeployParams, sendOptions);

        return kip37;
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
        Map<String, Boolean> result = Stream.of(
                new AbstractMap.SimpleEntry<>(INTERFACE_ID_IKIP37, false),
                new AbstractMap.SimpleEntry<>(INTERFACE_ID_IKIP37_BURNABLE, false),
                new AbstractMap.SimpleEntry<>(INTERFACE_ID_IKIP37_MINTABLE, false),
                new AbstractMap.SimpleEntry<>(INTERFACE_ID_IKIP37_METADATA, false),
                new AbstractMap.SimpleEntry<>(INTERFACE_ID_IKIP37_PAUSABLE, false))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        KIP13 kip13;
        try {
            kip13 = new KIP13(caver, contractAddress);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if(!kip13.isImplementedKIP13Interface()) {
            throw new RuntimeException("This contract does not support KIP-13.");
        }

        result.entrySet().forEach(entry -> entry.setValue(kip13.sendQuery(entry.getKey())));
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
        return (String)result.get(0).getValue();
    }

    /**
     * Get the balance of an account's tokens.
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
     * Get the balance of an account's tokens
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
     * Creates a new token type and assigns initialSupply to the minter.<p>
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
     * @param interfaceId interface identifier
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

        newSendOptions = new SendOptions(from, gas, value);
        return newSendOptions;
    }

    private static SendOptions determineSendOptionsWithSolidityType(KIP37 kip37, SendOptions sendOptions, String functionName, List<Type> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;

        String from = kip37.getDefaultSendOptions().getFrom();
        String gas = kip37.getDefaultSendOptions().getGas();
        String value = kip37.getDefaultSendOptions().getValue();

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

        newSendOptions = new SendOptions(from, gas, value);
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
