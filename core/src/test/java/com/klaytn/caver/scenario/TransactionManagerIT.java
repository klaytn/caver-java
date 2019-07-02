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

package com.klaytn.caver.scenario;

import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.ValueTransferTransaction;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.WAYNE;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static junit.framework.TestCase.assertEquals;

public class TransactionManagerIT extends Scenario {

    static final String MEMO_VALUE = "this is TransactionManagerIT";

    @Test
    public void testTransactionManager() throws Exception {
        TransactionManager transactionManager
                = new TransactionManager.Builder(caver, BRANDON)
                .setChaindId(LOCAL_CHAIN_ID)
                /*
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public void exception(Exception exception) {

                    }
                })
                .setGetNonceProcessor(new FastGetNonceProcessor(caverj))
                .setTransactionReceiptProcessor(new QueuingTransactionReceiptProcessor(
                        caverj,
                        new Callback<KlayTransactionReceipt.TransactionReceipt>() {
                            @Override
                            public void accept(KlayTransactionReceipt.TransactionReceipt result) {
                                System.out.println(result);
                            }

                            @Override
                            public void exception(Exception exception) {
                                System.out.println(exception);
                            }
                        }
                ))
                */
                .build();

        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction.create(
                BRANDON.getAddress(),
                WAYNE.getAddress(),
                BigInteger.ONE,
                GAS_LIMIT
        ).memo(MEMO_VALUE);

        KlayTransactionReceipt.TransactionReceipt valueTransferReceipt = transactionManager.executeTransaction(valueTransferTransaction);

        assertEquals(Numeric.toHexString(MEMO_VALUE.getBytes()), valueTransferReceipt.getInput());
    }
}
