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
import com.klaytn.caver.crypto.KlayCredentials;
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

    public static Account create(Caver caver, KlayCredentials klayCredentials, int chainId) {
        TransactionManager transactionManager = new TransactionManager.Builder(caver, klayCredentials)
                .setChaindId(chainId)
                .build();

        return Account.create(caver, transactionManager);
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed. </p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendUpdateTransaction(AccountUpdateTransaction)} instead.
     *
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendUpdateTransaction(
            Caver caver, KlayCredentials credentials, AccountUpdateTransaction transaction) {

        return Account.sendUpdateTransaction(caver, credentials, transaction, null);
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed.</p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendUpdateTransaction(AccountUpdateTransaction)} instead.
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendUpdateTransaction(
            Caver caver, KlayCredentials credentials, AccountUpdateTransaction transaction, ErrorHandler errorHandler) {
        TransactionManager transactionManager = new TransactionManager.Builder(caver, credentials)
                .setErrorHandler(errorHandler)
                .build();

        return new RemoteCall<>(() -> new Account(caver, transactionManager).send(transaction));
    }
}
