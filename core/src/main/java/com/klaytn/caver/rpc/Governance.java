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

package com.klaytn.caver.rpc;

import com.klaytn.caver.methods.response.*;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class Governance {
    /**
     * JSON-RPC service instance.
     */
    protected final Web3jService provider;

    /**
     * Creates a Governance instance
     * @param provider JSON-RPC service instance.
     */
    public Governance(Web3jService provider) {
        this.provider = provider;
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * String value = "single";
     * Bytes response = caver.rpc.governance.vote(IVote.VoteItem.GOVERNANCE_GOVERNANCE_MODE.getKey(), value);
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Request&lt;?, Bytes&gt;
     */
    public Request<?, Bytes> vote(IVote.VoteItem key, String value) {
        return vote(key.toString(), value);
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * String key = "governance.governancemode";
     * String value = "single";
     *
     * Bytes response = caver.rpc.governance.vote(key, value);
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Request&lt;?, Bytes&gt;
     */
    public Request<?, Bytes> vote(String key, String value) {
        if(!IVote.VoteItem.isExist(key)) {
            throw new IllegalArgumentException("The key " + key + "is invalid.");
        }

        return new Request<>(
                "governance_vote",
                Arrays.asList(key, value),
                provider,
                Bytes.class
        );
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * BigInteger unitPriceValue = new BigInteger("25000000000");
     *
     * Bytes response = caver.rpc.governance.vote(IVote.VoteItem.GOVERNANCE_UNIT_PRICE.getKey(), unitPriceValue).send();
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Bytes
     */
    public Request<?, Bytes> vote(IVote.VoteItem key, BigInteger value) {
        return vote(key.toString(), value);
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * String key = "governance.unitprice";
     * BigInteger unitPriceValue = new BigInteger("25000000000");
     *
     * Bytes response = caver.rpc.governance.vote(key, value);
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Request&lt;?, Bytes&gt;
     */
    public Request<?, Bytes> vote(String key, BigInteger value) {
        if(!IVote.VoteItem.isExist(key)) {
            throw new IllegalArgumentException("The " + key + "is invalid.");
        }

        return new Request<>(
                "governance_vote",
                Arrays.asList(key, value),
                provider,
                Bytes.class
        );
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * boolean value = true;
     *
     * Bytes response = caver.rpc.governance.vote(IVote.VoteItem.REWARD_USE_GINICOEFF.getKey(), value).send();
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Request&lt;?, Bytes&gt;
     */
    public Request<?, Bytes> vote(IVote.VoteItem key, boolean value) {
        return new Request<>(
                "governance_vote",
                Arrays.asList(key, value),
                provider,
                Bytes.class
        );
    }

    /**
     * Submits a new vote.<p>
     * If the node has the right to vote based on governance mode, the vote can be placed. If not, and error message will be returned and the vote will be ignored.
     * <pre>Example :
     * {@code
     * String key = "reward.useginicoeff";
     * boolean value = true;
     *
     * Bytes response = caver.rpc.governance.vote(key, value);
     * }
     * </pre>
     * @param key The name of the configuration setting to be changed. See the {@link IVote.VoteItem}.
     * @param value The value for each key.
     * @return Request&lt;?, Bytes&gt;
     */
    public Request<?, Bytes> vote(String key, boolean value) {
        if(!IVote.VoteItem.isExist(key)) {
            throw new IllegalArgumentException("The key " + key + "is invalid.");
        }

        return new Request<>(
                "governance_vote",
                Arrays.asList(key, value),
                provider,
                Bytes.class
        );
    }

    /**
     * Provides the current tally of governance votes.<p>
     * It shows the aggregated approval rate in percentage. When it goes over 50%, the vote passed.
     * <pre>Example :
     * {@code
     * GovernanceTally response = caver.rpc.governance.showTally().send();
     * GovernanceTally.TallyData item = item.getResult().get(0);
     *
     * float percent = item.getApprovalPercentage();
     * BigInteger unitPrice = IVote.VoteItem.getUnitPrice(item);
     * }
     * </pre>
     * @return Request&lt;?, GovernanceTally&gt;
     */
    public Request<?, GovernanceTally> showTally() {
        return new Request<>(
                "governance_showTally",
                Collections.emptyList(),
                provider,
                GovernanceTally.class
        );
    }

    /**
     * Provides the sum of all voting power that CNs have. Each CN has 1.0 ~ 2.0 voting power.<p>
     * In "none", "single" governance mode, totalVotingPower don't provide any information.
     * <pre>Example :
     * {@code
     * GovernanceVotingPower response = caver.rpc.governance.getTotalVotingPower().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceTally&gt;
     */
    public Request<?, GovernanceVotingPower> getTotalVotingPower() {
        return new Request<>(
                "governance_totalVotingPower",
                Collections.emptyList(),
                provider,
                GovernanceVotingPower.class
        );
    }

    /**
     * Provides the voting power of the node. The voting power can be 1.0 ~ 2.0.<p>
     * In "none", "single" governance mode, totalVotingPower don't provide any information.
     * <pre>Example :
     * {@code
     * GovernanceVotingPower response = caver.rpc.governance.getMyVotingPower().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceVotingPower&gt;
     */
    public Request<?, GovernanceVotingPower> getMyVotingPower() {
        return new Request<>(
                "governance_myVotingPower",
                Collections.emptyList(),
                provider,
                GovernanceVotingPower.class
        );
    }

    /**
     * Provides my vote information in the epoch.<p>
     * Each vote is stored in a block when the user's node generates a new block. After current epoch ends, this information is cleared.
     * <pre>Example :
     * {@code
     * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
     * List voteList = response.getResult();
     * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
     *
     * String mode = IVote.VoteItem.getGovernanceMode(voteList); // passed list.
     * mode = IVote.VoteItem.getGovernanceMode(myVote)); // passed list item.
     * }
     * </pre>
     * @return Request&lt;?, GovernanceMyVotes&gt;
     */
    public Request<?, GovernanceMyVotes> getMyVotes() {
        return new Request<>(
                "governance_myVotes",
                Collections.emptyList(),
                provider,
                GovernanceMyVotes.class
        );
    }

    /**
     * Provides the latest chain configuration
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.governance.getChainConfig().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig() {
        return getChainConfig(DefaultBlockParameterName.LATEST);
    }

    /**
     * Provides the chain configuration at the specified block number
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.governance.getChainConfig(BigInteger.ZERO).send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig(BigInteger blockNumber) {
        return getChainConfig(DefaultBlockParameter.valueOf(blockNumber));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.governance.getChainConfig("latest").send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceChainConfig&gt;
     */
    public Request<?, GovernanceChainConfig> getChainConfig(String blockTag) {
        return getChainConfig(DefaultBlockParameterName.fromString(blockTag));
    }

    /**
     * Provides the chain configuration by block tag (latest, earliest, pending)
     * <pre>Example :
     * {@code
     * GovernanceChainConfig response = caver.rpc.governance.getChainConfig(DefaultBlockParameterName.LATEST).send();
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
                "governance_getChainConfig",
                Arrays.asList(blockNumberOrTag),
                provider,
                GovernanceChainConfig.class
        );
    }

    /**
     * Provides the address of the node that a user is using.<p>
     * It is derived from the nodekey and used to sign consensus messages. And the value of "governingnode" has to be one of validator's node address.
     * <pre>Example :
     * {@code
     * Bytes20 response = caver.rpc.governance.getNodeAddress().send();
     * }
     * </pre>
     * @return Request&lt;?, Bytes20&gt;
     */
    public Request<?, Bytes20> getNodeAddress() {
        return new Request<>(
                "governance_nodeAddress",
                Collections.emptyList(),
                provider,
                Bytes20.class
        );
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.<p>
     * It pass the latest block tag as a parameter.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.governance.getParams().send();
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
     * GovernanceItems response = caver.rpc.governance.getParams(BigInteger.ZERO).send();
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
     * GovernanceItems response = caver.rpc.governance.getParams("latest").send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getParams(String blockTag) {
        DefaultBlockParameterName blockTagName = DefaultBlockParameterName.fromString(blockTag);
        return getParams(blockTagName);
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.governance.getParams(DefaultBlockParameterName.LATEST).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getParams(DefaultBlockParameterName blockTag) {
        return getParams((DefaultBlockParameter)blockTag);
    }

    Request<?, GovernanceItems> getParams(DefaultBlockParameter blockParameter) {
        return new Request<>(
                "governance_getParams",
                Arrays.asList(blockParameter.getValue()),
                provider,
                GovernanceItems.class
        );
    }

    /**
     * Returns governance items at specific block.<p>
     * It is the result of previous voting of the block and used as configuration for chain at the given block number.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.governance.getItemsAt(DefaultBlockParameterName.LATEST).send();
     * Map<String, Object> governanceItem = response.getResult();
     *
     * String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     * }
     * </pre>
     * @param blockTag The block tag to query
     * @return Request&lt;?, GovernanceItems&gt;
     */
    // public Request<?, GovernanceItems> getItemsAt(DefaultBlockParameterName blockTag) {
    //     return getItemsAt((DefaultBlockParameter)blockTag);
    // }

    // Request<?, GovernanceItems> getItemsAt(DefaultBlockParameter blockParameter) {
    //     return new Request<>(
    //             "governance_itemsAt",
    //             Arrays.asList(blockParameter.getValue()),
    //             provider,
    //             GovernanceItems.class
    //     );
    // }

    /**
     *  Returns the list of items that have received enough number of votes but not yet finalized.<p>
     *  At the end of the current epoch, these changes will be finalized and the result will be in effect from the epoch after next epoch.
     *  <pre>Example :
     *  {@code
     *  GovernanceItems response = caver.rpc.governance.getPendingChanges().send();
     *  Map<String, Object> governanceItem = response.getResult();
     *
     *  String mode = IVote.VoteItem.getGovernanceMode(governanceItem);
     *  mode = governanceItem.get("governance.governancemode");
     *  }</pre>
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getPendingChanges() {
        return new Request<>(
                "governance_pendingChanges",
                Collections.emptyList(),
                provider,
                GovernanceItems.class
        );
    }

    /**
     * Returns the votes from all nodes in the epoch. These votes are gathered from the header of each block.
     * <pre>Example :
     * {@code
     * GovernanceNodeVotes response = caver.rpc.governance.getVotes().send();
     * List list = response.getResult();
     * GovernanceNodeVotes.NodeVote item = (GovernanceNodeVotes.NodeVote)list.get(0);
     *
     * BigInteger unitPrice = IVote.VoteItem.getUnitPrice(list);
     * boolean useGini = IVote.VoteItem.toBooleanValue(item.getKey(), item.getValue());
     * }
     * </pre>
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceNodeVotes> getVotes() {
        return new Request<>(
                "governance_votes",
                Collections.emptyList(),
                provider,
                GovernanceNodeVotes.class
        );
    }

    /**
     * Returns an array of current idxCache in the memory cache.<p>
     * idxCache contains the block numbers where governance change happened. The cache can have up to 1000 block numbers in memory by default.
     * <pre>Example :
     * {@code
     * GovernanceIdxCache response = caver.rpc.governance.getIdxCache().send();
     * }</pre>
     * @return Request&lt;?, GovernanceIdxCache&gt;
     */
    public Request<?, GovernanceIdxCache> getIdxCache() {
        return new Request<>(
                "governance_idxCache",
                Collections.emptyList(),
                provider,
                GovernanceIdxCache.class
        );
    }

    /**
     * Returns an array that contains all block numbers on which a governance change ever happened.<p>
     * The result of idxCacheFromDb is the same or longer than that of idxCache.
     * <pre>Example :
     * {@code
     * GovernanceIdxCache response = caver.rpc.governance.getIdxCacheFromDb().send();
     * }
     * </pre>
     * @return Request&lt;?, GovernanceIdxCache&gt;
     */
    public Request<?, GovernanceIdxCache> getIdxCacheFromDb() {
        return new Request<>(
                "governance_idxCacheFromDb",
                Collections.emptyList(),
                provider,
                GovernanceIdxCache.class
        );
    }

    /**
     * Returns  the governance information stored in the given block. If no changes were stored in the given block, the function returns null.
     * <pre>Example :
     * {@code
     * GovernanceItems response = caver.rpc.governance.getItemCacheFromDb(BigInteger.ZERO).send();
     * }
     * </pre>
     * @param blockNumber A block number to query the governance change made in the block.
     * @return Request&lt;?, GovernanceItems&gt;
     */
    public Request<?, GovernanceItems> getItemCacheFromDb(BigInteger blockNumber) {
        return new Request<>(
                "governance_itemCacheFromDb",
                Arrays.asList(new DefaultBlockParameterNumber(blockNumber)),
                provider,
                GovernanceItems.class
        );
    }

    /**
     * The getStakingInfo returns staking information at a specific block.<p>
     * It passes the latest block tag as a parameter.
     * <pre>Example :
     * {@code
     * GovernanceStackingInfo response = caver.rpc.governance.getStakingInfo().send();
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
     * GovernanceStackingInfo response = caver.rpc.governance.getStakingInfo(BigInteger.ZERO).send();
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
     * GovernanceStackingInfo response = caver.rpc.governance.getStakingInfo("latest").send();
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
     * GovernanceStackingInfo response = caver.rpc.governance.getStakingInfo(DefaultBlockParameterName.LATEST).send();
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
                "governance_getStakingInfo",
                Arrays.asList(blockParam),
                provider,
                GovernanceStakingInfo.class
        );
    }

    public Request<?, RewardsAccumulated> getRewardsAccumulated(String fromBlock, String toBlock) {
        DefaultBlockParameterName fromBlockTagName = DefaultBlockParameterName.fromString(fromBlock);
        DefaultBlockParameterName toBlockTagName = DefaultBlockParameterName.fromString(toBlock);

        return getRewardsAccumulated(fromBlockTagName, toBlockTagName);
    }

    public Request<?, RewardsAccumulated> getRewardsAccumulated(BigInteger fromBlock, BigInteger toBlock) {
        return getRewardsAccumulated(fromBlock, toBlock);
    }

    public Request<?, RewardsAccumulated> getRewardsAccumulated(DefaultBlockParameterName fromBlock, DefaultBlockParameterName toBlock) {
        return getRewardsAccumulated((DefaultBlockParameter)fromBlock, (DefaultBlockParameter)toBlock);
    }

    Request<?, RewardsAccumulated> getRewardsAccumulated(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock) {
        return new Request<>(
                "governance_getRewardsAccumulated",
                Arrays.asList(fromBlock, toBlock),
                provider,
                RewardsAccumulated.class
        );
    }
}