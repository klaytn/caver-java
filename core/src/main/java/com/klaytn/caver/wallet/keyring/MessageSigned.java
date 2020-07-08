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
