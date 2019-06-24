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

package com.klaytn.caver.model.validator;

import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.model.dto.Expected;
import com.klaytn.caver.model.executor.TransactionExecutor;
import org.junit.Assert;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

public class TransactionValidator extends Validator {

    private TransactionExecutor transactionExecutor;
    private Expected expected;

    public TransactionValidator(TransactionExecutor transactionExecutor) {
        this.transactionExecutor = transactionExecutor;
        this.expected = transactionExecutor.getExpected();
    }

    @Override
    public void validate(Object object) throws IOException, TransactionException {
        Response response = (Response) object;
        Bytes32 bytes32 = (Bytes32) response;
        if (expected == null) return;
        if (!expected.isStatus()) {
            Assert.assertEquals(response.getError().getMessage(), expected.getErrorString());
            return;
        }

        KlayTransactionReceipt.TransactionReceipt actual = transactionExecutor.getReceipt(bytes32);
        saveContractAddress(actual);
        if (expected.getReceipt() != null) {
            checkReceipt(actual, bytes32.getResult());
        }
    }

    private void saveContractAddress(KlayTransactionReceipt.TransactionReceipt actual) {
        if (actual.getContractAddress() != null && transactionExecutor.getTransaction().getDeployedAddress() != null) {
            variableStorage.set(
                    transactionExecutor.getTransaction().getDeployedAddress(), actual.getContractAddress());
        }
    }

    public void checkReceipt(KlayTransactionReceipt.TransactionReceipt actualReceipt, String transactionHash) {
        Expected.Receipt expectedReceipt = expected.getReceipt();

        if (expectedReceipt.getStatus() != null) {
            Assert.assertEquals(Boolean.valueOf(expectedReceipt.getStatus()), actualReceipt.getStatus().equals("0x1"));
        }
        if (expectedReceipt.getTxError() != null) {
            Assert.assertEquals(expectedReceipt.getTxError(), actualReceipt.getTxError());
        }
        if (expectedReceipt.getCheckContractAddress()) {
            Assert.assertNotNull(actualReceipt.getContractAddress());
        }
        if (expectedReceipt.getCheckSenderTxHash()) {
            Assert.assertEquals(transactionHash, actualReceipt.getTransactionHash());
        }
    }
}
