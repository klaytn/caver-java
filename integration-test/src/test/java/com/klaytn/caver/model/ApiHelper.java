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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.Class.forName;

public class ApiHelper {
    public static final String GET_PREFIX = "get";
    public static final String IS_PREFIX = "is";

    private static Set<String> usingIs = new HashSet(
            Arrays.asList("klay_accountCreated", "klay_mining", "klay_syncing", "klay_writeThroughCaching"));
    private static Set<String> noGetter = new HashSet(
            Arrays.asList("klay_call", "klay_estimateGas", "klay_sendTransaction", "klay_signTransaction",
                    "klay_newBlockFilter", "klay_newFilter", "klay_newPendingTransactionFilter", "klay_uninstallFilter"));
    private static Set<ExceptionalRPC> exceptionalRPCs = new HashSet(Arrays.asList(
            new ExceptionalRPC("klay_sendRawTransaction", "sendSignedTransaction"),
            new ExceptionalRPC("klay_getBlockTransactionCountByNumber", "getTransactionCountByNumber"),
            new ExceptionalRPC("klay_getBlockTransactionCountByHash", "getTransactionCountByHash")
    ));

    public static Class getResponseClass(String methodName) {
        Class responseClass = null;
        try {
            Method method = Arrays.stream(forName("com.klaytn.caver.JsonRpc2_0Klay").getDeclaredMethods())
                    .filter(aMethod -> aMethod.getName().equals(toGetterName(methodName)))
                    .findFirst().orElseThrow(NullPointerException::new);
            Type returnType = method.getGenericReturnType();
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            responseClass = forName(typeArguments[1].getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return responseClass;
    }

    public static Class getModelClass(String methodName) {
        Class modelClass = null;
        try {
            Class responseClass = getResponseClass(methodName);
            Type superType = responseClass.getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments[0] instanceof ParameterizedType) {
                ParameterizedType model = (ParameterizedType) typeArguments[0];
                return Class.forName(model.getRawType().getTypeName());
            }
            modelClass = Class.forName(typeArguments[0].getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modelClass;
    }

    public static String toGetterName(String methodName) {
        Optional<ExceptionalRPC> exceptional = exceptionalRPCs.stream().filter(rpc -> methodName.equals(rpc.getFrom())).findAny();
        if (exceptional.isPresent()) return exceptional.get().getTo();

        String coreName = methodName.split("_")[1];
        if (coreName.startsWith(GET_PREFIX) || coreName.startsWith(IS_PREFIX)) return coreName;
        if (noGetter.contains(methodName)) return coreName;
        if (usingIs.contains(methodName)) return IS_PREFIX + toCamelCase(coreName);
        return GET_PREFIX + toCamelCase(coreName);
    }

    public static String toCamelCase(String methodName) {
        return methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    public static class ExceptionalRPC {
        private String from;
        private String to;

        public ExceptionalRPC(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}
