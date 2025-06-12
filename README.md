# Simple Blockchain in Java

A basic blockchain implementation in Java that demonstrates core blockchain concepts including blocks, hashing, mining, and chain validation.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Blockchain Concepts](#blockchain-concepts)
- [Tutorial Reference](#tutorial-reference)

## 🔍 Overview

This project implements a simple blockchain from scratch to demonstrate fundamental blockchain concepts. The blockchain consists of blocks that are cryptographically linked together, with a proof-of-work mining system to ensure data integrity.

## ✨ Features

- **Block Creation**: Create blocks with data and previous block references
- **SHA-256 Hashing**: Secure cryptographic hashing for block integrity
- **Proof-of-Work Mining**: Mining system with configurable difficulty
- **Chain Validation**: Verify the entire blockchain's integrity
- **JSON Output**: Pretty-printed blockchain representation

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+** for building and dependency management
- **Git** (optional, for cloning)

## 🚀 Installation & Setup

1. **Clone or download the project:**
   ```bash
   git clone <repository-url>
   cd myBlockchain
   ```

2. **Verify Java installation:**
   ```bash
   java --version
   ```

3. **Verify Maven installation:**
   ```bash
   mvn --version
   ```

## 🔧 Building the Project

### Compile the project:
```bash
mvn compile
```

### Run tests:
```bash
mvn test
```

### Package into JAR (with dependencies):
```bash
mvn clean package
```

This creates two JAR files in the `target/` directory:
- `myBlockchain-1.0-SNAPSHOT.jar` - Standard JAR
- `dependency-reduced-pom.xml` - Fat JAR with all dependencies included

## ▶️ Running the Application

### Method 1: Using Maven
```bash
mvn exec:java -Dexec.mainClass="myBlockchain.myBlockchain"
```

### Method 2: Using the compiled JAR
```bash
java -jar target/myBlockchain-1.0-SNAPSHOT.jar
```

### Method 3: Direct Java execution
```bash
java -cp target/classes myBlockchain.myBlockchain
```

### Expected Output
The application will:
1. Create and mine three blocks
2. Validate the blockchain integrity
3. Display the complete blockchain in JSON format

## 🧪 Testing

Run the test suite to verify functionality:

```bash
# Run all tests
mvn test

# Run tests with detailed output
mvn test -Dtest=BlockTest

# Run specific test class
mvn test -Dtest=StringUtilTest
```

## 📚 Blockchain Concepts

### 🔗 Blockchain
A chain of blocks that contain a signature for the previous block and some data. Essentially a linked-list that contains special properties that makes the data structure **immutable**, **verifiable**, and **append-only**.

### 🧱 Block
Each block has the hash of the previous block which helps to calculate the current block's hash. This makes it so that calculating and comparing hashes allows us to see if a blockchain is invalid.

### ⛏️ Mining
To prevent tampering with the data, we use the **hashcash proof-of-work system**. The way it works is that miners do the proof of work by trying different variable values in the block until its hash starts with a certain number of zeros.

**If someone were to tamper with the data:**
- Their blockchain would be invalid
- They would not be able to create a longer blockchain
- Honest blockchains on the network have a time advantage

For someone with malicious purpose to succeed, they would have to catch up with a longer and valid chain, which is only possible if they have more computation speed than all other nodes in the network combined.

## 📖 Tutorial Reference

This project is based on the tutorial: [Create simple Blockchain Java tutorial from scratch](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa)

---

## 📁 Project Structure

```
myBlockchain/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── myBlockchain/
│   │           ├── Block.java          # Block class implementation
│   │           ├── myBlockchain.java   # Main application
│   │           └── StringUtil.java     # Utility functions
│   └── test/
│       └── java/
│           └── myBlockchain/
│               ├── BlockTest.java      # Block unit tests
│               └── StringUtilTest.java # Utility tests
├── target/                             # Compiled classes and JARs
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```


