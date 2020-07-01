package com.klaytn.caver.wallet.keyring;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.klaytn.caver.utils.Utils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Represents a KeyStore DTO(Data transfer Object) class according to KIP-3.
 * For more details, please see below link.
 * http://kips.klaytn.com/KIPs/kip-3
 */
public class KeyStore {
    /**
     * Keystore Format V4 version
     */
    public static final int KEY_STORE_VERSION_V3 = 3;

    /**
     * Keystore Format V3 version
     */
    public static final int KEY_STORE_VERSION_V4 = 4;

    /**
     * An address in KeyStore
     */
    private String address;

    /**
     * An crypto in KeyStore V3
     */
    private KeyStore.Crypto crypto;

    /**
     * An keyring in KeyStore V4
     */
    private List keyRing;

    /**
     * An ID in KeyStore
     */
    private String id;

    /**
     * A Version in KeyStore
     */
    private int version;

    /**
     * Creates a KeyStore instance.
     */
    public KeyStore() {
    }

    /**
     * Setter function of keyRing.
     * @param keyRing
     */
    @JsonSetter("keyring")
    @JsonDeserialize(using = KeyStore.KeyRingDeserializer.class)
    public void setKeyring(List keyRing) {
        this.keyRing = keyRing;
    }

    /**
     * Getter function of keyRing.
     * @return List
     */
    public List getKeyring() {
        return this.keyRing;
    }

    /**
     * Getter function of address.
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter function of address.
     * @param address An address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter function of crypto.
     * @return KeyStore.Crypto
     */
    public KeyStore.Crypto getCrypto() {
        return crypto;
    }

    /**
     * Setter function of crypto. for KeyStore Format V3.
     * @param crypto KeyStore.Crypto.
     */
    @JsonSetter("crypto")
    public void setCrypto(KeyStore.Crypto crypto) {
        this.crypto = crypto;
    }

    /**
     * Setter function of Crypto. for KeyStore Format V1.
     * @param crypto KeyStore.Crypto
     */
    @JsonSetter("Crypto")  // older wallet files may have this attribute name
    public void setCryptoV1(KeyStore.Crypto crypto) {
        setCrypto(crypto);
    }

    /**
     * Getter function for id.
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Setter function for id.
     * @param id An id in KeyStore
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter function for version.
     * @return integer
     */
    public int getVersion() {
        return version;
    }

    /**
     * Setter function for version
     * @param version version in KeyStore
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Represent a Crypto data in KeyStore
     * It represents a 'keyring' in KeyStore Format V4
     *   - If role-based Keyring : It has a List that has Crypto list.
     *   - If other type keyring : It has a List that has Crypto.
     * It represents a 'crypto' in KeyStore Format V3
     */
    public static class Crypto {
        /**
         * Cipher algorithm name. : AES-128-CTR
         */
        private String cipher;

        /**
         * Encrypted key with specific cipher algorithm.
         */
        private String ciphertext;

        /**
         * Cipher Options to encrypt or decrypt key.
         */
        private KeyStore.CipherParams cipherparams;

        /**
         * Key derivation function algorithm name.
         */
        private String kdf;

        /**
         * Key derivation option to derive key.
         */
        private IKdfParams kdfparams;

        /**
         * Message authentication Code(MAC) when checking valid decryption key from KeyStore.
         */
        private String mac;

        /**
         * Creates an Crypto instance.
         */
        public Crypto() {
        }

