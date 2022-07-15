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

package com.klaytn.caver.methods.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.klaytn.caver.rpc.Governance;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.math.BigInteger;

/**
 * The class represented to store the response data of the {@link Governance#getChainConfig()} method.
 */
public class GovernanceChainConfig extends Response<GovernanceChainConfig.ChainConfigData> {

    public static class ChainConfigData {
        /**
         * The Istanbul instance.
         */
        @JsonProperty("istanbul")
        private Istanbul istanbul;

        /**
         * The Governance instance.
         */
        @JsonProperty("governance")
        private Governance governance;

        /**
         * The value of deriveShaImpl field.
         */
        @JsonProperty("deriveShaImpl")
        private int deriveShaImpl;

        /**
         * The value of istanbulCompatibleBlock field.
         */
        @JsonProperty("istanbulCompatibleBlock")
        private BigInteger istanbulCompatibleBlock;

        /**
         * The value of londonCompatibleBlock field.
         */
        @JsonProperty("londonCompatibleBlock")
        private BigInteger londonCompatibleBlock;

        /**
         * The value of ethTxTypeCompatibleBlock field.
         */
        @JsonProperty("ethTxTypeCompatibleBlock")
        private BigInteger ethTxTypeCompatibleBlock;

        /**
         * The value of magmaCompatibleBlock field.
         */
        @JsonProperty("magmaCompatibleBlock")
        private BigInteger magmaCompatibleBlock;

        /**
         * The value of chainId field.
         */
        @JsonProperty("chainId")
        private int chainId;

        public static class Istanbul {

            /**
             * The value of sub field.
             */
            @JsonProperty("sub")
            private BigInteger sub;

            /**
             * The value of policy field.
             */
            @JsonProperty("policy")
            private BigInteger policy;

            /**
             * The value of epoch field.
             */
            @JsonProperty("epoch")
            private BigInteger epoch;

            /**
             * Getter function for sub field.
             * @return BigInteger
             */
            public BigInteger getSub() {
                return sub;
            }

            /**
             * Setter function for sub field.
             * @param sub The value of sub.
             */
            public void setSub(BigInteger sub) {
                this.sub = sub;
            }

            /**
             * Getter function for policy field.
             * @return The value of policy.
             */
            public BigInteger getPolicy() {
                return policy;
            }

            /**
             * Setter function for policy field.
             * @param policy The value of policy.
             */
            public void setPolicy(BigInteger policy) {
                this.policy = policy;
            }

            /**
             * Getter function for epoch field.
             * @return The value of epoch.
             */
            public BigInteger getEpoch() {
                return epoch;
            }

