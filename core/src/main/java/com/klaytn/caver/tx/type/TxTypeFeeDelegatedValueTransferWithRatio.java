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
 * TxTypeFeeDelegatedValueTransferWithRatio is a value transfer transaction with a fee payer and its ratio.
 */
public class TxTypeFeeDelegatedValueTransferWithRatio extends TxTypeFeeDelegate {

    /**
     * Fee ratio of the fee payer. If it is 30, 30% of the fee will be paid by the fee payer.
     * 70% will be paid by the sender.
     */
    private final BigInteger feeRatio;

    protected TxTypeFeeDelegatedValueTransferWithRatio(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String from, BigInteger feeRatio) {
        super(nonce, gasPrice, gasLimit, from, to, value);
        this.feeRatio = feeRatio;
    }

    public static TxTypeFeeDelegatedValueTransferWithRatio createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String from, BigInteger feeRatio) {
        return new TxTypeFeeDelegatedValueTransferWithRatio(nonce, gasPrice, gasLimit, to, value, from, feeRatio);
    }

    public BigInteger getFeeRatio() {
        return feeRatio;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value, from and feeRatio.
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
        values.add(RlpString.create(getFeeRatio()));
        return values;
    }

    /**
     * This method is overridden as FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO;
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedValueTransferWithRatio decoded transaction
     */
    public static TxTypeFeeDelegatedValueTransferWithRatio decodeFromRawTransaction(byte[] rawTransaction) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, to, value, from, feeRatio, txSignatures, feePayer, feePayerSignatures])
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
            BigInteger feeRatio = ((RlpString) values.get(6)).asPositiveBigInteger();
            TxTypeFeeDelegatedValueTransferWithRatio tx
                    = TxTypeFeeDelegatedValueTransferWithRatio.createTransaction(nonce, gasPrice, gasLimit, to, value, from, feeRatio);
            tx.addSignatureData(values, 7);
            return tx;
        } catch (Exception e) {
            throw new RuntimeException("There is a error in the processing of decoding tx");
        }
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeFeeDelegatedValueTransferWithRatio decoded transaction
     */
    public static TxTypeFeeDelegatedValueTransferWithRatio decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }
}
