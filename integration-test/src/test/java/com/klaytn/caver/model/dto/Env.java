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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Env {
    private String url;
    private Credentials sender;
    private Credentials feePayer;
    private Accounts accounts;

    public Env() {
    }

    @JsonProperty("URL")
    public String getURL() {
        return url;
    }

    public Credentials getSender() {
        return sender;
    }

    public Credentials getFeePayer() {
        return feePayer;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public static class Credentials {
        private String address;
        private String privateKey;

        public Credentials() {
        }

        public Credentials(String address, String privateKey) {
            this.address = address;
            this.privateKey = privateKey;
        }

        public String getAddress() {
            return address;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }

    public static class Accounts {
        private Credentials acc1;
        private Credentials acc2;

        public Credentials getAcc1() {
            return acc1;
        }

        public Credentials getAcc2() {
            return acc2;
        }
    }
}
