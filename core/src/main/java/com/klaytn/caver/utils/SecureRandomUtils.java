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
 * This file is derived from web3j/crypto/src/main/java/org/web3j/crypto/SecureRandomUtils.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.utils;

import org.web3j.crypto.LinuxSecureRandom;

import java.security.SecureRandom;

public final class SecureRandomUtils {

    private static final SecureRandom SECURE_RANDOM;

    static {
        if (isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
        SECURE_RANDOM = new SecureRandom();
    }

    public static SecureRandom secureRandom() {
        return SECURE_RANDOM;
    }

    // Taken from BitcoinJ implementation
    // https://github.com/bitcoinj/bitcoinj/blob/3cb1f6c6c589f84fe6e1fb56bf26d94cccc85429/core/src/main/java/org/bitcoinj/core/Utils.java#L573
    private static int isAndroid = -1;

    static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }

    private SecureRandomUtils() { }
}
