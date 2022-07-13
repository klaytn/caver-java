/*
 * Copyright 202 The caver-java Authors
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
package com.klaytn.caver.methods.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * The interface represented to handle Vote item in Governance API's response class.
 * @see GovernanceItems
 * @see GovernanceMyVotes
 * @see GovernanceNodeVotes
 * @see GovernanceTally
 */
public interface IVote {
    enum VoteItem {
        GOVERNANCE_GOVERNANCE_MODE("governance.governancemode", "String"),
        GOVERNANCE_GOVERNING_NODE("governance.governingnode", "String"),
        GOVERNANCE_UNIT_PRICE("governance.unitprice", "BigInteger"),
        GOVERNANCE_ADD_VALIDATOR("governance.addvalidator", "String"),
        GOVERNANCE_REMOVE_VALIDATOR("governance.removevalidator", "String"),
        ISTANBUL_EPOCH("istanbul.epoch", "BigInteger"),
        ISTANBUL_COMMITTEE_SIZE("istanbul.committeesize", "BigInteger"),
        ISTANBUL_POLICY("istanbul.policy", "BigInteger"),
        REWARD_MINTING_AMOUNT("reward.mintingamount", "String"),
        REWARD_RATIO("reward.ratio", "String"),
        REWARD_USE_GINICOEFF("reward.useginicoeff", "Boolean"),
        REWARD_USE_DEFERRED_TX_FEE("reward.deferredtxfee", "Boolean"),
        REWARD_MINIMUM_STAKE("reward.minimumstake", "String"),
        REWARD_PROPOSER_UPDATE_INTERVAL("reward.proposerupdateinterval", "BigInteger"),
        REWARD_STAKING_UPDATE_INTERVAL("reward.stakingupdateinterval", "BigInteger"),
        KIP71_LOWER_BOUND_BASE_FEE("kip71.lowerboundbasefee", "BigInteger"),
        KIP71_UPPER_BOUND_BASE_FEE("kip71.upperboundbasefee", "BigInteger"),
        KIP71_GAS_TARGET("kip71.gastarget", "BigInteger"),
        KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE("kip71.maxblockgasusedforbasefee", "BigInteger"),
        KIP71_BASE_FEE_DENOMINATOR("kip71.basefeedenominator", "Integer");

        String key;
        String type;

        VoteItem(String key, String type) {
            this.key = key;
            this.type = type;
        }

