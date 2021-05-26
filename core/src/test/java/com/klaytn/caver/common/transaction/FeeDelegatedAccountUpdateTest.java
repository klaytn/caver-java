package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.type.FeeDelegatedAccountUpdate;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.wallet.keyring.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({FeeDelegatedAccountUpdateTest.createInstanceBuilder.class, FeeDelegatedAccountUpdateTest.createInstance.class, FeeDelegatedAccountUpdateTest.getRLPEncodingTest.class, FeeDelegatedAccountUpdateTest.signAsFeePayer_OneKeyTest.class, FeeDelegatedAccountUpdateTest.signAsFeePayer_AllKeyTest.class, FeeDelegatedAccountUpdateTest.appendFeePayerSignaturesTest.class, FeeDelegatedAccountUpdateTest.combineSignatureTest.class, FeeDelegatedAccountUpdateTest.getRawTransactionTest.class, FeeDelegatedAccountUpdateTest.getTransactionHashTest.class, FeeDelegatedAccountUpdateTest.getSenderTxHashTest.class, FeeDelegatedAccountUpdateTest.getRLPEncodingForFeePayerSignatureTest.class})
public class FeeDelegatedAccountUpdateTest {
    static Caver caver = new Caver(Caver.DEFAULT_URL);

    static ExpectedData setLegacyData() {
        String from = "0xac1aec09ef5f8dde6a0baf709ea388bbd7965f72";
        Account account = Account.createWithAccountKeyLegacy(from);

        String gas = "0x493e0";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        SignatureData senderSignatureData = new SignatureData(
                "0x0fe9",
                "0xd10f11309a670e133a408b7ebccf277e57af7d0701d5d811daec0dd8025ad961",
                "0x123e6dc3ca7a3603de954297fdf3d308c7ecdd43023f61121394182316313f82"
        );
        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        SignatureData feePayerSignatureData = new SignatureData(
                "0x0fe9",
                "0x6d13eff0efc972b0ecf89c65e502eda4f97ebbd5cbcedcaa99af0c063b0d59cf",
                "0x4cc0be553bb13a4549eae8f2b140282e1baf29728ae118f2f575e53a25cc305b"
        );
        String chainID = "0x7e3";

        FeeDelegatedAccountUpdate.Builder builder = new FeeDelegatedAccountUpdate.Builder()
                .setFrom(from)
                .setAccount(account)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setAccount(account)
                .setSignatures(senderSignatureData)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignatureData)
                .setChainId(chainID);

        String expectedRLPEncoding = "0x21f8ca808505d21dba00830493e094ac1aec09ef5f8dde6a0baf709ea388bbd7965f728201c0f847f845820fe9a0d10f11309a670e133a408b7ebccf277e57af7d0701d5d811daec0dd8025ad961a0123e6dc3ca7a3603de954297fdf3d308c7ecdd43023f61121394182316313f829423bf3d4eb274621e56ce65f6fa05da9e24785bb8f847f845820fe9a06d13eff0efc972b0ecf89c65e502eda4f97ebbd5cbcedcaa99af0c063b0d59cfa04cc0be553bb13a4549eae8f2b140282e1baf29728ae118f2f575e53a25cc305b";
        String expectedRLPTransactionHash = "0x4ef87ddc379e003ff736085cd98842c085712b3218402052e318e12da752f810";
        String expectedRLPSenderTransactionHash = "0xed17e28df6ed7ad8ae1b7fb25e1a81ce03c5b145fd62ea2281c866bd1a39ba43";
        String expectedRLPEncodingForFeePayerSigning = "0xf840a5e421808505d21dba00830493e094ac1aec09ef5f8dde6a0baf709ea388bbd7965f728201c09423bf3d4eb274621e56ce65f6fa05da9e24785bb88207e38080";

        ExpectedData expectedData = new ExpectedData(builder, expectedRLPEncoding, expectedRLPTransactionHash, expectedRLPSenderTransactionHash, expectedRLPEncodingForFeePayerSigning);

