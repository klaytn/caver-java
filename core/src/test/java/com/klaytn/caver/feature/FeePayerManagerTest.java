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

package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.model.ValueTransferTransaction;
import com.klaytn.caver.tx.type.TxType;
import com.klaytn.caver.utils.Convert;
import org.junit.Before;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.*;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class FeePayerManagerTest {

    static final BigInteger GAS_PRICE = Convert.toPeb("25", Convert.Unit.STON).toBigInteger();
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    static final BigInteger FEE_RATIO = BigInteger.valueOf(30);

    private Caver caver;

    @Before
    public void setUp() {
        caver = Caver.build(Caver.DEFAULT_URL);
    }

    @Test
    public void testFeePayerManagerValueTransfer() throws Exception {
        String rawTx = getSenderRawTx();
        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, FEE_PAYER)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChainId(LOCAL_CHAIN_ID)
                .build();

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(rawTx);

        assertEquals("0x1", transactionReceipt.getStatus());
        assertEquals(FEE_PAYER.getAddress(), transactionReceipt.getFeePayer());
        assertEquals(Numeric.toHexStringWithPrefixSafe(FEE_RATIO), transactionReceipt.getFeeRatio());
        assertEquals(feePayerManager.sign(rawTx).getSignatureData(), transactionReceipt.getFeePayerSignatures().get(0));
    }

    private String getSenderRawTx() throws Exception {
        TxType tx = ValueTransferTransaction.create(LUMAN.getAddress(), BRANDON.getAddress(), BigInteger.ONE, GAS_LIMIT)
                .gasPrice(GAS_PRICE)
                .nonce(getNonce(LUMAN.getAddress()))
                .feeRatio(FEE_RATIO)
                .buildFeeDelegated();
        return tx.sign(LUMAN, LOCAL_CHAIN_ID).getValueAsString();
    }

    BigInteger getNonce(String address) throws Exception {
        BigInteger nonce = caver.klay().getTransactionCount(
                address,
                DefaultBlockParameterName.PENDING).sendAsync().get().getValue();
        return nonce;
    }
}
