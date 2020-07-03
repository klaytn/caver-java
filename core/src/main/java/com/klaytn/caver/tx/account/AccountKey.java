/*
 * Copyright 2019 The caver-java Authors
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

package com.klaytn.caver.tx.account;

/**
 * @deprecated This interface replaced by {@link com.klaytn.caver.account.IAccountKey}
 */
@Deprecated
public interface AccountKey {
    Type getType();

    byte[] toRlp();

    enum Type {
        NIL((byte)0x0, AccountKeyNil.class),
        LEGACY((byte)0x01, AccountKeyLegacy.class),
        PUBLIC((byte)0x02, AccountKeyPublic.class),
        FAIL((byte)0x03, AccountKeyFail.class),
        MULTISIG((byte)0x04, AccountKeyWeightedMultiSig.class),
        ROLEBASED((byte)0x05, AccountKeyRoleBased.class);

        private byte value;
        private Class clazz;

        Type(byte value, Class clazz) {
            this.value = value;
            this.clazz = clazz;
        }

        public byte getValue() {
            return value;
        }

        public Class getClazz() {
            return clazz;
        }

        public static AccountKey.Type findByValue(byte value){
            for(AccountKey.Type v : values()){
                if( v.getValue() == value ){
                    return v;
                }
            }
            return PUBLIC;
        }
    }
}
