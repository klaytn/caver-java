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
 * This file is derived from web3j/core/src/main/java/org/web3j/crypto/RawTransaction.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * @deprecated This class is deprecated since caver-java:1.5.0
 */
@Deprecated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KlayTransaction {

    /**
     * The address from which the transaction is sent.
     */
    private String from;

    /**
     * The address to which the transaction is directed.
     */
    private String to;

    /**
     * Integer of the gas provided for the transaction execution.
     */
    private BigInteger gas;

    /**
     * Integer of the gasPrice used for each paid gas.
     */
    private BigInteger gasPrice;

    /**
     * Integer of the value sent with this transaction.
     */
    private BigInteger value;

    /**
     * The compiled code of a contract or the hash of the invoked method signature and encoded parameters.
     */
    private String data;

    /**
     * Integer of a nonce. This allows to overwrite your own pending transactions that use the same nonce.
     */
    private BigInteger nonce;

    public KlayTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                           String to, BigInteger value, String data) {
        this.from = from;
        this.to = to;
        this.gas = gasLimit;
        this.gasPrice = gasPrice;
        this.value = value;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }

        this.nonce = nonce;
    }

    public static KlayTransaction createKlayTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value) {

        return new KlayTransaction(from, nonce, gasPrice, gasLimit, to, value, null);
    }


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getValue() {
        return convert(value);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return convert(nonce);
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
