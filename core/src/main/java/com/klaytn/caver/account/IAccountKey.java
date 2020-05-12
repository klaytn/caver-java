package com.klaytn.caver.account;

public interface IAccountKey {

    /**
     * Encodes AccountKey by RLP-encoding method.
     * @return RLP-encoded AccountKey String
     */
    String getRLPEncoding();
}
