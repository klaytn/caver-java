package com.klaytn.caver.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.Arrays;


/**
 * AccountKeyPublic is used for accounts having one public key.
 * If an account has an AccountKeyPublic object, the tx validation process is done like below:
 *
 * Get the public key derived from ecrecover(txhash, txsig)
 * Check that the derived public key is the same as the corresponding
 * account's public key
 *
 */
@JsonDeserialize(using = AccountKeyPublic.AccountKeyPublicDeserializer.class)
@JsonSerialize(using = AccountKeyPublic.AccountKeyPublicSerializer.class)
public class AccountKeyPublic implements IAccountKey{

    /**
     * AccountKeyPublic's Type attribute.
     */
    private static final String TYPE = "0x02";

    /**
     * ECC Public Key value with "SECP-256k1" curve.
     * This String has following format.
     * 1. Uncompressed format : 0x{Public Key X point}||{Public Y point}
     * 2. Compressed format : 0x{02 or 03 || Public Key X}
     */
    private String publicKey;

    public static final int OFFSET_X_POINT = 0;
    public static final int OFFSET_Y_POINT = 1;

    /**
     * Creates an AccountKeyPublic instance.
     * @param publicKey ECC public key. (Uncompress or compress format)
     */
    public AccountKeyPublic(String publicKey) {
        setPublicKey(publicKey);
    }

    /**
     * Creates AccountKeyPublic instance from Elliptic curve x, y coordinates.
     * @param x The point x
     * @param y The point y
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromXYPoint(String x, String y) {
        String publicKey = Numeric.prependHexPrefix(x) + Numeric.cleanHexPrefix(y);
        return new AccountKeyPublic(publicKey);
    }

    /**
     * Creates AccountKeyPublic instance from ECC Public Key.
     * @param publicKey The public key string. This public key can be in format of compressed or uncompressed.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic fromPublicKey(String publicKey) {
        return new AccountKeyPublic(publicKey);
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic string
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic string.
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyPublic byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyPublic byte array
     * @return AccountKeyPublic
     */
    public static AccountKeyPublic decode(byte[] rlpEncodedKey) {
        byte type = Numeric.hexStringToByteArray(getType())[0];
        if(rlpEncodedKey[0] != type) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyPublic Tag");
        }

        //remove Tag
        byte[] encodedPublicKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);
        RlpList rlpList = RlpDecoder.decode(encodedPublicKey);
        String compressedPubKey = ((RlpString) rlpList.getValues().get(0)).asString();

        try {
            //Get decompressed Public Key and ECC Point validation Check in toDecompressPublicKeyXY()
            String publicKey = Utils.decompressPublicKey(compressedPubKey);
            return new AccountKeyPublic(publicKey);
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Encodes a AccountKeyPublic Object by RLP-encoding method.
     * @return RLP-encoded AccountKeyPublic String
     */
    @Override
    public String getRLPEncoding() {
        String compressedKey = Utils.compressPublicKey(this.publicKey);
        byte[] encodedPubKey = RlpEncoder.encode(RlpString.create(Numeric.hexStringToByteArray(compressedKey)));
        byte[] type = Numeric.hexStringToByteArray(AccountKeyPublic.getType());

        return Numeric.toHexString(BytesUtils.concat(type, encodedPubKey));
    }

    /**
     * Returns the x and y coordinates of publicKey.
     * @return String array of X,Y coordinates.
     */
    public String[] getXYPoint() {
        String key = this.getPublicKey();
        if (AccountKeyPublicUtils.isCompressedPublicKey(this.getPublicKey())) {
            key = Utils.decompressPublicKey(this.getPublicKey());
        }

        String noPrefixKeyStr = Numeric.cleanHexPrefix(key);
        String[] arr = new String[2];

        arr[OFFSET_X_POINT] = noPrefixKeyStr.substring(0, 64); // x point
        arr[OFFSET_Y_POINT] = noPrefixKeyStr.substring(64); // y point

        return arr;
    }

    /**
     * Getter function for publicKey
     * @return ECC PublicKey string
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Returns an AccountKeyPublic's type attribute
     * @return AccountKeyPublic's type attribute
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Setter function for publicKey
     * @param publicKey ECC public key(compressed or uncompressed format)
     */
    public void setPublicKey(String publicKey) {
        if(!Utils.isValidPublicKey(publicKey)) {
            throw new RuntimeException("Invalid Public Key format");
        }

        this.publicKey = publicKey;
    }

    /**
     * Serialize class to AccountKeyPublic into JSON.
     */
    public static class AccountKeyPublicSerializer extends JsonSerializer<AccountKeyPublic> {
        @Override
        public void serialize(AccountKeyPublic accountKeyPublic, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("keyType");
            jsonGenerator.writeNumber(Numeric.toBigInt(getType()));

            String[] xy = accountKeyPublic.getXYPoint();

            jsonGenerator.writeFieldName("key");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("x", Utils.addHexPrefix(xy[OFFSET_X_POINT]));
            jsonGenerator.writeStringField("y", Utils.addHexPrefix(xy[OFFSET_Y_POINT]));
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
        }
    }

    /**
     * Deserialize class to JSON to AccountKeyPublic.
     */
    public static class AccountKeyPublicDeserializer extends JsonDeserializer<AccountKeyPublic> {
        @Override
        public AccountKeyPublic deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            byte type = (byte)node.get("keyType").intValue();

            JsonNode key = node.get("key");
            String x = key.get("x").asText();
            String y = key.get("y").asText();

            return AccountKeyPublic.fromXYPoint(x, y);
        }
    }
}
