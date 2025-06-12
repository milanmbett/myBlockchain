
package myBlockchain;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Security;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import myBlockchain.Blockchain.Block;
import myBlockchain.Transactions.Transaction;
import myBlockchain.Transactions.Wallet;

public class BlockTest 
{
    private Block testBlock;
    private static Wallet walletA;
    private static Wallet walletB;
    
    @BeforeAll
    public static void setupClass() {
        // Add BouncyCastle provider for cryptographic operations
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        // Create test wallets
        walletA = new Wallet();
        walletB = new Wallet();
    }
    
    @BeforeEach
    public void setup() {
        testBlock = new Block("0");
    }
    
    /*
     * Testing if Block constructor creates
     * a valid block with proper initialization
     */
    @Test
    public void testBlockCreation() {
        assertNotNull(testBlock, "Block should not be null");
        assertNotNull(testBlock.hash, "Block hash should not be null");
        assertNotNull(testBlock.previousHash, "Previous hash should not be null");
        assertEquals("0", testBlock.previousHash, "Previous hash should match constructor parameter");
        assertNotNull(testBlock.transactions, "Transactions list should not be null");
        assertTrue(testBlock.transactions.isEmpty(), "New block should have empty transactions list");
    }
    
    /*
     * Testing if calculateHash() returns
     * a non-null, non-empty hash string
     */
    @Test
    public void testCalculateHashNotEmpty() {
        String hash = testBlock.calculateHash();
        
        assertNotNull(hash, "Calculated hash should not be null");
        assertFalse(hash.isEmpty(), "Calculated hash should not be empty");
        assertEquals(64, hash.length(), "SHA-256 hash should be exactly 64 characters long");
    }
    
    /*
     * Testing if calculateHash() returns
     * consistent results for the same block
     */
    @Test
    public void testCalculateHashConsistency() {
        String hash1 = testBlock.calculateHash();
        String hash2 = testBlock.calculateHash();
        
        assertEquals(hash1, hash2, "Hash calculation should be consistent");
    }
    
    /*
     * Testing if calculateHash() returns
     * different hashes for different blocks
     */
    @Test
    public void testCalculateHashUniqueness() {
        Block block1 = new Block("0");
        Block block2 = new Block("1");
        
        // Wait a moment to ensure different timestamps
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String hash1 = block1.calculateHash();
        String hash2 = block2.calculateHash();
        
        assertNotEquals(hash1, hash2, "Different blocks should have different hashes");
    }
    
    /*
     * Testing if hash contains only
     * hexadecimal characters (0-9, a-f)
     */
    @Test
    public void testHashFormat() {
        String hash = testBlock.calculateHash();
        
        assertTrue(hash.matches("[0-9a-f]+"), "Hash should contain only hexadecimal characters (0-9, a-f)");
    }
    
    /*
     * Testing if addTransaction() properly
     * adds valid transactions to the block
     */
    @Test
    public void testAddValidTransaction() {
        // Create a simple transaction (for genesis block)
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 50f, null);
        transaction.transactionId = "test_transaction";
        
        boolean result = testBlock.addTransaction(transaction);
        
