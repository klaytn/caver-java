package com.klaytn.caver;

import com.klaytn.caver.common.contract.ContractTest;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;
import com.klaytn.caver.transaction.ITransactionWithGasPriceField;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.EthereumDynamicFee;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.protocol.core.BatchRequest;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class DynamicFeeTest {
    static Caver caver;
    static SingleKeyring rich, sender, feePayer;

    static String kip7Address;

    @Before
    public void setUp() throws Exception {
        caver = new Caver(Caver.DEFAULT_URL);
        rich = caver.wallet.keyring.createFromPrivateKey("0x871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5");
        caver.wallet.add(rich);

        sender = caver.wallet.keyring.generate();
        fillKlay(sender, "200");
        feePayer = caver.wallet.keyring.generate();
        fillKlay(feePayer, "200");

        deployKIP7();
    }

    // deploy a KIP-7 token contract for testing
    private void deployKIP7() throws TransactionException, IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        String name = "Jamie";
        String symbol = "JME";
        BigInteger decimals = BigInteger.valueOf(18);
        BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(decimals.intValue())); // 100000 * 10^18

        Contract contract = caver.contract.create(ContractTest.jsonObj);
        SendOptions sendOptions = new SendOptions(sender.getAddress(), BigInteger.valueOf(6500000));
        contract.deploy(sendOptions, ContractTest.BINARY, name, symbol, decimals, initialSupply);

        kip7Address = contract.getContractAddress();
    }

    // rich sends enough KLAY to an account.
    private void fillKlay(AbstractKeyring keyring, String amount) throws IOException, TransactionException {
        caver.wallet.add(keyring);
        ValueTransfer valueTransfer = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                        .setFrom(rich.getAddress())
                        .setTo(sender.getAddress())
                        .setValue(caver.utils.convertToPeb(amount, Utils.KlayUnit.KLAY))
                        .setGas(BigInteger.valueOf(6000000))
        );
        caver.wallet.sign(rich.getAddress(), valueTransfer);
        String txHash = caver.rpc.klay.sendRawTransaction(valueTransfer).send().getResult();

        // Check receipt to make sure tx is processed.
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
        receiptProcessor.waitForTransactionReceipt(txHash);
    }

    public void generateTxsBomb() throws IOException {
        generateTxsBomb(100);
    }

    private void generateTxsBomb(int num) throws IOException {
        BatchRequest batchRequest = caver.getRpc().newBatch();
        BigInteger senderNonce = caver.rpc.klay.getTransactionCount(sender.getAddress()).send().getValue();
        String input = "0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029";
        for (int i = 0; i < num; i++) {
            SmartContractDeploy smartContractDeploy = caver.transaction.smartContractDeploy.create(
                    TxPropertyBuilder.smartContractDeploy()
                            .setFrom(sender.getAddress())
                            .setGas(BigInteger.valueOf(5000000))
                            .setInput(input)
                            .setNonce(senderNonce)
            );
            caver.wallet.sign(sender.getAddress(), smartContractDeploy);
            String rawTx = smartContractDeploy.getRLPEncoding();
            batchRequest.add(caver.rpc.klay.sendRawTransaction(rawTx));
            senderNonce = senderNonce.add(BigInteger.valueOf(1));
        }
        batchRequest.send();
    }

    private boolean validateGasFeeWithReceipt(TransactionReceipt.TransactionReceiptData receipt) throws IOException {
        BigInteger gasPriceInReceipt = Numeric.toBigInt(receipt.getGasPrice());
        BigInteger gasPriceAtParentBlock = caver.rpc.klay.getGasPriceAt(
                Numeric.toBigInt(receipt.getBlockNumber()).subtract(BigInteger.valueOf(1)).longValue()
        ).send().getValue(); // Klaytn will return baseFee
        BigInteger gasPriceAtReceiptBlock = caver.rpc.klay.getGasPriceAt(
                Numeric.toBigInt(receipt.getBlockNumber()).longValue()
        ).send().getValue(); // Klaytn will return baseFee

        // To process a transaction, the gasPrice of the tx should be equal or bigger than baseFee(effectiveGasPrice)
        if (Numeric.toBigInt(receipt.getEffectiveGasPrice()).compareTo(gasPriceInReceipt) > 0) {
            return false;
        }

        // effectiveGasPrice should be defined by baseFee used gas price when tx is processed
        if (Numeric.toBigInt(receipt.getEffectiveGasPrice()).compareTo(gasPriceAtReceiptBlock) != 0) {
            return false;
        }

        // set gasPrice with `baseFee * 2`
        if (gasPriceAtParentBlock.multiply(BigInteger.valueOf(2)).compareTo(gasPriceInReceipt) != 0) {
            return false;
        }
        return true;
    }

    private boolean validateDynamicFeeTxWithReceipt(TransactionReceipt.TransactionReceiptData receipt) throws IOException {
        BigInteger maxFeePerGas = Numeric.toBigInt(receipt.getMaxFeePerGas());
        BigInteger gasPriceAtParentBlock = caver.rpc.klay.getGasPriceAt(
                Numeric.toBigInt(receipt.getBlockNumber()).subtract(BigInteger.valueOf(1)).longValue()
        ).send().getValue(); // Klaytn will return baseFee

        // To process a transaction, the maxFeePerGas of the tx should be equal or bigger than baseFee(effectiveGasPrice)
        if (Numeric.toBigInt(receipt.getEffectiveGasPrice()).compareTo(maxFeePerGas) > 0) {
            return false;
        }

        // Set gasPrice with `baseFee * 2`
        if (gasPriceAtParentBlock.multiply(BigInteger.valueOf(2)).compareTo(maxFeePerGas) != 0) {
            return false;
        }
        return true;
    }

    private boolean validateGasPriceInTx(AbstractTransaction tx) throws IOException {
        // Klaytn will return baseFee
        BigInteger gasPrice = caver.rpc.klay.getGasPrice().send().getValue();

        // If transaction type is TxTypeEthereumDynamicFee,
        // validate `maxPriorityFeePerGas` and `maxFeePerGas`.
        if (tx.getType().contains("DynamicFee")) {
            BigInteger mpfg = caver.rpc.klay.getMaxPriorityFeePerGas().send().getValue();
            BigInteger maxPriorityFeePerGas = new BigInteger(((EthereumDynamicFee) tx).getMaxPriorityFeePerGas());
            BigInteger maxFeePerGas = new BigInteger(((EthereumDynamicFee) tx).getMaxFeePerGas());
            if (mpfg.compareTo(maxPriorityFeePerGas) != 0) return false;
            // maxFeePerGas will be set with `baseFee * 2`, so maxFeePerGas can't be smaller than current base fee
            if (maxFeePerGas.compareTo(gasPrice) < 0) return false;
            return true;
        }

        BigInteger gasPriceInTx = new BigInteger(((ITransactionWithGasPriceField) tx).getGasPrice());
        if (gasPriceInTx.compareTo(gasPrice) < 0) return false;
        return true;
    }

    @Test
    @Ignore
    public void contractDeployTest() throws Exception {
        // Generate many txs to increase baseFee
        generateTxsBomb();

        String name = "Jamie";
        String symbol = "JME";
        BigInteger decimals = BigInteger.valueOf(18);
        BigInteger initialSupply = BigInteger.valueOf(100_000).multiply(BigInteger.TEN.pow(decimals.intValue())); // 100000 * 10^18

        Contract contract = caver.contract.create(ContractTest.jsonObj);
        SendOptions sendOptions = new SendOptions(sender.getAddress(), BigInteger.valueOf(6500000));

        // Mock constructor method to validate receipt
        Contract contractSpy = Mockito.spy(contract);
        Mockito.when(contractSpy.getMethod("constructor").send(any())).thenAnswer(i -> {
            TransactionReceipt.TransactionReceiptData receipt = (TransactionReceipt.TransactionReceiptData) i.getArguments()[0];
            boolean isValid = validateGasFeeWithReceipt(receipt);
            assertEquals(isValid, true);
            return receipt;
        });

        contract.deploy(sendOptions, ContractTest.BINARY, name, symbol, decimals, initialSupply);
        assertNotNull(contract.getContractAddress());
    }

    @Test
    @Ignore
    public void contractSendTest() throws Exception {
        // Generate many txs to increase baseFee
        generateTxsBomb();

        SendOptions sendOptions = new SendOptions(sender.getAddress(), BigInteger.valueOf(6500000));

        Contract contract = caver.contract.create(ContractTest.jsonObj, kip7Address);
        TransactionReceipt.TransactionReceiptData receipt = contract.send(sendOptions, "transfer", feePayer.getAddress(), BigInteger.ONE);

        boolean isValid = validateGasFeeWithReceipt(receipt);
        assertEquals(isValid, true);
    }

    @Test
    @Ignore
    public void contractSignTest() throws Exception {
        // Generate many txs to increase baseFee
        generateTxsBomb();

        SendOptions sendOptions = new SendOptions(sender.getAddress(), BigInteger.valueOf(6500000));

        Contract contract = caver.contract.create(ContractTest.jsonObj, kip7Address);
        AbstractTransaction signedTx = contract.sign(sendOptions, "transfer", feePayer.getAddress(), BigInteger.ONE);

        boolean isValid = validateGasPriceInTx(signedTx);
        assertEquals(isValid, true);
    }

    @Test
    @Ignore
    public void contractSignAsFeePayerTest() throws Exception {
        // Generate many txs to increase baseFee
        generateTxsBomb();

        SendOptions sendOptions = new SendOptions();
        sendOptions.setFrom(sender.getAddress());
        sendOptions.setGas(BigInteger.valueOf(6500000));
        sendOptions.setFeeDelegation(true);
        sendOptions.setFeePayer(feePayer.getAddress());

        Contract contract = caver.contract.create(ContractTest.jsonObj, kip7Address);
        AbstractFeeDelegatedTransaction signedTx = contract.signAsFeePayer(sendOptions, "transfer", feePayer.getAddress(), BigInteger.ONE);

        boolean isValid = validateGasPriceInTx(signedTx);
        assertEquals(isValid, true);
    }

    @Test
    @Ignore
    public void sign() throws IOException, TransactionException {
        generateTxsBomb();

        ValueTransfer tx = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
        );
        tx.sign(sender);
        assertFalse(validateGasPriceInTx(tx));

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(tx).send();
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
        assertFalse(validateGasFeeWithReceipt(receiptData));
    }

    @Test
    @Ignore
    public void ethereumDynamicFeeSign() throws IOException {
        generateTxsBomb();

        EthereumDynamicFee tx = caver.transaction.ethereumDynamicFee.create(
                TxPropertyBuilder.ethereumDynamicFee()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
        );
        tx.sign(sender);
        assertFalse(validateGasPriceInTx(tx));
    }

    @Test
    @Ignore
    public void signAsFeePayer() throws IOException, TransactionException {
        generateTxsBomb();

        FeeDelegatedValueTransfer tx = caver.transaction.feeDelegatedValueTransfer.create(
                TxPropertyBuilder.feeDelegatedValueTransfer()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
                        .setFeePayer(feePayer.getAddress())
        );
        tx.signAsFeePayer(feePayer);
        tx.sign(sender);
        assertFalse(validateGasPriceInTx(tx));

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(tx).send();
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
        assertFalse(validateGasFeeWithReceipt(receiptData));
    }

    @Test
    @Ignore
    public void walletSign() throws IOException, TransactionException {
        generateTxsBomb();

        ValueTransfer tx = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
        );
        caver.wallet.sign(sender.getAddress(), tx);
        assertFalse(validateGasPriceInTx(tx));

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(tx).send();
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
        assertFalse(validateGasFeeWithReceipt(receiptData));
    }

    @Test
    @Ignore
    public void ethereumDynamicFeeWalletSign() throws IOException {
        generateTxsBomb();

        EthereumDynamicFee tx = caver.transaction.ethereumDynamicFee.create(
                TxPropertyBuilder.ethereumDynamicFee()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
        );
        caver.wallet.sign(sender.getAddress(), tx);
        assertFalse(validateGasPriceInTx(tx));
    }

    @Test
    @Ignore
    public void walletSignAsFeePayer() throws IOException, TransactionException {
        generateTxsBomb();

        FeeDelegatedValueTransfer tx = caver.transaction.feeDelegatedValueTransfer.create(
                TxPropertyBuilder.feeDelegatedValueTransfer()
                        .setFrom(sender.getAddress())
                        .setTo(caver.wallet.keyring.generate().getAddress())
                        .setValue(BigInteger.ONE)
                        .setGas(BigInteger.valueOf(2500000))
                        .setFeePayer(feePayer.getAddress())
        );
        caver.wallet.signAsFeePayer(feePayer.getAddress(), tx);
        caver.wallet.sign(sender.getAddress(), tx);
        assertFalse(validateGasPriceInTx(tx));

        Bytes32 txHash = caver.rpc.klay.sendRawTransaction(tx).send();
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash.getResult());
        assertFalse(validateGasFeeWithReceipt(receiptData));
    }
}
