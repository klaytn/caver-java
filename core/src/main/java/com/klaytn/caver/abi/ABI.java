/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.abi;

import com.klaytn.caver.abi.datatypes.*;
import com.klaytn.caver.contract.ContractEvent;
import com.klaytn.caver.contract.ContractIOType;
import com.klaytn.caver.contract.ContractMethod;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.klaytn.caver.abi.TypeEncoder.isDynamic;

/**
 * Representing a ABI type encode / decode.
 */
public class ABI {

    /**
     * Encodes a function call.
     * @param method A ContractMethod instance.
     * @param params A List of method parameter.
     * @return String
     */
    public static String encodeFunctionCall(ContractMethod method, List<Object> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String functionSignature = ABI.buildFunctionString(method);

        List<String> solTypeList = new ArrayList();

        for (ContractIOType contractIOType : method.getInputs()) {
            solTypeList.add(contractIOType.getTypeAsString());
        }

        return encodeFunctionCall(functionSignature, solTypeList, params);
    }

    /**
     * Encodes a function call.
     * @param functionSig A function signature string.
     * @param params A List of method parameter.
     * @return String
     */
    public static String encodeFunctionCall(String functionSig, List<String> solTypeList, List<Object> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String methodId = encodeFunctionSignature(functionSig);
        String encodedParams = encodeParameters(solTypeList, params);

        return methodId + encodedParams;
    }

    /**
     * Encodes a function call
     * @param method A ContractMethod instance.
     * @param params A List of method parameter wrapped solidity wrapper class.
     * @return String
     */
    public static String encodeFunctionCallWithSolidityWrapper(ContractMethod method, List<Type> params) {
        String methodId = encodeFunctionSignature(method);
        String encodedArguments = ABI.encodeParameters(params);

        return methodId + encodedArguments;
    }

    /**
     * Encodes a function signature.
     * @param method A ContractMethod instance.
     * @return String
     */
    public static String encodeFunctionSignature(ContractMethod method) {
        return encodeFunctionSignature(buildFunctionString(method));
    }

    /**
     * Encodes a function signature.
     * @param functionName A function name string.
     * @return String
     */
    public static String encodeFunctionSignature(String functionName) {
        byte[] input = functionName.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
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
    public static String encodeContractDeploy(ContractMethod constructor, String byteCode, List<Object> constructorParams) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String encodedParam = "";

        if(constructor != null) {
            constructor.checkTypeValid(constructorParams);
            encodedParam = ABI.encodeParameters(constructor, constructorParams);
        }

        return byteCode + encodedParam;
    }

    /**
     * Build a function name string.
     * @param method A ContractMethod instance.
     * @return String
     */
    public static String buildFunctionString(ContractMethod method) {
        StringBuilder result = new StringBuilder();
        result.append(method.getName());
        result.append("(");
        String params = method.getInputs().stream()
                .map(ContractIOType::getTypeAsString)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");

        //remove "tuple" string
        return result.toString().replace("tuple", "");
    }

    /**
     * Encodes a event signature.
     * @param event A ContractEvent instance.
     * @return String
     */
    public static String encodeEventSignature(ContractEvent event) {
        return encodeEventSignature(buildEventString(event));
    }

    /**
     * Encodes a event signature.
     * @param eventName A event signature.
     * @return String
     */
    public static String encodeEventSignature(String eventName) {
        byte[] input = eventName.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }

