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

package com.klaytn.caver.model;

import com.klaytn.caver.model.executor.TransactionExecutor;
import com.klaytn.caver.tx.type.TxType;

import java.lang.reflect.Constructor;
import java.util.*;

public class TransactionGenerator {

    public static Map<String, TransactionGenerator> mapper = new HashMap<>();

    static {
        mapper.put("ACCOUNT_CREATION", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeAccountCreation",
                true, true, true, false, false, false, false));
        mapper.put("ACCOUNT_UPDATE", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeAccountUpdate",
                false, false, true, false, false, false, false));
        mapper.put("CANCEL", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeCancel",
                false, false, false, false, false, false, false));
        mapper.put("FEE_DELEGATED_ACCOUNT_UPDATE", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedAccountUpdate",
                false, false, true, false, false, false, false));
        mapper.put("FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedAccountUpdateWithRatio",
                false, false, true, true, false, false, false));
        mapper.put("FEE_DELEGATED_CANCEL", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedCancel",
                false, false, false, false, false, false, false));
        mapper.put("FEE_DELEGATED_CANCEL_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedCancelWithRatio",
                false, false, false, true, false, false, false));
        mapper.put("FEE_DELEGATED_SMART_CONTRACT_DEPLOY", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractDeploy",
                false, true, false, false, true, true, false));
        mapper.put("FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractDeployWithRatio",
                false, true, false, true, true, true, false));
        mapper.put("FEE_DELEGATED_SMART_CONTRACT_EXECUTION", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecution",
                true, true, false, false, true, false, false));
        mapper.put("FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecutionWithRatio",
                true, true, false, true, true, false, false));
        mapper.put("FEE_DELEGATED_VALUE_TRANSFER", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedValueTransfer",
                true, true, false, false, false, false, false));
        mapper.put("FEE_DELEGATED_VALUE_TRANSFER_MEMO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedValueTransferMemo",
                true, true, false, false, true, false, false));
        mapper.put("FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedValueTransferMemoWithRatio",
                true, true, false, true, true, false, false));
        mapper.put("FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeFeeDelegatedValueTransferWithRatio",
                true, true, false, true, false, false, false));
        mapper.put("LEGACY", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeLegacyTransaction",
                true, true, false, false, false, false, true));
        mapper.put("SMART_CONTRACT_DEPLOY", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeSmartContractDeploy",
                false, true, false, false, true, true, false));
        mapper.put("SMART_CONTRACT_EXECUTION", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeSmartContractExecution",
                true, true, false, false, true, false, false));
        mapper.put("VALUE_TRANSFER", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeValueTransfer",
                true, true, false, false, false, false, false));
        mapper.put("VALUE_TRANSFER_MEMO", new TransactionGenerator("com.klaytn.caver.tx.type.TxTypeValueTransferMemo",
                true, true, false, false, true, false, false));
    }

    String classPath;
    boolean to;
    boolean value;
    boolean accountKey;
    boolean feeRatio;
    boolean payload;
    boolean codeFormat;
    boolean legacy;

    public TransactionGenerator(String classPath, boolean to, boolean value, boolean accountKey, boolean feeRatio,
                                boolean payload, boolean codeFormat, boolean legacy) {
        this.classPath = classPath;
        this.to = to;
        this.value = value;
        this.accountKey = accountKey;
        this.feeRatio = feeRatio;
        this.payload = payload;
        this.codeFormat = codeFormat;
        this.legacy = legacy;
    }

    public String getClassPath() {
        return classPath;
    }

    public boolean hasTo() {
        return to;
    }

    public boolean hasValue() {
        return value;
    }

    public boolean hasAccountKey() {
        return accountKey;
    }

    public boolean hasFeeRatio() {
        return feeRatio;
    }

    public boolean hasPayload() {
        return payload;
    }

    public boolean hasCodeFormat() {
        return codeFormat;
    }

    public boolean isLegacy() {
        return legacy;
    }


    public static TxType generate(String type, TransactionExecutor transactionExecutor) throws Exception {
        TransactionGenerator transactionGenerator = TransactionGenerator.mapper.get(type);
        Constructor<?>[] constructors = Class.forName(transactionGenerator.getClassPath()).getDeclaredConstructors();

        List objects = new ArrayList<>(Arrays.asList(
                transactionExecutor.getNonce(),
                transactionExecutor.getTx().getGasPrice(),
                transactionExecutor.getTx().getGas()
        ));

        if (transactionGenerator.hasTo()) {
            objects.add(transactionExecutor.getTo());
        }
        if (transactionGenerator.hasValue()) {
            objects.add(transactionExecutor.getTx().getValue());
        }
        if (transactionGenerator.isLegacy()) {
            objects.add(transactionExecutor.getTx().getData());
        } else {
            objects.add(transactionExecutor.getTx().getFrom());
        }
        if (transactionGenerator.hasPayload()) {
            objects.add(transactionExecutor.getTx().getPayload());
        }
        if (transactionGenerator.hasAccountKey()) {
            objects.add(transactionExecutor.getTx().getAccountKey());
        }
        if (transactionGenerator.hasFeeRatio()) {
            objects.add(transactionExecutor.getTx().getFeeRatio());
        }
        if (transactionGenerator.hasCodeFormat()) {
            objects.add(transactionExecutor.getTx().getCodeFormat());
        }

        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        TxType transaction = (TxType) constructor.newInstance(objects.toArray());

        return transaction;
    }
}
