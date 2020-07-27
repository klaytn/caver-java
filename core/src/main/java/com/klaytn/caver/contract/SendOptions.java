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
        if(from == null) {
            throw new IllegalArgumentException("from is missing.");
        }

        if(!Utils.isAddress(from)) {
            throw new IllegalArgumentException("Invalid address. : " + from);
        }

        this.from = from;
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(String gas) {
        //Gas value must be set.
        if(gas == null || gas.isEmpty() || gas.equals("0x")) {
            throw new IllegalArgumentException("gas is missing.");
        }

        if(!Utils.isNumber(gas)) {
            throw new IllegalArgumentException("Invalid gas. : "  + gas);
        }
        this.gas = gas;
    }

    /**
     * Setter function for gas
     * @param gas The maximum amount of gas the transaction is allowed to use.
     */
    public void setGas(BigInteger gas) {
        setGas(Numeric.toHexStringWithPrefix(gas));
    }

    /**
     * Setter function for value.
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is missing.");
        }

        if(!Utils.isNumber(value)) {
            throw new IllegalArgumentException("Invalid value : " + value);
        }

        this.value = Numeric.prependHexPrefix(value);
    }

    /**
     * Setter function for value
     * @param value The amount of KLAY in peb to be transferred.
     */
    public void setValue(BigInteger value) {
        setValue(Numeric.toHexStringWithPrefix(value));
    }
}
