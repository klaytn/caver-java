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

package com.klaytn.caver.legacy.feature;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.account.*;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class AccountKeyTest {

    @Test
    public void testAccountKeyNil() {
        AccountKeyNil accountKeyNil = AccountKeyNil.create();
        assertEquals("0x80", Numeric.toHexString(accountKeyNil.toRlp()));
    }

    @Test
    public void testAccountKeyLegacy() {
        AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.create();
        assertEquals("0x01c0", Numeric.toHexString(accountKeyLegacy.toRlp()));
    }

    @Test
    public void testAccountKeyPublic() {
        KlayCredentials credentials = KlayCredentials.create("0xf8cc7c3813ad23817466b1802ee805ee417001fcce9376ab8728c92dd8ea0a6b");
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        AccountKeyPublic accountKeyPublic = AccountKeyPublic.create(publicKey);

        assertEquals("0xf8cc7c3813ad23817466b1802ee805ee417001fcce9376ab8728c92dd8ea0a6b",
                Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
        assertEquals("0xdbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8",
                accountKeyPublic.getX());
        assertEquals("0x906d7170ba349c86879fb8006134cbf57bda9db9214a90b607b6b4ab57fc026e",
                accountKeyPublic.getY());

        String rawTransaction = Numeric.toHexString(accountKeyPublic.toRlp());

        assertEquals("0x02a102dbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8",
                rawTransaction);

        AccountKeyPublic decodedPublic = AccountKeyPublic.decodeFromRlp(rawTransaction);
        assertEquals(AccountKey.Type.PUBLIC, decodedPublic.getType());

        assertEquals(publicKey, decodedPublic.getPublicKey());
    }

    @Test
    public void testAccountKeyWeightedMultiSig() {
        BigInteger threshold = BigInteger.valueOf(3);
        AccountKeyWeightedMultiSig.WeightedPublicKey key0 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0xc734b50ddb229be5e929fc4aa8080ae8240a802d23d3290e5e6156ce029b110e",
                        "0x61a443ac3ffff164d1fb3617875f07641014cf17af6b7dc38e429fe838763712"
                )
        );
        AccountKeyWeightedMultiSig.WeightedPublicKey key1 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0x12d45f1cc56fbd6cd8fc877ab63b5092ac77db907a8a42c41dad3e98d7c64dfb",
                        "0x8ef355a8d524eb444eba507f236309ce08370debaa136cb91b2f445774bff842"
                )
        );
        AccountKeyWeightedMultiSig.WeightedPublicKey key2 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0xea9a9f85065a00d7b9ffd3a8532a574035984587fd08107d8f4cbad6b786b0cd",
                        "0xb95ebb02d9397b4a8faceb58d485d612f0379a923ec0ddcf083378460a56acca"
                )
        );
        AccountKeyWeightedMultiSig.WeightedPublicKey key3 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0x8551bc489d62fa2e6f767ba87fe93a62b679fca8ff3114eb5805e6487b51e8f6",
                        "0x4206aa84bc8955fcbfcc396854228aa63ebacd81b7311a31ab9d71d90b7ec3d7"
                )
        );

        List<AccountKeyWeightedMultiSig.WeightedPublicKey> weightedPublicKeys = new ArrayList<>();
        weightedPublicKeys.add(key0);
        weightedPublicKeys.add(key1);
        weightedPublicKeys.add(key2);
        weightedPublicKeys.add(key3);

        AccountKeyWeightedMultiSig accountKeyWeightedMultiSig
                = AccountKeyWeightedMultiSig.create(threshold, weightedPublicKeys);
        String rawTx = Numeric.toHexString(accountKeyWeightedMultiSig.toRlp());
        assertEquals(
                "0x04f89303f890e301a102c734b50ddb229be5e929fc4aa8080ae8240a802d23d3290e5e6156ce029b110ee301a10212d45f1cc56fbd6cd8fc877ab63b5092ac77db907a8a42c41dad3e98d7c64dfbe301a102ea9a9f85065a00d7b9ffd3a8532a574035984587fd08107d8f4cbad6b786b0cde301a1038551bc489d62fa2e6f767ba87fe93a62b679fca8ff3114eb5805e6487b51e8f6",
                rawTx
        );

        // Decoding
        AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decodeFromRlp(rawTx);
        assertEquals(AccountKey.Type.MULTISIG, multiSig.getType());
        assertEquals(threshold, multiSig.getThreshold());
        assertEquals(4, multiSig.getWeightedPublicKeys().size());
        assertEquals(key0.getKey().getX(), multiSig.getWeightedPublicKeys().get(0).getKey().getX());
        assertEquals(key0.getKey().getY(), multiSig.getWeightedPublicKeys().get(0).getKey().getY());
        assertEquals(key0.getWeight(), multiSig.getWeightedPublicKeys().get(0).getWeight());
        assertEquals(key1.getKey().getX(), multiSig.getWeightedPublicKeys().get(1).getKey().getX());
        assertEquals(key1.getKey().getY(), multiSig.getWeightedPublicKeys().get(1).getKey().getY());
        assertEquals(key1.getWeight(), multiSig.getWeightedPublicKeys().get(1).getWeight());
        assertEquals(key2.getKey().getX(), multiSig.getWeightedPublicKeys().get(2).getKey().getX());
        assertEquals(key2.getKey().getY(), multiSig.getWeightedPublicKeys().get(2).getKey().getY());
        assertEquals(key2.getWeight(), multiSig.getWeightedPublicKeys().get(2).getWeight());
        assertEquals(key3.getKey().getX(), multiSig.getWeightedPublicKeys().get(3).getKey().getX());
        assertEquals(key3.getKey().getY(), multiSig.getWeightedPublicKeys().get(3).getKey().getY());
        assertEquals(key3.getWeight(), multiSig.getWeightedPublicKeys().get(3).getWeight());
    }

    @Test
    public void testAccountKeyRoleBased() {

        AccountKeyPublic roleTransaction = AccountKeyPublic.create(
                "0xe4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512d",
                "0x0a5735a23ce1654b14680054a993441eae7c261983a56f8e0da61280758b5919"
        );

        AccountKeyWeightedMultiSig.WeightedPublicKey roleUpdateKey0 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0xe4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512d",
                        "0x0a5735a23ce1654b14680054a993441eae7c261983a56f8e0da61280758b5919"
                )
        );
        AccountKeyWeightedMultiSig.WeightedPublicKey roleUpdateKey1 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0x36f6355f5b532c3c1606f18fa2be7a16ae200c5159c8031dd25bfa389a4c9c06",
                        "0x6fdf9fc87a16ac359e66d9761445d5ccbb417fb7757a3f5209d713824596a50d"
                )
        );
        List<AccountKeyWeightedMultiSig.WeightedPublicKey> weightedPublicKeys = new ArrayList<>();
        weightedPublicKeys.add(roleUpdateKey0);
        weightedPublicKeys.add(roleUpdateKey1);

        AccountKeyWeightedMultiSig roleUpdate = AccountKeyWeightedMultiSig.create(
                BigInteger.valueOf(2), weightedPublicKeys
        );

        AccountKeyPublic roleFeePayer = AccountKeyPublic.create(
                "0xc8785266510368d9372badd4c7f4a94b692e82ba74e0b5e26b34558b0f081447",
                "0x94c27901465af0a703859ab47f8ae17e54aaba453b7cde5a6a9e4a32d45d72b2"
        );

        String rawTransaction = Numeric.toHexString(
                AccountKeyRoleBased.create(Arrays.asList(roleTransaction, roleUpdate, roleFeePayer)).toRlp()
        );
        assertEquals("0x05f898a302a103e4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512db84e04f84b02f848e301a103e4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512de301a10336f6355f5b532c3c1606f18fa2be7a16ae200c5159c8031dd25bfa389a4c9c06a302a102c8785266510368d9372badd4c7f4a94b692e82ba74e0b5e26b34558b0f081447"
                , rawTransaction);

        // Decoding
        AccountKeyRoleBased decodeRoleBased = AccountKeyRoleBased.decodeFromRlp(rawTransaction);
        assertEquals(AccountKey.Type.PUBLIC, decodeRoleBased.getRoleTransaction().getType());
        assertEquals(AccountKey.Type.MULTISIG, decodeRoleBased.getRoleUpdate().getType());
        assertEquals(AccountKey.Type.PUBLIC, decodeRoleBased.getRoleFeePayer().getType());

        AccountKeyNil accountKeyNil = AccountKeyNil.create();
        AccountKeyFail accountKeyFail = AccountKeyFail.create();
        AccountKeyLegacy accountKeyLegacy = AccountKeyLegacy.create();
        AccountKeyRoleBased newRoleBased = AccountKeyRoleBased.create(Arrays.asList(
                accountKeyNil,
                accountKeyFail,
                accountKeyLegacy
        ));
        AccountKeyRoleBased decodeRoleBased2
                = AccountKeyRoleBased.decodeFromRlp(Numeric.toHexString(newRoleBased.toRlp()));
        assertEquals(AccountKey.Type.NIL, decodeRoleBased2.getAccountKeys().get(0).getType());
        assertEquals(AccountKey.Type.FAIL, decodeRoleBased2.getAccountKeys().get(1).getType());
        assertEquals(AccountKey.Type.LEGACY, decodeRoleBased2.getAccountKeys().get(2).getType());
    }

    @Test
    public void testIncompleteAccountKeyRoleBased() {

        AccountKeyPublic roleTransaction = AccountKeyPublic.create(
                "0xe4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512d",
                "0x0a5735a23ce1654b14680054a993441eae7c261983a56f8e0da61280758b5919"
        );

        AccountKeyWeightedMultiSig.WeightedPublicKey roleUpdateKey0 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0xe4a01407460c1c03ac0c82fd84f303a699b210c0b054f4aff72ff7dcdf01512d",
                        "0x0a5735a23ce1654b14680054a993441eae7c261983a56f8e0da61280758b5919"
                )
        );
        AccountKeyWeightedMultiSig.WeightedPublicKey roleUpdateKey1 = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                BigInteger.valueOf(1),
                AccountKeyPublic.create(
                        "0x36f6355f5b532c3c1606f18fa2be7a16ae200c5159c8031dd25bfa389a4c9c06",
                        "0x6fdf9fc87a16ac359e66d9761445d5ccbb417fb7757a3f5209d713824596a50d"
                )
        );
        List<AccountKeyWeightedMultiSig.WeightedPublicKey> weightedPublicKeys = new ArrayList<>();
        weightedPublicKeys.add(roleUpdateKey0);
        weightedPublicKeys.add(roleUpdateKey1);

        AccountKeyWeightedMultiSig roleUpdate = AccountKeyWeightedMultiSig.create(
                BigInteger.valueOf(2), weightedPublicKeys
        );

        String rawTransaction = Numeric.toHexString(AccountKeyRoleBased.create(Arrays.asList(roleTransaction, roleUpdate)).toRlp());

        // Decoding
        AccountKeyRoleBased decodeRoleBased = AccountKeyRoleBased.decodeFromRlp(rawTransaction);
        assertEquals(AccountKey.Type.PUBLIC, decodeRoleBased.getRoleTransaction().getType());
        assertEquals(AccountKey.Type.MULTISIG, decodeRoleBased.getRoleUpdate().getType());
    }
}
