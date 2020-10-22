package com.klaytn.caver.ipfs;

import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IPFS {
    io.ipfs.api.IPFS ipfs;

    public IPFS(String host, int port, boolean ssl) {
        ipfs = new io.ipfs.api.IPFS(host, port, "/api/v0/", ssl);
    }

    public static String toHex(String encodedHash) {
        Multihash multihash = Multihash.fromBase58(encodedHash);
        return multihash.toHex();
    }

    public static String fromHex(String hexString) {
        Multihash multihash = Multihash.fromHex(hexString);
        return multihash.toString();
    }

    public String add(String path) throws IOException {
        NamedStreamable streamable = new NamedStreamable.FileWrapper(new File(path));
        List<MerkleNode> nodeList = ipfs.add(streamable);

        return nodeList.get(0).hash.toString();
    }

    public byte[] get(String encodedHash) throws IOException {
        Multihash multihash = Multihash.fromBase58(encodedHash);
        return this.ipfs.cat(multihash);
    }
}