        assertTrue(result, "Adding valid transaction should return true");
        assertEquals(1, testBlock.transactions.size(), "Block should contain one transaction");
        assertEquals(transaction, testBlock.transactions.get(0), "Block should contain the added transaction");
    }
    
    /*
     * Testing if addTransaction() rejects
     * null transactions
     */
    @Test
    public void testAddNullTransaction() {
        boolean result = testBlock.addTransaction(null);
        
        assertFalse(result, "Adding null transaction should return false");
        assertTrue(testBlock.transactions.isEmpty(), "Block should remain empty after adding null transaction");
    }
    
    /*
     * Testing if mineBlock() changes
     * the block hash and sets merkle root
     */
    @Test
    public void testMineBlock() {
        // Add a transaction before mining
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 50f, null);
        transaction.transactionId = "test_transaction";
        testBlock.addTransaction(transaction);
        
        String originalHash = testBlock.hash;
        
        // Mine with low difficulty for faster testing
        testBlock.mineBlock(1);
        
        assertNotEquals(originalHash, testBlock.hash, "Mining should change the block hash");
        assertNotNull(testBlock.merkleRoot, "Mining should set merkle root");
        assertFalse(testBlock.merkleRoot.isEmpty(), "Merkle root should not be empty");
    }
    
    /*
     * Testing if mineBlock() produces
     * hash that meets difficulty requirement
     */
    @Test
    public void testMineBlockDifficulty() {
        // Add a transaction before mining
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 50f, null);
        transaction.transactionId = "test_transaction";
        testBlock.addTransaction(transaction);
        
        int difficulty = 2;
        testBlock.mineBlock(difficulty);
        
        String expectedPrefix = "00"; // difficulty of 2 means 2 leading zeros
        assertTrue(testBlock.hash.startsWith(expectedPrefix), 
                  "Mined hash should start with " + difficulty + " zeros");
    }
    
    /*
     * Testing if mining with higher difficulty
     * produces more leading zeros
     */
    @Test
    public void testMineBlockHigherDifficulty() {
        // Add a transaction before mining
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 50f, null);
        transaction.transactionId = "test_transaction";
        testBlock.addTransaction(transaction);
        
        int difficulty = 3;
        testBlock.mineBlock(difficulty);
        
        String expectedPrefix = "000"; // difficulty of 3 means 3 leading zeros
        assertTrue(testBlock.hash.startsWith(expectedPrefix), 
                  "Mined hash should start with " + difficulty + " zeros");
    }
    
    /*
     * Testing if blocks can be properly
     * chained together with previous hashes
     */
    @Test
    public void testBlockChaining() {
        Block block1 = new Block("0");
        
        // Add transaction and mine first block
        Transaction transaction1 = new Transaction(walletA.publicKey, walletB.publicKey, 50f, null);
        transaction1.transactionId = "transaction_1";
        block1.addTransaction(transaction1);
        block1.mineBlock(1);
        
        // Create second block with first block's hash
        Block block2 = new Block(block1.hash);
        
        assertEquals(block1.hash, block2.previousHash, "Second block should reference first block's hash");
        assertNotEquals(block1.hash, block2.hash, "Blocks should have different hashes");
    }
    
    /*
     * Testing if block with multiple transactions
     * calculates merkle root correctly
     */
    @Test
    public void testMultipleTransactions() {
        // Add multiple transactions
        Transaction transaction1 = new Transaction(walletA.publicKey, walletB.publicKey, 25f, null);
        transaction1.transactionId = "transaction_1";
        Transaction transaction2 = new Transaction(walletB.publicKey, walletA.publicKey, 15f, null);
        transaction2.transactionId = "transaction_2";
        
        testBlock.addTransaction(transaction1);
        testBlock.addTransaction(transaction2);
        
        assertEquals(2, testBlock.transactions.size(), "Block should contain two transactions");
        
        // Mine the block to calculate merkle root
        testBlock.mineBlock(1);
        
        assertNotNull(testBlock.merkleRoot, "Merkle root should be calculated");
        assertFalse(testBlock.merkleRoot.isEmpty(), "Merkle root should not be empty");
        assertEquals(64, testBlock.merkleRoot.length(), "Merkle root should be 64 characters (SHA-256 hash)");
    }
    
    /*
     * Testing if empty block (no transactions)
     * can still be mined successfully
     */
    @Test
    public void testMineEmptyBlock() {
        // Don't add any transactions
        assertTrue(testBlock.transactions.isEmpty(), "Block should be empty initially");
        
        // Mine empty block
        testBlock.mineBlock(1);
        
        assertTrue(testBlock.hash.startsWith("0"), "Empty block should still be mineable");
        assertEquals("", testBlock.merkleRoot, "Empty block should have empty merkle root");
    }
}