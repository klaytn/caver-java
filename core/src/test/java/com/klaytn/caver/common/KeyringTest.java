package com.klaytn.caver.common;

import com.klaytn.caver.account.*;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.WalletFile;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.crypto.CipherException;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({KeyringTest.GenerateTest.class,
        KeyringTest.CreateFromPrivateKeyTest.class,
        KeyringTest.CreateFromKlaytnWalletKeyTest.class,
        KeyringTest.CreateTest.class,
        KeyringTest.CreateWithSingleKeyTest.class,
        KeyringTest.CreateWithMultipleKeyTest.class,
        KeyringTest.CreateWithRoleBasedKeyTest.class,})
public class KeyringTest {

    public static void checkValidateSingleKey(Keyring actualKeyring, String expectedAddress, String expectedPrivateKey) {
        assertTrue(Utils.isValidAddress(actualKeyring.getAddress()));
        assertEquals(expectedAddress, actualKeyring.getAddress());

        PrivateKey actualPrivateKey = actualKeyring.getKeys().get(0)[0];
        assertTrue(Utils.isPrivateKeyValid(actualPrivateKey.getPrivateKey()));
        assertEquals(expectedPrivateKey, actualPrivateKey.getPrivateKey());
    }

    public static void checkValidateMultipleKey(Keyring actualKeyring, String expectedAddress, String[] expectedPrivateKeyArr) {
        assertTrue(Utils.isValidAddress(actualKeyring.getAddress()));
        assertEquals(expectedAddress, actualKeyring.getAddress());

        PrivateKey[] actualPrivateKeyArr = actualKeyring.getKeys().get(0);
        assertEquals(expectedPrivateKeyArr.length, actualPrivateKeyArr.length);

        for(int i=0; i<actualPrivateKeyArr.length; i++) {
            assertEquals(expectedPrivateKeyArr[i], actualPrivateKeyArr[i].getPrivateKey());
        }

    }