        /**
         * Creates Crypto instances with given params.
         * @param privateKeys An array of PrivateKeys to be encrypted.
         * @param password The password to be used for encryption. The encrypted in KeyStore can be decrypted with this password.
         * @param option The options to use when encrypt a keys.
         * @return List
         * @throws CipherException
         */
        public static List<KeyStore.Crypto> createCrypto(PrivateKey[] privateKeys, String password, KeyStoreOption option) throws CipherException {
            final int PRIVATE_KEY_SIZE = 32;
            final String CIPHER_METHOD = "aes-128-ctr";

            List<KeyStore.Crypto> cryptoList = new ArrayList<>();

            String kdfName = "";

            byte[] salt = (option.kdfParams.getSalt() != null) ? Numeric.hexStringToByteArray(option.kdfParams.getSalt()) : Utils.generateRandomBytes(32);

            byte[] iv;
            if(option.cipherParams.getIv() == null) {
                iv = Utils.generateRandomBytes(16);
                option.getCipherParams().setIv(Numeric.toHexStringNoPrefix(iv));
            } else {
                iv = Numeric.hexStringToByteArray(option.cipherParams.getIv());
            }

            for(int i=0; i < privateKeys.length; i++) {
                byte[] derivedKey;

                //Check KDF Algorithm
                //SCRYPT
                if(option.kdfParams instanceof KeyStore.ScryptKdfParams) {
                    kdfName = KeyStore.ScryptKdfParams.getName();
                    derivedKey = generateDerivedScryptKey(
                            password.getBytes(UTF_8),
                            salt,
                            ((KeyStore.ScryptKdfParams) option.kdfParams).getN(),
                            ((KeyStore.ScryptKdfParams) option.kdfParams).getR(),
                            ((KeyStore.ScryptKdfParams) option.kdfParams).getP(),
                            option.kdfParams.getDklen());

                    ((KeyStore.ScryptKdfParams) option.kdfParams).setSalt(Numeric.toHexStringNoPrefix(salt));
                }
                //PBKDF2
                else if(option.kdfParams instanceof KeyStore.Pbkdf2KdfParams) {
                    kdfName = KeyStore.Pbkdf2KdfParams.getName();

                    derivedKey = generatePbkdf2DerivedKey(
                            password.getBytes(UTF_8),
                            salt,
                            ((KeyStore.Pbkdf2KdfParams) option.kdfParams).getC(),
                            ((KeyStore.Pbkdf2KdfParams) option.kdfParams).getPrf()
                    );

                    ((KeyStore.Pbkdf2KdfParams) option.kdfParams).setSalt(Numeric.toHexStringNoPrefix(salt));
                } else {
                    throw new RuntimeException("Unsupported KDF");
                }

                //generate keys for used cipher encryption.(AES)
                byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);

                //text
                byte[] privateKeyBytes =
                        Numeric.toBytesPadded(Numeric.toBigInt(privateKeys[i].getPrivateKey()), PRIVATE_KEY_SIZE);

                byte[] cipherText = performCipherOperation(
                        Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes);

                byte[] mac = generateMac(derivedKey, cipherText);

                //Set KeyStore
                KeyStore.Crypto crypto = new KeyStore.Crypto();
                crypto.setCipher(CIPHER_METHOD);
                crypto.setCiphertext(Numeric.toHexStringNoPrefix(cipherText));
                crypto.setCipherparams(option.getCipherParams());

                crypto.setKdf(kdfName);
                crypto.setKdfparams(option.kdfParams);
                crypto.setMac(Numeric.toHexStringNoPrefix(mac));

                cryptoList.add(crypto);
            }
            return cryptoList;
        }

        /**
         * Decrypts a keys in KeyStore
         * @param crypto Crypto instance
         * @param password The password to use for decryption.
         * @return String
         * @throws CipherException
         */
        public static String decryptCrypto(KeyStore.Crypto crypto, String password) throws CipherException {
            byte[] mac = Numeric.hexStringToByteArray(crypto.getMac());
            byte[] iv = Numeric.hexStringToByteArray(crypto.getCipherparams().getIv());
            byte[] cipherText = Numeric.hexStringToByteArray(crypto.getCiphertext());

            byte[] derivedKey;

            //Check KDF Algorithm
            IKdfParams kdfParams = crypto.getKdfparams();
            //SCRYPT
            if (kdfParams instanceof KeyStore.ScryptKdfParams) {
                KeyStore.ScryptKdfParams scryptKdfParams =
                        (KeyStore.ScryptKdfParams) crypto.getKdfparams();
                int dklen = scryptKdfParams.getDklen();
                int n = scryptKdfParams.getN();
                int p = scryptKdfParams.getP();
                int r = scryptKdfParams.getR();
                byte[] salt = Numeric.hexStringToByteArray(scryptKdfParams.getSalt());

                derivedKey = generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, r, p, dklen);
            }
            //PBKDF2
            else if (kdfParams instanceof KeyStore.Pbkdf2KdfParams) {
                KeyStore.Pbkdf2KdfParams aes128CtrKdfParams =
                        (KeyStore.Pbkdf2KdfParams) crypto.getKdfparams();
                int c = aes128CtrKdfParams.getC();
                String prf = aes128CtrKdfParams.getPrf();
                byte[] salt = Numeric.hexStringToByteArray(aes128CtrKdfParams.getSalt());

                derivedKey = generatePbkdf2DerivedKey(password.getBytes(UTF_8), salt, c, prf);
            } else {
                throw new CipherException("Unable to deserialize params: " + crypto.getKdf());
            }

