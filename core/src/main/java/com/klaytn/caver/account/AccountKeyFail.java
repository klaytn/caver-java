package com.klaytn.caver.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.Arrays;

/**
 * AccountKeyFail is used for smart contract accounts so that a transaction sent from
 * the smart contract account always fails.
 * If an account has the key AccountKeyFail, the tx validation process always fails.
 */
@JsonSerialize(using = AccountKeyFail.AccountKeyFailSerializer.class)
public class AccountKeyFail implements IAccountKey{
    /**
     * AccountKeyFail's RLP-Encoded data
     */
    private static final byte[] RLP = new byte[]{(byte) 0x03, (byte) 0xc0};

    /**
     * AccountKeyFail's Type attribute.
     */
    private static final String TYPE = "0x03";

    /**
     * Creates an AccountKeyFail instance.
     */
    public AccountKeyFail() {
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail string
     * @param rlpEncodedKey RLP-encoded AccountKeyFail string
     * @return AccountKeyFail
     */
    public static AccountKeyFail decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyFail byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyFail byte array
     * @return AccountKeyFail
     */
    public static AccountKeyFail decode(byte[] rlpEncodedKey) {
        if(!Arrays.equals(rlpEncodedKey, RLP)) {
            throw new RuntimeException("Invalid RLP-encoded account key String");
        }

        return new AccountKeyFail();
    }

    /**
     * Encodes a AccountKeyFail Object by RLP-Encoding method.
     * @return RLP-encoded AccountKeyFail String
     */
    @Override
    public String getRLPEncoding() {
        return Numeric.toHexString(RLP);
    }

    /**
     * Returns an AccountKeyFail's type attribute
     * @return AccountKeyFail's type attribute
     */
    public static String getType() {
        return TYPE;
    }

    public static class AccountKeyFailSerializer extends JsonSerializer<AccountKeyFail> {
        @Override
        public void serialize(AccountKeyFail value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartObject();

            gen.writeFieldName("keyType");
            gen.writeNumber(Numeric.toBigInt(getType()));

            gen.writeFieldName("key");
            gen.writeStartObject();
            gen.writeEndObject();

            gen.writeEndObject();
        }
    }
}
