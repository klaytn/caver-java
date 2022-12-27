/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.validator;

import com.klaytn.caver.account.*;
import com.klaytn.caver.methods.response.AccountKey;
import com.klaytn.caver.rpc.Klay;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.SignatureData;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Validator {
    /**
     * The Klay instance to call Klay RPC API.
     */
    Klay klay;

    /**
     * Create a Validator instance.
     * @param klay The Klay instance.
     */
    public Validator(Klay klay) {
        this.klay = klay;
    }

    /**
     * Validate a signed message.<p>
     * This function will compare public key in account key information from Klaytn and public key recovered form signature.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String message = "Some data";
     * SignatureData sigData = new SignatureData("0x{v data}", "0x{r data}", "0x{s data}");
     *
     * boolean isValid = caver.validator.validateSignedMessage(message, sigData, address);
     * }
     * </pre>
     *
     * @param message The raw message string. If you want to pass data that already hashed with Klaytn specific prefix, see {@link Validator#validateSignedMessage(String, SignatureData, String, boolean)}
     * @param signature The {@link SignatureData} instance to validate signature data.
     * @param address The address of the account that signed the message.
     * @return boolean
     */
    public boolean validateSignedMessage(String message, SignatureData signature, String address) {
        return validateSignedMessage(message, signature, address, false);
    }

    /**
     * Validate a signed message.<p>
     * This function will compare public key in account key information from Klaytn and public key recovered form signature.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String message = "Some data";
     * SignatureData sigData = new SignatureData("0x{v data}", "0x{r data}", "0x{s data}");
     *
     * boolean isValid = caver.validator.validateSignedMessage(message, sigData, address);
     * }
     * </pre>
     *
     * @param message The raw message string. If you want to pass data that already hashed with Klaytn specific prefix, the third parameter should be passed as true.
     * @param signature The {@link SignatureData} instance to validate signature data.
     * @param address The address of the account that signed the message.
     * @param isHashed If the `isHashed` is true, the given message will NOT automatically be prefixed with "\x19Klaytn Signed Message:\n" + message.length + message, and be assumed as already prefixed.
     * @return boolean
     */
    public boolean validateSignedMessage(String message, SignatureData signature, String address, boolean isHashed) {
        return validateSignedMessage(message, Collections.singletonList(signature), address, isHashed);
    }

    /**
     * Validate a signed message.<p>
     * This function will compare public key in account key information from Klaytn and public key recovered form signature.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String message = "Some data";
     * List<SignatureData> sigDataList = Arrays.asList(new SignatureData("0x{v data}", "0x{r data}", "0x{s data}"), .....);
     *
     * boolean isValid = caver.validator.validateSignedMessage(message, sigDataList, address);
     * }
     * </pre>
     *
     * @param message The raw message string. If you want to pass data that already hashed with Klaytn specific prefix, see {@link Validator#validateSignedMessage(String, List, String, boolean)}
     * @param signatures The list of  {@link SignatureData} instance to validate signatures.
     * @param address The address of the account that signed the message.
     * @return boolean
     */
    public boolean validateSignedMessage(String message, List<SignatureData> signatures, String address) {
        return validateSignedMessage(message, signatures, address, false);
    }

    /**
     * Validate a signed message.<p>
     * This function will compare public key in account key information from Klaytn and public key recovered form signature.
     * <pre>Example :
     * {@code
     * String address = "0x{address}";
     * String message = "Some data";
     * List<SignatureData> sigDataList = Arrays.asList(new SignatureData("0x{v data}", "0x{r data}", "0x{s data}"), .....);
     *
     * boolean isValid = caver.validator.validateSignedMessage(message, sigDataList, address);
     * }
     * </pre>
     *
     * @param message The raw message string. If you want to pass data that already hashed with Klaytn specific prefix, see {@link Validator#validateSignedMessage(String, List, String, boolean)}
     * @param signatures The list of  {@link SignatureData} instance to validate signatures.
     * @param address The address of the account that signed the message.
     * @param isHashed If the `isHashed` is true, the given message will NOT automatically be prefixed with "\x19Klaytn Signed Message:\n" + message.length + message, and be assumed as already prefixed.
     * @return boolean
     */
    public boolean validateSignedMessage(String message, List<SignatureData> signatures, String address, boolean isHashed) {
        try {
            //Loading Account Key.
            AccountKey accountKey = klay.getAccountKey(address).send();
            if(accountKey.hasError()) {
                throw new RuntimeException("error code : " + accountKey.getError().getCode() + " error message : " + accountKey.getError().getMessage());
            }

            signatures = SignatureData.refineSignature(signatures);

            //Extract public keys from signatures.
            List<String> pubKeys = new ArrayList<>();
            for(SignatureData signature : signatures) {
                pubKeys.add(Utils.recoverPublicKey(message, signature, isHashed));
            }

            // For accounts that have not yet been applied in Klaytn's state,
            // the return value of `caver.rpc.klay.getAccountKey` is null.
            // In this case, the account's key has never been updated,
            // so the logic is the same as in AccountKeyLegacy.
            AccountKey.AccountKeyData acctKeyData = accountKey.getResult();
            IAccountKey acctKey;
            if (acctKeyData == null) {
                acctKey = new AccountKeyLegacy();
            } else {
                acctKey = acctKeyData.getAccountKey();
            }
            //Compare a account key from queried and public keys extracting from signature.
            return validateWithAccountType(address, acctKey, pubKeys, AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex());
        } catch(IOException e) {
            throw new RuntimeException("Failed to get AccountKey from Klaytn", e);
        } catch(SignatureException e) {
            throw new RuntimeException("Failed to recover signature", e);
        }
    }

    /**
     * Validates the sender of the transaction.<p>
     * This function compares the public keys of the account key of sender with the public keys recovered from signatures field.
     * <pre>Example :
     * {@code
     * ValueTransfer tx = caver.transaction.valueTransfer.create(...);
     * boolean isValid = caver.validator.validateSender(tx);
     * }
     * </pre>
     *
     * @param tx An instance of transaction to validate
     * @return boolean
     */
    public boolean validateSender(AbstractTransaction tx) {
        try {
            //Loading Account Key.
            AccountKey accountKey = klay.getAccountKey(tx.getFrom()).send();
            if(accountKey.hasError()) {
                throw new RuntimeException("error code : " + accountKey.getError().getCode() + " error message : " + accountKey.getError().getMessage());
            }

            List<String> publicKeys = tx.recoverPublicKeys();
            int role = tx.getType().contains("AccountUpdate") ? AccountKeyRoleBased.RoleGroup.ACCOUNT_UPDATE.getIndex() : AccountKeyRoleBased.RoleGroup.TRANSACTION.getIndex();

            return validateWithAccountType(tx.getFrom(), accountKey.getResult().getAccountKey(), publicKeys, role);
        } catch(IOException e) {
            throw new RuntimeException("Failed to get AccountKey from Klaytn", e);
        }
    }

    /**
     * Validates the fee payer in the transaction.<p>
     * This function compares the public keys of the account key of the fee payer with the public keys recovered from feePayerSignatures field.
     * <pre>Example :
     * {@code
     * ValueTransfer tx = caver.transaction.valueTransfer.create(...);
     * boolean isValid = caver.validator.validateSender(tx);
     * }
     * </pre>
     *
     * @param tx An instance of transaction to validate
     * @return boolean
     */
    public boolean validateFeePayer(AbstractFeeDelegatedTransaction tx) {
        try {
            //Loading Account Key.
            AccountKey accountKey = klay.getAccountKey(tx.getFeePayer()).send();
            if(accountKey.hasError()) {
                throw new RuntimeException("error code : " + accountKey.getError().getCode() + " error message : " + accountKey.getError().getMessage());
            }

            List<String> publicKeys = tx.recoverFeePayerPublicKeys();
            int role = AccountKeyRoleBased.RoleGroup.FEE_PAYER.getIndex();

            return validateWithAccountType(tx.getFeePayer(), accountKey.getResult().getAccountKey(), publicKeys, role);
        } catch(IOException e) {
            throw new RuntimeException("Failed to get AccountKey from Klaytn", e);
        }
    }

    /**
     * Validates a transaction.<p>
     * This function compares the public keys of the account key of sender with the public keys recovered from signatures field.<p>
     * If the transaction is fee-delegated with the `feePayerSignatures` variable inside, this function compares the public keys recovered from `feePayerSignatures` with the public keys of the fee payer.
     * <pre>Example
     * {@code
     * ValueTransfer tx = caver.transaction.valueTransfer.create(...);
     * boolean isValid = caver.validator.validateSender(tx);
     * }
     * </pre>
     *
     * @param tx An instance of transaction to validate.
     * @return boolean
     */
    public boolean validateTransaction(AbstractTransaction tx) {
        boolean isValid = this.validateSender(tx);

        if(isValid && tx instanceof AbstractFeeDelegatedTransaction) {
            isValid = this.validateFeePayer((AbstractFeeDelegatedTransaction)tx);
        }

        return isValid;
    }

    private boolean validateWithAccountType(String address, IAccountKey accountKey, List<String> pubKeys, int role) {
        // For accounts that have not yet been applied in Klaytn's state, the return value of `caver.rpc.klay.getAccountKey` is null.
        // In this case, the account's key has never been updated, so the logic is the same as in AccountKeyLegacy.
        if(accountKey == null) {
            accountKey = new AccountKeyLegacy();
        }

        Account account = new Account(address, accountKey);

        if(accountKey instanceof AccountKeyLegacy) {
            return validateAccountKeyLegacy(account, pubKeys);
        } else if(accountKey instanceof AccountKeyPublic) {
            return validateAccountKeyPublic(account, pubKeys);
        } else if(accountKey instanceof AccountKeyWeightedMultiSig) {
            return validateAccountKeyWeightedMultiSig(account, pubKeys);
        } else if(accountKey instanceof AccountKeyRoleBased) {
            return validateAccountKeyRoleBased(account, pubKeys, role);
        } else if(accountKey instanceof AccountKeyFail || accountKey instanceof AccountKeyNil){
            return false;
        } else {
            throw new IllegalArgumentException("Invalid account key type");
        }
    }

    private boolean validateAccountKeyLegacy(Account account, List<String> publicKeys) {
        boolean isValid = false;
        for(String publicKey : publicKeys) {
            if(account.getAddress().equals(Utils.publicKeyToAddress(publicKey))) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    private boolean validateAccountKeyPublic(Account account, List<String> publicKeys) {
        boolean isValid = false;

        AccountKeyPublic accountKeyPublic = (AccountKeyPublic)account.getAccountKey();
        for(String publicKey : publicKeys) {
            if(accountKeyPublic.getPublicKey().equals(publicKey)) {
                isValid = true;
            }
        }

        return isValid;
    }

    private boolean validateAccountKeyWeightedMultiSig(Account account, List<String> publicKeys) {
        AccountKeyWeightedMultiSig accountKey = (AccountKeyWeightedMultiSig)account.getAccountKey();

        // TODO: If an invalid signature is included, it should be changed to return false.
        BigInteger sumOfWeight = BigInteger.ZERO;
        for(String pubKey : publicKeys) {
            for(WeightedPublicKey weightedPublicKey : accountKey.getWeightedPublicKeys()) {
                if(pubKey.equals(weightedPublicKey.getPublicKey())) {
                    sumOfWeight = sumOfWeight.add(weightedPublicKey.getWeight());
                    break;
                }
            }
        }

        return sumOfWeight.compareTo(accountKey.getThreshold()) >= 0;
    }

    private boolean validateAccountKeyRoleBased(Account account, List<String> publicKeys, int role) {
        AccountKeyRoleBased accountKey = (AccountKeyRoleBased)account.getAccountKey();
        IAccountKey roleKey = accountKey.getAccountKeys().get(role);

        if(roleKey instanceof AccountKeyRoleBased) {
            throw new IllegalArgumentException("Invalid account key type: nested composite type");
        }

        Account roleAccount = new Account(account.getAddress(), roleKey);

        // TODO: If an invalid signature is included, it should be changed to return false.
        if(roleKey instanceof AccountKeyLegacy) {
            return validateAccountKeyLegacy(roleAccount, publicKeys);
        } else if(roleKey instanceof AccountKeyPublic) {
            return validateAccountKeyPublic(roleAccount, publicKeys);
        } else if(roleKey instanceof AccountKeyWeightedMultiSig) {
            return validateAccountKeyWeightedMultiSig(roleAccount, publicKeys);
        } else if(roleKey instanceof AccountKeyFail || roleKey instanceof AccountKeyNil){
            return false;
        } else {
            throw new IllegalArgumentException("Invalid account key type");
        }
    }
}
