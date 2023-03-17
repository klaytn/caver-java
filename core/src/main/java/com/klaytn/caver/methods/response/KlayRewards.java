/*
 * Copyright 2022 The caver-java Authors
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

public class KlayRewards extends Response<KlayRewards.BlockRewards> {
    public static class BlockRewards {
        /**
         * the amount newly minted
         */
        @JsonProperty("minted")
        private String minted; 
        /**
         * total tx fee spent
         */
        @JsonProperty("totalFee")
        private String totalFee;
        /**
         * the amount burnt
         */
        @JsonProperty("burntFee")
        private String burntFee;
        /**
         * the amount allocated to the block proposer
         */
        @JsonProperty("proposer")
        private String proposer;
        /**
         * total amount allocated to stakers
         */
        @JsonProperty("stakers")
        private String stakers;
        /**
         * the amount allocated to KGF
         */
        @JsonProperty("kff")
        private String kff;
        /**
         * the amount allocated to KIR
         */
        @JsonProperty("kcf")
        private String kcf;    
        /**
         * mapping from reward recipient to amounts
         */
        @JsonProperty("rewards")
        private Map<String,String> rewards;

        public BlockRewards() {}
        public BlockRewards(String Minted, String TotalFee, String BurntFee, String Proposer, 
                       String Stakers, String Kff, String Kcf, Map<String,String> Rewards) {
            this.minted = Minted;
            this.totalFee = TotalFee;
            this.burntFee = BurntFee;
            this.proposer = Proposer;
            this.stakers = Stakers;
            this.kff = Kff;
            this.kcf = Kcf;
            this.rewards = Rewards;
        }
    
        public String getMinted() {
            return this.minted;
        }

        public String getTotalFee() {
            return this.totalFee;
        }
        public String getBurntFee() {
            return this.burntFee;
        }
        public String getProposer() {
            return this.proposer;
        }
        public String getStakers() {
            return this.stakers;
        }
        public String getKff() {
            return this.kff;
        }
        public String getKcf() {
            return this.kcf;
        }
        public Map<String,String> getRewards() {
            return this.rewards;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((minted == null) ? 0 : minted.hashCode());
            result = prime * result + ((totalFee == null) ? 0 : totalFee.hashCode());
            result = prime * result + ((burntFee == null) ? 0 : burntFee.hashCode());
            result = prime * result + ((proposer == null) ? 0 : proposer.hashCode());
            result = prime * result + ((stakers == null) ? 0 : stakers.hashCode());
            result = prime * result + ((kff == null) ? 0 : kff.hashCode());
            result = prime * result + ((kcf == null) ? 0 : kcf.hashCode());
            result = prime * result + ((rewards == null) ? 0 : rewards.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BlockRewards other = (BlockRewards) obj;
            if (minted == null) {
                if (other.minted != null)
                    return false;
            } else if (!minted.equals(other.minted))
                return false;
            if (totalFee == null) {
                if (other.totalFee != null)
                    return false;
            } else if (!totalFee.equals(other.totalFee))
                return false;
            if (burntFee == null) {
                if (other.burntFee != null)
                    return false;
            } else if (!burntFee.equals(other.burntFee))
                return false;
            if (proposer == null) {
                if (other.proposer != null)
                    return false;
            } else if (!proposer.equals(other.proposer))
                return false;
            if (stakers == null) {
                if (other.stakers != null)
                    return false;
            } else if (!stakers.equals(other.stakers))
                return false;
            if (kff == null) {
                if (other.kff != null)
                    return false;
            } else if (!kff.equals(other.kff))
                return false;
            if (kcf == null) {
                if (other.kcf != null)
                    return false;
            } else if (!kcf.equals(other.kcf))
                return false;
            if (rewards == null) {
                if (other.rewards != null)
                    return false;
            } else if (!rewards.equals(other.rewards))
                return false;
            return true;
        }
    }
}