            /**
             * Setter function for epoch field.
             * @param epoch The value of epoch.
             */
            public void setEpoch(BigInteger epoch) {
                this.epoch = epoch;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public static class Governance {

            /**
             * The value of reward field.
             */
            @JsonProperty("reward")
            private Reward reward;

            /**
             * The value of kip71 field.
             */
            @JsonProperty("kip71")
            private KIP71 kip71;

            /**
             * The value of governingNode field.
             */
            @JsonProperty("governingNode")
            private String governingNode;

            /**
             * The value of governanceMode field.
             */
            @JsonProperty("governanceMode")
            private String governanceMode;

            /**
             * Getter function for reward field.
             * @return Reward
             */
            public Reward getReward() {
                return reward;
            }

            /**
             * Setter function for reward field.
             * @param reward The value of reward field.
             */
            public void setReward(Reward reward) {
                this.reward = reward;
            }

            /**
             * Getter function for kip71 field.
             * @return KIP71
             */
            public KIP71 getKIP71() {
                return kip71;
            }

            /**
             * Setter function for kip71 field.
             * @param kip71 The value of kip71 field.
             */
            public void setKIP71(KIP71 kip71) {
                this.kip71 = kip71;
            }

            /**
             * Getter function for governingNode field.
             * @return String
             */
            public String getGoverningNode() {
                return governingNode;
            }

            /**
             * Setter function for governingNode field.
             * @param governingNode The value of governingNode field.
             */
            public void setGoverningNode(String governingNode) {
                this.governingNode = governingNode;
            }

            /**
             * Getter function for governanceMode field.
             * @return String
             */
            public String getGovernanceMode() {
                return governanceMode;
            }

            /**
             * Setter function for governanceMode field.
             * @param governanceMode The value of governanceMode field.
             */
            public void setGovernanceMode(String governanceMode) {
                this.governanceMode = governanceMode;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public static class Reward {

            /**
             * The value of useGiniCoeff field.
             */
            @JsonProperty("useGiniCoeff")
            private boolean useGiniCoeff;

            /**
             * The value of stakingUpdateInterval field.
             */
            @JsonProperty("stakingUpdateInterval")
            private BigInteger stakingUpdateInterval;

            /**
             * The value of ratio field.
             */
            @JsonProperty("ratio")
            private String ratio;

            /**
             * The value of proposerUpdateInterval field.
             */
            @JsonProperty("proposerUpdateInterval")
            private BigInteger proposerUpdateInterval;

            /**
             * The value of mintingAmount field.
             */
            @JsonProperty("mintingAmount")
            private BigInteger mintingAmount;

            /**
             * The value of minimumStake field.
             */
            @JsonProperty("minimumStake")
            private BigInteger minimumStake;

            /**
             * The value of deferredTxFee field.
             */
            @JsonProperty("deferredTxFee")
            private boolean deferredTxFee;

            /**
             * Getter function for useGiniCoeff.
             * @return boolean
             */
            public boolean getUseGiniCoeff() {
                return useGiniCoeff;
            }

            /**
             * Setter function for useGiniCoeff.
             * @param useGiniCoeff The value of useGiniCoeff
             */
            public void setUseGiniCoeff(boolean useGiniCoeff) {
                this.useGiniCoeff = useGiniCoeff;
            }

            /**
             * Getter function for stakingUpdateInterval
             * @return BigInteger
             */
            public BigInteger getStakingUpdateInterval() {
                return stakingUpdateInterval;
            }

            /**
             * Setter function for stakingUpdateInterval.
             * @param stakingUpdateInterval The valuie of stakingUpdateInterval.
             */
            public void setStakingUpdateInterval(BigInteger stakingUpdateInterval) {
                this.stakingUpdateInterval = stakingUpdateInterval;
            }

            /**
             * Getter function for ratio.
             * @return String
             */
            public String getRatio() {
                return ratio;
            }

            /**
             * Setter function for ratio.
             * @param ratio The value of ratio field.
             */
            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            /**
             * Getter function for proposerUpdateInterval.
             * @return BigInteger
             */
            public BigInteger getProposerUpdateInterval() {
                return proposerUpdateInterval;
            }

            /**
             * Setter function for proposerUpdateInterval.
             * @param proposerUpdateInterval The value of proposerUpdateInterval
             */
            public void setProposerUpdateInterval(BigInteger proposerUpdateInterval) {
                this.proposerUpdateInterval = proposerUpdateInterval;
            }

            /**
             * Getter function for mintingAmount
             * @return BigInteger
             */
            public BigInteger getMintingAmount() {
                return mintingAmount;
            }

            /**
             * Setter function for mintingAmount.
             * @param mintingAmount The value of mintingAmount.
             */
            public void setMintingAmount(BigInteger mintingAmount) {
                this.mintingAmount = mintingAmount;
            }

            /**
             * Getter function for minimumStake.
             * @return BigInteger
             */
            public BigInteger getMinimumStake() {
                return minimumStake;
            }

            /**
             * Setter function for minimumStake.
             * @param minimumStake The value of minimumStake.
             */
            public void setMinimumStake(BigInteger minimumStake) {
                this.minimumStake = minimumStake;
            }

            /**
             * Getter function for deferredTxFee.
             * @return boolean
             */
            public boolean getDeferredTxFee() {
                return deferredTxFee;
            }

            /**
             * Setter function for deferredTxFee.
             * @param deferredTxFee The value of deferredTxFee.
             */
            public void setDeferredTxFee(boolean deferredTxFee) {
                this.deferredTxFee = deferredTxFee;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public static class KIP71 {

            /**
             * The value of basefeedenominator field.
             */
            @JsonProperty("basefeedenominator")
            private Integer baseFeeDenominator;

            /**
             * The value of gastarget field.
             */
            @JsonProperty("gastarget")
            private BigInteger gasTarget;

            /**
             * The value of lowerboundbasefee field.
             */
            @JsonProperty("lowerboundbasefee")
            private BigInteger lowerBoundBaseFee;

            /**
             * The value of upperboundbasefee field.
             */
            @JsonProperty("upperboundbasefee")
            private BigInteger upperBoundBaseFee;

            /**
             * The value of maxblockgasusedforbasefee field.
             */
            @JsonProperty("maxblockgasusedforbasefee")
            private BigInteger maxBlockGasUsedForBaseFee;

            /**
             * Getter function for baseFeeDenominator.
             * @return Integer
             */
            public Integer getBaseFeeDenominator() {
                return baseFeeDenominator;
            }

            /**
             * Setter function for baseFeeDenominator.
             * @param baseFeeDenominator The value of baseFeeDenominator
             */
            public void setBaseFeeDenominator(Integer baseFeeDenominator) {
                this.baseFeeDenominator = baseFeeDenominator;
            }

            /**
             * Getter function for gasTarget.
             * @return BigInteger
             */
            public BigInteger getGasTarget() {
                return gasTarget;
            }

            /**
             * Setter function for gasTarget.
             * @param gasTarget The value of gasTarget
             */
            public void setGasTarget(BigInteger gasTarget) {
                this.gasTarget = gasTarget;
            }

            /**
             * Getter function for lowerBoundBaseFee.
             * @return BigInteger
             */
            public BigInteger getLowerBoundBaseFee() {
                return lowerBoundBaseFee;
            }

            /**
             * Setter function for lowerBoundBaseFee.
             * @param lowerBoundBaseFee The value of lowerBoundBaseFee
             */
            public void setLowerBoundBaseFee(BigInteger lowerBoundBaseFee) {
                this.lowerBoundBaseFee = lowerBoundBaseFee;
            }

            /**
             * Getter function for upperBoundBaseFee.
             * @return BigInteger
             */
            public BigInteger getUpperBoundBaseFee() {
                return upperBoundBaseFee;
            }

            /**
             * Setter function for upperBoundBaseFee.
             * @param upperBoundBaseFee The value of upperBoundBaseFee
             */
            public void setUpperBoundBaseFee(BigInteger upperBoundBaseFee) {
                this.upperBoundBaseFee = upperBoundBaseFee;
            }

            /**
             * Getter function for maxBlockGasUsedForBaseFee.
             * @return BigInteger
             */
            public BigInteger getMaxBlockGasUsedForBaseFee() {
                return maxBlockGasUsedForBaseFee;
            }

            /**
             * Setter function for maxBlockGasUsedForBaseFee.
             * @param maxBlockGasUsedForBaseFee The value of maxBlockGasUsedForBaseFee
             */
            public void setMaxBlockGasUsedForBaseFee(BigInteger maxBlockGasUsedForBaseFee) {
                this.maxBlockGasUsedForBaseFee = maxBlockGasUsedForBaseFee;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        /**
         * Getter function for istanbul.
         * @return Istanbul
         */
        public Istanbul getIstanbul() {
            return istanbul;
        }

        /**
         * Getter function for governance.
         * @return Governance
         */
        public Governance getGovernance() {
            return governance;
        }

        /**
         * Getter function for deriveShaImpl
         * @return int
         */
        public int getDeriveshaimpl() {
            return deriveShaImpl;
        }

        /**
         * Getter function for chainId.
         * @return int
         */
        public int getChainid() {
            return chainId;
        }

        /**
         * Getter function for istanbulCompatibleBlock.
         * @return BigInteger
         */
        public BigInteger getIstanbulCompatibleBlock() {
            return istanbulCompatibleBlock;
        }

        /**
         * Getter function for londonCompatibleBlock.
         * @return BigInteger
         */
        public BigInteger getLondonCompatibleBlock() {
            return londonCompatibleBlock;
        }

        /**
         * Getter function for ethTxTypeCompatibleBlock.
         * @return BigInteger
         */
        public BigInteger getEthTxTypeCompatibleBlock() {
            return ethTxTypeCompatibleBlock;
        }

        /**
         * Getter function for magmaCompatibleBlock.
         * @return BigInteger
         */
        public BigInteger getMagmaCompatibleBlock() {
            return magmaCompatibleBlock;
        }


        @Override
        public String toString() {
            return Utils.printString(this);
        }
    }

    @Override
    public String toString() {
        return Utils.printString(this);
    }
}
