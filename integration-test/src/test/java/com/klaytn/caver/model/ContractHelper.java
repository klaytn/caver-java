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

import com.fasterxml.jackson.databind.JsonNode;
import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.model.dto.Env;
import com.klaytn.caver.model.dto.Transaction;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.*;

public class ContractHelper {

    public static Map<String, Class> typeMapper = new HashMap<>();

    static {
        typeMapper.put("address", String.class);
        typeMapper.put("uint256", BigInteger.class);
        typeMapper.put("bytes", byte[].class);
    }

    public static List<Class> extractParamTypes(JsonNode params) {
        List<Class> additionalParamTypes = new ArrayList<>();
        for (JsonNode param : params) {
            additionalParamTypes.add(guessType(param.asText()));
        }
        return additionalParamTypes;
    }

    public static Class guessType(String param) {
        if (param.startsWith("0x")) {
            if (isAddress(param)) return String.class;
            return byte[].class;
        }
        if (param.equals("false") || param.equals("true")) {
            return Boolean.class;
        }
        return BigInteger.class;
    }

    private static boolean isAddress(String param) {
        return param.matches("0x\\w{40}$");
    }

    public static boolean hasParentheses(String methodName) {
        return methodName.contains("(") && methodName.contains(")");
    }

    public static String removeParentheses(String methodName) {
        return methodName.substring(0, methodName.indexOf("("));
    }

    public static Class<?>[] getParenthesesTypes(String methodName) {
        String[] splitTypes = methodName.substring(methodName.indexOf("(") + 1, methodName.indexOf(")")).split(",");
        List<Class> types = new ArrayList<>();
        for (String splitType : splitTypes) {
            types.add(typeMapper.get(splitType));
        }
        return types.toArray(new Class[]{});
    }

    public static Object[] extractDeployParams(Env env, List<Class> paramTypes, JsonNode params) {
        Caver caver = Caver.build(env.getURL());
        KlayCredentials sender = KlayCredentials.create(
                env.getSender().getPrivateKey(),
                env.getSender().getAddress()
        );

        List deployParams
                = new ArrayList(Arrays.asList(caver, sender, Transaction.DEFAULT_CHAIN_ID, new DefaultGasProvider()));
        Iterator<JsonNode> paramIterator = params.iterator();
        for (Class paramType : paramTypes) {
            deployParams.add(convertType(paramType, paramIterator.next().asText()));
        }
        return deployParams.toArray();
    }

    public static Object convertType(Class paramType, String param) {
        if (paramType.equals(String.class)) return param;
        if (paramType.equals(Boolean.class)) return param.equals("true");
        if (paramType.equals(byte[].class)) return Numeric.hexStringToByteArray(param);
        return new BigInteger(param);
    }

    public static String getEventName(String eventName) {
        return "get" + eventName + "Events";
    }
}
