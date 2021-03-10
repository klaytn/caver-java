package com.klaytn.caver.abi.datatypes.generated;

import com.klaytn.caver.abi.datatypes.Uint;

import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Uint40 extends Uint {
    public static final com.klaytn.caver.abi.datatypes.generated.Uint40 DEFAULT = new com.klaytn.caver.abi.datatypes.generated.Uint40(BigInteger.ZERO);

    public Uint40(BigInteger value) {
        super(40, value);
    }

    public Uint40(long value) {
        this(BigInteger.valueOf(value));
    }
}
