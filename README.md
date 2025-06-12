# myBlockchain - Educational Blockchain Implementation in Java

A comprehensive educational blockchain implementation in Java that demonstrates fundamental blockchain concepts including cryptographic hashing, proof-of-work mining, digital wallets, transactions, and chain validation. This project serves as a practical introduction to blockchain technology and cryptocurrency mechanics.

## 📋 Table of Contents

- [Blockchain Fundamentals](#-blockchain-fundamentals)
- [Core Components](#-core-components)
- [Advanced Concepts](#-advanced-concepts)
- [Building & Running](#-building--running)
- [Testing](#-testing)
- [Project Structure](#-project-structure)
- [Tutorial Reference](#-tutorial-reference)
- [License](#-license)

## 🔗 Blockchain Fundamentals

### What is a Blockchain?
A **blockchain** is a distributed, immutable ledger that maintains a continuously growing list of records (blocks) linked and secured using cryptography. Think of it as a digital ledger book where every page (block) is connected to the previous page, and once written, the content cannot be changed without detection.

### 🧱 Block - The Building Block
Each block in a blockchain contains:
- **Hash**: A unique digital fingerprint identifying the block
- **Previous Hash**: Links to the previous block, creating the chain
- **Timestamp**: When the block was created
- **Transactions**: The actual data (money transfers, contracts, etc.)
- **Nonce**: A number used in the mining process
- **Merkle Root**: A single hash representing all transactions in the block

```java
public class Block 
{
    public String hash;                      // Current block's unique identifier
    public String previousHash;              // Previous block's hash (creates the chain)
    private long timeStamp;                  // When this block was created
    private int nonce;                       // Number used in mining
    public String merkleRoot;                // Root of transaction tree
    public ArrayList<Transaction> transactions; // All transactions in this block
}
```

### 👛 Wallet - Your Digital Identity
A **wallet** is your digital identity on the blockchain:
- **Public Key**: Your "address" - like a bank account number that others can see
- **Private Key**: Your "signature" - secret key that proves you own the wallet
- **Balance**: Calculated from all unspent transaction outputs (UTXOs) you own

Think of it like having a transparent safe (public key) that everyone can see, but only you have the combination (private key) to open it.

### 💸 Transaction - Moving Value
A **transaction** transfers value from one wallet to another:
- **Sender**: Who is sending the funds (identified by public key)
- **Receiver**: Who is receiving the funds (identified by public key)
- **Amount**: How much is being transferred
- **Digital Signature**: Mathematical proof that the sender authorized this transaction
- **Inputs**: References to previous transactions that gave you these funds
- **Outputs**: New allocations of funds (to receiver and change back to sender)

### 🔐 Hashing - Digital Fingerprints
**Hashing** creates a unique digital fingerprint for any piece of data:
- **SHA-256**: The algorithm we use (creates 256-bit hashes)
- **Deterministic**: Same input always produces same hash
- **Avalanche Effect**: Tiny change in input creates completely different hash
- **One-way**: Easy to compute hash from data, impossible to reverse

Example: `"Hello"` → `2cf24dba4f21d4288094...` (64 character hash)

## 🔧 Core Components

### Block Structure
The blockchain consists of linked blocks, each containing:
1. **Header Information**: Hash, previous hash, timestamp, nonce
2. **Transaction Data**: All transactions included in this block
3. **Merkle Root**: Single hash representing all transactions

### Wallet System
Wallets manage your blockchain identity:
- Generate cryptographic key pairs (public/private keys)
- Sign transactions to prove ownership
- Calculate balance from unspent transaction outputs
- Create new transactions to send funds

### Transaction Processing
The UTXO (Unspent Transaction Output) model ensures accurate accounting:
1. **Check Balance**: Verify sender has enough unspent outputs
2. **Create Inputs**: Reference previous transaction outputs to spend
3. **Create Outputs**: Allocate funds to receiver and change to sender
4. **Sign Transaction**: Prove ownership with digital signature
5. **Validate**: Verify all inputs exist and signature is valid

## ⚡ Advanced Concepts

### ⛏️ Mining - Securing the Network
**Mining** is the process of adding new blocks to the blockchain using **Proof-of-Work**:

1. **Collect Transactions**: Gather pending transactions into a block
2. **Set Target**: Define difficulty (number of leading zeros required in hash)
3. **Nonce Iteration**: Try different nonce values repeatedly
4. **Hash Calculation**: Compute block hash with current nonce
5. **Difficulty Check**: If hash meets target, block is valid
6. **Add to Chain**: Accept block and broadcast to network

**Why Mining Matters:**
- **Security**: Makes tampering computationally expensive
- **Consensus**: Ensures all participants agree on transaction history
- **Decentralization**: No central authority needed to validate transactions

Example: Mining with difficulty 3 requires hash starting with "000"
```
Nonce: 1234 → Hash: 0a5c3f... (invalid, doesn't start with 000)
Nonce: 1235 → Hash: 1b2a8e... (invalid, doesn't start with 000)
Nonce: 1236 → Hash: 000abc... (valid! Block is mined)
```

### 🌳 Merkle Trees - Efficient Verification
**Merkle Trees** provide efficient and secure verification of large amounts of data:

Structure:
```
        Root Hash (Merkle Root)
       /                    \
   Hash(1,2)              Hash(3,4)
   /      \               /      \
Hash(1) Hash(2)      Hash(3)  Hash(4)
  |       |           |       |
 Tx1     Tx2         Tx3     Tx4
```

**Benefits:**
- **Efficiency**: Verify any transaction with only log(n) hashes
- **Integrity**: Any change in transactions changes the root hash
- **Scalability**: Blocks can contain millions of transactions
- **Security**: Tamper detection without downloading entire block

### 🔐 Cryptographic Security
Our implementation uses industry-standard cryptography:

**ECDSA (Elliptic Curve Digital Signature Algorithm):**
- Creates public/private key pairs
- Signs transactions with private key
- Verifies signatures with public key
- Provides mathematical proof of ownership

**SHA-256 Hashing:**
- Creates unique fingerprints for all data
- Links blocks together immutably
- Used in mining proof-of-work
- Ensures data integrity

### 🔄 UTXO Model vs Account Model
Our blockchain uses the **UTXO (Unspent Transaction Output)** model:

**UTXO Model (Bitcoin-style):**
- Tracks individual "coins" or outputs
- Each transaction consumes old UTXOs and creates new ones
- More complex but offers better privacy and parallelization
- Example: You have UTXOs worth 3, 5, and 2 coins (total balance: 10)

**Account Model (Ethereum-style):**
- Maintains account balances directly
- Simpler to understand and implement
- Example: Your account balance is 10 coins

## 🚀 Building & Running

### Prerequisites
- **Java Development Kit (JDK) 17** or higher
- **Apache Maven 3.6+** for build management

### Building with Maven
```bash
# Clean and compile the project
mvn clean compile

# Run tests
mvn test

# Create executable JAR with all dependencies
mvn clean package
```

### Running the Application

**Option 1: Pre-built JAR (Recommended)**
Download the runnable JAR file from GitHub releases:
```bash
java -jar myBlockchain.jar
```

**Option 2: Build and Run with Maven**
```bash
# Build the project
mvn clean package

# Run the executable JAR
java -jar target/myBlockchain.jar
```

**Option 3: Maven Direct Execution**
```bash
mvn exec:java -Dexec.mainClass="myBlockchain.myBlockchain"
```

### Expected Output
The application demonstrates a complete blockchain workflow:
```
Creating and Mining Genesis block...
Block Mined!!! : 000abc123def456...

WalletA's balance is: 100.0
WalletA is Attempting to send funds (40) to WalletB...
Transaction Successfully added to Block
Block Mined!!! : 000def456abc789...

WalletA's balance is: 60.0
WalletB's balance is: 40.0

WalletA Attempting to send more funds (1000) than it has...
Transaction failed: Insufficient funds

WalletB is Attempting to send funds (20) to WalletA...
Transaction Successfully added to Block

WalletA's balance is: 80.0
WalletB's balance is: 20.0

Blockchain is valid
```

## 🧪 Testing

### Running Tests
```bash
# Execute all tests
mvn test

# Run specific test class
mvn test -Dtest=StringUtilTest

# Run tests with verbose output
mvn test -Dtest=BlockTest

# Generate test reports
mvn surefire-report:report
```

### Test Coverage
Our test suite covers:
- **Block Functionality**: Hash calculation, mining, and validation
- **Cryptographic Utilities**: SHA-256 hashing and ECDSA signatures
- **Transaction Processing**: Input/output validation and signature verification
- **Wallet Operations**: Key generation, balance calculation, and fund transfers
- **Blockchain Validation**: Complete chain integrity checks

### Test Files
- **`BlockTest.java`**: Tests block creation, mining, and hash validation
- **`StringUtilTest.java`**: Tests cryptographic utility functions

## 📁 Project Structure

```
myBlockchain/
├── 📁 src/
│   ├── 📁 main/
│   │   └── 📁 java/
│   │       └── 📁 myBlockchain/
│   │           ├── 📄 myBlockchain.java           # Main application entry point
│   │           ├── 📁 Blockchain/
│   │           │   └── 📄 Block.java              # Block implementation with mining
│   │           ├── 📁 Transactions/
│   │           │   ├── 📄 Transaction.java        # Transaction processing
│   │           │   ├── 📄 TransactionInput.java   # Transaction input handling
│   │           │   ├── 📄 TransactionOutput.java  # Transaction output handling
│   │           │   └── 📄 Wallet.java             # Digital wallet implementation
│   │           └── 📁 Util/
│   │               └── 📄 StringUtil.java         # Cryptographic utilities
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 myBlockchain/
│               ├── 📄 BlockTest.java              # Block functionality tests
│               └── 📄 StringUtilTest.java         # Utility function tests
├── 📁 target/                                     # Compiled artifacts and JAR files
│   ├── 📄 myBlockchain.jar                        # Executable JAR with dependencies
│   └── 📄 myBlockchain-THIN.jar                   # Lightweight JAR without dependencies
├── 📄 pom.xml                                     # Maven build configuration
├── 📄 dependency-reduced-pom.xml                  # Optimized POM for shaded JAR
└── 📄 README.md                                   # Project documentation
```

### Key Components Explained

**Main Application (`myBlockchain.java`)**
- Entry point of the application
- Orchestrates blockchain operations
- Demonstrates complete blockchain workflow
- Sets up cryptographic providers (BouncyCastle)

**Blockchain Package**
- `Block.java`: Implements individual blocks with mining capabilities
- Contains block hashing, validation, and proof-of-work mining

**Transactions Package**
- `Transaction.java`: Handles transaction creation, signing, and verification
- `TransactionInput.java`: Manages references to previous transaction outputs
- `TransactionOutput.java`: Represents spendable transaction outputs
- `Wallet.java`: Manages public/private keys and initiates transactions

**Util Package**
- `StringUtil.java`: Cryptographic utility functions (SHA-256, ECDSA)
- Helper methods for hash generation and signature verification

**Test Package**
- Comprehensive unit tests for all major components
- Validates cryptographic functions and blockchain operations

## 🔧 Configuration & Customization

### Adjustable Parameters (in `myBlockchain.java`)
```java
public static int difficulty = 3;              // Mining difficulty (1-6 recommended)
public static float minimumTransaction = 0.1f; // Minimum transaction amount
```

**Mining Difficulty Levels:**
- **1-2**: Very fast mining (seconds) - Good for testing
- **3-4**: Moderate mining (10-30 seconds) - Educational demonstration
- **5-6**: Slow mining (minutes) - Closer to real blockchain experience
- **7+**: Very slow mining (hours) - Not recommended for education

### Dependencies (from `pom.xml`)
- **Gson 2.10.1**: JSON serialization for blockchain data
- **BouncyCastle 1.70**: Cryptographic provider for ECDSA and SHA-256
- **JUnit Jupiter**: Testing framework for unit tests

## 🚨 Important Notes

### Security Considerations
- This is an **educational implementation** - not production-ready
- Private keys are stored in memory (real wallets use secure storage)
- No network layer (real blockchains are distributed)
- Simplified mining (real mining uses more sophisticated algorithms)

### Performance Notes
- Mining time increases exponentially with difficulty
- Large transactions may consume significant memory
- Test with small amounts and low difficulty for faster execution

### Educational Limitations
- No transaction fees implementation
- No scripting language (like Bitcoin Script)
- No consensus between multiple nodes
- Simplified UTXO management

## 📖 Tutorial Reference

This project is based on the comprehensive tutorial series:
**[Create simple Blockchain Java tutorial from scratch](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa)**

The tutorial covers:
- Blockchain fundamentals and implementation
- Cryptographic concepts and security
- Transaction processing and UTXO model
- Mining and proof-of-work consensus
- Digital signatures and wallet management

## 📄 License

This project is intended for educational purposes and learning blockchain concepts. Feel free to use, modify, and distribute for educational and non-commercial purposes.
