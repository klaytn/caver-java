/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/Credential.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.crypto;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import java.util.*;

import static com.klaytn.caver.wallet.KlayWalletUtils.*;

/**
 * @deprecated Please use KeyringClass instead.
 * @see com.klaytn.caver.wallet.keyring.AbstractKeyring
 */
@Deprecated
public class KlayCredentials {
    private final List<ECKeyPair> ecKeyPairForTransactionList;
    private final List<ECKeyPair> ecKeyPairForUpdateList;
    private final List<ECKeyPair> ecKeyPairForFeeFeePayerList;
    private final String address;

    private KlayCredentials(ECKeyPair ecKeyPair, String address) {
        this.ecKeyPairForTransactionList = Collections.unmodifiableList(Arrays.asList(ecKeyPair));
        this.ecKeyPairForUpdateList = Collections.unmodifiableList(Collections.emptyList());
        this.ecKeyPairForFeeFeePayerList = Collections.unmodifiableList(Collections.emptyList());
        this.address = !Strings.isEmpty(address) ? Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(address), 40) : "";
    }

    private KlayCredentials(List<ECKeyPair> ecKeyPairForTransaction, List<ECKeyPair> ecKeyPairForUpdate, List<ECKeyPair> ecKeyPairForFeePayer, String address) {
        this.ecKeyPairForTransactionList = (ecKeyPairForTransaction != null && ecKeyPairForTransaction.size() != 0) ? Collections.unmodifiableList(ecKeyPairForTransaction) : Collections.unmodifiableList(Collections.emptyList());;
        this.ecKeyPairForUpdateList = (ecKeyPairForUpdate != null && ecKeyPairForUpdate.size() != 0) ? Collections.unmodifiableList(ecKeyPairForUpdate) : Collections.unmodifiableList(Collections.emptyList());
        this.ecKeyPairForFeeFeePayerList = (ecKeyPairForFeePayer != null && ecKeyPairForFeePayer.size() != 0) ? Collections.unmodifiableList(ecKeyPairForFeePayer) : Collections.unmodifiableList(Collections.emptyList());
        this.address = !Strings.isEmpty(address) ? Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(address), 40) : "";
    }

    @Deprecated
    public ECKeyPair getEcKeyPair() {
        if (ecKeyPairForTransactionList.size() > 0) {
            return ecKeyPairForTransactionList.get(0);
        }
        throw new RuntimeException("Transaction key does not exist.");
    }

    /**
     * Returns keys for transaction signing
     *
     * @return List ECKeyPair List for transaction Signing
     * @throws RuntimeException If there is no key for Transaction signing
     */
    public List<ECKeyPair> getEcKeyPairsForTransactionList() throws RuntimeException {
        if (ecKeyPairForTransactionList.size() > 0) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Transaction key does not exist.");
    }

    /**
     * Returns keys for update signing
     *
     * @return List ECKeyPair List for update Signing
     * @throws RuntimeException If there is no key for update signing
     */
    public List<ECKeyPair> getEcKeyPairsForUpdateList() throws RuntimeException {
        if (ecKeyPairForUpdateList.size() > 0) {
            return ecKeyPairForUpdateList;
        } else if (ecKeyPairForTransactionList.size() > 0) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Update key does not exist.");
    }

    /**
     * Returns keys for fee payer signing
     *
     * @return List ECKeyPair List for fee payer Signing
     * @throws RuntimeException If there is no key for fee payer signing
     */
    public List<ECKeyPair> getEcKeyPairsForFeePayerList() throws RuntimeException {
        if (ecKeyPairForFeeFeePayerList.size() > 0) {
            return ecKeyPairForFeeFeePayerList;
        } else if (ecKeyPairForTransactionList.size() > 0) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Fee key does not exist.");
    }

    /**
     * Returns raw keys. getEcKeyPairsFor(Role)List returns the keys that play the role.
     * but getRawEcKeyPair returns the key pair as it is, regardless of role.
     *
     * @return List ECKeyPair List
     */
    public List<List<ECKeyPair>> getRawEcKeyPairs() {
        List ecKeyPairs = new ArrayList();
        ecKeyPairs.add(ecKeyPairForTransactionList);
        ecKeyPairs.add(ecKeyPairForUpdateList);
        ecKeyPairs.add(ecKeyPairForFeeFeePayerList);
        return ecKeyPairs;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Static method for creating KlayCredentials instance
     * Use address extracted from private key
     *
     * @param privateKey private key for transaction signing
     * @return KlayCredentials
     */
    public static KlayCredentials create(String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     * Use address extracted from private key
     *
     * @param ecKeyPair ecKeyPair for transaction signing
     * @return KlayCredentials
     */
    public static KlayCredentials create(ECKeyPair ecKeyPair) {
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param privateKey private key for transaction signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(String privateKey, String address) {
        return create(ECKeyPair.create(Numeric.toBigInt(privateKey)), Numeric.prependHexPrefix(address));
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param ecKeyPair ecKeyPair for transaction signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(ECKeyPair ecKeyPair, String address) {
        return new KlayCredentials(ecKeyPair, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param ecKeyPair ecKeyPair for transaction signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(List<ECKeyPair> ecKeyPair, String address) {
        return new KlayCredentials(ecKeyPair, null, null, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param ecKeyPairForTransaction ecKeyPair list for transaction signing
     * @param ecKeyPairForUpdate ecKeyPair list for update signing
     * @param ecKeyPairForFeePayer ecKeyPair list for fee payer signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(List<ECKeyPair> ecKeyPairForTransaction, List<ECKeyPair> ecKeyPairForUpdate, List<ECKeyPair> ecKeyPairForFeePayer, String address) {
        return new KlayCredentials(ecKeyPairForTransaction, ecKeyPairForUpdate, ecKeyPairForFeePayer, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param ecKeyPairsArrayForTransaction ecKeyPair array for transaction signing
     * @param ecKeyPairsArrayForUpdate ecKeyPair array for update signing
     * @param ecKeyPairArrayForFeePayer ecKeyPair array for fee payer signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(ECKeyPair[] ecKeyPairsArrayForTransaction, ECKeyPair[] ecKeyPairsArrayForUpdate, ECKeyPair[] ecKeyPairArrayForFeePayer, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = Arrays.asList(ecKeyPairsArrayForTransaction);
        List<ECKeyPair> ecKeyPairsForUpdate = Arrays.asList(ecKeyPairsArrayForUpdate);
        List<ECKeyPair> ecKeyPairsForForFeePayer = Arrays.asList(ecKeyPairArrayForFeePayer);

        return create(ecKeyPairsForTransaction, ecKeyPairsForUpdate, ecKeyPairsForForFeePayer, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param privateKeyArrayForTransaction private key array for transaction signing
     * @param privateKeyArrayForUpdate private key array for transaction signing
     * @param privateKeyArrayForFeePayer private key array for transaction signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(String[] privateKeyArrayForTransaction, String[] privateKeyArrayForUpdate, String[] privateKeyArrayForFeePayer, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForUpdate = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForForFeePayer = new ArrayList<>();

        for (String privateKey : privateKeyArrayForTransaction) {
            ecKeyPairsForTransaction.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        for (String privateKey : privateKeyArrayForUpdate) {
            ecKeyPairsForUpdate.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        for (String privateKey : privateKeyArrayForFeePayer) {
            ecKeyPairsForForFeePayer.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        return create(ecKeyPairsForTransaction, ecKeyPairsForUpdate, ecKeyPairsForForFeePayer, address);
    }

    /**
     * Static method for creating KlayCredentials instance
     *
     * @param ecKeyPairListForRoleBased ecKeyPairList for role-base signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials createRoleBased(List<List<ECKeyPair>> ecKeyPairListForRoleBased, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForUpdate = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForForFeePayer = new ArrayList<>();

        Iterator<List<ECKeyPair>> iterator = ecKeyPairListForRoleBased.iterator();
        if (iterator.hasNext()) {
            for (ECKeyPair ecKeyPair : iterator.next()) {
                ecKeyPairsForTransaction.add(ecKeyPair);
            }
        }
        if (iterator.hasNext()) {
            for (ECKeyPair ecKeyPair : iterator.next()) {
                ecKeyPairsForUpdate.add(ecKeyPair);
            }
        }
        if (iterator.hasNext()) {
            for (ECKeyPair ecKeyPair : iterator.next()) {
                ecKeyPairsForForFeePayer.add(ecKeyPair);
            }
        }

        return create(ecKeyPairsForTransaction, ecKeyPairsForUpdate, ecKeyPairsForForFeePayer, address);
    }
    /**
     * Static method for creating KlayCredentials instance
     *
     * @param privateKeyArrayForTransaction private key array for transaction signing
     * @param address address of account
     * @return KlayCredentials
     */
    public static KlayCredentials create(String[] privateKeyArrayForTransaction, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>();

        for (String privateKey : privateKeyArrayForTransaction) {
            ecKeyPairsForTransaction.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        return create(ecKeyPairsForTransaction, null, null, address);
    }

    public static KlayCredentials createWithKlaytnWalletKey(String klaytnWalletKey) {
        klaytnWalletKey = Numeric.cleanHexPrefix(klaytnWalletKey);
        String privateKey = klaytnWalletKey.substring(0, 64);
        String address = klaytnWalletKey.substring(68);
        return create(privateKey, address);
    }

    public String getKlaytnWalletKey() {
        if (!canExportKlaytnWalletKey()) {
            throw new RuntimeException("The account cannot be exported in KlaytnWalletKey format. Use the WalletFile.createFull(String password, KlayCredentials klayCredentials) or WalletFile.createStandard(String password, KlayCredentials klayCredentials)");
        }

        return Numeric.toHexStringWithPrefixZeroPadded(getEcKeyPair().getPrivateKey(), 64)
                + CHECKSUM
                + getAddress();
    }

    private boolean canExportKlaytnWalletKey() {
        return ecKeyPairForTransactionList.size() == 1 && ecKeyPairForUpdateList.size() == 0 && ecKeyPairForFeeFeePayerList.size() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KlayCredentials that = (KlayCredentials) o;

        if (!Objects.equals(ecKeyPairForTransactionList, that.ecKeyPairForTransactionList)) {
            return false;
        }

        if (!Objects.equals(ecKeyPairForUpdateList, that.ecKeyPairForUpdateList)) {
            return false;
        }

        if (!Objects.equals(ecKeyPairForFeeFeePayerList, that.ecKeyPairForFeeFeePayerList)) {
            return false;
        }

        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        int transactionHashCode = ecKeyPairForTransactionList.hashCode();
        int updateHashCode = ecKeyPairForUpdateList.hashCode();
        int feeHashCode = ecKeyPairForUpdateList.hashCode();
        int addressHashCode = address.hashCode();

        int[] arrHashCode = {transactionHashCode, updateHashCode, feeHashCode,addressHashCode};
        int result = 0;
        for (int hashCode : arrHashCode) {
            result = result * 31 + hashCode;
        }

        return result;
    }
}
