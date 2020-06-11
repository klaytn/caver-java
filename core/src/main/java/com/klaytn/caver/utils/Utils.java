package com.klaytn.caver.utils;

import com.klaytn.caver.crypto.KlaySignatureData;
import com.klaytn.caver.wallet.keyring.SignatureData;
import org.bouncycastle.math.ec.ECPoint;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static final int LENGTH_ADDRESS_String = 40;
    public static final int LENGTH_PRIVATE_KEY_STRING = 64;
    private static final Pattern HEX_STRING = Pattern.compile("^[0-9A-Fa-f]*$");

    public static boolean isValidPrivateKey(String privateKey) {
        String noHexPrefixKey = Numeric.cleanHexPrefix(privateKey);
        if(noHexPrefixKey.length() != LENGTH_PRIVATE_KEY_STRING && HEX_STRING.matcher(noHexPrefixKey).matches()) {
            return false;
        }

        ECPoint point = Sign.publicPointFromPrivate(Numeric.toBigInt(privateKey));
        return point.isValid();
    }

    public static boolean isAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == LENGTH_ADDRESS_String && HEX_STRING.matcher(cleanInput).matches();
    }

    public static boolean isHex(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        return HEX_STRING.matcher(cleanInput).matches();
    }

    public static boolean isHexStrict(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        return input.startsWith("0x") && HEX_STRING.matcher(cleanInput).matches();
    }

    public static boolean isNumber(String input) {
        try {
            Numeric.toBigInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isKlaytnWalletKey(String key) {
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
        if(!Utils.isAddress(arr[2])) {
            return false;
        }
        if(!Utils.isValidPrivateKey(arr[0])) {
            return false;
        }

        return true;
    }

    public static String[] parseKlaytnWalletKey(String key) {
        if(!isKlaytnWalletKey(key)) {
            throw new IllegalArgumentException("Invalid Klaytn wallet key.");
        }

        //0x{private key}0x{type}0x{address in hex}
        //[0] = privateKey
        //[1] = type - must be "00"
        //[2] = address
        key = Numeric.cleanHexPrefix(key);
        String[] arr = key.split("0x");

        for(int i=0; i< arr.length; i++) {
            arr[i] = Numeric.prependHexPrefix(arr[i]);
        }

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

    public static boolean isEmptySig(SignatureData signatureData) {
        SignatureData emptySig = SignatureData.getEmptySignature();

        return emptySig.equals(signatureData);
    }


    public static boolean isEmptySig(List<SignatureData> signatureDataList) {
        SignatureData emptySig = SignatureData.getEmptySignature();

        boolean isMatched = signatureDataList.stream().allMatch(emptySig::equals);

        return isMatched;
    }

    /**
     * Recovers the address that was used to sign the given data.
     * This function automatically creates a message hash by appending a Klaytn sign prefix to the message.
     * @param message A plain message when using signed.
     * @param signatureData The signature values in KlaySignatureData
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, SignatureData signatureData) throws SignatureException {
        return recover(message, signatureData, false);
    }


    /**
     * Recovers the address that was used to sign the given data.
     * @param message A plain message or hashed message.
     * @param signatureData The signature values in KlaySignatureData
     * @param isPrefixed If true, the message param already hashed by appending a Klaytn sign prefix to the message.
     * @return String
     * @throws SignatureException It throws when recover operation has failed.
     */
    public static String recover(String message, SignatureData signatureData, boolean isPrefixed) throws SignatureException {
        String messageHash = message;
        if(!isPrefixed) {
            messageHash = Utils.hashMessage(message);
        }

        byte[] r = Numeric.hexStringToByteArray(signatureData.getR());
        byte[] s = Numeric.hexStringToByteArray(signatureData.getS());

        if(r == null || r.length != 32) {
            throw new IllegalArgumentException("r must be 32 bytes");
        }
        if(s == null || s.length != 32) {
            throw new IllegalArgumentException("s must be 32 bytes");
        }

        int header = Numeric.toBigInt(signatureData.getV()).intValue() & 0xFF;
        // The header byte: 0x1B = first key with even y, 0x1C = first key with odd y,
        //                  0x1D = second key with even y, 0x1E = second key with odd y
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }

        ECDSASignature sig = new ECDSASignature(
                new BigInteger(1, r),
                new BigInteger(1, s));

        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(recId, sig, Numeric.hexStringToByteArray(messageHash));
        if (key == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return Numeric.prependHexPrefix(Keys.getAddress(key));
    }
}