    /**
     * Build a event name string.
     * @param event A ContractEvent instance
     * @return String
     */
    public static String buildEventString(ContractEvent event) {
        StringBuilder result = new StringBuilder();
        result.append(event.getName());
        result.append("(");
        String params = event.getInputs().stream()
                .map(ContractIOType::getTypeAsString)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");

        //remove "tuple" string
        return result.toString().replace("tuple", "");
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
    public static String encodeParameter(String solidityType, Object value) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Type type = TypeDecoder.instantiateType(solidityType, value);
        return encodeParameter(type);
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
    public static String encodeParameters(ContractMethod method, List<Object> values) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<String> solTypeList = new ArrayList<>();
        for(ContractIOType type : method.getInputs()) {
            solTypeList.add(type.getTypeAsString());
        }

        return encodeParameters(solTypeList, values);
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
    public static String encodeParameters(List<String> solidityTypes, List<Object> values) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Type> typeList = new ArrayList<>();
        for(int i=0; i < solidityTypes.size(); i++) {
            Type type = TypeDecoder.instantiateType(solidityTypes.get(i), values.get(i));
            typeList.add(type);
        }

        return encodeParameters(typeList);
    }

    /**
     * Encodes a parameter based on its type to its ABI representation.
     * @param parameter A parameter that wrapped solidity type wrapper.
     * @return String
     */
    public static String encodeParameter(Type parameter) {
        return encodeParameters(Arrays.asList(parameter));
    }

    /**
     * Encodes a parameter based on its type to its ABI representation.
     * @param parameters A List of parameters that wrappped solidity type wrapper
     * @return String
     */
    public static String encodeParameters(List<Type> parameters) {
        return new DefaultFunctionEncoder().encodeParameters(parameters);
    }

    /**
     * Decodes an ABI encoded parameter.
     * @param solidityType A solidity type string.
     * @param encoded The ABI byte code to decode
     * @return Type
     * @throws ClassNotFoundException
     */
    public static Type decodeParameter(String solidityType, String encoded) throws ClassNotFoundException {
        return decodeParameters(Arrays.asList(solidityType), encoded).get(0);
    }

    /**
     * Decodes an ABI encoded parameters.
     * @param solidityTypeList A List of solidity type string.
     * @param encoded The ABI byte code to decode
     * @return List
     * @throws ClassNotFoundException
     */
    public static List<Type> decodeParameters(List<String> solidityTypeList, String encoded) throws ClassNotFoundException {
        List<TypeReference<Type>> params = new ArrayList<>();

        for(String solType : solidityTypeList) {
            params.add(TypeReference.makeTypeReference(solType));
        }

        return FunctionReturnDecoder.decode(encoded, params);
    }

    /**
     * Decodes an ABI encoded output parameters.
     * @param method A ContractMethod instance.
     * @param encoded The ABI byte code to decoded
     * @return List
     * @throws ClassNotFoundException
     */
    public static List<Type> decodeParameters(ContractMethod method, String encoded) throws ClassNotFoundException {
        List<TypeReference<Type>> resultParams = new ArrayList<>();

        for(ContractIOType ioType: method.getOutputs()) {
            resultParams.add(TypeReference.makeTypeReference(ioType.getTypeAsString()));
        }

        return FunctionReturnDecoder.decode(encoded, resultParams);
    }

    /**
     * Decodes an ABI encoded log data and indexed topic data
     * @param inputs A list of ContractIOType instance.
     * @param data An ABI-encoded in the data field of a log
     * @param topics A list of indexed parameter topics of the log.
     * @return EventValues
     * @throws ClassNotFoundException
     */
    public static EventValues decodeLog(List<ContractIOType> inputs, String data, List<String> topics) throws ClassNotFoundException {
        List<TypeReference<Type>> indexedList = new ArrayList<>();
        List<TypeReference<Type>> nonIndexedList = new ArrayList<>();

        for(ContractIOType input: inputs) {
            if(input.isIndexed()) {
                indexedList.add(TypeReference.makeTypeReference(input.getTypeAsString()));
            } else {
                nonIndexedList.add(TypeReference.makeTypeReference(input.getTypeAsString()));
            }
        }

        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(data, nonIndexedList);
        List<Type> indexedValues = new ArrayList<>();

        for(int i=0; i < indexedList.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedList.get(i));
            indexedValues.add(value);
        }

        return new EventValues(indexedValues, nonIndexedValues);
    }

}
