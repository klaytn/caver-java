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

package com.klaytn.caver.tx.model;

import com.klaytn.caver.tx.exception.UnsupportedTxTypeException;
import com.klaytn.caver.tx.type.TxType;
import com.klaytn.caver.utils.Convert;

import java.math.BigInteger;

public abstract class TransactionTransformer<T extends TransactionTransformer> {

    private BigInteger nonce;
    private String from;
    private BigInteger gasPrice = Convert.toPeb("25", Convert.Unit.STON).toBigInteger();
    private BigInteger gasLimit;
    protected boolean feeDelegate;

    public T from(String from) {
        this.from = from;
        return (T) this;
    }

    public T nonce(BigInteger nonce) {
        this.nonce = nonce;
        return (T) this;
    }

    public T gasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
        return (T) this;
    }

    public T gasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return (T) this;
    }

    public T feeDelegate() {
        this.feeDelegate = true;
        return (T) this;
    }

    public TransactionTransformer(String from, BigInteger gasLimit) {
        this.from = from;
        this.gasLimit = gasLimit;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public String getFrom() {
        return from;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public abstract TxType build() throws UnsupportedTxTypeException;

    public abstract TxType buildFeeDelegated() throws UnsupportedTxTypeException;

    public TxType build(boolean isFeeDelegated) throws UnsupportedTxTypeException {
        return isFeeDelegated ? buildFeeDelegated() : build();
    }
}
