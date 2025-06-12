package myBlockchain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StringUtilTest
{
    /* 
     * Testing if applySHA256() returns
     * a non-null, non-empty string
    */    @Test
    public void test1()
    {
        String test = StringUtil.applySHA256("test");
        
        assertNotNull(test, "Hash should not be null");
        assertFalse(test.isEmpty(), "Hash should not be empty");
    }    
    /*
     * Testing if applySHA256() returns
     * a 64 character long string
     */
    @Test
    public void test2()
    {
        String hash = StringUtil.applySHA256("test");
        
        assertEquals(64, hash.length(), "SHA-256 hash should be exactly 64 characters long");
    }    
    /*
     * Testing if applySHA256() returns
     * a string that only contains 
     * characters from (0-9),(a,f)
     * (hexadecimal characters)
     */
    @Test
    public void test3()
    {
        String hash = StringUtil.applySHA256("test");
        
        assertTrue(hash.matches("[0-9a-f]+"), "Hash should contain only hexadecimal characters (0-9, a-f)");
    }
    /*
     * Testing if applySHA256() returns
     * the same hash for the same input
     */
    @Test
    public void test4()
    {
        String hash1 = StringUtil.applySHA256("test");
        String hash2 = StringUtil.applySHA256("test");
        
        assertEquals(hash1, hash2);
    }
    /*
     * Testing if applySHA256() returns
     * the different hash for the different input
     */
    @Test
    public void test5()
    {
        String hash1 = StringUtil.applySHA256("test1");
        String hash2 = StringUtil.applySHA256("test2");
        
        assertNotEquals(hash1, hash2);
    }    
    /*
     * Testing if applySHA256() throws
     * exception when input is null
     */
    @Test
    public void test6()
    {
        assertThrows(RuntimeException.class, () -> 
        {
            StringUtil.applySHA256(null);
        }, "Should throw RuntimeException when input is null");
    }    /*
     * Testing applySHA256() correctness
     * for known inputs
     */
    @Test
    public void test7()
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
}