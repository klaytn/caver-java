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
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecution;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecutionWithRatio;
import com.klaytn.caver.tx.type.TxTypeSmartContractExecution;

import java.math.BigInteger;

public class SmartContractExecutionTransaction extends TransactionTransformer<SmartContractExecutionTransaction> {

    private String recipient;
    private BigInteger amount;
    private byte[] payload;
    private BigInteger feeRatio;

    private SmartContractExecutionTransaction(String from, String recipient, BigInteger amount,
                                              byte[] payload, BigInteger gasLimit) {
        super(from, gasLimit);
        this.recipient = recipient;
        this.amount = amount;
        this.payload = payload;
    }

    public static SmartContractExecutionTransaction create(String from, String recipient, BigInteger amount,
                                                           byte[] payload, BigInteger gasLimit) {
        return new SmartContractExecutionTransaction(from, recipient, amount, payload, gasLimit);
    }

    public SmartContractExecutionTransaction feeRatio(BigInteger feeRatio) {
        this.feeRatio = feeRatio;
        return this;
    }

    @Override
    public TxType build() {
        if (this.feeDelegate) {
            return buildFeeDelegated();
        }
        return TxTypeSmartContractExecution.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                this.recipient, this.amount, getFrom(), this.payload);
    }

    @Override
    public TxType buildFeeDelegated() {
        if (this.feeRatio != null) {
            return TxTypeFeeDelegatedSmartContractExecutionWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                    this.recipient, this.amount, getFrom(), this.payload, this.feeRatio);
        }
        return TxTypeFeeDelegatedSmartContractExecution.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                this.recipient, this.amount, getFrom(), this.payload);
    }
}
