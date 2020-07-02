/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver;

import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.tx.account.AccountKey;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;

import java.math.BigInteger;

public interface Klay {

    /**
     * Returns true if the account associated with the address is created. It returns false otherwise.
     *
     * @param address               address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Boolean - The existence of an input address
     */
    Request<?, Boolean> isAccountCreated(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns a list of addresses owned by client.
     *
     * @return Addresses - Addresses owned by the client.
     */
    Request<?, Addresses> getAccounts();

    /**
     * Returns the account information of a given address. There are three different account types in
     * Klaytn: Legacy Account, Externally Owned Account (EOA), and Smart Contract Account. See Klaytn
     * {@link IAccountType}.
     *
     * @param address               Address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return KlayAccount - Each account type has different attributes.
     */
    Request<?, KlayAccount> getAccount(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the account key of the Externally Owned Account (EOA) at a given address. If the account
     * of the given address is a Legacy Account or a Smart Contract Account, it will return an empty key
     * value. See {@link AccountKey}.
     *
     * @param address               Address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return KlayAccountKey - The account key consist of public key(s) and a key type.
     */
    Request<?, KlayAccountKey> getAccountKey(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the balance of the account of given address.
     *
     * @param address               address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Quantity - Integer of the current balance in peb.
     */
    Request<?, Quantity> getBalance(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns code at a given address.
     *
     * @param address               address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Bytes - The code from the given address.
     */
    Request<?, Bytes> getCode(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the number of transactions sent from an address.
     *
     * @param address               address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Quantity - Integer of the number of transactions send from this address.
     */
    Request<?, Quantity> getTransactionCount(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns true if an input account has a non-empty codeHash at the time of a specific block number.
     * It returns false if the account is an EOA or a smart contract account which doesn't have codeHash.
     *
     * @param address               address
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Boolean - true means the input parameter is an existing smart contract address.
     */
    Request<?, Boolean> isContractAccount(String address, DefaultBlockParameter defaultBlockParameter);

    /**
     * The sign method calculates a Klaytn-specific signature.
     * NOTE: The address to sign with must be unlocked.
     *
     * @param address address
     * @param message Message to sign
     * @return Bytes - Signature
     */
    Request<?, Bytes> sign(String address, String message);

    /**
     * Returns the number of most recent block.
     *
     * @return Quantity - Integer of the current block number the client is on.
     */
    Request<?, Quantity> getBlockNumber();

    /**
     * Returns information about a block by block number.
     *
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @param isFullTransaction     If true it returns the full transaction objects, if false only the hashes
     *                              of the transactions.
     * @return KlayBlock - A block object or null when no block was found
     */
    Request<?, KlayBlock> getBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean isFullTransaction);

    /**
     * Returns information about a block by hash.
     *
     * @param blockHash         Hash of a block.
     * @param isFullTransaction If true it returns the full transaction objects, if false only the hashes of
     *                          the transactions.
     * @return KlayBlock - A block object or null when no block was found
     */
    Request<?, KlayBlock> getBlockByHash(String blockHash, boolean isFullTransaction);

    /**
     * Returns receipts included in a block identified by block hash.
     *
     * @param blockHash Block hash
     * @return BlockReceipts - Receipts included in a block. If the target block contains no transaction, an
     * empty array [] is returned.
     */
    Request<?, BlockReceipts> getBlockReceipts(String blockHash);

    /**
     * Returns the number of transactions in a block matching the given block number.
     *
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Quantity - Integer of the number of transactions in this block.
     */
    Request<?, Quantity> getTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the number of transactions in a block from a block matching the given block hash.
     *
     * @param blockHash Hash of a block
     * @return Quantity - Integer of the number of transactions in this block.
     */
    Request<?, Quantity> getTransactionCountByHash(String blockHash);

    /**
     * Returns a block with consensus information matched by the given hash.
     *
     * @param blockHash Hash of a block
     * @return KlayBlockWithConsensusInfo - A block object with consensus information (a proposer and a list
     * of committee members), or null when no block was found:
     */
    Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByHash(String blockHash);

    /**
     * Returns a block with consensus information matched by the given block number.
     *
     * @param blockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return KlayBlockWithConsensusInfo - A block object with consensus information (a proposer and a list
     * of committee members), or null when no block was found:
     */
    Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(DefaultBlockParameter blockParameter);

    /**
     * Returns a list of all validators in the committee at the specified block. If the parameter is not set,
     * returns a list of all validators in the committee at the latest block.
     *
     * @param defaultBlockParameter (optional) Integer of a block number, or the string "earliest" or "latest".
     * @return Addresses - Array of addresses of all validators in the committee, or null when no committee
     * was found
     */
    Request<?, Addresses> getCommittee(DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the size of the committee at the specified block. If the parameter is not set, returns the
     * size of the committee at the latest block.
     *
     * @param defaultBlockParameter (optional) Integer of a block number, or the string "earliest" or "latest".
     * @return Quantity - The size of the committee, or -1 when no committee was found
     */
    Request<?, Quantity> getCommitteeSize(DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns a list of all validators of the council at the specified block. If the parameter is not set,
     * returns a list of all validators of the council at the latest block.
     *
     * @param defaultBlockParameter (optional) Integer of a block number, or the string "earliest" or "latest".
     * @return Addresses - Addresses of all validators of the council.
     */
    Request<?, Addresses> getCouncil(DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the size of the council at the specified block. If the parameter is not set, returns the
     * size of the council at the latest block.
     *
     * @param defaultBlockParameter (optional) Integer of a block number, or the string "earliest" or "latest".
     * @return Quantity - The size of the council, or -1 when no council was found
     */
    Request<?, Quantity> getCouncilSize(DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the value from a storage position at a given address.
     *
     * @param address               address
     * @param position              Integer of the position in the storage.
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Bytes - The value at this storage position.
     */
    Request<?, Bytes> getStorageAt(
            String address, DefaultBlockParameterNumber position, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns the hash of the current block, the seedHash, and the boundary condition to be met ("target").
     *
     * @return Work - Array with three 32-bytes DATA
     */
    Request<?, Work> getWork();

    /**
     * Returns true if client is actively mining new blocks.
     * NOTE: Currently, every node is on mining mode by default to resend transactions.
     * Please note that actual "mining" process is only done by CN nodes.
     *
     * @return Boolean - true if the client is mining, otherwise false.
     */
    Request<?, Boolean> isMining();

    /**
     * Returns an object with data about the sync status or false.
     *
     * @return KlaySyncing - an object with sync status data or false when not syncing
     */
    Request<?, KlaySyncing> isSyncing();

    /**
     * Executes a new message call immediately without creating a transaction on the block chain.
     * It returns data or an error object of JSON RPC if error occurs.
     *
     * @param callObject            The transaction call object. See the the {@link CallObject}'s properties.
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Bytes - The return value of executed contract.
     */
    Request<?, Bytes> call(CallObject callObject, DefaultBlockParameter defaultBlockParameter);

    /**
     * Generates and returns an estimate of how much gas is necessary to allow the transaction to complete.
     * The transaction will not be added to the blockchain. Note that the estimate may be significantly more
     * than the amount of gas actually used by the transaction, for a variety of reasons including Klaytn Virtual
     * Machine mechanics and node performance.
     *
     * @param callObject The transaction call object. See the the {@link CallObject}'s properties.
     * @return Quantity - The amount of gas used.
     */
    Request<?, Quantity> estimateGas(CallObject callObject);

    /**
     * Generates and returns an estimate of how much computation cost spent to execute the transaction.
     * Klaytn limits the computation cost of a transaction to 100000000 currently not to take too much time
     * by a single transaction. The transaction will not be added to the blockchain like klay_estimateGas.
     *
     * @param callObject            The transaction call object. See the the {@link CallObject}'s properties. If no gas limit
     *                              is specified, the Klaytn node uses the default gas limit (uint64 / 2) as an upper bound.
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Quantity - The amount of computation cost used.
     */
    Request<?, Quantity> estimateComputationCost(CallObject callObject, DefaultBlockParameter defaultBlockParameter);

    /**
     * Returns information about a transaction by block hash and transaction index position.
     *
     * @param blockHash Hash of a block
     * @param index     Integer of the transaction index position.
     * @return KlayTransaction -  A transaction object, or null when no transaction was found
     */
    Request<?, KlayTransaction> getTransactionByBlockHashAndIndex(String blockHash, DefaultBlockParameterNumber index);

    /**
     * Returns information about a transaction by block number and transaction index position.
     *
     * @param blockNumber Integer block number, or the string "latest", "earliest" or "pending"
     * @param index       The transaction index position.
     * @return KlayTransaction -  A transaction object, or null when no transaction was found
     */
    Request<?, KlayTransaction> getTransactionByBlockNumberAndIndex(
            DefaultBlockParameter blockNumber, DefaultBlockParameterNumber index);

    /**
     * Returns the information about a transaction requested by transaction hash.
     *
     * @param txHash Hash of a transaction.
     * @return KlayTransaction -  A transaction object, or null when no transaction was found
     */
    Request<?, KlayTransaction> getTransactionByHash(String txHash);

    /**
     * Returns the information about a transaction requested by sender transaction hash. Please note that
     * this API returns correct result only if indexing feature is enabled. This can be checked by call
     * {@link #isSenderTxHashIndexingEnabled()}.
     *
     * @param txHash Hash of a transaction before signing of feePayer(senderTransactionHash)
     * @return KlayTransaction -  A transaction object, or null when no transaction was found
     */
    Request<?, KlayTransaction> getTransactionBySenderTxHash(String txHash);

    /**
     * Returns the receipt of a transaction by transaction hash.
     * NOTE: The receipt is not available for pending transactions.
     *
     * @param transactionHash Hash of a transaction
     * @return KlayTransactionReceipt - A transaction receipt object, or null when no receipt was found
     */
    Request<?, KlayTransactionReceipt> getTransactionReceipt(String transactionHash);

    /**
     * Returns the receipt of a transaction by sender transaction hash.
     * NOTE: The receipt is not available for pending transactions. Please note that this API returns correct
     * result only if indexing feature is enabled. This can be checked by call {@link #isSenderTxHashIndexingEnabled()}.
     *
     * @param transactionHash Hash of a transaction before signing of feePayer(senderTransactionHash).
     * @return KlayTransactionReceipt - A transaction receipt object, or null when no receipt was found
     */
    Request<?, KlayTransactionReceipt> getTransactionReceiptBySenderTxHash(String transactionHash);

    /**
     * Creates a new message call transaction or a contract creation for signed transactions.
     *
     * @param signedTransactionData The signed transaction data.
     * @return Bytes32 - The transaction hash or the zero hash if the transaction is not yet available.
     */
    Request<?, Bytes32> sendSignedTransaction(String signedTransactionData);

    /**
     * Creates a new message call transaction or a contract creation if the data field contains code.
     *
     * @param transaction KlayTransaction object
     * @return Bytes32 - The transaction hash or the zero hash if the transaction is not yet available.
     */
    Request<?, Bytes32> sendTransaction(com.klaytn.caver.methods.request.KlayTransaction transaction);

    /**
     * Creates a rawTransaction based on the give transaction information.
     * NOTE: The address to sign with must be unlocked.
     *
     * @param transaction KlayTransaction object
     * @return KlaySignTransaction - Signed raw transaction and Transaction information including hash
     */
    Request<?, KlaySignTransaction> signTransaction(com.klaytn.caver.methods.request.KlayTransaction transaction);

    /**
     * Returns the chain ID of the chain.
     *
     * @return Integer of the chain ID of the chain.
     */
    Request<?, Quantity> getChainID();

    /**
     * Returns the current client version of a Klaytn node.
     *
     * @return Bytes The current client version of a Klaytn node.
     */
    Request<?, Bytes> getClientVersion();

    /**
     * Returns the current price per gas in peb.
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn
     * instead of suggesting a gas price as in Ethereum.
     *
     * @return Quantity - Integer of the current gas price in peb.
     */
    Request<?, Quantity> getGasPrice();

    /**
     * Returns the unit price of the given block in peb.<br>
     * NOTE: This API has different behavior from Ethereum's and returns a gas price of Klaytn instead
     * of suggesting a gas price as in Ethereum.
     *
     * @param defaultBlockParameter Integer block number, or the string "latest", "earliest" or "pending"
     * @return Quantity - Integer of the current gas price in peb.
     */
    Request<?, Quantity> getGasPriceAt(DefaultBlockParameter defaultBlockParameter);


    /**
     * Returns true if the node is writing blockchain data in parallel manner. It is enabled by default.
     *
     * @return Boolean - true means the node is writing blockchain data in parallel manner. It is false
     * if the node is writing the data in serial manner.
     */
    Request<?, Boolean> isParallelDBWrite();

    /**
     * Returns true if the node is indexing sender transaction hash to transaction hash mapping information.
     * It is disabled by default.
     *
     * @return true means the node is indexing sender transaction hash to transaction hash mapping information.
     */
    Request<?, Boolean> isSenderTxHashIndexingEnabled();

    /**
     * Returns the Klaytn protocol version of the node.
     *
     * @return Bytes - The Klaytn protocol version of the node.
     */
    Request<?, Bytes> getProtocolVersion();

    /**
     * Returns the rewardbase of the current node. Rewardbase is the address of the account
     * where the block rewards goes to. It is only required for CNs.
     *
     * @return Bytes20 - Address
     */
    Request<?, Bytes20> getRewardbase();

    /**
     * Returns true if the node is using write through caching. If enabled, block bodies and receipts are cached
     * when thery are written to persistent storage. It is false by default.
     *
     * @return Boolean - true means the node is using write through caching.
     */
    Request<?, Boolean> isWriteThroughCaching();

    /**
     * Polling method for a filter, which returns an array of logs which occurred since last poll.
     *
     * @param filterId A filter id
     * @return KlayLogs -  Array of log objects, or an empty array if nothing has changed since last poll.
     */
    Request<?, KlayLogs> getFilterChanges(BigInteger filterId);

    /**
     * Returns an array of all logs matching filter with given id, which has been obtained using
     * {@link #newFilter(KlayFilter)}. Note that filter ids returned by other filter creation functions,
     * such as {@link #newBlockFilter()} or {@link #newPendingTransactionFilter()}, cannot be used with this function.
     *
     * @param filterId A filter id
     * @return KlayLogs -  Array of log objects, or an empty array if nothing has changed since last poll.
     */
    Request<?, KlayLogs> getFilterLogs(BigInteger filterId);

    /**
     * Returns an array of all logs matching a given filter object.
     *
     * @param filter {@link KlayLogFilter}
     * @return KlayLogs -  Array of log objects, or an empty array if nothing has changed since last poll.
     */
    Request<?, KlayLogs> getLogs(KlayLogFilter filter);

    /**
     * Creates a filter in the node, to notify when a new block arrives.
     * To check if the state has changed, call {@link #getFilterChanges(BigInteger)}
     *
     * @return Quantity - A filter id
     */
    Request<?, Quantity> newBlockFilter();

    /**
     * Creates a filter object, based on filter options, to notify when the state changes (logs).
     * To check if the state has changed, call klay_{@link #getFilterChanges(BigInteger)}.
     * To obtain all logs matching the filter created by klay_newFilter, call {@link #getFilterLogs(BigInteger)}.
     *
     * @param filter {@link KlayLogFilter}
     * @return Quantity - A filter id
     */
    Request<?, Quantity> newFilter(KlayFilter filter);

    /**
     * Creates a filter in the node, to notify when new pending transactions arrive.
     * To check if the state has changed, call {@link #getFilterChanges(BigInteger)}
     *
     * @return Quantity - A filter id
     */
    Request<?, Quantity> newPendingTransactionFilter();

    /**
     * Uninstalls a filter with given id. Should always be called when watch is no longer needed.
     * Additionally, filters timeout when they are not requested with {@link #getFilterChanges(BigInteger)}
     * for a period of time.
     *
     * @param filterId A filter id
     * @return Boolean - true if the filter was successfully uninstalled, otherwise false.
     */
    Request<?, Boolean> uninstallFilter(BigInteger filterId);

    /**
     * Returns Keccak-256 (not the standardized SHA3-256) of the given data.
     *
     * @param data The data to convert into a SHA3 hash.
     * @return Bytes The SHA3 result of the given data.
     */
    Request<?, Bytes> getSha3(String data);

    //===========================================================

    /*Personal*/
    Request<?, NewAccountIdentifier> newAccount(String passphrase);

    Request<?, PersonalUnlockAccount> unlockAccount(String address,
                                                    String passphrase, BigInteger duration);

    Request<?, Boolean> lockAccount(String address);

    Request<?, Bytes20> importRawKey();
}
