/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.feature;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.wallet.KlayWalletUtils;
import com.klaytn.caver.wallet.Wallet;
import com.klaytn.caver.wallet.WalletFile;
import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.io.File;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class WalletTest {

    @Test
    public void testCreateWalletWithAddress() throws Exception {
        ECKeyPair keyPair = Keys.createEcKeyPair();

        WalletFile walletFile = Wallet.createStandard(
                "password",
                keyPair,
                Keys.getAddress(keyPair)
        );

        String keyDirectory = KlayWalletUtils.getBaobabKeyDirectory();
        File destinationDir = new File(keyDirectory);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                assertTrue("Unable to create destination directory [" + destinationDir.getAbsolutePath() + "], exiting...",
                        false);
            }
        }
        String fileName = getWalletFileName(walletFile);
        File destination = new File(keyDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.writeValue(destination, walletFile);

        // FIXME : Validate json file
        destination.delete();
    }

    @Test
    public void testCreateWallet() throws Exception {
        // Create Wallet File
        WalletFile walletFile = Wallet.createStandard(
                "password",
                Keys.createEcKeyPair()
        );

        String keyDirectory = KlayWalletUtils.getBaobabKeyDirectory();
        File destinationDir = new File(keyDirectory);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                assertTrue("Unable to create destination directory [" + destinationDir.getAbsolutePath() + "], exiting...",
                        false);
            }
        }
        String fileName = getWalletFileName(walletFile);
        File destination = new File(keyDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.writeValue(destination, walletFile);

        // FIXME : Validate json file
        destination.delete();
    }

    @Test
    public void testCreateWalletForRoleBased() throws Exception {
        testCreateWalletForCredentials(generateRoleBasedCredentials());
    }

    @Test
    public void testCreateWalletForMultiSig() throws Exception {
        testCreateWalletForCredentials(generateMultiSigCredentials());
    }

    private void testCreateWalletForCredentials(KlayCredentials credentials) throws Exception {
        String password = generateRandomPassword();
        KlayCredentials originalCredentials = credentials;
        WalletFile walletFile = Wallet.createFull(password, originalCredentials);

        String keyDirectory = KlayWalletUtils.getBaobabKeyDirectory();
        File destinationDir = new File(keyDirectory);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                assertTrue("Unable to create destination directory [" + destinationDir.getAbsolutePath() + "], exiting...",
                        false);
            }
        }
        String fileName = getWalletFileName(walletFile);
        File destination = new File(keyDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.writeValue(destination, walletFile);
        KlayCredentials loadCredentials = KlayWalletUtils.loadCredentials(password, destination.getPath());

        destination.delete();
        assertEquals(true, originalCredentials.equals(loadCredentials));
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        return now.format(format) + walletFile.getAddress() + ".json";
    }

    private KlayCredentials generateRoleBasedCredentials() throws Exception {
        Random random = new Random();
        String address = generateRandomHexString(40);

        List<ECKeyPair> transactionECKeyPairList = new ArrayList<>();
        List<ECKeyPair> updateECKeyPairList = new ArrayList<>();
        List<ECKeyPair> feePayerECKeyPairList = new ArrayList<>();

        int sizeTransactionKey = random.nextInt(10);
        for (int i = 0; i < sizeTransactionKey; i++) {
            transactionECKeyPairList.add(Keys.createEcKeyPair());
        }

        int sizeUpdateKey = random.nextInt(10);
        for (int i = 0; i < sizeUpdateKey; i++) {
            updateECKeyPairList.add(Keys.createEcKeyPair());
        }

        int sizeFeePayerKey = random.nextInt(10);
        if (sizeTransactionKey + sizeUpdateKey + sizeFeePayerKey == 0) {
            sizeFeePayerKey = 1;
        }
        for (int i = 0; i < sizeFeePayerKey; i++) {
            feePayerECKeyPairList.add(Keys.createEcKeyPair());
        }

        return KlayCredentials.create(transactionECKeyPairList, updateECKeyPairList, feePayerECKeyPairList, address);
    }

    private KlayCredentials generateMultiSigCredentials() throws Exception {
        Random random = new Random();
        String address = generateRandomHexString(40);

        List<ECKeyPair> transactionECKeyPairList = new ArrayList<>();

        int size = random.nextInt(9) + 1;
        for (int i = 0; i < size; i++) {
            transactionECKeyPairList.add(Keys.createEcKeyPair());
        }

        return KlayCredentials.create(transactionECKeyPairList, address);
    }

    private String generateRandomPassword() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            result.append((char) random.nextInt(128));
        }

        return result.toString();
    }

    private String generateRandomHexString(int size) {
        List<Character> hexChar = new ArrayList<>();
        for (char c = '0'; c <= '9' ; c++) {
            hexChar.add(new Character(c));
        }

        for (char c = 'a'; c <= 'f' ; c++) {
            hexChar.add(new Character(c));
        }

        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(hexChar.get(random.nextInt(16)));
        }

        return result.toString();
    }
}
