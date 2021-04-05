package com.klaytn.caver.abi.datatypes.generated;

import com.klaytn.caver.abi.datatypes.Uint;

import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Uint32 extends Uint {
    public static final com.klaytn.caver.abi.datatypes.generated.Uint32 DEFAULT = new com.klaytn.caver.abi.datatypes.generated.Uint32(BigInteger.ZERO);

    public Uint32(BigInteger value) {
        super(32, value);
    }

    public Uint32(long value) {
        this(BigInteger.valueOf(value));
    }
}
