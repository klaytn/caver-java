[![CircleCI](https://circleci.com/gh/klaytn/caver-java/tree/dev.svg?style=svg)](https://circleci.com/gh/klaytn/caver-java/tree/dev)

# caver-java: Caver Java Klaytn Dapp API

caver-java is a lightweight, high modular, convenient Java and Android library to interact with clients (nodes) on the Klaytn network:
This library is an interface which allows Java applications to easily communicate with [Klaytn](https://www.klaytn.com) network.

## Features
- Complete implementation of Klaytn’s JSON-RPC client API over HTTP and IPC
- Support of Klaytn transaction, account, and account key types
- Auto-generation of Java smart contract wrapper to deploy and execute a smart contract from native Java code
- Creation of a new wallet and managing Klaytn wallets
- Command line tools
- Android compatible

## Getting started

### Installation

#### add a Repository

Before install caver-java, You should add a jitpack repository for IPFS feature.

**maven**
```groovy
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**gradle**
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### add a dependency

**maven**
```groovy
<dependency>
    <groupId>com.klaytn.caver</groupId>
    <artifactId>core</artifactId>
    <version>X.X.X</version>
</dependency>
```

**gradle**
```groovy
compile 'com.klaytn.caver:core:X.X.X'
```
If you want to use Android dependency, just append -android at the end of version. (e.g. 1.5.4-android)

You can find latest caver-java version at [release page](https://github.com/klaytn/caver-java/releases).

## Start a Client
If you want to run your own EN (Endpoint Node), see [EN Operation Guide](https://docs.klaytn.com/node/en) to set up.
Otherwise, you can use a Klaytn public EN (https://api.cypress.klaytn.net:8651/) to connect to the mainnet and another public EN (https://api.baobab.klaytn.net:8651) to connect to the Baobab testnet.

```java
Caver caver = new Caver(Caver.BAOBAB_URL);
```

## Transactions
When you send transactions, `caver-java` provides easy-to-use wrapper classes. 

Here's an example of transferring KLAY using keystore.json and `ValueTransfer` class:
```java
Caver caver = new Caver(Caver.BAOBAB_URL);

//Read keystore json file.
File file = new File("./keystore.json");

//Decrypt keystore.
ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
KeyStore keyStore = objectMapper.readValue(file, KeyStore.class);
AbstractKeyring keyring = KeyringFactory.decrypt(keyStore, "password");

//Add to caver wallet.
caver.wallet.add(keyring);

BigInteger value = new BigInteger(Utils.convertToPeb(BigDecimal.ONE, "KLAY"));

//Create a value transfer transaction
ValueTransfer valueTransfer = new ValueTransfer.Builder()
        .setKlaytnCall(caver.rpc.getKlay())
        .setFrom(keyring.getAddress())
        .setTo("0x8084fed6b1847448c24692470fc3b2ed87f9eb47")
        .setValue(value)
        .setGas(BigInteger.valueOf(25000))
        .build();

//Sign to the transaction
valueTransfer.sign(keyring);

//Send a transaction to the klaytn blockchain platform (Klaytn)
Bytes32 result = caver.rpc.klay.sendRawTransaction(valueTransfer.getRawTransaction()).send();
if(result.hasError()) {
    throw new RuntimeException(result.getError().getMessage());
}

//Check transaction receipt.
TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
TransactionReceipt.TransactionReceiptData transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(result.getResult());
```

If you have address and private key(s) of keyring, you can make keyring directly through [KeyringFactory.create](https://docs.klaytn.com/bapp/sdk/caver-java/getting-started#creating-a-keyring).

`<valueUnit>` means a unit of value that is used in Klaytn. It is defined as an enum type. Examples of possible values are as below.

```
PEB, KPEB, MPEB, GPEB, STON, UKLAY, MKLAY, KLAY, KKLAY, MKLAY, GKLAY
```

If `<valueUnit>` is not given as a parameter, default unit of `<value>` is `PEB`. You can use `Utils.convertToPeb` or `Utils.convertFromPeb` to easily convert a value to another unit like below.

```java
Utils.convertToPeb("1", KLAY).toBigInteger();  // 1000000000000000000
Utils.convertFromPeb("1000000000000000000", KLAY).toBigInteger();  // 1
```

### Fee Delegation
Klaytn provides [Fee Delegation](https://docs.klaytn.com/klaytn/design/transactions#fee-delegation) feature. Here's an example code.
When you are a sender:

```java
Caver caver = new Caver(Caver.BAOBAB_URL);
SingleKeyring senderKeyring = KeyringFactory.createFromPrivateKey("0x{privateKey}");
caver.wallet.add(senderKeyring);

FeeDelegatedValueTransfer feeDelegatedValueTransfer = new FeeDelegatedValueTransfer.Builder()
        .setKlaytnCall(caver.rpc.klay)
        .setFrom(senderKeyring.getAddress())
        .setTo("0x176ff0344de49c04be577a3512b6991507647f72")
        .setValue(BigInteger.valueOf(1))
        .setGas(BigInteger.valueOf(30000))
        .build();

caver.wallet.sign(senderKeyring.getAddress(), feeDelegatedValueTransfer);
String rlpEncoded = feeDelegatedValueTransfer.getRLPEncoding();
System.out.println(rlpEncoded);
```
After signing a transaction, the sender can get the RLP-encoded string through `feeDelegatedValueTransfer.getRLPEncoding()`.
Then, the sender sends the transaction to the fee payer who will pay for the transaction fee instead.

When you are a fee payer:

```java
Caver caver = new Caver(Caver.BAOBAB_URL);

SingleKeyring feePayerKeyring = KeyringFactory.createFromPrivateKey("0x{privateKey}");
caver.wallet.add(feePayerKeyring);

String rlpEncoded = "0x{RLP-encoded string}"; // The result of feeDelegatedValueTransfer.getRLPEncoding() in above example
FeeDelegatedValueTransfer feeDelegatedValueTransfer = FeeDelegatedValueTransfer.decode(rlpEncoded);
feeDelegatedValueTransfer.setFeePayer(feePayerKeyring.getAddress());

caver.wallet.signAsFeePayer(feePayerKeyring.getAddress(), feeDelegatedValueTransfer);

TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

String rlpEncoded = feeDelegatedValueTransfer.getRLPEncoding();

try {
  // Send the transaction using `caver.rpc.klay.sendRawTransaction`.
  Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(rlpEncoding).send();
  if(sendResult.hasError()) {
    //do something to handle error

  }

  String txHash = sendResult.getResult();
  TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash);
} catch (IOException | TransactionException e) {
  // do something to handle exception.
}
```
After the fee payer gets the transaction from the sender, the fee payer can sign with `signAsFeePayer`. 
For more information about Klaytn transaction types, visit [Transactions](https://docs.klaytn.com/klaytn/design/transactions).


## Klaytn Accounts
An account in Klaytn is a data structure containing information about a person's balance or a smart contract. If you require further information about Klaytn accounts, you can refer to the [Accounts](https://docs.klaytn.com/klaytn/design/account).

### Account Key
An account key represents the key structure associated with an account.  Each account key has its own unique role. To get more details about the Klaytn account key, please read [Account Key](https://docs.klaytn.com/klaytn/design/account#account-key). These are 6 types of Account Keys in Klaytn:
- AccountKeyNil
- AccountKeyLegacy
- AccountKeyPublic
- AccountKeyFail
- AccountKeyWeightedMultiSig
- AccountKeyRoleBased

If you want to update the key of the given account, follow below steps :

1. Create new private key(s) to use
2. Create a keyring instance using the new private key(s) and the account address to update. 
After the AccountKey has been successfully updated in Klaytn, you can use the Keyring instance created here.
3. To update the AccountKey of Klaytn Account, create an Account instance using the toAccount function.
4. Create an AccountUpdate transaction (AccountUpdate/FeeDelegatedAccountUpdate/FeeDelegatedAccountUpdateWithRatio).
5. Sign the AccountUpdate transaction
6. Send signed transaction through `caver.rpc.klay.sendRawTransaction`

```java
Caver caver = new Caver(Caver.BAOBAB_URL);
SingleKeyring senderKeyring = KeyringFactory.createFromPrivateKey("0x{privateKey}");
caver.wallet.add(senderKeyring);

String newPrivateKey = KeyringFactory.generateSingleKey();
SingleKeyring newKeyring = KeyringFactory.createFromPrivateKey(newPrivateKey);

Account account = newKeyring.toAccount();

AccountUpdate accountUpdate = new AccountUpdate.Builder()
        .setKlaytnCall(caver.rpc.klay)
        .setFrom(senderKeyring.getAddress())
        .setAccount(account)
        .setGas(BigInteger.valueOf(50000))
        .build();

try {
    caver.wallet.sign(senderKeyring.getAddress(), accountUpdate);
    String rlpEncoded = accountUpdate.getRLPEncoding();

    Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(rlpEncoded).send();
    if(sendResult.hasError()) {
        //do something to handle error
    }

    String txHash = sendResult.getResult();

    TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);
    TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash);
} catch (IOException | TransactionException e) {
    // do something to handle exception.
}

