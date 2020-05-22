package com.klaytn.caver.common;

import com.klaytn.caver.account.*;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.Wallet;
import com.klaytn.caver.wallet.keyring.Keyring;
import com.klaytn.caver.wallet.keyring.MessageSigned;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
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
            keyArr[i] = PrivateKey.generate().getPrivateKey();
        }

        String address = PrivateKey.generate().getDerivedAddress();
        return Keyring.createWithMultipleKey(address, keyArr);
    }

    public static Keyring generateRoleBaseKeyring(int[] numArr) {
        String[][] keyArr = new String[3][];

        for(int i=0; i<numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j=0; j<length; j++) {
                arr[j] = PrivateKey.generate().getPrivateKey();
            }
            keyArr[i] = arr;
        }

        String address = PrivateKey.generate().getDerivedAddress();
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
        public void signWithKey_coupledKey() throws SignatureException {
            String privateKey = "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3";
            String address = "0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a";
            String message = "0xdeadbeaf";

            ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));

            PrivateKey key = new PrivateKey(privateKey);
            assertEquals(key.getPublicKey(false), Numeric.toHexStringWithPrefix(keyPair.getPublicKey()));

            Keyring keyring = Keyring.createFromPrivateKey(privateKey);
            MessageSigned signed = keyring.signMessage(message, 0, 0);
            Sign.SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(message), keyPair);

            BigInteger publicKey = Sign.signedMessageToKey(Numeric.hexStringToByteArray(message), data);
            assertEquals(Numeric.toHexStringWithPrefix(publicKey), key.getPublicKey(false));

            assertEquals(Numeric.toHexString(data.getR()), Numeric.toHexString(signed.getSignatureData().getR()));
            assertEquals(Numeric.toHexString(data.getS()), Numeric.toHexString(signed.getSignatureData().getS()));
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
