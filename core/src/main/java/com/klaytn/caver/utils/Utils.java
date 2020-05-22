package com.klaytn.caver.utils;

import org.bouncycastle.math.ec.ECPoint;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.util.regex.Pattern;

public class Utils {
    public static final int LENGTH_ADDRESS_String = 40;
    public static final int LENGTH_PRIVATE_KEY_STRING = 64;
    private static final Pattern HEX_STRING = Pattern.compile("^[0-9A-Fa-f]+$");

    public static boolean isPrivateKeyValid(String privateKey) {
        String noHexPrefixKey = Numeric.cleanHexPrefix(privateKey);
        if(noHexPrefixKey.length() != LENGTH_PRIVATE_KEY_STRING && HEX_STRING.matcher(noHexPrefixKey).matches()) {
            return false;
        }

        ECPoint point = Sign.publicPointFromPrivate(Numeric.toBigInt(privateKey));
        return point.isValid();
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == LENGTH_ADDRESS_String && HEX_STRING.matcher(cleanInput).matches();
    }

    public static boolean isKlaytnWalletKeyFormat(String key) {
        //0x{private key}0x{type}0x{address in hex}
        //[0] = privateKey
        //[1] = type - must be "00"
        //[2] = address
        key = Numeric.cleanHexPrefix(key);

        if(key.length() != 110) {
            return false;
        }

        String[] arr = key.split("0x");

        if(!arr[1].equals("00")) {
            return false;
        }
        if(!Utils.isValidAddress(arr[2])) {
            return false;
        }
        if(!Utils.isPrivateKeyValid(arr[0])) {
            return false;
        }

        return true;
    }

    public static String[] parseKlaytnWalletKey(String key) {
        //0x{private key}0x{type}0x{address in hex}
        //[0] = privateKey
        //[1] = type - must be "00"
        //[2] = address
        key = Numeric.cleanHexPrefix(key);
        String[] arr = key.split("0x");

        return arr;
    }

    public static String hashMessage(String message) {
        final String preamble = "\\x19Klaytn Signed Message:\\n";

        String klaytnMessage =  preamble + message.length() + message;
        return Hash.sha3(klaytnMessage);
    }

    public static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        SecureRandomUtils.secureRandom().nextBytes(bytes);
        return bytes;
    }

}
