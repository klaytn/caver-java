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
 * @deprecated Please use {@link com.klaytn.caver.transaction.type.ChainDataAnchoring} instead.
 */
@Deprecated
public class TxTypeChainDataAnchoringTransaction extends AbstractTxType {

    /**
     * data of the child chain
     */
    private final byte[] anchoredData;

    protected TxTypeChainDataAnchoringTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String from, byte[] anchoredData) {
        super(nonce, gasPrice, gasLimit, from, "", BigInteger.ZERO);
        this.anchoredData = anchoredData;
    }

    public static TxTypeChainDataAnchoringTransaction createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String from, byte[] anchoredData) {
        return new TxTypeChainDataAnchoringTransaction(nonce, gasPrice, gasLimit, from, anchoredData);
    }

    public byte[] getAnchoredData() {
        return anchoredData;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, to, value, from and anchoredData.
     * List elements can be different depending on transaction type.
     *
     * @return List RlpType List
     */
    @Override
    public List<RlpType> rlpValues() {
        List<RlpType> values = super.rlpValues();
        values.add(RlpString.create(Numeric.hexStringToByteArray(getFrom())));
        values.add(RlpString.create(getAnchoredData()));
        return values;
    }

    /**
     * This method is overridden as CHAIN_DATA_ANCHORING type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.CHAIN_DATA_ANCHORING;
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeChainDataAnchoringTransaction decoded transaction
     */
    public static TxTypeChainDataAnchoringTransaction decodeFromRawTransaction(byte[] rawTransaction) {
        byte[] rawTransactionExceptType = KlayTransactionUtils.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(rawTransactionExceptType);
        RlpList values = (RlpList) rlpList.getValues().get(0);
        BigInteger nonce = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        BigInteger gasLimit = ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        String from = ((RlpString) values.getValues().get(3)).asString();
        byte[] payload = ((RlpString) values.getValues().get(4)).getBytes();

        TxTypeChainDataAnchoringTransaction tx
                = new TxTypeChainDataAnchoringTransaction(nonce, gasPrice, gasLimit, from, payload);
        if (values.getValues().size() > 4) {
            RlpList vrs = (RlpList) ((RlpList) (values.getValues().get(5))).getValues().get(0);
            byte[] v = ((RlpString) vrs.getValues().get(0)).getBytes();
            byte[] r = ((RlpString) vrs.getValues().get(1)).getBytes();
            byte[] s = ((RlpString) vrs.getValues().get(2)).getBytes();
            tx.setSenderSignatureData(new KlaySignatureData(v, r, s));
        }
        return tx;
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction from sender
     * @return TxTypeChainDataAnchoringTransaction decoded transaction
     */
    public static TxTypeChainDataAnchoringTransaction decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }

}
