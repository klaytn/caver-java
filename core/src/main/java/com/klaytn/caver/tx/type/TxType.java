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

package com.klaytn.caver.tx.type;

import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.crpyto.KlaySignatureData;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import org.web3j.rlp.RlpType;

import java.util.List;

/**
 * This interface
 */
public interface TxType {

    /**
     * rlp encoding for signature(SigRLP)
     *
     * @param credentials credential info of a signer
     * @param chainId     chain ID
     * @return KlaySignatureData processed signature data
     */
    KlaySignatureData getSignatureData(KlayCredentials credentials, int chainId);

    /**
     * rlp encoding for transaction hash(TxHash)
     *
     * @param credentials credential info of a signer
     * @param chainId     chain ID
     * @return KlayRawTransaction this contains transaction hash and processed signature data
     */
    KlayRawTransaction sign(KlayCredentials credentials, int chainId);

    /**
     * create RlpType List. List elements can be different depending on transaction type.
     *
     * @return List RlpType List
     */
    List<RlpType> rlpValues();

    /**
     * This method is overridden from each transaction type.
     * The return value is used for rlp encoding.
     *
     * @return Type transaction type
     */
    Type getType();

    /**
     * create rlp encoded value for signature component
     *
     * @return byte[] rlp encoded value
     */
    byte[] getEncodedTransactionNoSig();

    enum Type {
        /**
         * This represents a type of transactions existed previously in Klaytn.
         */
        LEGACY((byte) 0x00),

        /**
         * This creates an externally owned account with the given account key.
         */
        ACCOUNT_CREATION((byte) 0x18),

        /**
         * This updates the key of the account.
         */
        ACCOUNT_UPDATE((byte) 0x20),

        /**
         * This updates the key of the account. The transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_ACCOUNT_UPDATE((byte) 0x21),

        /**
         * This updates the key of the account. The given ratio of the transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO((byte) 0x22),

        /**
         * This transfers KLAY only.
         */
        VALUE_TRANSFER((byte) 0x08),

        /**
         * This transfers KLAY with a memo.
         */
        VALUE_TRANSFER_MEMO((byte) 0x10),

        /**
         * This is a value transfer transaction with a fee payer. The fee payer address can be different from the sender.
         */
        FEE_DELEGATED_VALUE_TRANSFER((byte) 0x09),

        /**
         * This is a value transfer transaction with a fee payer and its ratio.
         */
        FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO((byte) 0x0a),

        /**
         * This transfers KLAY with a data. The fee is paid by the fee payer.
         */
        FEE_DELEGATED_VALUE_TRANSFER_MEMO((byte) 0x11),

        /**
         * This transfers KLAY with a data. The given ratio of the transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO((byte) 0x12),

        /**
         * This deploys a smart contract to the given address.
         */
        SMART_CONTRACT_DEPLOY((byte) 0x28),

        /**
         * This deploys a smart contract. The fee is paid by the fee payer.
         */
        FEE_DELEGATED_SMART_CONTRACT_DEPLOY((byte) 0x29),

        /**
         * This deploys a smart contract. The given ratio of the transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO((byte) 0x2a),

        /**
         * This executes a smart contract with the given data.
         */
        SMART_CONTRACT_EXECUTION((byte) 0x30),

        /**
         * This executes a smart contract with the given data. The fee is paid by the fee payer.
         */
        FEE_DELEGATED_SMART_CONTRACT_EXECUTION((byte) 0x31),

        /**
         * This executes a smart contract with the given data. The given ratio of the transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO((byte) 0x32),

        /**
         * This cancels the transaction with the same nonce in the txpool.
         */
        CANCEL((byte) 0x38),

        /**
         * This cancels the transaction with the same nonce in the txpool. The fee is paid by the fee payer.
         */
        FEE_DELEGATED_CANCEL((byte) 0x39),

        /**
         * This cancels the transaction with the same nonce in the txpool. The given ratio of the transaction fee is paid by the fee payer.
         */
        FEE_DELEGATED_CANCEL_WITH_RATIO((byte) 0x3a),

        /**
         * This is a transaction for anchoring child chain data. This transaction is generated and submitted by a servicechain.
         * Submitting transactions of this type via RPC is prohibited.
         */
        CHAIN_DATA_ANCHORING((byte) 0x48);

        private byte value;

        Type(byte value) {
            this.value = value;
        }

        public byte get() {
            return value;
        }

        public static Type findByValue(byte value) {
            for (Type v : values()) {
                if (v.get() == value) {
                    return v;
                }
            }
            return LEGACY;
        }
    }
}
