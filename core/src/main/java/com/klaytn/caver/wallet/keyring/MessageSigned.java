/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.wallet.keyring;

import java.util.List;

/**
 * Represents a signed data info
 */
public class MessageSigned {
    /**
     * Signed message
     */
    String messageHash;

    /**
     * ECDSA signature data(V, R, S) list
     */
    List<SignatureData> signatures;

    /**
     * Plain message
     */
    String message;

    /**
     * Creates a MessageSigned instance
     * @param messageHash Signed message string
     * @param signatures ECDSA signature data list
     * @param message Plain message string
     */
    public MessageSigned(String messageHash, List<SignatureData> signatures, String message) {
        this.messageHash = messageHash;
        this.signatures = signatures;
        this.message = message;
    }

    /**
     * Getter function of message hash
     * @return String
     */
    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Getter function of ECDSA signature data
     * @return SignatureData
     */
    public List<SignatureData> getSignatures() {
        return signatures;
    }

    /**
     * Getter function of message
     * @return String
     */
    public String getMessage() {
        return message;
    }
}
