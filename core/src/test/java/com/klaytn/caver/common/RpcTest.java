package com.klaytn.caver.common;

import com.klaytn.caver.Caver;
import com.klaytn.caver.account.*;
import com.klaytn.caver.methods.response.AccountKeyResponse;
import com.klaytn.caver.methods.response.Bytes;
import com.klaytn.caver.rpc.Klay;
import org.junit.Test;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RpcTest {
    static Web3jService web3jService = new HttpService("http://13.125.127.149:8551");
    static Klay klay = new Klay(web3jService);

    public static class encodeAccountKeyTest {
        @Test
        public void withAccountKeyNil() throws IOException {
            String expected = "0x80";

            Request request = klay.encodeAccountKey(new AccountKeyNil());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyFail() throws IOException {
            String expected = "0x03c0";

            Request request = klay.encodeAccountKey(new AccountKeyFail());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyLegacy() throws IOException {
            String expected = "0x01c0";

            Request request = klay.encodeAccountKey(new AccountKeyLegacy());
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyPublic() throws IOException {
            String expected = "0x02a102dbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8";

            String x = "0xdbac81e8486d68eac4e6ef9db617f7fbd79a04a3b323c982a09cdfc61f0ae0e8";
            String y = "0x906d7170ba349c86879fb8006134cbf57bda9db9214a90b607b6b4ab57fc026e";
            AccountKeyPublic accountKeyPublic = AccountKeyPublic.fromXYPoint(x, y);

            Request request = klay.encodeAccountKey(accountKeyPublic);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyWeightedMultiSig() throws IOException {
            String expected = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyWeightedMultiSig multiSig = AccountKeyWeightedMultiSig.decode(expected);

            Request request = klay.encodeAccountKey(multiSig);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }

        @Test
        public void withAccountKeyRoleBased() throws IOException {
            String expected = "0x05f8c4a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a71b84e04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKeyRoleBased accountKeyRoleBased = AccountKeyRoleBased.decode(expected);

            Request request = klay.encodeAccountKey(accountKeyRoleBased);
            Bytes response = (Bytes) request.send();

            assertEquals(expected, response.getResult());
        }
    }

    public static class decodeAccountKeyTest {
        @Test
        public void withAccountKeyNil() throws IOException {
            String encodedStr = "0x80";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyNil);
        }

        @Test
        public void withAccountKeyFail() throws IOException {
            String encodedStr = "0x03c0";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyFail);
        }

        @Test
        public void withAccountKeyLegacy() throws IOException {
            String encodedStr = "0x01c0";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyLegacy);
        }

        @Test
        public void withAccountKeyPublic() throws IOException {
            String encodedStr = "0x02a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyPublic);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyWeightedMultiSig() throws IOException {
            String encodedStr = "0x04f84b02f848e301a102c10b598a1a3ba252acc21349d61c2fbd9bc8c15c50a5599f420cccc3291f9bf9e301a1021769a9196f523c419be50c26419ebbec34d3d6aa8b59da834212f13dbec9a9c1";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyWeightedMultiSig);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

        @Test
        public void withAccountKeyRoleBased() throws IOException {
            String encodedStr = "0x05f876a302a1036250dad4985bc22c8b9b84d1a05624c4daa0e83c8ae8fb35702d9024a8c14a718180b84e04f84b01f848e301a103e7615d056e770b3262e5b39a4823c3124989924ed4dcfab13f10b252701540d4e301a1036f21d60c16200d99e6777422470b3122b65850d5135a5a4b41344a5607a1446d";

            AccountKeyResponse accountKeyResponse = klay.decodeAccountKey(encodedStr).send();
            assertTrue(accountKeyResponse.getResult().getAccountKey() instanceof AccountKeyRoleBased);
            assertEquals(encodedStr, accountKeyResponse.getResult().getAccountKey().getRLPEncoding());
        }

    }

}
