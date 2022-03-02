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

package com.klaytn.caver.common.kct.kip37;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.*;
import com.klaytn.caver.abi.datatypes.generated.Uint256;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip37.KIP37;
import com.klaytn.caver.kct.kip37.KIP37ConstantData;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.Utils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.klaytn.caver.base.Accounts.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class KIP37Test {
    public static KIP37 kip37;
    private static final String tokenURI = "https://kip37.example/{id}.json";

    public static KIP37 deployContract(String uri) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        return caver.kct.kip37.deploy(uri, LUMAN.getAddress());
    }

    public static boolean createToken(KIP37 kip37, BigInteger tokenId, BigInteger initialSupply, String URI, String sender) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        TransactionReceipt.TransactionReceiptData receiptData = kip37.create(tokenId, initialSupply, tokenURI, new SendOptions(sender));
        return receiptData.getStatus().equals("0x1");
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws Exception {
            kip37 = deployContract(tokenURI);
        }

        @Test
        public void deploy() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            KIP37 contract = caver.kct.kip37.deploy(tokenURI, LUMAN.getAddress());

            assertNotNull(contract.getContractAddress());
        }

        @Test
        public void uri() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String expected = "https://kip37.example/000000000000000000000000000000000000000000000000000000000004cce0.json";
            String tokenUri = kip37.uri("0x4cce0");
            assertEquals(expected, tokenUri);
        }
    }

    public static class MintableTest {
        @BeforeClass
        public static void initContract() throws Exception {
            kip37 = deployContract(tokenURI);

            BigInteger initialSupply = BigInteger.valueOf(1000);
            String tokenURI = "http://mintable.token/";
            if(!createToken(kip37, BigInteger.ZERO, initialSupply, tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.ONE, initialSupply, tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(2), initialSupply, tokenURI, LUMAN.getAddress())) {
                fail();
            }
        }

        @Test
        public void mint() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger tokenID = BigInteger.ONE;
            TransactionReceipt.TransactionReceiptData receiptData = kip37.mint(BRANDON.getAddress(), tokenID, BigInteger.TEN, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.TEN) >= 0);
        }

        @Test
        public void mint_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String tokenID = "0x1";
            TransactionReceipt.TransactionReceiptData receiptData = kip37.mint(BRANDON.getAddress(), tokenID, BigInteger.TEN, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.TEN) >= 0);
        }

        @Test
        public void mintWithToList() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger tokenID = BigInteger.ONE;
            String[] toArr = new String[] {WAYNE.getAddress(), BRANDON.getAddress()};
            BigInteger[] mintAmountArr = new BigInteger[] {BigInteger.TEN, BigInteger.TEN};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.mint(toArr, tokenID, mintAmountArr, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);

            balance = kip37.balanceOf(WAYNE.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);
        }

        @Test
        public void mintWithToList_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String tokenID = "0x1";
            String[] toArr = new String[] {WAYNE.getAddress(), BRANDON.getAddress()};
            BigInteger[] mintAmountArr = new BigInteger[] {BigInteger.TEN, BigInteger.TEN};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.mint(toArr, tokenID, mintAmountArr, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);

            balance = kip37.balanceOf(WAYNE.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);
        }

        @Test
        public void mintBatch() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String to = BRANDON.getAddress();
            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(1), BigInteger.valueOf(2)};
            BigInteger[] valuesArr = new BigInteger[] {BigInteger.TEN, BigInteger.TEN};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.mintBatch(to, tokenIdArr, valuesArr, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);

            balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.valueOf(2));
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);
        }

        @Test
        public void mintBatch_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String to = BRANDON.getAddress();
            String[] tokenIdArr = new String[] {"0x1", "0x2"};
            BigInteger[] valuesArr = new BigInteger[] {BigInteger.TEN, BigInteger.TEN};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.mintBatch(to, tokenIdArr, valuesArr, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            BigInteger balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.ONE);
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);

            balance = kip37.balanceOf(BRANDON.getAddress(), BigInteger.valueOf(2));
            assertTrue(balance.compareTo(BigInteger.ZERO) > 0);
        }

        @Test
        public void isMinter() {
            try {
                assertTrue(kip37.isMinter(LUMAN.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addMinter() {
            try {
                if(kip37.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(BRANDON.getAddress());
                    kip37.renounceMinter(sendOptions);
                }

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress());

                kip37.addMinter(BRANDON.getAddress(), sendOptions);

                assertTrue(kip37.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renounceMinter() {
            try {
                if(!kip37.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(LUMAN.getAddress());
                    kip37.addMinter(BRANDON.getAddress(), sendOptions);
                }

                SendOptions sendOptions = new SendOptions(BRANDON.getAddress());
                kip37.renounceMinter(sendOptions);

                assertFalse(kip37.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class PausableTest {
        @BeforeClass
        public static void initContract() throws Exception {
            kip37 = deployContract(tokenURI);
            SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
            kip37.setDefaultSendOptions(options);
        }

        @Before
        public void clear() {
            try {
                if(kip37.paused()) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    TransactionReceipt.TransactionReceiptData receiptData = kip37.unpause(options);
                    if(!receiptData.getStatus().equals("0x1")) {
                        fail();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pause() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip37.pause(options);
                assertTrue(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedDefaultOptions() {
            try{
                kip37.pause();
                assertTrue(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoDefaultGas() {
            try {
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress());
                kip37.setDefaultSendOptions(sendOptions);
                kip37.pause();
                assertTrue(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoGas() {
            try {
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress());
                kip37.pause(sendOptions);
                assertTrue(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void unPause() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip37.pause(options);
                assertTrue(kip37.paused());

                kip37.unpause(options);
                assertFalse(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addPauser() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip37.addPauser(BRANDON.getAddress(), options);

                assertTrue(kip37.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renouncePauser() {
            try {
                if(!kip37.isPauser(BRANDON.getAddress())) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    kip37.addPauser(BRANDON.getAddress(), options);
                }

                SendOptions options = new SendOptions(BRANDON.getAddress(), BigInteger.valueOf(4000000));
                kip37.renouncePauser(options);

                assertFalse(kip37.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void paused() {
            try {
                assertFalse(kip37.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pauseToken() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.ZERO;
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.pause(tokenId, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            boolean result = kip37.paused(tokenId);
            assertTrue(result);
        }

        @Test
        public void pauseToken_StringID() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.valueOf(1);
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.pause("0x1", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            boolean result = kip37.paused(tokenId);
            assertTrue(result);
        }

        @Test
        public void unpauseToken() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.valueOf(2);
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.pause(tokenId, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            receiptData = kip37.unpause(tokenId, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            boolean result = kip37.paused(tokenId);
            assertFalse(result);
        }

        @Test
        public void unpauseToken_StringID() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.valueOf(3);
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.pause("0x3", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            receiptData = kip37.unpause("0x3", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            boolean result = kip37.paused(tokenId);
            assertFalse(result);
        }

        @Test
        public void pausedToken() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.valueOf(4);
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            boolean result = kip37.paused(tokenId);
            assertFalse(result);
        }

        @Test
        public void pausedToken_StringID() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            BigInteger tokenId = BigInteger.valueOf(5);
            if(!createToken(kip37, tokenId, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            boolean result = kip37.paused("0x5");
            assertFalse(result);
        }
    }

    public static class BurnableTest {
        @BeforeClass
        public static void init() throws Exception {
            kip37 = deployContract(tokenURI);
            if(!createToken(kip37, BigInteger.ZERO, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.ONE, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(2), BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }
        }

        @Test
        public void burn() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger tokenID = BigInteger.ZERO;
            TransactionReceipt.TransactionReceiptData receiptData = kip37.burn(LUMAN.getAddress(), BigInteger.ZERO, BigInteger.TEN, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertTrue(kip37.balanceOf(LUMAN.getAddress(), tokenID).compareTo(BigInteger.valueOf(990)) <= 0);
        }

        @Test
        public void burn_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String tokenID = "0x0";
            TransactionReceipt.TransactionReceiptData receiptData = kip37.burn(LUMAN.getAddress(), BigInteger.ZERO, BigInteger.TEN, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }
            assertTrue(kip37.balanceOf(LUMAN.getAddress(), tokenID).compareTo(BigInteger.valueOf(990)) <= 0);
        }

        @Test
        public void burnBatch() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.ONE, BigInteger.valueOf(2)};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(10), BigInteger.valueOf(10)};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.burnBatch(LUMAN.getAddress(), tokenIds, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            String[] accountArr = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            List<BigInteger> balanceList = kip37.balanceOfBatch(accountArr, tokenIds);

            assertTrue(balanceList.get(0).compareTo(BigInteger.valueOf(990))<=0);
            assertTrue(balanceList.get(1).compareTo(BigInteger.valueOf(990))<=0);
        }

        @Test
        public void burnBatch_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] tokenIds = new String[] {"0x1", "0x2"};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(10), BigInteger.valueOf(10)};

            TransactionReceipt.TransactionReceiptData receiptData = kip37.burnBatch(LUMAN.getAddress(), tokenIds, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            String[] accountArr = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            List<BigInteger> balanceList = kip37.balanceOfBatch(accountArr, tokenIds);

            assertTrue(balanceList.get(0).compareTo(BigInteger.valueOf(990))<=0);
            assertTrue(balanceList.get(1).compareTo(BigInteger.valueOf(990))<=0);
        }
    }

    public static class IKIP37Test {
        static Caver caver = new Caver(Caver.DEFAULT_URL);
        @BeforeClass
        public static void init() throws Exception {
            kip37 = deployContract(tokenURI);
            if(!createToken(kip37, BigInteger.ZERO, BigInteger.valueOf(2000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.ONE, BigInteger.valueOf(2000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(2), BigInteger.valueOf(2000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(3), BigInteger.valueOf(2000), tokenURI, LUMAN.getAddress())) {
                fail();
            }
        }

        @Test
        public void totalSupply() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger total = kip37.totalSupply(BigInteger.ZERO);
            assertEquals(BigInteger.valueOf(2000), total);
        }

        @Test
        public void totalSupplyStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger total = kip37.totalSupply("0x0");
            assertEquals(BigInteger.valueOf(2000), total);
        }

        @Test
        public void balanceOf() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger balance = kip37.balanceOf(LUMAN.getAddress(), BigInteger.ZERO);
            assertEquals(BigInteger.valueOf(2000), balance);
        }

        @Test
        public void balanceOfStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger balance = kip37.balanceOf(LUMAN.getAddress(), "0x0");
            assertEquals(BigInteger.valueOf(2000), balance);
        }

        @Test
        public void balanceOfBatch() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] accounts = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.ZERO, BigInteger.valueOf(1)};

            List<BigInteger> balances = kip37.balanceOfBatch(accounts, tokenIds);
            assertEquals(BigInteger.valueOf(2000), balances.get(0));
            assertEquals(BigInteger.valueOf(2000), balances.get(1));
        }

        @Test
        public void balanceOfBatchStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] accounts = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            String[] tokenIds = new String[] {"0x0", "0x1"};

            List<BigInteger> balances = kip37.balanceOfBatch(accounts, tokenIds);
            assertEquals(BigInteger.valueOf(2000), balances.get(0));
            assertEquals(BigInteger.valueOf(2000), balances.get(1));
        }

        @Test
        public void isApprovedForAll() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
            String owner = LUMAN.getAddress();
            String operator = WAYNE.getAddress();

            assertFalse(kip37.isApprovedForAll(owner, operator));
        }

        @Test
        public void setApprovedForAll() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, TransactionException {
            String owner = LUMAN.getAddress();
            String operator = BRANDON.getAddress();

            if(kip37.isApprovedForAll(owner, operator)) {
                TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, false, new SendOptions(LUMAN.getAddress()));
                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, true, new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(300000)));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertTrue(kip37.isApprovedForAll(owner, operator));
        }

        @Test
        public void safeTransferFrom() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String from = LUMAN.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();
            BigInteger tokenId = BigInteger.valueOf(2);
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeTransferFromStringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String from = LUMAN.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();
            String tokenId = "0x2";
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeTransferFromWithoutData() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String from = LUMAN.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();
            BigInteger tokenId = BigInteger.valueOf(2);
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeTransferFromStringIDWithoutData() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String from = LUMAN.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();
            String tokenId = "0x2";
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeBatchTransferFrom() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(3)};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String to = caver.wallet.keyring.generate().getAddress();

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void safeBatchTransferFrom_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] tokenIds = new String[] {"0x2", "0x3"};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String to = caver.wallet.keyring.generate().getAddress();

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void safeBatchTransferFromWithoutData() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(3)};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String to = caver.wallet.keyring.generate().getAddress();

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void safeBatchTransferFrom_StringIDWithoutData() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] tokenIds = new String[] {"0x2", "0x3"};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String to = caver.wallet.keyring.generate().getAddress();

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void safeTransferFromWithOperator() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger tokenId = BigInteger.valueOf(2);

            String from = LUMAN.getAddress();
            String operator = BRANDON.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();

            if(!kip37.isApprovedForAll(from, operator)) {
                TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, true, new SendOptions(from));
                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, BigInteger.TEN, "", new SendOptions(operator));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.TEN, kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeTransferFromWithOperator_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String tokenId = "0x2";

            String from = LUMAN.getAddress();
            String operator = BRANDON.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();

            if(!kip37.isApprovedForAll(from, operator)) {
                TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, true, new SendOptions(from));
                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, BigInteger.TEN, "", new SendOptions(operator));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.TEN, kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeBatchTransferFromWithOperator() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(3)};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String from = LUMAN.getAddress();
            String operator = BRANDON.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();

            if(!kip37.isApprovedForAll(from, operator)) {
                TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, true, new SendOptions(from));
                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void safeBatchTransferFromWithOperator_StringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] tokenIds = new String[] {"0x2", "0x3"};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String from = LUMAN.getAddress();
            String operator = BRANDON.getAddress();
            String to = caver.wallet.keyring.generate().getAddress();

            if(!kip37.isApprovedForAll(from, operator)) {
                TransactionReceipt.TransactionReceiptData receiptData = kip37.setApprovalForAll(operator, true, new SendOptions(from));
                if(!receiptData.getStatus().equals("0x1")) {
                    fail();
                }
            }

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, "data", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(3)));
        }

        @Test
        public void supportInterface() {
            final String INTERFACE_ID_KIP13 = "0x01ffc9a7";
            final String INTERFACE_ID_KIP37 = "0x6433ca1f";
            final String INTERFACE_ID_KIP37_PAUSABLE = "0x0e8ffdb7";
            final String INTERFACE_ID_KIP37_BURNABLE = "0x9e094e9e";
            final String INTERFACE_ID_KIP37_MINTABLE = "0xdfd9d9ec";
            final String INTERFACE_ID_FALSE = "0xFFFFFFFF";

            try {
                boolean isSupported_KIP13 = kip37.supportsInterface(INTERFACE_ID_KIP13);
                assertTrue(isSupported_KIP13);

                boolean isSupported_KIP37_PAUSABLE = kip37.supportsInterface(INTERFACE_ID_KIP37_PAUSABLE);
                assertTrue(isSupported_KIP37_PAUSABLE);

                boolean isSupported_KIP37_BURNABLE = kip37.supportsInterface(INTERFACE_ID_KIP37_BURNABLE);
                assertTrue(isSupported_KIP37_BURNABLE);

                boolean isSupported_KIP37_MINTABLE = kip37.supportsInterface(INTERFACE_ID_KIP37_MINTABLE);
                assertTrue(isSupported_KIP37_MINTABLE);

                boolean isSupported_KIP37 = kip37.supportsInterface(INTERFACE_ID_KIP37);
                assertTrue(isSupported_KIP37);

                boolean isSupported_FALSE = kip37.supportsInterface(INTERFACE_ID_FALSE);
                assertFalse(isSupported_FALSE);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class DetectInterfaceTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static Caver caver;
        static final String byteCodeWithMintable = "0x60806040523480156200001157600080fd5b506040516200291338038062002913833981018060405260208110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b505092919050505080620000a66301ffc9a760e01b620000ef60201b60201c565b620000b781620001f860201b60201c565b620000cf636433ca1f60e01b620000ef60201b60201c565b620000e7630e89341c60e01b620000ef60201b60201c565b5050620002c3565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614156200018c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b80600490805190602001906200021092919062000214565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200025757805160ff191683800117855562000288565b8280016001018555821562000288579182015b82811115620002875782518255916020019190600101906200026a565b5b5090506200029791906200029b565b5090565b620002c091905b80821115620002bc576000816000905550600101620002a2565b5090565b90565b61264080620002d36000396000f3fe608060405234801561001057600080fd5b50600436106100925760003560e01c80634e1273f4116100665780634e1273f414610428578063a22cb465146105c9578063bd85b03914610619578063e985e9c51461065b578063f242432a146106d757610092565b8062fdd58e1461009757806301ffc9a7146100f95780630e89341c1461015e5780632eb2c2d614610205575b600080fd5b6100e3600480360360408110156100ad57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506107e6565b6040518082815260200191505060405180910390f35b6101446004803603602081101561010f57600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191690602001909291905050506108c6565b604051808215151515815260200191505060405180910390f35b61018a6004803603602081101561017457600080fd5b810190808035906020019092919050505061092d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101ca5780820151818401526020810190506101af565b50505050905090810190601f1680156101f75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610426600480360360a081101561021b57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561027857600080fd5b82018360208201111561028a57600080fd5b803590602001918460208302840111640100000000831117156102ac57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561030c57600080fd5b82018360208201111561031e57600080fd5b8035906020019184602083028401116401000000008311171561034057600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156103a057600080fd5b8201836020820111156103b257600080fd5b803590602001918460018302840111640100000000831117156103d457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506109d1565b005b6105726004803603604081101561043e57600080fd5b810190808035906020019064010000000081111561045b57600080fd5b82018360208201111561046d57600080fd5b8035906020019184602083028401116401000000008311171561048f57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156104ef57600080fd5b82018360208201111561050157600080fd5b8035906020019184602083028401116401000000008311171561052357600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050610e60565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156105b557808201518184015260208101905061059a565b505050509050019250505060405180910390f35b610617600480360360408110156105df57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080351515906020019092919050505061103e565b005b6106456004803603602081101561062f57600080fd5b81019080803590602001909291905050506111d9565b6040518082815260200191505060405180910390f35b6106bd6004803603604081101561067157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506111f6565b604051808215151515815260200191505060405180910390f35b6107e4600480360360a08110156106ed57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001909291908035906020019064010000000081111561075e57600080fd5b82018360208201111561077057600080fd5b8035906020019184600183028401116401000000008311171561079257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061128a565b005b60008073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16141561086d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001806124686029913960400191505060405180910390fd5b6001600083815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109c55780601f1061099a576101008083540402835291602001916109c5565b820191906000526020600020905b8154815290600101906020018083116109a857829003601f168201915b50505050509050919050565b8151835114610a2b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001806124916026913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415610ab1576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061258d6023913960400191505060405180910390fd5b610ab9611600565b73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff161480610aff5750610afe85610af9611600565b6111f6565b5b610b54576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806124b76030913960400191505060405180910390fd5b6000610b5e611600565b9050610b6e818787878787611608565b60008090505b8451811015610d42576000858281518110610b8b57fe5b602002602001015190506000858381518110610ba357fe5b60200260200101519050610c2a816040518060600160405280602881526020016124e7602891396001600086815260200190815260200160002060008d73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546116109092919063ffffffff16565b6001600084815260200190815260200160002060008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550610ce1816001600085815260200190815260200160002060008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546116d090919063ffffffff16565b6001600084815260200190815260200160002060008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505050806001019050610b74565b508473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8787604051808060200180602001838103835285818151815260200191508051906020019060200280838360005b83811015610df2578082015181840152602081019050610dd7565b50505050905001838103825284818151815260200191508051906020019060200280838360005b83811015610e34578082015181840152602081019050610e19565b5050505090500194505050505060405180910390a4610e57818787878787611758565b50505050505050565b60608151835114610ebc576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260278152602001806124416027913960400191505060405180910390fd5b60608351604051908082528060200260200182016040528015610eee5781602001602082028038833980820191505090505b50905060008090505b845181101561103357600073ffffffffffffffffffffffffffffffffffffffff16858281518110610f2457fe5b602002602001015173ffffffffffffffffffffffffffffffffffffffff161415610f99576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f8152602001806125e6602f913960400191505060405180910390fd5b60016000858381518110610fa957fe5b602002602001015181526020019081526020016000206000868381518110610fcd57fe5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482828151811061101c57fe5b602002602001018181525050806001019050610ef7565b508091505092915050565b8173ffffffffffffffffffffffffffffffffffffffff1661105d611600565b73ffffffffffffffffffffffffffffffffffffffff1614156110ca576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602781526020018061250f6027913960400191505060405180910390fd5b80600260006110d7611600565b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff16611184611600565b73ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051808215151515815260200191505060405180910390a35050565b600060036000838152602001908152602001600020549050919050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415611310576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061258d6023913960400191505060405180910390fd5b611318611600565b73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff16148061135e575061135d85611358611600565b6111f6565b5b6113b3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260278152602001806125666027913960400191505060405180910390fd5b60006113bd611600565b90506113dd8187876113ce88611e28565b6113d788611e28565b87611608565b61145a836040518060600160405280602881526020016124e7602891396001600088815260200190815260200160002060008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546116109092919063ffffffff16565b6001600086815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550611511836001600087815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546116d090919063ffffffff16565b6001600086815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f628787604051808381526020018281526020019250505060405180910390a46115f7818787878787611e81565b50505050505050565b600033905090565b505050505050565b60008383111582906116bd576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825283818151815260200191508051906020019080838360005b83811015611682578082015181840152602081019050611667565b50505050905090810190601f1680156116af5780820380516001836020036101000a031916815260200191505b509250505060405180910390fd5b5060008385039050809150509392505050565b60008082840190508381101561174e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60006117798573ffffffffffffffffffffffffffffffffffffffff1661242d565b15611e1d57600115158573ffffffffffffffffffffffffffffffffffffffff166301ffc9a7634e2312e060e01b6040518263ffffffff1660e01b815260040180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060206040518083038186803b15801561181857600080fd5b505afa15801561182c573d6000803e3d6000fd5b505050506040513d602081101561184257600080fd5b810190808051906020019092919050505015151415611aa55760008573ffffffffffffffffffffffffffffffffffffffff1663bc197c8189898888886040518663ffffffff1660e01b8152600401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b8381101561193c578082015181840152602081019050611921565b50505050905001848103835286818151815260200191508051906020019060200280838360005b8381101561197e578082015181840152602081019050611963565b50505050905001848103825285818151815260200191508051906020019080838360005b838110156119bd5780820151818401526020810190506119a2565b50505050905090810190601f1680156119ea5780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015611a0f57600080fd5b505af1158015611a23573d6000803e3d6000fd5b505050506040513d6020811015611a3957600080fd5b8101908080519060200190929190505050905063bc197c8160e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415611aa3576001915050611e1e565b505b600115158573ffffffffffffffffffffffffffffffffffffffff166301ffc9a7637cc2d01760e01b6040518263ffffffff1660e01b815260040180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060206040518083038186803b158015611b3f57600080fd5b505afa158015611b53573d6000803e3d6000fd5b505050506040513d6020811015611b6957600080fd5b810190808051906020019092919050505015151415611dcc5760008573ffffffffffffffffffffffffffffffffffffffff16639b49e33289898888886040518663ffffffff1660e01b8152600401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b83811015611c63578082015181840152602081019050611c48565b50505050905001848103835286818151815260200191508051906020019060200280838360005b83811015611ca5578082015181840152602081019050611c8a565b50505050905001848103825285818151815260200191508051906020019080838360005b83811015611ce4578082015181840152602081019050611cc9565b50505050905090810190601f168015611d115780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015611d3657600080fd5b505af1158015611d4a573d6000803e3d6000fd5b505050506040513d6020811015611d6057600080fd5b81019080805190602001909291905050509050639b49e33260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415611dca576001915050611e1e565b505b6040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260368152602001806125b06036913960400191505060405180910390fd5b5b9695505050505050565b6060806001604051908082528060200260200182016040528015611e5b5781602001602082028038833980820191505090505b5090508281600081518110611e6c57fe5b60200260200101818152505080915050919050565b6000611ea28573ffffffffffffffffffffffffffffffffffffffff1661242d565b1561241e57600060608673ffffffffffffffffffffffffffffffffffffffff1663f23a6e6160e01b8a8a898989604051602401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611f82578082015181840152602081019050611f67565b50505050905090810190601f168015611faf5780820380516001836020036101000a031916815260200191505b509650505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b602083106120485780518252602082019150602081019050602083039250612025565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146120aa576040519150601f19603f3d011682016040523d82523d6000602084013e6120af565b606091505b509150915081801561212a575063f23a6e6160e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19168180602001905160208110156120f857600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b1561213a57600192505050612423565b8673ffffffffffffffffffffffffffffffffffffffff1663e78b332560e01b8a8a898989604051602401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156122115780820151818401526020810190506121f6565b50505050905090810190601f16801561223e5780820380516001836020036101000a031916815260200191505b509650505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b602083106122d757805182526020820191506020810190506020830392506122b4565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114612339576040519150601f19603f3d011682016040523d82523d6000602084013e61233e565b606091505b5080925081935050508180156123bd575063e78b332560e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191681806020019051602081101561238b57600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b156123cd57600192505050612423565b6040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260308152602001806125366030913960400191505060405180910390fd5b600190505b9695505050505050565b600080823b90506000811191505091905056fe4b495033373a206163636f756e747320616e6420696473206c656e677468206d69736d617463684b495033373a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734b495033373a2069647320616e6420616d6f756e7473206c656e677468206d69736d617463684b495033373a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f7665644b495033373a20696e73756666696369656e742062616c616e636520666f72207472616e736665724b495033373a2073657474696e6720617070726f76616c2073746174757320666f722073656c664b495033373a207472616e7366657220746f206e6f6e204b49503337526563656976657220696d706c656d656e7465724b495033373a2063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f7665644b495033373a207472616e7366657220746f20746865207a65726f20616464726573734b495033373a206261746368207472616e7366657220746f206e6f6e204b49503337526563656976657220696d706c656d656e7465724b495033373a2062617463682062616c616e636520717565727920666f7220746865207a65726f2061646472657373a165627a7a72305820b8e912f617ca3aa572692cdbdbfd18a21d3d744a66ca4959e70ba261686593770029";
        static final String byteCodeWithoutBurnablePausable = "0x60806040523480156200001157600080fd5b506040516200422138038062004221833981018060405260208110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b505092919050505080620000a66301ffc9a760e01b6200012760201b60201c565b620000b7816200023060201b60201c565b620000cf636433ca1f60e01b6200012760201b60201c565b620000e7630e89341c60e01b6200012760201b60201c565b5062000108620000fc6200024c60201b60201c565b6200025460201b60201c565b6200012063dfd9d9ec60e01b6200012760201b60201c565b5062000528565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415620001c4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b80600490805190602001906200024892919062000479565b5050565b600033905090565b6200026f816005620002b560201b620038931790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b620002c782826200039960201b60201c565b156200033b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141562000422576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620041ff6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620004bc57805160ff1916838001178555620004ed565b82800160010185558215620004ed579182015b82811115620004ec578251825591602001919060010190620004cf565b5b509050620004fc919062000500565b5090565b6200052591905b808211156200052157600081600090555060010162000507565b5090565b90565b613cc780620005386000396000f3fe608060405234801561001057600080fd5b506004361061010a5760003560e01c806398650275116100a2578063cd53d08e11610071578063cd53d08e146108bc578063cfa84fc11461092a578063d81d0a1514610a80578063e985e9c514610bec578063f242432a14610c685761010a565b806398650275146107c4578063a22cb465146107ce578063aa271e1a1461081e578063bd85b0391461087a5761010a565b80634b068c78116100de5780634b068c78146104a05780634e1273f414610587578063836a104014610728578063983b2d56146107805761010a565b8062fdd58e1461010f57806301ffc9a7146101715780630e89341c146101d65780632eb2c2d61461027d575b600080fd5b61015b6004803603604081101561012557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d77565b6040518082815260200191505060405180910390f35b6101bc6004803603602081101561018757600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610e57565b604051808215151515815260200191505060405180910390f35b610202600480360360208110156101ec57600080fd5b8101908080359060200190929190505050610ebe565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610242578082015181840152602081019050610227565b50505050905090810190601f16801561026f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61049e600480360360a081101561029357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001906401000000008111156102f057600080fd5b82018360208201111561030257600080fd5b8035906020019184602083028401116401000000008311171561032457600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561038457600080fd5b82018360208201111561039657600080fd5b803590602001918460208302840111640100000000831117156103b857600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561041857600080fd5b82018360208201111561042a57600080fd5b8035906020019184600183028401116401000000008311171561044c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610f62565b005b61056d600480360360608110156104b657600080fd5b810190808035906020019092919080359060200190929190803590602001906401000000008111156104e757600080fd5b8201836020820111156104f957600080fd5b8035906020019184600183028401116401000000008311171561051b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506113f1565b604051808215151515815260200191505060405180910390f35b6106d16004803603604081101561059d57600080fd5b81019080803590602001906401000000008111156105ba57600080fd5b8201836020820111156105cc57600080fd5b803590602001918460208302840111640100000000831117156105ee57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561064e57600080fd5b82018360208201111561066057600080fd5b8035906020019184602083028401116401000000008311171561068257600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f8201169050808301925050505050505091929192905050506115f0565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156107145780820151818401526020810190506106f9565b505050509050019250505060405180910390f35b61077e6004803603606081101561073e57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506117ce565b005b6107c26004803603602081101561079657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506118ce565b005b6107cc61193f565b005b61081c600480360360408110156107e457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050611951565b005b6108606004803603602081101561083457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611aec565b604051808215151515815260200191505060405180910390f35b6108a66004803603602081101561089057600080fd5b8101908080359060200190929190505050611b09565b6040518082815260200191505060405180910390f35b6108e8600480360360208110156108d257600080fd5b8101908080359060200190929190505050611b26565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610a7e6004803603606081101561094057600080fd5b81019080803590602001909291908035906020019064010000000081111561096757600080fd5b82018360208201111561097957600080fd5b8035906020019184602083028401116401000000008311171561099b57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156109fb57600080fd5b820183602082011115610a0d57600080fd5b80359060200191846020830284011164010000000083111715610a2f57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050611b59565b005b610bea60048036036060811015610a9657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190640100000000811115610ad357600080fd5b820183602082011115610ae557600080fd5b80359060200191846020830284011164010000000083111715610b0757600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190640100000000811115610b6757600080fd5b820183602082011115610b7957600080fd5b80359060200191846020830284011164010000000083111715610b9b57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050611d00565b005b610c4e60048036036040811015610c0257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611e2e565b604051808215151515815260200191505060405180910390f35b610d75600480360360a0811015610c7e57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019092919080359060200190640100000000811115610cef57600080fd5b820183602082011115610d0157600080fd5b80359060200191846001830284011164010000000083111715610d2357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611ec2565b005b60008073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610dfe576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180613a536029913960400191505060405180910390fd5b6001600083815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060048054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f565780601f10610f2b57610100808354040283529160200191610f56565b820191906000526020600020905b815481529060010190602001808311610f3957829003601f168201915b50505050509050919050565b8151835114610fbc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180613aa56026913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415611042576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526023815260200180613c146023913960400191505060405180910390fd5b61104a612238565b73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff161480611090575061108f8561108a612238565b611e2e565b5b6110e5576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613acb6030913960400191505060405180910390fd5b60006110ef612238565b90506110ff818787878787612240565b60008090505b84518110156112d357600085828151811061111c57fe5b60200260200101519050600085838151811061113457fe5b602002602001015190506111bb81604051806060016040528060288152602001613afb602891396001600086815260200190815260200160002060008d73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546122489092919063ffffffff16565b6001600084815260200190815260200160002060008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550611272816001600085815260200190815260200160002060008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461230890919063ffffffff16565b6001600084815260200190815260200160002060008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505050806001019050611105565b508473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8787604051808060200180602001838103835285818151815260200191508051906020019060200280838360005b83811015611383578082015181840152602081019050611368565b50505050905001838103825284818151815260200191508051906020019060200280838360005b838110156113c55780820151818401526020810190506113aa565b5050505090500194505050505060405180910390a46113e8818787878787612390565b50505050505050565b60006114036113fe612238565b611aec565b611458576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613b236030913960400191505060405180910390fd5b61146184612a60565b156114d4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f4b495033373a20746f6b656e20616c726561647920637265617465640000000081525060200191505060405180910390fd5b336006600086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061154133858560405180602001604052806000815250612ad2565b6000825111156115e957837f6bb7ff708619ba0610cba295a58592e0451dee2622938c8755667688daf3529b836040518080602001828103825283818151815260200191508051906020019080838360005b838110156115ae578082015181840152602081019050611593565b50505050905090810190601f1680156115db5780820380516001836020036101000a031916815260200191505b509250505060405180910390a25b9392505050565b6060815183511461164c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526027815260200180613a2c6027913960400191505060405180910390fd5b6060835160405190808252806020026020018201604052801561167e5781602001602082028038833980820191505090505b50905060008090505b84518110156117c357600073ffffffffffffffffffffffffffffffffffffffff168582815181106116b457fe5b602002602001015173ffffffffffffffffffffffffffffffffffffffff161415611729576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f815260200180613c6d602f913960400191505060405180910390fd5b6001600085838151811061173957fe5b60200260200101518152602001908152602001600020600086838151811061175d57fe5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548282815181106117ac57fe5b602002602001018181525050806001019050611687565b508091505092915050565b6117de6117d9612238565b611aec565b611833576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613b236030913960400191505060405180910390fd5b61183c83612a60565b6118ae576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4b495033373a206e6f6e6578697374656e7420746f6b656e000000000000000081525060200191505060405180910390fd5b6118c982848360405180602001604052806000815250612ad2565b505050565b6118de6118d9612238565b611aec565b611933576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613b236030913960400191505060405180910390fd5b61193c81612d30565b50565b61194f61194a612238565b612d8a565b565b8173ffffffffffffffffffffffffffffffffffffffff16611970612238565b73ffffffffffffffffffffffffffffffffffffffff1614156119dd576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526027815260200180613b746027913960400191505060405180910390fd5b80600260006119ea612238565b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff16611a97612238565b73ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051808215151515815260200191505060405180910390a35050565b6000611b02826005612de490919063ffffffff16565b9050919050565b600060036000838152602001908152602001600020549050919050565b60066020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b611b69611b64612238565b611aec565b611bbe576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613b236030913960400191505060405180910390fd5b611bc783612a60565b611c39576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4b495033373a206e6f6e6578697374656e7420746f6b656e000000000000000081525060200191505060405180910390fd5b8051825114611c93576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180613a7c6029913960400191505060405180910390fd5b60008090505b8251811015611cfa576000838281518110611cb057fe5b602002602001015190506000838381518110611cc857fe5b60200260200101519050611ced82878360405180602001604052806000815250612ad2565b5050806001019050611c99565b50505050565b611d10611d0b612238565b611aec565b611d65576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613b236030913960400191505060405180910390fd5b60008090505b8251811015611e0d57611d90838281518110611d8357fe5b6020026020010151612a60565b611e02576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4b495033373a206e6f6e6578697374656e7420746f6b656e000000000000000081525060200191505060405180910390fd5b806001019050611d6b565b50611e2983838360405180602001604052806000815250612ec2565b505050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415611f48576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526023815260200180613c146023913960400191505060405180910390fd5b611f50612238565b73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff161480611f965750611f9585611f90612238565b611e2e565b5b611feb576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526027815260200180613bed6027913960400191505060405180910390fd5b6000611ff5612238565b90506120158187876120068861327b565b61200f8861327b565b87612240565b61209283604051806060016040528060288152602001613afb602891396001600088815260200190815260200160002060008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546122489092919063ffffffff16565b6001600086815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550612149836001600087815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461230890919063ffffffff16565b6001600086815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f628787604051808381526020018281526020019250505060405180910390a461222f8187878787876132d4565b50505050505050565b600033905090565b505050505050565b60008383111582906122f5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825283818151815260200191508051906020019080838360005b838110156122ba57808201518184015260208101905061229f565b50505050905090810190601f1680156122e75780820380516001836020036101000a031916815260200191505b509250505060405180910390fd5b5060008385039050809150509392505050565b600080828401905083811015612386576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60006123b18573ffffffffffffffffffffffffffffffffffffffff16613880565b15612a5557600115158573ffffffffffffffffffffffffffffffffffffffff166301ffc9a7634e2312e060e01b6040518263ffffffff1660e01b815260040180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060206040518083038186803b15801561245057600080fd5b505afa158015612464573d6000803e3d6000fd5b505050506040513d602081101561247a57600080fd5b8101908080519060200190929190505050151514156126dd5760008573ffffffffffffffffffffffffffffffffffffffff1663bc197c8189898888886040518663ffffffff1660e01b8152600401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b83811015612574578082015181840152602081019050612559565b50505050905001848103835286818151815260200191508051906020019060200280838360005b838110156125b657808201518184015260208101905061259b565b50505050905001848103825285818151815260200191508051906020019080838360005b838110156125f55780820151818401526020810190506125da565b50505050905090810190601f1680156126225780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b15801561264757600080fd5b505af115801561265b573d6000803e3d6000fd5b505050506040513d602081101561267157600080fd5b8101908080519060200190929190505050905063bc197c8160e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614156126db576001915050612a56565b505b600115158573ffffffffffffffffffffffffffffffffffffffff166301ffc9a7637cc2d01760e01b6040518263ffffffff1660e01b815260040180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060206040518083038186803b15801561277757600080fd5b505afa15801561278b573d6000803e3d6000fd5b505050506040513d60208110156127a157600080fd5b810190808051906020019092919050505015151415612a045760008573ffffffffffffffffffffffffffffffffffffffff16639b49e33289898888886040518663ffffffff1660e01b8152600401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b8381101561289b578082015181840152602081019050612880565b50505050905001848103835286818151815260200191508051906020019060200280838360005b838110156128dd5780820151818401526020810190506128c2565b50505050905001848103825285818151815260200191508051906020019080838360005b8381101561291c578082015181840152602081019050612901565b50505050905090810190601f1680156129495780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b15801561296e57600080fd5b505af1158015612982573d6000803e3d6000fd5b505050506040513d602081101561299857600080fd5b81019080805190602001909291905050509050639b49e33260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415612a02576001915050612a56565b505b6040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526036815260200180613c376036913960400191505060405180910390fd5b5b9695505050505050565b6000806006600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415915050919050565b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415612b75576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4b495033373a206d696e7420746f20746865207a65726f20616464726573730081525060200191505060405180910390fd5b6000612b7f612238565b9050612ba081600087612b918861327b565b612b9a8861327b565b87612240565b612c03836001600087815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461230890919063ffffffff16565b6001600086815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550612c7d83600360008781526020019081526020016000205461230890919063ffffffff16565b60036000868152602001908152602001600020819055508473ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f628787604051808381526020018281526020019250505060405180910390a4612d28816000878787876132d4565b505050505050565b612d4481600561389390919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b612d9e81600561396e90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415612e6b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180613b9b6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161415612f65576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4b495033373a206d696e7420746f20746865207a65726f20616464726573730081525060200191505060405180910390fd5b8151835114612fbf576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180613aa56026913960400191505060405180910390fd5b6000612fc9612238565b9050612fda81600087878787612240565b60008090505b845181101561315c5761307260016000878481518110612ffc57fe5b6020026020010151815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205485838151811061305c57fe5b602002602001015161230890919063ffffffff16565b6001600087848151811061308257fe5b6020026020010151815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550613125600360008784815181106130ec57fe5b602002602001015181526020019081526020016000205485838151811061310f57fe5b602002602001015161230890919063ffffffff16565b6003600087848151811061313557fe5b60200260200101518152602001908152602001600020819055508080600101915050612fe0565b508473ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8787604051808060200180602001838103835285818151815260200191508051906020019060200280838360005b8381101561320d5780820151818401526020810190506131f2565b50505050905001838103825284818151815260200191508051906020019060200280838360005b8381101561324f578082015181840152602081019050613234565b5050505090500194505050505060405180910390a461327381600087878787612390565b505050505050565b60608060016040519080825280602002602001820160405280156132ae5781602001602082028038833980820191505090505b50905082816000815181106132bf57fe5b60200260200101818152505080915050919050565b60006132f58573ffffffffffffffffffffffffffffffffffffffff16613880565b1561387157600060608673ffffffffffffffffffffffffffffffffffffffff1663f23a6e6160e01b8a8a898989604051602401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156133d55780820151818401526020810190506133ba565b50505050905090810190601f1680156134025780820380516001836020036101000a031916815260200191505b509650505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b6020831061349b5780518252602082019150602081019050602083039250613478565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146134fd576040519150601f19603f3d011682016040523d82523d6000602084013e613502565b606091505b509150915081801561357d575063f23a6e6160e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191681806020019051602081101561354b57600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b1561358d57600192505050613876565b8673ffffffffffffffffffffffffffffffffffffffff1663e78b332560e01b8a8a898989604051602401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015613664578082015181840152602081019050613649565b50505050905090810190601f1680156136915780820380516001836020036101000a031916815260200191505b509650505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b6020831061372a5780518252602082019150602081019050602083039250613707565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d806000811461378c576040519150601f19603f3d011682016040523d82523d6000602084013e613791565b606091505b508092508193505050818015613810575063e78b332560e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19168180602001905160208110156137de57600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b1561382057600192505050613876565b6040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180613bbd6030913960400191505060405180910390fd5b600190505b9695505050505050565b600080823b905060008111915050919050565b61389d8282612de4565b15613910576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b6139788282612de4565b6139cd576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180613b536021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550505056fe4b495033373a206163636f756e747320616e6420696473206c656e677468206d69736d617463684b495033373a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734b495033373a20746f4c69737420616e64205f76616c756573206c656e677468206d69736d617463684b495033373a2069647320616e6420616d6f756e7473206c656e677468206d69736d617463684b495033373a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f7665644b495033373a20696e73756666696369656e742062616c616e636520666f72207472616e736665724d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b495033373a2073657474696e6720617070726f76616c2073746174757320666f722073656c66526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b495033373a207472616e7366657220746f206e6f6e204b49503337526563656976657220696d706c656d656e7465724b495033373a2063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f7665644b495033373a207472616e7366657220746f20746865207a65726f20616464726573734b495033373a206261746368207472616e7366657220746f206e6f6e204b49503337526563656976657220696d706c656d656e7465724b495033373a2062617463682062616c616e636520717565727920666f7220746865207a65726f2061646472657373a165627a7a72305820ce694c8c35830759ea8ac17445c54a2799118c21020c02014c942aa3dd6570100029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
        static final String byteCodeNotSupportedKIP13 = "0x608060405234801561001057600080fd5b5061051f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063693ec85e1461003b578063e942b5161461016f575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c1565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102bf6004803603604081101561018557600080fd5b81019080803590602001906401000000008111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111640100000000831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561023957600080fd5b82018360208201111561024b57600080fd5b8035906020019184600183028401116401000000008311171561026d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103cc565b005b60606000826040518082805190602001908083835b602083106102f957805182526020820191506020810190506020830392506102d6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c05780601f10610395576101008083540402835291602001916103c0565b820191906000526020600020905b8154815290600101906020018083116103a357829003601f168201915b50505050509050919050565b806000836040518082805190602001908083835b6020831061040357805182526020820191506020810190506020830392506103e0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061044992919061044e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061048f57805160ff19168380011785556104bd565b828001600101855582156104bd579182015b828111156104bc5782518255916020019190600101906104a1565b5b5090506104ca91906104ce565b5090565b6104f091905b808211156104ec5760008160009055506001016104d4565b5090565b9056fea165627a7a723058203ffebc792829e0434ecc495da1b53d24399cd7fff506a4fd03589861843e14990029";
        static final String abi_mintable = "[\n" +
                "  {\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"uri\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"payable\":false,\n" +
                "    \"stateMutability\":\"nonpayable\",\n" +
                "    \"type\":\"constructor\"\n" +
                "  }\n" +
                "]";

        static final String abi_without_pausable_burnable = "[\n" +
                "  {\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"uri\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"payable\":false,\n" +
                "    \"stateMutability\":\"nonpayable\",\n" +
                "    \"type\":\"constructor\"\n" +
                "  }\n" +
                "]";

        static final String abi_not_supported_kip13 = "[\n" +
                "  {\n" +
                "    \"constant\":true,\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"key\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"name\":\"get\",\n" +
                "    \"outputs\":[\n" +
                "      {\n" +
                "        \"name\":\"\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"payable\":false,\n" +
                "    \"stateMutability\":\"view\",\n" +
                "    \"type\":\"function\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"constant\":false,\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"key\",\n" +
                "        \"type\":\"string\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"value\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"name\":\"set\",\n" +
                "    \"outputs\":[],\n" +
                "    \"payable\":false,\n" +
                "    \"stateMutability\":\"nonpayable\",\n" +
                "    \"type\":\"function\"\n" +
                "  }\n" +
                "]";


        @BeforeClass
        public static void initCaver() throws Exception {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            kip37 = deployContract(tokenURI);
        }

        @Test
        public void detectInterface() {
            Map<String, Boolean> result = kip37.detectInterface();
            assertTrue(result.get(KIP37.INTERFACE.IKIP37.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_BURNABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_METADATA.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_MINTABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_PAUSABLE.getName()));

            result.entrySet().forEach(entry -> System.out.println("Interface ID : " + entry.getKey() + " - " + entry.getValue()));
        }

        @Test
        public void detectInterface_staticMethod() {
            Map<String, Boolean> result = caver.kct.kip37.detectInterface(kip37.getContractAddress());
            assertTrue(result.get(KIP37.INTERFACE.IKIP37.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_BURNABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_METADATA.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_MINTABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_PAUSABLE.getName()));
        }

        @Test
        public void only_mintable() throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            Contract contract = new Contract(caver, abi_mintable);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeWithMintable, "uri");

            Map<String, Boolean> result = caver.kct.kip37.detectInterface(contract.getContractAddress());
            assertTrue(result.get(KIP37.INTERFACE.IKIP37.getName()));
            assertFalse(result.get(KIP37.INTERFACE.IKIP37_BURNABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_METADATA.getName()));
            assertFalse(result.get(KIP37.INTERFACE.IKIP37_MINTABLE.getName()));
            assertFalse(result.get(KIP37.INTERFACE.IKIP37_PAUSABLE.getName()));
        }

        @Test
        public void withoutBurnable_Pausable() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            Contract contract = new Contract(caver, abi_without_pausable_burnable);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeWithoutBurnablePausable, "uri");

            Map<String, Boolean> result = caver.kct.kip37.detectInterface(contract.getContractAddress());
            assertTrue(result.get(KIP37.INTERFACE.IKIP37.getName()));
            assertFalse(result.get(KIP37.INTERFACE.IKIP37_BURNABLE.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_METADATA.getName()));
            assertTrue(result.get(KIP37.INTERFACE.IKIP37_MINTABLE.getName()));
            assertFalse(result.get(KIP37.INTERFACE.IKIP37_PAUSABLE.getName()));
        }

        @Test
        public void notSupportedKIP13() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This contract does not support KIP-13.");

            Contract contract = new Contract(caver, abi_not_supported_kip13);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeNotSupportedKIP13);

            Map<String, Boolean> result = caver.kct.kip37.detectInterface(contract.getContractAddress());
        }
    }

    public static class UriTest {
        static Caver caver;

        @BeforeClass
        public static void initCaver() throws Exception {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        }

        @Test
        public void uriWithID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String expected = "https://token-cdn-domain/000000000000000000000000000000000000000000000000000000000004cce0.json";

            String tokenURI = "https://token-cdn-domain/{id}.json";
            KIP37 kip37 = caver.kct.kip37.deploy(tokenURI, LUMAN.getAddress());

            String uriFromStr = kip37.uri("0x4cce0");
            assertEquals(expected, uriFromStr);

            String uriFromInt = kip37.uri(BigInteger.valueOf(314592));
            assertEquals(expected, uriFromInt);
        }

        @Test
        public void uriWithoutID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String tokenURI = "https://token-cdn-domain/example.json";
            KIP37 kip37 = caver.kct.kip37.deploy(tokenURI, LUMAN.getAddress());

            String uriFromStr = kip37.uri("0x4cce0");
            assertEquals(tokenURI, uriFromStr);

            String uriFromInt = kip37.uri(BigInteger.valueOf(314592));
            assertEquals(tokenURI, uriFromInt);
        }
    }

    public static class FeeDelegationTest {
        static Caver caver;
        static String contractAddress;
        static String sender;
        static String feePayer;

        @BeforeClass
        public static void init() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.create(LUMAN.getAddress(), "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            caver.wallet.add(caver.wallet.keyring.create(BRANDON.getAddress(), "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

            sender = LUMAN.getAddress();
            feePayer = BRANDON.getAddress();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(sender);
            sendOptions.setGas(BigInteger.valueOf(3000000));

            KIP37 kip37 = caver.kct.kip37.deploy(tokenURI, sender);
            contractAddress = kip37.getContractAddress();
        }

        @Test
        public void deploy() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            KIP37 kip37 = caver.kct.kip37.deploy(tokenURI, sendOptions);
            assertNotNull(kip37.getContractAddress());
        }

        @Test
        public void addMinter_FD() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            KIP37 kip37 = caver.kct.kip37.create(contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());

            TransactionReceipt.TransactionReceiptData receiptData = kip37.addMinter(caver.wallet.keyring.generate().getAddress(), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), receiptData.getType());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
        }

        @Test
        public void addMinter_FDWithRatio() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            KIP37 kip37 = caver.kct.kip37.create(contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            TransactionReceipt.TransactionReceiptData receiptData = kip37.addMinter(caver.wallet.keyring.generate().getAddress(), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), receiptData.getType());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
            assertEquals(sendOptions.getFeeRatio(), receiptData.getFeeRatio());
        }

        @Test
        public void addMinter_defaultOptions_FDWithRatio() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            SendOptions defaultSendOptions = new SendOptions();
            defaultSendOptions.setFrom(LUMAN.getAddress());
            defaultSendOptions.setGas(BigInteger.valueOf(10000000));
            defaultSendOptions.setFeeDelegation(true);
            defaultSendOptions.setFeePayer(BRANDON.getAddress());
            defaultSendOptions.setFeeRatio(BigInteger.valueOf(10));

            KIP37 kip37 = caver.kct.kip37.create(contractAddress);
            kip37.setDefaultSendOptions(defaultSendOptions);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.addMinter(caver.wallet.keyring.generate().getAddress());

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), receiptData.getType());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals(defaultSendOptions.getFrom(), receiptData.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(defaultSendOptions.getFeePayer(), receiptData.getFeePayer());
            assertEquals(defaultSendOptions.getFeeRatio(), receiptData.getFeeRatio());
        }

        @Test
        public void addMinter_overwrite_defaultOptions_FDWithRatio() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            SendOptions defaultOptions = new SendOptions();
            defaultOptions.setFrom(LUMAN.getAddress());
            defaultOptions.setGas(BigInteger.valueOf(10000000));
            defaultOptions.setFeeDelegation(false);
            defaultOptions.setFeePayer(BRANDON.getAddress());
            defaultOptions.setFeeRatio(BigInteger.valueOf(10));

            KIP37 kip37 = caver.kct.kip37.create(contractAddress);
            kip37.setDefaultSendOptions(defaultOptions);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());
            options.setFeeRatio(BigInteger.valueOf(30));

            TransactionReceipt.TransactionReceiptData receiptData = kip37.addMinter(caver.wallet.keyring.generate().getAddress(), options);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecutionWithRatio.toString(), receiptData.getType());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals(options.getFrom(), receiptData.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(options.getGas(), receiptData.getGas());
            assertEquals(options.getFeePayer(), receiptData.getFeePayer());
            assertEquals(options.getFeeRatio(), receiptData.getFeeRatio());
        }

        @Test
        public void sign_constructor() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException {
            KIP37 kip37 = caver.kct.kip37.create();

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = (AbstractFeeDelegatedTransaction)kip37.sign(options, "constructor", KIP37ConstantData.BINARY, tokenURI);
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractDeploy.toString(), signed.getType());
            assertEquals(options.getFrom(), signed.getFrom());
            assertEquals(options.getFeePayer(), signed.getFeePayer());
            assertFalse(Utils.isEmptySig(signed.getSignatures()));
            assertTrue(Utils.isEmptySig(signed.getFeePayerSignatures()));

            caver.wallet.signAsFeePayer(feePayer, signed);

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(signed).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertNotNull(receiptData.getContractAddress());
            assertEquals("0x1", receiptData.getStatus());
        }

        @Test
        public void sign_addMinter() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException {
            KIP37 kip37 = caver.kct.kip37.create(contractAddress);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = (AbstractFeeDelegatedTransaction)kip37.sign(options, "addMinter", caver.wallet.keyring.generate().getAddress());
            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), signed.getType());
            assertEquals(options.getFrom(), signed.getFrom());
            assertEquals(options.getFeePayer(), signed.getFeePayer());
            assertFalse(Utils.isEmptySig(signed.getSignatures()));
            assertTrue(Utils.isEmptySig(signed.getFeePayerSignatures()));

            caver.wallet.signAsFeePayer(feePayer, signed);

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(signed).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
        }

        @Test
        public void signAsFeePayer_addMinter() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TransactionException {
            KIP37 kip37 = caver.kct.kip37.create(contractAddress);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = kip37.signAsFeePayer(options, "addMinter", caver.wallet.keyring.generate().getAddress());

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), signed.getType());
            assertEquals(options.getFrom(), signed.getFrom());
            assertEquals(options.getFeePayer(), signed.getFeePayer());
            assertTrue(Utils.isEmptySig(signed.getSignatures()));
            assertFalse(Utils.isEmptySig(signed.getFeePayerSignatures()));

            caver.wallet.sign(sender, signed);

            Bytes32 txHash = caver.rpc.klay.sendRawTransaction(signed).send();
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
            assertEquals("0x1", receiptData.getStatus());
        }
    }

    public static class decodeFunctionCall {

        public void checkTypeAndValue(List<Object> expected, List<Type> actual) {
            assertEquals(expected.size(), actual.size());

            for(int i=0; i<actual.size(); i++) {
                assertEquals(expected.get(i), actual.get(i).getValue());
            }
        }

        @Test
        public void decodeFunctionCall() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            KIP37 kip37 = caver.kct.kip37.create();

            List<Object> expected = new ArrayList<>();
            String encoded = kip37.encodeABI("pause");
            List<Type> actual = kip37.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(LUMAN.getAddress(), BigInteger.ONE);
            encoded = kip37.encodeABI("balanceOf", LUMAN.getAddress(), BigInteger.ONE);
            actual = kip37.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(LUMAN.getAddress());
            encoded = kip37.encodeABI("isMinter", LUMAN.getAddress());
            actual = kip37.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(LUMAN.getAddress(), BRANDON.getAddress(), BigInteger.valueOf(2), BigInteger.valueOf(100), "0x1234");
            encoded = kip37.encodeABI("safeTransferFrom", LUMAN.getAddress(), BRANDON.getAddress(),  BigInteger.valueOf(2), BigInteger.valueOf(100), "0x1234");
            actual = kip37.decodeFunctionCall(encoded);
            assertEquals(expected.get(0), actual.get(0).getValue());
            assertEquals(expected.get(1), actual.get(1).getValue());
            assertEquals(expected.get(2), actual.get(2).getValue());
            assertEquals(expected.get(3), actual.get(3).getValue());
            assertEquals(expected.get(4), Numeric.toHexString((byte[]) actual.get(4).getValue()));
        }

        @Test
        public void decodeFunctionCall_OverrideFunction() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            KIP37 kip37 = caver.kct.kip37.create();

            BigInteger tokenID = BigInteger.ONE;
            Uint256 tokenIdSol = new Uint256(tokenID);
            Address to = new Address(BRANDON.getAddress());
            Uint256 amount = new Uint256(BigInteger.ONE);

            String encoded = kip37.encodeABIWithSolidityType("mint", tokenIdSol, to, amount);
            List<Type> actual = kip37.decodeFunctionCall(encoded);

            assertEquals(tokenIdSol, actual.get(0));
            assertEquals(to, actual.get(1));
            assertEquals(amount, actual.get(2));


            String[] toArr = new String[] {WAYNE.getAddress(), BRANDON.getAddress()};
            List<Address> addressList = Arrays.stream(toArr).map(Address::new).collect(Collectors.toList());
            DynamicArray<Address> toListSol = new DynamicArray<Address>(Address.class, addressList);

            BigInteger[] mintAmountArr = new BigInteger[] {BigInteger.TEN, BigInteger.TEN};
            List<Uint256> valueList = Arrays.stream(mintAmountArr).map(Uint256::new).collect(Collectors.toList());
            DynamicArray<Uint256> valuesSol = new DynamicArray<Uint256>(Uint256.class, valueList);

            encoded = kip37.encodeABIWithSolidityType("mint", tokenIdSol, toListSol, valuesSol);
            actual = kip37.decodeFunctionCall(encoded);

            assertEquals(tokenIdSol, actual.get(0));
            assertEquals(toListSol, actual.get(1));
            assertEquals(valuesSol, actual.get(2));
        }
    }


}
