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

import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.type.TxType;
import com.klaytn.caver.tx.type.TxTypeAccountUpdate;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedAccountUpdate;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedAccountUpdateWithRatio;

import java.math.BigInteger;

public class AccountUpdateTransaction extends TransactionTransformer<AccountUpdateTransaction> {

    private AccountKey accountKey;
    private BigInteger feeRatio;

    private AccountUpdateTransaction(String from, AccountKey accountKey, BigInteger gasLimit) {
        super(from, gasLimit);
        this.accountKey = accountKey;
    }

    public static AccountUpdateTransaction create(String from, AccountKey accountKey, BigInteger gasLimit) {
        return new AccountUpdateTransaction(from, accountKey, gasLimit);
    }

    public AccountUpdateTransaction feeRatio(BigInteger feeRatio) {
        this.feeRatio = feeRatio;
        return this;
    }

    @Override
    public TxType build() {
        if (this.feeDelegate) {
            return buildFeeDelegated();
        }

        return TxTypeAccountUpdate.createTransaction(getNonce(), getGasPrice(), getGasLimit(), getFrom(), this.accountKey);
    }

    @Override
    public TxType buildFeeDelegated() {
        if (this.feeRatio != null) {
            return TxTypeFeeDelegatedAccountUpdateWithRatio.createTransaction(getNonce(), getGasPrice(), getGasLimit(),
                    getFrom(), this.accountKey, this.feeRatio);
        }
        return TxTypeFeeDelegatedAccountUpdate.createTransaction(getNonce(), getGasPrice(), getGasLimit(), getFrom(),
                this.accountKey);
    }
}
