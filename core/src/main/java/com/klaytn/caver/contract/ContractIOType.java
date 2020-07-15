package com.klaytn.caver.contract;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.generated.AbiTypes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonDeserialize(using = ContractIOType.ContractIOTypeDeserializer.class)
public class ContractIOType {
    String name;
    String type;
    String javaType;
    boolean indexed;

    public ContractIOType() {
    }

    public ContractIOType(String name, String type, boolean indexed) {
        this.name = name;
        this.type = type;
        this.indexed = indexed;

        this.javaType = buildTypeName(type);
    }

    private static String buildTypeName(String typeDeclaration) {
        String regex = "(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?";
        Pattern pattern = Pattern.compile(regex);

        String type = trimStorageDeclaration(typeDeclaration);
        Matcher matcher = pattern.matcher(type);
        if (matcher.find()) {
            Class<?> baseType = AbiTypes.getType(matcher.group(1));
            String firstArrayDimension = matcher.group(2);
            String secondArrayDimension = matcher.group(3);

            TypeName typeName;

            if ("".equals(firstArrayDimension)) {
                typeName = ParameterizedTypeName.get(DynamicArray.class, baseType);
            } else {
                Class<?> rawType = getStaticArrayTypeReferenceClass(firstArrayDimension);
                typeName = ParameterizedTypeName.get(rawType, baseType);
            }

            if (secondArrayDimension != null) {
                if ("".equals(secondArrayDimension)) {
                    return ParameterizedTypeName.get(ClassName.get(DynamicArray.class), typeName).toString();
                } else {
                    Class<?> rawType = getStaticArrayTypeReferenceClass(secondArrayDimension);
                    return ParameterizedTypeName.get(ClassName.get(rawType), typeName).toString();
                }
            }

            return typeName.toString();
        } else {
            Class<?> cls = AbiTypes.getType(type);
            return ClassName.get(cls).toString();
        }
    }

    private static Class<?> getStaticArrayTypeReferenceClass(String type) {
        try {
            return Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + type);
        } catch (ClassNotFoundException e) {
            // Unfortunately we can't encode it's length as a type if it's > 32.
            return StaticArray.class;
        }
    }

    private static String trimStorageDeclaration(String type) {
        if (type.endsWith(" storage") || type.endsWith(" memory")) {
            return type.split(" ")[0];
        } else {
            return type;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public static class ContractIOTypeDeserializer extends JsonDeserializer<ContractIOType> {
        @Override
        public ContractIOType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode root = p.getCodec().readTree(p);

            String type = root.get("type").asText();
            String name = root.get("name").asText();
            boolean indexed = false;

            if(root.hasNonNull("indexed")) {
                indexed = root.get("indexed").asBoolean();
            }
            return new ContractIOType(name, type, indexed);
        }
    }
}
