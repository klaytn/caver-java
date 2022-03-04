/*
 * Copyright 2020 The caver-java Authors
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

package com.klaytn.caver.wallet.keyring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.klaytn.caver.utils.Utils;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representing a ECDSA Signature data
 */
public class SignatureData {
    /**
     * Version byte
     */
    private String v;

    /**
     * ECDSA Signature data R
     */
    private String r;

    /**
     * ECDSA Signature data S
     */
    private String s;

    /**
     * Creates a SignatureData instance
     * @param v The version byte.
     * @param r The ECDSA Signature data R
     * @param s The ECDSA Signature data S
     */
    public SignatureData(String v, String r, String s) {
        this.v = Utils.addHexPrefix(v);
        this.r = Utils.addHexPrefix(r);
        this.s = Utils.addHexPrefix(s);
    }

    /**
     * Creates a SignatureData instance
     * @param v The version byte.
     * @param r The ECDSA Signature data R
     * @param s The ECDSA Signature data S
     */
    public SignatureData(byte[] v, byte[] r, byte[] s) {
        this.v = Numeric.toHexString(v);
        this.r = Numeric.toHexString(r);
        this.s = Numeric.toHexString(s);
    }

    /**
     * Get empty signature.
     * @return SignatureData
     */
    public static SignatureData getEmptySignature() {
        SignatureData emptySig = new SignatureData(
                "0x01",
                "0x",
                "0x"
        );

        return emptySig;
    }

    /**
     * Decodes a RLP encoded string contains signature list.
     * @param signatureRlpTypeList a RLP encoded string contains signature list.
     * @return List
     */
    public static List<SignatureData> decodeSignatures(List<RlpType> signatureRlpTypeList) {
        List<SignatureData> signatureDataList = new ArrayList<>();

        for (RlpType signature : signatureRlpTypeList) {
            List<RlpType> vrs = ((RlpList) signature).getValues();
            if (vrs.size() < 3) continue;
            byte[] v = ((RlpString) vrs.get(0)).getBytes();
            byte[] r = ((RlpString) vrs.get(1)).getBytes();
            byte[] s = ((RlpString) vrs.get(2)).getBytes();
            signatureDataList.add(new SignatureData(v, r, s));
        }

        return signatureDataList;
    }

    /**
     * Set "V" field according to EIP-155
     * @param chainId The chain id specific to the network.
     */
    public void makeEIP155Signature(int chainId) {
        if(this.getV() == null || this.getV().isEmpty() || this.getV().equals("0x")) {
            throw new IllegalArgumentException("V value must be set.");
        }

        int v = Numeric.toBigInt(this.getV()).intValue();
        v = (v + chainId * 2) + 8;

        setV(Numeric.toHexStringWithPrefix(BigInteger.valueOf(v)));
    }

    /**
     * Refines the array containing signatures
     *   - Removes duplicate signatures
     *   - Removes the default empty signature("0x01", "0x", "0x")
     *   - For an empty signature array, return an array containing the default empty signature("0x01", "0x", "0x")
     * @param signatureDataList The list of SignatureData
     * @return List&lt;String&gt;
     */
    public static List<SignatureData> refineSignature(List<SignatureData> signatureDataList) {
        SignatureData emptySig = SignatureData.getEmptySignature();

        List<SignatureData> refinedList = new ArrayList<>();

        for(SignatureData signData : signatureDataList) {
            if(! Utils.isEmptySig(signData)) {
                if(!refinedList.contains(signData)) {
                    refinedList.add(signData);
                }
            }
        }

        if(refinedList.size() == 0) {
            refinedList.add(emptySig);
        }

        return refinedList;
    }


