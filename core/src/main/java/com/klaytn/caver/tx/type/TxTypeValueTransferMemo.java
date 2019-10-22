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
 * TxTypeValueTransferMemo transfers KLAY with a memo.
 */
public class TxTypeValueTransferMemo extends AbstractTxType {

    /**
     * memo
     */
    private final byte[] payload;

    protected TxTypeValueTransferMemo(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String from, byte[] payload) {
        super(nonce, gasPrice, gasLimit, from, to, value);
        this.payload = payload;
    }

    public static TxTypeValueTransferMemo createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String from, byte[] payload) {
        return new TxTypeValueTransferMemo(nonce, gasPrice, gasLimit, to, value, from, payload);
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeValueTransferMemo decoded transaction
     */
    public static TxTypeValueTransferMemo decodeFromRawTransaction(byte[] rawTransaction) {
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

            TxTypeValueTransferMemo tx
                    = TxTypeValueTransferMemo.createTransaction(nonce, gasPrice, gasLimit, to, value, from, payload);
            tx.addSignatureData(values, 7);
            return tx;
        } catch (Exception e) {
            throw new RuntimeException("There is a error in the processing of decoding tx");
        }
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeValueTransferMemo decoded transaction
     */
    public static TxTypeValueTransferMemo decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }

    public byte[] getPayload() {
        return payload;
    }

    /**
     * This method is overridden as VALUE_TRANSFER_MEMO type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.VALUE_TRANSFER_MEMO;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value, from and payload.
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
        result.add(RlpString.create(getPayload()));
        return result;
    }
}
