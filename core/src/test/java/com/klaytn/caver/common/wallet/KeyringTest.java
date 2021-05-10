package com.klaytn.caver.common.wallet;

import com.klaytn.caver.account.*;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.crypto.CipherException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KeyringTest {

    public static void checkValidateSingleKey(SingleKeyring actualKeyring, String expectedAddress, String expectedPrivateKey) {
        assertTrue(Utils.isAddress(actualKeyring.getAddress()));
        assertEquals(expectedAddress, actualKeyring.getAddress());

        PrivateKey actualPrivateKey = actualKeyring.getKey();
        assertTrue(Utils.isValidPrivateKey(actualPrivateKey.getPrivateKey()));
        assertEquals(expectedPrivateKey, actualPrivateKey.getPrivateKey());
    }

    public static void checkValidateMultipleKey(MultipleKeyring actualKeyring, String expectedAddress, String[] expectedPrivateKeyArr) {
        assertTrue(Utils.isAddress(actualKeyring.getAddress()));
        assertEquals(expectedAddress, actualKeyring.getAddress());

        PrivateKey[] actualPrivateKeyArr = actualKeyring.getKeys();
        assertEquals(expectedPrivateKeyArr.length, actualPrivateKeyArr.length);

        for(int i=0; i<actualPrivateKeyArr.length; i++) {
            assertEquals(expectedPrivateKeyArr[i], actualPrivateKeyArr[i].getPrivateKey());
        }

    }

    public static void checkValidateRoleBasedKey(RoleBasedKeyring actualKeyring, String expectedAddress, List<String[]> expectedPrivateKeyList) {
        assertTrue(Utils.isAddress(actualKeyring.getAddress()));
        assertEquals(expectedAddress, actualKeyring.getAddress());

        List<PrivateKey[]> actualKeyList = actualKeyring.getKeys();
        assertEquals(expectedPrivateKeyList.size(), actualKeyList.size());

        for(int i=0; i<actualKeyList.size(); i++) {
            PrivateKey[] actualKeyArr = actualKeyList.get(i);
            String[] expectedKeyArr = expectedPrivateKeyList.get(i);

            assertEquals(expectedKeyArr.length, actualKeyArr.length);

            for(int j=0; j<actualKeyArr.length; j++) {
                assertEquals(expectedKeyArr[j], actualKeyArr[j].getPrivateKey());
            }
        }
    }

    public static void checkValidKeyring(AbstractKeyring expect, AbstractKeyring actual) {
        assertEquals(expect.getAddress(), actual.getAddress());
        assertEquals(expect.getClass(), actual.getClass());

        if(expect instanceof SingleKeyring) {
            assertEquals(((SingleKeyring) expect).getKey().getPrivateKey(), ((SingleKeyring)actual).getKey().getPrivateKey());
        }

        if(expect instanceof MultipleKeyring) {
            MultipleKeyring expectKeyring = (MultipleKeyring)expect;
            MultipleKeyring actualKeyring = (MultipleKeyring)actual;

            for(int i=0; i<expectKeyring.getKeys().length; i++) {
                assertEquals(expectKeyring.getKeys().length, actualKeyring.getKeys().length);
            }
        }
        if(expect instanceof RoleBasedKeyring) {
            RoleBasedKeyring expectKeyring = (RoleBasedKeyring)expect;
            RoleBasedKeyring actualKeyring = (RoleBasedKeyring)actual;

            for(int i=0; i< expectKeyring.getKeys().size(); i++) {
                PrivateKey[] actualArr = actualKeyring.getKeys().get(i);
                PrivateKey[] expectedArr = expectKeyring.getKeys().get(i);

                assertEquals(expectedArr.length, actualArr.length);

                for(int j=0; j<actualArr.length; j++) {
                    assertEquals(expectedArr[j].getPrivateKey(), actualArr[j].getPrivateKey());
                }
            }
        }
    }

    public static MultipleKeyring generateMultipleKeyring(int num) {
        String[] keyArr = new String[num];

        for(int i=0; i<num; i++) {
            keyArr[i] = PrivateKey.generate("entropy").getPrivateKey();
        }

        String address = PrivateKey.generate("entropy").getDerivedAddress();
        return KeyringFactory.createWithMultipleKey(address, keyArr);
    }

    public static RoleBasedKeyring generateRoleBaseKeyring(int[] numArr) {
        String[][] keyArr = new String[3][];

        for(int i=0; i<numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j=0; j<length; j++) {
                arr[j] = PrivateKey.generate("entropy").getPrivateKey();
            }
            keyArr[i] = arr;
        }

        String address = PrivateKey.generate("entropy").getDerivedAddress();
        List<String[]> arr = Arrays.asList(keyArr);

        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static class generateTest {
        //CA-KEYRING-001
        @Test
        public void generate() {
            AbstractKeyring keyring = KeyringFactory.generate();
            assertTrue(Utils.isAddress(keyring.getAddress()));
        }
        //CA-KEYRING-002
        @Test
        public void generateWithEntropy() {
            byte[] random = Utils.generateRandomBytes(32);
            AbstractKeyring keyring = KeyringFactory.generate(Numeric.toHexString(random));

            assertTrue(Utils.isAddress(keyring.getAddress()));
        }
    }

    public static class createFromPrivateKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-003
        @Test
        public void createFromPrivateKey() {
            SingleKeyring keyring = KeyringFactory.generate();
            String expectedAddress = keyring.getAddress();
            String expectedPrivateKey = keyring.getKey().getPrivateKey();

            SingleKeyring actualKeyring = KeyringFactory.createFromPrivateKey(expectedPrivateKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-004
        @Test
        public void createFromPrivateKeyWithoutHexPrefix() {
            SingleKeyring keyring = KeyringFactory.generate();
            String expectedAddress = keyring.getAddress();
            String expectedPrivateKey = keyring.getKey().getPrivateKey();

            SingleKeyring actualKeyring = KeyringFactory.createFromPrivateKey(Numeric.cleanHexPrefix(expectedPrivateKey));
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-005
        @Test
        public void createFromPrivateFromKlaytnWalletKey() {
            String klaytnWalletKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d80x000xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";
            String expectedPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
            String expectedAddress = "0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";

            SingleKeyring actualKeyring = KeyringFactory.createFromPrivateKey(klaytnWalletKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-006
        @Test
        public void createFromPrivate_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid private key.");

            byte[] random = Utils.generateRandomBytes(31);
            AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(Numeric.toHexString(random));
        }
    }


    public static class createFromKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-007
        @Test
        public void createFromKlaytnWalletKey() {
            String klaytnWalletKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d80x000xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";
            String expectedPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
            String expectedAddress = "0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";

            SingleKeyring actualKeyring = KeyringFactory.createFromKlaytnWalletKey(klaytnWalletKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-008
        @Test
        public void createFromKlaytnWalletKey_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalidWalletKey = "39d87f15c695ec94d6d7107b48dee85e252f21fedd371e1c6baefbdf0x000x658b7b7a94ac398a8e7275e719a10c";
            AbstractKeyring actualKeyring = KeyringFactory.createFromKlaytnWalletKey(invalidWalletKey);
        }
    }

    public static class createTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-009
        @Test
        public void create_SingleKey() {
            PrivateKey expectedPrivateKey = PrivateKey.generate();

            SingleKeyring actualKeyring = KeyringFactory.create(expectedPrivateKey.getDerivedAddress(), expectedPrivateKey.getPrivateKey());
            checkValidateSingleKey(actualKeyring, expectedPrivateKey.getDerivedAddress(), expectedPrivateKey.getPrivateKey());
        }

        //CA-KEYRING-010
        @Test
        public void create_MultiPleKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[] privateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            MultipleKeyring actualKeyring = KeyringFactory.create(expectedAddress, privateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, privateKeyArr);
        }

        //CA-KEYRING-011
        @Test
        public void create_MultiPleKey_throwException_exceedKeyCount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("MultipleKey has up to 10");

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[] privateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            MultipleKeyring actualKeyring = KeyringFactory.create(expectedAddress, privateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, privateKeyArr);
        }

        //CA-KEYRING-012
        @Test
        public void create_RoleBasedKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            RoleBasedKeyring actualKeyring = KeyringFactory.create(expectedAddress, expectedKeyList);
            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }

        //CA-KEYRING-013
        @Test
        public void createWithRoleBasedKey_EmptyRole() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {},
                    {},
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    }
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            RoleBasedKeyring actualKeyring = KeyringFactory.create(expectedAddress, expectedKeyList);
            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }

        //CA-KEYRING-014
        @Test
        public void create_RoleBasedKey_throwException_exceedComponent() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("RoleBasedKey component must have 3.");

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {},
                    {},
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {}
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            AbstractKeyring actualKeyring = KeyringFactory.create(expectedAddress, expectedKeyList);
        }

        //CA-KEYRING-015
        @Test
        public void create_RoleBasedKey_throwException_exceedKeyCount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The keys in RoleBasedKey component has up to 10.");

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {},
                    {},
                    { // 12 elements.
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            AbstractKeyring actualKeyring = KeyringFactory.create(expectedAddress, expectedKeyList);
        }
    }

    public static class createWithSingleKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-016
        @Test
        public void createWithSingleKey_coupled() {
            PrivateKey key = PrivateKey.generate();
            String expectedAddress = key.getDerivedAddress();
            String expectedPrivateKey = key.getPrivateKey();

            SingleKeyring actualKeyring = KeyringFactory.createWithSingleKey(expectedAddress, expectedPrivateKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-017
        @Test
        public void createWithSingleKey_decoupled() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring actualKeyring = KeyringFactory.createWithSingleKey(expectedAddress, expectedPrivateKey);

            assertTrue(actualKeyring.isDecoupled());
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-018
        @Test
        public void createWithSingleKey_throwException_KlaytnWalletKeyFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid format of parameter. Use 'fromKlaytnWalletKey' to create Keyring from KlaytnWalletKey.");

            SingleKeyring keyring = KeyringFactory.generate();
            String klaytnWalletKey = keyring.getKlaytnWalletKey();

            AbstractKeyring actualKeyring = KeyringFactory.createWithSingleKey(keyring.getAddress(), klaytnWalletKey);
        }
    }


    public static class createWithMultipleKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-019
        @Test
        public void createWithMultipleKey() {
            String expectedAddress = KeyringFactory.generate().getAddress();

            String[] expectedPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            MultipleKeyring actualKeyring = KeyringFactory.createWithMultipleKey(expectedAddress, expectedPrivateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, expectedPrivateKeyArr);
        }

        //CA-KEYRING-020
        @Test
        public void createWithMultipleKey_throwException_invalidKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid private key.");

            String expectedAddress = KeyringFactory.generate().getAddress();

            byte[] random = Utils.generateRandomBytes(31);
            String[] expectedPrivateKeyArr = {
                    Numeric.toHexString(random),
                    PrivateKey.generate().getPrivateKey(),
            };

            AbstractKeyring actualKeyring = KeyringFactory.createWithMultipleKey(expectedAddress, expectedPrivateKeyArr);
        }
    }

    public static class createWithRoleBasedKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-021
        @Test
        public void createWithRoleBasedKey() {
            String expectedAddress = KeyringFactory.generate().getAddress();
            String[][] expectedPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey()
                    }
            };
            List<String[]> expectedKeyList = Arrays.asList(expectedPrivateKeyArr);
            RoleBasedKeyring actualKeyring = KeyringFactory.createWithRoleBasedKey(expectedAddress, expectedKeyList);

            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }
        //CA-KEYRING-022
        @Test
        public void createWithRoleBasedKey_throwException_exceedComponent() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("RoleBasedKey component must have 3.");

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {},
                    {},
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {}
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            AbstractKeyring actualKeyring = KeyringFactory.createWithRoleBasedKey(expectedAddress, expectedKeyList);
        }

        //CA-KEYRING-023
        @Test
        public void createWithRoleBasedKey_throwException_exceedKeyCount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The keys in RoleBasedKey component has up to 10.");

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] privateKeyArr = {
                    {},
                    {},
                    { // 12 elements.
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
            };

            List<String[]> expectedKeyList = Arrays.asList(privateKeyArr);
            AbstractKeyring actualKeyring = KeyringFactory.createWithRoleBasedKey(expectedAddress, expectedKeyList);
        }
    }

    public static class copyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-024
        @Test
        public void copy_coupled() {
            SingleKeyring expectedKeyring = KeyringFactory.generate();
            String expectedPrivateKey = expectedKeyring.getKey().getPrivateKey();
            AbstractKeyring actualKeyring = expectedKeyring.copy();

            checkValidateSingleKey((SingleKeyring) actualKeyring, expectedKeyring.getAddress(), expectedPrivateKey);
        }

        //CA-KEYRING-025
        @Test
        public void copy_decoupled() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring expectedKeyring = KeyringFactory.create(expectedAddress, expectedPrivateKey);
            AbstractKeyring actualKeyring = expectedKeyring.copy();

            checkValidateSingleKey((SingleKeyring) actualKeyring, expectedAddress, expectedPrivateKey);
        }

        //CA-KEYRING-026
        @Test
        public void copy_multipleKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[] expectedAddressKeys = new String[] {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            AbstractKeyring expectedKeyring = KeyringFactory.createWithMultipleKey(expectedAddress, expectedAddressKeys);
            AbstractKeyring actualKeyring = expectedKeyring.copy();

            checkValidateMultipleKey((MultipleKeyring) actualKeyring, expectedAddress, expectedAddressKeys);
        }

        //CA-KEYRING-027
        @Test
        public void copy_roleBasedKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[][] expectedPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                            PrivateKey.generate().getPrivateKey()
                    }
            };
            List<String[]> expectedKeyList = Arrays.asList(expectedPrivateKeyArr);

            AbstractKeyring expectedKeyring = KeyringFactory.createWithRoleBasedKey(expectedAddress, expectedKeyList);
            AbstractKeyring actualKeyring = expectedKeyring.copy();

            checkValidateRoleBasedKey((RoleBasedKeyring) actualKeyring, expectedAddress, expectedKeyList);
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static final String HASH = "0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550";
        static final int CHAIN_ID = 1;

        //CA-KEYRING-028
        @Test
        public void coupleKey(){
            AbstractKeyring keyring = KeyringFactory.generate();
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        //CA-KEYRING-029
        @Test
        public void coupledKey_with_NotExistedRole(){
            AbstractKeyring keyring = KeyringFactory.generate();

            SignatureData expectedSignatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(expectedSignatureData.getR(), signatureData.getR());
            assertEquals(expectedSignatureData.getS(), signatureData.getS());
            assertEquals(expectedSignatureData.getV(), signatureData.getV());
        }

        //CA-KEYRING-030
        @Test
        public void coupleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");
            AbstractKeyring keyring = KeyringFactory.generate();
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        //CA-KEYRING-031
        @Test
        public void coupleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");
            AbstractKeyring keyring = KeyringFactory.generate();
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);
        }

        //CA-KEYRING-032
        @Test
        public void deCoupleKey() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        //CA-KEYRING-033
        @Test
        public void deCoupleKey_With_NotExistedRole() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            SignatureData expectedSignatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(expectedSignatureData.getR(), signatureData.getR());
            assertEquals(expectedSignatureData.getS(), signatureData.getS());
            assertEquals(expectedSignatureData.getV(), signatureData.getV());
        }

        //CA-KEYRING-034
        @Test
        public void deCoupleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        //CA-KEYRING-035
        @Test
        public void deCoupleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);
        }

        //CA-KEYRING-036
        @Test
        public void multipleKey() {
            AbstractKeyring keyring = generateMultipleKeyring(3);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        //CA-KEYRING-037
        @Test
        public void multipleKey_With_NotExistedRole() {
            AbstractKeyring keyring = generateMultipleKeyring(3);
            SignatureData expectedSignatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(expectedSignatureData.getR(), signatureData.getR());
            assertEquals(expectedSignatureData.getS(), signatureData.getS());
            assertEquals(expectedSignatureData.getV(), signatureData.getV());
        }

        //CA-KEYRING-038
        @Test
        public void multipleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");

            AbstractKeyring keyring = generateMultipleKeyring(3);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), -1);
        }

        //CA-KEYRING-039
        @Test
        public void multipleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");
            AbstractKeyring keyring = generateMultipleKeyring(3);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 10);
        }

        //CA-KEYRING-040
        @Test
        public void roleBasedKey() {
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{2,3,4});

            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        //CA-KEYRING-041
        @Test
        public void roleBasedKey_With_NotExistedRole() {
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});

            SignatureData expectedSignatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(expectedSignatureData.getR(), signatureData.getR());
            assertEquals(expectedSignatureData.getS(), signatureData.getS());
            assertEquals(expectedSignatureData.getV(), signatureData.getV());
        }

        //CA-KEYRING-042
        @Test
        public void roleBasedKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        //CA-KEYRING-043
        @Test
        public void roleBasedKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});
            SignatureData signatureData = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 10);
        }
    }

    public static class signWithKeysTest {
        static final String HASH = "0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550";
        static final int CHAIN_ID = 1;

        public void checkSignature(List<SignatureData> expected, List<SignatureData> actual) {
            assertEquals(expected.size(), actual.size());

            for(int i=0; i<expected.size(); i++) {
                assertEquals(expected.get(i).getR(), actual.get(i).getR());
                assertEquals(expected.get(i).getS(), actual.get(i).getS());
                assertEquals(expected.get(i).getV(), actual.get(i).getV());
            }
        }

        //CA-KEYRING-044
        @Test
        public void coupleKey() {
            AbstractKeyring keyring = KeyringFactory.generate();
            List<SignatureData> signatureDataList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            assertEquals(1, signatureDataList.size());
            assertNotNull(signatureDataList.get(0).getR());
            assertNotNull(signatureDataList.get(0).getS());
            assertNotNull(signatureDataList.get(0).getV());
        }

        //CA-KEYRING-045
        @Test
        public void coupleKey_With_NotExistedRole() {
            AbstractKeyring keyring = KeyringFactory.generate();
            List<SignatureData> expectedList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(1, actualList.size());
            assertNotNull(actualList.get(0).getR());
            assertNotNull(actualList.get(0).getS());
            assertNotNull(actualList.get(0).getV());

            checkSignature(expectedList, actualList);
        }

        //CA-KEYRING-046
        @Test
        public void deCoupleKey() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(1, actualList.size());
            assertNotNull(actualList.get(0).getR());
            assertNotNull(actualList.get(0).getS());
            assertNotNull(actualList.get(0).getV());
        }

        //CA-KEYRING-047
        @Test
        public void deCoupleKey_With_NotExistedRole() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            AbstractKeyring keyring = KeyringFactory.create(address, privateKey);

            List<SignatureData> expectedList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(1, actualList.size());
            checkSignature(expectedList, actualList);
        }

        //CA-KEYRING-048
        @Test
        public void multipleKey() {
            AbstractKeyring keyring = generateMultipleKeyring(3);

            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(3, actualList.size());

            for(int i=0; i<actualList.size(); i++) {
                assertNotNull(actualList.get(0).getV());
                assertNotNull(actualList.get(0).getR());
                assertNotNull(actualList.get(0).getS());
            }
        }

        //CA-KEYRING-049
        @Test
        public void multipleKey_With_NotExistedRole() {
            AbstractKeyring keyring = generateMultipleKeyring(3);

            List<SignatureData> expectedList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(3, actualList.size());
            checkSignature(expectedList, actualList);
        }

        //CA-KEYRING-050
        @Test
        public void roleBasedKey() {
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3,3,4});

            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(3, actualList.size());

            for(int i=0; i<actualList.size(); i++) {
                assertNotNull(actualList.get(0).getV());
                assertNotNull(actualList.get(0).getR());
                assertNotNull(actualList.get(0).getS());
            }
        }

        //CA-KEYRING-051
        @Test
        public void roleBasedKey_With_NotExistedRole() {
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3,0,4});

            List<SignatureData> expectedList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<SignatureData> actualList = keyring.sign(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(3, actualList.size());
            checkSignature(expectedList, actualList);
        }
    }

    public static class signMessageTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String data = "some data";

        //CA-KEYRING-052
        @Test
        public void coupledKey_NoIndex() {
            AbstractKeyring keyring = KeyringFactory.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data, 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-053
        @Test
        public void coupleKey_WithIndex() {
            AbstractKeyring keyring = KeyringFactory.generate();
            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.hashMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatures().get(0).getV());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getS());
        }

        //CA-KEYRING-054
        @Test
        public void coupleKey_NotExistedRoleIndex() {
            AbstractKeyring keyring = KeyringFactory.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-055
        @Test
        public void coupleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            SingleKeyring keyring = KeyringFactory.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 3);
        }

        //CA-KEYRING-056
        @Test
        public void coupleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");

            SingleKeyring keyring = KeyringFactory.generate();
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

        //CA-KEYRING-057
        @Test
        public void decoupledKey_NoIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring decoupled = KeyringFactory.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 0);
            MessageSigned actual = decoupled.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-058
        @Test
        public void decoupledKey_WithIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring decoupled = KeyringFactory.create(address, privateKey);

            MessageSigned actual = decoupled.signMessage(data, 0, 0);

            assertEquals(Utils.hashMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatures().get(0).getV());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getS());
        }


        //CA-KEYRING-059
        @Test
        public void decoupleKey_NotExistedRoleIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring decoupled = KeyringFactory.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 0);
            MessageSigned actual = decoupled.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }


        //CA-KEYRING-060
        @Test
        public void decoupleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring decoupled = KeyringFactory.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 3);
        }

        //CA-KEYRING-061
        @Test
        public void decoupleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");

            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring decoupled = KeyringFactory.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, -1);
        }

        //CA-KEYRING-062
        @Test
        public void multipleKey_NoIndex() {
            MultipleKeyring keyring = generateMultipleKeyring(3);

            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-063
        @Test
        public void multipleKey_WithIndex() {
            MultipleKeyring keyring = generateMultipleKeyring(3);

            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.hashMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getS());
        }

        //CA-KEYRING-064
        @Test
        public void multipleKey_NotExistedRoleIndex() {
            MultipleKeyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, 2);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 2);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-065
        @Test
        public void multipleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            MultipleKeyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, 6);
        }

        //CA-KEYRING-066
        @Test
        public void multipleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");

            MultipleKeyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

        //CA-KEYRING-068
        @Test
        public void roleBasedKey_WithIndex() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.hashMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getR());
            assertNotNull(actual.getSignatures().get(0).getS());
        }

        //CA-KEYRING-069
        @Test
        public void roleBasedKey_NotExistedRoleKey() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {3,0,5});

            MessageSigned expect = keyring.signMessage(data, 0, 2);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 2);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(expect.getSignatures().get(0).getR(), actual.getSignatures().get(0).getR());
            assertEquals(expect.getSignatures().get(0).getS(), actual.getSignatures().get(0).getS());
            assertEquals(expect.getSignatures().get(0).getV(), actual.getSignatures().get(0).getV());
        }

        //CA-KEYRING-070
        @Test
        public void roleBasedKey_throwException_WithInvalidKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            MessageSigned expect = keyring.signMessage(data, 0, 8);
        }

        //CA-KEYRING-071
        @Test
        public void roleBasedKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index cannot be negative");

            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

