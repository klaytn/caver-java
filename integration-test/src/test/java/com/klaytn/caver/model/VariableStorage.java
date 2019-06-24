/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.model;

import com.klaytn.caver.crpyto.KlayCredentials;
import org.web3j.crypto.Keys;

import java.util.*;

public class VariableStorage {

    public static List<Pair> pairs = new ArrayList<>();

    static {
        pairs.add(new Pair("filterId", Arrays.asList("klay_newFilter", "klay_newPendingTransactionFilter", "klay_newBlockFilter")));
    }

    private Map<String, Object> variables = new HashMap<>();

    public static boolean isStartWithRandom(Object value) {
        return value instanceof String && ((String) value).startsWith("random");
    }

    public static boolean isHexValue(Object value) {
        return value instanceof String && ((String) value).startsWith("0x");
    }

    public Object get(Object key) {
        return variables.getOrDefault(key, null);
    }

    public void set(String key, Object value) {
        variables.put(key, value);
    }

    public String convertRandom(String expectedValue) {
        if (isStartWithRandom(expectedValue)) {
            return ((KlayCredentials) variables.get(expectedValue)).getAddress();
        }
        return expectedValue;
    }

    public Object createRandom(String target) throws Exception {
        Object mayTarget = variables.get(target);
        if (mayTarget != null) return mayTarget;

        KlayCredentials random = KlayCredentials.create(Keys.createEcKeyPair());
        variables.put(target, random);
        return random;
    }

    public boolean has(Object param) {
        return variables.containsKey(param);
    }

    public static class Pair {
        private String variableName;
        private List<String> methodNames;

        public Pair(String variableName, List<String> methodNames) {
            this.variableName = variableName;
            this.methodNames = methodNames;
        }

        public String getVariableName() {
            return variableName;
        }

        public boolean hasSameMethodName(String target) {
            return methodNames.stream().anyMatch(methodName -> methodName.equals(target));
        }
    }
}