senderKeyring = caver.wallet.updateKeyring(newKeyring);
```

## Manage Contracts using Java Smart Contract Wrappers

Caver supports `Contract` class to make it easy to interact with smart contract in Klaytn.
Before generating a wrapper code, you need to compile the smart contract first (Note: This will only work if solidity compiler is installed in your computer).

```shell
$ solc --abi --bin ./test.sol
```

You can create a contract instance as below using the result of compiling the smart contract: 

```java
Caver caver = new Caver(Caver.DEFAULT_URL);
try {
    Contract contract = new Contract(caver, ABI);
    contract.getMethods().forEach((methodName, contractMethod) -> {
        System.out.println("methodName : " + methodName + ", ContractMethod : " + contractMethod);
    });
    System.out.println("ContractAddress : " + contract.getContractAddress());
} catch (IOException e) {
    //handle exception..
}
```

If you want to deploy the smart contract at Baobab testnet, you could do like this:

```java
Caver caver = new Caver(Caver.DEFAULT_URL);
SingleKeyring deployer = KeyringFactory.createFromPrivateKey("0x{private key}");
caver.wallet.add(deployer);
try {
    Contract contract = new Contract(caver, ABI);
    ContractDeployParams params = new ContractDeployParams(byteCode, null);
    SendOptions sendOptions = new SendOptions();
    sendOptions.setFrom(deployer.getAddress());
    sendOptions.setGas(BigInteger.valueOf(40000))
    
    Contract newContract = contract.deploy(params, sendOptions);
    System.out.println("Contract address : " + newContract.getContractAddress());
} catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
    //handle exception..
}
```

After the smart contract has been deployed, you can load the smart contract as below:

```java
Caver caver = new Caver(Caver.DEFAULT_URL);
String contractAddress = "0x3466D49256b0982E1f240b64e097FF04f99Ed4b9";
try {
    Contract contract = new Contract(caver, ABI, contractAddress);
    contract.getMethods().forEach((methodName, contractMethod) -> {
        System.out.println("methodName : " + methodName + ", ContractMethod : " + contractMethod);
    });
    System.out.println("ContractAddress : " + contract.getContractAddress());
} catch (IOException e) {
    //handle exception..
}
```

To transact with a smart contract:
```java
Caver caver = new Caver(Caver.DEFAULT_URL);
SingleKeyring executor = KeyringFactory.createFromPrivateKey("0x{private key}");
caver.wallet.add(executor);
try {
    Contract contract = new Contract(caver, ABI, '0x{address in hex}');
    
    SendOptions sendOptions = new SendOptions();
    sendOptions.setFrom(executor.getAddress());
    sendOptions.setGas(BigInteger.valueOf(40000))
    TransactionReceipt.TransactionReceiptData receipt = contract.getMethod("set").send(Arrays.asList("testValue"), sendOptions);
    } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
    //handle exception..
}
```

## Filters
TBD

## Web3j Similarity
We made caver-java as similar as possible to web3j for easy usability.
```java
/* start a client */
Web3j web3 = Web3j.build(new HttpService(<endpoint>)); // Web3j
Caver caver = Caver.build(new HttpService(<endpoint>)); // caver-java

