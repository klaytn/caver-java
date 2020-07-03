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
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/WalletFile.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.wallet;

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

import java.io.IOException;
import java.util.List;

/**
 * @deprecated This class replaced by {@link com.klaytn.caver.wallet.keyring.KeyStore}
 */
@Deprecated
public class WalletFile {
    private String address;
    private WalletFile.Crypto crypto;
    private String id;
    private int version;
    private List keyRing;

    @JsonSetter("keyring")
    @JsonDeserialize(using = KeyRingDeserializer.class)
    public void setKeyring(List keyRing) {
        this.keyRing = keyRing;
    }

    public List getKeyring() {
        return this.keyRing;
    }

    public WalletFile() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public WalletFile.Crypto getCrypto() {
        return crypto;
    }

    @JsonSetter("crypto")
    public void setCrypto(WalletFile.Crypto crypto) {
        this.crypto = crypto;
    }

    @JsonSetter("Crypto")  // older wallet files may have this attribute name
    public void setCryptoV1(WalletFile.Crypto crypto) {
        setCrypto(crypto);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletFile)) {
            return false;
        }

        WalletFile that = (WalletFile) o;

        if (getAddress() != null
                ? !getAddress().equals(that.getAddress())
                : that.getAddress() != null) {
            return false;
        }
        if (getCrypto() != null
                ? !getCrypto().equals(that.getCrypto())
                : that.getCrypto() != null) {
            return false;
        }
        if (getId() != null
                ? !getId().equals(that.getId())
                : that.getId() != null) {
            return false;
        }
        return version == that.version;
    }

    @Override
    public int hashCode() {
        int result = getAddress() != null ? getAddress().hashCode() : 0;
        result = 31 * result + (getCrypto() != null ? getCrypto().hashCode() : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + version;
        return result;
    }

    public static class Crypto {
        private String cipher;
        private String ciphertext;
        private WalletFile.CipherParams cipherparams;

        private String kdf;
        private WalletFile.KdfParams kdfparams;

        private String mac;

        public Crypto() {
        }

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public WalletFile.CipherParams getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(WalletFile.CipherParams cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public WalletFile.KdfParams getKdfparams() {
            return kdfparams;
        }

        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "kdf")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = WalletFile.Aes128CtrKdfParams.class, name = Wallet.AES_128_CTR),
                @JsonSubTypes.Type(value = WalletFile.ScryptKdfParams.class, name = Wallet.SCRYPT)
        })
        // To support my Ether Wallet keys uncomment this annotation & comment out the above
        //  @JsonDeserialize(using = KdfParamsDeserialiser.class)
        // Also add the following to the ObjectMapperFactory
        // objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        public void setKdfparams(WalletFile.KdfParams kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WalletFile.Crypto)) {
                return false;
            }

            WalletFile.Crypto that = (WalletFile.Crypto) o;

            if (getCipher() != null
                    ? !getCipher().equals(that.getCipher())
                    : that.getCipher() != null) {
                return false;
            }
            if (getCiphertext() != null
                    ? !getCiphertext().equals(that.getCiphertext())
                    : that.getCiphertext() != null) {
                return false;
            }
            if (getCipherparams() != null
                    ? !getCipherparams().equals(that.getCipherparams())
                    : that.getCipherparams() != null) {
                return false;
            }
            if (getKdf() != null
                    ? !getKdf().equals(that.getKdf())
                    : that.getKdf() != null) {
                return false;
            }
            if (getKdfparams() != null
                    ? !getKdfparams().equals(that.getKdfparams())
                    : that.getKdfparams() != null) {
                return false;
            }
            return getMac() != null
                    ? getMac().equals(that.getMac()) : that.getMac() == null;
        }

        @Override
        public int hashCode() {
            int result = getCipher() != null ? getCipher().hashCode() : 0;
            result = 31 * result + (getCiphertext() != null ? getCiphertext().hashCode() : 0);
            result = 31 * result + (getCipherparams() != null ? getCipherparams().hashCode() : 0);
            result = 31 * result + (getKdf() != null ? getKdf().hashCode() : 0);
            result = 31 * result + (getKdfparams() != null ? getKdfparams().hashCode() : 0);
            result = 31 * result + (getMac() != null ? getMac().hashCode() : 0);
            return result;
        }

    }

    static class KeyRingDeserializer extends JsonDeserializer<List> {

        @Override
        public List deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ArrayNode root = objectMapper.readTree(jsonParser);

            if (root.get(0).isArray()) {
                return (List) objectMapper.convertValue(root, new TypeReference<List<List<Crypto>>>() {
                });
            } else {
                return (List) objectMapper.convertValue(root, new TypeReference<List<Crypto>>() {
                });
            }
        }
    }

    public static class CipherParams {
        private String iv;

        public CipherParams() {
        }

        public String getIv() {
            return iv;
        }

        public void setIv(String iv) {
            this.iv = iv;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WalletFile.CipherParams)) {
                return false;
            }

            WalletFile.CipherParams that = (WalletFile.CipherParams) o;

            return getIv() != null
                    ? getIv().equals(that.getIv()) : that.getIv() == null;
        }

        @Override
        public int hashCode() {
            int result = getIv() != null ? getIv().hashCode() : 0;
            return result;
        }
    }

    interface KdfParams {
        int getDklen();

        String getSalt();
    }

    public static class Aes128CtrKdfParams implements WalletFile.KdfParams {
        private int dklen;
        private int c;
        private String prf;
        private String salt;

        public Aes128CtrKdfParams() {
        }

        public int getDklen() {
            return dklen;
        }

        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public String getPrf() {
            return prf;
        }

        public void setPrf(String prf) {
            this.prf = prf;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WalletFile.Aes128CtrKdfParams)) {
                return false;
            }

            WalletFile.Aes128CtrKdfParams that = (WalletFile.Aes128CtrKdfParams) o;

            if (dklen != that.dklen) {
                return false;
            }
            if (c != that.c) {
                return false;
            }
            if (getPrf() != null
                    ? !getPrf().equals(that.getPrf())
                    : that.getPrf() != null) {
                return false;
            }
            return getSalt() != null
                    ? getSalt().equals(that.getSalt()) : that.getSalt() == null;
        }

        @Override
        public int hashCode() {
            int result = dklen;
            result = 31 * result + c;
            result = 31 * result + (getPrf() != null ? getPrf().hashCode() : 0);
            result = 31 * result + (getSalt() != null ? getSalt().hashCode() : 0);
            return result;
        }
    }

    public static class ScryptKdfParams implements WalletFile.KdfParams {
        private int dklen;
        private int n;
        private int p;
        private int r;
        private String salt;

        public ScryptKdfParams() {
        }

        public int getDklen() {
            return dklen;
        }

        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WalletFile.ScryptKdfParams)) {
                return false;
            }

            WalletFile.ScryptKdfParams that = (WalletFile.ScryptKdfParams) o;

            if (dklen != that.dklen) {
                return false;
            }
            if (n != that.n) {
                return false;
            }
            if (p != that.p) {
                return false;
            }
            if (r != that.r) {
                return false;
            }
            return getSalt() != null
                    ? getSalt().equals(that.getSalt()) : that.getSalt() == null;
        }

        @Override
        public int hashCode() {
            int result = dklen;
            result = 31 * result + n;
            result = 31 * result + p;
            result = 31 * result + r;
            result = 31 * result + (getSalt() != null ? getSalt().hashCode() : 0);
            return result;
        }
    }

    // If we need to work with MyEtherWallet we'll need to use this deserializer, see the
    // following issue https://github.com/kvhnuke/etherwallet/issues/269
    static class KdfParamsDeserialiser extends JsonDeserializer<WalletFile.KdfParams> {

        @Override
        public WalletFile.KdfParams deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ObjectNode root = objectMapper.readTree(jsonParser);
            WalletFile.KdfParams kdfParams;

            // it would be preferable to detect the class to use based on the kdf parameter in the
            // container object instance
            JsonNode n = root.get("n");
            if (n == null) {
                kdfParams = objectMapper.convertValue(root, WalletFile.Aes128CtrKdfParams.class);
            } else {
                kdfParams = objectMapper.convertValue(root, WalletFile.ScryptKdfParams.class);
            }

            return kdfParams;
        }
    }
}
