package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.KlaySignatureDataUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.Wallet;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class PrivateKey {
    static final int LEN_UNCOMPRESSED_PUBLIC_KEY_STRING = 128;
    private String privateKey;

    public PrivateKey(String privateKey) {
        if(!Utils.isPrivateKeyValid(privateKey)) {
            throw new IllegalArgumentException("Invalid private key.");
        }
        this.privateKey = Numeric.prependHexPrefix(privateKey);
    }

    public static PrivateKey generate() {
        return PrivateKey.generate(null);
    }

    public static PrivateKey generate(String entropy) {
        byte[] random = Utils.generateRandomBytes(32);

        byte[] entropyArr;
        if(entropy == null || entropy.isEmpty()) {
            entropyArr = Utils.generateRandomBytes(32);
        } else {
            entropyArr = Numeric.hexStringToByteArray(entropy);
        }

        byte[] innerHex = Hash.sha3(BytesUtils.concat(random, entropyArr));
        byte[] middleHex = BytesUtils.concat(BytesUtils.concat(Utils.generateRandomBytes(32), innerHex), Utils.generateRandomBytes(32));

        String outerHex = Numeric.toHexString(Hash.sha3(middleHex));

        return new PrivateKey(outerHex);
    }

    public KlaySignatureData sign(String sigHash, int chainId) {
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        Sign.SignatureData signatureData = Sign.signMessage(Numeric.hexStringToByteArray(sigHash), keyPair);
        KlaySignatureData klaySignatureData = KlaySignatureDataUtils.createEip155KlaySignatureData(signatureData, chainId);

        return klaySignatureData;
    }

    public KlaySignatureData signMessage(String messageHash) {
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        Sign.SignatureData signatureData = Sign.signMessage(Numeric.hexStringToByteArray(messageHash), keyPair);
        KlaySignatureData klaySignatureData = new KlaySignatureData(new byte[]{signatureData.getV()}, signatureData.getR(), signatureData.getS());

        return klaySignatureData;
    }

    public String getPublicKey(boolean compressed) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));

        if(compressed) {
            return AccountKeyPublicUtils.compressPublicKey(Numeric.toHexStringWithPrefixZeroPadded(publicKey, LEN_UNCOMPRESSED_PUBLIC_KEY_STRING));
        }

        return Numeric.toHexStringNoPrefixZeroPadded(publicKey, LEN_UNCOMPRESSED_PUBLIC_KEY_STRING);
    }

    public String getDerivedAddress() {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));
        return Keys.getAddress(publicKey);
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