//        //CA-KEYRING-072
//        @Test
//        public void roleBasedKey_throwException_NoIndex_WithNoDefaultKey() {
//            expectedException.expect(RuntimeException.class);
//            expectedException.expectMessage("Default Key does not have enough keys to sign.");
//
//            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {0,4,5});
//            MessageSigned expect = keyring.signMessage(data);
//        }
    }

    public static class recoverTest {
        public void checkAddress(String expect, String actual) {
            expect = Numeric.prependHexPrefix(expect);
            actual = Numeric.prependHexPrefix(actual);

            assertEquals(expect, actual);
        }

        //CA-KEYRING-073
//        @Test
//        public void withSignedMessage() throws SignatureException {
//            SingleKeyring keyring = KeyringFactory.generate();
//            String message = "Some data";
//            MessageSigned signed = keyring.signMessage(message, 0, 0);
//
//            String actualAddr = Utils.recover(signed);
//            checkAddress(keyring.getAddress(), actualAddr);
//        }

        //CA-KEYRING-074
        @Test
        public void withMessageAndSignature() throws SignatureException {
            SingleKeyring keyring = KeyringFactory.generate();
            String message = "Some data";

            MessageSigned signed = keyring.signMessage(message, 0, 0);
            String actualAddr = Utils.recover(signed.getMessage(), signed.getSignatures().get(0));

            checkAddress(keyring.getAddress(), actualAddr);
        }

        //CA-KEYRING-075
        @Test
        public void alreadyPrefix() throws SignatureException {
            SingleKeyring keyring = KeyringFactory.generate();
            String message = "Some data";

            MessageSigned signed = keyring.signMessage(message, 0, 0);
            String actualAddr = Utils.recover(signed.getMessageHash(), signed.getSignatures().get(0), true);

            checkAddress(keyring.getAddress(), actualAddr);
        }
    }

    public static class decryptTest {

        String jsonV3 = "{\n" +
                "  \"version\":3,\n" +
                "  \"id\":\"7a0a8557-22a5-4c90-b554-d6f3b13783ea\",\n" +
                "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
                "  \"crypto\":{\n" +
                "    \"ciphertext\":\"696d0e8e8bd21ff1f82f7c87b6964f0f17f8bfbd52141069b59f084555f277b7\",\n" +
                "    \"cipherparams\":{\"iv\":\"1fd13e0524fa1095c5f80627f1d24cbd\"},\n" +
                "    \"cipher\":\"aes-128-ctr\",\n" +
                "    \"kdf\":\"scrypt\",\n" +
                "    \"kdfparams\":{\n" +
                "      \"dklen\":32,\n" +
                "      \"salt\":\"7ee980925cef6a60553cda3e91cb8e3c62733f64579f633d0f86ce050c151e26\",\n" +
                "      \"n\":4096,\n" +
                "      \"r\":8,\n" +
                "      \"p\":1\n" +
                "    },\n" +
                "    \"mac\":\"8684d8dc4bf17318cd46c85dbd9a9ec5d9b290e04d78d4f6b5be9c413ff30ea4\"\n" +
                "  }\n" +
                "}";

        String jsonV4 ="{\n" +
                "  \"version\":4,\n" +
                "  \"id\":\"55da3f9c-6444-4fc1-abfa-f2eabfc57501\",\n" +
                "  \"address\":\"0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2\",\n" +
                "  \"keyring\":[\n" +
                "    [\n" +
                "      {\n" +
                "        \"ciphertext\":\"93dd2c777abd9b80a0be8e1eb9739cbf27c127621a5d3f81e7779e47d3bb22f6\",\n" +
                "        \"cipherparams\":{\"iv\":\"84f90907f3f54f53d19cbd6ae1496b86\"},\n" +
                "        \"cipher\":\"aes-128-ctr\",\n" +
                "        \"kdf\":\"scrypt\",\n" +
                "        \"kdfparams\":{\n" +
                "          \"dklen\":32,\n" +
                "          \"salt\":\"69bf176a136c67a39d131912fb1e0ada4be0ed9f882448e1557b5c4233006e10\",\n" +
                "          \"n\":4096,\n" +
                "          \"r\":8,\n" +
                "          \"p\":1\n" +
                "        },\n" +
                "        \"mac\":\"8f6d1d234f4a87162cf3de0c7fb1d4a8421cd8f5a97b86b1a8e576ffc1eb52d2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"ciphertext\":\"53d50b4e86b550b26919d9b8cea762cd3c637dfe4f2a0f18995d3401ead839a6\",\n" +
                "        \"cipherparams\":{\"iv\":\"d7a6f63558996a9f99e7daabd289aa2c\"},\n" +
                "        \"cipher\":\"aes-128-ctr\",\n" +
                "        \"kdf\":\"scrypt\",\n" +
                "        \"kdfparams\":{\n" +
                "          \"dklen\":32,\n" +
                "          \"salt\":\"966116898d90c3e53ea09e4850a71e16df9533c1f9e1b2e1a9edec781e1ad44f\",\n" +
                "          \"n\":4096,\n" +
                "          \"r\":8,\n" +
                "          \"p\":1\n" +
                "        },\n" +
                "        \"mac\":\"bca7125e17565c672a110ace9a25755847d42b81aa7df4bb8f5ce01ef7213295\"\n" +
                "      }\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"ciphertext\":\"f16def98a70bb2dae053f791882f3254c66d63416633b8d91c2848893e7876ce\",\n" +
                "        \"cipherparams\":{\"iv\":\"f5006128a4c53bc02cada64d095c15cf\"},\n" +
                "        \"cipher\":\"aes-128-ctr\",\n" +
                "        \"kdf\":\"scrypt\",\n" +
                "        \"kdfparams\":{\n" +
                "          \"dklen\":32,\n" +
                "          \"salt\":\"0d8a2f71f79c4880e43ff0795f6841a24cb18838b3ca8ecaeb0cda72da9a72ce\",\n" +
                "          \"n\":4096,\n" +
                "          \"r\":8,\n" +
                "          \"p\":1\n" +
                "        },\n" +
                "        \"mac\":\"38b79276c3805b9d2ff5fbabf1b9d4ead295151b95401c1e54aed782502fc90a\"\n" +
                "      }\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"ciphertext\":\"544dbcc327942a6a52ad6a7d537e4459506afc700a6da4e8edebd62fb3dd55ee\",\n" +
                "        \"cipherparams\":{\"iv\":\"05dd5d25ad6426e026818b6fa9b25818\"},\n" +
                "        \"cipher\":\"aes-128-ctr\",\n" +
                "        \"kdf\":\"scrypt\",\n" +
                "        \"kdfparams\":{\n" +
                "          \"dklen\":32,\n" +
                "          \"salt\":\"3a9003c1527f65c772c54c6056a38b0048c2e2d58dc0e584a1d867f2039a25aa\",\n" +
                "          \"n\":4096,\n" +
                "          \"r\":8,\n" +
                "          \"p\":1\n" +
                "        },\n" +
                "        \"mac\":\"19a698b51409cc9ac22d63d329b1201af3c89a04a1faea3111eec4ca97f2e00f\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"ciphertext\":\"dd6b920f02cbcf5998ed205f8867ddbd9b6b088add8dfe1774a9fda29ff3920b\",\n" +
                "        \"cipherparams\":{\"iv\":\"ac04c0f4559dad80dc86c975d1ef7067\"},\n" +
                "        \"cipher\":\"aes-128-ctr\",\n" +
                "        \"kdf\":\"scrypt\",\n" +
                "        \"kdfparams\":{\n" +
                "          \"dklen\":32,\n" +
                "          \"salt\":\"22279c6dbcc706d7daa120022a236cfe149496dca8232b0f8159d1df999569d6\",\n" +
                "          \"n\":4096,\n" +
                "          \"r\":8,\n" +
                "          \"p\":1\n" +
                "        },\n" +
                "        \"mac\":\"1c54f7378fa279a49a2f790a0adb683defad8535a21bdf2f3dadc48a7bddf517\"\n" +
                "      }\n" +
                "    ]\n" +
                "  ]\n" +
                "}";

        //CA-KEYRING-076
        @Test
        public void coupleKey() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            SingleKeyring expect = KeyringFactory.createFromPrivateKey(privateKey);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = expect.encrypt(password, option);
            AbstractKeyring actual = KeyringFactory.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-077
        @Test
        public void deCoupleKey() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            SingleKeyring expect = KeyringFactory.create(address, privateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = expect.encrypt(password, option);
            AbstractKeyring actual = KeyringFactory.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-078
        @Test
        public void multipleKey() throws CipherException {
            String password = "password";
            MultipleKeyring expect = generateMultipleKeyring(3);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = expect.encrypt(password, option);
            AbstractKeyring actual = KeyringFactory.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-079
        @Test
        public void roleBasedKey() throws CipherException {
            String password = "password";
            RoleBasedKeyring expect = generateRoleBaseKeyring(new int[]{3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = expect.encrypt(password, option);
            AbstractKeyring actual = KeyringFactory.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-080
        @Test
        public void roleBasedKey_withEmptyRole() throws CipherException {
            String password = "password";
            RoleBasedKeyring expect = generateRoleBaseKeyring(new int[]{3,0,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = expect.encrypt(password, option);
            AbstractKeyring actual = KeyringFactory.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-081
        @Test
        public void jsonStringV4() throws IOException, CipherException {
            String password = "password";
            String expectedAddress = "0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2";
            String[][] expectedPrivateKeys = new String[][] {
                    {
                        "0xd1e9f8f00ef9f93365f5eabccccb3f3c5783001b61a40f0f74270e50158c163d",
                        "0x4bd8d0b0c1575a7a35915f9af3ef8beb11ad571337ec9b6aca7c88ca7458ef5c",
                    },
                    {
                        "0xdc2690ac6017e32ef17ea219c2a2fd14a2bb73e7a0a253dfd69abba3eb8d7d91",
                    },
                    {
                        "0xf17bf8b7bee09ffc50a401b7ba8e633b9e55eedcf776782f2a55cf7cc5c40aa8",
                        "0x4f8f1e9e1466609b836dba611a0a24628aea8ee11265f757aa346bde3d88d548",
                    }
            };

            AbstractKeyring expect = KeyringFactory.createWithRoleBasedKey(expectedAddress, Arrays.asList(expectedPrivateKeys));
            AbstractKeyring actual = KeyringFactory.decrypt(jsonV4, password);

            checkValidKeyring(expect, actual);
        }

        //CA-KEYRING-082
        @Test
        public void jsonStringV3() throws IOException, CipherException {
            String password = "password";
            AbstractKeyring expect = KeyringFactory.createWithSingleKey("0x86bce8c859f5f304aa30adb89f2f7b6ee5a0d6e2",
                                                        "0x36e0a792553f94a7660e5484cfc8367e7d56a383261175b9abced7416a5d87df");

            AbstractKeyring actual = KeyringFactory.decrypt(jsonV3, password);
            checkValidKeyring(expect, actual);
        }

    }



    public static class encryptTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkValidateKeyStore(KeyStore actualData, String password, AbstractKeyring expectedKeyring, int version) throws CipherException{
            assertEquals(expectedKeyring.getAddress(), actualData.getAddress());

            if(actualData.getVersion() == 4) {
                assertNotNull(actualData.getKeyring());
                assertNull(actualData.getCrypto());
            }

            if(expectedKeyring instanceof RoleBasedKeyring) {
                assertTrue(actualData.getKeyring().get(0) instanceof List);
            } else {
                assertTrue(actualData.getKeyring().get(0) instanceof KeyStore.Crypto);
            }

            AbstractKeyring actualKeyring = KeyringFactory.decrypt(actualData, password);

            checkValidKeyring(expectedKeyring, actualKeyring);
        }

        //CA-KEYRING-083
        @Test
        public void keyStoreV4_scrypt() throws CipherException {
            String password = "password";
            SingleKeyring keyring = KeyringFactory.generate();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());

            KeyStore store = keyring.encrypt(password, option);
            checkValidateKeyStore(store, password, keyring, 4);
        }

        //CA-KEYRING-084
        @Test
        public void keyStoreV4_pbkdf2() throws CipherException {
            String password = "password";
            SingleKeyring keyring = KeyringFactory.generate();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore store = keyring.encrypt(password, option);
            checkValidateKeyStore(store, password, keyring, 4);
        }

        //CA-KEYRING-085
//        @Test
//        public void singleKeyStringWithCouple() throws CipherException {
//            String password = "password";
//            String privateKey = PrivateKey.generate().getPrivateKey();
//            SingleKeyring expectedKeyring = KeyringFactory.createFromPrivateKey(privateKey);
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());
//            KeyStore store = Keyring.encrypt(privateKey, password, option);
//
//            checkValidateKeyStore(store, password, expectedKeyring, 4);
//        }

        //CA-KEYRING-086
//        @Test
//        public void singleKeyStringWithDecoupled() throws CipherException{
//            String password = "password";
//            String privateKey = PrivateKey.generate().getPrivateKey();
//            String address = PrivateKey.generate().getDerivedAddress();
//            Keyring expectedKeyring = Keyring.create(address, privateKey);
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);
//
//            KeyStore store = Keyring.encrypt(privateKey, password, option);
//            checkValidateKeyStore(store, password, expectedKeyring, 4);
//        }

        //CA-KEYRING-087
//        @Test
//        public void klaytnWalletKey() throws CipherException{
//            String password = "password";
//            String privateKey = PrivateKey.generate().getPrivateKey();
//            String address = PrivateKey.generate().getDerivedAddress();
//            Keyring expectedKeyring = Keyring.create(address, privateKey);
//
//            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedKeyring.getAddress());
//
//            KeyStore store = Keyring.encrypt(klaytnWalletKey, password, option);
//            checkValidateKeyStore(store, password, expectedKeyring, 4);
//        }

        //CA-KEYRING-088
//        @Test
//        public void klaytnWalletKey_throwException_InvalidAddress() throws CipherException {
//            expectedException.expect(RuntimeException.class);
//            expectedException.expectMessage("The address defined in options does not match the address of KlaytnWalletKey");
//
//            String password = "password";
//            Keyring expectedKeyring = Keyring.generate();
//            String invalidAddress = Keyring.generate().getAddress();
//
//            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), invalidAddress);
//
//            KeyStore store = Keyring.encrypt(klaytnWalletKey, password, option);
//        }

        //CA-KEYRING-089
//        @Test
//        public void multipleKeyString() throws CipherException{
//            String password = "password";
//            String address = Keyring.generate().getAddress();
//
//            String[] privateKeyArr = new String[] {
//                    PrivateKey.generate().getPrivateKey(),
//                    PrivateKey.generate().getPrivateKey(),
//                    PrivateKey.generate().getPrivateKey()
//            };
//            Keyring expect = Keyring.create(address, privateKeyArr);
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);
//
//            KeyStore keyStore = Keyring.encrypt(privateKeyArr, password, option);
//            checkValidateKeyStore(keyStore, password, expect,4);
//        }

        //CA-KEYRING-090
//        @Test
//        public void multipleKeyString_throwException_NoAddress() throws CipherException {
//            expectedException.expect(IllegalArgumentException.class);
//            expectedException.expectMessage("The address must be defined inside the option object to encrypt multiple keys.");
//
//            String password = "password";
//            String address = Keyring.generate().getAddress();
//
//            String[] privateKeyArr = new String[] {
//                    PrivateKey.generate().getPrivateKey(),
//                    PrivateKey.generate().getPrivateKey(),
//                    PrivateKey.generate().getPrivateKey()
//            };
//            Keyring expect = Keyring.create(address, privateKeyArr);
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), null);
//
//            KeyStore keyStore = Keyring.encrypt(privateKeyArr, password, option);
//        }

//        //CA-KEYRING-091
//        @Test
//        public void roleBasedKeyString() throws CipherException {
//            String password = "password";
//            String address = Keyring.generate().getAddress();
//            String[][] privateKeyArr = new String[][] {
//                    {
//                        PrivateKey.generate().getPrivateKey(),
//                        PrivateKey.generate().getPrivateKey(),
//                        PrivateKey.generate().getPrivateKey()
//                    },
//                    {
//                        PrivateKey.generate().getPrivateKey(),
//                        PrivateKey.generate().getPrivateKey(),
//                    },
//                    {
//                        PrivateKey.generate().getPrivateKey(),
//                        PrivateKey.generate().getPrivateKey(),
//                    }
//            };
//
//            Keyring expect = Keyring.create(address, Arrays.asList(privateKeyArr));
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);
//
//            KeyStore keyStore = Keyring.encrypt(Arrays.asList(privateKeyArr), password, option);
//            checkValidateKeyStore(keyStore, password, expect,4);
//        }

//        //CA-KEYRING-092
//        @Test
//        public void roleBasedKeyString_throwException_noAddress() throws CipherException {
//            expectedException.expect(IllegalArgumentException.class);
//            expectedException.expectMessage("The address must be defined inside the option object to encrypt roleBased keys.");
//
//            String password = "password";
//            String address = Keyring.generate().getAddress();
//            String[][] privateKeyArr = new String[][] {
//                    {
//                            PrivateKey.generate().getPrivateKey(),
//                            PrivateKey.generate().getPrivateKey(),
//                            PrivateKey.generate().getPrivateKey()
//                    },
//                    {
//                            PrivateKey.generate().getPrivateKey(),
//                            PrivateKey.generate().getPrivateKey(),
//                    },
//                    {
//                            PrivateKey.generate().getPrivateKey(),
//                            PrivateKey.generate().getPrivateKey(),
//                    }
//            };
//
//            Keyring expect = Keyring.create(address, Arrays.asList(privateKeyArr));
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), null);
//
//            KeyStore keyStore = Keyring.encrypt(Arrays.asList(privateKeyArr), password, option);
//        }

        //CA-KEYRING-093
        @Test
        public void keyring_single() throws CipherException {
            String password = "password";
            SingleKeyring keyring = KeyringFactory.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-094
        @Test
        public void keyring_multiple() throws CipherException {
            String password = "password";
            MultipleKeyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-095
        @Test
        public void keyring_roleBased() throws CipherException {
            String password = "password";
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-096
        @Test
        public void abstractKeyring_singleKey() throws CipherException {
            String password = "password";
            AbstractKeyring keyring = KeyringFactory.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-097
        @Test
        public void abstractKeyring_multipleKey() throws CipherException {
            String password = "password";
            AbstractKeyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-098
        @Test
        public void abstractKeyring_roleBasedKey() throws CipherException {
            String password = "password";
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void singleKeyring_noOptions() throws CipherException {
            String password = "password";
            SingleKeyring singleKeyring = KeyringFactory.generate();

            KeyStore keyStore = singleKeyring.encrypt(password);
            checkValidateKeyStore(keyStore, password, singleKeyring, 4);
        }

        @Test
        public void multipleKeyring_noOptions() throws CipherException {
            String password = "password";
            MultipleKeyring singleKeyring = generateMultipleKeyring(3);

            KeyStore keyStore = singleKeyring.encrypt(password);
            checkValidateKeyStore(keyStore, password, singleKeyring, 4);
        }

        @Test
        public void roleBasedKeyring_noOptions() throws CipherException {
            String password = "password";
            RoleBasedKeyring singleKeyring = generateRoleBaseKeyring(new int[] {3,4,5});

            KeyStore keyStore = singleKeyring.encrypt(password);
            checkValidateKeyStore(keyStore, password, singleKeyring, 4);
        }
    }

    public static class encryptV3Test {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkValidateKeyStore(KeyStore actualData, String password, AbstractKeyring expectedKeyring, int version) throws CipherException{
            assertEquals(expectedKeyring.getAddress(), actualData.getAddress());

            if(actualData.getVersion() == version) {
                assertNotNull(actualData.getCrypto());
                assertNull(actualData.getKeyring());
            }

            AbstractKeyring actualKeyring = KeyringFactory.decrypt(actualData, password);

            checkValidKeyring(expectedKeyring, actualKeyring);
        }

        //CA-KEYRING-099
//        @Test
//        public void coupleKeyString() throws CipherException {
//            String password = "password";
//            String privateKey = PrivateKey.generate().getPrivateKey();
//            AbstractKeyring expect = KeyringFactory.createFromPrivateKey(privateKey);
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());
//
//            KeyStore keyStore = KeyringFactory.encryptV3(privateKey, password, option);
//            checkValidateKeyStore(keyStore, password, expect, 3);
//        }

        //CA-KEYRING-100
//        @Test
//        public void deCoupledKeyString() throws CipherException {
//            String password = "password";
//
//            String expectedAddress = PrivateKey.generate().getDerivedAddress();
//            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();
//
//            AbstractKeyring expect = KeyringFactory.createWithSingleKey(expectedAddress, expectedPrivateKey);
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedAddress);
//
//            KeyStore keyStore = KeyringFactory.encryptV3(expectedPrivateKey, password, option);
//            checkValidateKeyStore(keyStore, password, expect, 3);
//        }

        //CA-KEYRING-101
//        @Test
//        public void klaytnWalletKey() throws CipherException {
//            String password = "password";
//            String privateKey = PrivateKey.generate().getPrivateKey();
//            String address = PrivateKey.generate().getDerivedAddress();
//            AbstractKeyring expectedKeyring = KeyringFactory.create(address, privateKey);
//
//            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
//
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedKeyring.getAddress());
//
//            KeyStore store = KeyringFactory.encryptV3(klaytnWalletKey, password, option);
//            checkValidateKeyStore(store, password, expectedKeyring, 3);
//        }

        //CA-KEYRING-102
//        @Test
//        public void klaytnWalletKey_throwException_InvalidAddress() throws CipherException {
//            expectedException.expect(RuntimeException.class);
//            expectedException.expectMessage("The address defined in options does not match the address of KlaytnWalletKey");
//
//            String password = "password";
//            AbstractKeyring expectedKeyring = KeyringFactory.generate();
//            String invalidAddress = KeyringFactory.generate().getAddress();
//
//            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
//            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), invalidAddress);
//
//            KeyStore store = KeyringFactory.encryptV3(klaytnWalletKey, password, option);
//        }

        //CA-KEYRING-103
        @Test
        public void keyring_single() throws CipherException {
            String password = "password";
            SingleKeyring keyring = KeyringFactory.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 3);
        }

        @Test
        public void keyring_single_noOption() throws CipherException {
            String password = "password";
            SingleKeyring keyring = KeyringFactory.generate();

            KeyStore keyStore = keyring.encryptV3(password);
            checkValidateKeyStore(keyStore, password, keyring, 3);
        }

        //CA-KEYRING-104
        @Test
        public void throwException_keyring_multiple() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class. Use 'encrypt()' function");
            String password = "password";
            MultipleKeyring keyring = generateMultipleKeyring(3);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());
            KeyStore keyStore = keyring.encryptV3(password, option);
        }

        //CA-KEYRING-105
        @Test
        public void throwException_keyring_roleBased() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class. Use 'encrypt()' function");
            String password = "password";
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {2,3,4});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());
            KeyStore keyStore = keyring.encryptV3(password, option);
        }

        //CA-KEYRING-106
        @Test
        public void abstractKeyring_singleKey() throws CipherException {
            String password = "password";
            AbstractKeyring keyring = KeyringFactory.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-106
        @Test
        public void abstractKeyring_singleKey_noOption() throws CipherException {
            String password = "password";
            AbstractKeyring keyring = KeyringFactory.generate();

            KeyStore keyStore = keyring.encryptV3(password);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        //CA-KEYRING-107
        @Test
        public void throwException_instanceMethod_multipleKey() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class. Use 'encrypt()' function");

            String password = "password";
            AbstractKeyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
        }

        //CA-KEYRING-108
        @Test
        public void throwException_instanceMethod_roleBasedKey() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class. Use 'encrypt()' function");

            String password = "password";
            AbstractKeyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
        }

    }

    public static class getKeyByRoleTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-109
        @Test
        public void getKeyByRole() {
            int[] count = {2,3,4};
            RoleBasedKeyring roleKeyring = generateRoleBaseKeyring(count);
            PrivateKey[] keys = roleKeyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            assertNotNull(keys);
            assertEquals(2, keys.length);
        }

        //CA-KEYRING-110
        @Test
        public void getKeyByRole_defaultKey() {
            MultipleKeyring keyring = generateMultipleKeyring(3);
            PrivateKey[] keys = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex());

            assertNotNull(keys);
            assertEquals(3, keys.length);
        }

        //CA-KEYRING-111
        @Test
        public void getKeyByRole_throwException_defaultKeyEmpty() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The Key with specified role group does not exists. The TRANSACTION role group is also empty");

            int[] count = {0, 0, 3};
            RoleBasedKeyring keyring = generateRoleBaseKeyring(count);

            PrivateKey[] keys = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());
        }

        //CA-KEYRING-112
        @Test
        public void getKeyByRole_throwException_invalidIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid role index");

            MultipleKeyring keyring = generateMultipleKeyring(4);
            PrivateKey[] keys = keyring.getKeyByRole(4);
        }
    }

    public static class getKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-KEYRING-113
        @Test
        public void getKlaytnWalletKey_coupled() {
            SingleKeyring keyring = KeyringFactory.generate();
            String expectedKeyStr = keyring.getKey().getPrivateKey() + "0x00" + keyring.getAddress();

            assertEquals(expectedKeyStr, keyring.getKlaytnWalletKey());
        }

        //CA-KEYRING-114
        @Test
        public void getKlaytnWalletKey_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            String expectedKeyStr = privateKey + "0x00" + Numeric.prependHexPrefix(address);

            assertEquals(expectedKeyStr, keyring.getKlaytnWalletKey());

            String actualKey = keyring.getKlaytnWalletKey();
        }

        //CA-KEYRING-115
        @Test
        public void getKlaytnWallet_throwException_multiKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class.");

            MultipleKeyring keyring = generateMultipleKeyring(3);
            String keyStr = keyring.getKlaytnWalletKey();
        }

        //CA-KEYRING-116
        @Test
        public void getKlaytnWallet_thrownException_roleBased() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Not supported for this class.");

            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[]{1,3,4});
            String keyStr = keyring.getKlaytnWalletKey();
        }
    }

    public static class getPublicKeyTest {

        //CA-KEYRING-117
        @Test
        public void getPublicKey_single() {
            SingleKeyring keyring = KeyringFactory.generate();
            String publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKey().getPublicKey(false), publicKeys);

        }

        //CA-KEYRING-118
        @Test
        public void getPublicKey_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);
            String publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKey().getPublicKey(false), publicKeys);
        }

        //CA-KEYRING-119
        @Test
        public void getPublicKey_multiple() {
            MultipleKeyring keyring = generateMultipleKeyring(2);
            String[] publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKeys()[0].getPublicKey(false), publicKeys[0]);
            assertEquals(keyring.getKeys()[1].getPublicKey(false), publicKeys[1]);

            assertEquals(2, publicKeys.length);
        }

        //CA-KEYRING-120
        @Test
        public void getPublicKey_roleBased() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {2, 3, 1});
            List<String[]> publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKeys().get(0)[0].getPublicKey(false), publicKeys.get(0)[0]);
            assertEquals(keyring.getKeys().get(0)[1].getPublicKey(false), publicKeys.get(0)[1]);

            assertEquals(keyring.getKeys().get(1)[0].getPublicKey(false), publicKeys.get(1)[0]);
            assertEquals(keyring.getKeys().get(1)[1].getPublicKey(false), publicKeys.get(1)[1]);
            assertEquals(keyring.getKeys().get(1)[2].getPublicKey(false), publicKeys.get(1)[2]);

            assertEquals(keyring.getKeys().get(2)[0].getPublicKey(false), publicKeys.get(2)[0]);

            assertEquals(2, publicKeys.get(0).length);
            assertEquals(3, publicKeys.get(1).length);
            assertEquals(1, publicKeys.get(2).length);
        }

        @Test
        public void getPublicKey_single_compressed() {
            SingleKeyring keyring = KeyringFactory.generate();
            String publicKeys = keyring.getPublicKey(true);

            assertEquals(keyring.getKey().getPublicKey(true), publicKeys);
        }

        @Test
        public void getPublicKey_multiple_compressed() {
            MultipleKeyring keyring = generateMultipleKeyring(2);
            String[] publicKeys = keyring.getPublicKey(true);

            assertEquals(keyring.getKeys()[0].getPublicKey(true), publicKeys[0]);
            assertEquals(keyring.getKeys()[1].getPublicKey(true), publicKeys[1]);
            assertEquals(2, publicKeys.length);
        }

        @Test
        public void getPublicKey_roleBased_compressed() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {2, 3, 1});
            List<String[]> publicKeys = keyring.getPublicKey(true);

            assertEquals(keyring.getKeys().get(0)[0].getPublicKey(true), publicKeys.get(0)[0]);
            assertEquals(keyring.getKeys().get(0)[1].getPublicKey(true), publicKeys.get(0)[1]);

            assertEquals(keyring.getKeys().get(1)[0].getPublicKey(true), publicKeys.get(1)[0]);
            assertEquals(keyring.getKeys().get(1)[1].getPublicKey(true), publicKeys.get(1)[1]);
            assertEquals(keyring.getKeys().get(1)[2].getPublicKey(true), publicKeys.get(1)[2]);

            assertEquals(keyring.getKeys().get(2)[0].getPublicKey(true), publicKeys.get(2)[0]);

            assertEquals(2, publicKeys.get(0).length);
            assertEquals(3, publicKeys.get(1).length);
            assertEquals(1, publicKeys.get(2).length);
        }
    }

    public static class isDecoupledTest {

        //CA-KEYRING-121
        @Test
        public void isDecoupled_coupled() {
            SingleKeyring keyring = KeyringFactory.generate();

            assertFalse(keyring.isDecoupled());
        }

        //CA-KEYRING-122
        @Test
        public void isDecoupled_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            SingleKeyring keyring = KeyringFactory.create(address, privateKey);

            assertTrue(keyring.isDecoupled());
        }

        //CA-KEYRING-123
        @Test
        public void isDecoupled_multiKey() {
            MultipleKeyring keyring = generateMultipleKeyring(3);
            assertTrue(keyring.isDecoupled());
        }

        //CA-KEYRING-124
        @Test
        public void isDecoupled_roleBased() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[]{2,3,1});
            assertTrue(keyring.isDecoupled());
        }
    }

    public static class toAccountTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkAccountKeyPublic(SingleKeyring keyring, Account account) {
            String expectedPublicKey = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()).getPublicKey(false);

            assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
            assertEquals(Numeric.prependHexPrefix(keyring.getAddress()), Numeric.prependHexPrefix(account.getAddress()));
            assertEquals(expectedPublicKey, ((AccountKeyPublic) account.getAccountKey()).getPublicKey());
        }

        public void checkAccountKeyWeightedMultiSig(MultipleKeyring keyring, Account account, WeightedMultiSigOptions options) {
            String[] expectedPublicKeys = keyring.getPublicKey();
            List<WeightedPublicKey> actualKeys = ((AccountKeyWeightedMultiSig) account.getAccountKey()).getWeightedPublicKeys();

            assertTrue(account.getAccountKey() instanceof AccountKeyWeightedMultiSig);
            assertEquals(Numeric.prependHexPrefix(keyring.getAddress()), Numeric.prependHexPrefix(account.getAddress()));
            assertEquals(options.getThreshold(), ((AccountKeyWeightedMultiSig) account.getAccountKey()).getThreshold());

            checkPublicKey(expectedPublicKeys, actualKeys, options);
        }

        public void checkPublicKey(String[] expectedPublicKey, List<WeightedPublicKey> actualKey, WeightedMultiSigOptions options) {
            for(int i=0; i<actualKey.size(); i++) {
                assertEquals(expectedPublicKey[i], actualKey.get(i).getPublicKey());
                assertEquals(options.getWeights().get(i), actualKey.get(i).getWeight());
            }
        }

        //CA-KEYRING-125
        @Test
        public void singleKeyTest() {
            SingleKeyring keyring = KeyringFactory.generate();
            Account account = keyring.toAccount();

            checkAccountKeyPublic(keyring, account);
        }

        //CA-KEYRING-126
        @Test
        public void toAccount_withMultipleType() {
            MultipleKeyring expectedKeyring = generateMultipleKeyring(3);

            BigInteger[] optionWeight = {
                    BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
            };

            WeightedMultiSigOptions expectedOptions = new WeightedMultiSigOptions(
                    BigInteger.ONE, Arrays.asList(optionWeight)
            );

            Account account = expectedKeyring.toAccount();
            checkAccountKeyWeightedMultiSig(expectedKeyring, account, expectedOptions);
        }

        //CA-KEYRING-127
        @Test
        public void toAccount_withRoleBasedType() {
            RoleBasedKeyring expectedKeyring = generateRoleBaseKeyring(new int[] {2, 1, 4});

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE},
            };

            WeightedMultiSigOptions[] expectedOption = {
                    new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[0])),
                    new WeightedMultiSigOptions(),
                    new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[2])),
            };
            List<String[]> expectedPublicKeys = expectedKeyring.getPublicKey();

            Account account = expectedKeyring.toAccount();

            IAccountKey key = account.getAccountKey();
            assertTrue(key instanceof  AccountKeyRoleBased);

            IAccountKey txRoleKey = ((AccountKeyRoleBased) key).getRoleTransactionKey();
            assertTrue(txRoleKey instanceof  AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(0), ((AccountKeyWeightedMultiSig) txRoleKey).getWeightedPublicKeys(), expectedOption[0]);

            IAccountKey accountRoleKey = ((AccountKeyRoleBased) key).getRoleAccountUpdateKey();
            assertTrue(accountRoleKey instanceof  AccountKeyPublic);
            assertEquals(expectedPublicKeys.get(1)[0], ((AccountKeyPublic) accountRoleKey).getPublicKey());

            IAccountKey feePayerKey = ((AccountKeyRoleBased) key).getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(0), ((AccountKeyWeightedMultiSig) txRoleKey).getWeightedPublicKeys(), expectedOption[0]);

        }

        //CA-KEYRING-128
        @Test
        public void multipleKeyTest() {
            MultipleKeyring keyring = generateMultipleKeyring(3);

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Account account = keyring.toAccount(options);
            checkAccountKeyWeightedMultiSig(keyring, account, options);
        }

        //CA-KEYRING-129
        @Test
        public void multipleKeyTest_throwException_noKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The count of public keys is not equal to the length of weight array.");

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            MultipleKeyring keyring = generateMultipleKeyring(0);
            Account account = keyring.toAccount(options);
        }

        //CA-KEYRING-130
        @Test
        public void multipleKeyTest_throwException_weightedOptionCount() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The count of public keys is not equal to the length of weight array.");

            MultipleKeyring keyring = generateMultipleKeyring(2);

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Account account = keyring.toAccount(options);
        }

        //CA-KEYRING-131
