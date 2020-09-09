package com.klaytn.caver.common;

import com.klaytn.caver.common.transaction.FeeDelegatedValueTransferTest;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UtilsTest.isAddressTest.class,
        UtilsTest.isValidPrivateKeyTest.class,
        UtilsTest.isKlaytnWalletKeyTest.class,
        UtilsTest.isValidPublicKeyTest.class,
        UtilsTest.decompressPublicKeyTest.class,
        UtilsTest.compressedPublicKeyTest.class,
        UtilsTest.hashMessageTest.class,
        UtilsTest.parseKlaytnWalletKeyTest.class,
        UtilsTest.isHexTest.class,
        UtilsTest.isHexStrictTest.class,
        UtilsTest.addHexPrefixTest.class,
        UtilsTest.stripHexPrefixTest.class,
        UtilsTest.convertToPebTest.class,
        UtilsTest.convertFromPebTest.class
})
public class UtilsTest {

    public static class isAddressTest {

        @Test
        public void validAddressTest() {
            String[] validAddress = new String[] {
                    "0xff6916ea19a50878e39c41aaadfeb0cab1b41dad",
                    "0x4834113481fbbac68565987d30f5216bc5719d3b",
                    "ff6916ea19a50878e39c41aaadfeb0cab1b41dad",
                    "4834113481fbbac68565987d30f5216bc5719d3b"
            };

            for(int i=0; i<validAddress.length; i++) {
                assertTrue(Utils.isAddress(validAddress[i]));
            }
        }

        @Test
        public void invalidAddressTest() {
            String[] invalidAddress = new String[] {
                    "0xff6916ea19a50878e39c41cab1b41da",// Length is not 40
                    "0xKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK", // Not Hex String
            };

            for(int i=0; i<invalidAddress.length; i++) {
                assertFalse(Utils.isAddress(invalidAddress[i]));
            }
        }
    }

    public static class isValidPrivateKeyTest {
        @Test
        public void validPrivateKey() {
            String key = PrivateKey.generate().getPrivateKey();

            assertTrue(Utils.isValidPrivateKey(key));
        }

        @Test
        public void invalidPrivateKey() {
            String[] invalidAddress = new String[] {
                    "0xff6916ea19a50878e39c41cab1b41d0xff6916ea19a50878e39c41cab1bdd41dK",// Length is not 64
                    "0xff6916ea19a50878e39c41cab1b41d0xff6916ea19a50878e39c41cab1bdd4KK", // Not Hex String
            };

            for(int i=0; i<invalidAddress.length; i++) {
                assertFalse(Utils.isAddress(invalidAddress[i]));
            }
        }
    }

    public static class isKlaytnWalletKeyTest {
        @Test
        public void validWalletKey() {
            String walletKey = KeyringFactory.generate().getKlaytnWalletKey();
            assertTrue(Utils.isKlaytnWalletKey(walletKey));
        }

        @Test
        public void inValidWalletKey() {
            String[] invalidWalletKey = new String[] {
                    "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x01" +"0xfc26de905386050894cddbb5a824318b96dde595", // invalid type
                    "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0xfc26de905386050894cddbb5a824318b96dde595", // no Type
                    "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x00" +"0xfc26de905386050894cddbb5a824318b96dde59", // invalid address - invalid length
                    "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x00" +"fc26de905386050894cddbb5a824318b96dde595", // invalid address - no prefix
                    "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9575a6433098481" + "0x00" +"0xfc26de905386050894cddbb5a824318b96dde595", // invalid privateKey - invalid length
                    "63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x00" +"0xfc26de905386050894cddbb5a824318b96dde595", // invalid type - no prefix
            };

            for(int i=0; i<invalidWalletKey.length; i++) {
                assertFalse(Utils.isAddress(invalidWalletKey[i]));
            }
        }
    }

    public static class isValidPublicKeyTest {
        @Test
        public void uncompressedKeyTest() {
            String key = KeyringFactory.generate().getPublicKey();
            assertTrue(Utils.isValidPublicKey(key));
        }

