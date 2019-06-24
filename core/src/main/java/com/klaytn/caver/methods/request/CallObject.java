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

package com.klaytn.caver.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.klaytn.caver.Klay;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallObject {

    /**
     * The address the transaction is sent from.
     */
    private String from;

    /**
     * The address the transaction is directed to.
     */
    private String to;

    /**
     * Integer of the gas provided for the transaction execution.
     * {@link Klay#call(CallObject, DefaultBlockParameter)} consumes zero gas,
     * but this parameter may be needed by some executions.
     */
    private BigInteger gasLimit;

    /**
     * Integer of the gasPrice used for each paid gas.
     */
    private BigInteger gasPrice;

    /**
     * Integer of the value sent with this transaction.
     */
    private BigInteger value;

    /**
     * Hash of the method signature and encoded parameters.
     */
    private String data;

    public CallObject(String from, String to, BigInteger gasLimit, BigInteger gasPrice, BigInteger value, String data) {
        this.from = from;
        this.to = to;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
        this.value = value;
        this.data = data;
    }

    public static CallObject createCallObject(String from, String to, BigInteger gas,
                                              BigInteger gasPrice, BigInteger value, String data) {
        return new CallObject(from, to, gas, gasPrice, value, data);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGasLimit() {
        return convert(gasLimit);
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

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
