package com.klaytn.caver;

import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.SmartContractDeploy;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.utils.Utils;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Before;
import org.web3j.protocol.core.BatchRequest;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class DynamicFeeTest {
    static Caver caver;
    static SingleKeyring rich, sender, feePayer;

    @Before
    public void setUp() throws Exception {
        caver = new Caver(Caver.DEFAULT_URL);
        rich = caver.wallet.keyring.createFromPrivateKey("0x871ccee7755bb4247e783110cafa6437f9f593a1eaeebe0efcc1b0852282c3e5");
        caver.wallet.add(rich);

        sender = caver.wallet.keyring.generate();
        fillKlay(sender, "200");
        feePayer = caver.wallet.keyring.generate();
        fillKlay(feePayer, "200");
    }

    // rich sends enough KLAY to an account.
    private void fillKlay(AbstractKeyring keyring, String amount) throws IOException {
        caver.wallet.add(keyring);
        ValueTransfer valueTransfer = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                        .setFrom(rich.getAddress())
                        .setTo(sender.getAddress())
                        .setValue(caver.utils.convertToPeb(amount, Utils.KlayUnit.KLAY))
                        .setGas(BigInteger.valueOf(6000000))
        );
        caver.wallet.sign(rich.getAddress(), valueTransfer);
        caver.rpc.klay.sendRawTransaction(valueTransfer).send();
    }

    private void generateTxsBomb(int num) throws IOException {
        if (num == 0) {
            num = 100;
        }
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
            senderNonce = senderNonce.add(BigInteger.valueOf(1));
            caver.wallet.sign(sender.getAddress(), smartContractDeploy);
            String rawTx = smartContractDeploy.getRLPEncoding();
            batchRequest.add(caver.rpc.klay.sendRawTransaction(rawTx));
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
}
