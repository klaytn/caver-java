package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParam;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.klaytn.caver.base.Accounts.*;
import static org.junit.Assert.*;

public class KIP7Test {

    public static KIP7 kip7contract;
    public static final String CONTRACT_NAME = "Kale";
    public static final String CONTRACT_SYMBOL = "KALE";
    public static final int CONTRACT_DECIMALS = 18;
    public static final BigInteger CONTRACT_INITIAL_SUPPLY = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 100000 * 10^18

    public static void deployContract() throws IOException, NoSuchMethodException, TransactionException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        KIP7DeployParam kip7DeployParam = new KIP7DeployParam(CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);
        SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(45000));
        kip7contract = KIP7.deploy(caver, kip7DeployParam, LUMAN.getAddress());
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            KIP7Test.deployContract();
        }

        @Test
        public void name(){
            try {
                String name = kip7contract.name();
                assertEquals(CONTRACT_NAME, name);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void symbol(){
            try {
                String symbol = kip7contract.symbol();
                assertEquals(CONTRACT_SYMBOL, symbol);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void decimals(){
            try {
                String decimals = kip7contract.decimals();
                assertEquals(BigInteger.valueOf(CONTRACT_DECIMALS), Numeric.toBigInt(decimals));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void totalSupply(){
            try {
                String totalSupply = kip7contract.totalSupply();
                assertEquals(CONTRACT_INITIAL_SUPPLY, Numeric.toBigInt(totalSupply));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }
    }

    public static class PausableTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            KIP7Test.deployContract();
        }

        @Before
        public void setUnpause() {
            try {
                kip7contract.setDefaultSendOptions(new SendOptions());
                if(kip7contract.paused()) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    TransactionReceipt.TransactionReceiptData receiptData = kip7contract.unpause(options);
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
                kip7contract.pause(options);
                assertTrue(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedDefaultOptions() {
            try{
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip7contract.setDefaultSendOptions(options);
                kip7contract.pause();
                assertTrue(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoDefaultGas() {
            try {
                kip7contract.getDefaultSendOptions().setFrom(LUMAN.getAddress());
                kip7contract.pause();
                assertTrue(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void pausedNoGas() {
            try {
                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                kip7contract.pause(sendOptions);
                assertTrue(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void unPause() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip7contract.pause(options);
                assertTrue(kip7contract.paused());

                kip7contract.unpause(options);
                assertFalse(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addPauser() {
            try {
                SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                kip7contract.addPauser(BRANDON.getAddress(), options);

                assertTrue(kip7contract.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renouncePauser() {
            try {
                if(!kip7contract.isPauser(BRANDON.getAddress())) {
                    SendOptions options = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(4000000));
                    kip7contract.addPauser(BRANDON.getAddress(), options);
                }

                SendOptions options = new SendOptions(BRANDON.getAddress(), BigInteger.valueOf(4000000));
                kip7contract.renouncePauser(options);

                assertFalse(kip7contract.isPauser(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void paused() {
            try {
                assertFalse(kip7contract.paused());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }


    public static class BurnableTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            KIP7Test.deployContract();
        }

        @Test
        public void burn() {
            try {
                String totalSupply = kip7contract.totalSupply();

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String burnAmount = Utils.convertToPeb("100", "KLAY");
                kip7contract.burn(new BigInteger(burnAmount), sendOptions);

                String afterSupply = kip7contract.totalSupply();
                assertEquals(Numeric.toBigInt(afterSupply), Numeric.toBigInt(totalSupply).subtract(new BigInteger(burnAmount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void burnFrom() {
            try {
                String beforeBalance = kip7contract.balanceOf(LUMAN.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String burnAmount = Utils.convertToPeb("100", "KLAY");

                kip7contract.approve(BRANDON.getAddress(), Numeric.toBigInt(burnAmount), sendOptions);

                kip7contract.burnFrom(LUMAN.getAddress(), new BigInteger(burnAmount), new SendOptions(BRANDON.getAddress(), (String)null));
                String afterBalance = kip7contract.balanceOf(LUMAN.getAddress());

                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(beforeBalance).subtract(new BigInteger(burnAmount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class MintableTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            KIP7Test.deployContract();
        }

        @Test
        public void mint() {
            try {
                String beforeTotalSupply = kip7contract.totalSupply();

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String mintAmount = Utils.convertToPeb("100", "KLAY");

                kip7contract.mint(LUMAN.getAddress(), new BigInteger(mintAmount), sendOptions);

                String afterTotalSupply = kip7contract.totalSupply();
                assertEquals(Numeric.toBigInt(afterTotalSupply), Numeric.toBigInt(beforeTotalSupply).add(new BigInteger(mintAmount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void isMinter() {
            try {
                assertTrue(kip7contract.isMinter(LUMAN.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void addMinter() {
            try {
                if(kip7contract.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(BRANDON.getAddress(), (String)null);
                    kip7contract.renounceMinter(sendOptions);
                }

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);

                kip7contract.addMinter(BRANDON.getAddress(), sendOptions);

                assertTrue(kip7contract.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void renounceMinter() {
            try {
                if(!kip7contract.isMinter(BRANDON.getAddress())) {
                    SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                    kip7contract.addMinter(BRANDON.getAddress(), sendOptions);
                }

                SendOptions sendOptions = new SendOptions(BRANDON.getAddress(), (String)null);
                kip7contract.renounceMinter(sendOptions);

                assertFalse(kip7contract.isMinter(BRANDON.getAddress()));
            } catch (Exception e) {

            }
        }
    }

    public static class commonTest {
        @BeforeClass
        public static void init() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            KIP7Test.deployContract();
        }

        @Test
        public void balanceOf() {
            try {
                assertNotNull(kip7contract.balanceOf(LUMAN.getAddress()));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void allowance() {
            try {
                String allowance = kip7contract.allowance(LUMAN.getAddress(), WAYNE.getAddress());
                assertEquals(Numeric.toBigInt(allowance), BigInteger.ZERO);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void approve() {
            try {
                String allowance = kip7contract.allowance(LUMAN.getAddress(), BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger approveAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.approve(BRANDON.getAddress(), approveAmount, sendOptions);

                String afterAllowance = kip7contract.allowance(LUMAN.getAddress(), BRANDON.getAddress());
                assertEquals(Numeric.toBigInt(afterAllowance), Numeric.toBigInt(allowance).add(approveAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void transfer() {
            try {
                String beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.transfer(BRANDON.getAddress(), transferAmount, sendOptions);

                String afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(beforeBalance).add(transferAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void transferFrom() {
            SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions allowanceOptions = new SendOptions(BRANDON.getAddress(), (String)null);
            BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18
            try{
                //reset
                kip7contract.approve(BRANDON.getAddress(), BigInteger.ZERO, ownerOptions);

                //Test
                kip7contract.approve(BRANDON.getAddress(), allowAmount, ownerOptions);
                String preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.transferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, allowanceOptions);
                String afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(preBalance).add(allowAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransfer() {
            SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS));

            try {
                String beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.safeTransfer(BRANDON.getAddress(), transferAmount, sendOptions);

                String afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(beforeBalance).add(transferAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferWithData() {
            SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            BigInteger amount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS));
            String data = "buffered data";
            try {
                String beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.safeTransfer(BRANDON.getAddress(), transferAmount, data, sendOptions);

                String afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(beforeBalance).add(transferAmount));

            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFrom() {
            SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions allowanceOptions = new SendOptions(BRANDON.getAddress(), (String)null);
            BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18
            try{
                //reset
                kip7contract.approve(BRANDON.getAddress(), BigInteger.ZERO, ownerOptions);

                //Test
                kip7contract.approve(BRANDON.getAddress(), allowAmount, ownerOptions);
                String preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.safeTransferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, allowanceOptions);
                String afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(preBalance).add(allowAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void safeTransferFromWithData() {
            SendOptions ownerOptions = new SendOptions(LUMAN.getAddress(), (String)null);
            SendOptions allowanceOptions = new SendOptions(BRANDON.getAddress(), (String)null);
            BigInteger allowAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18
            String data = "buffered data";
            try{
                //reset
                kip7contract.approve(BRANDON.getAddress(), BigInteger.ZERO, ownerOptions);

                //Test
                kip7contract.approve(BRANDON.getAddress(), allowAmount, ownerOptions);
                String preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.safeTransferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, data, allowanceOptions);
                String afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(Numeric.toBigInt(afterBalance), Numeric.toBigInt(preBalance).add(allowAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

}
