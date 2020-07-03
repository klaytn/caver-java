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

package com.klaytn.caver.wallet;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.wallet.exception.CredentialNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @deprecated This class replaced by {@link com.klaytn.caver.wallet.KeyringContainer}
 * @see com.klaytn.caver.wallet.keyring.KeyringFactory
 * @see com.klaytn.caver.wallet.keyring.AbstractKeyring
 */
@Deprecated
public class WalletManager {

    private Optional<String> defaultAddress = Optional.empty();

    private Map<String, KlayCredentials> accounts;

    public WalletManager() {
        this.accounts = new HashMap<>();
    }

    public Map<String, KlayCredentials> getAccounts() {
        return accounts;
    }

    public void add(KlayCredentials credentials) {
        if(!defaultAddress.isPresent()) {
            defaultAddress = Optional.of(credentials.getAddress());
        }
        accounts.put(credentials.getAddress(), credentials);
    }


    public void remove(String address) {
        accounts.remove(address);
    }

    public void clear() {
        accounts.clear();
    }

    public KlayCredentials getDefault() throws CredentialNotFoundException {
        if(!defaultAddress.isPresent()) {
            throw new CredentialNotFoundException();
        }
        return accounts.get(defaultAddress.get());
    }

    public KlayCredentials findByAddress(String from) throws CredentialNotFoundException {
        KlayCredentials credentials = accounts.getOrDefault(from, null);
        if(credentials == null) {
            throw new CredentialNotFoundException();
        }
        return credentials;
    }
}
