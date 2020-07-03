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
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/Wallet.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.wallet;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.utils.SecureRandomUtils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @deprecated please see below class and function
 * {@link com.klaytn.caver.wallet.keyring.AbstractKeyring#encrypt(String)}
 * {@link com.klaytn.caver.wallet.keyring.KeyringFactory#decrypt(String, String)}
 */
@Deprecated
public class Wallet {

    static final int PRIVATE_KEY_SIZE = 32;

    private static final int N_LIGHT = 1 << 12;
    private static final int P_LIGHT = 6;

    private static final int N_FULL = 1 << 18;
    private static final int P_FULL = 1;

    private static final int R = 8;
    private static final int DKLEN = 32;

    private static final int CURRENT_VERSION = 4;
    private static final int VERSION_3 = 3;

    private static final String CIPHER = "aes-128-ctr";
    static final String AES_128_CTR = "pbkdf2";
    static final String SCRYPT = "scrypt";

    public static WalletFile create(String password, ECKeyPair ecKeyPair, int n, int p, String address)
            throws CipherException {

        return createWalletFile(password, ecKeyPair, n, p, address);
    }

    public static WalletFile create(String password, ECKeyPair ecKeyPair, int n, int p)
            throws CipherException {

        return createWalletFile(password, ecKeyPair, n, p);
    }

    public static WalletFile create(String password, KlayCredentials klayCredentials, int n, int p)
            throws CipherException {

        return createWalletFile(password, klayCredentials, n, p);
    }

    public static WalletFile createFull(String password, ECKeyPair ecKeyPair, String address)
            throws CipherException {
        return create(password, ecKeyPair, N_FULL, P_FULL, address);
    }

    public static WalletFile createFull(String password, ECKeyPair ecKeyPair)
            throws CipherException {
        return create(password, ecKeyPair, N_FULL, P_FULL);
    }

    public static WalletFile createFull(String password, KlayCredentials klayCredentials)
            throws CipherException {
        return create(password, klayCredentials, N_FULL, P_FULL);
    }

    public static WalletFile createStandard(String password, ECKeyPair ecKeyPair, String address)
            throws CipherException {
        KlayCredentials klayCredentials = KlayCredentials.create(ecKeyPair, address);
        return create(password, ecKeyPair, N_LIGHT, P_LIGHT, address);
    }

    public static WalletFile createStandard(String password, KlayCredentials klayCredentials)
            throws CipherException {
        return create(password, klayCredentials, N_LIGHT, P_LIGHT);
    }

    public static WalletFile createStandard(String password, ECKeyPair ecKeyPair)
            throws CipherException {
        KlayCredentials klayCredentials = KlayCredentials.create(ecKeyPair);
        return create(password, klayCredentials, N_LIGHT, P_LIGHT);
    }

    private static WalletFile createWalletFile(
            String password,
            ECKeyPair ecKeyPair,
            int n, int p, String address) throws CipherException {

        WalletFile walletFile = new WalletFile();

        if (!address.isEmpty()) {
            walletFile.setAddress(address);
        } else {
            walletFile.setAddress(Keys.getAddress(ecKeyPair));
        }

        List<WalletFile.Crypto> multisigKeyRing = new ArrayList<>();
        WalletFile.Crypto crypto = generateCrypto(password, n, p, ecKeyPair);
        multisigKeyRing.add(crypto);

        walletFile.setKeyring(multisigKeyRing);
        walletFile.setId(UUID.randomUUID().toString());
        walletFile.setVersion(CURRENT_VERSION);

        return walletFile;
    }

    private static WalletFile createWalletFile(
            String password,
            KlayCredentials klayCredentials,
            int n, int p) throws CipherException {

        WalletFile walletFile = new WalletFile();
        walletFile.setAddress(klayCredentials.getAddress());

        List<List<WalletFile.Crypto>> cryptosList = new ArrayList<>();
        List<List<ECKeyPair>> ecKeyPairsList = klayCredentials.getRawEcKeyPairs();

        for (List<ECKeyPair> ecKeyPairs : ecKeyPairsList) {
            List<WalletFile.Crypto> cryptos = new ArrayList<>();
            for (ECKeyPair ecKeyPair : ecKeyPairs) {
                WalletFile.Crypto crypto = generateCrypto(password, n, p, ecKeyPair);
                cryptos.add(crypto);
            }
            cryptosList.add(cryptos);
        }

        boolean isMultisig = true;
        for (int i = 1 ; i < cryptosList.size() ; i++) {
            if (cryptosList.get(i).size() != 0) {
                isMultisig = false;
                break;
            }
        }

        List keyRing = isMultisig ? cryptosList.get(0) : cryptosList;
        
        walletFile.setKeyring(keyRing);
        walletFile.setId(UUID.randomUUID().toString());
        walletFile.setVersion(CURRENT_VERSION);

        return walletFile;
    }

    private static WalletFile createWalletFile(
            String password,
            ECKeyPair ecKeyPair,
            int n, int p) throws CipherException {
        return createWalletFile(password, ecKeyPair, n, p, "");
    }

    private static WalletFile.Crypto generateCrypto(String password, int n, int p, ECKeyPair ecKeyPair) throws CipherException {
        byte[] salt = generateRandomBytes(32);

        byte[] derivedKey = generateDerivedScryptKey(
                password.getBytes(UTF_8), salt, n, R, p, DKLEN);

        byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        byte[] iv = generateRandomBytes(16);

        byte[] privateKeyBytes =
                Numeric.toBytesPadded(ecKeyPair.getPrivateKey(), PRIVATE_KEY_SIZE);

        byte[] cipherText = performCipherOperation(
                Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes);

        byte[] mac = generateMac(derivedKey, cipherText);

        WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher(CIPHER);
        crypto.setCiphertext(Numeric.toHexStringNoPrefix(cipherText));

        WalletFile.CipherParams cipherParams = new WalletFile.CipherParams();
        cipherParams.setIv(Numeric.toHexStringNoPrefix(iv));
        crypto.setCipherparams(cipherParams);

        crypto.setKdf(SCRYPT);
        WalletFile.ScryptKdfParams kdfParams = new WalletFile.ScryptKdfParams();
        kdfParams.setDklen(DKLEN);
        kdfParams.setN(n);
        kdfParams.setP(p);
        kdfParams.setR(R);
        kdfParams.setSalt(Numeric.toHexStringNoPrefix(salt));
        crypto.setKdfparams(kdfParams);

        crypto.setMac(Numeric.toHexStringNoPrefix(mac));

        return crypto;
    }

    private static byte[] generateDerivedScryptKey(
            byte[] password, byte[] salt, int n, int r, int p, int dkLen) throws CipherException {
        return SCrypt.generate(password, salt, n, r, p, dkLen);
    }

    private static byte[] generateAes128CtrDerivedKey(
            byte[] password, byte[] salt, int c, String prf) throws CipherException {

        if (!prf.equals("hmac-sha256")) {
            throw new CipherException("Unsupported prf:" + prf);
        }

        // Java 8 supports this, but you have to convert the password to a character array, see
        // http://stackoverflow.com/a/27928435/3211687

        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(password, salt, c);
        return ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
    }

    private static byte[] performCipherOperation(
            int mode, byte[] iv, byte[] encryptKey, byte[] text) throws CipherException {

        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(mode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(text);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new CipherException("Error performing cipher operation", e);
        }
    }

    private static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
        byte[] result = new byte[16 + cipherText.length];

        System.arraycopy(derivedKey, 16, result, 0, 16);
        System.arraycopy(cipherText, 0, result, 16, cipherText.length);

        return Hash.sha3(result);
    }

    public static KlayCredentials decrypt(String password, WalletFile walletFile)
            throws CipherException {

        validate(walletFile);

        if (walletFile.getVersion() == VERSION_3){
            WalletFile.Crypto crypto = walletFile.getCrypto();
            ECKeyPair ecKeyPair = getECKeyPair(crypto, password);
            return KlayCredentials.create(ecKeyPair, Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(walletFile.getAddress()),40));
        }

        List keyRing = (List)walletFile.getKeyring();
        List<List<ECKeyPair>> ecKeyPairForRoleBased = new ArrayList<>();
        if (keyRing.get(0) instanceof WalletFile.Crypto) {
            List<ECKeyPair> ecKeyPairs = new ArrayList<>();
            for (WalletFile.Crypto crypto : (List<WalletFile.Crypto>)keyRing) {
                ECKeyPair ecKeyPair = getECKeyPair(crypto, password);
                ecKeyPairs.add(ecKeyPair);
            }
            ecKeyPairForRoleBased.add(ecKeyPairs);
        } else {
            for (List<WalletFile.Crypto> multiKeyRing : (List<List<WalletFile.Crypto>>) keyRing) {
                List<ECKeyPair> ecKeyPairs = new ArrayList<>();
                for (WalletFile.Crypto crypto : multiKeyRing) {
                    ECKeyPair ecKeyPair = getECKeyPair(crypto, password);
                    ecKeyPairs.add(ecKeyPair);
                }
                ecKeyPairForRoleBased.add(ecKeyPairs);
            }
        }

        return KlayCredentials.createRoleBased(ecKeyPairForRoleBased, Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(walletFile.getAddress()),40));
    }

    static ECKeyPair getECKeyPair(WalletFile.Crypto crypto, String password) throws CipherException {
        byte[] mac = Numeric.hexStringToByteArray(crypto.getMac());
        byte[] iv = Numeric.hexStringToByteArray(crypto.getCipherparams().getIv());
        byte[] cipherText = Numeric.hexStringToByteArray(crypto.getCiphertext());

        byte[] derivedKey;

        WalletFile.KdfParams kdfParams = crypto.getKdfparams();
        if (kdfParams instanceof WalletFile.ScryptKdfParams) {
            WalletFile.ScryptKdfParams scryptKdfParams =
                    (WalletFile.ScryptKdfParams) crypto.getKdfparams();
            int dklen = scryptKdfParams.getDklen();
            int n = scryptKdfParams.getN();
            int p = scryptKdfParams.getP();
            int r = scryptKdfParams.getR();
            byte[] salt = Numeric.hexStringToByteArray(scryptKdfParams.getSalt());
            derivedKey = generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, r, p, dklen);
        } else if (kdfParams instanceof WalletFile.Aes128CtrKdfParams) {
            WalletFile.Aes128CtrKdfParams aes128CtrKdfParams =
                    (WalletFile.Aes128CtrKdfParams) crypto.getKdfparams();
            int c = aes128CtrKdfParams.getC();
            String prf = aes128CtrKdfParams.getPrf();
            byte[] salt = Numeric.hexStringToByteArray(aes128CtrKdfParams.getSalt());

            derivedKey = generateAes128CtrDerivedKey(password.getBytes(UTF_8), salt, c, prf);
        } else {
            throw new CipherException("Unable to deserialize params: " + crypto.getKdf());
        }

        byte[] derivedMac = generateMac(derivedKey, cipherText);

        if (!Arrays.equals(derivedMac, mac)) {
            throw new CipherException("Invalid password provided");
        }

        byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        byte[] privateKey = performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText);
        return ECKeyPair.create(privateKey);
    }

    static void validate(WalletFile walletFile) throws CipherException {
        if (walletFile.getVersion() != CURRENT_VERSION && walletFile.getVersion() != VERSION_3) {
            throw new CipherException("Wallet version is not supported");
        }

        if (walletFile.getVersion() == VERSION_3) {
            WalletFile.Crypto crypto = walletFile.getCrypto();
            validateCrypto(crypto);
            return;
        }

        // Validate current version
        List keyRing = walletFile.getKeyring();
        if (keyRing.get(0) instanceof WalletFile.Crypto) {
            for (WalletFile.Crypto crypto : (List<WalletFile.Crypto>)keyRing) {
                validateCrypto(crypto);
            }
        } else {
            for (Object multiKeyRing : keyRing) {
                for (WalletFile.Crypto crypto : (List<WalletFile.Crypto>)multiKeyRing)  {
                    validateCrypto(crypto);
                }
            }
        }
    }


    static void validateCrypto(WalletFile.Crypto crypto) throws CipherException {
        if (!crypto.getCipher().equals(CIPHER)) {
            throw new CipherException("Wallet cipher is not supported");
        }

        if (!crypto.getKdf().equals(AES_128_CTR) && !crypto.getKdf().equals(SCRYPT)) {
            throw new CipherException("KDF type is not supported");
        }
    }

    static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        SecureRandomUtils.secureRandom().nextBytes(bytes);
        return bytes;
    }
}
