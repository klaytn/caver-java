/*
 * Copyright 2020 The caver-java Authors
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

import com.klaytn.caver.utils.Utils;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents uploading and getting file from IPFS.
 */
public class IPFS {

    /**
     * The IPFS instance.
     */
    io.ipfs.api.IPFS ipfs;


    public IPFS() {
    }

    /**
     * Creates an IPFS instance.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     */
    public IPFS(String host, int port, boolean ssl) {
        setIPFSNode(host, port, ssl);
    }

    /**
     * Decode encoded multi hash string with Base58 and add hex prefix(0x).
     * @param encodedHash A encoded string with Base58.
     * @return String
     */
    public static String toHex(String encodedHash) {
        Multihash multihash = Multihash.fromBase58(encodedHash);
        return Utils.addHexPrefix(multihash.toHex());
    }

    /**
     * Encode hex string with Base58 algorithm.
     * @param hexString A hex string to encode.
     * @return String
     */
    public static String fromHex(String hexString) {
        String noPrefixStr = Utils.stripHexPrefix(hexString);
        Multihash multihash = Multihash.fromHex(noPrefixStr);
        return multihash.toString();
    }

    /**
     * Add file to IPFS.
     * @param path A file path to add at IPFS.
     * @return String
     * @throws IOException
     */
    public String add(String path) throws IOException {
        NamedStreamable streamable = new NamedStreamable.FileWrapper(new File(path));
        return add(streamable);
    }

    /**
     * Add byte array to IPFS
     * @param content A byte array to add at IPFS
     * @return String
     * @throws IOException
     */
    public String add(byte[] content) throws IOException {
        NamedStreamable streamable = new NamedStreamable.ByteArrayWrapper(content);
        return add(streamable);
    }

    /**
     * Get file from IPFS.
     * @param encodedHash A encoded multi hash string with base58.
     * @return byte[]
     * @throws IOException
     */
    public byte[] get(String encodedHash) throws IOException {
        Multihash multihash = Multihash.fromBase58(encodedHash);
        return this.ipfs.cat(multihash);
    }

    /**
     * Set a IPFS node.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     */
    public void setIPFSNode(String host, int port, boolean ssl) {
        this.ipfs = new io.ipfs.api.IPFS(host, port, "/api/v0/", ssl);;
    }


    /**
     * Add stream data to IPFS
     * @param streamable The streamable data
     * @return String
     * @throws IOException
     */
    private String add(NamedStreamable streamable) throws IOException {
        List<MerkleNode> nodeList = ipfs.add(streamable);
        return nodeList.get(0).hash.toString();
    }
}
