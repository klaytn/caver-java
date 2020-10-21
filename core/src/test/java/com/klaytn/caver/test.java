package com.klaytn.caver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.base.Accounts;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransferMemo;
import com.klaytn.caver.wallet.keyring.KeyStore;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Test;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.LUMAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class test {

    public SingleKeyring loadKeyStore() throws CipherException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileReader file = new FileReader("./keystore.json");

        KeyStore store = mapper.readValue(file, KeyStore.class);
        SingleKeyring feePayer = (SingleKeyring)KeyringFactory.decrypt(store, "12345678!");

        return feePayer;
    }

    @Test
    public void createFeeDelegatedTx() {
        Caver caver = new Caver(Caver.MAINNET_URL);

        try {
            //2. Load keystore file
            SingleKeyring feePayer = loadKeyStore();

            //3. create fee delegated tx
            SingleKeyring sender = KeyringFactory.createFromPrivateKey("privateKey");

            caver.wallet.add(sender);
            caver.wallet.add(feePayer);

            FeeDelegatedValueTransfer feeDelegatedTx = new FeeDelegatedValueTransfer.Builder()
                    .setKlaytnCall(caver.rpc.klay)
                    .setFrom(sender.getAddress())
                    .setTo("0x176ff0344de49c04be577a3512b6991507647f72")
                    .setValue(BigInteger.valueOf(5))
                    .setGas(BigInteger.valueOf(50000))
                    .setFeePayer(feePayer.getAddress())
                    .build();

            caver.wallet.sign(sender.getAddress(), feeDelegatedTx);

            //4. sign fee payer's private key.
            caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedTx);

            //5. send transaction
            Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedTx).send();

            TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
            TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(response.getResult());
        } catch (Exception e) {
            //handle error
        }
    }

    @Test
    public void getNonce() throws IOException {
        Caver caver = new Caver(Caver.MAINNET_URL);
        caver.rpc.klay.getTransactionCount("0x176ff0344de49c04be577a3512b6991507647f72").send();
    }

    @Test
    public void getTest() {
        Caver caver = new Caver(Caver.MAINNET_URL);

        String address = "0x37223E5E41186A782e4A1F709829F521f43b18E5";
        SingleKeyring keyring = KeyringFactory.create(address, PrivateKey.generate().getPrivateKey());

        caver.wallet.add(keyring);
        SingleKeyring keyring1 = (SingleKeyring)caver.wallet.getKeyring(keyring.getAddress().toLowerCase());

        assertNotNull(keyring1);
    }

    @Test
    public void memoTest() throws IOException {
        Caver caver = new Caver(Caver.DEFAULT_URL);
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"));
        caver.wallet.add(KeyringFactory.createFromPrivateKey("0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"));

        ValueTransferMemo valueTransferMemo = new ValueTransferMemo.Builder()
                .setFrom(LUMAN.getAddress())
                .setTo(BRANDON.getAddress())
                .setValue("0x0")
                .setInput("0x11223344")
                .setKlaytnCall(caver.rpc.klay)
                .setGas("0x500000")
                .build();

        caver.wallet.sign(LUMAN.getAddress(), valueTransferMemo);
        caver.rpc.klay.sendRawTransaction(valueTransferMemo).send();
        caver.rpc.klay.getTransactionReceipt("0xa0550fbd171a5c84f9e66c798c6cf4c2c687bcd2ef7e1db2fcb8b95b36f3f13b").send();
    }

//    @Test
//    public void encodeABITest() {
//        Caver caver = new Caver(Caver.MAINNET_URL);
//
//        String abi = "abi";
//        String address = "address";
//        Contract contract = new Contract(caver, abi, address);
//
//        String encodedData = contract.getMethod("insertProduct").encodeABI(Arrays.asList(
//                product.getTransactionId(),
//                product.getQrCodeId(),
//                product.getRank(),
//                product.getColor(),
//                product.getSugarLevel(),
//                product.getAcidity(),
//                product.getWeight()
//        ));
//
//        FeeDelegatedSmartContractExecution feeDelegatedSmartContract = new FeeDelegatedSmartContractExecution.Builder()
//                .setKlaytnCall(caver.rpc.klay)
//                .setFrom(sender.getAddress())
//                .setTo(transactionSmartContractAddress)
//                .setInput(encodeData)
//                .setGas(BigInteger.valueOf(50000))
//                .setFeePayer(feePayer.getAddress())
//                .build();
//
//        caver.wallet.sign(sender.getAddress(), feeDelegatedSmartConstract);
//
//        //4. sign fee payer's private key.
//        caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedSmartConstract);
//
//        //5. send transaction
//        Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedSmartConstract).send();
//        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
//        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(response.getResult());
//
//    }

    private static final String ABIJson = "[{\"constant\":true,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]\n";


    @Test
    public void callContractFunction() {
        Caver caver = new Caver(Caver.DEFAULT_URL);

        try {
            Contract contract = new Contract(caver, ABIJson, "0x{address in hex}");
            List<Type> result = contract.getMethod("get").call(null, CallObject.createCallObject());
            System.out.println((String)result.get(0).getValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
