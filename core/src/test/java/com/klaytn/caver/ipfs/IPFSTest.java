/*
 * Copyright 2021 The caver-java Authors
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

package com.klaytn.caver.ipfs;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.ValueTransferMemo;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import org.junit.Test;
import org.web3j.protocol.exceptions.TransactionException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IPFSTest {
    static String key_klayProvider = "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3";
    static String text = "This is IPFS test.";
    static String fileName = "./ipfs.txt";

    private String projectId = "";
    private String projectSecret = "";

    public void createFile(String fileName, String text) {
        File file = new File(fileName);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, false);
            fw.write(text);
            fw.flush();

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadENV() {
        if (this.projectId != "" && this.projectSecret != "") {
            return;
        }
        Dotenv env = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        this.projectId = System.getenv("infuraProjectId") == null? env.get("infuraProjectId") : System.getenv("infuraProjectId");
        this.projectSecret =  System.getenv("infuraProjectSecret") == null? env.get("infuraProjectSecret"): System.getenv("infuraProjectSecret");
    }

    public TransactionReceipt.TransactionReceiptData sendFileHash(Caver caver, String fileHash) throws IOException, TransactionException {
        SingleKeyring klayProvider_keyring = caver.wallet.keyring.createFromPrivateKey(key_klayProvider);
        caver.wallet.add(klayProvider_keyring);


        ValueTransferMemo tx = caver.transaction.valueTransferMemo.create(
                TxPropertyBuilder.valueTransferMemo()
                        .setFrom(klayProvider_keyring.getAddress())
                        .setTo(BRANDON.getAddress())
                        .setGas(BigInteger.valueOf(25000))
                        .setValue("0x00")
                        .setInput(fileHash)
        );

        caver.wallet.sign(klayProvider_keyring.getAddress(), tx);

        String txHash = caver.rpc.klay.sendRawTransaction(tx).send().getResult();
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash);
        return receiptData;
    }

    @Test
    public void fromHex() {
        Caver caver = new Caver();
        String multiHashData = "0x12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";
        String expectedEncodedString = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";

        assertEquals(caver.ipfs.fromHex(multiHashData), expectedEncodedString);
    }

    @Test
    public void toHex() {
        Caver caver = new Caver();
        String encodedMultiHash = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";
        String expectedMultiHashData = "0x12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";

        assertEquals(caver.ipfs.toHex(encodedMultiHash), expectedMultiHashData);
    }

    @Test
    public void addFile() throws IOException {
        createFile(fileName, text);
        loadENV();
        Caver caver = new Caver();
        IPFSOptions options = caver.ipfs.createOptions(this.projectId, this.projectSecret);
        caver.ipfs.setIPFSNode("ipfs.infura.io", 5001, true, options);

        String encodedHash = caver.ipfs.add(fileName);
        assertNotNull(encodedHash);
    }

    @Test
    public void addByteArray() throws IOException {
        byte[] data = text.getBytes();

        loadENV();
        Caver caver = new Caver();
        IPFSOptions options = IPFSOptions.createOptions(this.projectId, this.projectSecret);
        caver.ipfs.setIPFSNode("ipfs.infura.io", 5001, true, options);

        String cid = caver.ipfs.add(data);
        assertNotNull(cid);


       byte[] content = caver.ipfs.get(cid);
       assertEquals(text, new String(content));
    }

    @Test
    public void get() throws IOException {
        String cid = "QmYzW1fXbapdxkZXMQeCYoDCjVc18H8tLfMfrxXRySmQiq";

        loadENV();
        Caver caver = new Caver();
        IPFSOptions options = IPFSOptions.createOptions(this.projectId, this.projectSecret);
        caver.ipfs.setIPFSNode("ipfs.infura.io", 5001, true, options);

        byte[] content = caver.ipfs.get(cid);
        String data = new String(content);
        assertEquals(text, data);
    }

    @Test
    public void integrationTest() throws IOException, TransactionException {
        loadENV();

        Caver caver = new Caver();
        IPFSOptions options = IPFSOptions.createOptions(this.projectId, this.projectSecret);
        caver.ipfs.setIPFSNode("ipfs.infura.io", 5001, true, options);

        createFile(fileName, text);
        String encodedHash = caver.ipfs.add(fileName);

        TransactionReceipt.TransactionReceiptData receiptData = sendFileHash(caver, caver.ipfs.toHex(encodedHash));
        String multiHash = receiptData.getInput();

        String encoded = caver.ipfs.fromHex(multiHash);
        byte[] data = caver.ipfs.get(encoded);

        assertEquals(text, new String(data));
    }
}
