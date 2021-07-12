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
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.math.BigInteger;

public class GovernanceChainConfig extends Response<GovernanceChainConfig.ChainConfigData> {

    public static class ChainConfigData {

        @JsonProperty("istanbul")
        private Istanbul istanbul;
        @JsonProperty("governance")
        private Governance governance;
        @JsonProperty("deriveShaImpl")
        private int deriveShaImpl;
        @JsonProperty("chainId")
        private int chainId;

        public static class Istanbul {
            @JsonProperty("sub")
            private BigInteger sub;
            @JsonProperty("policy")
            private BigInteger policy;
            @JsonProperty("epoch")
            private BigInteger epoch;

            public BigInteger getSub() {
                return sub;
            }

            public void setSub(BigInteger sub) {
                this.sub = sub;
            }

            public BigInteger getPolicy() {
                return policy;
            }

            public void setPolicy(BigInteger policy) {
                this.policy = policy;
            }

            public BigInteger getEpoch() {
                return epoch;
            }

            public void setEpoch(BigInteger epoch) {
                this.epoch = epoch;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public static class Governance {
            @JsonProperty("reward")
            private Reward reward;
            @JsonProperty("governingNode")
            private String governingNode;
            @JsonProperty("governanceMode")
            private String governanceMode;

            public Reward getReward() {
                return reward;
            }

            public void setReward(Reward reward) {
                this.reward = reward;
            }

            public String getGoverningNode() {
                return governingNode;
            }

            public void setGoverningNode(String governingNode) {
                this.governingNode = governingNode;
            }

            public String getGovernanceMode() {
                return governanceMode;
            }

            public void setGovernanceMode(String governanceMode) {
                this.governanceMode = governanceMode;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public static class Reward {
            @JsonProperty("useGiniCoeff")
            private boolean useGiniCoeff;
            @JsonProperty("stakingUpdateInterval")
            private BigInteger stakingUpdateInterval;
            @JsonProperty("ratio")
            private String ratio;
            @JsonProperty("proposerUpdateInterval")
            private BigInteger proposerUpdateInterval;
            @JsonProperty("mintingAmount")
            private BigInteger mintingAmount;
            @JsonProperty("minimumStake")
            private BigInteger minimumStake;
            @JsonProperty("deferredTxFee")
            private boolean deferredTxFee;

            public boolean getUseGiniCoeff() {
                return useGiniCoeff;
            }

            public void setUseGiniCoeff(boolean useGiniCoeff) {
                this.useGiniCoeff = useGiniCoeff;
            }

            public BigInteger getStakingUpdateInterval() {
                return stakingUpdateInterval;
            }

            public void setStakingUpdateInterval(BigInteger stakingUpdateInterval) {
                this.stakingUpdateInterval = stakingUpdateInterval;
            }

            public String getRatio() {
                return ratio;
            }

            public void setRatio(String ratio) {
                this.ratio = ratio;
            }

            public BigInteger getProposerUpdateInterval() {
                return proposerUpdateInterval;
            }

            public void setProposerUpdateInterval(BigInteger proposerUpdateInterval) {
                this.proposerUpdateInterval = proposerUpdateInterval;
            }

            public BigInteger getMintingAmount() {
                return mintingAmount;
            }

            public void setMintingAmount(BigInteger mintingAmount) {
                this.mintingAmount = mintingAmount;
            }

            public BigInteger getMinimumStake() {
                return minimumStake;
            }

            public void setMinimumStake(BigInteger minimumStake) {
                this.minimumStake = minimumStake;
            }

            public boolean getDeferredTxFee() {
                return deferredTxFee;
            }

            public void setDeferredTxFee(boolean deferredTxFee) {
                this.deferredTxFee = deferredTxFee;
            }

            @Override
            public String toString() {
                return Utils.printString(this);
            }
        }

        public Istanbul getIstanbul() {
            return istanbul;
        }

        public Governance getGovernance() {
            return governance;
        }

        public int getDeriveshaimpl() {
            return deriveShaImpl;
        }

        public int getChainid() {
            return chainId;
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
