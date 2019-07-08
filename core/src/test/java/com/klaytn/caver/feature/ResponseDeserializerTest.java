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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.methods.response.AccountEOA;
import com.klaytn.caver.methods.response.AccountSmartContract;
import com.klaytn.caver.methods.response.KlayAccount;
import com.klaytn.caver.methods.response.KlayAccountKey;
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.account.AccountKeyRoleBased;
import com.klaytn.caver.tx.account.AccountKeyWeightedMultiSig;
import org.junit.Test;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.math.BigInteger;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ResponseDeserializerTest {

    protected final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper(false);

    @Test
    public void testAccountKeyRolebased() throws IOException {
        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"key\":[{\"key\":{\"x\":\"0x819659d4f08e08d4bd97c6ce5ed2c2eb914201a5b3731eb9d208128df24b97dd\",\"y\":\"0x1824267ab9e55f5a3fb1030f0299fa73fc0037305d5b1d90100e2131af41c010\"},\"keyType\":2},{\"key\":{\"x\":\"0x73363604ca8776a2883b02046361b7eb6bd11f4fc10700ee51c525bcded134c1\",\"y\":\"0xfc3e3cb3f4f5b709df5a2075107bc73c8618440c08456bafc44ee6f27f9e6326\"},\"keyType\":2},{\"key\":{\"x\":\"0x95c920eb2571dff37baecdbbee32897e6e448c6725c5ab73569cc6f659684307\",\"y\":\"0xef7839023c48acf710ad322356c12b7c5b7f475515ba7d5834f41a993f42b8f9\"},\"keyType\":2}],\"keyType\":5}}";
        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) parsed.getResult().getKey();

        assertTrue(accountKeyRoleBased.getAccountKeys().get(0).getType().getValue() == 2);
        assertEquals("0x819659d4f08e08d4bd97c6ce5ed2c2eb914201a5b3731eb9d208128df24b97dd",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(0)).getX());
        assertEquals("0x1824267ab9e55f5a3fb1030f0299fa73fc0037305d5b1d90100e2131af41c010",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(0)).getY());

        assertTrue(accountKeyRoleBased.getAccountKeys().get(1).getType().getValue() == 2);
        assertEquals("0x73363604ca8776a2883b02046361b7eb6bd11f4fc10700ee51c525bcded134c1",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(1)).getX());
        assertEquals("0xfc3e3cb3f4f5b709df5a2075107bc73c8618440c08456bafc44ee6f27f9e6326",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(1)).getY());

        assertTrue(accountKeyRoleBased.getAccountKeys().get(2).getType().getValue() == 2);
        assertEquals("0x95c920eb2571dff37baecdbbee32897e6e448c6725c5ab73569cc6f659684307",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(2)).getX());
        assertEquals("0xef7839023c48acf710ad322356c12b7c5b7f475515ba7d5834f41a993f42b8f9",
                ((AccountKeyPublic) accountKeyRoleBased.getAccountKeys().get(2)).getY());
        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.ROLEBASED);
    }

    @Test
    public void testAccountKeyPublic() throws IOException {
        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"key\":{\"x\":\"0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7\",\"y\":\"0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6\"},\"keyType\":2}}";
        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
        assertTrue(parsed.getResult().getKeyType() == 2);

        AccountKeyPublic accountKeyPublic = (AccountKeyPublic) parsed.getResult().getKey();
        assertTrue(accountKeyPublic.getType() == AccountKey.Type.PUBLIC);
        assertEquals("0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7", accountKeyPublic.getX());
        assertEquals("0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6", accountKeyPublic.getY());
    }

    @Test
    public void testAccountKeyLegacy() throws IOException {
        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"key\":{},\"keyType\":1}}";
        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
        assertTrue(parsed.getResult().getKeyType() == 1);
        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.LEGACY);
    }

    @Test
    public void testAccountKeyFail() throws IOException {
        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"key\":{},\"keyType\":3}}";
        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
        assertTrue(parsed.getResult().getKeyType() == 3);
        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.FAIL);
    }

    @Test
    public void testAccountKeyMultiSig() throws IOException {
        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"key\":{\"threshold\":2,\"keys\":[{\"weight\":1,\"key\":{\"x\":\"0xae6b72d7ce2c11520ac00cbd1c4da216171a96eae1ae3a0a1f979a554c9063ae\",\"y\":\"0x79ddf38c8717030512f3ca6f304408a3beb51519b918b8d62a55ff4a8c165fea\"}},{\"weight\":1,\"key\":{\"x\":\"0xd4256fc43f42b3313b7204e42a82893a8d9b562f6c9b39456ee989339949c67c\",\"y\":\"0xfc5e78e71b26f5a93b5bec454e4d63947576ffd23b4df624579ff4eb67a2a29b\"}},{\"weight\":1,\"key\":{\"x\":\"0xd653eae5f0e9cd6bfe4c3929f4c4f28c94f3bd183eafafee2d73db38a020d9d8\",\"y\":\"0xe974e859b5be80755dedaebe937ac49800cbac483ca304179050a177e9ca0270\"}}]},\"keyType\":4}}";
        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
        assertTrue(parsed.getResult().getKeyType() == 4);

        AccountKeyWeightedMultiSig accountKeyPublic = (AccountKeyWeightedMultiSig) parsed.getResult().getKey();
        assertTrue(accountKeyPublic.getType() == AccountKey.Type.MULTISIG);
        assertEquals(BigInteger.valueOf(1), accountKeyPublic.getWeightedPublicKeys().get(0).getWeight());
        assertEquals("0xae6b72d7ce2c11520ac00cbd1c4da216171a96eae1ae3a0a1f979a554c9063ae",
                accountKeyPublic.getWeightedPublicKeys().get(0).getKey().getX());
        assertEquals(BigInteger.valueOf(1), accountKeyPublic.getWeightedPublicKeys().get(1).getWeight());
        assertEquals("0xd4256fc43f42b3313b7204e42a82893a8d9b562f6c9b39456ee989339949c67c",
                accountKeyPublic.getWeightedPublicKeys().get(1).getKey().getX());
        assertEquals(BigInteger.valueOf(1), accountKeyPublic.getWeightedPublicKeys().get(2).getWeight());
        assertEquals("0xd653eae5f0e9cd6bfe4c3929f4c4f28c94f3bd183eafafee2d73db38a020d9d8",
                accountKeyPublic.getWeightedPublicKeys().get(2).getKey().getX());
    }

    @Test
    public void testAccountEOA() throws IOException {
        String response = "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"accType\":1,\"account\":{\"balance\":4985316100000000000,\"humanReadable\":false,\"key\":{\"key\":{\"x\":\"0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7\",\"y\":\"0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6\"},\"keyType\":2},\"nonce\":11}}}";
        KlayAccount parsed = objectMapper.readValue(response, KlayAccount.class);
        assertTrue(parsed.getResult().getAccType() == 1);

        AccountEOA account = (AccountEOA) parsed.getResult().getAccount();
        assertEquals("4985316100000000000", account.getBalance());
        AccountKeyPublic accountKey = (AccountKeyPublic) account.getKey().getKey();
        assertEquals("0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7", accountKey.getX());
        assertEquals("0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6", accountKey.getY());
    }

    @Test
    public void testAccountSmartContract() throws IOException {
        String response = "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"accType\":2,\"account\":{\"balance\":\"0x0\",\"codeFormat\":0,\"codeHash\":\"80NXvdOay02rYC/JgQ7RfF7yoxY1N7W8P7BiPvkIeF8=\",\"humanReadable\":true,\"key\":{\"key\":{},\"keyType\":3},\"nonce\":0,\"storageRoot\":\"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\"}}}";
        KlayAccount parsed = objectMapper.readValue(response, KlayAccount.class);
        assertTrue(parsed.getResult().getAccType() == 2);

        AccountSmartContract account = (AccountSmartContract) parsed.getResult().getAccount();
        assertEquals("0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421", account.getStorageRoot());
        assertTrue(account.getKey().getKeyType() == 3);
    }

}

