/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.utils.model;

public class CommonTestData<T extends IMethodInputParams, R> {
    String id;
    String description;
    boolean skipJava = false;
    T input;
    ExpectedResult expectedResult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSkipJava() {
        return skipJava;
    }

    public void setSkipJava(boolean skipJava) {
        this.skipJava = skipJava;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }

    public ExpectedResult getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(ExpectedResult expectedResult) {
        this.expectedResult = expectedResult;
    }

    public static class ExpectedResult<R> {
        private R output;
        private String status;
        private String errorMessage;
        private String errorMessageJava;

        public R getOutput() {
            return output;
        }

        public void setOutput(R output) {
            this.output = output;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrorMessage() {
            if(this.errorMessageJava == null || this.errorMessageJava.isEmpty()) {
                return errorMessage;
            }
            return errorMessageJava;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessageJava() {
            return errorMessageJava;
        }

        public void setErrorMessageJava(String errorMessageJava) {
            this.errorMessageJava = errorMessageJava;
        }
    }
}
