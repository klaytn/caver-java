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
public class StaticArray30<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray30(List<T> values) {
        super(30, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray30(T... values) {
        super(30, values);
    }

    public StaticArray30(Class<T> type, List<T> values) {
        super(type, 30, values);
    }

    @SafeVarargs
    public StaticArray30(Class<T> type, T... values) {
        super(type, 30, values);
    }
}
