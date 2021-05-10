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

package com.klaytn.caver.kct.wrapper;

import com.klaytn.caver.Caver;
import com.klaytn.caver.kct.kip17.wrapper.KIP17Wrapper;
import com.klaytn.caver.kct.kip37.wrapper.KIP37Wrapper;
import com.klaytn.caver.kct.kip7.wrapper.KIP7Wrapper;

/**
 * Representing a KCTWrapper
 * 1. This class contains wrapper classes of KIP* which have static methods
 * 2. This class should be accessed via `caver.kct`
 */
public class KCTWrapper {
    /**
     * A KIP7Wrapper instance
     */
    public KIP7Wrapper kip7;

    /**
     * A KIP17Wrapper instance
     */
    public KIP17Wrapper kip17;

    /**
     * A KIP37Wrapper instance
     */
    public KIP37Wrapper kip37;

    /**
     * Creates a KCTWrapper instance
     * @param caver A Caver instance
     */
    public KCTWrapper(Caver caver) {
        this.kip7 = new KIP7Wrapper(caver);
        this.kip17 = new KIP17Wrapper(caver);
        this.kip37 = new KIP37Wrapper(caver);
    }
}
