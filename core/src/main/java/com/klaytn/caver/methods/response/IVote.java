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
        REWARD_STAKING_UPDATE_INTERVAL("reward.stakingupdateinterval", "BigInteger");

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
         * @param map The map instance to find value.
         * @return String
         */
        public static String getGovernanceMode(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_GOVERNANCE_MODE.getKey());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNANCE_MODE.
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
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getGovernanceMode(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNING_NODE.
         * @param map The map instance to find value.
         * @return String
         */
        public static String getGoverningNode(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_GOVERNING_NODE.getKey());
        }

        /**
         * Get the value of GOVERNANCE_GOVERNING_NODE.
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
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getGoverningNode(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_UNIT_PRICE.
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getUnitPrice(Map<String, Object> map) {
            return toBigIntegerValue(map.get(GOVERNANCE_UNIT_PRICE.getKey()));
        }

        /**
         * Get the value of governance unit price.
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
         * @param vote The instance implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getUnitPrice(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_ADD_VALIDATOR.
         * @param map The map instance to find value.
         * @return String
         */
        public static String getAddValidator(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_ADD_VALIDATOR.getKey());
        }

        /**
         * Get the value of GOVERNANCE_ADD_VALIDATOR.
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
         * @param vote The instance implemented IVote to find value.
         * @return String
         */
        public static String getAddValidator(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of GOVERNANCE_REMOVE_VALIDATOR.
         * @param map The map instance to find value.
         * @return String
         */
        public static String getRemoveValidator(Map<String, Object> map) {
            return (String)map.get(GOVERNANCE_REMOVE_VALIDATOR.getKey());
        }

        /**
         * Get the value of GOVERNANCE_REMOVE_VALIDATOR.
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
         * @param vote The instance implemented IVote to find value.
         * @return String
         */
        public static String getRemoveValidator(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_EPOCH.
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getEpoch(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_EPOCH.getKey()));
        }

        /**
         * Get the value of ISTANBUL_EPOCH.
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
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getEpoch(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_COMMITTEE_SIZE.
         * @param map The map instance to find value.
         * @return
         */
        public static BigInteger getCommitteeSize(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_COMMITTEE_SIZE.getKey()));
        }

        /**
         * Get the value of ISTANBUL_COMMITTEE_SIZE.
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
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getCommitteeSize(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of ISTANBUL_POLICY.
         * @param map The mpa instance to find value.
         * @return BigInteger
         */
        public static BigInteger getPolicy(Map<String, Object> map) {
            return toBigIntegerValue(map.get(ISTANBUL_POLICY.getKey()));
        }

        /**
         * Get the value of ISTANBUL_POLICY.
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
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getPolicy(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_MINTING_AMOUNT.
         * @param map The map instance to find value.
         * @return String
         */
        public static String getMintingAmount(Map<String, Object> map) {
            return (String)map.get(REWARD_MINTING_AMOUNT.getKey());
        }

        /**
         * Get the value of REWARD_MINTING_AMOUNT.
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
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getMintingAmount(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }


        /**
         * Get the value of REWARD_RATIO.
         * @param map The list instance to find value.
         * @return String
         */
        public static String getRatio(Map<String, Object> map) {
            return (String)map.get(REWARD_RATIO.getKey());
        }

        /**
         * Get the value of REWARD_RATIO.
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
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getRatio(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }


        /**
         * Get the value of REWARD_USE_GINICOEFF.
         * @param map The map instance to find value.
         * @return boolean
         */
        public static boolean getUseGinicoeff(Map<String, Object> map) {
            return (boolean)map.get(REWARD_USE_GINICOEFF.getKey());
        }

        /**
         * Get the value of REWARD_USE_GINICOEFF.
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
         * @param vote The instance that implemented IVote to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getUseGinicoeff(IVote vote) {
            return toBooleanValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_USE_DEFERRED_TX_FEE.
         * @param map The map instance to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getDeferredTxFee(Map<String, Object> map) {
            return (java.lang.Boolean)map.get(REWARD_USE_DEFERRED_TX_FEE.getKey());
        }

        /**
         * Get the value of REWARD_USE_DEFERRED_TX_FEE.
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
         * @param vote The instance that implemented IVote to find value.
         * @return Boolean
         */
        public static java.lang.Boolean getDeferredTxFee(IVote vote) {
            return toBooleanValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_MINIMUM_STAKE.
         * @param map The map instance to find value.
         * @return String
         */
        public static String getMinimumStake(Map<String, Object> map) {
            return (String)map.get(REWARD_MINIMUM_STAKE.getKey());
        }

        /**
         * Get the value of REWARD_MINIMUM_STAKE.
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
         * @param vote The instance that implemented IVote to find value.
         * @return String
         */
        public static String getMinimumStake(IVote vote) {
            return toStringValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_PROPOSER_UPDATE_INTERVAL.
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getProposerUpdateInterval(Map<String, Object> map) {
            return toBigIntegerValue(map.get(REWARD_PROPOSER_UPDATE_INTERVAL.getKey()));
        }

        /**
         * Get the value of REWARD_PROPOSER_UPDATE_INTERVAL.
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
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getProposerUpdateInterval(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Get the value of REWARD_STAKING_UPDATE_INTERVAL.
         * @param map The map instance to find value.
         * @return BigInteger
         */
        public static BigInteger getStakingUpdateInterval(Map<String, Object> map) {
            return toBigIntegerValue(map.get(REWARD_STAKING_UPDATE_INTERVAL.getKey()));
        }

        /**
         * Get the value of REWARD_STAKING_UPDATE_INTERVAL.
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
         * @param vote The instance that implemented IVote to find value.
         * @return BigInteger
         */
        public static BigInteger getStakingUpdateInterval(IVote vote) {
            return toBigIntegerValue(vote.getKey(), vote.getValue());
        }

        /**
         * Convert a Object to String.<p>
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
         * Convert a Object to boolean.<p>
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
         * Convert a Object to BigInteger.<p>
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
         * Convert a Object to BigInteger.
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
