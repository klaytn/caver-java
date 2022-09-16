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
import io.ipfs.api.JSONParser;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.Multipart;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents uploading and getting file from IPFS.
 */
public class IPFS {

   /**
    * The IPFS instance.
    */
   io.ipfs.api.IPFS ipfs;

    /**
     * The IPFSOptions instance.
     */
    IPFSOptions options;

    private String protocol = "";
    private String host = "";
    private int port = -1;
    private String version = "/api/v0/";


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
     * Creates an IPFS instance.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     * @param options An object contains configuration variables.
     */
    public IPFS(String host, int port, boolean ssl, IPFSOptions options) {
        setIPFSNode(host, port, ssl, options);
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
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        return add(bufferedReader.readLine().getBytes());
    }

    /**
     * Add byte array to IPFS.
     * @param content A byte array to add at IPFS
     * @return String
     * @throws IOException
     */
    public String add(byte[] content) throws IOException {
        // TODO: When IPFS library support setting basic auth, this function logic should be replaced
        URL target = new URL(this.protocol + "://" + this.host + ":"  + this.port + "/api/v0/add?stream-channels=true&progress=false&format=UTF-8");
        String boundary = Multipart.createBoundary();

        // Make a HttpURLConnection with basic auth (if auth is defined in IPFSOptions)
        HttpURLConnection conn = configureConnection(target, "POST", this.options);

        // This logic came from Multipart construction
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // From below logic is came from addFilePart function of the Multipart.java
        OutputStream out = conn.getOutputStream();
        out.write("--".getBytes("UTF-8"));
        out.write(boundary.getBytes("UTF-8"));
        addLineFeed(out);
        out.write("Content-Disposition: file; file=\"file\"".getBytes("UTF-8"));
        addLineFeed(out);
        out.write("Content-Type: application/octet-stream".getBytes("UTF-8"));
        addLineFeed(out);
        out.write("Content-Transfer-Encoding: binary".getBytes("UTF-8"));
        addLineFeed(out);
        addLineFeed(out);
        out.flush();

        NamedStreamable streamable = new NamedStreamable.ByteArrayWrapper(content);
        try {
            InputStream inputStream = streamable.getInputStream();
            byte[] buffer = new byte[4096];
            int r;
            while ((r = inputStream.read(buffer)) != -1)
                out.write(buffer, 0, r);
            out.flush();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        out.write("\r\n".getBytes("UTF-8"));
        out.flush();

        // Below logic is came from finish function of the Multipart
        out.write(("--" + boundary + "--").getBytes("UTF-8"));
        out.flush();
        out.close();

        StringBuilder b = new StringBuilder();
        int status = conn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                b.append(line);
            }
            reader.close();
            conn.disconnect();
        } else {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    b.append(line);
                }
                reader.close();
            } catch (Throwable t) {
            }
            throw new IOException("Server returned status: " + status + " with body: " + b.toString() + " and Trailer header: " + conn.getHeaderFields().get("Trailer"));
        }
        String res = b.toString();

        return JSONParser.parseStream(res).stream()
                .map(x -> MerkleNode.fromJSON((Map<String, Object>) x))
                .collect(Collectors.toList()).get(0).hash.toString();
    }

    /**
     * Get file from IPFS.
     * @param encodedHash A encoded multi hash string with base58.
     * @return byte[]
     * @throws IOException
     */
    public byte[] get(String encodedHash) throws IOException {
        // TODO: When IPFS library support setting basic auth, this function logic should be replaced
        Multihash multihash = Multihash.fromBase58(encodedHash);
        String path = "cat?arg=" + multihash;
        URL target = new URL(this.protocol, this.host, this.port, this.version + path);

        HttpURLConnection conn = configureConnection(target, "POST", this.options);

        try {
            OutputStream out = conn.getOutputStream();
            out.write(new byte[0]);
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream resp = new ByteArrayOutputStream();

            byte[] buf = new byte[4096];
            int r;
            while ((r = in.read(buf)) >= 0)
                resp.write(buf, 0, r);
            return resp.toByteArray();
        } catch (ConnectException e) {
            throw new RuntimeException("Couldn't connect to IPFS daemon at "+target+"\n Is IPFS running?");
        } catch (IOException e) {
            InputStream errorStream = conn.getErrorStream();
            String err = errorStream == null ? e.getMessage() : new String(readFully(errorStream));
            throw new RuntimeException("IOException contacting IPFS daemon.\n"+err+"\nTrailer: " + conn.getHeaderFields().get("Trailer"), e);
        }
    }

    /**
     * Set a IPFS node.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     */
    public void setIPFSNode(String host, int port, boolean ssl) {
        setIPFSNode(host, port, ssl, null);
    }

    /**
     * Set a IPFS node.
     * @param host The host url.
     * @param port The port number to use.
     * @param ssl either using ssl or not.
     * @param options An object contains configuration variables.
     */
    public void setIPFSNode(String host, int port, boolean ssl, IPFSOptions options) {
        this.host = host;
        this.port = port;
        this.options = options;
        if (ssl) {
            this.protocol = "https";
        } else {
            this.protocol = "http";
        }
    }

    private static final byte[] readFully(InputStream in) {
        try {
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int r;
            while ((r=in.read(buf)) >= 0)
                resp.write(buf, 0, r);
            return resp.toByteArray();

        } catch(IOException ex) {
            throw new RuntimeException("Error reading InputStrean", ex);
        }
    }

    private static HttpURLConnection configureConnection(URL target, String method, IPFSOptions options) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) target.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        // If options is not null, set request header properties
        if (options != null) {
            for (String key: options.headers.keySet())
                conn.setRequestProperty(key, options.headers.get(key));
        }
        conn.setDoOutput(true);
        return conn;
    }

    private void addLineFeed(OutputStream out) throws IOException {
        out.write("\r\n".getBytes("UTF-8"));
    }
}
