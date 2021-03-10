package com.klaytn.caver.abi.datatypes.generated;

import com.klaytn.caver.abi.datatypes.StaticArray;
import com.klaytn.caver.abi.datatypes.Type;

import java.util.List;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray1<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray1(List<T> values) {
        super(1, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray1(T... values) {
        super(1, values);
    }

    public StaticArray1(Class<T> type, List<T> values) {
        super(type, 1, values);
    }

    @SafeVarargs
    public StaticArray1(Class<T> type, T... values) {
        super(type, 1, values);
    }
}
