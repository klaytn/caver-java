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

import com.klaytn.caver.tx.type.TxType;
import com.klaytn.caver.tx.type.TxTypeCancel;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedCancel;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedCancelWithRatio;

import java.math.BigInteger;

public class CancelTransaction extends TransactionTransformer<CancelTransaction> {

    private BigInteger feeRatio;

    private CancelTransaction(String from, BigInteger gasLimit) {
        super(from, gasLimit);
    }

    public static CancelTransaction create(String from, BigInteger gasLimit) {
        return new CancelTransaction(from, gasLimit);
    }

    public CancelTransaction feeRatio(BigInteger feeRatio) {
        this.feeRatio = feeRatio;
        return this;
    }

    @Override
    public TxType build() {
        if (this.feeDelegate) {
            return buildFeeDelegated();
        }
        return TxTypeCancel.createTransaction(getNonce(), getGasPrice(), getGasLimit(), getFrom());
    }

    @Override
    public TxType buildFeeDelegated() {
        if (this.feeRatio != null) {
            return TxTypeFeeDelegatedCancelWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                    getFrom(), this.feeRatio);
        }
        return TxTypeFeeDelegatedCancel.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                getFrom());
    }
}