    public static void checkValidateRoleBasedKey(Keyring actualKeyring, String expectedAddress, List<String[]> expectedPrivateKeyList) {
        assertTrue(Utils.isValidAddress(actualKeyring.getAddress()));
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

    public static Keyring generateMultipleKeyring(int num) {
        String[] keyArr = new String[num];

        for(int i=0; i<num; i++) {
            keyArr[i] = PrivateKey.generate("entropy").getPrivateKey();
        }

        String address = PrivateKey.generate("entropy").getDerivedAddress();
        return Keyring.createWithMultipleKey(address, keyArr);
    }

    public static Keyring generateRoleBaseKeyring(int[] numArr) {
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

        return Keyring.createWithRoleBasedKey(address, arr);
    }

    public static class GenerateTest {
        @Test
        public void generate() {
            Keyring keyring = Keyring.generate();
            assertTrue(Utils.isValidAddress(keyring.getAddress()));
        }

        @Test
        public void generateWithEntropy() {
            byte[] random = Utils.generateRandomBytes(32);
            Keyring keyring = Keyring.generate(Numeric.toHexString(random));

            assertTrue(Utils.isValidAddress(keyring.getAddress()));
        }
    }

    public static class CreateFromPrivateKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createFromPrivateKey() {
            Keyring keyring = Keyring.generate();
            String expectedAddress = keyring.getAddress();
            String expectedPrivateKey = keyring.getKeys().get(0)[0].getPrivateKey();

            Keyring actualKeyring = Keyring.createFromPrivateKey(expectedPrivateKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createFromPrivateKeyWithoutHexPrefix() {
            Keyring keyring = Keyring.generate();
            String expectedAddress = keyring.getAddress();
            String expectedPrivateKey = keyring.getKeys().get(0)[0].getPrivateKey();

            Keyring actualKeyring = Keyring.createFromPrivateKey(Numeric.cleanHexPrefix(expectedPrivateKey));
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createFromPrivateFromKlaytnWalletKey() {
            String klaytnWalletKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d80x000xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";
            String expectedPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
            String expectedAddress = "0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";

            Keyring actualKeyring = Keyring.createFromPrivateKey(klaytnWalletKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createFromPrivate_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid private key.");

            byte[] random = Utils.generateRandomBytes(31);
            Keyring keyring = Keyring.createFromPrivateKey(Numeric.toHexString(random));
        }
    }


    public static class CreateFromKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createFromKlaytnWalletKey() {
            String klaytnWalletKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d80x000xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";
            String expectedPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
            String expectedAddress = "0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";

            Keyring actualKeyring = Keyring.createFromKlaytnWalletKey(klaytnWalletKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createFromKlaytnWalletKey_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalidWalletKey = "39d87f15c695ec94d6d7107b48dee85e252f21fedd371e1c6baefbdf0x000x658b7b7a94ac398a8e7275e719a10c";
            Keyring actualKeyring = Keyring.createFromKlaytnWalletKey(invalidWalletKey);
        }
    }

    public static class CreateTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void create_SingleKey() {
            PrivateKey expectedPrivateKey = PrivateKey.generate();

            Keyring actualKeyring = Keyring.create(expectedPrivateKey.getDerivedAddress(), expectedPrivateKey.getPrivateKey());
            checkValidateSingleKey(actualKeyring, expectedPrivateKey.getDerivedAddress(), expectedPrivateKey.getPrivateKey());
        }

        @Test
        public void create_MultiPleKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[] privateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            Keyring actualKeyring = Keyring.create(expectedAddress, privateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, privateKeyArr);
        }

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

            Keyring actualKeyring = Keyring.create(expectedAddress, privateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, privateKeyArr);
        }

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
            Keyring actualKeyring = Keyring.create(expectedAddress, expectedKeyList);
            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }


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
            Keyring actualKeyring = Keyring.create(expectedAddress, expectedKeyList);
            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }

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
            Keyring actualKeyring = Keyring.create(expectedAddress, expectedKeyList);
        }

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
            Keyring actualKeyring = Keyring.create(expectedAddress, expectedKeyList);
        }
    }

    public static class CreateWithSingleKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createWithSingleKey_coupled() {
            PrivateKey key = PrivateKey.generate();
            String expectedAddress = key.getDerivedAddress();
            String expectedPrivateKey = key.getPrivateKey();

            Keyring actualKeyring = Keyring.createWithSingleKey(expectedAddress, expectedPrivateKey);
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createWithSingleKey_decoupled() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();

            Keyring actualKeyring = Keyring.createWithSingleKey(expectedAddress, expectedPrivateKey);

            assertTrue(actualKeyring.isDecoupled());
            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void createWithSingleKey_throwException_KlaytnWalletKeyFormat() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid format of parameter. Use 'fromKlaytnWalletKey' to create Keyring from KlaytnWalletKey.");

            Keyring keyring = Keyring.generate();
            String klaytnWalletKey = keyring.getKlaytnWalletKey();

            Keyring actualKeyring = Keyring.createWithSingleKey(keyring.getAddress(), klaytnWalletKey);
        }
    }


    public static class CreateWithMultipleKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createWithMultipleKey() {
            String expectedAddress = Keyring.generate().getAddress();

            String[] expectedPrivateKeyArr = {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            Keyring actualKeyring = Keyring.createWithMultipleKey(expectedAddress, expectedPrivateKeyArr);
            checkValidateMultipleKey(actualKeyring, expectedAddress, expectedPrivateKeyArr);
        }

        @Test
        public void createWithMultipleKey_throwException_invalidKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid private key.");

            String expectedAddress = Keyring.generate().getAddress();

            byte[] random = Utils.generateRandomBytes(31);
            String[] expectedPrivateKeyArr = {
                    Numeric.toHexString(random),
                    PrivateKey.generate().getPrivateKey(),
            };

            Keyring actualKeyring = Keyring.createWithMultipleKey(expectedAddress, expectedPrivateKeyArr);
        }
    }

    public static class CreateWithRoleBasedKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void createWithRoleBasedKey() {
            String expectedAddress = Keyring.generate().getAddress();
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
            Keyring actualKeyring = Keyring.createWithRoleBasedKey(expectedAddress, expectedKeyList);

            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }

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
            Keyring actualKeyring = Keyring.createWithRoleBasedKey(expectedAddress, expectedKeyList);
        }

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
            Keyring actualKeyring = Keyring.createWithRoleBasedKey(expectedAddress, expectedKeyList);
        }
    }

    public static class CopyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void copy_coupled() {
            Keyring expectedKeyring = Keyring.generate();
            String expectedPrivateKey = expectedKeyring.getKeys().get(0)[0].getPrivateKey();
            Keyring actualKeyring = expectedKeyring.copy();

            checkValidateSingleKey(actualKeyring, expectedKeyring.getAddress(), expectedPrivateKey);
        }

        @Test
        public void copy_decoupled() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();

            Keyring expectedKeyring = Keyring.create(expectedAddress, expectedPrivateKey);
            Keyring actualKeyring = expectedKeyring.copy();

            checkValidateSingleKey(actualKeyring, expectedAddress, expectedPrivateKey);
        }

        @Test
        public void copy_multipleKey() {
            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String[] expectedAddressKeys = new String[] {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
            };

            Keyring expectedKeyring = Keyring.createWithMultipleKey(expectedAddress, expectedAddressKeys);
            Keyring actualKeyring = expectedKeyring.copy();

            checkValidateMultipleKey(actualKeyring, expectedAddress, expectedAddressKeys);
        }

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

            Keyring expectedKeyring = Keyring.createWithRoleBasedKey(expectedAddress, expectedKeyList);
            Keyring actualKeyring = expectedKeyring.copy();

            checkValidateRoleBasedKey(actualKeyring, expectedAddress, expectedKeyList);
        }
    }

    public static class SignWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        static final String HASH = "0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550";
        static final int CHAIN_ID = 1;

        @Test
        public void coupleKey(){
            Keyring keyring = Keyring.generate();
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        @Test
        public void coupledKey_with_NotExistedRole(){
            Keyring keyring = Keyring.generate();

            KlaySignatureData expectedSignatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(Numeric.toHexString(expectedSignatureData.getR()), Numeric.toHexString(signatureData.getR()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getS()), Numeric.toHexString(signatureData.getS()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getV()), Numeric.toHexString(signatureData.getV()));
        }

        @Test
        public void coupleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value");
            Keyring keyring = Keyring.generate();
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        @Test
        public void coupleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");
            Keyring keyring = Keyring.generate();
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);
        }

        @Test
        public void deCoupleKey() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        @Test
        public void deCoupleKey_With_NotExistedRole() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            KlaySignatureData expectedSignatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(Numeric.toHexString(expectedSignatureData.getR()), Numeric.toHexString(signatureData.getR()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getS()), Numeric.toHexString(signatureData.getS()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getV()), Numeric.toHexString(signatureData.getV()));
        }

        @Test
        public void deCoupleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value");
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        @Test
        public void deCoupleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);
        }

        @Test
        public void multipleKey() {
            Keyring keyring = generateMultipleKeyring(3);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        @Test
        public void multipleKey_With_NotExistedRole() {
            Keyring keyring = generateMultipleKeyring(3);
            KlaySignatureData expectedSignatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(Numeric.toHexString(expectedSignatureData.getR()), Numeric.toHexString(signatureData.getR()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getS()), Numeric.toHexString(signatureData.getS()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getV()), Numeric.toHexString(signatureData.getV()));
        }

        @Test
        public void multipleKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value");

            Keyring keyring = generateMultipleKeyring(3);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), -1);
        }

        @Test
        public void multipleKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");
            Keyring keyring = generateMultipleKeyring(3);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 10);
        }

        @Test
        public void roleBasedKey() {
            Keyring keyring = generateRoleBaseKeyring(new int[]{2,3,4});

            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 1);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());
        }

        @Test
        public void roleBasedKey_With_NotExistedRole() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});

            KlaySignatureData expectedSignatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 0);
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 0);

            assertNotNull(signatureData.getR());
            assertNotNull(signatureData.getS());
            assertNotNull(signatureData.getV());

            assertEquals(Numeric.toHexString(expectedSignatureData.getR()), Numeric.toHexString(signatureData.getR()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getS()), Numeric.toHexString(signatureData.getS()));
            assertEquals(Numeric.toHexString(expectedSignatureData.getV()), Numeric.toHexString(signatureData.getV()));
        }

        @Test
        public void roleBasedKey_throwException_negativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value");
            Keyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), -1);
        }

        @Test
        public void roleBasedKey_throwException_outOfBoundKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");

            Keyring keyring = generateRoleBaseKeyring(new int[] {2,0,4});
            KlaySignatureData signatureData = keyring.signWithKey(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), 10);
        }
    }

    public static class signWithKeysTest {
        static final String HASH = "0xe9a11d9ef95fb437f75d07ce768d43e74f158dd54b106e7d3746ce29d545b550";
        static final int CHAIN_ID = 1;

        public void checkSignature(List<KlaySignatureData> expected, List<KlaySignatureData> actual) {
            assertEquals(expected.size(), actual.size());

            for(int i=0; i<expected.size(); i++) {
                assertEquals(Numeric.toHexString(expected.get(i).getR()), Numeric.toHexString(actual.get(i).getR()));
                assertEquals(Numeric.toHexString(expected.get(i).getS()), Numeric.toHexString(actual.get(i).getS()));
                assertEquals(Numeric.toHexString(expected.get(i).getV()), Numeric.toHexString(actual.get(i).getV()));
            }
        }

        @Test
        public void coupleKey() {
            Keyring keyring = Keyring.generate();
            List<KlaySignatureData> klaySignatureDataList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            assertEquals(1, klaySignatureDataList.size());
            assertNotNull(klaySignatureDataList.get(0).getR());
            assertNotNull(klaySignatureDataList.get(0).getS());
            assertNotNull(klaySignatureDataList.get(0).getV());
        }

        @Test
        public void coupleKey_With_NotExistedRole() {
            Keyring keyring = Keyring.generate();
            List<KlaySignatureData> expectedList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(1, actualList.size());
            assertNotNull(actualList.get(0).getR());
            assertNotNull(actualList.get(0).getS());
            assertNotNull(actualList.get(0).getV());

            checkSignature(expectedList, actualList);
        }

        @Test
        public void deCoupleKey() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(1, actualList.size());
            assertNotNull(actualList.get(0).getR());
            assertNotNull(actualList.get(0).getS());
            assertNotNull(actualList.get(0).getV());
        }

        @Test
        public void deCoupleKey_With_NotExistedRole() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring keyring = Keyring.create(address, privateKey);

            List<KlaySignatureData> expectedList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(1, actualList.size());
            checkSignature(expectedList, actualList);
        }

        @Test
        public void multipleKey() {
            Keyring keyring = generateMultipleKeyring(3);

            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(3, actualList.size());

            for(int i=0; i<actualList.size(); i++) {
                assertNotNull(actualList.get(0).getV());
                assertNotNull(actualList.get(0).getR());
                assertNotNull(actualList.get(0).getS());
            }
        }

        @Test
        public void multipleKey_With_NotExistedRole() {
            Keyring keyring = generateMultipleKeyring(3);

            List<KlaySignatureData> expectedList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(3, actualList.size());
            checkSignature(expectedList, actualList);
        }

        @Test
        public void roleBasedKey() {
            Keyring keyring = generateRoleBaseKeyring(new int[]{3,3,4});

            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            assertEquals(3, actualList.size());

            for(int i=0; i<actualList.size(); i++) {
                assertNotNull(actualList.get(0).getV());
                assertNotNull(actualList.get(0).getR());
                assertNotNull(actualList.get(0).getS());
            }
        }

        @Test
        public void roleBasedKey_With_NotExistedRole() {
            Keyring keyring = generateRoleBaseKeyring(new int[]{3,0,4});

            List<KlaySignatureData> expectedList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
            List<KlaySignatureData> actualList = keyring.signWithKeys(HASH, CHAIN_ID, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());

            assertEquals(3, actualList.size());
            checkSignature(expectedList, actualList);
        }
    }

    public static class signMessageTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String data = "some data";

        @Test
        public void coupledKey_NoIndex() {
            Keyring keyring = Keyring.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void coupleKey_WithIndex() {
            Keyring keyring = Keyring.generate();
            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.signMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatureData().getV());
            assertNotNull(actual.getSignatureData().getR());
            assertNotNull(actual.getSignatureData().getS());
        }

        @Test
        public void coupleKey_NotExistedRoleIndex() {
            Keyring keyring = Keyring.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void coupleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");

            Keyring keyring = Keyring.generate();
            MessageSigned expect = keyring.signMessage(data, 0, 3);
        }

        @Test
        public void coupleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value.");

            Keyring keyring = Keyring.generate();
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

        @Test
        public void decoupledKey_NoIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring decoupled = Keyring.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 0);
            MessageSigned actual = decoupled.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void decoupledKey_WithIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring decoupled = Keyring.create(address, privateKey);

            MessageSigned actual = decoupled.signMessage(data, 0, 0);

            assertEquals(Utils.signMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatureData().getV());
            assertNotNull(actual.getSignatureData().getR());
            assertNotNull(actual.getSignatureData().getS());
        }

        @Test
        public void decoupleKey_NotExistedRoleIndex() {
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring decoupled = Keyring.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 0);
            MessageSigned actual = decoupled.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void decoupleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");

            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring decoupled = Keyring.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, 3);
        }

        @Test
        public void decoupleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value.");

            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring decoupled = Keyring.create(address, privateKey);

            MessageSigned expect = decoupled.signMessage(data, 0, -1);
        }

        @Test
        public void multipleKey_NoIndex() {
            Keyring keyring = generateMultipleKeyring(3);

            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 0);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void multipleKey_WithIndex() {
            Keyring keyring = generateMultipleKeyring(3);

            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.signMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatureData().getV());
            assertNotNull(actual.getSignatureData().getR());
            assertNotNull(actual.getSignatureData().getS());
        }

        @Test
        public void multipleKey_NotExistedRoleIndex() {
            Keyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, 2);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex(), 2);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void multipleKey_throwException_WithInvalidKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value.");

            Keyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, 6);
        }

        @Test
        public void multipleKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value.");

            Keyring keyring = generateMultipleKeyring(3);
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

        @Test
        public void roleBasedKey_NoIndex() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            MessageSigned expect = keyring.signMessage(data, 0, 0);
            MessageSigned actual = keyring.signMessage(data);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));

        }

        @Test
        public void roleBasedKey_WithIndex() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            MessageSigned actual = keyring.signMessage(data, 0, 0);

            assertEquals(Utils.signMessage(data), actual.getMessageHash());
            assertNotNull(actual.getSignatureData().getV());
            assertNotNull(actual.getSignatureData().getR());
            assertNotNull(actual.getSignatureData().getS());
        }

        @Test
        public void roleBasedKey_NotExistedRoleKey() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,0,5});

            MessageSigned expect = keyring.signMessage(data, 0, 2);
            MessageSigned actual = keyring.signMessage(data, AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex(), 2);

            assertEquals(expect.getMessage(), actual.getMessage());
            assertEquals(expect.getMessageHash(), actual.getMessageHash());

            assertEquals(Numeric.toHexString(expect.getSignatureData().getR()), Numeric.toHexString(actual.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getS()), Numeric.toHexString(actual.getSignatureData().getS()));
            assertEquals(Numeric.toHexString(expect.getSignatureData().getV()), Numeric.toHexString(actual.getSignatureData().getV()));
        }

        @Test
        public void roleBasedKey_throwException_WithInvalidKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex value must be less than the length of key array");

            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            MessageSigned expect = keyring.signMessage(data, 0, 8);
        }

        @Test
        public void roleBasedKey_throwException_WithNegativeKeyIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("keyIndex cannot have negative value.");

            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            MessageSigned expect = keyring.signMessage(data, 0, -1);
        }

        @Test
        public void roleBasedKey_throwException_NoIndex_WithNoDefaultKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Default Key does not have enough keys to sign.");

            Keyring keyring = generateRoleBaseKeyring(new int[] {0,4,5});
            MessageSigned expect = keyring.signMessage(data);
        }
    }

    public static class recoverTest {
        public void checkAddress(String expect, String actual) {
            expect = Numeric.prependHexPrefix(expect);
            actual = Numeric.prependHexPrefix(actual);

            assertEquals(expect, actual);
        }

        @Test
        public void withSignedMessage() throws SignatureException {
            Keyring keyring = Keyring.generate();
            String message = "Some data";
            MessageSigned signed = keyring.signMessage(message, 0, 0);

            String actualAddr = Keyring.recover(signed);
            checkAddress(keyring.getAddress(), actualAddr);
        }

        @Test
        public void withMessageAndSignature() throws SignatureException {
            Keyring keyring = Keyring.generate();
            String message = "Some data";

            MessageSigned signed = keyring.signMessage(message, 0, 0);
            String actualAddr = Keyring.recover(signed.getMessage(), signed.getSignatureData());

            checkAddress(keyring.getAddress(), actualAddr);
        }

        @Test
        public void alreadyPrefix() throws SignatureException {
            Keyring keyring = Keyring.generate();
            String message = "Some data";

            MessageSigned signed = keyring.signMessage(message, 0, 0);
            String actualAddr = Keyring.recover(signed.getMessageHash(), signed.getSignatureData(), true);

            checkAddress(keyring.getAddress(), actualAddr);
        }
    }

    public static class decryptTest {

        public void checkValidKeyring(Keyring expect, Keyring actual) {
            assertEquals(expect.getAddress(), actual.getAddress());

            for(int i=0; i<actual.getKeys().size(); i++) {
                PrivateKey[] actualArr = actual.getKeys().get(i);
                PrivateKey[] expectedArr = expect.getKeys().get(i);

                assertEquals(expectedArr.length, actualArr.length);

                for(int j=0; j<actualArr.length; j++) {
                    assertEquals(expectedArr[j].getPrivateKey(), actualArr[j].getPrivateKey());
                }
            }
        }

        @Test
        public void coupleKey() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring expect = Keyring.createFromPrivateKey(privateKey);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = Keyring.encrypt(expect, password, option);
            Keyring actual = Keyring.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        @Test
        public void deCoupleKey() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring expect = Keyring.create(address, privateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = Keyring.encrypt(expect, password, option);
            Keyring actual = Keyring.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        @Test
        public void multipleKey() throws CipherException {
            String password = "password";
            Keyring expect = generateMultipleKeyring(3);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = Keyring.encrypt(expect, password, option);
            Keyring actual = Keyring.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        @Test
        public void roleBasedKey() throws CipherException {
            String password = "password";
            Keyring expect = generateRoleBaseKeyring(new int[]{3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = Keyring.encrypt(expect, password, option);
            Keyring actual = Keyring.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

        @Test
        public void roleBasedKey_withEmptyRole() throws CipherException {
            String password = "password";
            Keyring expect = generateRoleBaseKeyring(new int[]{3,0,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore keyStore = Keyring.encrypt(expect, password, option);
            Keyring actual = Keyring.decrypt(keyStore, password);

            checkValidKeyring(expect, actual);
        }

    }



    public static class encryptTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkValidateKeyStore(KeyStore actualData, String password, Keyring expectedKeyring, int version) throws CipherException{
            assertEquals(expectedKeyring.getAddress(), actualData.getAddress());

            if(actualData.getVersion() == 4) {
                assertNotNull(actualData.getKeyring());
                assertNull(actualData.getCrypto());
            }

            boolean isMultiSig = expectedKeyring.getKeys().stream().skip(1).anyMatch(keyArr -> keyArr.length > 0);
            if(isMultiSig) {
                assertTrue(actualData.getKeyring().get(0) instanceof List);
            } else {
                assertTrue(actualData.getKeyring().get(0) instanceof KeyStore.Crypto);
            }

            Keyring actualKeyring = Keyring.decrypt(actualData, password);

            for(int i=0; i<actualKeyring.getKeys().size(); i++) {
                PrivateKey[] actualArr = actualKeyring.getKeys().get(i);
                PrivateKey[] expectedArr = expectedKeyring.getKeys().get(i);

                assertEquals(expectedArr.length, actualArr.length);

                for(int j=0; j<actualArr.length; j++) {
                    assertEquals(expectedArr[j].getPrivateKey(), actualArr[j].getPrivateKey());
                }
            }
        }

        @Test
        public void keyStoreV4_scrypt() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());

            KeyStore store = Keyring.encrypt(keyring, password, option);
            checkValidateKeyStore(store, password, keyring, 4);
        }

        @Test
        public void keyStoreV4_pbkdf2() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName());

            KeyStore store = Keyring.encrypt(keyring, password, option);
            checkValidateKeyStore(store, password, keyring, 4);
        }

        @Test
        public void singleKeyStringWithCouple() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring expectedKeyring = Keyring.createFromPrivateKey(privateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());
            KeyStore store = Keyring.encrypt(privateKey, password, option);

            checkValidateKeyStore(store, password, expectedKeyring, 4);
        }

        @Test
        public void singleKeyStringWithDecoupled() throws CipherException{
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring expectedKeyring = Keyring.create(address, privateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);

            KeyStore store = Keyring.encrypt(privateKey, password, option);
            checkValidateKeyStore(store, password, expectedKeyring, 4);
        }

        @Test
        public void klaytnWalletKey() throws CipherException{
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring expectedKeyring = Keyring.create(address, privateKey);

            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedKeyring.getAddress());

            KeyStore store = Keyring.encrypt(klaytnWalletKey, password, option);
            checkValidateKeyStore(store, password, expectedKeyring, 4);
        }

        @Test
        public void klaytnWalletKey_throwException_InvalidAddress() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The address defined in options does not match the address of KlaytnWalletKey");

            String password = "password";
            Keyring expectedKeyring = Keyring.generate();
            String invalidAddress = Keyring.generate().getAddress();

            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), invalidAddress);

            KeyStore store = Keyring.encrypt(klaytnWalletKey, password, option);
        }

        @Test
        public void multipleKeyString() throws CipherException{
            String password = "password";
            String address = Keyring.generate().getAddress();

            String[] privateKeyArr = new String[] {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey()
            };
            Keyring expect = Keyring.create(address, privateKeyArr);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);

            KeyStore keyStore = Keyring.encrypt(privateKeyArr, password, option);
            checkValidateKeyStore(keyStore, password, expect,4);
        }

        @Test
        public void multipleKeyString_throwException_NoAddress() throws CipherException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The address must be defined inside the option object to encrypt multiple keys.");

            String password = "password";
            String address = Keyring.generate().getAddress();

            String[] privateKeyArr = new String[] {
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey(),
                    PrivateKey.generate().getPrivateKey()
            };
            Keyring expect = Keyring.create(address, privateKeyArr);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), null);

            KeyStore keyStore = Keyring.encrypt(privateKeyArr, password, option);
        }

        @Test
        public void roleBasedKeyString() throws CipherException {
            String password = "password";
            String address = Keyring.generate().getAddress();
            String[][] privateKeyArr = new String[][] {
                    {
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey(),
                        PrivateKey.generate().getPrivateKey()
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

            Keyring expect = Keyring.create(address, Arrays.asList(privateKeyArr));
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), address);

            KeyStore keyStore = Keyring.encrypt(Arrays.asList(privateKeyArr), password, option);
            checkValidateKeyStore(keyStore, password, expect,4);
        }

        @Test
        public void roleBasedKeyString_throwException_noAddress() throws CipherException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The address must be defined inside the option object to encrypt roleBased keys.");

            String password = "password";
            String address = Keyring.generate().getAddress();
            String[][] privateKeyArr = new String[][] {
                    {
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey(),
                            PrivateKey.generate().getPrivateKey()
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

            Keyring expect = Keyring.create(address, Arrays.asList(privateKeyArr));
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), null);

            KeyStore keyStore = Keyring.encrypt(Arrays.asList(privateKeyArr), password, option);
        }

        @Test
        public void keyring_single() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = Keyring.encrypt(keyring, password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void keyring_multiple() throws CipherException {
            String password = "password";
            Keyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = Keyring.encrypt(keyring, password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void keyring_roleBased() throws CipherException {
            String password = "password";
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = Keyring.encrypt(keyring, password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void instanceMethod_singleKey() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void instanceMethod_multipleKey() throws CipherException {
            String password = "password";
            Keyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void instanceMethod_roleBasedKey() throws CipherException {
            String password = "password";
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encrypt(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }
    }

    public static class encryptV3Test {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkValidateKeyStore(KeyStore actualData, String password, Keyring expectedKeyring, int version) throws CipherException{
            assertEquals(expectedKeyring.getAddress(), actualData.getAddress());

            if(actualData.getVersion() == version) {
                assertNotNull(actualData.getCrypto());
                assertNull(actualData.getKeyring());
            }

            Keyring actualKeyring = Keyring.decrypt(actualData, password);

            for(int i=0; i<actualKeyring.getKeys().size(); i++) {
                PrivateKey[] actualArr = actualKeyring.getKeys().get(i);
                PrivateKey[] expectedArr = expectedKeyring.getKeys().get(i);

                assertEquals(expectedArr.length, actualArr.length);

                for(int j=0; j<actualArr.length; j++) {
                    assertEquals(expectedArr[j].getPrivateKey(), actualArr[j].getPrivateKey());
                }
            }
        }

        @Test
        public void coupleKeyString() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            Keyring expect = Keyring.createFromPrivateKey(privateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName());

            KeyStore keyStore = Keyring.encryptV3(privateKey, password, option);
            checkValidateKeyStore(keyStore, password, expect, 3);
        }

        @Test
        public void deCoupledKeyString() throws CipherException {
            String password = "password";

            String expectedAddress = PrivateKey.generate().getDerivedAddress();
            String expectedPrivateKey = PrivateKey.generate().getPrivateKey();

            Keyring expect = Keyring.createWithSingleKey(expectedAddress, expectedPrivateKey);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedAddress);

            KeyStore keyStore = Keyring.encryptV3(expectedPrivateKey, password, option);
            checkValidateKeyStore(keyStore, password, expect, 3);
        }

        @Test
        public void klaytnWalletKey() throws CipherException {
            String password = "password";
            String privateKey = PrivateKey.generate().getPrivateKey();
            String address = PrivateKey.generate().getDerivedAddress();
            Keyring expectedKeyring = Keyring.create(address, privateKey);

            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), expectedKeyring.getAddress());

            KeyStore store = Keyring.encryptV3(klaytnWalletKey, password, option);
            checkValidateKeyStore(store, password, expectedKeyring, 3);
        }

        @Test
        public void klaytnWalletKey_throwException_InvalidAddress() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The address defined in options does not match the address of KlaytnWalletKey");

            String password = "password";
            Keyring expectedKeyring = Keyring.generate();
            String invalidAddress = Keyring.generate().getAddress();

            String klaytnWalletKey = expectedKeyring.getKlaytnWalletKey();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), invalidAddress);

            KeyStore store = Keyring.encryptV3(klaytnWalletKey, password, option);
        }

        @Test
        public void keyring_single() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = Keyring.encryptV3(keyring, password, option);
            checkValidateKeyStore(keyStore, password, keyring, 3);
        }

        @Test
        public void throwException_keyring_multiple() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)");
            String password = "password";
            Keyring keyring = generateMultipleKeyring(3);

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());
            KeyStore keyStore = Keyring.encryptV3(keyring, password, option);
        }

        @Test
        public void throwException_keyring_roleBased() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)");
            String password = "password";
            Keyring keyring = generateRoleBaseKeyring(new int[] {2,3,4});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.ScryptKdfParams.getName(), keyring.getAddress());
            KeyStore keyStore = Keyring.encryptV3(keyring, password, option);
        }

        @Test
        public void instanceMethod_singleKey() throws CipherException {
            String password = "password";
            Keyring keyring = Keyring.generate();
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
            checkValidateKeyStore(keyStore, password, keyring, 4);
        }

        @Test
        public void throwException_instanceMethod_multipleKey() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)");

            String password = "password";
            Keyring keyring = generateMultipleKeyring(3);
            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
        }

        @Test
        public void throwException_instanceMethod_roleBasedKey() throws CipherException {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("This keyring cannot be encrypted keystore v3. use 'keyring.encrypt(password)");

            String password = "password";
            Keyring keyring = generateRoleBaseKeyring(new int[] {3,4,5});

            KeyStoreOption option = KeyStoreOption.getDefaultOptionWithKDF(KeyStore.Pbkdf2KdfParams.getName(), keyring.getAddress());

            KeyStore keyStore = keyring.encryptV3(password, option);
        }
    }

    public static class getKeyByRoleTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getKeyByRole() {
            int[] count = {2,3,4};
            Keyring roleKeyring = generateRoleBaseKeyring(count);
            PrivateKey[] keys = roleKeyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            assertNotNull(keys);
            assertEquals(2, roleKeyring.getKeys().size());
        }

        @Test
        public void getKeyByRole_defaultKey() {
            Keyring keyring = generateMultipleKeyring(3);
            PrivateKey[] keys = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex());

            assertNotNull(keys);
            assertEquals(3, keys.length);
        }

        @Test
        public void getKeyByRole_throwException_defaultKeyEmpty() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The key data with specified roleIndex does not exist. The default key in TransactionRole is also empty.");

            int[] count = {0, 0, 3};
            Keyring keyring = generateRoleBaseKeyring(count);

            PrivateKey[] keys = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex());
        }

        @Test
        public void getKeyByRole_throwException_invalidIndex() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid role index");

            Keyring keyring = generateMultipleKeyring(4);
            PrivateKey[] keys = keyring.getKeyByRole(4);
        }
    }

    public static class getKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getKlaytnWalletKey_coupled() {
            Keyring keyring = Keyring.generate();
            String expectedKeyStr = keyring.getKeys().get(0)[0].getPrivateKey() + "0x00" + keyring.getAddress();

            assertEquals(expectedKeyStr, keyring.getKlaytnWalletKey());
        }

        @Test
        public void getKlaytnWalletKey_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            Keyring keyring = Keyring.create(address, privateKey);
            String expectedKeyStr = privateKey + "0x00" + Numeric.prependHexPrefix(address);

            assertEquals(expectedKeyStr, keyring.getKlaytnWalletKey());

            String actualKey = keyring.getKlaytnWalletKey();
        }

        @Test
        public void getKlaytnWallet_throwException_multiKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The keyring cannot be exported in KlaytnWalletKey format. Use caver.wallet.keyring.encrypt or keyring.encrypt.");

            Keyring keyring = generateMultipleKeyring(3);
            String keyStr = keyring.getKlaytnWalletKey();
        }

        @Test
        public void getKlaytnWallet_thrownException_roleBased() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("The keyring cannot be exported in KlaytnWalletKey format. Use caver.wallet.keyring.encrypt or keyring.encrypt.");

            Keyring keyring = generateRoleBaseKeyring(new int[]{1,3,4});
            String keyStr = keyring.getKlaytnWalletKey();
        }
    }

    public static class getPublicKeyTest {

        @Test
        public void getPublicKey_single() {
            Keyring keyring = Keyring.generate();
            List<String[]> publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKeys().get(0)[0].getPublicKey(false), publicKeys.get(0)[0]);
            assertEquals(1, publicKeys.get(0).length);
            assertEquals(0, publicKeys.get(1).length);
            assertEquals(0, publicKeys.get(2).length);
        }

        @Test
        public void getPublicKey_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            Keyring keyring = Keyring.create(address, privateKey);
            List<String[]> publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKeys().get(0)[0].getPublicKey(false), publicKeys.get(0)[0]);
            assertEquals(1, publicKeys.get(0).length);
            assertEquals(0, publicKeys.get(1).length);
            assertEquals(0, publicKeys.get(2).length);
        }

        @Test
        public void getPublicKey_multiple() {
            Keyring keyring = generateMultipleKeyring(2);
            List<String[]> publicKeys = keyring.getPublicKey();

            assertEquals(keyring.getKeys().get(0)[0].getPublicKey(false), publicKeys.get(0)[0]);
            assertEquals(keyring.getKeys().get(0)[1].getPublicKey(false), publicKeys.get(0)[1]);

            assertEquals(2, publicKeys.get(0).length);
            assertEquals(0, publicKeys.get(1).length);
            assertEquals(0, publicKeys.get(2).length);
        }

        @Test
        public void getPublicKey_roleBased() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {2, 3, 1});
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
    }

    public static class isDecoupledTest {

        @Test
        public void isDecoupled_coupled() {
            Keyring keyring = Keyring.generate();

            assertFalse(keyring.isDecoupled());
        }

        @Test
        public void isDecoupled_decoupled() {
            String address = PrivateKey.generate().getDerivedAddress();
            String privateKey = PrivateKey.generate().getPrivateKey();

            Keyring keyring = Keyring.create(address, privateKey);

            assertTrue(keyring.isDecoupled());
        }

        @Test
        public void isDecoupled_multiKey() {
            Keyring keyring = generateMultipleKeyring(3);
            assertTrue(keyring.isDecoupled());
        }

        @Test
        public void isDecoupled_roleBased() {
            Keyring keyring = generateRoleBaseKeyring(new int[]{2,3,1});
            assertTrue(keyring.isDecoupled());
        }
    }

    public static class toAccountTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkAccountKeyPublic(Keyring keyring, Account account) {
            String expectedPublicKey = keyring.getKeyByRole(AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex())[0].getPublicKey(false);

            assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
            assertEquals(Numeric.prependHexPrefix(keyring.getAddress()), Numeric.prependHexPrefix(account.getAddress()));
            assertEquals(expectedPublicKey, ((AccountKeyPublic) account.getAccountKey()).getPublicKey());
        }

        public void checkAccountKeyWeightedMultiSig(Keyring keyring, Account account, WeightedMultiSigOptions options) {
            String[] expectedPublicKeys = keyring.getPublicKey().get(0);
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

        @Test
        public void singleKeyTest() {
            Keyring keyring = Keyring.generate();
            Account account = keyring.toAccount();

            checkAccountKeyPublic(keyring, account);
        }

        @Test
        public void singleKeyTest_throwException_multipleKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to create Account instance: There are two or more keys in RoleTransaction Key array.");

            Keyring keyring = generateMultipleKeyring(3);
            Account account = keyring.toAccount();
        }

        @Test
        public void singleKeyTest_throwException_roleBaseKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");

            Keyring keyring = generateRoleBaseKeyring(new int[]{1,2,3});
            Account account = keyring.toAccount();
        }

        @Test
        public void multipleKeyTest() {
            Keyring keyring = generateMultipleKeyring(3);

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Account account = keyring.toAccount(options);
            checkAccountKeyWeightedMultiSig(keyring, account, options);
        }

        @Test
        public void multipleKeyTest_throwException_noKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to create Account instance: There must be one or more keys in RoleTransaction Key array.");

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Keyring keyring = generateMultipleKeyring(0);
            Account account = keyring.toAccount(options);
        }

        @Test
        public void multipleKeyTest_throwException_weightedOptionCount() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to create Account instance: The number of keys and the number of elements in the Weights array should be the same.");

            Keyring keyring = generateMultipleKeyring(2);

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Account account = keyring.toAccount(options);
        }

        @Test
        public void multipleKeyTest_throwException_roleBasedKey() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Failed to create Account instance: There are exists keys in other Group(RoleAccountUpdate, RoleFeePayer)");

            Keyring keyring = generateRoleBaseKeyring(new int[]{3,3,4});

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)};
            WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

            Account account = keyring.toAccount(options);
        }

        @Test
        public void roleBasedKeyTest_SingleKey() {
            Keyring keyring = generateRoleBaseKeyring(new int[]{1,1,1});
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

        @Test
        public void roleBaseKeyTest_multipleKey() {
            Keyring keyring = generateRoleBaseKeyring(new int[] {2,3,4});
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

            Keyring keyring = Keyring.createWithRoleBasedKey(address, Arrays.asList(expectedPrivateKeyArr));
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
