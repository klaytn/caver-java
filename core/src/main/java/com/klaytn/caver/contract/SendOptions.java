/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.contract;

import com.klaytn.caver.utils.Utils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * Representing a options to send SmartContractExecution transaction.
 */
public class SendOptions {

    /**
     * The address of the sender.
     */
    String from;

    /**
     * The maximum amount of gas the transaction is allowed to use.
     */
    String gas;

    /**
     * The amount of KLAY in peb to be transferred.
     */
    String value = "0x0";

    /**
     * Creates a SendOption instance.
     */
    public SendOptions() {

    }
    /**
     * Creates a SendOption instance.
     * It sets value to 0x0.
     * @param from The address of the sender
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public SendOptions(String from, String gas) {
        this(from, gas, "0x0");
    }

    /**
     * Creates a SendOption instance.
     * It sets value to 0x0.
     * @param from The address of the sender
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public SendOptions(String from, BigInteger gas) {
        this(from, gas, BigInteger.ZERO);
    }

    /**
     * Creates a SendOption instance.
     * @param from The address of the sender
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public SendOptions(String from, String gas, String value) {
        setFrom(from);
        setGas(gas);
        setValue(value);
    }

    /**
     * Creates a SendOption instance.
     * @param from The address of the sender
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public SendOptions(String from, BigInteger gas, BigInteger value) {
        setFrom(from);
        setGas(gas);
        setValue(value);
    }

    /**
     * Getter function for from.
     * @return String
     */
    public String getFrom() {
        return from;
    }

    /**
     * Getter function for gas.
     * @return String
     */
    public String getGas() {
        return gas;
    }

    /**
     * Getter function for value.
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter function for from.
     * @param from The address of the sender
     */
    public void setFrom(String from) {
        if(from != null) {
            if(!Utils.isAddress(from)) {
                throw new IllegalArgumentException("Invalid address. : " + from);
            }

            this.from = from;
        }
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(String gas) {
        if (gas != null) {
            if(!Utils.isNumber(gas)) {
                throw new IllegalArgumentException("Invalid gas. : "  + gas);
            }

            this.gas = gas;
        }
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(BigInteger gas) {
        if(gas != null) {
            setGas(Numeric.toHexStringWithPrefix(gas));
        }
    }

    /**
     * Setter function for value.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(String value) {
        if(value != null) {
            if(!Utils.isNumber(value)) {
                throw new IllegalArgumentException("Invalid value : " + value);
            }

            this.value = Numeric.prependHexPrefix(value);
        } else {
            this.value = "0x0";
        }


    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        if(value != null) {
            setValue(Numeric.toHexStringWithPrefix(value));
        } else {
            this.value = "0x0";
        }
    }
}
