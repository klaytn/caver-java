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
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractDeploy;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractDeployWithRatio;
import com.klaytn.caver.tx.type.TxTypeSmartContractDeploy;

import java.math.BigInteger;

public class SmartContractDeployTransaction extends TransactionTransformer<SmartContractDeployTransaction> {

    private BigInteger amount;
    private byte[] payload;
    private BigInteger codeFormat;
    private BigInteger feeRatio;

    private SmartContractDeployTransaction(String from, BigInteger amount, byte[] payload, BigInteger gasLimit, BigInteger codeFormat) {
        super(from, gasLimit);
        this.amount = amount;
        this.payload = payload;
        this.codeFormat = codeFormat;
    }

    public static SmartContractDeployTransaction create(
            String from, BigInteger amount, byte[] payload, BigInteger gasLimit, BigInteger codeFormat) {
        return new SmartContractDeployTransaction(from, amount, payload, gasLimit, codeFormat);
    }

    public SmartContractDeployTransaction feeRatio(BigInteger feeRatio) {
        this.feeRatio = feeRatio;
        return this;
    }

    @Override
    public TxType build() {
        if (this.feeDelegate) {
            return buildFeeDelegated();
        }
        return TxTypeSmartContractDeploy.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                this.amount, getFrom(), this.payload, this.codeFormat);
    }

    @Override
    public TxType buildFeeDelegated() {
        if (this.feeRatio != null) {
            return TxTypeFeeDelegatedSmartContractDeployWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                    this.amount, getFrom(), this.payload, this.feeRatio, this.codeFormat);
        }
        return TxTypeFeeDelegatedSmartContractDeploy.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                this.amount, getFrom(), this.payload, this.codeFormat);
    }
}
