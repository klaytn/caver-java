package com.klaytn.caver.abi;

import com.klaytn.caver.contract.ContractEvent;
import com.klaytn.caver.contract.ContractIOType;
import com.klaytn.caver.contract.ContractMethod;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ABI {
    public static String encodeFunctionCall(ContractMethod method, List<Type> params) {
        String methodId = encodeFunctionSignature(method);
        String encodedParams = encodeParameters(params);

        return methodId + encodedParams;
    }

    public static String encodeFunctionCall(String functionSig, List<Type> params) {
        String methodId = encodeFunctionSignature(functionSig);
        String encodedParams = encodeParameters(params);

        return methodId + encodedParams;
    }

    public static String encodeFunctionSignature(ContractMethod method) {
        return encodeFunctionSignature(buildFunctionSignature(method));
    }

    public static String encodeFunctionSignature(String functionSig) {
        byte[] input = functionSig.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    public static String buildFunctionSignature(ContractMethod method) {
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

    public static String encodeEventSignature(ContractEvent event) {
        return encodeEventSignature(buildEventSignature(event));
    }

    public static String encodeEventSignature(String eventSig) {
        byte[] input = eventSig.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }

    public static String buildEventSignature(ContractEvent method) {
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

    public static String encodeParameter(Type parameter) {
        return encodeParameters(Arrays.asList(parameter));
    }

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

    public static Type decodeParameter(String solidityType, String encoded) throws ClassNotFoundException {
        String web3Type = ContractIOType.buildTypeName(solidityType);

        return decodeParameters(Arrays.asList(web3Type), encoded).get(0);
    }

    public static List<Type> decodeParameters(List<String> solidityTypeList, String encoded) throws ClassNotFoundException {
        List<TypeReference<Type>> params = new ArrayList<>();

        for(String solType : solidityTypeList) {
            Class web3TypeClass = Class.forName(ContractIOType.buildTypeName(solType));
            params.add(TypeReference.create(web3TypeClass));
        }

        return FunctionReturnDecoder.decode(encoded, params);
    }

    public static List<Type> decodeParameters(ContractMethod method, String encoded) throws ClassNotFoundException {
        List<TypeReference<Type>> resultParams = new ArrayList<>();

        for(ContractIOType ioType: method.getOutputs()) {
            Class cls = Class.forName(ioType.getJavaType());
            resultParams.add(TypeReference.create(cls));
        }

        return FunctionReturnDecoder.decode(encoded, resultParams);
    }

    public static EventValues decodeLog(List<ContractIOType> inputs, String data, List<String> topics) throws ClassNotFoundException {
        List<TypeReference<Type>> indexedList = new ArrayList<>();
        List<TypeReference<Type>> nonIndexedList = new ArrayList<>();

        for(ContractIOType input: inputs) {
            Class cls = Class.forName(input.getJavaType());
            if(input.isIndexed()) {
                indexedList.add(TypeReference.create(cls));
            } else {
                nonIndexedList.add(TypeReference.create(cls));
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
