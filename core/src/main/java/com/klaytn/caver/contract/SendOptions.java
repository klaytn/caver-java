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
 * Representing a options to create a SmartContractDeploy, SmartContractExecution, FeeDelegatedSmartContractDeploy,
 * FeeDelegatedSmartContractExecution, FeeDelegatedSmartContractDeployWithRatio, FeeDelegatedSmartContractExecutionWithRatio transaction.
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
     * The flag whether fee delegation feature is active.
     */
    boolean feeDelegation = false;

    /**
     * The address of fee payer.
     */
    String feePayer;

    /**
     * The fee ratio of the fee payer.
     * The valid range is between 1 and 99. Zero(0) is not allowed. 100 and above are not allowed as well.
     */
    String feeRatio = "0x0";

    /**
     * Creates a SendOptions instance.
     */
    public SendOptions() { }

    /**
     * Creates a SendOptions instance.
     * It should only be used when executing KIP7 / KIP7 class methods.
     * Because if gas passed to the method is null, the KIP7 / KIP7 class method automatically estimates gas.
     * @param from The address of the sender.
     */
    public SendOptions(String from) {
        this(from, (String)null);
    }

    /**
     * Creates a SendOptions instance.
     * It sets value to 0x0.
     * @param from The address of the sender.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public SendOptions(String from, String gas) {
        this(from, gas, "0x0");
    }

    /**
     * Creates a SendOptions instance.
     * It sets value to 0x0.
     * @param from The address of the sender.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public SendOptions(String from, BigInteger gas) {
        this(from, gas, BigInteger.ZERO);
    }

    /**
     * Creates a SendOptions instance.
     * @param from The address of the sender.
     * @param gas The maximum amount of gas the transaction is allowed to use.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public SendOptions(String from, String gas, String value) {
        setFrom(from);
        setGas(gas);
        setValue(value);
    }

    /**
     * Creates a SendOptions instance.
     * @param from The address of the sender.
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
     * Getter function for feeDelegation flag.
     * @return The flag whether fee delegation feature is active.
     */
    public boolean isFeeDelegation() {
        return feeDelegation;
    }

    /**
     * Getter function for fee payer.
     * @return The address of fee payer
     */
    public String getFeePayer() {
        return feePayer;
    }

    /**
     * Getter function for fee ratio.
     * @return The fee ratio of the fee payer.
     */
    public String getFeeRatio() {
        return feeRatio;
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

    /**
     * Setter function for feeDelegation.
     * @param feeDelegation The flag whether fee delegation feature is active.
     */
    public void setFeeDelegation(boolean feeDelegation) {
        this.feeDelegation = feeDelegation;
    }

    /**
     * Setter function for feePayer
     * @param feePayer The address of fee payer.
     */
    public void setFeePayer(String feePayer) {
        if(!isFeeDelegation()) {
            throw new IllegalArgumentException("Before set a 'feePayer' field, it should set a 'feeDelegation' field to true.");
        }

        if(feePayer != null) {
            if(!Utils.isAddress(feePayer)) {
                throw new IllegalArgumentException("Invalid address. : " + feePayer);
            }

            this.feePayer = feePayer;
        }
    }

    /**
     * Setter function for feeRatio.
     * @param feeRatio A fee ratio of the fee payer.
     */
    public void setFeeRatio(BigInteger feeRatio) {
        setFeeRatio(Numeric.toHexStringWithPrefix(feeRatio));
    }

    /**
     * Setter function for feeRatio.
     * @param feeRatio A fee ratio of the fee payer represented as a hexadecimal string.
     */
    public void setFeeRatio(String feeRatio) {
        if(!isFeeDelegation()) {
            throw new IllegalArgumentException("Before set a 'feeRatio' field, it should set a 'feeDelegation' field to true.");
        }

        if(getFeePayer().equals(Utils.DEFAULT_ZERO_ADDRESS)) {
            throw new IllegalArgumentException("Before set a 'feeRatio' field, it should set a 'feePayer' field.");
        }

        if(feeRatio == null || feeRatio.isEmpty()) {
            this.feeRatio = "0x0";
            return;
        }

        if(!Utils.isNumber(feeRatio) && !Utils.isHex(feeRatio)) {
            throw new IllegalArgumentException("Invalid type of feeRatio: feeRatio should be number type or hex number string");
        }

        int feeRatioVal = Numeric.toBigInt(feeRatio).intValue();
        if(feeRatioVal <= 0 || feeRatioVal >= 100) {
            throw new IllegalArgumentException("Invalid feeRatio: feeRatio is out of range. [1,99]");
        }

        this.feeRatio = feeRatio;
    }
}
