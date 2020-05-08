package com.klaytn.caver.common;

import com.klaytn.caver.account.AccountKeyFail;
import com.klaytn.caver.account.AccountKeyLegacy;
import com.klaytn.caver.account.AccountKeyPublic;
import com.klaytn.caver.tx.account.AccountKey;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({AccountKeyTest.AccountKeyFailTests.class, AccountKeyTest.AccountKeyLegacyTest.class, AccountKeyTest.AccountKeyPublicTest.class})
public class AccountKeyTest {

    public static class AccountKeyFailTests {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();
        //CA-ACCOUNT-001
        @Test
        public void decodeWithString() {
            String encodedString = "0x03c0";
            try {
                AccountKeyFail accountKeyFail = AccountKeyFail.decode(encodedString);
                assertTrue(accountKeyFail instanceof AccountKeyFail);
            } catch (Exception e) {
                fail();
            }
        }

        //CA-ACCOUNT-002
        @Test
        public void decodeWithByteArray() {
            byte[] encodedArr = new byte[]{(byte) 0x03, (byte) 0xc0};
            String encodeString = "0x03c0";
            try {
                AccountKeyFail accountKeyFail = AccountKeyFail.decode(encodedArr);
                assertTrue(accountKeyFail instanceof AccountKeyFail);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        //CA-ACCOUNT-003
        @Test
        public void decodeWithString_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            String encodedString ="0x03";

            //should throw RuntimeException
            AccountKeyFail accountKeyFail = AccountKeyFail.decode(encodedString);
        }

        //CA-ACCOUNT-005
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            byte[] encodedArr = new byte[]{(byte) 0x03};

            //should throw RuntimeException
            AccountKeyFail accountKeyFail = AccountKeyFail.decode(encodedArr);
        }
        //CA-ACCOUNT-006
        @Test
        public void encodeKey() {
            String expected = "0x03c0";
            AccountKeyFail accountKeyFail = new AccountKeyFail();

            assertEquals(expected, accountKeyFail.getRLPEncoding());
        }
    }

    public static class AccountKeyLegacyTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();
        //CA-ACCOUNT-007
        @Test
        public void decodeWithString() {
            String encodedString = "0x01c0";

            try {
                AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedString);
                assertTrue(accountKeyLegacy instanceof AccountKeyLegacy);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        //CA-ACCOUNT-008
        @Test
        public void decodeWithByteArray() {
            byte[] encodedArr = new byte[]{(byte) 0x01, (byte) 0xc0};
            String encodedString = "0x01c0";
            try {
                AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedArr);
                assertTrue(accountKeyLegacy instanceof AccountKeyLegacy);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        //CA-ACCOUNT-009
        @Test
        public void decodeWithString_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            String encodedString = "0x01";

            //should throw RuntimeException
            AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedString);
        }

        //CA-ACCOUNT-011
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            byte[] encodedArr = new byte[]{(byte) 0x01};

            //should throw RuntimeException
            AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedArr);
        }

        //CA-ACCOUNT-012
        @Test
        public void encode() {
            String encodedString = "0x01c0";
            try {
                AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedString);
                assertEquals(encodedString, accountKeyLegacy.getRLPEncoding());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public static class AccountKeyPublicTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        //CA-ACCOUNT-013
        @Test
        public void decodeWithString() {
            String expectedAccountKey = "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e";
            String actualEncodedKey = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            try {
                AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(actualEncodedKey);

                assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
                assertEquals(expectedAccountKey, accountKeyPublic.getPublicKey());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        //CA-ACCOUNT-014
        @Test
        public void decodeWithByteArray() {
            String expectedAccountKey = "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e";
            String actualEncodedKey = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";
            byte[] actualEncodedKeyArr = Numeric.hexStringToByteArray(actualEncodedKey);

            try {
                AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(actualEncodedKeyArr);

                assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
                assertEquals(accountKeyPublic.getPublicKey(), expectedAccountKey);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }

        //CA-ACCOUNT-015
        @Test
        public void decodeWithString_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyPublic Tag");

            String invalidEncodedValue = "0x03a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            //should throw RuntimeException
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(invalidEncodedValue);
        }

        //CA-ACCOUNT-016
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyPublic Tag");

            byte[] invalidEncodedValue = Numeric.hexStringToByteArray("0x03a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9");

            //should throw RuntimeException
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(invalidEncodedValue);
        }

        //CA-ACCOUNT-017
        @Test
        public void fromXYPoint() {
            String publicKey = "0x022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf11f7e6aa508dd50af53e190dcd4a2559aa1c3ef3f78b97b97e2928ac33e038464";
            String noPrefixPubKey = Numeric.cleanHexPrefix(publicKey);

            String x = noPrefixPubKey.substring(0, 64);
            String y = noPrefixPubKey.substring(64);

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromXYPoint(x, y);

            assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
            assertEquals(publicKey, accountKeyPublic.getPublicKey());
        }

        //CA-ACCOUNT-018
        @Test
        public void fromPublicKey_uncompressedFormat() {
            String publicKey = "0x022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf11f7e6aa508dd50af53e190dcd4a2559aa1c3ef3f78b97b97e2928ac33e038464";

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(publicKey);

            assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
            assertEquals(publicKey, accountKeyPublic.getPublicKey());
        }

        //CA-ACCOUNT-019
        @Test
        public void fromPublicKey_compressedFormat() {
            String publicKey = "0x02022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf1";

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(publicKey);

            assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
            assertEquals(publicKey, accountKeyPublic.getPublicKey());
        }

        //CA-ACCOUNT-020
        @Test
        public void getXYPoint_uncompressedFormat() {
            String publicKey = "0x022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf11f7e6aa508dd50af53e190dcd4a2559aa1c3ef3f78b97b97e2928ac33e038464";
            String noPrefixPubKey = Numeric.cleanHexPrefix(publicKey);

            String expectedX = noPrefixPubKey.substring(0, 64);
            String expectedY = noPrefixPubKey.substring(64);

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(publicKey);
            String[] arr = accountKeyPublic.getXYPoint();


            assertEquals(expectedX, arr[0]);
            assertEquals(expectedY, arr[1]);
        }

        //CA-ACCOUNT-021
        @Test
        public void getXYPoint_compressedFormat() {
            String compressedPublicKey = "0x02022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf1";

            String publicKey = "0x022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf11f7e6aa508dd50af53e190dcd4a2559aa1c3ef3f78b97b97e2928ac33e038464";
            String noPrefixPubKey = Numeric.cleanHexPrefix(publicKey);

            String expectedX = noPrefixPubKey.substring(0, 64);
            String expectedY = noPrefixPubKey.substring(64);

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(compressedPublicKey);
            String[] arr = accountKeyPublic.getXYPoint();


            assertEquals(expectedX, arr[0]);
            assertEquals(expectedY, arr[1]);
        }
    }


}
