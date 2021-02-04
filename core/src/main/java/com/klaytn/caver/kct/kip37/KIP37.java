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

package com.klaytn.caver.kct.kip37;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KIP37 extends Contract {
    public static final String FUNC_URI = "uri";
    public static final String FUNC_BALANCE_OF = "balanceOf";
    public static final String FUNC_BALANCE_OF_BATCH = "balanceOfBatch";
    public static final String FUNC_SET_APPROVED_FOR_ALL = "setApprovalForAll";
    public static final String FUNC_IS_APPROVED_FOR_ALL = "isApprovedForAll";
    public static final String FUNC_TOTAL_SUPPLY = "totalSupply";
    public static final String FUNC_SAFE_TRANSFER_FROM = "safeTransferFrom";
    public static final String FUNC_SAFE_BATCH_TRANSFER_FROM = "safeBatchTransferFrom";
    public static final String FUNC_BURN = "burn";
    public static final String FUNC_BURN_BATCH = "burnBatch";
    public static final String FUNC_CREATE = "create";
    public static final String FUNC_MINT = "mint";
    public static final String FUNC_MINT_BATCH = "mintBatch";
    public static final String FUNC_PAUSED = "paused";
    public static final String FUNC_PAUSE = "pause";
    public static final String FUNC_UNPAUSE = "unpause";
    public static final String FUNC_IS_PAUSER = "isPauser";
    public static final String FUNC_ADD_PAUSER = "addPauser";
    public static final String FUNC_RENOUNCE_PAUSER = "renouncePauser";
    public static final String FUNC_IS_MINTER = "isMinter";
    public static final String FUNC_ADD_MINTER = "addMinter";
    public static final String FUNC_RENOUNCE_MINTER = "renounceMinter";
    public static final String FUNC_SUPPORT_INTERFACE = "supportsInterface";

    public KIP37(Caver caver) throws IOException {
        super(caver, KIP37ConstantData.ABI);
    }

    public KIP37(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP37ConstantData.ABI, contractAddress);
    }

    public static KIP37 deploy(Caver caver, String uri, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return deploy(caver, uri, deployer, caver.getWallet());
    }

    public static KIP37 deploy(Caver caver, String uri, String deployer, IWallet wallet) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP37ConstantData.BINARY, uri);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(8000000));

        KIP37 kip37 = new KIP37(caver);
        kip37.setWallet(wallet);
        kip37.deploy(contractDeployParams, sendOptions);

        return kip37;
    }

    public String uri(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return uri(Numeric.toBigInt(tokenId));
    }

    public String uri(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNC_URI, tokenId);
        return (String)result.get(0).getValue();
    }

    public BigInteger balanceOf(String account, String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return balanceOf(account, Numeric.toBigInt(tokenId));
    }

    public BigInteger balanceOf(String account, BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNC_BALANCE_OF, account, tokenId);
        return (BigInteger)result.get(0).getValue();
    }

    public List<BigInteger> balanceOfBatch(String[] accounts, String[] tokenIds) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        BigInteger[] arr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return balanceOfBatch(accounts, arr);
    }

    public List<BigInteger> balanceOfBatch(String[] accounts, BigInteger[] tokenIds) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNC_BALANCE_OF_BATCH, accounts, tokenIds);
        DynamicArray<Uint256> batchList = (DynamicArray<Uint256>)result.get(0);
        return batchList.getValue()
                .stream()
                .map(NumericType::getValue)
                .collect(Collectors.toList());
    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value, String data) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, data, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value, String data) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, tokenId, value, data, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, String tokenId, BigInteger value, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return safeTransferFrom(from, to, Numeric.toBigInt(tokenId), value, data, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String from, String to, BigInteger tokenId, BigInteger value, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_SAFE_TRANSFER_FROM, Arrays.asList(from, to, tokenId, value, data));
        return this.send(sendOptions, FUNC_SAFE_TRANSFER_FROM, from, to, tokenId, value, data);
    }

    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeBatchTransferFrom(from, to, tokenIds, amounts, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, String[] tokenIds, BigInteger[] amounts, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return safeBatchTransferFrom(from, to, tokenIdArr, amounts, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData safeBatchTransferFrom(String from, String to, BigInteger[] tokenIds, BigInteger[] amounts, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_SAFE_BATCH_TRANSFER_FROM, Arrays.asList(from, to, tokenIds, amounts));
        return this.send(sendOptions, FUNC_SAFE_BATCH_TRANSFER_FROM, from, to, tokenIds, amounts);
    }

    public TransactionReceipt.TransactionReceiptData setApprovalForAll(String operator, boolean approved) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return setApprovalForAll(operator, approved, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData setApprovalForAll(String operator, boolean approved, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNC_SET_APPROVED_FOR_ALL, Arrays.asList(operator, approved));
        return this.send(sendOption, FUNC_SET_APPROVED_FOR_ALL, operator, approved);
    }

    public boolean isApprovedForAll(String owner, String operator) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNC_IS_APPROVED_FOR_ALL, owner, operator);
        return (Boolean)result.get(0).getValue();
    }

    public BigInteger totalSupply(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return totalSupply(Numeric.toBigInt(tokenId));
    }

    public BigInteger totalSupply(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        List<Type> result = this.call(FUNC_TOTAL_SUPPLY, tokenId);
        return (BigInteger)result.get(0).getValue();
    }

    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply, String uri) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, uri, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply, String uri) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(tokenId, initialSupply, uri, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData create(String tokenId, BigInteger initialSupply, String uri, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return create(Numeric.toBigInt(tokenId), initialSupply, uri, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData create(BigInteger tokenId, BigInteger initialSupply, String uri, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_CREATE, Arrays.asList(tokenId, initialSupply, uri));
        return this.send(sendOptions, FUNC_CREATE, tokenId, initialSupply, uri);
    }

    public TransactionReceipt.TransactionReceiptData mint(String to, String tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, Numeric.toBigInt(tokenId), value, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, tokenId, value, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mint(String to, String tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(to, Numeric.toBigInt(tokenId), value, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData mint(String to, BigInteger tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNC_MINT, Arrays.asList(new Uint256(tokenId), new Address(to), new Uint256(value)));
        return this.sendWithSolidityType(sendOptions, FUNC_MINT, new Uint256(tokenId), new Address(to), new Uint256(value));
    }

    public TransactionReceipt.TransactionReceiptData mint(String[] toList, String tokenId, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, Numeric.toBigInt(tokenId), values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mint(String[] toList, BigInteger tokenId, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, tokenId, values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mint(String[] toList, String tokenId, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mint(toList, Numeric.toBigInt(tokenId), values, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData mint(String[] toList, BigInteger tokenId, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        List<Uint256> valueList = Arrays.stream(values).map(Uint256::new).collect(Collectors.toList());
        List<Address> addressList = Arrays.stream(toList).map(Address::new).collect(Collectors.toList());

        Uint256 tokenIdSol = new Uint256(tokenId);
        DynamicArray<Address> toListSol = new DynamicArray<Address>(Address.class, addressList);
        DynamicArray<Uint256> valuesSol = new DynamicArray<Uint256>(Uint256.class, valueList);

        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNC_MINT, Arrays.asList(tokenIdSol, toListSol, valuesSol));
        return this.sendWithSolidityType(sendOptions, FUNC_MINT, tokenIdSol, toListSol, valuesSol);
    }

    public TransactionReceipt.TransactionReceiptData mintBatch(String to, String[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return mintBatch(to, tokenIdArr, values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mintBatch(String to, BigInteger[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return mintBatch(to, tokenIds, values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData mintBatch(String to, String[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        BigInteger[] tokenIdArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return mintBatch(to, tokenIdArr, values, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData mintBatch(String to, BigInteger[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        List<Uint256> tokenIdList = Arrays.stream(tokenIds).map(Uint256::new).collect(Collectors.toList());
        List<Uint256> valueList = Arrays.stream(values).map(Uint256::new).collect(Collectors.toList());

        Address toSol = new Address(to);
        DynamicArray<Uint256> tokenIdsSol = new DynamicArray<Uint256>(Uint256.class, tokenIdList);
        DynamicArray<Uint256> valuesSol = new DynamicArray<Uint256>(Uint256.class, valueList);

        SendOptions sendOptions = determineSendOptionsWithSolidityType(this, sendParam, FUNC_MINT_BATCH, Arrays.asList(toSol, tokenIdsSol, valuesSol));
        return this.sendWithSolidityType(sendOptions, FUNC_MINT_BATCH, toSol, tokenIdsSol, valuesSol);
    }

    public boolean isMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.call(callObject, FUNC_IS_MINTER, account);
        return (boolean)result.get(0).getValue();
    }

    public TransactionReceipt.TransactionReceiptData addMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException  {
        return addMinter(account, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData addMinter(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_ADD_MINTER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.send(sendOptions, FUNC_ADD_MINTER, account);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData renounceMinter() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renounceMinter(this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData renounceMinter(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_RENOUNCE_MINTER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.send(sendOptions, FUNC_RENOUNCE_MINTER);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData burn(String address, String tokenId, BigInteger value) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burn(address, tokenId, value, this.getDefaultSendOptions());
    }


    public TransactionReceipt.TransactionReceiptData burn(String address, BigInteger tokenId, BigInteger value) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burn(address, tokenId, value, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData burn(String address, String tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burn(address, Numeric.toBigInt(tokenId), value, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData burn(String address, BigInteger tokenId, BigInteger value, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_BURN, Arrays.asList(address, tokenId, value));
        return this.send(sendOptions, FUNC_BURN, address, tokenId, value);
    }

    public TransactionReceipt.TransactionReceiptData burnBatch(String address, String[] tokenIds, BigInteger[] values) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burnBatch(address, tokenIds, values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData burnBatch(String address, BigInteger[] tokenIds, BigInteger[] values) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return burnBatch(address, tokenIds, values, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData burnBatch(String address, String[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        BigInteger[] tokenIdsArr = Arrays.stream(tokenIds).map(Numeric::toBigInt).toArray(BigInteger[]::new);
        return burnBatch(address, tokenIdsArr, values, sendParam);
    }

    public TransactionReceipt.TransactionReceiptData burnBatch(String address, BigInteger[] tokenIds, BigInteger[] values, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_BURN_BATCH, Arrays.asList(address, tokenIds, values));
        return this.send(sendOptions, FUNC_BURN_BATCH, address, tokenIds, values);
    }

    public boolean paused() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.getMethod(FUNC_PAUSED).call(null, callObject);
        return (boolean)result.get(0).getValue();
    }

    public TransactionReceipt.TransactionReceiptData pause() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return pause(this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData pause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_PAUSE, null);
        return this.send(sendOptions, FUNC_PAUSE);
    }

    public TransactionReceipt.TransactionReceiptData unPause() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unPause(this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData unPause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_UNPAUSE, null);
        return this.send(sendOptions, FUNC_UNPAUSE);
    }

    public TransactionReceipt.TransactionReceiptData pause(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(tokenId, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData pause(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(tokenId, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData pause(String tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return pause(Numeric.toBigInt(tokenId));
    }

    public TransactionReceipt.TransactionReceiptData pause(BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_PAUSE, Arrays.asList(tokenId));
        return this.send(sendOptions, FUNC_PAUSE, tokenId);
    }

    public TransactionReceipt.TransactionReceiptData unPause(String tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unPause(tokenId, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData unPause(BigInteger tokenId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unPause(tokenId, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData unPause(String tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        return unPause(Numeric.toBigInt(tokenId), sendParam);
    }

    public TransactionReceipt.TransactionReceiptData unPause(BigInteger tokenId, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNC_UNPAUSE, Arrays.asList(tokenId));
        return this.send(sendOptions, FUNC_UNPAUSE, tokenId);
    }

    public boolean isPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();

        List<Type> result = this.call(callObject, FUNC_IS_PAUSER, account);
        return (boolean)result.get(0).getValue();
    }

    public TransactionReceipt.TransactionReceiptData addPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return addPauser(account, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData addPauser(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNC_ADD_PAUSER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNC_ADD_PAUSER).send(Arrays.asList(account), sendOption);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData renouncePauser(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renouncePauser(account, this.getDefaultSendOptions());
    }

    public TransactionReceipt.TransactionReceiptData renouncePauser(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOption = determineSendOptions(this, sendParam, FUNC_RENOUNCE_PAUSER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNC_RENOUNCE_PAUSER).send(Arrays.asList(account), sendOption);
        return receiptData;
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
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.5)).toBigInteger();

        return gasInteger;
    }

    private static BigInteger estimateGasWithSolidityType(KIP37 kip37, String functionName, CallObject callObject, List<Type> arguments) throws IOException {
        String gas = kip37.getMethod(functionName).estimateGasWithSolidityWrapper(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.5)).toBigInteger();

        return gasInteger;
    }


}
