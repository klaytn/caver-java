package com.klaytn.caver.kct.kip7;

import java.math.BigInteger;

public class KIP7DeployParams {
    String name;
    String symbol;
    int decimals;
    BigInteger initialSupply;

    public KIP7DeployParams(String name, String symbol, int decimals, BigInteger initialSupply) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.initialSupply = initialSupply;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public BigInteger getInitialSupply() {
        return initialSupply;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public void setInitialSupply(BigInteger initialSupply) {
        this.initialSupply = initialSupply;
    }
}
