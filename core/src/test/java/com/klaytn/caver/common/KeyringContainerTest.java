package com.klaytn.caver.common;

import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        KeyringContainerTest.generateTest.class,
        KeyringContainerTest.newKeyringTest.class,
        KeyringContainerTest.updateKeyringTest.class,
        KeyringContainerTest.getKeyringTest.class,
        KeyringContainerTest.addTest.class,
        KeyringContainerTest.removeTest.class,
        KeyringContainerTest.signMessageTest.class
})
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
}
