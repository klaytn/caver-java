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

package com.klaytn.caver.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.CommonTestData;
import com.klaytn.caver.utils.model.*;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

@Ignore
@RunWith(Parameterized.class)
public class UtilsTest {
    public static final String CAVER_CONFORMANCE_TESTS = "caver-conformance-tests";
    public static final String LAYER_NAME = "Utils";
    public static final String TEST_CLASS_NAME = "Utils";
    public static final String FULL_CLASS_NAME = "com.klaytn.caver.utils.Utils";

    public static final String JSON_POSTFIX = ".json";

    static Map<String, List<CommonTestData>> utilsTestSuite;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public String funcName;
    public String id;
    public CommonTestData testData;


    public UtilsTest(String funcName, String id, CommonTestData testData) {
        this.funcName = funcName;
        this.id = id;
        this.testData = testData;
    }

    static Map loadTestData() throws URISyntaxException, IOException {
        URL fileURL = UtilsTest.class.getClassLoader().getResource(CAVER_CONFORMANCE_TESTS + File.separator + LAYER_NAME + File.separator + TEST_CLASS_NAME);
        File dir = new File(fileURL.toURI());

        Map<String, CommonTestData<?, ?>> map = new HashMap<>();

        for(File file: Objects.requireNonNull(dir.listFiles())) {
            map.putAll(loadMethodTestData(file));
        }
        return map;
    }

    //generate test report form - methodName : Test ID
    @Parameterized.Parameters(name = "{0}: {1}")
    public static Collection<Object[]> parameterizeTestData() throws IOException, URISyntaxException {
        List<Object[]> dataSuite = new ArrayList<>();
        utilsTestSuite = loadTestData();

        for(String methodName: utilsTestSuite.keySet()) {
            List<CommonTestData> testData = utilsTestSuite.get(methodName);
            List<Object[]> list =  testData.stream().map(data -> {
                //These returned data sets each UtilsTest's funcName, id, testData field.
                return new Object[] {methodName, data.getId(), data};
            }).collect(Collectors.toList());

            dataSuite.addAll(list);
        }

        return dataSuite;
    }

    @Test
    public void test() throws Throwable {
        assumeThat(testData.isSkipJava(), is(false));

        if(testData.getExpectedResult().getStatus().equals("fail")) {
            expectedException.expectMessage(testData.getExpectedResult().getErrorMessage());
        }

        try {
            Object result = invokeMethod(this.funcName, FULL_CLASS_NAME, null, this.testData);

            if(testData.getExpectedResult().getStatus().equals("pass")) {
                if(this.funcName.equals("parseKlaytnWalletKey")) {
                    List list = Arrays.asList((String[])result);
                    assertEquals(this.testData.getExpectedResult().getOutput(), list);
                } else if(this.funcName.equals("decodeSignature")) {
                    Map<String, String> temp = (Map<String, String>)testData.getExpectedResult().getOutput();
                    SignatureData expected = new SignatureData(temp.get("v"), temp.get("r"), temp.get("s"));
                    assertEquals(expected, result);
                } else if(this.funcName.equals("publicKeyToAddress") || this.funcName.equals("recover")) {
                    String expectedAddress = (String)this.testData.getExpectedResult().getOutput();
                    assertEquals(expectedAddress.toLowerCase(Locale.ROOT), result);
                } else {
                    assertEquals(this.testData.getExpectedResult().getOutput(), result);
                }
            }
        } catch(InvocationTargetException e) {
            //If method that executing through reflection throws exception, it encapsulated with InvocationTargetException instance.
            //So it needs to rethrow an exception.
            throw e.getTargetException();
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    static Map loadMethodTestData(File file) throws IOException {
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        Map data = new HashMap();

        int lastIndex = file.getName().indexOf(JSON_POSTFIX);
        String fileName = file.getName().substring(0, lastIndex);

        switch(fileName) {
            case "addHexPrefix":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<HexString, String>>>>() {});
                break;
            case "checkAddressChecksum":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Address, Boolean>>>>() {});
                break;
            case "compressPublicKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, String>>>>() {});
                break;
            case "convertFromPeb":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<ConvertPeb, String>>>>() {});
                break;
            case "convertToPeb":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<ConvertPeb, String>>>>() {});
                break;
            case "decodeSignature":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<RawSig, Object>>>>() {});
                break;
            case "decompressPublicKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, String>>>>() {});
                break;
            case "hashMessage":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Message, String>>>>() {});
                break;
            case "isAddress":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Address, Boolean>>>>() {});
                break;
            case "isEmptySig":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Signature, Boolean>>>>() {});
                break;
            case "isHex":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<HexString, Boolean>>>>() {});
                break;
            case "isHexStrict":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<HexString, Boolean>>>>() {});
                break;
            case "isKlaytnWalletKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, Boolean>>>>() {});
                break;
            case "isValidPrivateKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, Boolean>>>>() {});
                break;
            case "isValidPublicKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, Boolean>>>>() {});
                break;
            case "parseKlaytnWalletKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, List<String>>>>>() {});
                break;
            case "publicKeyToAddress":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Key, String>>>>() {});
                break;
            case "recover":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Recover, String>>>>() {});
                break;
            case "recoverPublicKey":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<Recover, String>>>>() {});
                break;
            case "stripHexPrefix":
                data = mapper.readValue(file, new TypeReference<Map<String, List<CommonTestData<HexString, Boolean>>>>() {});
                break;
            default:
                break;
        }

        return data;
    }


    private Object invokeMethod(String functionName, String className, Object instance, CommonTestData data) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class clazz = Class.forName(className);
        Method method = clazz.getMethod(functionName, data.getInput().getInputTypeArray());

        Object result = method.invoke(instance, data.getInput().getInputArray());
        return result;
    }
}
