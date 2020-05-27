package com.klaytn.caver.common;

import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.KeyringContainer;
import com.klaytn.caver.wallet.keyring.Keyring;
import com.klaytn.caver.wallet.keyring.MessageSigned;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

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

    static void validateSingleKeyring(Keyring actual, String expectAddress, String expectKey) {
        assertEquals(expectAddress, actual.getAddress());
        assertEquals(expectKey, actual.getKeys().get(0)[0].getPrivateKey());
    }

    static void validateMultipleKeyring(Keyring actual, String expectAddress, String[] expectKeyArr) {
        PrivateKey[] actualKeyArr = actual.getKeys().get(0);

        assertEquals(expectAddress, actual.getAddress());
        for(int i=0; i<expectKeyArr.length; i++) {
            assertEquals(expectKeyArr[i], actualKeyArr[i].getPrivateKey());
        }
    }

    static void validateRoleBasedKeyring(Keyring actual, String expectAddress, String[][] expectKeyArr) {
        List<PrivateKey[]> actualKeyArr = actual.getKeys();

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
                Keyring keyring = container.getKeyring(address);
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

            Keyring added = container.newKeyring(expectAddress, expectPrivateKey);
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

            Keyring added = container.newKeyring(expectAddress, expectPrivateKeyArr);
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

            Keyring added = container.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
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

            Keyring added = container.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
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
            Keyring coupled = Keyring.generate();

            String address = coupled.getAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            Keyring decoupled = Keyring.createWithSingleKey(coupled.getAddress(), privateKey);

            container.add(decoupled);

            Keyring updated = container.updateKeyring(coupled);
            Keyring fromContainer = container.getKeyring(address);

            validateSingleKeyring(updated, address, coupled.getKeys().get(0)[0].getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), coupled.getKeys().get(0)[0].getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-008
        @Test
        public void updateToDecoupledKeyring() {
            KeyringContainer container = new KeyringContainer();
            Keyring coupled = Keyring.generate();
            Keyring deCoupled = Keyring.createWithSingleKey(coupled.getAddress(), PrivateKey.generate().getPrivateKey());

            container.add(coupled);

            Keyring updated = container.updateKeyring(deCoupled);
            Keyring fromContainer = container.getKeyring(coupled.getAddress());

            validateSingleKeyring(updated,  coupled.getAddress(), deCoupled.getKeys().get(0)[0].getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), deCoupled.getKeys().get(0)[0].getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-009
        @Test
        public void updateToMultipleKeyring() {
            KeyringContainer container = new KeyringContainer();
            Keyring origin = Keyring.generate();

            String[] expectPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            Keyring multipleKeyring = Keyring.createWithMultipleKey(origin.getAddress(), expectPrivateKeyArr);

            container.add(origin);

            Keyring updated = container.updateKeyring(multipleKeyring);
            Keyring fromContainer = container.getKeyring(origin.getAddress());

            validateMultipleKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateMultipleKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-010
        @Test
        public void updateToRoleBasedKeyring() {
            KeyringContainer container = new KeyringContainer();
            Keyring origin = Keyring.generate();

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

            Keyring roleBasedKeyring = Keyring.createWithRoleBasedKey(origin.getAddress(), Arrays.asList(expectPrivateKeyArr));

            container.add(origin);

            Keyring updated = container.updateKeyring(roleBasedKeyring);
            Keyring fromContainer = container.getKeyring(origin.getAddress());

            validateRoleBasedKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-011
        @Test
        public void throwException_NotExistedKeyring() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Failed to find keyring to update.");

            Keyring keyring = Keyring.generate();
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

            Keyring added = container.add(Keyring.generate());
            Keyring keyring = container.getKeyring(added.getAddress());

            validateSingleKeyring(keyring, added.getAddress(), added.getKeys().get(0)[0].getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-013
        @Test
        public void notExistsAddress() {
            KeyringContainer container = new KeyringContainer();
            Keyring keyring = Keyring.generate();

            Keyring actual = container.getKeyring(keyring.getAddress());
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
            Keyring keyringToAdd = Keyring.generate();

            Keyring added = container.add(keyringToAdd);
            Keyring fromContainer = container.getKeyring(added.getAddress());

            validateSingleKeyring(added, keyringToAdd.getAddress(), keyringToAdd.getKeys().get(0)[0].getPrivateKey());
            validateSingleKeyring(fromContainer, keyringToAdd.getAddress(), keyringToAdd.getKeys().get(0)[0].getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-016
        @Test
        public void deCoupledKeyring() {
            KeyringContainer container = new KeyringContainer();
            Keyring deCoupled = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());

            Keyring added = container.add(deCoupled);
            Keyring fromContainer = container.getKeyring(deCoupled.getAddress());

            validateSingleKeyring(added, deCoupled.getAddress(), deCoupled.getKeys().get(0)[0].getPrivateKey());
            validateSingleKeyring(fromContainer, deCoupled.getAddress(), deCoupled.getKeys().get(0)[0].getPrivateKey());
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

            Keyring multipleKeyring = Keyring.createWithMultipleKey(address, expectPrivateKeyArr);

            Keyring added = container.add(multipleKeyring);
            Keyring fromContainer = container.getKeyring(multipleKeyring.getAddress());

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

            Keyring roleBasedKeyring = Keyring.createWithRoleBasedKey(address, Arrays.asList(expectPrivateKeyArr));

            Keyring added = container.add(roleBasedKeyring);
            Keyring fromContainer = container.getKeyring(added.getAddress());

            validateRoleBasedKeyring(added, address, expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, address, expectPrivateKeyArr);
        }
    }

    public static class removeTest {

        //CA-KEYRINGCONTAINER-019
        @Test
        public void coupledKey() {
            KeyringContainer container = new KeyringContainer();
            Keyring keyringToAdd = Keyring.generate();

            Keyring added = container.add(keyringToAdd);
            boolean result = container.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, container.length());
            assertNull(container.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-020
        @Test
        public void deCoupledKey() {
            KeyringContainer container = new KeyringContainer();
            Keyring keyringToAdd = Keyring.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), PrivateKey.generate().getPrivateKey());

            Keyring added = container.add(keyringToAdd);
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

            Keyring keyringToAdd = Keyring.createWithMultipleKey(address, expectPrivateKeyArr);

            Keyring added = container.add(keyringToAdd);
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

            Keyring keyringToAdd = Keyring.createWithRoleBasedKey(address, Arrays.asList(expectPrivateKeyArr));

            Keyring added = container.add(keyringToAdd);
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

            Keyring roleBased = container.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 0, 0);
            MessageSigned actualData = container.signMessage(address, message);

            assertEquals(message, actualData.getMessage());
            assertEquals(Utils.hashMessage(message), actualData.getMessageHash());
            assertNotNull(actualData.getSignatureData());

            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getR()), Numeric.toHexString(actualData.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getS()), Numeric.toHexString(actualData.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getV()), Numeric.toHexString(actualData.getSignatureData().getV()));
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

            Keyring roleBased = container.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 1, 0);
            MessageSigned actualData = container.signMessage(address, message, 1, 0);

            assertEquals(message, actualData.getMessage());
            assertEquals(Utils.hashMessage(message), actualData.getMessageHash());
            assertNotNull(actualData.getSignatureData());

            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getR()), Numeric.toHexString(actualData.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getS()), Numeric.toHexString(actualData.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expectedData.getSignatureData().getV()), Numeric.toHexString(actualData.getSignatureData().getV()));
        }
    }
}
