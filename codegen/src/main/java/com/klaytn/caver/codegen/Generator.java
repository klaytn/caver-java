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
 * This file is derived from web3j/codegen/src/main/java/org/web3j/codegen/Generator.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.codegen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

/**
 * Common code generator methods.
 */
class Generator {
    void write(String packageName, TypeSpec typeSpec, String destinationDir) throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("    ")
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(new File(destinationDir));
    }

    static String buildWarning(Class cls) {
        return "Auto generated code.\n"
                + "<p><strong>Do not modifiy!</strong>\n";
    }
}
