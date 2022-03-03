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

package com.klaytn.caver.transaction.utils.wrapper;

/**
 * TransactionUtilsWrapper provide usability so that the static method of the access list and the static method of the access tuple<p>
 * can be accessed and used through `caver.transaction.utils`.
 */
public class TransactionUtilsWrapper {
    /**
     * AccessListWrapper instance.
     */
    public AccessListWrapper accessList;

    /**
     * AccessTupleWrapper instance.
     */
    public AccessTupleWrapper accessTuple;

    /**
     * Creates a TransactionUtils instance.
     */
    public TransactionUtilsWrapper() {
        this.accessList = new AccessListWrapper();
        this.accessTuple = new AccessTupleWrapper();
    }
}
