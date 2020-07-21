package com.klaytn.caver.contract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.SmartContractExecution;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.List;

/**
 * Representing a Contract's method information.
 */
public class ContractMethod {

    /**
     * A caver instance.
     */
    Caver caver;

    /**
     * The input type. It always set "function".
     */
    String type;

    /**
     * The function name.
     */
    String name;

    /**
     * The list of ContractIOType contains to function parameter information.
     */
    List<ContractIOType> inputs;

    /**
     * The list of ContractIOType contains to function return value information.
     */
    List<ContractIOType> outputs;

    /**
     * The function signature string. ex) foo(uint256,uin256)
     */
    String signature;

    /**
     * The contract address.
     */
    String contractAddress;

    /**
     * Creates a ContractMethod instance.
     */
    public ContractMethod() {
    }

    /**
     * Creates a ContractMethod instance.
     * @param caver A Caver instance.
     * @param type The input type. It always set "function"
     * @param name The function name
     * @param inputs The list of ContractIOType contains to function parameter information.
     * @param outputs The list of ContractIOType
     * @param signature The function signature string.
     * @param contractAddress The contract address
     */
    public ContractMethod(Caver caver, String type, String name, List<ContractIOType> inputs, List<ContractIOType> outputs, String signature, String contractAddress) {
        this.caver = caver;
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.signature = signature;
        this.contractAddress = contractAddress;
    }

    /**
     * Execute smart contract method in the EVM without sending any transaction.
     * @param arguments A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @return List
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Type> call(List<Type> arguments, SendOptions options) throws IOException, ClassNotFoundException {
        // Check the parameter type defined in function and the parameter type passed are the same.
        checkTypeValid(arguments);

        String encodedFunction = ABI.encodeFunctionCall(this, arguments);
        Bytes response = caver.rpc.klay.call(
                new CallObject(
                        options.from,
                        this.getContractAddress(),
                        Numeric.toBigInt(options.getGas()),
                        null,
                        Numeric.toBigInt(options.getValue()),
                        encodedFunction
                )).send();

        String encodedResult = response.getResult();
        return ABI.decodeParameters(this, encodedResult);
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * It sets TransactionReceiptProcessor to PollingTransactionReceiptProcessor.
     * @param argument A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Type> argument, SendOptions options) throws IOException, TransactionException {
        return send(argument, options, new PollingTransactionReceiptProcessor(caver, 1000, 15));
    }

    /**
     * Send a transaction to smart contract and execute its method.
     * @param argument A List of parameter that solidity wrapper type to call smart contract method.
     * @param options An option to execute smart contract method.
     * @param processor A TransactionReceiptProcessor to get receipt.
     * @return TransactionReceiptData
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData send(List<Type> argument, SendOptions options, TransactionReceiptProcessor processor) throws IOException, TransactionException {
        checkTypeValid(argument);

        String encodedFunction = ABI.encodeFunctionCall(this, argument);

        SmartContractExecution smartContractExecution = new SmartContractExecution.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(options.getFrom())
                .setTo(this.getContractAddress())
                .setInput(encodedFunction)
                .setGas(options.getGas())
                .build();

        caver.wallet.sign(options.getFrom(), smartContractExecution);
        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(smartContractExecution.getRawTransaction()).send();

        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(txHash.getResult());
        return receipt;
    }

    /**
     * Encodes the ABI for this method. It returns 32-bit function signature hash plus the encoded passed parameters.
     * @param argument A List of parameter that solidity wrapper type
     * @return The encoded ABI byte code to send via a transaction or call.
     */
    public String encodeABI(List<Type> argument) {
        return ABI.encodeFunctionCall(this, argument);
    }

    /**
     * Estimate the gas to execute the contract's method.
     * @param argument A List of parameter that solidity wrapper type
     * @param options An option to execute smart contract method.
     * @return String
     * @throws IOException
     */
    public String estimateGas(List<Type> argument, SendOptions options) throws IOException {
        String data = ABI.encodeFunctionCall(this, argument);
        CallObject callObject = new CallObject(options.getFrom(), this.getContractAddress(), Numeric.toBigInt(options.getGas()), null, Numeric.toBigInt(options.getValue()), data);

        Quantity estimateGas = caver.rpc.klay.estimateGas(callObject).send();
        if(estimateGas.hasError()) {
            throw new IOException(estimateGas.getError().getMessage());
        }

        return estimateGas.getResult();
    }

    /**
     * Check that passed parameter is valid to execute smart contract method.
     *   - check parameter count.
     *   - check defined parameter solidity type and parameter solidity wrapper type.
     * @param types A List of parameter that solidity wrapper type
     */
    public void checkTypeValid(List<Type> types) {
        if(types.size() != inputs.size()) {
            throw new IllegalArgumentException("Not matched passed parameter count.");
        }

        for(int i=0; i<types.size(); i++) {
            Type type = types.get(i);
            String solidityType = inputs.get(i).getType();

            if(!type.getTypeAsString().equals(solidityType)) {
                throw new IllegalArgumentException("Not matched parameter : " + getInputs().get(i).type + " " + getInputs().get(i).name + " You should use " + inputs.get(i).getJavaType());
            }
        }
    }

    /**
     * Getter function for Caver.
     * @return Caver
     */
    public Caver getCaver() {
        return caver;
    }

    /**
     * Getter function for type.
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Getter function for name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter function for input.
     * @return List
     */
    public List<ContractIOType> getInputs() {
        return inputs;
    }

    /**
     * Getter function for output.
     * @return List
     */
    public List<ContractIOType> getOutputs() {
        return outputs;
    }

    /**
     * Getter function for signature.
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Getter function for contract address
     * @return String
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Setter function for Caver.
     * @param caver The Caver instance.
     */
    public void setCaver(Caver caver) {
        this.caver = caver;
    }

    /**
     * Setter function for type.
     * @param type The input type. It always set "function".
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for name.
     * @param name A function name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for inputs
     * @param inputs The list of ContractIOType contains to function parameter information.
     */
    public void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    /**
     * Setter function for outputs
     * @param outputs The list of ContractIOTYpe contains to function return value information.
     */
    public void setOutputs(List<ContractIOType> outputs) {
        this.outputs = outputs;
    }

    /**
     * Setter function for function signature.
     * @param signature A function signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Setter function for contract address
     * @param contractAddress A contract address.
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
