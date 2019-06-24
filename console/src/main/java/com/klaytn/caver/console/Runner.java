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
 * This file is derived from web3j/console/src/main/java/org/web3j/console/Runner.java (2019/06/13).
 * Modified and improved for the caver-java development.
 */

package com.klaytn.caver.console;

import com.klaytn.caver.codegen.Console;
import com.klaytn.caver.codegen.SolidityFunctionWrapperGenerator;
import com.klaytn.caver.codegen.TruffleJsonFunctionWrapperGenerator;
import org.web3j.utils.Collection;

/**
 * Main entry point for running command line utilities.
 */
public class Runner {

    private static String USAGE = "Usage: caver-java solidity|truffle ...";

    private static String LOGO = "\n" + // generated at http://patorjk.com/software/taag
            " ________  ________  ___      ___ _______   ________                              \n" +
            "|\\   ____\\|\\   __  \\|\\  \\    /  /|\\  ___ \\ |\\   __  \\                   \n" +
            "\\ \\  \\___|\\ \\  \\|\\  \\ \\  \\  /  / | \\   __/|\\ \\  \\|\\  \\            \n" +
            " \\ \\  \\    \\ \\   __  \\ \\  \\/  / / \\ \\  \\_|/_\\ \\   _  _\\             \n" +
            "  \\ \\  \\____\\ \\  \\ \\  \\ \\    / /   \\ \\  \\_|\\ \\ \\  \\\\  \\|        \n" +
            "   \\ \\_______\\ \\__\\ \\__\\ \\__/ /     \\ \\_______\\ \\__\\\\ _\\           \n" +
            "    \\|_______|\\|__|\\|__|\\|__|/       \\|_______|\\|__|\\|__|                  \n" +
            "                                                                                  \n" +
            "                       ___  ________  ___      ___ ________                       \n" +
            "                      |\\  \\|\\   __  \\|\\  \\    /  /|\\   __  \\              \n" +
            " ____________         \\ \\  \\ \\  \\|\\  \\ \\  \\  /  / | \\  \\|\\  \\        \n" +
            "|\\____________\\     __ \\ \\  \\ \\   __  \\ \\  \\/  / / \\ \\   __  \\        \n" +
            "\\|____________|    |\\  \\\\_\\  \\ \\  \\ \\  \\ \\    / /   \\ \\  \\ \\  \\   \n" +
            "                   \\ \\________\\ \\__\\ \\__\\ \\__/ /     \\ \\__\\ \\__\\     \n" +
            "                    \\|________|\\|__|\\|__|\\|__|/       \\|__|\\|__|            \n" +
            "                                                                                  \n";

    public static void main(String[] args) throws Exception {
        System.out.println(LOGO);

        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY:
                    SolidityFunctionWrapperGenerator.main(Collection.tail(args));
                    break;
                case TruffleJsonFunctionWrapperGenerator.COMMAND_TRUFFLE:
                    TruffleJsonFunctionWrapperGenerator.run(Collection.tail(args));
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}
