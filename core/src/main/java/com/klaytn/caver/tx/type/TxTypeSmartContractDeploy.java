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
 * TxTypeSmartContractDeploy deploys a smart contract to the given address.
 */
public class TxTypeSmartContractDeploy extends AbstractTxType {

    /**
     * code of the newly deployed smart contract
     */
    private final byte[] payload;

    /**
     * The code format of smart contract code
     */
    private final BigInteger codeFormat;

    public TxTypeSmartContractDeploy(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
            BigInteger value, String from, byte[] payload, BigInteger codeFormat) {
        super(nonce, gasPrice, gasLimit, from, "", value);
        this.payload = payload;
        this.codeFormat = codeFormat;
    }

    public static TxTypeSmartContractDeploy createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
            BigInteger value, String from, byte[] payload, BigInteger codeFormat) {
        return new TxTypeSmartContractDeploy(nonce, gasPrice, gasLimit, value, from, payload, codeFormat);
    }

    public byte[] getPayload() {
        return payload;
    }

    public BigInteger getCodeFormat() {
        return codeFormat;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value, from, payload and isHumanReadable.
     * List elements can be different depending on transaction type.
     *
     * @return List RlpType List
     */
    @Override
    public List<RlpType> rlpValues() {
        List<RlpType> values = super.rlpValues();
        values.add(RlpString.create(Numeric.hexStringToByteArray(getTo())));
        values.add(RlpString.create(getValue()));
        values.add(RlpString.create(Numeric.hexStringToByteArray(getFrom())));
        values.add(RlpString.create(getPayload()));
        values.add(RlpString.create(0x0));
        values.add(RlpString.create(getCodeFormat()));
        return values;
    }

    /**
     * This method is overridden as SMART_CONTRACT_DEPLOY type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.SMART_CONTRACT_DEPLOY;
    }
}
