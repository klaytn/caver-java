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

package com.klaytn.caver.tx.type;

import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * ValueTransferTransaction transfers KLAY only.
 */
public class TxTypeValueTransfer extends AbstractTxType {

    protected TxTypeValueTransfer(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String from) {
        super(nonce, gasPrice, gasLimit, from, to, value);
    }

    public static TxTypeValueTransfer createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String from) {

        return new TxTypeValueTransfer(nonce, gasPrice, gasLimit, to, value, from);
    }

    /**
     * This method is overridden as VALUE_TRANSFER type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.VALUE_TRANSFER;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value and from.
     * List elements can be different depending on transaction type.
     *
     * @return List RlpType List
     */
    @Override
    public List<RlpType> rlpValues() {
        List<RlpType> result = super.rlpValues();
        result.add(RlpString.create(Numeric.hexStringToByteArray(getTo())));
        result.add(RlpString.create(getValue()));
        result.add(RlpString.create(Numeric.hexStringToByteArray(getFrom())));
        return result;
    }

}
