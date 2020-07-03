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

package com.klaytn.caver.tx.exception;

import com.klaytn.caver.CaverException;
import com.klaytn.caver.ErrorCode;
import com.klaytn.caver.ErrorType;

@Deprecated
public class UnsupportedTxTypeException extends CaverException {

    public UnsupportedTxTypeException() {
        super(ErrorType.CAVER, ErrorCode.UNSUPPORTED_TX_TYPE.getCode(), ErrorCode.UNSUPPORTED_TX_TYPE.getMessage());
    }
}
