package myBlockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder; //Objects to JSON

public class myBlockchain
{
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 7;
    public static void main(String[] args) 
    {
        blockchain.add(new Block("First", "0"));
        System.out.println("Trying to MINE the first block...");
        blockchain.get(0).mineBlock(difficulty);
        blockchain.add(new Block("Second", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to MINE the second block...");
        blockchain.get(1).mineBlock(difficulty);
        blockchain.add(new Block("Third", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to MINE the third block...");
        blockchain.get(2).mineBlock(difficulty);
        
        System.out.println("\nBlockchain is valid: " +isChainValid());

        String blockChainJSON = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\n The blockchain:");
        System.out.println(blockChainJSON);
    }
    //Checks if the blockchain is valid
    public static Boolean isChainValid()
    {
        Block current;
        Block previous;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i=1; i<blockchain.size(); ++i)
        {
            current = blockchain.get(i);
            previous = blockchain.get(i-1);

            if(!current.hash.equals(current.calculateHash()))
            {
                System.err.println("Current Hashes are not equal");
                return false;
            }
            if(!previous.hash.equals(current.previousHash))
            {
                System.err.println("Previous Hashes are not equal");
                return false;
            }

            if(!current.hash.substring( 0, difficulty).equals(hashTarget)) 
            {
			    System.out.println("This block hasn't been mined");
				return false;
			}
        }
        return true;
    }
}
