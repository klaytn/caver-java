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

package com.klaytn.caver;

import com.klaytn.caver.model.TestGenerator;
import org.junit.Test;

public class IntegrationTest {

    TestGenerator testGenerator;

    @Test
    public void INT_LEGACY() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-LEGACY")
                .setSkip("005.json", "006.json", "010.json", "018.json")
                .build();
        testGenerator.generateAll();
    }

    @Test
    public void VALUE_TRANSFER() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-VT")
                .setSkip("006.json", "010.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void VALUE_TRANSFER_MEMO() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-VTM")
                .setSkip("006.json", "010.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void SMART_CONTRACT_DEPLOY() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-DEPL")
                .setSkip("006.json", "010.json", "011.json", "015.json", "016.json", "017.json", "018.json", "021.json")
                .build();
        testGenerator.generateAll();
    }

    @Test
    public void SMART_CONTRACT_EXECUTION() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-EXE")
                .setSkip("005.json", "008.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_CANCEL() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-CANCEL")
                .setSkip("005.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_ACCUP() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-ACCUP")
                .setSkip("046.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_FD() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-FD")
                .setSkip("007.json", "008.json", "009.json", "010.json", "011.json", "012.json", "034.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_FDR() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-FDR")
                .setSkip("007.json", "008.json", "009.json", "010.json", "011.json", "012.json").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_SIGVALI() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-SIGVALI").build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_API() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-API")
                .setSkip("006.json", "009.json", "032.json", "034.json", "044.json")
                .build();
        testGenerator.generateAll();
    }

    @Test
    public void INT_SOL() throws Exception {
        testGenerator = new TestGenerator.Builder("INT-SOL").build();
        testGenerator.generateAll();
    }
}