    /**
     * Returns the RLP-encoded string of this signature.
     * @return RlpList
     */
    public RlpList toRlpList() {
        byte[] v = Numeric.hexStringToByteArray(getV());
        byte[] r = Numeric.hexStringToByteArray(getR());
        byte[] s = Numeric.hexStringToByteArray(getS());

        return new RlpList(
                RlpString.create(Bytes.trimLeadingZeroes(v)),
                RlpString.create(Bytes.trimLeadingZeroes(r)),
                RlpString.create(Bytes.trimLeadingZeroes(s))
        );
    }

    /**
     * Get a recover id from signatureData
     * @return int
     */
    @JsonIgnore
    public int getRecoverId() {
        int v = Numeric.toBigInt(this.getV()).intValue() & 0xFFFF;

        // The v value of EthereumAccessList or EthereumDynamicFee transaction has 0 or 1(parity value of y value in Signature).
        // It should be accepted 0 or 1 value
        if (v == 0 || v == 1) {
            return v;
        }

        if (v < 27) {
            throw new RuntimeException("v byte out of range: " + v);
        }

        // https://eips.ethereum.org/EIPS/eip-155
        // - v = parity value(Recovery Id) {0,1} + 27
        // - v =  parity value(Recovery Id) {0,1} + chainId * 2 + 35
        if(v < 35) {
            // v = parity value {0,1} + 27
            return v - 27;
        } 

        // v =  parity value(Recovery Id) {0,1} + chainId * 2 + 35
        return ((v - 35) % 2) == 0 ? 0 : 1;
    }

    /**
     * Get chain id from signatureData
     * @return BigInteger
     */
    @JsonIgnore
    public BigInteger getChainId() {
        int v = Numeric.toBigInt(this.getV()).intValue() & 0xFFFF;

        if (v < 35) {
            throw new RuntimeException("Cannot extract chainId from V value : " + v);
        }

        // v =  parity value {0,1} + chainId * 2 + 35
        int parity = this.getRecoverId();
        int chainId = (v - 35 - parity) >> 1;

        return BigInteger.valueOf(chainId);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o The reference object with which to compare.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignatureData that = (SignatureData) o;

        if (!v.equals(that.v)) {
            return false;
        }
        if (!r.equals(that.r)) {
            return false;
        }
        return s.equals(that.s);
    }

    /**
     * Returns a hash code value for the object.
     * @return integer
     */
    @Override
    public int hashCode() {
        int result = v.hashCode();
        result = 31 * result + r.hashCode();
        result = 31 * result + s.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the object.
     * @return String
     */
    @Override
    public String toString() {
        return "V : " + getV() + "\nR : " + getR() + "\nS : " + getS();
    }

    /**
     * Getter function for V.
     * @return String
     */
    @JsonIgnore
    public String getV() {
        return v;
    }

    /**
     * Getter function for R.
     * @return String
     */
    @JsonIgnore
    public String getR() {
        return r;
    }

    /**
     * Getter function for S.
     * @return String
     */
    @JsonIgnore
    public String getS() {
        return s;
    }

    @JsonProperty("v")
    public String getTrimZeroV() {
        String v = Utils.stripHexPrefix(getV());
        v =  v.replaceFirst("^0+(?!$)", "");
        return Utils.addHexPrefix(v);
    }

    @JsonProperty("r")
    public String getTrimZeroR() {
        String r = Utils.stripHexPrefix(getR());
        r =  r.replaceFirst("^0+(?!$)", "");
        return Utils.addHexPrefix(r);
    }

    @JsonProperty("s")
    public String getTrimZeroS() {
        String s = Utils.stripHexPrefix(getS());
        s =  s.replaceFirst("^0+(?!$)", "");
        return Utils.addHexPrefix(s);
    }

    /**
     * Setter function for V.
     * @param v Version byte.
     */
    public void setV(String v) {
        this.v = v;
    }

    /**
     * Setter function for R.
     * @param r ECDSA Signature data R.
     */
    public void setR(String r) {
        this.r = r;
    }

    /**
     * Setter function for S.
     * @param s ECDSA Signature data S.
     */
    public void setS(String s) {
        this.s = s;
    }
}
