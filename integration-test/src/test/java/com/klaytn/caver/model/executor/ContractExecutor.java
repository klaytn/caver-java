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

package com.klaytn.caver.model.executor;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.model.ContractHelper;
import com.klaytn.caver.model.VariableStorage;
import com.klaytn.caver.model.dto.Contract;
import com.klaytn.caver.model.dto.TestComponent;
import com.klaytn.caver.model.dto.Transaction;
import com.klaytn.caver.tx.SmartContract;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContractExecutor extends Executor {

    private Contract contract;
    private Caver caver;
    private KlayCredentials sender;

    public ContractExecutor(Contract contract, String url, KlayCredentials sender) {
        this.contract = contract;
        this.caver = Caver.build(url);
        this.sender = sender;
    }

    public Contract getContract() {
        return contract;
    }

    public Caver getCaver() {
        return caver;
    }

    public KlayCredentials getSender() {
        return sender;
    }

    @Override
    public Object execute() throws Exception {
        SmartContract smartContract = (SmartContract) variableStorage.get("contract");

        KlayCredentials from = getFrom();
        Method loadMethod = smartContract.getClass()
                .getDeclaredMethod("load", String.class, Caver.class, KlayCredentials.class, int.class, ContractGasProvider.class);
        smartContract = (SmartContract) loadMethod.invoke(smartContract.getClass(), smartContract.getContractAddress(),
                caver, from, Transaction.DEFAULT_CHAIN_ID, new DefaultGasProvider());

        String methodName = contract.getComponent().getMethod();
        Method targetMethod;
        if (ContractHelper.hasParentheses(methodName)) {
            targetMethod = smartContract.getClass().getDeclaredMethod(ContractHelper.removeParentheses(methodName),
                    ContractHelper.getParenthesesTypes(methodName));
        } else {
            targetMethod = Arrays.stream(smartContract.getClass().getDeclaredMethods())
                    .filter(aMethod -> aMethod.getName().equals(methodName))
                    .findAny().orElseThrow(NullPointerException::new);
        }

        Object[] objects = getParams(targetMethod);
        Object result = targetMethod.invoke(smartContract, objects);

        Object object;
        try {
            object = ((RemoteCall<?>) result).send();
        } catch (Exception e) {
            object = e.getMessage();
        }
        return object;
    }

    private Object[] getParams(Method targetMethod) throws Exception {
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        List<String> params = contract.getComponent().getParams();

        List objects = new ArrayList<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (VariableStorage.isStartWithRandom(params.get(i))) {
                objects.add(((KlayCredentials) variableStorage.createRandom(params.get(i))).getAddress());
                continue;
            }
            objects.add(ContractHelper.convertType(parameterTypes[i], params.get(i)));
        }
        return objects.toArray();
    }

    private KlayCredentials getFrom() throws Exception {
        String from = contract.getComponent().getFrom();
        if (VariableStorage.isStartWithRandom(from)) return (KlayCredentials) variableStorage.createRandom(from);
        if (sender.getAddress().equals(from)) return sender;
        throw new NullPointerException();
    }

    @Override
    public TestComponent.Type getType() {
        return TestComponent.Type.CONTRACT;
    }
}
