package com.klaytn.caver.ipfs;

import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IPFSTest {

    @Test
    public void fromHex() {
        String multiHashData = "12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";
        String expectedEncodedString = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";

        assertEquals(IPFS.fromHex(multiHashData), expectedEncodedString);
    }

    @Test
    public void toHex() {
        String encodedMultiHash = "QmYtUc4iTCbbfVSDNKvtQqrfyezPPnFvE33wFmutw9PBBk";
        String expectedMultiHashData = "12209cbc07c3f991725836a3aa2a581ca2029198aa420b9d99bc0e131d9f3e2cbe47";

        assertEquals(IPFS.toHex(encodedMultiHash), expectedMultiHashData);
    }

    @Test
    public void add() throws IOException {
        IPFS ipfs = new IPFS("ipfs.infura.io", 5001, true);

        String encodedHash = ipfs.add("hello.txt");

        System.out.println(encodedHash);
        assertNotNull(encodedHash);
    }

    @Test
    public void get() throws IOException {
        String cid = "Qmd3QDGie6w35bdJvTnrGeH2Ubbg55WqEVMhPk3h7EArkV";
        IPFS ipfs = new IPFS("ipfs.infura.io", 5001, true);

        byte[] content = ipfs.get(cid);
        String data = new String(content);
        System.out.println(data);
    }
}
