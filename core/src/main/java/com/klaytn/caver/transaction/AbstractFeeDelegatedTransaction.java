package com.klaytn.caver.transaction;

import com.klaytn.caver.Klay;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SignatureData;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

abstract public class AbstractFeeDelegatedTransaction extends AbstractTransaction{

    /**
     * The address of fee payer.
     */
    String feePayer;

    /**
     * The fee payer's signatures.
     */
    List<SignatureData> feePayerSignatures = new ArrayList<>();

    /**
     * Represent a AbstractFeeDelegatedTransaction builder
     * @param <B> An generic extends to AbstractFeeDelegatedTransaction.Builder
     */
    public static class Builder<B extends AbstractFeeDelegatedTransaction.Builder> extends AbstractTransaction.Builder<B> {
        String feePayer;
        private List<SignatureData> feePayerSignatures = new ArrayList<>();

        public Builder(String type) {
            super(type);
        }

        public B setFeePayer(String feePayer) {
            this.feePayer = feePayer;
            return (B) this;
        }

        public B setFeePayerSignatures(List<SignatureData> feePayerSignatures) {
            this.feePayerSignatures = feePayerSignatures;
            return (B) this;
        }

        public B setFeePayerSignatures(SignatureData data) {
            if(data == null) {
                data = SignatureData.getEmptySignature();
            }

            this.feePayerSignatures.add(data);
            return (B) this;
        }
    }

    /**
     * Create an AbstractFeeDelegatedTransaction instance
     * @param builder AbstractFeeDelegatedTransaction.Builder
     */
    public AbstractFeeDelegatedTransaction(Builder builder) {
        super(builder);
        setFeePayer(builder.feePayer);
        setFeePayerSignatures(builder.feePayerSignatures);
    }

    /**
     * Create an AbstractFeeDelegatedTransaction instance
     * @param klaytnCall Klay RPC instance
     * @param type Transaction's type string
     * @param from The address of the sender.
     * @param nonce A value used to uniquely identify a sender’s transaction.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId Network ID
     * @param signatures A Signature list
     * @param feePayer The address of the fee payer.
     * @param feePayerSignatures The fee payers's signatures.
     */
    public AbstractFeeDelegatedTransaction(Klay klaytnCall, String type, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String feePayer, List<SignatureData> feePayerSignatures) {
        super(klaytnCall, type, from, nonce, gas, gasPrice, chainId, signatures);
        setFeePayer(feePayer);
        setFeePayerSignatures(feePayerSignatures);
    }

    /**
     * Signs to the transaction with a single private key as a fee payer.
     * It sets Hasher default value.
     *   - signer : TransactionHasher.getHashForFeePayerSignature()
     * @param keyString The private key string.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String keyString) throws IOException {
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey(keyString);

        return signAsFeePayer(keyring, TransactionHasher::getHashForFeePayerSignature);
    }

    /**
     * Signs to the transaction with a single private key as a fee payer.
     * @param keyString The private key string.
     * @param hasher The function to get hash of transaction.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String keyString, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey(keyString);

        return signAsFeePayer(keyring, hasher);
    }

    /**
     * Sign the transaction as a fee payer using all private keys used as roleFeePayerKey in the Keyring instance.
     * It sets index and Hasher default value.
     *    - signer : TransactionHasher.getHashForFeePayerSignature()
     * @param keyring The Keyring instance.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(AbstractKeyring keyring) throws IOException {
        return signAsFeePayer(keyring, TransactionHasher::getHashForFeePayerSignature);
    }

    /**
     * Sign the the transaction as a fee payer using a private key at the index among the private keys used as roleFeePayerKey in the Keyring instance.
     * @param keyring The Keyring instance.
     * @param index The index of private key to use in Keyring instance.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(AbstractKeyring keyring, int index) throws IOException {
        return signAsFeePayer(keyring, index, TransactionHasher::getHashForFeePayerSignature);
    }

    /**
     * Sign the transaction as a fee payer using all private keys used as roleFeePayerKey in the Keyring instance.
     * @param keyring The Keyring instance.
     * @param hasher The function to get hash of transaction.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(AbstractKeyring keyring, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(this.getFeePayer().equals("0x")) {
            this.setFeePayer(keyring.getAddress());
        }

        if(!this.getFeePayer().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The feePayer address of the transaction is different with the address of the keyring to use.");
        }

        this.fillTransaction();
        int role = AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex();

        String hash = hasher.apply(this);
        List<SignatureData> sigList = keyring.sign(hash, Numeric.toBigInt(this.getChainId()).intValue(), role);

        this.appendFeePayerSignatures(sigList);

        return this;
    }

    /**
     * Sign the the transaction as a fee payer using a private key at the index among the private keys used as roleFeePayerKey in the Keyring instance.
     * @param keyring The Keyring instance.
     * @param index The index of private key to use in Keyring instance.
     * @param hasher The function to get hash of transaction.
     * @return AbstractFeeDelegatedTransaction
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(AbstractKeyring keyring, int index, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(this.getFeePayer().equals("0x")) {
            this.setFeePayer(keyring.getAddress());
        }

        if(!this.getFeePayer().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The feePayer address of the transaction is different with the address of the keyring to use.");
        }

        this.fillTransaction();
        int role = AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex();

        String hash = hasher.apply(this);
        SignatureData sigList = keyring.sign(hash, Numeric.toBigInt(this.getChainId()).intValue(), role, index);

        this.appendFeePayerSignatures(sigList);

        return this;
    }

    /**
     * Appends fee payer's signatures to the transaction.
     * @param signatureData SignatureData instance contains ECDSA signature data
     */
    public void appendFeePayerSignatures(SignatureData signatureData) {
        List<SignatureData> feePayerSignatureList = new ArrayList<>();
        feePayerSignatureList.add(signatureData);

        appendFeePayerSignatures(feePayerSignatureList);
    }

