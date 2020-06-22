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

import com.klaytn.caver.methods.request.KlayTransaction;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlaySignTransaction;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.type.TxTypeLegacyTransaction;
import com.klaytn.caver.utils.KlayUnit;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.LUMAN;
import static com.klaytn.caver.base.Accounts.WAYNE;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SendKlayIT extends Scenario {

    @Test
    @Ignore
    public void testTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        BigInteger value = KlayUnit.toPeb("0.5", KlayUnit.Unit.KLAY).toBigInteger();

        Boolean isUnlock = caver.klay().unlockAccount(LUMAN.getAddress(), "password", BigInteger.valueOf(10))
                .send().getResult();
        assertTrue(isUnlock);

        KlayTransaction transaction = KlayTransaction.createKlayTransaction(
                LUMAN.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, WAYNE.getAddress(), value);

        Bytes32 response = caver.klay().sendTransaction(transaction).send();
        String txHash = response.getResult();
        assertFalse(txHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(txHash);
        assertThat(transactionReceipt.getTransactionHash(), is(txHash));
    }

    @Test
    @Ignore
    public void testNodeSignedTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        BigInteger value = KlayUnit.toPeb("0.5", KlayUnit.Unit.KLAY).toBigInteger();

        Boolean isUnlock = caver.klay().unlockAccount(LUMAN.getAddress(), "password", BigInteger.valueOf(0))
                .send().getResult();
        assertTrue(isUnlock);

        KlayTransaction tx = KlayTransaction.createKlayTransaction(
                LUMAN.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, WAYNE.getAddress(), value);
        KlaySignTransaction.SignTransaction response = caver.klay().signTransaction(tx).send().getResult();

        Bytes32 klaySendTransaction = caver.klay().sendSignedTransaction(response.getRaw()).sendAsync().get();
        String transactionHash = klaySendTransaction.getResult();
        assertFalse(transactionHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);
        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    @Test
    public void testSignedTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        TxTypeLegacyTransaction tx = createKlayTransaction(nonce, WAYNE.getAddress());

        BigInteger balance = caver.klay().getBalance(
                LUMAN.getAddress(),
                DefaultBlockParameterName.LATEST).sendAsync().get().getValue();
        assertTrue(balance.compareTo(KlayUnit.toPeb("0.01", KlayUnit.Unit.KLAY).toBigInteger()) > 0);

        byte[] signedTransaction = tx.sign(LUMAN, LOCAL_CHAIN_ID).getValue();
        Bytes32 klaySendTransaction = caver.klay().sendSignedTransaction(Numeric.toHexString(signedTransaction)).sendAsync().get();
        String transactionHash = klaySendTransaction.getResult();
        assertFalse(transactionHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    private static TxTypeLegacyTransaction createKlayTransaction(BigInteger nonce, String toAddress) {
        BigInteger value = KlayUnit.toPeb("0.01", KlayUnit.Unit.KLAY).toBigInteger();
        return TxTypeLegacyTransaction.createTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, value, "");
    }

}
