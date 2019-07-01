/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/Transfer.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.tx;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.ErrorHandler;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.AccountUpdateTransaction;
import org.web3j.protocol.core.RemoteCall;

public class Account extends ManagedTransaction {

    private Account(Caver caver, TransactionManager transactionManager) {
        super(caver, transactionManager);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendUpdateTransaction(AccountUpdateTransaction transaction) {
        return new RemoteCall<>(() -> send(transaction));
    }

    public static Account create(Caver caver, TransactionManager transactionManager) {
        return new Account(caver, transactionManager);
    }

    public static Account create(Caver caver, KlayCredentials klayCredentials) {
        TransactionManager transactionManager = new TransactionManager.Builder(caver, klayCredentials)
                .build();

        return Account.create(caver, transactionManager);
    }
}
