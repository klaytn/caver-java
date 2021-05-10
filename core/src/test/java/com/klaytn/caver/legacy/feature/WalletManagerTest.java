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

import com.klaytn.caver.wallet.WalletManager;
import com.klaytn.caver.wallet.exception.CredentialNotFoundException;
import org.junit.Test;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.assertEquals;

public class WalletManagerTest {

    @Test
    public void testDefaultWhenOnlyOne() throws CredentialNotFoundException {
        WalletManager walletManager = new WalletManager();
        walletManager.add(BRANDON);
        assertEquals(BRANDON, walletManager.getDefault());
    }

    @Test
    public void testDefaultWhenManyCredentials() throws CredentialNotFoundException {
        WalletManager walletManager = new WalletManager();
        walletManager.add(BRANDON);
        walletManager.add(LUMAN);
        assertEquals(BRANDON, walletManager.getDefault());
        assertEquals(LUMAN, walletManager.findByAddress(LUMAN.getAddress()));
        assertEquals(BRANDON, walletManager.findByAddress(BRANDON.getAddress()));
    }

    @Test(expected = CredentialNotFoundException.class)
    public void testDefaultWhenNull() throws CredentialNotFoundException {
        WalletManager walletManager = new WalletManager();
        walletManager.getDefault();
    }
}
