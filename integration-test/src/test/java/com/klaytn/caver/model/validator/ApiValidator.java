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

import com.klaytn.caver.methods.response.KlayAccount;
import com.klaytn.caver.model.VariableStorage;
import org.web3j.protocol.core.Response;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

public class ApiValidator extends Validator {

    private Object expected;
    private String errorString;
    private String methodName;

    public ApiValidator(Object expected, String errorString, String methodName) {
        this.expected = expected;
        this.errorString = errorString;
        this.methodName = methodName;
    }

    @Override
    public void validate(Object object) {
        Response response = (Response) object;
        if (errorString != null) {
            assertEquals(errorString, response.getError().getMessage());
            return;
        }

        Object result = response.getResult();
        saveIfTarget(methodName, result);

        if (expected == null) {
            assertNull(response.getError());
            return;
        }

        if (result instanceof KlayAccount.Account) {
            assertEquals(
                    ((KlayAccount.Account) response.getResult()).getAccType(),
                    ((KlayAccount.Account) expected).getAccType()
            );
        } else assertEquals(result, expected);

    }

    private void saveIfTarget(String methodName, Object result) {
        VariableStorage.pairs.stream()
                .filter(pair -> pair.hasSameMethodName(methodName))
                .findAny()
                .ifPresent(pair -> variableStorage.set(pair.getVariableName(), result));
    }
}
