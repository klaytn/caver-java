package com.klaytn.caver.ipfs;

import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IPFSTest {

    static String text = "This is IPFS test.";
    static String fileName = "./ipfs.txt";

    public void createFile() {


        File file = new File(fileName);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(text);
            fw.flush();

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fromHex() {
        String multiHashData = "12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";
        String expectedEncodedString = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";

        assertEquals(IPFS.fromHex(multiHashData), expectedEncodedString);
    }

    @Test
    public void toHex() {
        String encodedMultiHash = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";
        String expectedMultiHashData = "0x12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";

        assertEquals(IPFS.toHex(encodedMultiHash), expectedMultiHashData);
    }

    @Test
    public void add() throws IOException {
        createFile();
        IPFS ipfs = new IPFS("ipfs.infura.io", 5001, true);

        String encodedHash = ipfs.add(fileName);
        assertNotNull(encodedHash);
    }

    @Test
    public void get() throws IOException {
        String cid = "QmYzW1fXbapdxkZXMQeCYoDCjVc18H8tLfMfrxXRySmQiq";
        IPFS ipfs = new IPFS("ipfs.infura.io", 5001, true);

        byte[] content = ipfs.get(cid);
        String data = new String(content);
        assertEquals(text, data);
    }
}
