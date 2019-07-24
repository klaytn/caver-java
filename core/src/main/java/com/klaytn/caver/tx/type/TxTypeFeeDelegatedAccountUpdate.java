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
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.account.AccountKeyDecoder;
import com.klaytn.caver.utils.KlayTransactionUtils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * TxTypeFeeDelegatedAccountUpdate updates the key of the account. The transaction fee is paid by the fee payer
 */
public class TxTypeFeeDelegatedAccountUpdate extends AbstractTxType implements TxTypeFeeDelegate {

    /**
     * newly created accountKey
     */
    private final AccountKey accountKey;

    public TxTypeFeeDelegatedAccountUpdate(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String from, AccountKey accountKey) {
        super(nonce, gasPrice, gasLimit, from, "", BigInteger.ZERO);
        this.accountKey = accountKey;
    }

    public static TxTypeFeeDelegatedAccountUpdate createTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String from, AccountKey accountKey) {
        return new TxTypeFeeDelegatedAccountUpdate(nonce, gasPrice, gasLimit, from, accountKey);
    }

    public AccountKey getAccountKey() {
        return accountKey;
    }

    /**
     * create RlpType List which contains nonce, gas price, gas limit, from and accountKey.
     * List elements can be different depending on transaction type.
     *
     * @return List RlpType List
     */
    @Override
    public List<RlpType> rlpValues() {
        List<RlpType> values = super.rlpValues();
        values.add(RlpString.create(Numeric.hexStringToByteArray(getFrom())));
        values.add(RlpString.create(getAccountKey().toRlp()));
        return values;
    }

    /**
     * This method is overridden as FEE_DELEGATED_ACCOUNT_UPDATE type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.FEE_DELEGATED_ACCOUNT_UPDATE;
    }

    /**
     * decode transaction hash from sender to reconstruct transaction with fee payer signature.
     *
     * @param rawTransaction signed transaction hash from sender
     * @return TxTypeFeeDelegatedAccountUpdate decoded transaction
     */
    public static TxTypeFeeDelegatedAccountUpdate decodeFromRawTransaction(byte[] rawTransaction) {
        byte[] rawTransactionExceptType = KlayTransactionUtils.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(rawTransactionExceptType);
        RlpList values = (RlpList) rlpList.getValues().get(0);

        BigInteger nonce = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        BigInteger gasLimit = ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        String from = ((RlpString) values.getValues().get(3)).asString();
        String accountkeyRaw = ((RlpString) values.getValues().get(4)).asString();

        TxTypeFeeDelegatedAccountUpdate tx
                = TxTypeFeeDelegatedAccountUpdate.createTransaction(nonce, gasPrice, gasLimit, from, AccountKeyDecoder.fromRlp(accountkeyRaw));
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
     * @param rawTransaction signed transaction hash from sender
     * @return TxTypeFeeDelegatedAccountUpdate decoded transaction
     */
    public static TxTypeFeeDelegatedAccountUpdate decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }
}
