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

package com.klaytn.caver.rpc;

import com.klaytn.caver.account.IAccountKey;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.KlayRewards.BlockRewards;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.Utils;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Klay {

    /**
     * JSON-RPC service instance.
     */
    protected final Web3jService web3jService;

    /**
     * Creates a Klay instance
     * @param web3jService JSON-RPC service instance.
     */
    public Klay(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    /**
     * Returns true if the account associated with the address is created. It returns false otherwise.<p>
     * It sets block tag to "LATEST"
     * @param address The account address
     * @return Boolean - The existence of an input address
     */
    public Request<?, Boolean> accountCreated(String address) {
        return accountCreated(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns true if the account associated with the address is created. It returns false otherwise.
     * @param address The account address
     * @param blockNumber The block number.
     * @return Boolean - The existence of an input address
     */
    public Request<?, Boolean> accountCreated(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return accountCreated(address, blockParameterNumber);
    }

    /**
     * Returns true if the account associated with the address is created. It returns false otherwise.
     * @param address The account address.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Boolean - The existence of an input address
     */
    public Request<?, Boolean> accountCreated(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_accountCreated",
                Arrays.asList(
                        address,
                        blockTag
                ),
                web3jService,
                Boolean.class
        );
    }

    /**
     * Returns a list of addresses owned by client.
     * @return Addresses - Addresses owned by the client.
     */
    public Request<?, Addresses> getAccounts() {
        return new Request<>(
                "klay_accounts",
                Collections.<String>emptyList(),
                web3jService,
                Addresses.class);
    }

    /**
     * Encodes an account key using the RLP encoding scheme.
     * @param accountKey Account Key Object
     * @return Bytes
     */
    public Request<?, Bytes> encodeAccountKey(IAccountKey accountKey) {
        return new Request<>(
                "klay_encodeAccountKey",
                Arrays.asList(accountKey),
                web3jService,
                Bytes.class);
    }

    /**
     * Decodes an RLP encoded account key.
     * @param encodedAccountKey RLP encoded account key
     * @return AccountKeyResponse
     */
    public Request<?, AccountKey> decodeAccountKey(String encodedAccountKey) {
        return new Request<>(
                "klay_decodeAccountKey",
                Arrays.asList(encodedAccountKey),
                web3jService,
                AccountKey.class
        );
    }


    /**
     * Returns the account information of a given address.<p>
     * There are two different account types in Klaytn: Externally Owned Account (EOA) and Smart Contract Account.<p>
     * It sets block tag to "LATEST"
     * @param address The account address.
     * @return AccountResponse
     */
    public Request<?, Account> getAccount(String address) {
        return getAccount(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns the account information of a given address.
     * There are two different account types in Klaytn: Externally Owned Account (EOA) and Smart Contract Account.
     * @param address The account address.
     * @param blockNumber The block number..
     * @return AccountResponse
     */
    public Request<?, Account> getAccount(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getAccount(address, blockParameterNumber);
    }

    /**
     * Returns the account information of a given address.<p>
     * There are two different account types in Klaytn: Externally Owned Account (EOA) and Smart Contract Account.
     * @param address The account address
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return AccountResponse
     */
    public Request<?, Account> getAccount(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getAccount",
                Arrays.asList(
                        address,
                        blockTag
                ),
                web3jService,
                Account.class);
    }

    /**
     * Returns AccountKey of a given address.<p>
     * If the account has AccountKeyLegacy or the account of the given address is a Smart Contract Account, it will return an empty key value.
     * It sets block tag to "LATEST".
     * @param address The account address
     * @return AccountKeyResponse
     */
    public Request<?, AccountKey> getAccountKey(String address) {
        return getAccountKey(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns AccountKey of a given address.<p>
     * If the account has AccountKeyLegacy or the account of the given address is a Smart Contract Account, it will return an empty key value.
     * @param address The account address
     * @param blockNumber The block number..
     * @return AccountKeyResponse
     */
    public Request<?, AccountKey> getAccountKey(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getAccountKey(address, blockParameterNumber);
    }

    /**
     * Returns AccountKey of a given address.<p>
     * If the account has AccountKeyLegacy or the account of the given address is a Smart Contract Account, it will return an empty key value.
     * @param address The account address
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return AccountKeyResponse
     */
    public Request<?, AccountKey> getAccountKey(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getAccountKey",
                Arrays.asList(
                        address,
                        blockTag.getValue()
                ),
                web3jService,
                AccountKey.class);
    }

    /**
     * Returns the balance of the account of given address.<p>
     * It sets block tag to "LATEST".
     * @param address The account address to check for balance.
     * @return Quantity
     */
    public Request<?, Quantity> getBalance(String address) {
        return getBalance(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns the balance of the account of given address.
     * @param address The account address to check for balance.
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getBalance(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getBalance(address, blockParameterNumber);
    }

    /**
     * Returns the balance of the account of given address.
     * @param address The account address to check for balance.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Quantity
     */
    public Request<?, Quantity> getBalance(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getBalance",
                Arrays.asList(
                        address,
                        blockTag
                ),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns code at a given address.<p>
     * It sets block tag to "LATEST"
     * @param address The account address
     * @return Bytes
     */
    public Request<?, Bytes> getCode(String address) {
        return getCode(address, DefaultBlockParameterName.LATEST);
    }


    /**
     * Returns code at a given address.
     * @param address The account address
     * @param blockNumber The block number.
     * @return Bytes
     */
    public Request<?, Bytes> getCode(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getCode(address, blockParameterNumber);
    }

    /**
     * Returns code at a given address.
     * @param address The account address
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Bytes
     */
    public Request<?, Bytes> getCode(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCode",
                Arrays.asList(
                        address,
                        blockTag.getValue()
                ),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns the number of transactions sent from an address.<p>
     * It sets block tag to "LATEST".
     * @param address The account address
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCount(String address) {
        return getTransactionCount(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns the number of transactions sent from an address.
     * @param address The account address
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCount(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getTransactionCount(address, blockParameterNumber);
    }

    /**
     * Returns the number of transaction sent from an address
     * @param address The account address
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCount(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getTransactionCount",
                Arrays.asList(
                        address,
                        blockTag.getValue()
                ),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns true if an input account has a non-empty codeHash at the time of a specific block number.<p>
     * It returns false if the account is an EOA or a smart contract account which doesn't have codeHash.<p>
     * It sets block tag to "LATEST".
     * @param address The account address
     * @return Boolean
     */
    public Request<?, Boolean> isContractAccount(String address) {
        return isContractAccount(address, DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns true if an input account has a non-empty codeHash at the time of a specific block number.<p>
     * It returns false if the account is an EOA or a smart contract account which doesn't have codeHash.
     * @param address The account address
     * @param blockNumber The block number..
     * @return Boolean
     */
    public Request<?, Boolean> isContractAccount(String address, long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return isContractAccount(address, blockParameterNumber);
    }

    /**
     * Returns true if an input account has a non-empty codeHash at the time of a specific block number.<p>
     * It returns false if the account is an EOA or a smart contract account which doesn't have codeHash.
     * @param address The account address
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Boolean
     */
    public Request<?, Boolean> isContractAccount(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_isContractAccount",
                Arrays.asList(
                        address,
                        blockTag.getValue()
                ),
                web3jService,
                Boolean.class);
    }

    /**
     * The sign method calculates a Klaytn-specific signature.<p>
     * NOTE : The address to sign with must be unlocked.
     * @param address The account address
     * @param message The message to sign.
     * @return Bytes
     */
    public Request<?, Bytes> sign(String address, String message) {
        return new Request<>(
                "klay_sign",
                Arrays.asList(
                        address,
                        message
                ),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns the number of most recent block.
     * @return Quantity
     */
    public Request<?, Quantity> getBlockNumber() {
        return new Request<>(
                "klay_blockNumber",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns a block header by block hash.
     * <pre>Example:
     * {@code
     *  String blockHash = "0x5f06bed1f3f11d4f2b0760cfdf95ce6b2e6431ca46e2b778f2b958d4e5b9aa43";
     *  BlockHeader response = caver.rpc.klay.getHeaderByHash(blockHash);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockHash The hash of block.
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeaderByHash(String blockHash) {
        return new Request<>(
                "klay_getHeaderByHash",
                Arrays.asList(blockHash),
                web3jService,
                BlockHeader.class);
    }

    /**
     * Returns a block header by block number.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeaderByNumber(BigInteger.valueOf(5));
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockNumber The block number.
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeaderByNumber(BigInteger blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);
        return getHeaderByNumber(blockParameterNumber);
    }

    /**
     * Returns a block header by block tag.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeaderByNumber(DefaultBlockParameterName.LATEST);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeaderByNumber(DefaultBlockParameterName blockTag) {
        return getHeaderByNumber(DefaultBlockParameter.valueOf(blockTag.getValue()));
    }

    /**
     * Returns a block header by block number or block tag.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeaderByNumber(DefaultBlockParameterName.LATEST);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     *
     *  response = caver.rpc.klay.getHeaderByNumber(new DefaultBlockParameterNumber(0));
     *  blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockNumberOrTag The block number or block tag which is one of "latest", "earliest", or "pending".
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeaderByNumber(DefaultBlockParameter blockNumberOrTag) {
        return new Request<>(
                "klay_getHeaderByNumber",
                Arrays.asList(blockNumberOrTag),
                web3jService,
                BlockHeader.class);
    }

    /**
     * Returns a block header by block hash.
     * <pre>Example:
     * {@code
     *  String blockHash = "0x5f06bed1f3f11d4f2b0760cfdf95ce6b2e6431ca46e2b778f2b958d4e5b9aa43";
     *  BlockHeader response = caver.rpc.klay.getHeader(blockHash);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockHash The hash of block.
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeader(String blockHash) {
        return getHeaderByHash(blockHash);
    }

    /**
     * Returns a block header by block number.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeaderByNumber(BigInteger.valueOf(5));
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockNumber The block number.
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeader(BigInteger blockNumber) {
        return getHeaderByNumber(blockNumber);
    }

    /**
     * Returns a block header by block tag.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeader(DefaultBlockParameterName.LATEST);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeader(DefaultBlockParameterName blockTag) {
        return getHeaderByNumber(DefaultBlockParameter.valueOf(blockTag.getValue()));
    }

    /**
     * Returns a block header by block number or block tag.
     * <pre>Example:
     * {@code
     *  BlockHeader response = caver.rpc.klay.getHeaderByNumber(DefaultBlockParameterName.LATEST);
     *  BlockHeader.BlockHeaderData blockHeaderData = response.getResult();
     *
     *  response = caver.rpc.klay.getHeaderByNumber(new DefaultBlockParameterNumber(0));
     *  blockHeaderData = response.getResult();
     * }
     * </pre>
     * @param blockNumberOrTag The block number or block tag which is one of "latest", "earliest", or "pending".
     * @return BlockHeader
     */
    public Request<?, BlockHeader> getHeader(DefaultBlockParameter blockNumberOrTag) {
        return getHeaderByNumber(blockNumberOrTag);
    }

    /**
     * Returns information about a block by block number.<p>
     * It set "isFullTransaction" param to false.
     * @param blockNumber The block number.
     * @return Block
     */
    public Request<?, Block> getBlockByNumber(long blockNumber) {
        return getBlockByNumber(blockNumber, false);
    }

    /**
     * Returns information about a block by block number.
     * @param blockNumber The block number.
     * @param isFullTransaction If true it returns the full transaction objects, if false only the hashes of the transactions.
     * @return Block
     */
    public Request<?, Block> getBlockByNumber(long blockNumber, boolean isFullTransaction) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);
        return getBlockByNumber(blockParameterNumber, isFullTransaction);
    }

    /**
     * Returns information about a block by block number.<p>
     * It set "isFullTransaction" param to false.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return KlayBlock
     */
    public Request<?, Block> getBlockByNumber(DefaultBlockParameter blockTag) {
        return getBlockByNumber(blockTag, false);
    }

    /**
     * Returns information about a block by block number.
     * @param defaultBlockParameter The string "latest", "earliest" or "pending"
     * @param isFullTransaction If true it returns the full transaction objects, if false only the hashes of the transactions.
     * @return Block
     */
    public Request<?, Block> getBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByNumber",
                Arrays.asList(defaultBlockParameter, isFullTransaction),
                web3jService,
                Block.class);
    }

    /**
     * Returns information about a block by block number.<p>
     * It set "isFullTransaction" param to false.
     * @param blockHash The hash of block.
     * @return Block
     */
    public Request<?, Block> getBlockByHash(String blockHash) {
        return getBlockByHash(blockHash, false);
    }

    /**
     * Returns information about a block by block number.
     * @param blockHash The hash of block.
     * @param isFullTransaction If true it returns the full transaction objects, if false only the hashes of the transactions.
     * @return Block
     */
    public Request<?, Block> getBlockByHash(String blockHash, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByHash",
                Arrays.asList(blockHash, isFullTransaction),
                web3jService,
                Block.class);
    }

    /**
     * Returns receipts included in a block identified by block hash.
     * @param blockHash The hash of block.
     * @return BlockReceipt
     */
    public Request<?, BlockTransactionReceipts> getBlockReceipts(String blockHash) {
        return new Request<>(
                "klay_getBlockReceipts",
                Arrays.asList(blockHash),
                web3jService,
                BlockTransactionReceipts.class);
    }

    /**
     * Returns the number of transactions in a block matching the given block number.
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getBlockTransactionCountByNumber(long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);
        return getTransactionCountByNumber(blockParameterNumber);
    }

    /**
     * Returns the number of transactions in a block matching the given block number.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Quantity
     */
    public Request<?, Quantity> getBlockTransactionCountByNumber(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getBlockTransactionCountByNumber",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the number of transactions in a block matching the given block number.
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCountByNumber(long blockNumber) {
        return getBlockTransactionCountByNumber(blockNumber);
    }

    /**
     * Returns the number of transactions in a block matching the given block number.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCountByNumber(DefaultBlockParameter blockTag) {
        return getBlockTransactionCountByNumber(blockTag);
    }

    /**
     * Returns the number of transactions in a block from a block matching the given block hash.
     * @param blockHash The hash of a block
     * @return Quantity
     */
    public Request<?, Quantity> getBlockTransactionCountByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the number of transactions in a block from a block matching the given block hash.
     * @param blockHash The hash of a block
     * @return Quantity
     */
    public Request<?, Quantity> getTransactionCountByHash(String blockHash) {
        return getBlockTransactionCountByHash(blockHash);
    }

    /**
     * Returns a block with consensus information matched by the given hash.
     * @param blockHash The hash of a block.
     * @return BlockWithConsensusInfo
     */
    public Request<?, BlockWithConsensusInfo> getBlockWithConsensusInfoByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByHash",
                Arrays.asList(blockHash),
                web3jService,
                BlockWithConsensusInfo.class);
    }

    /**
     * Returns a block with consensus information matched by the given block number.
     * @param blockNumber The block number.
     * @return BlockWithConsensusInfo
     */
    public Request<?, BlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getBlockWithConsensusInfoByNumber(blockParameterNumber);
    }

    /**
     * Returns a block with consensus information matched by the given block number.
     * @param blockTag The string "latest", "earliest"
     * @return BlockWithConsensusInfo
     */
    public Request<?, BlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByNumber",
                Arrays.asList(blockTag),
                web3jService,
                BlockWithConsensusInfo.class);
    }

    /**
     * Returns a list of all validators in the committee at the specified block.<p>
     * If the parameter is not set, returns a list of all validators in the committee at the latest block.
     * It sets block tag to "LATEST".
     * @return Addresses
     */
    public Request<?, Addresses> getCommittee() {
        return getCommittee(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns a list of all validators in the committee at the specified block.<p>
     * If the parameter is not set, returns a list of all validators in the committee at the latest block.
     * @param blockNumber The block number.
     * @return Addresses
     */
    public Request<?, Addresses> getCommittee(long blockNumber) {
        DefaultBlockParameterNumber parameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getCommittee(parameterNumber);
    }

    /**
     * Returns a list of all validators in the committee at the specified block.<p>
     * If the parameter is not set, returns a list of all validators in the committee at the latest block.
     * @param blockTag The string "latest", "earliest"
     * @return Addresses
     */
    public Request<?, Addresses> getCommittee(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCommittee",
                Arrays.asList(blockTag),
                web3jService,
                Addresses.class
        );
    }

    /**
     * Returns the size of the committee at the specified block.<p>
     * If the parameter is not set, returns the size of the committee at the latest block.<p>
     * It sets block tag to "LATEST".
     * @return Quantity
     */
    public Request<?, Quantity> getCommitteeSize() {
        return getCommitteeSize(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns the size of the committee at the specified block.<p>
     * If the parameter is not set, returns the size of the committee at the latest block.
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getCommitteeSize(long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getCommitteeSize(blockParameterNumber);
    }

    /**
     * Returns the size of the committee at the specified block.<p>
     * If the parameter is not set, returns the size of the committee at the latest block.
     * @param blockTag The string "earliest" or "latest".
     * @return
     */
    public Request<?, Quantity> getCommitteeSize(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCommitteeSize",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class
        );
    }

    /**
     * Returns a list of all validators of the council at the specified block.<p>
     * If the parameter is not set, returns a list of all validators of the council at the latest block.<p>
     * It set to block tag to "LATEST".
     * @return Addresses
     */
    public Request<?, Addresses> getCouncil() {
        return getCouncil(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns a list of all validators of the council at the specified block.<p>
     * If the parameter is not set, returns a list of all validators of the council at the latest block.<p>
     * @param blockNumber The block number.
     * @return Addresses
     */
    public Request<?, Addresses> getCouncil(long blockNumber) {
        DefaultBlockParameterNumber number = new DefaultBlockParameterNumber(blockNumber);
        return getCouncil(number);
    }

    /**
     * Returns a list of all validators of the council at the specified block.<p>
     * If the parameter is not set, returns a list of all validators of the council at the latest block.
     * @param blockTag The string "earliest" or "latest".
     * @return Addresses
     */
    public Request<?, Addresses> getCouncil(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCouncil",
                Arrays.asList(blockTag),
                web3jService,
                Addresses.class
        );
    }

    /**
     * Returns the size of the council at the specified block.<p>
     * If the parameter is not set, returns the size of the council at the latest block.<p>
     * It sets block tag to "LATEST".
     * @return Quantity
     */
    public Request<?, Quantity> getCouncilSize() {
        return getCouncilSize(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns the size of the council at the specified block.<p>
     * If the parameter is not set, returns the size of the council at the latest block.
     * @param blockNumber The block number
     * @return Quantity
     */
    public Request<?, Quantity> getCouncilSize(long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getCouncilSize(blockParameterNumber);
    }

    /**
     * Returns the size of the council at the specified block.<p>
     * If the parameter is not set, returns the size of the council at the latest block.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Quantity
     */
    public Request<?, Quantity> getCouncilSize(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCouncilSize",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class
        );
    }

    /**
     * Returns the value from a storage position at a given address.
     * @param address The account address.
     * @param position Integer of the position in the storage.
     * @param blockNumber The block number.
     * @return Bytes
     */
    public Request<?, Bytes> getStorageAt(
            String address, DefaultBlockParameterNumber position, long blockNumber) {
        DefaultBlockParameterNumber defaultBlockParameterNumber = new DefaultBlockParameterNumber(blockNumber);

        return getStorageAt(address, position, blockNumber);
    }

    /**
     * Returns the value from a storage position at a given address.
     * @param address The account address.
     * @param position Integer of the position in the storage.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return Bytes
     */
    public Request<?, Bytes> getStorageAt(
            String address, DefaultBlockParameterNumber position, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getStorageAt",
                Arrays.asList(
                        address,
                        position,
                        blockTag.getValue()
                ),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns an object with data about the sync status or false.
     * @return KlaySyncing
     */
    public Request<?, KlaySyncing> isSyncing() {
        return new Request<>(
                "klay_syncing",
                Collections.<String>emptyList(),
                web3jService,
                KlaySyncing.class);
    }

    /**
     * Returns the chain ID of the chain.
     * @return Quantity
     */
    public Request<?, Quantity> getChainID() {
        return new Request<>(
                "klay_chainID",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Executes a new message call immediately without creating a transaction on the block chain.<p>
     * It returns data or an error object of JSON RPC if error occurs.<p>
     * It sets block tag to "LATEST".
     * @param callObject The transaction call object.
     * @return Bytes
     */
    public Request<?, Bytes> call(CallObject callObject) {
        return call(callObject, DefaultBlockParameterName.LATEST);
    }

    /**
     * Executes a new message call immediately without creating a transaction on the block chain.<p>
     * It returns data or an error object of JSON RPC if error occurs.<p>
     * @param callObject The transaction call object.
     * @param blockNumber The block number.
     * @return Bytes
     */
    public Request<?, Bytes> call(CallObject callObject, Quantity blockNumber) {
        return new Request<>(
                "klay_call",
                Arrays.asList(callObject, blockNumber),
                web3jService,
                Bytes.class);
    }

    /**
     * Executes a new message call immediately without creating a transaction on the block chain.<p>
     * It returns data or an error object of JSON RPC if error occurs.
     * @param callObject The transaction call object.
     * @param blockTag the string "latest", "earliest" or "pending"
     * @return Bytes
     */
    public Request<?, Bytes> call(CallObject callObject, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_call",
                Arrays.asList(callObject, blockTag),
                web3jService,
                Bytes.class);
    }

    /**
     * Generates and returns an estimate of how much gas is necessary to allow the transaction to complete.<p>
     * The transaction will not be added to the blockchain.<p> Note that the estimate may be significantly more
     * than the amount of gas actually used by the transaction, for a variety of reasons including Klaytn Virtual
     * Machine mechanics and node performance.
     * @param callObject The transaction call object.
     * @return Quantity
     */
    public Request<?, Quantity> estimateGas(CallObject callObject) {
        return new Request<>(
                "klay_estimateGas",
                Arrays.asList(callObject),
                web3jService,
                Quantity.class);
    }

    /**
     * Generates and returns an estimate of how much computation cost spent to execute the transaction.<p>
     * Klaytn limits the computation cost of a transaction to 100000000 currently not to take too much time
     * by a single transaction.<p> The transaction will not be added to the blockchain like klay_estimateGas.
     * @param callObject The transaction call object.
     * @return Quantity
     */
    public Request<?, Quantity> estimateComputationCost(CallObject callObject) {
        return estimateComputationCost(callObject, DefaultBlockParameterName.LATEST);
    }

    /**
     * Generates and returns an estimate of how much computation cost spent to execute the transaction.<p>
     * Klaytn limits the computation cost of a transaction to 100000000 currently not to take too much time
     * by a single transaction.<p> The transaction will not be added to the blockchain like klay_estimateGas.
     * @param callObject The transaction call object.
     * @return Quantity
     */
    public Request<?, Quantity> estimateComputationCost(CallObject callObject, long blockNumber) {
        return estimateComputationCost(callObject, new DefaultBlockParameterNumber(blockNumber));
    }

    /**
     * Generates and returns an estimate of how much computation cost spent to execute the transaction.<p>
     * Klaytn limits the computation cost of a transaction to 100000000 currently not to take too much time
     * by a single transaction.<p> The transaction will not be added to the blockchain like klay_estimateGas.
     * @param callObject The transaction call object.
     * @return Quantity
     */
    public Request<?, Quantity> estimateComputationCost(CallObject callObject, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_estimateComputationCost",
                Arrays.asList(callObject, blockTag),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns information about a transaction by block hash and transaction index position.
     * @param blockHash The hash of a block.
     * @param index Integer of the transaction index position.
     * @return Transaction
     */
    public Request<?, Transaction> getTransactionByBlockHashAndIndex(String blockHash, long index) {
        DefaultBlockParameterNumber indexNumber = new DefaultBlockParameterNumber(index);

        return new Request<>(
                "klay_getTransactionByBlockHashAndIndex",
                Arrays.asList(blockHash, indexNumber),
                web3jService,
                Transaction.class);
    }

    /**
     * Returns information about a transaction by block number and transaction index position.
     * @param blockNumber The block number
     * @param index The transaction index position.
     * @return Transaction
     */
    public Request<?, Transaction> getTransactionByBlockNumberAndIndex(long blockNumber, long index) {
        return getTransactionByBlockNumberAndIndex(
                new DefaultBlockParameterNumber(blockNumber),
                new DefaultBlockParameterNumber(index)
        );
    }


    /**
     * Returns information about a transaction by block number and transaction index position.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @param index The transaction index position.
     * @return Transaction
     */
    public Request<?, Transaction> getTransactionByBlockNumberAndIndex(DefaultBlockParameter blockTag, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockNumberAndIndex",
                Arrays.asList(blockTag, index),
                web3jService,
                Transaction.class);
    }

    /**
     * Returns the information about a transaction requested by transaction hash.
     * @param txHash The hash of a transaction
     * @return Transaction
     */
    public Request<?, Transaction> getTransactionByHash(String txHash) {
        return new Request<>(
                "klay_getTransactionByHash",
                Arrays.asList(txHash),
                web3jService,
                Transaction.class);
    }

    /**
     * Returns the information about a transaction requested by sender transaction hash.<p>
     * Please note that this API returns correct result only if indexing feature is enabled by --sendertxhashindexing.<p>
     * This can be checked by call klay_isSenderTxHashIndexingEnabled.
     * @param senderTxHash The hash of a transaction before signing of feePayer(senderTransactionHash)
     * @return Transaction
     */
    public Request<?, Transaction> getTransactionBySenderTxHash(String senderTxHash) {
        return new Request<>(
                "klay_getTransactionBySenderTxHash",
                Arrays.asList(senderTxHash),
                web3jService,
                Transaction.class);
    }

    /**
     * Returns the receipt of a transaction by transaction hash.<p>
     * NOTE: The receipt is not available for pending transactions.
     * @param transactionHash The hash of a transaction.
     * @return TransactionReceipt
     */
    public Request<?, TransactionReceipt> getTransactionReceipt(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                TransactionReceipt.class);
    }

    /**
     * Returns the receipt of a transaction by sender transaction hash.
     * @param transactionHash The hash of a transaction before signing of feePayer(senderTransactionHash).
     * @return TransactionReceipt
     */
    public Request<?, TransactionReceipt> getTransactionReceiptBySenderTxHash(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceiptBySenderTxHash",
                Arrays.asList(transactionHash),
                web3jService,
                TransactionReceipt.class);
    }

    /**
     * Creates a new message call transaction or a contract creation for signed transactions.
     * @param signedTransactionData The signed transaction data.
     * @return Bytes32
     */
    public Request<?, Bytes32> sendRawTransaction(String signedTransactionData) {
        return new Request<>(
                "klay_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                Bytes32.class);
    }

    /**
     * Creates a new message call transaction or a contract creation for signed transactions.
     * @param transaction A transaction instance.
     * @return Bytes32
     */
    public Request<?, Bytes32> sendRawTransaction(AbstractTransaction transaction) {
        String rawTransaction = transaction.getRLPEncoding();

        return new Request<>(
                "klay_sendRawTransaction",
                Arrays.asList(rawTransaction),
                web3jService,
                Bytes32.class);
    }

    /**
     * Constructs a transaction with given parameters, signs the transaction with a sender's private key and propagates the transaction to Klaytn network.<p>
     * NOTE: The address to sign with must be unlocked.
     * @param transaction The object inherits AbstractTransaction.
     * @return Bytes32
     */
    public Request<?, Bytes32> sendTransaction(AbstractTransaction transaction) {
        return new Request<>(
                "klay_sendTransaction",
                Arrays.asList(transaction),
                web3jService,
                Bytes32.class);
    }

    /**
     * Constructs a transaction with given parameters, signs the transaction with a fee payer's private key and propagates the transaction to Klaytn network.<p>
     * This API supports only fee delegated type (including partial fee delegated type) transactions.<p>
     * NOTE: The fee payer address to sign with must be unlocked.
     * @param transaction The object inherits AbstractFeeDelegatedTransaction.
     * @return Bytes32
     */
    public Request<?, Bytes32> sendTransactionAsFeePayer(AbstractFeeDelegatedTransaction transaction) {
        return new Request<>(
                "klay_sendTransactionAsFeePayer",
                Arrays.asList(transaction),
                web3jService,
                Bytes32.class);
    }

    /**
     * Constructs a transaction with given parameters and signs the transaction with a sender's private key.<p>
     * This method can be used either to generate a sender signature or to make a final raw transaction that is ready to submit to Klaytn network.<p>
     * NOTE: The address to sign with must be unlocked.
     * @param transaction The object inherits AbstractTransaction.
     * @return KlaySignTransaction
     */
    public Request<?, SignTransaction> signTransaction(AbstractTransaction transaction) {
        if(Utils.isEmptySig(transaction.getSignatures())) {
            transaction.getSignatures().remove(0);
        }

        return new Request<>(
                "klay_signTransaction",
                Arrays.asList(transaction),
                web3jService,
                SignTransaction.class);
    }

    /**
     * Constructs a transaction with given parameters and signs the transaction with a fee payer's private key.<p>
     * This method can be used either to generate a fee payer signature or to make a final raw transaction that is ready to submit to Klaytn network.<p>
     * In case you just want to extract the fee-payer signature, simply take the feePayerSignatures from the result.<p>
     * Note that the raw transaction is not final if the sender's signature is not attached (that is, signatures in tx is empty).<p>
     * NOTE: The fee payer address to sign with must be unlocked.
     * @param transaction The object inherits AbstractFeeDelegatedTransaction.
     * @return KlaySignTransaction
     */
    public Request<?, SignTransaction> signTransactionAsFeePayer(AbstractFeeDelegatedTransaction transaction) {
        if(Utils.isEmptySig(transaction.getSignatures())) {
            transaction.getSignatures().remove(0);
        }

        return new Request<>(
                "klay_signTransactionAsFeePayer",
                Arrays.asList(transaction),
                web3jService,
                SignTransaction.class);
    }

    /**
     * Returns the decoded anchored data in the transaction for the given transaction hash.
     * @param hash The hash of transaction
     * @return DecodeAnchoringTransaction
     */
    public Request<?, DecodeAnchoringTransaction> getDecodedAnchoringTransaction(String hash) {
        return new Request<>(
                "klay_getDecodedAnchoringTransactionByHash",
                Arrays.asList(hash),
                web3jService,
                DecodeAnchoringTransaction.class
        );
    }

    /**
     * Returns the current client version of a Klaytn node.
     * @return Bytes
     */
    public Request<?, Bytes> getClientVersion() {
        return new Request<>(
                "klay_clientVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    /**
     * Provides the latest chain configuration
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfig().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig() {
        return getChainConfigAt(DefaultBlockParameterName.LATEST);
    }

    /**
     * Provides the chain configuration at the specified block number
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfig(BigInteger.ZERO).send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig(BigInteger blockNumber) {
        return getChainConfigAt(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfig("latest").send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig(String blockTag) {
        return getChainConfigAt(DefaultBlockParameterName.fromString(blockTag));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfig(DefaultBlockParameterName.LATEST).send();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig(DefaultBlockParameterName blockTag) {
        return getChainConfig((DefaultBlockParameter)blockTag);
    }
    public Request<?, GovernanceChainConfig> getChainConfig(DefaultBlockParameter blockNumberOrTag) {
        return new Request<>(
                "klay_chainConfig",
                Arrays.asList(blockNumberOrTag),
                web3jService,
                GovernanceChainConfig.class
        );
    }

    /**
     * Provides the chain configuration at the specified block number
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfigAt(BigInteger.ZERO).send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfigAt(BigInteger blockNumber) {
        return getChainConfigAt(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfigAt("latest").send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfigAt(String blockTag) {
        return getChainConfigAt(DefaultBlockParameterName.fromString(blockTag));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.klay.getChainConfigAt(DefaultBlockParameterName.LATEST).send();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfigAt(DefaultBlockParameterName blockTag) {
        return getChainConfigAt((DefaultBlockParameter)blockTag);
    }
    public Request<?, GovernanceChainConfig> getChainConfigAt(DefaultBlockParameter blockNumberOrTag) {
        return new Request<>(
                "klay_chainConfigAt",
                Arrays.asList(blockNumberOrTag),
                web3jService,
                GovernanceChainConfig.class
        );
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.<p>
     * It pass the latest block tag as a parameter.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getParams().send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }</pre>
     * @return Request&lt;?, Bytes20&gt;
     */
    public Request<?, GovernanceItems> getParams() {
        return getParams(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getParams(BigInteger.ZERO).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }</pre>
     * @param blockNumber The block number to query.
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getParams(BigInteger blockNumber) {
        return getParams(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getParams("latest").send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getParams(String blockTag) {
        return getParams(DefaultBlockParameterName.fromString(blockTag));
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getParams(DefaultBlockParameterName.LATEST).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getParams(DefaultBlockParameterName blockTag) {
        return getGovParamsAt((DefaultBlockParameter)blockTag);
    }

    Request<?, GovernanceItems> getParams(DefaultBlockParameter blockParameter) {
        return new Request<>(
                "klay_getParams",
                Arrays.asList(blockParameter.getValue()),
                web3jService,
                GovernanceItems.class
        );
    }

    /**
    * It pass the latest block tag as a parameter.
    * <pre>Example :
    * {@code
    * GovernanceItems response = caver.rpc.klay.getGovParams().send();
    * GovernanceItems response = caver.rpc.klay.getParams().send();
    * Map<String, Object> governanceItem = response.getResult();
    *
    * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
    * }</pre>
    * @return Request&lt;?, Bytes20&gt;
    */
    public Request<?, GovernanceItems> getGovParams() {
        return getGovParamsAt(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getGovParamsAt(BigInteger.ZERO).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }</pre>
     * @param blockNumber The block number to query.
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getGovParamsAt(BigInteger blockNumber) {
        return getGovParamsAt(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getGovParamsAt("latest").send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getGovParamsAt(String blockTag) {
        return getGovParamsAt(DefaultBlockParameterName.fromString(blockTag));
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.klay.getGovParamsAt(DefaultBlockParameterName.LATEST).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getGovParamsAt(DefaultBlockParameterName blockTag) {
        return getGovParamsAt((DefaultBlockParameter)blockTag);
    }

    Request<?, GovernanceItems> getGovParamsAt(DefaultBlockParameter blockParameter) {
        return new Request<>(
                "klay_govParamsAt",
                Arrays.asList(blockParameter.getValue()),
                web3jService,
                GovernanceItems.class
        );
    }

    /**
     * The getStakingInfo returns staking information at a specific block.<p>
     * It passes the latest block tag as a parameter.
     * <pre>Example :
     * {@code
     * GovernanceStackingInfo response = caver.rpc.klay.getStakingInfo().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceStackingInfo&gt;
     */
    public Request<?, GovernanceStakingInfo> getStakingInfo() {
        return getStakingInfo(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns staking information at a specific block.<p>
     * <pre>Example :
     * {@code
     * GovernanceStackingInfo response = caver.rpc.klay.getStakingInfo(BigInteger.ZERO).send();
     * }
     * </pre>
     * @param blockNumber The block number.
     * @return Request&lt;?, GovernanceStackingInfo&gt;
     */
    public Request<?, GovernanceStakingInfo> getStakingInfo(BigInteger blockNumber) {
        return getStakingInfo(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Returns staking information at a specific block.<p>
     * <pre>Example :
     * {@code
     * GovernanceStackingInfo response = caver.rpc.klay.getStakingInfo("latest").send();
     * }
     * </pre>
     * @param blockTag The block tag.
     * @return Request&lt;?, GovernanceStackingInfo&gt;
     */
    public Request<?, GovernanceStakingInfo> getStakingInfo(String blockTag) {
        DefaultBlockParameterName blockTagName = DefaultBlockParameterName.fromString(blockTag);
        return getStakingInfo(blockTagName);
    }

    /**
     * Returns staking information at a specific block.<p>
     * <pre>Example :
     * {@code
     * GovernanceStackingInfo response = caver.rpc.klay.getStakingInfo(DefaultBlockParameterName.LATEST).send();
     * }
     * </pre>
     * @param blockTag The block tag.
     * @return Request&lt;?, GovernanceStackingInfo&gt;
     */
    public Request<?, GovernanceStakingInfo> getStakingInfo(DefaultBlockParameterName blockTag) {
        return getStakingInfo((DefaultBlockParameter)blockTag);
    }

    Request<?, GovernanceStakingInfo> getStakingInfo(DefaultBlockParameter blockParam) {
        return new Request<>(
                "klay_getStakingInfo",
                Arrays.asList(blockParam),
                web3jService,
                GovernanceStakingInfo.class
        );
    }

    /**
     * Provides the address of the node that a user is using.<p>
     * It is derived from the nodekey and used to sign consensus messages. And the value of "governingnode" has to be one of validator's node address.
     * <pre>Example :
     * {@code
     * Bytes20 response = caver.rpc.klay.getNodeAddress().send();
     * }
     * </pre>
     * @return Request&lt;?, Bytes20&gt;
     */
    public Request<?, Bytes20> getNodeAddress() {
        return new Request<>(
                "klay_nodeAddress",
                Collections.emptyList(),
                web3jService,
                Bytes20.class
        );
    }

    /**
     * Returns distributed block rewards information at the latest block number
     * <pre>Example:
     * {@code
     *  KlayRewards response = caver.rpc.klay.getRewards();
     *  KlayRewards.BlockRewards blockRewards = response.getResult();
     * }
     * </pre>
     * @return KlayRewards
     */
    public Request<?, KlayRewards> getRewards() {
        return getRewards(DefaultBlockParameterName.LATEST);
    }

    /**
     * Returns distributed block rewards infomation by block number.
     * <pre>Example:
     * {@code
     *  KlayRewards response = caver.rpc.klay.getRewards(BigInteger.valueOf(5));
     *  KlayRewards.BlockRewards blockRewards = response.getResult();
     * }
     * </pre>
     * @param blockNumber The block number.
     * @return KlayRewards
     */
    public Request<?, KlayRewards> getRewards(BigInteger blockNumber) {
        return getRewards(new DefaultBlockParameterNumber(blockNumber));
    }

    /**
     * Returns distributed block rewards infomation by block tag.
     * <pre>Example:
     * {@code
     *  KlayRewards response = caver.rpc.klay.getRewards(DefaultBlockParameterName.LATEST);
     *  KlayRewards.BlockRewards blockRewards = response.getResult();
     * }
     * </pre>
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return KlayRewards
     */
    public Request<?, KlayRewards> getRewards(DefaultBlockParameterName blockTag) {
        return getRewards(DefaultBlockParameter.valueOf(blockTag.getValue()));
    }

    /**
     * Returns the current block rewards
     * @return KlayRewards
     */
    public Request<?, KlayRewards> getRewards(DefaultBlockParameter blockNumberOrTag) {
        return new Request<>(
            "klay_getRewards",
            Arrays.asList(blockNumberOrTag),
            web3jService,
            KlayRewards.class);
    }

    /**
     * 
     * @return GovernanceChainConfig
     */

    /**
     * Returns the current price per gas in peb.<p>
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn instead of suggesting a gas price as in Ethereum.
     * @return Quantity
     */
    public Request<?, Quantity> getGasPrice() {
        return new Request<>(
                "klay_gasPrice",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the unit price of the given block in peb.<p>
     * It returns latest unit price.<p>
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn instead of suggesting a gas price as in Ethereum.
     * @return Quantity
     */
    public Request<?, Quantity> getGasPriceAt() {
        return new Request<>(
                "klay_gasPriceAt",
                Arrays.asList(DefaultBlockParameterName.LATEST),
                web3jService,
                Quantity.class
        );
    }

    /**
     * Returns a suggestion for a gas tip cap for dynamic fee transactions in peb.<p>
     * Note: Since Klaytn has a fixed gas price, this `caver.rpc.klay.getMaxPriorityFeePerGas` returns the gas price set by Klaytn.
     * <pre> Example : 
     * {@code
     *
     * Quantity response = caver.rpc.klay.getMaxPriorityFeePerGas().send();
     * BigInteger result = response.getValue();
     *
     * }
     * </pre>
     * @return Quantity
     */
    public Request<?, Quantity> getMaxPriorityFeePerGas() {
        return new Request<>(
                "klay_maxPriorityFeePerGas",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the unit price of the given block in peb.<p>
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn instead of suggesting a gas price as in Ethereum.
     * @param blockNumber The block number.
     * @return Quantity
     */
    public Request<?, Quantity> getGasPriceAt(long blockNumber) {
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);
        return getGasPriceAt(blockParameterNumber);
    }

    /**
     * Returns the unit price of the given block in peb.<p>
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn instead of suggesting a gas price as in Ethereum.
     * @param blockTag The block tag.
     * @return Quantity
     */
    public Request<?, Quantity> getGasPriceAt(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_gasPriceAt",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class
        );
    }

    /**
     * Returns the upper bound gas price in peb.<p>
     * @return Quantity
     */
    public Request<?, Quantity> getUpperBoundGasPrice() {
        return new Request<>(
                "klay_upperBoundGasPrice",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns the lower bound gas price in peb.<p>
     * @return Quantity
     */
    public Request<?, Quantity> getLowerBoundGasPrice() {
        return new Request<>(
                "klay_lowerBoundGasPrice",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Returns true if the node is writing blockchain data in parallel manner. It is enabled by default.
     * @return Boolean
     */
    public Request<?, Boolean> isParallelDBWrite() {
        return new Request<>(
                "klay_isParallelDBWrite",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    /**
     * Returns true if the node is indexing sender transaction hash to transaction hash mapping information.<p>
     * It is disabled by default and can be enabled by --sendertxhashindexing.
     * @return Boolean
     */
    public Request<?, Boolean> isSenderTxHashIndexingEnabled() {
        return new Request<>(
                "klay_isSenderTxHashIndexingEnabled",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    /**
     * Returns the Klaytn protocol version of the node.<p>
     * @return Bytes
     */
    public Request<?, Bytes> getProtocolVersion() {
        return new Request<>(
                "klay_protocolVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    /**
     * Returns the reward base of the current node.<p>
     * Reward base is the address of the account where the block rewards goes to. It is only required for CNs.
     * @return Bytes20
     */
    public Request<?, Bytes20> getRewardbase() {
        return new Request<>(
                "klay_rewardbase",
                Collections.<String>emptyList(),
                web3jService,
                Bytes20.class);
    }

    /**
     * Polling method for a filter, which returns an array of logs which occurred since last poll.
     * @param filterId The filter id.
     * @return KlayLogs
     */
    public Request<?, KlayLogs> getFilterChanges(String filterId) {
        return new Request<>(
                "klay_getFilterChanges",
                Arrays.asList(filterId),
                web3jService,
                KlayLogs.class);
    }

    /**
     * Returns an array of all logs matching filter with given id, which has been obtained using klay_newFilter.<p>
     * Note that filter ids returned by other filter creation functions, such as klay_newBlockFilter or klay_newPendingTransactionFilter, cannot be used with this function.
     * @param filterId The filter id.
     * @return KlayLogs
     */
    public Request<?, KlayLogs> getFilterLogs(String filterId) {
        return new Request<>(
                "klay_getFilterLogs",
                Arrays.asList(filterId),
                web3jService,
                KlayLogs.class);
    }

    /**
     * Returns an array of all logs matching a given filter object.
     * @param filterOption The filter options
     * @return KlayLogs
     */
    public Request<?, KlayLogs> getLogs(KlayLogFilter filterOption) {
        return new Request<>(
                "klay_getLogs",
                Arrays.asList(filterOption),
                web3jService,
                KlayLogs.class);
    }

    /**
     * Creates a filter in the node, to notify when a new block arrives.<p>
     * To check if the state has changed, call klay_getFilterChanges.
     * @return Quantity
     */
    public Request<?, Quantity> newBlockFilter() {
        return new Request<>(
                "klay_newBlockFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Creates a filter object, based on filter options, to notify when the state changes (logs).<p>
     * To check if the state has changed, call getFilterChanges.<p>
     * To obtain all logs matching the filter created by klay_newFilter, call getFilterLogs(String).
     * @param filterOption The filter option.
     * @return Quantity
     */
    public Request<?, Quantity> newFilter(KlayFilter filterOption) {
        return new Request<>(
                "klay_newFilter",
                Arrays.asList(filterOption),
                web3jService,
                Quantity.class);
    }

    /**
     * Creates a filter in the node, to notify when new pending transactions arrive.<p>
     * To check if the state has changed, call klay_getFilterChanges.
     * @return Quantity
     */
    public Request<?, Quantity> newPendingTransactionFilter() {
        return new Request<>(
                "klay_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    /**
     * Uninstalls a filter with given id. Should always be called when watch is no longer needed.<p>
     * Additionally, filters timeout when they are not requested with klay_getFilterChanges for a period of time.
     * @param filterId A filter id.
     * @return Boolean
     */
    public Request<?, Boolean> uninstallFilter(String filterId) {
        return new Request<>(
                "klay_uninstallFilter",
                Arrays.asList(filterId),
                web3jService,
                Boolean.class);
    }

    /**
     * Returns Keccak-256 (not the standardized SHA3-256) of the given data.
     * @param data The data to convert into a SHA3 hash.
     * @return Bytes
     */
    public Request<?, Bytes> sha3(String data) {
        return new Request<>(
                "klay_sha3",
                Arrays.asList(data),
                web3jService,
                Bytes.class);
    }


    /**
     * Returns a list of addresses and storage keys used by the transaction, plus the gas consumed when the access list is added.
     * <pre>Example :
     * {@code
     *
     * BigInteger blockNumber = BigInteger.valueOf(5);
     * AccessListResult accessListResult = caver.rpc.klay.createAccessList(callObject, blockNumber).send();
     *
     * }
     * </pre>
     * @param callObject The transaction call object.
     * @param blockNumber The block number.
     * @return AccessListResult
     */
    public Request<?, AccessListResult> createAccessList(CallObject callObject, BigInteger blockNumber) {
        return createAccessList(callObject, new DefaultBlockParameterNumber(blockNumber));
    }

    /**
     * Returns a list of addresses and storage keys used by the transaction, plus the gas consumed when the access list is added.
     * <pre>Example:
     * {@code
     *
     * AccessListResult accessListResult = caver.rpc.klay.createAccessList(callObject, DefaultBlockParameterName.LATEST).send();
     *
     * }
     * </pre>
     * @param callObject The transaction call object.
     * @param blockTag The string "latest", "earliest" or "pending"
     * @return AccessListResult
     */
    public Request<?,AccessListResult> createAccessList(CallObject callObject, DefaultBlockParameterName blockTag) {
        return createAccessList(callObject, DefaultBlockParameter.valueOf(blockTag.getValue()));
    }

    /**
     * Returns a list of addresses and storage keys used by the transaction, plus the gas consumed when the access list is added.
     * <pre>Example:
     * {@code
     *
     * AccessListResult accessListResult = caver.rpc.klay.createAccessList(callObject, DefaultBlockParameterName.LATEST).send();
     *
     * accessListResult = caver.rpc.klay.createAccessList(callObject, new DefaultBlockParameterNumber(1)).send();
     *
     * }
     * </pre>
     * @param callObject The transaction call object.
     * @param blockNumberOrTag The block number or block tag which is one of "latest", "earliest", or "pending".
     * @return AccessListResult
     */
    public Request<?,AccessListResult> createAccessList(CallObject callObject, DefaultBlockParameter blockNumberOrTag) {
        if (blockNumberOrTag == null) {
            blockNumberOrTag = DefaultBlockParameterName.LATEST;
        }
        return new Request<>(
                "klay_createAccessList",
                Arrays.asList(callObject, blockNumberOrTag),
                web3jService,
                AccessListResult.class
        );
    }

    /**
     * Returns a list of addresses and storage keys used by the transaction, plus the gas consumed when the access list is added.
     * <pre>Example:
     * {@code
     *
     * String blockHash = "0x421440aef6024e2da883eadf663b9b485fe1c14f02883541fa4e6c16f7be8c74";
     * AccessListResult accessListResult = caver.rpc.klay.createAccessList(callObject, blockHash).send();
     *
     * }
     * </pre>
     * @param callObject The transaction call object.
     * @param blockHash The block hash.
     * @return AccessListResult
     */
    public Request<?, AccessListResult> createAccessList(CallObject callObject, String blockHash) {
        return new Request<>(
                "klay_createAccessList",
                Arrays.asList(callObject, blockHash),
                web3jService,
                AccessListResult.class
        );
    }


    /**
     * Returns fee history for the returned block range. This can be a subsection of the requested range if not all blocks are available.
     * <pre>Example:
     * {@code
     *
     * long blockCount = 5;
     * // Use block number from Transaction Receipt data.
     * long lastBlock = new BigInteger(caver.utils.stripHexPrefix(receiptData.getBlockNumber()), 16).longValue();
     * List<Float> rewardPercentiles = new ArrayList<Float>(Arrays.asList(0.3f, 0.5f, 0.8f));
     * FeeHistory feeHistory = caver.rpc.klay.getFeeHistory(blockCount, lastBlock, rewardPercentiles).send();
     *
     * }
     * </pre>
     * @param blockCount Number of blocks in the requested range. Between 1 and 1024 blocks can be requested in a single query. Less than requested may be returned if not all blocks are available.
     * @param lastBlock Highest number block (or block tag string) of the requested range.
     * @param rewardPercentiles A monotonically increasing list of percentile values to sample from each block’s effective priority fees per gas in ascending order, weighted by gas used. (Example: `['0', '25', '50', '75', '100']` or `['0', '0.5', '1', '1.5', '3', '80']`)
     * @return
     */
    public Request<?, FeeHistoryResult> getFeeHistory(long blockCount, long lastBlock, List<Float> rewardPercentiles) {
        return new Request<>(
                "klay_feeHistory",
                Arrays.asList(blockCount, lastBlock, rewardPercentiles),
                web3jService,
                FeeHistoryResult.class);
    }

    /**
     * Returns fee history for the returned block range. This can be a subsection of the requested range if not all blocks are available.
     * <pre>Example:
     * {@code
     *
     * long blockCount = 5;
     * List<Float> rewardPercentiles = new ArrayList<Float>(Arrays.asList(0.3f, 0.5f, 0.8f));
     * FeeHistory feeHistory = caver.rpc.klay.getFeeHistory(blockCount, DefaultBlockParameterName.LATEST, rewardPercentiles).send();
     *
     * }
     * </pre>
     * @param blockCount Number of blocks in the requested range. Between 1 and 1024 blocks can be requested in a single query. Less than requested may be returned if not all blocks are available.
     * @param lastBlock Highest number block (or block tag string) of the requested range.
     * @param rewardPercentiles A monotonically increasing list of percentile values to sample from each block’s effective priority fees per gas in ascending order, weighted by gas used. (Example: `['0', '25', '50', '75', '100']` or `['0', '0.5', '1', '1.5', '3', '80']`)
     * @return
     */
    public Request<?, FeeHistoryResult> getFeeHistory(long blockCount, DefaultBlockParameter lastBlock, List<Float> rewardPercentiles) {
        return new Request<>(
                "klay_feeHistory",
                Arrays.asList(blockCount, lastBlock, rewardPercentiles),
                web3jService,
                FeeHistoryResult.class);
    }

    /**
     * Creates a new subscription to specific events by using RPC Pub/Sub over Websockets.<p>
     *
     * The node will return a subscription id for each subscription created.
     * For each event that matches the subscription, a notification with relevant data is sent together with the subscription id.
     * If a connection is closed, all subscriptions created over the connection are removed. <p>
     *
     * It only allowed a 'newHeads' as a notificationType. <p>
     * Also, It automatically calls a "klay_unsubscribe" API when stopping subscription.
     *
     * <pre>Example
     * {@code
     * Disposable disposable = caver.rpc.klay.subscribe("newHeads", (blockData) -> {
     *
     * });
     *
     * //Cancel subscribe notification
     * disposable.dispose();
     * }
     * </pre>
     *
     * @param type The notification type to subscribe.
     * @param callback The callback method to handle notification.
     * @return Disposable
     */
    public Disposable subscribe(String type, Consumer<NewHeadsNotification> callback) {
        if(!type.equals("newHeads")) {
            throw new IllegalArgumentException("This function only allows the 'newHeads' as a type parameter.");
        }

        final Flowable<NewHeadsNotification> events = subscribeFlowable(type);
        return events.subscribe(callback);
    }

    /**
     * Creates a new subscription to specific events by using RPC Pub/Sub over Websockets.<p>
     *
     * The node will return a subscription id for each subscription created.
     * For each event that matches the subscription, a notification with relevant data is sent together with the subscription id.
     * If a connection is closed, all subscriptions created over the connection are removed. <p>
     *
     * It only allowed a 'logs' as a notification type. <p>
     * Also, It automatically calls a "klay_unsubscribe" API when stopping subscription.
     *
     * <pre>Example
     * {@code
     *
     * KlayFilter options = new KlayFilter();
     * options.setAddress(kip7contract.getContractAddress());
     * options.setFromBlock(DefaultBlockParameterName.LATEST);
     * options.setToBlock(DefaultBlockParameterName.LATEST);
     * options.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
     * options.addSingleTopic("0x0000000000000000000000002c8ad0ea2e0781db8b8c9242e07de3a5beabb71a");
     *
     * //Cancel subscribe notification
     * Disposable disposable = caver.rpc.klay.subscribe("logs", options, (logData) -> {
     *
     * });
     *
     * //Stop to subscribe notification
     * disposable.dispose();
     * }
     * </pre>
     * @param type The notification type to subscribe.
     * @param options The filter options to filter notification.
     * @param callback The callback method to handle notification.
     * @return Disposable
     */
    public Disposable subscribe(String type, KlayFilter options, Consumer<LogsNotification> callback) {
        if(!type.equals("logs")) {
            throw new IllegalArgumentException("This function only allows the 'logs' as a type parameter.");
        }

        final Flowable<LogsNotification> events = subscribeFlowable(type, options);
        return events.subscribe(callback);
    }

    /**
     * Cancels the subscription with a specific subscription id.
     * <pre>Example :
     * {@code
     * caver.rpc.klay.unsubscribe("{subscription id}").send();
     * }</pre>
     * @param subscriptionId The subscription id.
     * @return Boolean
     */
    public Request<?, Boolean> unsubscribe(String subscriptionId) {
        return new Request<>(
                "klay_unsubscribe",
                Arrays.asList(subscriptionId),
                web3jService,
                Boolean.class);
    }

    /**
     * Creates a new subscription to specific events by using RPC Pub/Sub over Websockets.<p>
     *
     * The node will return a subscription id for each subscription created.
     * For each event that matches the subscription, a notification with relevant data is sent together with the subscription id.
     * If a connection is closed, all subscriptions created over the connection are removed. <p>
     *
     * It only allowed a 'newHeads' as a notificationType. <p>
     * Also, It automatically calls a "klay_unsubscribe" API when stopping subscription.<p>
     *
     * You can configure the stream directly with the returned Flowable instance, i.e. register callback methods for various cases.
     *
     * <pre>Example :
     * {@code
     * Flowable<NewHeadsNotification> events = caver.rpc.klay.subscribeFlowable("heads");
     * Disposable disposable = events.take(1).subscribe((data) -> {});
     * }
     * </pre>
     *
     *
     * @param type The notification type to subscribe.
     * @return Flowable
     */
    public Flowable<NewHeadsNotification> subscribeFlowable(String type) {
        Request<?, Quantity> request = new Request<>(
                "klay_subscribe",
                Arrays.asList(type),
                web3jService,
                Quantity.class);

        return web3jService.subscribe(request, "klay_unsubscribe", NewHeadsNotification.class);
    }

    /**
     * Creates a new subscription to specific events by using RPC Pub/Sub over Websockets.<p>
     *
     * The node will return a subscription id for each subscription created.
     * For each event that matches the subscription, a notification with relevant data is sent together with the subscription id.
     * If a connection is closed, all subscriptions created over the connection are removed. <p>
     *
     * It only allowed a 'logs' as a notification type. <p>
     * Also, It automatically calls a "klay_unsubscribe" API when stopping subscription.
     *
     * You can configure the stream directly with the returned Flowable instance, i.e. register callback methods for various cases.
     *
     * <pre>Example
     * {@code
     *
     * KlayFilter options = new KlayFilter();
     * options.setAddress(kip7contract.getContractAddress());
     * options.setFromBlock(DefaultBlockParameterName.LATEST);
     * options.setToBlock(DefaultBlockParameterName.LATEST);
     * options.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
     * options.addSingleTopic("0x0000000000000000000000002c8ad0ea2e0781db8b8c9242e07de3a5beabb71a");
     *
     * Flowable<LogsNotification> events = caver.rpc.klay.subscribeFlowable("logs", options);
     * Disposable disposable = events.take(1).subscribe((logData) -> {});
     * }
     * </pre>
     * @param type The notification type to subscribe.
     * @param options The filter options to filter notification.
     * @return Flowable
     */
    public Flowable<LogsNotification> subscribeFlowable(String type, KlayFilter options) {
        Request<?, Quantity> request = new Request<>(
                "klay_subscribe",
                Arrays.asList(type, options),
                web3jService,
                Quantity.class);

        return web3jService.subscribe(request, "klay_unsubscribe", LogsNotification.class);
    }

    private Request<?, Quantity> subscribe(String type) {
        return new Request<>(
                "klay_subscribe",
                Arrays.asList(type),
                web3jService,
                Quantity.class);
    }


    private Request<?, Quantity> subscribe(String type, KlayFilter options) {
        return new Request<>(
                "klay_subscribe",
                Arrays.asList(type, options),
                web3jService,
                Quantity.class);
    }
}
