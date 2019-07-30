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
 * This file is derived from web3j/core/src/main/java/org/web3j/tx/Contract.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.tx;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.ErrorHandler;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.utils.CodeFormat;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
@SuppressWarnings("WeakerAccess")
public class SmartContract extends ManagedTransaction {

    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    public static final String BIN_NOT_PROVIDED = "Bin file was not provided";
    public static final String FUNC_DEPLOY = "deploy";

    protected String contractBinary;
    protected String contractAddress;
    protected ContractGasProvider gasProvider;
    protected KlayTransactionReceipt.TransactionReceipt transactionReceipt;
    protected Map<String, String> deployedAddresses;
    protected DefaultBlockParameter defaultBlockParameter = DefaultBlockParameterName.LATEST;

    private SmartContract(Caver caver, TransactionManager transactionManager) {
        super(caver, transactionManager);
    }

    protected SmartContract(String contractBinary, String contractAddress,
                            Caver caver, TransactionManager transactionManager,
                            ContractGasProvider gasProvider) {
        super(caver, transactionManager);

        this.contractAddress = contractAddress;
        this.contractBinary = contractBinary;
        this.gasProvider = gasProvider;
    }

    protected SmartContract(String contractBinary, String contractAddress, Caver caver,
                            KlayCredentials credentials, int chainId,
                            ContractGasProvider gasProvider) {

        this(contractBinary, contractAddress, caver,
                new TransactionManager.Builder(caver, credentials)
                        .setChaindId(chainId)
                        .build(),
                gasProvider);
    }

    protected SmartContract(String contractAddress,
                            Caver caver, TransactionManager transactionManager,
                            ContractGasProvider gasProvider) {
        this("", contractAddress, caver, transactionManager, gasProvider);
    }

    protected SmartContract(String contractAddress, Caver caver,
                            KlayCredentials credentials, int chainId,
                            ContractGasProvider gasProvider) {
        this("", contractAddress, caver,
                new TransactionManager.Builder(caver, credentials)
                        .setChaindId(chainId)
                        .build(),
                gasProvider);
    }

    public static SmartContract create(Caver caver, TransactionManager transactionManager) {
        return new SmartContract(caver, transactionManager);
    }

