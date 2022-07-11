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

package com.klaytn.caver.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayAccountKey;
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Transaction implements TestComponent {
    public static int DEFAULT_CHAIN_ID = 2019;

    private InnerTransaction tx;
    private String deployedAddress;
    private Expected expected;

    public InnerTransaction getTx() {
        return tx;
    }

    public String getDeployedAddress() {
        return deployedAddress;
    }

    public Expected getExpected() {
        return expected;
    }

    public void configure(KlayCredentials sender, KlayCredentials payer) {
        getTx().sender = sender;
        getTx().payer = payer;
    }

    public class InnerTransaction {
        private KlayCredentials sender;
        private KlayCredentials payer;

        private String type;
        private int chainId;
        private BigInteger nonce;
        private String from;
        private String to;
        private String humanReadable;
        private String value;
        private String gas;
        private String gasPrice;
        private String data;
        private String privateKey;
        private String feePayerPrivateKey;
        private String feePayer;
        private String feeRatio;
        private String v;
        private String r;
        private String s;
        private BigInteger codeFormat;
        private KlayAccountKey.AccountKeyValue accountKey;

        public KlayCredentials getSender() {
            return sender;
        }

        public KlayCredentials getPayer() {
            return payer;
        }

        public int getChainId() {
            if (chainId == 0) return DEFAULT_CHAIN_ID;
            return chainId;
        }

        public BigInteger getNonce() {
            return nonce;
        }

        public BigInteger getGasPrice() {
            if (gasPrice == null) return null;
            return Numeric.toBigInt(gasPrice);
        }

        public void setGasPrice(String gp) {
            gasPrice = gp;
        }

        public String getType() {
            return type;
        }

        public KlayCredentials getPrivateKey() {
            if (privateKey == null) return sender;
            return KlayCredentials.create(privateKey);
        }

        public KlayCredentials getFeePayerPrivateKey() {
            if (feePayer == null && feePayerPrivateKey == null) return payer;
            if (feePayer == null) return KlayCredentials.create(feePayerPrivateKey, payer.getAddress());
            if (feePayerPrivateKey == null) return KlayCredentials.create(payer.getEcKeyPair(), feePayer);
            return KlayCredentials.create(feePayerPrivateKey, feePayer);
        }

        public String getFeePayer() {
            return feePayer;
        }

        public BigInteger getFeeRatio() {
            return new BigDecimal(feeRatio).toBigInteger();
        }

        public String getFrom() {
            if (from == null) return sender.getAddress();
            return from;
        }

        public String getTo() {
            if (to == null) return "";
            return to;
        }

        public boolean isHumanReadable() {
            if (humanReadable != null) return Boolean.valueOf(humanReadable);
            if (to != null) return false; // FIXME
            return false;
        }

        public BigInteger getValue() {
            if (value == null) return BigInteger.ZERO;
            if (value.startsWith("0x")) return Numeric.toBigInt(value);
            return new BigDecimal(value).toBigInteger();
        }

        public BigInteger getGas() {
            return new BigDecimal(gas).toBigInteger();
        }

        public String getData() {
            if(data == null) return "";
            return data;
        }

        public byte[] getPayload() {
            if (data == null) return new byte[]{};
            return Numeric.hexStringToByteArray(data);
        }

        public BigInteger getCodeFormat() {
            if (codeFormat == null) return BigInteger.ZERO;
            return codeFormat;
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

        @JsonDeserialize(using = KlayAccountKey.AccountKeyDeserializer.class)
        public void setAccountKey(KlayAccountKey.AccountKeyValue accountKey) {
            this.accountKey = accountKey;
        }

        public AccountKey getAccountKey() {
            return accountKey.getKey();
        }
    }

    @Override
    public Type getType() {
        return Type.TRANSACTION;
    }

}
