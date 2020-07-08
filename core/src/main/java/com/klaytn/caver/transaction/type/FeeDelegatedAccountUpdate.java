package com.klaytn.caver.transaction.type;

import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.crypto.Hash;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a fee delegated account update transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/fee-delegation#txtypefeedelegatedaccountupdate to see more detail.
 */
public class FeeDelegatedAccountUpdate extends AbstractFeeDelegatedTransaction {

    /**
     * An account instance includes account key to be updated to the account in the network.
     */
    Account account;

    /**
     * FeeDelegatedAccountUpdate Builder class.
     */
    public static class Builder extends AbstractFeeDelegatedTransaction.Builder<FeeDelegatedAccountUpdate.Builder> {
        Account account;

        public Builder() {
            super(TransactionType.TxTypeFeeDelegatedAccountUpdate.toString());
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public FeeDelegatedAccountUpdate build() {
            return new FeeDelegatedAccountUpdate(this);
        }
    }

    /**
     * Creates a FeeDelegatedAccountUpdate instance.
     * @param builder FeeDelegatedAccountUpdate.Builder instance.
     */
    public FeeDelegatedAccountUpdate(Builder builder) {
        super(builder);
        setAccount(builder.account);
    }

    /**
     * Creates a FeeDelegatedAccountUpdate instance.
     * @param klaytnCall Klay RPC instance
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer A fee payer address.
     * @param feePayerSignatures A fee payer signature list.
     * @param account An account instance includes account key to be updated to the account in the network.
     */
    public FeeDelegatedAccountUpdate(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures, Account account) {
        super(
                klaytnCall,
                TransactionType.TxTypeFeeDelegatedAccountUpdate.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures,
                feePayer,
                feePayerSignatures
        );
        setAccount(account);
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdate string.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate string.
     * @return FeeDelegatedAccountUpdate
     */
    public static FeeDelegatedAccountUpdate decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded FeeDelegatedAccountUpdate byte array.
     * @param rlpEncoded RLP-encoded FeeDelegatedAccountUpdate byte array.
     * @return FeeDelegatedAccountUpdate
     */
    public static FeeDelegatedAccountUpdate decode(byte[] rlpEncoded) {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, txSignatures, feePayer, feePayerSignatures])
        if(rlpEncoded[0] != (byte)TransactionType.TxTypeFeeDelegatedAccountUpdate.getType()) {
            throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeFeeDelegatedAccountUpdate.toString());
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
        List<SignatureData> senderSignList = SignatureData.decodeSignatures(senderSignatures);

        String feePayer = ((RlpString) values.get(6)).asString();

        List<RlpType> feePayerSignatures = ((RlpList) (values.get(7))).getValues();
        List<SignatureData> feePayerSignList = SignatureData.decodeSignatures(feePayerSignatures);

        FeeDelegatedAccountUpdate feeDelegatedAccountUpdate = new FeeDelegatedAccountUpdate.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setAccount(account)
                .setSignatures(senderSignList)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignList)
                .build();

        return feeDelegatedAccountUpdate;
    }

    /**
     * Returns the RLP-encoded string of this transaction (i.e., rawTransaction).
     * @return String
     */
    @Override
    public String getRLPEncoding() {
        // TxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, txSignatures, feePayer, feePayerSignatures])
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
        rlpTypeList.add(new RlpList(senderSignatureRLPList));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(new RlpList(feePayerSignatureRLPList));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedAccountUpdate.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     * @return String
     */
    @Override
    public String getCommonRLPEncodingForSignature() {
        //SigRLP = encode([encode([type, nonce, gasPrice, gas, from, rlpEncodedKey]), chainid, 0, 0])
        //encode([type, nonce, gasPrice, gas, from, rlpEncodedKey])
        this.validateOptionalValues(true);

        byte type = (byte)TransactionType.TxTypeFeeDelegatedAccountUpdate.getType();

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
     * Returns a senderTxHash of transaction
     * @return String
     */
    @Override
    public String getSenderTxHash() {
        //SenderTxHashRLP = type + encode([nonce, gasPrice, gas, from, rlpEncodedKey, txSignatures])
        //SenderTxHash = keccak256(SenderTxHashRLP)
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
        byte[] type = new byte[] { (byte)TransactionType.TxTypeFeeDelegatedAccountUpdate.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(Hash.sha3(rawTx));
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     * @param txObj The AbstractFeeDelegatedTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractFeeDelegatedTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;
        if(!(txObj instanceof FeeDelegatedAccountUpdate)) return false;
        FeeDelegatedAccountUpdate feeDelegatedAccountUpdate = (FeeDelegatedAccountUpdate)txObj;

        if(!this.getAccount().getAddress().toLowerCase().equals(feeDelegatedAccountUpdate.getAccount().getAddress().toLowerCase())) {
            return false;
        }

        if(!this.getAccount().getRLPEncodingAccountKey().equals(feeDelegatedAccountUpdate.getAccount().getRLPEncodingAccountKey())) {
            return false;
        }

        return true;
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
