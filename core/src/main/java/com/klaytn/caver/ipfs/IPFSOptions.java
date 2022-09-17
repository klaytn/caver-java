package com.klaytn.caver.ipfs;

import okhttp3.Credentials;

import java.util.HashMap;
import java.util.Map;

public class IPFSOptions {
    Map<String, String> headers;

    public static IPFSOptions createOptions(String projectId, String projectSecret) {
        IPFSOptions options = new IPFSOptions();
        options.headers = new HashMap<String, String>();

        options.headers.put("Authorization", Credentials.basic(projectId, projectSecret));
        return options;
    }
}
