package myBlockchain.Blockchain;

import java.util.ArrayList;
import java.util.Date;

import myBlockchain.Transactions.Transaction;
import myBlockchain.Util.StringUtil;

public class Block 
{
    public String hash; // This block's hash
    public String previousHash; // Previous block's hash - creates the chain
    private long timeStamp; // When this block was created
    private int nonce; // Number used in mining process
    public String merkleRoot; // Root of the merkle tree of transactions
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // Transactions in this block
    
    public Block(String previousHash)
    {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }
    // Calculates the hash for this block
    public String calculateHash()
    {
        String calculatedHash = StringUtil.applySHA256(
            previousHash +
            Long.toString(timeStamp) +
            Integer.toString(nonce) +
            merkleRoot
            );
        return calculatedHash;
    }
    // Mines the block by finding a hash with the required difficulty
	public void mineBlock(int difficulty) 
    {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = StringUtil.getDificultyString(difficulty);
        // Keep trying different nonce values until hash meets difficulty requirement
		while(!hash.substring( 0, difficulty).equals(target)) 
        {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
    // Adds a transaction to this block after validation
    public boolean addTransaction(Transaction transaction) 
    {
		if(transaction == null) return false;		
        // Skip processing for genesis block
		if((previousHash != "0")) 
        {
			if((transaction.processTransaction() != true)) 
            {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}
