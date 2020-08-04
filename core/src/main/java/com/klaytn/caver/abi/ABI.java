package com.klaytn.caver.abi;

import com.klaytn.caver.contract.ContractEvent;
import com.klaytn.caver.contract.ContractIOType;
import com.klaytn.caver.contract.ContractMethod;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            solTypeList.add(contractIOType.getType());
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
     * Encodes a function call.
     * @param functionName A function name.
     * @param params A List of method parameter that wrapped solidity type wrapper class
     * @return String
     */
    public static String encodeFunctionCall(String functionName, List<Type> params) {
        String functionId = ABI.encodeFunctionSignature(ABI.buildFunctionString(functionName, params));
        String encodedArguments = ABI.encodeParameters(params);

        return functionId + encodedArguments;
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
     * Build a function name string.
     * @param method A ContractMethod instance.
     * @return String
     */
    public static String buildFunctionString(ContractMethod method) {
        StringBuilder result = new StringBuilder();
        result.append(method.getName());
        result.append("(");
        String params = method.getInputs().stream()
                .map(ContractIOType::getType)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");

        return result.toString();
    }

    public static String buildFunctionString(String methodName, List<Type> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream().map(Type::getTypeAsString).collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
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
                .map(ContractIOType::getType)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");

        return result.toString();
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
            solTypeList.add(type.getType());
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
        int dynamicDataOffset = getLength(parameters) * Type.MAX_BYTE_LENGTH;
        StringBuilder result = new StringBuilder();
        StringBuilder dynamicData = new StringBuilder();

        for (Type parameter:parameters) {
            String encodedValue = TypeEncoder.encode(parameter);

            if (isDynamic(parameter)) {
                String encodedDataOffset = TypeEncoder.encode(new Uint(BigInteger.valueOf(dynamicDataOffset)));
                result.append(encodedDataOffset);
                dynamicData.append(encodedValue);
                dynamicDataOffset += encodedValue.length() >> 1;
            } else {
                result.append(encodedValue);
            }
        }
        result.append(dynamicData);

        return result.toString();
    }

    /**
     * Decodes a ABI encoded parameter.
     * @param solidityType A solidity type string.
     * @param encoded The ABI byte code to decode
     * @return Type
     * @throws ClassNotFoundException
     */
    public static Type decodeParameter(String solidityType, String encoded) throws ClassNotFoundException {
        return decodeParameters(Arrays.asList(solidityType), encoded).get(0);
    }

    /**
     * Decodes a ABI encoded parameters.
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
     * Decodes a ABI encoded parameters.
     * @param method A ContractMethod instance.
     * @param encoded The ABI byte code to decoed
     * @return List
     * @throws ClassNotFoundException
     */
    public static List<Type> decodeParameters(ContractMethod method, String encoded) throws ClassNotFoundException {
        List<TypeReference<Type>> resultParams = new ArrayList<>();

        for(ContractIOType ioType: method.getOutputs()) {
            resultParams.add(TypeReference.makeTypeReference(ioType.getType()));
        }

        return FunctionReturnDecoder.decode(encoded, resultParams);
    }

    /**
     * Decodes a ABI-encoded log data and indexed topic data
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
                indexedList.add(TypeReference.makeTypeReference(input.getType()));
            } else {
                nonIndexedList.add(TypeReference.makeTypeReference(input.getType()));
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

    private static int getLength(List<Type> parameters) {
        int count = 0;
        for (Type type:parameters) {
            if (type instanceof StaticArray) {
                count += ((StaticArray) type).getValue().size();
            } else {
                count++;
            }
        }
        return count;
    }

    private static boolean isDynamic(Type parameter) {
        return parameter instanceof DynamicBytes
                || parameter instanceof Utf8String
                || parameter instanceof DynamicArray;
    }
}
