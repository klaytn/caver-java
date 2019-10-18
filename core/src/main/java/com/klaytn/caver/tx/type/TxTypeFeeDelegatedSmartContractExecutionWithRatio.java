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
 * TxTypeFeeDelegatedSmartContractExecutionWithRatio executes a smart contract with the given data.
 * The given ratio of the transaction fee is paid by the fee payer.
 */
public class TxTypeFeeDelegatedSmartContractExecutionWithRatio extends TxTypeFeeDelegate {

    /**
     * input data of the smart contract
     */
    private final byte[] payload;

    /**
     * Fee ratio of the fee payer. If it is 30, 30% of the fee will be paid by the fee payer.
     * 70% will be paid by the sender.
     */
    private final BigInteger feeRatio;

    protected TxTypeFeeDelegatedSmartContractExecutionWithRatio(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String from, byte[] payload, BigInteger feeRatio) {
        super(nonce, gasPrice, gasLimit, from, to, value);
        this.payload = payload;
        this.feeRatio = feeRatio;
    }

    public static TxTypeFeeDelegatedSmartContractExecutionWithRatio createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String from, byte[] payload, BigInteger feeRatio) {
        return new TxTypeFeeDelegatedSmartContractExecutionWithRatio(nonce, gasPrice, gasLimit, to, value, from, payload, feeRatio);
    }

    public byte[] getPayload() {
        return payload;
    }

    public BigInteger getFeeRatio() {
        return feeRatio;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value, from, payload and feeRatio.
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
        values.add(RlpString.create(getFeeRatio()));
        return values;
    }

    /**
     * This method is overridden as FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public TxType.Type getType() {
        return TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO;
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedSmartContractExecutionWithRatio decoded transaction
     */
    public static TxTypeFeeDelegatedSmartContractExecutionWithRatio decodeFromRawTransaction(byte[] rawTransaction) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, input, txSignatures])
        try {
            byte[] rawTransactionExceptType = KlayTransactionUtils.getRawTransactionNoType(rawTransaction);

            RlpList rlpList = RlpDecoder.decode(rawTransactionExceptType);
            List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();
            BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
            BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
            BigInteger gasLimit = ((RlpString) values.get(2)).asPositiveBigInteger();
            String to = ((RlpString) values.get(3)).asString();
            BigInteger value = ((RlpString) values.get(4)).asPositiveBigInteger();
            String from = ((RlpString) values.get(5)).asString();
            byte[] payload = ((RlpString) values.get(6)).getBytes();
            BigInteger feeRatio = ((RlpString) values.get(7)).asPositiveBigInteger();

            TxTypeFeeDelegatedSmartContractExecutionWithRatio tx
                    = TxTypeFeeDelegatedSmartContractExecutionWithRatio.createTransaction(nonce, gasPrice, gasLimit, to, value, from, payload, feeRatio);
            tx.addSignatureData(values, 8);
            return tx;
        } catch (Exception e) {
            throw new RuntimeException("There is a error in the processing of decoding tx");
        }
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedSmartContractExecutionWithRatio decoded transaction
     */
    public static TxTypeFeeDelegatedSmartContractExecutionWithRatio decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }
}
