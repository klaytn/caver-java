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
package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip37.KIP37;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.klaytn.caver.base.Accounts.*;
import static org.junit.Assert.*;

public class KIP37Test {
    public static KIP37 kip37;
    private static final String tokenURI = "https://kip37.example/item.json";

    public static void deployContract() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        kip37 = KIP37.deploy(caver, tokenURI, LUMAN.getAddress());
    }

    public static boolean createToken(KIP37 kip37, BigInteger tokenId, BigInteger initialSupply, String URI, String sender) throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        TransactionReceipt.TransactionReceiptData receiptData = kip37.create(tokenId, initialSupply, tokenURI, new SendOptions(sender));
        return receiptData.getStatus().equals("0x1");
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void deploy() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            KIP37 contract = KIP37.deploy(caver, tokenURI, LUMAN.getAddress());

            assertNotNull(contract.getContractAddress());
        }

        @Test
        public void uri() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String expected = tokenURI;
            String tokenUri = kip37.uri(BigInteger.valueOf(1));
            assertEquals(expected, tokenUri);
        }
    }

    public static class MintableTest {
        @BeforeClass
        public static void initContract() throws Exception {
            deployContract();

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
            deployContract();
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
            deployContract();
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
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
            if(!createToken(kip37, BigInteger.ZERO, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.ONE, BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(2), BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }

            if(!createToken(kip37, BigInteger.valueOf(3), BigInteger.valueOf(1000), tokenURI, LUMAN.getAddress())) {
                fail();
            }
        }

        @Test
        public void totalSupply() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger total = kip37.totalSupply(BigInteger.ZERO);
            assertEquals(BigInteger.valueOf(1000), total);
        }

        @Test
        public void totalSupplyStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger total = kip37.totalSupply("0x0");
            assertEquals(BigInteger.valueOf(1000), total);
        }

        @Test
        public void balanceOf() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger balance = kip37.balanceOf(LUMAN.getAddress(), BigInteger.ZERO);
            assertEquals(BigInteger.valueOf(1000), balance);
        }

        @Test
        public void balanceOfStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger balance = kip37.balanceOf(LUMAN.getAddress(), "0x0");
            assertEquals(BigInteger.valueOf(1000), balance);
        }

        @Test
        public void balanceOfBatch() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] accounts = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.ZERO, BigInteger.valueOf(1)};

            List<BigInteger> balances = kip37.balanceOfBatch(accounts, tokenIds);
            assertEquals(BigInteger.valueOf(1000), balances.get(0));
            assertEquals(BigInteger.valueOf(1000), balances.get(1));
        }

        @Test
        public void balanceOfBatchStringID() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String[] accounts = new String[] {LUMAN.getAddress(), LUMAN.getAddress()};
            String[] tokenIds = new String[] {"0x0", "0x1"};

            List<BigInteger> balances = kip37.balanceOfBatch(accounts, tokenIds);
            assertEquals(BigInteger.valueOf(1000), balances.get(0));
            assertEquals(BigInteger.valueOf(1000), balances.get(1));
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
            String to = KeyringFactory.generate().getAddress();
            BigInteger tokenId = BigInteger.valueOf(2);
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, "", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeTransferFromStringID() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            String from = LUMAN.getAddress();
            String to = KeyringFactory.generate().getAddress();
            String tokenId = "0x2";
            BigInteger values = BigInteger.valueOf(100);

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeTransferFrom(from, to, tokenId, values, "", new SendOptions(LUMAN.getAddress()));
            if(!receiptData.getStatus().equals("0x1")) {
                fail();
            }

            assertEquals(BigInteger.valueOf(100), kip37.balanceOf(to, BigInteger.valueOf(2)));
        }

        @Test
        public void safeBatchTransferFrom() throws NoSuchMethodException, TransactionException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
            BigInteger[] tokenIds = new BigInteger[] {BigInteger.valueOf(2), BigInteger.valueOf(3)};
            BigInteger[] values = new BigInteger[] {BigInteger.valueOf(100), BigInteger.valueOf(100)};

            String to = KeyringFactory.generate().getAddress();

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

            String to = KeyringFactory.generate().getAddress();

            TransactionReceipt.TransactionReceiptData receiptData = kip37.safeBatchTransferFrom(LUMAN.getAddress(), to, tokenIds, values, "data", new SendOptions(LUMAN.getAddress()));
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
            String to = KeyringFactory.generate().getAddress();

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
            String to = KeyringFactory.generate().getAddress();

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
            String to = KeyringFactory.generate().getAddress();

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
            String to = KeyringFactory.generate().getAddress();

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
}
