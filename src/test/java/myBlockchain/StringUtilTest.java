package myBlockchain;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import myBlockchain.Transactions.Transaction;
import myBlockchain.Transactions.Wallet;
import myBlockchain.Util.StringUtil;

public class StringUtilTest
{
    private static PrivateKey testPrivateKey;
    private static PublicKey testPublicKey;
    
    @BeforeAll
    public static void setupClass() {
        // Add BouncyCastle provider for cryptographic operations
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        // Generate test key pair for cryptographic tests
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            keyGen.initialize(256);
            KeyPair keyPair = keyGen.generateKeyPair();
            testPrivateKey = keyPair.getPrivate();
            testPublicKey = keyPair.getPublic();
        } catch (Exception e) {
            fail("Failed to generate test keys: " + e.getMessage());
        }
    }

    // =================== SHA-256 TESTS ===================
    
    /* 
     * Testing if applySHA256() returns
     * a non-null, non-empty string
     */
    @Test
    public void testApplySHA256_NotNullOrEmpty()
    {
        String hash = StringUtil.applySHA256("test");
        
        assertNotNull(hash, "Hash should not be null");
        assertFalse(hash.isEmpty(), "Hash should not be empty");
    }
    
    /*
     * Testing if applySHA256() returns
     * a 64 character long string (SHA-256 standard)
     */
    @Test
    public void testApplySHA256_CorrectLength()
    {
        String hash = StringUtil.applySHA256("test");
        
        assertEquals(64, hash.length(), "SHA-256 hash should be exactly 64 characters long");
    }
    
    /*
     * Testing if applySHA256() returns
     * a string that only contains 
     * hexadecimal characters (0-9, a-f)
     */
    @Test
    public void testApplySHA256_HexadecimalFormat()
    {
        String hash = StringUtil.applySHA256("test");
        
        assertTrue(hash.matches("[0-9a-f]+"), "Hash should contain only hexadecimal characters (0-9, a-f)");
    }
    
    /*
     * Testing if applySHA256() returns
     * the same hash for identical inputs (deterministic)
     */
    @Test
    public void testApplySHA256_Deterministic()
    {
        String hash1 = StringUtil.applySHA256("test");
        String hash2 = StringUtil.applySHA256("test");
        
        assertEquals(hash1, hash2, "Same input should always produce the same hash");
    }
    
    /*
     * Testing if applySHA256() returns
     * different hashes for different inputs
     */
    @Test
    public void testApplySHA256_UniqueForDifferentInputs()
    {
        String hash1 = StringUtil.applySHA256("test1");
        String hash2 = StringUtil.applySHA256("test2");
        
        assertNotEquals(hash1, hash2, "Different inputs should produce different hashes");
    }
    
    /*
     * Testing if applySHA256() throws
     * RuntimeException when input is null
     */
    @Test
    public void testApplySHA256_NullInputThrowsException()
    {
        assertThrows(RuntimeException.class, () -> 
        {
            StringUtil.applySHA256(null);
        }, "Should throw RuntimeException when input is null");
    }
    
    /*
     * Testing applySHA256() correctness
     * against known SHA-256 hash values
     */
    @Test
    public void testApplySHA256_KnownHashValues()
    {
        // Test with known SHA-256 input-output pairs
        String hash1 = StringUtil.applySHA256("hi");
        String hash2 = StringUtil.applySHA256("hello");
        String hash3 = StringUtil.applySHA256("test");
        String hash4 = StringUtil.applySHA256("blockchain");
        String hash5 = StringUtil.applySHA256("The quick brown fox jumps over the lazy dog");

        // Known SHA-256 hash values for the inputs above
        assertEquals("8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4", hash1);
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", hash2);
        assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", hash3);
        assertEquals("ef7797e13d3a75526946a3bcf00daec9fc9c9c4d51ddc7cc5df888f74dd434d1", hash4);
        assertEquals("d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592", hash5);
    }
    
    /*
     * Testing applySHA256() with empty string input
     */
    @Test
    public void testApplySHA256_EmptyStringInput()
    {
        String hash = StringUtil.applySHA256("");
        
        assertNotNull(hash, "Hash of empty string should not be null");
        assertEquals(64, hash.length(), "Hash of empty string should still be 64 characters");
        // Known SHA-256 hash of empty string
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }
    
    /*
     * Testing applySHA256() with very long input
     */
    @Test
    public void testApplySHA256_LongInput()
    {
        StringBuilder longInput = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longInput.append("a");
        }
        
        String hash = StringUtil.applySHA256(longInput.toString());
        
        assertNotNull(hash, "Hash of long input should not be null");
        assertEquals(64, hash.length(), "Hash of long input should still be 64 characters");
        assertTrue(hash.matches("[0-9a-f]+"), "Hash should contain only hexadecimal characters");
    }

    // =================== DIFFICULTY STRING TESTS ===================
    
    /*
     * Testing getDifficultyString() with various difficulty levels
     */
    @Test
    public void testGetDifficultyString_VariousDifficulties()
    {
        assertEquals("", StringUtil.getDificultyString(0));
        assertEquals("0", StringUtil.getDificultyString(1));
        assertEquals("00", StringUtil.getDificultyString(2));
        assertEquals("000", StringUtil.getDificultyString(3));
        assertEquals("0000", StringUtil.getDificultyString(4));
        assertEquals("00000", StringUtil.getDificultyString(5));
    }
    
    /*
     * Testing getDifficultyString() length matches difficulty
     */
    @Test
    public void testGetDifficultyString_CorrectLength()
    {
        for (int difficulty = 0; difficulty <= 10; difficulty++) {
            String difficultyString = StringUtil.getDificultyString(difficulty);
            assertEquals(difficulty, difficultyString.length(), 
                "Difficulty string length should match difficulty level");
        }
    }
    
    /*
     * Testing getDifficultyString() contains only zeros
     */
    @Test
    public void testGetDifficultyString_OnlyZeros()
    {
        String difficultyString = StringUtil.getDificultyString(5);
        assertTrue(difficultyString.matches("0*"), "Difficulty string should contain only zeros");
    }

    // =================== KEY CONVERSION TESTS ===================
    
    /*
     * Testing getStringFromKey() with public key
     */
    @Test
    public void testGetStringFromKey_PublicKey()
    {
        String keyString = StringUtil.getStringFromKey(testPublicKey);
        
        assertNotNull(keyString, "Key string should not be null");
        assertFalse(keyString.isEmpty(), "Key string should not be empty");
        
        // Verify it's valid Base64
        assertDoesNotThrow(() -> {
            Base64.getDecoder().decode(keyString);
        }, "Key string should be valid Base64");
    }
    
    /*
     * Testing getStringFromKey() with private key
     */
    @Test
    public void testGetStringFromKey_PrivateKey()
    {
        String keyString = StringUtil.getStringFromKey(testPrivateKey);
        
        assertNotNull(keyString, "Key string should not be null");
        assertFalse(keyString.isEmpty(), "Key string should not be empty");
        
        // Verify it's valid Base64
        assertDoesNotThrow(() -> {
            Base64.getDecoder().decode(keyString);
        }, "Key string should be valid Base64");
    }
    
    /*
     * Testing getStringFromKey() consistency
     */
    @Test
    public void testGetStringFromKey_Consistency()
    {
        String keyString1 = StringUtil.getStringFromKey(testPublicKey);
        String keyString2 = StringUtil.getStringFromKey(testPublicKey);
        
        assertEquals(keyString1, keyString2, "Same key should always produce same string representation");
    }

    // =================== ECDSA SIGNATURE TESTS ===================
    
    /*
     * Testing applyECDSASig() returns non-null signature
     */
    @Test
    public void testApplyECDSASig_NotNull()
    {
        String testData = "test message";
        byte[] signature = StringUtil.applyECDSASig(testPrivateKey, testData);
        
        assertNotNull(signature, "Signature should not be null");
        assertTrue(signature.length > 0, "Signature should not be empty");
    }
    
    /*
     * Testing verifyECDSASig() with valid signature
     */
    @Test
    public void testVerifyECDSASig_ValidSignature()
    {
        String testData = "test message for signing";
        byte[] signature = StringUtil.applyECDSASig(testPrivateKey, testData);
        
        boolean isValid = StringUtil.verifyECDSASig(testPublicKey, testData, signature);
        assertTrue(isValid, "Valid signature should be verified as true");
    }
    
    /*
     * Testing verifyECDSASig() with invalid signature
     */
    @Test
    public void testVerifyECDSASig_InvalidSignature()
    {
        String testData = "test message";
        String differentData = "different message";
        byte[] signature = StringUtil.applyECDSASig(testPrivateKey, testData);
        
        boolean isValid = StringUtil.verifyECDSASig(testPublicKey, differentData, signature);
        assertFalse(isValid, "Signature for different data should be invalid");
    }
      /*
     * Testing verifyECDSASig() with tampered signature
     */
    @Test
    public void testVerifyECDSASig_TamperedSignature()
    {
        String testData = "test message";
        byte[] signature = StringUtil.applyECDSASig(testPrivateKey, testData);
        
        // Tamper with the signature by creating a completely invalid signature
        byte[] tamperedSignature = new byte[signature.length];
        for (int i = 0; i < tamperedSignature.length; i++) {
            tamperedSignature[i] = (byte) (i % 256); // Fill with predictable but invalid data
        }
        
        // This should either return false or throw an exception (both are acceptable for invalid signatures)
        assertThrows(RuntimeException.class, () -> {
            StringUtil.verifyECDSASig(testPublicKey, testData, tamperedSignature);
        }, "Tampered signature should throw exception or return false");
    }
    
    /*
     * Testing ECDSA signature uniqueness
     */
    @Test
    public void testApplyECDSASig_Uniqueness()
    {
        String testData = "test message";
        byte[] signature1 = StringUtil.applyECDSASig(testPrivateKey, testData);
        byte[] signature2 = StringUtil.applyECDSASig(testPrivateKey, testData);
        
        // ECDSA signatures should be different each time due to randomness
        assertFalse(java.util.Arrays.equals(signature1, signature2), 
            "ECDSA signatures should be unique due to randomness");
        
        // But both should still be valid
        assertTrue(StringUtil.verifyECDSASig(testPublicKey, testData, signature1));
        assertTrue(StringUtil.verifyECDSASig(testPublicKey, testData, signature2));
    }

    // =================== MERKLE ROOT TESTS ===================
    
    /*
     * Testing getMerkleRoot() with empty transaction list
     */
    @Test
    public void testGetMerkleRoot_EmptyList()
    {
        ArrayList<Transaction> emptyTransactions = new ArrayList<>();
        String merkleRoot = StringUtil.getMerkleRoot(emptyTransactions);
        
        assertEquals("", merkleRoot, "Merkle root of empty list should be empty string");
    }
    
    /*
     * Testing getMerkleRoot() with single transaction
     */
    @Test
    public void testGetMerkleRoot_SingleTransaction()
    {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction tx = new Transaction(wallet1.publicKey, wallet2.publicKey, 50f, null);
        tx.transactionId = "test_transaction_id";
        transactions.add(tx);
        
        String merkleRoot = StringUtil.getMerkleRoot(transactions);
        
        assertNotNull(merkleRoot, "Merkle root should not be null");
        assertEquals("test_transaction_id", merkleRoot, "Single transaction merkle root should be the transaction ID");
    }
    
    /*
     * Testing getMerkleRoot() with multiple transactions
     */
    @Test
    public void testGetMerkleRoot_MultipleTransactions()
    {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        Transaction tx1 = new Transaction(wallet1.publicKey, wallet2.publicKey, 50f, null);
        tx1.transactionId = "tx1";
        transactions.add(tx1);
        
        Transaction tx2 = new Transaction(wallet2.publicKey, wallet1.publicKey, 25f, null);
        tx2.transactionId = "tx2";
        transactions.add(tx2);
        
        String merkleRoot = StringUtil.getMerkleRoot(transactions);
        
        assertNotNull(merkleRoot, "Merkle root should not be null");
        assertFalse(merkleRoot.isEmpty(), "Merkle root should not be empty");
        assertEquals(64, merkleRoot.length(), "Merkle root should be a SHA-256 hash (64 characters)");
        
        // Verify it's a valid hash by checking it contains only hex characters
        assertTrue(merkleRoot.matches("[0-9a-f]+"), "Merkle root should be a valid hexadecimal hash");
    }
    
    /*
     * Testing getMerkleRoot() deterministic behavior
     */
    @Test
    public void testGetMerkleRoot_Deterministic()
    {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        ArrayList<Transaction> transactions2 = new ArrayList<>();
        
        // Create identical transaction lists
        Transaction tx1a = new Transaction(wallet1.publicKey, wallet2.publicKey, 50f, null);
        tx1a.transactionId = "tx1";
        transactions1.add(tx1a);
        
        Transaction tx1b = new Transaction(wallet1.publicKey, wallet2.publicKey, 50f, null);
        tx1b.transactionId = "tx1";
        transactions2.add(tx1b);
        
        String merkleRoot1 = StringUtil.getMerkleRoot(transactions1);
        String merkleRoot2 = StringUtil.getMerkleRoot(transactions2);
        
        assertEquals(merkleRoot1, merkleRoot2, "Same transactions should produce same merkle root");
    }
    
    /*
     * Testing getMerkleRoot() with different transaction orders
     */
    @Test
    public void testGetMerkleRoot_OrderSensitive()
    {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        ArrayList<Transaction> transactions2 = new ArrayList<>();
        
        Transaction txA = new Transaction(wallet1.publicKey, wallet2.publicKey, 50f, null);
        txA.transactionId = "txA";
        
        Transaction txB = new Transaction(wallet2.publicKey, wallet1.publicKey, 25f, null);
        txB.transactionId = "txB";
        
        // Add in different orders
        transactions1.add(txA);
        transactions1.add(txB);
        
        transactions2.add(txB);
        transactions2.add(txA);
        
        String merkleRoot1 = StringUtil.getMerkleRoot(transactions1);
        String merkleRoot2 = StringUtil.getMerkleRoot(transactions2);
        
        assertNotEquals(merkleRoot1, merkleRoot2, "Different transaction orders should produce different merkle roots");
    }
}