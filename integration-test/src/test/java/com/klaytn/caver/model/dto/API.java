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

package com.klaytn.caver.model.dto;


public class API implements TestComponent {

    private InnerAPI api;
    private Object expected;
    private String errorString;

    public API(InnerAPI api, Object expected, String errorString) {
        this.api = api;
        this.expected = expected;
        this.errorString = errorString;
    }

    public InnerAPI getApi() {
        return api;
    }

    public Object getExpected() {
        return expected;
    }

    public String getErrorString() {
        return errorString;
    }

    public static class InnerAPI {
        private String method;
        private Object[] params;

        public InnerAPI() {
        }

        public String getMethod() {
            return method;
        }

        public Object[] getParams() {
            if (params == null) return new Object[]{};
            return params;
        }
    }

    @Override
    public Type getType() {
        return Type.API;
    }

}
