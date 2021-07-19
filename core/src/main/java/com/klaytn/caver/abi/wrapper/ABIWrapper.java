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

package com.klaytn.caver.abi.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.abi.EventValues;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.ContractEvent;
import com.klaytn.caver.contract.ContractIOType;
import com.klaytn.caver.contract.ContractMethod;
import com.klaytn.caver.utils.Utils;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representing an ABIWrapper which wraps all of static methods of ABI
 */
public class ABIWrapper {
    /**
     * Creates an ABIWrapper instance
     */
    public ABIWrapper() {
    }

    /**
     * Encodes a function call.
     * @param method A ContractMethod instance.
     * @param params A List of method parameter.
     * @return String
     */
    public String encodeFunctionCall(ContractMethod method, List<Object> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ABI.encodeFunctionCall(method, params);
    }

    /**
     * Encodes a function call.
     * @param functionSig A function signature string.
     * @param params A List of method parameter.
     * @return String
     */
    public String encodeFunctionCall(String functionSig, List<String> solTypeList, List<Object> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ABI.encodeFunctionCall(functionSig, solTypeList, params);
    }

    /**
     * Encodes a function call
     * @param method A ContractMethod instance.
     * @param params A List of method parameter wrapped solidity wrapper class.
     * @return String
     */
    public String encodeFunctionCallWithSolidityWrapper(ContractMethod method, List<Type> params) {
        return ABI.encodeFunctionCallWithSolidityWrapper(method, params);
    }

    /**
     * Encodes a function signature.
     * @param method A ContractMethod instance.
     * @return String
     */
    public String encodeFunctionSignature(ContractMethod method) {
        return ABI.encodeFunctionSignature(method);
    }

    /**
     * Encodes a function signature.
     * @param functionName A function name string.
     * @return String
     */
    public String encodeFunctionSignature(String functionName) {
        return ABI.encodeFunctionSignature(functionName);
    }

    /**
     * Encodes a data related contract deployment.
     * @param constructor A ContractMethod instance that contains constructor info.
     * @param byteCode A smart contract bytecode.
     * @param constructorParams A list of parameter that need to execute Constructor
     * @return String
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String encodeContractDeploy(ContractMethod constructor, String byteCode, List<Object> constructorParams) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ABI.encodeContractDeploy(constructor, byteCode, constructorParams);
    }

    /**
     * Build a function name string.
     * @param method A ContractMethod instance.
     * @return String
     */
    public String buildFunctionString(ContractMethod method) {
        return ABI.buildFunctionString(method);
    }

    /**
     * Encodes a event signature.
     * @param event A ContractEvent instance.
     * @return String
     */
    public String encodeEventSignature(ContractEvent event) {
        return ABI.encodeEventSignature(event);
    }

    /**
     * Encodes a event signature.
     * @param eventName A event signature.
     * @return String
     */
    public String encodeEventSignature(String eventName) {
        return ABI.encodeEventSignature(eventName);
    }


    /**
     * Build a event name string.
     * @param event A ContractEvent instance
     * @return String
     */
    public String buildEventString(ContractEvent event) {
        return ABI.buildEventString(event);
    }

    /**
     * Encodes a parameter based on its type to its ABI representation.
     * @param solidityType A solidity type to encode.
     * @param value A value to encode
     * @return String
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String encodeParameter(String solidityType, Object value) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ABI.encodeParameter(solidityType, value);
    }

    /**
     * Encodes a parameters based on its type to its ABI representation.
     * @param method A ContractMethod instance that contains to solidity type
     * @param values A List of value to encode
     * @return String
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public String encodeParameters(ContractMethod method, List<Object> values) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ABI.encodeParameters(method, values);
    }


    /**
     * Encodes a parameters based on its type to its ABI representation.
     * @param solidityTypes A list of solidity type to encode
     * @param values A List of value to encode
     * @return String
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String encodeParameters(List<String> solidityTypes, List<Object> values) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ABI.encodeParameters(solidityTypes, values);
    }

    /**
     * Encodes a parameter based on its type to its ABI representation.
     * @param parameter A parameter that wrapped solidity type wrapper.
     * @return String
     */
    public String encodeParameter(Type parameter) {
        return ABI.encodeParameter(parameter);
    }

    /**
     * Encodes a parameter based on its type to its ABI representation.
     * @param parameters A List of parameters that wrappped solidity type wrapper
     * @return String
     */
    public String encodeParameters(List<Type> parameters) {
        return ABI.encodeParameters(parameters);
    }

    /**
     * Decodes a ABI encoded parameter.
     * @param solidityType A solidity type string.
     * @param encoded The ABI byte code to decode
     * @return Type
     * @throws ClassNotFoundException
     */
    public Type decodeParameter(String solidityType, String encoded) throws ClassNotFoundException {
        return ABI.decodeParameter(solidityType, encoded);
    }

    /**
     * Decodes a ABI encoded parameters.
     * @param solidityTypeList A List of solidity type string.
     * @param encoded The ABI byte code to decode
     * @return List
     * @throws ClassNotFoundException
     */
    public List<Type> decodeParameters(List<String> solidityTypeList, String encoded) throws ClassNotFoundException {
        return ABI.decodeParameters(solidityTypeList, encoded);
    }

    /**
     * Decodes a ABI encoded parameters.
     * @param method A ContractMethod instance.
     * @param encoded The ABI byte code to decoed
     * @return List
     * @throws ClassNotFoundException
     */
    public List<Type> decodeParameters(ContractMethod method, String encoded) throws ClassNotFoundException {
        return ABI.decodeParameters(method, encoded);
    }

    /**
     * Decodes a ABI-encoded log data and indexed topic data
     * @param inputs A list of ContractIOType instance.
     * @param data An ABI-encoded in the data field of a log
     * @param topics A list of indexed parameter topics of the log.
     * @return EventValues
     * @throws ClassNotFoundException
     */
    public EventValues decodeLog(List<ContractIOType> inputs, String data, List<String> topics) throws ClassNotFoundException {
        return ABI.decodeLog(inputs, data, topics);
    }

    /**
     * Decodes a function call data that composed of function selector and encoded input argument.
     * <pre>Example :
     * {@code
     *  String encodedData = "0x24ee0.....";
     *  String abi = "{\n" +
     *                     "  \"name\":\"myMethod\",\n" +
     *                     "  \"type\":\"function\",\n" +
     *                     "  \"inputs\":[\n" +
     *                     "    {\n" +
     *                     "      \"type\":\"uint256\",\n" +
     *                     "      \"name\":\"myNumber\"\n" +
     *                     "    },\n" +
     *                     "    {\n" +
     *                     "      \"type\":\"string\",\n" +
     *                     "      \"name\":\"mystring\"\n" +
     *                     "    }\n" +
     *                     "  ]\n" +
     *                     "}";
     *
     * List<Type> params = caver.abi.decodeFunctionCall(abi, encoded);
     * }
     * </pre>
     * @param functionAbi The abi json string of a function.
     * @param encodedString The encode function call data string.
     * @return List&lt;Type&gt;
     * @throws ClassNotFoundException
     */
    public List<Type> decodeFunctionCall(String functionAbi, String encodedString) throws ClassNotFoundException {
        return ABI.decodeFunctionCall(functionAbi, encodedString);
    }
}
