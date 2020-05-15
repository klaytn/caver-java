package com.klaytn.caver.common;

import com.klaytn.caver.account.*;
import com.klaytn.caver.utils.AccountKeyPublicUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public void checkPublicKey(String expected, String actual) {
        expected = AccountKeyPublicUtils.compressPublicKey(expected);
        actual = AccountKeyPublicUtils.compressPublicKey(actual);

        expected = Numeric.cleanHexPrefix(expected);
        actual = Numeric.cleanHexPrefix(actual);

        assertEquals(expected, actual);
    }

    public void checkAccountKeyPublic(IAccountKey accountKey, String expectedPublicKey) {
        assertTrue(accountKey instanceof AccountKeyPublic);
        checkPublicKey(expectedPublicKey, ((AccountKeyPublic) accountKey).getPublicKey());
    }

    public void checkAccountKeyWeightedMultiSig(IAccountKey accountKey,String[] expectedKeys, WeightedMultiSigOptions expectedOption) {
        assertTrue(accountKey instanceof  AccountKeyWeightedMultiSig);
        AccountKeyWeightedMultiSig actualAccountKey = (AccountKeyWeightedMultiSig)accountKey;
        assertEquals(expectedOption.getThreshold(), actualAccountKey.getThreshold());

        List<WeightedPublicKey> weightedPublicKeyList = actualAccountKey.getWeightedPublicKeys();

        for(int i=0; i < weightedPublicKeyList.size(); i++) {
            checkPublicKey(expectedKeys[i], weightedPublicKeyList.get(i).getPublicKey());
            assertEquals(expectedOption.getWeights().get(i), weightedPublicKeyList.get(i).getWeight());
        }
    }

    //CA-ACCOUNT-043
    @Test
    public void createToAccountKeyPublic_uncompressed() {
        String address = "0xf43dcbb903a0b4b48a7dfa8a370a63f0a731708d";
        String publicKey = "0x1e3aec6e8bd8247aea112c3d1094566272974e56bb0151c58745847e2998ad0e5e8360b120dceea794c6cb1e4215208a78c82e8df5dcf1ac9aa73f1568ee5f2e";

        Account account = Account.create(address, publicKey);
        assertEquals(address, account.getAddress());
        checkAccountKeyPublic(account.getAccountKey(), publicKey);
    }

    //CA-ACCOUNT-044
    @Test
    public void createToAccountKeyPublic_compressed() {
        String address = "0xf43dcbb903a0b4b48a7dfa8a370a63f0a731708d";
        String publicKey = "0x021e3aec6e8bd8247aea112c3d1094566272974e56bb0151c58745847e2998ad0e";

        Account account = Account.create(address, publicKey);
        assertEquals(address, account.getAddress());
        checkAccountKeyPublic(account.getAccountKey(), publicKey);
    }

    //CA-ACCOUNT-045
    @Test
    public void createToAccountKeyWeightMultiSig_uncompressed() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] publicKeys = {
                "0x91245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02f2b0870653417943e795e7c8694c4f8be8af865b7a0224d1dec0bf8a1bf1b5a6",
                "0x77e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f3570b1a104b67d1cd169bbf61dd557f15ab5ee8b661326096954caddadf34ae6ac8",
                "0xd3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171cc4bd2ba7f0c969cd72bfa49c854d8ac2cf3d0edea7f0ce0fd31cf080374935d",
                "0xcfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3ee47cd2b6bbb917658c5fd3d02b0ddf1242b1603d1acbde7812a7d9d684ed37a9"
        };

        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)};
        WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

        Account account = Account.create(address, publicKeys, options);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), publicKeys, options);
    }

    //CA-ACCOUNT-046
    @Test
    public void createToAccountKeyWeightMultiSig_compressed() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] publicKeys = {
                "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e"
        };

        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)};
        WeightedMultiSigOptions options = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

        Account account = Account.create(address, publicKeys, options);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), publicKeys, options);
    }

    //CA-ACCOUNT-047
    @Test
    public void createToAccountKeyWeightMultiSig_noOption() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] publicKeys = {
                "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e"
        };

        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
        WeightedMultiSigOptions expectedOptions = new WeightedMultiSigOptions(BigInteger.ONE, Arrays.asList(weights));

        Account account = Account.create(address, publicKeys);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), publicKeys, expectedOptions);
    }

    //CA-ACCOUNT-048
    @Test
    public void createToAccountKeyRoleBased_uncompressed() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                    "0xb86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9af36becba90c8de06fd06da16492263267a63720985f94fc5a027d0a26d25e6ae",
                    "0xe4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d3bc4d619872b96c091c741a15736a7ef12f530b7593038bbbfbf6c35deee8a34",
                },
                {
                    "0x1a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716afe39b6106fb9db360fb5779e2d346c8328698174831941586b11bdc3e755905",
                    "0x1427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f65f63b92fd60be61c5d176ae1771e7738e6a043af814b9af5d81137df29ee95f2",
                    "0x90fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea5fa9bd7d8bc5c66a9a08d77563458b004bbd1d594a3a12ef108cdc7c04c525a6",
                },
                {
                    "0x91245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02f2b0870653417943e795e7c8694c4f8be8af865b7a0224d1dec0bf8a1bf1b5a6",
                    "0x77e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f3570b1a104b67d1cd169bbf61dd557f15ab5ee8b661326096954caddadf34ae6ac8",
                    "0xd3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171cc4bd2ba7f0c969cd72bfa49c854d8ac2cf3d0edea7f0ce0fd31cf080374935d",
                    "0xcfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3ee47cd2b6bbb917658c5fd3d02b0ddf1242b1603d1acbde7812a7d9d684ed37a9",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
        };

        WeightedMultiSigOptions[] options = {
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[1])),
                new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[2])),
        };

        Account account = Account.create(address, Arrays.asList(publicKeys), Arrays.asList(options));
        assertEquals(address, account.getAddress());

        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) account.getAccountKey();

        IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(txKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(txKey, publicKeys[0], options[0]);

        IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdateKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdateKey, publicKeys[1], options[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], options[2]);
    }

    //CA-ACCOUNT-049
    @Test
    public void createToAccountKeyRoleBased_compressed() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                        "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                        "0x02e4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d",
                },
                {
                        "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                        "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                        "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                },
                {
                        "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                        "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                        "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                        "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2)},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(2)},
        };

        WeightedMultiSigOptions[] options = {
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[1])),
                new WeightedMultiSigOptions(BigInteger.valueOf(3), Arrays.asList(optionWeight[2])),
        };

        Account account = Account.create(address, Arrays.asList(publicKeys), Arrays.asList(options));
        assertEquals(address, account.getAddress());

        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) account.getAccountKey();

        IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(txKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(txKey, publicKeys[0], options[0]);

        IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdateKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdateKey, publicKeys[1], options[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], options[2]);
    }

    //CA-ACCOUNT-050
    @Test
    public void createToAccountKeyRoleBased_noOption() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                        "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                        "0x02e4d4901155edabc2bd5b356c63e58af20fe0a74e5f210de6396b74094f40215d",
                },
                {
                        "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                        "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                        "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                },
                {
                        "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                        "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                        "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                        "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[2])),
        };

        Account account = Account.create(address, Arrays.asList(publicKeys));
        assertEquals(address, account.getAddress());

        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) account.getAccountKey();

        IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(txKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(txKey, publicKeys[0], expectedOptions[0]);

        IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdateKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdateKey, publicKeys[1], expectedOptions[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-051
    @Test
    public void createToAccountKeyRoleBased_noOption_AccountKeyPublic() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                        "0x02b86b2787e8c7accd7d2d82678c9bef047a0aafd72a6e690817506684e8513c9a",
                },
                {
                        "0x031a909c4d7dbb5281b1d1b55e79a1b2568111bd2830246c3173ce824000eb8716",
                        "0x021427ac6351bbfc15811e8e5389a674b01d7a2c253e69a6ed30a33583864368f6",
                        "0x0290fe4bb78bc981a40874ebcff2f9de4eba1e59ecd7a271a37814413720a3a5ea",
                },
                {
                        "0x0291245244462b3eee6436d3dc0ba3f69ef413fe2296c729733eff891a55f70c02",
                        "0x0277e05dd93cdd6362f8648447f33d5676cbc5f42f4c4946ae1ad62bd4c0c4f357",
                        "0x03d3bb14320d87eed081ae44740b5abbc52bac2c7ccf85b6281a0fc69f3ba4c171",
                        "0x03cfa4d1bee51e59e6842b136ff95b9d01385f94bed13c4be8996c6d20cb732c3e",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[2])),
        };

        Account account = Account.create(address, Arrays.asList(publicKeys));
        assertEquals(address, account.getAddress());

        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) account.getAccountKey();

        IAccountKey txKey = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(txKey instanceof  AccountKeyPublic);
        checkAccountKeyPublic(txKey, publicKeys[0][0]);

        IAccountKey accountUpdateKey = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdateKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdateKey, publicKeys[1], expectedOptions[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-052
    @Test
    public void createFromRLPEncoding_AccountKeyLegacy() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String rlpEncodedAccountKey = "0x01c0";

        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyLegacy);
    }

    //CA-ACCOUNT-053
    @Test
    public void createFromRLPEncoding_AccountKeyPublic() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String expectedPubKey = "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e";
        String rlpEncodedAccountKey = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
        checkAccountKeyPublic(account.getAccountKey(), expectedPubKey);
    }

    //CA-ACCOUNT-054
    @Test
    public void createFromRLPEncoding_AccountKeyFail() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String rlpEncodedAccountKey = "0x03c0";

        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyFail);
    }

    //CA-ACCOUNT-055
    @Test
    public void createFromRLPEncoding_AccountKeyWeightedMultiSig() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] expectPublicKey = {
            "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
            "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
        };
        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE};
        WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(2),Arrays.asList(weights));

        String rlpEncodedAccountKey= "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), expectPublicKey, expectedOption);
    }

    //CA-ACCOUNT-056
    @Test
    public void createFromRLPEncoding_AccountKeyRoleBased() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String rlpEncodedKey = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
        String[][] publicKeys = {
                {
                        "0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"
                },
                {
                        "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                        "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                },
                {
                        "0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                        "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(),
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
        };

        Account account = Account.createFromRLPEncoding(address, rlpEncodedKey);
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);

        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased)account.getAccountKey();
        IAccountKey transaction = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(transaction instanceof  AccountKeyPublic);
        checkAccountKeyPublic(transaction, publicKeys[0][0]);

        IAccountKey accountUpdate = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdate instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdate, publicKeys[1], expectedOptions[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-057
    @Test
    public void createFromRLPEncoding_AccountKeyRoleBasedWithAccountNil() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String rlpEncodedKey = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
        String[][] publicKeys = {
                {
                        "0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"
                },
                {
                },
                {
                        "0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                        "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(),
                new WeightedMultiSigOptions(),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
        };

        Account account = Account.createFromRLPEncoding(address, rlpEncodedKey);
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);

        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased)account.getAccountKey();
        IAccountKey transaction = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(transaction instanceof  AccountKeyPublic);
        checkAccountKeyPublic(transaction, publicKeys[0][0]);

        IAccountKey accountUpdate = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdate instanceof  AccountKeyNil);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-058
    @Test
    public void createWithAccountKeyLegacy() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createWithAccountKeyLegacy(address);
        assertTrue(account.getAccountKey() instanceof AccountKeyLegacy);
        assertEquals(address, account.getAddress());
    }

    //CA-ACCOUNT-059
    @Test
    public void createWithAccountKeyPublic_uncompressed() {
        String address = "0xf43dcbb903a0b4b48a7dfa8a370a63f0a731708d";
        String publicKey = "0x1e3aec6e8bd8247aea112c3d1094566272974e56bb0151c58745847e2998ad0e5e8360b120dceea794c6cb1e4215208a78c82e8df5dcf1ac9aa73f1568ee5f2e";

        Account account = Account.createWithAccountKeyPublic(address, publicKey);
        assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
        assertEquals(address, account.getAddress());
        checkAccountKeyPublic(account.getAccountKey(), publicKey);
    }

    //CA-ACCOUNT-060
    @Test
    public void createWithAccountKeyPublic_compressed() {
        String address = "0xf43dcbb903a0b4b48a7dfa8a370a63f0a731708d";
        String publicKey = "0x021e3aec6e8bd8247aea112c3d1094566272974e56bb0151c58745847e2998ad0e";

        Account account = Account.createWithAccountKeyPublic(address, publicKey);
        assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
        assertEquals(address, account.getAddress());
        checkAccountKeyPublic(account.getAccountKey(), publicKey);
    }

    //CA-ACCOUNT-061
    @Test
    public void createWithAccountKeyFail() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";

        Account account = Account.createWithAccountKeyFail(address);
        assertTrue(account.getAccountKey() instanceof AccountKeyFail);
        assertEquals(address, account.getAddress());
    }

    //CA-ACCOUNT-062
    @Test
    public void createWithAccountWeightedMultiSig() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] expectPublicKey = {
                "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
        };
        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE};
        WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(2),Arrays.asList(weights));

        Account account = Account.createWithAccountKeyWeightedMultiSig(address, expectPublicKey, expectedOption);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), expectPublicKey, expectedOption);
    }

    //CA-ACCOUNT-063
    @Test
    public void createWithAccountWeightedMultiSig_NoOption() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[] expectPublicKey = {
                "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6"
        };
        BigInteger[] weights = {BigInteger.ONE, BigInteger.ONE};
        WeightedMultiSigOptions expectedOption = new WeightedMultiSigOptions(BigInteger.valueOf(1),Arrays.asList(weights));

        Account account = Account.createWithAccountKeyWeightedMultiSig(address, expectPublicKey);
        assertEquals(address, account.getAddress());
        checkAccountKeyWeightedMultiSig(account.getAccountKey(), expectPublicKey, expectedOption);
    }

    //CA-ACCOUNT-064
    @Test
    public void createWithAccountRoleBased() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                        "0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95"
                },
                {
                        "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                        "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                },
                {
                        "0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                        "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(),
                new WeightedMultiSigOptions(BigInteger.valueOf(2), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
        };

        Account account = Account.createWithAccountKeyRoleBased(address, Arrays.asList(publicKeys), Arrays.asList(expectedOptions));
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);

        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased)account.getAccountKey();
        IAccountKey transaction = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(transaction instanceof  AccountKeyPublic);
        checkAccountKeyPublic(transaction, publicKeys[0][0]);

        IAccountKey accountUpdate = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdate instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdate, publicKeys[1], expectedOptions[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-065
    @Test
    public void createWithAccountRoleBased_NoOption() {
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        String[][] publicKeys = {
                {
                        "0x6250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a7117bc107912634970e82bc5450d28d6d1dcfa03f7d759d06b6be5ba96efd9eb95",
                        "0x1e3aec6e8bd8247aea112c3d1094566272974e56bb0151c58745847e2998ad0e5e8360b120dceea794c6cb1e4215208a78c82e8df5dcf1ac9aa73f1568ee5f2e"
                },
                {
                        "0xc10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9803a1898f45b2770eda7abce70e8503b5e82b748ec0ce557ac9f4f4796965e4e",
                        "0x1769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c12a4d0eeb91d7bd5d592653d43dd0593cfe24cb20a5dbef05832932e7c7191bf6",
                },
                {
                        "0xe7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4958423c3e2c2a45a9e0e4671b078c8763c3724416f3c6443279ebb9b967ab055",
                        "0x6f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d3a16e2e0f06d767ca158a1daf2463d78012287fd6503d1546229fdb1af532083",
                }
        };

        BigInteger[][] optionWeight = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ONE},
        };

        WeightedMultiSigOptions[] expectedOptions = {
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[0])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[1])),
                new WeightedMultiSigOptions(BigInteger.valueOf(1), Arrays.asList(optionWeight[2])),
        };

        Account account = Account.createWithAccountKeyRoleBased(address, Arrays.asList(publicKeys));
        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);

        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased)account.getAccountKey();
        IAccountKey transaction = accountKeyRoleBased.getRoleTransactionKey();
        assertTrue(transaction instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(transaction, publicKeys[0], expectedOptions[0]);

        IAccountKey accountUpdate = accountKeyRoleBased.getRoleAccountUpdateKey();
        assertTrue(accountUpdate instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(accountUpdate, publicKeys[1], expectedOptions[1]);

        IAccountKey feePayerKey = accountKeyRoleBased.getRoleFeePayerKey();
        assertTrue(feePayerKey instanceof  AccountKeyWeightedMultiSig);
        checkAccountKeyWeightedMultiSig(feePayerKey, publicKeys[2], expectedOptions[2]);
    }

    //CA-ACCOUNT-066
    @Test
    public void getRLPEncodingAccountKey_AccountKeyLegacy() {
        String rlpEncodedAccountKey = "0x01c0";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyLegacy);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }

    //CA-ACCOUNT-067
    @Test
    public void getRLPEncodingAccountKey_AccountKeyPublic() {
        String rlpEncodedAccountKey = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyPublic);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }

    //CA-ACCOUNT-068
    @Test
    public void getRLPEncodingAccountKey_AccountKeyFail() {
        String rlpEncodedAccountKey = "0x03c0";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyFail);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }

    //CA-ACCOUNT-069
    @Test
    public void getRLPEncodingAccountKey_AccountKeyWeightedMultiSig() {
        String rlpEncodedAccountKey = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyWeightedMultiSig);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }

    //CA-ACCOUNT-070
    @Test
    public void getRLPEncodingAccountKey_AccountKeyRoleBased() {
        String rlpEncodedAccountKey = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }

    //CA-ACCOUNT-071
    @Test
    public void getRLPEncodingAccountKey_AccountKeyRoleBasedWithAccountNil() {
        String rlpEncodedAccountKey = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";
        String address = "0xab9825316619a0720ad891135e92adb84fd74fc1";
        Account account = Account.createFromRLPEncoding(address, rlpEncodedAccountKey);

        assertEquals(address, account.getAddress());
        assertTrue(account.getAccountKey() instanceof AccountKeyRoleBased);
        assertEquals(rlpEncodedAccountKey, account.getRLPEncodingAccountKey());
    }


}
