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

package com.klaytn.caver.tx.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AccountKeyWeightedMultiSig is an account key type containing a threshold and WeightedPublicKeys.
 * WeightedPublicKeys contains a slice of {weight and key}. To be a valid tx for an account associated
 * with AccountKeyWeightedMultiSig, the weighted sum of signed public keys should be larger than the threshold.
 */
public class AccountKeyWeightedMultiSig implements AccountKey {

    /**
     * Validation threshold. To be a valid transaction, the weight sum of signatures should be larger than
     * or equal to the threshold.
     */
    private BigInteger threshold;

    /**
     * A slice of weighted public keys. A weighted public key contains a weight and a public key.
     */
    private List<WeightedPublicKey> weightedPublicKeys = new ArrayList<>();

    public static AccountKeyWeightedMultiSig create(BigInteger threshold, List<WeightedPublicKey> weightedPublicKeys) {
        return new AccountKeyWeightedMultiSig(threshold, weightedPublicKeys);
    }

    public AccountKeyWeightedMultiSig() {
    }

    public AccountKeyWeightedMultiSig(BigInteger threshold, List<WeightedPublicKey> weightedPublicKeys) {
        this.threshold = threshold;
        this.weightedPublicKeys.addAll(weightedPublicKeys);
    }

    public BigInteger getThreshold() {
        return threshold;
    }

    @JsonProperty("keys")
    public List<WeightedPublicKey> getWeightedPublicKeys() {
        return weightedPublicKeys;
    }

    @Override
    public byte[] toRlp() {
        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(threshold));

        List<RlpType> rlpWeightedPublicKeys = new ArrayList<>();
        for (WeightedPublicKey item : this.weightedPublicKeys) {
            List<RlpType> rlpWeightedPublicKey = new ArrayList<>();

            rlpWeightedPublicKey.addAll(Arrays.asList(
                    RlpString.create(item.weight),
                    RlpString.create(Numeric.hexStringToByteArray(item.key.toCompressedPublicKey()))));

            rlpWeightedPublicKeys.add(new RlpList(rlpWeightedPublicKey));
        }
        rlpTypeList.add(new RlpList(rlpWeightedPublicKeys));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = {getType().getValue()};
        return BytesUtils.concat(type, encodedTransaction);
    }

    public static AccountKeyWeightedMultiSig decodeFromRlp(byte[] rawTransaction) {
        byte[] transaction = AccountKeyDecoder.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(transaction);
        RlpList values = (RlpList) rlpList.getValues().get(0);

        BigInteger threshold = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();

        List<WeightedPublicKey> weightedPublicKeys = new ArrayList<>();
        RlpList rlpWeightedPublicKeys = (RlpList) values.getValues().get(1);
        for (RlpType item : rlpWeightedPublicKeys.getValues()) {
            RlpList rlpWeightedPublicKey = (RlpList) item;
            BigInteger weight = ((RlpString) rlpWeightedPublicKey.getValues().get(0)).asPositiveBigInteger();
            String compressedPublicKey = ((RlpString) rlpWeightedPublicKey.getValues().get(1)).asString();
            weightedPublicKeys.add(new WeightedPublicKey(weight, AccountKeyPublicUtils.decompressKey(compressedPublicKey)));
        }

        return new AccountKeyWeightedMultiSig(threshold, weightedPublicKeys);
    }

    public static AccountKeyWeightedMultiSig decodeFromRlp(String hexString) {
        return decodeFromRlp(Numeric.hexStringToByteArray(hexString));
    }

    @Override
    public Type getType() {
        return Type.MULTISIG;
    }

    public static class WeightedPublicKey {
        private BigInteger weight;
        private AccountKeyPublic key;

        public WeightedPublicKey() {
        }

        protected WeightedPublicKey(BigInteger weight, AccountKeyPublic key) {
            this.weight = weight;
            this.key = key;
        }

        public static WeightedPublicKey create(BigInteger weight, AccountKeyPublic accountKeyPublic) {
            return new WeightedPublicKey(weight, accountKeyPublic);
        }

        public BigInteger getWeight() {
            return weight;
        }

        public AccountKeyPublic getKey() {
            return key;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKeyWeightedMultiSig that = (AccountKeyWeightedMultiSig) o;
        return Arrays.equals(toRlp(), that.toRlp());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("AccountKeyWeightedMultiSig : {\n");
        for (WeightedPublicKey weightedPublicKey : weightedPublicKeys) {
            result.append("{Weight : " + weightedPublicKey.weight + " / ");
            result.append("PublicKey : " + weightedPublicKey.key.toString() + "}\n ");
        }
        result.append("}");
        return result.toString();
    }
}
