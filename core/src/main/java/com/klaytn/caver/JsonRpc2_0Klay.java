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
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.utils.Async;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @deprecated This class replaced by {@link com.klaytn.caver.rpc.Klay}
 * @see com.klaytn.caver.rpc.RPC
 */
@Deprecated
public class JsonRpc2_0Klay implements Klay {

    public static final int DEFAULT_BLOCK_TIME = 1 * 1000;

    protected final Web3jService web3jService;
    //private final JsonRpc2_0Rx web3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;
    private Admin web3j;

    public JsonRpc2_0Klay(Web3jService web3jService, Admin web3j) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService(), web3j);
    }

    public JsonRpc2_0Klay(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService,
            Admin web3j) {
        this.web3jService = web3jService;
        //this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
        this.web3j = web3j;
    }

    @Override
    public Request<?, Boolean> isAccountCreated(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_accountCreated",
                Arrays.asList(
                        address,
                        defaultBlockParameter
                ),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Addresses> getAccounts() {
        return new Request<>(
                "klay_accounts",
                Collections.<String>emptyList(),
                web3jService,
                Addresses.class);
    }

    @Override
    public Request<?, KlayAccount> getAccount(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getAccount",
                Arrays.asList(
                        address,
                        defaultBlockParameter.getValue()
                ),
                web3jService,
                KlayAccount.class);
    }

    @Override
    public Request<?, KlayAccountKey> getAccountKey(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getAccountKey",
                Arrays.asList(
                        address,
                        defaultBlockParameter.getValue()
                ),
                web3jService,
                KlayAccountKey.class);
    }

    @Override
    public Request<?, Quantity> getBalance(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getBalance",
                Arrays.asList(
                        address,
                        defaultBlockParameter
                ),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Bytes> getCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getCode",
                Arrays.asList(
                        address,
                        defaultBlockParameter.getValue()
                ),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Quantity> getTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getTransactionCount",
                Arrays.asList(
                        address,
                        defaultBlockParameter.getValue()
                ),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Boolean> isContractAccount(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_isContractAccount",
                Arrays.asList(
                        address,
                        defaultBlockParameter
                ),
                web3jService,
                Boolean.class);
    }

    @Override
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

    @Override
    public Request<?, Quantity> getBlockNumber() {
        return new Request<>(
                "klay_blockNumber",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, KlayBlock> getBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByNumber",
                Arrays.asList(defaultBlockParameter, isFullTransaction),
                web3jService,
                KlayBlock.class);
    }

    @Override
    public Request<?, KlayBlock> getBlockByHash(String blockHash, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByHash",
                Arrays.asList(blockHash, isFullTransaction),
                web3jService,
                KlayBlock.class);
    }

    @Override
    public Request<?, BlockReceipts> getBlockReceipts(String blockHash) {
        return new Request<>(
                "klay_getBlockReceipts",
                Arrays.asList(blockHash),
                web3jService,
                BlockReceipts.class);
    }

    @Override
    public Request<?, Quantity> getTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Quantity> getTransactionCountByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByHash",
                Arrays.asList(blockHash),
                web3jService,
                KlayBlockWithConsensusInfo.class);
    }

    @Override
    public Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByNumber",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                KlayBlockWithConsensusInfo.class);
    }

    @Override
    public Request<?, Addresses> getCommittee(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getCommittee",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Addresses.class
        );
    }

    @Override
    public Request<?, Quantity> getCommitteeSize(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getCommitteeSize",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Quantity.class
        );
    }

    @Override
    public Request<?, Addresses> getCouncil(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getCouncil",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Addresses.class
        );
    }

    @Override
    public Request<?, Quantity> getCouncilSize(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getCouncilSize",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Quantity.class
        );
    }

    @Override
    public Request<?, Bytes> getStorageAt(
            String address, DefaultBlockParameterNumber position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_getStorageAt",
                Arrays.asList(
                        address,
                        position,
                        defaultBlockParameter.getValue()
                ),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Work> getWork() {
        return new Request<>(
                "klay_getWork",
                Collections.<String>emptyList(),
                web3jService,
                Work.class);
    }

    @Override
    public Request<?, Boolean> isMining() {
        return new Request<>(
                "klay_mining",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, KlaySyncing> isSyncing() {
        return new Request<>(
                "klay_syncing",
                Collections.<String>emptyList(),
                web3jService,
                KlaySyncing.class);
    }

    @Override
    public Request<?, Bytes> call(CallObject callObject, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_call",
                Arrays.asList(callObject, defaultBlockParameter),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Quantity> estimateGas(CallObject callObject) {
        return new Request<>(
                "klay_estimateGas",
                Arrays.asList(callObject),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Quantity> estimateComputationCost(CallObject callObject, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_estimateComputationCost",
                Arrays.asList(callObject, defaultBlockParameter),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, KlayTransaction> getTransactionByBlockHashAndIndex(String blockHash, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockHashAndIndex",
                Arrays.asList(blockHash, index),
                web3jService,
                KlayTransaction.class);
    }

    @Override
    public Request<?, KlayTransaction> getTransactionByBlockNumberAndIndex(DefaultBlockParameter blockNumber, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockNumberAndIndex",
                Arrays.asList(blockNumber, index),
                web3jService,
                KlayTransaction.class);
    }

    @Override
    public Request<?, KlayTransaction> getTransactionByHash(String txHash) {
        return new Request<>(
                "klay_getTransactionByHash",
                Arrays.asList(txHash),
                web3jService,
                KlayTransaction.class);
    }

    @Override
    public Request<?, KlayTransaction> getTransactionBySenderTxHash(String txHash) {
        return new Request<>(
                "klay_getTransactionBySenderTxHash",
                Arrays.asList(txHash),
                web3jService,
                KlayTransaction.class);
    }

    @Override
    public Request<?, KlayTransactionReceipt> getTransactionReceipt(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                KlayTransactionReceipt.class);
    }

    @Override
    public Request<?, KlayTransactionReceipt> getTransactionReceiptBySenderTxHash(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceiptBySenderTxHash",
                Arrays.asList(transactionHash),
                web3jService,
                KlayTransactionReceipt.class);
    }

    @Override
    public Request<?, Bytes32> sendSignedTransaction(String signedTransactionData) {
        return new Request<>(
                "klay_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                Bytes32.class);
    }

    @Override
    public Request<?, Bytes32> sendTransaction(com.klaytn.caver.methods.request.KlayTransaction transaction) {
        return new Request<>(
                "klay_sendTransaction",
                Arrays.asList(transaction),
                web3jService,
                Bytes32.class);
    }

    @Override
    public Request<?, KlaySignTransaction> signTransaction(com.klaytn.caver.methods.request.KlayTransaction transaction) {
        return new Request<>(
                "klay_signTransaction",
                Arrays.asList(transaction),
                web3jService,
                KlaySignTransaction.class);
    }

    @Override
    public Request<?, Quantity> getChainID() {
        return new Request<>(
                "klay_chainID",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Bytes> getClientVersion() {
        return new Request<>(
                "klay_clientVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Quantity> getGasPrice() {
        return new Request<>(
                "klay_gasPrice",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Quantity> getGasPriceAt(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_gasPriceAt",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Quantity.class
        );
    }

    @Override
    public Request<?, Boolean> isParallelDBWrite() {
        return new Request<>(
                "klay_isParallelDBWrite",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Boolean> isSenderTxHashIndexingEnabled() {
        return new Request<>(
                "klay_isSenderTxHashIndexingEnabled",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Bytes> getProtocolVersion() {
        return new Request<>(
                "klay_protocolVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    @Override
    public Request<?, Bytes20> getRewardbase() {
        return new Request<>(
                "klay_rewardbase",
                Collections.<String>emptyList(),
                web3jService,
                Bytes20.class);
    }

    @Override
    public Request<?, Boolean> isWriteThroughCaching() {
        return new Request<>(
                "klay_writeThroughCaching",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, KlayLogs> getFilterChanges(BigInteger filterId) {
        return new Request<>(
                "klay_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                KlayLogs.class);
    }

    @Override
    public Request<?, KlayLogs> getFilterLogs(BigInteger filterId) {
        return new Request<>(
                "klay_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                KlayLogs.class);
    }

    @Override
    public Request<?, KlayLogs> getLogs(KlayLogFilter filter) {
        return new Request<>(
                "klay_getLogs",
                Arrays.asList(filter),
                web3jService,
                KlayLogs.class);
    }

    @Override
    public Request<?, Quantity> newBlockFilter() {
        return new Request<>(
                "klay_newBlockFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Quantity> newFilter(KlayFilter filter) {
        return new Request<>(
                "klay_newFilter",
                Arrays.asList(filter),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Quantity> newPendingTransactionFilter() {
        return new Request<>(
                "klay_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    @Override
    public Request<?, Boolean> uninstallFilter(BigInteger filterId) {
        return new Request<>(
                "klay_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Bytes> getSha3(String data) {
        return new Request<>(
                "klay_sha3",
                Arrays.asList(data),
                web3jService,
                Bytes.class);
    }

    //===========================================================

    @Override
    public Request<?, NewAccountIdentifier> newAccount(String passphrase) {
        return web3j.personalNewAccount(passphrase);
    }

    public Request<?, PersonalUnlockAccount> unlockAccount(String address,
                                                           String passphrase, BigInteger duration) {
        return web3j.personalUnlockAccount(
                address,
                passphrase,
                duration
        );
    }

    @Override
    public Request<?, Boolean> lockAccount(String address) {
        return new Request<>(
                "personal_lockAccount",
                Arrays.asList(
                        address
                ),
                web3jService,
                Boolean.class);
    }

    @Override
    public Request<?, Bytes20> importRawKey() {
        return new Request<>(
                "personal_importRawKey",
                Collections.<String>emptyList(),
                web3jService,
                Bytes20.class);
    }

}
