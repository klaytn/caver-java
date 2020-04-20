package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.tx.kct.KIP7;
import com.klaytn.caver.tx.manager.TransactionManager;
import org.junit.BeforeClass;
import org.junit.Test;
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

    //KCT-001
    public static void deployKIP7Contract() {
        try {
            KIP7 token = KIP7.deploy(mCaver,
                    mDeployerTxManager,
                    new DefaultGasProvider(),
                    ContractName,
                    ContractSymbol,
                    ContractDecimal,
                    ContractInitialSupply
            ).send();

            String address = token.getContractAddress();
            System.out.println("Contract Address is " + address);
            mContractAddress = address;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //KCT-002
    @Test
    public void name() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        try {
            String name = tokenHandler_owner.name().send();
            System.out.println("name is " + name);
            assertEquals(name, ContractName);
        } catch (Exception e) {
            fail("call name() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-003
    @Test
    public void symbol() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        try {
            String symbol = tokenHandler_owner.symbol().send();
            System.out.println("Symbol is " + symbol);
            assertEquals(symbol, ContractSymbol);
        } catch (Exception e) {
            fail("call symbol() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-004
    @Test
    public void decimals() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        try {
            BigInteger decimals = tokenHandler_owner.decimals().send();
            System.out.println("decimals is " + decimals);
            assertEquals(decimals, ContractDecimal);
        } catch (Exception e) {
            fail("call decimals() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-005
    @Test
    public void totalSupply() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        try {
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();
            System.out.println("totalSupply is " + totalSupply);
            assertEquals(totalSupply, ContractInitialSupply);
        } catch (Exception e) {
            fail("call totalSupply() function is failed");
            e.printStackTrace();
        }
    }

    //KCT-006
    @Test
    public void balanceOf() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddr = mDeployerCredential.getAddress();
        String notMinterAddr = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            boolean isMinter = tokenHandler_owner.isMinter(ownerAddr).send();
            System.out.println("Minter?? " + isMinter );
            assertTrue(isMinter);

            isMinter = tokenHandler_owner.isMinter(notMinterAddr).send();
            System.out.println("Minter?? " + isMinter );
            assertFalse(isMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-008
    @Test
    public void mint() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String newMinterAddr = mTestCredential.getAddress();
        BigInteger mintAmount = ContractInitialSupply;
        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(newMinterAddr).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.mint(newMinterAddr, mintAmount).send();
            BigInteger minterBalance = tokenHandler_owner.balanceOf(newMinterAddr).send();
            assertEquals(minterBalance, preBalance.add(mintAmount));

            ContractInitialSupply = tokenHandler_owner.totalSupply().send();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-009
    @Test
    public void addMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_minter = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String pauseUserAddr = mDeployerCredential.getAddress();
        String notPauseUserAddr = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            boolean isPauser = tokenHandler_owner.isPauser(pauseUserAddr).send();
            System.out.println("pause User?? " + isPauser );
            assertTrue(isPauser);

            isPauser = tokenHandler_owner.isPauser(notPauseUserAddr).send();
            System.out.println("pause User? " + isPauser );
            assertFalse(isPauser);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-012
    @Test
    public void paused() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            List<KIP7.PausedEventResponse> events = tokenHandler_owner.getPausedEvents(receipt);
            assertEquals(events.get(0).account, mDeployerCredential.getAddress());

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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.unpause().send();
            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);

            List<KIP7.UnpausedEventResponse> events = tokenHandler_owner.getUnpausedEvents(receipt);
            assertEquals(events.get(0).account, mDeployerCredential.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-014
    @Test
    public void addPauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String spenderAddress = mTestCredential.getAddress();
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-020
    @Test
    public void appprove() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String spenderAddress = mTestCredential.getAddress();
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.approve(spenderAddress, allowAmount).send();

            List<KIP7.ApprovalEventResponse> events = tokenHandler_owner.getApprovalEvents(receipt);
            assertEquals(events.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(events.get(0).spender, spenderAddress);
            assertEquals(events.get(0).value, allowAmount);

            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

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
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);

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
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-022
    @Test
    public void safeTransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);

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
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-023
    @Test
    public void safeTransferFromWithData() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);


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
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-024
    @Test
    public void burn() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18
        String ownerAddress = mDeployerTxManager.getDefaultAddress();

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger preTotalSupply = tokenHandler_owner.totalSupply().send();

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.burn(burnAmount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();
            assertEquals(preBalance.subtract(burnAmount), balance);
            assertEquals(preTotalSupply.subtract(burnAmount), totalSupply);

            ContractInitialSupply = totalSupply;

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-025
    @Test
    public void burnFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();


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
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

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

    //KCT-028
    @Test
    public void getTransferEvent_Burn() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.ONE.pow(ContractDecimal.intValue())); // 10 * 10^18
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

    //KCT-029
    @Test
    public void getTransferEvent_BurnFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.ONE.pow(ContractDecimal.intValue())); // 10 * 10^18

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";


        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger preTotalSupply = tokenHandler_owner.totalSupply().send();

            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger preAllowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(preAllowance, BigInteger.ZERO);

            tokenHandler_owner.approve(spenderAddress, burnAmount).send();
            BigInteger allowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(allowance, burnAmount);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_spender.burnFrom(ownerAddress, burnAmount).send();
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

    //KCT-030
    @Test
    public void getApprovalEvent_BurnFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger burnAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";


        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger preTotalSupply = tokenHandler_owner.totalSupply().send();

            tokenHandler_owner.approve(spenderAddress, BigInteger.ZERO).send();
            BigInteger preAllowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(preAllowance, BigInteger.ZERO);

            tokenHandler_owner.approve(spenderAddress, burnAmount).send();
            BigInteger allowance = tokenHandler_owner.allowance(ownerAddress, spenderAddress).send();
            assertEquals(allowance, burnAmount);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_spender.burnFrom(ownerAddress, burnAmount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(ownerAddress).send();
            BigInteger totalSupply = tokenHandler_owner.totalSupply().send();
            assertEquals(preBalance.subtract(burnAmount), balance);
            assertEquals(preTotalSupply.subtract(burnAmount), totalSupply);

            List<KIP7.ApprovalEventResponse> events = tokenHandler_owner.getApprovalEvents(receipt);
            assertEquals(events.get(0).owner, ownerAddress);
            assertEquals(events.get(0).spender, spenderAddress);
            assertEquals(events.get(0).value, BigInteger.ZERO);

            ContractInitialSupply = totalSupply;

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-031
    @Test
    public void getTransferEvent_Mint() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String newMinterAddr = mTestCredential.getAddress();
        String zeroAddr = "0x0000000000000000000000000000000000000000";
        BigInteger mintAmount = ContractInitialSupply;
        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.mint(newMinterAddr, mintAmount).send();

            List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
            assertEquals(events.get(0).from, zeroAddr);
            assertEquals(events.get(0).to, newMinterAddr);
            assertEquals(events.get(0).value, mintAmount);

            ContractInitialSupply = tokenHandler_owner.totalSupply().send();

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-032
    @Test
    public void getTransferEvent_Transfer() {
        try {
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
            String recipientAddress = mTestCredential.getAddress();
            BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

            try {
                KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.transfer(recipientAddress, amount).send();

                List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
                assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
                assertEquals(events.get(0).to, recipientAddress);
                assertEquals(events.get(0).value, amount);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //KCT-033
    @Test
    public void getTransferEvent_SafeTransfer() {
        try {
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
            String recipientAddress = mTestCredential.getAddress();
            BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

            try {
                KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount).send();

                List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
                assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
                assertEquals(events.get(0).to, recipientAddress);
                assertEquals(events.get(0).value, amount);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //KCT-034
    @Test
    public void getTransferEvent_SafeTransferWithData() {
        try {
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
            String recipientAddress = mTestCredential.getAddress();
            BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

            try {
                byte[] data = "buffered data".getBytes();
                KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount, data).send();

                List<KIP7.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
                assertEquals(events.get(0).from, mDeployerTxManager.getDefaultAddress());
                assertEquals(events.get(0).to, recipientAddress);
                assertEquals(events.get(0).value, amount);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //KCT-035
    @Test
    public void getTransferEvent_TransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.transferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();

            List<KIP7.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
            assertEquals(transferEventResponseList.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(transferEventResponseList.get(0).to, recipientAddress);
            assertEquals(transferEventResponseList.get(0).value, allowAmount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-036
    @Test
    public void getApprovalEvent_TransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.transferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            List<KIP7.ApprovalEventResponse> approvalEventResponses = tokenHandler_owner.getApprovalEvents(transferReceipt);

            assertEquals(approvalEventResponses.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponses.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponses.get(0).value, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-037
    @Test
    public void getTransferEvent_SafeTransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();

            List<KIP7.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
            assertEquals(transferEventResponseList.get(0).from, mDeployerTxManager.getDefaultAddress());
            assertEquals(transferEventResponseList.get(0).to, recipientAddress);
            assertEquals(transferEventResponseList.get(0).value, allowAmount);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-038
    @Test
    public void getApprovalEvent_SafeTransferFrom() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            List<KIP7.ApprovalEventResponse> approvalEventResponses = tokenHandler_owner.getApprovalEvents(transferReceipt);

            assertEquals(approvalEventResponses.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponses.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponses.get(0).value, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-039
    @Test
    public void getTransferEvent_SafeTransferFromWithData() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);


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
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-040
    @Test
    public void getApprovalEvent_SafeTransferFromWithData() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();

            byte[] data = "buffered data".getBytes();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount, data).send();
            List<KIP7.ApprovalEventResponse> approvalEventResponses = tokenHandler_owner.getApprovalEvents(transferReceipt);

            assertEquals(approvalEventResponses.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponses.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponses.get(0).value, BigInteger.ZERO);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-041
    @Test
    public void getApprovalEvent_Approve() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_spender = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-042
    @Test
    public void getPausedEvent_Pause() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            List<KIP7.PausedEventResponse> events = tokenHandler_owner.getPausedEvents(receipt);
            assertEquals(events.get(0).account, mDeployerCredential.getAddress());

            tokenHandler_owner.unpause().send();
            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-043
    @Test
    public void getUnPausedEvent_Unpause() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            KlayTransactionReceipt.TransactionReceipt unPausedReceipt = tokenHandler_owner.unpause().send();

            List<KIP7.UnpausedEventResponse> unpausedEventResponses = tokenHandler_owner.getUnpausedEvents(unPausedReceipt);
            assertEquals(unpausedEventResponses.get(0).account, mDeployerCredential.getAddress());

            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-044
    @Test
    public void getPauserAddedEvent_addPauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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

    //KCT-045
    @Test
    public void getPauserRemovedEvent_renouncePauser() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_pauser = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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

    //KCT-046
    @Test
    public void getMinterAddedEvent_addMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_minter = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String newMinter = mTestCredential.getAddress();
        String renounceMinter = mTesterTxManger.getDefaultAddress();

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.addMinter(newMinter).send();
            boolean isMinter = tokenHandler_owner.isMinter(newMinter).send();
            assertTrue(isMinter);

            List<KIP7.MinterAddedEventResponse> list = tokenHandler_owner.getMinterAddedEvents(receipt);
            assertEquals(list.get(0).account, newMinter);

            tokenHandler_minter.renounceMinter().send();
            isMinter = tokenHandler_minter.isMinter(renounceMinter).send();
            assertFalse(isMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-047
    @Test
    public void getMinterRemovedEvent_renounceMinter() {
        KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7 tokenHandler_minter = KIP7.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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




}
