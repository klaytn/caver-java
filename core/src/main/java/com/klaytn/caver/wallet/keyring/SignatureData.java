package com.klaytn.caver.wallet.keyring;

import com.klaytn.caver.crypto.KlaySignatureData;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignatureData {
    private String v;
    private String r;
    private String s;

    public SignatureData(String v, String r, String s) {
        this.v = v;
        this.r = r;
        this.s = s;
    }

    public SignatureData(byte[] v, byte[] r, byte[] s) {
        this.v = Numeric.toHexString(v);
        this.r = Numeric.toHexString(r);
        this.s = Numeric.toHexString(s);
    }

    public static SignatureData getEmptySignature() {
        SignatureData emptySig = new SignatureData(
                "0x01",
                "0x",
                "0x"
        );

        return emptySig;
    }

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

    public void makeEIP155Signature(int chainId) {
        if(this.getV() == null || this.getV().isEmpty() || this.getV().equals("0x")) {
            throw new IllegalArgumentException("V value must be set.");
        }

        int v = Numeric.toBigInt(this.getV()).intValue();
        v = (v + chainId * 2) + 8;

        setV(Numeric.toHexStringWithPrefix(BigInteger.valueOf(v)));
    }


    public RlpList toRlpList() {
        byte[] v = Numeric.hexStringToByteArray(getV());
        byte[] r = Numeric.hexStringToByteArray(getV());
        byte[] s = Numeric.hexStringToByteArray(getV());

        return new RlpList(
                RlpString.create(Bytes.trimLeadingZeroes(v)),
                RlpString.create(Bytes.trimLeadingZeroes(r)),
                RlpString.create(Bytes.trimLeadingZeroes(s))
        );
    }

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
        return !s.equals(that.s);
    }

    public String getV() {
        return v;
    }

    public String getR() {
        return r;
    }

    public String getS() {
        return s;
    }

    public void setV(String v) {
        this.v = v;
    }

    public void setR(String r) {
        this.r = r;
    }

    public void setS(String s) {
        this.s = s;
    }
}
