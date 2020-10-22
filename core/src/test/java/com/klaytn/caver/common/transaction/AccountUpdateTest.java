package com.klaytn.caver.common.transaction;

import com.klaytn.caver.Caver;
import com.klaytn.caver.account.Account;
import com.klaytn.caver.account.WeightedMultiSigOptions;
import com.klaytn.caver.transaction.TransactionHasher;
import com.klaytn.caver.transaction.type.AccountUpdate;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.PrivateKey;
import com.klaytn.caver.wallet.keyring.SignatureData;
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
import static org.junit.Assert.assertEquals;


public class AccountUpdateTest {

    public static AbstractKeyring generateRoleBaseKeyring(int[] numArr, String address) {
        String[][] keyArr = new String[3][];

        for(int i=0; i<numArr.length; i++) {
            int length = numArr[i];
            String[] arr = new String[length];
            for(int j=0; j<length; j++) {
                arr[j] = PrivateKey.generate("entropy").getPrivateKey();
            }
            keyArr[i] = arr;
        }

        List<String[]> arr = Arrays.asList(keyArr);

        return KeyringFactory.createWithRoleBasedKey(address, arr);
    }

    public static List<AccountUpdate> getAccountUpdateList() {
        List<AccountUpdate> accountUpdateList = new ArrayList<>();

        accountUpdateList.add(getLegacyTx());
        accountUpdateList.add(getPublicKeyTx());
        accountUpdateList.add(getFailTx());
        accountUpdateList.add(getMultiSigTx());
        accountUpdateList.add(getRoleBasedSigTx());

        return accountUpdateList;
    }

    public static List<ExpectedData> getExpectedDataList() {
        List<ExpectedData> expectedDataList = new ArrayList<>();

        expectedDataList.add(ExpectedData.getExpectedDataLegacy());
        expectedDataList.add(ExpectedData.getExpectedDataPublic());
        expectedDataList.add(ExpectedData.getExpectedDataFail());
        expectedDataList.add(ExpectedData.getExpectedDataMultiSig());
        expectedDataList.add(ExpectedData.getExpectedDataRoleBased());

        return expectedDataList;
    }

    public static AccountUpdate getLegacyTx() {
        String address = "0xdca786ce39b074966e8a9eae16eac90783974d80";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fea"),
                Numeric.hexStringToByteArray("0x866f7cf552d4062a3c1a6055cabbe358a21ce779cfe2b81cee87b66024b993af"),
                Numeric.hexStringToByteArray("0x2990dc2d9d36cc4de4b9a79c30aeab8d59e2d60631e0d90c8ac3c096b7a38852")
        );

        Account account = Account.createWithAccountKeyLegacy(address);

