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
package com.klaytn.caver.kct.kip13;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class KIP13 extends Contract{

    /**
     * Creates a KIP13 instance.
     * @param caver A Caver instance.
     * @param contractAddress A contract address.
     * @throws IOException
     */
    public KIP13(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP13ConstantData.ABI, contractAddress);
    }

    /**
     * Sends query to check whether interface is supported or not.<p>
     * If you want to execute this method alone, it recommended to execute {@link #isImplementedKIP13Interface()} before.
     * @param interfaceId The interface id to check.
     * @return boolean
     */
    public boolean sendQuery(String interfaceId) {
        try {
            List<Type> result = this.call("supportsInterface", interfaceId);
            return ((Bool)result.get(0)).getValue();
        } catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks if the contract implements KIP-13.
     * @return boolean
     */
    public boolean isImplementedKIP13Interface() {
        // The implementing contract will have a supportsInterface function, and it returns:
        //  - true when interfaceID is 0x01ffc9a7 (supportsInterface itself)
        //  - false when interfaceID is 0xffffffff
        //  - true for interfaceID this contract implements
        //  - false for any other interfaceID
        return this.sendQuery("0x01ffc9a7") && !this.sendQuery("0xffffffff");
    }
}
