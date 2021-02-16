package com.klaytn.caver.common;

import com.klaytn.caver.account.*;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.Utils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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

        //CA-ACCOUNT-004
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            byte[] encodedArr = new byte[]{(byte) 0x03};

            //should throw RuntimeException
            AccountKeyFail accountKeyFail = AccountKeyFail.decode(encodedArr);
        }

        //CA-ACCOUNT-005
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

        //CA-ACCOUNT-006
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

        //CA-ACCOUNT-007
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

        //CA-ACCOUNT-008
        @Test
        public void decodeWithString_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            String encodedString = "0x01";

            //should throw RuntimeException
            AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedString);
        }

        //CA-ACCOUNT-009
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid RLP-encoded account key String");

            byte[] encodedArr = new byte[]{(byte) 0x01};

            //should throw RuntimeException
            AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.decode(encodedArr);
        }

        //CA-ACCOUNT-010
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

        //CA-ACCOUNT-011
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

        //CA-ACCOUNT-012
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

        //CA-ACCOUNT-013
        @Test
        public void decodeWithString_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyPublic Tag");

            String invalidEncodedValue = "0x03a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            //should throw RuntimeException
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(invalidEncodedValue);
        }

        //CA-ACCOUNT-014
        @Test
        public void decodeWithByteArray_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyPublic Tag");

            byte[] invalidEncodedValue = Numeric.hexStringToByteArray("0x03a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9");

            //should throw RuntimeException
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.decode(invalidEncodedValue);
        }

        //CA-ACCOUNT-015
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

        //CA-ACCOUNT-016
        @Test
        public void fromPublicKey_uncompressedFormat() {
            String publicKey = "0x022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf11f7e6aa508dd50af53e190dcd4a2559aa1c3ef3f78b97b97e2928ac33e038464";

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(publicKey);

            assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
            assertEquals(publicKey, accountKeyPublic.getPublicKey());
        }

        //CA-ACCOUNT-017
        @Test
        public void fromPublicKey_compressedFormat() {
            String publicKey = "0x02022dfe0d7c496d954037ab15afd3352008f6c5bfe972850b7b321e96721f4bf1";

            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromPublicKey(publicKey);

            assertTrue(accountKeyPublic instanceof  AccountKeyPublic);
            assertEquals(publicKey, accountKeyPublic.getPublicKey());
        }

        //CA-ACCOUNT-018
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

        //CA-ACCOUNT-019
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

    public static class AccountKeyWeightedMultiSigTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkWeightedPublicKey(String[] expectedPublicKey, WeightedMultiSigOptions expectedOptions, AccountKeyWeightedMultiSig actualAccount) {
            //check Threshold
            assertEquals(expectedOptions.getThreshold(), actualAccount.getThreshold());

            //check WeightedPublicKey
            for(int i=0; i<expectedPublicKey.length; i++) {
                assertEquals(expectedOptions.getWeights().get(i), actualAccount.getWeightedPublicKeys().get(i).getWeight());

                String publicKey = actualAccount.getWeightedPublicKeys().get(i).getPublicKey();
                if(AccountKeyPublicUtils.isCompressedPublicKey(publicKey)) {
                    publicKey = Utils.decompressPublicKey(publicKey);
                }
                assertEquals(expectedPublicKey[i], publicKey);
            }
        }

        //CA-ACCOUNT-020
        @Test
        public void decodeWithString() {
            String[] expectedAccountKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
            };

            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), weightList);

            String encodedKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(encodedKey);

            checkWeightedPublicKey(expectedAccountKey, expectedOption, multiSig);
        }

        //CA-ACCOUNT-021
        @Test
        public void decodeWithByteArray() {
            String[] expectedAccountKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
            };

            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), weightList);
            byte[] encodedKeyArr = Numeric.hexStringToByteArray("0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1");

            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(encodedKeyArr);
            checkWeightedPublicKey(expectedAccountKey, expectedOption, multiSig);
        }

        //CA-ACCOUNT-022
        @Test
        public void decodeStringWithException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyWeightedMultiSig Tag");

            String encodedKey = "0x03f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";
            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(encodedKey);
        }

        //CA-ACCOUNT-023
        @Test
        public void decodeByteArrayWithException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyWeightedMultiSig Tag");

            byte[] encodedKeyArr = Numeric.hexStringToByteArray("0x03f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1");
            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(encodedKeyArr);
        }

        //CA-ACCOUNT-024
        @Test
        public void fromPublicKeysAndOptions() {
            String[] expectedAccountKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
            };

            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), weightList);

            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(expectedAccountKey, expectedOption);
            checkWeightedPublicKey(expectedAccountKey, expectedOption, multiSig);
        }

        //CA-ACCOUNT-025
        @Test
        public void getRLPEncoding() {
            String expectedEncodedData = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            String[] publicKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
            };

            BigInteger threshold = BigInteger.valueOf(2);

            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions option = new WeightedMultiSigOptions(threshold, weightList);
            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, option);
            String data = multiSig.getRLPEncoding();

            assertEquals(expectedEncodedData, data);
        }

        //CA-ACCOUNT-026
        @Test
        public void weightedMultiSigOptionTest_ThresholdCondition1() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid argument in passing params.");

            //The sum of weight of WeightedPublicKey should be bigger than threshold.
            BigInteger threshold = BigInteger.TEN;
            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions options = new WeightedMultiSigOptions(threshold, weightList);
        }

        //CA-ACCOUNT-027
        @Test
        public void weightedMultiSigOptionTest_ThresholdCondition2() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid argument in passing params.");

            //threshold value has bigger than zero.
            BigInteger threshold = BigInteger.ZERO;
            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions options = new WeightedMultiSigOptions(threshold, weightList);
        }

        //CA-ACCOUNT-028
        @Test
        public void weightedMultiSigOptionTest_WeightCount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid argument in passing params.");

            //Weights option must have smaller than 10
            BigInteger threshold = BigInteger.valueOf(2);
            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions options = new WeightedMultiSigOptions(threshold, weightList);
        }

        //CA-ACCOUNT-029
        @Test
        public void fromPublicKeysAndOptionsWithException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The count of public keys is not equal to the length of weight array.");

            String[] publicKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    "0xd31970913271bb571db505418414ae15e97337b944ef1bef84a0e5d20c2ece7f27a39deb8f449edea7cecf8f3588a51974f31d676a8b200fb61175149fff9b74",
                    "0x68ad36b538afe09997af82bb92d056404feb93816b5ec6a5199bc1d6bb15358fa3cfb84ac7cab3275e973be6699cd19c61ba8c470fee97a9998bd0684cf44355",
            };

            BigInteger threshold = BigInteger.valueOf(2);
            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions options = new WeightedMultiSigOptions(threshold, weightList);

            AccountKeyWeightedMultiSig accountKeyWeightedMultiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, options);
        }

        //CA-ACCOUNT-030
        @Test
        public void fromPublicKeysAndOptionsWithException2() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("It exceeds maximum public key count.");

            String[] publicKey = new String[] {
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    "0xd31970913271bb571db505418414ae15e97337b944ef1bef84a0e5d20c2ece7f27a39deb8f449edea7cecf8f3588a51974f31d676a8b200fb61175149fff9b74",
                    "0x68ad36b538afe09997af82bb92d056404feb93816b5ec6a5199bc1d6bb15358fa3cfb84ac7cab3275e973be6699cd19c61ba8c470fee97a9998bd0684cf44355",
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    "0xd31970913271bb571db505418414ae15e97337b944ef1bef84a0e5d20c2ece7f27a39deb8f449edea7cecf8f3588a51974f31d676a8b200fb61175149fff9b74",
                    "0x68ad36b538afe09997af82bb92d056404feb93816b5ec6a5199bc1d6bb15358fa3cfb84ac7cab3275e973be6699cd19c61ba8c470fee97a9998bd0684cf44355",
                    "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    "0xd31970913271bb571db505418414ae15e97337b944ef1bef84a0e5d20c2ece7f27a39deb8f449edea7cecf8f3588a51974f31d676a8b200fb61175149fff9b74",
                    "0x68ad36b538afe09997af82bb92d056404feb93816b5ec6a5199bc1d6bb15358fa3cfb84ac7cab3275e973be6699cd19c61ba8c470fee97a9998bd0684cf44355"
            };

            BigInteger threshold = BigInteger.valueOf(2);
            BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
            List<BigInteger> weightList = Arrays.asList(weight);

            WeightedMultiSigOptions options = new WeightedMultiSigOptions(threshold, weightList);

            AccountKeyWeightedMultiSig accountKeyWeightedMultiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, options);
        }
    }

    public static class AccountKeyRoleBasedTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        public void checkValid(String[][] expectedPublicKeyArr,
                                     List<WeightedMultiSigOptions> expectedOptionsList,
                                     AccountKeyRoleBased actualRoleBasedKey) {
            List<IAccountKey> actualKeys = actualRoleBasedKey.getAccountKeys();

            for(int i = 0; i<AccountKeyRoleBased.ROLE_GROUP_COUNT; i++) {
                IAccountKey key = actualKeys.get(i);
                if(key instanceof AccountKeyPublic) {
                    checkAccountKeyPublic(expectedPublicKeyArr[i], expectedOptionsList.get(i), (AccountKeyPublic)key);
                } else if (key instanceof AccountKeyWeightedMultiSig) {
                    checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[i], expectedOptionsList.get(i), (AccountKeyWeightedMultiSig)key);
                } else if (key instanceof AccountKeyLegacy) {

                }
            }
        }

        public void checkPublicKey(String expected, String actual) {
            expected = Utils.compressPublicKey(expected);
            actual = Utils.compressPublicKey(actual);

            expected = Numeric.cleanHexPrefix(expected);
            actual = Numeric.cleanHexPrefix(actual);

            assertEquals(expected, actual);
        }

        public void checkAccountKeyPublic(String[] expectedKey,
                                          WeightedMultiSigOptions expectedOptions,
                                          AccountKeyPublic accountKeyPublic) {
            assertEquals(expectedKey.length, 1);
            assertTrue(expectedOptions.isEmpty());

            checkPublicKey(expectedKey[0], accountKeyPublic.getPublicKey());
        }

        public void checkAccountKeyWeightedMultiSig(String[] expectedKeys, WeightedMultiSigOptions expectedOption, AccountKeyWeightedMultiSig accountKeyWeightedMultiSig) {
            assertEquals(expectedKeys.length, accountKeyWeightedMultiSig.getWeightedPublicKeys().size());
            assertEquals(expectedOption.getThreshold(), accountKeyWeightedMultiSig.getThreshold());

            List<WeightedPublicKey> weightedPublicKeyList = accountKeyWeightedMultiSig.getWeightedPublicKeys();

            for(int i=0; i < weightedPublicKeyList.size(); i++) {
                checkPublicKey(expectedKeys[i], weightedPublicKeyList.get(i).getPublicKey());
                assertEquals(expectedOption.getWeights().get(i), weightedPublicKeyList.get(i).getWeight());
            }
        }

        //CA-ACCOUNT-031
        @Test
        public void decodeWithString() {
            String encodedData = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
            String[][] expectedPublicKeyArr = {
                    //expectedTransactionKey
                    {"0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"},

                    //expectedAccountUpdateKey
                    {"0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                    "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    },

                    //expectedFeePayedKey
                    {"0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                    "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083"}
                };

            List<String[]> list = Arrays.asList(expectedPublicKeyArr);
            BigInteger[] weightArr = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

            WeightedMultiSigOptions transactionKeyOptions = new WeightedMultiSigOptions(); // empty WeightedMultiSigOptions
            WeightedMultiSigOptions accountUpdateKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(weightArr));
            WeightedMultiSigOptions feePayerKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(weightArr));

            AccountKeyRoleBased accountKey = AccountKeyRoleBased.decode(encodedData);

            IAccountKey transactionKey = accountKey.getRoleTransactionKey();
            assertTrue(transactionKey instanceof AccountKeyPublic);
            checkAccountKeyPublic(expectedPublicKeyArr[0], transactionKeyOptions, (AccountKeyPublic)transactionKey);

            IAccountKey accountUpdateKey = accountKey.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[1], accountUpdateKeyOption, (AccountKeyWeightedMultiSig)accountUpdateKey);

            IAccountKey feePayerKey = accountKey.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[2], feePayerKeyOption, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-032
        @Test
        public void decodeByteArray() {
            String encodedData = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
            byte[] encodedArr = Numeric.hexStringToByteArray(encodedData);
            String[][] expectedPublicKeyArr = {
                    //expectedTransactionKey
                    {"0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"},

                    //expectedAccountUpdateKey
                    {"0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                            "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    },

                    //expectedFeePayedKey
                    {"0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                            "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083"}
            };

            List<String[]> list = Arrays.asList(expectedPublicKeyArr);
            BigInteger[] weightArr = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

            WeightedMultiSigOptions transactionKeyOptions = new WeightedMultiSigOptions(); // empty WeightedMultiSigOptions
            WeightedMultiSigOptions accountUpdateKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(weightArr));
            WeightedMultiSigOptions feePayerKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(weightArr));

            AccountKeyRoleBased accountKey = AccountKeyRoleBased.decode(encodedArr);

            IAccountKey transactionKey = accountKey.getRoleTransactionKey();
            assertTrue(transactionKey instanceof AccountKeyPublic);
            checkAccountKeyPublic(expectedPublicKeyArr[0], transactionKeyOptions, (AccountKeyPublic)transactionKey);

            IAccountKey accountUpdateKey = accountKey.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[1], accountUpdateKeyOption, (AccountKeyWeightedMultiSig)accountUpdateKey);

            IAccountKey feePayerKey = accountKey.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[2], feePayerKeyOption, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-033
        @Test
        public void decodeWithStringAccountNil() {
            String encodedData = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
            String[][] expectedPublicKeyArr = {
                    //expectedTransactionKey
                    {"0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"},

                    //expectedAccountUpdateKey
                    {},

                    //expectedFeePayedKey
                    {"0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                            "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083"}
            };

            BigInteger[] weightArr = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

            WeightedMultiSigOptions expectedTransactionRoleOpt = new WeightedMultiSigOptions();
            WeightedMultiSigOptions expectedAccountUpdateRoleOpt = new WeightedMultiSigOptions();
            WeightedMultiSigOptions expectedFeePayerRoleOpt = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weightArr));

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.decode(encodedData);

            IAccountKey transactionKey = accountKeyRoleBased.getRoleTransactionKey();
            assertTrue(transactionKey instanceof AccountKeyPublic);
            checkAccountKeyPublic(expectedPublicKeyArr[0], expectedTransactionRoleOpt, (AccountKeyPublic)transactionKey);

            IAccountKey updateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
            assertTrue(updateKey instanceof AccountKeyNil);
            assertTrue(expectedAccountUpdateRoleOpt.isEmpty());

            IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig );
            checkAccountKeyWeightedMultiSig(expectedPublicKeyArr[2], expectedFeePayerRoleOpt, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-034
        @Test
        public void decodeString_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyRoleBased Tag");

            String invalidEncodedData = "0x06f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKeyRoleBased.decode(invalidEncodedData);
        }

        //CA-ACCOUNT-035
        @Test
        public void decodeByteArray_throwException() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid RLP-encoded AccountKeyRoleBased Tag");

            String invalidEncodedData = "0x06f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
            byte[] invalidEncodeDataArr = Numeric.hexStringToByteArray(invalidEncodedData);
            AccountKeyRoleBased.decode(invalidEncodeDataArr);
        }

        //CA-ACCOUNT-036
        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithUncompressedKey() {
            String[][] expectedPublicKey = {
                    {
                        "0xb86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9af36becba90c8de06fd06da16492263267a63720985f94fc5a027d0a26d25e6ae",
                        "0xe4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d3bc4d619872b96c091c741a15736a7ef12f530b7593038bbbfbf6c35deee8a34",
                    },
                    {
                        "0x1a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716afe39b6106fb9db360fb5779e2d346c8328698174831941586b11bdc3e755905",
                        "0x1427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f65f63b92fd60be61c5d176ae1771e7738e6a043af814b9af5d81137df29ee95f2",
                        "0x90fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea5fa9bd7d8bc5c66a9a08d77563458b004bbd1d594a3a12ef108cdc7c04c525a6",
                    },
                    {
                        "0x91245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02f2b0870653417943e795e7c8694c4f8be8af865b7a0224d1dec0bf8a1bf1b5a6",
                        "0x77e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f3570b1a104b67d1cd169bbf61dd557f15ab5ee8b661326096954caddadf34ae6ac8",
                        "0xd3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171cc4bd2ba7f0c969cd72bfa49c854d8ac2cf3d0edea7f0ce0fd31cf080374935d",
                        "0xcfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3ee47cd2b6bbb917658c5fd3d02b0ddf1242b1603d1acbde7812a7d9d684ed37a9",
                    }
            };
            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };
            WeightedMultiSigOptions transactionOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()]));
            WeightedMultiSigOptions accountUpdateOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()]));
            WeightedMultiSigOptions feePayerOpt = new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()]));

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionOpt);
            options.add(accountUpdateOpt);
            options.add(feePayerOpt);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);

            IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
            assertTrue(txKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()], transactionOpt, (AccountKeyWeightedMultiSig)txKey);

            IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()], accountUpdateOpt, (AccountKeyWeightedMultiSig)accountUpdateKey);

            IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()], feePayerOpt, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-037
        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithCompressedKey() {
            String[][] expectedPublicKey = {
                    {
                            "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                            "0x02e4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d",
                    },
                    {
                            "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                            "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                            "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                    },
                    {
                            "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                            "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                            "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                            "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                    }
            };

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };
            WeightedMultiSigOptions transactionOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()]));
            WeightedMultiSigOptions accountUpdateOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()]));
            WeightedMultiSigOptions feePayerOpt = new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()]));

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionOpt);
            options.add(accountUpdateOpt);
            options.add(feePayerOpt);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);

            IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
            assertTrue(txKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()], transactionOpt, (AccountKeyWeightedMultiSig)txKey);

            IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()], accountUpdateOpt, (AccountKeyWeightedMultiSig)accountUpdateKey);

            IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()], feePayerOpt, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-038
        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithAccountKeyNil() {
            String[][] expectedPublicKey = {
                    {
                    },
                    {
                            "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                            "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                            "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                    },
                    {
                            "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                            "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                            "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                            "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                    }
            };

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };
            WeightedMultiSigOptions transactionOpt = new WeightedMultiSigOptions();
            WeightedMultiSigOptions accountUpdateOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()]));
            WeightedMultiSigOptions feePayerOpt = new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()]));

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionOpt);
            options.add(accountUpdateOpt);
            options.add(feePayerOpt);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);

            IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
            assertTrue(txKey instanceof AccountKeyNil);
            assertTrue(transactionOpt.isEmpty());

            IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()], accountUpdateOpt, (AccountKeyWeightedMultiSig)accountUpdateKey);

            IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()], feePayerOpt, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-039
        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithAccountKeyNil_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid options: AccountKeyNil cannot have options.");

            String[][] expectedPublicKey = {
                    {
                    },
                    {
                            "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                            "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                            "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                    },
                    {
                            "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                            "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                            "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                            "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                    }
            };

            BigInteger[][] optionWeight = {
                    {BigInteger.ONE, BigInteger.ONE},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };
            WeightedMultiSigOptions transactionOpt = new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex()]));
            WeightedMultiSigOptions accountUpdateOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()]));
            WeightedMultiSigOptions feePayerOpt = new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()]));

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionOpt);
            options.add(accountUpdateOpt);
            options.add(feePayerOpt);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);
        }

        //CA-ACCOUNT-040
        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithAccountWeightedMultiSig_throwException() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid options : AccountKeyWeightedMultiSig must have options");

            String[][] expectedPublicKey = {
                    {
                            "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                            "0x02e4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d",
                    },
                    {
                            "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                            "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                            "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                    },
                    {
                            "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                            "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                            "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                            "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                    }
            };

            BigInteger[][] optionWeight = {
                    {},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                    {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
            };
            WeightedMultiSigOptions transactionOpt = new WeightedMultiSigOptions();
            WeightedMultiSigOptions accountUpdateOpt = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex()]));
            WeightedMultiSigOptions feePayerOpt = new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex()]));

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionOpt);
            options.add(accountUpdateOpt);
            options.add(feePayerOpt);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);
        }

        //CA-ACCOUNT-041
        @Test
        public void makeByConstructor() {
            String[] publicKey = {
                    "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                    "0x02e4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d",
            };

            BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE};
            WeightedMultiSigOptions option = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));
            AccountKeyWeightedMultiSig accountKeyWeightedMultiSig = AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKey, option);

            AccountKeyLegacy accountKeyLegacy = new AccountKeyLegacy();
            AccountKeyFail accountKeyFail = new AccountKeyFail();

            List<IAccountKey> accountKeys = new ArrayList<>();
            accountKeys.add(accountKeyLegacy);
            accountKeys.add(accountKeyFail);
            accountKeys.add(accountKeyWeightedMultiSig);

            AccountKeyRoleBased accountKeyRoleBased = new AccountKeyRoleBased(accountKeys);

            IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
            assertTrue(txKey instanceof AccountKeyLegacy);

            IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
            assertTrue(accountUpdateKey instanceof AccountKeyFail);

            IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
            assertTrue(feePayerKey instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(publicKey, option, (AccountKeyWeightedMultiSig)feePayerKey);
        }

        //CA-ACCOUNT-042
        @Test
        public void getRLPEncoding() {
            String expectedEncodedData = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
            String[][] publicKeyArr = {
                    //expectedTransactionKey
                    {"0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"},

                    //expectedAccountUpdateKey
                    {"0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                            "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                    },

                    //expectedFeePayedKey
                    {"0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                            "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083"}
            };

            List<String[]> list = Arrays.asList(publicKeyArr);
            BigInteger[] weightArr = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};

            WeightedMultiSigOptions transactionKeyOptions = new WeightedMultiSigOptions(); // empty WeightedMultiSigOptions
            WeightedMultiSigOptions accountUpdateKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(weightArr));
            WeightedMultiSigOptions feePayerKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(weightArr));

            List<WeightedMultiSigOptions> weightedMultiSigOptions = new ArrayList<>();
            weightedMultiSigOptions.add(transactionKeyOptions);
            weightedMultiSigOptions.add(accountUpdateKeyOption);
            weightedMultiSigOptions.add(feePayerKeyOption);

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(list, weightedMultiSigOptions);

            assertEquals(expectedEncodedData, accountKeyRoleBased.getRLPEncoding());
        }

        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithString() {
            String[][] expectedPublicKey = {
                    {
                        "fail"
                    },
                    {
                        "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e"
                    },
                    {
                        "legacy"
                    }
            };

            WeightedMultiSigOptions transactionKeyOption = new WeightedMultiSigOptions();
            WeightedMultiSigOptions accountUpdateKeyOption = new WeightedMultiSigOptions();
            WeightedMultiSigOptions feePayerKeyOption = new WeightedMultiSigOptions();

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionKeyOption);
            options.add(accountUpdateKeyOption);
            options.add(feePayerKeyOption);

            AccountKeyRoleBased roleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);
            assertTrue(roleBased.getRoleTransactionKey() instanceof AccountKeyFail);
            assertTrue(roleBased.getRoleAccountUpdateKey() instanceof AccountKeyPublic);
            checkPublicKey(expectedPublicKey[1][0], ((AccountKeyPublic) roleBased.getRoleAccountUpdateKey()).getPublicKey());
            assertTrue(roleBased.getRoleFeePayerKey() instanceof AccountKeyLegacy);
        }

        @Test
        public void fromRoleBasedPublicKeysAndOptionsWithStringAndWeightedMultiSig() {
            String[][] expectedPublicKey = {
                    {
                            "legacy"
                    },
                    {
                            "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                            "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
                    },
                    {
                            "fail"
                    }
            };

            BigInteger[] weightArr = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
            WeightedMultiSigOptions transactionKeyOption = new WeightedMultiSigOptions();
            WeightedMultiSigOptions accountUpdateKeyOption = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(weightArr));
            WeightedMultiSigOptions feePayerKeyOption = new WeightedMultiSigOptions();

            List<WeightedMultiSigOptions> options = new ArrayList<>();
            options.add(transactionKeyOption);
            options.add(accountUpdateKeyOption);
            options.add(feePayerKeyOption);

            AccountKeyRoleBased roleBased = AccountKeyRoleBased.fromRoleBasedPublicKeysAndOptions(Arrays.asList(expectedPublicKey), options);
            assertTrue(roleBased.getRoleTransactionKey() instanceof AccountKeyLegacy);
            assertTrue(roleBased.getRoleAccountUpdateKey() instanceof AccountKeyWeightedMultiSig);
            checkAccountKeyWeightedMultiSig(expectedPublicKey[1], accountUpdateKeyOption, (AccountKeyWeightedMultiSig)roleBased.getRoleAccountUpdateKey());
            assertTrue(roleBased.getRoleFeePayerKey() instanceof AccountKeyFail);
        }
    }
}