        @Test
        public void uncompressedKeyWithTagTest() {
            String key = "0x04019b186993b620455077b6bc37bf61666725d8d87ab33eb113ac0414cd48d78ff46e5ea48c6f22e8f19a77e5dbba9d209df60cbcb841b7e3e81fe444ba829831";
            assertTrue(Utils.isValidPublicKey(key));
        }

        @Test
        public void compressedKeyTest() {
            String key = KeyringFactory.generate().getPublicKey();
            key = Utils.compressPublicKey(key);
            assertTrue(Utils.isValidPublicKey(key));
        }

        @Test
        public void invalidLength_UncompressedKeyTest() {
            String invalidLengthKey = "0a7694872b7f0862d896780c476eefe5dcbcab6145853401f95a610bbbb0f726c1013a286500f3b524834eaeb383d1a882e16f4923cef8a5316c33772b3437";
            assertFalse(Utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidLength_CompressedKeyTest() {
//            String invalidLengthKey = "03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943"
            String invalidLengthKey = "0x03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b93";
            String key = KeyringFactory.generate().getPublicKey();
            key = Utils.compressPublicKey(key);

            assertFalse(Utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidIndicator_CompressedKeyTest() {
            String invalidLengthKey = "0x05434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943";
            assertFalse(Utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidPoint() {
            String invalidPointKey = "0x4be11ff42d8fc1954fb9ed52296db1657564c5e38517764664fb7cf4306a1e163a2686aa755dd0291aa2f291c3560ef4bf4b46c671983ff3e23f11a1b744ff4a";
            assertFalse(Utils.isValidPublicKey(invalidPointKey));
        }
    }

    public static class decompressPublicKeyTest {
        @Test
        public void decompressPublicKey() {
            String compressed = "03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943";
            String uncompressed = Utils.decompressPublicKey(compressed);

            assertTrue(Utils.isValidPublicKey(uncompressed));
        }

        @Test
        public void alreadyDecompressedKey() {
            String expectedUncompressed = PrivateKey.generate().getPublicKey(false);
            String actualUncompressed = Utils.decompressPublicKey(expectedUncompressed);
            assertTrue(Utils.isValidPublicKey(actualUncompressed));
            assertEquals(expectedUncompressed, actualUncompressed);
        }
    }

    public static class compressedPublicKeyTest {
        @Test
        public void compressedPublicKey() {
            String uncompressedKey = PrivateKey.generate().getPublicKey(false);
            String compressed = Utils.compressPublicKey(uncompressedKey);

            assertTrue(Utils.isValidPublicKey(compressed));
        }

        @Test
        public void alreadyCompressedKey() {
            String expectedCompressed = PrivateKey.generate().getPublicKey(true);
            String actualCompressed = Utils.compressPublicKey(expectedCompressed);
            assertTrue(Utils.isValidPublicKey(actualCompressed));
            assertEquals(expectedCompressed, actualCompressed);
        }
    }

    public static class hashMessageTest {
        @Test
        public void hashMessageTest() {
            String data = "0xdeadbeaf";
            String actual = Utils.hashMessage(data);
            assertEquals(66, actual.length());
        }
    }

    public static class parseKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void parseKlaytnWalletKey() {
            SingleKeyring keyring = KeyringFactory.generate();
            String walletKey = keyring.getKlaytnWalletKey();

            String[] parsedData = Utils.parseKlaytnWalletKey(walletKey);
            assertEquals(keyring.getKey().getPrivateKey(), parsedData[0]);
            assertEquals("0x00", parsedData[1]);
            assertEquals(keyring.getAddress(), parsedData[2]);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidType() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x01" +"0xfc26de905386050894cddbb5a824318b96dde595";
            String[] parsedData = Utils.parseKlaytnWalletKey(invalid);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidPrivateKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a64330984" + "0x00" +"0xfc26de905386050894cddbb5a824318b96dde595";
            String[] parsedData = Utils.parseKlaytnWalletKey(invalid);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidAddress() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x00" +"0xf05386050894cddbb5a824318b96dde595";
            String[] parsedData = Utils.parseKlaytnWalletKey(invalid);
        }
    }

    public static class isHexTest {
        @Test
        public void validHexTest() {
            String[] hex = new String[] {
                    "0x1234",
                    "ffff",
                    "0xaaaa",
                    "34567"
            };

            for(int i=0; i<hex.length; i++) {
                assertTrue(Utils.isHex(hex[i]));
            }
        }

        @Test
        public void invalidHexTest() {
            String invalidHex = "0xkkkkk";

            assertFalse(Utils.isHex(invalidHex));
        }
    }

    public static class isHexStrictTest {
        @Test
        public void validHexTest() {
            String[] hex = new String[] {
                    "0x1234",
                    "0xaaaa",
                    "0xffff"
            };

            for(int i=0; i<hex.length; i++) {
                assertTrue(Utils.isHexStrict(hex[i]));
            }
        }

        @Test
        public void invalidHexTest() {
            String[] hex = new String[] {
                    "0xKKKKKKK",
                    "1234",
            };

            for(int i=0; i<hex.length; i++) {
                assertFalse(Utils.isHexStrict(hex[i]));
            }
        }
    }

    public static class addHexPrefixTest {
        @Test
        public void addHexPrefixTest() {
            String hex = "1234";
            String expected = "0x1234";

            assertEquals(expected, Utils.addHexPrefix(hex));
        }

        @Test
        public void alreadyPrefixed() {
            String hex = "0x1234";

            assertEquals(hex, Utils.addHexPrefix(hex));
        }
    }

    public static class stripHexPrefixTest {
        @Test
        public void stripHexPrefixTest() {
            String hex = "0x1234";
            String expected = "1234";

            assertEquals(expected, Utils.stripHexPrefix(hex));
        }

        @Test
        public void alreadyStripedTest() {
            String hex = "1234";

            assertEquals(hex, Utils.stripHexPrefix(hex));
        }
    }

    public static class convertToPebTest {
        @Test
        public void from_peb() {
            String expected = "1";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "peb");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "peb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_peb_enum() {
            String expected = "1";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.peb);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.peb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_kpeb() {
            String expected = "1000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "kpeb");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "kpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_kpeb_enum() {
            String expected = "1000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.kpeb);
            assertEquals(expected, converted.toString());

            converted = Utils.convertToPeb("1", Utils.KlayUnit.kpeb);
            assertEquals(expected, converted.toString());
        }

        @Test
        public void from_Mpeb() {
            String expected = "1000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "Mpeb");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "Mpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Mpeb_enum() {
            String expected = "1000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_Gpeb() {
            String expected = "1000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "Gpeb");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "Gpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Gpeb_enum() {
            String expected = "1000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_ston() {
            String expected = "1000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "ston");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "ston");
            assertEquals(expected, converted);
        }

        @Test
        public void from_ston_enum() {
            String expected = "1000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.ston);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.ston);
            assertEquals(expected, converted);
        }

        @Test
        public void from_uKlay() {
            String expected = "1000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "uKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "uKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_uKlay_enum() {
            String expected = "1000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_mKlay() {
            String expected = "1000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "mKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "mKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_mKlay_enum() {
            String expected = "1000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_Klay() {
            String expected = "1000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "KLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "KLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Klay_enum() {
            String expected = "1000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_KKlay() {
            String expected = "1000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "kKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "kKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_KKlay_enum() {
            String expected = "1000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_MKlay() {
            String expected = "1000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "MKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "MKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_MKlay_enum() {
            String expected = "1000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_GKlay() {
            String expected = "1000000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "GKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "GKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_GKlay_enum() {
            String expected = "1000000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_TKlay() {
            String expected = "1000000000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, "TKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", "TKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_TKlay_enum() {
            String expected = "1000000000000000000000000000000";
            String converted;

            converted = Utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertToPeb("1", Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);
        }
    }

    public static class convertFromPebTest {
        @Test
        public void to_peb() {
            String amount = "1000000000000000000000000000";
            String expected = amount;

            String converted = Utils.convertFromPeb(amount, "peb");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "peb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_peb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = amount;

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.peb);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.peb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_kpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000000";

            String converted = Utils.convertFromPeb(amount, "kpeb");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "kpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_kpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.kpeb);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.kpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_Mpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000";

            String converted = Utils.convertFromPeb(amount, "Mpeb");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "Mpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_Mpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_Gpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = Utils.convertFromPeb(amount, "Gpeb");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "Gpeb");
            assertEquals(expected, converted);
        }
        @Test
        public void to_Gpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);
        }


        @Test
        public void to_ston() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = Utils.convertFromPeb(amount, "ston");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "ston");
            assertEquals(expected, converted);
        }

        @Test
        public void to_ston_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.ston);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.ston);
            assertEquals(expected, converted);
        }

