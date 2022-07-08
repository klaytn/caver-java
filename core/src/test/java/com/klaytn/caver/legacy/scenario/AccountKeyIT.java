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

package com.klaytn.caver.legacy.scenario;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.Account;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.account.AccountKeyRoleBased;
import com.klaytn.caver.tx.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.tx.model.AccountUpdateTransaction;
import com.klaytn.caver.utils.Convert;
import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class AccountKeyIT extends Scenario {

    KlayCredentials credentials1, credentials2, credentials3;

    @Test
    public void AccountKeyRolebased() throws Exception {
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
        ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                credentials.getAddress(),
                BigDecimal.valueOf(20),
                Convert.Unit.KLAY, GAS_LIMIT
        ).send();
        setUpAccount();

        AccountUpdateTransaction accountUpdateTransaction = AccountUpdateTransaction.create(
                credentials.getAddress(),
                createRolebased(),
                gasProvider.getGasPrice(),
                GAS_LIMIT);
        KlayTransactionReceipt.TransactionReceipt receipt = Account.create(caver, credentials, LOCAL_CHAIN_ID)
                .sendUpdateTransaction(accountUpdateTransaction)
                .send();
        assertEquals("0x1", receipt.getStatus());
    }

    private AccountKey createRolebased() {
        return AccountKeyRoleBased.create(Arrays.asList(
                AccountKeyPublic.create(credentials1.getEcKeyPair().getPublicKey()),
                AccountKeyPublic.create(credentials2.getEcKeyPair().getPublicKey()),
                getRoleFeePayer()
        ));
    }

    private AccountKeyWeightedMultiSig getRoleFeePayer() {
        return AccountKeyWeightedMultiSig.create(
                BigInteger.valueOf(5),
                Arrays.asList(
                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                                BigInteger.valueOf(2),
                                AccountKeyPublic.create(credentials1.getEcKeyPair().getPublicKey())
                        ),
                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                                BigInteger.valueOf(2),
                                AccountKeyPublic.create(credentials2.getEcKeyPair().getPublicKey())
                        ),
                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                                BigInteger.valueOf(1),
                                AccountKeyPublic.create(credentials3.getEcKeyPair().getPublicKey())
                        )
                )
        );
    }

    private void setUpAccount() throws Exception {
        ECKeyPair key1 = Keys.createEcKeyPair();
        credentials1 = KlayCredentials.create(key1);

        ECKeyPair key2 = Keys.createEcKeyPair();
        credentials2 = KlayCredentials.create(key2);

        ECKeyPair key3 = Keys.createEcKeyPair();
        credentials3 = KlayCredentials.create(key3);
    }
}
