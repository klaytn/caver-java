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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.core.Response;

import java.math.BigInteger;
import java.util.List;

public class GovernanceStackingInfo extends Response<GovernanceStackingInfo.StakingInfo> {
    public static class StakingInfo {
        @JsonProperty("UseGini")
        private boolean useGini;

        @JsonProperty("PoCAddr")
        private String pocAddr;

        @JsonProperty("KIRAddr")
        private String kirAddr;

        @JsonProperty("Gini")
        private int gini;

        @JsonProperty("CouncilStakingAmounts")
        private List<BigInteger> councilStakingAmounts;

        @JsonProperty("CouncilStakingAddrs")
        private List<String> councilStakingAddrs;

        @JsonProperty("CouncilRewardAddrs")
        private List<String> councilRewardAddrs;

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

        public boolean isUseGini() {
            return useGini;
        }

        public void setUseGini(boolean useGini) {
            this.useGini = useGini;
        }

        public String getPocAddr() {
            return pocAddr;
        }

        public void setPocAddr(String pocAddr) {
            this.pocAddr = pocAddr;
        }

        public String getKirAddr() {
            return kirAddr;
        }

        public void setKirAddr(String kirAddr) {
            this.kirAddr = kirAddr;
        }

        public int getGini() {
            return gini;
        }

        public void setGini(int gini) {
            this.gini = gini;
        }

        public List<BigInteger> getCouncilStakingAmounts() {
            return councilStakingAmounts;
        }

        public void setCouncilStakingAmounts(List<BigInteger> councilStakingAmounts) {
            this.councilStakingAmounts = councilStakingAmounts;
        }

        public List<String> getCouncilStakingAddrs() {
            return councilStakingAddrs;
        }

        public void setCouncilStakingAddrs(List<String> councilStakingAddrs) {
            this.councilStakingAddrs = councilStakingAddrs;
        }

        public List<String> getCouncilRewardAddrs() {
            return councilRewardAddrs;
        }

        public void setCouncilRewardAddrs(List<String> councilRewardAddrs) {
            this.councilRewardAddrs = councilRewardAddrs;
        }

        public List<String> getCouncilNodeAddrs() {
            return councilNodeAddrs;
        }

        public void setCouncilNodeAddrs(List<String> councilNodeAddrs) {
            this.councilNodeAddrs = councilNodeAddrs;
        }

        public BigInteger getBlockNum() {
            return blockNum;
        }

        public void setBlockNum(BigInteger blockNum) {
            this.blockNum = blockNum;
        }

        @Override
        public String toString() {
            return Utils.printString(this);
        }
    }
}
