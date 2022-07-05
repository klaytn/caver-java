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

package com.klaytn.caver.legacy.feature;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.tx.account.AccountKeyPublic;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.*;
import com.klaytn.caver.tx.type.AbstractTxType;
import com.klaytn.caver.utils.CodeFormat;
import com.klaytn.caver.utils.TransactionDecoder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Keys;
import com.klaytn.caver.utils.ChainId;
import org.web3j.utils.Numeric;
import static junit.framework.TestCase.assertEquals;

import java.math.BigInteger;

public class CustomGasTest {
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000L);

    private static Caver caver;
    private static DefaultGasProvider gasProvider;

    private static KlayCredentials sender;
    private static KlayCredentials feePayer;
    private static KlayCredentials to;

    private static BigInteger value;

    @BeforeClass
    public static void setup() throws Exception {
        caver = Caver.build("http://localhost:8551");
        gasProvider= new DefaultGasProvider(caver);

        sender = KlayCredentials.create(Keys.createEcKeyPair());
        feePayer = KlayCredentials.create(Keys.createEcKeyPair());
        to = KlayCredentials.create(Keys.createEcKeyPair());

        value = BigInteger.ZERO;
    }

    @Test
    public void testCustomGasPriceWithValueTransfer() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        ValueTransferTransaction tx = ValueTransferTransaction.create(
                sender.getAddress(),
                to.getAddress(),
                value,
                suggestedGasPrice,
                GAS_LIMIT
        );
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }

    @Test
    public void testCustomGasPriceWithValueTransferMemo() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        ValueTransferTransaction tx = ValueTransferTransaction.create(
                sender.getAddress(),
                to.getAddress(),
                value,
                suggestedGasPrice,
                GAS_LIMIT
        ).memo("test string");
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }

    @Test
    public void testCustomGasPriceWithAccountUpdate() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        AccountUpdateTransaction tx = AccountUpdateTransaction.create(
                sender.getAddress(),
                AccountKeyPublic.create(Keys.createEcKeyPair().getPublicKey()),
                suggestedGasPrice,
                GAS_LIMIT
        );
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }

    @Test
    public void testCustomGasPriceWithSmartContractDeploy() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        SmartContractDeployTransaction tx = SmartContractDeployTransaction.create(
                sender.getAddress(),
                value,
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                suggestedGasPrice,
                GAS_LIMIT,
                CodeFormat.EVM
        );
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }

    @Test
    public void testCustomGasPriceWithSmartContractExecution() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        SmartContractExecutionTransaction tx = SmartContractExecutionTransaction.create(
                sender.getAddress(),
                to.getAddress(),
                value,
                Numeric.hexStringToByteArray("0xd14e62b8000000000000000000000000000000000000000000000000dc67327b51f7c636"),
                suggestedGasPrice,
                GAS_LIMIT
        );
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }

    @Test
    public void testCustomGasPriceWithCancel() throws Exception {
        BigInteger suggestedGasPrice = gasProvider.getGasPrice();
        CancelTransaction tx = CancelTransaction.create(
                sender.getAddress(),
                suggestedGasPrice,
                GAS_LIMIT
        );
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        TransactionManager tm = new TransactionManager.Builder(caver, sender).build();
        KlayRawTransaction rawTransaction = tm.sign(tx);
        AbstractTxType decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeDelegate();
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        FeePayerManager fm = new FeePayerManager.Builder(caver, feePayer).build();
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);

        tx.feeRatio(BigInteger.valueOf(50));
        assertEquals(tx.getGasPrice(), suggestedGasPrice);

        rawTransaction = tm.sign(tx);
        rawTransaction = fm.sign(rawTransaction.getValueAsString());
        decoded = TransactionDecoder.decode(rawTransaction.getValueAsString());
        assertEquals(decoded.getGasPrice(), suggestedGasPrice);
    }
}
