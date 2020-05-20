package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.crypto.KlaySignatureData;

public class MessageSigned {
    String messageHash;
    KlaySignatureData signatureData;
    String message;

    public MessageSigned(String messageHash, KlaySignatureData signatureData, String message) {
        this.messageHash = messageHash;
        this.signatureData = signatureData;
        this.message = message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public KlaySignatureData getSignatureData() {
        return signatureData;
    }

    public String getMessage() {
        return message;
    }
}
