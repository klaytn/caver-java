package com.klaytn.caver.common.utils;

import com.klaytn.caver.Caver;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.MessageSigned;
import com.klaytn.caver.wallet.keyring.SignatureData;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.security.SignatureException;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class UtilsTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);
    public static class isAddressTest {
        @Test
        public void lowerCaseAddressTest() {
            String[] lowercase = new String[] {
                    "0xff6916ea19a50878e39c41aaadfeb0cab1b41dad",
                    "0x4834113481fbbac68565987d30f5216bc5719d3b",
                    "ff6916ea19a50878e39c41aaadfeb0cab1b41dad",
                    "4834113481fbbac68565987d30f5216bc5719d3b"
            };

            for(int i=0; i<lowercase.length; i++) {
                assertTrue(caver.utils.isAddress(lowercase[i]));
            }
        }

        @Test
        public void upperCaseAddressTest() {
            String[] uppercase = new String[] {
                    "0xFF6916EA19A50878E39C41AAADFEB0CAB1B41DAD",
                    "0x4834113481FBBAC68565987D30F5216BC5719D3B",
                    "4834113481FBBAC68565987D30F5216BC5719D3B",
                    "FF6916EA19A50878E39C41AAADFEB0CAB1B41DAD"
            };

            for(int i=0; i<uppercase.length; i++) {
                assertTrue(i+" index fail", caver.utils.isAddress(uppercase[i]));
            }
        }

        @Test
        public void checksumAddressTest() {
            String[] checksumAddress = new String[] {
                    "0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB",
                    "0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb",
                    "0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359",
                    "0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed"
            };

            for(int i=0; i<checksumAddress.length; i++) {
                assertTrue(i+" index fail", caver.utils.isAddress(checksumAddress[i]));
            }
        }

        @Test
        public void invalidAddressTest() {
            String[] invalidAddress = new String[] {
                    "0xff6916ea19a50878e39c41cab1b41da",// Length is not 40
                    "0xKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK", // Not Hex String
                    "0x2058a550ea824841e991ef386c3aD63D088303B3" // Invalid checksum address.
            };

            for(int i=0; i<invalidAddress.length; i++) {
                assertFalse(caver.utils.isAddress(invalidAddress[i]));
            }
        }
    }

    public static class isValidPrivateKeyTest {
        @Test
        public void validPrivateKey() {
            String key = caver.wallet.keyring.generateSingleKey();

            assertTrue(caver.utils.isValidPrivateKey(key));
        }

        @Test
        public void invalidPrivateKey() {
            String[] invalidAddress = new String[] {
                    "0xff6916ea19a50878e39c41cab1b41d0xff6916ea19a50878e39c41cab1bdd41dK",// Length is not 64
                    "0xff6916ea19a50878e39c41cab1b41d0xff6916ea19a50878e39c41cab1bdd4KK", // Not Hex String
            };

            for(int i=0; i<invalidAddress.length; i++) {
                assertFalse(caver.utils.isAddress(invalidAddress[i]));
            }
        }
    }

    public static class isKlaytnWalletKeyTest {
        @Test
        public void validWalletKey() {
            String walletKey = caver.wallet.keyring.generate().getKlaytnWalletKey();
            assertTrue(caver.utils.isKlaytnWalletKey(walletKey));
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
                assertFalse(caver.utils.isAddress(invalidWalletKey[i]));
            }
        }
    }

    public static class isValidPublicKeyTest {
        @Test
        public void uncompressedKeyTest() {
            String key = caver.wallet.keyring.generate().getPublicKey();
            assertTrue(caver.utils.isValidPublicKey(key));
        }

        @Test
        public void uncompressedKeyWithTagTest() {
            String key = "0x04019b186993b620455077b6bc37bf61666725d8d87ab33eb113ac0414cd48d78ff46e5ea48c6f22e8f19a77e5dbba9d209df60cbcb841b7e3e81fe444ba829831";
            assertTrue(caver.utils.isValidPublicKey(key));
        }

        @Test
        public void compressedKeyTest() {
            String key = caver.wallet.keyring.generate().getPublicKey();
            key = caver.utils.compressPublicKey(key);
            assertTrue(caver.utils.isValidPublicKey(key));
        }

        @Test
        public void invalidLength_UncompressedKeyTest() {
            String invalidLengthKey = "0a7694872b7f0862d896780c476eefe5dcbcab6145853401f95a610bbbb0f726c1013a286500f3b524834eaeb383d1a882e16f4923cef8a5316c33772b3437";
            assertFalse(caver.utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidLength_CompressedKeyTest() {
//            String invalidLengthKey = "03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943"
            String invalidLengthKey = "0x03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b93";
            String key = caver.wallet.keyring.generate().getPublicKey();
            key = caver.utils.compressPublicKey(key);

            assertFalse(caver.utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidIndicator_CompressedKeyTest() {
            String invalidLengthKey = "0x05434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943";
            assertFalse(caver.utils.isValidPublicKey(invalidLengthKey));
        }

        @Test
        public void invalidPoint() {
            String invalidPointKey = "0x4be11ff42d8fc1954fb9ed52296db1657564c5e38517764664fb7cf4306a1e163a2686aa755dd0291aa2f291c3560ef4bf4b46c671983ff3e23f11a1b744ff4a";
            assertFalse(caver.utils.isValidPublicKey(invalidPointKey));
        }
    }

    public static class decompressPublicKeyTest {
        @Test
        public void decompressPublicKey() {
            String compressed = "03434dedfc2eceed1e98fddfde3ebc57512c57f017195988cd5de62b722656b943";
            String uncompressed = caver.utils.decompressPublicKey(compressed);

            assertTrue(caver.utils.isValidPublicKey(uncompressed));
        }

        @Test
        public void alreadyDecompressedKey() {
            String expectedUncompressed = caver.wallet.keyring.generate().getPublicKey(false);
            String actualUncompressed = caver.utils.decompressPublicKey(expectedUncompressed);
            assertTrue(caver.utils.isValidPublicKey(actualUncompressed));
            assertEquals(expectedUncompressed, actualUncompressed);
        }

        @Test
        public void alreadyDecompressedKeyWithTag() {
            String expected = "0x04019b186993b620455077b6bc37bf61666725d8d87ab33eb113ac0414cd48d78ff46e5ea48c6f22e8f19a77e5dbba9d209df60cbcb841b7e3e81fe444ba829831";
            String key = caver.utils.decompressPublicKey(expected);

            assertEquals(expected, key);
        }
    }

    public static class compressedPublicKeyTest {
        @Test
        public void compressedPublicKey() {
            String uncompressedKey = caver.wallet.keyring.generate().getPublicKey(false);
            String compressed = caver.utils.compressPublicKey(uncompressedKey);

            assertTrue(caver.utils.isValidPublicKey(compressed));
        }

        @Test
        public void alreadyCompressedKey() {
            String expectedCompressed = caver.wallet.keyring.generate().getPublicKey(true);
            String actualCompressed = caver.utils.compressPublicKey(expectedCompressed);
            assertTrue(caver.utils.isValidPublicKey(actualCompressed));
            assertEquals(expectedCompressed, actualCompressed);
        }

        @Test
        public void compressedPublicKeyWithTag() {
            String key = "0x04019b186993b620455077b6bc37bf61666725d8d87ab33eb113ac0414cd48d78ff46e5ea48c6f22e8f19a77e5dbba9d209df60cbcb841b7e3e81fe444ba829831";
            String expected = caver.utils.compressPublicKey("019b186993b620455077b6bc37bf61666725d8d87ab33eb113ac0414cd48d78ff46e5ea48c6f22e8f19a77e5dbba9d209df60cbcb841b7e3e81fe444ba829831");

            assertEquals(expected, caver.utils.compressPublicKey(key));
        }
    }

    public static class hashMessageTest {
        @Test
        public void hashMessageTest() {
            String[] message = {"some data", "test data", "caver-java", "sign data", "0xaabbccdd", "0x11223344", "0x33445566"};
            String[] expected = {
                    "0x5373135dbda1d8f2eaee73adcc07d03cd3bbc2510b3bfdbd4fcf9de2f34c505e",
                    "0x416066097cb5808c62e1d7129249b37e661838ed49d6c3347a4a3f290020e952",
                    "0x096f523188dc1ddc620db4ec828733c0db209491849b85bb6bbd31d1498239cf",
                    "0x4c4865cee1cda8a2a3130be37a7739324a78ea36f39b18ae7dfecee5e5ce6fa2",
                    "0x4f8b23edb1db90554d99eb9667a950c78445f5efc6aaccb4d0243ee82f89b475",
                    "0x18ecf869a4437f5d036af0b25b2cb43362f857e19394e32e148439c99c768d1e",
                    "0x257527206116286147dd9827b21905add9c42ab16034ae6d6e9c2954199d0f08"
            };

            for(int i=0; i<message.length; i++) {
                assertEquals(expected[i], Utils.hashMessage(message[i]));
            }
        }


    }

    public static class parseKlaytnWalletKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void parseKlaytnWalletKey() {
            SingleKeyring keyring = caver.wallet.keyring.generate();
            String walletKey = keyring.getKlaytnWalletKey();

            String[] parsedData = caver.utils.parseKlaytnWalletKey(walletKey);
            assertEquals(keyring.getKey().getPrivateKey(), parsedData[0]);
            assertEquals("0x00", parsedData[1]);
            assertEquals(keyring.getAddress(), parsedData[2]);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidType() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x01" +"0xfc26de905386050894cddbb5a824318b96dde595";
            String[] parsedData = caver.utils.parseKlaytnWalletKey(invalid);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidPrivateKey() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a64330984" + "0x00" +"0xfc26de905386050894cddbb5a824318b96dde595";
            String[] parsedData = caver.utils.parseKlaytnWalletKey(invalid);
        }

        @Test
        public void invalidKlaytnWalletKey_invalidAddress() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid Klaytn wallet key.");

            String invalid = "0x63526af77dc34846a0909e5486f972c4a07074f0c94a2b9577675a6433098481" + "0x00" +"0xf05386050894cddbb5a824318b96dde595";
            String[] parsedData = caver.utils.parseKlaytnWalletKey(invalid);
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
                assertTrue(caver.utils.isHex(hex[i]));
            }
        }

        @Test
        public void invalidHexTest() {
            String invalidHex = "0xkkkkk";

            assertFalse(caver.utils.isHex(invalidHex));
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
                assertTrue(caver.utils.isHexStrict(hex[i]));
            }
        }

        @Test
        public void invalidHexTest() {
            String[] hex = new String[] {
                    "0xKKKKKKK",
                    "1234",
            };

            for(int i=0; i<hex.length; i++) {
                assertFalse(caver.utils.isHexStrict(hex[i]));
            }
        }
    }

    public static class addHexPrefixTest {
        @Test
        public void addHexPrefixTest() {
            String hex = "1234";
            String expected = "0x1234";

            assertEquals(expected, caver.utils.addHexPrefix(hex));
        }

        @Test
        public void alreadyPrefixed() {
            String hex = "0x1234";

            assertEquals(hex, caver.utils.addHexPrefix(hex));
        }
    }

    public static class stripHexPrefixTest {
        @Test
        public void stripHexPrefixTest() {
            String hex = "0x1234";
            String expected = "1234";

            assertEquals(expected, caver.utils.stripHexPrefix(hex));
        }

        @Test
        public void alreadyStripedTest() {
            String hex = "1234";

            assertEquals(hex, caver.utils.stripHexPrefix(hex));
        }
    }

    public static class convertToPebTest {
        @Test
        public void from_peb() {
            String expected = "1";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "peb");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "peb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_peb_enum() {
            String expected = "1";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.peb);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.peb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_kpeb() {
            String expected = "1000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "kpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "kpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_kpeb_enum() {
            String expected = "1000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.kpeb);
            assertEquals(expected, converted.toString());

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.kpeb);
            assertEquals(expected, converted.toString());
        }

        @Test
        public void from_Mpeb() {
            String expected = "1000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "Mpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "Mpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Mpeb_enum() {
            String expected = "1000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_Gpeb() {
            String expected = "1000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "Gpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "Gpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Gpeb_enum() {
            String expected = "1000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void from_ston() {
            String expected = "1000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "ston");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "ston");
            assertEquals(expected, converted);
        }

        @Test
        public void from_ston_enum() {
            String expected = "1000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.ston);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.ston);
            assertEquals(expected, converted);
        }

        @Test
        public void from_uKlay() {
            String expected = "1000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "uKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "uKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_uKlay_enum() {
            String expected = "1000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_mKlay() {
            String expected = "1000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "mKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "mKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_mKlay_enum() {
            String expected = "1000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_Klay() {
            String expected = "1000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "KLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "KLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_Klay_enum() {
            String expected = "1000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_KKlay() {
            String expected = "1000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "kKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "kKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_KKlay_enum() {
            String expected = "1000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_MKlay() {
            String expected = "1000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "MKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "MKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_MKlay_enum() {
            String expected = "1000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_GKlay() {
            String expected = "1000000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "GKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "GKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_GKlay_enum() {
            String expected = "1000000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void from_TKlay() {
            String expected = "1000000000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, "TKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", "TKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void from_TKlay_enum() {
            String expected = "1000000000000000000000000000000";
            String converted;

            converted = caver.utils.convertToPeb(BigDecimal.ONE, Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertToPeb("1", Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);
        }
    }

    public static class convertFromPebTest {
        @Test
        public void to_peb() {
            String amount = "1000000000000000000000000000";
            String expected = amount;

            String converted = caver.utils.convertFromPeb(amount, "peb");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "peb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_peb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = amount;

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.peb);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.peb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_kpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, "kpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "kpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_kpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.kpeb);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.kpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_Mpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, "Mpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "Mpeb");
            assertEquals(expected, converted);
        }

        @Test
        public void to_Mpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.Mpeb);
            assertEquals(expected, converted);
        }

        @Test
        public void to_Gpeb() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, "Gpeb");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "Gpeb");
            assertEquals(expected, converted);
        }
        @Test
        public void to_Gpeb_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.Gpeb);
            assertEquals(expected, converted);
        }


        @Test
        public void to_ston() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, "ston");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "ston");
            assertEquals(expected, converted);
        }

        @Test
        public void to_ston_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.ston);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.ston);
            assertEquals(expected, converted);
        }

        @Test
        public void to_uKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000";

            String converted = caver.utils.convertFromPeb(amount, "uKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "uKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_uKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.uKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_mKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000";

            String converted = caver.utils.convertFromPeb(amount, "mKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "mKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_mKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.mKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_KLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000";

            String converted = caver.utils.convertFromPeb(amount, "KLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "KLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_KLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.KLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_kKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000";

            String converted = caver.utils.convertFromPeb(amount, "kKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "kKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_kKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.kKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_MKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1000";

            String converted = caver.utils.convertFromPeb(amount, "MKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "MKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_MKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1000";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.MKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_GKLAY() {
            String amount = "1000000000000000000000000000";
            String expected = "1";

            String converted = caver.utils.convertFromPeb(amount, "GKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "GKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_GKLAY_enum() {
            String amount = "1000000000000000000000000000";
            String expected = "1";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.GKLAY);
            assertEquals(expected, converted);
        }

        @Test
        public void to_TKLAY() {
            String amount = "1000000000000000000000000000000";
            String expected = "1";

            String converted = caver.utils.convertFromPeb(amount, "TKLAY");
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), "TKLAY");
            assertEquals(expected, converted);
        }

        @Test
        public void to_TKLAY_enum() {
            String amount = "1000000000000000000000000000000";
            String expected = "1";

            String converted = caver.utils.convertFromPeb(amount, Utils.KlayUnit.TKLAY);
            assertEquals(expected, converted);

            converted = caver.utils.convertFromPeb(new BigDecimal(amount), Utils.KlayUnit.TKLAY);
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
                assertTrue(caver.utils.isNumber(valid[i]));
            }
        }

        @Test
        public void invalidHexNumber() {
            String[] invalid = new String[] {
                    "kkkkkkkkkk",
                    "0x1234k"
            };

            for(int i=0; i<invalid.length; i++) {
                assertFalse(caver.utils.isNumber(invalid[i]));
            }
        }
    }

    public static class isEmptySigTest {
        @Test
        public void validEmptySig() {
            SignatureData emptySig = new SignatureData(
                    "0x01", "0x", "0x"
            );

            assertTrue(caver.utils.isEmptySig(emptySig));
        }

        @Test
        public void validEmptySigList() {
            SignatureData[] emptySigArr = new SignatureData[] {
                    new SignatureData("0x01", "0x", "0x"),
                    new SignatureData("0x01", "0x", "0x"),
            };

            assertTrue(caver.utils.isEmptySig(Arrays.asList(emptySigArr)));
        }

        @Test
        public void notEmptySigData() {
            SignatureData signatureData = new SignatureData(
                    "0x25",
                    "0xb2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9",
                    "0x29da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567"
            );

            assertFalse(caver.utils.isEmptySig(signatureData));
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

            assertFalse(caver.utils.isEmptySig(Arrays.asList(signatureDataArr)));
        }
    }

    public static class generateRandomBytesTest {
        @Test
        public void generateRandomBytes() {
            byte[] arr = caver.utils.generateRandomBytes(32);

            assertEquals(32, arr.length);
        }
    }

    public static class recoverTest {
        public void checkAddress(String expect, String actual) {
            expect = Numeric.prependHexPrefix(expect);
            actual = Numeric.prependHexPrefix(actual);

            assertEquals(expect, actual);
        }

        @Test
        public void withMessageAndSignature() throws SignatureException {
            String expectedSignedMessage = "0xc69018da9396c4b87947e0784625af7475caf46e2af9cf57a44673ff0f625258642d8993751ae67271bcc131aa065adccf9f16fc4953f9c48f4a80d675c09ae81b";
            String expectedAddress = "0xb6a1f97502431e6f8d701f9e192c3cc43c07351a";
            String message = "Klaytn Test";

            SignatureData signatureData = caver.utils.decodeSignature(expectedSignedMessage);
            String actualAddress = caver.utils.recover(message, signatureData);

            checkAddress(expectedAddress, actualAddress);
        }

        @Test
        public void alreadyPrefix() throws SignatureException {
            SingleKeyring keyring = KeyringFactory.generate();
            String message = "Some data";

            MessageSigned signed = keyring.signMessage(message, 0, 0);
            String actualAddress = caver.utils.recover(signed.getMessageHash(), signed.getSignatures().get(0), true);

            checkAddress(keyring.getAddress(), actualAddress);
        }
    }

    public static class decodeSignatureTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void decodeSignatureTest() {
            String[] rawSigDataArr = {
                    "0xc69018da9396c4b87947e0784625af7475caf46e2af9cf57a44673ff0f625258642d8993751ae67271bcc131aa065adccf9f16fc4953f9c48f4a80d675c09ae81b",
                    "0x4c78ba080e717534772c4a9714b06a12f8d41062fca72885dafa8f1e1d6d78de35a50522df6361d16c05d1368bb9d86da1054f153301d5dedc6658d222616edd1b",
                    "0xacfc5c417a8506eb1bd8394553fbde4a9097ea854bdbbe0de2bfaebcc9a26f45521773632317323f3d3da09bf06185af1ee0481ef0d1abb8a790f3a110eadfc31c"
            };

            SignatureData[] signatureData = {
                    new SignatureData("1b", "c69018da9396c4b87947e0784625af7475caf46e2af9cf57a44673ff0f625258", "642d8993751ae67271bcc131aa065adccf9f16fc4953f9c48f4a80d675c09ae8"),
                    new SignatureData("1b", "4c78ba080e717534772c4a9714b06a12f8d41062fca72885dafa8f1e1d6d78de", "35a50522df6361d16c05d1368bb9d86da1054f153301d5dedc6658d222616edd"),
                    new SignatureData("1c", "acfc5c417a8506eb1bd8394553fbde4a9097ea854bdbbe0de2bfaebcc9a26f45", "521773632317323f3d3da09bf06185af1ee0481ef0d1abb8a790f3a110eadfc3")
            };

            for(int i=0; i < rawSigDataArr.length; i++) {
                assertEquals(signatureData[i], caver.utils.decodeSignature(rawSigDataArr[i]));
            }
        }

        @Test
        public void throwException_invalidLength() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Invalid signature data. The sig data length must 65 byte.");

            String rawSigData = "0xaaaaaa";
            caver.utils.decodeSignature(rawSigData);
        }
    }

    public static class recoverPublicKeyTest {
        @Test
        public void recoverWithMessage() throws SignatureException {
            SingleKeyring keyring = caver.wallet.keyring.generate();

            String message = "Some data";
            MessageSigned data = keyring.signMessage(message, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            String actual =  caver.utils.recoverPublicKey(data.getMessage(), data.getSignatures().get(0));
            assertEquals(keyring.getPublicKey(), actual);
        }

        @Test
        public void recoverWithMessageHash() throws SignatureException {
            SingleKeyring keyring = caver.wallet.keyring.generate();

            String message = "Some data";
            MessageSigned data = keyring.signMessage(message, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

            String actual =  caver.utils.recoverPublicKey(data.getMessage(), data.getSignatures().get(0), true);
            assertEquals(Utils.addHexPrefix(keyring.getPublicKey()), actual);
        }

        @Test
        public void recoverWithSampleData() throws SignatureException {
            String expected = "0xb5df4d5e6b4ee7a136460b911a69030fdd42c18ed067bcc2e25eda1b851314fad994c5fe946aad01ca2e348d4ff3094960661a8bc095f358538af54aeea48ff3";

            String message = "Some Message";
            SignatureData signatureData = new SignatureData(
                    "0x1b",
                    "0x8213e560e7bbe1f2e28fd69cbbb41c9108b84c98cd7c2c88d3c8e3549fd6ab10",
                    "0x3ca40c9e20c1525348d734a6724db152b9244bff6e0ff0c2b811d61d8f874f00"
            );

            String actual = caver.utils.recoverPublicKey(message, signatureData);
            assertEquals(expected, actual);
        }
    }

    public static class publicKeyToAddress {
        @Test
        public void publicKeyToAddressWithDecompressedFormat() {
            String expected = "0x5b2840bcbc2be07fb12d9129ed3a02d8e4465944";
            String publicKey = "0x68ffedd4a1d9fefa38f6ed9d58f0b85741a90ad604ab901c130c1fea42eab666dec186a48ad4db56b14898e8e18fe0176d926a2c1ffeeb6b6df805ec0bf41eb8";

            String actual = caver.utils.publicKeyToAddress(publicKey);
            assertEquals(expected, actual);
        }

        @Test
        public void publicKeyToAddressWithCompressedFormat() {
            String expected = "0x5b2840bcbc2be07fb12d9129ed3a02d8e4465944";
            String publicKey = "0x68ffedd4a1d9fefa38f6ed9d58f0b85741a90ad604ab901c130c1fea42eab666dec186a48ad4db56b14898e8e18fe0176d926a2c1ffeeb6b6df805ec0bf41eb8";

            publicKey = Utils.compressPublicKey(publicKey);
            String actual = caver.utils.publicKeyToAddress(publicKey);
            assertEquals(expected, actual);
        }
    }
}
