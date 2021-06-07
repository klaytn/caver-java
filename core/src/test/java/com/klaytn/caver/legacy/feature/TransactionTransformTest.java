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

package com.klaytn.caver.legacy.feature;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.model.*;
import com.klaytn.caver.tx.type.TxType;
import com.klaytn.caver.utils.CodeFormat;
import org.junit.Test;

import java.math.BigInteger;

import static junit.framework.TestCase.assertEquals;

public class TransactionTransformTest {

    private static final KlayCredentials CREDENTIALS = KlayCredentials.create("0xf8cc7c3813ad23817466b1802ee805ee417001fcce9376ab8728c92dd8ea0a6b");
    private static final AccountKeyPublic ACCOUNT_KEY_PUBLIC = AccountKeyPublic.create(CREDENTIALS.getEcKeyPair().getPublicKey());

    @Test
    public void testValueTransferTransform() {

        assertEquals(
                TxType.Type.VALUE_TRANSFER,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .build()
                        .getType()
        );
        assertEquals(
                TxType.Type.VALUE_TRANSFER_MEMO,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .memo("")
                        .build()
                        .getType()
        );
        assertEquals(
                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .nonce(BigInteger.ZERO)
                        .buildFeeDelegated()
                        .getType()
        );
        assertEquals(
                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .memo("")
                        .buildFeeDelegated()
                        .getType()
        );
        assertEquals(
                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );
        assertEquals(
                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO,
                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
                        .memo("")
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );
    }

    @Test
    public void testAccountUpdate() {
        assertEquals(
                TxType.Type.ACCOUNT_UPDATE,
                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
                        .build()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE,
                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
                        .buildFeeDelegated()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO,
                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );
    }

    @Test
    public void testSmartContractDeploy() {
        assertEquals(
                TxType.Type.SMART_CONTRACT_DEPLOY,
                SmartContractDeployTransaction.create("", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
                        .build()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY,
                SmartContractDeployTransaction.create("", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
                        .buildFeeDelegated()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO,
                SmartContractDeployTransaction.create("", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );
    }

    @Test
    public void testSmartContractExecution() {
        assertEquals(
                TxType.Type.SMART_CONTRACT_EXECUTION,
                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
                        .build()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION,
                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
                        .buildFeeDelegated()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO,
                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );

    }

    @Test
    public void testCancel() {
        assertEquals(
                TxType.Type.CANCEL,
                CancelTransaction.create("", BigInteger.ZERO)
                        .nonce(BigInteger.ZERO)
                        .build()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_CANCEL,
                CancelTransaction.create("", BigInteger.ZERO)
                        .nonce(BigInteger.ZERO)
                        .buildFeeDelegated()
                        .getType()
        );

        assertEquals(
                TxType.Type.FEE_DELEGATED_CANCEL_WITH_RATIO,
                CancelTransaction.create("", BigInteger.ZERO)
                        .nonce(BigInteger.ZERO)
                        .feeRatio(BigInteger.valueOf(10))
                        .buildFeeDelegated()
                        .getType()
        );
    }

}
