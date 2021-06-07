/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.utils.wrapper;

import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.math.BigDecimal;
import java.security.SignatureException;
import java.util.List;

/**
 * Representing an UtilsWrapper
 * 1. This class wraps all static methods of Utils
 * 2. This class should be accessed via `caver.utils`
 */
public class UtilsWrapper {
    /**
     * Creates an UtilsWrapper
     */
    public UtilsWrapper() {
    }

    /**
     * Check if string has address format.
     * @param address An address string.
     * @return boolean
     */
    public boolean isAddress(String address) {
        return Utils.isAddress(address);
    }

    /**
     * Check if address has valid checksum.
     * @param address An address
     * @return boolean
     */
    public boolean checkAddressChecksum(String address) {
        return Utils.checkAddressChecksum(address);
    }

    /**
     * Check if string has PrivateKey format.
     * @param privateKey A key string.
     * @return boolean
     */
    public boolean isValidPrivateKey(String privateKey) {
        return Utils.isValidPrivateKey(privateKey);
    }

    /**
     * Check if string has Klaytn wallet key format.
     * @param key A key string.
     * @return boolean
     */
    public boolean isKlaytnWalletKey(String key) {
        return Utils.isKlaytnWalletKey(key);
    }

    /**
     * Check if the given public key is valid.
     * It also check both compressed and uncompressed key.
     * @param publicKey public key
     * @return valid or not
     */
    public boolean isValidPublicKey(String publicKey) {
        return Utils.isValidPublicKey(publicKey);
    }

    /**
     * Convert a compressed public key to an uncompressed format.
     * Given public key has already uncompressed format, it will return
     * @param publicKey public key string(uncompressed or compressed)
     * @return uncompressed public key string
     */
    public String decompressPublicKey(String publicKey) {
        return Utils.decompressPublicKey(publicKey);
    }

    /**
     * Convert an uncompressed public key to a compressed public key
     * Given public key has already compressed format, it will return.
     * @param publicKey public key string(uncompressed or compressed)
     * @return compressed public key
     */
    public String compressPublicKey(String publicKey) {
        return Utils.compressPublicKey(publicKey);
    }

    /**
     * Hashing message with added prefix("\x19Klaytn Signed Message:\n").
     * @param message A message to hash.
     * @return String
     */
    public String hashMessage(String message) {
        return Utils.hashMessage(message);
    }

    /**
     * Parse a klaytn wallet key string.
     * @param key klaytn wallet key string
     * @return String array
     */
    public String[] parseKlaytnWalletKey(String key) {
        return Utils.parseKlaytnWalletKey(key);
    }

    /**
     * Check if string has hex format.
     * @param input A hex string
     * @return boolean
     */
    public boolean isHex(String input) {
        return Utils.isHex(input);
    }

    /**
     * Check if string has hex format with "0x" prefix.
     * @param input A hex string
     * @return boolean
     */
    public boolean isHexStrict(String input) {
        return Utils.isHexStrict(input);
    }

    /**
     * Add hex prefix("0x").
     * @param str A hex string
     * @return String
     */
    public String addHexPrefix(String str) {
        return Utils.addHexPrefix(str);
    }

    /**
     * Remove hex prefix("0x").
     * @param str A hex string
     * @return String
     */
    public String stripHexPrefix(String str) {
        return Utils.stripHexPrefix(str);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public String convertToPeb(String num, String unit) {
        return Utils.convertToPeb(num, unit);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public String convertToPeb(BigDecimal num, String unit) {
        return Utils.convertToPeb(num, unit);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public String convertToPeb(String num, Utils.KlayUnit unit) {
        return Utils.convertToPeb(num, unit);
    }

    /**
     * Converts amount to peb amount.
     * @param num The amount to convert.
     * @param unit Th unit to convert from.
     * @return String
     */
    public String convertToPeb(BigDecimal num, Utils.KlayUnit unit) {
        return Utils.convertToPeb(num, unit);
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public String convertFromPeb(String num, String unit) {
        return Utils.convertFromPeb(num, unit);
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public String convertFromPeb(BigDecimal num, String unit) {
        return Utils.convertFromPeb(num, unit);
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public String convertFromPeb(String num, Utils.KlayUnit unit) {
        return Utils.convertFromPeb(num, unit);
    }

    /**
     * Converts peb amount to specific unit amount.
     * @param num The peb amount
     * @param unit The unit to convert to
     * @return String
     */
    public String convertFromPeb(BigDecimal num, Utils.KlayUnit unit) {
        return Utils.convertFromPeb(num, unit);
    }

    /**
     * Recovers the address that was used to sign the given data.
     * This function automatically creates a message hash by appending a Klaytn sign prefix to the message.
     * @param message A plain message when using signed.
     * @param signatureData The signature values in KlaySignatureData
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public String recover(String message, SignatureData signatureData) throws SignatureException {
        return Utils.recover(message, signatureData);
    }

    /**
     * Recovers the address that was used to sign the given data.
     * @param message A plain message or hashed message.
     * @param signatureData The signature values in KlaySignatureData
     * @param isPrefixed If true, the message param already hashed by appending a Klaytn sign prefix to the message.
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public String recover(String message, SignatureData signatureData, boolean isPrefixed) throws SignatureException {
        return Utils.recover(message, signatureData, isPrefixed);
    }

    /**
     * Check if string has hex number format.
     * @param input A hex number format string
     * @return boolean
     */
    public boolean isNumber(String input) {
        return Utils.isNumber(input);
    }

    /**
     * Check if SignatureData instance has empty signature.
     * @param signatureData A SignatureData instance.
     * @return boolean
     */
    public boolean isEmptySig(SignatureData signatureData) {
        return Utils.isEmptySig(signatureData);
    }

    /**
     * Check if elements in SignatureData list has empty signature
     * @param signatureDataList A List of SignatureData
     * @return boolean
     */
    public boolean isEmptySig(List<SignatureData> signatureDataList) {
        return Utils.isEmptySig(signatureDataList);
    }

    /**
     * Generate random bytes
     * @param size A size to generate random byte
     * @return byte array
     */
    public byte[] generateRandomBytes(int size) {
        return Utils.generateRandomBytes(size);
    }
}
