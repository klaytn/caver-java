/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/WalletUtils.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.wallet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.utils.SecureRandomUtils;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.web3j.crypto.Hash.sha256;

public class KlayWalletUtils {

    public static final int ADDRESS_HEX_SIZE = 40;
    private static final int PRIVATE_KEY_HEX_SIZE = 64;

    public static final String CHECKSUM = "0x00";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    private static final Pattern HEX_STRING = Pattern.compile("^[0-9A-Fa-f]+$");

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateFullNewWalletFile(String address, String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(address, password, destinationDirectory, true);
    }

    public static String generateFullNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile("", password, destinationDirectory, true);
    }

    public static String generateNewWalletFile(String address, String password, File destinationDirectory)
            throws CipherException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, IOException {

        return generateNewWalletFile(address, password, destinationDirectory, false);
    }

    public static String generateNewWalletFile(String password, File destinationDirectory)
            throws CipherException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, IOException {

        return generateNewWalletFile("", password, destinationDirectory, false);
    }

    public static String generateNewWalletFile(
            String address, String password, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        return generateWalletFile(address, password, ecKeyPair, destinationDirectory, useFullScrypt);
    }

    private static String generateWalletFile(
            String address, String password, ECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException {

        WalletFile walletFile;
        if (useFullScrypt) {
            walletFile = Wallet.createFull(password, ecKeyPair, address);
        } else {
            walletFile = Wallet.createStandard(password, ecKeyPair, address);
        }

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);

        objectMapper.writeValue(destination, walletFile);

        return destination.getAbsolutePath();
    }

    /**
     * Generates a BIP-39 compatible Ethereum wallet. The private key for the wallet can
     * be calculated using following algorithm:
     * <pre>
     *     Key = SHA-256(BIP_39_SEED(mnemonic, password))
     * </pre>
     *
     * @param address              address
     * @param password             Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException     if the destination cannot be written to
     */
    public static Bip39Wallet generateBip39Wallet(String address, String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        String walletFile = generateWalletFile(address, password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static KlayCredentials loadCredentials(String password, String source)
            throws IOException, CipherException {
        return loadCredentials(password, new File(source));
    }

    public static KlayCredentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Wallet.decrypt(password, walletFile);
    }

    public static KlayCredentials loadCredentials(String klaytnWalletKey) {
        return KlayCredentials.createWithKlaytnWalletKey(klaytnWalletKey);
    }

    public static KlayCredentials loadBip39Credentials(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair ecKeyPair = ECKeyPair.create(sha256(seed));
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return KlayCredentials.create(ECKeyPair.create(sha256(seed)), address);
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return now.format(format) + walletFile.getAddress() + ".json";
    }

    public static String getBaobabKeyDirectory() {
        String directory = String.format("%s%sbaobab", getDefaultKeyDirectory(), File.separator);
        createDirectoryIfNotPresent(directory);
        return directory;
    }

    public static String getMainnetKeyDirectory() {
        String directory = String.format("%s%smainnet", getDefaultKeyDirectory(), File.separator);
        createDirectoryIfNotPresent(directory);
        return directory;
    }

    @Deprecated
    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() <= PRIVATE_KEY_HEX_SIZE && HEX_STRING.matcher(cleanPrivateKey).matches();
    }

    @Deprecated
    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_HEX_SIZE && HEX_STRING.matcher(cleanInput).matches();
    }

    public static String getDefaultKeyDirectory() {
        String defaultDirectory = getDefaultKeyDirectory(System.getProperty("os.name"));
        createDirectoryIfNotPresent(defaultDirectory);
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    private static void createDirectoryIfNotPresent(String defaultDirectory) {
        File file = new File(defaultDirectory);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException(
                        String.format("Default key directory is not created", defaultDirectory));
            }
        }
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return String.format("%s%sLibrary%sKlaytn%skeystore",
                    System.getProperty("user.home"), File.separator, File.separator, File.separator);
        } else if (osName.startsWith("win")) {
            return String.format("%s%sKlaytn%skeystore",
                    System.getenv("APPDATA"), File.separator, File.separator);
        } else {
            return String.format("%s%s.Klaytn%skeystore",
                    System.getProperty("user.home"), File.separator, File.separator);
        }
    }
}