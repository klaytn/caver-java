package com.klaytn.caver.transaction;

import com.klaytn.caver.Klay;
import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.Keyring;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
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
    List<KlaySignatureData> feePayerSignatures = new ArrayList<>();

    /**
     * Represent a AbstractFeeDelegatedTransaction builder
     * @param <B> An generic extends to AbstractFeeDelegatedTransaction.Builder
     */
    public static class Builder<B extends AbstractFeeDelegatedTransaction.Builder> extends AbstractTransaction.Builder<B> {
        String feePayer;
        private List<KlaySignatureData> signList = new ArrayList<>();

        public Builder(String type) {
            super(type);
        }

        public B setFeePayer(String feePayer) {
            this.feePayer = feePayer;
            return (B) this;
        }

        public B setFeePayerSignList(KlaySignatureData data) {
            this.signList.add(data);
            return (B) this;
        }

        public B setFeePayerSignList(List<KlaySignatureData> signList) {
            this.signList.addAll(signList);
            return (B) this;
        }
    }

    /**
     * Create AbstractFeeDelegatedTransaction instance
     * @param builder AbstractFeeDelegatedTransaction.Builder
     */
    public AbstractFeeDelegatedTransaction(Builder builder) {
        super(builder);
        this.feePayer = builder.feePayer;

        if (this.feePayerSignatures != null) {
            this.feePayerSignatures.addAll(builder.signList);
        }
    }

    /**
     * Create AbstractFeeDelegatedTransaction instance
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
    public AbstractFeeDelegatedTransaction(Klay klaytnCall, String type, String from, String nonce, String gas, String gasPrice, String chainId, List<KlaySignatureData> signatures, String feePayer, List<KlaySignatureData> feePayerSignatures) {
        super(klaytnCall, type, from, nonce, gas, gasPrice, chainId, signatures);
        this.feePayer = feePayer;
        this.feePayerSignatures = feePayerSignatures;
    }

    /**
     *
     * @param keyString
     * @return
     * @throws IOException
     */
    public AbstractFeeDelegatedTransaction signAsFeePayer(String keyString) throws IOException {
        Keyring keyring = Keyring.createFromPrivateKey(keyString);

        return signAsFeePayer(keyring, 0);
    }

    public AbstractFeeDelegatedTransaction signAsFeePayer(String keyString, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        Keyring keyring = Keyring.createFromPrivateKey(keyString);

        return signAsFeePayer(keyring, 0, TransactionHasher::getHashForSignature);
    }

    public AbstractFeeDelegatedTransaction signAsFeePayer(Keyring keyring) throws IOException {
        return signAsFeePayer(keyring, TransactionHasher::getHashForSignature);
    }

    public AbstractFeeDelegatedTransaction signAsFeePayer(Keyring keyring, int index) throws IOException {
        return signAsFeePayer(keyring, 0, TransactionHasher::getHashForSignature);
    }

    public AbstractFeeDelegatedTransaction signAsFeePayer(Keyring keyring, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(!this.getFeePayer().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The feePayer address of the transaction is different with the address of the keyring to use.");
        }

        this.fillTransaction();
        int role = AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex();
        String hash = hasher.apply(this);
        List<KlaySignatureData> sigList = keyring.signWithKeys(hash, Numeric.toBigInt(this.getChainId()).intValue(), role);

        this.appendFeePayerSignatures(sigList);

        return this;
    }

    public AbstractFeeDelegatedTransaction signAsFeePayer(Keyring keyring, int index, Function<AbstractFeeDelegatedTransaction, String> hasher) throws IOException {
        if(!this.getFeePayer().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The feePayer address of the transaction is different with the address of the keyring to use.");
        }

        this.fillTransaction();
        int role = AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex();
        String hash = hasher.apply(this);
        KlaySignatureData sigList = keyring.signWithKey(hash, Numeric.toBigInt(this.getChainId()).intValue(), role, index);

        this.appendFeePayerSignatures(sigList);

        return this;
    }

    /**
     * Appends signatures to the transaction.
     * @param signatureData KlaySignatureData instance contains ECDSA signature data
     */
    public void appendFeePayerSignatures(KlaySignatureData signatureData) {
        List<KlaySignatureData> signList = new ArrayList<>();
        signList.add(signatureData);
        appendFeePayerSignatures(signList);
    }

    /**
     * Appends signatures to the transaction.
     * @param signatureData List of KlaySignatureData contains ECDSA signature data
     */
    public void appendFeePayerSignatures(List<KlaySignatureData> signatureData) {
        this.feePayerSignatures.addAll(signatureData);
        this.feePayerSignatures = refineSignature(this.getSignatures());
    }

    public String combineSignature(List<String> rlpEncoded) {
        boolean fillVariable = false;

        // If the signatures are empty, there may be an undefined member variable.
        // In this case, the empty information is filled with the decoded result.
        boolean isContainsEmptySig = Utils.isEmptySig(this.getSignatures());
        if(this.getSignatures().size() == 0 || isContainsEmptySig) fillVariable = true;

        for(String encodedStr : rlpEncoded) {
            AbstractFeeDelegatedTransaction txObj = (AbstractFeeDelegatedTransaction) TransactionDecoder.decode(encodedStr);

            if(fillVariable) {
                if(this.getNonce().equals("0x")) this.setNonce(txObj.getNonce());
                if(this.getGasPrice().equals("0x")) this.setGasPrice(txObj.getGasPrice());
                if(this.getFeePayer().equals("0x")) {
                    if(!txObj.getFeePayer().equals("0x")) {
                        this.setFeePayer(txObj.getFeePayer());
                    }
                }
                fillVariable = false;
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

    public String getRLPEncodingForFeePayerSignature() {
        validateOptionalValues(true);

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

    public boolean compareTxField(AbstractFeeDelegatedTransaction txObj, boolean checkSig) {
        if(!super.compareTxField(txObj, checkSig)) return false;

        if(!this.getFeePayer().toLowerCase().equals(txObj.getFeePayer().toLowerCase())) return false;

        if(checkSig) {
            List<KlaySignatureData> dataList = this.getFeePayerSignatures();

            if(dataList.size() != txObj.getFeePayerSignatures().size()) return false;

            for(int i=0; i< dataList.size(); i++) {
                if(!dataList.get(i).equals(txObj.getFeePayerSignatures().get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public String getFeePayer() {
        return feePayer;
    }

    public void setFeePayer(String feePayer) {
        if(feePayer == null) {
            feePayer = "0x";
        }

        if(!feePayer.equals("0x") && !Utils.isAddress(feePayer)) {
            throw new IllegalArgumentException("Invalid address. : " + feePayer);
        }

        this.feePayer = feePayer;
    }

    public List<KlaySignatureData> getFeePayerSignatures() {
        return feePayerSignatures;
    }
}
