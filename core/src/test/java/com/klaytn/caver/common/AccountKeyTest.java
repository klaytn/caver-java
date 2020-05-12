package com.klaytn.caver.common;

import com.klaytn.caver.account.*;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({AccountKeyTest.AccountKeyFailTests.class, AccountKeyTest.AccountKeyLegacyTest.class, AccountKeyTest.AccountKeyPublicTest.class, AccountKeyTest.AccountKeyWeightedMultiSigTest.class})
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
                    publicKey = AccountKeyPublicUtils.decompressPublicKey(publicKey);
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
}
