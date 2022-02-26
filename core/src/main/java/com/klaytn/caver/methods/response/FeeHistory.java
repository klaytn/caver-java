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

import org.web3j.protocol.core.Response;

import java.util.List;

public class FeeHistory extends Response<FeeHistory.FeeHistoryData> {

    @Override
    public void setResult(FeeHistory.FeeHistoryData result) {
        super.setResult(result);
    }

    public static class FeeHistoryData<T> {
        /**
         * Lowest number block of returned range.
         */
        private String oldestBlock;

        /**
         * An array of block base fees per gas. This includes the next block after the newest of the returned range, because this value can be derived from the newest block. <p>
         * Zeroes are returned for pre-EIP-1559 blocks.
         */
        private List<String> baseFeePerGas;

        /**
         * A two-dimensional array of effective priority fees per gas at the requested block percentiles.
         */
        private List<List<String>> reward;

        /**
         * An array of gasUsed/gasLimit in the block.
         */
        private List<java.lang.Number> gasUsedRatio;

        public String getOldestBlock() {
            return oldestBlock;
        }

        public void setOldestBlock(String oldestBlock) {
            this.oldestBlock = oldestBlock;
        }

        public List<String> getBaseFeePerGas() {
            return baseFeePerGas;
        }

        public void setBaseFeePerGas(List<String> baseFeePerGas) {
            this.baseFeePerGas = baseFeePerGas;
        }

        public List<List<String>> getReward() {
            return reward;
        }

        public void setReward(List<List<String>> reward) {
            this.reward = reward;
        }

        public List<java.lang.Number> getGasUsedRatio() {
            return gasUsedRatio;
        }

        public void setGasUsedRatio(List<java.lang.Number> gasUsedRatio) {
            this.gasUsedRatio = gasUsedRatio;
        }
    }
}
