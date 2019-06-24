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

import java.math.BigInteger;

public class KlayPeerCount extends Response<KlayPeerCount.PeerCount> {

    public static class PeerCount {
        private BigInteger total;
        private BigInteger cn;
        private BigInteger pn;
        private BigInteger en;

        public PeerCount() {
        }

        public PeerCount(BigInteger total, BigInteger cn, BigInteger pn, BigInteger en) {
            this.total = total;
            this.cn = cn;
            this.pn = pn;
            this.en = en;
        }

        public BigInteger getTotal() {
            return total;
        }

        public BigInteger getCn() {
            return cn;
        }

        public BigInteger getPn() {
            return pn;
        }

        public BigInteger getEn() {
            return en;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof KlayPeerCount.PeerCount)) {
                return false;
            }

            KlayPeerCount.PeerCount that = (KlayPeerCount.PeerCount) o;

            if (getTotal() != null
                    ? !getTotal().equals(that.getTotal()) : that.getTotal() != null) {
                return false;
            }
            if (getCn() != null
                    ? !getCn().equals(that.getCn()) : that.getCn() != null) {
                return false;
            }
            if (getPn() != null ? !getPn().equals(that.getPn()) : that.getPn() != null) {
                return false;
            }
            return getEn() != null ? getEn().equals(that.getEn()) : that.getEn() == null;
        }

        @Override
        public int hashCode() {
            int result = getTotal() != null ? getTotal().hashCode() : 0;
            result = 31 * result + (getCn() != null ? getCn().hashCode() : 0);
            result = 31 * result + (getPn() != null ? getPn().hashCode() : 0);
            result = 31 * result + (getEn() != null ? getEn().hashCode() : 0);
            return result;
        }
    }
}
