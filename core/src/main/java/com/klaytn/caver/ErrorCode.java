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

package com.klaytn.caver;

/**
 * @deprecated This class is deprecated since caver-java:1.5.0
 */
public enum ErrorCode {
    EMPTY_NONCE(0x00, "empty nonce"),
    CREDENTIAL_NOT_FOUND(0x01, "credential not found"),
    UNSUPPORTED_TX_TYPE(0x02, "unsupported tx type");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
