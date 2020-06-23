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

package com.klaytn.caver.base;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.ValueTransferTransaction;
import com.klaytn.caver.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.klaytn.caver.base.LocalValues.KLAY_PROVIDER;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;

public class Accounts {

    public static final KlayCredentials LUMAN = KlayCredentials.create(
            "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3",
            "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a"
    );

    public static final KlayCredentials WAYNE = KlayCredentials.create(
            "0x92c0815f28b20cc22fff5fcf41adc80efe9d7ebe00439628b468f2f88a0aadc4",
            "0x3cd93ba290712e6d28ac98f2b820faf799ae8fdb"
    );

    public static final KlayCredentials BRANDON = KlayCredentials.create(
            "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e",
            "0xe97f27e9a5765ce36a7b919b1cb6004c7209217e"
    );

    public static final KlayCredentials FEE_PAYER = KlayCredentials.create(
            "0x1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178",
            "0x9d0dcbe163be73163348e7f96accb2b9e1e9dcf6"
    );

    static {
        List<KlayCredentials> testCredentials = new ArrayList<>(Arrays.asList(LUMAN, WAYNE, BRANDON, FEE_PAYER));
        fillUpKlay(testCredentials);
    }

    private static void fillUpKlay(List<KlayCredentials> testCredentials) {
        Caver caver = Caver.build(Caver.DEFAULT_URL);
        TransactionManager transactionManager
                = new TransactionManager.Builder(caver, KLAY_PROVIDER).setChaindId(LOCAL_CHAIN_ID).build();
        for (KlayCredentials testCredential : testCredentials) {
            feedToEach(transactionManager, testCredential);
        }
    }

    private static void feedToEach(TransactionManager transactionManager, KlayCredentials testCredential) {
        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction.create(
                KLAY_PROVIDER.getAddress(),
                testCredential.getAddress(),
                Convert.toPeb("100", Convert.Unit.KLAY).toBigInteger(),
                BigInteger.valueOf(4_300_000)
        );
        transactionManager.executeTransaction(valueTransferTransaction);
    }
}