        return expectedData;
    }

    static ExpectedData setAccountPublic() {
        String from = "0xac1aec09ef5f8dde6a0baf709ea388bbd7965f72";
        String publicKey = "0xd032771e5d927fb568cdf7605496b700277d7b9bcabe7657f45602348964e3963e290efde1cb8d1204659548bd50824cfa4b4d5199c66dbcceb3fb8de7f8b5b9";
        Account account = Account.createWithAccountKeyPublic(from, publicKey);

        String gas = "0x493e0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        SignatureData senderSignatureData = new SignatureData(
                "0x0fe9",
                "0x0e1a3542288951226c66e6e8de320ddef4e0c0d6650baec828998a7ce411fe",
                "0x52d0766f3b84f35787d2a810f97057d215dcbe070cd890b7ccb8aaa3aac8eacc"
        );

        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        SignatureData feePayerSignatureData = new SignatureData(
                "0x0fea",
                "0xfaca4cf91418c6fea61e9439620b656c7b0717b058fd8787865f4564a0f9974e",
                "0x3a483582435426e7b2aeffe3131a678ae54c7aa948fa5442b5ded209ba373221"
        );

        String chainID = "0x7e3";

        FeeDelegatedAccountUpdate.Builder builder = new FeeDelegatedAccountUpdate.Builder()
                .setFrom(from)
                .setAccount(account)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setAccount(account)
                .setSignatures(senderSignatureData)
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignatureData)
                .setChainId(chainID);

        String expectedRLPEncoding = "0x21f8ea018505d21dba00830493e094ac1aec09ef5f8dde6a0baf709ea388bbd7965f72a302a103d032771e5d927fb568cdf7605496b700277d7b9bcabe7657f45602348964e396f846f844820fe99f0e1a3542288951226c66e6e8de320ddef4e0c0d6650baec828998a7ce411fea052d0766f3b84f35787d2a810f97057d215dcbe070cd890b7ccb8aaa3aac8eacc9423bf3d4eb274621e56ce65f6fa05da9e24785bb8f847f845820feaa0faca4cf91418c6fea61e9439620b656c7b0717b058fd8787865f4564a0f9974ea03a483582435426e7b2aeffe3131a678ae54c7aa948fa5442b5ded209ba373221";
        String expectedRLPTransactionHash = "0xe939f967e154f9d3400e9042fb9d0c58bcee914274d883aac8d2fe8aec8d56e9";
        String expectedRLPSenderTransactionHash = "0x9fee61b7611d069477b9dec87f58cec7ef96d2559e441f7baf3f4468b3669784";
        String expectedRLPEncodingForFeePayerSigning = "0xf863b847f84521018505d21dba00830493e094ac1aec09ef5f8dde6a0baf709ea388bbd7965f72a302a103d032771e5d927fb568cdf7605496b700277d7b9bcabe7657f45602348964e3969423bf3d4eb274621e56ce65f6fa05da9e24785bb88207e38080";

        ExpectedData expectedData = new ExpectedData(builder, expectedRLPEncoding, expectedRLPTransactionHash, expectedRLPSenderTransactionHash, expectedRLPEncodingForFeePayerSigning);

        return expectedData;
    }

    static ExpectedData setAccountKeyFail() {
        String from = "0xac1aec09ef5f8dde6a0baf709ea388bbd7965f72";
        Account account = Account.createWithAccountKeyFail(from);

        String gas = "0x186a0";
        String nonce = "0x4";
        String gasPrice = "0x5d21dba00";
        SignatureData[] senderSignatureData = new SignatureData[]{
                new SignatureData(
                        "0x0fea",
                        "0x31115ed4fdf3cdc9c3071be0c14a992d1ca70b02382b3fadb2428a60f411edcb",
                        "0x33530406db890a6c144759f72977739edfe2f9718966de52d6dada0a70be27ae"
                ),
                new SignatureData(
                        "0x0fe9",
                        "0x2b50229dc8d4c18414d1f8f943658ca22e6c08a57f52e7e77910d01c1bb49286",
                        "0x795fc16b6327c41593eb9b7ea5bbf2c61e3b4b3e5cd3176d3ebd57007fa4842b"
                ),
                new SignatureData(
                        "0x0fea",
                        "0xd5e3d18cd12fb36180adad4df5b9f15089ac9f8448e7a7eeb0fc0f6497faa98d",
                        "0x246503976acea8e9a91e630963eaa344b193fc4e747c1ccaae253083d2695e96"
                ),
        };

        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        SignatureData feePayerSignatureData = new SignatureData(
                "0x0fea",
                "0x4dfc5650bf203652b440ba53791f0adc07c2fae0e746efbda2b5117c568474d3",
                "0x72bea336107273fd0e1624d4f70425bb040e59e8f2b5d856fc22ab898c2b156e"
        );

        String chainID = "0x7e3";

        FeeDelegatedAccountUpdate.Builder builder = new FeeDelegatedAccountUpdate.Builder()
                .setFrom(from)
                .setAccount(account)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setAccount(account)
                .setSignatures(Arrays.asList(senderSignatureData))
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignatureData)
                .setChainId(chainID);

        String expectedRLPEncoding = "0x21f90158048505d21dba00830186a094ac1aec09ef5f8dde6a0baf709ea388bbd7965f728203c0f8d5f845820feaa031115ed4fdf3cdc9c3071be0c14a992d1ca70b02382b3fadb2428a60f411edcba033530406db890a6c144759f72977739edfe2f9718966de52d6dada0a70be27aef845820fe9a02b50229dc8d4c18414d1f8f943658ca22e6c08a57f52e7e77910d01c1bb49286a0795fc16b6327c41593eb9b7ea5bbf2c61e3b4b3e5cd3176d3ebd57007fa4842bf845820feaa0d5e3d18cd12fb36180adad4df5b9f15089ac9f8448e7a7eeb0fc0f6497faa98da0246503976acea8e9a91e630963eaa344b193fc4e747c1ccaae253083d2695e969423bf3d4eb274621e56ce65f6fa05da9e24785bb8f847f845820feaa04dfc5650bf203652b440ba53791f0adc07c2fae0e746efbda2b5117c568474d3a072bea336107273fd0e1624d4f70425bb040e59e8f2b5d856fc22ab898c2b156e";
        String expectedRLPTransactionHash = "0xd8b8da94309ca9495d16e3d8e04df3a386ba338c5839bdcf8cee7f796b10e1a7";
        String expectedRLPSenderTransactionHash = "0xed215e3932acb07b94a5b0482f48c04e76e41749f88bfddfa70c7fe2f6daa278";
        String expectedRLPEncodingForFeePayerSigning = "0xf840a5e421048505d21dba00830186a094ac1aec09ef5f8dde6a0baf709ea388bbd7965f728203c09423bf3d4eb274621e56ce65f6fa05da9e24785bb88207e38080";

        ExpectedData expectedData = new ExpectedData(builder, expectedRLPEncoding, expectedRLPTransactionHash, expectedRLPSenderTransactionHash, expectedRLPEncodingForFeePayerSigning);

        return expectedData;
    }

    static ExpectedData setAccountKeyWeightedMultiSig() {
        String from = "0xed2f77b1962805385512c18ad6d66f3dee3def15";

        String[] publicKeyArr = new String[]{
                "0xac1350e8b234a7dac087e4899d5b30687bd153d181faaf4ef11fea7d1acbecf45669b120fd488cf638f7ea83cfe009d85b949aacd51cde80e3162a17aa5160f7",
                "0x798096c2691444e7c579e04f13c6edeb122b9e35718c041a51b74ccd5f1a086f2e499900717c718a434b15a989d4c33bae18a65f4c1e6b48f8103478cf1e5ef5",
                "0xaeb9ab821099f4c5d80caafbc0d1980a1a1a701cab8cd0cca8e2f187263a992d5bbf8ea44a1f9bf577175fdccc953c8b8d1bbeeefdabd839be1fcf89f532f470",
        };

        WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3)));
        Account account = Account.createWithAccountKeyWeightedMultiSig(from, publicKeyArr, options);

        String gas = "0x55730";
        String nonce = "0x2";
        String gasPrice = "0x5d21dba00";
        SignatureData senderSignatureData = new SignatureData(
                "0x0fe9",
                "0x3665a7ef1b6bc74171c00cca93380e4a28ff01090a28b9e7f861ca5c01cc26ae",
                "0x0bb148b53761c946bd154506317625aab8b910cf55185d16158e0fef67dd0a6c"
        );

        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        SignatureData feePayerSignatureData = new SignatureData(
                "0x0fe9",
                "0xd0275ad7a55bf6b3f30c5cd27a2940ba574f05d9650e41f08dce929a1b7985b3",
                "0x15576c4473dc7c42df9b1d9f106fbfc8a38646f4adca26f4a14a04ea5bd7351d"
        );

        String chainID = "0x7e3";

        FeeDelegatedAccountUpdate.Builder builder = new FeeDelegatedAccountUpdate.Builder()
                .setFrom(from)
                .setAccount(account)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setAccount(account)
                .setSignatures(Arrays.asList(senderSignatureData))
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignatureData)
                .setChainId(chainID);

        String expectedRLPEncoding = "0x21f9013b028505d21dba008305573094ed2f77b1962805385512c18ad6d66f3dee3def15b87204f86f02f86ce301a103ac1350e8b234a7dac087e4899d5b30687bd153d181faaf4ef11fea7d1acbecf4e302a103798096c2691444e7c579e04f13c6edeb122b9e35718c041a51b74ccd5f1a086fe303a102aeb9ab821099f4c5d80caafbc0d1980a1a1a701cab8cd0cca8e2f187263a992df847f845820fe9a03665a7ef1b6bc74171c00cca93380e4a28ff01090a28b9e7f861ca5c01cc26aea00bb148b53761c946bd154506317625aab8b910cf55185d16158e0fef67dd0a6c9423bf3d4eb274621e56ce65f6fa05da9e24785bb8f847f845820fe9a0d0275ad7a55bf6b3f30c5cd27a2940ba574f05d9650e41f08dce929a1b7985b3a015576c4473dc7c42df9b1d9f106fbfc8a38646f4adca26f4a14a04ea5bd7351d";
        String expectedRLPTransactionHash = "0xd2b7b5487b530e4ae3e164837cef3da078f4ba5ef6fbdafa4498edc83fb5ee45";
        String expectedRLPSenderTransactionHash = "0x873f2a5f234a276c64a05c8036720db0c0dbf75770a137746952d925b6fff0a1";
        String expectedRLPEncodingForFeePayerSigning = "0xf8b3b897f89521028505d21dba008305573094ed2f77b1962805385512c18ad6d66f3dee3def15b87204f86f02f86ce301a103ac1350e8b234a7dac087e4899d5b30687bd153d181faaf4ef11fea7d1acbecf4e302a103798096c2691444e7c579e04f13c6edeb122b9e35718c041a51b74ccd5f1a086fe303a102aeb9ab821099f4c5d80caafbc0d1980a1a1a701cab8cd0cca8e2f187263a992d9423bf3d4eb274621e56ce65f6fa05da9e24785bb88207e38080";

        ExpectedData expectedData = new ExpectedData(builder, expectedRLPEncoding, expectedRLPTransactionHash, expectedRLPSenderTransactionHash, expectedRLPEncodingForFeePayerSigning);

        return expectedData;
    }

    static ExpectedData setAccountKeyRoleBased() {
        String from = "0xed2f77b1962805385512c18ad6d66f3dee3def15";

        String[][] publicKeyArr = new String[][]{
                {
                        "0xca112896b2025047790bbcc74f48af339f71390b5335b5e657b50ef8f634fb9fb4f7caf09ec53e8ec0d78253c37d30367631ac44c4b4825b9b5a7bfb6b55b5af",
                        "0xec93c2f3990b61e692467cc2c49b9ff2f595002234c8116bae8807ff0236bd5e99e943b81be279230a218d297069099d873f49b12557ce6ac5bdeee8ddb83665",
                        "0x99a40e6be991d48c9a5604059517cf489c77d35df64d1ff0233469ff27350652e907ed7a6e1662730d9d22a6b102e44b499dd271802452bd668229db0fc4750c",
                },
                {
                        "0xf890dfeca00111977dd6be8198466099ba6528e40403fe32a9994cd03ad18f3d24f3776a5e445b34b8654d9c56fe2397d8ed4989614dfefb776f038e024436ea",
                        "0xfeedab225d4008d162e495fef4aba1b315369a140837d1150acecb71bb1c7faaef896b4a3e917824b18626fc86c2ab2f11307870a0ed2e72a2d8cfdaef5509bc",
                        "0xfff2068f7ab4ae7faed86395647502ec6c8219eebe13858ddaaf01112bf40de6dfe82af9538d96465bf11d49d3d5d56b7868476bfb3a1d930177aa05b0345603",
                },
                {
                        "0x3a8547ed514797bac885e5aaf2d8ee7a5b3df542efdf28b3886bd5b925f5231c1f2e0d8c9fedae714a5bd71411cb0ec60d63246b04daeedd3ff4ccd1fc95ecc4",
                        "0x13c45e4cbb231ba8150e5943a85a0ac9d6e7a347fcbc27833067e6ee0e63346dbf4c3e290296d9ea700f721b4f056582383be954b6a68525aae197a0f5813f73",
                        "0xc45e5eb35215d1a945ef3790b4689e9644e0b14661bdb2f5d0e3bb1b729f091d054a3c972a93421ccb80e29b0af9ce0c0d7ec5f773b25d7769837114a339c935",
                }

        };

//        WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3)));
        Account account = Account.createWithAccountKeyRoleBased(from, Arrays.asList(publicKeyArr));

        String gas = "0x61a80";
        String nonce = "0x3";
        String gasPrice = "0x5d21dba00";
        SignatureData[] senderSignatureData = new SignatureData[]{
                new SignatureData(
                        "0x0fea",
                        "0xbedc71cfef422e80c32b752b4ebac9ee9e047f3beea07f1929b3f2543dddfd6d",
                        "0x5454e3c2164fb6b844ccf74e07ca8420d427a3ca0f2f15b20be6c285ba308f45"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x66530029fde1835b844505657d15e6457e432744107c3111b998ce1e76a548f6",
                        "0x2b045a9959d522f738390f47a528045845896bf818e35355367e017bd1f41644"
                ),
                new SignatureData(
                        "0x0fea",
                        "0x15e210db2922121c4a8f1223149846844a1923212892acf66df0d0b3daab8b58",
                        "0x07680c111780bada54d62c62c8182984e92ec4817d59c313046d1f8030c001e7"
                ),
        };

        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        SignatureData feePayerSignatureData = new SignatureData(
                "0x0fe9",
                "0x5801ee7887b401d34888d9141915c167c5bfce429d836c1acbc229b9c6938481",
                "0x0f4c01747d3e5f6221063d7e74efc5c7d59020a74dce1c7e37e9a78d5d31de44"
        );

        String chainID = "0x7e3";

        FeeDelegatedAccountUpdate.Builder builder = new FeeDelegatedAccountUpdate.Builder()
                .setFrom(from)
                .setAccount(account)
                .setGas(gas)
                .setGasPrice(gasPrice)
                .setNonce(nonce)
                .setAccount(account)
                .setSignatures(Arrays.asList(senderSignatureData))
                .setFeePayer(feePayer)
                .setFeePayerSignatures(feePayerSignatureData)
                .setChainId(chainID);

        String expectedRLPEncoding = "0x21f902b8038505d21dba0083061a8094ed2f77b1962805385512c18ad6d66f3dee3def15b9016005f9015cb87204f86f01f86ce301a103ca112896b2025047790bbcc74f48af339f71390b5335b5e657b50ef8f634fb9fe301a103ec93c2f3990b61e692467cc2c49b9ff2f595002234c8116bae8807ff0236bd5ee301a10299a40e6be991d48c9a5604059517cf489c77d35df64d1ff0233469ff27350652b87204f86f01f86ce301a102f890dfeca00111977dd6be8198466099ba6528e40403fe32a9994cd03ad18f3de301a102feedab225d4008d162e495fef4aba1b315369a140837d1150acecb71bb1c7faae301a103fff2068f7ab4ae7faed86395647502ec6c8219eebe13858ddaaf01112bf40de6b87204f86f01f86ce301a1023a8547ed514797bac885e5aaf2d8ee7a5b3df542efdf28b3886bd5b925f5231ce301a10313c45e4cbb231ba8150e5943a85a0ac9d6e7a347fcbc27833067e6ee0e63346de301a103c45e5eb35215d1a945ef3790b4689e9644e0b14661bdb2f5d0e3bb1b729f091df8d5f845820feaa0bedc71cfef422e80c32b752b4ebac9ee9e047f3beea07f1929b3f2543dddfd6da05454e3c2164fb6b844ccf74e07ca8420d427a3ca0f2f15b20be6c285ba308f45f845820feaa066530029fde1835b844505657d15e6457e432744107c3111b998ce1e76a548f6a02b045a9959d522f738390f47a528045845896bf818e35355367e017bd1f41644f845820feaa015e210db2922121c4a8f1223149846844a1923212892acf66df0d0b3daab8b58a007680c111780bada54d62c62c8182984e92ec4817d59c313046d1f8030c001e79423bf3d4eb274621e56ce65f6fa05da9e24785bb8f847f845820fe9a05801ee7887b401d34888d9141915c167c5bfce429d836c1acbc229b9c6938481a00f4c01747d3e5f6221063d7e74efc5c7d59020a74dce1c7e37e9a78d5d31de44";
        String expectedRLPTransactionHash = "0x73540a1b47ec1836dae7657cfb8a7e7ae3e7e5cc54fd33f58b9f7efca6d8692c";
        String expectedRLPSenderTransactionHash = "0x21a6004af918a61c1f0b6420cb85b329ac65a5cca0a668d207cfd1796470313e";
        String expectedRLPEncodingForFeePayerSigning = "0xf901a4b90187f9018421038505d21dba0083061a8094ed2f77b1962805385512c18ad6d66f3dee3def15b9016005f9015cb87204f86f01f86ce301a103ca112896b2025047790bbcc74f48af339f71390b5335b5e657b50ef8f634fb9fe301a103ec93c2f3990b61e692467cc2c49b9ff2f595002234c8116bae8807ff0236bd5ee301a10299a40e6be991d48c9a5604059517cf489c77d35df64d1ff0233469ff27350652b87204f86f01f86ce301a102f890dfeca00111977dd6be8198466099ba6528e40403fe32a9994cd03ad18f3de301a102feedab225d4008d162e495fef4aba1b315369a140837d1150acecb71bb1c7faae301a103fff2068f7ab4ae7faed86395647502ec6c8219eebe13858ddaaf01112bf40de6b87204f86f01f86ce301a1023a8547ed514797bac885e5aaf2d8ee7a5b3df542efdf28b3886bd5b925f5231ce301a10313c45e4cbb231ba8150e5943a85a0ac9d6e7a347fcbc27833067e6ee0e63346de301a103c45e5eb35215d1a945ef3790b4689e9644e0b14661bdb2f5d0e3bb1b729f091d9423bf3d4eb274621e56ce65f6fa05da9e24785bb88207e38080";

        ExpectedData expectedData = new ExpectedData(builder, expectedRLPEncoding, expectedRLPTransactionHash, expectedRLPSenderTransactionHash, expectedRLPEncodingForFeePayerSigning);

        return expectedData;
    }


    public static class ExpectedData {
        FeeDelegatedAccountUpdate.Builder builder;
        String expectedRLPEncoding;
        String expectedTransactionHash;
        String expectedSenderTransactionHash;
        String expectedRLPEncodingForFeePayerSigning;

        public ExpectedData(FeeDelegatedAccountUpdate.Builder builder, String expectedRLPEncoding, String expectedTransactionHash, String expectedSenderTransactionHash, String expectedRLPEncodingForFeePayerSigning) {
            this.builder = builder;
            this.expectedRLPEncoding = expectedRLPEncoding;
            this.expectedTransactionHash = expectedTransactionHash;
            this.expectedSenderTransactionHash = expectedSenderTransactionHash;
            this.expectedRLPEncodingForFeePayerSigning = expectedRLPEncodingForFeePayerSigning;
        }

        public FeeDelegatedAccountUpdate.Builder getBuilder() {
            return builder;
        }

        public void setBuilder(FeeDelegatedAccountUpdate.Builder builder) {
            this.builder = builder;
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

        public void setExpectedTransactionHash(String expectedTransactionHash) {
            this.expectedTransactionHash = expectedTransactionHash;
        }

        public String getExpectedSenderTransactionHash() {
            return expectedSenderTransactionHash;
        }

        public void setExpectedSenderTransactionHash(String expectedSenderTransactionHash) {
            this.expectedSenderTransactionHash = expectedSenderTransactionHash;
        }

        public String getExpectedRLPEncodingForFeePayerSigning() {
            return expectedRLPEncodingForFeePayerSigning;
        }

        public void setExpectedRLPEncodingForFeePayerSigning(String expectedRLPEncodingForFeePayerSigning) {
            this.expectedRLPEncodingForFeePayerSigning = expectedRLPEncodingForFeePayerSigning;
        }
    }

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        List<String[]> arr = caver.wallet.keyring.generateRoleBasedKeys(numArr, "entropy");
        return caver.wallet.keyring.createWithRoleBasedKey(address, arr);
    }

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedAccountUpdate.Builder builder;
        FeeDelegatedAccountUpdate txObj;

        @Before
        public void preSetup() {
            builder = setLegacyData().getBuilder();
        }

        @Test
        public void BuilderTest() {
            txObj = builder.build();
            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedAccountUpdate.toString(), txObj.getType());
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            String nonce = null;
            String gasPrice = null;
            String chainId = null;
            txObj = builder
                    .setKlaytnCall(caver.rpc.getKlay())
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainId)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());
        }

        @Test
        public void BuilderTestWithBigInteger() {
            String gas = "0x493e0";
            String nonce = "0x0";
            String gasPrice = "0x5d21dba00";
            String chainId = "0x1";

            txObj = builder.setGas(Numeric.toBigInt(gas))
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainId))
                    .build();


            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            txObj = builder.setFrom(from).build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            txObj = builder.setFrom(from).build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";
            txObj = builder.setGas(gas).build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            txObj = builder.setGas(gas).build();
        }

        @Test
        public void throwException_setFeePayerSignatures_missingFeePayer() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("feePayer is missing: feePayer must be defined with feePayerSignatures.");

            String feePayer = null;

            txObj = builder.setFeePayer(feePayer).build();
        }

        @Test
        public void throwException_missingAccount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("account is missing.");

            Account account = null;

            txObj = builder.setAccount(account).build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
        String feePayer = "0x23bf3d4eb274621e56ce65f6fa05da9e24785bb8";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        Account account = Account.createWithAccountKeyLegacy(from);

        @Test
        public void createInstance() {
            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );

            assertNotNull(txObj);
            assertEquals(TransactionType.TxTypeFeeDelegatedAccountUpdate.toString(), txObj.getType());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }

        @Test
        public void throwException_missingAccount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("account is missing.");

            Account account = null;

            FeeDelegatedAccountUpdate txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setFrom(from)
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        List<ExpectedData> expectedDataList;
        FeeDelegatedAccountUpdate mTxObj;

        @Before
        public void preSetup() {
            expectedDataList = new ArrayList<>();
            expectedDataList.add(setLegacyData());
            expectedDataList.add(setAccountPublic());
            expectedDataList.add(setAccountKeyFail());
            expectedDataList.add(setAccountKeyWeightedMultiSig());
            expectedDataList.add(setAccountKeyRoleBased());
        }

        @Test
        public void getRLPEncoding() {
            for(ExpectedData expectedData : expectedDataList) {
                FeeDelegatedAccountUpdate txObj = expectedData.getBuilder().build();
                assertEquals(expectedData.getExpectedRLPEncoding(), txObj.getRLPEncoding());
            }
        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            ExpectedData expectedData = setLegacyData();
            FeeDelegatedAccountUpdate.Builder builder = expectedData.getBuilder();

            mTxObj = builder.setNonce(nonce).build();

            mTxObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            ExpectedData expectedData = setLegacyData();
            FeeDelegatedAccountUpdate.Builder builder = expectedData.getBuilder();

            mTxObj = builder.setGasPrice(gasPrice).build();

            mTxObj.getRLPEncoding();
        }
    }

    public static class signAsFeePayer_OneKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String senderPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        Account account;
        SingleKeyring keyring;
        String klaytnWalletKey;

        FeeDelegatedAccountUpdate txObj;

        SignatureData senderSignature = new SignatureData(
                "0x26",
                "0xab69d9adca15d9763c4ce6f98b35256717c6e932007658f19c5a255de9e70dda",
                "0x26aa676a3a1a6e96aff4a3df2335788d614d54fb4db1c3c48551ce1fa7ac5e52"
        );

        String expectedRawTx = "0x21f8e48204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0ba302a1033a514176466fa815ed481ffad09110a2d344f6c9b78c1d14afc351c3a51be33df845f84326a0ab69d9adca15d9763c4ce6f98b35256717c6e932007658f19c5a255de9e70ddaa026aa676a3a1a6e96aff4a3df2335788d614d54fb4db1c3c48551ce1fa7ac5e52945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0f295cd69b4144d9dbc906ba144933d2cc535d9d559f7a92b4672cc5485bf3a60a0784b8060234ffd64739b5fc2f2503939340ab4248feaa6efcf62cb874345fe40";

        @Before
        public void before() {
            PrivateKey sender = new PrivateKey(senderPrivateKey);
            account = Account.createWithAccountKeyPublic(sender.getDerivedAddress(), sender.getPublicKey(false));

            txObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );

            keyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            klaytnWalletKey = keyring.getKlaytnWalletKey();
        }

        @Test
        public void signAsFeePayer_KlaytnWalletKey() throws IOException {
            txObj.signAsFeePayer(klaytnWalletKey);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring() throws IOException {
            txObj.signAsFeePayer(keyring, 0, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_Keyring_NoSigner() throws IOException {
            txObj.signAsFeePayer(keyring, 0);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_multipleKey() throws IOException {
            String[] keyArr = {
                    caver.wallet.keyring.generateSingleKey(),
                    feePayerPrivateKey,
                    caver.wallet.keyring.generateSingleKey()
            };

            MultipleKeyring keyring = caver.wallet.keyring.createWithMultipleKey(feePayer, keyArr);
            txObj.signAsFeePayer(keyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void signAsFeePayer_roleBasedKey() throws IOException {
            String[][] keyArr = {
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            caver.wallet.keyring.generateSingleKey(),
                    },
                    {
                            caver.wallet.keyring.generateSingleKey()
                    },
                    {
                            caver.wallet.keyring.generateSingleKey(),
                            feePayerPrivateKey
                    }
            };

            RoleBasedKeyring roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(keyring.getAddress(), Arrays.asList(keyArr));
            txObj.signAsFeePayer(roleBasedKeyring, 1);
            assertEquals(1, txObj.getFeePayerSignatures().size());
            assertEquals(expectedRawTx, txObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = caver.wallet.keyring.generate();

            txObj.signAsFeePayer(keyring, 0);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring keyring = generateRoleBaseKeyring(new int[]{3, 3, 3}, feePayer);
            txObj.signAsFeePayer(keyring, 4);
        }
    }

    public static class signAsFeePayer_AllKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        FeeDelegatedAccountUpdate mTxObj;
        AbstractKeyring singleKeyring, multipleKeyring, roleBasedKeyring;

        String senderPrivateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String feePayerPrivateKey = "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        Account account;
        SingleKeyring keyring;
        String klaytnWalletKey;

        SignatureData senderSignature = new SignatureData(
                "0x26",
                "0xab69d9adca15d9763c4ce6f98b35256717c6e932007658f19c5a255de9e70dda",
                "0x26aa676a3a1a6e96aff4a3df2335788d614d54fb4db1c3c48551ce1fa7ac5e52"
        );

        @Before
        public void before() {
            singleKeyring = caver.wallet.keyring.createWithSingleKey(feePayer, feePayerPrivateKey);
            multipleKeyring = caver.wallet.keyring.createWithMultipleKey(
                    feePayer,
                    caver.wallet.keyring.generateMultipleKeys(8)
            );
            roleBasedKeyring = caver.wallet.keyring.createWithRoleBasedKey(
                    feePayer,
                    caver.wallet.keyring.generateRolBasedKeys(new int[]{3, 4, 5})
            );

            account = Account.createWithAccountKeyFail(from);

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }

        @Test
        public void signWithKeys_singleKeyring() throws IOException {
            mTxObj.signAsFeePayer(singleKeyring, TransactionHasher::getHashForFeePayerSignature);
            assertEquals(1, mTxObj.getSignatures().size());
        }

        @Test
        public void signWithKeys_singleKeyring_NoSigner() throws IOException {
            mTxObj.signAsFeePayer(singleKeyring);
            assertEquals(1, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void signWithKeys_multipleKeyring() throws IOException {
            mTxObj.signAsFeePayer(multipleKeyring);
            assertEquals(8, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            mTxObj.signAsFeePayer(roleBasedKeyring);
            assertEquals(5, mTxObj.getFeePayerSignatures().size());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The feePayer address of the transaction is different with the address of the keyring to use.");

            SingleKeyring keyring = caver.wallet.keyring.generate();
            mTxObj.signAsFeePayer(keyring);
        }
    }

    public static class appendFeePayerSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String feePayer = "0x5A0043070275d9f6054307Ee7348bD660849D90f";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";
        Account account;

        SignatureData senderSignature = new SignatureData(
                "0x26",
                "0xab69d9adca15d9763c4ce6f98b35256717c6e932007658f19c5a255de9e70dda",
                "0x26aa676a3a1a6e96aff4a3df2335788d614d54fb4db1c3c48551ce1fa7ac5e52"
        );

        FeeDelegatedAccountUpdate mTxObj;

        @Before
        public void before() {
            account = Account.createWithAccountKeyFail(from);

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
            );
        }


        @Test
        public void appendFeePayerSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendFeePayerSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignatureList() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
                            .setFeePayerSignatures(emptySignature)
            );

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void appendFeePayerSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
                            .setFeePayerSignatures(signatureData)
            );

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(2, mTxObj.getFeePayerSignatures().size());
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getFeePayerSignatures().get(1));
        }

        @Test
        public void appendFeePayerSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            SignatureData signatureData2 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x9a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0xa3ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setFrom(from)
                            .setSignatures(senderSignature)
                            .setFeePayer(feePayer)
                            .setAccount(account)
                            .setFeePayerSignatures(signatureData)
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);
            list.add(signatureData2);

            mTxObj.appendFeePayerSignatures(list);
            assertEquals(3, mTxObj.getFeePayerSignatures().size());
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getFeePayerSignatures().get(1));
            assertEquals(signatureData2, mTxObj.getFeePayerSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();


        String from = "0x9788016d3957e62cc7f3aa7f9f5d801e3277b4eb";
        String gas = "0x186a0";
        String nonce = "0x1";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        Account account = Account.createWithAccountKeyLegacy(from);

        FeeDelegatedAccountUpdate mTxObj;

        @Test
        public void combineSignatures() {
            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccount(account)
            );

            String rlpEncoded = "0x21f886018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0f847f845820fe9a05e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878a03e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24a940000000000000000000000000000000000000000c4c3018080";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData expectedSignatureData = new SignatureData(
                    "0x0fe9",
                    "0x5e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878",
                    "0x3e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24a"
            );

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleSignature() {
            SignatureData senderSignature = new SignatureData(
                    "0x0fe9",
                    "0x5e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878",
                    "0x3e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24a"
            );

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccount(account)
                            .setSignatures(senderSignature)
            );

            String[] rlpEncodedStrings = {
                    "0x21f872018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0f847f845820fe9a0dd841ac608f55a20a211599ab73b7cc8cacedb219aca053621b68a7cf1ce1625a055da30e64842b16650ec6fac6972b1344197a299c2f840190bbe01fdc82a447a80c4c3018080",
                    "0x21f872018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0f847f845820feaa0187d11596f3a2c9ef922fee8ebf07aa1c7ce7ae46834c54901436d10b9e0afd8a068094e4e51f2d07b60f14df1ddb75f1afb35ed8061aa51005559beab2cc9cd4c80c4c3018080"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoding = "0x21f90114018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0f8d5f845820fe9a05e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878a03e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24af845820fe9a0dd841ac608f55a20a211599ab73b7cc8cacedb219aca053621b68a7cf1ce1625a055da30e64842b16650ec6fac6972b1344197a299c2f840190bbe01fdc82a447af845820feaa0187d11596f3a2c9ef922fee8ebf07aa1c7ce7ae46834c54901436d10b9e0afd8a068094e4e51f2d07b60f14df1ddb75f1afb35ed8061aa51005559beab2cc9cd4c940000000000000000000000000000000000000000c4c3018080";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x5e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878",
                            "0x3e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24a"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xdd841ac608f55a20a211599ab73b7cc8cacedb219aca053621b68a7cf1ce1625",
                            "0x55da30e64842b16650ec6fac6972b1344197a299c2f840190bbe01fdc82a447a"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x187d11596f3a2c9ef922fee8ebf07aa1c7ce7ae46834c54901436d10b9e0afd8",
                            "0x68094e4e51f2d07b60f14df1ddb75f1afb35ed8061aa51005559beab2cc9cd4c"
                    )
            };

            assertEquals(expectedRLPEncoding, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void combineSignature_feePayerSignature() {
            String feePayer = "0x1576dfec8c77f984d627ff5e953ab527c30a3904";

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFeePayer(feePayer)
                            .setChainId(chainID)
                            .setAccount(account)
            );

            String rlpEncoded = "0x21f886018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f847f845820feaa06f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2ca0562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8";
            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncoded));

            SignatureData signatureData = new SignatureData(
                    "0x0fea",
                    "0x6f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2c",
                    "0x562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8"
            );
            assertEquals(rlpEncoded, combined);
            assertEquals(signatureData, mTxObj.getFeePayerSignatures().get(0));
        }

        @Test
        public void combineSignature_multipleFeePayerSignature() {
            String feePayer = "0x1576dfec8c77f984d627ff5e953ab527c30a3904";
            SignatureData feePayerSignatureData = new SignatureData(
                    "0x0fea",
                    "0x6f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2c",
                    "0x562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8"
            );

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setFeePayer(feePayer)
                            .setChainId(chainID)
                            .setAccount(account)
                            .setFeePayerSignatures(feePayerSignatureData)
            );

            String[] rlpEncodedStrings = new String[]{
                    "0x21f886018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f847f845820fe9a080eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504a048975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6",
                    "0x21f886018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f847f845820fe9a0ec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841a0078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a",
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStrings));
            String expectedRLPEncoded = "0x21f90114018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f8d5f845820feaa06f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2ca0562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8f845820fe9a080eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504a048975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6f845820fe9a0ec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841a0078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a";

            SignatureData[] expectedSignatureData = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x6f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2c",
                            "0x562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x80eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504",
                            "0x48975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841",
                            "0x078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a"
                    )
            };

            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignatureData[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedSignatureData[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedSignatureData[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void multipleSignature_senderSignature_feePayerSignature() {
            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccount(account)
            );


            String rlpEncodedString = "0x21f90100018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0f8d5f845820fe9a05e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878a03e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24af845820fe9a0dd841ac608f55a20a211599ab73b7cc8cacedb219aca053621b68a7cf1ce1625a055da30e64842b16650ec6fac6972b1344197a299c2f840190bbe01fdc82a447af845820feaa0187d11596f3a2c9ef922fee8ebf07aa1c7ce7ae46834c54901436d10b9e0afd8a068094e4e51f2d07b60f14df1ddb75f1afb35ed8061aa51005559beab2cc9cd4c80c4c3018080";
            SignatureData[] expectedSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fe9",
                            "0x5e5922bc693162599cca35416a96f44187a7a0ac4851eddf9ad8ec8359aa8878",
                            "0x3e128291576716d0be1ef5a8dba67eb2056fa1495529a77338d9c7a7b4c5e24a"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xdd841ac608f55a20a211599ab73b7cc8cacedb219aca053621b68a7cf1ce1625",
                            "0x55da30e64842b16650ec6fac6972b1344197a299c2f840190bbe01fdc82a447a"
                    ),
                    new SignatureData(
                            "0x0fea",
                            "0x187d11596f3a2c9ef922fee8ebf07aa1c7ce7ae46834c54901436d10b9e0afd8",
                            "0x68094e4e51f2d07b60f14df1ddb75f1afb35ed8061aa51005559beab2cc9cd4c"
                    ),
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));

            String rlpEncodedStringsWithFeePayerSignatures = "0x21f90114018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f8d5f845820feaa06f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2ca0562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8f845820fe9a080eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504a048975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6f845820fe9a0ec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841a0078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a";

            SignatureData[] expectedFeePayerSignatures = new SignatureData[]{
                    new SignatureData(
                            "0x0fea",
                            "0x6f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2c",
                            "0x562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0x80eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504",
                            "0x48975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6"
                    ),
                    new SignatureData(
                            "0x0fe9",
                            "0xec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841",
                            "0x078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a"
                    ),
            };

            combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedStringsWithFeePayerSignatures));

            assertEquals(expectedSignatures[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignatures[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignatures[2], mTxObj.getSignatures().get(2));

            assertEquals(expectedFeePayerSignatures[0], mTxObj.getFeePayerSignatures().get(0));
            assertEquals(expectedFeePayerSignatures[1], mTxObj.getFeePayerSignatures().get(1));
            assertEquals(expectedFeePayerSignatures[2], mTxObj.getFeePayerSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            String gas = "0x1000";

            mTxObj = caver.transaction.feeDelegatedAccountUpdate.create(
                    TxPropertyBuilder.feeDelegatedAccountUpdate()
                            .setNonce(nonce)
                            .setFrom(from)
                            .setGas(gas)
                            .setGasPrice(gasPrice)
                            .setChainId(chainID)
                            .setAccount(account)
            );

            String rlpEncoded = "0x21f90114018505d21dba00830186a0949788016d3957e62cc7f3aa7f9f5d801e3277b4eb8201c0c4c3018080941576dfec8c77f984d627ff5e953ab527c30a3904f8d5f845820feaa06f06eeeb86c6980bf314a3c4c84a9f610d8ed7055e48d3176f8be8fc7c4c0e2ca0562417d4c1653f0e420c63fb427198f636eb5364b9e95626026fdabedcc33eb8f845820fe9a080eb1d684765851433b6e91c702500436704a2b74bbe9fb0e237b7486fc86504a048975a10ca36aa7b439dc9e8a6b5cfd715476ed57e43619a5ef8a9266d544ad6f845820fe9a0ec1155a838e74333b6bc2b76bb99098882c3b522e7a850f01151d37b2fac9841a0078a3216312f05e92a732f26fbe084365f37f5523dc1def47c4cea932eaa972a";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {
        List<ExpectedData> expectedDataList;

        @Before
        public void preSetup() {
            expectedDataList = new ArrayList<>();
            expectedDataList.add(setLegacyData());
            expectedDataList.add(setAccountPublic());
            expectedDataList.add(setAccountKeyFail());
            expectedDataList.add(setAccountKeyWeightedMultiSig());
            expectedDataList.add(setAccountKeyRoleBased());
        }

        @Test
        public void getRawTransaction() {
            for(ExpectedData expectedData : expectedDataList) {
                FeeDelegatedAccountUpdate txObj = expectedData.getBuilder().build();
                assertEquals(expectedData.getExpectedRLPEncoding(), txObj.getRawTransaction());
            }
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        List<ExpectedData> expectedDataList;
        FeeDelegatedAccountUpdate mTxObj;

        @Before
        public void preSetup() {
            expectedDataList = new ArrayList<>();
            expectedDataList.add(setLegacyData());
            expectedDataList.add(setAccountPublic());
            expectedDataList.add(setAccountKeyFail());
            expectedDataList.add(setAccountKeyWeightedMultiSig());
            expectedDataList.add(setAccountKeyRoleBased());
        }

        @Test
        public void getTransactionHash() {
            for(ExpectedData expectedData : expectedDataList) {
                FeeDelegatedAccountUpdate txObj = expectedData.getBuilder().build();
                assertEquals(expectedData.getExpectedTransactionHash(), txObj.getTransactionHash());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = expectedDataList.get(0).getBuilder().setNonce(nonce).build();

            String txHash = mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = expectedDataList.get(0).getBuilder().setGasPrice(gasPrice).build();

            String txHash = mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        List<ExpectedData> expectedDataList;
        FeeDelegatedAccountUpdate mTxObj;

        @Before
        public void preSetup() {
            expectedDataList = new ArrayList<>();
            expectedDataList.add(setLegacyData());
            expectedDataList.add(setAccountPublic());
            expectedDataList.add(setAccountKeyFail());
            expectedDataList.add(setAccountKeyWeightedMultiSig());
            expectedDataList.add(setAccountKeyRoleBased());
        }

        @Test
        public void getSenderTransactionHash() {
            for(ExpectedData expectedData : expectedDataList) {
                FeeDelegatedAccountUpdate txObj = expectedData.getBuilder().build();
                assertEquals(expectedData.getExpectedSenderTransactionHash(), txObj.getSenderTxHash());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = expectedDataList.get(0).getBuilder().setNonce(nonce).build();
            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = expectedDataList.get(0).getBuilder().setGasPrice(gasPrice).build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForFeePayerSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        List<ExpectedData> expectedDataList;
        FeeDelegatedAccountUpdate mTxObj;

        @Before
        public void preSetup() {
            expectedDataList = new ArrayList<>();
            expectedDataList.add(setLegacyData());
            expectedDataList.add(setAccountPublic());
            expectedDataList.add(setAccountKeyFail());
            expectedDataList.add(setAccountKeyWeightedMultiSig());
            expectedDataList.add(setAccountKeyRoleBased());
        }

        @Test
        public void getRLPEncodingForFeePayerSignature() {
            for(ExpectedData expectedData : expectedDataList) {
                FeeDelegatedAccountUpdate txObj = expectedData.getBuilder().build();
                assertEquals(expectedData.getExpectedRLPEncodingForFeePayerSigning(), txObj.getRLPEncodingForFeePayerSignature());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String nonce = null;

            mTxObj = expectedDataList.get(0).getBuilder().setNonce(nonce).build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_GasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String gasPrice = null;

            mTxObj = expectedDataList.get(0).getBuilder().setGasPrice(gasPrice).build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }

        @Test
        public void throwException_NotDefined_ChainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            String chainID = null;

            mTxObj = expectedDataList.get(0).getBuilder().setChainId(chainID).build();

            mTxObj.getRLPEncodingForFeePayerSignature();
        }
    }
}
