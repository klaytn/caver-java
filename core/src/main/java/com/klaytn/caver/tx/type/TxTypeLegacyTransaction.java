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

package com.klaytn.caver.tx.type;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.KlaySignatureDataUtils;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.utils.KlayTransactionUtils;
import org.web3j.crypto.Sign;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * @deprecated This class replaced by {@link com.klaytn.caver.transaction.type.LegacyTransaction}
 */
@Deprecated
public class TxTypeLegacyTransaction extends AbstractTxType {

    /**
     * Data of the transaction
     */
    private String data;

    protected TxTypeLegacyTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                      BigInteger value, String data) {
        super(nonce, gasPrice, gasLimit, "", to, value);

        if (data != null) {
            this.data = Numeric.cleanHexPrefix(data);
        }
    }

    public static TxTypeLegacyTransaction createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {

        return new TxTypeLegacyTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    /**
     * decode transaction to reconstruct transaction
     *
     * @param rawTransaction RLP-encoded signed transaction
     * @return TxTypeValueTransfer decoded transaction
     */
    public static TxTypeLegacyTransaction decodeFromRawTransaction(byte[] rawTransaction) {
        // TxHashRLP = encode([nonce, gasPrice, gas, to, value, input, v, r, s])
        try {
            RlpList rlpList = RlpDecoder.decode(rawTransaction);
            List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

            BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
            BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
            BigInteger gasLimit = ((RlpString) values.get(2)).asPositiveBigInteger();
            String to = ((RlpString) values.get(3)).asString();
            BigInteger value = ((RlpString) values.get(4)).asPositiveBigInteger();
            String input = ((RlpString) values.get(5)).asString();

            TxTypeLegacyTransaction tx
                    = TxTypeLegacyTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, input);
            byte[] v = ((RlpString) values.get(6)).getBytes();
            byte[] r = ((RlpString) values.get(7)).getBytes();
            byte[] s = ((RlpString) values.get(8)).getBytes();
            KlaySignatureData signatureData = new KlaySignatureData(v, r, s);
            tx.addSenderSignatureData(signatureData);
            return tx;
        } catch (Exception e) {
            throw new RuntimeException("There is a error in the processing of decoding tx");
        }
    }

    /**
     * @param rawTransaction RLP-encoded signed transaction
     * @return TxTypeLegacyTransaction decoded transaction
     */
    public static TxTypeLegacyTransaction decodeFromRawTransaction(String rawTransaction) {
        return decodeFromRawTransaction(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(rawTransaction)));
    }

    public String getData() {
        return data;
    }

    /**
     * This method is overridden as LEGACY type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    @Override
    public Type getType() {
        return Type.LEGACY;
    }

    /**
     * rlp encoding for signature(SigRLP)
     *
     * @param credentials credential info of a signer
     * @param chainId     chain ID
     * @return KlaySignatureData processed signature data
     */
    @Override
    public KlaySignatureData getSignatureData(KlayCredentials credentials, int chainId) {
        KlaySignatureData signatureData = KlaySignatureData.createKlaySignatureDataFromChainId(chainId);
        List<RlpType> result = rlpValues();
        result.add(RlpString.create(Numeric.hexStringToByteArray(getTo())));
        result.add(RlpString.create(getValue()));
        result.add(RlpString.create(Numeric.hexStringToByteArray(getData())));
        result.addAll(signatureData.toRlpList().getValues());
        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(result));
        Sign.SignatureData signedSignatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        KlaySignatureData eip155SignatureData = KlaySignatureDataUtils.createEip155KlaySignatureData(signedSignatureData, chainId);
        return eip155SignatureData;
    }

    /**
     * rlp encoding for transaction hash(TxHash)
     *
     * @param credentials credential info of a signer
     * @param chainId     chain ID
     * @return KlayRawTransaction this contains transaction hash and processed signature data
     */
    @Override
    public KlayRawTransaction sign(KlayCredentials credentials, int chainId) {
        KlaySignatureData signatureData = getSignatureData(credentials, chainId);

        List<RlpType> result = rlpValues();
        String to = getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }
        result.add(RlpString.create(getValue()));
        byte[] data = Numeric.hexStringToByteArray(getData());
        result.add(RlpString.create(data));
        result.addAll(signatureData.toRlpList().getValues());

        return new KlayRawTransaction(RlpEncoder.encode(new RlpList(result)), signatureData);
    }

}

