package com.klaytn.caver.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayAccount;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.scenario.Scenario;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.kct.KIP17;
import com.klaytn.caver.kct.KIP7;
import com.klaytn.caver.tx.manager.TransactionManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

import static com.klaytn.caver.base.Accounts.*;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.*;

public class KIP17Test extends Scenario {
    private static final String sContractName = "NFTTest";
    private static final String sContractSymbol = "NFT";
    private static final String sTokenURI = "https://game.example/item-id-8u5h2m.json";
    private static final String sSTATUS_SUCCESS = "0x1";
    private static BigInteger sTotalSupply = BigInteger.ZERO;
    private String sZeroAddr = "0x0000000000000000000000000000000000000000";

    static Caver mCaver;
    static KlayCredentials mDeployerCredential, mTestCredential, mTestCredential2;
    static TransactionManager mDeployerTxManager, mTesterTxManger, mTesterTxManger2;
    static String mContractAddress;


    @BeforeClass
    public static void preSetup() throws Exception {
        mCaver = Caver.build(Caver.DEFAULT_URL);

        mDeployerCredential = LUMAN;
        mDeployerTxManager = new TransactionManager.Builder(mCaver, LUMAN).setChaindId(LOCAL_CHAIN_ID).build();

        mTestCredential = BRANDON;
        mTesterTxManger = new TransactionManager.Builder(mCaver, BRANDON).setChaindId(LOCAL_CHAIN_ID).build();

        mTestCredential2 = WAYNE;
        mTesterTxManger2 = new TransactionManager.Builder(mCaver, WAYNE).setChaindId(LOCAL_CHAIN_ID).build();

        deployKIP17Contract();
    }

