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

package com.klaytn.caver.transaction;

import com.klaytn.caver.methods.response.Transaction;
import com.klaytn.caver.rpc.Klay;

import java.io.IOException;
import java.util.List;

/**
 * This class is a helper class provides methods that handles Transaction object comfortably.
 */
public class TransactionHelper {

    /**
     * Querys transaction from Klaytn and converts to a caver transaction instance.
     * @see com.klaytn.caver.transaction.wrapper.TransactionWrapper#getTransactionByHash(String) 
     * <pre>Example :
     * {@code
     * AbstractTransaction tx = caver.transaction.getTransactionByHash("0x{txHash}");
     * }
     * </pre>
     * @param klay The Klay instance.
     * @param transactionHash The transaction hash string to query from Klaytn.
     * @return AbstractTransaction
     */
    public static AbstractTransaction getTransactionByHash(Klay klay, String transactionHash) {
        try {
            Transaction response = klay.getTransactionByHash(transactionHash).send();
            if(response.hasError() || response.getResult() == null) {
                throw new RuntimeException("Failed to get transaction from Klaytn with " + transactionHash);
            }

            Transaction.TransactionData txObject = response.getResult();
            return txObject.convertToCaverTransaction(klay);
        } catch(IOException e) {
            throw new RuntimeException("Failed to get transaction from Klaytn with" + transactionHash, e);
        }
    }

    /**
     * Recovers the public key from "signature" filed in raw transaction string.<p>
     * If you want to derive an address from public key, please use {@link com.klaytn.caver.utils.Utils#publicKeyToAddress(String)}
     * <pre>Example :
     * {@code
     * List<String> publicKeys = caver.transaction.recoverPublicKeys("0x{RLP-encoded transaction string}");
     * }
     * </pre>
     * @param rawTx The RLP-encoded transaction string to recover public keys from "signatures".
     * @return List&lt;String&gt;
     */
    public static List<String> recoverPublicKeys(String rawTx) {
        AbstractTransaction tx = TransactionDecoder.decode(rawTx);
        return tx.recoverPublicKeys();
    }

    /**
     * Recovers the public key from "feePayerSignature" filed in raw transaction string.<p>
     * If you want to derive an address from public key, please use {@link com.klaytn.caver.utils.Utils#publicKeyToAddress(String)}
     * <pre>Example :
     * {@code
     * List<String> publicKeys = caver.transaction.recoverFeePayerPublicKeys("0x{RLP-encoded transaction string}");
     * }
     * </pre>
     * @param rawTx The RLP-encoded transaction string to recover public keys from "signatures".
     * @return List&lt;String&gt;
     */
    public static List<String> recoverFeePayerPublicKeys(String rawTx) {
        AbstractFeeDelegatedTransaction tx = (AbstractFeeDelegatedTransaction)TransactionDecoder.decode(rawTx);
        return tx.recoverFeePayerPublicKeys();
    }
}
