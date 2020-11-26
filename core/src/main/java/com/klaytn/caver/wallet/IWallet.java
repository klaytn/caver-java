package com.klaytn.caver.wallet;

import com.klaytn.caver.transaction.AbstractFeeDelegatedTransaction;
import com.klaytn.caver.transaction.AbstractTransaction;

import java.util.List;
import java.util.function.Function;

/**
 * The Interface of wallet that can manage multiple key data.
 */
public interface IWallet {

    /**
     * Generates key data to manage in wallet.
     * @param num The number of key data to create.
     * @return List
     */
    List<String> generate(int num);

    /**
     * Check whether there is a key data corresponding to the address passed as a parameter in the wallet.
     * @param address An address to find key data in wallet.
     * @return boolean
     */
    boolean isExisted(String address);

    /**
     * Deletes the key data that associates with the given address from wallet.
     * @param address An address of the key data to be deleted in wallet.
     * @return boolean
     * @throws Exception
     */
    boolean remove(String address) throws Exception;

    /**
     * Signs the transaction using all keys in the key data corresponding to the address.
     * @param address An address of key data in wallet.
     * @param transaction An AbstractTransaction instance to sign
     * @return AbstractTransaction
     * @throws Exception
     */
    AbstractTransaction sign(String address, AbstractTransaction transaction) throws Exception;

    /**
     * Signs the FeeDelegatedTransaction using all keys in the key data corresponding to the address.
     * @param address An address of key data in wallet.
     * @param transaction An AbstractFeeDelegatedTransaction instance to sign.
     * @return AbstractFeeDelegatedTransaction
     * @throws Exception
     */
    AbstractFeeDelegatedTransaction signAsFeePayer(String address, AbstractFeeDelegatedTransaction transaction) throws Exception;
}