    /**
     * Appends fee payer's signatures to the transaction.
     * @param signatureData List of SignatureData contains ECDSA signature data
     */
    public void appendFeePayerSignatures(List<SignatureData> signatureData) {
        setFeePayerSignatures(signatureData);
    }

    /**
     * Combines signatures and feePayerSignatures to the transaction from RLP-encoded transaction strings and returns a single transaction with all signatures combined.
     * When combining the signatures into a transaction instance,
     * an error is thrown if the decoded transaction contains different value except signatures.
     * @param rlpEncoded A List of RLP-encoded transaction strings.
     * @return String
     */
    public String combineSignatures(List<String> rlpEncoded) {
        boolean fillVariable = false;

        // If the signatures are empty, there may be an undefined member variable.
        // In this case, the empty information is filled with the decoded result.
        // At initial state of AbstractFeeDelegateTx Object, feePayerSignature field has one empty signature.
        if((Utils.isEmptySig(this.getFeePayerSignatures())) || Utils.isEmptySig(this.getSignatures())) fillVariable = true;

        for(String encodedStr : rlpEncoded) {
            AbstractFeeDelegatedTransaction txObj = (AbstractFeeDelegatedTransaction) TransactionDecoder.decode(encodedStr);

            if(fillVariable) {
                if(this.getNonce().equals("0x")) this.setNonce(txObj.getNonce());
                if(this.getGasPrice().equals("0x")) this.setGasPrice(txObj.getGasPrice());
                if(this.getFeePayer().equals("0x")) {
                    if(!txObj.getFeePayer().equals("0x")) {
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
     * Returns a RLP-encoded transaction string for making fee payer's signature.
     * @return String
     */
    public String getRLPEncodingForFeePayerSignature() {
        byte[] txRLP = Numeric.hexStringToByteArray(getCommonRLPEncodingForSignature());

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(txRLP));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getFeePayer())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getChainId())));
        rlpTypeList.add(RlpString.create(0));
        rlpTypeList.add(RlpString.create(0));
        byte[] encoded = RlpEncoder.encode(new RlpList(rlpTypeList));
        return Numeric.toHexString(encoded);
    }

    /**
     * Check equals txObj passed parameter and current instance.
     * @param txObj The AbstractFeeDelegatedTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    public boolean compareTxField(AbstractFeeDelegatedTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;

        if(!this.getFeePayer().toLowerCase().equals(txObj.getFeePayer().toLowerCase())) return false;

        if(checkSig) {
            List<SignatureData> dataList = this.getFeePayerSignatures();

            if(dataList.size() != txObj.getFeePayerSignatures().size()) return false;

            for(int i=0; i< dataList.size(); i++) {
                if(!dataList.get(i).equals(txObj.getFeePayerSignatures().get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Getter function for feePayer
     * @return String
     */
    public String getFeePayer() {
        return feePayer;
    }

    /**
     * Setter function for feePayer
     * @param feePayer The address of fee payer.
     */
    public void setFeePayer(String feePayer) {
        if(feePayer == null) {
            feePayer = "0x";
        }

        if(!feePayer.equals("0x") && !Utils.isAddress(feePayer)) {
            throw new IllegalArgumentException("Invalid address. : " + feePayer);
        }

        this.feePayer = feePayer;
    }

    /**
     * Getter function for feePayerSignatures
     * @return List
     */
    public List<SignatureData> getFeePayerSignatures() {
        return feePayerSignatures;
    }

    public void setFeePayerSignatures(List<SignatureData> feePayerSignatures) {
        if(feePayerSignatures != null && feePayerSignatures.size() != 0 && !Utils.isEmptySig(feePayerSignatures)) {
            if (feePayer.equals("0x")) {
                throw new IllegalArgumentException("feePayer is missing: feePayer must be defined with feePayerSignatures.");
            }
        }
        this.feePayerSignatures.addAll(feePayerSignatures);
        this.feePayerSignatures = refineSignature(this.getFeePayerSignatures());
    }
}
