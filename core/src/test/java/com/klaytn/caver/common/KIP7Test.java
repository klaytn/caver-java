package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.kct.kip7.KIP7DeployParams;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Map;

import static com.klaytn.caver.base.Accounts.*;
import static org.junit.Assert.*;

public class KIP7Test {

    public static KIP7 kip7contract;
    public static final String CONTRACT_NAME = "Kale";
    public static final String CONTRACT_SYMBOL = "KALE";
    public static final int CONTRACT_DECIMALS = 18;
    public static final BigInteger CONTRACT_INITIAL_SUPPLY = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 100000 * 10^18

    public static void deployContract() throws Exception {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        KIP7DeployParams kip7DeployParam = new KIP7DeployParams(CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);
        SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(45000));
        kip7contract = KIP7.deploy(caver, kip7DeployParam, LUMAN.getAddress());
    }

    public static class ConstructorTest {
        @BeforeClass
        public static void init() throws Exception {
            KIP7Test.deployContract();
        }

        @Test
        public void deploy() throws Exception {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            KIP7 contract = KIP7.deploy(caver, LUMAN.getAddress(), CONTRACT_NAME, CONTRACT_SYMBOL, CONTRACT_DECIMALS, CONTRACT_INITIAL_SUPPLY);

            assertNotNull(contract.getContractAddress());
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
                int decimals = kip7contract.decimals();
                assertEquals(CONTRACT_DECIMALS, decimals);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        }

        @Test
        public void totalSupply(){
            try {
                BigInteger totalSupply = kip7contract.totalSupply();
                assertEquals(CONTRACT_INITIAL_SUPPLY, totalSupply);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void cloneTestWithSetWallet() throws IOException {
            Caver caver = new Caver(Caver.DEFAULT_URL);
            KIP7 kip7 = new KIP7(caver);

            KeyringContainer container = new KeyringContainer();
            container.generate(3);

            kip7.setWallet(container);
            KIP7 cloned = kip7.clone();

            assertEquals(3, ((KeyringContainer)cloned.getWallet()).length());
        }
    }

    public static class PausableTest {
        @BeforeClass
        public static void init() throws Exception {
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
        public static void init() throws Exception {
            KIP7Test.deployContract();
        }

        @Test
        public void burn() {
            try {
                BigInteger totalSupply = kip7contract.totalSupply();

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String burnAmount = Utils.convertToPeb("100", "KLAY");
                kip7contract.burn(new BigInteger(burnAmount), sendOptions);

                BigInteger afterSupply = kip7contract.totalSupply();
                assertEquals(afterSupply, totalSupply.subtract(new BigInteger(burnAmount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void burnFrom() {
            try {
                BigInteger beforeBalance = kip7contract.balanceOf(LUMAN.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String burnAmount = Utils.convertToPeb("100", "KLAY");

                kip7contract.approve(BRANDON.getAddress(), Numeric.toBigInt(burnAmount), sendOptions);

                kip7contract.burnFrom(LUMAN.getAddress(), new BigInteger(burnAmount), new SendOptions(BRANDON.getAddress(), (String)null));
                BigInteger afterBalance = kip7contract.balanceOf(LUMAN.getAddress());

                assertEquals(afterBalance, beforeBalance.subtract(new BigInteger(burnAmount)));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class MintableTest {
        @BeforeClass
        public static void init() throws Exception {
            KIP7Test.deployContract();
        }

        @Test
        public void mint() {
            try {
                BigInteger beforeTotalSupply = kip7contract.totalSupply();

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                String mintAmount = Utils.convertToPeb("100", "KLAY");

                kip7contract.mint(LUMAN.getAddress(), new BigInteger(mintAmount), sendOptions);

                BigInteger afterTotalSupply = kip7contract.totalSupply();
                assertEquals(afterTotalSupply, beforeTotalSupply.add(new BigInteger(mintAmount)));
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
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class CommonTest {
        @BeforeClass
        public static void init() throws Exception {
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
                BigInteger allowance = kip7contract.allowance(LUMAN.getAddress(), WAYNE.getAddress());
                assertEquals(allowance, BigInteger.ZERO);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void approve() {
            try {
                BigInteger allowance = kip7contract.allowance(LUMAN.getAddress(), BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger approveAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.approve(BRANDON.getAddress(), approveAmount, sendOptions);

                BigInteger afterAllowance = kip7contract.allowance(LUMAN.getAddress(), BRANDON.getAddress());
                assertEquals(afterAllowance, allowance.add(approveAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        public void transfer() {
            try {
                BigInteger beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.transfer(BRANDON.getAddress(), transferAmount, sendOptions);

                BigInteger afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(afterBalance, beforeBalance.add(transferAmount));
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
                BigInteger preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.transferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, allowanceOptions);
                BigInteger afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(afterBalance, preBalance.add(allowAmount));
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
                BigInteger beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.safeTransfer(BRANDON.getAddress(), transferAmount, sendOptions);

                BigInteger afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(afterBalance, beforeBalance.add(transferAmount));
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
                BigInteger beforeBalance = kip7contract.balanceOf(BRANDON.getAddress());

                SendOptions sendOptions = new SendOptions(LUMAN.getAddress(), (String)null);
                BigInteger transferAmount = BigInteger.TEN.multiply(BigInteger.TEN.pow(CONTRACT_DECIMALS)); // 10 * 10^18

                kip7contract.safeTransfer(BRANDON.getAddress(), transferAmount, data, sendOptions);

                BigInteger afterBalance = kip7contract.balanceOf(BRANDON.getAddress());
                assertEquals(afterBalance, beforeBalance.add(transferAmount));

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
                BigInteger preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.safeTransferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, allowanceOptions);
                BigInteger afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(afterBalance, preBalance.add(allowAmount));
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
                BigInteger preBalance = kip7contract.balanceOf(WAYNE.getAddress());

                kip7contract.safeTransferFrom(LUMAN.getAddress(), WAYNE.getAddress(), allowAmount, data, allowanceOptions);
                BigInteger afterBalance = kip7contract.balanceOf(WAYNE.getAddress());

                assertEquals(afterBalance, preBalance.add(allowAmount));
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

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
                boolean isSupported_KIP13 = kip7contract.supportInterface(INTERFACE_ID_KIP13);
                assertTrue(isSupported_KIP13);

                boolean isSupported_KIP7_PAUSABLE = kip7contract.supportInterface(INTERFACE_ID_KIP7_PAUSABLE);
                assertTrue(isSupported_KIP7_PAUSABLE);

                boolean isSupported_KIP7_BURNABLE = kip7contract.supportInterface(INTERFACE_ID_KIP7_BURNABLE);
                assertTrue(isSupported_KIP7_BURNABLE);

                boolean isSupported_KIP7_MINTABLE = kip7contract.supportInterface(INTERFACE_ID_KIP7_MINTABLE);
                assertTrue(isSupported_KIP7_MINTABLE);

                boolean isSupported_KIP7_METADATA = kip7contract.supportInterface(INTERFACE_ID_KIP7_METADATA);
                assertTrue(isSupported_KIP7_METADATA);

                boolean isSupported_KIP7 = kip7contract.supportInterface(INTERFACE_ID_KIP7);
                assertTrue(isSupported_KIP7);

                boolean isSupported_FALSE = kip7contract.supportInterface(INTERFACE_ID_FALSE);
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

        static final String byteCodeWithMintable = "0x60806040523480156200001157600080fd5b5060405160208062001fea833981018060405260208110156200003357600080fd5b81019080805190602001909291905050506200005c6301ffc9a760e01b620000b660201b60201c565b62000074636578737160e01b620000b660201b60201c565b6200008533620001bf60201b60201c565b6200009d63eab83e2060e01b620000b660201b60201c565b620000af33826200022060201b60201c565b5062000639565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916141562000153576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b620001da816004620003ec60201b6200167d1790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415620002c4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b620002e081600354620004d060201b620015f51790919060201c565b6003819055506200033f81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054620004d060201b620015f51790919060201c565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b620003fe82826200055960201b60201c565b1562000472576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b6000808284019050838110156200054f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415620005e2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602281526020018062001fc86022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b61197f80620006496000396000f3fe608060405234801561001057600080fd5b50600436106100f55760003560e01c806370a0823111610097578063aa271e1a11610066578063aa271e1a14610497578063b88d4fde146104f3578063dd62ed3e146105f8578063eb79554914610670576100f5565b806370a082311461038b578063983b2d56146103e35780639865027514610427578063a9059cbb14610431576100f5565b806323b872dd116100d357806323b872dd146101e357806340c10f1914610269578063423f6cef146102cf57806342842e0e1461031d576100f5565b806301ffc9a7146100fa578063095ea7b31461015f57806318160ddd146101c5575b600080fd5b6101456004803603602081101561011057600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610755565b604051808215151515815260200191505060405180910390f35b6101ab6004803603604081101561017557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506107bc565b604051808215151515815260200191505060405180910390f35b6101cd6107d3565b6040518082815260200191505060405180910390f35b61024f600480360360608110156101f957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506107dd565b604051808215151515815260200191505060405180910390f35b6102b56004803603604081101561027f57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061088e565b604051808215151515815260200191505060405180910390f35b61031b600480360360408110156102e557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610902565b005b6103896004803603606081101561033357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610920565b005b6103cd600480360360208110156103a157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610940565b6040518082815260200191505060405180910390f35b610425600480360360208110156103f957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610989565b005b61042f6109f3565b005b61047d6004803603604081101561044757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506109fe565b604051808215151515815260200191505060405180910390f35b6104d9600480360360208110156104ad57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a15565b604051808215151515815260200191505060405180910390f35b6105f66004803603608081101561050957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561057057600080fd5b82018360208201111561058257600080fd5b803590602001918460018302840111640100000000831117156105a457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610a32565b005b61065a6004803603604081101561060e57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610aa5565b6040518082815260200191505060405180910390f35b6107536004803603606081101561068657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156106cd57600080fd5b8201836020820111156106df57600080fd5b8035906020019184600183028401116401000000008311171561070157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610b2c565b005b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b60006107c9338484610b9d565b6001905092915050565b6000600354905090565b60006107ea848484610d94565b610883843361087e85600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461103490919063ffffffff16565b610b9d565b600190509392505050565b600061089933610a15565b6108ee576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252603081526020018061189a6030913960400191505060405180910390fd5b6108f883836110bd565b6001905092915050565b61091c828260405180602001604052806000815250610b2c565b5050565b61093b83838360405180602001604052806000815250610a32565b505050565b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b61099233610a15565b6109e7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252603081526020018061189a6030913960400191505060405180910390fd5b6109f08161127a565b50565b6109fc336112d4565b565b6000610a0b338484610d94565b6001905092915050565b6000610a2b82600461132e90919063ffffffff16565b9050919050565b610a3d8484846107dd565b50610a4a8484848461140c565b610a9f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e81526020018061184a602e913960400191505060405180910390fd5b50505050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b610b3683836109fe565b50610b433384848461140c565b610b98576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e81526020018061184a602e913960400191505060405180910390fd5b505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610c23576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001806119316023913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610ca9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001806118296021913960400191505060405180910390fd5b80600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925836040518082815260200191505060405180910390a3505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610e1a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806118eb6024913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610ea0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806118786022913960400191505060405180910390fd5b610ef281600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461103490919063ffffffff16565b600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550610f8781600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546115f590919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050565b6000828211156110ac576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611160576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b611175816003546115f590919063ffffffff16565b6003819055506111cd81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546115f590919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b61128e81600461167d90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b6112e881600461175890919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156113b5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602281526020018061190f6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600061142d8473ffffffffffffffffffffffffffffffffffffffff16611815565b61143a57600190506115ed565b60008473ffffffffffffffffffffffffffffffffffffffff16639d188c22338887876040518563ffffffff1660e01b8152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156115155780820151818401526020810190506114fa565b50505050905090810190601f1680156115425780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b15801561156457600080fd5b505af1158015611578573d6000803e3d6000fd5b505050506040513d602081101561158e57600080fd5b81019080805190602001909291905050509050639d188c2260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149150505b949350505050565b600080828401905083811015611673576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b611687828261132e565b156116fa576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b611762828261132e565b6117b7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260218152602001806118ca6021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b600080823b90506000811191505091905056fe4b4950373a20617070726f766520746f20746865207a65726f20616464726573734b4950373a207472616e7366657220746f206e6f6e204b495037526563656976657220696d706c656d656e7465724b4950373a207472616e7366657220746f20746865207a65726f20616464726573734d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b4950373a207472616e736665722066726f6d20746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b4950373a20617070726f76652066726f6d20746865207a65726f2061646472657373a165627a7a723058208a7b2477a3befbd83e78ac12086b96a9c9d0acccbc5ed2544c3fa5c6ee326c3d0029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
        static final String byteCodeWithoutBurnablePausable = "0x60806040523480156200001157600080fd5b506040516200245f3803806200245f833981018060405260808110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b828101905060208101848111156200006757600080fd5b81518560018202830111640100000000821117156200008557600080fd5b50509291906020018051640100000000811115620000a257600080fd5b82810190506020810184811115620000b957600080fd5b8151856001820283011164010000000082111715620000d757600080fd5b50509291906020018051906020019092919080519060200190929190505050838383620001116301ffc9a760e01b620001d660201b60201c565b62000129636578737160e01b620001d660201b60201c565b6200013a33620002df60201b60201c565b6200015263eab83e2060e01b620001d660201b60201c565b82600590805190602001906200016a92919062000759565b5081600690805190602001906200018392919062000759565b5080600760006101000a81548160ff021916908360ff160217905550620001b763a219a02560e01b620001d660201b60201c565b505050620001cc33826200034060201b60201c565b5050505062000808565b63ffffffff60e01b817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916141562000273576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4b495031333a20696e76616c696420696e74657266616365206964000000000081525060200191505060405180910390fd5b6001600080837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b620002fa8160046200050c60201b620019231790919060201c565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415620003e4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b6200040081600354620005f060201b6200189b1790919060201c565b6003819055506200045f81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054620005f060201b6200189b1790919060201c565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b6200051e82826200067960201b60201c565b1562000592576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b6000808284019050838110156200066f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141562000702576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806200243d6022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200079c57805160ff1916838001178555620007cd565b82800160010185558215620007cd579182015b82811115620007cc578251825591602001919060010190620007af565b5b509050620007dc9190620007e0565b5090565b6200080591905b8082111562000801576000816000905550600101620007e7565b5090565b90565b611c2580620008186000396000f3fe608060405234801561001057600080fd5b50600436106101165760003560e01c806370a08231116100a2578063a9059cbb11610071578063a9059cbb1461057c578063aa271e1a146105e2578063b88d4fde1461063e578063dd62ed3e14610743578063eb795549146107bb57610116565b806370a082311461045357806395d89b41146104ab578063983b2d561461052e578063986502751461057257610116565b806323b872dd116100e957806323b872dd14610287578063313ce5671461030d57806340c10f1914610331578063423f6cef1461039757806342842e0e146103e557610116565b806301ffc9a71461011b57806306fdde0314610180578063095ea7b31461020357806318160ddd14610269575b600080fd5b6101666004803603602081101561013157600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191690602001909291905050506108a0565b604051808215151515815260200191505060405180910390f35b610188610907565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101c85780820151818401526020810190506101ad565b50505050905090810190601f1680156101f55780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61024f6004803603604081101561021957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506109a9565b604051808215151515815260200191505060405180910390f35b6102716109c0565b6040518082815260200191505060405180910390f35b6102f36004803603606081101561029d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506109ca565b604051808215151515815260200191505060405180910390f35b610315610a7b565b604051808260ff1660ff16815260200191505060405180910390f35b61037d6004803603604081101561034757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a92565b604051808215151515815260200191505060405180910390f35b6103e3600480360360408110156103ad57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b06565b005b610451600480360360608110156103fb57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b24565b005b6104956004803603602081101561046957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610b44565b6040518082815260200191505060405180910390f35b6104b3610b8d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104f35780820151818401526020810190506104d8565b50505050905090810190601f1680156105205780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6105706004803603602081101561054457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c2f565b005b61057a610c99565b005b6105c86004803603604081101561059257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ca4565b604051808215151515815260200191505060405180910390f35b610624600480360360208110156105f857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610cbb565b604051808215151515815260200191505060405180910390f35b6107416004803603608081101561065457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156106bb57600080fd5b8201836020820111156106cd57600080fd5b803590602001918460018302840111640100000000831117156106ef57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610cd8565b005b6107a56004803603604081101561075957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610d4b565b6040518082815260200191505060405180910390f35b61089e600480360360608110156107d157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561081857600080fd5b82018360208201111561082a57600080fd5b8035906020019184600183028401116401000000008311171561084c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610dd2565b005b6000806000837bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200190815260200160002060009054906101000a900460ff169050919050565b606060058054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561099f5780601f106109745761010080835404028352916020019161099f565b820191906000526020600020905b81548152906001019060200180831161098257829003601f168201915b5050505050905090565b60006109b6338484610e43565b6001905092915050565b6000600354905090565b60006109d784848461103a565b610a708433610a6b85600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546112da90919063ffffffff16565b610e43565b600190509392505050565b6000600760009054906101000a900460ff16905090565b6000610a9d33610cbb565b610af2576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180611b406030913960400191505060405180910390fd5b610afc8383611363565b6001905092915050565b610b20828260405180602001604052806000815250610dd2565b5050565b610b3f83838360405180602001604052806000815250610cd8565b505050565b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b606060068054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c255780601f10610bfa57610100808354040283529160200191610c25565b820191906000526020600020905b815481529060010190602001808311610c0857829003601f168201915b5050505050905090565b610c3833610cbb565b610c8d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180611b406030913960400191505060405180910390fd5b610c9681611520565b50565b610ca23361157a565b565b6000610cb133848461103a565b6001905092915050565b6000610cd18260046115d490919063ffffffff16565b9050919050565b610ce38484846109ca565b50610cf0848484846116b2565b610d45576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180611af0602e913960400191505060405180910390fd5b50505050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b610ddc8383610ca4565b50610de9338484846116b2565b610e3e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180611af0602e913960400191505060405180910390fd5b505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610ec9576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526023815260200180611bd76023913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610f4f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180611acf6021913960400191505060405180910390fd5b80600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925836040518082815260200191505060405180910390a3505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614156110c0576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180611b916024913960400191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611146576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180611b1e6022913960400191505060405180910390fd5b61119881600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546112da90919063ffffffff16565b600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061122d81600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461189b90919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050565b600082821115611352576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611406576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f4b4950373a206d696e7420746f20746865207a65726f2061646472657373000081525060200191505060405180910390fd5b61141b8160035461189b90919063ffffffff16565b60038190555061147381600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461189b90919063ffffffff16565b600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b61153481600461192390919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f660405160405180910390a250565b61158e8160046119fe90919063ffffffff16565b8073ffffffffffffffffffffffffffffffffffffffff167fe94479a9f7e1952cc78f2d6baab678adc1b772d936c6583def489e524cb6669260405160405180910390a250565b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561165b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526022815260200180611bb56022913960400191505060405180910390fd5b8260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b60006116d38473ffffffffffffffffffffffffffffffffffffffff16611abb565b6116e05760019050611893565b60008473ffffffffffffffffffffffffffffffffffffffff16639d188c22338887876040518563ffffffff1660e01b8152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156117bb5780820151818401526020810190506117a0565b50505050905090810190601f1680156117e85780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b15801561180a57600080fd5b505af115801561181e573d6000803e3d6000fd5b505050506040513d602081101561183457600080fd5b81019080805190602001909291905050509050639d188c2260e01b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149150505b949350505050565b600080828401905083811015611919576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b61192d82826115d4565b156119a0576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f526f6c65733a206163636f756e7420616c72656164792068617320726f6c650081525060200191505060405180910390fd5b60018260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b611a0882826115d4565b611a5d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180611b706021913960400191505060405180910390fd5b60008260000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b600080823b90506000811191505091905056fe4b4950373a20617070726f766520746f20746865207a65726f20616464726573734b4950373a207472616e7366657220746f206e6f6e204b495037526563656976657220696d706c656d656e7465724b4950373a207472616e7366657220746f20746865207a65726f20616464726573734d696e746572526f6c653a2063616c6c657220646f6573206e6f74206861766520746865204d696e74657220726f6c65526f6c65733a206163636f756e7420646f6573206e6f74206861766520726f6c654b4950373a207472616e736665722066726f6d20746865207a65726f2061646472657373526f6c65733a206163636f756e7420697320746865207a65726f20616464726573734b4950373a20617070726f76652066726f6d20746865207a65726f2061646472657373a165627a7a7230582038f1cf7c9b15c61ab7c78564198e79d88eb545130b79cde885f96529521316950029526f6c65733a206163636f756e7420697320746865207a65726f2061646472657373";
        static final String byteCodeNotSupportedKIP13 = "0x608060405234801561001057600080fd5b5061051f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063693ec85e1461003b578063e942b5161461016f575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c1565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610134578082015181840152602081019050610119565b50505050905090810190601f1680156101615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102bf6004803603604081101561018557600080fd5b81019080803590602001906401000000008111156101a257600080fd5b8201836020820111156101b457600080fd5b803590602001918460018302840111640100000000831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561023957600080fd5b82018360208201111561024b57600080fd5b8035906020019184600183028401116401000000008311171561026d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103cc565b005b60606000826040518082805190602001908083835b602083106102f957805182526020820191506020810190506020830392506102d6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c05780601f10610395576101008083540402835291602001916103c0565b820191906000526020600020905b8154815290600101906020018083116103a357829003601f168201915b50505050509050919050565b806000836040518082805190602001908083835b6020831061040357805182526020820191506020810190506020830392506103e0565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051906020019061044992919061044e565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061048f57805160ff19168380011785556104bd565b828001600101855582156104bd579182015b828111156104bc5782518255916020019190600101906104a1565b5b5090506104ca91906104ce565b5090565b6104f091905b808211156104ec5760008160009055506001016104d4565b5090565b9056fea165627a7a723058203ffebc792829e0434ecc495da1b53d24399cd7fff506a4fd03589861843e14990029";
        static final String abi_mintable = "[\n" +
                "  {\n" +
                "    \"inputs\": [{ \"name\": \"initialSupply\", \"type\": \"uint256\" }],\n" +
                "    \"payable\": false,\n" +
                "    \"stateMutability\": \"nonpayable\",\n" +
                "    \"type\": \"constructor\"\n" +
                "  }\n" +
                "]";

        static final String abi_without_pausable_burnable = "[\n" +
                "  {\n" +
                "    \"inputs\":[\n" +
                "      {\n" +
                "        \"name\":\"name\",\n" +
                "        \"type\":\"string\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"symbol\",\n" +
                "        \"type\":\"string\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"decimals\",\n" +
                "        \"type\":\"uint8\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\":\"initialSupply\",\n" +
                "        \"type\":\"uint256\"\n" +
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
            caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
            KIP7Test.deployContract();
        }

        @Test
        public void detectInterface() {
            Map<String, Boolean> result = kip7contract.detectInterface();
            assertTrue(result.get(KIP7.INTERFACE.IKIP7.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_BURNABLE.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_METADATA.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_MINTABLE.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_PAUSABLE.getName()));
        }

        @Test
        public void detectInterface_staticMethod() {
            Map<String, Boolean> result = KIP7.detectInterface(kip7contract.getCaver(), kip7contract.getContractAddress());
            assertTrue(result.get(KIP7.INTERFACE.IKIP7.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_BURNABLE.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_METADATA.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_MINTABLE.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_PAUSABLE.getName()));
        }

        @Test
        public void only_mintable() throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            Contract contract = new Contract(caver, abi_mintable);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeWithMintable, BigInteger.valueOf(100000000000L));

            Map<String, Boolean> result = KIP7.detectInterface(caver, contract.getContractAddress());
            assertTrue(result.get(KIP7.INTERFACE.IKIP7.getName()));
            assertFalse(result.get(KIP7.INTERFACE.IKIP7_BURNABLE.getName()));
            assertFalse(result.get(KIP7.INTERFACE.IKIP7_METADATA.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_MINTABLE.getName()));
            assertFalse(result.get(KIP7.INTERFACE.IKIP7_PAUSABLE.getName()));
        }

        @Test
        public void withoutBurnable_Pausable() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            Contract contract = new Contract(caver, abi_without_pausable_burnable);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeWithoutBurnablePausable, "Test", "TST", 18, BigInteger.valueOf(100000000000L));

            Map<String, Boolean> result = KIP7.detectInterface(caver, contract.getContractAddress());
            assertTrue(result.get(KIP7.INTERFACE.IKIP7.getName()));
            assertFalse(result.get(KIP7.INTERFACE.IKIP7_BURNABLE.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_METADATA.getName()));
            assertTrue(result.get(KIP7.INTERFACE.IKIP7_MINTABLE.getName()));
            assertFalse(result.get(KIP7.INTERFACE.IKIP7_PAUSABLE.getName()));
        }

        @Test
        public void notSupportedKIP13() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This contract does not support KIP-13.");

            Contract contract = new Contract(caver, abi_not_supported_kip13);
            contract.deploy(new SendOptions(LUMAN.getAddress(), BigInteger.valueOf(10000000)), byteCodeNotSupportedKIP13);

            Map<String, Boolean> result = KIP7.detectInterface(caver, contract.getContractAddress());
        }
    }

}
