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

import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.KlayTransactionUtils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * TxTypeFeeDelegatedSmartContractDeploy deploys a smart contract. The fee is paid by the fee payer.
 */
public class TxTypeFeeDelegatedSmartContractDeploy extends TxTypeFeeDelegate {

    /**
     * code of the newly deployed smart contract
     */
    private final byte[] payload;

    private final BigInteger codeFormat;

    public TxTypeFeeDelegatedSmartContractDeploy(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value,
            String from, byte[] payload, BigInteger codeFormat) {
        super(nonce, gasPrice, gasLimit, from, "", value);
        this.payload = payload;
        this.codeFormat = codeFormat;
    }

    public static TxTypeFeeDelegatedSmartContractDeploy createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
            BigInteger value, String from, byte[] payload, BigInteger codeFormat) {
        return new TxTypeFeeDelegatedSmartContractDeploy(nonce, gasPrice, gasLimit, value, from, payload, codeFormat);
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
     * This method is overridden as FEE_DELEGATED_SMART_CONTRACT_DEPLOY type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY;
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedSmartContractDeploy decoded transaction
     */
    public static TxTypeFeeDelegatedSmartContractDeploy decodeFromRawTransaction(byte[] rawTransaction) {
        byte[] rawTransactionExceptType = KlayTransactionUtils.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(rawTransactionExceptType);
        RlpList values = (RlpList) rlpList.getValues().get(0);
        BigInteger nonce = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        BigInteger gasLimit = ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        String to = ((RlpString) values.getValues().get(3)).asString();
        BigInteger value = ((RlpString) values.getValues().get(4)).asPositiveBigInteger();
        String from = ((RlpString) values.getValues().get(5)).asString();
        byte[] payload = ((RlpString) values.getValues().get(6)).getBytes();
        BigInteger codeFormat = ((RlpString) values.getValues().get(8)).asPositiveBigInteger();

        TxTypeFeeDelegatedSmartContractDeploy tx
                = new TxTypeFeeDelegatedSmartContractDeploy(nonce, gasPrice, gasLimit, value, from, payload, codeFormat);
        if (values.getValues().size() > 8) {
            RlpList vrs = (RlpList) ((RlpList) (values.getValues().get(9))).getValues().get(0);
            byte[] v = ((RlpString) vrs.getValues().get(0)).getBytes();
            byte[] r = ((RlpString) vrs.getValues().get(1)).getBytes();
            byte[] s = ((RlpString) vrs.getValues().get(2)).getBytes();
            tx.setSenderSignatureData(new KlaySignatureData(v, r, s));
        }
        return tx;
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedSmartContractDeploy decoded transaction
     */
    public static TxTypeFeeDelegatedSmartContractDeploy decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }
}
