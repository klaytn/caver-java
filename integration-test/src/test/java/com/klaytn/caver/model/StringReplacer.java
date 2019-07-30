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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayBlock;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.model.dto.Env;
import com.klaytn.caver.model.dto.Transaction;
import com.klaytn.caver.model.executor.TransactionExecutor;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class StringReplacer {
    public static final String GENERATED_CONTRACT_PACKAGE = "com.klaytn.caver.generated.";

    public static final int MAX_RANDOM_COUNT = 2;

    public static final String RANDOM = "random";
    public static final String ENV_SENDER = "env.sender";
    public static final String PRIVATE_KEY_POSTFIX = ".privateKey";

    public static final String BLOCK_HASH = "blockHash";
    public static final String BLOCK_NUMBER = "blockNumber";
    public static final String TRANSACTION_INDEX = "transactionIndex";
    public static final String SENDER_TX_HASH = "senderTxHash";
    public static final String TRANSACTION_HASH = "transactionHash";
    public static final String RAW_TRANSACTION = "rawTransaction";
    public static final String ENV_ACCOUNTS_ACC1 = "env.accounts.acc1";
    public static final String ENV_ACCOUNTS_ACC2 = "env.accounts.acc2";

    private String content;
    private Env env;
    private VariableStorage variableStorage;

    public StringReplacer(File file, Env env) {
        this.content = readContent(file);
        this.env = env;
    }

    public void configure(VariableStorage variableStorage) {
        this.variableStorage = variableStorage;
    }

    public String replaceAll() throws Exception {
        replaceRandoms();
        replaceEnvSender();
        replaceEnvAcc();
        replaceAPI();
        deployContract();
        return content;
    }

    private void deployContract() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        if (!jsonNode.has("deploy")) return;

        for (Iterator<Map.Entry<String, JsonNode>> deployContents = jsonNode.get("deploy").fields(); deployContents.hasNext(); ) {
            Map.Entry<String, JsonNode> deployContent = deployContents.next();
            String contractName = deployContent.getKey();
            JsonNode params = deployContent.getValue().get("constructorParams");
            String classPath = GENERATED_CONTRACT_PACKAGE + contractName;

            List<Class> paramTypes = new ArrayList(Arrays.asList(Caver.class, KlayCredentials.class, int.class, ContractGasProvider.class));
            List<Class> additionalParamTypes = ContractHelper.extractParamTypes(params);
            paramTypes.addAll(additionalParamTypes);

            Class contractClass = Class.forName(classPath);
            Method deployMethod = contractClass.getMethod("deploy", paramTypes.toArray(new Class[]{}));

            Object[] objects = ContractHelper.extractDeployParams(env, additionalParamTypes, params);
            Object result = deployMethod.invoke(contractClass, objects);
            SmartContract contract = (SmartContract) ((RemoteCall<?>) result).send();

            replaceWord(contractName, contract.getContractAddress());
            if (contractName.endsWith("ReceiverMock")) continue;
            variableStorage.set("contract", contract);
        }
    }

    private void replaceAPI() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        for (JsonNode innerJsonNode : jsonNode.get("test")) {
            if (!innerJsonNode.has("api")) continue;

            JsonNode apiNode = innerJsonNode.get("api");
            if (apiNode.has("pre")) {
                Transaction transaction = objectMapper.readValue(apiNode.get("pre").toString(), Transaction.class);
                TransactionExecutor transactionExecutor = getConfiguredTransactionExecutor(transaction);
                replaceUsingReceipt(transactionExecutor);

            }
            if (apiNode.has("preSigned")) {
                Transaction transaction = objectMapper.readValue(apiNode.get("preSigned").toString(), Transaction.class);
                TransactionExecutor transactionExecutor = getConfiguredTransactionExecutor(transaction);
                replaceUsingSignature(transactionExecutor);
            }
            if (hasWord(BLOCK_HASH)) replaceBlockHash();
        }
    }

    private TransactionExecutor getConfiguredTransactionExecutor(Transaction transaction) {
        transaction.configure(
                KlayCredentials.create(env.getSender().getPrivateKey(), env.getSender().getAddress()),
                KlayCredentials.create(env.getFeePayer().getPrivateKey(), env.getFeePayer().getAddress())
        );
        return new TransactionExecutor(transaction, env.getURL());
    }

    private void replaceBlockHash() throws IOException {
        Caver caver = Caver.build(env.getURL());
        KlayBlock.Block block = caver.klay().getBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO), false)
                .send().getResult();
        replaceWord(BLOCK_HASH, block.getHash());
    }

    private void replaceUsingSignature(TransactionExecutor transactionExecutor) throws Exception {
        KlayRawTransaction signatureData = transactionExecutor.getSignatureData();

        replaceWord(RAW_TRANSACTION, signatureData.getValueAsString());
    }

    private void replaceUsingReceipt(TransactionExecutor transactionExecutor) throws Exception {
        KlayTransactionReceipt.TransactionReceipt receipt = transactionExecutor.getReceipt();

        replaceWord(BLOCK_HASH, receipt.getBlockHash());
        replaceWord(BLOCK_NUMBER, receipt.getBlockNumber());
        replaceWord(TRANSACTION_INDEX, receipt.getTransactionIndex());
        replaceWord(SENDER_TX_HASH, receipt.getSenderTxHash());
        replaceWord(TRANSACTION_HASH, receipt.getTransactionHash());
    }

    private void replaceEnvAcc() {
        replaceWord(ENV_ACCOUNTS_ACC1, env.getAccounts().getAcc1().getAddress());
        replaceWord(ENV_ACCOUNTS_ACC2, env.getAccounts().getAcc2().getAddress());
    }

    private void replaceEnvSender() {
        KlayCredentials sender = KlayCredentials.create(
                env.getSender().getPrivateKey(),
                env.getSender().getAddress()
        );
        replaceWord(ENV_SENDER, sender.getAddress());
    }

    private void replaceRandoms() throws Exception {
        for (int i = 0; i < MAX_RANDOM_COUNT; i++) {
            KlayCredentials klayCredentials = KlayCredentials.create(Keys.createEcKeyPair());
            String random = appendIndex(RANDOM, i);
            replaceWord(random, klayCredentials.getAddress());
            replaceWord(random + PRIVATE_KEY_POSTFIX,
                    Numeric.toHexStringWithPrefix(klayCredentials.getEcKeyPair().getPrivateKey()));
        }
    }

    public void replaceWord(String from, String to) {
        content = content.replace("\"" + from + "\"", "\"" + to + "\"");
    }

    public boolean hasWord(String word) {
        return content.contains("\"" + word + "\"");
    }

    public static String appendIndex(String word, int index) {
        if (index == 0) return word;
        return word + (index + 1);
    }

    public static String readContent(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
