package com.klaytn.caver.generated;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crpyto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayLogs;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class SecondaryMock extends SmartContract {
    private static final String BINARY = "60806040819052600080546001600160a01b0319163317908190556001600160a01b031681527f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d990602090a16102678061005a6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80632348238c14610046578063846224251461006e578063c6dbdf6114610076575b600080fd5b61006c6004803603602081101561005c57600080fd5b50356001600160a01b031661009a565b005b61006c610182565b61007e6101cd565b604080516001600160a01b039092168252519081900360200190f35b6000546001600160a01b031633146100e35760405162461bcd60e51b815260040180806020018281038252602c815260200180610207602c913960400191505060405180910390fd5b6001600160a01b0381166101285760405162461bcd60e51b815260040180806020018281038252602a8152602001806101dd602a913960400191505060405180910390fd5b600080546001600160a01b0319166001600160a01b03838116919091179182905560408051929091168252517f4101e71e974f68df5e9730cc223280b41654676bbb052cdcc735c3337e64d2d9916020908290030190a150565b6000546001600160a01b031633146101cb5760405162461bcd60e51b815260040180806020018281038252602c815260200180610207602c913960400191505060405180910390fd5b565b6000546001600160a01b03169056fe5365636f6e646172793a206e6577207072696d61727920697320746865207a65726f20616464726573735365636f6e646172793a2063616c6c6572206973206e6f7420746865207072696d617279206163636f756e74a265627a7a72305820e6d8d9988f3f9a427c09f30e063e17043fbd7d8db3d58067df577b21f1a7d3a764736f6c63430005090032";

    public static final String FUNC_TRANSFERPRIMARY = "transferPrimary";

    public static final String FUNC_ONLYPRIMARYMOCK = "onlyPrimaryMock";

    public static final String FUNC_PRIMARY = "primary";

    public static final Event PRIMARYTRANSFERRED_EVENT = new Event("PrimaryTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected SecondaryMock(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected SecondaryMock(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> transferPrimary(String recipient) {
        final Function function = new Function(
                FUNC_TRANSFERPRIMARY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(recipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onlyPrimaryMock() {
        throw new RuntimeException("cannot call constant function with void return type");
    }

    public RemoteCall<String> primary() {
        final Function function = new Function(FUNC_PRIMARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<PrimaryTransferredEventResponse> getPrimaryTransferredEvents(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
        List<SmartContract.EventValuesWithLog> valueList = extractEventParametersWithLog(PRIMARYTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<PrimaryTransferredEventResponse> responses = new ArrayList<PrimaryTransferredEventResponse>(valueList.size());
        for (SmartContract.EventValuesWithLog eventValues : valueList) {
            PrimaryTransferredEventResponse typedResponse = new PrimaryTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SecondaryMock load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new SecondaryMock(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static SecondaryMock load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SecondaryMock(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SecondaryMock> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SecondaryMock.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SecondaryMock> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SecondaryMock.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class PrimaryTransferredEventResponse {
        public KlayLogs.Log log;

        public String recipient;
    }
}
