package com.klaytn.caver.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.account.IAccountKey;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.request.KlayLogFilter;
import com.klaytn.caver.methods.response.*;
import com.klaytn.caver.methods.response.Boolean;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.tx.account.AccountKey;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class Klay {
    protected final Web3jService web3jService;

    public Klay(Web3jService web3jService) {
        this.web3jService = web3jService;
    }


    public Request<?, Boolean> accountCreated(String address) {
        return accountCreated(address, DefaultBlockParameterName.LATEST);
    }


    public Request<?, Boolean> accountCreated(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_accountCreated",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                Boolean.class
        );
    }

    /**
     * Returns true if the account associated with the address is created. It returns false otherwise.
     *
     * @param address The address
     * @param defaultBlockParameter The string "latest", "earliest" or "pending"
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

    public Request<?, Addresses> getAccounts() {
        return new Request<>(
                "klay_accounts",
                Collections.<String>emptyList(),
                web3jService,
                Addresses.class);
    }

    public Request<?, Bytes> encodeAccountKey(IAccountKey accountKey) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        String json = objectMapper.writeValueAsString(accountKey);
        return new Request<>(
                "klay_encodeAccountKey",
                Arrays.asList(accountKey),
                web3jService,
                Bytes.class);
    }

    public Request<?, AccountKeyResponse> decodeAccountKey(String encodedAccountKey) {
        return new Request<>(
                "klay_decodeAccountKey",
                Arrays.asList(encodedAccountKey),
                web3jService,
                AccountKeyResponse.class
        );
    }

    public Request<?, KlayAccount> getAccount(String address) {
        return getAccount(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, KlayAccount> getAccount(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_getAccount",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                KlayAccount.class);
    }

    public Request<?, KlayAccount> getAccount(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getAccount",
                Arrays.asList(
                        address,
                        blockTag
                ),
                web3jService,
                KlayAccount.class);
    }

    public Request<?, KlayAccountKey> getAccountKey(String address) {
        return getAccountKey(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, KlayAccountKey> getAccountKey(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_getAccountKey",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                KlayAccountKey.class);
    }

    public Request<?, KlayAccountKey> getAccountKey(String address, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getAccountKey",
                Arrays.asList(
                        address,
                        blockTag.getValue()
                ),
                web3jService,
                KlayAccountKey.class);
    }

    public Request<?, Quantity> getBalance(String address) {
        return getBalance(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, Quantity> getBalance(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_getBalance",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                Quantity.class);
    }

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

    public Request<?, Bytes> getCode(String address) {
        return getCode(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, Bytes> getCode(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_getCode",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                Bytes.class);
    }

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

    public Request<?, Quantity> getTransactionCount(String address) {
        return getTransactionCount(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, Quantity> getTransactionCount(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_getTransactionCount",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                Quantity.class);
    }

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

    public Request<?, Boolean> isContractAccount(String address) {
        return isContractAccount(address, DefaultBlockParameterName.LATEST);
    }

    public Request<?, Boolean> isContractAccount(String address, Quantity blockNumber) {
        return new Request<>(
                "klay_isContractAccount",
                Arrays.asList(
                        address,
                        blockNumber.getValue()
                ),
                web3jService,
                Boolean.class);
    }

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

    
    public Request<?, Quantity> getBlockNumber() {
        return new Request<>(
                "klay_blockNumber",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    public Request<?, KlayBlock> getBlockByNumber(DefaultBlockParameter defaultBlockParameter) {
        return getBlockByNumber(defaultBlockParameter, true);
    }

    
    public Request<?, KlayBlock> getBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByNumber",
                Arrays.asList(defaultBlockParameter, isFullTransaction),
                web3jService,
                KlayBlock.class);
    }

    public Request<?, KlayBlock> getBlockByNumber(Quantity blockNumber) {
        return getBlockByNumber(blockNumber, true);
    }


    public Request<?, KlayBlock> getBlockByNumber(Quantity blockNumber, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByNumber",
                Arrays.asList(blockNumber, isFullTransaction),
                web3jService,
                KlayBlock.class);
    }

    public Request<?, KlayBlock> getBlockByHash(String blockHash) {
        return getBlockByHash(blockHash, true);
    }
    
    public Request<?, KlayBlock> getBlockByHash(String blockHash, boolean isFullTransaction) {
        return new Request<>(
                "klay_getBlockByHash",
                Arrays.asList(blockHash, isFullTransaction),
                web3jService,
                KlayBlock.class);
    }

    
    public Request<?, BlockReceipts> getBlockReceipts(String blockHash) {
        return new Request<>(
                "klay_getBlockReceipts",
                Arrays.asList(blockHash),
                web3jService,
                BlockReceipts.class);
    }

    
    public Request<?, Quantity> getTransactionCountByNumber(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getBlockTransactionCountByNumber",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> getTransactionCountByNumber(Quantity blockNumber) {
        return new Request<>(
                "klay_getBlockTransactionCountByNumber",
                Arrays.asList(blockNumber),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, Quantity> getTransactionCountByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByHash(String blockHash) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByHash",
                Arrays.asList(blockHash),
                web3jService,
                KlayBlockWithConsensusInfo.class);
    }

    
    public Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByNumber",
                Arrays.asList(blockTag),
                web3jService,
                KlayBlockWithConsensusInfo.class);
    }

    public Request<?, KlayBlockWithConsensusInfo> getBlockWithConsensusInfoByNumber(Quantity blockNumber) {
        return new Request<>(
                "klay_getBlockWithConsensusInfoByNumber",
                Arrays.asList(blockNumber),
                web3jService,
                KlayBlockWithConsensusInfo.class);
    }


    public Request<?, Addresses> getCommittee() {
        return getCommittee(DefaultBlockParameterName.LATEST);
    }

    public Request<?, Addresses> getCommittee(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCommittee",
                Arrays.asList(blockTag),
                web3jService,
                Addresses.class
        );
    }

    public Request<?, Addresses> getCommittee(Quantity blockNumber) {
        return new Request<>(
                "klay_getCommittee",
                Arrays.asList(blockNumber),
                web3jService,
                Addresses.class
        );
    }

    public Request<?, Quantity> getCommitteeSize() {
        return getCommitteeSize(DefaultBlockParameterName.LATEST);
    }
    
    public Request<?, Quantity> getCommitteeSize(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCommitteeSize",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class
        );
    }

    public Request<?, Quantity> getCommitteeSize(Quantity blockNumber) {
        return new Request<>(
                "klay_getCommitteeSize",
                Arrays.asList(blockNumber),
                web3jService,
                Quantity.class
        );
    }

    public Request<?, Addresses> getCouncil(Quantity blockNumber) {
        return new Request<>(
                "klay_getCouncil",
                Arrays.asList(blockNumber),
                web3jService,
                Addresses.class
        );
    }

    
    public Request<?, Addresses> getCouncil(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCouncil",
                Arrays.asList(blockTag),
                web3jService,
                Addresses.class
        );
    }

    public Request<?, Quantity> getCouncilSize() {
        return getCouncilSize(DefaultBlockParameterName.LATEST);
    }


    public Request<?, Quantity> getCouncilSize(DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_getCouncilSize",
                Arrays.asList(blockTag),
                web3jService,
                Quantity.class
        );
    }

    public Request<?, Quantity> getCouncilSize(Quantity blockNumber) {
        return new Request<>(
                "klay_getCouncilSize",
                Arrays.asList(blockNumber),
                web3jService,
                Quantity.class
        );
    }

    public Request<?, Bytes> getStorageAt(
            String address, DefaultBlockParameterNumber position, Quantity blockNumber) {
        return new Request<>(
                "klay_getStorageAt",
                Arrays.asList(
                        address,
                        position,
                        blockNumber.getValue()
                ),
                web3jService,
                Bytes.class);
    }


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

    
    public Request<?, KlaySyncing> isSyncing() {
        return new Request<>(
                "klay_syncing",
                Collections.<String>emptyList(),
                web3jService,
                KlaySyncing.class);
    }

    public Request<?, Quantity> getChainID() {
        return new Request<>(
                "klay_chainID",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    public Request<?, Bytes> call(CallObject callObject) {
        return call(callObject, DefaultBlockParameterName.LATEST);
    }


    public Request<?, Bytes> call(CallObject callObject, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_call",
                Arrays.asList(callObject, blockTag),
                web3jService,
                Bytes.class);
    }

    public Request<?, Bytes> call(CallObject callObject, Quantity blockNumber) {
        return new Request<>(
                "klay_call",
                Arrays.asList(callObject, blockNumber),
                web3jService,
                Bytes.class);
    }

    
    public Request<?, Quantity> estimateGas(CallObject callObject) {
        return new Request<>(
                "klay_estimateGas",
                Arrays.asList(callObject),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> estimateGas(CallObject callObject, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_estimateGas",
                Arrays.asList(callObject, blockTag),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> estimateGas(CallObject callObject, Quantity blockNumber) {
        return new Request<>(
                "klay_estimateGas",
                Arrays.asList(callObject, blockNumber),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> estimateComputationCost(CallObject callObject) {
        return estimateComputationCost(callObject, DefaultBlockParameterName.LATEST);
    }


    public Request<?, Quantity> estimateComputationCost(CallObject callObject, DefaultBlockParameter blockTag) {
        return new Request<>(
                "klay_estimateComputationCost",
                Arrays.asList(callObject, blockTag),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> estimateComputationCost(CallObject callObject, Quantity blockNumber) {
        return new Request<>(
                "klay_estimateComputationCost",
                Arrays.asList(callObject, blockNumber),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, KlayTransaction> getTransactionByBlockHashAndIndex(String blockHash, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockHashAndIndex",
                Arrays.asList(blockHash, index),
                web3jService,
                KlayTransaction.class);
    }

    public Request<?, KlayTransaction> getTransactionByBlockNumberAndIndex(Quantity blockNumber, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockNumberAndIndex",
                Arrays.asList(blockNumber, index),
                web3jService,
                KlayTransaction.class);
    }


    public Request<?, KlayTransaction> getTransactionByBlockNumberAndIndex(DefaultBlockParameter blockTag, DefaultBlockParameterNumber index) {
        return new Request<>(
                "klay_getTransactionByBlockNumberAndIndex",
                Arrays.asList(blockTag, index),
                web3jService,
                KlayTransaction.class);
    }

    
    public Request<?, KlayTransaction> getTransactionByHash(String txHash) {
        return new Request<>(
                "klay_getTransactionByHash",
                Arrays.asList(txHash),
                web3jService,
                KlayTransaction.class);
    }

    
    public Request<?, KlayTransaction> getTransactionBySenderTxHash(String txHash) {
        return new Request<>(
                "klay_getTransactionBySenderTxHash",
                Arrays.asList(txHash),
                web3jService,
                KlayTransaction.class);
    }

    
    public Request<?, KlayTransactionReceipt> getTransactionReceipt(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                KlayTransactionReceipt.class);
    }

    
    public Request<?, KlayTransactionReceipt> getTransactionReceiptBySenderTxHash(String transactionHash) {
        return new Request<>(
                "klay_getTransactionReceiptBySenderTxHash",
                Arrays.asList(transactionHash),
                web3jService,
                KlayTransactionReceipt.class);
    }

    
    public Request<?, Bytes32> sendRawTransaction(String signedTransactionData) {
        return new Request<>(
                "klay_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                Bytes32.class);
    }

    
    public Request<?, Bytes32> sendTransaction(AbstractTransaction transaction) {
        return new Request<>(
                "klay_sendTransaction",
                Arrays.asList(transaction),
                web3jService,
                Bytes32.class);
    }

    public Request<?, Bytes32> sendTransactionAsFeePayer(AbstractFeeDelegatedTransaction transaction) {
        return new Request<>(
                "klay_sendTransactionAsFeePayer",
                Arrays.asList(transaction),
                web3jService,
                Bytes32.class);
    }

    
    public Request<?, KlaySignTransaction> signTransaction(AbstractTransaction transaction) {

        return new Request<>(
                "klay_signTransaction",
                Arrays.asList(transaction),
                web3jService,
                KlaySignTransaction.class);
    }

    public Request<?, KlaySignTransaction> signTransactionAsFeePayer(AbstractFeeDelegatedTransaction transaction) {
        return new Request<>(
                "klay_signTransactionAsFeePayer",
                Arrays.asList(transaction),
                web3jService,
                KlaySignTransaction.class);
    }

    public Request<?, ?> getDecodedAnchoringTransaction(String hash) {
        return null;
    }

    


    
    public Request<?, Bytes> getClientVersion() {
        return new Request<>(
                "klay_clientVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    
    public Request<?, Quantity> getGasPrice() {
        return new Request<>(
                "klay_gasPrice",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    public Request<?, Quantity> getGasPriceAt() {
        return getGasPriceAt(DefaultBlockParameterName.LATEST);
    }
    
    public Request<?, Quantity> getGasPriceAt(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "klay_gasPriceAt",
                Arrays.asList(defaultBlockParameter),
                web3jService,
                Quantity.class
        );
    }

    public Request<?, Quantity> getGasPriceAt(Quantity blockNumber) {
        return new Request<>(
                "klay_gasPriceAt",
                Arrays.asList(blockNumber),
                web3jService,
                Quantity.class
        );
    }

    
    public Request<?, Boolean> isParallelDBWrite() {
        return new Request<>(
                "klay_isParallelDBWrite",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    
    public Request<?, Boolean> isSenderTxHashIndexingEnabled() {
        return new Request<>(
                "klay_isSenderTxHashIndexingEnabled",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    
    public Request<?, Bytes> getProtocolVersion() {
        return new Request<>(
                "klay_protocolVersion",
                Collections.<String>emptyList(),
                web3jService,
                Bytes.class);
    }

    
    public Request<?, Bytes20> getRewardbase() {
        return new Request<>(
                "klay_rewardbase",
                Collections.<String>emptyList(),
                web3jService,
                Bytes20.class);
    }

    
    public Request<?, Boolean> writeThroughCaching() {
        return new Request<>(
                "klay_writeThroughCaching",
                Collections.<String>emptyList(),
                web3jService,
                Boolean.class);
    }

    
    public Request<?, KlayLogs> getFilterChanges(BigInteger filterId) {
        return new Request<>(
                "klay_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                KlayLogs.class);
    }

    
    public Request<?, KlayLogs> getFilterLogs(BigInteger filterId) {
        return new Request<>(
                "klay_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                KlayLogs.class);
    }

    
    public Request<?, KlayLogs> getLogs(KlayLogFilter filter) {
        return new Request<>(
                "klay_getLogs",
                Arrays.asList(filter),
                web3jService,
                KlayLogs.class);
    }

    
    public Request<?, Quantity> newBlockFilter() {
        return new Request<>(
                "klay_newBlockFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, Quantity> newFilter(KlayFilter filter) {
        return new Request<>(
                "klay_newFilter",
                Arrays.asList(filter),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, Quantity> newPendingTransactionFilter() {
        return new Request<>(
                "klay_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jService,
                Quantity.class);
    }

    
    public Request<?, Boolean> uninstallFilter(BigInteger filterId) {
        return new Request<>(
                "klay_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefix(filterId)),
                web3jService,
                Boolean.class);
    }

    
    public Request<?, Bytes> sha3(String data) {
        return new Request<>(
                "klay_sha3",
                Arrays.asList(data),
                web3jService,
                Bytes.class);
    }
}