    public static SmartContract create(Caver caver, KlayCredentials klayCredentials, int chainId) {
        TransactionManager transactionManager = new TransactionManager.Builder(caver, klayCredentials)
                .setChaindId(chainId)
                .build();

        return SmartContract.create(caver, transactionManager);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendDeployTransaction(SmartContractDeployTransaction transaction) {
        return new RemoteCall<>(() -> send(transaction));
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendExecutionTransaction(SmartContractExecutionTransaction transaction) {
        return new RemoteCall<>(() -> send(transaction));
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed.</p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendDeployTransaction(SmartContractDeployTransaction)} instead.
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendDeployTransaction(
            Caver caver, KlayCredentials credentials, SmartContractDeployTransaction transaction) {

        return SmartContract.sendDeployTransaction(caver, credentials, transaction, null);
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed.</p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendDeployTransaction(SmartContractDeployTransaction)} instead.
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendDeployTransaction(
            Caver caver, KlayCredentials credentials, SmartContractDeployTransaction transaction, ErrorHandler errorHandler) {

        TransactionManager transactionManager = new TransactionManager.Builder(caver, credentials)
                .setErrorHandler(errorHandler)
                .build();

        return new RemoteCall<>(() ->
                new SmartContract(caver, transactionManager).send(transaction));
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed.</p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendExecutionTransaction(SmartContractExecutionTransaction)} instead.
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendExecutionTransaction(
            Caver caver, KlayCredentials credentials, SmartContractExecutionTransaction transaction) {

        return SmartContract.sendExecutionTransaction(caver, credentials, transaction, null);
    }

    /**
     * @deprecated  <p>In caver-java 1.0.0, we provided static methods to send transactions for `ValueTransfer`, `Account`, `Cancel`, and `SmartContract` classes. The static methods will be removed.</p>
     *              <p>This deprecated method can be used only for Baobab Testnet.</p>
     *              Use {@link #sendExecutionTransaction(SmartContractExecutionTransaction)} instead.
     */
    @Deprecated
    public static RemoteCall<KlayTransactionReceipt.TransactionReceipt> sendExecutionTransaction(
            Caver caver, KlayCredentials credentials, SmartContractExecutionTransaction transaction, ErrorHandler errorHandler) {

        TransactionManager transactionManager = new TransactionManager.Builder(caver, credentials)
                .setErrorHandler(errorHandler)
                .build();

        return new RemoteCall<>(() ->
                new SmartContract(caver, transactionManager).send(transaction));
    }


    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setTransactionReceipt(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }

    public String getContractBinary() {
        return contractBinary;
    }

    public void setGasProvider(ContractGasProvider gasProvider) {
        this.gasProvider = gasProvider;
    }

    /**
     * Allow {@code gasPrice} to be set.
     *
     * @param newPrice gas price to use for subsequent transactions
     * @deprecated use ContractGasProvider
     */
    public void setGasPrice(BigInteger newPrice) {
        this.gasProvider = new StaticGasProvider(newPrice, gasProvider.getGasLimit());
    }

    /**
     * Get the current {@code gasPrice} value this contract uses when executing transactions.
     *
     * @return the gas price set on this contract
     * @deprecated use ContractGasProvider
     */
    public BigInteger getGasPrice() {
        return gasProvider.getGasPrice();
    }

    /**
     * Check that the contract deployed at the address associated with this smart contract wrapper
     * is in fact the contract you believe it is.
     *
     * <p>This method uses the klay_getCode method to get the contract byte code and validates it
     * against the byte code stored in this smart contract wrapper.
     *
     * @return true if the contract is valid
     * @throws IOException if unable to connect to klaytn node
     */
    public boolean isValid() throws IOException {
        if (contractBinary.equals(BIN_NOT_PROVIDED)) {
            throw new UnsupportedOperationException(
                    "Contract binary not present in contract wrapper, "
                            + "please generate your wrapper using -abiFile=<file>");
        }

        if (contractAddress.equals("")) {
            throw new UnsupportedOperationException(
                    "Contract binary not present, you will need to regenerate your smart");
        }

        Bytes klayGetCode = caver.klay()
                .getCode(contractAddress, DefaultBlockParameterName.LATEST)
                .send();
        if (klayGetCode.hasError()) {
            return false;
        }

        String code = Numeric.cleanHexPrefix(klayGetCode.getResult());
        // There may be multiple contracts in the Solidity bytecode, hence we only check for a
        // match with a subset
        return !code.isEmpty() && contractBinary.contains(code);
    }

    /**
     * If this Contract instance was created at deployment, the TransactionReceipt associated
     * with the initial creation will be provided, e.g. via a <em>deploy</em> method. This will
     * not persist for Contracts instances constructed via a <em>load</em> method.
     *
     * @return the TransactionReceipt generated at contract deployment
     */
    public Optional<KlayTransactionReceipt.TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(transactionReceipt);
    }

    /**
     * Sets the default block parameter. This use useful if one wants to query
     * historical state of a contract.
     *
     * @param defaultBlockParameter the default block parameter
     */
    public void setDefaultBlockParameter(DefaultBlockParameter defaultBlockParameter) {
        this.defaultBlockParameter = defaultBlockParameter;
    }

    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     * @return {@link List} of values returned by function call
     */
    private List<Type> executeCall(
            Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        Bytes klayCall = caver.klay().call(
                new CallObject(
                        transactionManager.getDefaultAddress(), contractAddress,
                        null, null, null, encodedFunction),
                defaultBlockParameter)
                .send();

        String value = klayCall.getResult();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type> T executeCallSingleValueReturn(
            Function function) throws IOException {
        List<Type> values = executeCall(function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type, R> R executeCallSingleValueReturn(
            Function function, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(function);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        Object value = result.getValue();
        if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString();  // cast isn't necessary
        } else {
            throw new ContractCallException(
                    "Unable to convert response: " + value
                            + " to expected type: " + returnType.getSimpleName());
        }
    }

    protected List<Type> executeCallMultipleValueReturn(
            Function function) throws IOException {
        return executeCall(function);
    }

    protected KlayTransactionReceipt.TransactionReceipt executeTransaction(
            Function function)
            throws TransactionException {
        return executeTransaction(function, BigInteger.ZERO);
    }

    private KlayTransactionReceipt.TransactionReceipt executeTransaction(
            Function function, BigInteger weiValue)
            throws TransactionException {
        return executeTransaction(FunctionEncoder.encode(function), weiValue, function.getName());
    }

    /**
     * Given the duration required to execute a transaction.
     *
     * @param data     to send in transaction
     * @param pebValue in Wei to send in transaction
     * @return {@link Optional} containing our transaction receipt
     * @throws IOException          if the call to the node fails
     * @throws TransactionException if the transaction was not mined while waiting
     */
    KlayTransactionReceipt.TransactionReceipt executeTransaction(
            String data, BigInteger pebValue, String funcName)
            throws TransactionException {

        TransactionTransformer transactionTransformer;
        if (FUNC_DEPLOY.equals(funcName)) {
            transactionTransformer = SmartContractDeployTransaction.create(
                    transactionManager.getDefaultAddress(),
                    pebValue,
                    Numeric.hexStringToByteArray(data),
                    gasProvider.getGasLimit(funcName),
                    CodeFormat.EVM
            );
        } else {
            transactionTransformer = SmartContractExecutionTransaction.create(
                    transactionManager.getDefaultAddress(),
                    contractAddress,
                    pebValue,
                    Numeric.hexStringToByteArray(data),
                    gasProvider.getGasLimit(funcName)
            );
        }

        KlayTransactionReceipt.TransactionReceipt receipt = send(transactionTransformer);
        return receipt;
    }

    protected <T extends Type> RemoteCall<T> executeRemoteCallSingleValueReturn(Function function) {
        return new RemoteCall<>(() -> executeCallSingleValueReturn(function));
    }

    protected <T> RemoteCall<T> executeRemoteCallSingleValueReturn(
            Function function, Class<T> returnType) {
        return new RemoteCall<>(() -> executeCallSingleValueReturn(function, returnType));
    }

    protected RemoteCall<List<Type>> executeRemoteCallMultipleValueReturn(Function function) {
        return new RemoteCall<>(() -> executeCallMultipleValueReturn(function));
    }

    protected RemoteCall<KlayTransactionReceipt.TransactionReceipt> executeRemoteCallTransaction(Function function) {
        return new RemoteCall<>(() -> executeTransaction(function));
    }

    protected RemoteCall<KlayTransactionReceipt.TransactionReceipt> executeRemoteCallTransaction(
            Function function, BigInteger weiValue) {
        return new RemoteCall<>(() -> executeTransaction(function, weiValue));
    }

    private static <T extends SmartContract> T create(
            T contract, String binary, String encodedConstructor, BigInteger value)
            throws IOException, TransactionException {
        KlayTransactionReceipt.TransactionReceipt transactionReceipt =
                contract.executeTransaction(binary + encodedConstructor, value, FUNC_DEPLOY);

        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setTransactionReceipt(transactionReceipt);

        return contract;
    }

    protected static <T extends SmartContract> T deploy(
            Class<T> type,
            Caver caver, KlayCredentials credentials, int chainId,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor, BigInteger value)
            throws RuntimeException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(
                    String.class, Caver.class,
                    KlayCredentials.class, int.class,
                    ContractGasProvider.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(
                    null, caver, credentials, chainId, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends SmartContract> T deploy(
            Class<T> type,
            Caver caver, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor, BigInteger value)
            throws RuntimeException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(
                    String.class, Caver.class,
                    TransactionManager.class,
                    ContractGasProvider.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(
                    null, caver, transactionManager, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends SmartContract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Caver caver, KlayCredentials credentials, int chainId,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor, BigInteger value) {
        return new RemoteCall<>(() -> deploy(
                type, caver, credentials, chainId, contractGasProvider, binary,
                encodedConstructor, value));
    }

    public static <T extends SmartContract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Caver caver, KlayCredentials credentials, int chainId,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor) {
        return new RemoteCall<>(() -> deploy(
                type, caver, credentials, chainId, contractGasProvider,
                binary, encodedConstructor, BigInteger.ZERO));
    }

    public static <T extends SmartContract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Caver caver, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor, BigInteger value) {
        return new RemoteCall<>(() -> deploy(
                type, caver, transactionManager, contractGasProvider, binary,
                encodedConstructor, value));
    }

    public static <T extends SmartContract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Caver caver, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary, String encodedConstructor) {
        return new RemoteCall<>(() -> deploy(
                type, caver, transactionManager, contractGasProvider, binary,
                encodedConstructor, BigInteger.ZERO));
    }

    public static EventValues staticExtractEventParameters(
            Event event, KlayLogs.Log log) {
        final List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    protected EventValues extractEventParameters(Event event, KlayLogs.Log log) {
        return staticExtractEventParameters(event, log);
    }

    protected List<EventValues> extractEventParameters(
            Event event, KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParameters(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected SmartContract.EventValuesWithLog extractEventParametersWithLog(Event event, KlayLogs.Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new SmartContract.EventValuesWithLog(eventValues, log);
    }

    protected List<SmartContract.EventValuesWithLog> extractEventParametersWithLog(
            Event event, KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParametersWithLog(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Subclasses should implement this method to return pre-existing addresses for deployed
     * contracts.
     *
     * @param networkId the network id, for example "1" for the main-net, "1001" for baobab.
     * @return the deployed address of the contract, if known, and null otherwise.
     */
    protected String getStaticDeployedAddress(String networkId) {
        return null;
    }

    public final void setDeployedAddress(String networkId, String address) {
        if (deployedAddresses == null) {
            deployedAddresses = new HashMap<>();
        }
        deployedAddresses.put(networkId, address);
    }

    public final String getDeployedAddress(String networkId) {
        String addr = null;
        if (deployedAddresses != null) {
            addr = deployedAddresses.get(networkId);
        }
        return addr == null ? getStaticDeployedAddress(networkId) : addr;
    }

    /**
     * Adds a log field to {@link EventValues}.
     */
    public static class EventValuesWithLog {
        private final EventValues eventValues;
        private final KlayLogs.Log log;

        private EventValuesWithLog(EventValues eventValues, KlayLogs.Log log) {
            this.eventValues = eventValues;
            this.log = log;
        }

        public List<Type> getIndexedValues() {
            return eventValues.getIndexedValues();
        }

        public List<Type> getNonIndexedValues() {
            return eventValues.getNonIndexedValues();
        }

        public KlayLogs.Log getLog() {
            return log;
        }
    }

    @SuppressWarnings("unchecked")
    protected static <S extends Type, T>
    List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<T>();
        for (Iterator<S> it = arr.iterator(); it.hasNext(); ) {
            out.add((T) it.next().getValue());
        }
        return out;
    }
}
