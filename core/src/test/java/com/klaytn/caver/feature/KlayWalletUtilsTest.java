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

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.wallet.KlayWalletUtils;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class KlayWalletUtilsTest {

    @Test
    public void testGenerateNewWalletAndLoad() throws Exception {
        String keystoreFilePath = KlayWalletUtils.generateNewWalletFile(
                "password",
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );

        KlayCredentials credentials = KlayWalletUtils.loadCredentials("password", keystoreFilePath);
        assertNotNull(credentials);

        new File(keystoreFilePath).delete();
    }

    @Test
    public void testGenerateFullNewWalletAndLoad() throws Exception {
        String keystoreFilePath = KlayWalletUtils.generateFullNewWalletFile(
                "password",
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );

        KlayCredentials credentials = KlayWalletUtils.loadCredentials("password", keystoreFilePath);
        assertNotNull(credentials);

        new File(keystoreFilePath).delete();
    }

    @Test
    public void testCreateKlayCredentialsWithHumanReadable() {
        String humanReadableAddress = "hello.klaytn";
        KlayCredentials credentials = KlayCredentials.create("0xd0fdd0999ca4dac89be8658c8a9a51a719c6f03060649bc39a3ac335b837a9b1", humanReadableAddress);
        assertEquals(credentials.getAddress(), humanReadableAddress);
    }

    @Test
    public void testCreateKlayCredentials() {
        KlayCredentials credentials = KlayCredentials.create("0xd0fdd0999ca4dac89be8658c8a9a51a719c6f03060649bc39a3ac335b837a9b1");
        assertEquals(credentials.getAddress(), "0x6f19c2f3c9694612a5c3e9f4341c243a26687110");
    }

    @Test
    public void testGetKlaytnWalletKeyNormal() {
        KlayCredentials credentials = KlayCredentials.create(
                "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"
        );
        assertEquals("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc30x000x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                credentials.getKlaytnWalletKey());

        KlayCredentials credentials2 = KlayCredentials.create(
                "0x600dfc414fe433881f6606c24955e4143df9d203ccb3e335efe970a4ad017d04"
        );
        assertEquals("0x600dfc414fe433881f6606c24955e4143df9d203ccb3e335efe970a4ad017d040x000xee135d0b57c7ff81b198763cfd7c43f03a5f7622",
                credentials2.getKlaytnWalletKey());
    }

    @Test
    public void testLoadKlaytnWalletKeyNormal() {
        KlayCredentials credentials = KlayWalletUtils.loadCredentials("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc30x000x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a");
        assertEquals("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3",
                Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
        assertEquals("0x2c8ad0ea2e0781db8b8c9242e07de3a5beabb71a",
                credentials.getAddress());


        KlayCredentials credentials2 = KlayWalletUtils.loadCredentials("0x600dfc414fe433881f6606c24955e4143df9d203ccb3e335efe970a4ad017d040x000xee135d0b57c7ff81b198763cfd7c43f03a5f7622");
        assertEquals("0x600dfc414fe433881f6606c24955e4143df9d203ccb3e335efe970a4ad017d04",
                Numeric.toHexStringWithPrefix(credentials2.getEcKeyPair().getPrivateKey()));
        assertEquals("0xee135d0b57c7ff81b198763cfd7c43f03a5f7622", credentials2.getAddress());
    }

}
