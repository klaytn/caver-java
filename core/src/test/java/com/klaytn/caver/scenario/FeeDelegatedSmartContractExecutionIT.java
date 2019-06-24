/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.scenario;

import com.klaytn.caver.fee.FeePayer;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.model.KlayRawTransaction;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecution;
import com.klaytn.caver.utils.CodeFormat;
import org.junit.Test;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static com.klaytn.caver.base.Accounts.BRANDON;
import static com.klaytn.caver.base.Accounts.FEE_PAYER;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FeeDelegatedSmartContractExecutionIT extends Scenario {

    public static final String SET_COMMAND = "setCount(uint256)";
    public static final String COUNT_COMMAND = "count()";
    public static final int CHANGE_VALUE = 27;

    @Test
    public void testFeeDelegatedSmartContractExecution() throws Exception {
        String deployedContract = getDeployedContract();
        KlayRawTransaction senderRawTransaction = getSenderRawTransaction(deployedContract);

        TxTypeFeeDelegatedSmartContractExecution payerTransaction =
                TxTypeFeeDelegatedSmartContractExecution.decodeFromRawTransaction(senderRawTransaction.getValueAsString());
        assertEquals(BRANDON.getAddress(), payerTransaction.getFrom());

        KlayRawTransaction payerRawTransaction = new FeePayer(FEE_PAYER, BAOBAB_CHAIN_ID).sign(payerTransaction);
        KlayTransactionReceipt.TransactionReceipt receipt = sendTxAndGetReceipt(payerRawTransaction.getValueAsString());
        assertTrue(deployedContract.equalsIgnoreCase(receipt.getTo()));

        Bytes changedValue = getChangedValue(deployedContract);
        assertEquals(BigInteger.valueOf(CHANGE_VALUE), Numeric.toBigInt(changedValue.getResult()));
    }

    private Bytes getChangedValue(String deployedContract) throws java.io.IOException {
        CallObject callObject = new CallObject(
                BRANDON.getAddress(),
                deployedContract,
                GAS_LIMIT,
                GAS_PRICE,
                BigInteger.ZERO,
                Numeric.prependHexPrefix(Hash.sha3String(COUNT_COMMAND).substring(2, 10)));
        return caver.klay().call(callObject, DefaultBlockParameterName.LATEST).send();
    }

    private KlayRawTransaction getSenderRawTransaction(String deployedContract) throws Exception {
        BigInteger nonce = getNonce(BRANDON.getAddress());
        TxTypeFeeDelegatedSmartContractExecution senderTransaction
                = TxTypeFeeDelegatedSmartContractExecution.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                deployedContract,
                BigInteger.ZERO,
                BRANDON.getAddress(),
                getPayLoad()
        );
        return senderTransaction.sign(BRANDON, BAOBAB_CHAIN_ID);
    }

    private String getDeployedContract() throws Exception {
        KlayTransactionReceipt.TransactionReceipt receipt = SmartContract.sendDeployTransaction(caver, BRANDON, SmartContractDeployTransaction.create(
                BRANDON.getAddress(),
                BigInteger.ZERO,
                Numeric.hexStringToByteArray("0x60806040526000805560405160208061014b83398101806040528101908080519060200190929190505050806000819055505061010a806100416000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60c6565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260cc565b6040518082815260200191505060405180910390f35b60c46004803603810190808035906020019092919050505060d4565b005b60005481565b600043905090565b80600081905550505600a165627a7a72305820d9f890da4e30bac256db19aacc47a7025c902da590bd8ebab1fe5425f3670df000290000000000000000000000000000000000000000000000000000000000000001"),
                GAS_LIMIT,
                CodeFormat.EVM
        )).send();
        return receipt.getContractAddress();
    }

    private byte[] getPayLoad() {
        BigInteger replaceValue = BigInteger.valueOf(CHANGE_VALUE);
        String payLoadNoCommand = Numeric.toHexString(Numeric.toBytesPadded(replaceValue, 32)).substring(2);
        String payLoad = new StringBuilder(Hash.sha3String(SET_COMMAND)
                .substring(2, 10))
                .append(payLoadNoCommand)
                .toString();
        return Numeric.hexStringToByteArray(payLoad);
    }
}
