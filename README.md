# 🔐 Java Blockchain Wallet System

This is a Java-based blockchain wallet simulation that enables users to create wallets, securely log in, view their balances, and request top-ups — all facilitated through blockchain transactions. It serves as an educational model to demonstrate the core mechanics behind cryptocurrencies and blockchain systems. The simulation incorporates key concepts such as UTXO (Unspent Transaction Output) tracking, ECDSA-based digital signatures, block creation, transaction validation, and Proof-of-Work mining — all implemented using Java's object-oriented programming paradigm.

---

## 🚀 Features

- ✅ Sign up and Login system
- 📦 Blockchain implementation with mining
- 🔐 Wallet generation with Public-Private key pairs
- 💸 Wallet top-up with blockchain transactions
- 💰 Balance viewing via UTXO scanning
- 🧱 Genesis block creation
- 📝 Real-time console interaction

---

## 📂 Project Structure

src/
│
├── Main.java # Entry point, UI logic (console-based)
├── Blockchain.java # Blockchain structure and block addition
├── Block.java # Individual block, mining, and transaction management
├── Transaction.java # Transaction logic, including validation and signature
├── TransactionInput.java # Input for UTXO-based transaction
├── TransactionOutput.java # Output (UTXO) representation
├── Wallet.java # Wallet creation, key pair management, and balance calc
├── StringUtil.java # Hashing, digital signature, and utility methods



---

## 🛠️ Technologies Used

- Java 17+
- Java Security (KeyPair, Signature, MessageDigest)
- OOP Principles
- SHA-256 Hashing
- ECDSA for digital signatures

---

## 🧱 How It Works

This Java-based project simulates a blockchain-powered wallet system. It implements the fundamental principles behind cryptocurrencies, including transaction validation, digital signatures, UTXOs, and block mining — all built using object-oriented Java. Below is a breakdown of the major components:

1. **🔐 Wallets & Key Pairs**
Each user owns a unique wallet backed by a cryptographic ECDSA key pair.
The public key acts as the user's address, while the private key is used for signing transactions.
Wallets can send, receive, and verify funds using blockchain records.

2. **💰 UTXO Management**
The system follows the UTXO (Unspent Transaction Output) model to track available funds.
Each transaction output not yet used becomes a UTXO for future transactions.
UTXOs ensure balance accuracy and prevent double-spending.

3. **📦 Blockchain & Blocks**
Blocks contain verified transactions and are chained using cryptographic hashes.
Each block stores a reference to the previous block's hash, ensuring immutability.
Mining difficulty and nonce values are used to simulate Proof-of-Work.

4. **🔄 Transactions**
Transactions represent the transfer of funds from one wallet to another.
They consume inputs (UTXOs) and generate new outputs for recipients.
Each transaction is digitally signed and validated before inclusion in a block.

5. **⚙️ Mining & Validation**
Mining involves solving a hash-based puzzle to add new blocks.
Only valid, signed transactions are included in mined blocks.
Once mined, blocks are appended to the chain and accepted as part of the ledger.

6. **🧑‍💼 User Authentication & Wallet Operations**
Users can sign up, log in, and view wallet balances in a secure environment.
A top-up system creates real blockchain transactions from a simulated coinbase wallet.
All wallet operations are transparently reflected in the blockchain.
---

## 🔧 How to Run


### Requirements
- Java 17+ installed
- Git installed (optional)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/karthik768990/Block-Chain-Java
   cd Block-Chain-Java

2.Compile and run the code:
  '''bash
    javac *.java
    java Main



 
 #### Pull requests are welcome! If you’d like to add features, fix bugs, or suggest improvements, feel free to fork and create a PR.
