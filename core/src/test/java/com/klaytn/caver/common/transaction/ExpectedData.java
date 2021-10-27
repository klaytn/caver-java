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

package com.klaytn.caver.common.transaction;

import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdate;
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdateWithRatio;

public class ExpectedData {
    String expectedRLP;
    String expectedTxHash;
    String expectedRlpEncodingForSigning;

    FeeDelegatedAccountUpdate.Builder fdauBuilder;
    FeeDelegatedAccountUpdateWithRatio.Builder fdauwrBuilder;
    String expectedRLPEncoding;
    String expectedTransactionHash;
    String expectedSenderTransactionHash;
    String expectedRLPEncodingForFeePayerSigning;

    public ExpectedData(String expectedRLP, String expectedTxHash, String expectedRlpEncodingForSigning) {
        this.expectedRLP = expectedRLP;
        this.expectedTxHash = expectedTxHash;
        this.expectedRlpEncodingForSigning = expectedRlpEncodingForSigning;
    }

    public ExpectedData(FeeDelegatedAccountUpdate.Builder builder, String expectedRLPEncoding, String expectedTransactionHash, String expectedSenderTransactionHash, String expectedRLPEncodingForFeePayerSigning) {
        this.fdauBuilder = builder;
        this.expectedRLPEncoding = expectedRLPEncoding;
        this.expectedTransactionHash = expectedTransactionHash;
        this.expectedSenderTransactionHash = expectedSenderTransactionHash;
        this.expectedRLPEncodingForFeePayerSigning = expectedRLPEncodingForFeePayerSigning;
    }

    public ExpectedData(FeeDelegatedAccountUpdateWithRatio.Builder builder, String expectedRLPEncoding, String expectedTransactionHash, String expectedSenderTransactionHash, String expectedRLPEncodingForFeePayerSigning) {
        this.fdauwrBuilder = builder;
        this.expectedRLPEncoding = expectedRLPEncoding;
        this.expectedTransactionHash = expectedTransactionHash;
        this.expectedSenderTransactionHash = expectedSenderTransactionHash;
        this.expectedRLPEncodingForFeePayerSigning = expectedRLPEncodingForFeePayerSigning;
    }

    public FeeDelegatedAccountUpdate.Builder getFDAUBuilder() {
        return fdauBuilder;
    }

    public FeeDelegatedAccountUpdateWithRatio.Builder getFDAUWRBuilder() {
        return fdauwrBuilder;
    }

    public String getExpectedRLPEncoding() {
        return expectedRLPEncoding;
    }

    public void setExpectedRLPEncoding(String expectedRLPEncoding) {
        this.expectedRLPEncoding = expectedRLPEncoding;
    }

    public String getExpectedTransactionHash() {
        return expectedTransactionHash;
    }

    public String getExpectedSenderTransactionHash() {
        return expectedSenderTransactionHash;
    }

    public String getExpectedRLPEncodingForFeePayerSigning() {
        return expectedRLPEncodingForFeePayerSigning;
    }

    public static ExpectedData getExpectedDataLegacy() {
        return new ExpectedData(
                "0x20f86c808505d21dba0083030d4094dca786ce39b074966e8a9eae16eac90783974d808201c0f847f845820feaa0866f7cf552d4062a3c1a6055cabbe358a21ce779cfe2b81cee87b66024b993afa02990dc2d9d36cc4de4b9a79c30aeab8d59e2d60631e0d90c8ac3c096b7a38852",
                "0xeea281154fc4000f01b47a5a6f0c2caa1481cbc9ef935cc8c35a5f006f8d97a6",
                "0xe420808505d21dba0083030d4094dca786ce39b074966e8a9eae16eac90783974d808201c0"
        );
    }

    public static ExpectedData getExpectedDataPublic() {
        return new ExpectedData(
                "0x20f88d808505d21dba0083030d4094ffb52bc54635f840013e142ebe7c06c9c91c1625a302a102c93fcbdb2b9dbef8ee5c4748ffdce11f1f5b06d7ba71cc2b7699e38be7698d1ef847f845820fe9a09c2ca281e94567846acbeef724b1a7a5f882d581aff9984755abd92272592b8ea0344fd23d7774ae9c227809bb579387dfcd69e74ae2fe3a788617f54a4001e5ab",
                "0x0c52c7e1d67da8221df26fa7ac01f33d87f46dc706844804f378cebe2e66c432",
                "0xf84520808505d21dba0083030d4094ffb52bc54635f840013e142ebe7c06c9c91c1625a302a102c93fcbdb2b9dbef8ee5c4748ffdce11f1f5b06d7ba71cc2b7699e38be7698d1e"
        );
    }

