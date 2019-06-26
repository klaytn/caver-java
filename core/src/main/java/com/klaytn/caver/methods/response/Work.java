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

package com.klaytn.caver.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * Array with the following properties:
 * <ol>
 * <li> 32-byte DATA - current block header pow-hash </li>
 * <li> 32-byte DATA - the seed hash used for the DAG </li>
 * <li> 32-byte DATA - the boundary condition ("target"), 2^256 / difficulty </li>
 * </ol>
 */
public class Work extends Response<List<String>> {
}