/* get nonce */
BigInteger nonce = web3j.ethGetTransactionCount(<address>, <blockParam>).send().getTransactionCount(); // Web3j
Quantity nonce = caver.klay().getTransactionCount(<address>, <blockParam>).send().getValue(); // caver-java

/* convert unit */
Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger(); // Web3j
Convert.toPeb("1.0", Convert.Unit.KLAY).toBigInteger(); // caver-java
 
/* generate wallet file */
WalletUtils.generateNewWalletFile(<password>, <filepath>); // Web3j
KlayWalletUtils.generateNewWalletFile(<address>, <password>, <filepath>); // caver-java

/* load credentials */
Credentials credentials = WalletUtils.loadCrendetials(<password>, <filepath>"); // Web3j
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <filepath>); // caver-java
                                                      
/* Value Transfer */
TransactionReceipt transactionReceipt = Transfer.sendFunds(...),send(); // Web3j
KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.create(...).sendFunds(...).send(); // caver-java
```

## Command-line Tool
A caver-java fat jar is distributed with open repository. The 'caver-java' allows you to generate Solidity smart contract function wrappers from the command line: 
- Generate Solidity smart contract function wrappers
Installation
```shell
$ brew tap klaytn/klaytn
$ brew install caver-java
```
After installation you can run command 'caver-java'
```shell
$ caver-java solidity generate -b <smart-contract>.bin -a <smart-contract>.abi -o <outputPath> -p <packagePath>
```

## Related projects 
**caver-js** for a javascript

## Build instructions
TBD

## Snapshot dependencies
TBD

## Thanks to
- The [web3j](https://github.com/web3j/web3j) project for the inspiration.  🙂 