package com.klaytn.caver.transaction.type;

import com.klaytn.caver.Klay;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.AccountKeyDecoder;
import com.klaytn.caver.account.IAccountKey;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an account update transaction.
 * Please refer to https://docs.klaytn.com/klaytn/design/transactions/basic#txtypeaccountupdate to see more detail.
 */
public class AccountUpdate extends AbstractTransaction {

    /**
     * The Account instance contains AccountKey to be updated to the account.
     */
    Account account;

    /**
     * AccountUpdate Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<AccountUpdate.Builder> {
        Account account;

        public Builder() {
            super(TransactionType.TxTypeAccountUpdate.toString());
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public AccountUpdate build() {
            return new AccountUpdate(this);
        }
    }

    /**
     * Creates an AccountUpdate instance.
     * @param builder AccountUpdate.Builder instance.
     */
    public AccountUpdate(AccountUpdate.Builder builder) {
        super(builder);
        setAccount(builder.account);
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
    public AccountUpdate(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<KlaySignatureData> signatures, Account account) {
        super(
                klaytnCall,
                TransactionType.TxTypeAccountUpdate.toString(),
                from,
                nonce,
                gas,
                gasPrice,
                chainId,
                signatures
        );
        setAccount(account);
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
        List<KlaySignatureData> signatureDataList = KlaySignatureData.decodeSignatures(senderSignatures);

        AccountUpdate accountUpdate = new AccountUpdate.Builder()
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setGas(gas)
                .setFrom(from)
                .setAccount(account)
                .setSignList(signatureDataList)
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

        for(KlaySignatureData signatureData : this.getSignatures()) {
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
