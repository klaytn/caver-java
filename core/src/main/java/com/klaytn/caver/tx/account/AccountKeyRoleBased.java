/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.tx.account;

import com.klaytn.caver.utils.BytesUtils;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AccountKeyRoleBased represents a role-based key.
 */
public class AccountKeyRoleBased implements AccountKey {

    /**
     * <p>First Key : roleTransaction<br>
     * Default key. Transactions other than TxTypeAccountUpdate should be signed by the key of this role.
     *
     * <p>Second Key : roleUpdate<br>
     * TxTypeAccountUpdate transaction should be signed by this key. If this key is not present in the account,
     * TxTypeAccountUpdate transaction is validated using RoleTransaction key.
     *
     * <p>Third Key : roleFeePayer<br>
     * If this account wants to send tx fee instead of the sender, the transaction should be signed by this key.
     * If this key is not present in the account, a fee-delegated transaction is validated using RoleTransaction key.
     */
    private List<AccountKey> accountKeys;

    protected AccountKeyRoleBased(List<AccountKey> accountKeys) {
        this.accountKeys = accountKeys;
    }

    public static AccountKeyRoleBased create(List<AccountKey> roleBasedAccountKeys) {
        return new AccountKeyRoleBased(roleBasedAccountKeys);
    }

    public AccountKey getRoleTransaction() {
        return accountKeys.get(0);
    }

    public AccountKey getRoleUpdate() {
        return accountKeys.get(1);
    }

    public AccountKey getRoleFeePayer() {
        return accountKeys.get(2);
    }

    public List<AccountKey> getAccountKeys() {
        return accountKeys;
    }

    public static AccountKeyRoleBased decodeFromRlp(byte[] rawTransaction) {
        byte[] transaction = AccountKeyDecoder.getRawTransactionNoType(rawTransaction);

        RlpList rlpList = RlpDecoder.decode(transaction);
        List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();
        List<AccountKey> accountKeys = new ArrayList<>();
        for (RlpType value : values) {
            accountKeys.add(AccountKeyDecoder.fromRlp(((RlpString) value).asString()));
        }
        return AccountKeyRoleBased.create(accountKeys);
    }

    public static AccountKeyRoleBased decodeFromRlp(String hexString) {
        return decodeFromRlp(Numeric.hexStringToByteArray(hexString));
    }

    @Override
    public byte[] toRlp() {
        List<RlpType> rlpTypeList = new ArrayList<>();
        for (AccountKey roleBasedAccountKey : accountKeys) {
            rlpTypeList.add(RlpString.create(roleBasedAccountKey.toRlp()));
        }

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = {getType().getValue()};
        return BytesUtils.concat(type, encodedTransaction);
    }

    @Override
    public Type getType() {
        return Type.ROLEBASED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKeyRoleBased that = (AccountKeyRoleBased) o;
        return Arrays.equals(toRlp(), that.toRlp());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("AccountKeyRoleBased : \n ");
        result.append("RoleTransaction : \n " + getRoleTransaction().toString() + "\n ");
        result.append("RoleUpdate : \n " + getRoleUpdate().toString() + "\n ");
        result.append("RoleFeePayer : \n " + getRoleFeePayer().toString());

        return result.toString();
    }
}
