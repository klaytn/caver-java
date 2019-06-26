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

package com.klaytn.caver.tx.manager;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.Quantity;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;
import java.math.BigInteger;

public class GetNonceProcessor {
    protected final Caver caver;

    public GetNonceProcessor(Caver caver) {
        this.caver = caver;
    }

    public BigInteger getNonce(KlayCredentials credentials) throws IOException {
        Quantity quantity = caver.klay().getTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return quantity.getValue();
    }
}
