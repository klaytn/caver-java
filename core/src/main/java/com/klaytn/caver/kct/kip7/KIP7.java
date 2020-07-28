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

    public KIP7(Caver caver) throws IOException {
        super(caver, KIP7ConstantData.ABI);
    }

    public KIP7(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP7ConstantData.ABI, contractAddress);
    }

    public static KIP7 deploy(Caver caver, KIP7DeployParam tokenInfo, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        List deployArgument = Arrays.asList(tokenInfo.getName(), tokenInfo.getSymbol(), tokenInfo.getDecimals(), tokenInfo.getInitialSupply());
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP7ConstantData.BINARY, deployArgument);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(4000000));

        KIP7 kip7 = new KIP7(caver);
        kip7.deploy(contractDeployParams, sendOptions);

        return kip7;
    }

    @Override
    public KIP7 clone() {
        try {
            return new KIP7(this.getCaver());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KIP7 clone(String tokenAddress) {
        try {
            return new KIP7(this.getCaver(), tokenAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean supportInterface(String interfaceId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SUPPORTS_INTERFACE).call(Arrays.asList(interfaceId), callObject);

        return (boolean)result.get(0).getValue();
    }

    public String name() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_NAME).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    public String symbol() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_SYMBOL).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    public String decimals() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_DECIMALS).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    public String totalSupply() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_TOTAL_SUPPLY).call(null, callObject);

        return (String)result.get(0).getValue();
    }

    public String balanceOf(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_BALANCE_OF).call(Arrays.asList(account), callObject);

        BigInteger value = (BigInteger)result.get(0).getValue();
        return Numeric.toHexStringWithPrefix(value);
    }

    public String allowance(String owner, String spender) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_ALLOWANCE).call(Arrays.asList(owner, spender), callObject);

        BigInteger value = (BigInteger)result.get(0).getValue();
        return Numeric.toHexStringWithPrefix(value);
    }

    public boolean isMinter(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_IS_MINTER).call(Arrays.asList(account), callObject);

        return (boolean)result.get(0).getValue();
    }

    public boolean isPauser(String account) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_IS_PAUSER).call(Arrays.asList(account), callObject);

        return (boolean)result.get(0).getValue();
    }

    public boolean paused() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        CallObject callObject = CallObject.createCallObject();
        List<Type> result = this.getMethod(FUNCTION_PAUSED).call(null, callObject);

        return (boolean)result.get(0).getValue();
    }

    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_APPROVE).send(Arrays.asList(spender, amount));
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData approve(String spender, BigInteger amount, SendOptions sendParam) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_APPROVE).send(Arrays.asList(spender, amount), sendParam);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount));
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData transfer(String recipient, BigInteger amount, SendOptions sendParam) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount), sendParam);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount));
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData transferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount), sendParam);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount));
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, SendOptions sendParam) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount), sendParam);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount, data));
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData safeTransfer(String recipient, BigInteger amount, String data, SendOptions sendParam) {
        TransactionReceipt.TransactionReceiptData receiptData = this.getMethod(FUNCTION_TRANSFER).send(Arrays.asList(recipient, amount, data), sendParam);
        return receiptData;
    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount) {

    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data) {

    }

    public TransactionReceipt.TransactionReceiptData safeTransferFrom(String sender, String recipient, BigInteger amount, String data, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount) {

    }

    public TransactionReceipt.TransactionReceiptData mint(String account, BigInteger amount, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData addMinter(String account) {

    }

    public TransactionReceipt.TransactionReceiptData addMinter(String account, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData renounceMinter() {

    }

    public TransactionReceipt.TransactionReceiptData renounceMinter(SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount) {

    }

    public TransactionReceipt.TransactionReceiptData burn(BigInteger amount, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount) {

    }

    public TransactionReceipt.TransactionReceiptData burnFrom(String account, BigInteger amount, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData addPauser(String account) {

    }

    public TransactionReceipt.TransactionReceiptData addPauser(String account, SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData pause() {

    }

    public TransactionReceipt.TransactionReceiptData pause(SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData unpause() {

    }

    public TransactionReceipt.TransactionReceiptData unpause(SendOptions sendParam) {

    }

    public TransactionReceipt.TransactionReceiptData renouncePauser() {

    }

    public TransactionReceipt.TransactionReceiptData renouncePauser(SendOptions sendParam) {

    }
}
