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
        private String Minted; 
        /**
         * total tx fee spent
         */
        @JsonProperty("totalFee")
        private String TotalFee;
        /**
         * the amount burnt
         */
        @JsonProperty("burntFee")
        private String BurntFee;
        /**
         * the amount allocated to the block proposer
         */
        @JsonProperty("proposer")
        private String Proposer;
        /**
         * total amount allocated to stakers
         */
        @JsonProperty("stakers")
        private String Stakers;
        /**
         * the amount allocated to KGF
         */
        @JsonProperty("kgf")
        private String Kgf;
        /**
         * the amount allocated to KIR
         */
        @JsonProperty("kir")
        private String Kir;    
        /**
         * mapping from reward recipient to amounts
         */
        @JsonProperty("rewards")
        private Map<String,String> Rewards;

        public BlockRewards() {}
        public BlockRewards(String Minted, String TotalFee, String BurntFee, String Proposer, 
                       String Stakers, String Kgf, String Kir, Map<String,String> Rewards) {
            this.Minted = Minted;
            this.TotalFee = TotalFee;
            this.BurntFee = BurntFee;
            this.Proposer = Proposer;
            this.Stakers = Stakers;
            this.Kgf = Kgf;
            this.Rewards = Rewards;
        }
    
        public String Minted() {
            return this.Minted;
        }

        public String TotalFee() {
            return this.TotalFee;
        }
        public String BurntFee() {
            return this.BurntFee;
        }
        public String Proposer() {
            return this.Proposer;
        }
        public String Stakers() {
            return this.Stakers;
        }
        public String Kgf() {
            return this.Kgf;
        }
        public String Kir() {
            return this.Kir;
        }
        public Map<String,String> Rewards() {
            return this.Rewards;
        }
    }
}
