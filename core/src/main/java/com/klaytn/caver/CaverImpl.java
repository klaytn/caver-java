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

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.http.HttpService;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class CaverImpl implements Caver {

    private Web3jService service;
    private Admin web3j;
    private Klay klay;
    private Net net;

    CaverImpl() {
        this(DEFAULT_URL);
    }

    CaverImpl(String url) {
        this(new HttpService(url));
    }

    CaverImpl(Web3jService web3jService) {
        service = web3jService;
        web3j = new JsonRpc2_0Admin(service);
        klay = new JsonRpc2_0Klay(service, web3j);
        net = new JsonRpc2_0Net(service);
    }

    @Override
    public Klay klay() {
        return klay;
    }

    @Override
    public Net net() {
        return net;
    }
}