        @Test
        public void to_uKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000";

            String converted = Utils.convertFromPeb(amount, "uKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "uKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_uKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_mKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000";

            String converted = Utils.convertFromPeb(amount, "mKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "mKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_mKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_KLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000";

            String converted = Utils.convertFromPeb(amount, "KLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "KLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_KLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_kKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000";

            String converted = Utils.convertFromPeb(amount, "kKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "kKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_kKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_MKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000";

            String converted = Utils.convertFromPeb(amount, "MKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "MKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_MKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_GKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1";

            String converted = Utils.convertFromPeb(amount, "GKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "GKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_GKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_TKLAY() {
            String amount = "1000000000000000000000000000000";
            String expected = "1";

            String converted = Utils.convertFromPeb(amount, "TKLAY");
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), "TKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_TKLAY_enum() {
            String amount = "1000000000000000000000000000000";
            String expected = "1";

            String converted = Utils.convertFromPeb(amount, Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);

            converted = Utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);
        }
    }

    public static class isNumberTest {
        @Test
        public void validHexNumber() {
            String[] valid = new String[] {
                    "0x1234",
                    "1234",
                    "aaaaaaa",
                    "0xaaaaaa"
            };

            for(int i=0; i<valid.length; i++) {
                assertTrue(Utils.isNumber(valid[i]));
            }
        }

        @Test
        public void invalidHexNumber() {
            String[] invalid = new String[] {
                    "kkkkkkkkkk",
                    "0x1234k"
            };

            for(int i=0; i<invalid.length; i++) {
                assertFalse(Utils.isNumber(invalid[i]));
            }
        }
    }

    public static class isEmptySigTest {
        @Test
        public void validEmptySig() {
            SignatureData emptySig = new SignatureData(
                    "0x01", "0x", "0x"
            );

            assertTrue(Utils.isEmptySig(emptySig));
        }

        @Test
        public void validEmptySigList() {
            SignatureData[] emptySigArr = new SignatureData[] {
                    new SignatureData("0x01", "0x", "0x"),
                    new SignatureData("0x01", "0x", "0x"),
            };

            assertTrue(Utils.isEmptySig(Arrays.asList(emptySigArr)));
        }

        @Test
        public void notEmptySigData() {
            SignatureData signatureData = new SignatureData(
                    "0x25",
                    "0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9",
                    "0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567"
            );

            assertFalse(Utils.isEmptySig(signatureData));
        }

        @Test
        public void notEmptySigDataList() {
            SignatureData[] signatureDataArr = new SignatureData[] {
                    new SignatureData(
                            "0x25",
                            "0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9",
                            "0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567"
                    ),
                    new SignatureData(
                            "0x25",
                            "0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9",
                            "0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567"
                    )
            };

            assertFalse(Utils.isEmptySig(Arrays.asList(signatureDataArr)));
        }
    }

    public static class generateRandomBytesTest {
        @Test
        public void generateRandomBytes() {
            byte[] arr = Utils.generateRandomBytes(32);

            assertEquals(32, arr.length);
        }
    }
}
