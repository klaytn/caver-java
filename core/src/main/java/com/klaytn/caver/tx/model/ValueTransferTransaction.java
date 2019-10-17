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

import com.klaytn.caver.tx.type.*;

import java.math.BigInteger;

public class ValueTransferTransaction extends TransactionTransformer<ValueTransferTransaction> {

    private String to;
    private BigInteger amount;
    private byte[] memo;
    private BigInteger feeRatio;

    private ValueTransferTransaction(String from, String to, BigInteger amount, BigInteger gasLimit) {
        super(from, gasLimit);
        this.to = to;
        this.amount = amount;
    }

    public static ValueTransferTransaction create(String from, String to, BigInteger amount, BigInteger gasLimit) {
        return new ValueTransferTransaction(from, to, amount, gasLimit);
    }

    public ValueTransferTransaction memo(String memo) {
        this.memo = memo.getBytes();
        return this;
    }

    public ValueTransferTransaction feeRatio(BigInteger feeRatio) {
        this.feeRatio = feeRatio;
        return this;
    }

    @Override
    public TxType build() {
        if (feeDelegate) {
            return buildFeeDelegated();
        }
        return buildWithoutFeeDelegated();
    }

    public TxType buildWithoutFeeDelegated() {
        if (this.memo != null) {
            return TxTypeValueTransferMemo.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to,
                    this.amount, getFrom(), this.memo);
        }
        return TxTypeValueTransfer.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to, this.amount, getFrom());
    }

    @Override
    public TxType buildFeeDelegated() {
        if (this.memo != null && this.feeRatio != null) {
            return TxTypeFeeDelegatedValueTransferMemoWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to,
                    this.amount, getFrom(), this.memo, this.feeRatio);
        }
        if (this.memo != null) {
            return TxTypeFeeDelegatedValueTransferMemo.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to,
                    this.amount, getFrom(), this.memo);
        }
        if (this.feeRatio != null) {
            return TxTypeFeeDelegatedValueTransferWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to,
                    this.amount, getFrom(), this.feeRatio);
        }
        return TxTypeFeeDelegatedValueTransfer.createTransaction(getNonce(), getGasPrice(), getGasLimit(), this.to,
                this.amount, getFrom());
    }

    public String getTo() {
        return to;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public byte[] getMemo() {
        return memo;
    }

    public BigInteger getFeeRatio() {
        return feeRatio;
    }
}
