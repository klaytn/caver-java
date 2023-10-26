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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.web3j.protocol.core.Response;

public class ForkStatusResult
  extends Response<ForkStatusResult.ForkStatusData> {

	public static class ForkStatusData {

		@JsonProperty("EthTxType")
		private Boolean EthTxType;

		@JsonProperty("Istanbul")
		private Boolean Istanbul;

		@JsonProperty("KIP103")
		private Boolean KIP103;

		@JsonProperty("Kore")
		private Boolean Kore;

		@JsonProperty("London")
		private Boolean London;

		@JsonProperty("Magma")
		private Boolean Magma;

		@JsonProperty("Mantle")
		private Boolean Mantle;

		public ForkStatusData() {}
		public ForkStatusData (
			Boolean EthTxType,
			Boolean Istanbul,
			Boolean KIP103,
			Boolean Kore,
			Boolean London,
			Boolean Magma,
			Boolean Mantle
		) {
			this.EthTxType = EthTxType;
			this.Istanbul = Istanbul;
			this.KIP103 = KIP103;
			this.Kore = Kore;
			this.London = London;
			this.Magma = Magma;
			this.Mantle = Mantle;
		}
	}
}
