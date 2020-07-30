package com.klaytn.caver.kct.kip7;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.TransactionReceipt;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class KIP7 extends Contract {

    private static final String FUNCTION_ADD_MINTER = "addMinter";
    private static final String FUNCTION_ADD_PAUSER = "addPauser";
    private static final String FUNCTION_ALLOWANCE = "allowance";
    private static final String FUNCTION_APPROVE = "approve";
    private static final String FUNCTION_BALANCE_OF = "balanceOf";
    private static final String FUNCTION_BURN = "burn";
    private static final String FUNCTION_BURN_FROM = "burnFrom";
    private static final String FUNCTION_DECIMALS = "decimals";
    private static final String FUNCTION_IS_MINTER = "isMinter";
    private static final String FUNCTION_IS_PAUSER = "isPauser";
    private static final String FUNCTION_MINT = "mint";
    private static final String FUNCTION_NAME = "name";
    private static final String FUNCTION_PAUSE = "pause";
    private static final String FUNCTION_PAUSED = "paused";
    private static final String FUNCTION_RENOUNCE_MINTER = "renounceMinter";
    private static final String FUNCTION_RENOUNCE_PAUSER = "renouncePauser";
    private static final String FUNCTION_SAFE_TRANSFER = "safeTransfer";
    private static final String FUNCTION_SAFE_TRANSFER_FROM = "safeTransferFrom";
    private static final String FUNCTION_SUPPORTS_INTERFACE = "supportsInterface";
    private static final String FUNCTION_SYMBOL = "symbol";
    private static final String FUNCTION_TOTAL_SUPPLY = "totalSupply";
    private static final String FUNCTION_TRANSFER = "transfer";
    private static final String FUNCTION_TRANSFER_FROM = "transferFrom";
    private static final String FUNCTION_UNPAUSE = "unpause";

    /**
     * Creates a KIP7 instance.
     * @param caver A Caver instance.
     * @throws IOException
     */
    public KIP7(Caver caver) throws IOException {
        super(caver, KIP7ConstantData.ABI);
    }

    /**
     * Creates a KIP7 instance
     * @param caver A Caver instance
     * @param contractAddress A contract address
     * @throws IOException
     */
    public KIP7(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP7ConstantData.ABI, contractAddress);
    }

    /**
     * Deploy KIP7 contract.
     * It must add deployer's keyring in caver.wallet.
     * @param caver A Caver instance
     * @param tokenInfo The KIP7 contract's deploy parameter values
     * @param deployer A deployer's address
     * @return KIP7
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public static KIP7 deploy(Caver caver, KIP7DeployParams tokenInfo, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        List deployArgument = Arrays.asList(tokenInfo.getName(), tokenInfo.getSymbol(), tokenInfo.getDecimals(), tokenInfo.getInitialSupply());
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP7ConstantData.BINARY, deployArgument);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(4000000));

        KIP7 kip7 = new KIP7(caver);
        kip7.deploy(contractDeployParams, sendOptions);

        return kip7;
    }

    /**
     * Estimates the gas to execute the contract's method.
     * @param kip7 A KIP7 instance.
     * @param functionName A KIP7 contract's method.
     * @param callObject A CallObject instance to execute estimateGas
     * @param arguments A arguments to execute contract's method.
     * @return BigInteger
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public static BigInteger estimateGas(KIP7 kip7, String functionName, CallObject callObject, List<Object> arguments) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String gas = kip7.getMethod(functionName).estimateGas(arguments, callObject);
        BigDecimal bigDecimal = new BigDecimal(Numeric.toBigInt(gas));
        BigInteger gasInteger = bigDecimal.multiply(new BigDecimal(1.5)).toBigInteger();

        return gasInteger;
    }

    /**
     * Copy instance
     * @return KIP7
     */
    @Override
    public KIP7 clone() {
        try {
            return new KIP7(this.getCaver());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copy instance with token address
     * @param tokenAddress A KIP7 token address
     * @return KIP7
     */
    public KIP7 clone(String tokenAddress) {
        try {
            return new KIP7(this.getCaver(), tokenAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Call method "supportsInterface" in KIP-13 standard contract.
     * @param interfaceId interface identifier
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean supportInterface(String interfaceId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SUPPORTS_INTERFACE).call(Arrays.asList(interfaceId), callObject);

        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "name" in KIP-7 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String name() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_NAME).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    /**
     * Call method "symbol" in KIP-7 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String symbol() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SYMBOL).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    /**
     * Call method "decimals" in KIP-7 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String decimals() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_DECIMALS).call(null, callObject);

        return Numeric.toHexStringWithPrefix((BigInteger)result.get(0).getValue());
    }

    /**
     * Call method "totalSupply" in KIP-7 standard contract.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String totalSupply() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_TOTAL_SUPPLY).call(null, callObject);

        return Numeric.toHexStringWithPrefix((BigInteger)result.get(0).getValue());
    }

    /**
     * Call method "balanceOf" in KIP-7 standard contract.
     * @param account An address for whom to query the balance
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String balanceOf(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_BALANCE_OF).call(Arrays.asList(account), callObject);

        BigInteger value = (BigInteger)result.get(0).getValue();
        return Numeric.toHexStringWithPrefix(value);
    }

    /**
     * Call method "allowance" in KIP-7 standard contract.
     * @param owner The account allowed `spender` to withdraw the tokens from the account.
     * @param spender The address is approved to withdraw the tokens.
     * @return String
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public String allowance(String owner, String spender) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_ALLOWANCE).call(Arrays.asList(owner, spender), callObject);

        BigInteger value = (BigInteger)result.get(0).getValue();
        return Numeric.toHexStringWithPrefix(value);
    }

    /**
     * Call method "isMinter" in KIP-7 standard contract.
     * @param account The account to check the minting permission
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_IS_MINTER).call(Arrays.asList(account), callObject);

        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "isPauser" in KIP-7 standard contract.
     * @param account The account to check the pausing permission
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean isPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_IS_PAUSER).call(Arrays.asList(account), callObject);

        return (boolean)result.get(0).getValue();
    }

    /**
     * Call method "paused" in KIP-7 standard contract.
     * @return boolean
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public boolean paused() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_PAUSED).call(null, callObject);

        return (boolean)result.get(0).getValue();
    }

    /**
     * Execute a method "approve" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param spender The address is approved to withdraw the tokens.
     * @param amount Amount the token amount will be approved.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return approve(spender, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "approve" in KIP-7 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param spender The address is approved to withdraw the tokens.
     * @param amount Amount the token amount will be approved.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_APPROVE, Arrays.asList(spender, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_APPROVE).send(Arrays.asList(spender, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "transfer" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return transfer(recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "transfer" in KIP-7 standard contract.
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_TRANSFER, Arrays.asList(recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "transferFrom" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return transferFrom(sender, recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "transferFrom" in KIP-7 standard contract.
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_TRANSFER_FROM, Arrays.asList(sender, recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransfer(recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER, Arrays.asList(recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER).send(Arrays.asList(recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransfer(recipient, amount, data, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER, Arrays.asList(recipient, amount, data));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER).send(Arrays.asList(recipient, amount, data), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransferFrom(sender, recipient, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransfer" in KIP-7 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(sender, recipient, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "safeTransferFrom" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return safeTransferFrom(sender, recipient, amount, data, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "safeTransferFrom" in KIP-7 standard contract.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sender The current owner of the tokens.
     * @param recipient The address of the account to receive the token.
     * @param amount The token amount will be transferred.
     * @param data Additional data with no specified format, sent in call to `_to`
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_SAFE_TRANSFER_FROM, Arrays.asList(sender, recipient, amount, data));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_SAFE_TRANSFER_FROM).send(Arrays.asList(sender, recipient, amount, data), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "mint" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account that will receive the minted token
     * @param amount The token amount to mint
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return mint(account, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "mint" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account that will receive the minted token
     * @param amount The token amount to mint
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_MINT, Arrays.asList(account, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_MINT).send(Arrays.asList(account, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "addMinter" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the minting permission
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return addMinter(account, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "addMinter" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the minting permission
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addMinter(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_ADD_MINTER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_MINTER).send(Arrays.asList(account), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "renounceMinter" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renounceMinter() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renounceMinter(this.getDefaultSendOptions());
    }

    /**
     * Execute a method "renounceMinter" in KIP-7 standard contract.
     * Caller must have "Minter" Permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renounceMinter(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_MINTER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_MINTER).send(Arrays.asList(sendOptions));
        return receiptData;
    }

    /**
     * Execute a method "burn" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param amount The token amount to be burned
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burn(amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "burn" in KIP-7 standard contract.
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param amount The token amount to be burned
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN, Arrays.asList(amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_BURN).send(Arrays.asList(amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "burnFrom" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account will be deducted is the The token amount to be burned
     * @param amount The token amount to be burned
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return burnFrom(account, amount, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "burnFrom" in KIP-7 standard contract.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account will be deducted is the The token amount to be burned
     * @param amount The token amount to be burned
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_BURN_FROM, Arrays.asList(account, amount));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_BURN_FROM).send(Arrays.asList(account, amount), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "addPauser" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the pausing permission
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return addPauser(account, this.getDefaultSendOptions());
    }

    /**
     * Execute a method "addPauser" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param account The account to be given the pausing permission
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData addPauser(String account, SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_ADD_PAUSER, Arrays.asList(account));

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_ADD_PAUSER).send(Arrays.asList(account), sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "pause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return pause(this.getDefaultSendOptions());
    }

    /**
     * Execute a method "pause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData pause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_PAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_PAUSE).send(null, sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return unpause(this.getDefaultSendOptions());
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData unpause(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_PAUSE, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_UNPAUSE).send(null, sendOptions);
        return receiptData;
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * It will use default sendOptions in contract instance to passed sendOptions
     * If a gas value in default sendOptions has null, it will automatically set gas value through estimateGas().
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renouncePauser() throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        return renouncePauser(this.getDefaultSendOptions());
    }

    /**
     * Execute a method "unpause" in KIP-7 standard contract.
     * Caller must have Pauser permission.
     * If a gas value in sendOptions has null, it will automatically set gas value through estimateGas().
     * @param sendParam A SendOptions need to execute contract's method.
     * @return TransactionReceipt.TransactionReceiptData
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws TransactionException
     */
    public TransactionReceipt.TransactionReceiptData renouncePauser(SendOptions sendParam) throws NoSuchMethodException, IOException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        SendOptions sendOptions = determineSendOptions(this, sendParam, FUNCTION_RENOUNCE_PAUSER, null);

        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_RENOUNCE_PAUSER).send(null, sendOptions);
        return receiptData;
    }

    private static SendOptions determineSendOptions(KIP7 kip7, SendOptions sendOptions, String functionName, List<Object> argument) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions newSendOptions = null;
        if(sendOptions.getGas() == null) {
            CallObject callObject = CallObject.createCallObject(sendOptions.getFrom());
            BigInteger gas = estimateGas(kip7, functionName, callObject, argument);
            newSendOptions = new SendOptions(sendOptions.getFrom(), gas);
        } else {
            newSendOptions = sendOptions;
        }

        return newSendOptions;
    }
}
