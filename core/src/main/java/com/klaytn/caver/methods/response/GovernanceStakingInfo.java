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
import java.util.List;

/**
 * The class represented to store the response data of the {@link Governance#getStakingInfo()} method.
 */
public class GovernanceStakingInfo extends Response<GovernanceStakingInfo.StakingInfo> {
    public static class StakingInfo {

        /**
         * The boolean value whether or not the Gini coefficient is used.
         */
        @JsonProperty("UseGini")
        private boolean useGini;

        /**
         * The contract address of PoC.
         */
        @JsonProperty("PoCAddr")
        private String pocAddr;

        /**
         * The contract address of KIR.
         */
        @JsonProperty("KIRAddr")
        private String kirAddr;

        /**
         * Gini coefficient.
         */
        @JsonProperty("Gini")
        private int gini;

        /**
         * The amount of KLAY which the associated nodes stake.
         */
        @JsonProperty("CouncilStakingAmounts")
        private List<BigInteger> councilStakingAmounts;

        /**
         * The contract addresses in which the associated nodes deploy for staking.
         */
        @JsonProperty("CouncilStakingAddrs")
        private List<String> councilStakingAddrs;

        /**
         * The addresses to which the block reward of the associated nodes is sent.
         */
        @JsonProperty("CouncilRewardAddrs")
        private List<String> councilRewardAddrs;

        /**
         * The addresses of the consensus node.
         */
        @JsonProperty("CouncilNodeAddrs")
        private List<String> councilNodeAddrs;

        @JsonProperty("BlockNum")
        private BigInteger blockNum;

        public StakingInfo() {
        }

        public StakingInfo(boolean useGini, String pocAddr, String kirAddr, int gini, List<BigInteger> councilStakingAmounts, List<String> councilStakingAddrs, List<String> councilRewardAddrs, List<String> councilModeAddrs, BigInteger blockNum) {
            this.useGini = useGini;
            this.pocAddr = pocAddr;
            this.kirAddr = kirAddr;
            this.gini = gini;
            this.councilStakingAmounts = councilStakingAmounts;
            this.councilStakingAddrs = councilStakingAddrs;
            this.councilRewardAddrs = councilRewardAddrs;
            this.councilNodeAddrs = councilModeAddrs;
            this.blockNum = blockNum;
        }

        /**
         * Getter function for useGini
         * @return boolean
         */
        public boolean isUseGini() {
            return useGini;
        }

        /**
         * Setter function for useGini.
         * @param useGini The boolean value whether or not the Gini coefficient is used.
         */
        public void setUseGini(boolean useGini) {
            this.useGini = useGini;
        }

        /**
         * Getter function for PocAddr.
         * @return String
         */
        public String getPocAddr() {
            return pocAddr;
        }

        /**
         * Setter function for PocAddr.
         * @param pocAddr The contract address of PoC.
         */
        public void setPocAddr(String pocAddr) {
            this.pocAddr = pocAddr;
        }

        /**
         * Getter function for KIRAddr.
         * @return String
         */
        public String getKirAddr() {
            return kirAddr;
        }

        /**
         * Setter function for KIRAddr
         * @param kirAddr The contract address of KIR.
         */
        public void setKirAddr(String kirAddr) {
            this.kirAddr = kirAddr;
        }

        /**
         * Getter function for Gini
         * @return int
         */
        public int getGini() {
            return gini;
        }

        /**
         * Setter function for Gini
         * @param gini The Gini coefficient.
         */
        public void setGini(int gini) {
            this.gini = gini;
        }

        /**
         * Getter function for councilStakingAmounts
         * @return List&lt;BigInteger&gt;
         */
        public List<BigInteger> getCouncilStakingAmounts() {
            return councilStakingAmounts;
        }

        /**
         * Setter function for councilStakingAmounts
         * @param councilStakingAmounts The amount of KLAY which the associated nodes stake.
         */
        public void setCouncilStakingAmounts(List<BigInteger> councilStakingAmounts) {
            this.councilStakingAmounts = councilStakingAmounts;
        }

        /**
         * Getter function for councilStakingAddrs.
         * @return List&lt;String&gt;
         */
        public List<String> getCouncilStakingAddrs() {
            return councilStakingAddrs;
        }

        /**
         * Setter function for councilStakingAddrs
         * @param councilStakingAddrs The contract addresses in which the associated nodes deploy for staking.
         */
        public void setCouncilStakingAddrs(List<String> councilStakingAddrs) {
            this.councilStakingAddrs = councilStakingAddrs;
        }

        /**
         * Getter function for councilRewardAddrs
         * @return List&lt;String&gt;
         */
        public List<String> getCouncilRewardAddrs() {
            return councilRewardAddrs;
        }

        /**
         * Setter function for councilRewardAddrs
         * @param councilRewardAddrs List&lt;String&gt;
         */
        public void setCouncilRewardAddrs(List<String> councilRewardAddrs) {
            this.councilRewardAddrs = councilRewardAddrs;
        }

        /**
         * Getter function for councilNodeAddrs
         * @return List&lt;String&gt;
         */
        public List<String> getCouncilNodeAddrs() {
            return councilNodeAddrs;
        }

        /**
         * Setter function for councilNodeAddrs
         * @param councilNodeAddrs List&lt;String&gt;
         */
        public void setCouncilNodeAddrs(List<String> councilNodeAddrs) {
            this.councilNodeAddrs = councilNodeAddrs;
        }

        /**
         * Getter function for blockNum
         * @return BigInteger
         */
        public BigInteger getBlockNum() {
            return blockNum;
        }

        /**
         * Setter function for blockNum
         * @param blockNum The block number at which the staking information is given.
         */
        public void setBlockNum(BigInteger blockNum) {
            this.blockNum = blockNum;
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
