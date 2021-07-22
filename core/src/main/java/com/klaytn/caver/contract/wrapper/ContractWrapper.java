/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.contract.wrapper;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;

import java.io.IOException;

/**
 * Representing a ContractWrapper
 * 1. This class wraps constructors of Contract class
 * 2. This class should be accessed via `caver.contract`
 */
public class ContractWrapper {
    /**
     * A Caver instance
     */
    private Caver caver;

    /**
     * Creates a ContractWrapper instance
     * @param caver A Caver instance
     */
    public ContractWrapper(Caver caver) {
        this.caver = caver;
    }

    /**
     * Creates a Contract instance
     * @param abi A contract's ABI(Application Binary Interface) json string.
     * @return Contract
     * @throws IOException
     */
    public Contract create(String abi) throws IOException {
        return Contract.create(caver, abi);
    }

    /**
     * Creates a Contract instance
     * @param abi A contract's ABI(Application Binary Interface) json string.
     * @param contractAddress An address string of contract deployed on Klaytn.
     * @return Contract
     * @throws IOException
     */
    public Contract create(String abi, String contractAddress) throws IOException {
        return Contract.create(caver, abi, contractAddress);
    }
}
