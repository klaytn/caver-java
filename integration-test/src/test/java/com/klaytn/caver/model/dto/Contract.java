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

package com.klaytn.caver.model.dto;

import java.math.BigInteger;
import java.util.List;

public class Contract implements TestComponent {

    private Component component;
    private Expected expected;
    private Component.Type contractComponentType;

    public Component getComponent() {
        return component;
    }

    public Expected getExpected() {
        return expected;
    }

    public Component.Type getContractComponentType() {
        return contractComponentType;
    }

    public Contract(Component component, Expected expected, Component.Type contractComponentType) {
        this.component = component;
        this.expected = expected;
        this.contractComponentType = contractComponentType;
    }

    @Override
    public Type getType() {
        return Type.CONTRACT;
    }

    public static class Component {
        private String from;
        private String to;
        private BigInteger gas;
        private String method;
        private List<String> params;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public BigInteger getGas() {
            return gas;
        }

        public String getMethod() {
            return method;
        }

        public List<String> getParams() {
            return params;
        }

        enum Type {
            CALL,
            SEND
        }

    }
}
