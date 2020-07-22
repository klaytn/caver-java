package com.klaytn.caver.common;

import com.klaytn.caver.abi.ABI;
import com.klaytn.caver.contract.ContractIOType;
import com.klaytn.caver.contract.ContractMethod;
import org.junit.Test;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ABITest {

    public static class encodeFunctionSig {
        @Test
        public void encodeFunctionSignatureTest() {
            assertEquals("0xcdcd77c0", ABI.encodeFunctionSignature("baz(uint32,bool)"));
            assertEquals("0xfce353f6", ABI.encodeFunctionSignature("bar(bytes3[2])"));
            assertEquals("0xa5643bf2", ABI.encodeFunctionSignature("sam(bytes,bool,uint256[])"));
        }
    }

    public static class encodeEventSig {
        @Test
        public void encodeEventSignature() {
            assertEquals(
                    ("0x50cb9fe53daa9737b786ab3646f04d0150dc50ef4e75f59509d83667ad5adb20"),
                    ABI.encodeEventSignature("Deposit(address,hash256,uint256)"));

            assertEquals(
                    ("0x71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed"),
                    ABI.encodeEventSignature("Notify(uint256,uint256)"));

        }
    }


    public static class encodeFunctionCall {
        @Test
        public void encodeFunctionCallTest() {
            String expected = "0xcdcd77c000000000000000000000000000000000000000000000000000000000000000450000000000000000000000000000000000000000000000000000000000000001";
            String functionSig = "baz(uint32,bool)";
            List<Type> params = Arrays.asList(new Uint32(69), new Bool(true));

            assertEquals(expected, ABI.encodeFunctionCall(functionSig, params));
        }

        @Test
        public void encodeFunctionCallTest2() {
            String expected = "0xfce353f661626300000000000000000000000000000000000000000000000000000000006465660000000000000000000000000000000000000000000000000000000000";
            String functionSig = "bar(bytes3[2])";
            List<Type> params = Arrays.asList(new StaticArray2(Bytes3.class, new Bytes3("abc".getBytes()), new Bytes3("def".getBytes())));

            assertEquals(expected, ABI.encodeFunctionCall(functionSig, params));
        }

        @Test
        public void encodeFunctionCallTest3() {
            String expected = "0xa5643bf20000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000464617665000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000003";
            String functionSig = "sam(bytes,bool,uint256[])";
            List<Type> params = Arrays.asList(
                    new DynamicBytes("dave".getBytes()),
                    new Bool(true),
                    new DynamicArray<Uint>(Uint.class, new Uint(BigInteger.valueOf(1)), new Uint(BigInteger.valueOf(2)), new Uint(BigInteger.valueOf(3)))
            );

            assertEquals(expected, ABI.encodeFunctionCall(functionSig, params));
        }
    }

    public static class encodeParameter {
        @Test
        public void encodeBoolType() {
            assertEquals(
                    ABI.encodeParameter(new Bool(false)),
                    ("0000000000000000000000000000000000000000000000000000000000000000"));
            assertEquals(
                    ABI.encodeParameter(new Bool(true)),
                    ("0000000000000000000000000000000000000000000000000000000000000001"));
        }

        @Test
        public void encodeUintType() {
            Uint uint = new Uint(BigInteger.valueOf(255));
            assertEquals(
                    "00000000000000000000000000000000000000000000000000000000000000ff",
                    ABI.encodeParameter(uint));

            Uint uint2 = new Uint(BigInteger.valueOf(65535));
            assertEquals(
                    "000000000000000000000000000000000000000000000000000000000000ffff",
                    ABI.encodeParameter(uint2));

            Uint uint3 = new Uint(BigInteger.valueOf(65536));
            assertEquals(
                    "0000000000000000000000000000000000000000000000000000000000010000",
                    ABI.encodeParameter(uint3));

            Uint uint4 = new Uint(new BigInteger("4722366482869645213695"));
            assertEquals(
                    ABI.encodeParameter(uint4),
                    "0000000000000000000000000000000000000000000000ffffffffffffffffff");

            Uint uint5 = new Uint(BigInteger.ZERO);
            assertEquals(
                    ABI.encodeParameter(uint5),
                    "0000000000000000000000000000000000000000000000000000000000000000");
        }

        @Test
        public void encodeIntType() {
            Int int1 = new Int(BigInteger.ZERO);
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(int1));

            Int int2 = new Int(BigInteger.valueOf(127));
            assertEquals(
                    ("000000000000000000000000000000000000000000000000000000000000007f"),
                    ABI.encodeParameter(int2));

            Int int3 = new Int(BigInteger.valueOf(32767));
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000007fff"),
                    ABI.encodeParameter(int3));

            Int int4 = new Int(BigInteger.valueOf(2147483647));
            assertEquals(
                    ("000000000000000000000000000000000000000000000000000000007fffffff"),
                    ABI.encodeParameter(int4));
        }

        @Test
        public void encodeAddressType() {
            Address address = new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338");
            assertEquals(address.getTypeAsString(), ("address"));
            assertEquals(
                    ("000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"),
                    ABI.encodeParameter(address));
        }

        @Test
        public void encodeUtf8StringType() {
            Utf8String string = new Utf8String("Hello, world!");
            assertEquals(
                    ("000000000000000000000000000000000000000000000000000000000000000d"
                            + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"),
                    ABI.encodeParameter(string));
        }

        @Test
        public void encodeStaticBytesType() {
            Bytes staticBytes = new Bytes6(new byte[] {0, 1, 2, 3, 4, 5});
            assertEquals(
                    ("0001020304050000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(staticBytes));

            Bytes empty = new Bytes1(new byte[] {0});
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(empty));

            Bytes ones = new Bytes1(new byte[] {127});
            assertEquals(
                    ("7f00000000000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(ones));

            Bytes dave = new Bytes4("dave".getBytes());
            assertEquals(
                    ("6461766500000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(dave));
        }

        @Test
        public void encodeDynamicBytesType() {
            DynamicBytes dynamicBytes = new DynamicBytes(new byte[] {0, 1, 2, 3, 4, 5});
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000006"
                            + "0001020304050000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(dynamicBytes));

            DynamicBytes empty = new DynamicBytes(new byte[] {0});
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000001"
                            + "0000000000000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(empty));

            DynamicBytes dave = new DynamicBytes("dave".getBytes());
            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000004"
                            + "6461766500000000000000000000000000000000000000000000000000000000"),
                    ABI.encodeParameter(dave));

            DynamicBytes loremIpsum =
                    new DynamicBytes(
                            ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                                    + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
                                    + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
                                    + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                                    + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                                    + "sint occaecat cupidatat non proident, sunt in culpa qui officia "
                                    + "deserunt mollit anim id est laborum.")
                                    .getBytes());
            assertEquals(
                    ("00000000000000000000000000000000000000000000000000000000000001bd"
                            + "4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73"
                            + "656374657475722061646970697363696e6720656c69742c2073656420646f20"
                            + "656975736d6f642074656d706f7220696e6369646964756e74207574206c6162"
                            + "6f726520657420646f6c6f7265206d61676e6120616c697175612e2055742065"
                            + "6e696d206164206d696e696d2076656e69616d2c2071756973206e6f73747275"
                            + "6420657865726369746174696f6e20756c6c616d636f206c61626f726973206e"
                            + "69736920757420616c697175697020657820656120636f6d6d6f646f20636f6e"
                            + "7365717561742e2044756973206175746520697275726520646f6c6f7220696e"
                            + "20726570726568656e646572697420696e20766f6c7570746174652076656c69"
                            + "7420657373652063696c6c756d20646f6c6f726520657520667567696174206e"
                            + "756c6c612070617269617475722e204578636570746575722073696e74206f63"
                            + "63616563617420637570696461746174206e6f6e2070726f6964656e742c2073"
                            + "756e7420696e2063756c706120717569206f666669636961206465736572756e"
                            + "74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e000000"),
                    ABI.encodeParameter(loremIpsum));
        }

        @Test
        public void encodeDynamicArrayTest() {
            DynamicArray<Uint> array =
                    new DynamicArray<>(
                            Uint.class,
                            new Uint(BigInteger.ONE),
                            new Uint(BigInteger.valueOf(2)),
                            new Uint(BigInteger.valueOf(3)));

            assertEquals(
                    ("0000000000000000000000000000000000000000000000000000000000000003"
                            + "0000000000000000000000000000000000000000000000000000000000000001"
                            + "0000000000000000000000000000000000000000000000000000000000000002"
                            + "0000000000000000000000000000000000000000000000000000000000000003"),
                    ABI.encodeParameter(array));
        }
    }



    public static class decodeParameterTest {
        @Test
        public void decodeBoolType() throws ClassNotFoundException {
            assertEquals(
                    ABI.decodeParameter("bool", "0000000000000000000000000000000000000000000000000000000000000000"),
                    (new Bool(false)));
            assertEquals(
                    ABI.decodeParameter("bool", "0000000000000000000000000000000000000000000000000000000000000001"),
                    (new Bool(true)));
        }

        @Test
        public void decodeUintType() throws ClassNotFoundException {
            Uint uint = new Uint256(BigInteger.valueOf(255));
            assertEquals(
                    ABI.decodeParameter("uint256", "00000000000000000000000000000000000000000000000000000000000000ff"),
                    uint);


            Uint uint2 = new Uint256(BigInteger.valueOf(65535));
            assertEquals(
                    ABI.decodeParameter("uint256", "000000000000000000000000000000000000000000000000000000000000ffff"),
                    uint2
            );

            Uint uint3 = new Uint256(BigInteger.valueOf(65536));
            assertEquals(
                    ABI.decodeParameter("uint256", "0000000000000000000000000000000000000000000000000000000000010000"),
                    uint3);

            Uint uint4 = new Uint256(new BigInteger("4722366482869645213695"));
            assertEquals(
                    ABI.decodeParameter("uint256", "0000000000000000000000000000000000000000000000ffffffffffffffffff"),
                    uint4);

            Uint uint5 = new Uint256(BigInteger.ZERO);
            assertEquals(
                    ABI.decodeParameter("uint256", "0000000000000000000000000000000000000000000000000000000000000000"),
                    uint5
                    );
        }

        @Test
        public void decodeIntType() throws ClassNotFoundException {
            Int int1 = new Int256(BigInteger.ZERO);
            assertEquals(
                    ABI.decodeParameter("int256", "0000000000000000000000000000000000000000000000000000000000000000"),
                    int1);

            Int int2 = new Int256(BigInteger.valueOf(127));
            assertEquals(
                    ABI.decodeParameter("int256", "000000000000000000000000000000000000000000000000000000000000007f"),
                    int2);

            Int int3 = new Int256(BigInteger.valueOf(32767));
            assertEquals(
                    ABI.decodeParameter("int256", "0000000000000000000000000000000000000000000000000000000000007fff"),
                    int3);

            Int int4 = new Int256(BigInteger.valueOf(2147483647));
            assertEquals(
                    ABI.decodeParameter("int256","000000000000000000000000000000000000000000000000000000007fffffff"),
                    int4);
        }

        @Test
        public void decodeAddressType() throws ClassNotFoundException {
            Address address = new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338");
            assertEquals(
                    ABI.decodeParameter("address","000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"),
                    address);
        }

        @Test
        public void decodeUtf8StringType() throws ClassNotFoundException {
            Utf8String string = new Utf8String("Hello, world!");
            assertEquals(
                    ABI.decodeParameter("string",
                            "0000000000000000000000000000000000000000000000000000000000000020"
                            + "000000000000000000000000000000000000000000000000000000000000000d"
                            + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"),
                    string);
        }

        @Test
        public void decodeStaticBytesType() throws ClassNotFoundException {
            Bytes staticBytes = new Bytes6(new byte[] {0, 1, 2, 3, 4, 5});
            assertEquals(
                    ABI.decodeParameter("bytes6", "0001020304050000000000000000000000000000000000000000000000000000"),
                    staticBytes);

            Bytes empty = new Bytes1(new byte[] {0});
            assertEquals(
                    ABI.decodeParameter("bytes1", "0000000000000000000000000000000000000000000000000000000000000000"),
                    empty);

            Bytes ones = new Bytes1(new byte[] {127});
            assertEquals(
                    ABI.decodeParameter("bytes1", "7f00000000000000000000000000000000000000000000000000000000000000"),
                    ones);

            Bytes dave = new Bytes4("dave".getBytes());
            assertEquals(
                    ABI.decodeParameter("bytes4", "6461766500000000000000000000000000000000000000000000000000000000"),
                    dave);
        }

        @Test
        public void decodeDynamicBytesType() throws ClassNotFoundException {
            DynamicBytes dynamicBytes = new DynamicBytes(new byte[] {0, 1, 2, 3, 4, 5});
            assertEquals(
                    ABI.decodeParameter("bytes",
                            "0000000000000000000000000000000000000000000000000000000000000020"
                                    + "0000000000000000000000000000000000000000000000000000000000000006"
                                    + "0001020304050000000000000000000000000000000000000000000000000000"),
                    dynamicBytes);

            DynamicBytes empty = new DynamicBytes(new byte[] {0});
            assertEquals(
                    ABI.decodeParameter("bytes",
                            "0000000000000000000000000000000000000000000000000000000000000020"
                            +"0000000000000000000000000000000000000000000000000000000000000001"
                            + "0000000000000000000000000000000000000000000000000000000000000000"),
                    empty);

            DynamicBytes dave = new DynamicBytes("dave".getBytes());
            assertEquals(
                    ABI.decodeParameter("bytes",
                            "0000000000000000000000000000000000000000000000000000000000000020"
                            +"0000000000000000000000000000000000000000000000000000000000000004"
                            + "6461766500000000000000000000000000000000000000000000000000000000"),
                    dave);

            DynamicBytes loremIpsum =
                    new DynamicBytes(
                            ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                                    + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
                                    + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
                                    + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                                    + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                                    + "sint occaecat cupidatat non proident, sunt in culpa qui officia "
                                    + "deserunt mollit anim id est laborum.")
                                    .getBytes());
            assertEquals(
                    ABI.decodeParameter("bytes",
                            "0000000000000000000000000000000000000000000000000000000000000020"
                            +"00000000000000000000000000000000000000000000000000000000000001bd"
                            + "4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73"
                            + "656374657475722061646970697363696e6720656c69742c2073656420646f20"
                            + "656975736d6f642074656d706f7220696e6369646964756e74207574206c6162"
                            + "6f726520657420646f6c6f7265206d61676e6120616c697175612e2055742065"
                            + "6e696d206164206d696e696d2076656e69616d2c2071756973206e6f73747275"
                            + "6420657865726369746174696f6e20756c6c616d636f206c61626f726973206e"
                            + "69736920757420616c697175697020657820656120636f6d6d6f646f20636f6e"
                            + "7365717561742e2044756973206175746520697275726520646f6c6f7220696e"
                            + "20726570726568656e646572697420696e20766f6c7570746174652076656c69"
                            + "7420657373652063696c6c756d20646f6c6f726520657520667567696174206e"
                            + "756c6c612070617269617475722e204578636570746575722073696e74206f63"
                            + "63616563617420637570696461746174206e6f6e2070726f6964656e742c2073"
                            + "756e7420696e2063756c706120717569206f666669636961206465736572756e"
                            + "74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e000000"),
                    loremIpsum);
        }

//        @Test
//        public void decodeDynamicArrayTest() throws ClassNotFoundException {
//            DynamicArray<Uint256> array =
//                    new DynamicArray<>(
//                            Uint256.class,
//                            new Uint256(BigInteger.ONE),
//                            new Uint256(BigInteger.valueOf(2)),
//                            new Uint256(BigInteger.valueOf(3)));
//
////            Class web3TypeClass = Class.forName("org.web3j.abi.datatypes.DynamicArray");
//
//            assertEquals(
//                    ABI.decodeParameter("uint256[]","0000000000000000000000000000000000000000000000000000000000000003"
//                            + "0000000000000000000000000000000000000000000000000000000000000001"
//                            + "0000000000000000000000000000000000000000000000000000000000000002"
//                            + "0000000000000000000000000000000000000000000000000000000000000003"),
//                    array);
//        }

//        @Test
//        public void test() throws ClassNotFoundException {
//            String className = ContractIOType.buildTypeName("uint256");
//            DynamicArray dynamicArray = new DynamicArray(Class.forName(className), Collections.emptyList());
//
//            DynamicArray<Uint256> dynamicArray = new DynamicArray(Class.forName(className));
//
//            String encoded = "0000000000000000000000000000000000000000000000000000000000000003"
//                    + "0000000000000000000000000000000000000000000000000000000000000001"
//                    + "0000000000000000000000000000000000000000000000000000000000000002"
//                    + "0000000000000000000000000000000000000000000000000000000000000003";
//
//            TypeReference reference = TypeReference.create(dynamicArray.getClass());
////            List<TypeReference<Type>> list = Arrays.asList(TypeReference.create(DynamicArray<Uint256>>));
//            List list = Arrays.<TypeReference<?>>asList(
//                    new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint>() {});
//
//
//
//
//            FunctionReturnDecoder.decode(encoded, list);
//        }
    }

}
