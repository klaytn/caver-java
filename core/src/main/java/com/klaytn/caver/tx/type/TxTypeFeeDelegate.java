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
import com.klaytn.caver.tx.exception.EmptyNonceException;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This interface represents which is fee delegated transaction type
 */
public abstract class TxTypeFeeDelegate extends AbstractTxType {
    final static String EMPTY_FEE_PAYER_ADDRESS = "0x30";
    final static int DEFAULT_FEE_RATIO = 100;

    private Set<KlaySignatureData> feePayerSignatureData;
    private String feePayer;

    public TxTypeFeeDelegate(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                             String from, String to, BigInteger value) {
        super(nonce, gasPrice, gasLimit, from, to, value);
        this.feePayerSignatureData = new HashSet<>();
        this.feePayer = EMPTY_FEE_PAYER_ADDRESS;
    }

    public Set<KlaySignatureData> getFeePayerSignatureData() {
        return feePayerSignatureData;
    }

    public String getFeePayer() {
        return this.feePayer;
    }

    public void setFeePayer(String feePayer) {
        this.feePayer = feePayer;
    }

    public BigInteger getFeeRatio() {
        return BigInteger.valueOf(DEFAULT_FEE_RATIO);
    }

    /**
     * add a feePayer signature
     *
     * @param feePayerSignatureData signature data signed by feePayer
     */
    public void addFeePayerSignatureData(KlaySignatureData feePayerSignatureData) {
        this.feePayerSignatureData.add(feePayerSignatureData);
    }

    /**
     * add feePayers signature
     *
     * @param feePayerSignatureData signature data signed by feePayer
     */
    public void addFeePayerSignatureData(Set<KlaySignatureData> feePayerSignatureData) {
        this.feePayerSignatureData.addAll(feePayerSignatureData);
    }

    /**
     * add feePayers signature
     *
     * @param signatureRlpTypeList rlp type list of signatures
     */
    protected void addFeePayerSignatureData(List<RlpType> signatureRlpTypeList) {
        for (RlpType signatureRlpType : signatureRlpTypeList) {
            List<RlpType> vrs = ((RlpList) signatureRlpType).getValues();
            if (vrs.size() < 3) continue;
            byte[] v = ((RlpString) vrs.get(0)).getBytes();
            byte[] r = ((RlpString) vrs.get(1)).getBytes();
            byte[] s = ((RlpString) vrs.get(2)).getBytes();
            addFeePayerSignatureData(new KlaySignatureData(v, r, s));
        }
    }

    /**
     * add signature data
     *
     * @param values rlp encoded rawTransaction
     * @param offset where sender's signature data begins
     */
    public void addSignatureData(List<RlpType> values, int offset) {
        if (values.size() > offset) {
            List<RlpType> senderSignatures = ((RlpList) (values.get(offset))).getValues();
            addSenderSignatureData(senderSignatures);
        }

        if (values.size() > offset + 1) {
            String feePayer = ((RlpString) values.get(offset + 1)).asString();
            setFeePayer(feePayer);
        }

        if (values.size() > offset + 2) {
            List<RlpType> feePayerSignatures = ((RlpList) (values.get(offset + 2))).getValues();
            addFeePayerSignatureData(feePayerSignatures);
        }
    }

    /**
     * add signature data
     *
     * @param txType TxType holding a signature
     */
    public void addSignatureData(TxTypeFeeDelegate txType) {
        addSenderSignatureData(txType.getSenderSignatureDataSet());
        addFeePayerSignatureData(txType.getSenderSignatureDataSet());
    }

    /**
     * rlp encoding for transaction hash(TxHash)
     *
     * @param credentials credential info of a signer
     * @param chainId     chain ID
     * @return KlayRawTransaction this contains transaction hash and processed signature data
     * @throws EmptyNonceException throw exception when nonce is null
     */
    @Override
    public KlayRawTransaction sign(KlayCredentials credentials, int chainId) {
        if (getNonce() == null) {
            throw new EmptyNonceException();
        }
        Set<KlaySignatureData> newSignatureDataSet = getNewSenderSignatureDataSet(credentials, chainId);
        addSenderSignatureData(newSignatureDataSet);
        
        List<RlpType> rlpTypeList = new ArrayList<>(rlpValues());
        List<RlpType> senderSignatureList = new ArrayList<>();

        for (KlaySignatureData klaySignatureData : getSenderSignatureDataSet()) {
            senderSignatureList.add(klaySignatureData.toRlpList());
        }

        rlpTypeList.add(new RlpList(senderSignatureList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.feePayer)));
        if (this.feePayer.equals(EMPTY_FEE_PAYER_ADDRESS)) {
            rlpTypeList.add(new RlpList(KlaySignatureData.createKlaySignatureDataFromChainId(1).toRlpList()));
        } else {
            List<RlpType> feePayerSignatureList = new ArrayList<>();
            for (KlaySignatureData klaySignatureData : this.feePayerSignatureData) {
                feePayerSignatureList.add(klaySignatureData.toRlpList());
            }
            rlpTypeList.add(new RlpList(feePayerSignatureList));
        }

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = {getType().get()};
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);
        return new KlayRawTransaction(rawTx, getSenderSignatureData());
    }
}
