package com.klaytn.caver.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.klaytn.caver.tx.account.AccountKey;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.Arrays;

/**
 * AccountKeyLegacy represents a key of legacy account types. If an account has AccountKeyLegacy,
 * the tx validation process is done like below (as Ethereum did):
 *
 * Get the public key from ecrecover(txhash, txsig)
 * Get the address of the public key
 * The address is the sender
 *
 */
@JsonSerialize(using = AccountKeyLegacy.AccountKeyLegacySerializer.class)
public class AccountKeyLegacy implements IAccountKey{

    /**
     * AccountKeyLegacy's RLP-encoded data.
     */
    private static final byte[] RLP = new byte[]{(byte) 0x01, (byte) 0xc0};

    /**
     * AccountKeyLegacy's Type attribute.
     */
    private static final String TYPE = "0x01";


    /**
     * Creates an AccountKeyLegacy instance
     */
    public AccountKeyLegacy() {

    }

    /**
     * Decodes a RLP-encoded AccountKeyLegacy string
     * @param rlpEncodedKey A RLP-encoded AccountKeyLegacy string
     * @return AccountKeyLegacy
     */
    public static AccountKeyLegacy decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyLegacy byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyLegacy byte array
     * @return AccountKeyLegacy
     */
    public static AccountKeyLegacy decode(byte[] rlpEncodedKey) {
        if(!Arrays.equals(RLP, rlpEncodedKey)) {
            throw new RuntimeException("Invalid RLP-encoded account key String");
        }

        return new AccountKeyLegacy();
    }

    /**
     * Encodes a AccountKeyLegacy Object by RLP-encoding method.
     * @return RLP-encoded AccountKeyLegacy string
     */
    @Override
    public String getRLPEncoding() {
        return Numeric.toHexString(RLP);
    }

    /**
     * Returns an AccountKeyLegacy's type attribute
     * @return AccountKeyLegacy's type attribute
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Serialize class to AccountKeyLegacy object into JSON.
     */
    public static class AccountKeyLegacySerializer extends JsonSerializer<AccountKeyLegacy> {
        @Override
        public void serialize(AccountKeyLegacy value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
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
