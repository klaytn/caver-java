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

package com.klaytn.caver.legacy.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayAccount;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.kct.KIP7;
import com.klaytn.caver.tx.manager.TransactionManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.*;


public class KIP7Test {
    private static final String ContractName = "TestKIP7Contract";
    private static final String ContractSymbol = "TKCP";
    private static final BigInteger ContractDecimal = BigInteger.valueOf(18);
    private static BigInteger ContractInitialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 100000 * 10^18

    static Caver mCaver;
    static KlayCredentials mDeployerCredential, mTestCredential;
    static TransactionManager mDeployerTxManager, mTesterTxManger, mTxManager;
    static String mContractAddress;


    @BeforeClass
    public static void preSetup() throws Exception {
        mCaver = Caver.build(Caver.DEFAULT_URL);

        mDeployerCredential = LUMAN;
        mDeployerTxManager = new TransactionManager.Builder(mCaver, LUMAN).setChaindId(LOCAL_CHAIN_ID).build();

        mTestCredential = BRANDON;
        mTesterTxManger = new TransactionManager.Builder(mCaver, BRANDON).setChaindId(LOCAL_CHAIN_ID).build();

        deployKIP7Contract();
    }

    public static void deployKIP7Contract() {
        try {
            KIP7 token = KIP7.deploy(mCaver,
                    mDeployerTxManager,
                    new DefaultGasProvider(mCaver),
                    ContractName,
                    ContractSymbol,
                    ContractDecimal,
                    ContractInitialSupply
            ).send();

            String address = token.getContractAddress();
            mContractAddress = address;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //KCT-001
    @Test
    public void deployContract() {
        try {
            KIP7 token = KIP7.deploy(mCaver,
                    mDeployerTxManager,
                    new DefaultGasProvider(mCaver),
                    ContractName,
                    ContractSymbol,
                    ContractDecimal,
                    ContractInitialSupply
            ).send();

            String contractAddress = token.getContractAddress();
            KlayAccount response = mCaver.klay().getAccount(contractAddress, DefaultBlockParameterName.LATEST).send();
            KlayAccount.Account account = response.getResult();
            assertEquals(0x02, account.getAccType());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-002
    @Test
    public void name() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        try {
            String name = tokenHandler_owner.name().send();
            assertEquals(name, ContractName);
        } catch (Exception e) {
            fail("call name() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-003
    @Test
    public void symbol() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        try {
            String symbol = tokenHandler_owner.symbol().send();
            assertEquals(symbol, ContractSymbol);
        } catch (Exception e) {
            fail("call symbol() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-004
    @Test
    public void decimals() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        try {
            BigInteger decimals = tokenHandler_owner.decimals().send();
            assertEquals(decimals, ContractDecimal);
        } catch (Exception e) {
            fail("call decimals() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-005
    @Test
    public void totalSupply() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        try {
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();
            assertEquals(totalSupply, ContractInitialSupply);
        } catch (Exception e) {
            fail("call totalSupply() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-006
    @Test
    public void balanceOf() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String noValueAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));;
        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(noValueAddress).send();
            tokenHandler_owner.transfer(noValueAddress, amount).send();

            BigInteger balance = tokenHandler_owner.balanceOf(noValueAddress).send();
            assertEquals(preBalance.add(amount), balance);
        } catch (Exception e) {
            e.printStackTrace();
            fail("call balanceOf() function is failed");
        }
    }

    //KCT-007
    @Test
    public void isMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String ownerAddr = mDeployerCredential.getAddress();
        String notMinterAddr = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            boolean isMinter = tokenHandler_owner.isMinter(ownerAddr).send();
            assertTrue(isMinter);

            isMinter = tokenHandler_owner.isMinter(notMinterAddr).send();
            assertFalse(isMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-008
    @Test
    public void mint() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String newMinterAddr = mTestCredential.getAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";
        BigInteger mintAmount = ContractInitialSupply;
        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(newMinterAddr).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.mint(newMinterAddr, mintAmount).send();
            BigInteger minterBalance = tokenHandler_owner.balanceOf(newMinterAddr).send();
            assertEquals(minterBalance, preBalance.add(mintAmount));

            List<KIP7.TransferEventResponse> list = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(list.get(0).from, zeroAddr);
            assertEquals(list.get(0).to, newMinterAddr);
            assertEquals(list.get(0).value, mintAmount);

            ContractInitialSupply = tokenHandler_owner.totalSupply().send();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-009
    @Test
    public void addMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String newMinter = mTestCredential.getAddress();

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.addMinter(newMinter).send();
            boolean isMinter = tokenHandler_owner.isMinter(newMinter).send();
            assertTrue(isMinter);

            List<KIP7.MinterAddedEventResponse> list = tokenHandler_owner.getMinterAddedEvents(receipt);
            assertEquals(list.get(0).account, newMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-010
    @Test
    public void renounceMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_minter = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        String renounceMinter = mTesterTxManger.getDefaultAddress();

        try {
            tokenHandler_owner.addMinter(renounceMinter).send();
            boolean isMinter = tokenHandler_owner.isMinter(renounceMinter).send();
            assertTrue(isMinter);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_minter.renounceMinter().send();
            isMinter = tokenHandler_minter.isMinter(renounceMinter).send();
            assertFalse(isMinter);

            List<KIP7.MinterRemovedEventResponse> list = tokenHandler_minter.getMinterRemovedEvents(receipt);
            assertEquals(list.get(0).account, renounceMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-011
    @Test
    public void isPauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String pauseUserAddr = mDeployerCredential.getAddress();
        String notPauseUserAddr = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            boolean isPauser = tokenHandler_owner.isPauser(pauseUserAddr).send();
            assertTrue(isPauser);

            isPauser = tokenHandler_owner.isPauser(notPauseUserAddr).send();
            assertFalse(isPauser);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-012
    @Test
    public void paused() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            List<KIP7.PausedEventResponse> pausedEvents = tokenHandler_owner.getPausedEvents(receipt);
            assertEquals(pausedEvents.get(0).account, mDeployerCredential.getAddress());

            tokenHandler_owner.unpause().send();
            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-013
    @Test
    public void unpause() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));

        try {
            tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            KlayTransactionReceipt.TransactionReceipt unPauseReceipt = tokenHandler_owner.unpause().send();
            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);

            List<KIP7.UnpausedEventResponse> unPausedEvents = tokenHandler_owner.getUnpausedEvents(unPauseReceipt);
            assertEquals(unPausedEvents.get(0).account, mDeployerCredential.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-014
    @Test
    public void addPauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        String userAddr = mTestCredential.getAddress();
        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.addPauser(userAddr).send();
            boolean isPauseUser = tokenHandler_owner.isPauser(userAddr).send();
            assertTrue(isPauseUser);

            List<KIP7.PauserAddedEventResponse> events = tokenHandler_owner.getPauserAddedEvents(receipt);
            assertEquals(events.get(0).account, userAddr);

            tokenHandler_pauser.renouncePauser().send();
            isPauseUser = tokenHandler_pauser.isPauser(userAddr).send();
            assertFalse(isPauseUser);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-015
    @Test
    public void renouncePauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        String userAddr = mTestCredential.getAddress();

        try {
            tokenHandler_owner.addPauser(userAddr).send();
            boolean isPauseUser = tokenHandler_owner.isPauser(userAddr).send();
            assertTrue(isPauseUser);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_pauser.renouncePauser().send();
            isPauseUser = tokenHandler_pauser.isPauser(userAddr).send();
            assertFalse(isPauseUser);

            List<KIP7.PauserRemovedEventResponse> events = tokenHandler_pauser.getPauserRemovedEvents(receipt);
            assertEquals(events.get(0).account, userAddr);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-016
    @Test
    public void transfer() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.transfer(recipientAddress, amount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(events.get(0).to, recipientAddress);
            assertEquals(events.get(0).value, amount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-017
    @Test
    public void safeTransfer() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(events.get(0).to, recipientAddress);
            assertEquals(events.get(0).value, amount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-018
    @Test
    public void safeTransferWithData() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            byte[] data = "buffered data".getBytes();
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount, data).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(events.get(0).to, recipientAddress);
            assertEquals(events.get(0).value, amount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-019
    @Test
    public void allowance() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String spenderAddress = mTestCredential.getAddress();
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        try {
            BigInteger preAllowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.approve(spenderAddress, allowAmount).send();

            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, preAllowance.add(allowAmount));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-020
    @Test
    public void appprove() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String spenderAddress = mTestCredential.getAddress();
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        try {
            //reset
            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            //Test
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.approve(spenderAddress, allowAmount).send();

            List<KIP7.ApprovalEventResponse> events = tokenHandler_owner.getApprovalEvents(receipt);
            assertEquals(events.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(events.get(0).spender, spenderAddress);
            assertEquals(events.get(0).value, allowAmount);

            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            //reset
            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-021
    @Test
    public void transferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            //reset
            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            //test
            tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.transferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
            assertEquals(transferEventResponseList.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(transferEventResponseList.get(0).to, recipientAddress);
            assertEquals(transferEventResponseList.get(0).value, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(transferReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-022
    @Test
    public void safeTransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            //reset
            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            //test
            tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
            assertEquals(transferEventResponseList.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(transferEventResponseList.get(0).to, recipientAddress);
            assertEquals(transferEventResponseList.get(0).value, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(transferReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, BigInteger.ZERO);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-023
    @Test
    public void safeTransferFromWithData() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            //reset
            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            //test
            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();

            byte[] data = "buffered data".getBytes();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount, data).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
            assertEquals(transferEventResponseList.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(transferEventResponseList.get(0).to, recipientAddress);
            assertEquals(transferEventResponseList.get(0).value, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponses = tokenHandler_owner.getApprovalEvents(transferReceipt);
            assertEquals(approvalEventResponses.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponses.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponses.get(0).value, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-024
    @Test
    public void burn() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger preTotalSupply = tokenHandler_owner.totalSupply().send();

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.burn(burnAmount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();
            assertEquals(preBalance.subtract(burnAmount), balance);
            assertEquals(preTotalSupply.subtract(burnAmount), totalSupply);

            List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(events.get(0).from, ownerAddress);
            assertEquals(events.get(0).to, zeroAddr);
            assertEquals(events.get(0).value, burnAmount);
            ContractInitialSupply = totalSupply;

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-025
    @Test
    public void burnFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger preTotalSupply = tokenHandler_owner.totalSupply().send();

            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger allowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            tokenHandler_owner.approve(spenderAddress, burnAmount).send();
            allowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(allowance, burnAmount);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_spender.burnFrom(ownerAddress, burnAmount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();

            assertEquals(preBalance.subtract(burnAmount), balance);
            assertEquals(preTotalSupply.subtract(burnAmount), totalSupply);

            List<KIP7.TransferEventResponse> transferEvents = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(transferEvents.get(0).from, ownerAddress);
            assertEquals(transferEvents.get(0).to, zeroAddr);
            assertEquals(transferEvents.get(0).value, burnAmount);

            List<KIP7.ApprovalEventResponse> approvalEvents = tokenHandler_owner.getApprovalEvents(receipt);
            assertEquals(approvalEvents.get(0).owner, ownerAddress);
            assertEquals(approvalEvents.get(0).spender, spenderAddress);
            assertEquals(approvalEvents.get(0).value, BigInteger.ZERO);

            ContractInitialSupply = totalSupply;

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-026
    @Test
    public void supportsInterface() {
        final String INTERFACE_ID_KIP13 = "0x01ffc9a7";
        final String INTERFACE_ID_KIP7_PAUSABLE = "0x4d5507ff";
        final String INTERFACE_ID_KIP7_BURNABLE = "0x3b5a0bf8";
        final String INTERFACE_ID_KIP7_MINTABLE = "0xeab83e20";
        final String INTERFACE_ID_KIP7_METADATA  = "0xa219a025";
        final String INTERFACE_ID_KIP7  = "0x65787371";
        final String INTERFACE_ID_FALSE = "0xFFFFFFFF";

        try {
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));

            boolean isSupported_KIP13 = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP13)).send();
            assertTrue(isSupported_KIP13);

            boolean isSupported_KIP7_PAUSABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP7_PAUSABLE)).send();
            assertTrue(isSupported_KIP7_PAUSABLE);

            boolean isSupported_KIP7_BURNABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP7_BURNABLE)).send();
            assertTrue(isSupported_KIP7_BURNABLE);

            boolean isSupported_KIP7_MINTABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP7_MINTABLE)).send();
            assertTrue(isSupported_KIP7_MINTABLE);

            boolean isSupported_KIP7_METADATA = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP7_METADATA)).send();
            assertTrue(isSupported_KIP7_METADATA);

            boolean isSupported_KIP7 = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP7)).send();
            assertTrue(isSupported_KIP7);

            boolean isSupported_FALSE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_FALSE)).send();
            assertFalse(isSupported_FALSE);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-027
    @Test
    public void getTransferEventTest() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String toAddr = mTesterTxManger.getDefaultAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));
        try {
            KlayTransactionReceipt.TransactionReceipt receipt =  tokenHandler_owner.transfer(toAddr, amount).send();
            List<KIP7.TransferEventResponse> event = tokenHandler_owner.getTransferEvents(receipt);

            assertEquals(event.size(), 1);
            assertNotNull(event.get(0).log.getLogIndex());
            assertNotNull(event.get(0).log.getTransactionIndex());
            assertNotNull(event.get(0).log.getTransactionHash());
            assertNotNull(event.get(0).log.getBlockHash());
            assertNotNull(event.get(0).log.getBlockNumber());
            assertNotNull(event.get(0).log.getAddress());
            assertNotNull(event.get(0).log.getData());
            assertNotNull(event.get(0).log.getTopics());
            assertEquals(event.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(event.get(0).to, toAddr);
            assertEquals(event.get(0).value, amount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-028
    @Test
    public void getApprovalEventTest() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        String spender = mTesterTxManger.getDefaultAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));
        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.approve(spender, amount).send();
            List<KIP7.ApprovalEventResponse> event = tokenHandler_owner.getApprovalEvents(receipt);

            assertEquals(event.size(), 1);
            assertNotNull(event.get(0).log.getLogIndex());
            assertNotNull(event.get(0).log.getTransactionIndex());
            assertNotNull(event.get(0).log.getTransactionHash());
            assertNotNull(event.get(0).log.getBlockHash());
            assertNotNull(event.get(0).log.getBlockNumber());
            assertNotNull(event.get(0).log.getAddress());
            assertNotNull(event.get(0).log.getData());
            assertNotNull(event.get(0).log.getTopics());
            assertEquals(event.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(event.get(0).spender, spender);
            assertEquals(event.get(0).value, amount);

            //reset allowance
            tokenHandler_owner.approve(spender, BigInteger.ZERO).send();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-029
    @Test
    public void getPausedEventTest() {
        KIP7 tokenHanler = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));

        try {
            //Check Paused Event
            KlayTransactionReceipt.TransactionReceipt pausedReceipt = tokenHanler.pause().send();
            List<KIP7.PausedEventResponse> paused_events = tokenHanler.getPausedEvents(pausedReceipt);

            assertEquals(paused_events.size(), 1);
            assertNotNull(paused_events.get(0).log.getLogIndex());
            assertNotNull(paused_events.get(0).log.getTransactionIndex());
            assertNotNull(paused_events.get(0).log.getTransactionHash());
            assertNotNull(paused_events.get(0).log.getBlockHash());
            assertNotNull(paused_events.get(0).log.getBlockNumber());
            assertNotNull(paused_events.get(0).log.getAddress());
            assertNotNull(paused_events.get(0).log.getData());
            assertNotNull(paused_events.get(0).log.getTopics());
            assertEquals(paused_events.get(0).account, mDeployerTxManager.getDefaultAddress());

            //Check UnPaused Event
            KlayTransactionReceipt.TransactionReceipt unPausedReceipt = tokenHanler.unpause().send();
            List<KIP7.UnpausedEventResponse> unpaused_events = tokenHanler.getUnpausedEvents(unPausedReceipt);

            assertEquals(unpaused_events.size(), 1);
            assertNotNull(unpaused_events.get(0).log.getLogIndex());
            assertNotNull(unpaused_events.get(0).log.getTransactionIndex());
            assertNotNull(unpaused_events.get(0).log.getTransactionHash());
            assertNotNull(unpaused_events.get(0).log.getBlockHash());
            assertNotNull(unpaused_events.get(0).log.getBlockNumber());
            assertNotNull(unpaused_events.get(0).log.getAddress());
            assertNotNull(unpaused_events.get(0).log.getData());
            assertNotNull(unpaused_events.get(0).log.getTopics());
            assertEquals(unpaused_events.get(0).account, mDeployerTxManager.getDefaultAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-030
    @Test public void getPauserRoleEvents() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        String userAddr = mTestCredential.getAddress();
        try {

            //Check PauserAdded Event
            KlayTransactionReceipt.TransactionReceipt addPauserReceipt = tokenHandler_owner.addPauser(userAddr).send();
            List<KIP7.PauserAddedEventResponse> addedEvents = tokenHandler_owner.getPauserAddedEvents(addPauserReceipt);

            assertEquals(addedEvents.size(), 1);
            assertNotNull(addedEvents.get(0).log.getLogIndex());
            assertNotNull(addedEvents.get(0).log.getTransactionIndex());
            assertNotNull(addedEvents.get(0).log.getTransactionHash());
            assertNotNull(addedEvents.get(0).log.getBlockHash());
            assertNotNull(addedEvents.get(0).log.getBlockNumber());
            assertNotNull(addedEvents.get(0).log.getAddress());
            assertNotNull(addedEvents.get(0).log.getData());
            assertNotNull(addedEvents.get(0).log.getTopics());
            assertEquals(addedEvents.get(0).account, userAddr);

            KlayTransactionReceipt.TransactionReceipt renounceReceipt = tokenHandler_pauser.renouncePauser().send();

            //Check PauserRemoved Event
            List<KIP7.PauserRemovedEventResponse> removedEvents = tokenHandler_owner.getPauserRemovedEvents(renounceReceipt);
            assertEquals(removedEvents.size(), 1);
            assertNotNull(removedEvents.get(0).log.getLogIndex());
            assertNotNull(removedEvents.get(0).log.getTransactionIndex());
            assertNotNull(removedEvents.get(0).log.getTransactionHash());
            assertNotNull(removedEvents.get(0).log.getBlockHash());
            assertNotNull(removedEvents.get(0).log.getBlockNumber());
            assertNotNull(removedEvents.get(0).log.getAddress());
            assertNotNull(removedEvents.get(0).log.getData());
            assertNotNull(removedEvents.get(0).log.getTopics());
            assertEquals(removedEvents.get(0).account, userAddr);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-031
    @Test
    public void getMinterRoleEvents() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider(mCaver));
        KIP7 tokenHandler_minter = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider(mCaver));
        String minter = mTesterTxManger.getDefaultAddress();

        try {
            //Check MinterAdded Event
            KlayTransactionReceipt.TransactionReceipt addMinterReceipt = tokenHandler_owner.addMinter(minter).send();

            List<KIP7.MinterAddedEventResponse> minterAddEvent = tokenHandler_owner.getMinterAddedEvents(addMinterReceipt);
            assertEquals(minterAddEvent.size(), 1);
            assertNotNull(minterAddEvent.get(0).log.getLogIndex());
            assertNotNull(minterAddEvent.get(0).log.getTransactionIndex());
            assertNotNull(minterAddEvent.get(0).log.getTransactionHash());
            assertNotNull(minterAddEvent.get(0).log.getBlockHash());
            assertNotNull(minterAddEvent.get(0).log.getBlockNumber());
            assertNotNull(minterAddEvent.get(0).log.getAddress());
            assertNotNull(minterAddEvent.get(0).log.getData());
            assertNotNull(minterAddEvent.get(0).log.getTopics());
            assertEquals(minterAddEvent.get(0).account, minter);

            //Check MinterRemoved Event
            KlayTransactionReceipt.TransactionReceipt renounceMinterReceipt = tokenHandler_minter.renounceMinter().send();

            List<KIP7.MinterRemovedEventResponse> renounceMinterEvent = tokenHandler_minter.getMinterRemovedEvents(renounceMinterReceipt);
            assertEquals(renounceMinterEvent.size(), 1);
            assertNotNull(renounceMinterEvent.get(0).log.getLogIndex());
            assertNotNull(renounceMinterEvent.get(0).log.getTransactionIndex());
            assertNotNull(renounceMinterEvent.get(0).log.getTransactionHash());
            assertNotNull(renounceMinterEvent.get(0).log.getBlockHash());
            assertNotNull(renounceMinterEvent.get(0).log.getBlockNumber());
            assertNotNull(renounceMinterEvent.get(0).log.getAddress());
            assertNotNull(renounceMinterEvent.get(0).log.getData());
            assertNotNull(renounceMinterEvent.get(0).log.getTopics());
            assertEquals(renounceMinterEvent.get(0).account, minter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
