/*
 * Copyright 2019 The caver-java Authors
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

import java.util.Optional;

public class KlaySignTransaction extends Response<KlaySignTransaction.SignTransaction> {

    public Optional<SignTransaction> getSignTransaction() {
        return Optional.ofNullable(getResult());
    }

    public static class SignTransaction {
        private String raw;
        private SignTransactionResult tx;

        public SignTransaction() {
        }

        public SignTransaction(String raw, SignTransactionResult tx) {
            this.raw = raw;
            this.tx = tx;
        }

        public String getRaw() {
            return raw;
        }

        public SignTransactionResult getTx() {
            return tx;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SignTransaction)) {
                return false;
            }

            SignTransaction that = (SignTransaction) o;

            if (getRaw() != null
                    ? !getRaw().equals(that.getRaw()) : that.getRaw() != null) {
                return false;
            }
            return getTx() != null ? getTx().equals(that.getTx()) : that.getTx() == null;
        }

        @Override
        public int hashCode() {
            int result = getRaw() != null ? getRaw().hashCode() : 0;
            result = 31 * result + (getTx() != null ? getTx().hashCode() : 0);
            return result;
        }

        public static class SignTransactionResult {
            private String nonce;
            private String gasPrice;
            private String gas;
            private String to;
            private String value;
            private String input;
            private String feePayer;
            private String feeRatio;
            private String v;
            private String r;
            private String s;
            private String hash;

            public SignTransactionResult(String nonce, String gasPrice, String gas, String to, String value, String input, String v, String r, String s, String hash) {
                this.nonce = nonce;
                this.gasPrice = gasPrice;
                this.gas = gas;
                this.to = to;
                this.value = value;
                this.input = input;
                this.v = v;
                this.r = r;
                this.s = s;
                this.hash = hash;
            }

            public SignTransactionResult(String nonce, String gasPrice, String gas, String to, String value, String input, String feePayer, String feeRatio, String v, String r, String s, String hash) {
                this.nonce = nonce;
                this.gasPrice = gasPrice;
                this.gas = gas;
                this.to = to;
                this.value = value;
                this.input = input;
                this.feePayer = feePayer;
                this.feeRatio = feeRatio;
                this.v = v;
                this.r = r;
                this.s = s;
                this.hash = hash;
            }

            public SignTransactionResult() {
            }

            public String getNonce() {
                return nonce;
            }

            public String getGasPrice() {
                return gasPrice;
            }

            public String getGas() {
                return gas;
            }

            public String getTo() {
                return to;
            }

            public String getValue() {
                return value;
            }

            public String getInput() {
                return input;
            }

            public String getV() {
                return v;
            }

            public String getR() {
                return r;
            }

            public String getS() {
                return s;
            }

            public String getHash() {
                return hash;
            }

            public String getFeePayer() {
                return feePayer;
            }

            public String getFeeRatio() {
                return feeRatio;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof SignTransactionResult)) {
                    return false;
                }

                SignTransactionResult that = (SignTransactionResult) o;

                if (getNonce() != null
                        ? !getNonce().equals(that.getNonce()) : that.getNonce() != null) {
                    return false;
                }
                if (getGasPrice() != null
                        ? !getGasPrice().equals(that.getGasPrice()) : that.getGasPrice() != null) {
                    return false;
                }
                if (getGas() != null
                        ? !getGas().equals(that.getGas()) : that.getGas() != null) {
                    return false;
                }
                if (getTo() != null
                        ? !getTo().equals(that.getTo()) : that.getTo() != null) {
                    return false;
                }
                if (getValue() != null
                        ? !getValue().equals(that.getValue()) : that.getValue() != null) {
                    return false;
                }
                if (getInput() != null
                        ? !getInput().equals(that.getInput()) : that.getInput() != null) {
                    return false;
                }
                if (getV() != null
                        ? !getV().equals(that.getV()) : that.getV() != null) {
                    return false;
                }
                if (getR() != null
                        ? !getR().equals(that.getR()) : that.getR() != null) {
                    return false;
                }
                if (getS() != null
                        ? !getS().equals(that.getS()) : that.getS() != null) {
                    return false;
                }
                return getHash() != null ? getHash().equals(that.getHash()) : that.getHash() == null;
            }

            @Override
            public int hashCode() {
                int result = getNonce() != null ? getNonce().hashCode() : 0;
                result = 31 * result + (getGasPrice() != null ? getGasPrice().hashCode() : 0);
                result = 31 * result + (getGas() != null ? getGas().hashCode() : 0);
                result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
                result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
                result = 31 * result + (getInput() != null ? getInput().hashCode() : 0);
                result = 31 * result + (getV() != null ? getV().hashCode() : 0);
                result = 31 * result + (getR() != null ? getR().hashCode() : 0);
                result = 31 * result + (getS() != null ? getS().hashCode() : 0);
                result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
                return result;
            }
        }

    }
}