            byte[] derivedMac = generateMac(derivedKey, cipherText);

            if (!Arrays.equals(derivedMac, mac)) {
                throw new CipherException("Invalid password provided");
            }

            //get key for using cipher decryption.(AES)
            byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
            byte[] privateKey = performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText);

            return Numeric.toHexString(privateKey);
        }

        /**
         * Derived key using SCRYPT algorithm.
         * @param password The password to use for key derivation.
         * @param salt Salt
         * @param n Parameter n
         * @param r Parameter r
         * @param p Parameter p
         * @param dkLen derivate key length
         * @return byte array
         * @throws CipherException
         */
        private static byte[] generateDerivedScryptKey(
                byte[] password, byte[] salt, int n, int r, int p, int dkLen) throws CipherException {
            return SCrypt.generate(password, salt, n, r, p, dkLen);
        }

        /**
         * Derived key using PBKDF2 algorithm.
         * @param password The password to use for key derivation
         * @param salt Salt
         * @param c Parameter c
         * @param prf Parameter prf(Pseudo Random Function) name
         * @return byte array
         * @throws CipherException
         */
        private static byte[] generatePbkdf2DerivedKey(
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

        /**
         * Cipher operation with AEC-128-CTR algorithm
         * @param mode Encryption or Decryption
         * @param iv Initial Vector
         * @param encryptKey The key to use Cipher operation.
         * @param text The text to use Cipher operation.
         * @return byte array
         * @throws CipherException
         */
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

        /**
         * Creates an MAC with derived key from KDF algorithm and cipher text from AES-128-CTR encryption.
         * @param derivedKey The derived key from KDF algorithm
         * @param cipherText The cipher text from Cipher(AES-128-CTR) encryption
         * @return byte array
         */
        private static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
            byte[] result = new byte[16 + cipherText.length];

            System.arraycopy(derivedKey, 16, result, 0, 16);
            System.arraycopy(cipherText, 0, result, 16, cipherText.length);

            return Hash.sha3(result);
        }

        /**
         * Getter function for cipher.
         * @return String
         */
        public String getCipher() {
            return cipher;
        }

        /**
         * Setter function for cipher.
         * @param cipher cipher algorithm name.
         */
        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        /**
         * Getter function for cipher text.
         * @return String
         */
        public String getCiphertext() {
            return ciphertext;
        }

        /**
         * Setter function for cipher text
         * @param ciphertext encrypted key
         */
        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        /**
         * Getter function for CipherParams.
         * @return KeyStore.CipherParams.
         */
        public KeyStore.CipherParams getCipherparams() {
            return cipherparams;
        }

        /**
         * Setter function for CipherParams.
         * @param cipherparams KeyStore.CipherParams.
         */
        public void setCipherparams(KeyStore.CipherParams cipherparams) {
            this.cipherparams = cipherparams;
        }

        /**
         * Getter function for kdf name.
         * @return String
         */
        public String getKdf() {
            return kdf;
        }

        /**
         * Setter function for kdf name.
         * @param kdf kdf name.
         */
        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        /**
         * Getter function for KdfParams.
         * @return KeyStore.KdfParams
         */
        public IKdfParams getKdfparams() {
            return kdfparams;
        }

        /**
         * Setter function for KdfParams.
         * @param kdfparams KeyStore.KdfParams.
         */
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "kdf")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = Pbkdf2KdfParams.class, name = Pbkdf2KdfParams.NAME),
                @JsonSubTypes.Type(value = KeyStore.ScryptKdfParams.class, name = ScryptKdfParams.NAME)
        })
        // To support my Ether Wallet keys uncomment this annotation & comment out the above
        //  @JsonDeserialize(using = KdfParamsDeserialiser.class)
        // Also add the following to the ObjectMapperFactory
        // objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        public void setKdfparams(IKdfParams kdfparams) {
            this.kdfparams = kdfparams;
        }

        /**
         * Getter function for mac.
         * @return String
         */
        public String getMac() {
            return mac;
        }

        /**
         * Setter function for mac.
         * @param mac mac string.
         */
        public void setMac(String mac) {
            this.mac = mac;
        }
    }

    /**
     * Represents an JSON deserializer class.
     * It deserialize keyring in KeyStore from JSON String.
     */
    static class KeyRingDeserializer extends JsonDeserializer<List> {

        /**
         * deserialize keyring in KeyStore
         * @param jsonParser JSONParser
         * @param deserializationContext DeserializationContext
         * @return List
         * @throws IOException
         */
        @Override
        public List deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ArrayNode root = objectMapper.readTree(jsonParser);

            if (root.get(0).isArray()) {
                return (List) objectMapper.convertValue(root, new TypeReference<List<List<KeyStore.Crypto>>>() {
                });
            } else {
                return (List) objectMapper.convertValue(root, new TypeReference<List<KeyStore.Crypto>>() {
                });
            }
        }
    }

    /**
     * Represents a Cipher option to encrypt or decrypt key
     */
    public static class CipherParams {
        /**
         * Initial Vector when using cipher operation.
         */
        private String iv;


        /**
         * Creates CipherParams instance
         * It set iv valus to random byte(16 bytes)
         */
        public CipherParams() {
            this.iv = Numeric.toHexStringNoPrefix(Utils.generateRandomBytes(16));
        }

        /**
         * Creates CipherParams instance.
         * @param iv Initial vector string.
         */
        public CipherParams(String iv) {
            setIv(iv);
        }

        /**
         * Getter function for IV
         * @return String
         */
        public String getIv() {
            return iv;
        }

        /**
         * Setter function for IV
         * @param iv Initial vector string.
         */
        public void setIv(String iv) {
            if(Numeric.hexStringToByteArray(iv).length != 16) {
                throw new IllegalArgumentException("AES-128-CTR must have iv length 16.");
            }
            this.iv = iv;
        }
    }

    /**
     * Represents an interface to implement kdf algorithm option
     */
    interface IKdfParams {

        /**
         * Getter function for derived key length
         * @return integer
         */
        int getDklen();

        /**
         * Getter function for salt
         * @return String
         */
        String getSalt();
    }

    /**
     * Represent a PBKDF2 parameter Class used in Key derivation
     */
    public static class Pbkdf2KdfParams implements IKdfParams {
        /**
         * PBKDF2 name used in KeyStore
         */
        private static final String NAME = "pbkdf2";

        /**
         * derived key length
         */
        private int dklen = 32;

        /**
         * PBKDF2 algorithm parameter - Number of iteration.
         */
        private int c = 262144;

        /**
         * The name of pseudo random function
         */
        private String prf = "hmac-sha256";

        /**
         * Salt
         */
        private String salt;

        /**
         * Creates a Pbkdf2KdfParams instance.
         * It will set default value.
         */
        public Pbkdf2KdfParams() {
            this.dklen = 32;
            this.c = 262144;
            this.prf = "hmac-sha256";
            this.salt = Numeric.toHexStringNoPrefix(Utils.generateRandomBytes(32));
        }

        /**
         * Create a Pbkdf2KdfParams instance
         * @param dklen derived key length
         * @param c PBKDF2 parameter
         * @param prf prf name used in PBKDF2 algorithm
         * @param salt salt
         */
        public Pbkdf2KdfParams(int dklen, int c, String prf, String salt) {
            this.dklen = dklen;
            this.c = c;
            this.prf = prf;
            this.salt = salt;
        }

        /**
         * Creates a Pbkdf2KdfParams instance
         * It will set default value excepts salt.
         * @param salt salt
         */
        public Pbkdf2KdfParams(String salt) {
            this(32, 262144, "hmac-sha256", salt);
        }

        /**
         * Getter function for dklen
         * @return int
         */
        public int getDklen() {
            return dklen;
        }

        /**
         * Setter function for dklen
         * @param dklen derived key length
         */
        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        /**
         * Getter function for C
         * @return integer
         */
        public int getC() {
            return c;
        }

        /**
         * Setter function for C
         * @param c The value of C
         */
        public void setC(int c) {
            this.c = c;
        }

        /**
         * Getter function for PRF name
         * @return String
         */
        public String getPrf() {
            return prf;
        }

        /**
         * Setter function for PRF name
         * @param prf prf name
         */
        public void setPrf(String prf) {
            this.prf = prf;
        }

        /**
         * Getter function for salt
         * @return String
         */
        public String getSalt() {
            return salt;
        }

        /**
         * Setter function for satl
         * @param salt salt String
         */
        public void setSalt(String salt) {
            this.salt = salt;
        }

        /**
         * Getter function for PBKDF2 name used in KeyStore
         * @return String
         */
        public static String getName() {
            return NAME;
        }
    }

    /**
     * Represent a Scrypt parameter Class used in Key derivation
     */
    public static class ScryptKdfParams implements IKdfParams {
        /**
         * The scrypt algorithm name used in KeyStore
         */
        private static final String NAME = "scrypt";

        /**
         * derived key length
         */
        private int dklen;

        /**
         * CPU/memory cost parameter.
         */
        private int n;

        /**
         * Parallelization parameter
         */
        private int p;

        /**
         * The block size parameter.
         */
        private int r;

        /**
         * Salt
         */
        private String salt;

        /**
         * Creates a ScryptKdfParams instance.
         * It will set default value.
         */
        public ScryptKdfParams() {
            this.dklen = 32;
            this.n = 4096;
            this.p = 1;
            this.r = 8;
            this.salt = Numeric.toHexStringNoPrefix(Utils.generateRandomBytes(32));
        }

        /**
         * Creates a ScryptKdfParams instance.
         * @param dklen derived key length.
         * @param n Scrypt parameter "N"
         * @param p Scrypt parameter "p"
         * @param r Scrypt parameter "r"
         * @param salt Salt
         */
        public ScryptKdfParams(int dklen, int n, int p, int r, String salt) {
            this.dklen = dklen;
            this.n = n;
            this.p = p;
            this.r = r;
            this.salt = salt;
        }

        /**
         * Create a ScryptKdfParams instance.
         * It will set default value excepts salt.
         * @param salt Salt
         */
        public ScryptKdfParams(String salt) {
            this(32, 4096, 1 , 8, salt);
        }

        /**
         * Getter function for dkLen
         * @return integer
         */
        public int getDklen() {
            return dklen;
        }

        /**
         * Setter function for dkLen
         * @param dklen derived key length
         */
        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        /**
         * Getter function for N
         * @return integer
         */
        public int getN() {
            return n;
        }

        /**
         * Setter function for N
         * @param n Scrypt parameter "N" value
         */
        public void setN(int n) {
            this.n = n;
        }

        /**
         * Getter function for P
         * @return integer
         */
        public int getP() {
            return p;
        }

        /**
         * Setter function for p
         * @param p Scrypt parameter "p" value
         */
        public void setP(int p) {
            this.p = p;
        }

        /**
         * Getter function for r
         * @return integer
         */
        public int getR() {
            return r;
        }

        /**
         * Setter function for r
         * @param r Scrypt parameter "r" value
         */
        public void setR(int r) {
            this.r = r;
        }

        /**
         * Getter function for Salt
         * @return salt
         */
        public String getSalt() {
            return salt;
        }

        /**
         * Setter function for salt
         * @param salt salt
         */
        public void setSalt(String salt) {
            this.salt = salt;
        }

        /**
         * Getter function for SCRYPT name used in KeyStore
         * @return String
         */
        public static String getName() {
            return NAME;
        }
    }

    /**
     * Represents an JSON deserializer class.
     * It deserialize KdfParams in KeyStore from JSON String.
     */
    // If we need to work with MyEtherWallet we'll need to use this deserializer, see the
    // following issue https://github.com/kvhnuke/etherwallet/issues/269
    static class KdfParamsDeserialiser extends JsonDeserializer<IKdfParams> {

        /**
         * deserialize KdfParams in KeyStore
         * @param jsonParser JSONParser
         * @param deserializationContext DeserializationContext
         * @return KeyStore.KdfParams
         * @throws IOException
         */
        @Override
        public IKdfParams deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ObjectNode root = objectMapper.readTree(jsonParser);
            IKdfParams kdfParams;

            // it would be preferable to detect the class to use based on the kdf parameter in the
            // container object instance
            JsonNode n = root.get("n");
            if (n == null) {
                kdfParams = objectMapper.convertValue(root, Pbkdf2KdfParams.class);
            } else {
                kdfParams = objectMapper.convertValue(root, KeyStore.ScryptKdfParams.class);
            }

            return kdfParams;
        }
    }
}
