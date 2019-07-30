/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/codegen/src/test/java/org/web3j/codegen/SolidityFunctionWrapperGeneratorTest.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.codegen;

import org.junit.Test;
import org.web3j.utils.Strings;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class SolidityFunctionWrapperGeneratorTest extends TempFileProvider {

    private String solidityBaseDir;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        URL url = SolidityFunctionWrapperGeneratorTest.class.getResource("/solidity");
        solidityBaseDir = url.getPath();
    }

    @Test
    public void testGetFileNoExtension() {
        assertThat(FunctionWrapperGenerator.getFileNameNoExtension(""), is(""));
        assertThat(FunctionWrapperGenerator.getFileNameNoExtension("file"), is("file"));
        assertThat(FunctionWrapperGenerator.getFileNameNoExtension("file."), is("file"));
        assertThat(FunctionWrapperGenerator.getFileNameNoExtension("file.txt"), is("file"));
    }

    @Test
    public void testGreeterGeneration() throws Exception {
        testCodeGenerationJvmTypes("greeter", "Greeter");
        testCodeGenerationSolidityTypes("greeter", "Greeter");
    }

    @Test
    public void testHumanStandardTokenGeneration() throws Exception {
        testCodeGenerationJvmTypes("contracts", "HumanStandardToken");
        testCodeGenerationSolidityTypes("contracts", "HumanStandardToken");
    }

    @Test
    public void testSimpleStorageGeneration() throws Exception {
        testCodeGenerationJvmTypes("simplestorage", "SimpleStorage");
        testCodeGenerationSolidityTypes("simplestorage", "SimpleStorage");
    }

    @Test
    public void testFibonacciGeneration() throws Exception {
        testCodeGenerationJvmTypes("fibonacci", "Fibonacci");
        testCodeGenerationSolidityTypes("fibonacci", "Fibonacci");
    }

    @Test
    public void testArrays() throws Exception {
        testCodeGenerationJvmTypes("arrays", "Arrays");
        testCodeGenerationSolidityTypes("arrays", "Arrays");
    }

    @Test
    public void testShipIt() throws Exception {
        testCodeGenerationJvmTypes("shipit", "ShipIt");
        testCodeGenerationSolidityTypes("shipit", "ShipIt");
    }

    @Test
    public void testMisc() throws Exception {
        testCodeGenerationJvmTypes("misc", "Misc");
        testCodeGenerationSolidityTypes("misc", "Misc");
    }

    @Test
    public void testContractsNoBin() throws Exception {
        testCodeGeneration("contracts", "HumanStandardToken", FunctionWrapperGenerator.JAVA_TYPES_ARG, false);
        testCodeGeneration("contracts", "HumanStandardToken", FunctionWrapperGenerator.SOLIDITY_TYPES_ARG, false);
    }

    @Test
    public void testGenerationCommandPrefixes() throws Exception {
        testCodeGeneration(Arrays.asList(SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY, SolidityFunctionWrapperGenerator.COMMAND_GENERATE),
                "contracts", "HumanStandardToken", FunctionWrapperGenerator.JAVA_TYPES_ARG, true);
        testCodeGeneration(Arrays.asList(SolidityFunctionWrapperGenerator.COMMAND_GENERATE),
                "contracts", "HumanStandardToken", FunctionWrapperGenerator.SOLIDITY_TYPES_ARG, true);
    }



    private void testCodeGenerationJvmTypes(
            String contractName, String inputFileName) throws Exception {
        testCodeGeneration(contractName, inputFileName, FunctionWrapperGenerator.JAVA_TYPES_ARG, true);
    }

    private void testCodeGenerationSolidityTypes(
            String contractName, String inputFileName) throws Exception {
        testCodeGeneration(contractName, inputFileName, FunctionWrapperGenerator.SOLIDITY_TYPES_ARG, true);
    }

    private void testCodeGeneration(String contractName, String inputFileName,
                                    String types, boolean useBin) throws Exception {
        testCodeGeneration(emptyList(), contractName, inputFileName, types, useBin);
    }

    private void testCodeGeneration(List<String> prefixes,
            String contractName, String inputFileName, String types, boolean useBin)
            throws Exception {
        String packageName = null;
        if (types.equals(FunctionWrapperGenerator.JAVA_TYPES_ARG)) {
            packageName = "com.klaytn.caver.generated.java";
        } else if (types.equals(FunctionWrapperGenerator.SOLIDITY_TYPES_ARG)) {
            packageName = "com.klaytn.caver.generated.solidity";
        }

        List<String> options = new ArrayList<>();
        options.addAll(prefixes);
        options.add(types);
        if (useBin) {
            options.add("-b");
            options.add(solidityBaseDir + File.separator + contractName + File.separator
                    + "result" + File.separator + inputFileName + ".bin");
        }
        options.add("-a");
        options.add(solidityBaseDir + File.separator + contractName + File.separator
                + "result" + File.separator + inputFileName + ".abi");
        options.add("-p");
        options.add(packageName);
        options.add("-o");
        options.add(tempDirPath);

        SolidityFunctionWrapperGenerator.main(options.toArray(new String[options.size()]));

        verifyGeneratedCode(tempDirPath + File.separator
                + packageName.replace('.', File.separatorChar) + File.separator
                + Strings.capitaliseFirstLetter(inputFileName) + ".java");
    }

    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                     compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, null, null, compilationUnits);
            boolean result = task.call();

            System.out.println(diagnostics.getDiagnostics());
            assertTrue("Generated contract contains compile time error", result);
        }
    }
}
