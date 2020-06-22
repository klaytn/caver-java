package com.klaytn.caver.scenario;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayAccountKey;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.ValueTransfer;
import com.klaytn.caver.tx.account.AccountKey;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.account.AccountKeyRoleBased;
import com.klaytn.caver.tx.account.AccountKeyWeightedMultiSig;
import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.AccountUpdateTransaction;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.model.TransactionTransformer;
import com.klaytn.caver.utils.KlayUnit;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.LocalValues.LOCAL_CHAIN_ID;
import static org.junit.Assert.assertEquals;

public class RoleBasedAccountScenario extends Scenario {
    protected final String MEMO = "Klaytn MemoTest 1234567890!";

    protected void roleBasedTransactionTest(TransactionGetter transactionGetter, ReceiptChecker receiptChecker, boolean isUpdateTest) throws Exception {
        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.generateTestAccount(1, 1, 1);
        List<KlayCredentials> senderCredentialsList = roleBasedAccountGenerator.getSenderCredentialForTest(isUpdateTest);

        TransactionManager transactionManager = new TransactionManager.Builder(caver, senderCredentialsList.get(0))
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();

        TransactionTransformer transactionTransformer = transactionGetter.get(roleBasedAccountGenerator.getAddress());
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(transactionTransformer);
        receiptChecker.check(transactionTransformer, transactionReceipt);
    }

    protected void feeDelegatedRoleBasedMultiTransactionSignerTest(TransactionGetter transactionGetter, ReceiptChecker receiptChecker, boolean isUpdateTest) throws Exception {
        AccountSizeGenerator accountSizeGenerator = new AccountSizeGenerator();
        int transactionAccountSize = accountSizeGenerator.getTransactionAccountSize();
        int updateAccountSize = accountSizeGenerator.getUpdateAccountSize();

        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.generateTestAccount(transactionAccountSize, updateAccountSize, 1);
        List<KlayCredentials> senderCredentialsList = roleBasedAccountGenerator.getSenderCredentialForTest(isUpdateTest);
        List<KlayCredentials> feePayerCredentialsList = roleBasedAccountGenerator.getFeePayerAccountCredential();

        KlayRawTransaction klayRawTransaction = null;
        KlayTransactionReceipt.TransactionReceipt transactionReceipt;
        TransactionTransformer transactionTransformer = null;

        for (int i = 0; i < senderCredentialsList.size(); i++) {
            TransactionManager transactionManager = new TransactionManager.Builder(caver, senderCredentialsList.get(i))
                    .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                    .setChaindId(LOCAL_CHAIN_ID)
                    .build();

            if (klayRawTransaction == null) {
                // 1. The transaction constructor creates and signs a transaction.
                transactionTransformer = transactionGetter.get(roleBasedAccountGenerator.getAddress());
                klayRawTransaction = transactionManager.sign(transactionTransformer);
            } else {
                // 2. Those with the RoleTransaction key receive and sign the rawTransaction.
                klayRawTransaction = transactionManager.sign(klayRawTransaction.getValueAsString());
            }
        }

        // 3. After all signs are signed, the last person receiving the transaction signs and transmits the transaction to the klaytn network.
        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayerCredentialsList.get(0))
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChainId(LOCAL_CHAIN_ID)
                .build();

