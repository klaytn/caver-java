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

package com.klaytn.caver.common.kct.kip17;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17ConstantData;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class KIP17Test {
    public static KIP17 kip17Contract;
    public static final String CONTRACT_NAME = "NFT";
    public static final String CONTRACT_SYMBOL = "NFT_KALE";
    private static final String sTokenURI = "https://game.example/item-id-8u5h2m.json";

    public static void deployContract() throws Exception {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        KIP17DeployParams kip17DeployParam = new KIP17DeployParams(CONTRACT_NAME, CONTRACT_SYMBOL);
        kip17Contract = caver.kct.kip17.deploy(kip17DeployParam, LUMAN.getAddress());
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void deploy() throws Exception {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(caver.wallet.keyring.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            KIP17 contract = caver.kct.kip17.deploy(LUMAN.getAddress(), CONTRACT_NAME, CONTRACT_SYMBOL);

            assertNotNull(contract.getContractAddress());
        }

        @Test
        public void name(){
            try {
                String name = kip17Contract.name();
                assertEquals(CONTRACT_NAME, name);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void symbol(){
            try {
                String symbol = kip17Contract.symbol();
                assertEquals(CONTRACT_SYMBOL, symbol);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void totalSupply(){
            try {
                BigInteger totalSupply = kip17Contract.totalSupply();
                assertEquals(BigInteger.ZERO, totalSupply);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void cloneTestWithSetWallet() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.generate(3);
            KIP17 kip17 = caver.kct.kip17.create();
            KIP17 cloned = kip17.clone();

            assertEquals(3, ((KeyringContainer)cloned.getWallet()).length());
        }
    }

    public static class PausableTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Before
        public void setUnpause() {
            try {
                kip17Contract.setDefaultSendOptions(new SendOptions());
                if(kip17Contract.paused()) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    TransactionReceipt.TransactionReceiptData receiptData = kip17Contract.unpause(options);
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
                kip17Contract.pause(options);
                assertTrue(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedDefaultOptions() {
            try{
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip17Contract.setDefaultSendOptions(options);
                kip17Contract.pause();
                assertTrue(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoDefaultGas() {
            try {
                kip17Contract.getDefaultSendOptions().setFrom(LUMAN.getAddress());
                kip17Contract.pause();
                assertTrue(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoGas() {
            try {
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                kip17Contract.pause(sendOptions);
                assertTrue(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void unPause() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip17Contract.pause(options);
                assertTrue(kip17Contract.paused());

                kip17Contract.unpause(options);
                assertFalse(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addPauser() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip17Contract.addPauser(BRANDON.getAddress(), options);

                assertTrue(kip17Contract.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renouncePauser() {
            try {
                if(!kip17Contract.isPauser(BRANDON.getAddress())) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    kip17Contract.addPauser(BRANDON.getAddress(), options);
                }

                SendOptions options = new SendOptions(BRANDON.getAddress(), BigInteger.valueOf(4000000));
                kip17Contract.renouncePauser(options);

                assertFalse(kip17Contract.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void paused() {
            try {
                assertFalse(kip17Contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class BurnableTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void burn() {
            try {
                BigInteger tokenID = BigInteger.valueOf(0);
                SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                SendOptions userOptions = new SendOptions(BRANDON.getAddress(), (String)null);
                kip17Contract.mint(BRANDON.getAddress(), tokenID, ownerOptions);

                String address = kip17Contract.ownerOf(tokenID);
                assertEquals(BRANDON.getAddress(), address);

                kip17Contract.burn(tokenID, userOptions);
                BigInteger balance = kip17Contract.balanceOf(BRANDON.getAddress());

                assertEquals(BigInteger.ZERO, balance);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class MintableTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void mint() {
            try {
                BigInteger beforeTotalSupply = kip17Contract.totalSupply();

                BigInteger tokenId = BigInteger.valueOf(0);
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);


                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions);

                BigInteger afterTotalSupply = kip17Contract.totalSupply();
                assertEquals(afterTotalSupply, beforeTotalSupply.add(BigInteger.valueOf(1)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void mintWithTokenURI() {
            BigInteger tokenId = BigInteger.ONE;
            SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            try {
                kip17Contract.mintWithTokenURI(LUMAN.getAddress(), tokenId, sTokenURI, sendOptions);
                String uri = kip17Contract.tokenURI(tokenId);
                assertEquals(uri, sTokenURI);

                String tokenOwnerAddr = kip17Contract.ownerOf(tokenId);
                assertEquals(LUMAN.getAddress(), tokenOwnerAddr);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void isMinter() {
            try {
                assertTrue(kip17Contract.isMinter(LUMAN.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addMinter() {
            try {
                if(kip17Contract.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(BRANDON.getAddress(), (String)null);
                    kip17Contract.renounceMinter(sendOptions);
                }

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);

                kip17Contract.addMinter(BRANDON.getAddress(), sendOptions);

                assertTrue(kip17Contract.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renounceMinter() {
            try {
                if(!kip17Contract.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                    kip17Contract.addMinter(BRANDON.getAddress(), sendOptions);
                }

                SendOptions sendOptions = new SendOptions(BRANDON.getAddress(), (String)null);
                kip17Contract.renounceMinter(sendOptions);

                assertFalse(kip17Contract.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class EnumerableTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void totalSupply() {
            try {
                BigInteger tokenId = BigInteger.ZERO;
                BigInteger preTotalCount = kip17Contract.totalSupply();

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions);

                BigInteger afterTotalCount = kip17Contract.totalSupply();
                assertEquals(afterTotalCount, preTotalCount.add(BigInteger.ONE));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void tokenByIndex() {
            try {
                BigInteger[] tokenIds = new BigInteger[]{BigInteger.valueOf(7000), BigInteger.valueOf(7001), BigInteger.valueOf(7002)};
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);

                for(int i=0; i<tokenIds.length; i++) {
                    kip17Contract.mint(LUMAN.getAddress(), tokenIds[i], sendOptions);
                }

                BigInteger total = kip17Contract.totalSupply();

                BigInteger index = total.subtract(BigInteger.ONE);
                BigInteger tokenId = kip17Contract.tokenByIndex(index);
                assertEquals(tokenIds[2], tokenId);

                index = index.subtract(BigInteger.ONE);
                tokenId = kip17Contract.tokenByIndex(index);
                assertEquals(tokenIds[1], tokenId);

                index = index.subtract(BigInteger.ONE);
                tokenId = kip17Contract.tokenByIndex(index);
                assertEquals(tokenIds[0], tokenId);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void tokenOfOwnerByIndex() {
            try {
                BigInteger[] tokenIds = new BigInteger[]{BigInteger.valueOf(8000), BigInteger.valueOf(8001), BigInteger.valueOf(8002)};
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);

                for(int i=0; i<tokenIds.length; i++) {
                    kip17Contract.mint(BRANDON.getAddress(), tokenIds[i], sendOptions);
                }

                BigInteger token1 = kip17Contract.tokenOwnerByIndex(BRANDON.getAddress(), BigInteger.ZERO);
                assertEquals(token1, tokenIds[0]);

                BigInteger token2 = kip17Contract.tokenOwnerByIndex(BRANDON.getAddress(), BigInteger.ONE);
                assertEquals(token2, tokenIds[1]);

                BigInteger token3 = kip17Contract.tokenOwnerByIndex(BRANDON.getAddress(), BigInteger.valueOf(2));
                assertEquals(token3, tokenIds[2]);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class CommonTest {
        @BeforeClass
        public static void init() throws Exception {
            deployContract();
        }

        @Test
        public void ownerOf() {
            try {
                BigInteger tokenId = BigInteger.ZERO;
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions );

                String address = kip17Contract.ownerOf(tokenId);
                assertEquals(LUMAN.getAddress(), address);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void balanceOf() {
            try {

                BigInteger preBalance = kip17Contract.balanceOf(LUMAN.getAddress());

                BigInteger tokenId = BigInteger.valueOf(100000);
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions);

                BigInteger currentBalance = kip17Contract.balanceOf(LUMAN.getAddress());
                assertEquals(currentBalance, preBalance.add(BigInteger.ONE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        public void approve() {
            SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            try {
                BigInteger tokenId = BigInteger.valueOf(100);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions);

                kip17Contract.approve(BRANDON.getAddress(), tokenId, sendOptions);
                String approvedAddress = kip17Contract.getApproved(tokenId);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void getApproved() {
            SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            try {
                BigInteger tokenId = BigInteger.valueOf(6001);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, sendOptions);

                kip17Contract.approve(BRANDON.getAddress(), tokenId, sendOptions);
                assertEquals(BRANDON.getAddress(), kip17Contract.getApproved(tokenId));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Test
        public void setApprovalForAll() {
            SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            try {
                kip17Contract.setApproveForAll(BRANDON.getAddress(), true, sendOptions);
                assertTrue(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));

                kip17Contract.setApproveForAll(BRANDON.getAddress(), false, sendOptions);
                assertFalse(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void isApprovedForAll() {
            SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            try {
                kip17Contract.setApproveForAll(BRANDON.getAddress(), true, sendOptions);
                assertTrue(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));

                kip17Contract.setApproveForAll(BRANDON.getAddress(), false, sendOptions);
                assertFalse(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void transferFrom_approve() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            try {
                BigInteger tokenId = BigInteger.valueOf(7777);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, ownerOption);

                kip17Contract.approve(BRANDON.getAddress(), tokenId, ownerOption);
                kip17Contract.transferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenId, spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenId));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFrom_approve() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            try {
                BigInteger tokenId = BigInteger.valueOf(7778);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, ownerOption);

                kip17Contract.approve(BRANDON.getAddress(), tokenId, ownerOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenId, spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenId));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFromWithData_approve() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            try {
                BigInteger tokenId = BigInteger.valueOf(6666);
                kip17Contract.mint(LUMAN.getAddress(), tokenId, ownerOption);

                kip17Contract.approve(BRANDON.getAddress(), tokenId, ownerOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenId, "buffer", spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenId));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void transferFrom_setApprovedForAll() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(3001), BigInteger.valueOf(3002)};

            try {
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[0], ownerOption);
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[1], ownerOption);

                kip17Contract.setApproveForAll(BRANDON.getAddress(), true, ownerOption);
                kip17Contract.transferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[0], spenderOption);
                kip17Contract.transferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[1], spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[0]));
                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[1]));

                kip17Contract.setApproveForAll(BRANDON.getAddress(), false, ownerOption);
                assertFalse(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFrom_setApprovedForAll() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(4001), BigInteger.valueOf(4002)};

            try {
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[0], ownerOption);
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[1], ownerOption);

                kip17Contract.setApproveForAll(BRANDON.getAddress(), true, ownerOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[0], spenderOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[1], spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[0]));
                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[1]));

                kip17Contract.setApproveForAll(BRANDON.getAddress(), false, ownerOption);
                assertFalse(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFromWithData_setApprovedForAll() {
            SendOptions ownerOption = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions spenderOption = new SendOptions(BRANDON.getAddress(), (String)null);

            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(771), BigInteger.valueOf(772)};

            try {
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[0], ownerOption);
                kip17Contract.mint(LUMAN.getAddress(), tokenIdArr[1], ownerOption);

                kip17Contract.setApproveForAll(BRANDON.getAddress(), true, ownerOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[0], "buffered", spenderOption);
                kip17Contract.safeTransferFrom(LUMAN.getAddress(), BRANDON.getAddress(), tokenIdArr[1], "buffered", spenderOption);

                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[0]));
                assertEquals(BRANDON.getAddress(), kip17Contract.ownerOf(tokenIdArr[1]));

                kip17Contract.setApproveForAll(BRANDON.getAddress(), false, ownerOption);
                assertFalse(kip17Contract.isApprovedForAll(LUMAN.getAddress(), BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void supportsInterface() {
            final String INTERFACE_ID_KIP13 = "0x01ffc9a7";
            final String INTERFACE_ID_KIP17  = "0x80ac58cd";
            final String INTERFACE_ID_KIP17_PAUSABLE = "0x4d5507ff";
            final String INTERFACE_ID_KIP17_BURNABLE = "0x42966c68";
            final String INTERFACE_ID_KIP17_MINTABLE = "0xeab83e20";
            final String INTERFACE_ID_KIP17_METADATA = "0x5b5e139f";
            final String INTERFACE_ID_KIP17_METADATA_MINTABLE = "0xfac27f46";
            final String INTERFACE_ID_KIP17_ENUMERABLE = "0x780e9d63";
            final String INTERFACE_ID_FALSE = "0xFFFFFFFF";

            try {
                boolean isSupported_KIP13 = kip17Contract.supportInterface(INTERFACE_ID_KIP13);
                assertTrue(isSupported_KIP13);

                boolean isSupported_KIP17_PAUSABLE = kip17Contract.supportInterface(INTERFACE_ID_KIP17_PAUSABLE);
                assertTrue(isSupported_KIP17_PAUSABLE);

                boolean isSupported_KIP17_BURNABLE = kip17Contract.supportInterface(INTERFACE_ID_KIP17_BURNABLE);
                assertTrue(isSupported_KIP17_BURNABLE);

                boolean isSupported_KIP17_MINTABLE = kip17Contract.supportInterface(INTERFACE_ID_KIP17_MINTABLE);
                assertTrue(isSupported_KIP17_MINTABLE);

                boolean isSupported_KIP17_METADATA = kip17Contract.supportInterface(INTERFACE_ID_KIP17_METADATA);
                assertTrue(isSupported_KIP17_METADATA);

                boolean isSupported_KIP17_METADATA_MINTABLE = kip17Contract.supportInterface(INTERFACE_ID_KIP17_METADATA_MINTABLE);
                assertTrue(isSupported_KIP17_METADATA_MINTABLE);

                boolean isSupported_KIP17_ENUMERABLE = kip17Contract.supportInterface(INTERFACE_ID_KIP17_ENUMERABLE);
                assertTrue(isSupported_KIP17_ENUMERABLE);

                boolean isSupported_KIP17 = kip17Contract.supportInterface(INTERFACE_ID_KIP17);
                assertTrue(isSupported_KIP17);

                boolean isSupported_FALSE = kip17Contract.supportInterface(INTERFACE_ID_FALSE);
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
        static final String bytecodeWithFullMetadataMintable = "0x60806040523480156200001157600080fd5b506040516200334238038062003342833981018060405260408110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b50509291906020018051640100000000811115620000a257600080fd5b82810190506020810184811115620000b957600080fd5b8151856001820283011164010000000082111715620000d757600080fd5b505092919050505081818181620000fb6301ffc9a760e01b620001aa60201b60201c565b620001136380ac58cd60e01b620001aa60201b60201c565b6200012b63780e9d6360e01b620001aa60201b60201c565b816009908051906020019062000143929190620004d8565b5080600a90805190602001906200015c929190620004d8565b5062000175635b5e139f60e01b620001aa60201b60201c565b505050506200018a33620002b360201b60201c565b620001a263fac27f4660e01b620001aa60201b60201c565b505062000587565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916141562000247576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b620002ce81600c6200031460201b620026b71790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b620003268282620003f860201b60201c565b156200039a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141562000481576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180620033206022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200051b57805160ff19168380011785556200054c565b828001600101855582156200054c579182015b828111156200054b5782518255916020019190600101906200052e565b5b5090506200055b91906200055f565b5090565b6200058491905b808211156200058057600081600090555060010162000566565b5090565b90565b612d8980620005976000396000f3fe608060405234801561001057600080fd5b506004361061012c5760003560e01c80636352211e116100ad578063a22cb46511610071578063a22cb46514610707578063aa271e1a14610757578063b88d4fde146107b3578063c87b56dd146108b8578063e985e9c51461095f5761012c565b80636352211e1461057057806370a08231146105de57806395d89b4114610636578063983b2d56146106b957806398650275146106fd5761012c565b806323b872dd116100f457806323b872dd146102f35780632f745c591461036157806342842e0e146103c35780634f6ccce71461043157806350bb4e7f146104735761012c565b806301ffc9a71461013157806306fdde0314610196578063081812fc14610219578063095ea7b31461028757806318160ddd146102d5575b600080fd5b61017c6004803603602081101561014757600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191690602001909291905050506109db565b604051808215151515815260200191505060405180910390f35b61019e610a42565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101de5780820151818401526020810190506101c3565b50505050905090810190601f16801561020b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102456004803603602081101561022f57600080fd5b8101908080359060200190929190505050610ae4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102d36004803603604081101561029d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b7f565b005b6102dd610d75565b6040518082815260200191505060405180910390f35b61035f6004803603606081101561030957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d82565b005b6103ad6004803603604081101561037757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610df1565b6040518082815260200191505060405180910390f35b61042f600480360360608110156103d957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610eb0565b005b61045d6004803603602081101561044757600080fd5b8101908080359060200190929190505050610ed0565b6040518082815260200191505060405180910390f35b6105566004803603606081101561048957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156104d057600080fd5b8201836020820111156104e257600080fd5b8035906020019184600183028401116401000000008311171561050457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610f50565b604051808215151515815260200191505060405180910390f35b61059c6004803603602081101561058657600080fd5b8101908080359060200190929190505050610fcf565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610620600480360360208110156105f457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611097565b6040518082815260200191505060405180910390f35b61063e61116c565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561067e578082015181840152602081019050610663565b50505050905090810190601f1680156106ab5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6106fb600480360360208110156106cf57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061120e565b005b610705611278565b005b6107556004803603604081101561071d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050611283565b005b6107996004803603602081101561076d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611426565b604051808215151515815260200191505060405180910390f35b6108b6600480360360808110156107c957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561083057600080fd5b82018360208201111561084257600080fd5b8035906020019184600183028401116401000000008311171561086457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611443565b005b6108e4600480360360208110156108ce57600080fd5b81019080803590602001909291905050506114b5565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610924578082015181840152602081019050610909565b50505050905090810190601f1680156109515780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6109c16004803603604081101561097557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506115c8565b604051808215151515815260200191505060405180910390f35b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ada5780601f10610aaf57610100808354040283529160200191610ada565b820191906000526020600020905b815481529060010190602001808311610abd57829003601f168201915b5050505050905090565b6000610aef8261165c565b610b44576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612cd1602b913960400191505060405180910390fd5b6002600083815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6000610b8a82610fcf565b90508073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610c2e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4b495031373a20617070726f76616c20746f2063757272656e74206f776e657281525060200191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610c6e5750610c6d81336115c8565b5b610cc3576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526037815260200180612cfc6037913960400191505060405180910390fd5b826002600084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550818373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a4505050565b6000600780549050905090565b610d8c33826116ce565b610de1576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612bd36030913960400191505060405180910390fd5b610dec8383836117c2565b505050565b6000610dfc83611097565b8210610e53576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602a815260200180612b05602a913960400191505060405180910390fd5b600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208281548110610e9d57fe5b9060005260206000200154905092915050565b610ecb83838360405180602001604052806000815250611443565b505050565b6000610eda610d75565b8210610f31576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612ca6602b913960400191505060405180910390fd5b60078281548110610f3e57fe5b90600052602060002001549050919050565b6000610f5b33611426565b610fb0576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612b2f6030913960400191505060405180910390fd5b610fba84846117e6565b610fc48383611807565b600190509392505050565b6000806001600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561108e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180612b806028913960400191505060405180910390fd5b80915050919050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561111e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180612c556029913960400191505060405180910390fd5b611165600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020611891565b9050919050565b6060600a8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156112045780601f106111d957610100808354040283529160200191611204565b820191906000526020600020905b8154815290600101906020018083116111e757829003601f168201915b5050505050905090565b61121733611426565b61126c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612b2f6030913960400191505060405180910390fd5b6112758161189f565b50565b611281336118f9565b565b3373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611325576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4b495031373a20617070726f766520746f2063616c6c6572000000000000000081525060200191505060405180910390fd5b80600460003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051808215151515815260200191505060405180910390a35050565b600061143c82600c61195390919063ffffffff16565b9050919050565b61144e848484610d82565b61145a84848484611a31565b6114af576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612c256030913960400191505060405180910390fd5b50505050565b60606114c08261165c565b611515576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180612ab4602e913960400191505060405180910390fd5b600b60008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156115bc5780601f10611591576101008083540402835291602001916115bc565b820191906000526020600020905b81548152906001019060200180831161159f57829003601f168201915b50505050509050919050565b6000600460008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000806001600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415915050919050565b60006116d98261165c565b61172e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612d33602b913960400191505060405180910390fd5b600061173983610fcf565b90508073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1614806117a857508373ffffffffffffffffffffffffffffffffffffffff1661179084610ae4565b73ffffffffffffffffffffffffffffffffffffffff16145b806117b957506117b881856115c8565b5b91505092915050565b6117cd838383611f93565b6117d783826121ee565b6117e1828261238c565b505050565b6117f08282612453565b6117fa828261238c565b6118038161266b565b5050565b6118108261165c565b611865576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612ba8602b913960400191505060405180910390fd5b80600b6000848152602001908152602001600020908051906020019061188c9291906129e2565b505050565b600081600001549050919050565b6118b381600c6126b790919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b61190d81600c61279290919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156119da576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180612c036022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000806060611a558673ffffffffffffffffffffffffffffffffffffffff1661284f565b611a6457600192505050611f8b565b8573ffffffffffffffffffffffffffffffffffffffff1663150b7a0260e01b33898888604051602401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611b34578082015181840152602081019050611b19565b50505050905090810190601f168015611b615780820380516001836020036101000a031916815260200191505b5095505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310611bf95780518252602082019150602081019050602083039250611bd6565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114611c5b576040519150601f19603f3d011682016040523d82523d6000602084013e611c60565b606091505b5080925081935050506000815114158015611ce4575063150b7a0260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916818060200190516020811015611cb257600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b15611cf457600192505050611f8b565b8573ffffffffffffffffffffffffffffffffffffffff16636745782b60e01b33898888604051602401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611dc4578082015181840152602081019050611da9565b50505050905090810190601f168015611df15780820380516001836020036101000a031916815260200191505b5095505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310611e895780518252602082019150602081019050602083039250611e66565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114611eeb576040519150601f19603f3d011682016040523d82523d6000602084013e611ef0565b606091505b5080925081935050506000815114158015611f745750636745782b60e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916818060200190516020811015611f4257600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b15611f8457600192505050611f8b565b6000925050505b949350505050565b8273ffffffffffffffffffffffffffffffffffffffff16611fb382610fcf565b73ffffffffffffffffffffffffffffffffffffffff161461201f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180612c7e6028913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156120a5576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526023815260200180612ae26023913960400191505060405180910390fd5b6120ae81612862565b6120f5600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612920565b61213c600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612943565b816001600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550808273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4505050565b60006122466001600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208054905061295990919063ffffffff16565b9050600060066000848152602001908152602001600020549050818114612333576000600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002083815481106122b357fe5b9060005260206000200154905080600560008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020838154811061230b57fe5b9060005260206000200181905550816006600083815260200190815260200160002081905550505b600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208054809190600190036123859190612a62565b5050505050565b600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805490506006600083815260200190815260200160002081905550600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190806001815401808255809150509060018203906000526020600020016000909192909190915055505050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156124f6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4b495031373a206d696e7420746f20746865207a65726f20616464726573730081525060200191505060405180910390fd5b6124ff8161165c565b15612572576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031373a20746f6b656e20616c7265616479206d696e746564000000000081525060200191505060405180910390fd5b816001600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061260b600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612943565b808273ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a45050565b6007805490506008600083815260200190815260200160002081905550600781908060018154018082558091505090600182039060005260206000200160009091929091909150555050565b6126c18282611953565b15612734576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b61279c8282611953565b6127f1576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180612b5f6021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b600080823b905060008111915050919050565b600073ffffffffffffffffffffffffffffffffffffffff166002600083815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161461291d5760006002600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b50565b6129386001826000015461295990919063ffffffff16565b816000018190555050565b6001816000016000828254019250508190555050565b6000828211156129d1576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10612a2357805160ff1916838001178555612a51565b82800160010185558215612a51579182015b82811115612a50578251825591602001919060010190612a35565b5b509050612a5e9190612a8e565b5090565b815481835581811115612a8957818360005260206000209182019101612a889190612a8e565b5b505050565b612ab091905b80821115612aac576000816000905550600101612a94565b5090565b9056fe4b495031374d657461646174613a2055524920717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031373a207472616e7366657220746f20746865207a65726f20616464726573734b49503137456e756d657261626c653a206f776e657220696e646578206f7574206f6620626f756e64734d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b495031373a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031374d657461646174613a2055524920736574206f66206e6f6e6578697374656e7420746f6b656e4b495031373a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b495031373a207472616e7366657220746f206e6f6e204b49503137526563656976657220696d706c656d656e7465724b495031373a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734b495031373a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4b49503137456e756d657261626c653a20676c6f62616c20696e646578206f7574206f6620626f756e64734b495031373a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031373a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4b495031373a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656ea165627a7a723058208b7a6dcd5f7fc75450d49553b138abdd36f29599463acdcb619ea30f613d86b60029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
        static final String byteCodeWithoutBurnablePausable = "0x60806040523480156200001157600080fd5b506040516200343f3803806200343f833981018060405260408110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b50509291906020018051640100000000811115620000a257600080fd5b82810190506020810184811115620000b957600080fd5b8151856001820283011164010000000082111715620000d757600080fd5b505092919050505081818181620000fb6301ffc9a760e01b620001c260201b60201c565b620001136380ac58cd60e01b620001c260201b60201c565b6200012b63780e9d6360e01b620001c260201b60201c565b816009908051906020019062000143929190620004f0565b5080600a90805190602001906200015c929190620004f0565b5062000175635b5e139f60e01b620001c260201b60201c565b505050506200018a33620002cb60201b60201c565b620001a263eab83e2060e01b620001c260201b60201c565b620001ba63fac27f4660e01b620001c260201b60201c565b50506200059f565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614156200025f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b620002e681600c6200032c60201b6200279c1790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b6200033e82826200041060201b60201c565b15620003b2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141562000499576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806200341d6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200053357805160ff191683800117855562000564565b8280016001018555821562000564579182015b828111156200056357825182559160200191906001019062000546565b5b50905062000573919062000577565b5090565b6200059c91905b80821115620005985760008160009055506001016200057e565b5090565b90565b612e6e80620005af6000396000f3fe608060405234801561001057600080fd5b50600436106101375760003560e01c806350bb4e7f116100b8578063986502751161007c578063986502751461076e578063a22cb46514610778578063aa271e1a146107c8578063b88d4fde14610824578063c87b56dd14610929578063e985e9c5146109d057610137565b806350bb4e7f146104e45780636352211e146105e157806370a082311461064f57806395d89b41146106a7578063983b2d561461072a57610137565b806323b872dd116100ff57806323b872dd146102fe5780632f745c591461036c57806340c10f19146103ce57806342842e0e146104345780634f6ccce7146104a257610137565b806301ffc9a71461013c57806306fdde03146101a1578063081812fc14610224578063095ea7b31461029257806318160ddd146102e0575b600080fd5b6101876004803603602081101561015257600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610a4c565b604051808215151515815260200191505060405180910390f35b6101a9610ab3565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101e95780820151818401526020810190506101ce565b50505050905090810190601f1680156102165780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102506004803603602081101561023a57600080fd5b8101908080359060200190929190505050610b55565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102de600480360360408110156102a857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610bf0565b005b6102e8610de6565b6040518082815260200191505060405180910390f35b61036a6004803603606081101561031457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610df3565b005b6103b86004803603604081101561038257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610e62565b6040518082815260200191505060405180910390f35b61041a600480360360408110156103e457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610f21565b604051808215151515815260200191505060405180910390f35b6104a06004803603606081101561044a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610f95565b005b6104ce600480360360208110156104b857600080fd5b8101908080359060200190929190505050610fb5565b6040518082815260200191505060405180910390f35b6105c7600480360360608110156104fa57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561054157600080fd5b82018360208201111561055357600080fd5b8035906020019184600183028401116401000000008311171561057557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611035565b604051808215151515815260200191505060405180910390f35b61060d600480360360208110156105f757600080fd5b81019080803590602001909291905050506110b4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6106916004803603602081101561066557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061117c565b6040518082815260200191505060405180910390f35b6106af611251565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156106ef5780820151818401526020810190506106d4565b50505050905090810190601f16801561071c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61076c6004803603602081101561074057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506112f3565b005b61077661135d565b005b6107c66004803603604081101561078e57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050611368565b005b61080a600480360360208110156107de57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061150b565b604051808215151515815260200191505060405180910390f35b6109276004803603608081101561083a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156108a157600080fd5b8201836020820111156108b357600080fd5b803590602001918460018302840111640100000000831117156108d557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050611528565b005b6109556004803603602081101561093f57600080fd5b810190808035906020019092919050505061159a565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561099557808201518184015260208101905061097a565b50505050905090810190601f1680156109c25780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610a32600480360360408110156109e657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506116ad565b604051808215151515815260200191505060405180910390f35b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b4b5780601f10610b2057610100808354040283529160200191610b4b565b820191906000526020600020905b815481529060010190602001808311610b2e57829003601f168201915b5050505050905090565b6000610b6082611741565b610bb5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612db6602b913960400191505060405180910390fd5b6002600083815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6000610bfb826110b4565b90508073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610c9f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4b495031373a20617070726f76616c20746f2063757272656e74206f776e657281525060200191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610cdf5750610cde81336116ad565b5b610d34576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526037815260200180612de16037913960400191505060405180910390fd5b826002600084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550818373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a4505050565b6000600780549050905090565b610dfd33826117b3565b610e52576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612cb86030913960400191505060405180910390fd5b610e5d8383836118a7565b505050565b6000610e6d8361117c565b8210610ec4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602a815260200180612bea602a913960400191505060405180910390fd5b600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208281548110610f0e57fe5b9060005260206000200154905092915050565b6000610f2c3361150b565b610f81576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612c146030913960400191505060405180910390fd5b610f8b83836118cb565b6001905092915050565b610fb083838360405180602001604052806000815250611528565b505050565b6000610fbf610de6565b8210611016576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612d8b602b913960400191505060405180910390fd5b6007828154811061102357fe5b90600052602060002001549050919050565b60006110403361150b565b611095576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612c146030913960400191505060405180910390fd5b61109f84846118cb565b6110a983836118ec565b600190509392505050565b6000806001600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415611173576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180612c656028913960400191505060405180910390fd5b80915050919050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611203576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180612d3a6029913960400191505060405180910390fd5b61124a600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020611976565b9050919050565b6060600a8054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156112e95780601f106112be576101008083540402835291602001916112e9565b820191906000526020600020905b8154815290600101906020018083116112cc57829003601f168201915b5050505050905090565b6112fc3361150b565b611351576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612c146030913960400191505060405180910390fd5b61135a81611984565b50565b611366336119de565b565b3373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561140a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4b495031373a20617070726f766520746f2063616c6c6572000000000000000081525060200191505060405180910390fd5b80600460003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051808215151515815260200191505060405180910390a35050565b600061152182600c611a3890919063ffffffff16565b9050919050565b611533848484610df3565b61153f84848484611b16565b611594576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180612d0a6030913960400191505060405180910390fd5b50505050565b60606115a582611741565b6115fa576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180612b99602e913960400191505060405180910390fd5b600b60008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156116a15780601f10611676576101008083540402835291602001916116a1565b820191906000526020600020905b81548152906001019060200180831161168457829003601f168201915b50505050509050919050565b6000600460008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000806001600084815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415915050919050565b60006117be82611741565b611813576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612e18602b913960400191505060405180910390fd5b600061181e836110b4565b90508073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff16148061188d57508373ffffffffffffffffffffffffffffffffffffffff1661187584610b55565b73ffffffffffffffffffffffffffffffffffffffff16145b8061189e575061189d81856116ad565b5b91505092915050565b6118b2838383612078565b6118bc83826122d3565b6118c68282612471565b505050565b6118d58282612538565b6118df8282612471565b6118e881612750565b5050565b6118f582611741565b61194a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b815260200180612c8d602b913960400191505060405180910390fd5b80600b60008481526020019081526020016000209080519060200190611971929190612ac7565b505050565b600081600001549050919050565b61199881600c61279c90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b6119f281600c61287790919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611abf576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180612ce86022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000806060611b3a8673ffffffffffffffffffffffffffffffffffffffff16612934565b611b4957600192505050612070565b8573ffffffffffffffffffffffffffffffffffffffff1663150b7a0260e01b33898888604051602401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611c19578082015181840152602081019050611bfe565b50505050905090810190601f168015611c465780820380516001836020036101000a031916815260200191505b5095505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310611cde5780518252602082019150602081019050602083039250611cbb565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114611d40576040519150601f19603f3d011682016040523d82523d6000602084013e611d45565b606091505b5080925081935050506000815114158015611dc9575063150b7a0260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916818060200190516020811015611d9757600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b15611dd957600192505050612070565b8573ffffffffffffffffffffffffffffffffffffffff16636745782b60e01b33898888604051602401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015611ea9578082015181840152602081019050611e8e565b50505050905090810190601f168015611ed65780820380516001836020036101000a031916815260200191505b5095505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310611f6e5780518252602082019150602081019050602083039250611f4b565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114611fd0576040519150601f19603f3d011682016040523d82523d6000602084013e611fd5565b606091505b50809250819350505060008151141580156120595750636745782b60e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191681806020019051602081101561202757600080fd5b81019080805190602001909291905050507bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b1561206957600192505050612070565b6000925050505b949350505050565b8273ffffffffffffffffffffffffffffffffffffffff16612098826110b4565b73ffffffffffffffffffffffffffffffffffffffff1614612104576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180612d636028913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561218a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526023815260200180612bc76023913960400191505060405180910390fd5b61219381612947565b6121da600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612a05565b612221600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612a28565b816001600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550808273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4505050565b600061232b6001600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080549050612a3e90919063ffffffff16565b9050600060066000848152602001908152602001600020549050818114612418576000600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020838154811061239857fe5b9060005260206000200154905080600560008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002083815481106123f057fe5b9060005260206000200181905550816006600083815260200190815260200160002081905550505b600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080548091906001900361246a9190612b47565b5050505050565b600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805490506006600083815260200190815260200160002081905550600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190806001815401808255809150509060018203906000526020600020016000909192909190915055505050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156125db576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4b495031373a206d696e7420746f20746865207a65726f20616464726573730081525060200191505060405180910390fd5b6125e481611741565b15612657576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031373a20746f6b656e20616c7265616479206d696e746564000000000081525060200191505060405180910390fd5b816001600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506126f0600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020612a28565b808273ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a45050565b6007805490506008600083815260200190815260200160002081905550600781908060018154018082558091505090600182039060005260206000200160009091929091909150555050565b6127a68282611a38565b15612819576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b6128818282611a38565b6128d6576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180612c446021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b600080823b905060008111915050919050565b600073ffffffffffffffffffffffffffffffffffffffff166002600083815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614612a025760006002600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b50565b612a1d60018260000154612a3e90919063ffffffff16565b816000018190555050565b6001816000016000828254019250508190555050565b600082821115612ab6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10612b0857805160ff1916838001178555612b36565b82800160010185558215612b36579182015b82811115612b35578251825591602001919060010190612b1a565b5b509050612b439190612b73565b5090565b815481835581811115612b6e57818360005260206000209182019101612b6d9190612b73565b5b505050565b612b9591905b80821115612b91576000816000905550600101612b79565b5090565b9056fe4b495031374d657461646174613a2055524920717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031373a207472616e7366657220746f20746865207a65726f20616464726573734b49503137456e756d657261626c653a206f776e657220696e646578206f7574206f6620626f756e64734d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b495031373a206f776e657220717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031374d657461646174613a2055524920736574206f66206e6f6e6578697374656e7420746f6b656e4b495031373a207472616e736665722063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f766564526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b495031373a207472616e7366657220746f206e6f6e204b49503137526563656976657220696d706c656d656e7465724b495031373a2062616c616e636520717565727920666f7220746865207a65726f20616464726573734b495031373a207472616e73666572206f6620746f6b656e2074686174206973206e6f74206f776e4b49503137456e756d657261626c653a20676c6f62616c20696e646578206f7574206f6620626f756e64734b495031373a20617070726f76656420717565727920666f72206e6f6e6578697374656e7420746f6b656e4b495031373a20617070726f76652063616c6c6572206973206e6f74206f776e6572206e6f7220617070726f76656420666f7220616c6c4b495031373a206f70657261746f7220717565727920666f72206e6f6e6578697374656e7420746f6b656ea165627a7a72305820f1e4b54636e204b36742d94fe36135c736f22cb8b569f00dbc1b06129135841f0029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
        static final String byteCodeNotSupportedKIP13 = "0x608060405234801561001057600080fd5b5061051f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063693ec85e1461003b578063e942b5161461016f575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c1565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102bf6004803603604081101561018557600080fd5b81019080803590602001906401000000008111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111640100000000831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561023957600080fd5b82018360208201111561024b57600080fd5b8035906020019184600183028401116401000000008311171561026d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103cc565b005b60606000826040518082805190602001908083835b602083106102f957805182526020820191506020810190506020830392506102d6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c05780601f10610395576101008083540402835291602001916103c0565b820191906000526020600020905b8154815290600101906020018083116103a357829003601f168201915b50505050509050919050565b806000836040518082805190602001908083835b6020831061040357805182526020820191506020810190506020830392506103e0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061044992919061044e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061048f57805160ff19168380011785556104bd565b828001600101855582156104bd579182015b828111156104bc5782518255916020019190600101906104a1565b5b5090506104ca91906104ce565b5090565b6104f091905b808211156104ec5760008160009055506001016104d4565b5090565b9056fea165627a7a723058203ffebc792829e0434ecc495da1b53d24399cd7fff506a4fd03589861843e14990029";
        static final String abi_metadata_mintable = "[\n" +
                "  {\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"name\",\n" +
                "        \"type\":\"string\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"symbol\",\n" +
                "        \"type\":\"string\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"payable\":false,\n" +
                "    \"stateMutability\":\"nonpayable\",\n" +
                "    \"type\":\"constructor\"\n" +
                "  }\n" +
                "]";

        static final String abi_without_burnable_pausable = "[\n" +
                "  {\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"name\",\n" +
                "        \"type\":\"string\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"symbol\",\n" +
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
            deployContract();
        }

        @Test
        public void detectInterface() {
            Map<String, Boolean> result = kip17Contract.detectInterface();
            assertTrue(result.get(KIP17.INTERFACE.IKIP17.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_ENUMERABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_BURNABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_PAUSABLE.getName()));

        }

        @Test
        public void detectInterface_staticMethod() {
            Map<String, Boolean> result = caver.kct.kip17.detectInterface(kip17Contract.getContractAddress());
            assertTrue(result.get(KIP17.INTERFACE.IKIP17.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_ENUMERABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_BURNABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_PAUSABLE.getName()));
        }

        @Test
        public void metadata_mintable() throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            Contract contract = new Contract(caver, abi_metadata_mintable);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), bytecodeWithFullMetadataMintable, "Test", "TST");

            Map<String, Boolean> result = caver.kct.kip17.detectInterface(contract.getContractAddress());
            assertTrue(result.get(KIP17.INTERFACE.IKIP17.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_ENUMERABLE.getName()));
            assertFalse(result.get(KIP17.INTERFACE.IKIP17_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA_MINTABLE.getName()));
            assertFalse(result.get(KIP17.INTERFACE.IKIP17_BURNABLE.getName()));
            assertFalse(result.get(KIP17.INTERFACE.IKIP17_PAUSABLE.getName()));
        }

        @Test
        public void without_burnable_pausable() throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This contract does not support KIP-13.");

            Contract contract = new Contract(caver, abi_not_supported_kip13);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeNotSupportedKIP13);

            Map<String, Boolean> result = caver.kct.kip17.detectInterface(contract.getContractAddress());
            assertTrue(result.get(KIP17.INTERFACE.IKIP17.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_ENUMERABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_MINTABLE.getName()));
            assertTrue(result.get(KIP17.INTERFACE.IKIP17_METADATA_MINTABLE.getName()));
            assertFalse(result.get(KIP17.INTERFACE.IKIP17_BURNABLE.getName()));
            assertFalse(result.get(KIP17.INTERFACE.IKIP17_PAUSABLE.getName()));
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

            KIP17DeployParams kip17DeployParam = new KIP17DeployParams(CONTRACT_NAME, CONTRACT_SYMBOL);

            KIP17 kip17 = caver.kct.kip17.deploy(kip17DeployParam, sender);
            contractAddress = kip17.getContractAddress();
        }

        @Test
        public void deploy() throws TransactionException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            KIP17 kip17 = caver.kct.kip17.deploy(sendOptions, CONTRACT_NAME, CONTRACT_SYMBOL);
            assertNotNull(kip17.getContractAddress());
        }

        @Test
        public void addMinter_FD() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            KIP17 kip17 = caver.kct.kip17.create(contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());

            TransactionReceipt.TransactionReceiptData receiptData = kip17.addMinter(caver.wallet.keyring.generate().getAddress(), sendOptions);

            assertEquals(TransactionType.TxTypeFeeDelegatedSmartContractExecution.toString(), receiptData.getType());
            assertEquals("0x1", receiptData.getStatus());
            assertEquals(sendOptions.getFrom(), receiptData.getFrom());
            assertEquals(contractAddress, receiptData.getTo());
            assertEquals(sendOptions.getFeePayer(), receiptData.getFeePayer());
        }

        @Test
        public void addMinter_FDWithRatio() throws IOException, TransactionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            KIP17 kip17 = caver.kct.kip17.create(contractAddress);

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(LUMAN.getAddress());
            sendOptions.setGas(BigInteger.valueOf(10000000));
            sendOptions.setFeeDelegation(true);
            sendOptions.setFeePayer(BRANDON.getAddress());
            sendOptions.setFeeRatio(BigInteger.valueOf(10));

            TransactionReceipt.TransactionReceiptData receiptData = kip17.addMinter(caver.wallet.keyring.generate().getAddress(), sendOptions);

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

            KIP17 kip17 = caver.kct.kip17.create(contractAddress);
            kip17.setDefaultSendOptions(defaultSendOptions);

            TransactionReceipt.TransactionReceiptData receiptData = kip17.addMinter(caver.wallet.keyring.generate().getAddress());

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

            KIP17 kip17 = caver.kct.kip17.create(contractAddress);
            kip17.setDefaultSendOptions(defaultOptions);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());
            options.setFeeRatio(BigInteger.valueOf(30));

            TransactionReceipt.TransactionReceiptData receiptData = kip17.addMinter(caver.wallet.keyring.generate().getAddress(), options);

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
            KIP17 kip17 = caver.kct.kip17.create();

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = (AbstractFeeDelegatedTransaction)kip17.sign(options, "constructor", KIP17ConstantData.BINARY, CONTRACT_NAME, CONTRACT_SYMBOL);
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
            KIP17 kip17 = caver.kct.kip17.create(contractAddress);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = (AbstractFeeDelegatedTransaction)kip17.sign(options, "addMinter", caver.wallet.keyring.generate().getAddress());
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
            KIP17 kip17 = caver.kct.kip17.create(contractAddress);

            SendOptions options = new SendOptions();
            options.setFrom(LUMAN.getAddress());
            options.setGas(BigInteger.valueOf(20000000));
            options.setFeeDelegation(true);
            options.setFeePayer(BRANDON.getAddress());

            AbstractFeeDelegatedTransaction signed = kip17.signAsFeePayer(options, "addMinter", caver.wallet.keyring.generate().getAddress());

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
        public void decodeFunctionCall() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            KIP17 kip17 = caver.kct.kip17.create();

            List<Object> expected = new ArrayList<>();
            String encoded = kip17.encodeABI("pause");
            List<Type> actual = kip17.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(LUMAN.getAddress());
            encoded = kip17.encodeABI("balanceOf", LUMAN.getAddress());
            actual = kip17.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(BRANDON.getAddress(), LUMAN.getAddress(), BigInteger.valueOf(10));
            encoded = kip17.encodeABI("transferFrom", BRANDON.getAddress(), LUMAN.getAddress(), BigInteger.valueOf(10));
            actual = kip17.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);

            expected = Arrays.asList(LUMAN.getAddress());
            encoded = kip17.encodeABI("isMinter", LUMAN.getAddress());
            actual = kip17.decodeFunctionCall(encoded);
            checkTypeAndValue(expected, actual);
        }

    }
}
