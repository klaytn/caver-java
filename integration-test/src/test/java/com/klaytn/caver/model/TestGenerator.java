/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.model.dto.*;
import com.klaytn.caver.model.executor.ApiExecutor;
import com.klaytn.caver.model.executor.ContractExecutor;
import com.klaytn.caver.model.executor.Executor;
import com.klaytn.caver.model.executor.TransactionExecutor;
import com.klaytn.caver.model.validator.ApiValidator;
import com.klaytn.caver.model.validator.ContractValidator;
import com.klaytn.caver.model.validator.TransactionValidator;
import com.klaytn.caver.model.validator.Validator;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class TestGenerator {
    public static final String JSON_POSTFIX = ".json";
    public static final String KLAYTN_INTEGRATION_TESTS = "klaytn-integration-tests";
    public static final String ENV_FILE = "env.template" + JSON_POSTFIX;

    private ObjectMapper objectMapper
            = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Env env;
    private File[] filesToRead;

    private TestGenerator(File envFile, File[] filesToRead) throws Exception {
        this.filesToRead = filesToRead;
        this.env = readEnv(envFile);
    }

    private Env readEnv(File envFile) throws Exception {
        if (envFile == null) {
            URL envUrl = getClass().getClassLoader().getResource(KLAYTN_INTEGRATION_TESTS + File.separator + ENV_FILE);
            return objectMapper.readValue(envUrl, Env.class);
        }
        return objectMapper.readValue(envFile, Env.class);
    }

    private TestData readTestData(File file, VariableStorage variableStorage) throws Exception {
        System.out.printf("\n\n========= reading %s =========\n", file.getName());
        StringReplacer stringReplacer = new StringReplacer(file, env);
        stringReplacer.configure(variableStorage);
        String replacedContent = stringReplacer.replaceAll();
        return objectMapper.readValue(replacedContent, TestData.class);
    }

    private void configure(TestComponent testComponent) {
        if (testComponent.getType().equals(TestComponent.Type.TRANSACTION)) {
            ((Transaction) testComponent).configure(
                    KlayCredentials.create(env.getSender().getPrivateKey(), env.getSender().getAddress()),
                    KlayCredentials.create(env.getFeePayer().getPrivateKey(), env.getFeePayer().getAddress())
            );
        }
    }

    public void generateAll() throws Exception {
        for (File file : filesToRead) {
            generateOne(file);
        }
    }

    private void generateOne(File file) throws Exception {
        VariableStorage variableStorage = new VariableStorage();
        TestData testData = readTestData(file, variableStorage);
        System.out.printf("\n========= %s %s started =========\n\n", testData.getTcID(), testData.getTcName());
        executeAll(testData, variableStorage);
        System.out.printf("\n\n========= %s %s finished =========\n\n", testData.getTcID(), testData.getTcName());
    }

    private void executeAll(TestData testData, VariableStorage variableStorage) throws Exception {
        for (TestComponent testComponent : testData.getTest()) {
            configure(testComponent);

            Executor executor = getExecutor(testComponent, env.getURL());
            executor.configure(variableStorage);
            Object response = executor.execute();

            Validator validator = getValidate(executor);
            validator.configure(variableStorage);
            validator.validate(response);
        }
    }

    private Validator getValidate(Executor executor) {
        if (executor.getType().equals(TestComponent.Type.API)) {
            return new ApiValidator(
                    ((ApiExecutor) executor).getExpected(),
                    ((ApiExecutor) executor).getErrorString(),
                    ((ApiExecutor) executor).getApi().getMethod()
            );
        }
        if (executor.getType().equals(TestComponent.Type.TRANSACTION)) {
            return new TransactionValidator(((TransactionExecutor) executor));
        }
        return new ContractValidator((ContractExecutor) executor);
    }

    private Executor getExecutor(TestComponent testComponent, String URL) {
        if (testComponent.getType().equals(TestComponent.Type.API)) {
            return new ApiExecutor((API) testComponent, URL);
        }
        if (testComponent.getType().equals(TestComponent.Type.TRANSACTION)) {
            return new TransactionExecutor((Transaction) testComponent, URL);
        }
        return new ContractExecutor(
                (Contract) testComponent,
                URL,
                KlayCredentials.create(env.getSender().getPrivateKey(), env.getSender().getAddress())
        );
    }

    public Env getEnv() {
        return env;
    }

    public File[] getFilesToRead() {
        return filesToRead;
    }

    public static class Builder {
        private String dirName;
        private File envFile;
        private Set<String> skip = new HashSet<>();
        private Set<String> only = new HashSet<>();

        public Builder(String dirName) {
            this.dirName = dirName;
        }

        public Builder setSkip(String... fileName) {
            skip.addAll(Arrays.asList(fileName));
            return this;
        }

        public Builder setOnly(String... fileName) {
            only.addAll(Arrays.asList(fileName));
            return this;
        }

        public Builder setEnvFile(File envFile) {
            this.envFile = envFile;
            return this;
        }

        public TestGenerator build() throws Exception {
            URL url = getClass().getClassLoader().getResource(KLAYTN_INTEGRATION_TESTS + File.separator + dirName);
            File dir = new File(url.toURI());
            Stream<File> fileStream = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .filter(file -> file.getName().endsWith(JSON_POSTFIX));
            if (!only.isEmpty()) {
                return new TestGenerator(
                        envFile, fileStream.filter(file -> only.contains(file.getName())).toArray(File[]::new));
            }
            return new TestGenerator(
                    envFile, fileStream.filter(file -> !skip.contains(file.getName())).toArray(File[]::new));
        }
    }
}
