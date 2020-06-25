package com.klaytn.caver.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * AccountKeyRoleBased represents a role-based key.
 */
@JsonDeserialize(using = AccountKeyRoleBased.AccountKeyRoleBasedDeserializer.class)
@JsonSerialize(using = AccountKeyRoleBased.AccountKeyRoleBasedSerializer.class)
public class AccountKeyRoleBased implements IAccountKey{

    /**
     * AccountKeyRoleBased's Type attribute.
     */
    private static final String TYPE = "0x05";

    public enum RoleGroup {
        TRANSACTION(0),
        ACCOUNT_UPDATE(1),
        FEE_PAYER(2);

        private int groupIndex;

        RoleGroup(int groupIndex) {
            this.groupIndex = groupIndex;
        }

        public int getIndex() {
            return this.groupIndex;
        }
    }

    public static final int ROLE_GROUP_COUNT = RoleGroup.values().length;

    /**
     * First Key : roleTransaction
     * Default key. Transactions other than TxTypeAccountUpdate should be signed by the key of this role.
     *
     * Second Key : roleAccountUpdate
     * TxTypeAccountUpdate transaction should be signed by this key. If this key is not present in the account,
     * TxTypeAccountUpdate transaction is validated using RoleTransaction key.
     *
     * Third Key : roleFeePayer
     * If this account wants to send tx fee instead of the sender, the transaction should be signed by this key.
     * If this key is not present in the account, a fee-delegated transaction is validated using RoleTransaction key.
     */
    private List<IAccountKey> accountKeys;

    /**
     * Creates an AccountKeyRoleBased instance.
     * @param accountKeys List of a AccountKey implements IAccountKey interface
     */
    public AccountKeyRoleBased(List<IAccountKey> accountKeys) {
        setAccountKeys(accountKeys);
    }

    /**
     * Decodes a RLP-encoded AccountKeyRoleBased string
     * @param rlpEncodedKey RLP-encoded AccountKeyRoleBased string.
     * @return AccountKeyRoleBased
     */
    public static AccountKeyRoleBased decode(String rlpEncodedKey) {
        return decode(Numeric.hexStringToByteArray(rlpEncodedKey));
    }

    /**
     * Decodes a RLP-encoded AccountKeyRoleBased byte array
     * @param rlpEncodedKey RLP-encoded AccountKeyRoleBased byte array
     * @return AccountKeyRoleBased
     */
    public static AccountKeyRoleBased decode(byte[] rlpEncodedKey) {
        byte type = Numeric.hexStringToByteArray(getType())[0];
        if(rlpEncodedKey[0] != type) {
            throw new IllegalArgumentException("Invalid RLP-encoded AccountKeyRoleBased Tag");
        }

        List<IAccountKey> accountKeys = new ArrayList<>();
        //remove Tag
        byte[] encodedKey = Arrays.copyOfRange(rlpEncodedKey, 1, rlpEncodedKey.length);

        RlpList rlpList = RlpDecoder.decode(encodedKey);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

        for (RlpType value : values) {
            accountKeys.add(AccountKeyDecoder.decode(((RlpString) value).asString()));
        }
        return new AccountKeyRoleBased(accountKeys);
    }

    /**
     * Creates an AccountKeyRoleBased with given params.
     * @param pubArray An List contains public key string array.
     * @param options An List contains WeightedMultiSigOptions
     * @return AccountKeyRoleBased
     */
    public static AccountKeyRoleBased fromRoleBasedPublicKeysAndOptions(List<String[]> pubArray, List<WeightedMultiSigOptions> options) {
        List<IAccountKey> accountKeys = new ArrayList<>();

        /*
        * pubArray must have up to three item.
        * each item can have
        * {String, String...}, {}
        *
        * options must have up to three item.
        * Valid WeightedMultiSigOption or Empty WeightedMultiSigOption
        *  - Valid WeightedMultiSigOption : make AccountKeyWeightedMultiSig instance
        *  - Empty WeightedMultiSigOption : make AccountKeyPublicKey instance
        * */
        if(pubArray.size() > ROLE_GROUP_COUNT) {
            throw new IllegalArgumentException("pubArray must have up to three items");
        }
        if(options.size() != pubArray.size()) {
            throw new IllegalArgumentException("pubArray and options must have the same number of items.");
        }

        for(int i=0; i<pubArray.size(); i++) {
            String[] publicKeyArr = pubArray.get(i);
            WeightedMultiSigOptions weightedMultiSigOption = options.get(i);
            if(publicKeyArr == null) {
                throw new RuntimeException("Invalid publicKey format");
            }

            //Set AccountKeyNil
            if(publicKeyArr.length == 0) {
                if (!weightedMultiSigOption.isEmpty()) {
                    throw new RuntimeException("Invalid options: AccountKeyNil cannot have options.");
                }
                accountKeys.add(new AccountKeyNil());
                continue;
            }
            //Set AccountKeyPublic
            else if (publicKeyArr.length == 1 && weightedMultiSigOption.isEmpty()) {
                    accountKeys.add(AccountKeyPublic.fromPublicKey(publicKeyArr[0]));
                    continue;
            }

            if (weightedMultiSigOption.isEmpty()) {
                throw new RuntimeException("Invalid options : AccountKeyWeightedMultiSig must have options");
            }
            accountKeys.add(AccountKeyWeightedMultiSig.fromPublicKeysAndOptions(publicKeyArr, weightedMultiSigOption));
        }

        return new AccountKeyRoleBased(accountKeys);
    }

