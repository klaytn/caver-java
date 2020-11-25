package com.klaytn.caver.wallet;

import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;

import java.util.List;
import java.util.function.Function;

public interface IWallet {

    /**
     * Generates wallet data.
     * @param num The number of data to create.
     * @return List
     */
    List<String> generate(int num);

    /**
     * Check whether there is a wallet data corresponding to the address passed as a parameter in the wallet.
     * @param address An address to find wallet data in wallet.
     * @return boolean
     */
    boolean isExisted(String address);

    /**
     * Deletes the wallet data that associates with the given address from wallet.
     * @param address An address of the wallet data to be deleted in wallet.
     * @return boolean
     * @throws Exception
     */
    boolean remove(String address) throws Exception;

    /**
     * Signs the transaction using all keys in the wallet data corresponding to the address.
     * @param address An address of wallet data in wallet.
     * @param transaction An AbstractTransaction instance to sign
     * @return AbstractTransaction
     * @throws Exception
     */
    AbstractTransaction sign(String address, AbstractTransaction transaction) throws Exception;

    /**
     * Signs the FeeDelegatedTransaction using all keys in the wallet data corresponding to the address.
     * @param address An address of keyring in KeyringContainer.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @return AbstractFeeDelegatedTransaction
     * @throws Exception
     */
    AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction) throws Exception;
}
