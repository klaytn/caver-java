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
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/Credential.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.crypto;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import static com.klaytn.caver.wallet.KlayWalletUtils.*;

public class KlayCredentials {

    private final ECKeyPair ecKeyPair;
    private final String address;

    private KlayCredentials(ECKeyPair ecKeyPair, String address) {
        this.ecKeyPair = ecKeyPair;
        this.address = address;
    }

    public ECKeyPair getEcKeyPair() {
        return ecKeyPair;
    }

    public String getAddress() {
        return address;
    }

    public static KlayCredentials create(String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    public static KlayCredentials create(ECKeyPair ecKeyPair) {
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    public static KlayCredentials create(String privateKey, String address) {
        return create(ECKeyPair.create(Numeric.toBigInt(privateKey)), address);
    }

    public static KlayCredentials create(ECKeyPair ecKeyPair, String address) {
        return new KlayCredentials(ecKeyPair, address);
    }

    public static KlayCredentials createWithKlaytnWalletKey(String klaytnWalletKey) {
        klaytnWalletKey = Numeric.cleanHexPrefix(klaytnWalletKey);
        String privateKey = klaytnWalletKey.substring(0, 64);
        String address = klaytnWalletKey.substring(68);
        return create(privateKey, address);
    }

    public String getKlaytnWalletKey() {
        return Numeric.toHexStringWithPrefixZeroPadded(getEcKeyPair().getPrivateKey(), 64)
                + CHECKSUM
                + getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KlayCredentials that = (KlayCredentials) o;

        if (ecKeyPair != null ? !ecKeyPair.equals(that.ecKeyPair) : that.ecKeyPair != null) {
            return false;
        }

        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = ecKeyPair != null ? ecKeyPair.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}