    /**
     * Getter function for accountKeys
     * @return accountKeys
     */
    public List<IAccountKey> getAccountKeys() {
        return accountKeys;
    }

    /**
     * Setter function for accountKeys
     * @param accountKeys List of a AccountKey implements IAccountKey interface
     */
    public void setAccountKeys(List<IAccountKey> accountKeys) {
        if(accountKeys.size() > ROLE_GROUP_COUNT) throw new RuntimeException("It exceeds maximum role based key count.");
        this.accountKeys = accountKeys;
    }

    /**
     * Encodes a AccountKeyRoleBased Object by RLP-encoding method.
     * @return RLP-encoded AccountKeyRoleBased String
     */
    @Override
    public String getRLPEncoding() {
        List<RlpType> rlpTypeList = new ArrayList<>();
        for(IAccountKey accountKey: accountKeys) {
            byte[] encodedData = Numeric.hexStringToByteArray(accountKey.getRLPEncoding());
            rlpTypeList.add(RlpString.create(encodedData));
        }
        byte[] encodedRoleBasedKey = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = Numeric.hexStringToByteArray(AccountKeyRoleBased.getType());

        return Numeric.toHexString(BytesUtils.concat(type, encodedRoleBasedKey));
    }

    /**
     * Returns a AccountKeyRoleBased's type attribute
     * @return AccountKeyRoleBased's type attribute
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Returns a RoleTransactionKey
     * @return IAccountKey
     */
    public IAccountKey getRoleTransactionKey() {
        return this.getAccountKeys().get(RoleGroup.TRANSACTION.getIndex());
    }

    /**
     * Returns a RoleAccountUpdateKey
     * @return IAccountKey
     */
    public IAccountKey getRoleAccountUpdateKey() {
        return this.getAccountKeys().get(RoleGroup.ACCOUNT_UPDATE.getIndex());
    }

    /**
     * Returns a RoleFeePayerKey
     * @return IAccountKey
     */
    public IAccountKey getRoleFeePayerKey() {
        return this.getAccountKeys().get(RoleGroup.FEE_PAYER.getIndex());
    }

    public static class AccountKeyRoleBasedSerializer extends JsonSerializer<AccountKeyRoleBased> {
        @Override
        public void serialize(AccountKeyRoleBased accountKeyRoleBased, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("keyType");
            jsonGenerator.writeNumber(Numeric.toBigInt(getType()));

//            jsonGenerator.writeFieldName("key");
            jsonGenerator.writeArrayFieldStart("key");
            for(IAccountKey accountKey : accountKeyRoleBased.getAccountKeys()) {
                jsonGenerator.writeObject(accountKey);
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
    }

    public static class AccountKeyRoleBasedDeserializer extends JsonDeserializer<AccountKeyRoleBased> {

        private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

        public static IAccountKey decode(JsonNode accountKeyJson) throws IOException {
            String type = Numeric.toHexStringWithPrefixZeroPadded(accountKeyJson.get("keyType").bigIntegerValue(), 2);
            IAccountKey accountKey = null;

            if(type.equals(AccountKeyPublic.getType())) {
                accountKey = objectMapper.readValue(accountKeyJson.toString(), AccountKeyPublic.class);
            } else if(type.equals(AccountKeyWeightedMultiSig.getType())) {
                accountKey = objectMapper.readValue(accountKeyJson.toString(), AccountKeyWeightedMultiSig.class);
            } else if(type.equals(AccountKeyLegacy.getType())){
                accountKey = new AccountKeyLegacy();
            } else if(type.equals(AccountKeyFail.getType())) {
                accountKey = new AccountKeyFail();
            } else {
                accountKey = new AccountKeyNil();
            }

            return accountKey;
        }

        @Override
        public AccountKeyRoleBased deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode root = p.getCodec().readTree(p);
            String type = Numeric.toHexStringWithPrefixZeroPadded(root.get("keyType").bigIntegerValue(), 2);

            JsonNode key = root.get("key");
            Iterator<JsonNode> iterator = key.iterator();

            List<IAccountKey> accountKeyList = new ArrayList<>();

            while(iterator.hasNext()) {
                JsonNode node = iterator.next();
                IAccountKey accountKey = decode(node);
                accountKeyList.add(accountKey);
            }

            return new AccountKeyRoleBased(accountKeyList);
        }
    }
}
