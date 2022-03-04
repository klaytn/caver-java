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

package com.klaytn.caver.transaction.type;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractFeeDelegatedWithRatioTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.crypto.Hash;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a fee delegated account update with ratio transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/partial-fee-delegation#txtypefeedelegatedaccountupdatewithratio to see more detail.
 */
public class FeeDelegatedAccountUpdateWithRatio extends AbstractFeeDelegatedWithRatioTransaction {

    /**
     * An account instance includes account key to be updated to the account in the network.
     */
    Account account;

    /**
     * A unit price of gas in peb the sender will pay for a transaction fee.
     */
    String gasPrice = "0x";

    /**
     * FeeDelegatedAccountUpdateWithRatio Builder class.
     */
    public static class Builder extends AbstractFeeDelegatedWithRatioTransaction.Builder<FeeDelegatedAccountUpdateWithRatio.Builder> {
        Account account;
        String gasPrice = "0x";

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.toString());
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public Builder setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
            return this;
        }

        public Builder setGasPrice(BigInteger gasPrice) {
            setGasPrice(Numeric.toHexStringWithPrefix(gasPrice));
            return this;
        }

        public FeeDelegatedAccountUpdateWithRatio build() {
            return new FeeDelegatedAccountUpdateWithRatio(this);
        }
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance.
     * @param builder FeeDelegatedAccountUpdateWithRatio.Builder instance.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public static FeeDelegatedAccountUpdateWithRatio create(FeeDelegatedAccountUpdateWithRatio.Builder builder) {
        return new FeeDelegatedAccountUpdateWithRatio(builder);
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param account An account instance includes account key to be updated to the account in the network.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public static FeeDelegatedAccountUpdateWithRatio create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, Account account) {
        return new FeeDelegatedAccountUpdateWithRatio(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, feePayer, feePayerSignatures, feeRatio, account);
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance.
     * @param builder FeeDelegatedAccountUpdateWithRatio.Builder instance.
     */
    public FeeDelegatedAccountUpdateWithRatio(Builder builder) {
        super(builder);
        setAccount(builder.account);
        setGasPrice(builder.gasPrice);
    }

    /**
     * Creates a FeeDelegatedAccountUpdateWithRatio instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param feeRatio A fee ratio of the fee payer.
     * @param account An account instance includes account key to be updated to the account in the network.
     */
    public FeeDelegatedAccountUpdateWithRatio(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, String feeRatio, Account account) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.toString(),
                from,
                nonce,
                gas,
                chainId,
                signatures,
                feePayer,
                feePayerSignatures,
                feeRatio
        );
        setAccount(account);
        setGasPrice(gasPrice);
    }

    /**
     * Getter function for gas price
     * @return String
     */
    public String getGasPrice() {
        return gasPrice;
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(String gasPrice) {
        if(gasPrice == null || gasPrice.isEmpty() || gasPrice.equals("0x")) {
            gasPrice = "0x";
        }

        if(!gasPrice.equals("0x") && !Utils.isNumber(gasPrice)) {
            throw new IllegalArgumentException("Invalid gasPrice. : " + gasPrice);
        }

        this.gasPrice = gasPrice;
    }

    /**
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     */
    public void setGasPrice(BigInteger gasPrice) {
        setGasPrice(Numeric.toHexStringWithPrefix(gasPrice));
    }


    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio string.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public static FeeDelegatedAccountUpdateWithRatio decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdateWithRatio byte array.
     * @return FeeDelegatedAccountUpdateWithRatio
     */
    public static FeeDelegatedAccountUpdateWithRatio decode(byte[] rlpEncoded) {
        // type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, feeRatio, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.toString());
        }

        //remove Tag
        byte[] detachedType = Arrays.copyOfRange(rlpEncoded, 1, rlpEncoded.length);

        RlpList rlpList = RlpDecoder.decode(detachedType);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

        BigInteger nonce = ((RlpString) values.get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.get(1)).asPositiveBigInteger();
        BigInteger gas = ((RlpString) values.get(2)).asPositiveBigInteger();
        String from = ((RlpString) values.get(3)).asString();
        Account account = Account.createFromRLPEncoding(from, ((RlpString) values.get(4)).asString());
        BigInteger feeRatio = ((RlpString) values.get(5)).asPositiveBigInteger();

        List<RlpType> senderSignatures = ((RlpList) (values.get(6))).getValues();
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(7)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(8))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdateWithRatio = new FeeDelegatedAccountUpdateWithRatio.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setAccount(account)
                .setFeeRatio(feeRatio)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedAccountUpdateWithRatio;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, feeRatio, txSignatures, feePayer, feePayerSignatures])
        this.validateOptionalValues(false);

        List<RlpType> senderSignatureRLPList = new ArrayList<>();
        List<RlpType> feePayerSignatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            senderSignatureRLPList.add(signatureData.toRlpList());
        }

        for(SignatureData signatureData : this.getFeePayerSignatures()) {
            feePayerSignatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(account.getRLPEncodingAccountKey())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigFeePayerRLP = encode([encode([type, nonce, gasPrice, gas, from, rlpEncodedKey, feeRatio]), feePayer, chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, from, rlpEncodedKey, feeRatio])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(account.getRLPEncodingAccountKey())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }

    /**
     * Returns a senderTxHash of transaction
     * @return String
     */
    @Override
    public String getSenderTxHash() {
        // SenderTxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, feeRatio, txSignatures])
        this.validateOptionalValues(false);

        List<RlpType> signatureRLPList = new ArrayList<>();

        for(SignatureData signatureData : this.getSignatures()) {
            signatureRLPList.add(signatureData.toRlpList());
        }

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(account.getRLPEncodingAccountKey())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getFeeRatio())));
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedAccountUpdateWithRatio.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(Hash.sha3(rawTx));
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param txObj The AbstractFeeDelegatedWithRatioTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractFeeDelegatedWithRatioTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!(txObj instanceof FeeDelegatedAccountUpdateWithRatio)) return false;
        FeeDelegatedAccountUpdateWithRatio feeDelegatedAccountUpdate = (FeeDelegatedAccountUpdateWithRatio)txObj;

        if(!this.getAccount().getAddress().toLowerCase().equals(feeDelegatedAccountUpdate.getAccount().getAddress().toLowerCase())) {
            return false;
        }

        if(!this.getAccount().getRLPEncodingAccountKey().equals(feeDelegatedAccountUpdate.getAccount().getRLPEncodingAccountKey())) {
            return false;
        }
        if (!this.getGasPrice().equals(feeDelegatedAccountUpdate.getGasPrice())) return false;

        return true;
    }

    /**
     * Combines signatures to the transaction from RLP-encoded transaction strings and returns a single transaction with all signatures combined.
     * When combining the signatures into a transaction instance,
     * an error is thrown if the decoded transaction contains different value except signatures.
     * @param rlpEncoded A List of RLP-encoded transaction strings.
     * @return String
     */
    @Override
    public String combineSignedRawTransactions(List<String> rlpEncoded) {
        boolean fillVariable = false;

        // If the signatures are empty, there may be an undefined member variable.
        // In this case, the empty information is filled with the decoded result.
        // At initial state of AbstractFeeDelegateTx Object, feePayerSignature field has one empty signature.
        if((Utils.isEmptySig(this.getFeePayerSignatures())) || Utils.isEmptySig(this.getSignatures())) fillVariable = true;

        for(String encodedStr : rlpEncoded) {
            AbstractFeeDelegatedTransaction decode = (AbstractFeeDelegatedTransaction) TransactionDecoder.decode(encodedStr);
            if (!decode.getType().equals(this.getType())) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }
            FeeDelegatedAccountUpdateWithRatio txObj = (FeeDelegatedAccountUpdateWithRatio) decode;

            if(fillVariable) {
                if(this.getNonce().equals("0x")) this.setNonce(txObj.getNonce());
                if(this.getGasPrice().equals("0x")) this.setGasPrice(txObj.getGasPrice());
                if(this.getFeePayer().equals("0x") || this.getFeePayer().equals(Utils.DEFAULT_ZERO_ADDRESS)) {
                    if(!txObj.getFeePayer().equals("0x") && !txObj.getFeePayer().equals(Utils.DEFAULT_ZERO_ADDRESS)) {
                        this.setFeePayer(txObj.getFeePayer());
                        fillVariable = false;
                    }
                }
            }

            // Signatures can only be combined for the same transaction.
            // Therefore, compare whether the decoded transaction is the same as this.
            if(!this.compareTxField(txObj, false)) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }

            this.appendSignatures(txObj.getSignatures());
            this.appendFeePayerSignatures(txObj.getFeePayerSignatures());
        }

        return this.getRLPEncoding();
    }

    /**
     * Fills empty optional transaction field.(gasPrice)
     * @throws IOException
     */
    @Override
    public void fillTransaction() throws IOException {
        super.fillTransaction();
        if(this.gasPrice.equals("0x")) {
            this.setGasPrice(this.getKlaytnCall().getGasPrice().send().getResult());
        }
        if(this.getGasPrice().equals("0x")) {
            throw new RuntimeException("Cannot fill transaction data. (gasPrice). `klaytnCall` must be set in Transaction instance to automatically fill the nonce, chainId or gasPrice. Please call the `setKlaytnCall` to set `klaytnCall` in the Transaction instance.");
        }
    }

    /**
     * Checks that member variables that can be defined by the user are defined.
     * If there is an undefined variable, an error occurs.
     */
    @Override
    public void validateOptionalValues(boolean checkChainID) {
        super.validateOptionalValues(checkChainID);
        if(this.getGasPrice() == null || this.getGasPrice().isEmpty() || this.getGasPrice().equals("0x")) {
            throw new RuntimeException("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");
        }
    }


    /**
     * Getter function for Account
     * @return Account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Setter function for Account
     * @param account Account instance
     */
    public void setAccount(Account account) {
        if(account == null) {
            throw new IllegalArgumentException("account is missing.");
        }

        if(!this.getFrom().toLowerCase().equals(account.getAddress().toLowerCase())) {
            throw new  IllegalArgumentException("Transaction's 'from' address and 'account address' do not match.");
        }

        this.account = account;
    }
}
