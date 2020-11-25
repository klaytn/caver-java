package com.klaytn.caver.wallet;

import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;

import java.util.function.Function;

public interface IWallet {

    boolean isExisted(String address);

    boolean remove(String address) throws Exception;

    AbstractTransaction sign(String address, AbstractTransaction transaction) throws Exception;

    AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction) throws Exception;
}