//        @Test
//        public void multipleKeyTest_throwException_roleBasedKey() {
//            expectedException.expect(RuntimeException.class);
//            expectedException.expectMessage("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");
//
//            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[]{3,3,4});
//
//            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
//            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));
//
//            Account account = keyring.toAccount(options);
//        }

        //CA-KEYRING-132
        @Test
        public void roleBasedKeyTest_SingleKey() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[]{1,1,1});
            List<String[]> expectedPublicKeys = keyring.getPublicKey();

            WeightedMultiSigOptions[] weightedMultiSigOptions = new WeightedMultiSigOptions[] {
                    new WeightedMultiSigOptions(),
                    new WeightedMultiSigOptions(),
                    new WeightedMultiSigOptions()};

            Account account = keyring.toAccount(Arrays.asList(weightedMultiSigOptions));

            AccountKeyRoleBased key = (AccountKeyRoleBased) account.getAccountKey();
            IAccountKey txRoleKey = key.getRoleTransactionKey();
            assertTrue(txRoleKey instanceof AccountKeyPublic);
            assertEquals(expectedPublicKeys.get(0)[0], ((AccountKeyPublic) txRoleKey).getPublicKey());

            IAccountKey accountRoleKey = key.getRoleAccountUpdateKey();
            assertTrue(accountRoleKey instanceof AccountKeyPublic);
            assertEquals(expectedPublicKeys.get(1)[0], ((AccountKeyPublic) accountRoleKey).getPublicKey());

            IAccountKey feePayerRoleKey = key.getRoleFeePayerKey();
            assertTrue(feePayerRoleKey instanceof AccountKeyPublic);
            assertEquals(expectedPublicKeys.get(2)[0], ((AccountKeyPublic) feePayerRoleKey).getPublicKey());
        }

        //CA-KEYRING-133
        @Test
        public void roleBaseKeyTest_multipleKey() {
            RoleBasedKeyring keyring = generateRoleBaseKeyring(new int[] {2,3,4});
            List<String[]> expectedPublicKeys = keyring.getPublicKey();

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };

            WeightedMultiSigOptions[] options = {
                    new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                    new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[1])),
                    new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[2])),
            };

            Account account = keyring.toAccount(Arrays.asList(options));
            AccountKeyRoleBased key = (AccountKeyRoleBased) account.getAccountKey();

            IAccountKey txRoleKey = key.getRoleTransactionKey();
            assertTrue(txRoleKey instanceof AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(0), ((AccountKeyWeightedMultiSig) txRoleKey).getWeightedPublicKeys(), options[0]);

            IAccountKey accountRoleKey = key.getRoleAccountUpdateKey();
            assertTrue(accountRoleKey instanceof AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(1), ((AccountKeyWeightedMultiSig) accountRoleKey).getWeightedPublicKeys(), options[1]);

            IAccountKey feePayerRoleKey = key.getRoleFeePayerKey();
            assertTrue(feePayerRoleKey instanceof AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(2), ((AccountKeyWeightedMultiSig) feePayerRoleKey).getWeightedPublicKeys(), options[2]);
        }

        //CA-KEYRING-134
        @Test
        public void roleBasedKeyTest_combined() {
            String address = PrivateKey.generate().getDerivedAddress();
            String[][] expectedPrivateKeyArr = {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                    },
                    {
                    },
                    {
                            PrivateKey.generate().getPrivateKey()
                    }
            };

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };

            WeightedMultiSigOptions[] options = {
                    new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                    new WeightedMultiSigOptions(),
                    new WeightedMultiSigOptions(),
            };

            RoleBasedKeyring keyring = KeyringFactory.createWithRoleBasedKey(address, Arrays.asList(expectedPrivateKeyArr));
            List<String[]> expectedPublicKeys = keyring.getPublicKey();
            Account account = keyring.toAccount(Arrays.asList(options));

            AccountKeyRoleBased key = (AccountKeyRoleBased) account.getAccountKey();

            IAccountKey txRoleKey = key.getRoleTransactionKey();
            assertTrue(txRoleKey instanceof AccountKeyWeightedMultiSig);
            checkPublicKey(expectedPublicKeys.get(0), ((AccountKeyWeightedMultiSig) txRoleKey).getWeightedPublicKeys(), options[0]);

            IAccountKey accountRoleKey = key.getRoleAccountUpdateKey();
            assertTrue(accountRoleKey instanceof AccountKeyNil);

            IAccountKey feePayerRoleKey = key.getRoleFeePayerKey();
            assertTrue(feePayerRoleKey instanceof AccountKeyPublic);
            assertEquals(expectedPublicKeys.get(2)[0], ((AccountKeyPublic) feePayerRoleKey).getPublicKey());
        }
    }
}
