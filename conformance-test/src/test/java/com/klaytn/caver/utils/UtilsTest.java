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
import com.klaytn.caver.utils.model.*;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class UtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public static final String JSON_POSTFIX = ".json";
    public static final String CAVER_CONFORMANCE_TESTS = "caver-conformance-tests";
    public static final String LAYER_NAME = "Utils";
    public static final String CLASS_NAME = "Utils";

    static Map<String, List<CommonTestData>> map;

    public String id;
    public String description;
    public CommonTestData testData;
    public String funcName;

    public UtilsTest(String funcName, String id, String description, CommonTestData testData) {
        this.funcName = funcName;
        this.id = id;
        this.description = description;
        this.testData = testData;
    }

    public static Map loadFromDirectories(File dir) throws IOException {
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        Map<String, CommonTestData<?, ?>> map = new HashMap<>();

        for(File file: dir.listFiles()) {
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

            map.putAll(data);
        }
        return map;
    }

    @Parameterized.Parameters(name = "{0}: {1}")
    public static Collection<Object[]> data() throws IOException, URISyntaxException {
        URL fileURL = UtilsTest.class.getClassLoader().getResource(CAVER_CONFORMANCE_TESTS + File.separator + LAYER_NAME + File.separator + CLASS_NAME);
        File dir = new File(fileURL.toURI());

        map = loadFromDirectories(dir);
        List<Object[]> dataSuite = new ArrayList<>();

        for(String key: map.keySet()) {
            List<CommonTestData> testData = map.get(key);
            List<Object[]> list =  testData.stream().map(data -> {
                return new Object[] {key, data.getId(), data.getDescription(), data};
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
            Class myClass = Class.forName("com.klaytn.caver.utils.Utils");
            Method method = myClass.getMethod(this.funcName, testData.getInput().getInputTypeArray());

            Object result = method.invoke(myClass.newInstance(), testData.getInput().getInputArray());

            if(!testData.getExpectedResult().getStatus().equals("fail")) {
                if(this.funcName.equals("parseKlaytnWalletKey")) {
                    String[] strResult = (String[])result;
                    List list = Arrays.asList(strResult);
                    assertEquals(this.testData.getExpectedResult().getOutput(), list);
                } else if(this.funcName.equals("decodeSignature")) {
                    Map<String, String> temp = (Map<String, String>)testData.getExpectedResult().getOutput();
                    SignatureData expected = new SignatureData(temp.get("v"), temp.get("r"), temp.get("s"));

                    assertEquals(expected, result);
                } else {
                    assertEquals(this.testData.getExpectedResult().getOutput(), result);
                }

            }
        } catch(InvocationTargetException e) {
            throw e.getTargetException();
        }

    }


}
