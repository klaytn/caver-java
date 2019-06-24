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
#### maven
```groovy
<dependency>
  <groupId>com.klaytn.caver</groupId>
  <artifactId>core</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
#### gradle
```groovy
compile 'com.klaytn.caver:core:1.0.0'
```
If you want to use Android dependency, just append -android at the end of version. (e.g. 1.0.0-android)

## Start a Client
If you want to run your own EN (Endpoint Node), see [EN Operation Guide](https://docs.klaytn.com/node/en) to set up.
Otherwise, you can use a Klaytn public EN (https://api.cypress.klaytn.net:8651/) to connect to the mainnet and another public EN (https://api.baobab.klaytn.net:8651) to connect to the Baobab testnet.

```java
Caver caver  = Caver.build("https://api.cypress.klaytn.net:8651/");
```

## Transactions
When you send transactions, `caver-java` provides easy-to-use wrapper classes. Here's an example of transferring value using `ValueTransfer` class:
```java
Caver caver = Caver.build(<endpoint>);
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.sendFunds(
            caver, credentials, <address>, 
            <value>, <valueUnit>, <gasLimit>)
            .send();
```
`<valueUnit>` means a unit of value that is used in Klaytn. It is defined as an enum type. Examples of possible values are as below.

```
PEB, KPEB, MPEB, GPEB, STON, UKLAY, MKLAY, KLAY, KKLAY, MKLAY, GKLAY
```

If `<valueUnit>` is not given as a parameter, default unit of `<value>` is `PEB`. You can use `Convert` object to easily convert a value to another unit like below.

```java
Convert.toPeb("1", KLAY).toBigInteger();  // 1000000000000000000
Convert.fromPeb("1000000000000000000", KLAY).toBigInteger();  // 1
```

### Fee Delegation
Klaytn provides [Fee Delegation](https://docs.klaytn.com/klaytn/design/transactions#fee-delegation) feature. Here's an example code.
When you are a sender:

```java
KlayCredentials sender = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
TxTypeFeeDelegatedValueTransferMemo tx = TxTypeFeeDelegatedValueTransferMemo.createTransaction(
            <nonce>, <gasPrice>, <gasLimit>, <toAddress>, 
            <value>, sender.getAddress(), <memo>
);
String senderRawTransaction = tx.sign(sender, <chainID>).getValueAsString();
```
After signing a transaction, the sender can get the signed transaction as a string (`senderRawTransaction`).
Then, the sender sends the transaction to the fee payer who will pay for the transaction fee instead.

When you are a fee payer:

```java
Caver caver = Caver.build(<endpoint>);
KlayCredentials feePayer = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).build();
feePayerManager.executeTransaction(senderRawTransaction);
```
After the fee payer gets the transaction from the sender, the fee payer can easily send the transaction using the `FeePayerManager` class. We will cover manager classes (`TransactionManager`, `FeePayerManger`) in more detail in the next section.
For more information about Klaytn transaction types, visit [Transactions](https://docs.klaytn.com/klaytn/design/transactions).

### Manager
There are manager classes (`TransactionManager`, `FeePayerManager`) that help to create a transaction object. Using the builder class, you can easily customize the attributes listed below.
- TransactionReceiptProcessor
- ErrorHandler
- GetNonceProcessor

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

If you want to update the key of the given account:

```java
Caver caver = Caver.build(<endpoint>);
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
AccountUpdateTransaction accountUpdateTransaction = AccountUpdateTransaction.create(
      credentials.getAddress(),
      <newAccountKey>,
      <gasLimit>
);
Account.sendUpdateTransaction(caver, credentials, accountUpdateTransaction).send();
```

## Manage Contracts using Java Smart Contract Wrappers
Caver supports auto-generation of smart contract wrapper code. Using the contract wrapper you can easily deploy and execute a smart contract.
Before generating a wrapper code, you need to compile the smart contract first (Note: This will only work if solidity compiler is installed in your computer).

```shell
$ solc <contract>.sol --bin --abi --optimize -o <output-dir>/
```
Then generate the wrapper code using caver-java’s [command-line tool](#Command-line Tool).
```shell
$ caver-java solidity generate -b <smart-contract>.bin -a <smart-contract>.abi -o <outputPath> -p <packagePath>
```
Above code will output `<smartContract>`.java.
After generating the wrapper code, you can deploy your smart contract:

```java
Caver caver = Caver.build(<endpoint>);
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
<smartContract> contract = <smartContract>.deploy(
      caver, credentials, <chainId>, <contractGasProvider>,
      <param1>, ..., <paramN>).send();
```
For example, if your smart contract is [ERC20Mock](https://github.com/OpenZeppelin/openzeppelin-solidity/blob/master/contracts/mocks/ERC20Mock.sol) and want to deploy the smart contract at Baobab testnet, you could do like this:

```java
Caver caver = Caver.build("https://api.baobab.klaytn.net:8651");
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
ERC20Mock erc20Mock = ERC20Mock.deploy(
		  caver, credentials, ChainId.BAOBAB_TESTNET, new DefaultGasProvider(), 
  		credentials.getAddress(), BigInteger.valueOf(100)).send();
```

After the smart contract has been deployed, you can load the smart contract as below:

```java
Caver caver = Caver.build(<endpoint>);
KlayCredentials credentials = KlayWalletUtils.loadCredentials(<password>, <walletfilePath>);
<smartContract> contract = <smartContract>.load(
    <deployedContractAddress>, caver, credentials, <chainId>, <contractGasProvider>
);
```
To transact with a smart contract:
```java
KlayTransactionReceipt.TransactionReceipt transactionReceipt = contract.<someMethod>(
      <param1>,
      ...).send();
```
To call a smart contract:
```java
<type> result = contract.<someMethod>(<param1>, ...).send();
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
KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.sendFunds().send(); // caver-java
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