package com.klaytn.caver.common;

import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KeyringContainerTest {

    static void validateSingleKeyring(AbstractKeyring actual, String expectAddress, String expectKey) {
        assertTrue(actual instanceof SingleKeyring);
        SingleKeyring keyring = (SingleKeyring)actual;
        assertEquals(expectAddress, actual.getAddress());
        assertEquals(expectKey, keyring.getKey().getPrivateKey());
    }

    static void validateMultipleKeyring(AbstractKeyring actual, String expectAddress, String[] expectKeyArr) {
        assertTrue(actual instanceof MultipleKeyring);
        MultipleKeyring keyring = (MultipleKeyring)actual;

        PrivateKey[] actualKeyArr = keyring.getKeys();

        assertEquals(expectAddress, actual.getAddress());
        for(int i=0; i<expectKeyArr.length; i++) {
            assertEquals(expectKeyArr[i], actualKeyArr[i].getPrivateKey());
        }
    }

    static void validateRoleBasedKeyring(AbstractKeyring actual, String expectAddress, String[][] expectKeyArr) {
        assertTrue(actual instanceof RoleBasedKeyring);
        RoleBasedKeyring keyring = (RoleBasedKeyring)actual;

        List<PrivateKey[]> actualKeyArr = keyring.getKeys();

        assertEquals(expectAddress, actual.getAddress());

        for(int i=0; i<expectKeyArr.length; i++) {
            PrivateKey[] privateKeys = actualKeyArr.get(i);
            for (int j=0; j<expectKeyArr[i].length; j++) {
                assertEquals(expectKeyArr[i][j], privateKeys[j].getPrivateKey());
            }
        }
    }

    static ValueTransfer generateValueTransfer(AbstractKeyring keyring) {
        return new ValueTransfer.Builder()
                .setFrom(keyring.getAddress())
                .setTo(keyring.getAddress())
                .setValue("0x1")
                .setChainId("0x7e3")
                .setNonce("0x0")
                .setGas("0x15f90")
                .setGasPrice("0x5d21dba00")
                .build();
    }

    static FeeDelegatedValueTransfer generateFeeDelegatedValueTransfer(AbstractKeyring keyring) {
        return new FeeDelegatedValueTransfer.Builder()
                .setFrom(keyring.getAddress())
                .setTo(keyring.getAddress())
                .setValue("0x1")
                .setChainId("0x7e3")
                .setNonce("0x0")
                .setGas("0x15f90")
                .setGasPrice("0x5d21dba00")
                .build();
    }

    static AccountUpdate generateAccountUpdate(RoleBasedKeyring keyring) {
        return new AccountUpdate.Builder()
                .setFrom(keyring.getAddress())
                .setAccount(keyring.toAccount())
                .setChainId("0x7E3")
                .setNonce("0x0")
                .setGas("0x15f90")
                .setGasPrice("0x5d21dba00")
                .build();
    }



    public static class generateTest {
        public void checkAddress(List<String> expectedAddress, KeyringContainer container) {
            expectedAddress.stream().forEach(address -> {
                AbstractKeyring keyring = container.getKeyring(address);
                assertNotNull(keyring);
                assertEquals(address, keyring.getAddress());
            });
        }

        //CA-KEYRINGCONTAINER-001
        @Test
        public void validKeyringCountNoEntropy() {
            KeyringContainer container = new KeyringContainer();
            List<String> expectAddressList = container.generate(10);

            checkAddress(expectAddressList, container);
        }

        //CA-KEYRINGCONTAINER-002
        @Test
        public void validKeyringCountWithEntropy() {
            KeyringContainer container = new KeyringContainer();
            List<String> expectAddressList = container.generate(10, "entropy");

            checkAddress(expectAddressList, container);
        }
    }

    public static class newKeyringTest {

        //CA-KEYRINGCONTAINER-003
        @Test
        public void newSingleKeyring() {
            KeyringContainer container = new KeyringContainer();
            PrivateKey privateKey = PrivateKey.generate();
            String expectAddress = privateKey.getDerivedAddress();
            String expectPrivateKey = privateKey.getPrivateKey();

            AbstractKeyring added = container.newKeyring(expectAddress, expectPrivateKey);
            validateSingleKeyring(added, expectAddress, expectPrivateKey);
            assertEquals(1, container.length());
        }

        //CA-KEYRINGCONTAINER-004
        @Test
        public void newMultipleKeyring() {
            KeyringContainer container = new KeyringContainer();
            String expectAddress = PrivateKey.generate().getDerivedAddress();
            String[] expectPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            AbstractKeyring added = container.newKeyring(expectAddress, expectPrivateKeyArr);
            validateMultipleKeyring(added, expectAddress, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-005
        @Test
        public void newRoleBasedKeyring() {
            KeyringContainer container = new KeyringContainer();
            String expectAddress = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    },
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    },
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                    }
            };

            AbstractKeyring added = container.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
            validateRoleBasedKeyring(added, expectAddress, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-006
        @Test
        public void newRoleBasedKeyringWithEmptyRole() {
            KeyringContainer container = new KeyringContainer();
            String expectAddress = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                    },
                    {
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            AbstractKeyring added = container.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
            validateRoleBasedKeyring(added, expectAddress, expectPrivateKeyArr);
        }
    }

    public static class updateKeyringTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRINGCONTAINER-007
        @Test
        public void updateToCoupledKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring coupled = KeyringFactory.generate();

            String address = coupled.getAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring decoupled = KeyringFactory.createWithSingleKey(coupled.getAddress(), privateKey);

            container.add(decoupled);

            AbstractKeyring updated = container.updateKeyring(coupled);
            AbstractKeyring fromContainer = container.getKeyring(address);

            validateSingleKeyring(updated, address, coupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), coupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-008
        @Test
        public void updateToDecoupledKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring coupled = KeyringFactory.generate();
            SingleKeyring deCoupled = KeyringFactory.createWithSingleKey(coupled.getAddress(), PrivateKey.generate().getPrivateKey());

            container.add(coupled);

            AbstractKeyring updated = container.updateKeyring(deCoupled);
            AbstractKeyring fromContainer = container.getKeyring(coupled.getAddress());

            validateSingleKeyring(updated,  coupled.getAddress(), deCoupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), deCoupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-009
        @Test
        public void updateToMultipleKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring origin = KeyringFactory.generate();

            String[] expectPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(origin.getAddress(), expectPrivateKeyArr);

            container.add(origin);

            AbstractKeyring updated = container.updateKeyring(multipleKeyring);
            AbstractKeyring fromContainer = container.getKeyring(origin.getAddress());

            validateMultipleKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateMultipleKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-010
        @Test
        public void updateToRoleBasedKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring origin = KeyringFactory.generate();

            String[][] expectPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(origin.getAddress(), Arrays.asList(expectPrivateKeyArr));

            container.add(origin);

            AbstractKeyring updated = container.updateKeyring(roleBasedKeyring);
            AbstractKeyring fromContainer = container.getKeyring(origin.getAddress());

            validateRoleBasedKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-011
        @Test
        public void throwException_NotExistedKeyring() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Failed to find keyring to update.");

            SingleKeyring keyring = KeyringFactory.generate();
            KeyringContainer container = new KeyringContainer();

            container.updateKeyring(keyring);
        }
    }

    public static class getKeyringTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRINGCONTAINER-012
        @Test
        public void withValidAddress() {
            KeyringContainer container = new KeyringContainer();

            SingleKeyring added = (SingleKeyring)container.add(KeyringFactory.generate());
            AbstractKeyring keyring = container.getKeyring(added.getAddress());

            validateSingleKeyring(keyring, added.getAddress(), added.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-013
        @Test
        public void notExistsAddress() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring keyring = KeyringFactory.generate();

            AbstractKeyring actual = container.getKeyring(keyring.getAddress());
            assertNull(actual);
        }

        //CA-KEYRINGCONTAINER-014
        @Test
        public void throwException_InvalidAddress() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address. To get keyring from wallet, you need to pass a valid address string as a parameter.");

            KeyringContainer container = new KeyringContainer();
            String invalidAddress = "invalid";

            container.getKeyring(invalidAddress);
        }

        @Test
        public void upperCaseAddress() {
            String address = "0x37223E5E41186A782e4A1F709829F521f43b18E5";
            SingleKeyring keyring = KeyringFactory.create(address, PrivateKey.generate().getPrivateKey());

            KeyringContainer container = new KeyringContainer();
            container.add(keyring);

            assertNotNull(container.getKeyring(address));
            assertEquals(address, container.getKeyring(address).getAddress());
        }
    }

    public static class addTest {

        //CA-KEYRINGCONTAINER-015
        @Test
        public void singleKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring keyringToAdd = KeyringFactory.generate();

            AbstractKeyring added = container.add(keyringToAdd);
            AbstractKeyring fromContainer = container.getKeyring(added.getAddress());

            validateSingleKeyring(added, keyringToAdd.getAddress(), keyringToAdd.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, keyringToAdd.getAddress(), keyringToAdd.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-016
        @Test
        public void deCoupledKeyring() {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring deCoupled = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());

            AbstractKeyring added = container.add(deCoupled);
            AbstractKeyring fromContainer = container.getKeyring(deCoupled.getAddress());

            validateSingleKeyring(added, deCoupled.getAddress(), deCoupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, deCoupled.getAddress(), deCoupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-017
        @Test
        public void multipleKeyring() {
            KeyringContainer container = new KeyringContainer();
            String address = PrivateKey.generate().getDerivedAddress();
            String[] expectPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(address, expectPrivateKeyArr);

            AbstractKeyring added = container.add(multipleKeyring);
            AbstractKeyring fromContainer = container.getKeyring(multipleKeyring.getAddress());

            validateMultipleKeyring(added, address, expectPrivateKeyArr);
            validateMultipleKeyring(fromContainer, address, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-018
        @Test
        public void roleBasedKeyring() {
            KeyringContainer container = new KeyringContainer();
            String address = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, Arrays.asList(expectPrivateKeyArr));

            AbstractKeyring added = container.add(roleBasedKeyring);
            AbstractKeyring fromContainer = container.getKeyring(added.getAddress());

            validateRoleBasedKeyring(added, address, expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, address, expectPrivateKeyArr);
        }

    }

    public static class removeTest {

        //CA-KEYRINGCONTAINER-019
        @Test
        public void coupledKey() {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyringToAdd = KeyringFactory.generate();

            AbstractKeyring added = container.add(keyringToAdd);
            boolean result = container.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, container.length());
            assertNull(container.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-020
        @Test
        public void deCoupledKey() {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyringToAdd = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());

            AbstractKeyring added = container.add(keyringToAdd);
            boolean result = container.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, container.length());
            assertNull(container.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-021
        @Test
        public void multipleKey() {
            KeyringContainer container = new KeyringContainer();
            String address = PrivateKey.generate().getDerivedAddress();
            String[] expectPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            AbstractKeyring keyringToAdd = KeyringFactory.createWithMultipleKey(address, expectPrivateKeyArr);

            AbstractKeyring added = container.add(keyringToAdd);
            boolean result = container.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, container.length());
            assertNull(container.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-022
        @Test
        public void roleBasedKey() {
            KeyringContainer container = new KeyringContainer();
            String address = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            AbstractKeyring keyringToAdd = KeyringFactory.createWithRoleBasedKey(address, Arrays.asList(expectPrivateKeyArr));

            AbstractKeyring added = container.add(keyringToAdd);
            boolean result = container.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, container.length());
            assertNull(container.getKeyring(keyringToAdd.getAddress()));
        }
    }

    public static class signMessageTest {

        //CA-KEYRINGCONTAINER-023
        @Test
        public void signMessage() {
            KeyringContainer container = new KeyringContainer();
            String message = "message";

            String address = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            AbstractKeyring roleBased = container.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 0, 0);
            MessageSigned actualData = container.signMessage(address, message);

            assertEquals(message, actualData.getMessage());
            assertEquals(Utils.hashMessage(message), actualData.getMessageHash());
            assertNotNull(actualData.getSignatures());

            assertEquals(expectedData.getSignatures().get(0).getR(), actualData.getSignatures().get(0).getR());
            assertEquals(expectedData.getSignatures().get(0).getS(), actualData.getSignatures().get(0).getS());
            assertEquals(expectedData.getSignatures().get(0).getV(), actualData.getSignatures().get(0).getV());
        }

        //CA-KEYRINGCONTAINER-024
        @Test
        public void signMessageWithIndex() {
            KeyringContainer container = new KeyringContainer();
            String message = "message";

            String address = PrivateKey.generate().getDerivedAddress();
            String[][] expectPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            AbstractKeyring roleBased = container.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 1, 0);
            MessageSigned actualData = container.signMessage(address, message, 1, 0);

            assertEquals(message, actualData.getMessage());
            assertEquals(Utils.hashMessage(message), actualData.getMessageHash());
            assertNotNull(actualData.getSignatures());

            assertEquals(expectedData.getSignatures().get(0).getR(), actualData.getSignatures().get(0).getR());
            assertEquals(expectedData.getSignatures().get(0).getS(), actualData.getSignatures().get(0).getS());
            assertEquals(expectedData.getSignatures().get(0).getV(), actualData.getSignatures().get(0).getV());
        }
    }

    public static class signTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void sign_withIndex_singleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring singleKeyring = KeyringFactory.generate();

            container.add(singleKeyring);
            ValueTransfer valueTransfer = generateValueTransfer(singleKeyring);

            container.sign(singleKeyring.getAddress(), valueTransfer, 0);
            SignatureData expectedSig = singleKeyring.sign(TransactionHasher.getHashForSignature(valueTransfer), valueTransfer.getChainId(), 0, 0);

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_multipleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            String[] multipleKey = KeyringFactory.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(address, multipleKey);

            ValueTransfer valueTransfer = generateValueTransfer(multipleKeyring);

            AbstractKeyring keyring = container.add(multipleKeyring);
            container.sign(multipleKeyring.getAddress(), valueTransfer, 1);

            SignatureData expectedSig = keyring.sign(TransactionHasher.getHashForSignature(valueTransfer), valueTransfer.getChainId(), 0, 1);

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_roleBasedKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            ValueTransfer valueTransfer = generateValueTransfer(roleBasedKeyring);

            AbstractKeyring keyring = container.add(roleBasedKeyring);
            container.sign(keyring.getAddress(), valueTransfer, 2);

            SignatureData expectedSig = keyring.sign(TransactionHasher.getHashForSignature(valueTransfer), valueTransfer.getChainId(), 0, 2);

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_accountUpdate() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            AccountUpdate accountUpdate = generateAccountUpdate(roleBasedKeyring);
            AbstractKeyring keyring = container.add(roleBasedKeyring);
            container.sign(keyring.getAddress(), accountUpdate, 2);

            SignatureData expectedSig = roleBasedKeyring.sign(TransactionHasher.getHashForSignature(accountUpdate), accountUpdate.getChainId(), 1, 2);

            assertEquals(1, accountUpdate.getSignatures().size());
            assertEquals(expectedSig, accountUpdate.getSignatures().get(0));
        }

        @Test
        public void throwException_withIndex_notExistedKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            KeyringContainer container = new KeyringContainer();

            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(KeyringFactory.generate());
            container.sign(address, valueTransfer, 0);
        }

        @Test
        public void sign_singleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(KeyringFactory.generate());

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            container.sign(keyring.getAddress(), valueTransfer);

            assertEquals(1, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_multipleKeyring() throws IOException {
            String address = PrivateKey.generate().getDerivedAddress();
            String[] multipleKey = KeyringFactory.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(address, multipleKey);

            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(multipleKeyring);


            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            container.sign(keyring.getAddress(), valueTransfer);

            assertEquals(3, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_roleBasedKeyring() throws IOException {
            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(roleBasedKeyring);

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            container.sign(keyring.getAddress(), valueTransfer);

            assertEquals(4, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_customHasher() throws IOException {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(KeyringFactory.generate());

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            container.sign(keyring.getAddress(), valueTransfer, (AbstractTransaction tx) -> {
                return "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1";
            });

            List<SignatureData> expected = keyring.sign("0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1", valueTransfer.getChainId(), AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(expected.get(0), valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_AccountUpdateTx() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            container.add(roleBasedKeyring);
            AccountUpdate tx = generateAccountUpdate(roleBasedKeyring);

            container.sign(address, tx);
            assertEquals(5, tx.getSignatures().size());
        }

        @Test
        public void throwException_notExistKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            KeyringContainer container = new KeyringContainer();
            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(KeyringFactory.generate());
            container.sign(address, valueTransfer);
        }
    }

    public static class signAsFeePayerTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void signAsFeePayer_withIndex_singleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();
            SingleKeyring singleKeyring = KeyringFactory.generate();

            container.add(singleKeyring);
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(singleKeyring);

            container.signAsFeePayer(singleKeyring.getAddress(), feeDelegatedValueTransfer, 0);
            SignatureData expectedSig = singleKeyring.sign(TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer), feeDelegatedValueTransfer.getChainId(), 2, 0);

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void signAsFeePayer_withIndex_multipleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            String[] multipleKey = KeyringFactory.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(address, multipleKey);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(multipleKeyring);

            AbstractKeyring keyring = container.add(multipleKeyring);
            container.signAsFeePayer(keyring.getAddress(), feeDelegatedValueTransfer, 1);

            SignatureData expectedSig = keyring.sign(TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer), feeDelegatedValueTransfer.getChainId(), 2, 1);

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void signAsFeePayer_withIndex_roleBasedKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();

            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(roleBasedKeyring);

            AbstractKeyring keyring = container.add(roleBasedKeyring);
            container.signAsFeePayer(keyring.getAddress(), feeDelegatedValueTransfer, 1);

            SignatureData expectedSig = keyring.sign(TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer), feeDelegatedValueTransfer.getChainId(), 2, 1);

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void throwException_withIndex_notExistedKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            KeyringContainer container = new KeyringContainer();

            String address = "0x1234567890123456789012345678901234567890";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(KeyringFactory.generate());
            container.signAsFeePayer(address, feeDelegatedValueTransfer, 0);
        }

        @Test
        public void signAsFeePayer_singleKeyring() throws IOException {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(KeyringFactory.generate());

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            container.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(1, valueTransfer.getSignatures().size());
        }

        @Test
        public void signAsFeePayer_multipleKeyring() throws IOException {
            String address = PrivateKey.generate().getDerivedAddress();
            String[] multipleKey = KeyringFactory.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = KeyringFactory.createWithMultipleKey(address, multipleKey);

            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(multipleKeyring);

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            container.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(3, valueTransfer.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_roleBasedKeyring() throws IOException {
            String address = PrivateKey.generate().getDerivedAddress();
            List<String[]> roleBasedKey = KeyringFactory.generateRoleBasedKeys(new int[]{4,5,6}, "entropy");
            RoleBasedKeyring roleBasedKeyring = KeyringFactory.createWithRoleBasedKey(address, roleBasedKey);

            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(roleBasedKeyring);

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            container.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(6, valueTransfer.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_customHasher() throws IOException {
            KeyringContainer container = new KeyringContainer();
            AbstractKeyring keyring = container.add(KeyringFactory.generate());

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            container.signAsFeePayer(keyring.getAddress(), valueTransfer, (AbstractFeeDelegatedTransaction tx) -> {
                return "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1";
            });

            List<SignatureData> expected = keyring.sign("0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1", valueTransfer.getChainId(), AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex());
            assertEquals(expected.get(0), valueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void throwException_notExistKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            KeyringContainer container = new KeyringContainer();
            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(KeyringFactory.generate());
            container.sign(address, valueTransfer);
        }

    }
}
