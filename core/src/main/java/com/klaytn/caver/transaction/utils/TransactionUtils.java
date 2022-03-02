/*
 * Copyright 2022 The caver-java Authors
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

package com.klaytn.caver.transaction.utils;

import com.klaytn.caver.transaction.utils.accessList.wrapper.AccessListWrapper;

/**
 * TransactionUtils includes various helper classes for transaction packages.
 */
public class TransactionUtils {
    /**
     * AccessListWrapper instance.
     */
    public AccessListWrapper accessList;

    /**
     * Creates a TransactionUtils instance.
     * @param accessList An instance of AccessListWrapper class.
     */
    public TransactionUtils(AccessListWrapper accessList) {
        this.accessList = accessList;
    }
}