        /**
         * Check if there is an enum mapped to the given type string.
         * @param key The key string to find enum defined in VoteItem.
         * @return boolean
         */
        public static boolean isExist(String key) {
            for(VoteItem item : VoteItem.values()) {
                if(item.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Retrieve a VoteItem from key string.
         * @param key The key to retrieve VoteItem enum.
         * @return VoteItem
         */
        public static VoteItem fromString(String key) {
            for(VoteItem item : VoteItem.values()) {
                if(item.getKey().equals(key)) {
                    return item;
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_GOVERNANCE_MODE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getGovernanceMode(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getGovernanceMode(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_GOVERNANCE_MODE.getKey());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNANCE_MODE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getGovernanceMode(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getGovernanceMode(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(GOVERNANCE_GOVERNANCE_MODE.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_GOVERNANCE_MODE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getGovernanceMode(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getGovernanceMode(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNING_NODE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getGoverningNode(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getGoverningNode(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_GOVERNING_NODE.getKey());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNING_NODE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getGoverningNode(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getGoverningNode(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(GOVERNANCE_GOVERNING_NODE.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_GOVERNING_NODE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getGoverningNode(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getGoverningNode(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_UNIT_PRICE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getUnitPrice(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getUnitPrice(Map<String, Object> map) {
            return toBigIntegerValue(map.get(GOVERNANCE_UNIT_PRICE.getKey()));
        }

        /**
         * Get the value of GOVERNANCE_UNIT_PRICE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getUnitPrice(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getUnitPrice(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(GOVERNANCE_UNIT_PRICE.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_UNIT_PRICE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getUnitPrice(myVote);
         * }</pre>
         * @param vote The instance implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getUnitPrice(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_ADD_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getAddValidator(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getAddValidator(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_ADD_VALIDATOR.getKey());
        }

        /**
         * Get the value of GOVERNANCE_ADD_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getAddValidator(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getAddValidator(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(GOVERNANCE_ADD_VALIDATOR.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_ADD_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getAddValidator(myVote);
         * }</pre>
         * @param vote The instance implemented IVote to find value.
         * @return String
         */
        public static String getAddValidator(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_REMOVE_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getRemoveValidator(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getRemoveValidator(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_REMOVE_VALIDATOR.getKey());
        }

        /**
         * Get the value of GOVERNANCE_REMOVE_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getRemoveValidator(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getRemoveValidator(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(GOVERNANCE_REMOVE_VALIDATOR.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of GOVERNANCE_REMOVE_VALIDATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getRemoveValidator(myVote);
         * }</pre>
         * @param vote The instance implemented IVote to find value.
         * @return String
         */
        public static String getRemoveValidator(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_EPOCH.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getEpoch(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getEpoch(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_EPOCH.getKey()));
        }

        /**
         * Get the value of ISTANBUL_EPOCH.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getEpoch(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getEpoch(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(ISTANBUL_EPOCH.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of ISTANBUL_EPOCH.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getEpoch(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getEpoch(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_COMMITTEE_SIZE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getCommitteeSize(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return
         */
        public static BigInteger getCommitteeSize(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_COMMITTEE_SIZE.getKey()));
        }

        /**
         * Get the value of ISTANBUL_COMMITTEE_SIZE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getCommitteeSize(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getCommitteeSize(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(ISTANBUL_COMMITTEE_SIZE.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of ISTANBUL_COMMITTEE_SIZE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getCommitteeSize(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getCommitteeSize(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_POLICY.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getPolicy(governanceItem);
         * }</pre>
         * @param map The mpa instance to find value.
         * @return BigInteger
         */
        public static BigInteger getPolicy(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_POLICY.getKey()));
        }

        /**
         * Get the value of ISTANBUL_POLICY.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getPolicy(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getPolicy(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(ISTANBUL_POLICY.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of ISTANBUL_POLICY.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getPolicy(myVote));
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getPolicy(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_MINTING_AMOUNT.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getMintingAmount(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getMintingAmount(Map<String, Object> map) {
            return (String)map.get(REWARD_MINTING_AMOUNT.getKey());
        }

        /**
         * Get the value of REWARD_MINTING_AMOUNT.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getMintingAmount(voteList);
         * }</pre>
         * @param list The list instance to find value..
         * @return String
         */
        public static String getMintingAmount(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_MINTING_AMOUNT.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_MINTING_AMOUNT.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getMintingAmount(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getMintingAmount(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }


        /**
         * Get the value of REWARD_RATIO.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getRatio(governanceItem);
         * }</pre>
         * @param map The list instance to find value.
         * @return String
         */
        public static String getRatio(Map<String, Object> map) {
            return (String)map.get(REWARD_RATIO.getKey());
        }

        /**
         * Get the value of REWARD_RATIO.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getRatio(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getRatio(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_RATIO.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_RATIO.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getRatio(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getRatio(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }


        /**
         * Get the value of REWARD_USE_GINICOEFF.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * boolean value = IVote.VoteItem.getUseGinicoeff(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return boolean
         */
        public static boolean getUseGinicoeff(Map<String, Object> map) {
            return (boolean)map.get(REWARD_USE_GINICOEFF.getKey());
        }

        /**
         * Get the value of REWARD_USE_GINICOEFF.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * boolean value = IVote.VoteItem.getUseGinicoeff(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return boolean
         */
        public static java.lang.Boolean getUseGinicoeff(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_USE_GINICOEFF.getKey())) {
                    return (java.lang.Boolean)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_USE_GINICOEFF.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * boolean value = IVote.VoteItem.getUseGinicoeff(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getUseGinicoeff(IVote vote) {
            return toBooleanValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_USE_DEFERRED_TX_FEE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * boolean value = IVote.VoteItem.getDeferredTxFee(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getDeferredTxFee(Map<String, Object> map) {
            return (java.lang.Boolean)map.get(REWARD_USE_DEFERRED_TX_FEE.getKey());
        }

        /**
         * Get the value of REWARD_USE_DEFERRED_TX_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * boolean value = IVote.VoteItem.getDeferredTxFee(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getDeferredTxFee(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_USE_DEFERRED_TX_FEE.getKey())) {
                    return (java.lang.Boolean)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_USE_DEFERRED_TX_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * boolean value = IVote.VoteItem.getDeferredTxFee(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getDeferredTxFee(IVote vote) {
            return toBooleanValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_MINIMUM_STAKE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * String value = IVote.VoteItem.getMinimumStake(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return String
         */
        public static String getMinimumStake(Map<String, Object> map) {
            return (String)map.get(REWARD_MINIMUM_STAKE.getKey());
        }

        /**
         * Get the value of REWARD_MINIMUM_STAKE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * String value = IVote.VoteItem.getMinimumStake(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return String
         */
        public static String getMinimumStake(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_MINIMUM_STAKE.getKey())) {
                    return (String)vote.getValue();
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_MINIMUM_STAKE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * String value = IVote.VoteItem.getMinimumStake(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getMinimumStake(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_PROPOSER_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getProposerUpdateInterval(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getProposerUpdateInterval(Map<String, Object> map) {
            return toBigIntegerValue(map.get(REWARD_PROPOSER_UPDATE_INTERVAL.getKey()));
        }

        /**
         * Get the value of REWARD_PROPOSER_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getProposerUpdateInterval(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getProposerUpdateInterval(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_PROPOSER_UPDATE_INTERVAL.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_PROPOSER_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getProposerUpdateInterval(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getProposerUpdateInterval(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_STAKING_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getStakingUpdateInterval(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getStakingUpdateInterval(Map<String, Object> map) {
            return toBigIntegerValue(map.get(REWARD_STAKING_UPDATE_INTERVAL.getKey()));
        }

        /**
         * Get the value of REWARD_STAKING_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getStakingUpdateInterval(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getStakingUpdateInterval(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(REWARD_STAKING_UPDATE_INTERVAL.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of REWARD_STAKING_UPDATE_INTERVAL.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getStakingUpdateInterval(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getStakingUpdateInterval(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of KIP71_LOWER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71LowerBoundBaseFee(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71LowerBoundBaseFee(Map<String, Object> map) {
            return toBigIntegerValue(map.get(KIP71_LOWER_BOUND_BASE_FEE.getKey()));
        }

        /**
         * Get the value of KIP71_LOWER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71LowerBoundBaseFee(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71LowerBoundBaseFee(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(KIP71_LOWER_BOUND_BASE_FEE.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of KIP71_LOWER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getKIP71LowerBoundBaseFee(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71LowerBoundBaseFee(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of KIP71_UPPER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71UpperBoundBaseFee(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71UpperBoundBaseFee(Map<String, Object> map) {
            return toBigIntegerValue(map.get(KIP71_UPPER_BOUND_BASE_FEE.getKey()));
        }

        /**
         * Get the value of KIP71_UPPER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71UpperBoundBaseFee(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71UpperBoundBaseFee(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(KIP71_UPPER_BOUND_BASE_FEE.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of KIP71_UPPER_BOUND_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getKIP71UpperBoundBaseFee(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71UpperBoundBaseFee(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of KIP71_GAS_TARGET.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71GasTarget(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71GasTarget(Map<String, Object> map) {
            return toBigIntegerValue(map.get(KIP71_GAS_TARGET.getKey()));
        }

        /**
         * Get the value of KIP71_GAS_TARGET.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71GasTarget(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71GasTarget(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(KIP71_GAS_TARGET.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of KIP71_GAS_TARGET.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getKIP71GasTarget(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71GasTarget(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71MaxBlockGasUsedForBaseFee(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71MaxBlockGasUsedForBaseFee(Map<String, Object> map) {
            return toBigIntegerValue(map.get(KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE.getKey()));
        }

        /**
         * Get the value of KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71MaxBlockGasUsedForBaseFee(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71MaxBlockGasUsedForBaseFee(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE.getKey())) {
                    return toBigIntegerValue(vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of KIP71_MAX_BLOCK_GAS_USED_FOR_BASE_FEE.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getKIP71MaxBlockGasUsedForBaseFee(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getKIP71MaxBlockGasUsedForBaseFee(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of KIP71_BASE_FEE_DENOMINATOR.
         * <pre>Example :
         * {@code
         * GovernanceItems response = caver.rpc.governance.getItemsAt().send();
         * Map<String, Object> governanceItem = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71BaseFeeDenominator(governanceItem);
         * }</pre>
         * @param map The map instance to find value.
         * @return Integer
         */
        public static Integer getKIP71BaseFeeDenominator(Map<String, Object> map) {
            return toIntegerValue(KIP71_BASE_FEE_DENOMINATOR.getKey(), map.get(KIP71_BASE_FEE_DENOMINATOR.getKey()));
        }

        /**
         * Get the value of KIP71_BASE_FEE_DENOMINATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         *
         * BigInteger value = IVote.VoteItem.getKIP71BaseFeeDenominator(voteList);
         * }</pre>
         * @param list The list instance to find value.
         * @return Integer
         */
        public static Integer getKIP71BaseFeeDenominator(List<IVote> list) {
            for(IVote vote: list) {
                if(vote.getKey().equals(KIP71_BASE_FEE_DENOMINATOR.getKey())) {
                    return toIntegerValue(KIP71_BASE_FEE_DENOMINATOR.getKey(), vote.getValue());
                }
            }
            return null;
        }

        /**
         * Get the value of KIP71_BASE_FEE_DENOMINATOR.
         * <pre>Example :
         * {@code
         * GovernanceMyVotes response = caver.rpc.governance.getMyVotes().send();
         * List voteList = response.getResult();
         * GovernanceMyVotes.MyVote myVote = (GovernanceMyVotes.MyVote)voteList.get(0);
         *
         * BigInteger value = IVote.VoteItem.getKIP71BaseFeeDenominator(myVote);
         * }</pre>
         * @param vote The instance that implemented IVote to find value.
         * @return Integer
         */
        public static Integer getKIP71BaseFeeDenominator(IVote vote) {
            return toIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Convert an Object to String.<p>
         * If the VoteItem mapped to key is not existed or the type of VoteItem mapped to key is not valid, It will throw RuntimeException.
         * @param key The key mapped to value.
         * @param value The value converted to string.
         * @return String
         */
        public static String toStringValue(String key, Object value) {
            validateKeyValues(key, "String");
            return (String) value;
        }

        /**
         * Convert an Object to boolean.<p>
         * If the VoteItem mapped to key is not existed or the type of VoteItem mapped to key is not valid, It will throw RuntimeException.
         * @param key The key mapped to value.
         * @param value The value converted to string.
         * @return boolean
         */
        public static boolean toBooleanValue(String key, Object value) {
            validateKeyValues(key, "Boolean");
            return (boolean)value;
        }

        /**
         * Convert an Object to BigInteger.<p>
         * If the VoteItem mapped to key is not existed or the type of VoteItem mapped to key is not valid, It will throw RuntimeException.
         * @param key The key mapped to value.
         * @param value The value converted to BigInteger.
         * @return BigInteger
         */
        public static BigInteger toBigIntegerValue(String key, Object value) {
            validateKeyValues(key, "BigInteger");
            return toBigIntegerValue(value);
        }

        /**
         * Convert an Object to BigInteger.
         * @param value The value converted to BigInteger.
         * @return BigIneger.
         */
        public static BigInteger toBigIntegerValue(Object value) {
            BigInteger ret = null;

            if ( value instanceof BigInteger ) {
                ret = (BigInteger) value;
            } else if ( value instanceof String ) {
                ret = new BigInteger( (String) value );
            } else if ( value instanceof BigDecimal) {
                ret = ((BigDecimal) value).toBigInteger();
            } else if ( value instanceof java.lang.Number) {
                ret = BigInteger.valueOf( ((java.lang.Number) value).longValue() );
            } else {
                throw new ClassCastException( "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigInteger." );
            }

            return ret;
        }

        /**
         * Convert an Object to Integer.<p>
         * If the VoteItem mapped to key is not existed or the type of VoteItem mapped to key is not valid, It will throw RuntimeException.
         * @param key The key mapped to value.
         * @param value The value converted to Integer.
         * @return Integer
         */
        public static Integer toIntegerValue(String key, Object value) {
            validateKeyValues(key, "Integer");
            return (Integer) value;
        }

        private static void validateKeyValues(String key, String type) {
            VoteItem item = fromString(key);

            if(item == null) {
                throw new IllegalArgumentException("The " + key + "cannot existed in Vote item");
            }

            if(!item.getType().equals(type)) {
                throw new IllegalArgumentException("The type " + key + " cannot be converted to the " + type + "type. It can be converted to " + item.getType());
            }
        }

        public String getKey() {
            return key;
        }

        String getType() {
            return type;
        }
    }

    public String getKey();
    public Object getValue();
}