    public static void deployKIP17Contract() {
        try {
            KIP17 token = KIP17.deploy(
                    mCaver,
                    mDeployerTxManager,
                    new StaticGasProvider(DefaultGasProvider.GAS_PRICE, BigInteger.valueOf(6_000_000)),
                    sContractName,
                    sContractSymbol
            ).send();

            mContractAddress = token.getContractAddress();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void checkTxStatus(KlayTransactionReceipt.TransactionReceipt receipt) {
        assertEquals("Error Status - " + receipt.getErrorMessage() + "\n",
                receipt.getStatus(),
                sSTATUS_SUCCESS);
    }

    private void checkTransferEventValue(KIP17.TransferEventResponse response, String from, String to, BigInteger tokenId) {
        assertEquals(response.from, from);
        assertEquals(response.to, to);
        assertEquals(response.tokenId, tokenId);
    }

    private void checkApprovalEventValue(KIP17.ApprovalEventResponse response, String owner, String approved, BigInteger tokenId) {
        assertEquals(response.owner, owner);
        assertEquals(response.approved, approved);
        assertEquals(response.tokenId, tokenId);
    }

    private void checkApprovalForAllEventValue(KIP17.ApprovalForAllEventResponse response, String owner, String operator, boolean approved) {
        assertEquals(response.owner, owner);
        assertEquals(response.operator, operator);
        assertEquals(response.approved, approved);
    }

    private void checkPausedEventValue(KIP17.PausedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    private void checkUnPausedEventValue(KIP17.UnpausedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    private void checkPauserAddedEventValue(KIP17.PauserAddedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    private void checkPauserRemovedEventValue(KIP17.PauserRemovedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    private void checkMinterAddedEventValue(KIP17.MinterAddedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    private void checkMinterRemovedEventValue(KIP17.MinterRemovedEventResponse response, String account) {
        assertEquals(response.account, account);
    }

    //KCT-032
    @Test
    public void deployContract() {
        try {
            KIP17 token = KIP17.deploy(
                    mCaver,
                    mDeployerTxManager,
                    new StaticGasProvider(DefaultGasProvider.GAS_PRICE, BigInteger.valueOf(6_000_000)),
                    sContractName,
                    sContractSymbol
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

    //KCT-033
    @Test
    public void name() {
        KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        try {
            String name = tokenHandler.name().send();
            assertEquals(name, sContractName);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-034
    @Test
    public void symbol() {
        try {
            KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

            String symbol = tokenHandler.symbol().send();
            assertEquals(symbol, sContractSymbol);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-035
    @Test
    public void totalSupply() {
        KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        try {
            BigInteger preTotalCount = tokenHandler.totalSupply().send();
            BigInteger tokenId = BigInteger.valueOf(100000);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = tokenHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);

            List<KIP17.TransferEventResponse> transferEventResponses = tokenHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            sTotalSupply = tokenHandler.totalSupply().send();

            assertEquals(preTotalCount.add(BigInteger.ONE), sTotalSupply);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-036
    @Test
    public void balanceOf() {
        KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();

        try {
            BigInteger preBalance = tokenHandler.balanceOf(ownerAddress).send();
            BigInteger tokenId = BigInteger.valueOf(100001);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = tokenHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = tokenHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            BigInteger currentBalance = tokenHandler.balanceOf(ownerAddress).send();
            assertEquals(preBalance.add(BigInteger.ONE), currentBalance);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-037
    @Test
    public void ownerOf() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(6000);

            KlayTransactionReceipt.TransactionReceipt receipt = ownerHandler.mint(userAddress, tokenId).send();
            checkTxStatus(receipt);
            List<KIP17.TransferEventResponse> list = ownerHandler.getTransferEvents(receipt);
            checkTransferEventValue(list.get(0), sZeroAddr, userAddress, tokenId);

            String address = ownerHandler.ownerOf(tokenId).send();
            assertEquals(userAddress, address);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-038
    @Test
    public void pausedFeature() {
        KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        try {
            boolean isPaused = tokenHandler.paused().send();
            if(isPaused) {
                KlayTransactionReceipt.TransactionReceipt preUnpauseReceipt = tokenHandler.unpause().send();
                checkTxStatus(preUnpauseReceipt);

                assertFalse(tokenHandler.paused().send());
            }

            //Pause Test
            KlayTransactionReceipt.TransactionReceipt pausedReceipt = tokenHandler.pause().send();
            checkTxStatus(pausedReceipt);

            List<KIP17.PausedEventResponse> pausedEventResponses = tokenHandler.getPausedEvents(pausedReceipt);
            checkPausedEventValue(pausedEventResponses.get(0), ownerAddress);

            isPaused = tokenHandler.paused().send();
            assertTrue(isPaused);

            //Unpause Test
            KlayTransactionReceipt.TransactionReceipt unPausedReceipt = tokenHandler.unpause().send();
            checkTxStatus(unPausedReceipt);

            List<KIP17.UnpausedEventResponse> unPausedEventResponses = tokenHandler.getUnpausedEvents(unPausedReceipt);
            checkUnPausedEventValue(unPausedEventResponses.get(0), ownerAddress);

            isPaused = tokenHandler.paused().send();
            assertFalse(isPaused);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCt-039
    @Test
    public void addPauser() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 pauserHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            boolean isPauser = pauserHandler.isPauser(userAddress).send();
            if(isPauser) {
                KlayTransactionReceipt.TransactionReceipt renouncePauserReceipt = pauserHandler.renouncePauser().send();
                checkTxStatus(renouncePauserReceipt);

                List<KIP17.PauserRemovedEventResponse> pauserRemovedEventResponses = pauserHandler.getPauserRemovedEvents(renouncePauserReceipt);
                checkPauserRemovedEventValue(pauserRemovedEventResponses.get(0), ownerAddress);

                assertFalse(pauserHandler.isPauser(ownerAddress).send());
            }

            //Test
            KlayTransactionReceipt.TransactionReceipt receipt = ownerHandler.addPauser(userAddress).send();
            checkTxStatus(receipt);

            List<KIP17.PauserAddedEventResponse> pauserAddedEventResponses = ownerHandler.getPauserAddedEvents(receipt);
            checkPauserAddedEventValue(pauserAddedEventResponses.get(0), userAddress);

            isPauser = ownerHandler.isPauser(mTesterTxManger.getDefaultAddress()).send();
            assertTrue(isPauser);

            //reset
            KlayTransactionReceipt.TransactionReceipt renounceReceipt = pauserHandler.renouncePauser().send();
            checkTxStatus(renounceReceipt);

            List<KIP17.PauserRemovedEventResponse> pauserRemovedEventResponses = ownerHandler.getPauserRemovedEvents(renounceReceipt);
            checkPauserRemovedEventValue(pauserRemovedEventResponses.get(0), userAddress);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-040
    @Test
    public void renouncePauser() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 pauserHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String pauserAddress = mTesterTxManger.getDefaultAddress();

        try {
            boolean isPauser = ownerHandler.isPauser(pauserAddress).send();
            if(!isPauser) {
                KlayTransactionReceipt.TransactionReceipt addPauserReceipt = ownerHandler.addPauser(pauserAddress).send();
                checkTxStatus(addPauserReceipt);

                List<KIP17.PauserAddedEventResponse> list = ownerHandler.getPauserAddedEvents(addPauserReceipt);
                checkPauserAddedEventValue(list.get(0), pauserAddress);
            }

            //Test
            KlayTransactionReceipt.TransactionReceipt receipt = pauserHandler.renouncePauser().send();
            checkTxStatus(receipt);

            List<KIP17.PauserRemovedEventResponse> pauserRemovedEventResponses = ownerHandler.getPauserRemovedEvents(receipt);
            checkPauserRemovedEventValue(pauserRemovedEventResponses.get(0), pauserAddress);

            isPauser = ownerHandler.isPauser(mTesterTxManger.getDefaultAddress()).send();
            assertFalse(isPauser);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-041
    @Test
    public void addMinter() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 minterHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String minterAddress = mTesterTxManger.getDefaultAddress();

        try {
            boolean isMinter = ownerHandler.isMinter(mTesterTxManger.getDefaultAddress()).send();
            if(isMinter) {
                KlayTransactionReceipt.TransactionReceipt renounceMinterReceipt = minterHandler.renounceMinter().send();
                checkTxStatus(renounceMinterReceipt);

                List<KIP17.MinterRemovedEventResponse> removedEventResponses = minterHandler.getMinterRemovedEvents(renounceMinterReceipt);
                checkMinterRemovedEventValue(removedEventResponses.get(0), minterAddress);
            }
            //Test
            KlayTransactionReceipt.TransactionReceipt addMinterReceipt = ownerHandler.addMinter(mTesterTxManger.getDefaultAddress()).send();
            checkTxStatus(addMinterReceipt);
            isMinter = ownerHandler.isMinter(mTesterTxManger.getDefaultAddress()).send();
            assertTrue(isMinter);

            List<KIP17.MinterAddedEventResponse> minterAddedEventResponses = ownerHandler.getMinterAddedEvents(addMinterReceipt);
            checkMinterAddedEventValue(minterAddedEventResponses.get(0), minterAddress);

            //reset
            KlayTransactionReceipt.TransactionReceipt renounceMinterReceipt = minterHandler.renounceMinter().send();
            checkTxStatus(renounceMinterReceipt);

            List<KIP17.MinterRemovedEventResponse> removedEventResponses = minterHandler.getMinterRemovedEvents(renounceMinterReceipt);
            checkMinterRemovedEventValue(removedEventResponses.get(0), minterAddress);

            isMinter = ownerHandler.isMinter(mTesterTxManger.getDefaultAddress()).send();
            assertFalse(isMinter);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-042
    @Test
    public void renounceMinter() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 minterHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String minterAddress = mTesterTxManger.getDefaultAddress();

        try {
            boolean isMinter = ownerHandler.isMinter(minterAddress).send();
            if(!isMinter) {
                KlayTransactionReceipt.TransactionReceipt addMinterReceipt = ownerHandler.addMinter(minterAddress).send();
                checkTxStatus(addMinterReceipt);
                assertTrue(ownerHandler.isMinter(minterAddress).send());
            }

            //Test
            KlayTransactionReceipt.TransactionReceipt renounceMinterReceipt = minterHandler.renounceMinter().send();
            checkTxStatus(renounceMinterReceipt);

            List<KIP17.MinterRemovedEventResponse> minterRemovedEventResponses = minterHandler.getMinterRemovedEvents(renounceMinterReceipt);
            checkMinterRemovedEventValue(minterRemovedEventResponses.get(0), minterAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-043
    @Test
    public void mint() {
        KIP17 minterHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        try {
            BigInteger tokenId = sTotalSupply;
            BigInteger preTotalSupply = minterHandler.totalSupply().send();

            //Test
            KlayTransactionReceipt.TransactionReceipt receipt = minterHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(receipt);
            List<KIP17.TransferEventResponse> transferEventResponses = minterHandler.getTransferEvents(receipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            String tokenOwnerAddr = minterHandler.ownerOf(tokenId).send();
            assertEquals(ownerAddress, tokenOwnerAddr);

            BigInteger total = minterHandler.totalSupply().send();
            assertEquals(preTotalSupply.add(BigInteger.ONE), total);

            sTotalSupply = total;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-044
    @Test
    public void mintWithTokenURI() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddr = mDeployerTxManager.getDefaultAddress();
        try {
            BigInteger tokenId = sTotalSupply;
            BigInteger preTotalSupply = ownerHandler.totalSupply().send();

            //Test
            KlayTransactionReceipt.TransactionReceipt receipt = ownerHandler.mintWithTokenURI(mDeployerTxManager.getDefaultAddress(), tokenId, sTokenURI).send();
            checkTxStatus(receipt);

            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(receipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddr, tokenId);

            String uri = ownerHandler.tokenURI(tokenId).send();
            assertEquals(uri, sTokenURI);

            String tokenOwnerAddr = ownerHandler.ownerOf(tokenId).send();
            assertEquals(ownerAddr, tokenOwnerAddr);

            BigInteger total = ownerHandler.totalSupply().send();
            assertEquals(preTotalSupply.add(BigInteger.ONE), total);

            sTotalSupply = total;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-045
    @Test
    public void tokenOfOwnerByIndex() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 tester2Handler = KIP17.load(mContractAddress, mCaver, mTesterTxManger2, new DefaultGasProvider());

        String userAddress = "0x25925f77ea2c3b82a1ab45858558076fdc44fcc4";
        BigInteger[] tokenIDArr = new BigInteger[] {BigInteger.valueOf(1000), BigInteger.valueOf(1001), BigInteger.valueOf(1002)};

        try {
            KlayTransactionReceipt.TransactionReceipt mintReceipt1 = ownerHandler.mint(userAddress, tokenIDArr[0]).send();
            checkTxStatus(mintReceipt1);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt1);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, userAddress, tokenIDArr[0]);

            KlayTransactionReceipt.TransactionReceipt mintReceipt2 = ownerHandler.mint(userAddress, tokenIDArr[1]).send();
            checkTxStatus(mintReceipt2);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt2);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, userAddress, tokenIDArr[1]);

            KlayTransactionReceipt.TransactionReceipt mintReceipt3 = ownerHandler.mint(userAddress, tokenIDArr[2]).send();
            checkTxStatus(mintReceipt3);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt3);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, userAddress, tokenIDArr[2]);

            //Test
            BigInteger tokenID_1 = tester2Handler.tokenOfOwnerByIndex(userAddress, BigInteger.ZERO).send();
            assertEquals(tokenID_1, tokenIDArr[0]);

            BigInteger tokenID_2 = tester2Handler.tokenOfOwnerByIndex(userAddress, BigInteger.ONE).send();
            assertEquals(tokenID_2, tokenIDArr[1]);

            BigInteger tokenID_3 = tester2Handler.tokenOfOwnerByIndex(userAddress, BigInteger.valueOf(2)).send();
            assertEquals(tokenID_3, tokenIDArr[2]);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-046
    @Test
    public void tokenByIndex() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();

        try {
            BigInteger total = ownerHandler.totalSupply().send();
            BigInteger[] tokenIdArr = new BigInteger[]{total, total.add(BigInteger.ONE), total.add(BigInteger.valueOf(2))};

            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(mDeployerTxManager.getDefaultAddress(), tokenIdArr[0]).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[0]);

            mintReceipt = ownerHandler.mint(mDeployerTxManager.getDefaultAddress(), tokenIdArr[1]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[1]);

            mintReceipt = ownerHandler.mint(mDeployerTxManager.getDefaultAddress(), tokenIdArr[2]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[2]);

            total = ownerHandler.totalSupply().send();

            //Test
            BigInteger findIndex = total.subtract(BigInteger.ONE);
            BigInteger tokenId = ownerHandler.tokenByIndex(findIndex).send();
            assertEquals(tokenId, tokenIdArr[2]);

            findIndex = findIndex.subtract(BigInteger.ONE);
            tokenId = ownerHandler.tokenByIndex(findIndex).send();
            assertEquals(tokenId, tokenIdArr[1]);

            findIndex = findIndex.subtract(BigInteger.ONE);
            tokenId = ownerHandler.tokenByIndex(findIndex).send();
            assertEquals(tokenId, tokenIdArr[0]);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-047
    @Test
    public void transferFrom() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(2000);

            KlayTransactionReceipt.TransactionReceipt receipt = ownerHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(receipt);

            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(receipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            KlayTransactionReceipt.TransactionReceipt transactionReceipt = ownerHandler.transferFrom(ownerAddress, userAddress, tokenId).send();
            checkTxStatus(transactionReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(transactionReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            String address = ownerHandler.ownerOf(tokenId).send();
            assertEquals(address, userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-048
    @Test
    public void safeTransferFrom() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(2001);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            KlayTransactionReceipt.TransactionReceipt transferReceipt = ownerHandler.safeTransferFrom(ownerAddress, userAddress, tokenId).send();
            checkTxStatus(transferReceipt);
            transferEventResponses= ownerHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            String address = ownerHandler.ownerOf(tokenId).send();
            assertEquals(address, userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-049
    @Test
    public void safeTransferFromWithData() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(2002);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            byte[] data = "buffered data".getBytes();

            KlayTransactionReceipt.TransactionReceipt transferReceipt = ownerHandler.safeTransferFrom(ownerAddress, userAddress, tokenId, data).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            String address = ownerHandler.ownerOf(tokenId).send();
            assertEquals(address, userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-050
    @Test
    public void getApproved() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(6001);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> list = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(list.get(0), sZeroAddr, ownerAddress, tokenId);

            KlayTransactionReceipt.TransactionReceipt approvedReceipt = ownerHandler.approve(userAddress, tokenId).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalEventResponse> approvalEventResponses = ownerHandler.getApprovalEvents(approvedReceipt);
            checkApprovalEventValue(approvalEventResponses.get(0), ownerAddress, userAddress, tokenId);

            String address = ownerHandler.getApproved(tokenId).send();
            assertEquals(userAddress, address);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-051
    @Test
    public void isApprovedForAll() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            KlayTransactionReceipt.TransactionReceipt receipt = ownerHandler.setApprovalForAll(userAddress, true).send();
            checkTxStatus(receipt);
            List<KIP17.ApprovalForAllEventResponse> approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(receipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, userAddress, true);

            boolean isApprovedAll = ownerHandler.isApprovedForAll(ownerAddress, userAddress).send();
            assertTrue(isApprovedAll);

            receipt = ownerHandler.setApprovalForAll(userAddress, false).send();
            checkTxStatus(receipt);
            approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(receipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, userAddress, false);

            isApprovedAll = ownerHandler.isApprovedForAll(ownerAddress, userAddress).send();
            assertFalse(isApprovedAll);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    //KCT-052
    @Test
    public void approve() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 operatorHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String operatorAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(4000);

            //mint token to owner address
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            //test
            KlayTransactionReceipt.TransactionReceipt approveReceipt = ownerHandler.approve(operatorAddress, tokenId).send();
            checkTxStatus(approveReceipt);
            List<KIP17.ApprovalEventResponse> approvalEventResponses = ownerHandler.getApprovalEvents(approveReceipt);
            checkApprovalEventValue(approvalEventResponses.get(0), ownerAddress, operatorAddress, tokenId);

            String approvedAddress = ownerHandler.getApproved(tokenId).send();
            assertEquals(operatorAddress, approvedAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-053
    @Test
    public void setApprovalForAll() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 operatorHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String operatorAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(4100), BigInteger.valueOf(4200)};
            //mint token to owner address
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[0]).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[0]);

            mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[1]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[1]);

            //test
            KlayTransactionReceipt.TransactionReceipt approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, true).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalForAllEventResponse> approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, true);

            boolean isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertTrue(isApproved);

            //reset
            approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, false).send();
            checkTxStatus(approvedReceipt);
            approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, false);

            isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertFalse(isApproved);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-054
    @Test
    public void burn() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();

        try {
            BigInteger tokenID = BigInteger.valueOf(9999);
            //mint Token
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenID).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponseList = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponseList.get(0), sZeroAddr, ownerAddress, tokenID);

            String address = ownerHandler.ownerOf(tokenID).send();
            assertEquals(ownerAddress, address);

            BigInteger preOwnerBalance = ownerHandler.balanceOf(ownerAddress).send();
            //Test
            KlayTransactionReceipt.TransactionReceipt burnReceipt = ownerHandler.burn(tokenID).send();
            checkTxStatus(burnReceipt);
            transferEventResponseList = ownerHandler.getTransferEvents(burnReceipt);
            checkTransferEventValue(transferEventResponseList.get(0), ownerAddress, sZeroAddr, tokenID);

            BigInteger ownerBalance = ownerHandler.balanceOf(ownerAddress).send();
            assertEquals(preOwnerBalance.subtract(BigInteger.ONE), ownerBalance);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-055
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
            KIP7 tokenHandler_owner = KIP7.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

            boolean isSupported_KIP13 = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP13)).send();
            assertTrue(isSupported_KIP13);

            boolean isSupported_KIP17_PAUSABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_PAUSABLE)).send();
            assertTrue(isSupported_KIP17_PAUSABLE);

            boolean isSupported_KIP17_BURNABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_BURNABLE)).send();
            assertTrue(isSupported_KIP17_BURNABLE);

            boolean isSupported_KIP17_MINTABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_MINTABLE)).send();
            assertTrue(isSupported_KIP17_MINTABLE);

            boolean isSupported_KIP17_METADATA = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_METADATA)).send();
            assertTrue(isSupported_KIP17_METADATA);

            boolean isSupported_KIP17_METADATA_MINTABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_METADATA_MINTABLE)).send();
            assertTrue(isSupported_KIP17_METADATA_MINTABLE);

            boolean isSupported_KIP17_ENUMERABLE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17_ENUMERABLE)).send();
            assertTrue(isSupported_KIP17_ENUMERABLE);

            boolean isSupported_KIP17 = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_KIP17)).send();
            assertTrue(isSupported_KIP17);

            boolean isSupported_FALSE = tokenHandler_owner.supportsInterface(Numeric.hexStringToByteArray(INTERFACE_ID_FALSE)).send();
            assertFalse(isSupported_FALSE);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-056
    @Test
    public void getTransferEventTest() {
        KIP17 tokenHandler_owner = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String toAddr = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(2222);

            KlayTransactionReceipt.TransactionReceipt receipt =  tokenHandler_owner.mint(toAddr, tokenId).send();
            checkTxStatus(receipt);
            List<KIP17.TransferEventResponse> event = tokenHandler_owner.getTransferEvents(receipt);

            assertEquals(event.size(), 1);
            assertNotNull(event.get(0).log.getLogIndex());
            assertNotNull(event.get(0).log.getTransactionIndex());
            assertNotNull(event.get(0).log.getTransactionHash());
            assertNotNull(event.get(0).log.getBlockHash());
            assertNotNull(event.get(0).log.getBlockNumber());
            assertNotNull(event.get(0).log.getAddress());
            assertNotNull(event.get(0).log.getData());
            assertNotNull(event.get(0).log.getTopics());
            assertEquals(event.get(0).from, sZeroAddr);
            assertEquals(event.get(0).to, toAddr);
            assertEquals(event.get(0).tokenId, tokenId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-057
    @Test
    public void getApprovalEventTest() {
        KIP17 tokenHandler_owner = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String userAddress = mTesterTxManger.getDefaultAddress();

        try {
            BigInteger tokenId = BigInteger.valueOf(2223);

            KlayTransactionReceipt.TransactionReceipt receipt =  tokenHandler_owner.mint(ownerAddress, tokenId).send();
            checkTxStatus(receipt);

            KlayTransactionReceipt.TransactionReceipt approvedReceipt = tokenHandler_owner.approve(userAddress, tokenId).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalEventResponse> event = tokenHandler_owner.getApprovalEvents(approvedReceipt);


            assertEquals(event.size(), 1);
            assertNotNull(event.get(0).log.getLogIndex());
            assertNotNull(event.get(0).log.getTransactionIndex());
            assertNotNull(event.get(0).log.getTransactionHash());
            assertNotNull(event.get(0).log.getBlockHash());
            assertNotNull(event.get(0).log.getBlockNumber());
            assertNotNull(event.get(0).log.getAddress());
            assertNotNull(event.get(0).log.getData());
            assertNotNull(event.get(0).log.getTopics());
            assertEquals(event.get(0).owner, ownerAddress);
            assertEquals(event.get(0).approved, userAddress);
            assertEquals(event.get(0).tokenId, tokenId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-058
    @Test
    public void getPausedEventTest() {
        KIP17 tokenHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());

        try {
            //Check Paused Event
            KlayTransactionReceipt.TransactionReceipt pausedReceipt = tokenHandler.pause().send();
            List<KIP17.PausedEventResponse> paused_events = tokenHandler.getPausedEvents(pausedReceipt);

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
            KlayTransactionReceipt.TransactionReceipt unPausedReceipt = tokenHandler.unpause().send();
            List<KIP17.UnpausedEventResponse> unpaused_events = tokenHandler.getUnpausedEvents(unPausedReceipt);

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

    //KCT-059
    @Test public void getPauserRoleEvents() {
        KIP17 tokenHandler_owner = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 tokenHandler_pauser = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String userAddr = mTestCredential.getAddress();
        try {

            //Check PauserAdded Event
            KlayTransactionReceipt.TransactionReceipt addPauserReceipt = tokenHandler_owner.addPauser(userAddr).send();
            List<KIP17.PauserAddedEventResponse> addedEvents = tokenHandler_owner.getPauserAddedEvents(addPauserReceipt);

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
            List<KIP17.PauserRemovedEventResponse> removedEvents = tokenHandler_owner.getPauserRemovedEvents(renounceReceipt);
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

    //KCT-060
    @Test
    public void getMinterRoleEvents() {
        KIP17 tokenHandler_owner = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 tokenHandler_minter = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String minter = mTesterTxManger.getDefaultAddress();

        try {
            //Check MinterAdded Event
            KlayTransactionReceipt.TransactionReceipt addMinterReceipt = tokenHandler_owner.addMinter(minter).send();

            List<KIP17.MinterAddedEventResponse> minterAddEvent = tokenHandler_owner.getMinterAddedEvents(addMinterReceipt);
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

            List<KIP17.MinterRemovedEventResponse> renounceMinterEvent = tokenHandler_minter.getMinterRemovedEvents(renounceMinterReceipt);
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

    //KCT-061
    @Test
    public void transferFrom_Approve() {
        KIP17 owner_handler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 spender_handler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();
        try {
            BigInteger tokenId = BigInteger.valueOf(7777);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = owner_handler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = owner_handler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            String address = owner_handler.ownerOf(tokenId).send();
            assertEquals(address , ownerAddress);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = owner_handler.approve(spenderAddress, tokenId).send();
            checkTxStatus(approveReceipt);
            List<KIP17.ApprovalEventResponse> approvalEventResponses = owner_handler.getApprovalEvents(approveReceipt);
            checkApprovalEventValue(approvalEventResponses.get(0), ownerAddress, spenderAddress, tokenId);

            address = owner_handler.getApproved(tokenId).send();
            assertEquals(address, spenderAddress);

            KlayTransactionReceipt.TransactionReceipt transferReceipt = spender_handler.transferFrom(ownerAddress, userAddress, tokenId).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = spender_handler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            address = spender_handler.ownerOf(tokenId).send();
            assertEquals(address , userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-062
    @Test
    public void safeTransferFrom_Approve() {
        KIP17 owner_handler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 spender_handler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();
        try {
            BigInteger tokenId = BigInteger.valueOf(7778);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = owner_handler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = owner_handler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            String address = owner_handler.ownerOf(tokenId).send();
            assertEquals(address , ownerAddress);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = owner_handler.approve(spenderAddress, tokenId).send();
            checkTxStatus(approveReceipt);
            List<KIP17.ApprovalEventResponse> approvalEventResponses = owner_handler.getApprovalEvents(approveReceipt);
            checkApprovalEventValue(approvalEventResponses.get(0), ownerAddress, spenderAddress, tokenId);

            address = owner_handler.getApproved(tokenId).send();
            assertEquals(address, spenderAddress);

            KlayTransactionReceipt.TransactionReceipt transferReceipt = spender_handler.safeTransferFrom(ownerAddress, userAddress, tokenId).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = spender_handler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            address = spender_handler.ownerOf(tokenId).send();
            assertEquals(address , userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-063
    @Test
    public void safeTransferFromWithData_Approve() {
        KIP17 owner_handler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 spender_handler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());
        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String spenderAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();
        try {
            BigInteger tokenId = BigInteger.valueOf(7779);

            KlayTransactionReceipt.TransactionReceipt mintReceipt = owner_handler.mint(ownerAddress, tokenId).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = owner_handler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenId);

            String address = owner_handler.ownerOf(tokenId).send();
            assertEquals(address , ownerAddress);

            KlayTransactionReceipt.TransactionReceipt approveReceipt = owner_handler.approve(spenderAddress, tokenId).send();
            checkTxStatus(approveReceipt);
            List<KIP17.ApprovalEventResponse> approvalEventResponses = owner_handler.getApprovalEvents(approveReceipt);
            checkApprovalEventValue(approvalEventResponses.get(0), ownerAddress, spenderAddress, tokenId);

            address = owner_handler.getApproved(tokenId).send();
            assertEquals(address, spenderAddress);

            byte[] data = "buffered data".getBytes();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = spender_handler.safeTransferFrom(ownerAddress, userAddress, tokenId, data).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = spender_handler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenId);

            address = spender_handler.ownerOf(tokenId).send();
            assertEquals(address , userAddress);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-064
    @Test
    public void transferFrom_SetApprovedForAll() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 operatorHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String operatorAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();

        try {
            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(4001), BigInteger.valueOf(4002)};
            //mint token to owner address
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[0]).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[0]);

            mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[1]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[1]);

            //test
            KlayTransactionReceipt.TransactionReceipt approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, true).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalForAllEventResponse> approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, true);

            boolean isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertTrue(isApproved);

            KlayTransactionReceipt.TransactionReceipt transferReceipt = operatorHandler.transferFrom(ownerAddress, userAddress, tokenIdArr[0]).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[0]);

            String address = ownerHandler.ownerOf(tokenIdArr[0]).send();
            assertEquals(address, userAddress);

            transferReceipt = operatorHandler.transferFrom(ownerAddress, userAddress, tokenIdArr[1]).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[1]);

            address = ownerHandler.ownerOf(tokenIdArr[1]).send();
            assertEquals(address, userAddress);

            //reset
            approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, false).send();
            checkTxStatus(approvedReceipt);
            approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, false);

            isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertFalse(isApproved);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-065
    @Test
    public void safeTransferFrom_SetApprovedForAll() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 operatorHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String operatorAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();

        try {
            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(4003), BigInteger.valueOf(4004)};
            //mint token to owner address
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[0]).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[0]);

            mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[1]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[1]);

            //test
            KlayTransactionReceipt.TransactionReceipt approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, true).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalForAllEventResponse> approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, true);

            boolean isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertTrue(isApproved);

            KlayTransactionReceipt.TransactionReceipt transferReceipt = operatorHandler.safeTransferFrom(ownerAddress, userAddress, tokenIdArr[0]).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[0]);

            String address = ownerHandler.ownerOf(tokenIdArr[0]).send();
            assertEquals(address, userAddress);

            transferReceipt = operatorHandler.safeTransferFrom(ownerAddress, userAddress, tokenIdArr[1]).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[1]);

            address = ownerHandler.ownerOf(tokenIdArr[1]).send();
            assertEquals(address, userAddress);

            //reset
            approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, false).send();
            checkTxStatus(approvedReceipt);
            approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, false);

            isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertFalse(isApproved);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    //KCT-066
    @Test
    public void safeTransferFromWithData_SetApprovedForAll() {
        KIP17 ownerHandler = KIP17.load(mContractAddress, mCaver, mDeployerTxManager, new DefaultGasProvider());
        KIP17 operatorHandler = KIP17.load(mContractAddress, mCaver, mTesterTxManger, new DefaultGasProvider());

        String ownerAddress = mDeployerTxManager.getDefaultAddress();
        String operatorAddress = mTesterTxManger.getDefaultAddress();
        String userAddress = mTesterTxManger2.getDefaultAddress();

        try {
            BigInteger[] tokenIdArr = new BigInteger[] {BigInteger.valueOf(4005), BigInteger.valueOf(4006)};
            //mint token to owner address
            KlayTransactionReceipt.TransactionReceipt mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[0]).send();
            checkTxStatus(mintReceipt);
            List<KIP17.TransferEventResponse> transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[0]);

            mintReceipt = ownerHandler.mint(ownerAddress, tokenIdArr[1]).send();
            checkTxStatus(mintReceipt);
            transferEventResponses = ownerHandler.getTransferEvents(mintReceipt);
            checkTransferEventValue(transferEventResponses.get(0), sZeroAddr, ownerAddress, tokenIdArr[1]);

            //test
            KlayTransactionReceipt.TransactionReceipt approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, true).send();
            checkTxStatus(approvedReceipt);
            List<KIP17.ApprovalForAllEventResponse> approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, true);

            boolean isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertTrue(isApproved);

            byte[] data = "buffered data".getBytes();
            KlayTransactionReceipt.TransactionReceipt transferReceipt = operatorHandler.safeTransferFrom(ownerAddress, userAddress, tokenIdArr[0], data).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[0]);

            String address = ownerHandler.ownerOf(tokenIdArr[0]).send();
            assertEquals(address, userAddress);

            transferReceipt = operatorHandler.safeTransferFrom(ownerAddress, userAddress, tokenIdArr[1], data).send();
            checkTxStatus(transferReceipt);
            transferEventResponses = operatorHandler.getTransferEvents(transferReceipt);
            checkTransferEventValue(transferEventResponses.get(0), ownerAddress, userAddress, tokenIdArr[1]);

            address = ownerHandler.ownerOf(tokenIdArr[1]).send();
            assertEquals(address, userAddress);

            //reset
            approvedReceipt = ownerHandler.setApprovalForAll(operatorAddress, false).send();
            checkTxStatus(approvedReceipt);
            approvalForAllEventResponses = ownerHandler.getApprovalForAllEvents(approvedReceipt);
            checkApprovalForAllEventValue(approvalForAllEventResponses.get(0), ownerAddress, operatorAddress, false);

            isApproved = ownerHandler.isApprovedForAll(ownerAddress, operatorAddress).send();
            assertFalse(isApproved);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
