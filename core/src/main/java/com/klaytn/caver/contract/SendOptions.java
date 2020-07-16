package com.klaytn.caver.contract;

import com.klaytn.caver.utils.Utils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class SendOptions {
    String from;
    String gas;
    String value;

    public SendOptions(String from, String gas) {
        this(from, gas, "0x0");
    }

    public SendOptions(String from, BigInteger gas) {
        this(from, gas, BigInteger.ZERO);
    }

    public SendOptions(String from, String gas, String value) {
        setFrom(from);
        setGas(gas);
        setValue(value);
    }

    public SendOptions(String from, BigInteger gas, BigInteger value) {
        setFrom(from);
        setGas(gas);
        setValue(value);
    }

    public String getFrom() {
        return from;
    }

    public String getGas() {
        return gas;
    }

    public String getValue() {
        return value;
    }

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
     * Setter function for gas price.
     * @param gasPrice A unit price of gas in peb the sender will pay for a transaction fee.
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
