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

package com.klaytn.caver.ipfs.wrapper;

import com.klaytn.caver.ipfs.IPFS;

import java.io.IOException;

/**
 * Representing an IPFSWrapper
 * 1. This class should be accessed through `caver.ipfs`
 * 2. This class wraps all public methods (including static methods) of IPFS
 */
public class IPFSWrapper {
    /**
     * The IPFS instance
     */
    private IPFS ipfs;

    /**
     * Creates an IPFSWrapper instance
     */
    public IPFSWrapper() {
        this.ipfs = new IPFS();
    }

    /**
     * Decode encoded multi hash string with Base58 and add hex prefix(0x).
     * @param encodedHash A encoded string with Base58.
     * @return String
     */
    public String toHex(String encodedHash) {
        return IPFS.toHex(encodedHash);
    }

    /**
     * Encode hex string with Base58 algorithm.
     * @param hexString A hex string to encode.
     * @return String
     */
    public String fromHex(String hexString) {
        return IPFS.fromHex(hexString);
    }

    /**
     * Add file to IPFS.
     * @param path A file path to add at IPFS.
     * @return String
     * @throws IOException
     */
    public String add(String path) throws IOException {
        return this.ipfs.add(path);
    }

    /**
     * Add byte array to IPFS
     * @param content A byte array to add at IPFS
     * @return String
     * @throws IOException
     */
    public String add(byte[] content) throws IOException {
        return this.ipfs.add(content);
    }

    /**
     * Get file from IPFS.
     * @param encodedHash A encoded multi hash string with base58.
     * @return byte[]
     * @throws IOException
     */
    public byte[] get(String encodedHash) throws IOException {
        return this.ipfs.get(encodedHash);
    }

    /**
     * Set a IPFS node.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     */
    public void setIPFSNode(String host, int port, boolean ssl) {
        this.ipfs.setIPFSNode(host, port, ssl);
    }
}
