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

import com.klaytn.caver.model.ApiHelper;
import com.klaytn.caver.model.dto.API;
import com.klaytn.caver.model.dto.TestComponent;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class ApiExecutor extends Executor {

    private API api;
    private Web3jService service;

    public ApiExecutor(API api, String URL) {
        this.api = api;
        this.service = new HttpService(URL);
    }

    public API.InnerAPI getApi() {
        return api.getApi();
    }

    public Object getExpected() {
        return api.getExpected();
    }

    public String getErrorString() {
        return api.getErrorString();
    }

    @Override
    public Response execute() throws IOException {
        Response response;
        String methodName = getApi().getMethod();

        response = new Request<>(
                methodName,
                Arrays.asList(getParams()),
                service,
                ApiHelper.getResponseClass(methodName)).send();

        return response;
    }

    private Object[] getParams() {
        Object[] params = getApi().getParams();
        for (int i = 0; i < params.length; i++) {
            params[i] = checkReplaceValue(params[i]);
        }
        return params;
    }

    private Object checkReplaceValue(Object param) {
        if (variableStorage.has(param)) {
            return variableStorage.get(param);
        }
        checkObject(param);
        return param;
    }

    private void checkObject(Object param) {
        if (param instanceof LinkedHashMap) {
            LinkedHashMap paramObject = (LinkedHashMap) param;
            traverseObject(paramObject);
        }
    }

    private void traverseObject(LinkedHashMap paramObject) {
        for (Object o : paramObject.keySet()) {
            Object value = paramObject.get(o);
            if (value instanceof String && variableStorage.has(value)) {
                paramObject.put(o, variableStorage.get(value));
            }
        }
    }

    @Override
    public TestComponent.Type getType() {
        return api.getType();
    }

}
