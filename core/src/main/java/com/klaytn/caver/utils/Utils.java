/*
 * Copyright 2020 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.utils;

import com.klaytn.caver.wallet.keyring.SignatureData;
import org.bouncycastle.math.ec.ECPoint;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static final int LENGTH_ADDRESS_STRING = 40;
    public static final int LENGTH_PRIVATE_KEY_STRING = 64;

    /**
     * Check if string has address format.
     * @param address An address string.
     * @return boolean
     */
    public static boolean isAddress(String address) {
        Pattern baseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$", Pattern.CASE_INSENSITIVE);
        Pattern lowerCase = Pattern.compile("^(0x|0X)?[0-9a-f]{40}$");
        Pattern upperCase = Pattern.compile("^(0x|0X)?[0-9A-F]{40}$");

        //check if it has the basic requirements of an address.
        if(!baseAddrPattern.matcher(address).matches()) {
            return false;
        }

        //check if it's ALL lowercase or ALL upppercase
        if(lowerCase.matcher(address).matches() || upperCase.matcher(address).matches()) {
            return true;
        }

        //check checksum address
        return checkAddressChecksum(address.replaceFirst("0X", "0x"));
    }

    /**
     * Check if address has valid checksum.
     * @param address An address
     * @return boolean
     */
    public static boolean checkAddressChecksum(String address) {
        return (Keys.toChecksumAddress(address).equals(Utils.addHexPrefix(address)));
    }

    /**
     * Check if string has PrivateKey format.
     * @param privateKey A key string.
     * @return boolean
     */
    public static boolean isValidPrivateKey(String privateKey) {
        String noHexPrefixKey = Numeric.cleanHexPrefix(privateKey);
        if(noHexPrefixKey.length() != LENGTH_PRIVATE_KEY_STRING && isHex(privateKey)) {
            return false;
        }

        ECPoint point = Sign.publicPointFromPrivate(Numeric.toBigInt(privateKey));
        return point.isValid();
    }

    /**
     * Check if string has Klaytn wallet key format.
     * @param key A key string.
     * @return boolean
     */
    public static boolean isKlaytnWalletKey(String key) {
        //0x{private key}0x{type}0x{address in hex}
        //[0] = privateKey
        //[1] = type - must be "00"
        //[2] = address
        key = Numeric.cleanHexPrefix(key);

        if(key.length() != 110) {
            return false;
        }

        String[] arr = key.split("0x");

        if(!arr[1].equals("00")) {
            return false;
        }
        if(!Utils.isAddress(arr[2])) {
            return false;
        }
        if(!Utils.isValidPrivateKey(arr[0])) {
            return false;
        }

        return true;
    }

    /**
     * Check if the given public key is valid.
     * It also check both compressed and uncompressed key.
     * @param publicKey public key
     * @return valid or not
     */
    public static boolean isValidPublicKey(String publicKey) {
        String noPrefixPubKey = Numeric.cleanHexPrefix(publicKey);
        boolean result;

        if(noPrefixPubKey.length() == 130 && noPrefixPubKey.startsWith("04")) {
            noPrefixPubKey = noPrefixPubKey.substring(2);
        }

        if(noPrefixPubKey.length() != 66 && noPrefixPubKey.length() != 128) {
            return false;
        }

        //Compressed Format
        if(noPrefixPubKey.length() == 66) {
            if(!noPrefixPubKey.startsWith("02") && !noPrefixPubKey.startsWith("03")) {
                return false;
            }
            result = AccountKeyPublicUtils.validateXYPoint(publicKey);
        } else { // Decompressed Format
            String x = noPrefixPubKey.substring(0, 64);
            String y = noPrefixPubKey.substring(64);

            result = AccountKeyPublicUtils.validateXYPoint(x, y);
        }

        return result;
    }

    /**
     * Convert a compressed public key to an uncompressed format.
     * Given public key has already uncompressed format, it will return
     * @param publicKey public key string(uncompressed or compressed)
     * @return uncompressed public key string
     */
    public static String decompressPublicKey(String publicKey) {
        if(AccountKeyPublicUtils.isUncompressedPublicKey(publicKey)) {
            return publicKey;
        }

        ECPoint ecPoint = AccountKeyPublicUtils.getECPoint(publicKey);
        String pointXY = Numeric.toHexStringWithPrefixZeroPadded(ecPoint.getAffineXCoord().toBigInteger(), 64) +
                Numeric.toHexStringNoPrefixZeroPadded(ecPoint.getAffineYCoord().toBigInteger(), 64);
        return pointXY;
    }

    /**
     * Convert an uncompressed public key to a compressed public key
     * Given public key has already compressed format, it will return.
     * @param publicKey public key string(uncompressed or compressed)
     * @return compressed public key
     */
    public static String compressPublicKey(String publicKey) {
        if(AccountKeyPublicUtils.isCompressedPublicKey(publicKey)){
            return publicKey;
        }

        String noPrefixKey = Numeric.cleanHexPrefix(publicKey);
        if(noPrefixKey.length() == 130 && noPrefixKey.startsWith("04")) {
            noPrefixKey = noPrefixKey.substring(2);
        }

        BigInteger publicKeyBN = Numeric.toBigInt(noPrefixKey);


        String publicKeyX = noPrefixKey.substring(0, 64);
        String pubKeyYPrefix = publicKeyBN.testBit(0) ? "03" : "02";
        return pubKeyYPrefix + publicKeyX;
    }

    /**
     * Hashing message with added prefix("\x19Klaytn Signed Message:\n").
     * @param message A message to hash.
     * @return String
     */
    public static String hashMessage(String message) {
        final String preamble = "\\x19Klaytn Signed Message:\\n";

        String klaytnMessage =  preamble + message.length() + message;
        return Hash.sha3(klaytnMessage);
    }

    /**
     * Parse a klaytn wallet key string.
     * @param key klaytn wallet key string
     * @return String array
     */
    public static String[] parseKlaytnWalletKey(String key) {
        if(!isKlaytnWalletKey(key)) {
            throw new IllegalArgumentException("Invalid Klaytn wallet key.");
        }

        //0x{private key}0x{type}0x{address in hex}
        //[0] = privateKey
        //[1] = type - must be "00"
        //[2] = address
        key = Numeric.cleanHexPrefix(key);
        String[] arr = key.split("0x");

        for(int i=0; i< arr.length; i++) {
            arr[i] = Numeric.prependHexPrefix(arr[i]);
        }

        return arr;
    }

    /**
     * Check if string has hex format.
     * @param input A hex string
     * @return boolean
     */
    public static boolean isHex(String input) {
        Pattern pattern = Pattern.compile("^(-0x|0x)?[0-9A-Fa-f]*$");
        return pattern.matcher(input).matches();
    }

    /**
     * Check if string has hex format with "0x" prefix.
     * @param input A hex string
     * @return boolean
     */
    public static boolean isHexStrict(String input) {
        Pattern pattern = Pattern.compile("^(-)?0x[0-9A-Fa-f]*$");
        return pattern.matcher(input).matches();
    }

    /**
     * Add hex prefix("0x").
     * @param str A hex string
     * @return String
     */
    public static String addHexPrefix(String str) {
        return Numeric.prependHexPrefix(str);
    }

    /**
     * Remove hex prefix("0x").
     * @param str A hex string
     * @return String
     */
    public static String stripHexPrefix(String str) {
        return Numeric.cleanHexPrefix(str);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public static String convertToPeb(String num, String unit) {
        return convertToPeb(new BigDecimal(num), KlayUnit.fromString(unit));
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public static String convertToPeb(BigDecimal num, String unit) {
        return convertToPeb(num, KlayUnit.fromString(unit));
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public static String convertToPeb(String num, KlayUnit unit) {
        return convertToPeb(new BigDecimal(num), unit);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public static String convertToPeb(BigDecimal num, KlayUnit unit) {
        return num.multiply(unit.getPebFactor()).toString();
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public static String convertFromPeb(String num, String unit) {
        return convertFromPeb(new BigDecimal(num), KlayUnit.fromString(unit));
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public static String convertFromPeb(BigDecimal num, String unit) {
        return convertFromPeb(num, KlayUnit.fromString(unit));
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public static String convertFromPeb(String num, KlayUnit unit) {
        return convertFromPeb(new BigDecimal(num), unit).toString();
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public static String convertFromPeb(BigDecimal num, KlayUnit unit) {
        return num.divide(unit.getPebFactor()).toString();
    }

    /**
     * Recovers the address that was used to sign the given data.
     * This function automatically creates a message hash by appending a Klaytn sign prefix to the message.
     * @param message A plain message when using signed.
     * @param signatureData The signature values in KlaySignatureData
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, SignatureData signatureData) throws SignatureException {
        return recover(message, signatureData, false);
    }


    /**
     * Recovers the address that was used to sign the given data.
     * @param message A plain message or hashed message.
     * @param signatureData The signature values in KlaySignatureData
     * @param isPrefixed If true, the message param already hashed by appending a Klaytn sign prefix to the message.
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, SignatureData signatureData, boolean isPrefixed) throws SignatureException {
        String messageHash = message;
        if(!isPrefixed) {
            messageHash = Utils.hashMessage(message);
        }

        byte[] r = Numeric.hexStringToByteArray(signatureData.getR());
        byte[] s = Numeric.hexStringToByteArray(signatureData.getS());

        if(r == null || r.length != 32) {
            throw new IllegalArgumentException("r must be 32 bytes");
        }
        if(s == null || s.length != 32) {
            throw new IllegalArgumentException("s must be 32 bytes");
        }

        int header = Numeric.toBigInt(signatureData.getV()).intValue() & 0xFF;
        // The header byte: 0x1B = first key with even y, 0x1C = first key with odd y,
        //                  0x1D = second key with even y, 0x1E = second key with odd y
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }

        ECDSASignature sig = new ECDSASignature(
                new BigInteger(1, r),
                new BigInteger(1, s));

        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(recId, sig, Numeric.hexStringToByteArray(messageHash));
        if (key == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return Numeric.prependHexPrefix(Keys.getAddress(key));
    }

    /**
     * Check if string has hex number format.
     * @param input A hex number format string
     * @return boolean
     */
    public static boolean isNumber(String input) {
        try {
            Numeric.toBigInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Check if SignatureData instance has empty signature.
     * @param signatureData A SignatureData instance.
     * @return boolean
     */
    public static boolean isEmptySig(SignatureData signatureData) {
        SignatureData emptySig = SignatureData.getEmptySignature();

        return emptySig.equals(signatureData);
    }

    /**
     * Check if elements in SignatureData list has empty signature
     * @param signatureDataList A List of SignatureData
     * @return boolean
     */
    public static boolean isEmptySig(List<SignatureData> signatureDataList) {
        SignatureData emptySig = SignatureData.getEmptySignature();

        //if stream is empty, allMatch() returns always true.
        boolean isMatched = signatureDataList.stream().allMatch(emptySig::equals);

        return isMatched;
    }

    /**
     * Generate random bytes
     * @param size A size to generate random byte
     * @return byte array
     */
    public static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        SecureRandomUtils.secureRandom().nextBytes(bytes);
        return bytes;
    }

    public enum KlayUnit {
        peb("peb", 0),
        kpeb("kpeb", 3),
        Mpeb("Mpeb", 6),
        Gpeb("Gpeb", 9),
        ston("ston", 9),
        uKLAY("uKLAY", 12),
        mKLAY("mKLAY", 15),
        KLAY("KLAY", 18),
        kKLAY("kKLAY", 21),
        MKLAY("MKLAY", 24),
        GKLAY("GKLAY", 27),
        TKLAY("TKLAY", 30);

        private String unit;
        private BigDecimal pebFactor;

        KlayUnit(String unit, int factor) {
            this.unit = unit;
            this.pebFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getPebFactor() {
            return pebFactor;
        }

        @Override
        public String toString() {
            return unit;
        }

        public static KlayUnit fromString(String unitName) {
            if (unitName != null) {
                for (KlayUnit unit : KlayUnit.values()) {
                    if (unitName.equals(unit.unit)) {
                        return unit;
                    }
                }
            }
            return KlayUnit.valueOf(unitName);
        }
    }
}