        transactionReceipt = feePayerManager.executeTransaction(klayRawTransaction.getValueAsString());
        receiptChecker.check(transactionTransformer, transactionReceipt);
    }

    protected void feeDelegatedRoleBasedTransactionMultiFeePayerTest(TransactionGetter transactionGetter, ReceiptChecker receiptChecker, boolean isUpdateTest) throws Exception {
        AccountSizeGenerator accountSizeGenerator = new AccountSizeGenerator();
        int feePayerAccountSize = accountSizeGenerator.getFeePayerAccountSize();

        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.generateTestAccount(1, 1, feePayerAccountSize);
        List<KlayCredentials> senderCredentialsList = roleBasedAccountGenerator.getSenderCredentialForTest(isUpdateTest);
        List<KlayCredentials> feePayerCredentialsList = roleBasedAccountGenerator.getFeePayerAccountCredential();

        KlayRawTransaction klayRawTransaction = null;
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = null;

        // 1. The transaction constructor creates and signs a transaction.
        TransactionManager transactionManager = new TransactionManager.Builder(caver, senderCredentialsList.get(0))
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();

        TransactionTransformer transactionTransformer = transactionGetter.get(roleBasedAccountGenerator.getAddress());
        klayRawTransaction = transactionManager.sign(transactionTransformer);

        for (int i = 0; i < feePayerAccountSize; i++) {
            if (i < feePayerAccountSize - 1) {
                // 2. Those with the RoleFeePayer key receive and sign the rawTransaction.
                FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayerCredentialsList.get(i))
                        .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                        .setChainId(LOCAL_CHAIN_ID)
                        .build();
                klayRawTransaction = feePayerManager.sign(klayRawTransaction.getValueAsString());
            } else {
                // 3. After all signs are signed, the last person receiving the transaction signs and transmits the transaction to the klaytn network.
                FeePayerManager feePayerManagerForExcuter = new FeePayerManager.Builder(caver, feePayerCredentialsList.get(i))
                        .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                        .setChainId(LOCAL_CHAIN_ID)
                        .build();
                transactionReceipt = feePayerManagerForExcuter.executeTransaction(klayRawTransaction.getValueAsString());
            }
        }

        receiptChecker.check(transactionTransformer, transactionReceipt);
    }

    protected void feeDelegatedRoleBasedTransactionMultiTransactionSignerMultiFeePayerTest(TransactionGetter transactionGetter, ReceiptChecker receiptChecker, boolean isUpdateTest) throws Exception {
        AccountSizeGenerator accountSizeGenerator = new AccountSizeGenerator();
        int transactionAccountSize = accountSizeGenerator.getTransactionAccountSize();
        int updateAccountSize = accountSizeGenerator.getUpdateAccountSize();
        int feePayerAccountSize = accountSizeGenerator.getFeePayerAccountSize();

        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.generateTestAccount(transactionAccountSize, updateAccountSize, feePayerAccountSize);
        List<KlayCredentials> senderCredentialsList = roleBasedAccountGenerator.getSenderCredentialForTest(isUpdateTest);
        List<KlayCredentials> feePayerCredentialsList = roleBasedAccountGenerator.getFeePayerAccountCredential();

        KlayRawTransaction klayRawTransaction = null;
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = null;
        TransactionTransformer transactionTransformer = null;

        for (int i = 0; i < senderCredentialsList.size(); i++) {
            TransactionManager transactionManager = new TransactionManager.Builder(caver, senderCredentialsList.get(i))
                    .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                    .setChaindId(LOCAL_CHAIN_ID)
                    .build();

            if (klayRawTransaction == null) {
                // 1. The transaction constructor creates and signs a transaction.
                transactionTransformer = transactionGetter.get(roleBasedAccountGenerator.getAddress());
                klayRawTransaction = transactionManager.sign(transactionTransformer);
            } else {
                // 2. Those with the RoleTransaction key receive and sign the rawTransaction.
                klayRawTransaction = transactionManager.sign(klayRawTransaction.getValueAsString());
            }
        }

        for (int i = 0; i < feePayerAccountSize; i++) {
            FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayerCredentialsList.get(i))
                    .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                    .setChainId(LOCAL_CHAIN_ID)
                    .build();

            if (i < feePayerAccountSize - 1) {
                // 3. Those with the RoleFeePayer key receive and sign the rawTransaction.
                klayRawTransaction = feePayerManager.sign(klayRawTransaction.getValueAsString());
            } else {
                // 4. After all signs are signed, the last person receiving the transaction signs and transmits the transaction to the klaytn network.
                transactionReceipt = feePayerManager.executeTransaction(klayRawTransaction.getValueAsString());
            }
        }

        receiptChecker.check(transactionTransformer, transactionReceipt);
    }

    protected void feeDelegatedRoleBasedTransactionTest(TransactionGetter transactionGetter, ReceiptChecker receiptChecker, boolean isUpdateTest) throws Exception {
        RoleBasedAccountGenerator roleBasedAccountGenerator = new RoleBasedAccountGenerator();
        roleBasedAccountGenerator.generateTestAccount(1, 1, 1);
        List<KlayCredentials> senderCredentialsList = roleBasedAccountGenerator.getSenderCredentialForTest(isUpdateTest);
        List<KlayCredentials> feePayerCredentialsList = roleBasedAccountGenerator.getFeePayerAccountCredential();

        // 1. The transaction constructor creates and signs a transaction.
        TransactionManager transactionManager = new TransactionManager.Builder(caver, senderCredentialsList.get(0))
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChaindId(LOCAL_CHAIN_ID)
                .build();

        TransactionTransformer transactionTransformer = transactionGetter.get(roleBasedAccountGenerator.getAddress());
        KlayRawTransaction klayRawTransaction = transactionManager.sign(transactionTransformer);

        // 2. After all signs are signed, the last person receiving the transaction signs and transmits the transaction to the klaytn network.
        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayerCredentialsList.get(0))
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                .setChainId(LOCAL_CHAIN_ID)
                .build();

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(klayRawTransaction.getValueAsString());
        receiptChecker.check(transactionTransformer, transactionReceipt);
    }

    protected List<ECKeyPair> createECKeyPairList(int size) throws Exception {
        List<ECKeyPair> ecKeyPairList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ecKeyPairList.add(Keys.createEcKeyPair());
        }

        return ecKeyPairList;
    }

    protected void chargeAccount(String address) throws Exception {
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.create(caver, BRANDON, LOCAL_CHAIN_ID).sendFunds(
                BRANDON.getAddress(),
                address,
                BigDecimal.valueOf(1),
                KlayUnit.Unit.KLAY, GAS_LIMIT
        ).send();

        assertEquals("0x1", transactionReceipt.getStatus());
    }

    protected KlayCredentials createAccount() throws Exception {
        KlayCredentials user = KlayCredentials.create(Keys.createEcKeyPair());
        chargeAccount(user.getAddress());
        return user;
    }

    protected KlayCredentials createAccount(KlayCredentials oldCredentials) throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        KlayCredentials user = KlayCredentials.create(ecKeyPair.getPrivateKey().toString(16), oldCredentials.getAddress());
        chargeAccount(user.getAddress());
        return user;
    }

    protected AccountKeyWeightedMultiSig createRandomAccountKeyWeightedMultiSig(List<ECKeyPair> ecKeyPairList) throws Exception {
        Random random = new Random();
        List<AccountKeyWeightedMultiSig.WeightedPublicKey> weightedTransactionPublicKeys = new ArrayList<>();
        int sumOfWeight = 0;
        for (ECKeyPair ecKeyPair : ecKeyPairList) {
            int weight = random.nextInt(20) + 1;
            sumOfWeight += weight;
            AccountKeyWeightedMultiSig.WeightedPublicKey key = AccountKeyWeightedMultiSig.WeightedPublicKey.create(
                    BigInteger.valueOf(weight),
                    AccountKeyPublic.create(ecKeyPair.getPublicKey())
            );
            weightedTransactionPublicKeys.add(key);
        }

        AccountKeyWeightedMultiSig newAccountKey = AccountKeyWeightedMultiSig.create(
                BigInteger.valueOf(random.nextInt(sumOfWeight) + 1),
                weightedTransactionPublicKeys
        );

        return newAccountKey;
    }

    protected interface TransactionGetter {
        TransactionTransformer get(String from) throws Exception;
    }

    protected interface ReceiptChecker {
        void check(TransactionTransformer transactionTransformer, KlayTransactionReceipt.TransactionReceipt transactionReceipt) throws Exception;
    }

    class AccountSizeGenerator {
        private int transactionAccountSize;
        private int updateAccountSize;
        private int feePayerAccountSize;

        public AccountSizeGenerator() {
            Random random = new Random();
            this.transactionAccountSize = random.nextInt(9) + 1;
            this.updateAccountSize = random.nextInt(9) + 1;
            this.feePayerAccountSize = random.nextInt(9) + 1;
        }

        public int getTransactionAccountSize() {
            return transactionAccountSize;
        }

        public int getUpdateAccountSize() {
            return updateAccountSize;
        }

        public int getFeePayerAccountSize() {
            return feePayerAccountSize;
        }
    }

    public class RoleBasedAccountGenerator {
        private List<KlayCredentials> transactionAccountCredential;
        private List<KlayCredentials> updateAccountCredential;
        private List<KlayCredentials> feePayerAccountCredential;
        private KlayCredentials oldAccount;
        private AccountKey newAccountKey;
        private String address;

        public RoleBasedAccountGenerator() throws Exception {
            transactionAccountCredential = new ArrayList<>();
            updateAccountCredential = new ArrayList<>();
            feePayerAccountCredential = new ArrayList<>();
            caver = Caver.build(Caver.DEFAULT_URL);
        }

        public List<KlayCredentials> getTransactionAccountCredential() {
            return transactionAccountCredential;
        }

        public List<KlayCredentials> getUpdateAccountCredential() {
            return updateAccountCredential;
        }

        public List<KlayCredentials> getFeePayerAccountCredential() {
            return feePayerAccountCredential;
        }

        public List<KlayCredentials> getSenderCredentialForTest(boolean isUpdateTest) {
            if (isUpdateTest) {
                return updateAccountCredential;
            }
            return transactionAccountCredential;
        }

        public KlayCredentials getOldAccount() {
            return oldAccount;
        }

        public AccountKey getNewAccountKey() {
            return newAccountKey;
        }

        public String getAddress() {
            return address;
        }

        public void generateTestAccount(int transactionAccountCount, int updateAccountCount, int feePayerAccountCount) throws Exception {
            oldAccount = createAccount();
            address = oldAccount.getAddress();

            setRandomRoleBasedNewAccountKey(oldAccount, transactionAccountCount, updateAccountCount, feePayerAccountCount);

            TransactionManager transactionManager = new TransactionManager.Builder(caver, oldAccount)
                    .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 10))
                    .setChaindId(LOCAL_CHAIN_ID)
                    .build();

            AccountUpdateTransaction accountUpdateTx = AccountUpdateTransaction.create(
                    oldAccount.getAddress(),
                    newAccountKey,
                    GAS_LIMIT
            );

            KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(accountUpdateTx);
            assertEquals("0x1", transactionReceipt.getStatus());

            KlayAccountKey klayAccountKey = caver.klay().getAccountKey(oldAccount.getAddress(), DefaultBlockParameterName.LATEST).send();
            AccountKey responseAccountKey = klayAccountKey.getResult().getKey();

            assertEquals("Response\n" + responseAccountKey.toString() + "\nExpected" + newAccountKey.toString(), responseAccountKey, newAccountKey);
        }

        public void setRandomRoleBasedNewAccountKey(KlayCredentials oldAccount, int transactionAccountCount, int updateAcocountCount, int feePayerAccountCount) throws Exception {
            List<AccountKey> roleBasedAccountKeyList = new ArrayList<>();
            List<ECKeyPair> transactionECKeyPairList = new ArrayList<>();
            List<ECKeyPair> updateECKeyPairList = new ArrayList<>();
            List<ECKeyPair> feePayerECKeyPairList = new ArrayList<>();

            for (int i = 0; i < transactionAccountCount; i++) {
                KlayCredentials credentials = KlayCredentials.create(createECKeyPairList(10 / transactionAccountCount), createECKeyPairList(0), createECKeyPairList(0), oldAccount.getAddress());
                transactionAccountCredential.add(credentials);
                transactionECKeyPairList.addAll(credentials.getEcKeyPairsForTransactionList());
            }
            for (int i = 0; i < updateAcocountCount; i++) {
                KlayCredentials credentials = KlayCredentials.create(createECKeyPairList(0), createECKeyPairList(10 / updateAcocountCount), createECKeyPairList(0), oldAccount.getAddress());
                updateAccountCredential.add(credentials);
                updateECKeyPairList.addAll(credentials.getEcKeyPairsForUpdateList());
            }
            for (int i = 0; i < feePayerAccountCount; i++) {
                KlayCredentials credentials = KlayCredentials.create(createECKeyPairList(0), createECKeyPairList(0), createECKeyPairList(10 / feePayerAccountCount), oldAccount.getAddress());
                feePayerAccountCredential.add(credentials);
                feePayerECKeyPairList.addAll(credentials.getEcKeyPairsForFeePayerList());
            }

            roleBasedAccountKeyList.add(createRandomAccountKeyWeightedMultiSig(transactionECKeyPairList));
            roleBasedAccountKeyList.add(createRandomAccountKeyWeightedMultiSig(updateECKeyPairList));
            roleBasedAccountKeyList.add(createRandomAccountKeyWeightedMultiSig(feePayerECKeyPairList));

            newAccountKey = AccountKeyRoleBased.create(roleBasedAccountKeyList);
        }
    }
}
