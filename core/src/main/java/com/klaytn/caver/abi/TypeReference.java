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
 * This file is derived from web3j/abi/src/main/java/org/web3j/abi/TypeReference.java (2021/04/05).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.abi;

import com.klaytn.caver.abi.datatypes.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Type wrapper to get around limitations of Java's type erasure. This is so that we can pass around
 * Typed {@link com.klaytn.caver.abi.datatypes.Array} types.
 *
 * <p>See <a href="http://gafter.blogspot.com.au/2006/12/super-type-tokens.html">this blog post</a>
 * for further details.
 *
 * <p>It may make sense to switch to using Java's reflection <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html">Type</a> to avoid
 * working around this fundamental generics limitation.
 */
public abstract class TypeReference<T extends com.klaytn.caver.abi.datatypes.Type>
        implements Comparable<TypeReference<T>> {
    protected static Pattern ARRAY_SUFFIX = Pattern.compile("\\[(\\d*)]");

    private final Type type;
    private final boolean indexed;

    protected TypeReference() {
        this(false);
    }

    protected TypeReference(boolean indexed) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        this.indexed = indexed;
    }

    /**
     * getSubTypeReference() is used by instantiateType to see what TypeReference is wrapped by this
     * one. eg calling getSubTypeReference() on a TypeReference to
     * DynamicArray[StaticArray3[Uint256]] would return a TypeReference to StaticArray3[Uint256]
     *
     * @return the type wrapped by this Array TypeReference, or null if not Array
     */
    TypeReference getSubTypeReference() {
        return null;
    }

    public int compareTo(TypeReference<T> o) {
        // taken from the blog post comments - this results in an errror if the
        // type parameter is left out.
        return 0;
    }

    public Type getType() {
        return type;
    }

    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Workaround to ensure type does not come back as T due to erasure, this enables you to create
     * a TypeReference via {@link Class Class&lt;T&gt;}.
     *
     * @return the parameterized Class type if applicable, otherwise a regular class
     * @throws ClassNotFoundException if the class type cannot be determined
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClassType() throws ClassNotFoundException {
        Type clsType = getType();

        if (getType() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) clsType).getRawType();
        } else {
            return (Class<T>) Class.forName(clsType.getTypeName());
        }
    }

    public static <T extends com.klaytn.caver.abi.datatypes.Type> TypeReference<T> create(Class<T> cls) {
        return create(cls, false);
    }

    public static <T extends com.klaytn.caver.abi.datatypes.Type> TypeReference<T> create(
            Class<T> cls, boolean indexed) {
        return new TypeReference<T>(indexed) {
            public Type getType() {
                return cls;
            }
        };
    }

    /**
     * This is a helper method that only works for atomic types (uint, bytes, etc). Array types must
     * be wrapped by a {@link ParameterizedType}.
     *
     * @param solidityType the solidity as a string eg Address Int
     * @param primitives is it a primitive type
     * @return returns
     * @throws ClassNotFoundException when the class cannot be found.
     */
    protected static Class<? extends com.klaytn.caver.abi.datatypes.Type> getAtomicTypeClass(
            String solidityType, boolean primitives) throws ClassNotFoundException {

        if (ARRAY_SUFFIX.matcher(solidityType).find()) {
            throw new ClassNotFoundException(
                    "getAtomicTypeClass does not work with array types."
                            + " See makeTypeReference()");
        } else {
            return AbiTypes.getType(solidityType, primitives);
        }
    }

    /**
     * Type wrapper class for Struct type(StaticStruct, DynamicStruct).
     */
    public abstract static class StructTypeReference<T extends com.klaytn.caver.abi.datatypes.Type>
            extends TypeReference<T> {
        /**
         * A List of TypeReference wrapped a field in struct type.
         */
        private List<TypeReference> typeList;

        /**
         * Create a StructTypeReference instance.
         * @param typeList A List of TypeReference wrapped a field in struct type.
         */
        public StructTypeReference(List<TypeReference> typeList) {
            this.typeList = typeList;
        }

        /**
         * Getter for typeList
         * @return A List of TypeReference wrapped a field in struct type.
         */
        public List<TypeReference> getTypeList() {
            return typeList;
        }
    }

    /**
     * Type wrapper class for static array type.
     */
    public abstract static class StaticArrayTypeReference<T extends com.klaytn.caver.abi.datatypes.Type>
            extends TypeReference<T> {

        /**
         * A size of array
         */
        private final int size;

        /**
         * Create StaticArrayTypeReference instance.
         * @param size A size of array
         */
        protected StaticArrayTypeReference(int size) {
            this.size = size;
        }

        /**
         * Getter for size
         * @return A size of array
         */
        public int getSize() {
            return size;
        }
    }

    /**
     * Create a TypeReference instance using solidity type string.
     * @param solidityType A solidity type string to make TypeReference.
     * @return TypeReference
     * @throws ClassNotFoundException
     */
    public static TypeReference makeTypeReference(String solidityType)
            throws ClassNotFoundException {
        return makeTypeReference(solidityType, false, false);
    }

    /**
     * Create a TypeReference instance using solidity type string.
     * @param solidityType A solidity type string to make TypeReference.
     * @param indexed true if the field is part of the log's topics, false if it one of the logs data.
     * @param primitives If true, a TypeReference instance is created with a type representing the range of the passed integer type.
     * @return TypeReference
     * @throws ClassNotFoundException
     */
    public static TypeReference makeTypeReference(
            String solidityType, final boolean indexed, final boolean primitives)
            throws ClassNotFoundException {

        // Check a solidityType string whether a atomic type or array type.
        // The atomic type is a type except an array.
        if (isAtomicTypeString(solidityType)) {
            //Struct type.
            if(solidityType.contains("tuple")) {
                return makeStructTypeReference(solidityType);
            }
            //Others.
            else {
                final Class<? extends com.klaytn.caver.abi.datatypes.Type> typeClass =
                        getAtomicTypeClass(solidityType, primitives);
                return create(typeClass, indexed);
            }
        }

        // Make a TypeReference instance of Array type.
        Matcher nextSquareBrackets = ARRAY_SUFFIX.matcher(solidityType);
        nextSquareBrackets.find();
        int lastReadStringPosition = nextSquareBrackets.start();

        // Make a TypeReference instance that element type of array.
        TypeReference arrayWrappedType;
        if(solidityType.contains("tuple")) {
            int index = solidityType.lastIndexOf(')');
            String baseType = solidityType.substring(0, index + 1);
            arrayWrappedType = makeStructTypeReference(baseType);

            lastReadStringPosition = index + 1;
            nextSquareBrackets.find(lastReadStringPosition);
        } else {
            String baseType = solidityType.substring(0, lastReadStringPosition);
            final Class<? extends com.klaytn.caver.abi.datatypes.Type> baseClass =
                    getAtomicTypeClass(baseType, primitives);

            arrayWrappedType = create(baseClass, indexed);
        }

        final int len = solidityType.length();
        // for each [\d*], wrap the previous TypeReference in an array
        while (lastReadStringPosition < len) {
            String arraySize = nextSquareBrackets.group(1);
            final TypeReference baseTr = arrayWrappedType;
            //If an arraySize has null or "", it makes a Dynamic array.
            if (arraySize == null || arraySize.equals("")) {
                arrayWrappedType =
                        new TypeReference<DynamicArray>(indexed) {
                            @Override
                            TypeReference getSubTypeReference() {
                                return baseTr;
                            }

                            @Override
                            public Type getType() {
                                return new ParameterizedType() {
                                    @Override
                                    public Type[] getActualTypeArguments() {
                                        return new Type[] {baseTr.getType()};
                                    }

                                    @Override
                                    public Type getRawType() {
                                        return DynamicArray.class;
                                    }

                                    @Override
                                    public Type getOwnerType() {
                                        return Class.class;
                                    }
                                };
                            }
                        };
            } else {
                //If a arraySize existed, it makes a Static array.
                final Class arrayclass;
                int arraySizeInt = Integer.parseInt(arraySize);
                if (arraySizeInt <= StaticArray.MAX_SIZE_OF_STATIC_ARRAY) {
                    arrayclass =
                            Class.forName(
                                    "com.klaytn.caver.abi.datatypes.generated.StaticArray" + arraySize);
                } else {
                    arrayclass = StaticArray.class;
                }
                arrayWrappedType =
                        new StaticArrayTypeReference<StaticArray>(arraySizeInt) {

                            @Override
                            TypeReference getSubTypeReference() {
                                return baseTr;
                            }

                            @Override
                            public boolean isIndexed() {
                                return indexed;
                            }

                            @Override
                            public Type getType() {
                                return new ParameterizedType() {
                                    @Override
                                    public Type[] getActualTypeArguments() {
                                        return new Type[] {baseTr.getType()};
                                    }

                                    @Override
                                    public Type getRawType() {
                                        return arrayclass;
                                    }

                                    @Override
                                    public Type getOwnerType() {
                                        return Class.class;
                                    }
                                };
                            }
                        };
            }

            //find a next [] brackets.
            lastReadStringPosition = nextSquareBrackets.end();
            nextSquareBrackets = ARRAY_SUFFIX.matcher(solidityType);
            // cant find any more [] and string isn't fully parsed
            if (!nextSquareBrackets.find(lastReadStringPosition) && lastReadStringPosition != len) {
                throw new ClassNotFoundException(
                        "Unable to make TypeReference from " + solidityType);
            }
        }
        return arrayWrappedType;
    }

    /**
     * Creates a StructTypeReference instance using tupleString.
     * @param tupleString A tuple type string
     * @return StructTypeReference
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static TypeReference makeStructTypeReference(String tupleString) throws ClassNotFoundException {
        List<TypeReference> typeReferences = new ArrayList<>();

        //Split a component.
        //if a tuple string has a "tuple(string,bool,string,uint256)"
        //it parsed a component as a "string", "bool", "string", "uint256" and created a list instance.
        List<String> components = splitComponent(tupleString);

        boolean hasDynamic = false;
        for(int i=0; i<components.size(); i++) {
            TypeReference reference = TypeReference.makeTypeReference(components.get(i));
            typeReferences.add(reference);

            if(TypeDecoder.isDynamic(reference)) {
                hasDynamic = true;
            }
        }

        if(hasDynamic) {
            return new StructTypeReference<DynamicStruct>(typeReferences) {
                @Override
                public Type getType() {
                    return DynamicStruct.class;
                }
            };
        } else {
            return new StructTypeReference<StaticStruct>(typeReferences) {
                @Override
                public Type getType() {
                    return StaticStruct.class;
                }
            };
        }
    }

    private static List<String> splitComponent(String tupleString) {
        //set a component string.
        //tuple(string,tuple(string,string))
        //string,tuple(string,string)
        String component = tupleString.substring(6, tupleString.length()-1);

        List<String> array = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for(int i=0; i < component.length(); i++) {
            char a = component.charAt(i);

            if(a == 't' && component.startsWith("tuple", i)) {
                int endIndex = findTupleEndIndex(component, i);
                builder.append(component.substring(i, endIndex+1));

                i = endIndex;
            } else if(a == ',') {
                array.add(builder.toString());
                builder.setLength(0);
            } else {
                builder.append(a);
            }

            if(i == component.length()-1) {
                array.add(builder.toString());
                builder.setLength(0);
            }
        }

        return array;
    }

    private static int findTupleEndIndex(String subString, int startIndex) {
        int depth = 0;
        int endIndex = 0;

        for(int i = startIndex; i < subString.length(); i++) {
            char a = subString.charAt(i);
            if(a == '(') {
                depth++;
            } else if(a == ')') {
                depth--;

                if(depth == 0) {
                    endIndex = i;
                    break;
                }
            }
        }

        return endIndex;
    }

    private static boolean isAtomicTypeString(String solidityType) {
        //Check that the beginning of the string is `tuple`, and the last character is `]`.
        boolean isTupleArray = solidityType.startsWith("tuple") && (solidityType.charAt(solidityType.length() - 1) == ']');
        Matcher nextSquareBrackets = ARRAY_SUFFIX.matcher(solidityType);

        if(!nextSquareBrackets.find()) {
            return true;
        }
        //If a solidityType string has a tuple(uint256[],bool), It should return a true.
        //uint256[] is just one of components in tuple.
        else {
            if(solidityType.startsWith("tuple") && !isTupleArray) {
                return true;
            }
            return false;
        }
    }
}
