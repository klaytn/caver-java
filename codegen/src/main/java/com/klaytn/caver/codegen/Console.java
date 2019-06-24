/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/codegen/src/main/java/org/web3j/codegen/Console.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.codegen;

/**
 * Command line utility classes.
 */
public class Console {
    public static void exitError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public static void exitError(Throwable throwable) {
        exitError(throwable.getMessage());
    }

    public static void exitSuccess(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
