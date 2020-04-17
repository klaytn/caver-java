package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.tx.kct.KIP7Token;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.utils.ChainId;
import com.klaytn.caver.utils.Convert;
import com.klaytn.caver.wallet.WalletManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.*;


public class KCT7TokenTest {
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
            KIP7Token token = KIP7Token.deploy(mCaver,
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String newMinterAddr = mTestCredential.getAddress();
        BigInteger mintAmount = ContractInitialSupply;
        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.mint(newMinterAddr, mintAmount).send();
            BigInteger minterBalance = tokenHandler_owner.balanceOf(newMinterAddr).send();

            BigInteger total = tokenHandler_owner.totalSupply().send();
            assertEquals(total, minterBalance.add(mintAmount));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-009
    @Test
    public void addMinter() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String newMinter = mTestCredential.getAddress();

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.addMinter(newMinter).send();
            boolean isMinter = tokenHandler_owner.isMinter(newMinter).send();
            assertTrue(isMinter);

            List<KIP7Token.MinterAddedEventResponse> list = tokenHandler_owner.getMinterAddedEvents(receipt);
            assertEquals(list.get(0).account, newMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-010
    @Test
    public void renounceMinter() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_minter = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String renounceMinter = mTesterTxManger.getDefaultAddress();

        try {
            tokenHandler_owner.addMinter(renounceMinter).send();
            boolean isMinter = tokenHandler_owner.isMinter(renounceMinter).send();
            assertTrue(isMinter);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_minter.renounceMinter().send();
            isMinter = tokenHandler_minter.isMinter(renounceMinter).send();
            assertFalse(isMinter);

            List<KIP7Token.MinterRemovedEventResponse> list = tokenHandler_minter.getMinterRemovedEvents(receipt);
            assertEquals(list.get(0).account, renounceMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-011
    @Test
    public void isPauser() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            List<KIP7Token.PausedEventResponse> events = tokenHandler_owner.getPausedEvents(receipt);
            assertEquals(events.get(0).account, mDeployerCredential.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-013
    @Test
    public void unpause() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            tokenHandler_owner.pause().send();
            boolean isPaused = tokenHandler_owner.paused().send();
            assertTrue(isPaused);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.unpause().send();
            isPaused = tokenHandler_owner.paused().send();
            assertFalse(isPaused);

            List<KIP7Token.UnpausedEventResponse> events = tokenHandler_owner.getUnpausedEvents(receipt);
            assertEquals(events.get(0).account, mDeployerCredential.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-014
    @Test
    public void addPauser() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        String userAddr = mTestCredential.getAddress();
        try {
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.addPauser(userAddr).send();
            boolean isPauseUser = tokenHandler_owner.isPauser(userAddr).send();
            assertTrue(isPauseUser);

            List<KIP7Token.PauserAddedEventResponse> events = tokenHandler_owner.getPauserAddedEvents(receipt);
            assertEquals(events.get(0).account, userAddr);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-015
    @Test
    public void renouncePauser() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_pauser = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String userAddr = mTestCredential.getAddress();

        try {
            tokenHandler_owner.addPauser(userAddr).send();
            boolean isPauseUser = tokenHandler_owner.isPauser(userAddr).send();
            assertTrue(isPauseUser);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_pauser.renouncePauser().send();
            isPauseUser = tokenHandler_pauser.isPauser(userAddr).send();
            assertFalse(isPauseUser);

            List<KIP7Token.PauserRemovedEventResponse> events = tokenHandler_pauser.getPauserRemovedEvents(receipt);
            assertEquals(events.get(0).account, userAddr);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-016
    @Test
    public void transfer() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.transfer(recipientAddress, amount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7Token.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7Token.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String recipientAddress = mTestCredential.getAddress();
        BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue()));

        try {
            byte[] data = "buffered data".getBytes();
            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.safeTransfer(recipientAddress, amount, data).send();
            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(balance, preBalance.add(amount));

            List<KIP7Token.TransferEventResponse> events = tokenHandler_owner.getTransferEvents(receipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String spenderAddress = mTestCredential.getAddress();
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt receipt = tokenHandler_owner.approve(spenderAddress, allowAmount).send();

            List<KIP7Token.ApprovalEventResponse> events = tokenHandler_owner.getApprovalEvents(receipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_spender = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7Token.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);

            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.transferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7Token.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_spender = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7Token.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);

            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7Token.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_spender = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(ContractDecimal.intValue())); // 10 * 10^18

        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String recipientAddress = "0x176de75de3c4f253c69dcbc6575b0ccbda724f75";

        try {
            BigInteger allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = tokenHandler_owner.approve(mTesterTxManger.getDefaultAddress(), allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, allowAmount);

            List<KIP7Token.ApprovalEventResponse> approvalEventResponseList = tokenHandler_owner.getApprovalEvents(approveReceipt);
            assertEquals(approvalEventResponseList.get(0).owner, mDeployerTxManager.getDefaultAddress());
            assertEquals(approvalEventResponseList.get(0).spender, spenderAddress);
            assertEquals(approvalEventResponseList.get(0).value, allowAmount);


            BigInteger preBalance = tokenHandler_owner.balanceOf(recipientAddress).send();

            byte[] data = "buffered data".getBytes();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = tokenHandler_spender.safeTransferFrom(mDeployerTxManager.getDefaultAddress(), recipientAddress, allowAmount).send();
            allowance = tokenHandler_owner.allowance(mDeployerTxManager.getDefaultAddress(), spenderAddress).send();
            assertEquals(allowance, BigInteger.ZERO);

            BigInteger balance = tokenHandler_owner.balanceOf(recipientAddress).send();
            assertEquals(preBalance.add(allowAmount), balance);

            List<KIP7Token.TransferEventResponse> transferEventResponseList = tokenHandler_owner.getTransferEvents(transferReceipt);
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
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
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

    //KCT-024
    @Test
    public void burnFrom() {
        KIP7Token tokenHandler_owner = KIP7Token.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP7Token tokenHandler_spender = KIP7Token.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
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





}
