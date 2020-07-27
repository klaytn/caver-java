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
        }

        this.value = "0x0";
    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        if(value != null) {
            setValue(Numeric.toHexStringWithPrefix(value));
        }

        this.value = "0x0";
    }
}
