package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        Caver caver = new Caver(Caver.DEFAULT_URL);

        public void checkAddress(List<String> expectedAddress) {
            expectedAddress.stream().forEach(address -> {
                AbstractKeyring keyring = caver.wallet.getKeyring(address);
                assertNotNull(keyring);
                assertEquals(address, keyring.getAddress());
            });
        }

        //CA-KEYRINGCONTAINER-001
        @Test
        public void validKeyringCountNoEntropy() {
            List<String> expectAddressList = caver.wallet.generate(10);

            checkAddress(expectAddressList);
        }

        //CA-KEYRINGCONTAINER-002
        @Test
        public void validKeyringCountWithEntropy() {
            List<String> expectAddressList = caver.wallet.generate(10, "entropy");

            checkAddress(expectAddressList);
        }
    }

    public static class newKeyringTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        //CA-KEYRINGCONTAINER-003
        @Test
        public void newSingleKeyring() {
            PrivateKey privateKey = caver.wallet.keyring.generate().getKey();
            String expectAddress = privateKey.getDerivedAddress();
            String expectPrivateKey = privateKey.getPrivateKey();

            AbstractKeyring added = caver.wallet.newKeyring(expectAddress, expectPrivateKey);
            validateSingleKeyring(added, expectAddress, expectPrivateKey);
            assertEquals(1, caver.wallet.length());
        }

        //CA-KEYRINGCONTAINER-004
        @Test
        public void newMultipleKeyring() {
            String expectAddress = caver.wallet.keyring.generate().getAddress();
            String[] expectPrivateKeyArr = {
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
            };

            AbstractKeyring added = caver.wallet.newKeyring(expectAddress, expectPrivateKeyArr);
            validateMultipleKeyring(added, expectAddress, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-005
        @Test
        public void newRoleBasedKeyring() {
            String expectAddress = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            AbstractKeyring added = caver.wallet.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
            validateRoleBasedKeyring(added, expectAddress, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-006
        @Test
        public void newRoleBasedKeyringWithEmptyRole() {
            String expectAddress = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                },
                {
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            AbstractKeyring added = caver.wallet.newKeyring(expectAddress, Arrays.asList(expectPrivateKeyArr));
            validateRoleBasedKeyring(added, expectAddress, expectPrivateKeyArr);
        }
    }

    public static class updateKeyringTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRINGCONTAINER-007
        @Test
        public void updateToCoupledKeyring() {
            SingleKeyring coupled = caver.wallet.keyring.generate();

            String address = coupled.getAddress();
            String privateKey = caver.wallet.keyring.generateSingleKey();

            SingleKeyring decoupled = caver.wallet.keyring.createWithSingleKey(coupled.getAddress(), privateKey);

            caver.wallet.add(decoupled);

            AbstractKeyring updated = caver.wallet.updateKeyring(coupled);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(address);

            validateSingleKeyring(updated, address, coupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), coupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-008
        @Test
        public void updateToDecoupledKeyring() {
            SingleKeyring coupled = caver.wallet.keyring.generate();
            SingleKeyring deCoupled = caver.wallet.keyring.createWithSingleKey(
                coupled.getAddress(),
                caver.wallet.keyring.generateSingleKey()
            );

            caver.wallet.add(coupled);

            AbstractKeyring updated = caver.wallet.updateKeyring(deCoupled);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(coupled.getAddress());

            validateSingleKeyring(updated, coupled.getAddress(), deCoupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, coupled.getAddress(), deCoupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-009
        @Test
        public void updateToMultipleKeyring() {
            SingleKeyring origin = caver.wallet.keyring.generate();

            String[] expectPrivateKeyArr = {
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
            };

            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                origin.getAddress(),
                expectPrivateKeyArr
            );

            caver.wallet.add(origin);

            AbstractKeyring updated = caver.wallet.updateKeyring(multipleKeyring);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(origin.getAddress());

            validateMultipleKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateMultipleKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-010
        @Test
        public void updateToRoleBasedKeyring() {
            SingleKeyring origin = caver.wallet.keyring.generate();

            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                origin.getAddress(),
                Arrays.asList(expectPrivateKeyArr)
            );

            caver.wallet.add(origin);

            AbstractKeyring updated = caver.wallet.updateKeyring(roleBasedKeyring);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(origin.getAddress());

            validateRoleBasedKeyring(updated, origin.getAddress(), expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, origin.getAddress(), expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-011
        @Test
        public void throwException_NotExistedKeyring() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Failed to find keyring to update.");

            SingleKeyring keyring = caver.wallet.keyring.generate();

            caver.wallet.updateKeyring(keyring);
        }
    }

    public static class getKeyringTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRINGCONTAINER-012
        @Test
        public void withValidAddress() {
            SingleKeyring added = (SingleKeyring) caver.wallet.add(caver.wallet.keyring.generate());
            AbstractKeyring keyring = caver.wallet.getKeyring(added.getAddress());

            validateSingleKeyring(keyring, added.getAddress(), added.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-013
        @Test
        public void notExistsAddress() {
            SingleKeyring keyring = caver.wallet.keyring.generate();

            AbstractKeyring actual = caver.wallet.getKeyring(keyring.getAddress());
            assertNull(actual);
        }

        //CA-KEYRINGCONTAINER-014
        @Test
        public void throwException_InvalidAddress() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address. To get keyring from wallet, you need to pass a valid address string as a parameter.");

            String invalidAddress = "invalid";

            caver.wallet.getKeyring(invalidAddress);
        }

        @Test
        public void upperCaseAddress() {
            String address = "0x37223E5E41186A782E4A1F709829F521F43B18E5";
            SingleKeyring keyring = caver.wallet.keyring.create(
                address,
                caver.wallet.keyring.generateSingleKey()
            );

            caver.wallet.add(keyring);

            assertNotNull(caver.wallet.getKeyring(address));
            assertEquals(address, caver.wallet.getKeyring(address).getAddress());
        }
    }

    public static class addTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        //CA-KEYRINGCONTAINER-015
        @Test
        public void singleKeyring() {
            SingleKeyring keyringToAdd = caver.wallet.keyring.generate();

            AbstractKeyring added = caver.wallet.add(keyringToAdd);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(added.getAddress());

            validateSingleKeyring(added, keyringToAdd.getAddress(), keyringToAdd.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, keyringToAdd.getAddress(), keyringToAdd.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-016
        @Test
        public void deCoupledKeyring() {
            SingleKeyring deCoupled = caver.wallet.keyring.createWithSingleKey(
                caver.wallet.keyring.generate().getAddress(),
                caver.wallet.keyring.generateSingleKey()
            );

            AbstractKeyring added = caver.wallet.add(deCoupled);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(deCoupled.getAddress());

            validateSingleKeyring(added, deCoupled.getAddress(), deCoupled.getKey().getPrivateKey());
            validateSingleKeyring(fromContainer, deCoupled.getAddress(), deCoupled.getKey().getPrivateKey());
        }

        //CA-KEYRINGCONTAINER-017
        @Test
        public void multipleKeyring() {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] expectPrivateKeyArr = {
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
            };

            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                address,
                expectPrivateKeyArr
            );

            AbstractKeyring added = caver.wallet.add(multipleKeyring);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(multipleKeyring.getAddress());

            validateMultipleKeyring(added, address, expectPrivateKeyArr);
            validateMultipleKeyring(fromContainer, address, expectPrivateKeyArr);
        }

        //CA-KEYRINGCONTAINER-018
        @Test
        public void roleBasedKeyring() {
            String address = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                address,
                Arrays.asList(expectPrivateKeyArr)
            );

            AbstractKeyring added = caver.wallet.add(roleBasedKeyring);
            AbstractKeyring fromContainer = caver.wallet.getKeyring(added.getAddress());

            validateRoleBasedKeyring(added, address, expectPrivateKeyArr);
            validateRoleBasedKeyring(fromContainer, address, expectPrivateKeyArr);
        }

    }

    public static class removeTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        //CA-KEYRINGCONTAINER-019
        @Test
        public void coupledKey() {
            AbstractKeyring keyringToAdd = caver.wallet.keyring.generate();

            AbstractKeyring added = caver.wallet.add(keyringToAdd);
            boolean result = caver.wallet.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, caver.wallet.length());
            assertNull(caver.wallet.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-020
        @Test
        public void deCoupledKey() {
            AbstractKeyring keyringToAdd = caver.wallet.keyring.createWithSingleKey(
                caver.wallet.keyring.generate().getAddress(),
                caver.wallet.keyring.generateSingleKey()
            );

            AbstractKeyring added = caver.wallet.add(keyringToAdd);
            boolean result = caver.wallet.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, caver.wallet.length());
            assertNull(caver.wallet.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-021
        @Test
        public void multipleKey() {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] expectPrivateKeyArr = {
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
                caver.wallet.keyring.generateSingleKey(),
            };

            AbstractKeyring keyringToAdd = caver.wallet.keyring.createWithMultipleKey(
                address,
                expectPrivateKeyArr
            );

            AbstractKeyring added = caver.wallet.add(keyringToAdd);
            boolean result = caver.wallet.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, caver.wallet.length());
            assertNull(caver.wallet.getKeyring(keyringToAdd.getAddress()));
        }

        //CA-KEYRINGCONTAINER-022
        @Test
        public void roleBasedKey() {
            String address = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            AbstractKeyring keyringToAdd = caver.wallet.keyring.createWithRoleBasedKey(
                address,
                Arrays.asList(expectPrivateKeyArr)
            );

            AbstractKeyring added = caver.wallet.add(keyringToAdd);
            boolean result = caver.wallet.remove(added.getAddress());

            assertTrue(result);
            assertEquals(0, caver.wallet.length());
            assertNull(caver.wallet.getKeyring(keyringToAdd.getAddress()));
        }

        @Test
        public void keyringWithChecksumAddress() {
            String checksumAddress = "0xAF00B68464538Ee586b4f20C22b8A4ad28Ac4761";
            SingleKeyring singleKeyring = caver.wallet.keyring.create(
                    checksumAddress,
                    caver.wallet.keyring.generateSingleKey()
            );
            caver.wallet.add(singleKeyring);
            caver.wallet.remove(singleKeyring.getAddress());
            assertEquals("Should remove singleKeyring having checksum address from caver.wallet", caver.wallet.length() , 0);

            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                    checksumAddress,
                    caver.wallet.keyring.generateMultipleKeys(3)
            );
            caver.wallet.add(multipleKeyring);
            caver.wallet.remove(multipleKeyring.getAddress());
            assertEquals("Should remove multipleKeyring having checksum address from caver.wallet", caver.wallet.length(), 0);

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                    checksumAddress,
                    caver.wallet.keyring.generateRolBasedKeys(new int[]{2, 2, 1})
            );
            caver.wallet.add(roleBasedKeyring);
            caver.wallet.remove(multipleKeyring.getAddress());
            assertEquals("Should remove roleBasedKeyring having checksum address from caver.wallet", caver.wallet.length(), 0);
        }
    }

    public static class signMessageTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        //CA-KEYRINGCONTAINER-023
        @Test
        public void signMessage() {
            String message = "message";

            String address = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            AbstractKeyring roleBased = caver.wallet.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 0, 0);
            MessageSigned actualData = caver.wallet.signMessage(address, message);

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
            String message = "message";

            String address = caver.wallet.keyring.generate().getAddress();
            String[][] expectPrivateKeyArr = {
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                },
                {
                    caver.wallet.keyring.generateSingleKey(),
                    caver.wallet.keyring.generateSingleKey(),
                }
            };

            AbstractKeyring roleBased = caver.wallet.newKeyring(address, Arrays.asList(expectPrivateKeyArr));
            MessageSigned expectedData = roleBased.signMessage(message, 1, 0);
            MessageSigned actualData = caver.wallet.signMessage(address, message, 1, 0);

            assertEquals(message, actualData.getMessage());
            assertEquals(Utils.hashMessage(message), actualData.getMessageHash());
            assertNotNull(actualData.getSignatures());

            assertEquals(expectedData.getSignatures().get(0).getR(), actualData.getSignatures().get(0).getR());
            assertEquals(expectedData.getSignatures().get(0).getS(), actualData.getSignatures().get(0).getS());
            assertEquals(expectedData.getSignatures().get(0).getV(), actualData.getSignatures().get(0).getV());
        }
    }

    public static class signTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void sign_withIndex_singleKeyring() throws IOException {
            SingleKeyring singleKeyring = caver.wallet.keyring.generate();

            caver.wallet.add(singleKeyring);
            ValueTransfer valueTransfer = generateValueTransfer(singleKeyring);

            caver.wallet.sign(singleKeyring.getAddress(), valueTransfer, 0);
            SignatureData expectedSig = singleKeyring.sign(TransactionHasher.getHashForSignature(valueTransfer), valueTransfer.getChainId(), 0, 0);

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_multipleKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] multipleKey = caver.wallet.keyring.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                address,
                multipleKey
            );

            ValueTransfer valueTransfer = generateValueTransfer(multipleKeyring);

            AbstractKeyring keyring = caver.wallet.add(multipleKeyring);
            caver.wallet.sign(multipleKeyring.getAddress(), valueTransfer, 1);

            SignatureData expectedSig = keyring.sign(
                TransactionHasher.getHashForSignature(valueTransfer),
                valueTransfer.getChainId(),
                0,
                1
            );

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_roleBasedKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            ValueTransfer valueTransfer = generateValueTransfer(roleBasedKeyring);

            AbstractKeyring keyring = caver.wallet.add(roleBasedKeyring);
            caver.wallet.sign(keyring.getAddress(), valueTransfer, 2);

            SignatureData expectedSig = keyring.sign(
                TransactionHasher.getHashForSignature(valueTransfer),
                valueTransfer.getChainId(),
                0,
                2
            );

            assertEquals(1, valueTransfer.getSignatures().size());
            assertEquals(expectedSig, valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_withIndex_accountUpdate() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            AccountUpdate accountUpdate = generateAccountUpdate(roleBasedKeyring);
            AbstractKeyring keyring = caver.wallet.add(roleBasedKeyring);
            caver.wallet.sign(keyring.getAddress(), accountUpdate, 2);

            SignatureData expectedSig = roleBasedKeyring.sign(
                TransactionHasher.getHashForSignature(accountUpdate),
                accountUpdate.getChainId(),
                1,
                2
            );

            assertEquals(1, accountUpdate.getSignatures().size());
            assertEquals(expectedSig, accountUpdate.getSignatures().get(0));
        }

        @Test
        public void throwException_withIndex_notExistedKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(caver.wallet.keyring.generate());
            caver.wallet.sign(address, valueTransfer, 0);
        }

        @Test
        public void sign_singleKeyring() throws IOException {
            AbstractKeyring keyring = caver.wallet.add(caver.wallet.keyring.generate());

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            caver.wallet.sign(keyring.getAddress(), valueTransfer);

            assertEquals(1, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_multipleKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] multipleKey = caver.wallet.keyring.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(address, multipleKey);

            AbstractKeyring keyring = caver.wallet.add(multipleKeyring);


            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            caver.wallet.sign(keyring.getAddress(), valueTransfer);

            assertEquals(3, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_roleBasedKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            AbstractKeyring keyring = caver.wallet.add(roleBasedKeyring);

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            caver.wallet.sign(keyring.getAddress(), valueTransfer);

            assertEquals(4, valueTransfer.getSignatures().size());
        }

        @Test
        public void sign_customHasher() throws IOException {
            AbstractKeyring keyring = caver.wallet.add(caver.wallet.keyring.generate());

            ValueTransfer valueTransfer = generateValueTransfer(keyring);
            caver.wallet.sign(keyring.getAddress(), valueTransfer, (AbstractTransaction tx) -> {
                return "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1";
            });

            List<SignatureData> expected = keyring.sign(
                "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1",
                valueTransfer.getChainId(),
                AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()
            );
            assertEquals(expected.get(0), valueTransfer.getSignatures().get(0));
        }

        @Test
        public void sign_AccountUpdateTx() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            caver.wallet.add(roleBasedKeyring);
            AccountUpdate tx = generateAccountUpdate(roleBasedKeyring);

            caver.wallet.sign(address, tx);
            assertEquals(5, tx.getSignatures().size());
        }

        @Test
        public void throwException_notExistKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(caver.wallet.keyring.generate());
            caver.wallet.sign(address, valueTransfer);
        }
    }

    public static class signAsFeePayerTest {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void signAsFeePayer_withIndex_singleKeyring() throws IOException {
            SingleKeyring singleKeyring = caver.wallet.keyring.generate();

            caver.wallet.add(singleKeyring);
            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(singleKeyring);

            caver.wallet.signAsFeePayer(singleKeyring.getAddress(), feeDelegatedValueTransfer, 0);
            SignatureData expectedSig = singleKeyring.sign(
                TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer),
                feeDelegatedValueTransfer.getChainId(),
                2,
                0
            );

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void signAsFeePayer_withIndex_multipleKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] multipleKey = caver.wallet.keyring.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(address, multipleKey);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(multipleKeyring);

            AbstractKeyring keyring = caver.wallet.add(multipleKeyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), feeDelegatedValueTransfer, 1);

            SignatureData expectedSig = keyring.sign(
                TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer),
                feeDelegatedValueTransfer.getChainId(),
                2,
                1
            );

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void signAsFeePayer_withIndex_roleBasedKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(roleBasedKeyring);

            AbstractKeyring keyring = caver.wallet.add(roleBasedKeyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), feeDelegatedValueTransfer, 1);

            SignatureData expectedSig = keyring.sign(TransactionHasher.getHashForFeePayerSignature(feeDelegatedValueTransfer), feeDelegatedValueTransfer.getChainId(), 2, 1);

            assertEquals(1, feeDelegatedValueTransfer.getFeePayerSignatures().size());
            assertEquals(expectedSig, feeDelegatedValueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void throwException_withIndex_notExistedKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            String address = "0x1234567890123456789012345678901234567890";

            FeeDelegatedValueTransfer feeDelegatedValueTransfer = generateFeeDelegatedValueTransfer(caver.wallet.keyring.generate());
            caver.wallet.signAsFeePayer(address, feeDelegatedValueTransfer, 0);
        }

        @Test
        public void signAsFeePayer_singleKeyring() throws IOException {
            AbstractKeyring keyring = caver.wallet.add(caver.wallet.keyring.generate());

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(1, valueTransfer.getSignatures().size());
        }

        @Test
        public void signAsFeePayer_multipleKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            String[] multipleKey = caver.wallet.keyring.generateMultipleKeys(3);
            MultipleKeyring multipleKeyring = caver.wallet.keyring.createWithMultipleKey(address, multipleKey);

            AbstractKeyring keyring = caver.wallet.add(multipleKeyring);

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(3, valueTransfer.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_roleBasedKeyring() throws IOException {
            String address = caver.wallet.keyring.generate().getAddress();
            List<String[]> roleBasedKey = caver.wallet.keyring.generateRoleBasedKeys(
                new int[]{4,5,6},
                "entropy"
            );
            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(address, roleBasedKey);

            AbstractKeyring keyring = caver.wallet.add(roleBasedKeyring);

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), valueTransfer);

            assertEquals(6, valueTransfer.getFeePayerSignatures().size());
        }

        @Test
        public void signAsFeePayer_customHasher() throws IOException {
            AbstractKeyring keyring = caver.wallet.add(caver.wallet.keyring.generate());

            FeeDelegatedValueTransfer valueTransfer = generateFeeDelegatedValueTransfer(keyring);
            caver.wallet.signAsFeePayer(keyring.getAddress(), valueTransfer, (AbstractFeeDelegatedTransaction tx) -> {
                return "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1";
            });

            List<SignatureData> expected = keyring.sign(
                "0xd4aab6590bdb708d1d3eafe95a967dafcd2d7cde197e512f3f0b8158e7b65fd1", valueTransfer.getChainId(),
                AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()
            );
            assertEquals(expected.get(0), valueTransfer.getFeePayerSignatures().get(0));
        }

        @Test
        public void throwException_notExistKeyring() throws IOException {
            expectedException.expect(NullPointerException.class);
            expectedException.expectMessage("Failed to find keyring from wallet with address");

            String address = "0x1234567890123456789012345678901234567890";

            ValueTransfer valueTransfer = generateValueTransfer(caver.wallet.keyring.generate());
            caver.wallet.sign(address, valueTransfer);
        }

    }

}
