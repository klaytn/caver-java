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

package com.klaytn.caver.common.rpc;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.LogsNotification;
import com.klaytn.caver.methods.response.Quantity;
import io.reactivex.disposables.Disposable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.protocol.websocket.events.LogNotification;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubscribeTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    static int blockNotificationCount = 0;

    public static KIP7 kip7contract;
    public static final String CONTRACT_NAME = "Kale";
    public static final String CONTRACT_SYMBOL = "KALE";
    public static final int CONTRACT_DECIMALS = 18;
    public static final BigInteger CONTRACT_INITIAL_SUPPLY = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 100000 * 10^18

    public static void deployContract(Caver caver) throws Exception {
        KIP7DeployParams kip7DeployParam = new KIP7DeployParams(CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);
        SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(45000));
        kip7contract = caver.kct.kip7.deploy(kip7DeployParam, LUMAN.getAddress());
    }

    @Test
    public void newHeadsTest() throws IOException {
        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        Caver caver = new Caver(webSocketService);
        webSocketService.connect();

        final Disposable disposable = caver.rpc.klay.subscribe("newHeads", (data) -> {
            blockNotificationCount++;
        });

        while(blockNotificationCount<10);

        assertEquals(10, blockNotificationCount);

        disposable.dispose();
        caver.currentProvider.close();
    }

    @Test
    public void newHeads_InvalidType() throws ConnectException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("It is only allowed a 'newHeads' as a type parameter.");

        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        Caver caver = new Caver(webSocketService);
        webSocketService.connect();

        final Disposable disposable = caver.rpc.klay.subscribe("logs", (data) -> {
            blockNotificationCount++;
        });

        webSocketService.close();
    }

    @Test
    public void logsTest() throws Exception {
        final LogsNotification[] log = {null};

        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        webSocketService.connect();
        Caver caver = new Caver(webSocketService);

        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        deployContract(caver);

        KlayFilter options = new KlayFilter();
        options.setAddress(kip7contract.getContractAddress());
        options.setFromBlock(DefaultBlockParameterName.LATEST);
        options.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
        options.addSingleTopic("0x0000000000000000000000002c8ad0ea2e0781db8b8c9242e07de3a5beabb71a");

        Disposable disposable = caver.rpc.klay.subscribe("logs", options, (data) -> {
            log[0] = data;
        });

        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(BigInteger.valueOf(18).intValue()));;
        SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), DefaultGasProvider.GAS_LIMIT);

        kip7contract.transfer(BRANDON.getAddress(), amount, sendOptions);

        while(log[0] == null);

        assertEquals("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef", log[0].getParams().getResult().getTopics().get(0));
        assertEquals("0x0000000000000000000000002c8ad0ea2e0781db8b8c9242e07de3a5beabb71a", log[0].getParams().getResult().getTopics().get(1));
        assertEquals("0x000000000000000000000000e97f27e9a5765ce36a7b919b1cb6004c7209217e", log[0].getParams().getResult().getTopics().get(2));

        disposable.dispose();
        webSocketService.close();
    }

    @Test
    public void logs_InvalidType() throws ConnectException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("It is only allowed a 'logs' as a type parameter.");

        WebSocketService webSocketService = new WebSocketService("ws://localhost:8552", false);
        Caver caver = new Caver(webSocketService);
        webSocketService.connect();

        KlayFilter options = new KlayFilter();
        options.setFromBlock(DefaultBlockParameterName.LATEST);
        options.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
        options.addSingleTopic("0x0000000000000000000000002c8ad0ea2e0781db8b8c9242e07de3a5beabb71a");

        Disposable disposable = caver.rpc.klay.subscribe("invalid", options, (data) -> {
        });

        webSocketService.close();
    }
}
