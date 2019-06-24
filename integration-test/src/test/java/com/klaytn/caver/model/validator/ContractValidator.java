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

import com.fasterxml.jackson.databind.JsonNode;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.model.ContractHelper;
import com.klaytn.caver.model.dto.Expected;
import com.klaytn.caver.model.executor.ContractExecutor;
import com.klaytn.caver.tx.SmartContract;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.klaytn.caver.model.VariableStorage.isStartWithRandom;
import static org.junit.Assert.assertEquals;

public class ContractValidator extends Validator {

    Expected expected;

    public ContractValidator(ContractExecutor executor) {
        expected = executor.getContract().getExpected();
    }

    @Override
    public void validate(Object object) throws Exception {
        checkError(object);
        checkReceipt(object);
        checkReturns(object);
    }

    private void checkReturns(Object object) {
        if (expected.getReturns() != null && expected.isStatus()) {
            Object returns = expected.getReturns();
            if (isStartWithRandom(returns)) {
                returns = ((KlayCredentials) variableStorage.get((String) returns)).getAddress();
            }
            assertEquals(returns, object.toString());
        }
    }

    private void checkReceipt(Object object) throws Exception {
        if (object instanceof KlayTransactionReceipt.TransactionReceipt) {
            if (expected.getReceipt() != null) {
                KlayTransactionReceipt.TransactionReceipt actualReceipt = (KlayTransactionReceipt.TransactionReceipt) object;
                Expected.Receipt expectedReceipt = expected.getReceipt();

                assertEquals(Boolean.valueOf(expectedReceipt.getStatus()), actualReceipt.getStatus().equals("0x1"));
                if (expected.getReceipt().getStatus().equals("false")) {
                    assertEquals(expectedReceipt.getTxError(), actualReceipt.getTxError());
                }
            }

            if (expected.getEvents() != null) {
                SmartContract smartContract = (SmartContract) variableStorage.get("contract");
                JsonNode eventsNode = expected.getEvents().getValues();
                traverseEvents(object, smartContract, eventsNode);
            }
        }
    }

    private void checkError(Object object) {
        if (expected.getErrorString() != null) {
            assertEquals(expected.getErrorStringJava(), object);
        }
    }

    private void traverseEvents(Object object, SmartContract smartContract, JsonNode eventsNode) throws Exception {
        Iterator<Map.Entry<String, JsonNode>> iterator = eventsNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> event = iterator.next();
            Method method = getEventMethod(smartContract, event.getKey());
            Object eventResult = ((List) method.invoke(smartContract, object)).get(0);
            traverseEventFields(event, eventResult);
        }
    }

    private Method getEventMethod(SmartContract smartContract, String eventKey) {
        return Arrays.stream(smartContract.getClass().getDeclaredMethods())
                .filter(aMethod -> aMethod.getName().equals(ContractHelper.getEventName(eventKey)))
                .findAny().orElseThrow(NullPointerException::new);
    }

    private void traverseEventFields(Map.Entry<String, JsonNode> event, Object eventResult) throws Exception {
        Iterator<Map.Entry<String, JsonNode>> values = event.getValue().fields();
        while (values.hasNext()) {
            validateEventValue(eventResult, values.next());
        }
    }

    private void validateEventValue(Object log, Map.Entry<String, JsonNode> value) throws Exception {
        String expectedKey = value.getKey();
        String expectedValue = value.getValue().asText();
        Field actualField = log.getClass().getDeclaredField(expectedKey);

        assertEquals(
                ContractHelper.convertType(actualField.getType(), variableStorage.convertRandom(expectedValue)),
                actualField.get(log)
        );
    }
}
