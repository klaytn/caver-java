/*
 * Modifications copyright 2021 The caver-java Authors
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is derived from web3j/abi/src/main/java/org/web3j/abi/DefaultFunctionReturnDecoder.java (2021/04/05).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.abi;

import com.klaytn.caver.abi.datatypes.*;
import com.klaytn.caver.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.klaytn.caver.abi.TypeDecoder.MAX_BYTE_LENGTH_FOR_HEX_STRING;
import static com.klaytn.caver.abi.TypeDecoder.isDynamic;
import static com.klaytn.caver.abi.Utils.*;


/**
 * Contract Application Binary Interface (ABI) encoding for functions. Further details are
 * available <a href="https://docs.soliditylang.org/en/latest/abi-spec.html">here</a>.
 */
public class DefaultFunctionReturnDecoder extends FunctionReturnDecoder {

    public List<Type> decodeFunctionResult(
            String rawInput, List<TypeReference<Type>> outputParameters) {

        String input = Numeric.cleanHexPrefix(rawInput);

        if (Strings.isEmpty(input)) {
            return Collections.emptyList();
        } else {
            return build(input, outputParameters);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Type> Type decodeEventParameter(
            String rawInput, TypeReference<T> typeReference) {

        String input = Numeric.cleanHexPrefix(rawInput);

        try {
            Class<T> type = typeReference.getClassType();

            if (Bytes.class.isAssignableFrom(type)) {
                Class<Bytes> bytesClass = (Class<Bytes>) Class.forName(type.getName());
                return TypeDecoder.decodeBytes(input, bytesClass);
            } else if (Array.class.isAssignableFrom(type)
                    || BytesType.class.isAssignableFrom(type)
                    || Utf8String.class.isAssignableFrom(type)) {
                return TypeDecoder.decodeBytes(input, Bytes32.class);
            } else {
                return TypeDecoder.decode(input, type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    private static List<Type> build(String input, List<TypeReference<Type>> outputParameters) {
        List<Type> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (TypeReference<?> typeReference : outputParameters) {
            try {
                int hexStringDataOffset = getDataOffset(input, offset, typeReference);

                @SuppressWarnings("unchecked")
                Class<Type> classType = (Class<Type>) typeReference.getClassType();

                Type result;
                if (DynamicStruct.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeDynamicStruct(input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;

                } else if (DynamicArray.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeDynamicArray(input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;

                } else if (typeReference instanceof TypeReference.StaticArrayTypeReference) {
                    int length = ((TypeReference.StaticArrayTypeReference) typeReference).getSize();
                    result =
                            TypeDecoder.decodeStaticArray(input, hexStringDataOffset, typeReference, length);
                    if(isDynamic(typeReference)) {
                        offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                    } else {
                        offset += getStaticArrayElementSize((TypeReference.StaticArrayTypeReference)typeReference) * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                    }
                } else if (StaticStruct.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeStaticStruct(input, hexStringDataOffset, typeReference);
                    offset +=
                        getStaticStructComponentSize((TypeReference.StructTypeReference) typeReference)
                            * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else {
                    result = TypeDecoder.decode(input, hexStringDataOffset, classType);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                }
                results.add(result);

            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("Invalid class reference provided", e);
            }
        }
        return results;
    }

    public static <T extends Type> int getDataOffset(
            String input, int offset, TypeReference<?> typeReference)
            throws ClassNotFoundException {
        if(isDynamic(typeReference)){
            return TypeDecoder.decodeUintAsInt(input, offset) << 1;
        } else {
            return offset;
        }
    }
}
