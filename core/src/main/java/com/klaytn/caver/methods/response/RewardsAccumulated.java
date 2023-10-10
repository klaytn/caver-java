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

package com.klaytn.caver.methods.response;

import java.util.Map;

import org.web3j.protocol.core.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RewardsAccumulated extends Response<RewardsAccumulated.RewardsAccumulatedData> {
    public static class RewardsAccumulatedData {

        @JsonProperty("firstBlockTime")
        private String firstBlockTime;

        @JsonProperty("lastBlockTime")
        private String lastBlockTime;

        @JsonProperty("firstBlock")
        private String firstBlock;

        @JsonProperty("lastBlock")
        private String lastBlock;

        @JsonProperty("totalMinted")
        private String totalMinted;

        @JsonProperty("totalTxFee")
        private String totalTxFee;

        @JsonProperty("totalBurntTxFee")
        private String totalBurntTxFee;

        @JsonProperty("totalProposerRewards")
        private String totalProposerRewards;

        @JsonProperty("totalStakingRewards")
        private String totalStakingRewards;

        @JsonProperty("totalKFFRewards")
        private String totalKFFRewards;

        @JsonProperty("totalKCFRewards")
        private String totalKCFRewards;

        /**
         * mapping from reward recipient to amounts
         */
        @JsonProperty("rewards")
        private Map<String, String> rewards;

        public RewardsAccumulatedData() {}
        public RewardsAccumulatedData(
                String firstBlockTime,
                String lastBlockTime,
                String firstBlock,
                String lastBlock,
                String totalMinted,
                String totalTxFee,
                String totalBurntTxFee,
                String totalProposerRewards,
                String totalStakingRewards,
                String totalKFFRewards,
                String totalKCFRewards,
                Map<String, String> Rewards
        ) {
            this.firstBlockTime = firstBlockTime;
            this.lastBlockTime = lastBlockTime;
            this.firstBlock = firstBlock;
            this.lastBlock = lastBlock;
            this.totalMinted = totalMinted;
            this.totalTxFee = totalTxFee;
            this.totalBurntTxFee = totalBurntTxFee;
            this.totalProposerRewards = totalProposerRewards;
            this.totalStakingRewards = totalStakingRewards;
            this.totalKFFRewards = totalKFFRewards;
            this.totalKCFRewards = totalKCFRewards;
            this.rewards = rewards;
        }
    }
}