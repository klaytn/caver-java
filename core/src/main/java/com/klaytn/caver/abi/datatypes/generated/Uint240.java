/*
 * Modifications copyright 2021 The caver-java Authors
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is derived from web3j/abi/src/main/java/org/web3j/abi/generated/Uint240.java (2021/04/05).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.abi.datatypes.generated;

import com.klaytn.caver.abi.datatypes.Uint;

import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Uint240 extends Uint {
    public static final com.klaytn.caver.abi.datatypes.generated.Uint240 DEFAULT = new com.klaytn.caver.abi.datatypes.generated.Uint240(BigInteger.ZERO);

    public Uint240(BigInteger value) {
        super(240, value);
    }

    public Uint240(long value) {
        this(BigInteger.valueOf(value));
    }
}
