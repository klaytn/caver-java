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

import java.util.*;

import static com.klaytn.caver.wallet.KlayWalletUtils.*;

public class KlayCredentials {
    private final List<ECKeyPair> ecKeyPairForTransactionList;
    private final List<ECKeyPair> ecKeyPairForUpdateList;
    private final List<ECKeyPair> ecKeyPairForFeeFeePayerList;
    private final String address;

    private KlayCredentials(ECKeyPair ecKeyPair, String address) {
        this.ecKeyPairForTransactionList = Collections.unmodifiableList(Arrays.asList(ecKeyPair));
        this.ecKeyPairForUpdateList = null;
        this.ecKeyPairForFeeFeePayerList = null;
        this.address = address;
    }

    private KlayCredentials(List<ECKeyPair> ecKeyPairForTransactionArray, List<ECKeyPair> ecKeyPairForUpdateArray, List<ECKeyPair> ecKeyPairForFeeArray, String address) {
        this.ecKeyPairForTransactionList = (ecKeyPairForTransactionArray != null && ecKeyPairForTransactionArray.size() != 0) ? Collections.unmodifiableList(ecKeyPairForTransactionArray) : null;
        this.ecKeyPairForUpdateList = (ecKeyPairForUpdateArray != null && ecKeyPairForUpdateArray.size() != 0) ? Collections.unmodifiableList(ecKeyPairForUpdateArray) : null;
        this.ecKeyPairForFeeFeePayerList = (ecKeyPairForFeeArray != null && ecKeyPairForFeeArray.size() != 0) ? Collections.unmodifiableList(ecKeyPairForFeeArray) : null;
        this.address = address;
    }

    public ECKeyPair getEcKeyPair() {
        return ecKeyPairForTransactionList.get(0);
    }

    public List<ECKeyPair> getEcKeyPairsForTransactionList() throws NullPointerException {
        if (ecKeyPairForTransactionList != null) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Transaction key does not exist.");
    }

    public List<ECKeyPair> getEcKeyPairsForUpdateList() throws NullPointerException {
        if (ecKeyPairForUpdateList != null) {
            return ecKeyPairForUpdateList;
        } else if (ecKeyPairForTransactionList != null) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Update key does not exist.");
    }

    public List<ECKeyPair> getEcKeyPairsForFeePayerList() throws NullPointerException {
        if (ecKeyPairForFeeFeePayerList != null) {
            return ecKeyPairForFeeFeePayerList;
        } else if (ecKeyPairForTransactionList != null) {
            return ecKeyPairForTransactionList;
        }
        throw new RuntimeException("Fee key does not exist.");
    }

    public String getAddress() {
        return address;
    }

    public static KlayCredentials create(String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    public static KlayCredentials create(ECKeyPair ecKeyPair) {
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return create(ecKeyPair, address);
    }

    public static KlayCredentials create(String privateKey, String address) {
        return create(ECKeyPair.create(Numeric.toBigInt(privateKey)), address);
    }

    public static KlayCredentials create(ECKeyPair ecKeyPair, String address) {
        return new KlayCredentials(ecKeyPair, address);
    }

    public static KlayCredentials create(List<ECKeyPair> ecKeyPair, String address) {
        return new KlayCredentials(ecKeyPair, null, null, address);
    }

    public static KlayCredentials create(List<ECKeyPair> ecKeyPairForTransaction, List<ECKeyPair> ecKeyPairForUpdate, List<ECKeyPair> ecKeyPairForFeePayer, String address) {
        return new KlayCredentials(ecKeyPairForTransaction, ecKeyPairForUpdate, ecKeyPairForFeePayer, address);
    }

    public static KlayCredentials create(ECKeyPair[] ecKeyPairsArrayForTransaction, ECKeyPair[] ecKeyPairsArrayForUpdate, ECKeyPair[] ecKeyPairsArrayForFee, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>(Arrays.asList(ecKeyPairsArrayForTransaction));
        List<ECKeyPair> ecKeyPairsForUpdate = new ArrayList<>(Arrays.asList(ecKeyPairsArrayForUpdate));
        List<ECKeyPair> ecKeyPairsForForFee = new ArrayList<>(Arrays.asList(ecKeyPairsArrayForFee));

        return create(ecKeyPairsForTransaction, ecKeyPairsForUpdate, ecKeyPairsForForFee, address);
    }

    public static KlayCredentials create(String[] privateKeysForTransaction, String[] privateKeysForUpdate, String[] privateKeysForFee, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForUpdate = new ArrayList<>();
        List<ECKeyPair> ecKeyPairsForForFee = new ArrayList<>();

        for (String privateKey : privateKeysForTransaction) {
            ecKeyPairsForTransaction.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        for (String privateKey : privateKeysForUpdate) {
            ecKeyPairsForUpdate.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        for (String privateKey : privateKeysForFee) {
            ecKeyPairsForForFee.add(ECKeyPair.create(Numeric.toBigInt(privateKey)));
        }

        return create(ecKeyPairsForTransaction, ecKeyPairsForUpdate, ecKeyPairsForForFee, address);
    }

    public static KlayCredentials create(String[] privateKeysForTransaction, String address) {
        List<ECKeyPair> ecKeyPairsForTransaction = new ArrayList<>();

        for (String privateKey : privateKeysForTransaction) {
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
        return Numeric.toHexStringWithPrefixZeroPadded(getEcKeyPair().getPrivateKey(), 64)
                + CHECKSUM
                + getAddress();
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
        int transactionHashCode = ecKeyPairForTransactionList != null ? ecKeyPairForTransactionList.hashCode() : 0;
        int updateHashCode = ecKeyPairForUpdateList != null ? ecKeyPairForUpdateList.hashCode() : 0;
        int feeHashCode = ecKeyPairForUpdateList != null ? ecKeyPairForUpdateList.hashCode() : 0;
        int addressHashCode = address != null ? address.hashCode() : 0;

        int[] arrHashCode = {feeHashCode, updateHashCode, addressHashCode, transactionHashCode};
        int result = 0;
        for (int hashCode : arrHashCode) {
            result = result * 31 + hashCode;
        }

        return result;
    }
}