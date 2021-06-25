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

package com.klaytn.caver;

import com.klaytn.caver.rpc.RPC;
import org.junit.Test;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

import static org.junit.Assert.assertTrue;

public class CaverTest {
    @Test
    public void setProvider() {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        HttpService httpService = new HttpService(Caver.DEFAULT_URL);

        caver.setProvider(webSocketService);
        assertTrue(caver.rpc.getWeb3jService() instanceof WebSocketService);

        caver.setProvider(httpService);
        assertTrue(caver.rpc.getWeb3jService() instanceof HttpService);
    }

    @Test
    public void setRPC() {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        HttpService httpService = new HttpService(Caver.DEFAULT_URL);

        caver.setRpc(new RPC(webSocketService));
        assertTrue(caver.rpc.getWeb3jService() instanceof WebSocketService);
        assertTrue(caver.getProvider() instanceof WebSocketService);

        caver.setRpc(new RPC(httpService));
        assertTrue(caver.rpc.getWeb3jService() instanceof HttpService);
        assertTrue(caver.getProvider() instanceof HttpService);
    }
}