        return new AccountUpdate.Builder()
                .setFrom(address)
                .setGas(gas)
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setChainId(chainID)
                .setSignatures(signatureData)
                .setAccount(account)
                .build();
    }

    public static AccountUpdate getPublicKeyTx() {
        String address = "0xffb52bc54635f840013e142ebe7c06c9c91c1625";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fe9"),
                Numeric.hexStringToByteArray("0x9c2ca281e94567846acbeef724b1a7a5f882d581aff9984755abd92272592b8e"),
                Numeric.hexStringToByteArray("0x344fd23d7774ae9c227809bb579387dfcd69e74ae2fe3a788617f54a4001e5ab")
        );

        String publicKey = "0xc93fcbdb2b9dbef8ee5c4748ffdce11f1f5b06d7ba71cc2b7699e38be7698d1edfa5c0486858a516e8a46c4834ac0ad10ed7dc7ec818a88a9f75fe5fabd20e90";
        Account account = Account.createWithAccountKeyPublic(address, publicKey);

        return new AccountUpdate.Builder()
                .setFrom(address)
                .setGas(gas)
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setChainId(chainID)
                .setSignatures(signatureData)
                .setAccount(account)
                .build();
    }

    public static AccountUpdate getFailTx() {
        String address = "0x26b05cce63f78ddf6a769fb2db39e54b9f2db620";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fe9"),
                Numeric.hexStringToByteArray("0x86361c43593859b6989794a6848c5ba1e5d8bd860522347cd167042acd6a7816"),
                Numeric.hexStringToByteArray("0x773f5cc10f734b3b4486b9c5b7e5def156e06d9d9f4a3aaae6662f9a2126094c")
        );

        Account account = Account.createWithAccountKeyFail(address);

        return new AccountUpdate.Builder()
                .setFrom(address)
                .setGas(gas)
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setChainId(chainID)
                .setSignatures(signatureData)
                .setAccount(account)
                .build();
    }

    public static AccountUpdate getMultiSigTx() {
        String address = "0x2dcd60f120bd64e35093a2945ce61c0bcb71dc93";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fe9"),
                Numeric.hexStringToByteArray("0x02aca4ec6773a26c71340c2500cb45886a61797bcd82790f7f01150ced48b0ac"),
                Numeric.hexStringToByteArray("0x20502f22a1b3c95a5f260a03dc3de0eaa1f4a618b1d2a7d4da643507302e523c")
        );

        String[] pubKeyArr = new  String[] {
                "0xe1c4bb4d01245ebdc62a88092f6c79b59d56e319ae694050e7a0c1cff93a0d9240bf159aa0ee59bacb41df2185cf0be1ca316c349d839e4edc04e1af77ec8c4e",
                "0x13853532348457b4fb18526c6447a6cdff38a791dc2e778f19a843fc6b3a3e8d4cb21a4c331ccc967aa9127fb7e49ce52eaf69c967521d4066745371868b297b",
                "0xe0f3c6f28dc933ac3cf7fc3143f0d38bc83aa9541ce7bb67c356cad5c9b020a3a0b24f48b17b1f7880ba027ad39095ae53888d788816658e47a58193c1b81720"
        };

        BigInteger[] weight = new BigInteger[] {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
        WeightedMultiSigOptions weightedMultiSigOptions = new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(weight));
        Account account = Account.createWithAccountKeyWeightedMultiSig(address, pubKeyArr, weightedMultiSigOptions);

        return new AccountUpdate.Builder()
                .setFrom(address)
                .setGas(gas)
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setChainId(chainID)
                .setSignatures(signatureData)
                .setAccount(account)
                .build();
    }

    public static AccountUpdate getRoleBasedSigTx() {
        String address = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        SignatureData signatureData = new SignatureData(
                Numeric.hexStringToByteArray("0x0fe9"),
                Numeric.hexStringToByteArray("0x66e28c27f16ba34325770e842874d07473180bcec22e86851a6882acbaeb56c3"),
                Numeric.hexStringToByteArray("0x761e12fe11003aa4cb8fd9b44a41e5edebeb943cc366264b345d0f7e63853724")
        );

        String[][] pubKeyArr = new String[][] {
                {
                    "0xf7e7e03c328d39cee6201080ac2576919f904f0b8e47fcb7ea8869e7db0baf4470a0b29a1f6dd007e19a53da122d18bf6273cdddb2903ef0ad2b350b207ad67c",
                    "0xedacd9095274f292c702514f6443f58337e7d7c8311694f31c73e86f150ecf45820929c143da861f6009784e36a6ebd99f83b1baf93fd72e820b5df3cd00883b",
                    "0xb74fd682a6a805415e7711890bc91a283c268c78947ebf25a02a2e02625a68aa825b5213f3e9f03c34650da902a2a70915dcc1c7fe86333a7e40e638361335a4"
                },
                {
                    "0xd0ae803893f344ee664378bbc9ebb35ca2d94f7d7ecea4e3e2f9f33817cdb04bb54cf4211eef21e9627a7d0ca6960e8f1135a35c751f526ce203c6e36b3f2230",
                },
                {
                    "0x4b4cd35195aa4324184a64821e514a991b513cc354f5fa6d78fb99e23949bc59613d8be87ad3e1418ad11e1d5537233b697bc0c8c5d7335a6decf687cce700ba",
                    "0x3e65f4a76bca1488a1a046d6976778852aa41f07156d2c42e81c3da6621435d2350f8419fe8255dc87158c8ae30378b19b0d3d224eb410ca2de847a41caeb617"
                }
        };

        WeightedMultiSigOptions[] options = new WeightedMultiSigOptions[] {
            new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
            new WeightedMultiSigOptions(),
            new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(BigInteger.ONE, BigInteger.ONE))
        };

        Account account = Account.createWithAccountKeyRoleBased(address, Arrays.asList(pubKeyArr), Arrays.asList(options));

        return new AccountUpdate.Builder()
                .setFrom(address)
                .setGas(gas)
                .setNonce(nonce)
                .setGasPrice(gasPrice)
                .setChainId(chainID)
                .setSignatures(signatureData)
                .setAccount(account)
                .build();
    }

    public static class ExpectedData {
        String expectedRLP;
        String expectedTxHash;
        String expectedRlpEncodingForSigning;

        public ExpectedData(String expectedRLP, String expectedTxHash, String expectedRlpEncodingForSigning) {
            this.expectedRLP = expectedRLP;
            this.expectedTxHash = expectedTxHash;
            this.expectedRlpEncodingForSigning = expectedRlpEncodingForSigning;
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

    public static class createInstanceBuilder {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        Account account = Account.createWithAccountKeyLegacy(from);

        @Test
        public void BuilderTest() {
            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();

            assertNotNull(txObj);
        }

        @Test
        public void BuilderWithRPCTest() throws IOException {
            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setKlaytnCall(new Caver(Caver.DEFAULT_URL).rpc.getKlay())
                    .setGas(gas)
                    .setFrom(from)
                    .setAccount(account)
                    .build();

            txObj.fillTransaction();

            assertFalse(txObj.getNonce().isEmpty());
            assertFalse(txObj.getGasPrice().isEmpty());
            assertFalse(txObj.getChainId().isEmpty());        }

        @Test
        public void BuilderTestWithBigInteger() {
            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(Numeric.toBigInt(nonce))
                    .setGas(Numeric.toBigInt(gas))
                    .setGasPrice(Numeric.toBigInt(gasPrice))
                    .setChainId(Numeric.toBigInt(chainID))
                    .setFrom(from)
                    .setAccount(account)
                    .build();


            assertNotNull(txObj);

            assertEquals(gas, txObj.getGas());
            assertEquals(gasPrice, txObj.getGasPrice());
            assertEquals(chainID, txObj.getChainId());
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();
        }

        @Test
        public void throwException_missingAccount() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("account is missing.");

            Account account = null;

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();
        }
    }

    public static class createInstance {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
        String gas = "0x30d40";
        String nonce = "0x0";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";

        Account account = Account.createWithAccountKeyLegacy(from);

        @Test
        public void createInstance() {
            AccountUpdate txObj = new AccountUpdate(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    account
            );

            assertNotNull(txObj);
        }

        @Test
        public void throwException_invalidFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid address.");

            String from = "invalid Address";

            AccountUpdate txObj = new AccountUpdate(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    account
            );
        }

        @Test
        public void throwException_missingFrom() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("from is missing.");

            String from = null;

            AccountUpdate txObj = new AccountUpdate(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    account
            );
        }

        @Test
        public void throwException_invalidGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid gas.");

            String gas = "invalid gas";

            AccountUpdate txObj = new AccountUpdate(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    account
            );
        }

        @Test
        public void throwException_missingGas() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("gas is missing.");

            String gas = null;

            AccountUpdate txObj = new AccountUpdate(
                    null,
                    from,
                    nonce,
                    gas,
                    gasPrice,
                    chainID,
                    null,
                    account
            );
        }
    }

    public static class getRLPEncodingTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void getRLPEncoding() {
            List<AccountUpdate> updateList = getAccountUpdateList();
            List<ExpectedData> expectedDataList = getExpectedDataList();

            for(int i=0; i< updateList.size(); i++) {
                assertEquals(expectedDataList.get(i).expectedRLP, updateList.get(i).getRLPEncoding());
            }

        }

        @Test
        public void throwException_NoNonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            String from = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
            String gas = "0x30d40";
            String nonce = "0x0";
            String gasPrice = "0x5d21dba00";
            String chainID = "0x7e3";

            Account account = Account.createWithAccountKeyLegacy(from);

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();

            txObj.getRLPEncoding();
        }

        @Test
        public void throwException_NoGasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            String from = "0xfb675bea5c3fa279fd21572161b6b6b2dbd84233";
            String gas = "0x30d40";
            String nonce = "0x0";
            String gasPrice = "0x5d21dba00";
            String chainID = "0x7e3";

            Account account = Account.createWithAccountKeyLegacy(from);

            AccountUpdate txObj = new AccountUpdate.Builder()
                    .setNonce(nonce)
                    .setGas(gas)
                    .setChainId(chainID)
                    .setFrom(from)
                    .setAccount(account)
                    .build();

            txObj.getRLPEncoding();
        }
    }

    public static class signWithKeyTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";

        String expectedRLPEncoding = "0x20f8888204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0ba302a1033a514176466fa815ed481ffad09110a2d344f6c9b78c1d14afc351c3a51be33df845f84325a0f7d479628f05f51320f0842193e3f7ae55a5b49d3645bf55c35bee1e8fd2593aa04de8eab5338fdc86e96f8c49ed516550f793fc2c4007614ce3d2a6b33cf9e451";

        AccountUpdate mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;



        @Before
        public void before() {
            Account account = Account.createWithAccountKeyPublic(from, new PrivateKey(privateKey).getPublicKey(false));

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void signWithKey_Keyring() throws IOException{
            mTxObj.sign(coupledKeyring, 0, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoIndex() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring, 0);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_Keyring_Only() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_NoIndex() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KeyString_Only() throws IOException {
            mTxObj.sign(privateKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKey_KlayWalletKey() throws IOException {
            mTxObj.sign(klaytnWalletKey);
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void throwException_InvalidIndex() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("Invalid index : index must be less than the length of the key.");

            AbstractKeyring role = generateRoleBaseKeyring(new int[]{3,3,3}, from);
            mTxObj.sign(role, 4);
        }
    }

    public static class signWithKeysTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";

        String expectedRLPEncoding = "0x20f8888204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0ba302a1033a514176466fa815ed481ffad09110a2d344f6c9b78c1d14afc351c3a51be33df845f84325a0f7d479628f05f51320f0842193e3f7ae55a5b49d3645bf55c35bee1e8fd2593aa04de8eab5338fdc86e96f8c49ed516550f793fc2c4007614ce3d2a6b33cf9e451";

        AccountUpdate mTxObj;
        AbstractKeyring coupledKeyring, deCoupledKeyring;
        String klaytnWalletKey;



        @Before
        public void before() {
            Account account = Account.createWithAccountKeyPublic(from, new PrivateKey(privateKey).getPublicKey(false));

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            coupledKeyring = KeyringFactory.createFromPrivateKey(privateKey);
            deCoupledKeyring = KeyringFactory.createWithSingleKey(PrivateKey.generate().getDerivedAddress(), privateKey);
            klaytnWalletKey = privateKey + "0x00" + coupledKeyring.getAddress();
        }

        @Test
        public void signWithKeys_Keyring() throws IOException {
            mTxObj.sign(coupledKeyring, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_Keyring_NoSigner() throws IOException {
            mTxObj.sign(coupledKeyring);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void signWithKeys_KeyString_NoSigner() throws IOException {
            mTxObj.sign(privateKey, TransactionHasher::getHashForSignature);
            assertEquals(1, mTxObj.getSignatures().size());
            assertEquals(expectedRLPEncoding, mTxObj.getRawTransaction());
        }

        @Test
        public void throwException_NotMatchAddress() throws IOException {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("The from address of the transaction is different with the address of the keyring to use");

            mTxObj.sign(deCoupledKeyring);
        }

        @Test
        public void signWithKeys_roleBasedKeyring() throws IOException {
            AbstractKeyring roleBased = generateRoleBaseKeyring(new int[]{3,3,3}, from);

            mTxObj.sign(roleBased);
            assertEquals(3, mTxObj.getSignatures().size());
        }
    }

    public static class appendSignaturesTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String privateKey = "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8";
        String from = "0xa94f5374Fce5edBC8E2a8697C15331677e6EbF0B";
        String to = "0x7b65B75d204aBed71587c9E519a89277766EE1d0";
        String gas = "0xf4240";
        String gasPrice = "0x19";
        String nonce = "0x4d2";
        String chainID = "0x1";

        AccountUpdate mTxObj;
        Account account = Account.createWithAccountKeyPublic(from, new PrivateKey(privateKey).getPublicKey(false));


        @Before
        public void before() {
            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();
        }


        @Test
        public void appendSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj.appendSignatures(signatureData);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignatureList_EmptySig() {
            SignatureData emptySignature = SignatureData.getEmptySignature();

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .setSignatures(emptySignature)
                    .build();

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData);

            mTxObj.appendSignatures(list);
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
        }

        @Test
        public void appendSignature_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .setSignatures(signatureData)
                    .build();

            SignatureData signatureData1 = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x7a5011b41cfcb6270af1b5f8aeac8aeabb1edb436f028261b5add564de694700"),
                    Numeric.hexStringToByteArray("0x23ac51660b8b421bf732ef8148d0d4f19d5e29cb97be6bccb5ae505ebe89eb4a")
            );

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);

            mTxObj.appendSignatures(list);
            assertEquals(2, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
        }

        @Test
        public void appendSignatureList_ExistedSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0xade9480f584fe481bf070ab758ecc010afa15debc33e1bd75af637d834073a6e"),
                    Numeric.hexStringToByteArray("0x38160105d78cef4529d765941ad6637d8dcf6bd99310e165fee1c39fff2aa27e")
            );

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .setSignatures(signatureData)
                    .build();

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

            List<SignatureData> list = new ArrayList<>();
            list.add(signatureData1);
            list.add(signatureData2);

            mTxObj.appendSignatures(list);
            assertEquals(3, mTxObj.getSignatures().size());
            assertEquals(signatureData, mTxObj.getSignatures().get(0));
            assertEquals(signatureData1, mTxObj.getSignatures().get(1));
            assertEquals(signatureData2, mTxObj.getSignatures().get(2));
        }
    }

    public static class combineSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x40efcb7d744fdc881f698a8ec573999fe6383545";
        String gas = "0x15f90";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";
        Account account = Account.createWithAccountKeyLegacy(from);

        AccountUpdate mTxObj;

        @Before
        public void before() {


            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();
        }

        @Test
        public void combineSignature() {
            SignatureData expectedSignature = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0xf2a83743da6931ce25a29d04f1c51cec8464f0d9d4dabb5acb059aa3fb8c345a"),
                    Numeric.hexStringToByteArray("0x65879e06474669005e02e0b8ca06cba6f8943022305659f8936f1f6109147fdd")
            );

            String rlpEncoded = "0x20f86c018505d21dba0083015f909440efcb7d744fdc881f698a8ec573999fe63835458201c0f847f845820fe9a0f2a83743da6931ce25a29d04f1c51cec8464f0d9d4dabb5acb059aa3fb8c345aa065879e06474669005e02e0b8ca06cba6f8943022305659f8936f1f6109147fdd";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);
            String combined = mTxObj.combineSignedRawTransactions(list);

            assertEquals(rlpEncoded, combined);
            assertEquals(expectedSignature, mTxObj.getSignatures().get(0));
        }

        @Test
        public void combine_multipleSignature() {
            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fe9"),
                    Numeric.hexStringToByteArray("0xf2a83743da6931ce25a29d04f1c51cec8464f0d9d4dabb5acb059aa3fb8c345a"),
                    Numeric.hexStringToByteArray("0x65879e06474669005e02e0b8ca06cba6f8943022305659f8936f1f6109147fdd")
            );


            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .setSignatures(signatureData)
                    .build();

            String expectedRLPEncoded = "0x20f8fa018505d21dba0083015f909440efcb7d744fdc881f698a8ec573999fe63835458201c0f8d5f845820fe9a0f2a83743da6931ce25a29d04f1c51cec8464f0d9d4dabb5acb059aa3fb8c345aa065879e06474669005e02e0b8ca06cba6f8943022305659f8936f1f6109147fddf845820feaa0638f0d712b4b709cadab174dea6da50e5429ea59d78446e810af954af8d67981a0129ad4eb9222e161e9e52be9c2384e1b1ff7566c640bc5b30c054efd64b081e7f845820fe9a0935584330d98f4a8a1cf83bf81ea7a18e33a962ad17b6a9eb8e04e3f5f95179da026804e07b5c105427497e8336300c1435d30ffa8d379dc27e5c1facd966c58db";

            SignatureData[] expectedSignature = new SignatureData[] {
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0xf2a83743da6931ce25a29d04f1c51cec8464f0d9d4dabb5acb059aa3fb8c345a"),
                            Numeric.hexStringToByteArray("0x65879e06474669005e02e0b8ca06cba6f8943022305659f8936f1f6109147fdd")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fea"),
                            Numeric.hexStringToByteArray("0x638f0d712b4b709cadab174dea6da50e5429ea59d78446e810af954af8d67981"),
                            Numeric.hexStringToByteArray("0x129ad4eb9222e161e9e52be9c2384e1b1ff7566c640bc5b30c054efd64b081e7")
                    ),
                    new SignatureData(
                            Numeric.hexStringToByteArray("0x0fe9"),
                            Numeric.hexStringToByteArray("0x935584330d98f4a8a1cf83bf81ea7a18e33a962ad17b6a9eb8e04e3f5f95179d"),
                            Numeric.hexStringToByteArray("0x26804e07b5c105427497e8336300c1435d30ffa8d379dc27e5c1facd966c58db")
                    )
            };

            String[] rlpEncodedString = new String[] {
                    "0x20f86c018505d21dba0083015f909440efcb7d744fdc881f698a8ec573999fe63835458201c0f847f845820feaa0638f0d712b4b709cadab174dea6da50e5429ea59d78446e810af954af8d67981a0129ad4eb9222e161e9e52be9c2384e1b1ff7566c640bc5b30c054efd64b081e7",
                    "0x20f86c018505d21dba0083015f909440efcb7d744fdc881f698a8ec573999fe63835458201c0f847f845820fe9a0935584330d98f4a8a1cf83bf81ea7a18e33a962ad17b6a9eb8e04e3f5f95179da026804e07b5c105427497e8336300c1435d30ffa8d379dc27e5c1facd966c58db"
            };

            String combined = mTxObj.combineSignedRawTransactions(Arrays.asList(rlpEncodedString));
            assertEquals(expectedRLPEncoded, combined);
            assertEquals(expectedSignature[0], mTxObj.getSignatures().get(0));
            assertEquals(expectedSignature[1], mTxObj.getSignatures().get(1));
            assertEquals(expectedSignature[2], mTxObj.getSignatures().get(2));
        }

        @Test
        public void throwException_differentField() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("Transactions containing different information cannot be combined.");

            SignatureData signatureData = new SignatureData(
                    Numeric.hexStringToByteArray("0x0fea"),
                    Numeric.hexStringToByteArray("0x3d820b27d0997baf16f98df01c7b2b2e9734ad05b2228c4d403c2facff8397f3"),
                    Numeric.hexStringToByteArray("0x1f4a44eeb8b7f0b0019162d1d6b90c401078e56fcd7495e74f7cfcd37e25f017")
            );

            String nonce = "0x1000";

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            String rlpEncoded = "0x20f86c018505d21dba0083015f909440efcb7d744fdc881f698a8ec573999fe63835458201c0f847f845820feaa0638f0d712b4b709cadab174dea6da50e5429ea59d78446e810af954af8d67981a0129ad4eb9222e161e9e52be9c2384e1b1ff7566c640bc5b30c054efd64b081e7";
            List<String> list = new ArrayList<>();
            list.add(rlpEncoded);

            mTxObj.combineSignedRawTransactions(list);
        }
    }

    public static class getRawTransactionTest {

        @Test
        public void getRawTransaction() {
            List<AccountUpdate> updateList = getAccountUpdateList();
            List<ExpectedData> expectedDataList = getExpectedDataList();

            for(int i=0; i< updateList.size(); i++) {
                assertEquals(expectedDataList.get(i).expectedRLP, updateList.get(i).getRawTransaction());
            }
//            String rawTx = mTxObj.getRawTransaction();
//            assertEquals(expectedRLPEncoding, rawTx);
        }
    }

    public static class getTransactionHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x40efcb7d744fdc881f698a8ec573999fe6383545";
        String gas = "0x15f90";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";
        Account account = Account.createWithAccountKeyLegacy(from);

        AccountUpdate mTxObj;

        @Test
        public void getTransactionHash() {
            List<AccountUpdate> updateList = getAccountUpdateList();
            List<ExpectedData> expectedDataList = getExpectedDataList();

            for(int i=0; i< updateList.size(); i++) {
                assertEquals(expectedDataList.get(i).expectedTxHash, updateList.get(i).getTransactionHash());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getTransactionHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getTransactionHash();
        }
    }

    public static class getSenderTxHashTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x40efcb7d744fdc881f698a8ec573999fe6383545";
        String gas = "0x15f90";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";
        Account account = Account.createWithAccountKeyLegacy(from);

        AccountUpdate mTxObj;


        @Test
        public void getSenderTransactionHash() {
            List<AccountUpdate> updateList = getAccountUpdateList();
            List<ExpectedData> expectedDataList = getExpectedDataList();

            for(int i=0; i< updateList.size(); i++) {
                assertEquals(expectedDataList.get(i).expectedTxHash, updateList.get(i).getSenderTxHash());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getSenderTxHash();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getSenderTxHash();
        }
    }

    public static class getRLPEncodingForSignatureTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        String from = "0x40efcb7d744fdc881f698a8ec573999fe6383545";
        String gas = "0x15f90";
        String gasPrice = "0x5d21dba00";
        String chainID = "0x7e3";
        String nonce = "0x1";
        Account account = Account.createWithAccountKeyLegacy(from);

        AccountUpdate mTxObj;


        @Test
        public void getRLPEncodingForSignature() {
            List<AccountUpdate> updateList = getAccountUpdateList();
            List<ExpectedData> expectedDataList = getExpectedDataList();

            for(int i=0; i< updateList.size(); i++) {
                assertEquals(expectedDataList.get(i).expectedRlpEncodingForSigning, updateList.get(i).getCommonRLPEncodingForSignature());
            }
        }

        @Test
        public void throwException_NotDefined_Nonce() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("nonce is undefined. Define nonce in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_gasPrice() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("gasPrice is undefined. Define gasPrice in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setGas(gas)
                    .setNonce(nonce)
                    .setChainId(chainID)
                    .setAccount(account)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }

        @Test
        public void throwException_NotDefined_chainID() {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("chainId is undefined. Define chainId in transaction or use 'transaction.fillTransaction' to fill values.");

            mTxObj = new AccountUpdate.Builder()
                    .setFrom(from)
                    .setNonce(nonce)
                    .setGas(gas)
                    .setGasPrice(gasPrice)
                    .setAccount(account)
                    .build();

            mTxObj.getRLPEncodingForSignature();
        }
    }
}
