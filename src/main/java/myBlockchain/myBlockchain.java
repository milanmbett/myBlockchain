package myBlockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.GsonBuilder; //Objects to JSON

import myBlockchain.Blockchain.Block;
import myBlockchain.Transactions.Transaction;
import myBlockchain.Transactions.TransactionInput;
import myBlockchain.Transactions.TransactionOutput;
import myBlockchain.Transactions.Wallet;

public class myBlockchain
{
    // The main blockchain storage - list of all blocks
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    // Unspent transaction outputs - tracks available funds
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
    // Mining difficulty - number of leading zeros required
	public static int difficulty = 3;
    // Minimum amount for a valid transaction
	public static float minimumTransaction = 0.1f;
    // Test wallets for demonstration
	public static Wallet walletA;
	public static Wallet walletB;
    // First transaction to initialize the blockchain
	public static Transaction genesisTransaction;

	public static void main(String[] args) 
    {	
        // Setup cryptographic provider for ECDSA
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
        // Create test wallets
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();
		
        // Create genesis transaction to give initial funds to walletA
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	
		genesisTransaction.transactionId = "0";
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciever, genesisTransaction.value, genesisTransaction.transactionId));
        // Add genesis transaction output to UTXOs
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
        // Create and mine the genesis block
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
        // Test transaction: walletA sends 40 to walletB
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
        // Test invalid transaction: walletA tries to send more than it has
		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
        // Test transaction: walletB sends 20 back to walletA
		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
        // Validate the entire blockchain
		isChainValid();
		
	}
	
    // Validates the integrity of the entire blockchain
	public static Boolean isChainValid() 
    {
		Block currentBlock; 
		Block previousBlock;
        // Create target string for difficulty check
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        // Temporary UTXO map to track transaction validity
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>();
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
        // Check each block in the chain
		for(int i=1; i < blockchain.size(); i++) 
        {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
            // Verify block's hash is correct
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) )
            {
				System.out.println("#Current Hashes not equal");
				return false;
			}
            // Verify chain links are correct
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) 
            {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
            // Verify block was properly mined
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) 
            {
				System.out.println("#This block hasn't been mined");
				return false;
			}
            // Validate all transactions in the block
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) 
            {
				Transaction currentTransaction = currentBlock.transactions.get(t);
                // Verify transaction signature
				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
                // Verify input and output values match
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) 
                {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}
                // Validate all transaction inputs
				for(TransactionInput input: currentTransaction.inputs) 
                {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
                    // Remove spent UTXO
					tempUTXOs.remove(input.transactionOutputId);
				}
                // Add new outputs to UTXO set
				for(TransactionOutput output: currentTransaction.outputs) 
                {
					tempUTXOs.put(output.id, output);
				}
				
                // Verify transaction outputs go to correct recipients
				if( currentTransaction.outputs.get(0).reciever != currentTransaction.reciever) 
                {
					System.out.println("#Transaction(" + t + ") output reciever is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciever != currentTransaction.sender) 
                {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
			}	
		}
		System.out.println("Blockchain is valid");
		return true;
	}
    // Mines a new block and adds it to the blockchain
	public static void addBlock(Block newBlock) 
    {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
