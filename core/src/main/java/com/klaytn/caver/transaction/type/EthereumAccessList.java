/*
 * Copyright 2022 The caver-java Authors
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

import com.klaytn.caver.account.AccountKeyRoleBased;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.TransactionDecoder;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TransactionHelper;
import com.klaytn.caver.transaction.utils.AccessList;
import com.klaytn.caver.utils.BytesUtils;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.web3j.crypto.Hash;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Represents an ethereum access list transaction.
 */
public class EthereumAccessList extends AbstractTransaction {
    /**
     * The account address that will receive the transferred value.
     */
    String to = "0x";

    /**
     * Data attached to the transaction, used for transaction execution.
     */
    String input = "0x";

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value = "0x00";

    /**
     * Access list is an EIP-2930 access list.
     */
    AccessList accessList;

    /**
     * A unit price of gas in peb the sender will pay for a transaction fee.
     */
    String gasPrice = "0x";

    /**
     * EthereumAccessList Builder class
     */
    public static class Builder extends AbstractTransaction.Builder<EthereumAccessList.Builder> {
        private String to = "0x";
        private String value = "0x0";
        private String input = "0x";
        private AccessList accessList = new AccessList();
        String gasPrice = "0x";

        public Builder() {
            super(TransactionType.TxTypeEthereumAccessList.toString());
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setValue(BigInteger value) {
            setValue(Numeric.toHexStringWithPrefix(value));
            return this;
        }

        public Builder setInput(String input) {
            this.input = input;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setAccessList(AccessList accessList) {
            this.accessList = accessList;
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

        public EthereumAccessList build() {
            return new EthereumAccessList(this);
        }
    }

    /**
     * Creates a EthereumAccessList instance.
     *
     * @param builder EthereumAccessList.Builder instance.
     * @return EthereumAccessList
     */
    public static EthereumAccessList create(EthereumAccessList.Builder builder) {
        return new EthereumAccessList(builder);
    }


    /**
     * Create a EthereumAccessList instance.
     *
     * @param klaytnCall Klay RPC instance
     * @param from       The address of the sender.
     * @param nonce      A value used to uniquely identify a sender’s transaction.
     * @param gas        The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice   A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId    Network ID
     * @param signatures A Signature list
     * @param to         The account address that will receive the transferred value.
     * @param input      Data attached to the transaction, used for transaction execution.
     * @param value      The amount of KLAY in peb to be transferred.
     * @param accessList The EIP-2930 access list.
     * @return EthereumAccessList
     */
    public static EthereumAccessList create(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String input, String value, AccessList accessList) {
        return new EthereumAccessList(klaytnCall, from, nonce, gas, gasPrice, chainId, signatures, to, input, value, accessList);
    }

    /**
     * Creates a EthereumAccessList instance.
     *
     * @param builder EthereumAccessList.Builder instance.
     */
    public EthereumAccessList(EthereumAccessList.Builder builder) {
        super(builder);

        setTo(builder.to);
        setValue(builder.value);
        setInput(builder.input);
        setAccessList(builder.accessList);
        setGasPrice(builder.gasPrice);
    }


    /**
     * Create a EthereumAccessList instance.
     *
     * @param klaytnCall Klay RPC instance
     * @param from       The address of the sender.
     * @param nonce      A value used to uniquely identify a sender’s transaction.
     * @param gas        The maximum amount of gas the transaction is allowed to use.
     * @param gasPrice   A unit price of gas in peb the sender will pay for a transaction fee.
     * @param chainId    Network ID
     * @param signatures A Signature list
     * @param to         The account address that will receive the transferred value.
     * @param input      Data attached to the transaction, used for transaction execution.
     * @param value      The amount of KLAY in peb to be transferred.
     */
    public EthereumAccessList(Klay klaytnCall, String from, String nonce, String gas, String gasPrice, String chainId, List<SignatureData> signatures, String to, String input, String value, AccessList accessList) {
        super(
                klaytnCall,
                TransactionType.TxTypeEthereumAccessList.toString(),
                from,
                nonce,
                gas,
                chainId,
                signatures
        );
        setTo(to);
        setValue(value);
        setInput(input);
        setAccessList(accessList);
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


    @Override
    public String getRLPEncoding() {
        // TransactionPayload = 0x7801 + encode([chainId, nonce, gasPrice, gas, to, value, data, accessList, signatureYParity, signatureR, signatureS])
        this.validateOptionalValues(true);

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getChainId())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(this.getAccessList().toRlpList());
        SignatureData signatureData = this.getSignatures().get(0);
        rlpTypeList.addAll(signatureData.toRlpList().getValues());

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = Numeric.toBytesPadded(BigInteger.valueOf(TransactionType.TxTypeEthereumAccessList.getType()), 2);
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    @Override
    public String getCommonRLPEncodingForSignature() {
        return getRLPEncodingForSignature();
    }

    /**
     * Returns the RLP-encoded string to make the signature of this transaction.
     *
     * @return String
     */
    @Override
    public String getRLPEncodingForSignature() {
        // SigRLP = 0x01 || rlp([chainId, nonce, gasPrice, gasLimit, to, value, data, accessList])

        this.validateOptionalValues(true);

        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getChainId())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getNonce())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGasPrice())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getGas())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getTo())));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt(this.getValue())));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray(this.getInput())));
        rlpTypeList.add(this.getAccessList().toRlpList());

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[]{(byte) TransactionType.TxTypeEthereumAccessList.getType()};
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);

        return Numeric.toHexString(rawTx);
    }

    /**
     * Check equals txObj passed parameter and Current instance.
     *
     * @param obj      The AbstractTransaction Object to compare
     * @param checkSig Check whether signatures field is equal.
     * @return boolean
     */
    @Override
    public boolean compareTxField(AbstractTransaction obj, boolean checkSig) {
        if (!super.compareTxField(obj, checkSig)) return false;
        if (!(obj instanceof EthereumAccessList)) return false;
        EthereumAccessList txObj = (EthereumAccessList) obj;

        if (!this.getTo().toLowerCase().equals(txObj.getTo().toLowerCase())) return false;
        if (!Numeric.toBigInt(this.getValue()).equals(Numeric.toBigInt(txObj.getValue()))) return false;
        if (!this.getInput().equals(txObj.getInput())) return false;
        if (!this.getAccessList().equals(txObj.getAccessList())) return false;
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
            EthereumAccessList txObj = (EthereumAccessList) decode;

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
     * Decodes a RLP-encoded EthereumAccessList string.
     *
     * @param rlpEncoded RLP-encoded EthereumAccessList string
     * @return EthereumAccessList
     */
    public static EthereumAccessList decode(String rlpEncoded) {
        return decode(Numeric.hexStringToByteArray(rlpEncoded));
    }

    /**
     * Decodes a RLP-encoded EthereumAccessList byte array.
     *
     * @param rlpEncoded RLP-encoded EthereumAccessList byte array.
     * @return EthereumAccessList
     */
    public static EthereumAccessList decode(byte[] rlpEncoded) {
        // TxHashRLP = 0x7801 + encode([chainId, nonce, gasPrice, gas, to, value, data, accessList, signatureYParity, signatureR, signatureS])
        try {
            if ((rlpEncoded[0] << 8 | rlpEncoded[1]) != TransactionType.TxTypeEthereumAccessList.getType()) {
                throw new IllegalArgumentException("Invalid RLP-encoded tag - " + TransactionType.TxTypeEthereumAccessList.toString());
            }
            byte[] detachedType = Arrays.copyOfRange(rlpEncoded, 2, rlpEncoded.length);
            RlpList rlpList = RlpDecoder.decode(detachedType);
            List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();

            BigInteger chainId = ((RlpString) values.get(0)).asPositiveBigInteger();
            BigInteger nonce = ((RlpString) values.get(1)).asPositiveBigInteger();
            BigInteger gasPrice = ((RlpString) values.get(2)).asPositiveBigInteger();
            BigInteger gas = ((RlpString) values.get(3)).asPositiveBigInteger();
            String to = ((RlpString) values.get(4)).asString();
            BigInteger value = ((RlpString) values.get(5)).asPositiveBigInteger();
            String input = ((RlpString) values.get(6)).asString();

            AccessList accessList = AccessList.decode((RlpList) values.get(7));

            EthereumAccessList ethereumAccessList = new EthereumAccessList.Builder()
                    .setFrom(null)
                    .setInput(input)
                    .setValue(value)
                    .setChainId(chainId)
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setNonce(nonce)
                    .setTo(to)
                    .setAccessList(accessList)
                    .build();

            byte[] v = ((RlpString) values.get(8)).getBytes();
            byte[] r = ((RlpString) values.get(9)).getBytes();
            byte[] s = ((RlpString) values.get(10)).getBytes();
            SignatureData signatureData = new SignatureData(v, r, s);

            ethereumAccessList.appendSignatures(signatureData);
            return ethereumAccessList;
        } catch (Exception e) {
            throw new RuntimeException("There is an error while decoding process.");
        }
    }

    /**
     * Appends signatures array to transaction.
     * EthereumAccessList transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData SignatureData instance contains ECDSA signature data
     */
    @Override
    public void appendSignatures(SignatureData signatureData) {
        if(this.getSignatures().size() != 0 && !Utils.isEmptySig(this.getSignatures().get(0))) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");
        }

        if (!Utils.isEmptySig(signatureData)) {
            int v = Integer.decode(signatureData.getV());
            if (v != 0 && v != 1) {
                throw new RuntimeException("Invalid signature: The y-parity of the transaction should either be 0 or 1.");
            }
        }
        super.appendSignatures(signatureData);
    }

    /**
     * Appends signatures array to transaction.
     * EthereumAccessList transaction cannot have more than one signature, so an error occurs if the transaction already has a signature.
     * @param signatureData List of SignatureData contains ECDSA signature data
     */
    @Override
    public void appendSignatures(List<SignatureData> signatureData) {
        if(this.getSignatures().size() != 0 && !Utils.isEmptySig(this.getSignatures())) {
            throw new RuntimeException("Signatures already defined." + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");
        }

        if(signatureData.size() != 1) {
            throw new RuntimeException("Signatures are too long " + TransactionType.TxTypeEthereumAccessList.toString() + " cannot include more than one signature.");
        }

        SignatureData signature = signatureData.get(0);
        if (!Utils.isEmptySig(signature)) {
            int v = Integer.decode(signature.getV());
            if (v != 0 && v != 1) {
                throw new RuntimeException("Invalid signature: The y-parity of the transaction should either be 0 or 1.");
            }
        }

        super.appendSignatures(signatureData);
    }

    /**
     * Returns a hash string of transaction
     * @return String
     */
    @Override
    public String getTransactionHash() {
        // TxHashRLP = 0x01 + encode([chainId, nonce, gasPrice, gas, to, value, data, accessList, signatureYParity, signatureR, signatureS])
        String rlpEncoded = this.getRLPEncoding();
        byte[] rlpEncodedBytes = Numeric.hexStringToByteArray(rlpEncoded);
        byte[] detachedType = Arrays.copyOfRange(rlpEncodedBytes, 1, rlpEncodedBytes.length);
        return Hash.sha3(Numeric.toHexString(detachedType));
    }

    /**
     * Signs to the transaction with a single private key.
     * It sets Hasher default value.
     *   - signer : TransactionHasher.getHashForSignature()
     * @param keyString The private key string.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(String keyString) throws IOException {
        AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(keyString);
        return this.sign(keyring, TransactionHasher::getHashForSignature);
    }

    /**
     * Signs using all private keys used in the role defined in the Keyring instance.
     * It sets index and Hasher default value.
     * @param keyring The Keyring instance.
     * @param signer The function to get hash of transaction.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(AbstractKeyring keyring, Function<AbstractTransaction, String> signer) throws IOException {
        if(TransactionHelper.isEthereumTransaction(this.getType()) && keyring.isDecoupled()) {
            throw new IllegalArgumentException(this.getType() + " cannot be signed with a decoupled keyring.");
        }

        if(this.getFrom().equals("0x") || this.getFrom().equals(Utils.DEFAULT_ZERO_ADDRESS) || this.getFrom().isEmpty()){
            this.setFrom(keyring.getAddress());
        }

        if(!this.getFrom().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The from address of the transaction is different with the address of the keyring to use");
        }

        this.fillTransaction();

        String hash = signer.apply(this);
        List<SignatureData> sigList = keyring.ecsign(hash, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());

        this.appendSignatures(sigList);

        return this;
    }

    /**
     * Signs using all private keys used in the role defined in the Keyring instance.
     * It sets index and Hasher default value.
     *   - signer : TransactionHasher.getHashForSignature()
     * @param keyring The Keyring instance.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(AbstractKeyring keyring) throws IOException {
        return this.sign(keyring, TransactionHasher::getHashForSignature);
    }

    /**
     * Signs to the transaction with a single private key.
     * @param keyString The private key string
     * @param signer The function to get hash of transaction.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(String keyString, Function<AbstractTransaction, String> signer) throws IOException {

        AbstractKeyring keyring = KeyringFactory.createFromPrivateKey(keyString);
        return this.sign(keyring, signer);
    }

    /**
     * Signs to the transaction with a private key in the Keyring instance.
     * It sets signer to TransactionHasher.getHashForSignature()
     * @param keyring The Keyring instance.
     * @param index The index of private key to use in Keyring instance.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(AbstractKeyring keyring, int index) throws IOException {
        return this.sign(keyring, index, TransactionHasher::getHashForSignature);
    }

    /**
     * Signs to the transaction with a private key in the Keyring instance.
     * @param keyring The Keyring instance.
     * @param index The index of private key to use in Keyring instance.
     * @param signer The function to get hash of transaction.
     * @return AbstractTransaction
     * @throws IOException
     */
    @Override
    public AbstractTransaction sign(AbstractKeyring keyring, int index, Function<AbstractTransaction, String> signer) throws IOException {
        if(TransactionHelper.isEthereumTransaction(this.getType()) && keyring.isDecoupled()) {
            throw new IllegalArgumentException(this.getType() + " cannot be signed with a decoupled keyring.");
        }

        if(this.getFrom().equals("0x") || this.getFrom().equals(Utils.DEFAULT_ZERO_ADDRESS) || this.getFrom().isEmpty()){
            this.setFrom(keyring.getAddress());
        }

        if(!this.getFrom().toLowerCase().equals(keyring.getAddress().toLowerCase())) {
            throw new IllegalArgumentException("The from address of the transaction is different with the address of the keyring to use");
        }

        this.fillTransaction();

        String hash = signer.apply(this);
        SignatureData signatureData = keyring.ecsign(hash, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex(), index);

        this.appendSignatures(signatureData);

        return this;
    }

    /**
     * Getter function for to
     *
     * @return String
     */
    public String getTo() {
        return to;
    }

    /**
     * Setter function for to
     *
     * @param to The account address that will receive the transferred value.
     */
    public void setTo(String to) {
        // "to" field in EthereumAccessList allows null
        if (to == null || to.isEmpty()) {
            to = "0x";
        }

        if (!to.equals("0x") && !Utils.isAddress(to)) {
            throw new IllegalArgumentException("Invalid address. : " + to);
        }
        this.to = to;
    }

    /**
     * Getter function for input
     *
     * @return String
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter function for input
     *
     * @param input Data attached to the transaction.
     */
    public void setInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input is missing");
        }

        if (!Utils.isHex(input)) {
            throw new IllegalArgumentException("Invalid input : " + input);
        }
        this.input = Numeric.prependHexPrefix(input);
    }


    /**
     * Getter function for value
     *
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter function for value
     *
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value is missing");
        }

        if (!Utils.isNumber(value)) {
            throw new IllegalArgumentException("Invalid value : " + value);
        }
        this.value = value;
    }

    /**
     * Setter function for value
     *
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        setValue(Numeric.toHexStringWithPrefix(value));
    }

    /**
     * Getter function for accessList
     *
     * @return accessList Access list is an EIP-2930 access list.
     */
    public AccessList getAccessList() {
        return accessList;
    }

    /**
     * Setter function for accessList
     *
     * @param accessList Access list is an EIP-2930 access list.
     */
    public void setAccessList(AccessList accessList) {
        if (accessList == null) {
            accessList = new AccessList();
        }
        this.accessList = accessList;
    }
}