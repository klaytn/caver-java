package com.klaytn.caver.kct.kip7;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.SendOptions;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class KIP7 extends Contract {

    private static final String FUNCTION_SUPPORT_INTERFACE = "supportsInterface";
    private static final String FUNCTION_NAME = "name";
    private static final String FUNCTION_SYMBOL = "symbol";
    private static final String FUNCTION_DECIMALS = "decimals";
    private static final String FUNCTION_TOTAL_SUPPLY = "totalSupply";
    private static final String FUNCTION_BALANCE_OF = "balanceOf";
    private static final String FUNCTION_ALLOWANCE = "allowance";
    private static final String FUNCTION_IS_MINTER = "isMinter";
    private static final String FUNCTION_IS_PAUSER = "isPauser";

    public KIP7(Caver caver) throws IOException {
        super(caver, KIP7ConstantData.ABI);
    }

    public KIP7(Caver caver, String contractAddress) throws IOException {
        super(caver, KIP7ConstantData.ABI, contractAddress);
    }

    public static KIP7 deploy(Caver caver, KIP7DeployParam tokenInfo, String deployer) throws IOException, NoSuchMethodException, InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, TransactionException {
        List deployArgument = Arrays.asList(tokenInfo.getName(), tokenInfo.getSymbol(), tokenInfo.getDecimals(), tokenInfo.getInitialSupply());
        ContractDeployParams contractDeployParams = new ContractDeployParams(KIP7ConstantData.BINARY, deployArgument);
        SendOptions sendOptions = new SendOptions(deployer, BigInteger.valueOf(4000000));

        KIP7 kip7 = new KIP7(caver);
        kip7.deploy(contractDeployParams, sendOptions);

        return kip7;
    }

    @Override
    public KIP7 clone() {
        try {
            return new KIP7(this.getCaver());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KIP7 clone(String tokenAddress) {
        try {
            return new KIP7(this.getCaver(), tokenAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean supportInterface(String interfaceId) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SendOptions sendOptions = new SendOptions(null, (String) null);
        List<Type> result = this.getMethod("supportInterface").call(Arrays.asList(interfaceId), sendOptions);

        return (boolean)result.get(0).getValue();
    }

//    public boolean name() {
//        SendOptions sendOptions = new SendOptions(null, (String) null);
//    }
}