    public static ExpectedData getExpectedDataFail() {
        return new ExpectedData(
                "0x20f86c808505d21dba0083030d409426b05cce63f78ddf6a769fb2db39e54b9f2db6208203c0f847f845820fe9a086361c43593859b6989794a6848c5ba1e5d8bd860522347cd167042acd6a7816a0773f5cc10f734b3b4486b9c5b7e5def156e06d9d9f4a3aaae6662f9a2126094c",
                "0xfb6053ce6d0321eebcdbce2c123fd501bc38ab6bcf74a34001663a56d227cd92",
                "0xe420808505d21dba0083030d409426b05cce63f78ddf6a769fb2db39e54b9f2db6208203c0"
        );
    }

    public static ExpectedData getExpectedDataMultiSig() {
        return new ExpectedData(
                "0x20f8dd808505d21dba0083030d40942dcd60f120bd64e35093a2945ce61c0bcb71dc93b87204f86f02f86ce301a102e1c4bb4d01245ebdc62a88092f6c79b59d56e319ae694050e7a0c1cff93a0d92e301a10313853532348457b4fb18526c6447a6cdff38a791dc2e778f19a843fc6b3a3e8de301a102e0f3c6f28dc933ac3cf7fc3143f0d38bc83aa9541ce7bb67c356cad5c9b020a3f847f845820fe9a002aca4ec6773a26c71340c2500cb45886a61797bcd82790f7f01150ced48b0aca020502f22a1b3c95a5f260a03dc3de0eaa1f4a618b1d2a7d4da643507302e523c",
                "0x6b67ca5e8f1ef46e009348541d0866dbb2902b75a4dccb3b7286d6987f556b44",
                "0xf89520808505d21dba0083030d40942dcd60f120bd64e35093a2945ce61c0bcb71dc93b87204f86f02f86ce301a102e1c4bb4d01245ebdc62a88092f6c79b59d56e319ae694050e7a0c1cff93a0d92e301a10313853532348457b4fb18526c6447a6cdff38a791dc2e778f19a843fc6b3a3e8de301a102e0f3c6f28dc933ac3cf7fc3143f0d38bc83aa9541ce7bb67c356cad5c9b020a3"
        );
    }

    public static ExpectedData getExpectedDataRoleBased() {
        return new ExpectedData(
                "0x20f90156808505d21dba0083030d4094fb675bea5c3fa279fd21572161b6b6b2dbd84233b8eb05f8e8b87204f86f02f86ce301a102f7e7e03c328d39cee6201080ac2576919f904f0b8e47fcb7ea8869e7db0baf44e301a103edacd9095274f292c702514f6443f58337e7d7c8311694f31c73e86f150ecf45e301a102b74fd682a6a805415e7711890bc91a283c268c78947ebf25a02a2e02625a68aaa302a102d0ae803893f344ee664378bbc9ebb35ca2d94f7d7ecea4e3e2f9f33817cdb04bb84e04f84b01f848e301a1024b4cd35195aa4324184a64821e514a991b513cc354f5fa6d78fb99e23949bc59e301a1033e65f4a76bca1488a1a046d6976778852aa41f07156d2c42e81c3da6621435d2f847f845820fe9a066e28c27f16ba34325770e842874d07473180bcec22e86851a6882acbaeb56c3a0761e12fe11003aa4cb8fd9b44a41e5edebeb943cc366264b345d0f7e63853724",
                "0x57cdfb7b92c16608b467c28e6519f66ef89923046fce37e086baa1f5775ef312",
                "0xf9010e20808505d21dba0083030d4094fb675bea5c3fa279fd21572161b6b6b2dbd84233b8eb05f8e8b87204f86f02f86ce301a102f7e7e03c328d39cee6201080ac2576919f904f0b8e47fcb7ea8869e7db0baf44e301a103edacd9095274f292c702514f6443f58337e7d7c8311694f31c73e86f150ecf45e301a102b74fd682a6a805415e7711890bc91a283c268c78947ebf25a02a2e02625a68aaa302a102d0ae803893f344ee664378bbc9ebb35ca2d94f7d7ecea4e3e2f9f33817cdb04bb84e04f84b01f848e301a1024b4cd35195aa4324184a64821e514a991b513cc354f5fa6d78fb99e23949bc59e301a1033e65f4a76bca1488a1a046d6976778852aa41f07156d2c42e81c3da6621435d2"
        );
    }

    public String getExpectedRLP() {
        return expectedRLP;
    }

    public String getExpectedTxHash() {
        return expectedTxHash;
    }

    public String getExpectedRlpEncodingForSigning() {
        return expectedRlpEncodingForSigning;
    }
}