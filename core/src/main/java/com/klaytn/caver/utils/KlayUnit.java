/*
 * Modifications copyright 2019 The caver-java Authors
 * Copyright 2016 Conor Svensson
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
 *
 * This file is derived from web3j/utils/src/main/java/org/web3j/utils/Convert.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.utils;

import java.math.BigDecimal;

/**
 * Klaytn unit conversion functions.
 */
public final class KlayUnit {
    private KlayUnit() { }

    public static BigDecimal fromPeb(double number, Unit unit) {return fromPeb(new BigDecimal(number), unit);}
    
    public static BigDecimal fromPeb(String number, Unit unit) {
        return fromPeb(new BigDecimal(number), unit);
    }

    public static BigDecimal fromPeb(BigDecimal number, Unit unit) {
        return number.divide(unit.getpebFactor());
    }

    public static BigDecimal toPeb(double number, Unit unit) {
        return toPeb(new BigDecimal(number), unit);
    }

    public static BigDecimal toPeb(String number, Unit unit) {
        return toPeb(new BigDecimal(number), unit);
    }

    public static BigDecimal toPeb(BigDecimal number, Unit unit) {
        return number.multiply(unit.getpebFactor());
    }

    public enum Unit {
        PEB("peb", 0),
        KPEB("kpeb", 3),
        MPEB("Mpeb", 6),
        GPEB("Gpeb", 9),
        STON("Ston", 9),
        uKLAY("uKLAY", 12),
        mKLAY("mKLAY", 15),
        KLAY("KLAY", 18),
        KKLAY("kKLAY", 21),
        MKLAY("MKLAY", 24),
        GKLAY("GKLAY", 27);

        private String name;
        private BigDecimal pebFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.pebFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getpebFactor() {
            return pebFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}
