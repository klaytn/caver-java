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
package com.klaytn.caver.fee;

import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.tx.type.TxTypeFeeDelegate;
import com.klaytn.caver.utils.KlaySignatureDataUtils;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.type.AbstractTxType;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeePayer {

    final static String EMPTY_FEE_PAYER_ADDRESS = "0x30";
    private KlayCredentials credentials;
    private int chainId;

    public FeePayer(KlayCredentials credentials, int chainId) {
        this.credentials = credentials;
        this.chainId = chainId;
    }

    public KlayRawTransaction sign(TxTypeFeeDelegate txType) {
        Set<KlaySignatureData> feePayerSignatureDataSet = getFeePayerSignatureData(txType);

        List<RlpType> rlpTypeList = new ArrayList<>(txType.rlpValues());
        List<RlpType> senderSignatureList = new ArrayList<>();

        for (KlaySignatureData senderSignature : txType.getSenderSignatureDataSet()) {
            senderSignatureList.add(senderSignature.toRlpList());
        }
        rlpTypeList.add(new RlpList(senderSignatureList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(credentials.getAddress())));

        List<RlpType> feePayerSignatureList = new ArrayList<>();

        String feePayer = txType.getFeePayer();
        if (!feePayer.equals(EMPTY_FEE_PAYER_ADDRESS)) {
            for (KlaySignatureData feePayerSignatureData : txType.getFeePayerSignatureData()) {
                feePayerSignatureList.add(feePayerSignatureData.toRlpList());
            }
        }

        for (KlaySignatureData feePayerSignatureData : feePayerSignatureDataSet) {
            feePayerSignatureList.add(feePayerSignatureData.toRlpList());
        }
        rlpTypeList.add(new RlpList(feePayerSignatureList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = {txType.getType().get()};
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);
        return new KlayRawTransaction(rawTx, feePayerSignatureDataSet);
    }

    @Deprecated
    public KlaySignatureData getSignatureData(AbstractTxType txType) {
        KlaySignatureData signatureData = KlaySignatureData.createKlaySignatureDataFromChainId(chainId);
        byte[] encodedTransaction = txType.getEncodedTransactionNoSig();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(encodedTransaction));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(credentials.getAddress())));
        rlpTypeList.addAll(signatureData.toRlpList().getValues());
        byte[] encodedTransaction2 = RlpEncoder.encode(new RlpList(rlpTypeList));

        Sign.SignatureData signedSignatureData = Sign.signMessage(encodedTransaction2, credentials.getEcKeyPair());
        return KlaySignatureDataUtils.createEip155KlaySignatureData(signedSignatureData, chainId);
    }

    /**
     * extract signature data of fee payer signed in TxType
     *
     * @param txType txType to extract fee payer's signature data
     * @return Set fee payer's signature data
     */
    private Set<KlaySignatureData> getFeePayerSignatureData(AbstractTxType txType) {
        KlaySignatureData signatureData = KlaySignatureData.createKlaySignatureDataFromChainId(chainId);
        Set<KlaySignatureData> feePayerSignatureDataSet = new HashSet<>();
        byte[] encodedTransactionNoSig = txType.getEncodedTransactionNoSig();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(encodedTransactionNoSig));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(credentials.getAddress())));
        rlpTypeList.addAll(signatureData.toRlpList().getValues());
        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));

        for (ECKeyPair ecKeyPair : credentials.getEcKeyPairsForFeePayerList()) {
            Sign.SignatureData signedSignatureData = Sign.signMessage(encodedTransaction, ecKeyPair);
            feePayerSignatureDataSet.add(KlaySignatureDataUtils.createEip155KlaySignatureData(signedSignatureData, chainId));
        }

        return feePayerSignatureDataSet;
    }
}
