package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.KIP7;
import com.klaytn.caver.kct.kip17.KIP17;
import com.klaytn.caver.kct.kip17.KIP17DeployParams;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.*;

public class KIP17Test {
    public static KIP17 kip17Contract;
    public static final String CONTRACT_NAME = "NFT";
    public static final String CONTRACT_SYMBOL = "NFT_KALE";
    private static final String sTokenURI = "https://game.example/item-id-8u5h2m.json";

    public static void deployContract() throws IOException, NoSuchMethodException, TransactionException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        KIP17DeployParams kip7DeployParam = new KIP17DeployParams(CONTRACT_NAME, CONTRACT_SYMBOL);
        SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(45000));
        kip17Contract = KIP17.deploy(caver, kip7DeployParam, LUMAN.getAddress());
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            deployContract();
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
    }

    public static class PausableTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
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
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
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
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
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

            }
        }
    }

    public static class EnumerableTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
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
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
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
}
