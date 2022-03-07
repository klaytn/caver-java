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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an account update transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypeaccountupdate to see more detail.
 */
@JsonIgnoreProperties(value = { "chainId" })
public class AccountUpdate extends AbstractTransaction {

    /**
     * The Account instance contains AccountKey to be updated to the account.
     */
    Account account;

    /**
     * A unit price of gas in peb the sender will pay for a transaction fee.
     */
    String gasPrice = "0x";

    /**
     * AccountUpdate Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<AccountUpdate.Builder> {
        Account account;
        String gasPrice = "0x";

        public Builder() {
            super(TransactionType.TxTypeAccountUpdate.toString());
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

        public AccountUpdate build() {
            return new AccountUpdate(this);
        }
    }

    /**
     * Creates an AccountUpdate instance.
     * @param builder AccountUpdate.Builder instance.
     * @return AccountUpdate
     */
    public static AccountUpdate create(AccountUpdate.Builder builder) {
        return new AccountUpdate(builder);
    }

    /**
     * Creates an AccountUpdate instance.
     * @param klaytnCall Klay RPC instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param account The Account instance contains AccountKey to be updated to the account.
     * @return AccountUpdate
     */
    public static AccountUpdate create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, Account account) {
        return new AccountUpdate(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, account);
    }

    /**
     * Creates an AccountUpdate instance.
     * @param builder AccountUpdate.Builder instance.
     */
    public AccountUpdate(AccountUpdate.Builder builder) {
        super(builder);
        setAccount(builder.account);
        setGasPrice(builder.gasPrice);
    }

    /**
     * Creates an AccountUpdate instance.
     * @param klaytnCall Klay RPC instance.
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param account The Account instance contains AccountKey to be updated to the account.
     */
    public AccountUpdate(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, Account account) {
        super(
                klaytnCall,
                TransactionType.TxTypeAccountUpdate.toString(),
                from,
                nonce,
                gas,
                chainId,
                signatures
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
     * Decodes a RLP-encoded AccountUpdate string.
     * @param rlpEncoded RLP-encoded AccountUpdate string.
     * @return AccountUpdate
     */
    public static AccountUpdate decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded AccountUpdate byte array.
     * @param rlpEncoded RLP-encoded AccountUpdate byte array.
     * @return AccountUpdate
     */
    public static AccountUpdate decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, txSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeAccountUpdate.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeAccountUpdate.toString());
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

        List<RlpType> senderSignatures = ((RlpList) (values.get(5))).getValues();
        List<SignatureData> signatureDataList = SignatureData.decodeSignatures(senderSignatures);

        AccountUpdate accountUpdate = new AccountUpdate.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setAccount(account)
                .setSignatures(signatureDataList)
                .build();

        return accountUpdate;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, txSignatures])
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
        rlpTypeList.add(new RlpList(signatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeAccountUpdate.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        // SigRLP = encode([encode([type, nonce, gasPrice, gas, from, rlpEncodedKey]), chainid, 0, 0])
        // encode([type, nonce, gasPrice, gas, from, rlpEncodedKey]
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeAccountUpdate.getType();

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(type));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFrom())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(account.getRLPEncodingAccountKey())));

        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        String encodedStr = Numeric.toHexString(encoded);

        return encodedStr;
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param obj The AbstractTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractTransaction obj, boolean checkSig) {
        if(!super.compareTxField(obj, checkSig)) return false;
        if(!(obj instanceof AccountUpdate)) return false;
        AccountUpdate txObj = (AccountUpdate)obj;

        if(!this.getAccount().getAddress().toLowerCase().equals(txObj.getAccount().getAddress().toLowerCase())) {
            return false;
        }

        if(!this.getAccount().getRLPEncodingAccountKey().equals(txObj.getAccount().getRLPEncodingAccountKey())) {
            return false;
        }
        if(Numeric.toBigInt(this.getGasPrice()).compareTo(Numeric.toBigInt(txObj.getGasPrice())) != 0) return false;

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
        if(Utils.isEmptySig(this.getSignatures())) fillVariable = true;

        for(String encodedStr : rlpEncoded) {
            AbstractTransaction decode = TransactionDecoder.decode(encodedStr);
            if (!decode.getType().equals(this.getType())) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }
            AccountUpdate txObj = (AccountUpdate) decode;

            if(fillVariable) {
                if(this.getNonce().equals("0x")) this.setNonce(txObj.getNonce());
                if(this.getGasPrice().equals("0x")) this.setGasPrice(txObj.getGasPrice());
                fillVariable = false;
            }

            // Signatures can only be combined for the same transaction.
            // Therefore, compare whether the decoded transaction is the same as this.
            if(!this.compareTxField(txObj, false)) {
                throw new RuntimeException("Transactions containing different information cannot be combined.");
            }

            this.appendSignatures(txObj.getSignatures());
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
