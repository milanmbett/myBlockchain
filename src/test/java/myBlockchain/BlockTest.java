package myBlockchain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlockTest 
{    
    /*
     * Testing if Block constructor creates
     * a block with correct data and previousHash
     */
    @Test
    public void test1()
    {
        String testData = "Test Block Data";
        String testPreviousHash = "0000000000000000000000000000000000000000000000000000000000000000";
        
        Block block = new Block(testData, testPreviousHash);
        
        assertEquals(testPreviousHash, block.previousHash, "Previous hash should match constructor input");
        assertNotNull(block.hash, "Hash should be calculated and not null");
    }    
    /*
     * Testing if Block constructor sets
     * a reasonable timestamp (close to current time)
     */
    @Test
    public void test2()
    {
        long beforeCreation = System.currentTimeMillis();
        Block block = new Block("Test Data", "0");
        long afterCreation = System.currentTimeMillis();
        
        assertTrue(afterCreation >= beforeCreation, "Block should be created within reasonable time");
        
        try 
        {
            Thread.sleep(1);
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
        
        Block block2 = new Block("Test Data", "0");
        assertNotEquals(block.hash, block2.hash, "Blocks created at different times should have different hashes");
    }    
    /*
     * Testing if Block constructor calculates
     * and sets a non-null, non-empty hash
     */
    @Test
    public void test3()
    {
        Block block = new Block("Test Data", "Previous Hash");
        
        assertNotNull(block.hash, "Hash should not be null");
        assertFalse(block.hash.isEmpty(), "Hash should not be empty");
        assertEquals(64, block.hash.length(), "Hash should be 64 characters long (SHA-256)");
        assertTrue(block.hash.matches("[0-9a-f]+"), "Hash should contain only hexadecimal characters");
    }    
    /*
     * Testing if two blocks with same data and previousHash
     * but different timestamps have different hashes
     */
    @Test
    public void test4()
    {
        Block block1 = new Block("Same Data", "Same Previous Hash");
        
        try 
        {
            Thread.sleep(1);
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
        
        Block block2 = new Block("Same Data", "Same Previous Hash");
        
        assertNotEquals(block1.hash, block2.hash, "Blocks with same data but different timestamps should have different hashes");
    }    
    /*
     * Testing if blocks with different data
     * have different hashes
     */
    @Test
    public void test5()
    {
        Block block1 = new Block("Data 1", "Same Previous Hash");
        Block block2 = new Block("Data 2", "Same Previous Hash");
        
        assertNotEquals(block1.hash, block2.hash, "Blocks with different data should have different hashes");
    }    
    /*
     * Testing if blocks with different previousHash
     * have different hashes
     */
    @Test
    public void test6()
    {
        Block block1 = new Block("Same Data", "Previous Hash 1");
        Block block2 = new Block("Same Data", "Previous Hash 2");
        
        assertNotEquals(block1.hash, block2.hash, "Blocks with different previous hashes should have different hashes");
    }    
    /*
     * Testing blockchain integrity:
     * creating a chain where each block's hash
     * becomes the next block's previousHash
     */
    @Test
    public void test7()
    {
        Block genesisBlock = new Block("Genesis Block", "0");
        Block secondBlock = new Block("Second Block", genesisBlock.hash);
        assertEquals(genesisBlock.hash, secondBlock.previousHash, "Second block's previousHash should match genesis block's hash");
        
        Block thirdBlock = new Block("Third Block", secondBlock.hash);
        assertEquals(secondBlock.hash, thirdBlock.previousHash, "Third block's previousHash should match second block's hash");
        assertNotEquals(genesisBlock.hash, secondBlock.hash, "Genesis and second block should have different hashes");
        assertNotEquals(secondBlock.hash, thirdBlock.hash, "Second and third block should have different hashes");
        assertNotEquals(genesisBlock.hash, thirdBlock.hash, "Genesis and third block should have different hashes");
    } 
    /*
     * Testing Block creation with null data
     * should not crash and handle gracefully
     */
    @Test
    public void test8()
    {
        try 
        {
            Block block = new Block(null, "Previous Hash");
            assertNotNull(block.hash, "Hash should still be calculated even with null data");
            assertNotNull(block.previousHash, "Previous hash should be set");
        } 
        catch (Exception e) 
        {
            assertTrue(e instanceof RuntimeException, "Should throw RuntimeException when data is null");
        }
    }
}
